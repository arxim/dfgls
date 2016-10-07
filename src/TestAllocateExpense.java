import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import df.bean.db.table.Doctor;
import df.bean.db.table.TrnExpenseDetail;
import df.bean.obj.util.DoctorProfileService;
import df.bean.obj.util.MapUtil;
import df.bean.obj.util.TrnExpenseDetailService;
import df.bean.process.summary.Summary;
import df.bean.process.summary.SummaryCreater;

/**
 * 
 * @author admin
 */
public class TestAllocateExpense {

	/**
	 * @param args
	 *            the command line arguments
	 */

	public static void main(String[] args) {

    	String YYYY = "2014";
    	String MM = "05";
    	String HOSPITAL_CODE = "011";
        Map<String, Double> noProcessList = new HashMap<String, Double>();

    	SummaryCreater create = new SummaryCreater(HOSPITAL_CODE,YYYY, MM, "2" , "20140610");
    	Summary  summary  = create.createSummary("monthly");
     	summary.setRevenueType("all");

     	ArrayList<HashMap<String, String>> summaryDocCode = new ArrayList<HashMap<String, String>>();
     	summaryDocCode = summary.getData();//<doctor code, revenue>//Know Net

     	
    	ArrayList<HashMap<String, String>> notFoundNewDrCode = new ArrayList<HashMap<String, String>>();

     	TrnExpenseDetailService trnExpenseDetailService = new TrnExpenseDetailService();

     	//trnExpenseDetailService.UpdateOLD_DOCTOR_CODE(YYYY, MM,HOSPITAL_CODE);//Don't forget to set OLD_DR_CODE before
     	     	
     	for(HashMap<String, String> recordDocCode:summaryDocCode){
     		
     		double netAmount = Double.parseDouble(recordDocCode.get("DR_NET_PAID_AMT"));
     		if(netAmount<0){//Don't forget to change netAmount<0
     			
         		String docCodeToken = recordDocCode.get("DOCTOR_CODE");//Get DrCode has problem
         		String hospital = recordDocCode.get("HOSPITAL_CODE");
         		System.out.println("-----------------------------------Start TrnExpense process---------------------------------------");
         		System.out.println("Doctor code : "+docCodeToken+ " Net_amount : "+netAmount+" Hospitalcode : "+hospital);

      
         		ArrayList<TrnExpenseDetail> minusExpenseDetails = trnExpenseDetailService.GetExpensesDetailByDoctorCode(docCodeToken, HOSPITAL_CODE, YYYY, MM);//Keep expenses of DrCode
         		System.out.println("\tMinus Expense of this doctor code size is "+minusExpenseDetails.size());
         		
         		//Show minus expense list
         		for(TrnExpenseDetail minusExpenseDetail:minusExpenseDetails){
         			System.out.println("\t\t Line no: "+minusExpenseDetail.getLineNumber()+"  \tcost: "+minusExpenseDetail.getAmount());
         		}
         		//End show minus expense list
         		
         		boolean netAmounPlus = false;
         		
         		for(TrnExpenseDetail minusExpenseDetail:minusExpenseDetails){
         			
         			
         			if(netAmounPlus==false){//If this net amount not positive yet
         				System.out.println("---Next expense---");
             			System.out.println("This Expense detail cost is: "+minusExpenseDetail.getAmount()+" line no. is "+minusExpenseDetail.getLineNumber());
         				
                 			DoctorProfileService doctorProfileService = new DoctorProfileService();   
                 			ArrayList<Doctor> doctors = doctorProfileService.GetDoctorsByDoctorCode(docCodeToken, HOSPITAL_CODE);
        					//Find DrProfile Related
        					Doctor doctorProfile = doctors.get(0);//It has only 1
        					String doctorProfileCode = doctorProfile.getDoctorProfileCode();
        					System.out.println("DrProfileSize: "+doctors.size()+" DoctorPrifile[0]: "+doctorProfileCode);
        					ArrayList<String> drCodes = doctorProfileService.GetDoctorCodesByDoctorProflie(doctorProfileCode,docCodeToken, HOSPITAL_CODE);
                     		//End find DrProfile Related
        					System.out.println("DrCode of this DrProfile size : "+drCodes.size());
        					System.out.println("List :");
        					for(String drCode:drCodes){
        						System.out.println("\t"+drCode);
        					}
        					//Filter DrCode is not minus Net
        					ArrayList<String> drCodesPositiveNet = new ArrayList<String>();
        					for(String drCode:drCodes){
        						for(HashMap<String, String> scanRecord:summaryDocCode){
         							if(scanRecord.get("DOCTOR_CODE").equals(drCode)){
         								Double net = Double.parseDouble(scanRecord.get("DR_NET_PAID_AMT"));
         								if(net>0&&!drCodesPositiveNet.contains(drCode)){
         									drCodesPositiveNet.add(drCode);
         								}
         							}
         						}
        					}
        					drCodes = drCodesPositiveNet;
        					//End of Filter DrCode is not minus Net
        					System.out.println("DrCode positive Net size : "+drCodes.size());
        					System.out.println("List :");
        					for(String drCode:drCodes){
        						System.out.println("\t"+drCode);
        					}
        					//Start find DR_NET_PAID_AMT and use index to point each DrCode
        					ArrayList<Double> netOfEachDrCode = new ArrayList<Double>();
                     			for(String drCode:drCodes){
                     					netOfEachDrCode.add(trnExpenseDetailService.GetNetOfDoctorCode(summaryDocCode, drCode));
                     		}//End find DR_NET_PAID_AMT and use index to point each DrCode
                     			System.out.println("-----After sort-----");
                                for(int a=0;a<=drCodes.size()-1;a++){
                                	System.out.println("\t"+"DrCode : "+drCodes.get(a)+" , Net current : "+netOfEachDrCode.get(a));
                                }
                                
                                
                                
                     			//Sort Net of each drCode
                                 Map<String, Double> map1 = new HashMap<String, Double>();

                                 for(int i=0;i<=drCodes.size()-1;i++){
                                         map1.put(drCodes.get(i), netOfEachDrCode.get(i));
                                 }
                                 map1 = MapUtil.sortByValue(map1);
                                drCodes = new ArrayList<String>();
                                netOfEachDrCode = new ArrayList<Double>();
                                Iterator it = map1.entrySet().iterator();
                                while (it.hasNext()) {
                                	Map.Entry pairs = (Map.Entry)it.next();
                                    drCodes.add((String) pairs.getKey() );
                                    netOfEachDrCode.add((Double) pairs.getValue());
                                    it.remove(); // avoids a ConcurrentModificationException
                                }

                                
                                
                                //End Sort Net of each drCode
                                
                                
                                
                                System.out.println("-----Before sort-----");
                                for(int a=0;a<=drCodes.size()-1;a++){
                                	System.out.println("\t"+"DrCode : "+drCodes.get(a)+" , Net current : "+netOfEachDrCode.get(a));
                                }
                                
            					System.out.println("---List Of DrCode---");
                                for(int a=0;a<=drCodes.size()-1;a++){
                                	System.out.println("\t"+"DrCode : "+drCodes.get(a)+" , Net current : "+netOfEachDrCode.get(a));
                                }
            					System.out.println("---End List Of DrCode---");

                                
                                
                                
                                boolean done = false;//Check this expense can find new net yet
                     			//Find new drCode to new expense
                                for(int i=0;i<=netOfEachDrCode.size()-1;i++){
                     					if(done == false){
                     						
                         					Double currentExpense = minusExpenseDetail.getAmount();//Expense of old doctor code
                         					String currentOldDocCode = docCodeToken;//Old doctor code
                         					Double currentOldDocCodeNet = netAmount;//Net of old doctor code
                         					String currentNewDocCode = drCodes.get(i);//New doctor code
                         					Double currentNewDocCodeNet = netOfEachDrCode.get(i);//Net of new doctor code
                         					String expenseLineNumber = minusExpenseDetail.getLineNumber();
                         					
                         					
                         					if(currentNewDocCodeNet>=currentExpense){
                         						System.out.println("This Expense detail cost is: "+minusExpenseDetail.getAmount()+" line no. is "+minusExpenseDetail.getLineNumber());
                         						System.out.println("Found new DocCode can replace, that is: "+currentNewDocCode+" and has DR_NET_PAID_AMT is: "+currentNewDocCodeNet);
                         						System.out.println("----Before update----");
                         						System.out.println("\t"+"OldDoctorCode: "+currentOldDocCode+" DR_NET_PAID_AMT is: "+currentOldDocCodeNet);
                         						System.out.println("\t"+"NewDoctorCode: "+currentNewDocCode+" DR_NET_PAID_AMT is: "+currentNewDocCodeNet);
                         						System.out.println("----After update----");
                         						currentNewDocCodeNet = currentNewDocCodeNet - currentExpense;//New net of new drCode = old net - this expense
                         						currentOldDocCodeNet = currentOldDocCodeNet + currentExpense;//New net of old drCode = old net + this expense
                         						System.out.println("\t"+"OldDoctorCode: "+currentOldDocCode+" DR_NET_PAID_AMT is: "+currentOldDocCodeNet);
                         						System.out.println("\t"+"NewDoctorCode: "+currentNewDocCode+" DR_NET_PAID_AMT is: "+currentNewDocCodeNet);
                         						
                         						//Update to DB
                         						System.out.println("Update DB : "+YYYY+":"+MM+":"+docCodeToken+":"+HOSPITAL_CODE+" Line : "+expenseLineNumber+" New Code : "+currentNewDocCode);
                         						//trnExpenseDetailService.UpdateDoctorCodeOfExpenseDetail(YYYY, MM, currentOldDocCode, HOSPITAL_CODE,expenseLineNumber, currentNewDocCode);//Update new DrCode to the DB
                         						//End Update to DB
                         						
                         						//Update current "DR_NET_PAID_AMT" to the HashMap
                         						recordDocCode.put("DR_NET_PAID_AMT", Double.toString(currentOldDocCodeNet));
                         						//End update current "DR_NET_PAID_AMT" to the HashMap
                         						
                         						//Update new net to new drCode
                         						for(HashMap<String, String> scanRecord:summaryDocCode){
                         							if(scanRecord.get("DOCTOR_CODE").equals(currentNewDocCode)){
                         								scanRecord.put("DR_NET_PAID_AMT",Double.toString(currentNewDocCodeNet));
                         							}
                         						}
                         						//End update new net to new drCode
                         						
                         						//Update new net of new DrCode to the net list
                         						netOfEachDrCode.set(i,currentNewDocCodeNet);//Set new net to list
                         						//End update new net of new DrCode to the current net
                         						
                         						//Update new "DOCTOR_CODE" to the minusExpenseDetail detail 
                         						minusExpenseDetail.setDoctorCode(currentNewDocCode);
                         						//End update new "DOCTOR_CODE" to the minusExpenseDetail detail 
                         						
                         						i--;//Back if net enough with next expense
                         						
                         						
                         						System.out.println("Now "+recordDocCode.get("DOCTOR_CODE")+" DR_NET_PAID_AMT is: "+Double.parseDouble(recordDocCode.get("DR_NET_PAID_AMT")));
                         						if(Double.parseDouble(recordDocCode.get("DR_NET_PAID_AMT"))>=0){	
                         							netAmounPlus = true;
                         							System.out.println("Now "+recordDocCode.get("DOCTOR_CODE")+" already no debt");
                         						}
                         						done = true;
                         					}
                     					}                   					
                     				}
                                System.out.println("---End of this expense---");
         					}
         			
         		}//End of loop minusEnpense
     			if(!netAmounPlus){
     				System.out.println("Get DrCode can't find new drCode to the notFoundNewDrCodeList");
					notFoundNewDrCode.add(recordDocCode);//Can't find DrCode to replace instead
     			}         
     			System.out.println("------------------------------------End TrnExpense process-------------------------------------------");
     		}
     		else{//Get DrCode NetAmount >= 0
     			noProcessList.put(recordDocCode.get("DOCTOR_CODE"), Double.parseDouble(recordDocCode.get("DR_NET_PAID_AMT")));
     		}
     	}

     	System.out.println("Done this process : "+noProcessList.size()); 
     	System.out.println("notFoundNewDrCode : "+notFoundNewDrCode.size());
     	for(HashMap<String, String> notFoundDr:notFoundNewDrCode){
     		System.out.println(notFoundDr.get("DOCTOR_CODE"));
     	}
     	
    }
}
