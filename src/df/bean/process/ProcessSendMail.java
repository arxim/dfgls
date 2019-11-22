package df.bean.process;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.mail.smtp.SMTPSendFailedException;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class ProcessSendMail {
	final static Logger logger = Logger.getLogger(ProcessSendMail.class);
	private DBConnection conn = null;
	
	public ProcessSendMail(DBConnection conn) {
        this.conn = conn;
    }
	
	public boolean SendMail(String hospitalCode, String year, String month, String path, String reportName) {
		boolean result = true;
		boolean sendMailSuccess = false;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        HashMap hm = new HashMap();
        String reportNameInEmail = "";
        String taxTerm = "";
		String getEmail = "";
		String subject = "";
		String doctorName = "";
		
        if(reportName.contains("PaymentVoucher")) {
        	if(month.equals("01")) {
        		subject = "Payment Voucher for January "+year;
        	}
        	else if(month.equals("02")) {
        		subject = "Payment Voucher for February "+year;
        	}
			else if(month.equals("03")) {
				subject = "Payment Voucher for March "+year;
			}
			else if(month.equals("04")) {
				subject = "Payment Voucher for April "+year;
			}
			else if(month.equals("05")) {
				subject = "Payment Voucher for May "+year;
			}
			else if(month.equals("06")) {
				subject = "Payment Voucher for June "+year;
			}
			else if(month.equals("07")) {
				subject = "Payment Voucher for July "+year;
			}
			else if(month.equals("08")) {
				subject = "Payment Voucher for August "+year;
			}
			else if(month.equals("09")) {
				subject = "Payment Voucher for September "+year;
			}
			else if(month.equals("10")) {
				subject = "Payment Voucher for October "+year;
			}
			else if(month.equals("11")) {
				subject = "Payment Voucher for November "+year;
			}
			else if(month.equals("12")) {
				subject = "Payment Voucher for December "+year;
			}
        	reportName = reportName+hospitalCode;
        	reportNameInEmail = "PaymentVoucher_"+year+month;
        	getEmail = getPaymentVoucherEmail(hospitalCode, year, month);
        }
        else if(reportName.contains("TaxLetter406")) {
        	getEmail = getTax406Email(hospitalCode, year, month);
        	if(month.equals("01")) {
        		taxTerm = "FirstTerm";
        		subject = "Tax 40(6) Report for First Term "+year;
        	}
        	else if(month.equals("06")) {
        		taxTerm = "SecondTerm";
        		subject = "Tax 40(6) Report for Second Term "+year;
        	}
        	else if(month.equals("12")) {
        		taxTerm = "Yearly";
        		subject = "Tax 40(6) Report for Yearly "+year;
        	}
        	reportNameInEmail = "TaxLetter_"+taxTerm+year;
        }
        else if(reportName.contains("Tax402")) {
        	getEmail = getTax402Email(hospitalCode, year);
        	reportNameInEmail = "WithholdingTaxCertificate_"+year;
        	subject = "Withholding Tax Certificate for "+year;
        }
        
        ResultSet rs = conn.executeQuery(getEmail);
        String pathName = conn.executeQueryString("SELECT VALUE FROM CONFIG WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND NAME = 'IP_UPLOAD_FILE'");
        System.out.println(path+"\\");
        try {
			while (rs.next()) {
				if(reportName.contains("PaymentVoucher")) {
		        	hm.put("month", month);
		        	hm.put("from_doctor", rs.getString("DOCTOR_CODE"));
		        	hm.put("to_doctor", rs.getString("DOCTOR_CODE"));
		        	hm.put("SUBREPORT_DIR", path+"SummaryDFUnpaidSubreport.jasper");
		        }
		        else if(reportName.contains("Tax402")) {
		        	hm.put("doctor", rs.getString("DOCTOR_CODE"));
		        }
		        else if(reportName.contains("TaxLetter406")) {
		        	hm.put("term", month);
		        	hm.put("doctor_code", rs.getString("DOCTOR_CODE"));
		        }
	        	hm.put("year", year);
				hm.put("hospital_code", hospitalCode);
	        	hm.put("hospital_logo", path+"\\");
	        	
	        	doctorName = rs.getString("NAME_THAI");
	        	System.out.println(reportName);
	        	if(generatePDF(hm, path+"\\", reportName, rs.getString("TAX_ID"))) {
	        		sendMailSuccess = sendMail(rs.getString("EMAIL"), path + "\\output\\" + reportName+"_encrypted.pdf", reportNameInEmail, subject, doctorName);
	        	}
	        	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			result = false;
		}
        
        return result;
	}
	
	public boolean generatePDF(HashMap hm, String path, String reportName, String password) {
		DBConn connection = new DBConn();
		boolean isSuccess = false;
        try {
        	
        	connection.setStatement();
        	Map map = hm;
        	String pw = password;
        	String filePath = path+"\\";
        	//logger.info(pw);

	        // fills compiled report with parameters and a connection
	        JasperPrint print = JasperFillManager.fillReport(path + reportName + ".jasper", map, connection.getConnection());
	
	        // exports report to pdf
	        JRExporter exporter = new JRPdfExporter();
	        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
	        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream(path + "output\\" + reportName + ".pdf")); // your output goes here
	        exporter.exportReport();
	
	        isSuccess = manipulatePdf(path + "output\\" + reportName+".pdf", path + "output\\" + reportName+"_encrypted.pdf", password);
	        
	    } catch (Exception e) {
//	        throw new RuntimeException("It's not possible to generate the pdf report.", e);
	    	logger.error("Exception: "+e);
	    	isSuccess = false;
	    } finally {
	        // it's your responsibility to close the connection, don't forget it!
	        if (connection != null) {
	            try {
	                connection.closeDB("TestGenPdf");
	            } catch (Exception e) {
	            }
	        }
	    }
        return isSuccess;
	}
	
	public boolean manipulatePdf(String src, String dest, String password) throws IOException, DocumentException {
//    	
		boolean isEncrypted = false;
		logger.info(src);
		logger.info(dest);
		
		try {
					
			PdfReader reader = new PdfReader(src);
			PdfEncryptor.encrypt(reader, new FileOutputStream(dest),
	            password.getBytes(), "Df@dm1n".getBytes(), PdfWriter.AllowDegradedPrinting,
	            PdfWriter.STRENGTH128BITS);
			
			logger.info("Successfully Done");
			isEncrypted = true;
		} catch (Exception e) {

			logger.error(e);
			isEncrypted = false;
		}
		return isEncrypted;
    }
	
	public boolean sendMail(String emailTo, String filePath, String reportName, String subject, String doctorName) {
		boolean sendSuccess = false;
		// Recipient's email ID needs to be mentioned.
		String to = emailTo;
		String body = "";
		String report = "";
		
		if(reportName.contains("TaxLetter")) {
			report = "หนังสือรับรองรายได้  (ภาษี 40(6))";
		}
		else if(reportName.contains("WithholdingTax")) {
			report = "หนังสือรับรอง 50 ทวิ ";
		}
		body = "เรียน "+doctorName+"\n\n";
		body += "\t ระบบ Doctor fee ได้จัดส่งรายงาน"+report+" มาพร้อมอีเมลฉบับนี้\n";
		body += "เพื่อความปลอดภัยของข้อมูลกรุณาระบุรหัสผ่าน (Password) เพื่อเรียกดู"+report+"\n";
		body += "รหัสผ่านของท่าน คือ เลขประจำตัวผู้เสียภาษีอากร (TAX ID) \n\n\n";
		body += "หมายเหตุ\n";
		body += "\t หากท่านมีความประสงค์จะเปลี่ยนแปลงที่อยู่อีเมลในการรับข้อมูล ท่านสามารถแจ้งความประสงค์ได้ที่ MAO\n";
		body += "\t อีเมลฉบับนี้ถูกส่งด้วยระบบอัตโนมัติ โปรดกรุณาอย่าตอบกลับผ่านอีเมลนี้";
		
		logger.info(reportName+", "+subject+", "+doctorName);
		// Sender's email ID needs to be mentioned
		String from = "doctorfee-noreply@glsict.com";
		
		// Assuming you are sending email from localhost 172.18.22.5
		//String host = "172.18.22.5";
		String host = "DC-EXCHC.BDMS.CO.TH";
		
		// Get system properties
		Properties properties = System.getProperties();
		
		// Setup mail server
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", "25");
//		properties.setProperty("mail.smtp.auth", "true");
//		
//		Authenticator authenticator = new Authenticator()
//        {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication()
//            {
//                return new PasswordAuthentication( user, password );
//            }
//        };

		
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			
			// Set Subject: header field
			message.setSubject(subject);
			
			// Create the message part 
			BodyPart messageBodyPart = new MimeBodyPart();
			// Fill the message
			messageBodyPart.setContent(body, "text/plain; charset=utf-8");
			//messageBodyPart.setText(""+body);
			
			// Create a multipar message
			Multipart multipart = new MimeMultipart();
			
			// Set text message part
			multipart.addBodyPart(messageBodyPart);
			
			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
//			String filename = filePath;
			DataSource source = new FileDataSource(filePath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(reportName+".pdf");
			multipart.addBodyPart(messageBodyPart);
			
			// Send the complete message parts
			message.setContent(multipart );
			
			// Send message
			Transport.send(message);
			logger.info("Sent message successfully....");
			sendSuccess = true;
		} catch (MessagingException mex) {
			mex.printStackTrace();
			logger.error(mex.getMessage());
			sendSuccess = false;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			sendSuccess = false;
		}
		return sendSuccess;
	}
	
	public String getPaymentVoucherEmail(String hospital_code, String year, String month) {
		return "SELECT PM.DOCTOR_CODE, DR.NAME_THAI, DR.EMAIL, DR.TAX_ID "
				+ "FROM SUMMARY_PAYMENT PM "
				+ "LEFT OUTER JOIN DOCTOR DR ON (PM.DOCTOR_CODE = DR.CODE AND PM.HOSPITAL_CODE = DR.HOSPITAL_CODE) "
				+ "LEFT OUTER JOIN DOCTOR_PROFILE DP ON (DR.DOCTOR_PROFILE_CODE = DP.CODE AND DR.HOSPITAL_CODE = DP.HOSPITAL_CODE) "
				+ "LEFT OUTER JOIN BANK BK ON (DR.BANK_CODE = BK.CODE) "
				+ "LEFT OUTER JOIN PAYMENT_MODE PY ON PM.PAYMENT_MODE_CODE = PY.CODE "
				+ "LEFT OUTER JOIN VW_EXPENSE_VOUCHER SG ON (PM.DOCTOR_CODE = SG.DOCTOR_CODE AND PM.HOSPITAL_CODE = SG.HOSPITAL_CODE "
				+ "AND PM.YYYY+PM.MM = SG.YYYY+SG.MM) "
				+ "WHERE PM.HOSPITAL_CODE = '"+hospital_code+"' "
				+ "AND PM.YYYY = '"+year+"' AND PM.MM = '"+month+"' "
//				+"--AND (DR.DOCTOR_PROFILE_CODE BETWEEN '19URO009' AND '19URO009')\r\n" + 
				+ "AND PM.PAYMENT_MODE_CODE <> 'U' "
				+ "AND PM.DR_NET_PAID_AMT <> 0 "
				+ "AND LEN(PM.PAYMENT_DATE) = 8 "
				+ "AND (DR.EMAIL <> '' AND DR.EMAIL IS NOT NULL) "
//				+ "AND DR.EMAIL LIKE '%glsict.com'"
				+ "ORDER BY DR.DOCTOR_PROFILE_CODE, DR.CODE";
	}
	
	public String getTax406Email(String hospital_code, String year, String month) {
		return "SELECT ST.DOCTOR_CODE, DR.NAME_THAI, DR.EMAIL, DR.TAX_ID "
				+ "FROM SUMMARY_TAX_406 ST "
				+ "LEFT OUTER JOIN DOCTOR DR ON ST.DOCTOR_CODE = DR.CODE "
				+ "AND ST.HOSPITAL_CODE = DR.HOSPITAL_CODE "
				+ "LEFT OUTER JOIN DOCTOR_PROFILE DP ON DR.DOCTOR_PROFILE_CODE = DP.CODE "
				+ "AND DR.HOSPITAL_CODE = DP.HOSPITAL_CODE "
				+ "LEFT OUTER JOIN HOSPITAL HOS ON ST.HOSPITAL_CODE = HOS.CODE "
				+ "WHERE ST.HOSPITAL_CODE = '"+hospital_code+"' "
				+ "AND YYYY = '"+year+"' "
				+ "AND MM = '"+month+"' "
				+ "AND SUM_TAX_DR_AMT > 0 "
				+ "AND PRINT_DATE <> '' "
				+ "AND (DR.EMAIL <> '' AND DR.EMAIL IS NOT NULL) "
//				+ "AND DR.EMAIL LIKE '%glsict.com' "
				+ "ORDER BY DR.DOCTOR_PROFILE_CODE, DR.CODE";
	}
	
	public String getTax402Email(String hospital_code, String year) {
		return "SELECT TAX.DOCTOR_CODE, DR.NAME_THAI, DR.EMAIL, DR.TAX_ID " 
				+ "FROM HOSPITAL LEFT OUTER JOIN SUMMARY_TAX_402 TAX ON HOSPITAL.CODE = TAX.HOSPITAL_CODE "
				+ "LEFT OUTER JOIN DOCTOR DR ON TAX.DOCTOR_CODE = DR.CODE AND TAX.HOSPITAL_CODE = DR.HOSPITAL_CODE "
				+ "WHERE TAX.HOSPITAL_CODE = '"+hospital_code+"' "
				+ "AND TAX.SUM_NORMAL_TAX_AMT > 0 "
				+ "AND TAX.MM = '13' AND DR.PAYMENT_MODE_CODE != 'U' "
				+ "AND TAX.YYYY = '"+year+"' "
				+ "AND TAX.PRINT_DATE <> '' "
				+ "AND (DR.EMAIL <> '' AND DR.EMAIL IS NOT NULL) ";
//				+ "AND DR.EMAIL LIKE '%glsict.com'";
	}

}
