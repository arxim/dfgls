package df.bean.process.summary;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Summary {

	protected String hospitalCode = null;

	protected String mm = null;

	protected String yyyy = null;

	protected String term = null;

	protected String payDate = null;
	
	protected String revenueType  = null;
	
	public ArrayList<HashMap<String, String>> getData() {
		return null;
	}

	public String[][] getDataStr() {
		return null;
	}

	public void setRevenueType(String revenueType) {
		this.revenueType = revenueType;
	};

	
}
