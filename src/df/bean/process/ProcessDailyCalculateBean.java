package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.TrnDaily;
import df.bean.obj.Item.DrMethodAllocation;
import df.bean.obj.doctor.CareProvider;
import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.JDate;

public class ProcessDailyCalculateBean {
	String HOSPITAL_CODE;
	String START_DATE;
	String END_DATE;
	String INVOICE_NO;
	String TRANSACTION_DATE;
	String mode;
	String count="0";
	String maxsize="0";
	String state="0";
    private DoctorList drList = null;
	private DBConnection conn = null;
    private DrMethodAllocation drMethodAllocation =null;
	private TrnDaily tDaily = null;
    private int countNum = 0;		 		

    public void EndProcess(){
		this.countNum = 0;
        tDaily.setStatement(null);
        tDaily.setResultSet(null);
        tDaily = null;
        this.conn.Close();
    }
	    
	public String processRequest(String MAX_ROW, String curRow, String HOSPITAL_CODE, String START_DATE, String END_DATE, String INVOICE_NO, String LINE_NO, String TRANSACTION_DATE, String USER) {
		this.maxsize=MAX_ROW;
		this.HOSPITAL_CODE=HOSPITAL_CODE;
		this.START_DATE=START_DATE;
		this.END_DATE=END_DATE;
		this.INVOICE_NO=INVOICE_NO;
		this.TRANSACTION_DATE=TRANSACTION_DATE;
		String data="";
    	String hospitalCode = this.HOSPITAL_CODE;
		String replaceStartDeta = JDate.saveDate(this.START_DATE);
		String replaceEndDate = JDate.saveDate(this.END_DATE);
    	String invoiceNo = this.INVOICE_NO;
		String bothJson= "";
		//System.out.println(curRow);
    	if(curRow.equals("0")){
    		System.out.println("Max Row = "+this.maxsize+" and Row = 2 is Initial Process Daily Calculate");
        	this.conn = new DBConnection();
    		this.conn.connectToLocal(); //add 20120902   	
            this.drList = new DoctorList(hospitalCode, conn); //add 20120902
            this.drList.newAllDoctor(hospitalCode);
            this.drMethodAllocation = new DrMethodAllocation(conn, hospitalCode);		               
        	this.tDaily = new TrnDaily(conn);
    	}
    	if(!processCalculate(replaceStartDeta, replaceEndDate, hospitalCode, invoiceNo, LINE_NO, TRANSACTION_DATE, USER)){
    		data="0";
    	}else{
    		data="1";
    	}
		this.countNum++;
        bothJson = "{\"count\":\""+this.countNum+"\",\"lastData\":\"N\"}";
        if(this.countNum == Integer.parseInt(this.maxsize)){
        	this.EndProcess();
        }
		return data;
	}	
	public boolean processCalculate(String startDate, String endDate, String hospitalCode, String invoiceNo, String lineNo, String transactionDate, String user) {
        boolean ret = false;
        String doctorCode = "";
        CareProvider careProvider = null;
            try {
                ret = false;
                String sql = TrnDaily.getSQL_TRN_DAILY(startDate, endDate, hospitalCode, invoiceNo, lineNo);
                tDaily.OpenResultSet(sql);            	
                while (tDaily.MoveNext()) {
                    doctorCode = tDaily.getDoctorCode();
                    careProvider = this.drList.getDoctor(doctorCode);
                    if (careProvider != null) {
                        careProvider.setTrnDaily(tDaily);
                        careProvider.setDrMethodAllocation(this.drMethodAllocation);
                        careProvider.setUser(user);
                        if (careProvider.computeTransDaily()) { 
                            ret = true;
                        } else {  
                            System.out.println("Daily Cal False : "+sql);
                            ret = false;
                        }
                    } else {
                    	//System.out.println("Careprovier is null");
                        System.out.println("Daily Cal False : "+sql);
                        ret = false;
                    }
                }
            } catch (Exception e) {
            	System.out.println("Daily Calculate Exception == > Line no : "+lineNo+"<>"+e.getMessage());
                ret=false;
            }
        return ret;
    }
}