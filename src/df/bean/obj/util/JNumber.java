package df.bean.obj.util;

import java.text.DecimalFormat;

public class JNumber {
    
    static public final String FORMAT_MONEY = "0.00";
    
    public JNumber() {
    }
    static public double setFormat(double value, String sFormat) {
        return Double.parseDouble((new java.text.DecimalFormat(sFormat)).format(value));
    }
    static public String getShowMoney(double value) {
    	//Result(1223222.5562) = 1,223,222.56
    	String money = "";
    	if(value<1000){
    		money = (new java.text.DecimalFormat("0.00")).format(value);
    	}else{
    		money = (new java.text.DecimalFormat("0,000.00")).format(value);
    	}
        return money;
    }
    static public String getShowMoneyNoComma(double value) {
    	//Result(1223222.5562) = 1223222.56
    	String money = "";
    	if(value<1000){
    		money = (new java.text.DecimalFormat("0.00")).format(value);
    	}else{
    		money = (new java.text.DecimalFormat("0,000.00")).format(value);
    	}
        return money.replaceAll(",", "");
    }
    static public String getSaveMoney(double value) {
    	//Result(1223222.5562) = 1223222.56
    	return (new java.text.DecimalFormat("0.00")).format(value);
    }
    static public String getSaveMoney(String value) {
    	String t = value.replaceAll("E-","");
    	//Result(1223222.5562) = 1223222.56
        return t.replaceAll(",", "");
    }
    static public String getMoneyFormat(String m){
    	String mn = "";
    	int baht = 0;
    	String satang = "";
    	baht = Integer.parseInt(m.substring(0, m.length()-2));
    	satang = m.substring(m.length()-2, m.length());
    	mn = ""+baht+"."+satang;
    	return mn;
    }
	public static String showDouble(double d, int precision){
		String myformat = "###,###,###,###,##0";
		String result = "";
		if (precision == 0){
			DecimalFormat df = new DecimalFormat(myformat);
			return df.format(d).replaceAll("[^.0-9\\.]+", "");
		}
		myformat = "###############.";

		for(int x= 0; x < precision; x++)
		myformat = myformat + "0";

		DecimalFormat df = new DecimalFormat(myformat);
		result = df.format(d);
		result = result.replace(",", "");
		return result;
	}

}
