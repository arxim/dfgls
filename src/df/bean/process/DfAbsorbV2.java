package df.bean.process;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;

public class DfAbsorbV2 {
	private DBConn conn;
	private String mm;
	private PrintWriter outputStream;

	private DBConn getDBConn(){
		try{
		if(this.conn == null){
			this.conn = new DBConn();
			this.conn.setStatement();
		}
		}catch(Exception err){
			System.out.println(err.getMessage());
		}
		return this.conn;
	}
	
	private void ReadData(){
	   try{
		    FileInputStream fstream = new FileInputStream("C:\\PayHistory.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
	        outputStream = new PrintWriter(new FileOutputStream("c://q.txt"));
		    while ((strLine = br.readLine()) != null)
		    {
		    	this.ShowFile(strLine);
		    }
		    System.out.println("Finish");
		    outputStream.close();
		    in.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}		
	}
	private void ShowFile(String str){
		String[] aStr = str.split("\\|");
		String id = "1|2|7|4";
		String[] aFile = id.split("\\|");
		if("M".equalsIgnoreCase(aStr[4]) && Double.parseDouble(aStr[7]) > 0){
			this.setDfVerify(aStr[2]);
			this.setDfAbsorb(aStr[7]);
			if(this.GetDrCode(aStr[1])){
				for(int i=0; i < aFile.length; i++){
					//System.out.print(aStr[Integer.parseInt(aFile[i])]);
					if( (i+1) < aFile.length){
						//System.out.print(" = ");
					}
				}
				//System.out.println("");		
			}
		}
		//return null;
	}

	private String dfVerify;
	private String dfAbsorb;
	
	private String getDfAbsorb() {
		return dfAbsorb;
	}

	private void setDfAbsorb(String dfAbsorb) {
		this.dfAbsorb = dfAbsorb;
	}

	private String getDfVerify() {
		return dfVerify;
	}

	private void setDfVerify(String dfVerify) {
		this.dfVerify = dfVerify;
	}

	private Boolean GetDrCode(String dfCode){
		Boolean status = false;
		String str = "SELECT CODE FROM DOCTOR WHERE GUARANTEE_DR_CODE='"+ dfCode +"'";
		String[][] dr = this.getDBConn().query(str);
		for(int i =0; i < dr.length ; i++){
			//System.out.println("   :" + dr[i][0] + " : " + this.getDfVerify());
			this.GetTrnDaily(dr[i][0], this.getDfVerify());
			status = true;
/*			if(this.GetTrnDaily(dr[i][0], this.getDfVerify())){
				System.out.println("   :" + dr[i][0]);
				status = true;
			}*/
		}
		return status;
	}
	
	private Boolean GetTrnDaily(String DrCode, String dtVerify){
		String sq = "";
		Boolean status = false;
		String str = "SELECT DR_AMT, DR_TAX_406,LINE_NO,GUARANTEE_NOTE,INVOICE_NO FROM TRN_DAILY WHERE INVOICE_DATE < '20090300' " +
		"AND MM <> '03' AND DOCTOR_CODE='"+ DrCode +"' AND VERIFY_DATE like '"+dtVerify+"%'";
		String[][] dr = this.getDBConn().query(str);
		for(int i =0; i < dr.length ; i++){
			//System.out.println("DF "+ dr[i][2] +" : " + dr[i][0] + " : TAX 406 =" + dr[i][1]+" Amount of Absorb ="+this.UpdateAbsorb(dr[i][0]));
			//System.out.println("      UPDATE TRN "+ dr[i][2] +" : " + this.UpdateAbsorb(dr[i][0]) );
			//System.out.println("      UPDATE TRN_DALIY SET GUARANTEE_NOTE='ABC', DR_AMT='"+this.UpdateAbsorb(dr[i][0])+"' WHERE LINE_NO='"+ dr[i][2] +"' AND INVOICE_NO='"+ dr[i][4] +"' AND DOCTOR_CODE='"+ DrCode +"'; ");
			if(Double.parseDouble(this.getDfAbsorb()) <= 0){
				return status;
			}else if(Double.parseDouble(this.getDfAbsorb())>= Double.parseDouble(dr[i][0])){				
				sq = "UPDATE TRN_DALIY SET GUARANTEE_NOTE='ABSORB TRANSFER', DR_AMT='0', "+
				"AMOUNT_AFT_DISCOUNT = '0', DR_TAX_406 = '0', HP_AMT = '0' "+
				"WHERE LINE_NO='"+ dr[i][2] +"' AND INVOICE_NO='"+ dr[i][4] +"' " +
				"AND DOCTOR_CODE='"+ DrCode +"'; ";
				outputStream.println(sq);
				status = true;
			}
		}
		return status;
	}
	private String UpdateAbsorb(String dr_amt){
		Double absorb = Double.parseDouble(this.getDfAbsorb());
		Double amt = Double.parseDouble(dr_amt);
		Double m = 0.0;
		if(absorb > amt){
			m = absorb -amt;			
		}else{
			m = amt - absorb;
		}
		String ret = String.valueOf(m);
		this.setDfAbsorb(ret);
		return ret;
	}
	
	public static void main(String[] arg){
		DfAbsorbV2 ab = new DfAbsorbV2();
		ab.ReadData();
	}
}
