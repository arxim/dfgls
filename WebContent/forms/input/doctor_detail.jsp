<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
            // Short Page Profile Doctor
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_DOCTOR)) {
                response.sendRedirect("../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Doctor Details", "รายละเอียดข้อมูลแพทย์");
            labelMap.add("HOSPITAL_CODE", "Hospital Code", "รหัสโรงพยาบาล");
            labelMap.add("DOCTOR_PROFILE_CODE", "Profile Code", "รหัสประวัติแพทย์");
            labelMap.add("CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("NAME_THAI", "Name (TH)", "ชื่อ (ไทย)");
            labelMap.add("NAME_ENG", "Name (EN)", "ชื่อ (อังกฤษ)");
            labelMap.add("ACTIVE", "Status", "สถานะ");
            labelMap.add("ACTIVE_0", "Inactive", "ไม่ใช้งาน");
            labelMap.add("ACTIVE_1", "Active", "ใช้งาน");
            labelMap.add("TAX_ID", "Tax ID / Nation ID", "เลขผู้เสียภาษี/เลขบัตรประชาชน");
            labelMap.add("LICENSE_ID", "License ID", "เลขที่ใบอนุญาต");
            labelMap.add("FROM_DATE", "License Issue Date", "วันออกใบอนุญาต");
            labelMap.add("TO_DATE", "License Expire Date", "วันหมดอายุ");
            labelMap.add("SUBTITLE_ADDRESS", "Address", "ที่อยู่");
            labelMap.add("ADDRESS1", "Address No. / Address", "เลขที่/ที่อยู่");
            labelMap.add("ADDRESS2", "Sub District / District", "แขวง/เขต");
            labelMap.add("ADDRESS3", "Province", "จังหวัด");
            labelMap.add("ZIP", "Postal Code", "รหัสไปรษณีย์");
            labelMap.add("SUBTITLE_INFORMATION", "Doctor Information", "ข้อมูลแพทย์");
            labelMap.add("DOCTOR_TYPE_CODE", "Doctor Type", "ประเภทแพทย์");
            labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "หมวดหมู่แพทย์");
            labelMap.add("HOSPITAL_UNIT_CODE", "Hospital Unit", "หน่วยย่อย");
            labelMap.add("DEPARTMENT_CODE", "Department Code", "รหัสแผนก");
            labelMap.add("PAYMENT_MODE_CODE", "Payment Mode", "ประเภทการจ่าย");
            labelMap.add("INCLUDE_REVENUE_CODE", "Include 40(6) Revenue to Code", "รวมรายได้ภาษีไว้ที่รหัส");
            labelMap.add("PAY_TAX_402_BY", "Pay Tax by", "จ่ายภาษีโดย");
            labelMap.add("GUARANTEE_PROFILE", "Guarantee In Profile", "การันตีภายใต้แพทย์เจ้าของ");
            labelMap.add("GUARANTEE_DR_CODE", "Include Revenue To DR.", "การันตีรายได้รวมที่แพทย์");
            labelMap.add("GUARANTEE_DR_CODE_DP", "Doctor Profile", "รหัสประวัติแพทย์");
            labelMap.add("GUARANTEE_DR_CODE_D", "Doctor", "รหัสแพทย์");
            labelMap.add("GUARANTEE_SOURCE", "Revenue for Guarantee", "คิดการันตีจากยอด");
            labelMap.add("GUARANTEE_SOURCE_DF", "DF After Allocate", "จำนวนเงินหลังแบ่ง");
            labelMap.add("GUARANTEE_SOURCE_DF_VALUE", "AF", "AF");
            labelMap.add("GUARANTEE_SOURCE_TAX", "DF Before Allocate", "จำนวนเงินก่อนแบ่ง");
            labelMap.add("GUARANTEE_SOURCE_TAX_VALUE", "BF", "BF");
            labelMap.add("GUARANTEE_DAY", "Guarantee by Day", "วันที่นำมาคิดการันตี");
            labelMap.add("GUARANTEE_DAY_VERIFY", "Execute Date", "วันที่ทำการคีย์ค่าแพทย์");
            labelMap.add("GUARANTEE_DAY_VERIFY_VALUE", "VER", "VER");
            labelMap.add("GUARANTEE_DAY_INVOICE", "Invoice Date", "วันที่ทำการออกใบแจ้งหนี้");
            labelMap.add("GUARANTEE_DAY_INVOICE_VALUE", "INV", "INV");
            labelMap.add("GUARANTEE_DAY_UNDEFINE", "Undefine Date", "ไม่ทำการกำหนด");
            labelMap.add("SALARY", "Salary", "เงินเดือน");
            labelMap.add("POSITION_AMT", "Position Amount", "ค่าประจำตำแหน่ง");
            labelMap.add("OVER_GUARANTEE_PCT", "Over Guarantee Allocate", "ส่วนแบ่งเกินการันตี");
            labelMap.add("IN_GUARANTEE_PCT", "In Guarantee Allocate", "ส่วนแบ่งในการันตี");
            labelMap.add("IS_HOLD", "Time to Payment", "จำนวนครั้งทำจ่าย");
            labelMap.add("IS_HOLD_Y", "2 Time", "2 ครั้ง");
            labelMap.add("IS_HOLD_N", "1 Time ", "1 ครั้ง ");
            labelMap.add("PAYMENT_TIME", "Time to Payment", "จำนวนครั้งจ่ายแพทย์");
            labelMap.add("ONE_TIME", "1", "1");
            labelMap.add("TWO_TIME", "2", "2");
            labelMap.add("TIME_TABLE", "Master Time Table", "ตารางข้อมูลแพทย์ต้นแบบ");
            labelMap.add("IS_ADVANCE_PAYMENT", "Advance Payment", "จ่ายค่าแพทย์ทุกกรณี");
            labelMap.add("IS_ADVANCE_PAYMENT_Y", "Yes", "ใช่");
            labelMap.add("IS_ADVANCE_PAYMENT_N", "No", "ไม่ใช่");
            labelMap.add("GUARANTEE_START_DATE","Guarantee Start Date","วันเริ่มการันตี");
            labelMap.add("GUARANTEE_EXPIRE_DATE","Guarantee Expire Date","วันสิ้นสุดการันตี");
			labelMap.add("SUBTITLE_PAYMENT_INFORMATION", "Payment/Tax Information", "ข้อมูลการจ่ายเงินและภาษี");
            labelMap.add("SUBTITLE_BANK_ACCOUNT_INFORMATION", "Bank Account Information", "ข้อมูลบัญชีธนาคาร");
            labelMap.add("BANK_ACCOUNT_NO", "Account No", "เลขที่บัญชี");
            labelMap.add("BANK_ACCOUNT_NAME", "Account Name", "ชื่อบัญชี");
            labelMap.add("BANK_CODE", "Bank Code", "รหัสธนาคาร");
            labelMap.add("BANK_BRANCH_CODE", "Branch Code", "รหัสสาขา");
            labelMap.add("GUARANTEE_INFORMATION", "Guarantee Information", "รายละเอียดการการันตี");
            labelMap.add("EMAIL", "E-mail", "E-mail");
            labelMap.add("SUBTITLE_OTHER", "Other", "อื่น ๆ");
            labelMap.add("NOTE", "Note", "หมายเหตุ");
            labelMap.add("taxG_d","Doctor","แพทย์");
            labelMap.add("taxG_h","Hospital","โรงพยาบาล");
            labelMap.add("ALERT_BANK","Plase select BANK","กรุณาเลือกธนาคาร");
            labelMap.add("SPECIAL_TYPE" ,  "Specialty" , "ความชำนาญพิเศษ");
            labelMap.add("TAX_SUM","Summary Revenue","คำนวณรวมรายได้");
            labelMap.add("TAX_STEP","Step Allocate","คำนวณอัตราก้าวหน้า");
            labelMap.add("TAX_3","WithHolding Tax 3%","หักภาษี ณ ที่จ่าย 3%");
            labelMap.add("TAX_5","WithHolding Tax 5%","หักภาษี ณ ที่จ่าย 5%");
            labelMap.add("TAX_14","WithHolding Tax 14%","หักภาษี ณ ที่จ่าย 14%");
            labelMap.add("TAX_15","WithHolding Tax 15%","หักภาษี ณ ที่จ่าย 15%");
            labelMap.add("TAX_402_METHOD","Tax 40(2) Calculation","การคำนวณรายได้ 40(2)");
            labelMap.add("TAX_406_METHOD","Tax 40(6) Calculation","การคำนวณรายได้ 40(6)");
            labelMap.add("GUARANTEE_PER_HOUR","Guarantee Amount / hour","ค่าการันตีต่อชั่วโมง");
            labelMap.add("EXTRA_PER_HOUR","Extra / hour","ค่าเวรต่อชั่วโมง");
            labelMap.add("DOCTOR_GROUP_CODE","Doctor Group","กลุ่มแพทย์");
            request.setAttribute("labelMap", labelMap.getHashMap());

            String[] taxGT = {labelMap.get("taxG_d"),labelMap.get("taxG_h")};
            String[] taxValue = {"0","1"};
            String remark;

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorRec = null, doctorProfileRec = null, doctorCategoryRec = null, bankRec = null, bankBranchRec = null, departmentRec = null, specialTypeRec = null  ,  hospitalUnitRec = null;
            DataRecord doctorRecLog = null;
            byte MODE = DBMgr.MODE_INSERT;
                    
            if (request.getParameter("MODE") != null) {

                MODE = Byte.parseByte(request.getParameter("MODE"));

                doctorRec = new DataRecord("DOCTOR");
                doctorRecLog = new DataRecord("LOG_DOCTOR");
                doctorRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                doctorRec.addField("DOCTOR_PROFILE_CODE", Types.VARCHAR, request.getParameter("DOCTOR_PROFILE_CODE"));
                doctorRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                doctorRec.addField("NAME_THAI", Types.VARCHAR, request.getParameter("NAME_THAI"));
                doctorRec.addField("NAME_ENG", Types.VARCHAR, request.getParameter("NAME_ENG"));
                doctorRec.addField("TAX_ID", Types.VARCHAR, request.getParameter("TAX_ID"));
                doctorRec.addField("LICENSE_ID", Types.VARCHAR, request.getParameter("LICENSE_ID"));
                doctorRec.addField("FROM_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("FROM_DATE")));
                doctorRec.addField("TO_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("TO_DATE")));
                doctorRec.addField("ADDRESS1", Types.VARCHAR, request.getParameter("ADDRESS1"));
                doctorRec.addField("ADDRESS2", Types.VARCHAR, request.getParameter("ADDRESS2"));
                doctorRec.addField("ADDRESS3", Types.VARCHAR, request.getParameter("ADDRESS3"));
                doctorRec.addField("ZIP", Types.VARCHAR, request.getParameter("ZIP"));
                doctorRec.addField("DOCTOR_TYPE_CODE", Types.VARCHAR, request.getParameter("DOCTOR_TYPE_CODE"));
                doctorRec.addField("DOCTOR_CATEGORY_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CATEGORY_CODE"));
                doctorRec.addField("HOSPITAL_UNIT_CODE", Types.VARCHAR, request.getParameter("HOSPITAL_UNIT_CODE"));
                doctorRec.addField("DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("DEPARTMENT_CODE"));
                doctorRec.addField("PAYMENT_MODE_CODE", Types.VARCHAR, request.getParameter("PAYMENT_MODE_CODE"));

                String strGuranteeDrCode = request.getParameter("CODE");
                doctorRec.addField("GUARANTEE_DAY", Types.VARCHAR, request.getParameter("GUARANTEE_DAY"));
				doctorRec.addField("GUARANTEE_DR_CODE", Types.VARCHAR, request.getParameter("GUARANTEE_DR_CODE"));
                doctorRec.addField("GUARANTEE_SOURCE", Types.VARCHAR, request.getParameter("GUARANTEE_SOURCE"));
                doctorRec.addField("IS_GUARANTEE_PROFILE", Types.VARCHAR, request.getParameter("GUARANTEE_PROFILE"));
                doctorRec.addField("OVER_GUARANTEE_PCT", Types.NUMERIC, request.getParameter("OVER_GUARANTEE_PCT"));
                doctorRec.addField("IN_GUARANTEE_PCT", Types.NUMERIC, request.getParameter("IN_GUARANTEE_PCT"));
                doctorRec.addField("PAYMENT_TIME", Types.VARCHAR, request.getParameter("IS_HOLD"));
                doctorRec.addField("IS_ADVANCE_PAYMENT", Types.VARCHAR, request.getParameter("IS_ADVANCE_PAYMENT"));
                doctorRec.addField("BANK_ACCOUNT_NO", Types.VARCHAR, request.getParameter("BANK_ACCOUNT_NO"));
                doctorRec.addField("BANK_ACCOUNT_NAME", Types.VARCHAR, request.getParameter("BANK_ACCOUNT_NAME"));
                doctorRec.addField("BANK_CODE", Types.VARCHAR, request.getParameter("BANK_CODE"));
                doctorRec.addField("BANK_BRANCH_CODE", Types.VARCHAR, request.getParameter("BANK_BRANCH_CODE"));
                doctorRec.addField("NOTE", Types.VARCHAR, request.getParameter("NOTE"));
                doctorRec.addField("EMAIL", Types.VARCHAR, request.getParameter("EMAIL"));
                doctorRec.addField("GUARANTEE_START_DATE",Types.VARCHAR, JDate.saveDate(request.getParameter("GUARANTEE_START_DATE")));
                doctorRec.addField("GUARANTEE_EXPIRE_DATE", Types.VARCHAR , JDate.saveDate(request.getParameter("GUARANTEE_EXPIRE_DATE")));
                doctorRec.addField("PAY_TAX_402_BY", Types.VARCHAR, request.getParameter("PAY_TAX_402_BY"));
                
                doctorRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                doctorRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                doctorRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                doctorRec.addField("DOCTOR_TAX_CODE", Types.VARCHAR, request.getParameter("INCLUDE_REVENUE_CODE"));
                doctorRec.addField("GUARANTEE_PER_HOUR", Types.NUMERIC, request.getParameter("GUARANTEE_PER_HOUR"));
                doctorRec.addField("EXTRA_PER_HOUR", Types.NUMERIC, request.getParameter("EXTRA_PER_HOUR"));
                doctorRec.addField("DOCTOR_GROUP_CODE", Types.VARCHAR, request.getParameter("DOCTOR_GROUP_CODE"));
                doctorRec.addField("TAX_402_METHOD", Types.VARCHAR, request.getParameter("TAX_402_METHOD"));
                doctorRec.addField("TAX_406_METHOD", Types.VARCHAR, request.getParameter("TAX_406_METHOD"));
                doctorRec.addField("SPECIAL_TYPE_CODE", Types.VARCHAR, request.getParameter("SPECIAL_TYPE"));
                
                // for log
                doctorRecLog.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                doctorRecLog.addField("DOCTOR_PROFILE_CODE", Types.VARCHAR, request.getParameter("DOCTOR_PROFILE_CODE"));
                doctorRecLog.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                doctorRecLog.addField("NAME_THAI", Types.VARCHAR, request.getParameter("NAME_THAI"));
                doctorRecLog.addField("NAME_ENG", Types.VARCHAR, request.getParameter("NAME_ENG"));
                doctorRecLog.addField("TAX_ID", Types.VARCHAR, request.getParameter("TAX_ID"));
                doctorRecLog.addField("LICENSE_ID", Types.VARCHAR, request.getParameter("LICENSE_ID"));
                doctorRecLog.addField("FROM_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("FROM_DATE")));
                doctorRecLog.addField("TO_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("TO_DATE")));
                doctorRecLog.addField("ADDRESS1", Types.VARCHAR, request.getParameter("ADDRESS1"));
                doctorRecLog.addField("ADDRESS2", Types.VARCHAR, request.getParameter("ADDRESS2"));
                doctorRecLog.addField("ADDRESS3", Types.VARCHAR, request.getParameter("ADDRESS3"));
                doctorRecLog.addField("ZIP", Types.VARCHAR, request.getParameter("ZIP"));
                doctorRecLog.addField("DOCTOR_TYPE_CODE", Types.VARCHAR, request.getParameter("DOCTOR_TYPE_CODE"));
                doctorRecLog.addField("DOCTOR_CATEGORY_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CATEGORY_CODE"));
                doctorRecLog.addField("HOSPITAL_UNIT_CODE", Types.VARCHAR, request.getParameter("HOSPITAL_UNIT_CODE"));
                doctorRecLog.addField("DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("DEPARTMENT_CODE"));
                doctorRecLog.addField("PAYMENT_MODE_CODE", Types.VARCHAR, request.getParameter("PAYMENT_MODE_CODE"));
				doctorRecLog.addField("GUARANTEE_DAY", Types.VARCHAR, request.getParameter("GUARANTEE_DAY"));
				doctorRecLog.addField("GUARANTEE_DR_CODE", Types.VARCHAR, request.getParameter("GUARANTEE_DR_CODE"));
                doctorRecLog.addField("GUARANTEE_SOURCE", Types.VARCHAR, request.getParameter("GUARANTEE_SOURCE"));
                doctorRecLog.addField("OVER_GUARANTEE_PCT", Types.NUMERIC, request.getParameter("OVER_GUARANTEE_PCT"));
                doctorRecLog.addField("PAYMENT_TIME", Types.VARCHAR, request.getParameter("IS_HOLD"));
                doctorRecLog.addField("IS_ADVANCE_PAYMENT", Types.VARCHAR, request.getParameter("IS_ADVANCE_PAYMENT"));
                doctorRecLog.addField("BANK_ACCOUNT_NO", Types.VARCHAR, request.getParameter("BANK_ACCOUNT_NO"));
                doctorRecLog.addField("BANK_ACCOUNT_NAME", Types.VARCHAR, request.getParameter("BANK_ACCOUNT_NAME"));
                doctorRecLog.addField("BANK_CODE", Types.VARCHAR, request.getParameter("BANK_CODE"));
                doctorRecLog.addField("BANK_BRANCH_CODE", Types.VARCHAR, request.getParameter("BANK_BRANCH_CODE"));
                doctorRecLog.addField("NOTE", Types.VARCHAR, request.getParameter("NOTE"));
                doctorRecLog.addField("GUARANTEE_START_DATE",Types.VARCHAR, JDate.saveDate(request.getParameter("GUARANTEE_START_DATE")));
                doctorRecLog.addField("GUARANTEE_EXPIRE_DATE", Types.VARCHAR , JDate.saveDate(request.getParameter("GUARANTEE_EXPIRE_DATE")));
                doctorRecLog.addField("PAY_TAX_402_BY", Types.VARCHAR, request.getParameter("PAY_TAX_402_BY"));                
                doctorRecLog.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate(), true);
                doctorRecLog.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime(), true);
                doctorRecLog.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                doctorRecLog.addField("EMAIL", Types.VARCHAR, request.getParameter("EMAIL"));
                doctorRecLog.addField("DOCTOR_TAX_CODE", Types.VARCHAR, request.getParameter("INCLUDE_REVENUE_CODE"));
                doctorRecLog.addField("TAX_402_METHOD", Types.VARCHAR, request.getParameter("TAX_402_METHOD"));
                doctorRecLog.addField("TAX_406_METHOD", Types.VARCHAR, request.getParameter("TAX_406_METHOD"));
                doctorRecLog.addField("SPECIAL_TYPE_CODE", Types.VARCHAR, request.getParameter("SPECIAL_TYPE"));

                if (MODE == DBMgr.MODE_INSERT) {
                	doctorRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                	doctorRecLog.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                	if (DBMgr.insertRecord(doctorRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/doctor_setup.jsp?CODE=" + doctorRec.getField("DOCTOR_PROFILE_CODE").getValue()));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }else if (MODE == DBMgr.MODE_UPDATE) {
                	Boolean act = false;
                	//if(1==1){
               		if(!request.getParameter("BANK_ACCOUNT_NO").equalsIgnoreCase(request.getParameter("TEMP_BANK_ACCOUNT_NO"))){
               			act = true;
               		}else if(!request.getParameter("BANK_ACCOUNT_NAME").equalsIgnoreCase(request.getParameter("TEMP_BANK_ACCOUNT_NAME"))){
               			act = true;
               		}else if(!request.getParameter("BANK_CODE").equalsIgnoreCase(request.getParameter("TEMP_BANK_CODE"))){
               			act = true;
               		}else if(!request.getParameter("BANK_BRANCH_CODE").equalsIgnoreCase(request.getParameter("TEMP_BANK_BRANCH_CODE"))){
               			act = true;
               		}
               		if(act || request.getParameter("ACTIVE").equals(null)){
               			//out.println("Active = '';");
               			doctorRecLog.addField("ACTIVE", Types.VARCHAR, "");
               			doctorRec.addField("ACTIVE", Types.VARCHAR, "");
               		}else{
               			doctorRecLog.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
               			doctorRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
               		}
               		DataRecord doctor = DBMgr.getRecord("SELECT HOSPITAL_CODE, DOCTOR_PROFILE_CODE, CODE, NAME_THAI, NAME_ENG, TAX_ID, LICENSE_ID, FROM_DATE, TO_DATE, ADDRESS1, ADDRESS2, ADDRESS3, ZIP, "+
               				"DOCTOR_TYPE_CODE, DOCTOR_CATEGORY_CODE, HOSPITAL_UNIT_CODE, DEPARTMENT_CODE, PAYMENT_MODE_CODE, GUARANTEE_DAY, GUARANTEE_DR_CODE, GUARANTEE_SOURCE, "+
               				"IS_GUARANTEE_PROFILE, OVER_GUARANTEE_PCT, IN_GUARANTEE_PCT, PAYMENT_TIME, IS_ADVANCE_PAYMENT, BANK_ACCOUNT_NO, BANK_ACCOUNT_NAME, BANK_CODE, BANK_BRANCH_CODE, "+
               				"NOTE, EMAIL, GUARANTEE_START_DATE, GUARANTEE_EXPIRE_DATE, PAY_TAX_402_BY, UPDATE_DATE, UPDATE_TIME, USER_ID, DOCTOR_TAX_CODE, GUARANTEE_PER_HOUR, EXTRA_PER_HOUR, "+
               				"DOCTOR_GROUP_CODE, TAX_402_METHOD, TAX_406_METHOD, SPECIAL_TYPE_CODE, ACTIVE                FROM DOCTOR WHERE CODE = '" + request.getParameter("CODE") + "' AND DOCTOR_PROFILE_CODE = '" + request.getParameter("DOCTOR_PROFILE_CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' " );
               		System.out.println(doctor.getSize());
               		System.out.println(doctorRec.getSize());
               		
               		remark = "แก้ไข ";
               		for(int i = 0; i < doctorRec.getSize(); i++){
               			if(!doctor.getValueOfIndex(i).getValue().equalsIgnoreCase(doctorRec.getValueOfIndex(i).getValue())
               					&& !doctorRec.getValueOfIndex(i).getName().equals("USER_ID")
               					&& !doctorRec.getValueOfIndex(i).getName().equals("UPDATE_DATE")
               					&& !doctorRec.getValueOfIndex(i).getName().equals("UPDATE_TIME")){
               				System.out.println("แก้ไข"+doctorRec.getValueOfIndex(i).getName());
               				remark += doctorRec.getValueOfIndex(i).getName()+", ";
               			}
               			//System.out.println("doctor: "+doctor.getValueOfIndex(i).getName()+","+doctor.getValueOfIndex(i).getValue());
               			//System.out.println(i+": "+doctorRec.getValueOfIndex(i).getName()+", "+doctorRec.getValueOfIndex(i).getValue());
               		}
               		doctorRecLog.addField("REMARK", Types.VARCHAR, remark);
                	
                    DBMgr.insertRecord(doctorRecLog);
                    
                    if (DBMgr.updateRecord(doctorRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/doctor_setup.jsp?CODE=" + doctorRec.getField("DOCTOR_PROFILE_CODE").getValue()));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }
                
                response.sendRedirect("../message.jsp");
                return;
            }
            else if (request.getParameter("DOCTOR_PROFILE_CODE") != null && request.getParameter("CODE") != null) {
                doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("CODE") + "' AND DOCTOR_PROFILE_CODE = '" + request.getParameter("DOCTOR_PROFILE_CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' " );
                doctorProfileRec = DBMgr.getRecord("SELECT * FROM DOCTOR_PROFILE WHERE CODE = '" + request.getParameter("DOCTOR_PROFILE_CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' ");
                if (doctorRec == null || doctorProfileRec == null) {
                    MODE = DBMgr.MODE_INSERT;
                }
                else {
                    MODE = DBMgr.MODE_UPDATE;
                    doctorCategoryRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM DOCTOR_CATEGORY WHERE CODE = '" + DBMgr.getRecordValue(doctorRec, "DOCTOR_CATEGORY_CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' ");
                    bankRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM BANK WHERE CODE = '" + DBMgr.getRecordValue(doctorRec, "BANK_CODE") + "' ");
                    bankBranchRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM BANK_BRANCH WHERE CODE = '" + DBMgr.getRecordValue(doctorRec, "BANK_BRANCH_CODE") + "' AND BANK_CODE='"+DBMgr.getRecordValue(doctorRec, "BANK_CODE")+"'");
                    departmentRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE CODE = '" + DBMgr.getRecordValue(doctorRec, "DEPARTMENT_CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' ");
                    hospitalUnitRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM HOSPITAL_UNIT WHERE CODE = '" + DBMgr.getRecordValue(doctorRec, "HOSPITAL_UNIT_CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' ");
                    specialTypeRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_ENG FROM SPECIAL_TYPE WHERE CODE = '" + DBMgr.getRecordValue(doctorRec, "SPECIAL_TYPE_CODE") + "' ");
                }      
            }
            else if (request.getParameter("DOCTOR_PROFILE_CODE") != null) {
                doctorProfileRec = DBMgr.getRecord("SELECT * FROM DOCTOR_PROFILE WHERE CODE = '" + request.getParameter("DOCTOR_PROFILE_CODE") + "'");
            }

            //authentication
            /*
            Type of Doctor
            1	System Admin 	-> All

            2	Manager		->
                    - มีหน้าที่ตรวจสอบข้อมูลและทำการ Active รหัสแพทย์นั้นๆ
                    - ไม่สามารถแก้ไขข้อมูลทั้งหมดได้ยกเว้น Active
            3	Organize Doctor	->
                    - สามารถดูข้อมูลและเพิ่มเติมเปลี่ยนแปลงแก้ไขได้ทั้งหมด
                    - ไม่สามารถ Active รหัสแพทย์นั้นได้ (ในกรณีเพิ่มหรือแก้ไข ให้ Set เป็น InActive)
            4	DF User(IT)	->
                    - สามารถดูข้อมูลได้ทั้งหมดแต่ไม่สามารถทำการเปลี่ยนแปลงแก้ไข

            5	User(Doctor)	->
			
            
			แก้ใข : 
				1. DF User ดูข้อมูลแพทย์ได้ทั้งหมด และสามารถแก้ไขข้อมูลแพทย์ที่ Active เป็น 1,0 ได้ยกเว้น Active เป็น null 
				  และไม่สามารถแก้ไขข้อมูล Bank ได้
				
				2. Manager มีสิทธิ์เหมือน DF User แต่สามารถเรียกรายการที่ MAO แก้ไขมา Approve ได้ (Active เป็น null)
				
				3. MAO สามารถใช้งานได้ทุก Field ยกเว้น Actvie และเมื่อมีการเพิ่มรหัสแพทย์ระบบจะ Set Active = ''
				แต่เมื่อมีการแก้ไขข้อมูลทั่วไปที่ไม่ใช่ข้อมูล Bank ไม่ต้องมีการ Approve จาก Manager
				 แต่เมื่อมีการแก้ไขข้อมูล Bank จะต้องมีการ Approve จาก Manager
 
*/ 
            String readonlyManager = "";
            String disabledManager = "";
            String disabledDFUser = "";
            String disabledOrgDC = "";
            String disabledAdmin = "";
            //---- แก้ไข 2009-07-02
            String dfuserLoginDisabled = "";
            String dfuserLoginReadonly = "";
            String dfuserLoginSave = "";
            String dataActiveIsNullDfuser = "";
            
          	//---- แก้ไข 2009-08-10
          	String readonlyManagerV2 = "";
          	String disabledManagerV2 = "";
            if("1".equalsIgnoreCase(session.getAttribute("USER_GROUP_CODE").toString())){
            	/*
            	readonlyManager = "readonly=\"readonly\"";
                disabledManager = "disabled=\"disabled\"";
                disabledDFUser = "disabled=\"disabled\"";
                disabledOrgDC = "disabled=\"disabled\"";
                */
                disabledAdmin = "disabled=\"disabled\"";
            }else if("2".equalsIgnoreCase(session.getAttribute("USER_GROUP_CODE").toString())){
                //readonlyManager = "readonly=\"readonly\""; //20090710
                //disabledManager = "disabled=\"disabled\""; //20090710
                readonlyManagerV2 = "readonly=\"readonly\""; //20090710
                disabledManagerV2 = "disabled=\"disabled\""; //20090710                   
            }else if("3".equalsIgnoreCase(session.getAttribute("USER_GROUP_CODE").toString())){
                disabledOrgDC = "disabled=\"disabled\"";
            }else if("4".equalsIgnoreCase(session.getAttribute("USER_GROUP_CODE").toString())){
                //readonlyManager = "readonly=\"readonly\"";
                //disabledManager = "disabled=\"disabled\"";
                //disabledDFUser = "disabled=\"disabled\"";
                //disabledOrgDC = "disabled=\"disabled\"";
            	dfuserLoginDisabled = "disabled=\"disabled\"";
            	dfuserLoginReadonly = "readonly=\"readonly\"";
            	//out.println(DBMgr.getRecordValue(doctorRec, "ACTIVE"));
            	if("".equalsIgnoreCase(DBMgr.getRecordValue(doctorRec, "ACTIVE").toString()) || DBMgr.getRecordValue(doctorRec, "ACTIVE").toString() == null){
            		dfuserLoginSave = "disabled=\"disabled\"";
            		//return;
            	}
            }else{
            	readonlyManager = "readonly=\"readonly\"";
                disabledManager = "disabled=\"disabled\"";
                disabledOrgDC = "disabled=\"disabled\"";
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-1.6.min.js"></script>
        <script type="text/javascript">
        
        //  ---------------------- SPECIAL TYPE CODE -------------------------
        function SPECIAL_TYPE_KeyPress(e) {
        	
        	var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

            if (key == 13) { 
            	document.mainForm.SPECIAL_TYPE.blur();
                return false;
            }
            else {
                return true;
            }
        }

        function AJAX_Refresh_SPECIAL_TYPE() {
         		var target = "../../RetrieveData?TABLE=SPECIAL_TYPE&COND=CODE='" + document.mainForm.SPECIAL_TYPE.value + "' ";
         		AJAX_Request(target, AJAX_Handle_Refresh_SPECIAL_TYPE);         	
        }
        
        function AJAX_Handle_Refresh_SPECIAL_TYPE() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;

                // Data not found
                if (!isXMLNodeExist(xmlDoc, "CODE")) {
                    //document.mainForm.SPECIAL_TYPE.value = "";
                    document.mainForm.SPECIAL_TYPE_DESCRIPTION.value = "";
                    return;
                }
                
              //   alert(getXMLNodeValue(xmlDoc, "DESCRIPTION_ENG"));	
                
                // Data found
                document.mainForm.SPECIAL_TYPE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_ENG");
            }
        }
        
        //-------------------------------------------  END SPECIAL TYPE ----------------------------------------------
        
            
            function DOCTOR_CATEGORY_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CATEGORY_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_CATEGORY() {
                var target = "../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CATEGORY_CODE.value = "";
                        document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function HOSPITAL_UNIT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.HOSPITAL_UNIT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_HOSPITAL_UNIT() {
                var target = "../../RetrieveData?TABLE=HOSPITAL_UNIT&COND=CODE='" + document.mainForm.HOSPITAL_UNIT_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_HOSPITAL_UNIT);
            }
            
            function AJAX_Handle_Refresh_HOSPITAL_UNIT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.HOSPITAL_UNIT_CODE.value = "";
                        document.mainForm.HOSPITAL_UNIT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.HOSPITAL_UNIT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DEPARTMENT_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.DEPARTMENT_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DEPARTMENT);
            }
            
            function AJAX_Handle_Refresh_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DEPARTMENT_CODE.value = "";
                        document.mainForm.DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function BANK_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.BANK_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_BANK() {
                var target = "../../RetrieveData?TABLE=BANK&COND=CODE='" + document.mainForm.BANK_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_BANK);
            }
            
            function AJAX_Handle_Refresh_BANK() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.BANK_CODE.value = "";
                        document.mainForm.BANK_DESCRIPTION.value = "";
                        //document.mainForm.SEARCH_BANK_BRANCH_CODE.disabled = false;
                        return;
                    }
                    
                    // Data found
                    document.mainForm.BANK_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                    //document.mainForm.SEARCH_BANK_BRANCH_CODE.disabled = true;

                    if(document.mainForm.BANK_BRANCH_CODE.value != ""){
                        AJAX_Refresh_BANK_BRANCH();
                    }
                }
            }
            
            function BANK_BRANCH_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.BANK_BRANCH_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_BANK_BRANCH() {
                var target = "../../RetrieveData?TABLE=BANK_BRANCH&COND=BANK_CODE='" + document.mainForm.BANK_CODE.value + "' AND CODE='" + document.mainForm.BANK_BRANCH_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_BANK_BRANCH);
            }
            
            function AJAX_Handle_Refresh_BANK_BRANCH() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.BANK_BRANCH_CODE.value = "";
                        document.mainForm.BANK_BRANCH_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.BANK_BRANCH_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "CODE");
                    
                    switch (document.mainForm.MODE.value) {
                        case "<%=DBMgr.MODE_INSERT%>" :
                            if (beExist) {
                                if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";
                                document.mainForm.submit();
                            }
                        }
                        else {
                            document.mainForm.submit();
                        }
                        break;
                    case "<%=DBMgr.MODE_UPDATE%>" :
                        if (beExist) {
                            document.mainForm.submit();
                        }
                        else {
                            alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                        }
                        break;
                    }
                }
            }

            function SAVE_Click() {
				if(document.mainForm.PAYMENT_MODE_CODE.value == 'B'){
				
					if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.DOCTOR_PROFILE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.NAME_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.NAME_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.DOCTOR_CATEGORY_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.DEPARTMENT_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.BANK_ACCOUNT_NO, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
						!isObjectEmptyString(document.mainForm.BANK_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
						!isObjectEmptyString(document.mainForm.BANK_BRANCH_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
						isObjectValidDate(document.mainForm.FROM_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
						isObjectValidDate(document.mainForm.TO_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
						isObjectValidDate(document.mainForm.GUARANTEE_START_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
						isObjectValidDate(document.mainForm.GUARANTEE_EXPIRE_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
						//isObjectValidNumber(document.mainForm.SALARY, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
						//isObjectValidNumber(document.mainForm.POSITION_AMT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
						isObjectValidNumber(document.mainForm.OVER_GUARANTEE_PCT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>')) {
					AJAX_VerifyData();
					}
				
				}else{
					if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.DOCTOR_PROFILE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.NAME_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.NAME_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.DOCTOR_CATEGORY_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						!isObjectEmptyString(document.mainForm.DEPARTMENT_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
						isObjectValidDate(document.mainForm.FROM_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
						isObjectValidDate(document.mainForm.TO_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
						isObjectValidDate(document.mainForm.GUARANTEE_START_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
						isObjectValidDate(document.mainForm.GUARANTEE_EXPIRE_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
						//isObjectValidNumber(document.mainForm.SALARY, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
						//isObjectValidNumber(document.mainForm.POSITION_AMT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
						isObjectValidNumber(document.mainForm.OVER_GUARANTEE_PCT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>')) {
                	AJAX_VerifyData();
            		}
				}
			}
            
            
            // ----------------------------- GUARANTEE INPROFILE -----------------------------------------
            
              function GUARANTEE_DR_CODE_KeyPress(e) {
        	
	        	var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
	
	            if (key == 13) { 
	            	document.mainForm.GUARANTEE_DR_CODE.blur();
	                return false;
	            }
	            else {
	                return true;
	            }
	        }

	        function AJAX_Refresh_GUARANTEE_DR_CODE() {
	        	
	        	var doctor_code_profile =  $("#DOCTOR_PROFILE_CODE").val();
	         	var action  =  $("input[name=GUARANTEE_PROFILE]").val();
	         	var target =  "";
	         	
	         	if(action == "Y"){
	        		target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.GUARANTEE_DR_CODE.value + "'";
	        		alert(target);
	        		AJAX_Request(target, AJAX_Handle_Refresh_GUARANTEE_DR_CODE);
	         	} else {
	        		target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.GUARANTEE_DR_CODE.value + "'";
	        		AJAX_Request(target, AJAX_Handle_Refresh_GUARANTEE_DR_CODE);
	         	}
	        	
	        }
        
	        function AJAX_Handle_Refresh_GUARANTEE_DR_CODE() {
	            if (AJAX_IsComplete()) {
	                var xmlDoc = AJAX.responseXML;
	
	                // Data not found
	                if (!isXMLNodeExist(xmlDoc, "CODE")) {
	                    //document.mainForm.SPECIAL_TYPE.value = "";
	                    document.mainForm.GUARANTEE_DR_CODE_DESCRIPTION.value = "";
	                    return;
	                }
	                
	                // Data found
	                document.mainForm.GUARANTEE_DR_CODE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "NAME_ENG");
	            }
	        }          
            
            //------------------------------END GUARANTEE  INPROFILE -----------------------------------
				
			$(function(){
				
				/**
				 	 Radio Onclick  Guarantee In Profile data.
				*/
				$("input[name=GUARANTEE_PROFILE]").click(function() { 
					
					var guaranteeInProfile = $(this).val();
					var doctorProfileCode = $("#DOCTOR_PROFILE_CODE").val();
		        	var hospitalCode = $("#hospital_code").val();
		         	 	 
		            	 //alert(valArrData[1]);
		                 $.ajax({
		                     url: 'compronent_select_guarantee_profile.jsp',
		                     type: 'POST',
		                     data: {
		                         is_guarantee_profile  : guaranteeInProfile , 
		                         doctor_profile_code  : doctorProfileCode , 
		                         hospital_code : hospitalCode
		                     } , 
		                     success: function(data) {
		                    	 $("#IS_GUARANTEE_PROFILE").html(data);
		                     }
		                 });
		           
				});
			});
			</script>
</head>    
<body>
        <form id="mainForm" name="mainForm" method="post" action="doctor_detail.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <input type="hidden" id="hospital_code" name="hospital_code" value="<%= session.getAttribute("HOSPITAL_CODE").toString() %>"/>
            <!--
            <input type="hidden" id="LICENSE_ID" name="LICENSE_ID" value="" />
            <input type="hidden" id="FROM_DATE" name="FROM_DATE" value="" />
            <input type="hidden" id="TO_DATE" name="TO_DATE" value="" />
            -->
            <table class="form">
                <tr>
                    <th colspan="4" class="buttonBar">
                    	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
						<% session.getAttribute("USER_GROUP_CODE").toString().equalsIgnoreCase("0"); %>
                        <input type="button" id="SAVE" name="SAVE" <%=dfuserLoginSave%> class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location.href='doctor_setup.jsp?CODE=<%=request.getParameter("DOCTOR_PROFILE_CODE")%>'" />
                    </th>
				</tr>
                <tr>
                    <td class="label">
                        <label for="DOCTOR_PROFILE_CODE">${labelMap.DOCTOR_PROFILE_CODE}*</label>                    
                    </td>
                    <td class="input" colspan="3">
                    	<input type="text" id="DOCTOR_PROFILE_CODE" name="DOCTOR_PROFILE_CODE" class="short" maxlength="20" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorProfileRec, "CODE") %>" />
                        <input type="text" id="DOCTOR_PROFILE_NAME" name="DOCTOR_PROFILE_NAME" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorProfileRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" />                    
                    </td>
                </tr>
                <tr>
	                  <td class="label">
	                    <label for="CODE"><span class="style1">${labelMap.CODE}*</span></label>                    
	                  </td>
	                  <td class="input">
	                        <input name="CODE" type="text" class="short" id="CODE" value="<%= DBMgr.getRecordValue(doctorRec, "CODE")%>" maxlength="50" <%=readonlyManager%> />                    </td>
	                  <td class="label">
	                        <label for="ACTIVE_1">${labelMap.ACTIVE}</label>                    </td>
	                   <td class="input">
	                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(doctorRec, "ACTIVE").equalsIgnoreCase("1") ? " checked=\"checked\"" : ""%> <%=dfuserLoginDisabled%> <%=disabledOrgDC %> <%=disabledAdmin %>/>
	                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
	                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(doctorRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> <%=dfuserLoginDisabled%> <%=disabledOrgDC %> <%=disabledAdmin %>/>
	                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                    
	                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="NAME_ENG"><span class="style1">${labelMap.NAME_ENG}*</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input name="NAME_ENG" type="text" class="long" id="NAME_ENG" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_ENG").equals("") ? DBMgr.getRecordValue(doctorProfileRec, "NAME_ENG") : DBMgr.getRecordValue(doctorRec, "NAME_ENG") %>" maxlength="255" <%=readonlyManager%>/>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="NAME_THAI"><span class="style1">${labelMap.NAME_THAI}*</span></label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="NAME_THAI" type="text" class="long" id="NAME_THAI" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_THAI").equals("") ? DBMgr.getRecordValue(doctorProfileRec, "NAME_THAI") : DBMgr.getRecordValue(doctorRec, "NAME_THAI") %>" maxlength="255"  <%=readonlyManager%>/>
					</td>
                </tr>
                <tr><th colspan="4">${labelMap.SUBTITLE_ADDRESS}</th></tr>
                <tr>
                    <td class="label">
                        <label for="ADDRESS1">${labelMap.ADDRESS1}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="ADDRESS1" type="text" class="long" id="ADDRESS1" value="<%= DBMgr.getRecordValue(doctorRec, "ADDRESS1")%>" maxlength="255" <%=readonlyManager%>/>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="ADDRESS2">${labelMap.ADDRESS2}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="ADDRESS2" type="text" class="long" id="ADDRESS2" value="<%= DBMgr.getRecordValue(doctorRec, "ADDRESS2")%>" maxlength="255" <%=readonlyManager%>/>                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="ADDRESS3">${labelMap.ADDRESS3}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="ADDRESS3" type="text" class="long" id="ADDRESS3" value="<%= DBMgr.getRecordValue(doctorRec, "ADDRESS3")%>" maxlength="255" <%=readonlyManager%>/>                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="ZIP">${labelMap.ZIP}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="ZIP" type="text" class="short" id="ZIP" value="<%= DBMgr.getRecordValue(doctorRec, "ZIP")%>" maxlength="50" <%=readonlyManager%>/>                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="EMAIL">${labelMap.EMAIL}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="EMAIL" type="text" class="long" id="EMAIL" value="<%= DBMgr.getRecordValue(doctorRec, "EMAIL")%>" maxlength="50" <%=readonlyManager%>/>                    </td>
                </tr>
                <tr><th colspan="4">${labelMap.SUBTITLE_INFORMATION}</th></tr>
				<tr>
                    <td class="label">
                        <label for="LICENSE_ID">${labelMap.LICENSE_ID}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="LICENSE_ID" type="text" class="short" id="LICENSE_ID" value="<%= DBMgr.getRecordValue(doctorRec, "LICENSE_ID")%>" maxlength="50"  <%=readonlyManager%>/>                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="FROM_DATE">${labelMap.FROM_DATE}</label>                    </td>
                    <td class="input">
                        <input name="FROM_DATE" type="text" class="short" id="FROM_DATE" value="<%= JDate.showDate(DBMgr.getRecordValue(doctorRec, "FROM_DATE"))%>" maxlength="10"  <%=readonlyManager%>/>
                        <input type="image" <%=disabledManager%> class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('FROM_DATE'); return false;" />                    </td>
                    <td class="label">
                        <label for="TO_DATE">${labelMap.TO_DATE}</label>                    </td>
                    <td class="input">
                        <input name="TO_DATE" type="text" class="short" id="TO_DATE" value="<%= JDate.showDate(DBMgr.getRecordValue(doctorRec, "TO_DATE"))%>" maxlength="10" <%=readonlyManager%>/>
                        <input type="image" <%=disabledManager%> class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('TO_DATE'); return false;" />                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="DOCTOR_TYPE_CODE"><span class="style1">${labelMap.DOCTOR_TYPE_CODE}*</span></label>                    </td>
                    <td class="input" colspan="3">
                        <%=DBMgr.generateDropDownList("DOCTOR_TYPE_CODE", "long", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM DOCTOR_TYPE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(doctorRec, "DOCTOR_TYPE_CODE"))%>                   
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="DOCTOR_GROUP_CODE"><span class="style1">${labelMap.DOCTOR_GROUP_CODE}*</span></label>                    </td>
                    <td class="input" colspan="3">
                    	<select id="DOCTOR_GROUP_CODE" name="DOCTOR_GROUP_CODE" class="long">
                    	<option value="">---กลุ่มแพทย์ ---</option>
                    	<option value="PT" class=""<%= DBMgr.getRecordValue(doctorRec, "DOCTOR_GROUP_CODE").equals("PT") ? " selected=\"selected\"" : "" %>>แพทย์ Part Time </option>
                    	<option value="FT" class="" <%= DBMgr.getRecordValue(doctorRec, "DOCTOR_GROUP_CODE").equals("FT") ? " selected=\"selected\"" : "" %> >แพทย์ Full Time</option>
                    	</select>
                    </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="DOCTOR_CATEGORY_CODE"><span class="style1">${labelMap.DOCTOR_CATEGORY_CODE}*</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input name="DOCTOR_CATEGORY_CODE" type="text" class="short" id="DOCTOR_CATEGORY_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "CODE")%>" onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CATEGORY();"  <%=readonlyManager%>/>
                        <input type="image" <%=disabledManager%> class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_CATEGORY_CODE&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY'); return false;" />
                        <input name="DOCTOR_CATEGORY_DESCRIPTION" type="text" class="long" id="DOCTOR_CATEGORY_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "DESCRIPTION")%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HOSPITAL_UNIT_CODE">${labelMap.HOSPITAL_UNIT_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="HOSPITAL_UNIT_CODE" name="HOSPITAL_UNIT_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(hospitalUnitRec, "CODE") %>" onkeypress="return HOSPITAL_UNIT_CODE_KeyPress(event);" onblur="AJAX_Refresh_HOSPITAL_UNIT();"  <%=readonlyManager%>/>
                        <input id="SEARCH_HOSPITAL_UNIT_CODE" name="SEARCH_HOSPITAL_UNIT_CODE" type="image" <%=disabledManager%> class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=HOSPITAL_UNIT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&TARGET=HOSPITAL_UNIT_CODE&HANDLE=AJAX_Refresh_HOSPITAL_UNIT'); return false;" />
                        <input type="text" id="HOSPITAL_UNIT_DESCRIPTION" name="HOSPITAL_UNIT_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(hospitalUnitRec, "DESCRIPTION") %>" />                    </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="DEPARTMENT_CODE"><span class="style1">${labelMap.DEPARTMENT_CODE}*</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input name="DEPARTMENT_CODE" type="text" class="short" id="DEPARTMENT_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(departmentRec, "CODE")%>" onkeypress="return DEPARTMENT_CODE_KeyPress(event);" onblur="AJAX_Refresh_DEPARTMENT();"  <%=readonlyManager%>/>
                        <input id="SEARCH_DEPARTMENT_CODE" name="SEARCH_DEPARTMENT_CODE" type="image" <%=disabledManager%> class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DEPARTMENT_CODE&HANDLE=AJAX_Refresh_DEPARTMENT'); return false;" />
                        <input name="DEPARTMENT_DESCRIPTION" type="text" class="long" id="DEPARTMENT_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(departmentRec, "DESCRIPTION")%>" />                    </td>
                </tr>            
                
                <tr>
                  	<td class="label">
                    	<label for="SPECIAL_TYPE"><span class="style1">${labelMap.SPECIAL_TYPE}</span></label>                    
                   	</td>
                    <td class="input" colspan="3">
                        <input name="SPECIAL_TYPE" type="text" class="short" id="SPECIAL_TYPE" maxlength="20" value="<%= DBMgr.getRecordValue(specialTypeRec, "CODE")%>"  onkeypress="return SPECIAL_TYPE_KeyPress(event);" onblur="AJAX_Refresh_SPECIAL_TYPE();"  <%=readonlyManager%>/>
                        <input id="SEARCH_SPECIAL_TYPE" name="SEARCH_SPECIAL_TYPE"  type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=SPECIAL_TYPE&DISPLAY_FIELD=DESCRIPTION_ENG&TARGET=SPECIAL_TYPE&HANDLE=AJAX_Refresh_SPECIAL_TYPE'); return false;" />
                        <input name="SPECIAL_TYPE_DESCRIPTION"  type="text" class="long" id="SPECIAL_TYPE_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(specialTypeRec, "DESCRIPTION_ENG")%>" />                    
                    </td>
                </tr>                
                    
				<tr><th colspan="4">${labelMap.GUARANTEE_INFORMATION}</th></tr>
                <tr>
                    <td class="label">
                        <label for="GUARANTEE_START_DATE">${labelMap.GUARANTEE_START_DATE}</label>
					</td>
                    <td class="input">
                        <input name="GUARANTEE_START_DATE" type="text" class="short" id="GUARANTEE_START_DATE" value="<%= JDate.showDate(DBMgr.getRecordValue(doctorRec, "GUARANTEE_START_DATE"))%>" maxlength="10"  <%=readonlyManager%>/>
                        <input type="image" <%=disabledManager%> class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('GUARANTEE_START_DATE'); return false;" />                    </td>
                    <td class="label">
                        <label for="GUARANTEE_EXPIRE_DATE">${labelMap.GUARANTEE_EXPIRE_DATE}</label>                    </td>
                    <td class="input">
                        <input name="GUARANTEE_EXPIRE_DATE" type="text" class="short" id="GUARANTEE_EXPIRE_DATE" value="<%= JDate.showDate(DBMgr.getRecordValue(doctorRec, "GUARANTEE_EXPIRE_DATE"))%>" maxlength="10"  <%=readonlyManager%>/>
                        <input type="image" <%=disabledManager%> class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('GUARANTEE_EXPIRE_DATE'); return false;" />                    </td>
                </tr>
                 <tr>
                    <td class="label">
                        <label for="GUARANTEE_PER_HOUR">${labelMap.GUARANTEE_PER_HOUR}</label></td>
                    <td class="input">
                        <input id="GUARANTEE_PER_HOUR" name="GUARANTEE_PER_HOUR" type="text" class="short alignRight" value="<%= DBMgr.getRecordValue(doctorRec, "GUARANTEE_PER_HOUR")%>"  <%=readonlyManager%>/></td>
                    <td class="label">
                        <label for="EXTRA_PER_HOUR">${labelMap.EXTRA_PER_HOUR}</label></td>
                    <td class="input">
                        <input id="EXTRA_PER_HOUR" name="EXTRA_PER_HOUR" type="text" class="short alignRight" value="<%= DBMgr.getRecordValue(doctorRec, "EXTRA_PER_HOUR")%>"  <%=readonlyManager%>/></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="TAX_ID">${labelMap.IN_GUARANTEE_PCT}</label></td>
                    <td class="input">
                        <input id="IN_GUARANTEE_PCT" name="IN_GUARANTEE_PCT" type="text" class="short alignRight" value="<%= DBMgr.getRecordValue(doctorRec, "IN_GUARANTEE_PCT")%>" maxlength="4"  <%=readonlyManager%>/>(%)</td>
                    <td class="label">
                        <label for="TAX_ID">${labelMap.OVER_GUARANTEE_PCT}</label></td>
                    <td class="input">
                        <input id="OVER_GUARANTEE_PCT" name="OVER_GUARANTEE_PCT" type="text" class="short alignRight" value="<%= DBMgr.getRecordValue(doctorRec, "OVER_GUARANTEE_PCT")%>" maxlength="4"  <%=readonlyManager%>/>(%)</td>
                </tr>
                <tr>
<!--                     <td class="label"> -->
<%--                         <label for="TAX_ID">${labelMap.GUARANTEE_PROFILE}</label> --%>
<!--                     </td> -->
<!--                     <td class="input"> -->
<%--                      	<input id="GUARANTEE_PROFILE1" name="GUARANTEE_PROFILE" type="radio" value="Y"  <%if(DBMgr.getRecordValue(doctorRec, "IS_GUARANTEE_PROFILE").equals("Y")){out.println("checked");}%>/>Yes  --%>
<%--                       	<input id="GUARANTEE_PROFILE2" name="GUARANTEE_PROFILE" type="radio" value="N"  <%if(DBMgr.getRecordValue(doctorRec, "IS_GUARANTEE_PROFILE").equals("N")){out.println("checked");}%>/>No --%>
<!--                     </td> -->
                    <td class="label">
                        <label for="GUARANTEE_DR_CODE">${labelMap.GUARANTEE_DR_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                    	<%
                        	 String  sqlGetDescriptDoctor = "SELECT * FROM DOCTOR WHERE CODE =  '" + DBMgr.getRecordValue(doctorRec, "GUARANTEE_DR_CODE")  +"'  AND  HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE") + "' ";
                        	 DataRecord getDoctorCodeDescription  =   DBMgr.getRecord(sqlGetDescriptDoctor);
                        %>
                    	<input type="text" id="GUARANTEE_DR_CODE" name="GUARANTEE_DR_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(doctorRec, "GUARANTEE_DR_CODE") %>" onkeypress="return GUARANTEE_DR_CODE_KeyPress(event);" onblur="return AJAX_Refresh_GUARANTEE_DR_CODE();"  <%=dfuserLoginReadonly%>  <%=readonlyManagerV2%>/>
                        <input id="SEARCH_GUARANTEE_DR_CODE" name="SEARCH_GUARANTEE_DR_CODE"  type="image" <%=dfuserLoginDisabled%> <%=disabledManagerV2 %> class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&BEACTIVE=1&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&TARGET=GUARANTEE_DR_CODE&HANDLE=AJAX_Refresh_GUARANTEE_DR_CODE'); return false;" />
                        <input type="text" id="GUARANTEE_DR_CODE_DESCRIPTION" name="GUARANTEE_DR_CODE_DESCRIPTION" class="long" readonly="readonly" value="<%=DBMgr.getRecordValue(doctorRec, "GUARANTEE_DR_CODE").equals("") ? "" :  DBMgr.getRecordValue(getDoctorCodeDescription, "NAME_" + labelMap.getFieldLangSuffix()) %>" />                   
                        </td>
                    
                </tr>	
                <tr>
	                <td class="label">
	                    <label for="GUARANTEE_SOURCE">${labelMap.GUARANTEE_SOURCE}</label>
	                </td>
	                <td class="input" colspan="">
	                    <select id="GUARANTEE_SOURCE" name="GUARANTEE_SOURCE" class="medium">
	                        <option value="NONE"<%= DBMgr.getRecordValue(doctorRec, "GUARANTEE_SOURCE").equalsIgnoreCase("NONE") ? " selected=\"selected\"" : "" %>>-- Not specified --</option>
	                        <option value="${labelMap.GUARANTEE_SOURCE_TAX_VALUE}"<%= DBMgr.getRecordValue(doctorRec, "GUARANTEE_SOURCE").equalsIgnoreCase("BF") ? " selected=\"selected\"" : "" %>>${labelMap.GUARANTEE_SOURCE_TAX}</option>
	                        <option value="${labelMap.GUARANTEE_SOURCE_DF_VALUE}"<%= DBMgr.getRecordValue(doctorRec, "GUARANTEE_SOURCE").equalsIgnoreCase("AF") ? " selected=\"selected\"" : "" %>>${labelMap.GUARANTEE_SOURCE_DF}</option>
	                    </select>                   
	                </td>
	                <td class="label">
                        <label for="GUARANTEE_DAY">${labelMap.GUARANTEE_DAY}</label>
                    </td>
                    <td class="input">
                        <select id="GUARANTEE_DAY" name="GUARANTEE_DAY" class="medium">
                            <option value=""<%= !DBMgr.getRecordValue(doctorRec, "GUARANTEE_DAY").equalsIgnoreCase("VER") && !DBMgr.getRecordValue(doctorRec, "GUARANTEE_DAY").equalsIgnoreCase("INV") ? " selected=\"selected\"" : "" %>>${labelMap.GUARANTEE_DAY_UNDEFINE}</option>
                            <option value="${labelMap.GUARANTEE_DAY_VERIFY_VALUE}"<%= DBMgr.getRecordValue(doctorRec, "GUARANTEE_DAY").equalsIgnoreCase("VER") ? " selected=\"selected\"" : "" %>>${labelMap.GUARANTEE_DAY_VERIFY}</option>
                            <option value="${labelMap.GUARANTEE_DAY_INVOICE_VALUE}"<%= DBMgr.getRecordValue(doctorRec, "GUARANTEE_DAY").equalsIgnoreCase("INV") ? " selected=\"selected\"" : "" %>>${labelMap.GUARANTEE_DAY_INVOICE}</option>
                        </select>                   
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="TIME_TABLE">${labelMap.TIME_TABLE}</label>
                    </td>
                    <td class="input" colspan="3" valign="middle">
                        <a href="time_table.jsp?DOCTOR_PROFILE_CODE=<%=DBMgr.getRecordValue(doctorProfileRec, "CODE")%>&CODE=<%=DBMgr.getRecordValue(doctorRec, "CODE")%>"><img src="../../images/clock.png" border="1"></a>
					</td>
                </tr>
                <tr><th colspan="4">${labelMap.SUBTITLE_PAYMENT_INFORMATION}</th></tr>
				<tr>
                    <td class="label">
                        <label for="PAYMENT_MODE_CODE"><span class="style1">${labelMap.PAYMENT_MODE_CODE}*</span></label>                    </td>
                    <td class="input" colspan="">
                        <%= DBMgr.generateDropDownList("PAYMENT_MODE_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM PAYMENT_MODE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", 
                        		DBMgr.getRecordValue(doctorRec, "PAYMENT_MODE_CODE").equalsIgnoreCase("")? "CQ" : DBMgr.getRecordValue(doctorRec, "PAYMENT_MODE_CODE")) %>                                     
                    </td>
                    <td class="label">
                        <label for="INCLUDE_REVENUE_CODE"><span class="">${labelMap.INCLUDE_REVENUE_CODE}</span></label>
                    </td>
                    <td class="input" colspan="">    
                    	<input name="INCLUDE_REVENUE_CODE" type="text" class="short" id="INCLUDE_REVENUE_CODE" value="<%= DBMgr.getRecordValue(doctorRec, "DOCTOR_TAX_CODE")%>" maxlength="50"  <%=readonlyManager%>/>
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_ENG&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=INCLUDE_REVENUE_CODE&HANDLE=dummyFunction'); return false;"/>
                    </td>
                </tr>                
                <tr>
                    <td class="label"><label for="IS_ADVANCE_PAYMENT_Y"><span class="style1">${labelMap.IS_ADVANCE_PAYMENT}*</span></label></td>
                    <td class="input">
                        <input type="radio" id="IS_ADVANCE_PAYMENT_Y" name="IS_ADVANCE_PAYMENT" value="Y"<%= DBMgr.getRecordValue(doctorRec, "IS_ADVANCE_PAYMENT").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %>  <%=readonlyManager%>/>
                        <label for="ACTIVE_1">${labelMap.IS_ADVANCE_PAYMENT_Y}</label>
                        <input type="radio" id="IS_ADVANCE_PAYMENT_N" name="IS_ADVANCE_PAYMENT" value="N"<%= DBMgr.getRecordValue(doctorRec, "IS_ADVANCE_PAYMENT").equalsIgnoreCase("N") || DBMgr.getRecordValue(doctorRec, "IS_ADVANCE_PAYMENT")=="" ? " checked=\"checked\"" : "" %>  <%=readonlyManager%>/>
                        <label for="IS_ADVANCE_PAYMENT_N">${labelMap.IS_ADVANCE_PAYMENT_N}</label>                    </td>
                    <td class="label"><label for="IS_HOLD_Y"><span class="style1">${labelMap.IS_HOLD}*</span></label></td>
                    <td class="input">
                        <input type="radio" id="IS_HOLD_Y" name="IS_HOLD" value="2"<%= DBMgr.getRecordValue(doctorRec, "PAYMENT_TIME").equalsIgnoreCase("2") ? " checked=\"checked\"" : "" %>  <%=readonlyManager%>/>
                        <label for="ACTIVE_1">${labelMap.IS_HOLD_Y}</label>
                        <input type="radio" id="IS_HOLD_N" name="IS_HOLD" value="1"<%= DBMgr.getRecordValue(doctorRec, "PAYMENT_TIME").equalsIgnoreCase("1") || DBMgr.getRecordValue(doctorRec, "PAYMENT_TIME").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %>  <%=readonlyManager%>/>
                        <label for="IS_HOLD_N">${labelMap.IS_HOLD_N}</label>
                    </td>
                </tr>
                <tr>
                  <td class="label">
                      <label for="TAX_ID"><span class="style2">${labelMap.TAX_ID}</span></label>                    </td>
                    <td class="input">
                        <input name="TAX_ID" type="text" class="medium" id="TAX_ID" value="<%= DBMgr.getRecordValue(doctorRec, "TAX_ID")%>" maxlength="50"  <%=readonlyManager%>/>                    </td>
                    <td class="label"><label for="PAY_TAX_402_BY"><span class="style1">${labelMap.PAY_TAX_402_BY}*</span></label></td>
                    <td class="input">
                        <%= DBMgr.generateDropDownList("PAY_TAX_402_BY", "", taxValue, taxGT, (DBMgr.getRecordValue(doctorRec, "PAY_TAX_402_BY")=="" ? "0" : DBMgr.getRecordValue(doctorRec, "PAY_TAX_402_BY") ) ) %>                       
                    </td>
                </tr>
                <tr>
                	<td class="label">
	                    <label for="TAX_402_METHOD">${labelMap.TAX_402_METHOD}</label>
	                </td>
	               	<td class="input">
	               		<select id="TAX_402_METHOD" name="TAX_402_METHOD" class="medium">
	                        <option value="" >-- Not specified --</option>
	                        <option value="SUM"<%= DBMgr.getRecordValue(doctorRec, "TAX_402_METHOD").equalsIgnoreCase("SUM") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_SUM}</option>
	                        <option value="STP"<%= DBMgr.getRecordValue(doctorRec, "TAX_402_METHOD").equalsIgnoreCase("STP")||DBMgr.getRecordValue(doctorRec, "TAX_402_METHOD").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_STEP}</option>
	                        <option value="3"<%= DBMgr.getRecordValue(doctorRec, "TAX_402_METHOD").equalsIgnoreCase("3") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_3}</option>
	                        <option value="5"<%= DBMgr.getRecordValue(doctorRec, "TAX_402_METHOD").equalsIgnoreCase("5") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_5}</option>
	                        <option value="14"<%= DBMgr.getRecordValue(doctorRec, "TAX_402_METHOD").equalsIgnoreCase("14") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_14}</option>
	                        <option value="15"<%= DBMgr.getRecordValue(doctorRec, "TAX_402_METHOD").equalsIgnoreCase("15") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_15}</option>
	                	</select>	                	
	         		</td>
                	<td class="label">
	                    <label for="TAX_406_METHOD">${labelMap.TAX_406_METHOD}</label>
	                </td>
	               	<td class="input">
	               		<select id="TAX_406_METHOD" name="TAX_406_METHOD" class="medium">
	                        <option value=""<%= DBMgr.getRecordValue(doctorRec, "TAX_406_METHOD").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>-- Not specified --</option>
	                        <option value="SUM"<%= DBMgr.getRecordValue(doctorRec, "TAX_406_METHOD").equalsIgnoreCase("SUM")|| DBMgr.getRecordValue(doctorRec, "TAX_406_METHOD").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_SUM}</option>
	                        <option value="STP"<%= DBMgr.getRecordValue(doctorRec, "TAX_406_METHOD").equalsIgnoreCase("STP") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_STEP}</option>
	                        <option value="3"<%= DBMgr.getRecordValue(doctorRec, "TAX_406_METHOD").equalsIgnoreCase("3") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_3}</option>
	                        <option value="5"<%= DBMgr.getRecordValue(doctorRec, "TAX_406_METHOD").equalsIgnoreCase("5") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_5}</option>
	                        <option value="14"<%= DBMgr.getRecordValue(doctorRec, "TAX_406_METHOD").equalsIgnoreCase("14") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_14}</option>
	                        <option value="15"<%= DBMgr.getRecordValue(doctorRec, "TAX_406_METHOD").equalsIgnoreCase("15") ? " selected=\"selected\"" : "" %>>${labelMap.TAX_15}</option>
	                	</select>	                	
	         		</td>
                </tr>
				<tr><th colspan="4">${labelMap.SUBTITLE_BANK_ACCOUNT_INFORMATION}</th></tr>
                <tr>
                    <td class="label">
                        <label id="BANK_ACCOUNT_LABEL" for="BANK_ACCOUNT_NO">${labelMap.BANK_ACCOUNT_NO}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="BANK_ACCOUNT_NO" type="text" class="medium" id="BANK_ACCOUNT_NO" value="<%= DBMgr.getRecordValue(doctorRec, "BANK_ACCOUNT_NO")%>" maxlength="11"  <%=dfuserLoginReadonly%> <%=readonlyManagerV2%>/>                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="BANK_ACCOUNT_NAME">${labelMap.BANK_ACCOUNT_NAME}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="BANK_ACCOUNT_NAME" type="text" class="long" id="BANK_ACCOUNT_NAME" value="<%= DBMgr.getRecordValue(doctorRec, "BANK_ACCOUNT_NAME")%>" maxlength="255"  <%=dfuserLoginReadonly%> <%=readonlyManagerV2%>/>                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="BANK_CODE">${labelMap.BANK_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="BANK_CODE" name="BANK_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(bankRec, "CODE") %>" onkeypress="return BANK_CODE_KeyPress(event);" onblur="AJAX_Refresh_BANK();"  <%=dfuserLoginReadonly%>  <%=readonlyManagerV2%>/>
                        <input id="SEARCH_BANK_CODE" name="SEARCH_BANK_CODE" type="image" <%=dfuserLoginDisabled%> <%=disabledManagerV2 %> class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=BANK&BEACTIVE=1&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=BANK_CODE&HANDLE=AJAX_Refresh_BANK'); return false;" />
                        <input type="text" id="BANK_DESCRIPTION" name="BANK_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(bankRec, "DESCRIPTION") %>" />                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="BANK_BRANCH_CODE">${labelMap.BANK_BRANCH_CODE}</label>                    </td>
                    <td class="input" colspan="3"><input type="text" id="BANK_BRANCH_CODE" name="BANK_BRANCH_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(bankBranchRec, "CODE") %>" onkeypress="return BANK_BRANCH_CODE_KeyPress(event);" onblur="AJAX_Refresh_BANK_BRANCH();"  <%=dfuserLoginReadonly%>  <%=readonlyManagerV2%>/>
                      	<input id="SEARCH_BANK_BRANCH_CODE" name="SEARCH_BANK_BRANCH_CODE" type="image" <%=dfuserLoginDisabled%> <%=disabledManagerV2 %> class="image_button" src="../../images/search_button.png" alt="Search" onclick="return checkBankBranchCode()" />
                        <input type="text" id="BANK_BRANCH_DESCRIPTION" name="BANK_BRANCH_DESCRIPTION" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(bankBranchRec, "DESCRIPTION") %>" />
                        <%
            			if(doctorRec!=null){
            				%>
            				<input type="hidden" name="TEMP_BANK_ACCOUNT_NO" value="<%= DBMgr.getRecordValue(doctorRec, "BANK_ACCOUNT_NO")%>"/>
            				<input type="hidden" name="TEMP_BANK_ACCOUNT_NAME" value="<%= DBMgr.getRecordValue(doctorRec, "BANK_ACCOUNT_NAME")%>"/>
            				<input type="hidden" name="TEMP_BANK_CODE" value="<%= DBMgr.getRecordValue(doctorRec, "BANK_CODE") %>"/>
            				<input type="hidden" name="TEMP_BANK_BRANCH_CODE" value="<%= DBMgr.getRecordValue(doctorRec, "BANK_BRANCH_CODE") %>"/>
            				<input type="hidden" name="ACTIVE" value="<%= DBMgr.getRecordValue(doctorRec, "ACTIVE") %>"/>
            				<%	
            			}                        
                        %>                  
                    </td>
                </tr>
                <tr><th colspan="4">${labelMap.SUBTITLE_OTHER}</th></tr>
                <tr>
                    <td class="label">
                        <label for="NOTE">${labelMap.NOTE}</label>                    </td>
                    <td class="input" colspan="3">
                        <textarea id="NOTE" name="NOTE" class="long" rows="5" <%=readonlyManager%>><%= DBMgr.getRecordValue(doctorRec, "NOTE")%></textarea>                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
						<% session.getAttribute("USER_GROUP_CODE").toString().equalsIgnoreCase("0"); %>
                        <input type="button" id="SAVE" name="SAVE" <%=dfuserLoginSave%> class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location.href='doctor_setup.jsp?CODE=<%=request.getParameter("DOCTOR_PROFILE_CODE")%>'" />
                    </th>
                </tr>
            </table>
            <%
            if("readonly=\"readonly\"".equalsIgnoreCase(readonlyManager)){
                %>
                <input type="hidden" name="IS_HOLD" value="<%=DBMgr.getRecordValue(doctorRec, "IS_HOLD") %>"/>
                <input type="hidden" name="GUARANTEE_DR_CODE" value="<%=DBMgr.getRecordValue(doctorRec, "GUARANTEE_DR_CODE") %>"/>
                <input type="hidden" name="IS_ADVANCE_PAYMENT" value="<%=DBMgr.getRecordValue(doctorRec, "IS_ADVANCE_PAYMENT") %>"/>
                <input type="hidden" name="DOCTOR_TYPE_CODE" value="<%=DBMgr.getRecordValue(doctorRec, "DOCTOR_TYPE_CODE") %>"/>
                <input type="hidden" name="GUARANTEE_SOURCE" value="<%=DBMgr.getRecordValue(doctorRec, "GUARANTEE_SOURCE") %>"/>
                <input type="hidden" name="PAYMENT_MODE_CODE" value="<%=DBMgr.getRecordValue(doctorRec, "PAYMENT_MODE_CODE") %>"/>
                <input type="hidden" name="PAY_TAX_402_BY" value="<%=DBMgr.getRecordValue(doctorRec, "PAY_TAX_402_BY") %>"/>
                <%
            }
            if("readonly=\"readonly\"".equalsIgnoreCase(disabledOrgDC)){
                %>
                <input type="hidden" name="ACTIVE" value="<%=DBMgr.getRecordValue(doctorRec, "ACTIVE") %>"/>
                <%
            }            
            %>
        </form>
        
        <br />
        <br />
        <br />
        <br />
        
        <script language="javascript">
					function checkBankBranchCode(){
				        if(document.mainForm.BANK_DESCRIPTION.value==""){
				            alert("${labelMap.ALERT_BANK}");
				            document.mainForm.BANK_CODE.focus();
				        }else{
				            openSearchForm('../search.jsp?TABLE=BANK_BRANCH&BEACTIVE=1&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&COND=[ and BANK_CODE=\''+ document.mainForm.BANK_CODE.value +'\']&TARGET=BANK_BRANCH_CODE&HANDLE=AJAX_Refresh_BANK_BRANCH');
				        }
				        return false;
					}

				    if('readonly=\"readonly\"'=='<%=readonlyManager%>'){
				        //select
				        //radio 
				        document.mainForm.PAYMENT_MODE_CODE[0].disabled = true;
				        document.mainForm.PAY_TAX_402_BY[0].disabled = true;
				        document.mainForm.GUARANTEE_SOURCE[0].disabled = true;       
				        document.mainForm.IS_ADVANCE_PAYMENT[0].disabled = true;
				        //document.mainForm.IS_ADVANCE_PAYMENT[1].disabled = true;
				
				        document.mainForm.GUARANTEE_DR_CODE[0].disabled = true;
				        //document.mainForm.GUARANTEE_DR_CODE[1].disabled = true;
				
				        document.mainForm.IS_HOLD[0].disabled = true;
				      	//document.mainForm.IS_HOLD[1].disabled = true;
				
				        //DOCTOR_TYPE_CODE
				        document.mainForm.DOCTOR_TYPE_CODE[0].disabled = true;
				        //document.getElementById("GUARANTEE_DR_CODE").disabled = true;
				    }

					function changePaymentMode(){
					    var e_select = document.mainForm.PAYMENT_MODE_CODE;
					    var e_text = document.getElementById("BANK_ACCOUNT_LABEL");
					    if(e_select.value=='B'){
					        e_text.innerHTML = e_text.innerHTML + '*';
					        e_text.style.color = '#003399';
					    }else{
					        var str = e_text.innerHTML;
					        e_text.innerHTML = str.replace('*','');
					        e_text.style.color = '#333';
					    }
					}
				
			</script>

    </body>
</html>