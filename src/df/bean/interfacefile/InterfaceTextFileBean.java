package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.Variables;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author arxim
 */
public abstract class InterfaceTextFileBean {

    private String fileName;
    private Scanner inputStream;
    private int line_amount;
    private int column_amount;
    private ArrayList data_list;
    private String message;
    private DBConnection dbconn;
    private String bill_date;
    private String info;
    private String path_file;
    private String owner_bank;
    private String user_name;

    public abstract boolean insertData(String fn, DBConnection d);
    public abstract boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path);
    public abstract boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path, String filing_type);

    protected void setConn(DBConnection db){
        this.dbconn = db;
        /*
        //if(this.dbconn==null){
            this.dbconn = db;
            this.dbconn.setUserName(DBConnPool.CONN_USER);
            this.dbconn.setPassword(DBConnPool.CONN_PASS);
            this.dbconn.Connect();
        //}
        */
    }

    public void setUserName(String s){
    	this.user_name = s;
    }
    public void setPath(String p){
        this.path_file = p;
    }
    public void setOwnerBank(String b){
    	this.owner_bank = b;
    }
            
    protected DBConnection getConn(){
        return this.dbconn;
    }

    protected void setFileName(String fn) {
        fileName = fn;
    }
    protected void setInfo(String s){
        this.info = s;
    }
    protected void setMessage(String s) {
        this.message = s;
    }
    protected void setBillDate(String s){
        this.bill_date = s;
    }
    
    public String getUserName(){
    	return this.user_name;
    }
    public String getOwnerBank(){
    	return this.owner_bank;
    }
    public String getBillDate(){
        return this.bill_date;
    }
    public String getMessage() {
        return this.message;
    }
    public String getInfo(){
        return this.info;
    }
    public int getLine() {
        return this.line_amount;
    }
    public int getColumn() {
        return this.column_amount;
    }
    public ArrayList getData() {
        return this.data_list;
    }

    protected void copyDataFile() throws Exception {
        int line_amt = 0;
        int col_amt = 0;
        String line_data = "";
        inputStream = new Scanner(new FileInputStream(fileName));
        data_list = new ArrayList();
        while (inputStream.hasNextLine()) {
            line_data = inputStream.nextLine();
            data_list.add(line_data);
            //if(line_amt==0){ System.out.println("First line \n"+line_data); }
            if (line_data.split("[|]").length > col_amt) {
                col_amt = line_data.split("[|]").length;
            }
            line_amt++;
        }

        //System.out.println("Last line \n"+line_data);
        this.column_amount = col_amt;
        this.line_amount = line_amt;
        inputStream.close();
        //inputStream = null;
    }
    
    protected String checkString(String text, int textLength, String textAdd, String position ){
    //parameter text        = Data
    //          textLenght  = Fix Lenght of Count
    //          textAdd     = Add Text? etc. 0 or space
    //          position    = First position of text 'f' or Last position of text 'l'
        if(text.equals(null)||text.equals("null")){
            text = " ";
        }
        String textTemp = "";
        if(text.length() > textLength){
            textTemp = text.substring(0,textLength-1);
            return textTemp;
        }else{
            if(position.equals("f")){
                textTemp = addText(textAdd,(textLength-text.length()))+text;
            }else{
                textTemp = text+addText(textAdd,(textLength-text.length()));
            }
            return textTemp;
        }
    }
    
    protected String addText(String text, int count){
        String temp = "";
        for(int i = 0; i<count; i++){
            temp = temp+text;
        }
        return temp;
    }
    /*
    protected boolean writeFile(String[] data){
        boolean status = true;
        PrintWriter outputStream = null;
        String dt = "";
        dt = data[1];
        
        File f = new File(Variables.upload_file+this.fileName+".txt");
        if (f.exists()){
            f.delete();
        }
        try{
            outputStream = new PrintWriter(new FileOutputStream(this.path_file+this.fileName+".txt"));
            for ( int i = 0; i<data.length; i++ ){
                try {
                    outputStream.println("" + new String(data[i].trim().getBytes(),"TIS-620"));
                } catch (UnsupportedEncodingException ex) {}
            }
            outputStream.close();
        } catch (FileNotFoundException ex){
            System.out.println("Problem opening files.");
            status = false;
        }
        return status;
    }
    */
    protected boolean writeFileNew(String[] data){
        boolean status = true;
        PrintWriter outputStream = null;
        try{
        	System.out.println(this.fileName);
            File f = new File(this.fileName);
            if(f.exists()){
                f.delete();
            }
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fOut, "Cp874");
            outputStream = new PrintWriter(osw);
            System.out.println("Size of data : "+data.length);
            for ( int i = 0; i<data.length; i++ ){
                try {
                    //out.write(data[i].trim()+"\n");
                    outputStream.println("" + data[i]);
                } catch (Exception ex) {
                    System.out.println("Output file "+ex);
                }
            }
            
            outputStream.close();
        } catch (Exception ex){
            System.out.println("Problem opening files.");
            status = false;
        }
        
        return status;
    }
    
    protected boolean writeFileNewACGL(String[] data, boolean newFile){
        boolean status = true;
        PrintWriter outputStream = null;
		File file = new File(this.fileName);
		FileOutputStream fOut;
		OutputStreamWriter osw;
		try {
			if(file.exists() && newFile) {
				file.delete();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			fOut = new FileOutputStream(file, true);
	        osw = new OutputStreamWriter(fOut, "Cp874");
            outputStream = new PrintWriter(osw, true);
            System.out.println("Size of data : "+data.length);
			for ( int i = 0; i<data.length; i++ ){
                try {
                    //out.write(data[i].trim()+"\n");
                    //outputStream.println("" + data[i]);
                	if(data[i] != null) {
                		outputStream.println("" + data[i]);
                	}
                } catch (Exception ex) {
                    System.out.println("Output file "+ex);
                }
            }
			System.out.println("Done");
		} catch (IOException e) {
			System.out.println("Problem opening files.");
			status = false;
		} finally {
			if (outputStream != null)
				outputStream.close();
		}
		
        return status;
    }
    
    protected String deleteFile(String fname){
        File f = new File(fname);
        if (f.exists()){
            f.delete();
        }
        return "";
    }
}
