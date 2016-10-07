package df.bean.obj.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Scanner;

import df.bean.obj.util.JFile;

public class FileConn {

    private JFile jf = new JFile();
    //private Scanner inputStream;

    public ArrayList<HashMap<String, Object>> getDataFromFile(String sourceFile, String user, String password) {
    	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        jf.readFile(sourceFile, user, password);
        return data;
    }
    public ArrayList<HashMap<String, String>> getDataFromFile(String sourceFile) {
    	ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    	String arrayData[] = null;
    	String arrayColumn[] = null;
        int line_amt = 0;
        String data = "";
        try{
        	
        	BufferedReader in = new BufferedReader(new InputStreamReader( new FileInputStream(sourceFile), "TIS-620"));		      
    		while ((data = in.readLine()) != null) {
    			if(line_amt == 0){
	                arrayColumn = data.split("[|]");
	            }else{
					HashMap<String, String> map = new HashMap<String, String>();
	            	arrayData = data.split("[|]");
					for (int i = 0; i < arrayData.length; i++) {
						map.put(arrayColumn[i].toString(), arrayData[i].toString());
					}
					dataList.add(map);
	            }
	            line_amt++;
    		}
    		in.close();
    		/*
	        inputStream = new Scanner(new FileInputStream(sourceFile));
	        while (inputStream.hasNextLine()) {
	            data = inputStream.nextLine();
	            if(line_amt == 0){
	                arrayColumn = data.split("[|]");
	                //System.out.println("Number Of Column : "+arrayColumn.length);
	            }else{
					HashMap<String, String> map = new HashMap<String, String>();
	            	arrayData = data.split("[|]");
					for (int i = 0; i < arrayData.length; i++) {
						map.put(arrayColumn[i].toString(), arrayData[i].toString());
					}
					dataList.add(map);
					System.out.println(map);
	            }
	            line_amt++;
	        }
	        System.out.println("Number Of Line : "+line_amt);
	        inputStream.close();
	        */
        }catch (Exception e){
        	System.out.println("Error Get Data from InputStream : "+e);
        }
        return dataList;
    }
}