import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class TestUploadFile {

	public static void main(String[] args) {
		String user = "dfuser";
	    String pass ="welcome";

	    String sharedFolder="InterfaceFile";
	    String path="smb://192.168.10.65/"+sharedFolder+"/New.txt";
	    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, pass);
	    SmbFile smbFile = null;
	    try {
		    smbFile = new SmbFile(path, auth);
	    	
	    	//copy file authen 
		    NtlmPasswordAuthentication authDest = new NtlmPasswordAuthentication("","adm", "welcome");
		    SmbFile smbFileDest = new SmbFile("smb://192.168.10.67/InterfaceFile/New.txt",authDest);
		    smbFile.copyTo(smbFileDest);
		    
	    	
		    /*
			//write file
		    smbFile = new SmbFile(path,auth);
		    SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile);
			smbfos.write("testing....and writing to a file\n".getBytes());
			smbfos.write("testing....and writing to a file\n".getBytes());
			smbfos.write("testing....and writing to a file\n".getBytes());
			System.out.println("Write");

			//read file
			BufferedReader in = new BufferedReader(new InputStreamReader(new SmbFileInputStream(smbFile)));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null)
		        System.out.println(inputLine);
		    in.close();
		    */
		} catch (Exception e) {
			System.out.println(e);
		}
	    System.out.println("read/write completed...");
	}	
}