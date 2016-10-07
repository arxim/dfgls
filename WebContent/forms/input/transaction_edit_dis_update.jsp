<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.*"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_RECEIPT)) {
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
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            DBConn conData = new DBConn();
            conData.setStatement();
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            //HashMap m = (HashMap)request.getParameterMap();
            //out.println(m.toString());
            //Iterator keyValuePairs1 = m.entrySet().iterator();
            String item[];
            //String receipt_date = JDate.saveDate(request.getParameter("RECEIPT_DATE").toString());
            String cmd;
            DBConnection con = new DBConnection();
            con.connectToLocal();
            //con.connectToServer();
            
            //Start Insert Data
            //Process
            double TxtTypePercent=0;
            String MaType="", MaType_inactive="", MaType_discount="",MaType_change="", TypeDisCount="", InvoiceNumber="";
            if(request.getParameter("ChProcess_inactive") !=null && !request.getParameter("ChProcess_inactive").equals(""))
            {
            	MaType_inactive		=request.getParameter("ChProcess_inactive");
            	System.out.println("MaType_inactive="+MaType_inactive);
            }
            if(request.getParameter("ChProcess_discount") !=null && !request.getParameter("ChProcess_discount").equals(""))
            {
            	MaType_discount		=request.getParameter("ChProcess_discount");
            	System.out.println("ChProcess_discount="+MaType_discount);
            }
            if(request.getParameter("ChProcess_change") !=null && !request.getParameter("ChProcess_change").equals(""))
            {
            	MaType_change		=request.getParameter("ChProcess_change");
            	System.out.println("ChProcess_change="+MaType_change);
            }
            System.out.println("ChProcess_discount="+MaType_discount);
            System.out.println("ChProcess_change="+MaType_change);
            //Type Discount
            System.out.println("RdType="+request.getParameter("RdType"));
            if(request.getParameter("RdType") !=null && !request.getParameter("RdType").equals(""))
            {
            	TypeDisCount			=request.getParameter("RdType");
            }
            if(request.getParameter("TxtTypePercent")!=null && !request.getParameter("TxtTypePercent").equals(""))
            {
            	System.out.println("TxtTypePercent="+request.getParameter("TxtTypePercent"));
            	TxtTypePercent				=Double.parseDouble(request.getParameter("TxtTypePercent"));
            }
            //Not All Invoice
            String ChAllNote="", TxtareaNoteAll="";
            System.out.println("ChAllNote="+request.getParameter("ChAllNote"));
            if(request.getParameter("ChAllNote")!=null && !request.getParameter("ChAllNote").equals(""))
            {
            	System.out.println("ok_note");
            	ChAllNote			=request.getParameter("ChAllNote");
            }
            TxtareaNoteAll		=request.getParameter("TxtareaNoteAll");
            //Data Transaction
            String arrDis[]=null, arrLineNo[]=null, arrDoctorCode[]=null, arrNote[]=null, arrDisAmount[]=null, arrInvoiceNumber[]=null;
            if(request.getParameterValues("INVOICE_NUMBER[]") !=null )
            {
            	//System.out.println("INVOICE_NUMBER="+request.getParameter("INVOICE_NUMBER"));
            	//InvoiceNumber				=request.getParameter("INVOICE_NUMBER");
				arrInvoiceNumber				=request.getParameterValues("INVOICE_NUMBER[]");
            }
            else
            {
            	System.out.println("invoice_number="+request.getParameter("INVOICE_NUMBER"));
            }
            if(request.getParameterValues("DIS[]")!=null)
            {
            	arrDis			=request.getParameterValues("DIS[]");
            }
            if(request.getParameterValues("DIS[]")!=null)
            {
            	arrLineNo		=request.getParameterValues("LINE_NO_ARR[]");
            }
            if(request.getParameterValues("DOCTOR_CODE_ARR[]") !=null)
            {
				arrDoctorCode	=request.getParameterValues("DOCTOR_CODE_ARR[]");
            }
            if(request.getParameterValues("NOTE_ARR[]") !=null)
            {
				arrNote			=request.getParameterValues("NOTE_ARR[]");
            }
            if(request.getParameterValues("AMOUNT_OF_DISCOUNT_ARR[]") !=null)
            {
				arrDisAmount	=request.getParameterValues("AMOUNT_OF_DISCOUNT_ARR[]");
				System.out.println("arrDisAmount="+arrDisAmount.length);
				for(int b=0;b<arrDisAmount.length;b++)
				{
					System.out.println("b="+b+" value="+arrDisAmount[b]);
				}
						
            }
			System.out.println("MaType_discount="+MaType_discount);
			System.out.println("TypeDisCount="+TypeDisCount);
			String insertData="", sqlData="";
			if(MaType_discount.equals("1") && TypeDisCount.equals("2"))//%
			{
				System.out.println("ok");
				MaType="010";
				//String getLineNo		=arrLineNo[0];
				//System.out.println("getLineNo="+getLineNo);
				System.out.println("ChAllNote="+ChAllNote);
				String getNote="";
				if(ChAllNote.equals("1"))//all note
				{
					getNote=TxtareaNoteAll;
				}
				
				//Query Invoice_no
				//String sqlInvoice="SELECT INVOICE_NO FROM TRN_DAILY "
				//+" WHERE LINE_NO='"+getLineNo+"'"
				//+" AND HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' ";
				//System.out.println("sqlInvoice= "+sqlInvoice);
				//String [][]arrInvoice = conData.query(sqlInvoice);
				//System.out.println("arrInvoice="+arrInvoice.length);
				//String Invoice_No=arrInvoice[0][0];
				
				//DELETE DATA
				/*
				String deleteData="DELETE FROM MA_TRN_DAILY WHERE "
			        +" HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' "
			        +" AND INVOICE_NO='"+InvoiceNumber+"'"
				 	+" AND MA_TYPE='"+MaType+"'";
			        System.out.println("deleteData="+deleteData);
			        //ResultSet rsDelete = con.executeQuery(deleteData);
			        try
		    	    {
						conData.insert(deleteData);
						conData.commitDB();
		    	    }
		    	    catch(Exception e)
		    	    {
		    	       System.out.println("DELETE Transaction discount % Excepiton : "+e+"query="+deleteData);
		    	       conData.rollDB();
		    	    }
		    	    */
			    //QUERY DATA FROM TRN_DAILY
			    sqlData="SELECT INVOICE_NO, INVOICE_DATE, HN_NO, PAYOR_OFFICE_CODE, TRANSACTION_DATE, "
			    +" ORDER_ITEM_CODE, INVOICE_TYPE, DOCTOR_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, "
			    +" AMOUNT_AFT_DISCOUNT, YYYY, MM, ACTIVE, LINE_NO, "
			    +" PATIENT_NAME, OLD_AMOUNT, DR_AMT, OLD_DR_AMT, DR_TAX_406, "
			    +" OLD_TAX_AMT, AMOUNT_BEF_WRITE_OFF, OLD_DR_AMT_BEF_WRITE_OFF, DR_AMT_BEF_WRITE_OFF, DR_TAX_406_BEF_WRITE_OFF "
			    +" FROM TRN_DAILY "
			    +" WHERE INVOICE_NO='"+arrInvoiceNumber[0]+"'"
			    +" AND HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' ";
				System.out.println("sqlData= "+sqlData);
				String [][]arrData = conData.query(sqlData);
				System.out.println("arrData="+arrData.length);
				
				if(arrData.length !=0)
				{
					for(int h=0;h<arrData.length;h++)
					{
						double amount_bef_discount=0, amount_of_discount=0, amount_aft_discount=0, getAftAmount=0, TotalAftAmount=0;
						double oldAmount=0, drAmt=0, oldDrAmt=0, drTax406=0, oldTaxAmt=0, amountBefWriteOff=0,
						oldDrAmtBefWriteOff=0, drAmtBefWriteOff=0, drTax406BefWriteOff=0;
						double oldAmountNew=0, drAmtNew=0, oldDrAmtNew=0, drTax406New=0, oldTaxAmtNew=0, amountBefWriteOffNew=0,
						oldDrAmtBefWriteOffNew=0, drAmtBefWriteOffNew=0, drTax406BefWriteOffNew=0;
						String updateData1="";
						double percent_bef=0, percent_total=0, percent_rs=0;//, ฺฺgetOfAmount=0;
						String invoice_date="";
						String invoice_no			=arrData[h][0]; //System.out.println("invoice_no="+invoice_no);
						invoice_date				=arrData[h][1]; //System.out.println("invoice_date="+invoice_date);
						String hn_no				=arrData[h][2]; //System.out.println("hn_no="+hn_no);
						String payor_office_code	=arrData[h][3]; //System.out.println("payor_office_code="+payor_office_code);
						String transaction_date		=arrData[h][4];	//System.out.println("transaction_date="+transaction_date);
						String order_item_code		=arrData[h][5]; //System.out.println("order_item_code="+order_item_code);
						String invoice_type			=arrData[h][6];
						String doctor_code			=arrData[h][7];
						/*amount_bef_discount			=Double.parseDouble(arrData[h][8]);
						amount_of_discount			=Double.parseDouble(arrData[h][9]);
						amount_aft_discount			=Double.parseDouble(arrData[h][10]);*/
						if(arrData[h][8] !=null)
						{
							amount_bef_discount			=Double.parseDouble(arrData[h][8]);
						}
						if(arrData[h][9] !=null)
						{
							amount_of_discount			=Double.parseDouble(arrData[h][9]);
						}
						if(arrData[h][10] !=null)
						{
							amount_aft_discount			=Double.parseDouble(arrData[h][10]);
						}
						
						String getYear				=arrData[h][11];
						String getMonth				=arrData[h][12];
						String getActive			=arrData[h][13];
						String LineNo				=arrData[h][14];
						String PatientName			=arrData[h][15];
						/*oldAmount					=Double.parseDouble(arrData[h][16]); //System.out.println("oldAmount="+oldAmount);
						drAmt						=Double.parseDouble(arrData[h][17]); //System.out.println("drAmt="+drAmt);
						oldDrAmt					=Double.parseDouble(arrData[h][18]); //System.out.println("oldDrAmt="+oldDrAmt);
						drTax406					=Double.parseDouble(arrData[h][19]); //System.out.println("drTax406="+drTax406);
						oldTaxAmt					=Double.parseDouble(arrData[h][20]); //System.out.println("oldTaxAmt="+oldTaxAmt);
						amountBefWriteOff			=Double.parseDouble(arrData[h][21]); //System.out.println("amountBefWriteOff="+amountBefWriteOff);
						oldDrAmtBefWriteOff			=Double.parseDouble(arrData[h][22]); //System.out.println("oldDrAmtBefWriteOff="+oldDrAmtBefWriteOff);
						drAmtBefWriteOff			=Double.parseDouble(arrData[h][23]); //System.out.println("drAmtBefWriteOff="+drAmtBefWriteOff);
						drTax406BefWriteOff			=Double.parseDouble(arrData[h][24]); //System.out.println("drTax406BefWriteOff="+drTax406BefWriteOff);
						*/
						if(arrData[h][16] !=null)
						{
							oldAmount					=Double.parseDouble(arrData[h][16]);
						}
						if(arrData[h][17] !=null)
						{
							drAmt						=Double.parseDouble(arrData[h][17]);
						}
						if(arrData[h][18] !=null)
						{
							oldDrAmt					=Double.parseDouble(arrData[h][18]);
						}
						if(arrData[h][19] !=null)
						{
							drTax406					=Double.parseDouble(arrData[h][19]);
						}
						if(arrData[h][20] !=null)
						{
							oldTaxAmt					=Double.parseDouble(arrData[h][20]);
						}
						if(arrData[h][21] !=null)
						{
							amountBefWriteOff			=Double.parseDouble(arrData[h][21]);
						}
						if(arrData[h][22] !=null)
						{
							oldDrAmtBefWriteOff			=Double.parseDouble(arrData[h][22]);
						}
						if(arrData[h][23] !=null)
						{
							drAmtBefWriteOff			=Double.parseDouble(arrData[h][23]);
						}
						if(arrData[h][24] !=null)
						{
							drTax406BefWriteOff			=Double.parseDouble(arrData[h][24]);
						}
						//Calculate
						System.out.println("txttypepercent="+TxtTypePercent);
						if(TxtTypePercent !=0)
						{
							System.out.println("TxtTypePercent !=0");
							if(amount_of_discount !=0)
							{
								percent_bef=Double.parseDouble(JNumber.getSaveMoney(100-((amount_aft_discount*100)/amount_bef_discount)));
								if(percent_bef != TxtTypePercent)
								{
									if(percent_bef>TxtTypePercent)
									{
										percent_total=percent_bef-TxtTypePercent;
										percent_rs=percent_bef-percent_total;
										//getAftAmount			=Double.parseDouble(JNumber.getSaveMoney((amount_bef_discount*percent_total)/100));จำนวนเงินที่ต้องเพิ่ม
										getAftAmount			=Double.parseDouble(JNumber.getSaveMoney((amount_bef_discount*percent_rs)/100));
										//TotalAftAmount		=Double.parseDouble(JNumber.getSaveMoney(amount_bef_discount-getAftAmount));
										
									}
									else
									{
										percent_total=TxtTypePercent-percent_bef;
										percent_rs=percent_bef+percent_total;
										//getAftAmount			=Double.parseDouble(JNumber.getSaveMoney((amount_bef_discount*percent_total)/100));
										getAftAmount			=Double.parseDouble(JNumber.getSaveMoney((amount_bef_discount*percent_rs)/100));
										
										
									}
									//TotalAftAmount			=Double.parseDouble(JNumber.getSaveMoney(amount_bef_discount-getAftAmount));
								}
							}
							else
							{
								percent_total=TxtTypePercent;
								percent_rs=TxtTypePercent;
								getAftAmount			=Double.parseDouble(JNumber.getSaveMoney((amount_bef_discount*percent_rs)/100));
							}
							TotalAftAmount			=Double.parseDouble(JNumber.getSaveMoney(amount_bef_discount-getAftAmount));
							System.out.println("amount_bef_discount="+amount_bef_discount);
							System.out.println("percent_rs="+percent_rs);
							System.out.println("TotalAftAmount="+TotalAftAmount);
							if(TotalAftAmount<0)
							{
								TotalAftAmount=0;
							}
									
							oldAmountNew=TotalAftAmount;
							updateData1+=" , OLD_AMOUNT="+oldAmountNew;
							
							if(amountBefWriteOff !=0)
							{
								amountBefWriteOffNew=TotalAftAmount;
								updateData1+=" , AMOUNT_BEF_WRITE_OFF="+amountBefWriteOffNew;
							}
							if(percent_bef != TxtTypePercent)
							{
								if(drAmt !=0)
								{
									double numDrAmt=0;
									numDrAmt=Double.parseDouble(JNumber.getSaveMoney((drAmt*percent_total)/100));
									if(percent_bef>TxtTypePercent)
									{
										drAmtNew=Double.parseDouble(JNumber.getSaveMoney(drAmt+numDrAmt));
									}
									else
									{
										drAmtNew=Double.parseDouble(JNumber.getSaveMoney(drAmt-numDrAmt));
									}
									if(drAmtNew<0)
									{
										drAmtNew=0;
									}
									oldDrAmtNew=drAmtNew;
									updateData1+=" , DR_AMT="+drAmtNew;
									updateData1+=" , OLD_DR_AMT="+oldDrAmtNew;
									
									if(drAmtBefWriteOff !=0)
									{
										drAmtBefWriteOffNew=drAmtNew;
										oldDrAmtBefWriteOffNew=oldDrAmtNew;
										updateData1+=" , OLD_DR_AMT_BEF_WRITE_OFF="+oldDrAmtBefWriteOffNew;
										updateData1+=" , DR_AMT_BEF_WRITE_OFF="+drAmtBefWriteOffNew;
										
									}
								}
								if(drTax406 !=0)
								{
									double numDrTax406=0;
									numDrTax406=Double.parseDouble(JNumber.getSaveMoney((drTax406*percent_total)/100));
									if(percent_bef>TxtTypePercent)
									{
										drTax406New=Double.parseDouble(JNumber.getSaveMoney(drTax406+numDrTax406));
									}
									else
									{
										drTax406New=Double.parseDouble(JNumber.getSaveMoney(drTax406-numDrTax406));
									}
									if(drTax406New<0)
									{
										drTax406New=0;
									}
									oldTaxAmtNew=drTax406New;
									updateData1+=" , DR_TAX_406="+drTax406New;
									updateData1+=" , OLD_TAX_AMT="+oldTaxAmtNew;
									
									if(drTax406BefWriteOff !=0)
									{
										drTax406BefWriteOffNew=drTax406New;
										updateData1+=" , DR_TAX_406_BEF_WRITE_OFF="+drTax406BefWriteOffNew;
									}
								}
							}
						}
						if(percent_bef != TxtTypePercent)
						{
						//INSERT DATA
							insertData="INSERT INTO MA_TRN_DAILY "
								+" (HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, "
								+" HN_NO, PAYOR_OFFICE_CODE, LINE_NO, "
								+" TRANSACTION_DATE, ORDER_ITEM_CODE, INVOICE_TYPE, "
								+" DOCTOR_CODE_OLD, DOCTOR_CODE_NEW, MA_TYPE, "
								+" DIS_TYPE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT_OLD,  "
								+" AMOUNT_OF_DISCOUNT_NEW, AMOUNT_AFT_DISCOUNT_OLD, AMOUNT_AFT_DISCOUNT_NEW, "
								+" UPDATE_DATE, UPDATE_TIME, USER_ID, "
								+" YYYY, MM, ACTIVE_OLD, "
								+" ACTIVE_NEW, NOTE, DIS_PERCENT, "
								+" PATIENT_NAME, AMOUNT_BEF_WRITE_OFF_OLD, AMOUNT_BEF_WRITE_OFF_NEW, "
								+" DR_AMT_OLD, DR_AMT_NEW, OLD_DR_AMT_OLD, OLD_DR_AMT_NEW, "
								+" DR_TAX_406_OLD, DR_TAX_406_NEW, OLD_AMOUNT_OLD, OLD_AMOUNT_NEW, "
								+" OLD_DR_AMT_BEF_WRITE_OFF_OLD, OLD_DR_AMT_BEF_WRITE_OFF_NEW, "
								+" DR_AMT_BEF_WRITE_OFF_OLD, DR_AMT_BEF_WRITE_OFF_NEW, "
								+" OLD_TAX_AMT_OLD, OLD_TAX_AMT_NEW, "
								+" DR_TAX_406_BEF_WRITE_OFF_OLD, DR_TAX_406_BEF_WRITE_OFF_NEW) "
								+" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"', '"+invoice_no+"','"+invoice_date+"', "
								+"'"+hn_no+"', '"+payor_office_code+"', '"+LineNo+"', "
								+"'"+transaction_date+"', '"+order_item_code+"', '"+invoice_type+"', "
								+"'"+doctor_code+"', '', '"+MaType+"', "
								+"'"+TypeDisCount+"', "+amount_bef_discount+", "+amount_of_discount+", "
								+getAftAmount+", "+amount_aft_discount+", "+TotalAftAmount+", "
								+"'"+JDate.getDate()+"', '"+JDate.getTime()+"', '"+session.getAttribute("USER_ID").toString()+"', "
								+"'"+getYear+"', '"+getMonth+"', '"+getActive+"', "
								+"'', '"+getNote+"',"+percent_rs+", "
								+"'"+PatientName+"', "+amountBefWriteOff+", "+amountBefWriteOffNew+", "
								+drAmt+", "+drAmtNew+", "+oldDrAmt+", "+oldDrAmtNew+", "
								+drTax406+", "+drTax406New+", "+oldAmount+", "+oldAmountNew+", "
								+oldDrAmtBefWriteOff+", "+oldDrAmtBefWriteOffNew+", "
								+drAmtBefWriteOff+", "+drAmtBefWriteOffNew+", "
								+oldTaxAmt+", "+oldTaxAmtNew+", "
								+drTax406BefWriteOff+", "+drTax406BefWriteOffNew+")";  
					            System.out.println("sqlInsert="+insertData);
							//ResultSet rsInsertData = con.executeQuery(insertData);
							try
				    	    {
								conData.insert(insertData);
								conData.commitDB();
								
								//UPDATE DATA TABLE TRN_DAILY
								String updateData="UPDATE TRN_DAILY SET ";
								updateData+=" AMOUNT_OF_DISCOUNT="+getAftAmount
								+" , AMOUNT_AFT_DISCOUNT="+TotalAftAmount;
								updateData+=updateData1;
								updateData+=" WHERE HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' "
						        +" AND INVOICE_NO='"+invoice_no+"' AND INVOICE_DATE='"+invoice_date+"' "
						        +" AND LINE_NO='"+LineNo+"'";
						            
						        System.out.println("updateData="+updateData);
								//ResultSet rsInsertData = con.executeQuery(insertData);
								try
							    {
									conData.insert(updateData);
									conData.commitDB();
							    }
							    catch(Exception e)
							    {
							        System.out.println("Update Transaction discount Percent UPDATE_TABLE: TRN_DAILY Excepiton : "+e+"query="+updateData);
							        conData.rollDB();
							    }
								
				    	    }
				    	    catch(Exception e)
				    	    {
				    	       System.out.println("insert_expense DIS TABLE: MA_TRN_DAILY Excepiton : "+e+"query="+insertData);
				    	       conData.rollDB();
				    	    }
						}   
					}
				}
				else
				{
					System.out.println("ไม่พบข้อมูลใน TRN_DAILY");
				}
			
			}
			else// inactive, discount[amount], change
			{
				System.out.println("inactive, discount[amount], change");
				String getLineNo="", getNoteSave="",getDis="",getNote="",getDoctorCode="", getInvoiceNumber="";
				double getOfAmount=0;
				int dd=0;
				System.out.println("arrLineNo.length="+arrLineNo.length);
				for(int nd=0; nd < arrLineNo.length;nd++)
				{
					dd=nd;
					dd++;
					getLineNo		=arrLineNo[nd];
					System.out.println("getLineNo="+getLineNo);
					getInvoiceNumber=arrInvoiceNumber[nd];
					System.out.println("getInvoiceNumber="+getInvoiceNumber);
					System.out.println("===============getLineNo="+getLineNo+"=====================");
					//System.out.println("arrDis.length="+arrDis.length);
					for(int d=0; d<arrDis.length;d++)
					{
						getDis		=arrDis[d];
						System.out.println("getDis="+getDis);
						if(!(getDis.equals("") && getDis ==null) && !(getLineNo.equals("") && getLineNo ==null))
						{
							System.out.println("ok getDis, getLineNo");
							if(getDis.equalsIgnoreCase(getLineNo))
							{
								System.out.println("ok getDis=getLineNo");
								//System.out.println("arrDoctorCode.length="+arrDoctorCode.length);
								if(MaType_discount.equals("1") && MaType_change.equals("1"))//amount, change doctor code
								{
									if(arrDisAmount.length !=0)
									{
										getOfAmount	  =Double.parseDouble(arrDisAmount[nd]);
										System.out.println("dis+ch getOfAmount="+getOfAmount);
									}
									if(request.getParameterValues("DOCTOR_CODE_ARR["+nd+"]") !=null)
									{
										arrDoctorCode=request.getParameterValues("DOCTOR_CODE_ARR["+nd+"]");
										getDoctorCode	=arrDoctorCode[0];
										System.out.println("dis+ch getDoctorCode="+getDoctorCode);
									}
								}
								else if(MaType_discount.equals("1"))
								{
									if(arrDisAmount.length !=0)
									{
										System.out.println("dis arrDisAmount="+arrDisAmount.length);
										if(!arrDisAmount[nd].equals(""))
										{
											getOfAmount	  =Double.parseDouble(arrDisAmount[nd]);
										}
										System.out.println("dis getOfAmount="+getOfAmount);
									}
								}
								else if(MaType_change.equals("1"))
								{
									if(request.getParameterValues("DOCTOR_CODE_ARR["+nd+"]") !=null)
									{
										arrDoctorCode=request.getParameterValues("DOCTOR_CODE_ARR["+nd+"]");
										getDoctorCode	=arrDoctorCode[0];
										System.out.println("ch getDoctorCode="+getDoctorCode);
									}
								}
									
								getNote="";
								if(arrNote.length !=0)
								{
									getNote			=arrNote[nd];
									System.out.println("note="+getNote);
								}
								
								if(MaType_inactive.equals("1")){getNoteSave="Inactive :"; }
								else if(MaType_discount.equals("1") && MaType_change.equals("1")) {getNoteSave="Discount-Change Dr.code :"; }
								else if(MaType_discount.equals("1")) {getNoteSave="Discount :"; }
								else if(MaType_change.equals("1")) {getNoteSave="Change DR.code :"; }
								
								if(ChAllNote.equals("1") )//all note
								{
									
									getNoteSave+=TxtareaNoteAll;
								}
								else
								{
									getNoteSave+=getNote;
								}
								System.out.println("getNoteSave="+getNoteSave);
								if(MaType_inactive.equals("1"))//inactive
								{
									MaType="100";
								}
								else// discount, change
								{
									if(MaType_discount.equals("1") && MaType_change.equals("1"))
									{
										MaType="011";
									}
									else if(MaType_discount.equals("1"))
									{
										MaType="010";
									}
									else if(MaType_change.equals("1"))
									{
										MaType="001";
									}
								}
							//======================================================================================	
								//Delete Data from table MA_TRN_DAILY
								
								String deleteData="DELETE FROM MA_TRN_DAILY WHERE "
							        +" HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' "
							        +" AND LINE_NO='"+getLineNo+"'"
							        +" AND INVOICE_NO='"+getInvoiceNumber+"'"
							        +" AND MA_TYPE='"+MaType+"'";
							        System.out.println("deleteData="+deleteData);
							        //ResultSet rsDelete = con.executeQuery(deleteData);
							        try
						    	    {
										conData.insert(deleteData);
										conData.commitDB();
						    	    }
						    	    catch(Exception e)
						    	    {
						    	       System.out.println("DELETE Transaction inactive, discount, change Excepiton : "+e+"query="+deleteData);
						    	       conData.rollDB();
						    	    }
									
							    //QUERY DATA FROM TRN_DAILY
							    sqlData="SELECT INVOICE_NO, INVOICE_DATE, HN_NO, PAYOR_OFFICE_CODE, TRANSACTION_DATE, "
								    +" ORDER_ITEM_CODE, INVOICE_TYPE, DOCTOR_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, "
								    +" AMOUNT_AFT_DISCOUNT, YYYY, MM, ACTIVE, LINE_NO, "
								    +" PATIENT_NAME, OLD_AMOUNT, DR_AMT, OLD_DR_AMT, DR_TAX_406, "
								    +" OLD_TAX_AMT, AMOUNT_BEF_WRITE_OFF, OLD_DR_AMT_BEF_WRITE_OFF, DR_AMT_BEF_WRITE_OFF, DR_TAX_406_BEF_WRITE_OFF "
								    +" FROM TRN_DAILY "
								    +" WHERE LINE_NO='"+getLineNo+"'"
								    +" AND INVOICE_NO='"+getInvoiceNumber+"'"
							    	+" AND HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' ";
								System.out.println("sqlData= "+sqlData);
								String [][]arrData = conData.query(sqlData);
								System.out.println("arrData="+arrData.length);
								
								double amount_bef_discount=0, amount_of_discount=0, amount_aft_discount=0;
								double oldAmount=0, drAmt=0, oldDrAmt=0, drTax406=0, oldTaxAmt=0, amountBefWriteOff=0,
								oldDrAmtBefWriteOff=0, drAmtBefWriteOff=0, drTax406BefWriteOff=0;
								double oldAmountNew=0, drAmtNew=0, oldDrAmtNew=0, drTax406New=0, oldTaxAmtNew=0, amountBefWriteOffNew=0,
								oldDrAmtBefWriteOffNew=0, drAmtBefWriteOffNew=0, drTax406BefWriteOffNew=0;
								String updateData="", updateData1="";
								double getAftAmountBef=0,percentDis=0,percent_bef=0,percent_total=0,TotalAftAmount=0;
								double getAftAmount=0;
								String getActiveNew="";
								boolean statusDis=false;
								String invoice_date="";
								if(arrData.length >0)
								{
									System.out.println("get Data OK");
									String invoice_no			=arrData[0][0];
									invoice_date				=arrData[0][1];
									String hn_no				=arrData[0][2];
									String payor_office_code	=arrData[0][3];
									String transaction_date		=arrData[0][4];
									String order_item_code		=arrData[0][5];
									String invoice_type			=arrData[0][6];
									String doctor_code			=arrData[0][7];
									System.out.println("invoice_date="+invoice_date);
									if(arrData[0][8] !=null)
									{
										amount_bef_discount			=Double.parseDouble(arrData[0][8]);
									}
									if(arrData[0][9] !=null)
									{
										amount_of_discount			=Double.parseDouble(arrData[0][9]);
									}
									if(arrData[0][10] !=null)
									{
										amount_aft_discount			=Double.parseDouble(arrData[0][10]);
									}
									String getYear				=arrData[0][11];
									String getMonth				=arrData[0][12];
									String getActive			=arrData[0][13];
									String PatientName			=arrData[0][15];
									if(arrData[0][16] !=null)
									{
										oldAmount					=Double.parseDouble(arrData[0][16]);
									}
									if(arrData[0][17] !=null)
									{
										drAmt						=Double.parseDouble(arrData[0][17]);
									}
									if(arrData[0][18] !=null)
									{
										oldDrAmt					=Double.parseDouble(arrData[0][18]);
									}
									if(arrData[0][19] !=null)
									{
										drTax406					=Double.parseDouble(arrData[0][19]);
									}
									if(arrData[0][20] !=null)
									{
										oldTaxAmt					=Double.parseDouble(arrData[0][20]);
									}
									if(arrData[0][21] !=null)
									{
										amountBefWriteOff			=Double.parseDouble(arrData[0][21]);
									}
									if(arrData[0][22] !=null)
									{
										oldDrAmtBefWriteOff			=Double.parseDouble(arrData[0][22]);
									}
									if(arrData[0][23] !=null)
									{
										drAmtBefWriteOff			=Double.parseDouble(arrData[0][23]);
									}
									if(arrData[0][24] !=null)
									{
										drTax406BefWriteOff			=Double.parseDouble(arrData[0][24]);
									}
									
									if(MaType_inactive.equals("1"))//inactive
									{
										//MaType="100";
										getActiveNew="0";
									}
									else// discount, change
									{
										if(MaType_discount.equals("1") && MaType_change.equals("1"))
										{
											//MaType="011";
											statusDis=true;
											//getAftAmount=Double.parseDouble(JNumber.getSaveMoney(amount_bef_discount-getOfAmount));
											//if(getAftAmount<0) 
											//{
											//	getAftAmount=0;
											//}
											
										}
										else if(MaType_discount.equals("1"))
										{
											//MaType="010";
											statusDis=true;
											//getAftAmount=Double.parseDouble(JNumber.getSaveMoney(amount_bef_discount-getOfAmount));
											//if(getAftAmount<0) 
											//{
											//	getAftAmount=0;
											//}
										}
										else if(MaType_change.equals("1"))
										{
											//MaType="001";
										}
									}
										if(statusDis)
										{
											//discount amount, change doctor code
											//หา % ที่ทำการลด
											//(amount_aft_discount*100)/amount_bef_discount
											getAftAmountBef=Double.parseDouble(JNumber.getSaveMoney(amount_bef_discount-getOfAmount));
											percentDis=Double.parseDouble(JNumber.getSaveMoney((getOfAmount*100)/amount_bef_discount));
											if(amount_of_discount !=0)
											{
												percent_bef=Double.parseDouble(JNumber.getSaveMoney(100-((amount_aft_discount*100)/amount_bef_discount)));
												if(percent_bef>percentDis)
												{
													percent_total=percent_bef-percentDis;
												}
												else
												{
													percent_total=percentDis-percent_bef;
												}
											}
											else
											{
												percent_total=percentDis;
											}
											System.out.println("amount_aft_discount="+amount_aft_discount);
											System.out.println("amount_bef_discount="+amount_bef_discount);
											System.out.println("percent_bef="+percent_bef);
											System.out.println("getOfAmount="+getOfAmount);
											System.out.println("percentDis="+percentDis);
											System.out.println("percent_bef=100-((amount_aft_discount*100)/amount_bef_discount)");
											System.out.println("percentDis=(getOfAmount*100)/amount_bef_discount");
											
											TotalAftAmount			=Double.parseDouble(JNumber.getSaveMoney(amount_bef_discount-getOfAmount));
											if(TotalAftAmount<0 || TotalAftAmount==0)
											{
												TotalAftAmount=0;
											}
											oldAmountNew=TotalAftAmount;
											updateData1+=" , OLD_AMOUNT="+oldAmountNew;
											
											if(amountBefWriteOff !=0)
											{
												amountBefWriteOffNew=TotalAftAmount;
												updateData1+=" , AMOUNT_BEF_WRITE_OFF="+amountBefWriteOffNew;
											}
											System.out.println("percent_bef="+percent_bef+"  percentDis="+percentDis);
											if(percent_bef != percentDis)
											{
												if(drAmt !=0)
												{
													double numDrAmt=0;
													numDrAmt=Double.parseDouble(JNumber.getSaveMoney((drAmt*percent_total)/100));
													if(percent_bef>percentDis)
													{
														drAmtNew=Double.parseDouble(JNumber.getSaveMoney(drAmt+numDrAmt));
													}
													else
													{
														drAmtNew=Double.parseDouble(JNumber.getSaveMoney(drAmt-numDrAmt));
													}
													if(drAmtNew<0)
													{
														drAmtNew=0;
													}
													oldDrAmtNew=drAmtNew;
													updateData1+=" , DR_AMT="+drAmtNew;
													updateData1+=" , OLD_DR_AMT="+oldDrAmtNew;
													
													if(drAmtBefWriteOff !=0)
													{
														drAmtBefWriteOffNew=drAmtNew;//drAmt
														oldDrAmtBefWriteOffNew=oldDrAmtNew;//oldDrAmt
														updateData1+=" , OLD_DR_AMT_BEF_WRITE_OFF="+oldDrAmtBefWriteOffNew;
														updateData1+=" , DR_AMT_BEF_WRITE_OFF="+drAmtBefWriteOffNew;
														
													}
												}
												if(drTax406 !=0)
												{
													double numDrTax=0;
													numDrTax=Double.parseDouble(JNumber.getSaveMoney((drTax406*percent_total)/100));
													if(percent_bef>percentDis)
													{
														drTax406New=Double.parseDouble(JNumber.getSaveMoney(drTax406+numDrTax));
													}
													else
													{
														drTax406New=Double.parseDouble(JNumber.getSaveMoney(drTax406-numDrTax));
													}
													if(drTax406New<0)
													{
														drTax406New=0;
													}
													oldTaxAmtNew=drTax406New;
													updateData1+=" , DR_TAX_406="+drTax406New;
													updateData1+=" , OLD_TAX_AMT="+oldTaxAmtNew;
													if(drTax406BefWriteOff !=0)
													{
														drTax406BefWriteOffNew=drTax406New;
														updateData1+=" , DR_TAX_406_BEF_WRITE_OFF="+drTax406BefWriteOffNew;
														
													}
												}
												
												//INSERT DATA discount
												insertData="INSERT INTO MA_TRN_DAILY "
													+" (HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, "
													+" HN_NO, PAYOR_OFFICE_CODE, LINE_NO, "
													+" TRANSACTION_DATE, ORDER_ITEM_CODE, INVOICE_TYPE, "
													+" DOCTOR_CODE_OLD, DOCTOR_CODE_NEW, MA_TYPE, "
													+" DIS_TYPE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT_OLD,  "
													+" AMOUNT_OF_DISCOUNT_NEW, AMOUNT_AFT_DISCOUNT_OLD, AMOUNT_AFT_DISCOUNT_NEW, "
													+" UPDATE_DATE, UPDATE_TIME, USER_ID, "
													+" YYYY, MM, ACTIVE_OLD, "
													+" ACTIVE_NEW, NOTE, DIS_PERCENT, "
													+" PATIENT_NAME, AMOUNT_BEF_WRITE_OFF_OLD, AMOUNT_BEF_WRITE_OFF_NEW, "
													+" DR_AMT_OLD, DR_AMT_NEW, OLD_DR_AMT_OLD, OLD_DR_AMT_NEW, "
													+" DR_TAX_406_OLD, DR_TAX_406_NEW, OLD_AMOUNT_OLD, OLD_AMOUNT_NEW, "
													+" OLD_DR_AMT_BEF_WRITE_OFF_OLD, OLD_DR_AMT_BEF_WRITE_OFF_NEW, "
													+" DR_AMT_BEF_WRITE_OFF_OLD, DR_AMT_BEF_WRITE_OFF_NEW, "
													+" OLD_TAX_AMT_OLD, OLD_TAX_AMT_NEW, "
													+" DR_TAX_406_BEF_WRITE_OFF_OLD, DR_TAX_406_BEF_WRITE_OFF_NEW) "
													+" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"', '"+invoice_no+"','"+invoice_date+"', "
													+"'"+hn_no+"', '"+payor_office_code+"', '"+getLineNo+"', "
													+"'"+transaction_date+"', '"+order_item_code+"', '"+invoice_type+"', "
													+"'"+doctor_code+"', '"+getDoctorCode+"', '"+MaType+"', "
													+"'"+TypeDisCount+"', "+amount_bef_discount+", "+amount_of_discount+", "
													+getOfAmount+", "+amount_aft_discount+", "+TotalAftAmount+", "
													+"'"+JDate.getDate()+"', '"+JDate.getTime()+"', '"+session.getAttribute("USER_ID").toString()+"', "
													+"'"+getYear+"', '"+getMonth+"', '"+getActive+"', "
													+"'"+getActiveNew+"', '"+getNoteSave+"',"+TxtTypePercent+", "
													+"'"+PatientName+"', "+amountBefWriteOff+", "+amountBefWriteOffNew+", "
													+drAmt+", "+drAmtNew+", "+oldDrAmt+", "+oldDrAmtNew+", "
													+drTax406+", "+drTax406New+", "+oldAmount+", "+oldAmountNew+", "
													+oldDrAmtBefWriteOff+", "+oldDrAmtBefWriteOffNew+", "
													+drAmtBefWriteOff+", "+drAmtBefWriteOffNew+", "
													+oldTaxAmt+", "+oldTaxAmtNew+", "
													+drTax406BefWriteOff+", "+drTax406BefWriteOffNew+")";  
												 
									            System.out.println("sqlInsert="+insertData);
													//ResultSet rsInsertData = con.executeQuery(insertData);
													try
										    	    {
														conData.insert(insertData);
														conData.commitDB();
														
														//UPDATE DATA TABLE TRN_DAILY
														updateData="UPDATE TRN_DAILY SET ";
														if(MaType.equals("010"))
														{
															updateData+=" AMOUNT_OF_DISCOUNT="+getOfAmount
															+" , AMOUNT_AFT_DISCOUNT="+TotalAftAmount;
															updateData+=updateData1;
														}
														else //011
														{
															if(getOfAmount !=0 && !getDoctorCode.equals(""))
															{
																updateData+=" AMOUNT_OF_DISCOUNT="+getOfAmount
																+" , AMOUNT_AFT_DISCOUNT="+TotalAftAmount
																+" , DOCTOR_CODE='"+getDoctorCode+"' ";
																updateData+=updateData1;
															}
															else if(getOfAmount ==0 && !getDoctorCode.equals(""))
															{
																updateData+=" DOCTOR_CODE='"+getDoctorCode+"' ";
															}
															else if(getOfAmount !=0 && getDoctorCode.equals(""))
															{
																updateData+=" AMOUNT_OF_DISCOUNT="+getOfAmount
																+" , AMOUNT_AFT_DISCOUNT="+TotalAftAmount;
																updateData+=updateData1;
																
															}
														}
														
														updateData+=" WHERE HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' "
											            +" AND INVOICE_NO='"+invoice_no+"' AND INVOICE_DATE='"+invoice_date+"' "
											            +" AND LINE_NO='"+getLineNo+"' ";
											            System.out.println("updateData="+updateData);
															//ResultSet rsInsertData = con.executeQuery(insertData);
															try
												    	    {
																conData.insert(updateData);
																conData.commitDB();
												    	    }
												    	    catch(Exception e)
												    	    {
												    	       System.out.println("Update Transaction inactive, discount, change Update TRN_DAILY Excepiton : "+e+"query="+updateData);
												    	       conData.rollDB();
												    	    }
										    	    }
										    	    catch(Exception e)
										    	    {
										    	       System.out.println("insert Transaction inactive, discount, change Insert MA_TRN_DAILY Excepiton : "+e+"query="+insertData);
										    	       conData.rollDB();
										    	    }
											}
											else if(!getDoctorCode.equals(""))//ไม่สามารถบันทึกข้อมูล Discount ได้ แต่มีการเปลี่ยน Doctor Code 
											{
												//INSERT DATA change doctor code
												MaType="001";
												insertData="INSERT INTO MA_TRN_DAILY "
													+" (HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, "
													+" HN_NO, PAYOR_OFFICE_CODE, LINE_NO, "
													+" TRANSACTION_DATE, ORDER_ITEM_CODE, INVOICE_TYPE, "
													+" DOCTOR_CODE_OLD, DOCTOR_CODE_NEW, MA_TYPE, "
													+" DIS_TYPE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT_OLD,  "
													+" AMOUNT_OF_DISCOUNT_NEW, AMOUNT_AFT_DISCOUNT_OLD, AMOUNT_AFT_DISCOUNT_NEW, "
													+" UPDATE_DATE, UPDATE_TIME, USER_ID, "
													+" YYYY, MM, ACTIVE_OLD, "
													+" ACTIVE_NEW, NOTE, DIS_PERCENT, "
													+" PATIENT_NAME, AMOUNT_BEF_WRITE_OFF_OLD, AMOUNT_BEF_WRITE_OFF_NEW, "
													+" DR_AMT_OLD, DR_AMT_NEW, OLD_DR_AMT_OLD, OLD_DR_AMT_NEW, "
													+" DR_TAX_406_OLD, DR_TAX_406_NEW, OLD_AMOUNT_OLD, OLD_AMOUNT_NEW, "
													+" OLD_DR_AMT_BEF_WRITE_OFF_OLD, OLD_DR_AMT_BEF_WRITE_OFF_NEW, "
													+" DR_AMT_BEF_WRITE_OFF_OLD, DR_AMT_BEF_WRITE_OFF_NEW, "
													+" OLD_TAX_AMT_OLD, OLD_TAX_AMT_NEW, "
													+" DR_TAX_406_BEF_WRITE_OFF_OLD, DR_TAX_406_BEF_WRITE_OFF_NEW) "
													+" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"', '"+invoice_no+"','"+invoice_date+"', "
													+"'"+hn_no+"', '"+payor_office_code+"', '"+getLineNo+"', "
													+"'"+transaction_date+"', '"+order_item_code+"', '"+invoice_type+"', "
													+"'"+doctor_code+"', '"+getDoctorCode+"', '"+MaType+"', "
													+"'', "+amount_bef_discount+", "+amount_of_discount+", "
													+getOfAmount+", "+amount_aft_discount+", "+TotalAftAmount+", "
													+"'"+JDate.getDate()+"', '"+JDate.getTime()+"', '"+session.getAttribute("USER_ID").toString()+"', "
													+"'"+getYear+"', '"+getMonth+"', '"+getActive+"', "
													+"'"+getActiveNew+"', '"+getNoteSave+"',"+TxtTypePercent+", "
													+"'"+PatientName+"', "+amountBefWriteOff+", "+amountBefWriteOffNew+", "
													+drAmt+", "+drAmtNew+", "+oldDrAmt+", "+oldDrAmtNew+", "
													+drTax406+", "+drTax406New+", "+oldAmount+", "+oldAmountNew+", "
													+oldDrAmtBefWriteOff+", "+oldDrAmtBefWriteOffNew+", "
													+drAmtBefWriteOff+", "+drAmtBefWriteOffNew+", "
													+oldTaxAmt+", "+oldTaxAmtNew+", "
													+drTax406BefWriteOff+", "+drTax406BefWriteOffNew+")";  
												 
									            System.out.println("sqlInsert="+insertData);
													//ResultSet rsInsertData = con.executeQuery(insertData);
													try
										    	    {
														conData.insert(insertData);
														conData.commitDB();
														
														//UPDATE DATA TABLE TRN_DAILY
														updateData="UPDATE TRN_DAILY SET ";
														updateData+=" DOCTOR_CODE='"+getDoctorCode+"' ";
														updateData+=" WHERE HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' "
											            +" AND INVOICE_NO='"+invoice_no+"' AND INVOICE_DATE='"+invoice_date+"' "
											            +" AND LINE_NO='"+getLineNo+"' ";
											            System.out.println("updateData="+updateData);
															//ResultSet rsInsertData = con.executeQuery(insertData);
															try
												    	    {
																conData.insert(updateData);
																conData.commitDB();
												    	    }
												    	    catch(Exception e)
												    	    {
												    	       System.out.println("Update Transaction inactive, discount, change Update TRN_DAILY Excepiton : "+e+"query="+updateData);
												    	       conData.rollDB();
												    	    }
										    	    }
										    	    catch(Exception e)
										    	    {
										    	       System.out.println("insert Transaction inactive, discount, change Insert MA_TRN_DAILY Excepiton : "+e+"query="+insertData);
										    	       conData.rollDB();
										    	    }
											}
										}
										else//inactive, change doctor code
										{
											//INSERT DATA inactive, change doctor code
											insertData="INSERT INTO MA_TRN_DAILY "
												+" (HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, "
												+" HN_NO, PAYOR_OFFICE_CODE, LINE_NO, "
												+" TRANSACTION_DATE, ORDER_ITEM_CODE, INVOICE_TYPE, "
												+" DOCTOR_CODE_OLD, DOCTOR_CODE_NEW, MA_TYPE, "
												+" DIS_TYPE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT_OLD,  "
												+" AMOUNT_OF_DISCOUNT_NEW, AMOUNT_AFT_DISCOUNT_OLD, AMOUNT_AFT_DISCOUNT_NEW, "
												+" UPDATE_DATE, UPDATE_TIME, USER_ID, "
												+" YYYY, MM, ACTIVE_OLD, "
												+" ACTIVE_NEW, NOTE, DIS_PERCENT, "
												+" PATIENT_NAME, AMOUNT_BEF_WRITE_OFF_OLD, AMOUNT_BEF_WRITE_OFF_NEW, "
												+" DR_AMT_OLD, DR_AMT_NEW, OLD_DR_AMT_OLD, OLD_DR_AMT_NEW, "
												+" DR_TAX_406_OLD, DR_TAX_406_NEW, OLD_AMOUNT_OLD, OLD_AMOUNT_NEW, "
												+" OLD_DR_AMT_BEF_WRITE_OFF_OLD, OLD_DR_AMT_BEF_WRITE_OFF_NEW, "
												+" DR_AMT_BEF_WRITE_OFF_OLD, DR_AMT_BEF_WRITE_OFF_NEW, "
												+" OLD_TAX_AMT_OLD, OLD_TAX_AMT_NEW, "
												+" DR_TAX_406_BEF_WRITE_OFF_OLD, DR_TAX_406_BEF_WRITE_OFF_NEW) "
												+" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"', '"+invoice_no+"','"+invoice_date+"', "
												+"'"+hn_no+"', '"+payor_office_code+"', '"+getLineNo+"', "
												+"'"+transaction_date+"', '"+order_item_code+"', '"+invoice_type+"', "
												+"'"+doctor_code+"', '"+getDoctorCode+"', '"+MaType+"', "
												+"'"+TypeDisCount+"', "+amount_bef_discount+", "+amount_of_discount+", "
												+getOfAmount+", "+amount_aft_discount+", "+TotalAftAmount+", "
												+"'"+JDate.getDate()+"', '"+JDate.getTime()+"', '"+session.getAttribute("USER_ID").toString()+"', "
												+"'"+getYear+"', '"+getMonth+"', '"+getActive+"', "
												+"'"+getActiveNew+"', '"+getNoteSave+"',"+TxtTypePercent+", "
												+"'"+PatientName+"', "+amountBefWriteOff+", "+amountBefWriteOffNew+", "
												+drAmt+", "+drAmtNew+", "+oldDrAmt+", "+oldDrAmtNew+", "
												+drTax406+", "+drTax406New+", "+oldAmount+", "+oldAmountNew+", "
												+oldDrAmtBefWriteOff+", "+oldDrAmtBefWriteOffNew+", "
												+drAmtBefWriteOff+", "+drAmtBefWriteOffNew+", "
												+oldTaxAmt+", "+oldTaxAmtNew+", "
												+drTax406BefWriteOff+", "+drTax406BefWriteOffNew+")";  
											 
								            System.out.println("sqlInsert="+insertData);
												//ResultSet rsInsertData = con.executeQuery(insertData);
												try
									    	    {
													conData.insert(insertData);
													conData.commitDB();
													
													//UPDATE DATA TABLE TRN_DAILY
													updateData="UPDATE TRN_DAILY SET ";
													if(MaType.equals("100"))
													{
														updateData+=" ACTIVE='"+getActiveNew+"' ";
													}
													else if(MaType.equals("001"))
													{
														updateData+=" DOCTOR_CODE='"+getDoctorCode+"' ";
														
													}
													updateData+=" WHERE HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' "
										            +" AND INVOICE_NO='"+invoice_no+"' AND INVOICE_DATE='"+invoice_date+"' "
										            +" AND LINE_NO='"+getLineNo+"' ";
										            System.out.println("updateData="+updateData);
														//ResultSet rsInsertData = con.executeQuery(insertData);
														try
											    	    {
															conData.insert(updateData);
															conData.commitDB();
											    	    }
											    	    catch(Exception e)
											    	    {
											    	       System.out.println("Update Transaction inactive, discount, change Update TRN_DAILY Excepiton : "+e+"query="+updateData);
											    	       conData.rollDB();
											    	    }
									    	    }
									    	    catch(Exception e)
									    	    {
									    	       System.out.println("insert Transaction inactive, discount, change Insert MA_TRN_DAILY Excepiton : "+e+"query="+insertData);
									    	       conData.rollDB();
									    	    }
										}//else //inactive, change doctor code
								}//if(arrData.length >0)
								else
								{
									System.out.println("ไม่พบข้อมูลใน Trn Daily");
								}
							}//if(getDis.equalsIgnoreCase(getLineNo))
							else
							{
								System.out.println("Code ไม่ตรงกัน");
							}
						}//if(!(getDis.equals("") && getDis ==null) && !(getLineNo.equals("") && getLineNo ==null))
						else
						{
							System.out.println("Discount & LineNo IS NULL");
						}
					}
				}//for
			}//else
				
            session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/transaction_edit.jsp"));
            con.Close();
            //con.freeConnection();
            response.sendRedirect("../message.jsp");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
    </head>
    <body>
    </body>
</html>
