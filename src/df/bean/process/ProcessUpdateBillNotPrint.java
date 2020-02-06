package df.bean.process;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import df.bean.db.dao.TrnDailyDAO;
import df.bean.obj.util.JDate;

public class ProcessUpdateBillNotPrint implements ProcessMaster{
	final static Logger logger = Logger.getLogger(ProcessUpdateBillNotPrint.class);

	String hospitalCode = "";
	String startDate = "";
	String endDate = "";
	String lineNo = "";
	String month = "";
	String year = "";
	List<Map<String,Object>> trnDailyListMap = null;
	TrnDailyDAO trnDaily = null;
	
	public boolean doProcess(String hospitalCode, String startDate, String endDate){
		TrnDailyDAO trn = new TrnDailyDAO();
		List<Map<String,Object>> trnDailyList = null;
		trnDailyList = trn.getTrnDailyForUpdateBillNotPrint(hospitalCode, startDate, endDate);
		logger.info("GetBillNotPrintList Complete :"+trnDailyList.size()+" Time : "+JDate.getTime());
		//this.hospitalCode = hospitalCode;
		//this.startDate = startDate;
		//this.endDate = endDate;
		//logger.info(trnDailyList);
		//doProcess();
		trn.prepareUpdateBillNotPrint();
		logger.info("Set Prepare Update Complete "+JDate.getTime());
		for(int i = 0; i<trnDailyList.size(); i++){
			trn.updateBillNotPrint(trnDailyList.get(i));
		}
		logger.info("Update Invoice No Complete "+JDate.getTime());
		if(trnDailyList.size()>0){
			return trn.updateInactiveBillNotPrint(hospitalCode, startDate, endDate);
		}else{
			logger.info("Update Inactive Complete "+JDate.getTime());
			return true;
		}
	}

	@Override
	public boolean doProcess() {
		trnDaily.prepareUpdateBillNotPrint();
		for(int i = 0; i<trnDailyListMap.size(); i++){
			trnDaily.updateBillNotPrint(trnDailyListMap.get(i));
		}
		return true;
	}
	
	@Override
	public boolean doRollback() {
		return false;
	}

	@Override
	public boolean doBatchClose() {
		return false;
	}
}