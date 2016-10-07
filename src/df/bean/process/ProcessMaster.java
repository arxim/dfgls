package df.bean.process;

public interface ProcessMaster {
	
	
	public boolean doProcess();
	public boolean doRollback();
	public boolean doBatchClose();
	
}
