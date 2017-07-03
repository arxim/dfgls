package df.bean.process;

import df.bean.process.ProcessDischargeSummary;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.BankTMBMediaClearing;
import df.bean.db.table.Doctor;
import df.bean.db.table.IntErpArReceipt;
import df.bean.db.table.IntHisBill;
import df.bean.db.table.IntHisVerify;
import df.bean.db.table.PayorOffice;
import df.bean.db.table.SummaryTax406;
import df.bean.db.table.TrnDaily;
import df.bean.obj.receipt.Receipt;
import df.bean.obj.util.DialogBox;

public class ProcessRollBack {
    private DBConnection conn = null;
    
    
    public ProcessRollBack(DBConnection conn) {
        this.conn = conn;
    }
    // TrnDaily
    public boolean rollBackTrnDaily(String hospitalCode, String startDate, String endDate) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        TrnDaily td = new TrnDaily(this.conn);

        try {
            conn.beginTrans();
            if (result) { result = td.rollBackUpdate(hospitalCode, startDate, endDate);  }
        } catch (Exception ex) {
        	System.out.println("Daily : "+ex);
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            td = null;
        }      
        return result;    
    }
    
    // Summary_Monthly
    public boolean rollBackSummaryMonthly(String hospitalCode, String YYYY, String MM) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        return result;
    }
    
    // Media clearing
    public boolean rollBackMediaClearing(String hospitalCode, String YYYY, String MM, String serviceType) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        BankTMBMediaClearing tmb = new BankTMBMediaClearing(conn);

        try {
            conn.beginTrans();
            if (result) { result = tmb.rollBackDelete(hospitalCode,YYYY, MM, serviceType);  }          
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            tmb = null;
        }
        
        return result;    
    }
    // Summary Tax 406
    public boolean rollBackSummaryTax406(String hospitalCode, String YYYY, String MM) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        SummaryTax406 st406 = new SummaryTax406(conn);

        try {
            conn.beginTrans();
            if (result) { result = st406.rollBackDelete(hospitalCode, YYYY, MM);  }          
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            st406 = null;
        }
        
        return result;    
    }
    //////////////////////////////////////////////////////////////
    // roll back receipt
    public boolean rollBackReceipt(String hospitalCode, String YYYY, String MM) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        Receipt rc = new Receipt(conn);
        PayorOffice st = new PayorOffice(conn);
        IntErpArReceipt er = new IntErpArReceipt(conn);
        Doctor dt = new Doctor(conn);
        
 
        try {
            conn.beginTrans();
            if (result) { result = rc.rollBackUpdate(hospitalCode,YYYY, MM, "TRN_DAILY");  }
            if (result) { result = st.rollBackUpdate(hospitalCode,YYYY, MM, "TRN_DAILY");  }
            if (result) { result = er.rollBackUpdate(hospitalCode,YYYY, MM, "TRN_DAILY");  }
            if (result) { result = dt.rollBackUpdate(hospitalCode,YYYY, MM, "TRN_DAILY");  }
            
//            if (result) { result = rc.rollBackUpdate(hospitalCode,YYYY, MM, "SUMMARY_DAILY");  }
//            if (result) { result = st.rollBackUpdate(hospitalCode,YYYY, MM, "SUMMARY_DAILY");  }
//            if (result) { result = er.rollBackUpdate(hospitalCode,YYYY, MM, "SUMMARY_DAILY");  }
//            if (result) { result = dt.rollBackUpdate(hospitalCode,YYYY, MM, "SUMMARY_DAILY");  }
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            rc = null;
            st = null;
            er = null;
            dt = null;
        }
        
        return result;    
    }
        
    // roll back receipt by AR
    public boolean rollBackReceiptByAROld(String hospitalCode, String YYYY, String MM) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        ProcessPartialPayment pt = new ProcessPartialPayment();
        IntErpArReceipt er = new IntErpArReceipt(conn);
        
        try {
            conn.beginTrans();
            result = pt.rollBack(YYYY, MM, hospitalCode);
            System.out.println("Rollback Receipt : "+result);
            if (result){ 
            	result = er.rollBackUpdate(hospitalCode,YYYY, MM, "TRN_DAILY");
            }else{
            	result = false;
            }            
            System.out.println("Rollback Receipt Finished");
        } catch (Exception ex) {
            System.out.println("Rollback Receipt By AR Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            er = null;
        }
        
        return result;    
    }
    
    public boolean rollBackReceiptByAR(String hospitalCode, String startDate, String endDate) { 
    	boolean result = true; 
    	this.conn = new DBConnection(); 
    	this.conn.connectToLocal(); 
    	ProcessPartialPayment pt = new ProcessPartialPayment(); 
    	IntErpArReceipt er = new IntErpArReceipt(conn); 

    	try { 
	    	conn.beginTrans(); 
	    	result = pt.rollBack(startDate, endDate, hospitalCode); 
	    	System.out.println("Rollback Receipt : "+result); 
	    	if (result){ 
		    	result = er.rollBackUpdate(hospitalCode,startDate, endDate, "TRN_DAILY"); 
	    	}else{ 
		    	result = false; 
	    	} 
	    	System.out.println("Rollback Receipt Finished"); 
    	} catch (Exception ex) { 
	    	System.out.println("Rollback Receipt By AR Error : " + ex.getMessage()); 
    	} finally { 
	    	if (result) { conn.commitTrans(); } 
	    	if (!result) { conn.rollBackTrans(); } 
	    	this.conn.Close(); 
	    	er = null; 
    	} 
    	return result; 
    }
        
    // roll back receipt by Payor
    public boolean rollBackReceiptByPayor(String hospitalCode, String YYYY, String MM) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        PayorOffice st = new PayorOffice(conn);
 
        try {
            conn.beginTrans();
            if (result) { result = st.rollBackUpdate(YYYY, MM,hospitalCode,"TRN_DAILY");  }
            
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            st = null;
        }
        
        return result;    
    }
    
    // roll back receipt by Doctor
    public boolean rollBackReceiptByDoctor(String hospitalCode, String YYYY, String MM) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        Doctor dt = new Doctor(conn);
        
        try {
            conn.beginTrans();
            if (result) { result = dt.rollBackUpdate(YYYY, MM, hospitalCode, "TRN_DAILY");  }
            
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            dt = null;
        }
        
        return result;    
    }
    
    /////////////////////////////////////////////////////////
    
    // roll back import bill
    public boolean rollBackImportBill(String hospitalCode, String startDate, String endDate) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        TrnDaily td = new TrnDaily(conn);

        try {
            conn.beginTrans();
            if (result) { 
            	result = td.rollBackImportVerifyDelete(hospitalCode,startDate, endDate);
            	result = td.rollBackImportBillDelete(hospitalCode,startDate, endDate);
            	result = td.rollBackImportBillUpdate(hospitalCode, startDate, endDate);
            }
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            td = null;
        }
        
        return result;    
    }
    // roll back import bill by verify
    public boolean rollBackImportVerify(String hospitalCode, String startDate, String endDate) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        TrnDaily td = new TrnDaily(conn);

        try {
            conn.beginTrans();
            if (result) { result = td.rollBackImportVerifyDelete(hospitalCode,startDate, endDate);  }
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            td = null;
        }
        
        return result;    
    }
    
    // roll back interface INT_HIS_BILL
    public boolean rollBackInterfaceHisBill(String hospitalCode, String startDate, String endDate) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        IntHisBill ihb = new IntHisBill(conn);

        try {
            conn.beginTrans();
            if (result) { result = ihb.rollBackDelete(hospitalCode,startDate, endDate);  }
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            ihb = null;
        }
        
        return result;    
    }
    // roll back interface INT_HIS_VERIFY
    public boolean rollBackInterfaceHisVerify(String hospitalCode, String startDate, String endDate) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        IntHisVerify ihv = new IntHisVerify(conn);

        try {
            conn.beginTrans();
            if (result) { result = ihv.rollBackDelete(hospitalCode,startDate, endDate);  }
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            ihv = null;
        }
        
        return result;    
    }    
    // roll back interface INT_ERP_AR_RECEIPT
    public boolean rollBackInterfaceErpArReceipt(String hospitalCode, String startDate, String endDate) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        IntErpArReceipt ar = new IntErpArReceipt(conn);

        try {
            conn.beginTrans();
            if (result) { result = ar.rollBackDelete(hospitalCode,startDate, endDate);  }
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            ar = null;
        }
        
        return result;    
    }
    // roll back interface Write off
    public boolean rollBackWriteOff(String hospitalCode, String startDate, String endDate) {
        boolean result = true;
        this.conn = new DBConnection();
        this.conn.connectToLocal();
        IntErpArReceipt ar = new IntErpArReceipt(conn);

        try {
            conn.beginTrans();
            if (result) { result = ar.rollBackWriteOffDelete(hospitalCode, startDate, endDate);  }
        } catch (Exception ex) {
            DialogBox.ShowError("Error : " + ex.getMessage());
        } finally {
            if (result) { conn.commitTrans(); }
            if (!result) { conn.rollBackTrans(); }
            this.conn.Close();
            ar = null;
        }
        
        return result;    
    }   
    
    /**
     * @author Mr.sarunyoo Keawsopa
     * @param hospitalCode
     * @param mm
     * @param yyyy
     * @return true
     */
    public boolean rollBackDischargeSummary(String  hospitalCode , String mm ,  String yyyy) { 
    	 boolean result = false;
         this.conn = new DBConnection();
         this.conn.connectToLocal();
        
         ProcessDischargeSummary obDischarge = new ProcessDischargeSummary();

         try {
            conn.beginTrans();
            if(obDischarge.rollBackDischarge(hospitalCode, mm, yyyy) > 0){ 
            	result = true;
            } 
         } catch (Exception ex) {
             DialogBox.ShowError("Error : " + ex.getMessage());
         } finally {
             if (result) { conn.commitTrans(); }
             if (!result) { conn.rollBackTrans(); }
             this.conn.Close();
             obDischarge = null;
         }
         return result;    
    }
}