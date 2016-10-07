package df.bean.interfacefile;

import java.util.ArrayList;
import java.util.HashMap;

public interface InterfaceFile {
	public boolean doProcess(ArrayList<HashMap<String,String>> dt);
	public boolean doRollback();
	public boolean doBatchClose();
}

