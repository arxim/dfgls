import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.process.ProcessAllocateMonthlyExpenseBean;
import df.bean.process.ProcessMaster;


public class ApplicationClient {

	public static void main(String[] args){ 
		
		DBConnection conn = new DBConnection();
		conn.connectToLocal();
		
		String payDate  = new Batch("011", conn).getPaymentDate();

    	ProcessMaster expProcess =  new  ProcessAllocateMonthlyExpenseBean("011", "2014", "01", "2" , payDate);
    	expProcess.doProcess();
    	
	}
}
