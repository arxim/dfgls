package df.bean.obj.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class JFile {
	
	public void writeFile(String sourceFile, String user, String password) {
		SmbFileOutputStream smbfos = null;
		user = "SCAP-SARUNYOO";
	    password = "sarunyoo";
	    sourceFile = "smb://192.168.2.145/ShareFreeALife/testx.txt";
	    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, password);
	    try {
		    SmbFile smbFile = new SmbFile(sourceFile,auth);
			smbfos = new SmbFileOutputStream(smbFile);
			smbfos.write("testing....and writing to a file\n".getBytes());
			smbfos.write("testing....and writing to a file\n".getBytes());
			smbfos.write("testing....and writing to a file\n".getBytes());
			System.out.println("Write");
		} catch (Exception e) {
			System.out.println(e);
		}
	    System.out.println("Write File finished...");
	}
	
	public void readFile(String sourceFile, String user, String password) {
	    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, password);
	    try {
		    SmbFile smbFile = new SmbFile(sourceFile,auth);
			BufferedReader in = new BufferedReader(new InputStreamReader(new SmbFileInputStream(smbFile)));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null)
		        System.out.println(inputLine);
		    in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	    System.out.println("Read File finished...");
	}	

}
