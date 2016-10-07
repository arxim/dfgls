package df.bean.obj.bank;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.BankTMBMediaClearing;
import df.bean.db.table.BankTMBPaymentMonthlyD;
import df.bean.db.table.BankTMBPaymentMonthlyH;
import df.bean.db.table.BankTMBPaymentMonthlyT;
import df.bean.db.table.Hospital;
import df.bean.db.table.PaymentMode;
import df.bean.db.table.Status;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Utils;
import df.bean.obj.util.Variables;

public class BankTMB {
    public String hospitalCode = "";
    public String doctorCode = "";
    public String tableName = "";
    private DBConnection dbConnection;
    private BankTMBPaymentMonthlyH header = null;
    private BankTMBPaymentMonthlyD detail = null;
    private BankTMBPaymentMonthlyT total = null;
    private BankTMBMediaClearing media = null;
    
    public BankTMB(String hospitalCode, DBConnection conn) {
        this.tableName = "SUMMARY_PAYMENT";
        this.setDBConnection(conn);
        this.hospitalCode = hospitalCode;
        header = new BankTMBPaymentMonthlyH(conn);
        detail = new BankTMBPaymentMonthlyD(conn);
        total = new BankTMBPaymentMonthlyT(conn);
        media = new BankTMBMediaClearing(conn);
    }  
    @Override
    public void finalize() {
        header = null;
        detail = null;
        total = null;
    }
    public DBConnection getDBConnection() {
        return dbConnection;
    }

    public void setDBConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    
    public boolean Calculate(String YYYY, String MM, String hospitalCode) {
        boolean ret = true;
        double mTotalCrAmount = 0;
        int runningNo = 1;
        this.hospitalCode = hospitalCode;
        Hospital hospital=null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = " select sum(DR_NET_PAID_AMT) as sumAmt, DOCTOR_CODE, BANK_CODE, BANK_ACCOUNT_NO, BANK_ACCOUNT_NAME " + 
                     " from " + tableName + " left join DOCTOR on " + tableName + ".DOCTOR_CODE = DOCTOR.CODE " +
                     " where DOCTOR.PAYMENT_MODE_CODE='" + PaymentMode.PAYMENT_BANK + "'" +
                     " and (BATCH_NO is null or BATCH_NO = '') " +
                     " and " + tableName + ".HOSPITAL_CODE='" + hospitalCode + "'" +
                     " and YYYY='" + YYYY + "' and MM='" + MM + "'" + 
                     " and DR_NET_PAID_AMT > 0" +
                     " group by DOCTOR_CODE, BANK_CODE, BANK_BRANCH_CODE, BANK_ACCOUNT_NO, BANK_ACCOUNT_NAME";

        try {
            stmt = this.getDBConnection().getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            hospital = new Hospital(hospitalCode, this.getDBConnection());
            
            // Header
            header.setHospitalCode(hospitalCode);
            header.setYyyy(YYYY);
            header.setMm(MM);
            header.setRecordType(header.RECORD_TYPE);
            header.setSequenceNumber(header.SEQUENCE_NUMBER);
            header.setBankCode(header.BANK_CODE);
            header.setCompanyAccountNo(hospital.getAccountNo());
            header.setCompanyName(hospital.getDescriptionEng());
            header.setPostDate(JDate.getDay() + JDate.getMonth() + JDate.getYear());
            header.setTapeNumber(header.getTapeNumber());
            header.setSpare(Utils.replAheadWith("0", header.getSpare(), 71));
            
            if (!header.insert()) { return false; }
            
            while (rs.next()) {
                runningNo ++;
                mTotalCrAmount = mTotalCrAmount + rs.getDouble("sumAmt");
                detail.setHospitalCode(hospitalCode);
                detail.setYyyy(YYYY);
                detail.setMm(MM);
                detail.setRecordType(detail.RECORD_TYPE);
                detail.setSequenceNumber(""+runningNo);
                detail.setBankCode(detail.BANK_CODE);
                detail.setAccountNumber(rs.getString("BANK_ACCOUNT_NO"));
                detail.setTransactionCode(detail.TRANSACTION_CODE);
                detail.setAmount(""+rs.getDouble("sumAmt"));
                detail.setServiceType(detail.SERVICE_TYPE);
                detail.setStatus(detail.STATUS);
                detail.setReferenceArea1("Ref 1");
                detail.setInserviceDate(JDate.getDate());
                detail.setCompanyCode(detail.COMPANY_CODE);
                detail.setHomeBranch(detail.HOME_BRANCH);
                detail.setReferenceArea2(detail.REFERENCE_AREA_2);
                detail.setTmbFlag(detail.TMB_FLAG);
                detail.setSpare(detail.getSpare());
                detail.setAccountName(rs.getString("BANK_ACCOUNT_NAME"));
                
                if (!detail.insert()) { return false; }
                
            }
            
            // Total
            runningNo ++;
            total.setHospitalCode(hospitalCode);
            total.setYyyy(YYYY);
            total.setMm(MM);
            total.setRecordType(total.RECORD_TYPE);
            total.setSequenceNumber(""+runningNo);
            total.setBankCode(total.BANK_CODE);
            total.setCompanyAccountNo(hospital.getAccountNo());
            total.setNoOfDrTransaction("0");
            total.setTotalDrAmount("0");
            total.setNoOfCrTransaction("0");
            total.setTotalCrAmount(""+mTotalCrAmount);
            
            total.setNoOfRejectDrTrans(total.getNoOfRejectDrTrans());
            total.setTotalRejectDrAmount(total.getTotalRejectDrAmount());
            total.setNoOfRejectCrTrans(total.getNoOfRejectCrTrans());
            total.setTotalRejectCrAmount(total.getTotalRejectCrAmount());
            
            total.setSpare("0");
            if (!total.insert()) { return false; }
            
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
            ret=false;
        } finally {
               //Clean up resources, close the connection.
                try {
                    header = null;
                    detail = null;
                    total = null;
                    
                    if(rs != null) {
                        rs.close();
                        rs = null;
                    }
                    if (stmt != null) {
                        stmt.close();
                        stmt = null;
                    }
                } catch (Exception ignored) { ignored.printStackTrace();   }
        }
                                                   

        
        return ret;
    } 
    
    // used
    public boolean setSummaryMonthlyStatusCalculated() {
        boolean ret = false;
        String sql = "update " + tableName + " set STATUS_MODIFY='" + Status.STATUS_CALCULATED + "'";
        sql = sql + " where HOSPITAL_CODE='" + this.hospitalCode + "'";
        sql = sql + " and DOCTOR_CODE='" + this.doctorCode + "'";
        sql = sql + " and (BATCH_NO is null or BATCH_NO = '')";
        sql = sql + " and (STATUS_MODIFY <> '" + Status.STATUS_CALCULATED + "' or STATUS_MODIFY is null)";
        if (this.getDBConnection().executeUpdate(sql)>-1) {
            ret = true;
        } 
        return ret;
    }

    public boolean CalculateMediaClearing(String YYYY, String MM, String paymentDate, String paymentType, String hospitalCode) {
        boolean ret = true;
        int runningNo = 0;
        this.hospitalCode = hospitalCode;
        Hospital hospital=null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = " select sum(DR_NET_PAID_AMT) as sumAmt, DOCTOR_CODE, BANK_CODE, BANK_BRANCH_CODE, BANK_ACCOUNT_NO, BANK_ACCOUNT_NAME, NAME_THAI " +        
                     " from " + tableName + " inner join DOCTOR on " + tableName + ".DOCTOR_CODE = DOCTOR.CODE " +
                     " and "+tableName + ".HOSPITAL_CODE = DOCTOR.HOSPITAL_CODE "+
                     " where DOCTOR.PAYMENT_MODE_CODE='" + PaymentMode.PAYMENT_BANK + "'" +
//                     " and (BATCH_NO is null or BATCH_NO = '') " +
                     " and " + tableName + ".HOSPITAL_CODE='" + hospitalCode + "'" +
                     " and PAYMENT_DATE = '" + paymentDate + "'" +
                     //" and PAY_DATE = '" + paymentDate + "'" +COMMENT BY NOP 2011-02-06
//                     " and YYYY='" + YYYY + "' and MM='" + MM + "'" + 
                     " and DR_NET_PAID_AMT > 0" +
                     " and PAYMENT_TYPE = '" + paymentType + "'" +
                     " group by DOCTOR_CODE, BANK_CODE, BANK_BRANCH_CODE, BANK_ACCOUNT_NO, BANK_ACCOUNT_NAME, NAME_THAI";
        System.out.println(sql);
        try {
            stmt = this.getDBConnection().getConnection().createStatement();
            rs = stmt.executeQuery(sql);
            hospital = new Hospital(hospitalCode, this.getDBConnection());
            
            while (rs.next()) {
                runningNo ++;
                this.doctorCode = rs.getString("DOCTOR_CODE");
                media.setFileType(BankTMBMediaClearing.FILE_TYPE);
                media.setRecordType(BankTMBMediaClearing.RECORD_TYPE);
                media.setBatchNumber("000001");
                media.setReceivingBankCode(Utils.replAheadWith("0", rs.getString("BANK_CODE"), 3));
                media.setReceivingBranchCode(Utils.replAheadWith("0", rs.getString("BANK_BRANCH_CODE"), 4));
                media.setReceivingAccountNo(Utils.replAheadWith("0", rs.getString("BANK_ACCOUNT_NO"), 11).trim());
                media.setSendingBankCode(Utils.replAheadWith("0", hospital.getBankCode(), 3));
                media.setSendingBranchCode(Utils.replAheadWith("0", hospital.getBankBranchCode(), 4));
                media.setSendingAccountNo(Utils.replAheadWith("0", hospital.getAccountNo(), 11));
                media.setEffectiveDate( paymentDate.substring(6, 8) + paymentDate.substring(4, 6) + paymentDate.substring(0, 4) );
                //media.setEffectiveDate( paymentDate );
                media.setServiceType(paymentType);
                media.setClearingHouseCode(BankTMBMediaClearing.CLEARING_HOUSE_CODE);
                media.setTransferAmount(Utils.replAheadWith("0", Utils.removeString(".", "" + rs.getDouble("sumAmt")), 12));
                media.setReceiverInformation(rs.getString("NAME_THAI"));
                media.setSenderInformation(hospital.getDescriptionThai());
                media.setOtherInformation(BankTMBMediaClearing.OTHER_INFORMATION);
                media.setReferenceRunningNo(Utils.replAheadWith("0",""+runningNo, 6));
                media.setSpace(Utils.replAheadWith(" ", media.getSpace(), 16));
                media.setCompanyCode(BankTMBMediaClearing.COMPANY_CODE);
                media.setSequenceNumber(Utils.replAheadWith("0",""+runningNo, 5));
                
                media.setDoctorCode(rs.getString("DOCTOR_CODE"));
                media.setHospitalCode(hospitalCode);
                media.setUpdateDate(JDate.getDate());
                media.setUpdateTime(JDate.getTime());
                media.setUserId(Variables.getUserID());
                media.setBatchNo("");
                media.setYyyy(YYYY);
                media.setMm(MM);
                                
                if (!media.insert()) { return false; }
                
            }
            
        } catch (SQLException e) {
            // TODO
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_BANK_TMB_PAYMENT_MONTHLY, "Transaction was duplicated", e.getMessage(), sql);
            e.printStackTrace();
            ret=false;
        } finally {
               //Clean up resources, close the connection.
                try {
                    media = null;
                    
                    if(rs != null) {
                        rs.close();
                        rs = null;
                    }
                    if (stmt != null) {
                        stmt.close();
                        stmt = null;
                    }
                } catch (Exception ignored) { ignored.printStackTrace();   }
        }
        return ret;
    } 
        
    
}
