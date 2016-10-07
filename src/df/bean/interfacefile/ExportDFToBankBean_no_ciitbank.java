/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.*;

/**
 *
 * @author nopphadon
 */
public class ExportDFToBankBean_no_ciitbank extends InterfaceTextFileBean {
    private ResultSet rs;
    private Statement stm;
    
    @Override
    public boolean insertData(String fn, DBConnection d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path) {
        String[] sub_data = null;
        String qy = "";
        if(this.getOwnerBank().equals("011")){
        	qy = "LIKE '011'";
        }else{
        	qy = "NOT LIKE '011'";
        }
        String dat = "SELECT FILE_TYPE, RECORD_TYPE, BATCH_NUMBER, RECEIVING_BANK_CODE," + //0-3
        "RECEIVING_BRANCH_CODE, RECEIVING_ACCOUNT_NO, SENDING_BANK_CODE, SENDING_BRANCH_CODE," + //4-7
        "SENDING_ACCOUNT_NO, EFFECTIVE_DATE, SERVICE_TYPE, CLEARING_HOUSE_CODE,"+ //8-11
        "TRANSFER_AMOUNT, RECEIVER_INFORMATION, SENDER_INFORMATION, OTHER_INFORMATION,"+ //12-15
        "REFERENCE_RUNNING_NO, SPACE, COMPANY_CODE, SEQUENCE_NUMBER "+ //16-19
        "FROM BANK_TMB_MEDIA_CLEARING WHERE (BATCH_NO IS NULL OR BATCH_NO = '') AND "+
        "YYYY LIKE '"+year+"' AND MM LIKE '"+month+"' AND SERVICE_TYPE LIKE '"+type+"' AND "+
        "HOSPITAL_CODE LIKE '"+hp+"' AND RECEIVING_BANK_CODE "+qy;
        //"HOSPITAL_CODE LIKE '"+hp+"' AND RECEIVING_BANK_CODE LIKE '"+this.getOwnerBank()+"'";
        boolean status = true;
        //System.out.println(dat);
        String[][] temp_data = null;
        try {
            setFileName(path);//set filename read
            temp_data = d.query(dat);
        }catch(Exception e){
            System.out.println(e);
        }
        writeFileNew(newBankMedia(temp_data));
        //writeFileNew(bankUOB(temp_data));
        return status;
    }
    
    private String[] newBankMedia(String[][] t){
    	String[] dt = new String[t.length];
    	for(int i = 0; i<t.length; i++){
            dt[i] = "TXN";  //Write Default Value of "File Type"
            dt[i] = dt[i]+checkString(t[i][14],120," ","b"); //---------------(02)Payor Name
            dt[i] = dt[i]+checkString(t[i][13],130," ","b"); //---------------(03)Receive Name
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(04)Mail to Name
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(05)Address 1
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(06)Address 2
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(07)Address 3
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(08)Address 4
            dt[i] = dt[i]+checkString("",10," ","b");        //---------------(09)Zip Code
            dt[i] = dt[i]+checkString("",16," ","b");        //---------------(10)Cust. Reference
            dt[i] = dt[i]+checkString(t[i][9],8," ","b");    //---------------(11)Effective Date
            dt[i] = dt[i]+checkString("",8," ","b");         //---------------(12)Pickup Date
            dt[i] = dt[i]+checkString("THB",3," ","b");      //---------------(13)Payment Currency
            dt[i] = dt[i]+checkString("",15," ","b");        //---------------(14)Comcode
            dt[i] = dt[i]+checkString("",15," ","b");        //---------------(15)Run Date
            dt[i] = dt[i]+checkString("",20," ","b");        //---------------(16)Vendor Code
            dt[i] = dt[i]+checkString(t[i][8],20," ","b");   //---------------(17)Debit Amount Number
            
            //String amount = ""+JNumber.setFormat(Double.parseDouble(t[i][12]) /100.00,"0.00");
            //dt[i] = dt[i]+checkString(amount,15," ","f");    //---------------(18)Payment Amount
            dt[i] = dt[i]+checkString(JNumber.getMoneyFormat(t[i][12]),15," ","f");//(18)Payment Amount
            dt[i] = dt[i]+checkString(t[i][3]+t[i][4],16," ","b");//----------(19)Bank+Branch Code
            dt[i] = dt[i]+checkString(t[i][5],20," ","b");   //---------------(20)Bank Account
            dt[i] = dt[i]+checkString(t[i][10],2," ","b");   //---------------(21)Pay type "Smart" 04
            dt[i] = dt[i]+checkString("00",2," ","b");       //---------------(22)Baht net "Default 00"
            dt[i] = dt[i]+checkString("",2," ","b");         //---------------(23)Delivery Method
            dt[i] = dt[i]+checkString("",20," ","b");        //---------------(24)Pay Check Location
            dt[i] = dt[i]+checkString("EMAIL",5," ","b");    //---------------(25)Advise Mode
            dt[i] = dt[i]+checkString("",50," ","b");        //---------------(26)Fax No.
            dt[i] = dt[i]+checkString("",50," ","b");        //---------------(27)Email Address
            dt[i] = dt[i]+checkString("",50," ","b");        //---------------(28)Mobile
            dt[i] = dt[i]+checkString("BEN",13," ","b");     //---------------(29)Fee Charge
            if(t[i][3].equals("011")){
            	dt[i] = dt[i]+checkString("DCB",3," ","b");  //---------------(30)Transaction Type
            }else{
            	dt[i] = dt[i]+checkString("MCL",3," ","b");  //---------------(30)Transaction Type
            }
            dt[i] = dt[i]+checkString("",5," ","b");         //---------------(31)Blank
            dt[i] = dt[i]+checkString("",4," ","b");         //---------------(32)Blank
            dt[i] = dt[i]+checkString("",30," ","b");        //---------------(33)Blank
            dt[i] = dt[i]+checkString("",105," ","b");       //---------------(34)Blank
            dt[i] = dt[i]+checkString("",70," ","b");        //---------------(35)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(36)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(37)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(38)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(39)Blank
            dt[i] = dt[i]+checkString("",16," ","b");        //---------------(40)Blank
            dt[i] = dt[i]+checkString("",7," ","b");         //---------------(41)Blank
            dt[i] = dt[i]+checkString("",2," ","b");         //---------------(42)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(43)Blank
            dt[i] = dt[i]+checkString("END",3," ","b");      //---------------(44)Blank
        }
    	return dt;
    }

    private String[] bankUOB(String[][] temp_data){
    	String[] sub_data = new String[temp_data.length]; 
        for(int i = 0; i<temp_data.length; i++){
            sub_data[i] = "102000001";  //Write Default Value of "File Type"
            
            for(int x = 0; x<temp_data[i].length; x++){
                //System.out.println(temp_data[i].length+" in column : "+x);
                if(x == 3){        //---------------------------------------Receive Bank Code
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],3," ","b");
                }else if(x == 4){  //---------------------------------------Receive Bank Branch
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],4,"0","f");
                }else if(x == 5){  //---------------------------------------Receive Account No.
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],11,"0","f");
                }else if(x == 6){  //---------------------------------------Send Bank Code
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],3," ","b");
                }else if(x == 7){  //---------------------------------------Send Bank Branch
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],4,"0","f");
                }else if(x == 8){  //---------------------------------------Send Account No.
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],11,"0","f");
                }else if(x == 9){  //---------------------------------------Effective Date
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],8," ","b");
                }else if(x == 10){  //--------------------------------------Service Type
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],2," ","b");
                }else if(x == 11){  //--------------------------------------Clearing House Code
                    sub_data[i] = sub_data[i] + "00";
                }else if(x == 12){  //--------------------------------------Transfer Amount
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],12,"0","f");
                }else if(x == 13){  //--------------------------------------Receive Information
                    //new String(sub_data[11].trim().getBytes(),"TIS-620")
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],60," ","b");
                }else if(x == 14){  //--------------------------------------Sender Information
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],60," ","b");
                }else if(x == 15){  //--------------------------------------Other Information
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],100," ","b");
                }else if(x == 16){  //--------------------------------------Running No.
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],6,"0","f");
                    //sub_data[i] = sub_data[i] + checkString(""+i,6,"0","f"); //Running from this method
                }else if(x == 17){  //--------------------------------------Space
                    sub_data[i] = sub_data[i] + checkString("",16," ","f");
                }else if(x == 18){  //--------------------------------------Company Code
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],4," ","b");
                }else if(x == 19){  //--------------------------------------Filler
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],5,"0","f");
                }else{}
            }
        }
        return sub_data;
    }
}
