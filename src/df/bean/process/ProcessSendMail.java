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
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.itextpdf.text.DocumentException;
import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class ProcessSendMail {
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
		
        if(reportName.contains("PaymentVoucher")) {
        	reportName = reportName+hospitalCode;
        	reportNameInEmail = "PaymentVoucher_"+year+month;
        	getEmail = getPaymentVoucherEmail(hospitalCode, year, month);
        }
        else if(reportName.contains("TaxLetter406")) {
        	getEmail = getTax406Email(hospitalCode, year, month);
        	if(month.equals("01")) {
        		taxTerm = "FirstTerm";
        	}
        	else if(month.equals("06")) {
        		taxTerm = "SecondTerm";
        	}
        	else if(month.equals("12")) {
        		taxTerm = "Yearly";
        	}
        	reportNameInEmail = "TaxLetter_"+taxTerm+year;
        }
        else if(reportName.contains("Tax402")) {
        	getEmail = getTax402Email(hospitalCode, year);
        	reportNameInEmail = "WithholdingTaxCertificate_"+year;
        }
        
        ResultSet rs = conn.executeQuery(getEmail);
        String pathName = conn.executeQueryString("SELECT VALUE FROM CONFIG WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND NAME = 'IP_UPLOAD_FILE'");
        try {
			while (rs.next()) {
				if(reportName.contains("PaymentVoucher")) {
		        	hm.put("month", month);
		        	hm.put("from_doctor", rs.getString("DOCTOR_CODE"));
		        	hm.put("to_doctor", rs.getString("DOCTOR_CODE"));
		        	hm.put("SUBREPORT_DIR", path+"SummaryDFUnpaidSubreport.jasper");
		        }
		        else if(reportName.contains("TaxLetter406") || reportName.contains("Tax402")) {
		        	hm.put("doctor", rs.getString("DOCTOR_CODE"));
		        }
	        	hm.put("year", year);
				hm.put("hospital_code", hospitalCode);
	        	hm.put("hospital_logo", path+"\\");
	        	
	        	if(generatePDF(hm, path+"\\", reportName, rs.getString("TAX_ID"))) {
	        		sendMailSuccess = sendMail(rs.getString("EMAIL"), path + "\\output\\" + reportName+"_encrypted.pdf", reportName);
	        	}
	        	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
        	System.out.println(pw);

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
	    	System.out.println("Exception: "+e);
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
		System.out.println(src);
		System.out.println(dest);
		
		try {
					
			PdfReader reader = new PdfReader(src);
			PdfEncryptor.encrypt(reader, new FileOutputStream(dest),
	            password.getBytes(), "Df@dm1n".getBytes(), PdfWriter.AllowDegradedPrinting,
	            PdfWriter.STRENGTH128BITS);
			
			System.out.println("Successfully Done");
			isEncrypted = true;
		} catch (Exception e) {

			e.printStackTrace();
			isEncrypted = false;
		}
		return isEncrypted;
    }
	
	public boolean sendMail(String emailTo, String filePath, String reportName) {
		boolean sendSuccess = false;
		// Recipient's email ID needs to be mentioned.
		String to = emailTo;
		
		// Sender's email ID needs to be mentioned
		String from = "doctorprofile@glsict.com";
		
		// Assuming you are sending email from localhost
		String host = "172.18.22.5";
		
		// Get system properties
		Properties properties = System.getProperties();
		
		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		
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
			message.setSubject("This is the Subject Line!");
			
			// Create the message part 
			BodyPart messageBodyPart = new MimeBodyPart();
			
			// Fill the message
			messageBodyPart.setText("This is message body");
			
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
			System.out.println("Sent message successfully....");
			sendSuccess = true;
		} catch (MessagingException mex) {
			mex.printStackTrace();
			sendSuccess = false;
		}
		return sendSuccess;
	}
	
	public String getPaymentVoucherEmail(String hospital_code, String year, String month) {
		return "SELECT PM.DOCTOR_CODE, DR.EMAIL, DR.TAX_ID "
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
				+ "AND DR.EMAIL LIKE '%glsict.com'"
				+ "ORDER BY DR.DOCTOR_PROFILE_CODE, DR.CODE";
	}
	
	public String getTax406Email(String hospital_code, String year, String month) {
		return "SELECT ST.DOCTOR_CODE, DR.EMAIL, DR.TAX_ID "
				+ "FROM SUMMARY_TAX_406 ST "
				+ "LEFT OUTER JOIN DOCTOR DR ON ST.DOCTOR_CODE = DR.CODE "
				+ "AND ST.HOSPITAL_CODE = DR.HOSPITAL_CODE "
				+ "LEFT OUTER JOIN DOCTOR_PROFILE DP ON DR.DOCTOR_PROFILE_CODE = DP.CODE "
				+ "AND DR.HOSPITAL_CODE = DP.HOSPITAL_CODE "
				+ "LEFT OUTER JOIN HOSPITAL HOS ON ST.HOSPITAL_CODE = HOS.CODE "
				+ "WHERE ST.HOSPITAL_CODE = '"+hospital_code+"' "
				+ "AND YYYY = '"+year+"' "
				+ "AND SUM_TAX_DR_AMT > 0 "
				+ "AND PRINT_DATE <> '' "
				+ "AND (DR.EMAIL <> '' AND DR.EMAIL IS NOT NULL) "
				+ "AND DR.EMAIL LIKE '%glsict.com' "
				+ "ORDER BY DR.DOCTOR_PROFILE_CODE, DR.CODE";
	}
	
	public String getTax402Email(String hospital_code, String year) {
		return "SELECT TAX.DOCTOR_CODE, DOCTOR.EMAIL, DR.TAX_ID " 
				+ "FROM HOSPITAL LEFT OUTER JOIN SUMMARY_TAX_402 TAX ON HOSPITAL.CODE = TAX.HOSPITAL_CODE "
				+ "LEFT OUTER JOIN DOCTOR ON TAX.DOCTOR_CODE = DOCTOR.CODE AND TAX.HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE "
				+ "WHERE TAX.HOSPITAL_CODE = '"+hospital_code+"' "
				+ "AND TAX.SUM_NORMAL_TAX_AMT > 0 "
				+ "AND TAX.MM = '13' AND DOCTOR.PAYMENT_MODE_CODE != 'U' "
				+ "AND TAX.YYYY = '"+year+"' "
				+ "AND TAX.PRINT_DATE <> '' "
				+ "AND (DR.EMAIL <> '' AND DR.EMAIL IS NOT NULL) "
				+ "AND DR.EMAIL LIKE '%glsict.com'";
	}

}
