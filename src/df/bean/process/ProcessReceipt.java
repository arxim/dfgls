package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Doctor;
import df.bean.db.table.IntErpArReceipt;
import df.bean.db.table.PayorOffice;
import df.bean.db.table.TRN_Error;
import df.bean.obj.receipt.Receipt;

public class ProcessReceipt extends Process{
    public ProcessReceipt(DBConnection conn) {
        super(conn);
        this.setDBConnection(conn);
    }
    
    @Override
    public boolean Calculate(String YYYY, String MM, String hospitalCode) {
        boolean ret = false;
            try {
                ret = true;
                if (updateReceiptByCash(YYYY, MM, hospitalCode, "TRN_DAILY") &&
                    updateReceiptByAR(YYYY, MM, hospitalCode, "TRN_DAILY") &&
                    updateReceiptByDoctor(YYYY, MM, hospitalCode, "TRN_DAILY") &&
                    updateReceiptByPayor(YYYY, MM, hospitalCode, "TRN_DAILY") &&
                    updateLoaded(YYYY, MM, hospitalCode)
                ){ ret = true; } else { ret = false; }
            } catch (Exception e) {
                e.printStackTrace();
                ret=false;
            }

        return ret;
    } 
    
    public boolean CalculateReceiptByAR(String YYYY, String MM, String startDate, String endDate, String hospitalCode) {
        boolean ret = false;
            try {
                ret = true;
                this.getDBConnection().beginTrans();
                if( updateReceiptByAR(YYYY, MM, startDate , endDate, hospitalCode, "TRN_DAILY") &&
                    updateLoadedByDate(startDate, endDate, hospitalCode) ){
                	ret = true; 
                }else{ ret = false; }
            } catch (Exception e) {
                // TODO
            	TRN_Error.setUser_name(this.getName());
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                TRN_Error.PROCESS_RECEIPT_BY_AR,"Receipt By AR is Error.",e.getMessage());                
                e.printStackTrace();
                ret=false;
            } finally {
                try {
                    if (ret) this.getDBConnection().commitTrans();
                    if (!ret) this.getDBConnection().rollBackTrans();
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }
        return ret;
    } 
    
    public boolean CalculateReceiptByDoctor(String YYYY, String MM, String hospitalCode, String doctorCode) {
        boolean ret = false;
            try {
                ret = true;
//                this.getDBConnection().beginTrans();
                if (updateReceiptByDoctor(YYYY, MM, hospitalCode, "TRN_DAILY", doctorCode))  
                {  ret = true; }
                else { ret = false;   }
                
            } catch (Exception e) {
                // TODO
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_RECEIPT_BY_DOCTOR, 
                                "Receipt By Doctor is Error.", 
                                e.getMessage());
                e.printStackTrace();
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }
//            if (ret) this.getDBConnection().commitTrans(); 
//            if (!ret) this.getDBConnection().rollBackTrans(); 

        return ret;
    } 
    
    public boolean CalculateReceiptByPayor(String YYYY, String MM, String hospitalCode, String payorCode) {
        boolean ret = false;
            try {
                ret = true;
//                this.getDBConnection().beginTrans();
                if (updateReceiptByPayor(YYYY, MM, hospitalCode, "TRN_DAILY", payorCode))  
                {  ret = true; }
                else { ret = false;   }
                
            } catch (Exception e) {
                // TODO
                e.printStackTrace();
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_RECEIPT_BY_PAYOR, 
                                "Receipt By Payor is Error.", 
                                e.getMessage());
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }
//            if (ret) this.getDBConnection().commitTrans(); 
//            if (!ret) this.getDBConnection().rollBackTrans(); 

        return ret;
    } 
    
    public boolean CalculateReceiptByCash(String YYYY, String MM, String startDate,String endDate, String hospitalCode) {
        boolean ret = false;
            try {
                ret = true;
//                this.getDBConnection().beginTrans();
                if (updateReceiptByCash(YYYY, MM, startDate , endDate, hospitalCode, "TRN_DAILY") )  
                {  ret = true; }
                else { ret = false; }
                
            } catch (Exception e) {
                // TODO
                e.printStackTrace();
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_IMPORT_BILL, "CalculateReceiptByCash is Error.", 
                                e.getMessage());
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }
        
//            if (ret) this.getDBConnection().commitTrans(); 
//            if (!ret) this.getDBConnection().rollBackTrans(); 
            
        return ret;
    } 
    //////////////////////////////////////////////////////////////////////
    
    public boolean updateReceiptByCash(String YYYY, String MM, String hospitalCode, String tableName) {
        boolean ret = false;
        int iCount = 0;
        Receipt rec = new Receipt(this.getDBConnection());
            try {
                ret = true;
                iCount = rec.updateReceipt(YYYY, MM, hospitalCode, tableName);
                if (( iCount > -1 )) {
                    ret = true;
                }
                else { ret = false;   }
                
            } catch (Exception e) {
                // TODO
                e.printStackTrace();
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                    rec = null;
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }

        return ret;
    } 
    
    // receipt by AR
    public boolean updateReceiptByAR(String YYYY, String MM, String hospitalCode, String tableName) {
        boolean ret = false;
        IntErpArReceipt erp = new IntErpArReceipt(this.getDBConnection());
        try {
            if (( erp.updateReceiptOverMonth(YYYY+MM+"00", YYYY+MM+"31", hospitalCode, tableName) > -1 ) 
                    && (erp.updateReceiptInMonth(YYYY+MM+"00", YYYY+MM+"31", hospitalCode, tableName) > -1)
                    && erp.updateYyyyMm(YYYY, MM, hospitalCode, tableName) > -1) {
                ret = true;
            }
            else { ret = false;   }
                
        } catch (Exception e) {
            e.printStackTrace();
            ret=false;
        } finally {
           //Clean up resources, close the connection.
            try {
                erp = null;
            } catch (Exception ex) { 
                ex.printStackTrace();  
                ret = false; 
            } 
        }

        return ret;
    }
    
    // receipt by doctor
    public boolean updateReceiptByDoctor(String YYYY, String MM, String hospitalCode, String tableName) {
        boolean ret = false;
        int iCount = 0;
        Doctor rec = new Doctor(this.getDBConnection());
            try {
                ret = true;
                iCount = rec.updateReceipt(YYYY, MM, hospitalCode, tableName);
                if (( iCount > -1 )) {
                    ret = true;
                }
                else { ret = false;   }
                
            } catch (Exception e) {
                // TODO
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                this.getClass().getName(), " updateReceiptByDoctor is Error.", 
                                e.getMessage());   
                e.printStackTrace();
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                    rec = null;
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }

        return ret;
    } 
    // receipt by payor
    public boolean updateReceiptByPayor(String YYYY, String MM, String hospitalCode, String tableName) {
        boolean ret = false;
        int iCount = 0;
        PayorOffice rec = new PayorOffice(this.getDBConnection());
            try {
                ret = true;
                iCount = rec.updateReceipt(YYYY, MM, hospitalCode, tableName);
                if (( iCount > -1 )) {
                    ret = true;
                }
                else { ret = false;   }
                
            } catch (Exception e) {
                // TODO
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                this.getClass().getName(), " updateReceiptByPayor is Error.", 
                                e.getMessage());   
                e.printStackTrace();
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                    rec = null;
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }

        return ret;
    } 
    
    // update status isloaded and yyyy / mm
    public boolean updateLoaded(String YYYY, String MM, String hospitalCode) {
        boolean ret = false;
        IntErpArReceipt erp = new IntErpArReceipt(this.getDBConnection());
        try {
            if (erp.updateIsLoaded(YYYY+MM+"00", YYYY+MM+"31", hospitalCode) > -1){
                ret = true;
            }
            else { ret = false;   }
                
        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                this.getClass().getName(), " updateLoaded is Error.", 
                                e.getMessage());   
            e.printStackTrace();
            ret=false;
        } finally {
           //Clean up resources, close the connection.
            try {
                erp = null;
            } catch (Exception ex) { 
                ex.printStackTrace();  
                ret = false; 
            } 
        }

        return ret;
    }
    
    /////////////////////  Update By date ////////////////////
    
    // receipt by AR date
    public boolean updateReceiptByAR(String YYYY, String MM, String startDate, String endDate, String hospitalCode, String tableName) {
        boolean ret = false;
        IntErpArReceipt erp = new IntErpArReceipt(this.getDBConnection());
        try {
        	
            if (( erp.updateReceiptOverMonth(startDate, endDate, hospitalCode, tableName) > -1 ) 
                    && (erp.updateReceiptInMonth(startDate, endDate, hospitalCode, tableName) > -1)
                    && erp.updateYyyyMm(YYYY, MM, startDate, endDate, hospitalCode, tableName) > -1) {
                ret = true;
            }
            /*
            if ((erp.updateReceiptInMonth(startDate, endDate, hospitalCode, tableName) > -1)
                    && erp.updateYyyyMm(YYYY, MM, startDate, endDate, hospitalCode, tableName) > -1) {
                ret = true;
            }
            */
            else { ret = false; }
                
        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_RECEIPT_BY_AR, " updateReceiptByAR is Error.", 
                                e.getMessage());   
            e.printStackTrace();
            ret=false;
        } finally {
           //Clean up resources, close the connection.
            try {
                erp = null;
            } catch (Exception ex) { 
                ex.printStackTrace();  
                ret = false; 
            } 
        }

        return ret;
    }
    
    // update status isloaded and yyyy / mm
    public boolean updateLoadedByDate(String startDate, String endDate, String hospitalCode) {
        boolean ret = false;
        IntErpArReceipt erp = new IntErpArReceipt(this.getDBConnection());
        try {
            if (erp.updateIsLoaded(startDate, endDate, hospitalCode) > -1){
                ret = true;
            }
            else { ret = false;   }
                
        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                this.getClass().getName(), " updateLoadedByDate is Error.", 
                                e.getMessage());   
            e.printStackTrace();
            ret=false;
        } finally {
           //Clean up resources, close the connection.
            try {
                erp = null;
            } catch (Exception ex) { 
                ex.printStackTrace();  
                ret = false; 
            } 
        }

        return ret;
    }
    
    // receipt by doctor
    public boolean updateReceiptByDoctor(String YYYY, String MM, String hospitalCode, 
                                            String tableName, String doctorCode) {
        boolean ret = false;
        int iCount = 0;
        Doctor rec = new Doctor(this.getDBConnection());
            try {
                ret = true;
                iCount = rec.updateReceipt(YYYY, MM, hospitalCode, tableName, doctorCode);
                if (( iCount > -1 )) {
                    ret = true;
                }
                else { ret = false;   }
                
            } catch (Exception e) {
                // TODO
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_RECEIPT_BY_DOCTOR, 
                                "Receipt By Doctor is Error.", 
                                e.getMessage()); 
                e.printStackTrace();
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                    rec = null;
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }

        return ret;
    } 
    
    // receipt by payor
    public boolean updateReceiptByPayor(String YYYY, String MM, String hospitalCode, 
                                            String tableName, String payorCode) {
        boolean ret = false;
        int iCount = 0;
        PayorOffice rec = new PayorOffice(this.getDBConnection());
            try {
                ret = true;
                iCount = rec.updateReceipt(YYYY, MM, hospitalCode, tableName, payorCode);
                if (( iCount > -1 )) {
                    ret = true;
                }
                else { ret = false;   }
                
            } catch (Exception e) {
                // TODO
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_RECEIPT_BY_PAYOR, 
                                "Receipt By Payor is Error.", 
                                e.getMessage());
                e.printStackTrace();
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                    rec = null;
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }

        return ret;
    } 
    
    public boolean updateReceiptByCash(String YYYY, String MM, String startDate, String endDate, String hospitalCode, String tableName) {
        boolean ret = false;
        int iCount = 0;
        Receipt rec = new Receipt(this.getDBConnection());
            try {
                ret = true;
                iCount = rec.updateReceipt(YYYY, MM, startDate, endDate, hospitalCode, tableName);
                if (( iCount > -1 )) {
                    ret = true;
                }
                else { ret = false;   }
                
            } catch (Exception e) {
                // TODO
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                                TRN_Error.PROCESS_IMPORT_BILL, " updateReceiptByCash is Error.", 
                                e.getMessage() );   
                e.printStackTrace();
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                    rec = null;
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    ret = false; 
                } 
            }

        return ret;
    } 
    
    // receipt by Write Off
    public boolean updateReceiptWriteOff(String startDate, String endDate, String hospitalCode, String tableName) {
        boolean ret = false;
        IntErpArReceipt erp = new IntErpArReceipt(this.getDBConnection());
        try {
            if ( erp.updateWriteOff(startDate, endDate, hospitalCode, tableName) > -1 ) {
                ret = true;
            }
            else { ret = false;   }
                
        } catch (Exception e) {
            e.printStackTrace();
            ret=false;
        } finally {
           //Clean up resources, close the connection.
            try {
                erp = null;
            } catch (Exception ex) { 
                ex.printStackTrace();  
                ret = false; 
            } 
        }

        return ret;
    }
    
}
