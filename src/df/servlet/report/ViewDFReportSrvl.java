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

import df.bean.db.conn.DBConn;
import df.bean.interfacefile.ExportReportSummaryDailyBean;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import df.bean.report.GenerateReportBean;
import df.bean.report.ReportQuery;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 * Servlet implementation class ViewDFReportSrvl
 */
public class ViewDFReportSrvl extends HttpServlet {

	String link = "./reports/output/";
	ExportReportSummaryDailyBean ers;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("ViewDFReport");
		HttpSession session = request.getSession(true);
		try {
			// set hospital name
			this.setHp_name(session.getAttribute("HOSPITAL_CODE").toString());
			if (request.getParameter("REPORT_MODULE").equals("iDoctor")) {
				this.reportiDoctor(request, response);
			}
		} catch (Exception ex) {
			responseError(ex.toString(), response);
		}
	}

	private String hp_name;

	private String getHp_name() {
		return hp_name;
	}

	private void setHp_name(String hp_code) {
		DBConn conn = null;
		try {
			if (conn == null) {
				conn = new DBConn();
				conn.setStatement();
			}
			String qHp = "SELECT DESCRIPTION_THAI FROM HOSPITAL WHERE CODE='" + hp_code + "'";
			String[][] arr = conn.query(qHp);
			this.hp_name = arr[0][0];
		} catch (Exception err) {
			conn.closeDB("");
			System.out.println(err.getMessage());
		}
		conn.closeDB("");
	}

	private void responseError(String e, HttpServletResponse r) {
		PrintWriter out = null;
		try {
			out = r.getWriter();
		} catch (IOException ex) {
		}
		r.setContentType("text/html");
		out.println("<body>");
		out.println("Error : \n" + e);
		out.println("</body>");
	}

	private void reportiDoctor(HttpServletRequest request, HttpServletResponse response) {

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
		String term = request.getParameter("term");
		String doctor_type_code = request.getParameter("DOCTOR_TYPE_CODE");
		String payment_mode_code = request.getParameter("PAYMENT_MODE_CODE");
		String save_file = request.getParameter("SAVE_FILE");
		String file_type = request.getParameter("FILE_TYPE");
		String paymentDate = JDate.saveDate(request.getParameter("date"));
		String is_onward = "%";
		String is_partial = "%";
		String is_discharge = "%";

		String from_doctor = "";
		String to_doctor = "";

		String linux_path = getServletConfig().getServletContext().getRealPath("") + "/reports/output/" + save_file
				+ ".txt";
		String windows_path = getServletConfig().getServletContext().getRealPath("") + "\\reports\\output\\" + save_file
				+ ".txt";
		String linux_logo_path = request.getRealPath("/") + "reports/";
		String windows_logo_path = request.getRealPath("/") + "reports\\";
		String logo_path = Variables.IS_WINDOWS ? windows_logo_path : linux_logo_path;
		String path = Variables.IS_WINDOWS ? windows_path : linux_path;

		try {
			if (from_date.equals("") || from_date.equals(null)) {
				from_date = "00000000";
			}
		} catch (Exception e) {}
		try {
			if (to_date.equals("") || to_date.equals(null)) {
				to_date = "99999999";
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

		if (reportfilename.equals("ImportTransaction") || reportfilename.equals("ImportChecklist") || reportfilename.equals("ImportVerifyTransaction")) {

			try {
				if (doctor_profile_code.equals("") || doctor_profile_code.equals(null)) {
					doctor_profile_code = "%";
				}
			} catch (Exception e) {}

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

			if (reportfilename.equals("ImportTransaction")) {
				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					System.out.println("view ImportTransaction");
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					if (file_type.equals("txt")) {
						this.reportGenerateFile(null, save_file, null, response, request,
								"" + ers.exportData(path, "", rq.getReport(reportfilename), null, null, cdb, null));
						cdb.closeDB("");
					} else {
						this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
					}
				}
			} else if (reportfilename.equals("ImportChecklist")) {

				hm.put("transaction_type", transaction_type);
				rq.setTransactionType(transaction_type);
				hm.put("transaction_module", transaction_module);
				rq.setTransactionModule(transaction_module);
				hm.put("invoice_no", invoice_no);
				rq.setInvoiceNo(invoice_no);
				hm.put("doc_type", document_type);
				rq.setDocType(document_type);

				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					System.out.println("view ImportChecklist");
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					if (file_type.equals("txt")) {
						this.reportGenerateFile(null, save_file, null, response, request,
								"" + ers.exportData(path, "", rq.getReport(reportfilename), null, null, cdb, null));
						cdb.closeDB("");
					} else {
						this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
					}
				}
			} else if (reportfilename.equals("ImportVerifyTransaction")) {
				
				if(request.getParameter("REPORT_DISPLAY").equals("view")){
		            this.reportGenerateView(hm, reportfilename, response);
		        }else{
		            if(file_type.equals("txt")){
		                this.reportGenerateFile(null, save_file, null, response, request, ""+ers.exportData(path, "", rq.getReport(reportfilename), null, null, cdb, null));
		                cdb.closeDB("");
		            }else{
		                this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
		            }
		        }
			}
		} else if (reportfilename.equals("GuaranteeSetup") || reportfilename.equals("SummaryRevenueByDetail")
				|| reportfilename.equals("PaymentVoucher" + hospital_code) || reportfilename.equals("ExpenseDetail")
				|| reportfilename.equals("SummaryRevenueByDoctor")
				|| reportfilename.equals("SummaryRevenueByDetailInMonthVCH") || reportfilename.equals("SummaryRevenueByDetailForDoctorVCH") || reportfilename.equals("SummaryDFUnpaidByDetail"+hospital_code) || reportfilename.equals("DFUnpaidSum"+hospital_code)) {

			if (doctor_code.equals("") || doctor_code.equals(null) || doctor_code.equals("%")) {
				from_doctor = "0";
				to_doctor = "ZZZZZZZZZ";
			} else {
				from_doctor = doctor_code;
				to_doctor = doctor_code;
			}

			HashMap hm = new HashMap();
			ReportQuery rq = new ReportQuery();

			hm.put("hospital_code", hospital_code);
			hm.put("month", month);
			hm.put("year", year);

			System.out.println("month : " + month);
			System.out.println("year : " + year);

			if (reportfilename.equals("GuaranteeSetup")) {
				System.out.println("GuaranteeSetup");

				hm.put("from_doctor", from_doctor);
				hm.put("to_doctor", to_doctor);
				hm.put("guarantee_department_code", guarantee_department_code);

				System.out.println("from_doctor >> " + from_doctor);
				System.out.println("to_doctor >> " + to_doctor);
				System.out.println("guarantee_department_code >> " + guarantee_department_code);

				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
				}

			} else if (reportfilename.equals("SummaryRevenueByDetail") || reportfilename.equals("SummaryRevenueByDetailForDoctorVCH")) {
				System.out.println("SummaryRevenueByDetail");

				hm.put("payment_date", paymentDate);
				hm.put("doctor_category", doctor_category);
				hm.put("doctor_department", doctor_department);
				hm.put("order_item", order_item_code);
				hm.put("order_item_category", order_item_category);
				hm.put("from_doctor", from_doctor);
				hm.put("to_doctor", to_doctor);
				hm.put("term", "%");

				hm.put("hospital_logo", logo_path);

				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					System.out.println(reportfilename);
					if (file_type.equals("txt")) {
						System.out.println(file_type + "<>" + path);
						System.out.println(rq.getReport(reportfilename));
						this.reportGenerateFile(null, save_file, null, response, request, "" + ers.exportData(path,
								reportfilename, rq.getReport(reportfilename), null, null, cdb, null));
						cdb.closeDB("");
					} else {
						this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
					}
				}
			} else if (reportfilename.equals("PaymentVoucher" + hospital_code)) {
				System.out.println("PaymentVoucher" + hospital_code);
				// TODO

				String subreport_path = getServletConfig().getServletContext().getRealPath("") + "\\reports";

				hm.put("from_doctor", from_doctor);
				hm.put("to_doctor", to_doctor);
				hm.put("payment_date", paymentDate);
				hm.put("hospital_logo", logo_path);
				hm.put("SUBREPORT_DIR", subreport_path);
				hm.put("term", "%");

				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
				}

			} else if (reportfilename.equals("ExpenseDetail")) {
				System.out.println("ExpenseDetail");
				hm.put("payment_date", paymentDate);
				hm.put("doctor_category", doctor_category);
				hm.put("doctor_department", doctor_department);
				hm.put("order_item", order_item_code);
				hm.put("from_doctor", from_doctor);
				hm.put("to_doctor", to_doctor);
				hm.put("expense_sign", expense_sign);
				hm.put("expense_account_code", expense_account_code);
				hm.put("expense_code", expense_code);

				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					System.out.println(reportfilename);
					if (file_type.equals("txt")) {
						System.out.println(file_type + "<>" + path);
						System.out.println(rq.getReport(reportfilename));
						this.reportGenerateFile(null, save_file, null, response, request, "" + ers.exportData(path,
								reportfilename, rq.getReport(reportfilename), null, null, cdb, null));
						cdb.closeDB("");
					} else {
						this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
					}
				}

			} else if (reportfilename.equals("SummaryRevenueByDoctor")) {
				System.out.println("SummaryRevenueByDoctor");
				hm.put("term", term);
				hm.put("doctor_type_code", doctor_type_code);
				hm.put("payment_mode_code", payment_mode_code);
				hm.put("doctor_department", doctor_department);

				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
				}

			} else if (reportfilename.equals("SummaryRevenueByDetailInMonthVCH")) {
				System.out.println("SummaryRevenueByDetailInMonthVCH");
				String yyyymm = year+month+"%";
				hm.put("from_doctor", from_doctor);
				hm.put("to_doctor", to_doctor);
				hm.put("doctor_category", doctor_category);
				hm.put("doctor_department", doctor_department);
				hm.put("order_item", order_item_code);
				hm.put("order_item_category", order_item_category);
				hm.put("term", "%");
				hm.put("payment_date", yyyymm );
				hm.put("hospital_logo", logo_path);

				System.out.println("from_doctor : " + from_doctor);
				System.out.println("to_doctor : " + to_doctor);
				System.out.println("doctor_category : " + doctor_category);
				System.out.println("doctor_department : " + doctor_department);
				System.out.println("order_item_code : " + order_item_code);
				System.out.println("order_item_category : " + order_item_category);
				System.out.println("paymentDate : " + yyyymm);

				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
				}

			}else if (reportfilename.equals("SummaryDFUnpaidByDetail"+hospital_code) || reportfilename.equals("DFUnpaidSum"+hospital_code)) {
				System.out.println("DFUnpaidByDetail");

				
				hm.put("form_date","00000000" );
				hm.put("to_date",year+month+"31" );
				hm.put("year",year);
				hm.put("month",month);
				hm.put("doctor", doctor_code);

				hm.put("hospital_logo", logo_path);

				if (request.getParameter("REPORT_DISPLAY").equals("view")) {
					this.reportGenerateView(hm, reportfilename, response);
				} else {
					System.out.println(reportfilename);
					if (file_type.equals("txt")) {
						System.out.println(file_type + "<>" + path);
						System.out.println(rq.getReport(reportfilename));
						this.reportGenerateFile(null, save_file, null, response, request, "" + ers.exportData(path,
								reportfilename, rq.getReport(reportfilename), null, null, cdb, null));
						cdb.closeDB("");
					} else {
						this.reportGenerateFile(hm, save_file, reportfilename, response, request, file_type);
					}
				}
			}else {}
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

		File reportFile = new File(
				this.getServletConfig().getServletContext().getRealPath("/reports/" + report_source_file + ".jasper"));
		hashM.put("SUBREPORT_DIR", this.getServletConfig().getServletContext().getRealPath("/reports/"));
		try {
			response.setContentType("application/pdf");
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), hashM, cdb.getConnection());
			// JasperPrintManager.printPage(jasperPrint, 1, false);
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

	private void reportGenerateFile(HashMap hashM, String report_file_gen, String report_source_file,
			HttpServletResponse response, HttpServletRequest request, String report_type) {
		GenerateReportBean grb = new GenerateReportBean();
		PrintWriter out = null;
		String upload_path = getServletConfig().getServletContext().getRealPath("") + "\\reports\\output\\";
		String URLpath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ""
				+ request.getContextPath() + "/reports/" + report_source_file + ".jasper"; // /mywebapp
		DBConn cdb = new DBConn();
		try {
			cdb.setStatement();
		} catch (Exception ex) {
			System.out.println(ex);
		}

		try {
			out = response.getWriter();
			System.out.println("upload_path : " + upload_path);
			System.out.println("URLpath : " + URLpath);

		} catch (IOException ex) {
		}

		if (report_type.equals("all")) {
			grb.setPath(upload_path);
			if (grb.exportReportPDF(report_file_gen, URLpath, hashM)
					&& grb.exportReportExcel(report_file_gen, URLpath, hashM, cdb)) {
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>View Report Servlet</title>");
				out.println("</head>");
				out.println("<body bgcolor=\"#dde4e8\">");
				out.println("<h1 align=\"center\">Report Generate File</h1>");
				out.println("<p align=\"center\">" + "<a href=\"" + link + report_file_gen + ".pdf\">"
						+ "<img src=\"./images/pdf_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p>");
				out.println("<p align=\"center\">" + "<a href=\"" + link + report_file_gen + ".xls\">"
						+ "<img src=\"./images/xls_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p><br>");
				out.println("<p align=\"center\">Write File Report Complete</p><br>");
				out.println("</body>");
				out.println("</html>");
			} else {
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>View Report Servlet</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>Report Result</h1>");
				out.println("<p align=\"center\">Can't write file report</p>");
				out.println("<p align=\"center\">" + link + report_file_gen + "</p>");
				out.println("</body>");
				out.println("</html>");
			}
		} else if (report_type.equals("xls")) {
			grb.setPath(upload_path);

			if (grb.exportReportExcel(report_file_gen, URLpath, hashM, cdb)) {
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>Generate Report Servlet</title>");
				out.println("</head>");
				out.println("<body bgcolor=\"#dde4e8\">");
				out.println("<h1 align=\"center\">Report Generate File</h1>");
				out.println("<p align=\"center\">" + "<a href=\"" + link + report_file_gen + ".xls\">"
						+ "<img src=\"./images/xls_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p><br>");
				out.println("<p align=\"center\">Write File Report Complete</p><br>");
				out.println("</body>");
				out.println("</html>");
			} else {
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>View Report Servlet</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>Report Result</h1>");
				out.println("<p align=\"center\">Can't write file report</p>");
				out.println("<p align=\"center\">" + link + report_file_gen + ":" + grb.getErrMesg() + "</p>");
				out.println("</body>");
				out.println("</html>");
			}
		} else if (report_type.equals("pdf")) {
			grb.setPath(upload_path);

			if (grb.exportReportPDF(report_file_gen, URLpath, hashM)) {
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>View Report Servlet</title>");
				out.println("</head>");
				out.println("<body bgcolor=\"#dde4e8\">");
				out.println("<h1 align=\"center\">Report Generate File</h1>");
				out.println("<p align=\"center\">" + "<a href=\"" + link + report_file_gen + ".pdf\">"
						+ "<img src=\"./images/pdf_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p>");
				out.println("<p align=\"center\">Write File Report Complete</p><br>");
				out.println("</body>");
				out.println("</html>");
			} else {
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>View Report Servlet</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>Report Result</h1>");
				out.println("<p align=\"center\">Can't write file report</p>");
				out.println("<p align=\"center\">" + grb.getErrMesg() + "</p>");
				out.println("</body>");
				out.println("</html>");
			}
		} else {
			if (report_type.equals("true")) {
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>View Report Servlet</title>");
				out.println("</head>");
				out.println("<body bgcolor=\"#dde4e8\">");
				out.println("<h1 align=\"center\">Report Generate File</h1>");
				out.println("<p align=\"center\">" + "<a href=\"" + link + report_file_gen + ".txt\">"
						+ "<img src=\"./images/txt_icon.gif\" width=\"50\" height=\"50\" border=\"0\"/></a></p>");
				out.println("<p align=\"center\">Write File Report Complete</p><br>");
				out.println("</body>");
				out.println("</html>");
			} else {
				response.setContentType("text/html");
				out.println("<html>");
				out.println("<head>");
				out.println("<title>View Report Servlet</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>Report Result</h1>");
				out.println("<p align=\"center\">Can't write text file report</p>");
				out.println("<p align=\"center\">" + link + report_file_gen + "</p>");
				out.println("</body>");
				out.println("</html>");
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

}
