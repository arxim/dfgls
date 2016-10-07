import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import df.bean.db.table.Doctor;
import df.bean.db.table.TrnExpenseDetail;
import df.bean.obj.util.DoctorProfileService;
import df.bean.obj.util.TrnExpenseDetailService;
import df.bean.process.summary.Summary;
import df.bean.process.summary.SummaryCreater;

/**
 * 
 * @author admin
 */
public class TestAllocateExpenseOld {

	/**
	 * @param args
	 *            the command line arguments
	 */

	public static void main(String[] args) {

    	String YYYY = "2014";
    	String MM = "04";
    	String HOSPITAL_CODE = "011";
        Map<String, Double> noProcessList = new HashMap<String, Double>();

    	SummaryCreater create = new SummaryCreater(HOSPITAL_CODE,YYYY, MM, "2" , "20140512");
    	Summary  summary  = create.createSummary("monthly");
     	summary.setRevenueType("all");

     	ArrayList<HashMap<String, String>> summaryDocCode = new ArrayList<HashMap<String, String>>();
     	summaryDocCode = summary.getData();//<doctor code, revenue>//Know Net

     	ArrayList<TrnExpenseDetail> expenseDetails = new ArrayList<TrnExpenseDetail>();
    	ArrayList<HashMap<String, String>> notFoundNewDrCode = new ArrayList<HashMap<String, String>>();

     	TrnExpenseDetailService trnExpenseDetailService = new TrnExpenseDetailService();

     	trnExpenseDetailService.UpdateOLD_DOCTOR_CODE(YYYY, MM,HOSPITAL_CODE);//Don't forget to set OLD_DR_CODE before
     	     	
     	for(HashMap<String, String> recordDocCode:summaryDocCode){
     		
     		double netAmount = Double.parseDouble(recordDocCode.get("DR_NET_PAID_AMT"));
     		if(netAmount<0){//Don't forget to change netAmount<0
     			
         		String docCodeToken = recordDocCode.get("DOCTOR_CODE");//Get DrCode has problem
         		String hospital = recordDocCode.get("HOSPITAL_CODE");
         		System.out.println("--------------------------------------------------------------------------");
         		System.out.println("Doctor code : "+docCodeToken+ " Net_amount : "+netAmount+" Hospitalcode : "+hospital);

         		expenseDetails = trnExpenseDetailService.GetExpensesDetailByDoctorCode(docCodeToken, HOSPITAL_CODE, YYYY, MM);//Keep expenses of DrCode
         		ArrayList<TrnExpenseDetail> minusExpenseDetails = new ArrayList<TrnExpenseDetail>();
         		//Must check the minus net is being to positive yet? if positive loop can stop     		
         		for(TrnExpenseDetail expenseDetail:expenseDetails){//Each order of expense and 1 DrCode can have many Expenses
         			if(expenseDetail.getExpense_sign() == -1){
         				minusExpenseDetails.add(expenseDetail);
         			}
         		}
         		System.out.println("\tMinus Expense of this doctor code size is "+minusExpenseDetails.size());
         		
         		for(TrnExpenseDetail minusExpenseDetail:minusExpenseDetails){
         			System.out.println("\t\t Line no: "+minusExpenseDetail.getLineNumber()+"  \tcost: "+minusExpenseDetail.getAmount());
         		}
         		
         		boolean netAmounPlus = false;
         		
         		for(TrnExpenseDetail minusExpenseDetail:minusExpenseDetails){
         			boolean done = false;
         			if(netAmounPlus==false){
		
             			if(done == false){
                 			DoctorProfileService doctorProfileService = new DoctorProfileService();   
                 			ArrayList<Doctor> doctors = doctorProfileService.GetDoctorsByDoctorCode(docCodeToken, HOSPITAL_CODE);
        					//Find DrProfile Related
        					
        					for(Doctor doctor:doctors){//Each Doctor
                 				if(done == false){
            						ArrayList<String> drCodes = doctorProfileService.GetDoctorCodesByDoctorProflie(doctor.getDoctorProfileCode(),docCodeToken, HOSPITAL_CODE);
                     				ArrayList<Double> a = new ArrayList<Double>();
                     				for(String drCode:drCodes){
                     					a.add(trnExpenseDetailService.GetNetOfDoctorCode(summaryDocCode, drCode));
                     				}//Got all DR_NET_PAID_AMT and use index to point each DrCode
                     				//Add
                                    Map<String, Double> map1 = new HashMap<String, Double>();
                                    for(int i=0;i<=drCodes.size()-1;i++){
                                           map1.put(drCodes.get(i), a.get(i));
                                    }
                                    System.out.println("Pring Map "+ map1);
                                    Collections.sort(a);
                                    drCodes = new ArrayList<String>();
                                    Iterator it = map1.entrySet().iterator();
                                	while (it.hasNext()) {
                                		Map.Entry pairs = (Map.Entry)it.next();
                                        drCodes.add((String) pairs.getKey() );
                                        it.remove(); // avoids a ConcurrentModificationException
                                	}
                                    //End Add
                                    	
                     				for(int i=0;i<=a.size()-1;i++){
                     					if(done == false){
                     						
                         					Double eachPositiveNetAmount = a.get(i);
                         					if(eachPositiveNetAmount>=minusExpenseDetail.getAmount()){
                         						String newDocCode = drCodes.get(i);
                         						System.out.println("This NET_AMT is: "+eachPositiveNetAmount);
                         						System.out.println("This Expense detail cost is: "+minusExpenseDetail.getAmount()+" line no. is "+minusExpenseDetail.getLineNumber());
                         						System.out.println("New DocCode is: "+newDocCode+" and has DR_NET_PAID_AMT is: "+eachPositiveNetAmount);
                         						eachPositiveNetAmount = eachPositiveNetAmount - minusExpenseDetail.getAmount();
                         						
                         						System.out.println("And d "+recordDocCode.get("DOCTOR_CODE")+" DR_NET_PAID_AMT is: "+Double.toString(netAmount+minusExpenseDetail.getAmount()));
                         						System.out.println("And n "+newDocCode+" DR_NET_PAID_AMT is: "+eachPositiveNetAmount);
                         						
                         						System.out.println("Update DB : "+YYYY+":"+MM+":"+docCodeToken+":"+HOSPITAL_CODE+" Line : "+minusExpenseDetail.getLineNumber()+" New Code : "+newDocCode);
                         						trnExpenseDetailService.UpdateDoctorCodeOfExpenseDetail(YYYY, MM, docCodeToken, HOSPITAL_CODE, minusExpenseDetail.getLineNumber(), newDocCode);//Update new DrCode to the DB
                         						
                         						recordDocCode.put("DR_NET_PAID_AMT", Double.toString(netAmount+minusExpenseDetail.getAmount()));
                         						//Update "DR_NET_PAID_AMT" to the HashMap
                         						
                         					
                         						for(HashMap<String, String> scanRecord:summaryDocCode){
                         							if(scanRecord.get("DOCTOR_CODE").equals(newDocCode)){
                         								scanRecord.put("DR_NET_PAID_AMT",Double.toString(eachPositiveNetAmount));
                         							}
                         						}
                         						a.set(i,eachPositiveNetAmount);

                         						minusExpenseDetail.setDoctorCode(newDocCode);//Update new "DOCTOR_CODE" to the expense detail 
                         						
                         						i--;
                         						System.out.println("Now "+recordDocCode.get("DOCTOR_CODE")+" DR_NET_PAID_AMT is: "+Double.parseDouble(recordDocCode.get("DR_NET_PAID_AMT"))+" morethan 0");
                         						if(Double.parseDouble(recordDocCode.get("DR_NET_PAID_AMT"))>0){	
                         							netAmounPlus = true;
                         						}
                         						
                         						done = true;
                         					}
                     					}                   					
                     				}
                 				}
                 			}
             			}	
         			}
         			if(!done){
         				System.out.println("Get not found to the notFoundNewDrCode");
         				noProcessList.put(recordDocCode.get("DOCTOR_CODE"), Double.parseDouble(recordDocCode.get("DR_NET_PAID_AMT")));
 						notFoundNewDrCode.add(recordDocCode);
         			}

         		}//End of loop minusEnpense
     		}
     	}
     	System.out.println("notFoundNewDrCode : "+notFoundNewDrCode.size());
     	System.out.println("Done this process : "+noProcessList);   	
    }
}
