package df.bean.obj.util; 

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

public class ReadProperties {
	
	public static void main(String[] args){
		ReadProperties rp = new ReadProperties();
		String propertiesName = "config.properties";
		//ArrayList<String> al = new ArrayList<String>();
		//al.add("driverClassName");
		//al.add("url");
		//al.add("user");
		//System.out.println(al); log4j.appender.FILE
		System.out.println(rp.getPropertiesData("config.properties", "interface.","ip"));
	}
	
    public HashMap<String,String> getPropertiesData(String propFile, String prefix, ArrayList<String> keyData) {
    	HashMap<String,String>  propData = new HashMap<String,String>();
        InputStream is = getClass().getClassLoader().getResourceAsStream(propFile);
        Properties dbProps = new Properties();
        try {
            dbProps.load(is);
            for(int i = 0; i<keyData.size(); i++){
            	propData.put(keyData.get(i), dbProps.getProperty(prefix+keyData.get(i)));            	
            }
            System.out.println("Props data : "+propData);
        } catch (Exception e) { System.out.println(e); }
        return propData;
    }
    
    public HashMap<String,String> getPropertiesData(String propFile, String prefix, String keyData) {
		ArrayList<String> al = new ArrayList<String>();
		al.add(keyData);
		return this.getPropertiesData(propFile, prefix, al);
    }
	
    public TreeMap<String,String> getPropertiesData(String propFile, String prefix) {
    	TreeMap<String, String> propData = new TreeMap<String, String>();
        InputStream is = getClass().getClassLoader().getResourceAsStream(propFile);
        Properties dbProps = new Properties();
        try {
            dbProps.load(is);
            Enumeration enuKeys = dbProps.keys();
            while (enuKeys.hasMoreElements()) {
                    String key = (String) enuKeys.nextElement();
                    String value = dbProps.getProperty(key);
                    if (key.startsWith(prefix)) {
                        //System.out.println(key + "= " + value);
                        propData.put(key,value );  
                    }
                }
                
           // System.out.println(propData);
        } catch (Exception e) { System.out.println(e); }
        return propData;
    }
    
    
	public Map<String, String>  getDataReadPropertiesFile(String propertyFileName) {
		Properties prop = new Properties();
		InputStream input = null; 
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String filename = propertyFileName;
			input = getClass().getClassLoader().getResourceAsStream(filename);
			// not found file
			if (input == null) {
				return map;
			}

			// found file
			prop.load(input);
			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				//map = new HashMap<String, String>();
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
			    map.put(key,value); 
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return map;

	}
	public  ArrayList<HashMap<String,String>> getDataObjReadPropertiesFile(String propertyFileName) {
		Map<String, String>  propData =  getDataReadPropertiesFile(propertyFileName); 
		ArrayList<HashMap<String,String>> dataList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
        Set<String> keys = propData.keySet();
        for(String key: keys){
        	map = new HashMap<String, String>(); 
        	String value = propData.get(key);
        	 
        	String  vaKey = value.split(":")[0];
			String  vaValue = value.split(":")[1];
			map.put("CODE", vaKey);
        	map.put("NAME", vaValue);
        	dataList.add(map);
            //System.out.println("CODE   "+vaKey+" NAME "+ vaValue);
        } 
       return dataList;
		
	}

}
