/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;

import df.bean.obj.util.JDate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 *
 * @author nopphadon
 */
public class WriteFile {
    PrintWriter outputStream;
    String filename = ""; //get path+filename and file extension (.xxx)
    
    public String writeFile(String[] data, String filename){
         
        try{
            outputStream = new PrintWriter(new FileOutputStream("c://"+filename+JDate.getDate()+".txt"));
        }
        catch (FileNotFoundException ex){
            System.out.println("Problem opening files.");
        }
        
        for ( int i = 0; i<data.length; i++ ){
            outputStream.println(""+data[i]);
        }
        outputStream.close(); 
        return "แปลงไฟล์ \""+filename+"\" เสร็จสมบูรณ์แล้ว";
    }
    
    public String writeFile(String[] data, String path, String filename){
        String status = "";
        String dt = "";
        try{
            FileWriter f = new FileWriter(path);
            f.write(dt);
            f.close();
        }catch(Exception err){
            status = ""+err;
        }
        return status;
    }
    
    public void setFileName(String fn){
        this.filename = fn;
    }
    
    protected boolean writeFileNew(String[] data){
        boolean status = true;
        PrintWriter outputStream = null;
        
        try{
            File f = new File(this.filename);
            if(f.exists()){
                f.delete();
            }
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fOut, "Cp874");
            outputStream = new PrintWriter(osw);
            
            for ( int i = 0; i<data.length; i++ ){
                try {
                    //out.write(data[i].trim()+"\n");
                    outputStream.println("" + data[i]);
                } catch (Exception ex) {
                }
            }
            
            outputStream.close();
        } catch (Exception ex){
            System.out.println("Problem opening files.");
            status = false;
        }
        return status;
    }
}
