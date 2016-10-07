<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
            //
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
            labelMap.add("TITLE_MAIN", "Time Table", "ตารางแพทย์");
            labelMap.add("GUARANTEE_TYPE", "Guarantee Type", "ประเภทการันตี");
            labelMap.add("GUARANTEE_DATE", "Guarantee Date", "วันที่การันตี");
            labelMap.add("GUARANTEE_DAY", "Guarantee Day", "วันการันตี");
            labelMap.add("GUARANTEE_AMOUNT", "Guarantee Amount", "จำนวนเงินการันตี");
            labelMap.add("GUARANTEE_TIME", "Guarantee Time", "เวลาการันตี");
            labelMap.add("GUARANTEE_FIX_AMOUNT", "Guarantee Fix Amount", "จำนวนฟิกการันตี");
            labelMap.add("GUARANTEE_INCLUDE_AMOUNT", "Guarantee Include Amount", "จำนวนเงินรวมการันตี");
            labelMap.add("IN_GUARANTEE", "In Guarantee", "ในการันตี");
            labelMap.add("OVER_GUARANTEE", "Over Guarantee", "เกินการันตี");
            labelMap.add("GUARANTEE_EXCLUDE_AMOUNT", "Extra Amount", "จำนวนเงินค่าเวร");
            labelMap.add("CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("GUARANTEE_DAILY_HEAD","Guarantee Daily","การันตีรายวัน");
            labelMap.add("GUARANTEE_MONTHLY_FIX_HEAD","Guarantee Monthly Fix Day","การันตีรายเดือนแบบกำหนดวัน");
            labelMap.add("GUARANTEE_DAILY_TO_MONTHLY_HEAD", "Guarantee Daily To Monthly", "Guarantee Daily To Monthly");
            labelMap.add("GUARANTEE_DATE_RANGE","Date 1 - End Month","วันที่ 1 ถึงสิ้นเดือน");
            labelMap.add("","","");
            request.setAttribute("labelMap", labelMap.getHashMap());

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorRec = null, doctorProfileRec = null, doctorTimeTable = null;
            byte MODE = DBMgr.MODE_INSERT;

            // for update
            ResultSet rs = null;
            String[] LABEL_DAY = {"วันจันทร์","วันอังคาร","วันพุธ","วันพฤหัสบดี","วันศุกร์","วันเสาร์","วันอาทิตย์"};
            String[] VALUE_DAY = {"mon","tue","wed","thu","fri","sat","sun"};
            String DL = "";
            String MLY = "";
            String MMY = "";
            String MLD = "";
            String MLA = "";
            String STP = "";
            
            String[] DLY_CH = {"","","","","","",""};
            String[] DLYSTART = {"","","","","","",""};
            String[] DLYSTOP = {"","","","","","",""};
            String[] DLYGURAN = {"","","","","","",""};
            String[] DLYTURN = {"","","","","","",""};
            String[] dly_fix = {"","","","","","",""};
            //String[] dly_include = {"","","","","","",""};

            String[] MLY_CH = {"","","","","","",""};
            String[] MLYSTART = {"","","","","","",""};
            String[] MLYSTOP = {"","","","","","",""};
            String[] MLYGURAN = {"","","","","","",""};
            String[] MLYTURN = {"","","","","","",""};
            String[] mly_fix = {"","","","","","",""};
            //String[] mly_include =  {"","","","","","",""}; 
            
            String[] MLD_CH = {"","","","","","",""};
            String[] MLDSTART = {"","","","","","",""};
            String[] MLDSTOP = {"","","","","","",""};
            String[] MLDGURAN = {"","","","","","",""};
            String[] MLDTURN = {"","","","","","",""};
            String[] mld_fix = {"","","","","","",""};
			
			String MLAGURAN = "";
			String MLATURN = "";//Exclude amount
			String mla_fix_amount = "";
			String mla_include_amount = "";

			String MMYGURAN = "";
			String MMYTURN = "";//Exclude amount
			String mmy_fix_amount = "";
			String mmy_include_amount = "";

			String stp_guarantee_amount = "";
			String stp_in_guarantee = "";
			String stp_out_guarantee = "";
			String stp_exclude_amount = "";
			String stp_fix_amount = "";
			String stp_include_amount = "";

            if (request.getParameter("MODE") != null) {
                String[] type_guarantee = request.getParameterValues("type_guarantee");
                String HOSPITAL_CODE = session.getAttribute("HOSPITAL_CODE").toString();
                String DOCTOR_CODE = request.getParameter("CODE") ;
                String DOCTOR_PROFILE_CODE = request.getParameter("DOCTOR_PROFILE_CODE") ;
                String GUARANTEE_TYPE = "";
                String DAY = "";
                String START_TIME = "";
                String STOP_TIME = "";
                //String DL = "";
                //String MLY = "";
                //String MLA = "";

                DataRecord record = new DataRecord("DOCTOR_TIME_TABLE");
                record.addField("HOSPITAL_CODE", Types.VARCHAR, HOSPITAL_CODE, true);
                record.addField("DOCTOR_CODE", Types.VARCHAR, DOCTOR_CODE, true);
                DBMgr.deleteRecord(record);

                if(type_guarantee==null){
                    response.sendRedirect("doctor_detail.jsp?CODE="+DOCTOR_CODE+"&DOCTOR_PROFILE_CODE="+DOCTOR_PROFILE_CODE);
                    return;
                }
                for(int i_type=0 ; i_type < type_guarantee.length ; i_type++){
                    if("DLY".equalsIgnoreCase(type_guarantee[i_type].toString())){
                        DL = "DLY";
                    }else if("MLD".equalsIgnoreCase(type_guarantee[i_type].toString())){
                    	MLD = "MLD";
                    }else if("MLY".equalsIgnoreCase(type_guarantee[i_type].toString())){
                        MLY = "MLY";
                    }else if("MLA".equalsIgnoreCase(type_guarantee[i_type].toString())){
                        MLA = "MLA";
                    }else if("MMY".equalsIgnoreCase(type_guarantee[i_type].toString())){
                        MMY = "MMY";
                    }else if("STP".equalsIgnoreCase(type_guarantee[i_type].toString())){
                        STP = "STP";
                    }
                    //out.println(type_guarantee[i_type].toString());
                    //out.println("<hr>");
                }  
                
                String[] DAY_CH = request.getParameterValues("DAY_CH");
                String[] DAY_START = request.getParameterValues("DAY_START");
                String[] DAY_STOP = request.getParameterValues("DAY_STOP");
                String[] DAY_GT = request.getParameterValues("DAY_GUARANTEE_AMOUNT");
                String[] DAY_TU = request.getParameterValues("DAY_TURN_AMOUNT");
                String[] DAY_FIX = request.getParameterValues("DAY_FIX_AMOUNT");
                //String[] DAY_INC = request.getParameterValues("DAY_INCLUDE_AMOUNT");
                if(DL.equalsIgnoreCase("DLY")){
                    GUARANTEE_TYPE = "DLY";
                    if(DAY_CH!=null){
                        for(int i_daych=0; i_daych < DAY_CH.length ; i_daych++){
                            DAY = DAY_CH[i_daych].toString();
                            START_TIME = DAY_START[i_daych].toString();
                            STOP_TIME = DAY_STOP[i_daych].toString();
                            
                            DataRecord dtInsert = new DataRecord("DOCTOR_TIME_TABLE");
                            dtInsert.addField("HOSPITAL_CODE", Types.VARCHAR , HOSPITAL_CODE, true);
                            dtInsert.addField("DOCTOR_CODE", Types.VARCHAR , DOCTOR_CODE, true);
                            dtInsert.addField("GUARANTEE_TYPE", Types.VARCHAR , GUARANTEE_TYPE, true);
                            dtInsert.addField("DAY", Types.VARCHAR , DAY, true);
                            dtInsert.addField("START_TIME", Types.VARCHAR , JDate.saveTime(START_TIME), true );
                            dtInsert.addField("STOP_TIME", Types.VARCHAR , JDate.saveTime(STOP_TIME));
                            dtInsert.addField("GUARANTEE_AMOUNT", Types.NUMERIC, DAY_GT[i_daych]);
                            dtInsert.addField("GUARANTEE_EXCLUDE_AMOUNT", Types.NUMERIC, DAY_TU[i_daych]);
                            dtInsert.addField("GUARANTEE_FIX_AMOUNT", Types.NUMERIC, DAY_FIX[i_daych]);//
                            //dtInsert.addField("GUARANTEE_INCLUDE_AMOUNT", Types.NUMERIC, DAY_INC[i_daych]);//                            
                            dtInsert.addField("USER_ID",Types.VARCHAR, session.getAttribute("USER_ID").toString());
                            dtInsert.addField("UPDATE_DATE",Types.VARCHAR, JDate.getDate());
                            dtInsert.addField("UPDATE_TIME",Types.VARCHAR, JDate.getTime());
                            DBMgr.insertRecord(dtInsert);
                        }
                    }
                }

                String[] MTD_CH = request.getParameterValues("MTD_CH");
                String[] MTD_START = request.getParameterValues("MTD_START");
                String[] MTD_STOP = request.getParameterValues("MTD_STOP");
                String[] MTD_GT = request.getParameterValues("MTD_GUARANTEE_AMOUNT");
                String[] MTD_TU = request.getParameterValues("MTD_TURN_AMOUNT");
                String[] MTD_FIX = request.getParameterValues("MTD_FIX_AMOUNT");
                //String[] MTD_INC = request.getParameterValues("MTD_INCLUDE_AMOUNT");
                if(MLD.equalsIgnoreCase("MLD")){
                    GUARANTEE_TYPE = "MLD";
                    if(MTD_CH!=null){
                        for(int i_daych=0; i_daych < MTD_CH.length ; i_daych++){
                            DAY = MTD_CH[i_daych].toString();
                            START_TIME = MTD_START[i_daych].toString();
                            STOP_TIME = MTD_STOP[i_daych].toString();

                            DataRecord dtInsert = new DataRecord("DOCTOR_TIME_TABLE");
                            dtInsert.addField("HOSPITAL_CODE", Types.VARCHAR , HOSPITAL_CODE, true);
                            dtInsert.addField("DOCTOR_CODE", Types.VARCHAR , DOCTOR_CODE, true);
                            dtInsert.addField("GUARANTEE_TYPE", Types.VARCHAR , GUARANTEE_TYPE, true);
                            dtInsert.addField("DAY", Types.VARCHAR , DAY, true);
                            dtInsert.addField("START_TIME", Types.VARCHAR , JDate.saveTime(START_TIME), true );
                            dtInsert.addField("STOP_TIME", Types.VARCHAR , JDate.saveTime(STOP_TIME) );
                            dtInsert.addField("GUARANTEE_AMOUNT", Types.NUMERIC, MTD_GT[i_daych]);
                            dtInsert.addField("GUARANTEE_EXCLUDE_AMOUNT", Types.NUMERIC, MTD_TU[i_daych]);
                            dtInsert.addField("GUARANTEE_FIX_AMOUNT", Types.NUMERIC, MTD_FIX[i_daych]);//
                            //dtInsert.addField("GUARANTEE_INCLUDE_AMOUNT", Types.NUMERIC, MTH_INC[i_daych]);//                            
                            dtInsert.addField("USER_ID",Types.VARCHAR, session.getAttribute("USER_ID").toString());
                            dtInsert.addField("UPDATE_DATE",Types.VARCHAR, JDate.getDate());
                            dtInsert.addField("UPDATE_TIME",Types.VARCHAR, JDate.getTime());                            
                            DBMgr.insertRecord(dtInsert);
                        }
                    }
                }  
                
                String[] MTH_CH = request.getParameterValues("MTH_CH");
                String[] MTH_START = request.getParameterValues("MTH_START");
                String[] MTH_STOP = request.getParameterValues("MTH_STOP");
                String[] MTH_GT = request.getParameterValues("MTH_GUARANTEE_AMOUNT");
                String[] MTH_TU = request.getParameterValues("MTH_TURN_AMOUNT");
                String[] MTH_FIX = request.getParameterValues("MTH_FIX_AMOUNT");
                //String[] MTH_INC = request.getParameterValues("MTH_INCLUDE_AMOUNT");                
               
                if(MLY.equalsIgnoreCase("MLY")){
                    GUARANTEE_TYPE = "MLY";
                    if(MTH_CH!=null){
                        for(int i_daych=0; i_daych < MTH_CH.length ; i_daych++){
                            DAY = MTH_CH[i_daych].toString();
                            START_TIME = MTH_START[i_daych].toString();
                            STOP_TIME = MTH_STOP[i_daych].toString();

                            DataRecord dtInsert = new DataRecord("DOCTOR_TIME_TABLE");
                            dtInsert.addField("HOSPITAL_CODE", Types.VARCHAR , HOSPITAL_CODE, true);
                            dtInsert.addField("DOCTOR_CODE", Types.VARCHAR , DOCTOR_CODE, true);
                            dtInsert.addField("GUARANTEE_TYPE", Types.VARCHAR , GUARANTEE_TYPE, true);
                            dtInsert.addField("DAY", Types.VARCHAR , DAY, true);
                            dtInsert.addField("START_TIME", Types.VARCHAR , JDate.saveTime(START_TIME), true );
                            dtInsert.addField("STOP_TIME", Types.VARCHAR , JDate.saveTime(STOP_TIME) );
                            dtInsert.addField("GUARANTEE_AMOUNT", Types.NUMERIC, MTH_GT[i_daych]);
                            dtInsert.addField("GUARANTEE_EXCLUDE_AMOUNT", Types.NUMERIC, MTH_TU[i_daych]);
                            dtInsert.addField("GUARANTEE_FIX_AMOUNT", Types.NUMERIC, MTH_FIX[i_daych]);//
                            //dtInsert.addField("GUARANTEE_INCLUDE_AMOUNT", Types.NUMERIC, MTH_INC[i_daych]);//                            
                            dtInsert.addField("USER_ID",Types.VARCHAR, session.getAttribute("USER_ID").toString());
                            dtInsert.addField("UPDATE_DATE",Types.VARCHAR, JDate.getDate());
                            dtInsert.addField("UPDATE_TIME",Types.VARCHAR, JDate.getTime());                            
                            DBMgr.insertRecord(dtInsert);
                        }
                    }
                }
                
                if(MLA.equalsIgnoreCase("MLA")){
                    GUARANTEE_TYPE = "MLA";
                    DataRecord dtInsert = new DataRecord("DOCTOR_TIME_TABLE");
                    dtInsert.addField("HOSPITAL_CODE", Types.VARCHAR , HOSPITAL_CODE, true);
                    dtInsert.addField("DOCTOR_CODE", Types.VARCHAR , DOCTOR_CODE, true);
                    dtInsert.addField("GUARANTEE_TYPE", Types.VARCHAR , GUARANTEE_TYPE, true);
                    dtInsert.addField("DAY", Types.VARCHAR , "ALL", true);
                    dtInsert.addField("START_TIME", Types.VARCHAR , JDate.saveTime("00:00"), true);
                    dtInsert.addField("STOP_TIME", Types.VARCHAR , JDate.saveTime("23:59"));
                    dtInsert.addField("GUARANTEE_AMOUNT", Types.NUMERIC, request.getParameter("MTHALL_GUARANTEE_AMOUNT"));
                    dtInsert.addField("GUARANTEE_EXCLUDE_AMOUNT", Types.NUMERIC, request.getParameter("MTHALL_TURN_AMOUNT"));
                    dtInsert.addField("GUARANTEE_FIX_AMOUNT", Types.NUMERIC, request.getParameter("MTHALL_FIX_GUARANTEE"));//
                    dtInsert.addField("GUARANTEE_INCLUDE_AMOUNT", Types.NUMERIC, request.getParameter("MTHALL_INCLUDE_GUARANTEE"));//                     
                    dtInsert.addField("USER_ID",Types.VARCHAR, session.getAttribute("USER_ID").toString());
                    dtInsert.addField("UPDATE_DATE",Types.VARCHAR, JDate.getDate());
                    dtInsert.addField("UPDATE_TIME",Types.VARCHAR, JDate.getTime());                    
                    DBMgr.insertRecord(dtInsert);
                }

                if(MMY.equalsIgnoreCase("MMY")){
                    GUARANTEE_TYPE = "MMY";
                    DataRecord dtInsert = new DataRecord("DOCTOR_TIME_TABLE");
                    dtInsert.addField("HOSPITAL_CODE", Types.VARCHAR , HOSPITAL_CODE, true);
                    dtInsert.addField("DOCTOR_CODE", Types.VARCHAR , DOCTOR_CODE, true);
                    dtInsert.addField("GUARANTEE_TYPE", Types.VARCHAR , GUARANTEE_TYPE, true);
                    dtInsert.addField("DAY", Types.VARCHAR , "ALL", true);
                    dtInsert.addField("START_TIME", Types.VARCHAR , JDate.saveTime("00:00"), true);
                    dtInsert.addField("STOP_TIME", Types.VARCHAR , JDate.saveTime("23:59"));
                    dtInsert.addField("GUARANTEE_AMOUNT", Types.NUMERIC, request.getParameter("MMY_GUARANTEE_AMOUNT"));
                    dtInsert.addField("GUARANTEE_EXCLUDE_AMOUNT", Types.NUMERIC, request.getParameter("MMY_TURN_AMOUNT"));
                    dtInsert.addField("GUARANTEE_FIX_AMOUNT", Types.NUMERIC, request.getParameter("MMY_FIX_GUARANTEE"));//
                    dtInsert.addField("GUARANTEE_INCLUDE_AMOUNT", Types.NUMERIC, request.getParameter("MMY_INCLUDE_GUARANTEE"));//                     
                    dtInsert.addField("USER_ID",Types.VARCHAR, session.getAttribute("USER_ID").toString());
                    dtInsert.addField("UPDATE_DATE",Types.VARCHAR, JDate.getDate());
                    dtInsert.addField("UPDATE_TIME",Types.VARCHAR, JDate.getTime());                    
                    DBMgr.insertRecord(dtInsert);
                }

                if(STP.equalsIgnoreCase("STP")){
                    GUARANTEE_TYPE = "STP";
                    DataRecord dtInsert = new DataRecord("DOCTOR_TIME_TABLE");
                    dtInsert.addField("HOSPITAL_CODE", Types.VARCHAR , HOSPITAL_CODE, true);
                    dtInsert.addField("DOCTOR_CODE", Types.VARCHAR , DOCTOR_CODE, true);
                    dtInsert.addField("GUARANTEE_TYPE", Types.VARCHAR , GUARANTEE_TYPE, true);
                    dtInsert.addField("DAY", Types.VARCHAR , "ALL", true);
                    dtInsert.addField("START_TIME", Types.VARCHAR , JDate.saveTime("00:00"), true);
                    dtInsert.addField("STOP_TIME", Types.VARCHAR , JDate.saveTime("23:59"));
                    dtInsert.addField("GUARANTEE_AMOUNT", Types.NUMERIC, request.getParameter("STP_GUARANTEE_AMOUNT"));
                    dtInsert.addField("GUARANTEE_EXCLUDE_AMOUNT", Types.NUMERIC, request.getParameter("STP_TURN_GUARNATEE"));
                    dtInsert.addField("GUARANTEE_FIX_AMOUNT", Types.NUMERIC, request.getParameter("STP_FIX_GUARANTEE"));//
                    dtInsert.addField("GUARANTEE_INCLUDE_AMOUNT", Types.NUMERIC, request.getParameter("STP_INCLUDE_GUARANTEE"));//                  
                    dtInsert.addField("IN_GUARANTEE", Types.NUMERIC, request.getParameter("IN_GUARANTEE"));
                    dtInsert.addField("OVER_GUARANTEE", Types.NUMERIC, request.getParameter("OVER_GUARANTEE"));
                    dtInsert.addField("USER_ID",Types.VARCHAR, session.getAttribute("USER_ID").toString());
                    dtInsert.addField("UPDATE_DATE",Types.VARCHAR, JDate.getDate());
                    dtInsert.addField("UPDATE_TIME",Types.VARCHAR, JDate.getTime());                    
                    DBMgr.insertRecord(dtInsert);
                }                
                response.sendRedirect("doctor_detail.jsp?CODE="+DOCTOR_CODE+"&DOCTOR_PROFILE_CODE="+DOCTOR_PROFILE_CODE);
                return;
            }
            else if (request.getParameter("DOCTOR_PROFILE_CODE") != null && request.getParameter("CODE") != null) {
                doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("CODE") + "' AND DOCTOR_PROFILE_CODE = '" + request.getParameter("DOCTOR_PROFILE_CODE") + "'" );
                doctorTimeTable = DBMgr.getRecord("SELECT * FROM DOCTOR_TIME_TABLE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND DOCTOR_CODE = '" + request.getParameter("CODE") + "'" );

                //record.addField("HOSPITAL_CODE", Types.VARCHAR, HOSPITAL_CODE, true);
                //record.addField("DOCTOR_CODE", Types.VARCHAR, DOCTOR_CODE, true);
                String sql = "SELECT HOSPITAL_CODE,DOCTOR_CODE,GUARANTEE_TYPE FROM DOCTOR_TIME_TABLE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND DOCTOR_CODE='"+ request.getParameter("CODE") +"' group by HOSPITAL_CODE,DOCTOR_CODE,GUARANTEE_TYPE";
                DBConnection con = new DBConnection();
                con.connectToLocal();
                rs = con.executeQuery(sql);

                DBConnection conn = new DBConnection();
                conn.connectToLocal();
                ResultSet rs_sub = null;
                while(rs.next()){
                    rs_sub = null;
                    if("DLY".equalsIgnoreCase(rs.getString("GUARANTEE_TYPE"))){
                        DL = "checked='checked'";

                        sql = "SELECT * FROM DOCTOR_TIME_TABLE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND DOCTOR_CODE='"+ request.getParameter("CODE") +"' and GUARANTEE_TYPE='DLY'";
                        rs_sub = conn.executeQuery(sql);
                        while(rs_sub.next()){
                            if("mon".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                DLY_CH[0] = "checked='checked'";
                                DLYSTART[0] = rs_sub.getString("START_TIME");
                                DLYSTOP[0] = rs_sub.getString("STOP_TIME");
                                DLYGURAN[0] = rs_sub.getString("GUARANTEE_AMOUNT");
                                DLYTURN[0] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                dly_fix[0] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //dly_include[0] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");
                            }
                            if("tue".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                DLY_CH[1] = "checked='checked'";
                                DLYSTART[1] = rs_sub.getString("START_TIME");
                                DLYSTOP[1] = rs_sub.getString("STOP_TIME");
                                DLYGURAN[1] = rs_sub.getString("GUARANTEE_AMOUNT");
                                DLYTURN[1] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                dly_fix[1] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //dly_include[1] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                
                            }
                            if("wed".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                DLY_CH[2] = "checked='checked'";
                                DLYSTART[2] = rs_sub.getString("START_TIME");
                                DLYSTOP[2] = rs_sub.getString("STOP_TIME");
                                DLYGURAN[2] = rs_sub.getString("GUARANTEE_AMOUNT");
                                DLYTURN[2] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                dly_fix[2] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //dly_include[2] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                
                            }
                            if("thu".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                DLY_CH[3] = "checked='checked'";
                                DLYSTART[3] = rs_sub.getString("START_TIME");
                                DLYSTOP[3] = rs_sub.getString("STOP_TIME");
                                DLYGURAN[3] = rs_sub.getString("GUARANTEE_AMOUNT");
                                DLYTURN[3] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                dly_fix[3] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //dly_include[3] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                
                            }
                            if("fri".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                DLY_CH[4] = "checked='checked'";
                                DLYSTART[4] = rs_sub.getString("START_TIME");
                                DLYSTOP[4] = rs_sub.getString("STOP_TIME");
                                DLYGURAN[4] = rs_sub.getString("GUARANTEE_AMOUNT");
                                DLYTURN[4] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                dly_fix[4] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //dly_include[4] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                
                            }
                            if("sat".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                DLY_CH[5] = "checked='checked'";
                                DLYSTART[5] = rs_sub.getString("START_TIME");
                                DLYSTOP[5] = rs_sub.getString("STOP_TIME");
                                DLYGURAN[5] = rs_sub.getString("GUARANTEE_AMOUNT");
                                DLYTURN[5] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                dly_fix[5] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                               // dly_include[5] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                
                            }
                            if("sun".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                DLY_CH[6] = "checked='checked'";
                                DLYSTART[6] = rs_sub.getString("START_TIME");
                                DLYSTOP[6] = rs_sub.getString("STOP_TIME");
                                DLYGURAN[6] = rs_sub.getString("GUARANTEE_AMOUNT");
                                DLYTURN[6] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                dly_fix[6] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //dly_include[6] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                
                            }
                        }
                    }else if("MLD".equalsIgnoreCase(rs.getString("GUARANTEE_TYPE"))){
                        MLD = "checked='checked'";
                        sql = "SELECT * FROM DOCTOR_TIME_TABLE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND DOCTOR_CODE='"+ request.getParameter("CODE") +"' AND GUARANTEE_TYPE='MLD'";
                        rs_sub = conn.executeQuery(sql);
                        while(rs_sub.next()){
                            if("mon".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLD_CH[0] = "checked='checked'";
                                MLDSTART[0] = rs_sub.getString("START_TIME");
                                MLDSTOP[0] = rs_sub.getString("STOP_TIME");
                                MLDGURAN[0] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLDTURN[0] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mld_fix[0] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mld_include[0] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                
                            }
                            if("tue".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLD_CH[1] = "checked='checked'";
                                MLDSTART[1] = rs_sub.getString("START_TIME");
                                MLDSTOP[1] = rs_sub.getString("STOP_TIME");
                                MLDGURAN[1] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLDTURN[1] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mld_fix[1] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mld_include[1] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("wed".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLD_CH[2] = "checked='checked'";
                                MLDSTART[2] = rs_sub.getString("START_TIME");
                                MLDSTOP[2] = rs_sub.getString("STOP_TIME");
                                MLDGURAN[2] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLDTURN[2] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mld_fix[2] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[2] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("thu".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLD_CH[3] = "checked='checked'";
                                MLDSTART[3] = rs_sub.getString("START_TIME");
                                MLDSTOP[3] = rs_sub.getString("STOP_TIME");
                                MLDGURAN[3] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLDTURN[3] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mld_fix[3] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[3] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("fri".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLD_CH[4] = "checked='checked'";
                                MLDSTART[4] = rs_sub.getString("START_TIME");
                                MLDSTOP[4] = rs_sub.getString("STOP_TIME");
                                MLDGURAN[4] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLDTURN[4] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mld_fix[4] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[4] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("sat".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLD_CH[5] = "checked='checked'";
                                MLDSTART[5] = rs_sub.getString("START_TIME");
                                MLDSTOP[5] = rs_sub.getString("STOP_TIME");
                                MLDGURAN[5] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLDTURN[5] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mld_fix[5] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[5] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("sun".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLD_CH[6] = "checked='checked'";
                                MLDSTART[6] = rs_sub.getString("START_TIME");
                                MLDSTOP[6] = rs_sub.getString("STOP_TIME");
                                MLDGURAN[6] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLDTURN[6] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mld_fix[6] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[6] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                        }
                    }else if("MLY".equalsIgnoreCase(rs.getString("GUARANTEE_TYPE"))){
                        MLY = "checked='checked'";
                        sql = "SELECT * FROM DOCTOR_TIME_TABLE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND DOCTOR_CODE='"+ request.getParameter("CODE") +"' and GUARANTEE_TYPE='MLY'";
                        rs_sub = conn.executeQuery(sql);
                        while(rs_sub.next()){
                            if("mon".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLY_CH[0] = "checked='checked'";
                                MLYSTART[0] = rs_sub.getString("START_TIME");
                                MLYSTOP[0] = rs_sub.getString("STOP_TIME");
                                MLYGURAN[0] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLYTURN[0] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mly_fix[0] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[0] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                
                            }
                            if("tue".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLY_CH[1] = "checked='checked'";
                                MLYSTART[1] = rs_sub.getString("START_TIME");
                                MLYSTOP[1] = rs_sub.getString("STOP_TIME");
                                MLYGURAN[1] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLYTURN[1] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mly_fix[1] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[1] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("wed".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLY_CH[2] = "checked='checked'";
                                MLYSTART[2] = rs_sub.getString("START_TIME");
                                MLYSTOP[2] = rs_sub.getString("STOP_TIME");
                                MLYGURAN[2] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLYTURN[2] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mly_fix[2] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[2] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("thu".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLY_CH[3] = "checked='checked'";
                                MLYSTART[3] = rs_sub.getString("START_TIME");
                                MLYSTOP[3] = rs_sub.getString("STOP_TIME");
                                MLYGURAN[3] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLYTURN[3] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mly_fix[3] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[3] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("fri".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLY_CH[4] = "checked='checked'";
                                MLYSTART[4] = rs_sub.getString("START_TIME");
                                MLYSTOP[4] = rs_sub.getString("STOP_TIME");
                                MLYGURAN[4] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLYTURN[4] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mly_fix[4] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[4] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("sat".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLY_CH[5] = "checked='checked'";
                                MLYSTART[5] = rs_sub.getString("START_TIME");
                                MLYSTOP[5] = rs_sub.getString("STOP_TIME");
                                MLYGURAN[5] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLYTURN[5] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mly_fix[5] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[5] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                            if("sun".equalsIgnoreCase(rs_sub.getString("DAY"))){
                                MLY_CH[6] = "checked='checked'";
                                MLYSTART[6] = rs_sub.getString("START_TIME");
                                MLYSTOP[6] = rs_sub.getString("STOP_TIME");
                                MLYGURAN[6] = rs_sub.getString("GUARANTEE_AMOUNT");
                                MLYTURN[6] = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                                mly_fix[6] = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                                //mly_include[6] = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                                  
                            }
                        }
                    }else if("MLA".equalsIgnoreCase(rs.getString("GUARANTEE_TYPE"))){
                        MLA = "checked='checked'";
						sql = "SELECT * FROM DOCTOR_TIME_TABLE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND DOCTOR_CODE='"+ request.getParameter("CODE") +"' and GUARANTEE_TYPE='MLA'";
                        rs_sub = conn.executeQuery(sql);
                        while(rs_sub.next()){
							MLAGURAN = rs_sub.getString("GUARANTEE_AMOUNT");
                        	MLATURN = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                			mla_fix_amount = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                			mla_include_amount = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                        	
						}
                    }else if("MMY".equalsIgnoreCase(rs.getString("GUARANTEE_TYPE"))){
                        MMY = "checked='checked'";
						sql = "SELECT * FROM DOCTOR_TIME_TABLE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND DOCTOR_CODE='"+ request.getParameter("CODE") +"' and GUARANTEE_TYPE='MMY'";
                        rs_sub = conn.executeQuery(sql);
                        while(rs_sub.next()){
							MMYGURAN = rs_sub.getString("GUARANTEE_AMOUNT");
                        	MMYTURN = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                			mmy_fix_amount = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                			mmy_include_amount = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");                        	
						}
                    }else if("STP".equalsIgnoreCase(rs.getString("GUARANTEE_TYPE"))){
                    	STP = "checked='checked'";
						sql = "SELECT * FROM DOCTOR_TIME_TABLE WHERE HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND DOCTOR_CODE='"+ request.getParameter("CODE") +"' and GUARANTEE_TYPE='STP'";
                        rs_sub = conn.executeQuery(sql);
                        while(rs_sub.next()){
                			stp_guarantee_amount = rs_sub.getString("GUARANTEE_AMOUNT");
                			stp_in_guarantee = rs_sub.getString("IN_GUARANTEE");
                			stp_out_guarantee = rs_sub.getString("OVER_GUARANTEE");
                			stp_exclude_amount = rs_sub.getString("GUARANTEE_EXCLUDE_AMOUNT");
                			stp_fix_amount = rs_sub.getString("GUARANTEE_FIX_AMOUNT");
                			stp_include_amount = rs_sub.getString("GUARANTEE_INCLUDE_AMOUNT");
						}
                    }
                }
                //rs_sub.close();
                //rs.close();
                //conn.Close();
                //con.Close();
                //con.freeConnection();
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
            <table class="form">
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  	</th>
                </tr>
                <tr>
                    <td class="label">
                        <label for="CODE">${labelMap.CODE} *</label>
                    </td>
                    <td class="input">
                        <input name="CODE" type="text" class="short" id="CODE" value="<%= DBMgr.getRecordValue(doctorRec, "CODE")%>" maxlength="50" readonly/>
                    </td>
                </tr>

                <tr>
                    <td class="label" valign="middle"><label for="DESCRIPTION_THAI">${labelMap.GUARANTEE_TYPE} *</label></td>
                    <td colspan="3" class="input">
                        <table width="100%" border="1" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="5"><input type="checkbox" id="type_time" name="type_guarantee" value="DLY" onclick="ChkBox(this);" <%=DL%>></td>
                                <td>รายวัน </td>
                            </tr>
                            <tr>
                                <td width="5"><input type="checkbox" id="type_day_mth" name="type_guarantee" value="MLD" onclick="ChkBox(this);" <%=MLD%>></td>
                                <td>รายวันคิดเป็นรายเดือน </td>
                            </tr>
                            <tr>
                                <td width="5"><input type="checkbox" id="type_mth_day" name="type_guarantee" value="MLY" onclick="ChkBox(this);" <%=MLY%>></td>
                                <td>รายเดือน (แบบระบุวัน)</td>
                            </tr>
                            <tr>
                                <td width="5"><input type="checkbox" id="type_mth_all" name="type_guarantee" value="MLA" onclick="ChkBox(this);" <%=MLA%>></td>
                                <td>รายเดือนทั้งเดือน </td>
                            </tr>
                            <tr>
                                <td width="5"><input type="checkbox" id="type_mmy_all" name="type_guarantee" value="MMY" onclick="ChkBox(this);" <%=MMY%>></td>
                                <td>รายเดือนเทียบรายปี </td>
                            </tr>
                            <tr>
                                <td width="5"><input type="checkbox" id="type_stp" name="type_guarantee" value="STP" onclick="ChkBox(this);" <%=STP%>></td>
                                <td>ขั้นบันได </td>
                            </tr>                                                      
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.GUARANTEE_DATE}</label></td>
                    <td colspan="3" class="input">
                        <div id="daily">
                            <table border='1'>
                                <tr>
                                    <th colspan="6">
                                        ${labelMap.GUARANTEE_DAILY_HEAD}
                                    </th>
                                </tr>
                                <tr>
                                    <td width='5%'>&nbsp;</td>
                                    <td width='20%' align="center">${labelMap.GUARANTEE_DAY}</td>
                                    <td width='35%' align="center">${labelMap.GUARANTEE_TIME}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_AMOUNT}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_EXCLUDE_AMOUNT}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_FIX_AMOUNT}</td>
                                </tr>
                                <%
                                for(int i = 0 ;i < LABEL_DAY.length ; i++){
                                %>
                                    <tr>
                                        <td><input type="checkbox" name="DAY_CH" value="<%=VALUE_DAY[i] %>" onclick="TimeDisabledArrayDLY(this,'<%=i%>');" <%=DLY_CH[i]%>></td>
                                        <td><%=LABEL_DAY[i]%></td>
                                        <td>
                                            <input type="text" class="veryShort" name="DAY_START" id="ID_DAY_START" value="<%= DLYSTART[i]=="" ? "08:00" : JDate.showTime(DLYSTART[i]) %>"> -
                                            <input type="text" class="veryShort" name="DAY_STOP" id="ID_DAY_STOP" value="<%= DLYSTOP[i]=="" ? "16:00" : JDate.showTime(DLYSTOP[i])%>">
                                        </td>
                                        <td>
                                            <input type="text" class="inputMoney" name="DAY_GUARANTEE_AMOUNT" id="ID_DAY_GUARANTEE_AMOUNT" value="<%= DLYGURAN[i]%>" onkeypress="return OnlyNumber();" maxlength="8"/>
                                        </td>
                                        <td>
                                            <input type="text" class="inputMoney" name="DAY_TURN_AMOUNT" id="ID_DAY_TURN_AMOUNT" value="<%= DLYTURN[i]%>" onkeypress="return OnlyNumber();" maxlength="8"/>                                      
                                        </td>
                                        <td>
                                            <input type="text" class="inputMoney" name="DAY_FIX_AMOUNT" id="ID_DAY_FIX_AMOUNT" value="<%= dly_fix[i]%>" onkeypress="return OnlyNumber();" maxlength="8"/>                                        
                                        </td>
                                    </tr>
                                <%}%>
                            </table>
                        </div>
                        <div id="dateMonth">
                            <table border='1'>
                                <tr>
                                    <th colspan="6">
                                        ${labelMap.GUARANTEE_DAILY_TO_MONTHLY_HEAD}
                                    </th>
                                </tr>
                                <tr>
                                    <td width='5%'>&nbsp;</td>
                                    <td width='20%' align="center">${labelMap.GUARANTEE_DAY}</td>
                                    <td width='35%' align="center">${labelMap.GUARANTEE_TIME}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_AMOUNT}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_EXCLUDE_AMOUNT}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_FIX_AMOUNT}</td>
                                </tr>
                                <%
                                for(int k = 0 ;k < LABEL_DAY.length ; k++){
                                %>
                                    <tr>
                                        <td><input type="checkbox" name="MTD_CH" value="<%=VALUE_DAY[k] %>" onclick="TimeDisabledArrayMLD(this,'<%=k%>');" <%=MLD_CH[k]%>></td>
                                        <td><%=LABEL_DAY[k] %></td>
                                        <td>
                                            <input  type="text" class="veryShort" name="MTD_START" id="ID_MLD_START" value="<%= MLDSTART[k]=="" ? "08:00" : JDate.showTime(MLDSTART[k])%>"> -
                                            <input  type="text" class="veryShort" name="MTD_STOP" id="ID_MLD_STOP" value="<%= MLDSTOP[k]=="" ? "16:00" : JDate.showTime(MLDSTOP[k])%>">
                                        </td>
                                        <td>
                                            <input  type="text" class="inputMoney" name="MTD_GUARANTEE_AMOUNT" id="ID_MLD_GUARANTEE_AMOUNT" value="<%= MLDGURAN[k]%>" onkeypress="return OnlyNumber();" maxlength="8"/>
                                        </td>
                                        <td>
                                            <input  type="text" class="inputMoney" name="MTD_TURN_AMOUNT" id="ID_MLD_TURN_AMOUNT" value="<%= MLDTURN[k]%>" onkeypress="return OnlyNumber();" maxlength="8"/>                                      
                                        </td>
                                        <td>
                                            <input  type="text" class="inputMoney" name="MTD_FIX_AMOUNT" id="ID_MLD_FIX_AMOUNT" value="<%= mld_fix[k]%>" onkeypress="return OnlyNumber();" maxlength="8"/>                                       
                                        </td>                                        
                                    </tr>
                                <%}%>                                
                            </table>
                        </div>
                        <div id="monthDate">
                            <table border='1'>
                                <tr>
                                    <th colspan="6">
                                        ${labelMap.GUARANTEE_MONTHLY_FIX_HEAD}
                                    </th>
                                </tr>
                                <tr>
                                    <td width='5%'>&nbsp;</td>
                                    <td width='20%' align="center">${labelMap.GUARANTEE_DAY}</td>
                                    <td width='35%' align="center">${labelMap.GUARANTEE_TIME}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_AMOUNT}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_EXCLUDE_AMOUNT}</td>
                                    <td width='10%' align="center">${labelMap.GUARANTEE_FIX_AMOUNT}</td>
                                </tr>
                                <%
                                for(int k = 0 ;k < LABEL_DAY.length ; k++){
                                %>
                                    <tr>
                                        <td><input type="checkbox" name="MTH_CH" value="<%=VALUE_DAY[k] %>" onclick="TimeDisabledArrayMLY(this,'<%=k%>');" <%=MLY_CH[k]%>></td>
                                        <td><%=LABEL_DAY[k] %></td>
                                        <td>
                                            <input type="text" class="veryShort" name="MTH_START" id="ID_MTH_START" value="<%= MLYSTART[k]=="" ? "08:00" : JDate.showTime(MLYSTART[k])%>"> -
                                            <input type="text" class="veryShort" name="MTH_STOP" id="ID_MTH_STOP" value="<%= MLYSTOP[k]=="" ? "16:00" : JDate.showTime(MLYSTOP[k])%>">
                                        </td>
                                        <td>
                                            <input type="text" class="inputMoney" name="MTH_GUARANTEE_AMOUNT" id="ID_MTH_GUARANTEE_AMOUNT" value="<%= MLYGURAN[k]%>" onkeypress="return OnlyNumber();" maxlength="8"/>
                                        </td>
                                        <td>
                                            <input type="text" class="inputMoney" name="MTH_TURN_AMOUNT" id="ID_MTH_TURN_AMOUNT" value="<%= MLYTURN[k]%>" onkeypress="return OnlyNumber();" maxlength="8"/>                                      
                                        </td>
                                        <td>
                                            <input type="text" class="inputMoney" name="MTH_FIX_AMOUNT" id="ID_MTH_FIX_AMOUNT" value="<%= mly_fix[k]%>" onkeypress="return OnlyNumber();" maxlength="8"/>                                       
                                        </td>                                        
                                    </tr>
                                <%}%>                                
                            </table>
                        </div>
                        
                        <div id="monthAll">
                            <table width='100%' border='0'>
                            	<tr>
                            		<td align='right' width='140'><b>${labelMap.GUARANTEE_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="MTHALL_GUARANTEE_AMOUNT" value="<%=MLAGURAN %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            		<td align='right'><b>${labelMap.GUARANTEE_EXCLUDE_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="MTHALL_TURN_AMOUNT" value="<%=MLATURN %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            	</tr>
                            	<tr>
                            		<td align='right'><b>${labelMap.GUARANTEE_FIX_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="MTHALL_FIX_GUARANTEE" value="<%=mla_fix_amount %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            		<td align='right'><b>${labelMap.GUARANTEE_INCLUDE_AMOUNT} :</b></td> 
                            		<td align='left'><input type="text" class="inputMoney" name="MTHALL_INCLUDE_GUARANTEE" value="<%=mla_include_amount %>" onkeypress="return OnlyNumber();" maxlength="8"></td>                            		
                            	</tr>                            	
                            </table>                            
                        </div>
                        <div id="monthYear">
                            <table width='100%' border='0'>
                            	<tr>
                            		<td align='right' width='140'><b>${labelMap.GUARANTEE_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="MMY_GUARANTEE_AMOUNT" value="<%=MMYGURAN %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            		<td align='right'><b>${labelMap.GUARANTEE_EXCLUDE_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="MMY_TURN_AMOUNT" value="<%=MMYTURN %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            	</tr>
                            	<tr>
                            		<td align='right'><b>${labelMap.GUARANTEE_FIX_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="MMY_FIX_GUARANTEE" value="<%=mmy_fix_amount %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            		<td align='right'><b>${labelMap.GUARANTEE_INCLUDE_AMOUNT} :</b></td> 
                            		<td align='left'><input type="text" class="inputMoney" name="MMY_INCLUDE_GUARANTEE" value="<%=mmy_include_amount %>" onkeypress="return OnlyNumber();" maxlength="8"></td>                            		
                            	</tr>                            	
                            </table>                            
                        </div>
                        <div id="stpSharing">
                            <table width='100%'>
                            	<tr>
                            		<td align='right' width='136'><b>${labelMap.GUARANTEE_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="STP_GUARANTEE_AMOUNT" value="<%=stp_guarantee_amount %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            		<td align='right' width='144'><b>${labelMap.GUARANTEE_EXCLUDE_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="STP_TURN_GUARNATEE" value="<%=stp_exclude_amount %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            	</tr>
                            	<tr>
                            		<td align='right'><b>${labelMap.GUARANTEE_FIX_AMOUNT} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="STP_FIX_GUARANTEE" value="<%=stp_fix_amount %>" onkeypress="return OnlyNumber();" maxlength="8"></td>
                            		<td align='right'><b>${labelMap.GUARANTEE_INCLUDE_AMOUNT} :</b></td> 
                            		<td align='left'><input type="text" class="inputMoney" name="STP_INCLUDE_GUARANTEE" value="<%=stp_include_amount %>" onkeypress="return OnlyNumber();" maxlength="8"></td>                            		
                            	</tr>                             	
                            	<tr>
                            		<td align='right'><b>${labelMap.IN_GUARANTEE} :</b></td>
                            		<td align='left'><input type="text" class="inputMoney" name="IN_GUARANTEE" value="<%=stp_in_guarantee %>" onkeypress="return OnlyNumber();" maxlength="8">%</td>
                            		<td align='right'><b>${labelMap.OVER_GUARANTEE} :</b></td> 
                            		<td align='left'><input type="text" class="inputMoney" name="OVER_GUARANTEE" value="<%=stp_out_guarantee %>" onkeypress="return OnlyNumber();" maxlength="8">%</td>                            		
                            	</tr>                            	
                            </table>
                        </div>                        
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="submit" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.history.back()" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<script language="javascript"><!--
    function ChkBox(obj){
        //var value = obj.value;
        var a = document.getElementById("type_time");
        var b = document.getElementById("type_mth_day");
        var c = document.getElementById("type_mth_all");
        var f = document.getElementById("type_mmy_all");
        var d = document.getElementById("type_stp");
        var e = document.getElementById("type_day_mth");

        var divDay = document.getElementById("daily");
        var divMthDate = document.getElementById("monthDate");
        var divMmy = document.getElementById("monthYear");
        var divMth = document.getElementById("monthAll");
        var divStp = document.getElementById("stpSharing");
        var divDateMth = document.getElementById("dateMonth");
        
        if(obj.value == "DLY"){
            c.checked = false;
            f.checked = false;
            d.checked = false;
            divMth.style.display = 'none';
            divStp.style.display = 'none';
            if(obj.checked){
                divDay.style.display = 'block';
            }else{
                divDay.style.display = 'none';
            }
        }else if(obj.value == "MLY"){
            c.checked = false;
            f.checked = false;
            d.checked = false;
            e.checked = false;
            divMth.style.display = 'none';
            divStp.style.display = 'none';
        	divDateMth.style.display = 'none';
            if(obj.checked){
                divMthDate.style.display = 'block';
            }else{
                divMthDate.style.display = 'none';
            }
        }else if(obj.value == "MLA"){
            a.checked = false;
            b.checked = false;
            d.checked = false;
            e.checked = false;
            f.checked = false;
            divDay.style.display = 'none';
            divStp.style.display = 'none';
            divMthDate.style.display = 'none';
        	divDateMth.style.display = 'none';
        	divMmy.style.display = 'none';
            if(obj.checked){
            	divMth.style.display = 'block';
            }else{
            	divMth.style.display = 'none';
            }            
        }else if(obj.value == "MMY"){
            a.checked = false;
            b.checked = false;
            c.checked = false;
            d.checked = false;
            e.checked = false;
            divDay.style.display = 'none';
            divStp.style.display = 'none';
            divMthDate.style.display = 'none';
        	divDateMth.style.display = 'none';
        	divMth.style.display = 'none';
            if(obj.checked){
            	divMmy.style.display = 'block';
            }else{
            	divMmy.style.display = 'none';
            }            
        }else if(obj.value == "STP"){
            a.checked = false;
            b.checked = false;
            c.checked = false;
            e.checked = false;
            divDay.style.display = 'none';
            divMthDate.style.display = 'none';
            divMth.style.display = 'none';
        	divDateMth.style.display = 'none';
            if(obj.checked){
            	divStp.style.display = 'block';
            }else{
            	divStp.style.display = 'none';
            }
        }else if(obj.value == "MLD"){
            b.checked = false;
            c.checked = false;
            d.checked = false;
            divMthDate.style.display = 'none';
            divMth.style.display = 'none';
            divStp.style.display = 'none';
            if(obj.checked){
            	divDateMth.style.display = 'block';
            }else{
            	divDateMth.style.display = 'none';
            }
        }
    }
    function TimeDisabledArrayDLY(obj, id){
        var e = document.mainForm.ID_DAY_START;
        var b = document.mainForm.ID_DAY_STOP;
        var d = document.mainForm.ID_DAY_GUARANTEE_AMOUNT;
        var f = document.mainForm.ID_DAY_TURN_AMOUNT;
		var g = document.mainForm.ID_DAY_FIX_AMOUNT;
        
        disabledOnCheck(obj,e,id);
        disabledOnCheck(obj,b,id);
        disabledOnCheck(obj,d,id);
        disabledOnCheck(obj,f,id);
        disabledOnCheck(obj,g,id);
    }
    function TimeDisabledArrayMLD(obj, id){
        var e = document.mainForm.ID_MLD_START;
        var b = document.mainForm.ID_MLD_STOP;
        var d = document.mainForm.ID_MLD_GUARANTEE_AMOUNT;
        var f = document.mainForm.ID_MLD_TURN_AMOUNT;
		var g = document.mainForm.ID_MLD_FIX_AMOUNT;
		
		disabledOnCheck(obj,e,id);
		disabledOnCheck(obj,b,id);
		disabledOnCheck(obj,d,id);
		disabledOnCheck(obj,f,id);
		disabledOnCheck(obj,g,id);     
    }
    function TimeDisabledArrayMLY(obj, id){
        var e = document.mainForm.ID_MTH_START;
        var b = document.mainForm.ID_MTH_STOP;
        var d = document.mainForm.ID_MTH_GUARANTEE_AMOUNT;
        var f = document.mainForm.ID_MTH_TURN_AMOUNT;
		var g = document.mainForm.ID_MTH_FIX_AMOUNT;
		
		disabledOnCheck(obj,e,id);
		disabledOnCheck(obj,b,id);
		disabledOnCheck(obj,d,id);
		disabledOnCheck(obj,f,id);
		disabledOnCheck(obj,g,id);      
    }
    function disabledOnCheck(obj,e,id){
        for(var i = 0 ; i < e.length ; i++){
            if(id==i){
                if(obj.checked){
                    e[i].disabled = false;
                }else{
                    e[i].disabled = true;
                }
            }
        }
    }

    function disabledOnLoad(e){
        for(var i = 0 ; i < e.length ; i++){
            e[i].disabled = true;
        }
    }
    function showDivElement(i,t,divID){
            if(t[i].checked){
                divID.style.display = "block";
            }else{
                divID.style.display = "none";
            }
    }
    function showDiv(){
        var a = document.getElementById("daily");
        var b = document.getElementById("monthDate");
        var c = document.getElementById("monthAll");
        var f = document.getElementById("monthYear");
        var d = document.getElementById("stpSharing");
        var e = document.getElementById("dateMonth");

        var t = document.getElementsByName("type_guarantee");
        for(var i = 0; i < t.length ; i++){
            if(i==0){
                showDivElement(i,t,a);
            }
            if(i==1){
                showDivElement(i,t,e);
            }
            if(i==2){
                showDivElement(i,t,b);
            }
            if(i==3){
                showDivElement(i,t,c);
            }  
            if(i==4){
                showDivElement(i,t,f);
            }           
            if(i==5){
                showDivElement(i,t,d);
            }
        }
    }

    function TimeDisabledArrayDLYOnload(){
        var e = document.mainForm.ID_DAY_START;
        var b = document.mainForm.ID_DAY_STOP;

        var d = document.mainForm.ID_DAY_GUARANTEE_AMOUNT;
        var f = document.mainForm.ID_DAY_TURN_AMOUNT;

        var g = document.mainForm.ID_DAY_FIX_AMOUNT;
                
        var ch = document.mainForm.DAY_CH;
        for(var i = 0 ; i < ch.length ; i++){
            if(ch[i].checked){
                e[i].disabled = false;
                b[i].disabled = false;
                d[i].disabled = false;
                f[i].disabled = false;
                g[i].disabled = false;              
            }
        }
    }
    function TimeDisabledArrayMLDOnload(obj, id){
        var e = document.mainForm.ID_MLD_START;
        var b = document.mainForm.ID_MLD_STOP;

        var d = document.mainForm.ID_MLD_GUARANTEE_AMOUNT;
        var f = document.mainForm.ID_MLD_TURN_AMOUNT;

        var g = document.mainForm.ID_MLD_FIX_AMOUNT;
                
        var ch = document.mainForm.MTD_CH;
        for(var i = 0 ; i < ch.length ; i++){
            if(ch[i].checked){
                e[i].disabled = false;
                b[i].disabled = false;
                d[i].disabled = false;
                f[i].disabled = false;
                g[i].disabled = false;               
            }
        }
    }
    function TimeDisabledArrayMLYOnload(obj, id){
        var e = document.mainForm.ID_MTH_START;
        var b = document.mainForm.ID_MTH_STOP;

        var d = document.mainForm.ID_MTH_GUARANTEE_AMOUNT;
        var f = document.mainForm.ID_MTH_TURN_AMOUNT;

        var g = document.mainForm.ID_MTH_FIX_AMOUNT;
                
        var ch = document.mainForm.MTH_CH;
        for(var i = 0 ; i < ch.length ; i++){
            if(ch[i].checked){
                e[i].disabled = false;
                b[i].disabled = false;
                d[i].disabled = false;
                f[i].disabled = false;
                g[i].disabled = false;              
            }
        }
    }
    function OnlyNumber(){
        if (event.keyCode < 45 || event.keyCode > 57)
        {
            event.returnValue = false;
        }else{
            event.returnValue = true;
        }
    }

    function BodyOnLoad(){
        disabledOnLoad(document.mainForm.ID_DAY_START);
        disabledOnLoad(document.mainForm.ID_DAY_STOP);
        disabledOnLoad(document.mainForm.ID_DAY_GUARANTEE_AMOUNT);
        disabledOnLoad(document.mainForm.ID_DAY_TURN_AMOUNT);
        disabledOnLoad(document.mainForm.ID_DAY_FIX_AMOUNT);
        //disabledOnLoad(document.mainForm.ID_DAY_INCLUDE_AMOUNT);

       	disabledOnLoad(document.mainForm.ID_MLD_START);
        disabledOnLoad(document.mainForm.ID_MLD_STOP);
        disabledOnLoad(document.mainForm.ID_MLD_GUARANTEE_AMOUNT);
        disabledOnLoad(document.mainForm.ID_MLD_TURN_AMOUNT);
        disabledOnLoad(document.mainForm.ID_MLD_FIX_AMOUNT);
        
        disabledOnLoad(document.mainForm.ID_MTH_START);
        disabledOnLoad(document.mainForm.ID_MTH_STOP);
        disabledOnLoad(document.mainForm.ID_MTH_GUARANTEE_AMOUNT);
        disabledOnLoad(document.mainForm.ID_MTH_TURN_AMOUNT);
        disabledOnLoad(document.mainForm.ID_MTH_FIX_AMOUNT);
        
        //disabledOnLoad(document.mainForm.ID_MTH_INCLUDE_AMOUNT);
        //disabledOnLoad(document.mainForm.DAY_CH);
        //disabledOnLoad(document.mainForm.MTD_CH);
        //disabledOnLoad(document.mainForm.MTH_CH);
                
        showDiv();

        TimeDisabledArrayDLYOnload();
        TimeDisabledArrayMLDOnload();
        TimeDisabledArrayMLYOnload();
    }
    BodyOnLoad();
</script>
