package df.servlet.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import df.bean.db.conn.DBConn;
import df.bean.db.table.PayorOffice;
import df.bean.interfacefile.ExportReportSummaryDailyBean;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import df.bean.report.GenerateReportBean;
import df.bean.report.ReportQuery;

public class ViewDFReportSrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (request.getParameter("REPORT_MODULE").equals("iDoctor")) {
				this.reportView(request, response);
			}
		} catch (Exception ex) {
			responseError(ex.toString(), response);
		}
	}
	
	private void reportView(HttpServletRequest request, HttpServletResponse response) {
		ExportReportSummaryDailyBean ers;
		DBConn cdb = new DBConn();
		try {
			cdb.setStatement();
		} catch (Exception ex) {
			System.out.println(ex);
		}

		HttpSession session = request.getSession(true);
		String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
		String user_id = request.getParameter("USER_ID");
		String reportfilename = request.getParameter("REPORT_FILE_NAME");
		String from_date = JDate.saveDate(request.getParameter("FROM_DATE"));
		String to_date = JDate.saveDate(request.getParameter("TO_DATE"));
		String payor_office_code = request.getParameter("PAYOR_OFFICE_CODE");
		String doctor_code = request.getParameter("DOCTOR_CODE");
		String doctor_profile_code = request.getParameter("DOCTOR_PROFILE_CODE");
		String doctor_department = request.getParameter("DOCTOR_DEPARTMENT_CODE");
		String order_category_code = request.getParameter("ORDER_CATEGORY_CODE");
		String doctor_category = request.getParameter("DOCTOR_CATEGORY_CODE");
		String order_item_code = request.getParameter("ORDER_ITEM_CODE");
		String order_item_category = request.getParameter("ORDER_ITEM_CATEGORY_CODE");
		String invoice_no = request.getParameter("INVOICE_NO");
		String transaction_module = request.getParameter("TRANSACTION_MODULE");
		String transaction_type = request.getParameter("TRANSACTION_TYPE");
		String admission_type_code = request.getParameter("ADMISSION_TYPE_CODE");
		String document_type = request.getParameter("DOCUMENT_TYPE");
		String year = request.getParameter("YYYY");
		String month = request.getParameter("MM");
		String doctor_code_from = request.getParameter("DOCTOR_CODE_FROM");
		String doctor_code_to = request.getParameter("DOCTOR_CODE_TO");
		String guarantee_department_code = request.getParameter("GUARANTEE_DEPARTMENT_CODE");
		String gl_type = request.getParameter("GL_TYPE");
		String expense_sign = request.getParameter("EXPENSE_SIGN");
		String expense_account_code = request.getParameter("EXPENSE_ACCOUNT_CODE");
		String expense_code = request.getParameter("EXPENSE_CODE");
		String term = request.getParameter("TERM");
		String doctor_type_code = request.getParameter("DOCTOR_TYPE_CODE");
		String payment_mode_code = request.getParameter("PAYMENT_MODE_CODE");
		String save_file = request.getParameter("SAVE_FILE");
		String file_type = request.getParameter("FILE_TYPE");
		String paymentDate = JDate.saveDate(request.getParameter("PAYMENT_DATE"));
		
		String is_onward = "%";
		String is_partial = "%";
		String is_discharge = "%";
		String from_doctor = "";
		String to_doctor = "";
		
		String linux_path = getServletConfig().getServletContext().getRealPath("") + "/reports/output/" + save_file+ ".txt";
		String windows_path = getServletConfig().getServletContext().getRealPath("") + "\\reports\\output\\" + save_file+ ".txt";
		String linux_logo_path = request.getRealPath("/") + "reports/";
		String windows_logo_path = request.getRealPath("/") + "reports\\";
		String logo_path = Variables.IS_WINDOWS ? windows_logo_path : linux_logo_path;
		String path = Variables.IS_WINDOWS ? windows_path : linux_path;
		
		System.out.println("Report Name : "+reportfilename+" : "+doctor_type_code);
		//set default param block
		try {
			if (from_date.equals("") || from_date.equals(null)) {
				from_date = "00000000";
			}
		} catch (Exception e) {}
		try {
			if (to_date.equals("") || to_date.equals(null)) {
				if( (year.equals("") && month.equals("")) || (year.equals(null) && month.equals(null))){
					to_date = "99999999";
				}else{
					to_date = year+month+"31";
				}
			}
		} catch (Exception e) {}
		try {
			if (doctor_code.equals("") || doctor_code.equals(null)) {
				doctor_code = "%";
			}
		} catch (Exception e) {}

		try {
			if (doctor_category.equals("") || doctor_category.equals(null)) {
				doctor_category = "%";
			}
		} catch (Exception e) {}
		
		order_item_category = "%";

		try {
			if (order_category_code.equals("") || order_category_code.equals(null)) {
				order_category_code = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (order_item_code.equals("") || order_item_code.equals(null)) {
				order_item_code = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (doctor_department.equals("") || doctor_department.equals(null)) {
				doctor_department = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (transaction_type.equals("") || transaction_type.equals(null)) {
				transaction_type = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (payor_office_code.equals("") || payor_office_code.equals(null)) {
				payor_office_code = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (transaction_module.equals("OW")) {
				is_onward = "Y";
				transaction_module = "TR";
			} else if (transaction_module.equals("PT")) {
				is_partial = "Y";
				is_onward = "N";
				transaction_module = "AR";
				transaction_type = "INV";
			} else {
				if (transaction_module.equals("AR")) {
					is_onward = "N";
					transaction_type = "INV";
				} else if (transaction_module.equals("TR")) {
					is_onward = "N";
				} else {
					is_onward = "%";
					if (transaction_module.equals("DY")) {
						is_discharge = "Y";
					} else if (transaction_module.equals("DH")) {
						is_discharge = "N";
					} else {
						is_discharge = "%";
					}
					transaction_module = "%";
				}
			}
		} catch (Exception e) {}

		try {
			if (invoice_no.equals("") || invoice_no.equals(null)) {
				invoice_no = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (doctor_type_code.equals("") || doctor_type_code.equals(null)) {
				doctor_type_code = "%";
			}
		} catch (Exception e) {}
		
		
		try {
			if (admission_type_code.equals("") || admission_type_code.equals(null)) {
				admission_type_code = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (save_file.equals("") || save_file.equals(null)) {
				save_file = "temp";
			}
		} catch (Exception e) {}
		
		try {
			if (document_type.equals("") || document_type.equals(null)) {
				document_type = "%";
			}
		} catch (Exception e) {}
		
		if (doctor_code.equals("") || doctor_code.equals(null) || doctor_code.equals("%")) {
			from_doctor = "0";
			to_doctor = "ZZZZZZZZZ";
		} else {
			from_doctor = doctor_code;
			to_doctor = doctor_code;
		}

		try {
			if (doctor_code_from.equals("") || doctor_code_from.equals(null)) {
				doctor_code_from = "0";
			}
		} catch (Exception e) {}
		
		try {
			if (doctor_code_to.equals("") || doctor_code_to.equals(null)) {
				doctor_code_to = "ZZZZZZZZZ";
			}
		} catch (Exception e) {}
		
		try {
			if (guarantee_department_code.equals("") || guarantee_department_code.equals(null)) {
				guarantee_department_code = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (paymentDate.equals("") || paymentDate.equals(null)) {
				paymentDate = "%";					
			}
		} catch (Exception e) {}
		
		try {
			if (expense_sign.equals("") || expense_sign.equals(null)) {
				expense_sign = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (expense_account_code.equals("") || expense_account_code.equals(null)) {
				expense_account_code = "%";
			}
		} catch (Exception e) {}
		
		try {
			if (expense_code.equals("") || expense_code.equals(null)) {
				expense_code = "%";
			}
		} catch (Exception e) {}

		try {
			if (doctor_profile_code.equals("") || doctor_profile_code.equals(null)) {
				doctor_profile_code = "%";
			}
		} catch (Exception e) {}

		try {
			if (term.equals("") || term.equals(null)) {
				term = "%";
			}
		} catch (Exception e) {}

		//set param to genfile and viewreport
		HashMap hm = new HashMap();
		ReportQuery rq = new ReportQuery();

		hm.put("hospital_code", hospital_code);
		rq.setHospitalCode(hospital_code);
		hm.put("from_date", from_date);
		rq.setFromDate(from_date);
		hm.put("to_date", to_date);
		rq.setToDate(to_date);
		hm.put("payorOffice", payor_office_code);
		rq.setPayorOffice(payor_office_code);
		hm.put("doctor_code", doctor_code);
		rq.setDoctorCode(doctor_code);
		hm.put("doctor_profile_code", doctor_profile_code);
		rq.setDoctorProfileCode(doctor_profile_code);
		hm.put("order_item", order_item_code);
		rq.setOrderItem(order_item_code);
		hm.put("transaction_type", transaction_type);
		rq.setTransactionType(transaction_type);
		hm.put("admission_type", admission_type_code);
		rq.setAdmissionType(admission_type_code);
		hm.put("doctor_department", doctor_department);
		rq.setDoctorDepartment(doctor_department);
		hm.put("order_category", order_category_code);
		rq.setOrderItemCategory(order_category_code);
		hm.put("doctor_category", doctor_category);
		rq.setDoctorCategory(doctor_category);
		hm.put("transaction_module", transaction_module);
		rq.setTransactionModule(transaction_module);
		hm.put("invoice_no", invoice_no);
		rq.setInvoiceNo(invoice_no);
		hm.put("doc_type", document_type);
		rq.setDocType(document_type);
		hm.put("hospital_code", hospital_code);
		rq.setHospitalCode(hospital_code);
		hm.put("month", month);
		rq.setMonth(month);
		hm.put("year", year);
		rq.setYear(year);
		hm.put("from_doctor", from_doctor);
		hm.put("to_doctor", to_doctor);
		hm.put("guarantee_department_code", guarantee_department_code);
		hm.put("payment_date", paymentDate);
		hm.put("order_item_category", order_item_category);
		hm.put("term", term);
		hm.put("hospital_logo", logo_path);
		hm.put("expense_sign", expense_sign);
		hm.put("expense_account_code", expense_account_code);
		hm.put("expense_code", expense_code);
		hm.put("doctor_type_code", doctor_type_code);
		hm.put("payment_mode_code", payment_mode_code);
		hm.put("doctor", doctor_code);

		System.out.println("Param Report : "+hm.toString());
		if (request.getParameter("REPORT_DISPLAY").equals("view")) {
			this.reportGenerateView(hm, reportfilename, response);
		} else {
			//this.reportGenerateFile(null, save_file, null, response, request,"" + ers.exportData(path, "", rq.getReport(reportfilename), null, null, cdb, null));
		}
	}

	private void reportGenerateView(HashMap hashM, String report_source_file, HttpServletResponse response) {
		byte[] b = null;
		DBConn cdb = new DBConn();

		try {
			cdb.setStatement();
		} catch (Exception ex) {
			System.out.println(ex);
		}

		File reportFile = new File(this.getServletConfig().getServletContext().getRealPath("/reports/" + report_source_file + ".jasper"));
		hashM.put("SUBREPORT_DIR", this.getServletConfig().getServletContext().getRealPath("/reports/"));
		try {
			response.setContentType("application/pdf");
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), hashM, cdb.getConnection());
			b = JasperRunManager.runReportToPdf(reportFile.getPath(), hashM, cdb.getConnection());
			if (b != null && b.length > 0) {
				response.setContentLength(b.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(b, 0, b.length);
				ouputStream.flush();
				ouputStream.close();
			} else {
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");
				out.println("<body>");
				out.println("Report is empty");
				out.println("</body>");
			}
		} catch (Exception e) {
			PrintWriter out = null;
			try {
				out = response.getWriter();
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>View Report Servlet</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>Report Error</h1>");
				out.println("Report Path :" + reportFile.getPath() + "<br>");
				out.println("Cause : " + e);
				out.println("</body>");
				out.println("</html>");
			} catch (IOException ex) {
			}
		} finally {
			cdb.closeDB("");
		}
	}
	
	private void responseError(String e, HttpServletResponse r) {
		PrintWriter out = null;
		try {
			out = r.getWriter();
		} catch (IOException ex) {
		}
		r.setContentType("text/html");
		out.println("<body>");
		out.println("Can't view report : " + e);
		out.println("</body>");
	}
}
