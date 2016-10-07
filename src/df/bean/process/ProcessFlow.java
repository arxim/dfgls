package df.bean.process;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author T.
 */
public class ProcessFlow {

    private final int dom[] = {
        0, 31, 29, 31,  /* null, jan, feb, mar */
        30, 31, 30,  /* apr, may, jun */
        31, 31, 30,  /* jul, aug, sep */
        31, 30, 31   /* oct, nov, dec */
    };
    private String hospital_code = "";
    private DBConnection conn = null;

    public ProcessFlow(){
    	this.conn = new DBConnection();
    }
    
    public ProcessFlow(DBConnection conn, String hospital_code) {
    	this.hospital_code = hospital_code;
    	//System.out.println("hospital_code"+this.hospital_code);
        this.conn = conn;
    }

    private String[][] Interface(String mm, String yy, String hospital_code) throws SQLException{
        //String[row][colum]
        String[][] processData = new String[32][4];
        int rowCount;
        int chkDate;
        String dt = yy + "" + mm;

        int chkRowFromMonth = this.dom[Integer.parseInt(mm)] + 1;
        for(int r = 1; r < chkRowFromMonth ; r++){
            processData[r][0] = "" + r + "";
        }
        
        //--- for his bill
        rowCount = 1;
        ResultSet hisBill = this.HisBill(dt);
        while(hisBill.next()){
            chkDate = Integer.parseInt(hisBill.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][1] = hisBill.getString("COUNTROW");
            rowCount++;
        }

        //--- for his hisVerify
        rowCount = 1;
        ResultSet hisVerify = this.HisVerify(dt);
        while(hisVerify.next()){
            chkDate = Integer.parseInt(hisVerify.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][2] = hisVerify.getString("COUNTROW");
            rowCount++;
        }

        //--- for his erpARReceipt
        
        rowCount = 1;
        ResultSet erpARReceipt = this.ErpARReceipt(dt);
        while(erpARReceipt.next()){
            chkDate = Integer.parseInt(erpARReceipt.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][3] = erpARReceipt.getString("COUNTROW");
            rowCount++;
        }
        return processData;
    }

    public String InterfaceHtml(String mm, String yy){
        try{
            String[][] value = this.Interface(mm, yy, hospital_code);
            String interfaceHTML = "";
            interfaceHTML = "<table class=\"data_free_width\" id=\"dataTable\" name=\"dataTable\" width='96%'>\n";

            int chkRowFromMonth = this.dom[Integer.parseInt(mm)] + 1;

            interfaceHTML += "<tr>\n";
            interfaceHTML += " <td class='sub_head' width='10%'>Date</td>\n";
            interfaceHTML += " <td class='sub_head' width='30%'>Transaction(Records)</td>\n";
            interfaceHTML += " <td class='sub_head' width='30%'>Verify Result(Records)</td>\n";
            interfaceHTML += " <td class='sub_head' width='30%'>AR Receipt(Records)</td>\n";
            interfaceHTML += "</tr>\n";
            for(int r = 1; r < chkRowFromMonth ; r++){
                interfaceHTML+="<tr>\n";
                for(int c = 0; c < 4;c++){
                    interfaceHTML+="<td class=\"row0 alignCenter\">" + this.showValueInTable(value[r][c],"../interface/trakcare_import.jsp?ProcessName=Interface") +"</td>\n";
                }
                interfaceHTML+="</tr>\n";
            }
            interfaceHTML+="</table>\n";
            return interfaceHTML;
        }catch(Exception err){
            return err.getMessage();
        }
    }
    private ResultSet HisBill(String dt){
        //SELECT COUNT(*) FROM INT_HIS_BILL WHERE TRANSACTION_DATE ='YYYYMMDD'
        ResultSet rs = null;
        String sql = "SELECT TRANSACTION_DATE, COUNT(TRANSACTION_DATE) as COUNTROW FROM INT_HIS_BILL WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%' group by TRANSACTION_DATE order by TRANSACTION_DATE";
        System.out.println(sql);
        rs = this.conn.executeQuery(sql);
        return rs;
    }
    private ResultSet HisVerify(String dt){
        //SELECT COUNT(*) FROM INT_HIS_VERIFY WHERE TRANSACTION_DATE ='YYYYMMDD'
        ResultSet rs = null;
        String sql = "SELECT TRANSACTION_DATE, COUNT(TRANSACTION_DATE) as COUNTROW FROM INT_HIS_VERIFY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%' group by TRANSACTION_DATE order by TRANSACTION_DATE";
        rs = this.conn.executeQuery(sql);
        return rs;
    }
    private ResultSet ErpARReceipt(String dt){
        //SELECT COUNT(*) FROM INT_ERP_AR_RECEIPT WHERE TRANSACTION_DATE ='YYYYMMDD'
        ResultSet rs = null;
        String sql = "SELECT TRANSACTION_DATE, COUNT(TRANSACTION_DATE) as COUNTROW FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%' group by TRANSACTION_DATE order by TRANSACTION_DATE";
        rs = this.conn.executeQuery(sql);
        return rs;
    }

    //for Inport Bill
    private String[][] ImportBill(String mm,String yy) throws SQLException{
        int chkDate;
        String dt = yy + "" + mm;
        
        String[][] processData =  new String[32][2];
        int chkRowFromMonth = this.dom[Integer.parseInt(mm)] + 1;
        for(int r = 1; r < chkRowFromMonth ; r++){
            processData[r][0] = "" + r + "";
        }

        String sql = "SELECT TRANSACTION_DATE, COUNT(TRANSACTION_DATE) as COUNTROW  FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%'  group by TRANSACTION_DATE order by TRANSACTION_DATE";
        ResultSet rs = this.conn.executeQuery(sql);
        while(rs.next()){
            chkDate = Integer.parseInt(rs.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][1] = rs.getString("COUNTROW");
        }
        
        return processData;
    }

    public String ImportBillHtml(String mm,String yy){
        try{
            String[][] value = this.ImportBill(mm, yy);
            String interfaceHTML = "";
            interfaceHTML = "<table class=\"data_free_width\" id=\"dataTable\" name=\"dataTable\" width='96%'>\n";

            int chkRowFromMonth = this.dom[Integer.parseInt(mm)] + 1;

            interfaceHTML += "<tr>\n";
            interfaceHTML += " <td class='sub_head' width='10%'>Date</td>\n";
            interfaceHTML += " <td class='sub_head' width='30%'>Transaction</td>\n";
            interfaceHTML += "</tr>\n";
            for(int r = 1; r < chkRowFromMonth ; r++){
                interfaceHTML+="<tr>\n";
                for(int c = 0; c < 2;c++){
                    interfaceHTML+="<td class=\"row0 alignCenter\">" + this.showValueInTable(value[r][c],"../process/ProcessImportBill.jsp?ProcessName=ImportBill") +"</td>\n";
                }
                interfaceHTML+="</tr>\n";
            }
            interfaceHTML+="</table>\n";
            return interfaceHTML;
        }catch(Exception err){
            return err.getMessage();
        }
    }
    
    //for compute daily
    private String[][] ComputeDaily(String mm,String yy) throws SQLException{
        int chkDate;
        String dt = yy + "" + mm;

        String[][] processData =  new String[32][3];
        int chkRowFromMonth = this.dom[Integer.parseInt(mm)] + 1;
        for(int r = 1; r < chkRowFromMonth ; r++){
            processData[r][0] = "" + r + "";
        }

        // จำนวน invoice ที่คำนวณแล้ว
        String sql = "SELECT TRANSACTION_DATE, COUNT(DISTINCT INVOICE_NO) as COUNTROW FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%' AND COMPUTE_DAILY_DATE <> '' group by TRANSACTION_DATE order by TRANSACTION_DATE";
        ResultSet rs = this.conn.executeQuery(sql);
        while(rs.next()){
            chkDate = Integer.parseInt(rs.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][1] = rs.getString("COUNTROW");
        }

        // จำนวน invoice ที่ยังไม่ได้คำนวณ
        sql = "SELECT TRANSACTION_DATE, COUNT(DISTINCT INVOICE_NO) as COUNTROW FROM TRN_DAILY " +
        	  "WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%' " +
        	  "AND COMPUTE_DAILY_DATE = '' AND INVOICE_TYPE != 'ORDER' AND DOCTOR_CODE NOT LIKE '99999%' " +
        	  "GROUP BY TRANSACTION_DATE ORDER BY TRANSACTION_DATE";
        rs = this.conn.executeQuery(sql);
        while(rs.next()){
            chkDate = Integer.parseInt(rs.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][2] = rs.getString("COUNTROW");
        }

        return processData;
    }

    public String ComputeDailyHtml(String mm,String yy){
        try{
            String[][] value = this.ComputeDaily(mm, yy);
            String interfaceHTML = "";
            interfaceHTML = "<table class=\"data_free_width\" id=\"dataTable\" name=\"dataTable\" width='96%'>\n";

            int chkRowFromMonth = this.dom[Integer.parseInt(mm)] + 1;

            interfaceHTML += "<tr>\n";
            interfaceHTML += " <td class='sub_head' width='10%' rowspan='2'>Date</td>\n";
            interfaceHTML += " <td class='sub_head' width='30%' colspan='2'>Transaction</td>\n";
            interfaceHTML += "</tr>\n";
            interfaceHTML += "<tr>\n";
            interfaceHTML += " <td class='sub_head' width='40%'>Calculated</td>\n";
            interfaceHTML += " <td class='sub_head' width='40%'>Not Calculate</td>\n";
            interfaceHTML += "</tr>\n";

            for(int r = 1; r < chkRowFromMonth ; r++){
                interfaceHTML+="<tr>\n";
                for(int c = 0; c < 3;c++){
                    interfaceHTML+="<td class=\"row0 alignCenter\">" + this.showValueInTable(value[r][c],"../process/ProcessBasicAllocateTest.jsp") +"</td>\n";
                }
                interfaceHTML+="</tr>\n";
            }
            interfaceHTML+="</table>\n";
            return interfaceHTML;
        }catch(Exception err){
            return err.getMessage();
        }
    }

    //for Receipt
    private String[][] Receipt(String mm,String yy) throws SQLException{
        int chkDate;
        String dt = yy + "" + mm;

        String[][] processData =  new String[32][5];
        int chkRowFromMonth = this.dom[Integer.parseInt(mm)] + 1;
        for(int r = 1; r < chkRowFromMonth ; r++){
            processData[r][0] = "" + r + "";
        }

        String sql = "";
        sql = "SELECT TRANSACTION_DATE, COUNT(DISTINCT INVOICE_NO) as COUNTROW FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%' AND PAY_BY_CASH = 'Y' group by TRANSACTION_DATE order by TRANSACTION_DATE;";
        ResultSet rs = this.conn.executeQuery(sql);
        while(rs.next()){
            chkDate = Integer.parseInt(rs.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][1] = rs.getString("COUNTROW");
        }

        sql = "SELECT RECEIPT_DATE, COUNT(DISTINCT INVOICE_NO) as COUNTROW FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+hospital_code+
        	  "' AND RECEIPT_DATE like '" + dt + "%' AND PAY_BY_AR = 'Y'  " +
        	  "group by RECEIPT_DATE order by RECEIPT_DATE;";
        rs = this.conn.executeQuery(sql);
        while(rs.next()){
            chkDate = Integer.parseInt(rs.getString("RECEIPT_DATE").toString().substring(6, 8));
            processData[chkDate][2] = rs.getString("COUNTROW");
        }

        sql = "SELECT TRANSACTION_DATE, COUNT(DISTINCT INVOICE_NO) as COUNTROW FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%' AND PAY_BY_PAYOR = 'Y' group by TRANSACTION_DATE order by TRANSACTION_DATE;";
        rs = this.conn.executeQuery(sql);
        while(rs.next()){
            chkDate = Integer.parseInt(rs.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][3] = rs.getString("COUNTROW");
        }

        sql = "SELECT TRANSACTION_DATE, COUNT(DISTINCT INVOICE_NO) as COUNTROW FROM TRN_DAILY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND TRANSACTION_DATE like '" + dt + "%' AND PAY_BY_DOCTOR = 'Y' group by TRANSACTION_DATE order by TRANSACTION_DATE;";
        rs = this.conn.executeQuery(sql);
        while(rs.next()){
            chkDate = Integer.parseInt(rs.getString("TRANSACTION_DATE").toString().substring(6, 8));
            processData[chkDate][4] = rs.getString("COUNTROW");
        }

        return processData;
    }

    public String ReceiptHtml(String mm,String yy){
        try{
            String[][] value = this.Receipt(mm, yy);
            String interfaceHTML = "";

            interfaceHTML = "<table class=\"data_free_width\" id=\"dataTable\" name=\"dataTable\" width='96%'>\n";

            int chkRowFromMonth = this.dom[Integer.parseInt(mm)] + 1;
            
            interfaceHTML += "<tr>\n";
            interfaceHTML += " <td class='sub_head' width='10%'> Date </td>\n";
            interfaceHTML += " <td class='sub_head' width='20%'>Cash</td>\n";
            interfaceHTML += " <td class='sub_head' width='20%'>AR</td>\n";
            interfaceHTML += " <td class='sub_head' width='20%'>Payor</td>\n";
            interfaceHTML += " <td class='sub_head' width='20%'>Doctor</td>\n";
            interfaceHTML += "</tr>\n";

            for(int r = 1; r < chkRowFromMonth ; r++){
                interfaceHTML+="<tr>\n";
                for(int c = 0; c < 5;c++){
                    interfaceHTML+="<td class=\"row0 alignCenter\">" + this.showValueInTable(value[r][c],"../process/ProcessReceiptByAR.jsp?ProcessName=Receipt") +"</td>\n";
                }
                interfaceHTML+="</tr>\n";
            }
            interfaceHTML+="</table>\n";
            return interfaceHTML;
        }catch(Exception err){
            return err.getMessage();
        }
    }

    private String showValueInTable(String data,String url){
        String ret = "";
        //ex. url = "../interface/trakcare_import.jsp";
        if(data==null) ret = "<a href='javascript:void(0)' onclick='opener.location.href=\""+ url +"\"; window.close();'><img src='../../images/hide_menu_button.png'></a>";
        else ret = data;
        return ret;
    }

    public String ComputeMonthly(String mm, String yy){
        String strReturn = "";
        //SELECT COUNT(*) FROM SUMMARY_MONTHLY WHERE YYYY = 'YYYY' AND MM = 'MM'
        String sql = "SELECT COUNT(*) FROM SUMMARY_MONTHLY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+ yy +"' AND MM = '"+ mm +"'";
        System.out.println("hospital_code = " + hospital_code);
        strReturn = this.conn.executeQueryString(sql);
        return strReturn;
    }
    public String ComputePaymentMonthlyForDF(String mm, String yy){
        String strReturn = "";
        //SELECT COUNT(*) FROM PAYMENT_MONTHLY WHERE YYYY = 'YYYY' AND MM = 'MM' AND PAYMENT_TYPE = '04'
        String sql = "SELECT COUNT(*) FROM PAYMENT_MONTHLY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+ yy +"' AND MM = '"+ mm +"' AND PAYMENT_TYPE = '04'";
        strReturn = this.conn.executeQueryString(sql);
        return strReturn;
    } 
    public String ExportToBankForDF(String mm, String yy){
        String strReturn = "";
        //SELECT COUNT(*) FROM BANK_TMB_MEDIA_CLEARING WHERE YYYY = 'YYYY' AND MM = 'MM'
        String sql = "SELECT COUNT(*) FROM BANK_TMB_MEDIA_CLEARING WHERE HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+ yy +"' AND MM = '"+ mm +"'";
        strReturn = this.conn.executeQueryString(sql);
        return strReturn;
    }
    public String ComputeCummaryTax(String mm, String yy){
        String strReturn = "";
        //SELECT COUNT(*) FROM SUMMARY_TAX_406 WHERE YYYY = 'YYYY' AND MM = 'MM'
        String sql = "SELECT COUNT(*) FROM SUMMARY_TAX_406 WHERE HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+ yy +"' AND MM = '"+ mm +"'";
        strReturn = this.conn.executeQueryString(sql);
        return strReturn;
    }
    public String ComputePaymentMonthlyForSalary(String mm, String yy){
        String strReturn = "";
        //SELECT COUNT(*) FROM PAYMENT_MONTHLY WHERE YYYY = 'YYYY' AND MM = 'MM' AND PAYMENT_TYPE = '01'
        String sql = "SELECT COUNT(*) FROM PAYMENT_MONTHLY WHERE HOSPITAL_CODE = '"+hospital_code+"' AND YYYY = '"+ yy +"' AND MM = '"+ mm +"' AND PAYMENT_TYPE = '01'";
        strReturn = this.conn.executeQueryString(sql);
        return strReturn;
    }             
}
