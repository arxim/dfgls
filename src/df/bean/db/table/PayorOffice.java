package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;
import java.util.ArrayList;
import java.util.List;

public class PayorOffice extends ABSTable {

    private String code;
    private String nameThai;
    private String nameEng;
    private String address;
    private String zip;
    private String tel;
    private String isAdvancePayment;
    private String hospitalCode;

    public PayorOffice() {
        super();
    }

    public PayorOffice(DBConnection dBConnection) {
        this.setDBConnection(dBConnection);
    }

    public String getAddress() {
        return this.address;
    }

    public String getCode() {
        return this.code;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getNameEng() {
        return this.nameEng;
    }

    public String getNameThai() {
        return this.nameThai;
    }

    public String getTel() {
        return this.tel;
    }

    public String getZip() {
        return this.zip;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public void setNameThai(String nameThai) {
        this.nameThai = nameThai;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }


    public PayorOffice(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from PAYOR_OFFICE where CODE='" + code + "'" + 
                            " and HOSPITAL_CODE ='" + hospitalCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.nameThai = this.getResultSet().getString("NAME_THAI");
                this.nameEng = this.getResultSet().getString("Name_Eng");
                this.address = this.getResultSet().getString("Address");
                this.zip = this.getResultSet().getString("Zip");
                this.tel = this.getResultSet().getString("Tel");
                this.isAdvancePayment = this.getResultSet().getString("IS_ADVANCE_PAYMENT");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(this.getResultSet() != null) {
                  try {
                     this.getResultSet().close();
                     this.setResultSet(null);
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }

    public String getIsAdvancePayment() {
        return isAdvancePayment;
    }

    public void setIsAdvancePayment(String isAdvancePayment) {
        this.isAdvancePayment = isAdvancePayment;
    }
    
        
    //update receipt by PAYOR
    public int updateReceipt(String YYYY, String MM, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_PAYOR = 'Y' "
            		+ " ,RECEIPT_DATE = INVOICE_DATE, RECEIPT_NO = INVOICE_NO "
                    + " ,YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " WHERE " + tableName + ".PAY_BY_PAYOR = 'N' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND IS_ONWARD != 'Y' "
                    + " AND " + tableName + ".PAYOR_OFFICE_CODE IN (SELECT CODE FROM PAYOR_OFFICE WHERE ACTIVE = '1' AND IS_ADVANCE_PAYMENT = 'Y') "
                    + " AND(TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
            rows = this.getDBConnection().executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    } 
    
    //update receipt by PAYOR
    public int updateReceipt(String YYYY, String MM, String hospitalCode, String tableName, String payorCode) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_PAYOR = 'Y' "
            		+ " ,RECEIPT_DATE = INVOICE_DATE, RECEIPT_NO = INVOICE_NO "
                    + " ,YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " WHERE " + tableName + ".PAY_BY_PAYOR = 'N' "
                    + " AND IS_ONWARD != 'Y' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND " + tableName + ".PAYOR_OFFICE_CODE = '" + payorCode + "'"
                    + " AND(TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
            rows = this.getDBConnection().executeUpdate(sql);
            //System.out.println(sql+":"+rows);
        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
            TRN_Error.PROCESS_RECEIPT_BY_PAYOR, "Receipt By Payor is Error.", e.getMessage(), sql);
            e.printStackTrace();
        }
        return rows;
    } 
    
    public boolean rollBackUpdate(String YYYY, String MM, String hospitalCode, String tableName) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "UPDATE " + tableName + " SET YYYY = ''"
                    + " ,MM = ''"
            		+ " ,RECEIPT_DATE = '', RECEIPT_NO = ''"
                    + " WHERE " + tableName + ".PAY_BY_PAYOR = 'Y' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (PAY_BY_CASH <> 'Y' AND PAY_BY_AR <> 'Y' AND PAY_BY_DOCTOR <> 'Y' AND PAY_BY_CASH_AR <> 'Y')"
                    + " AND(TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
        
        String sql2 = "UPDATE " + tableName + " SET " + tableName + ".PAY_BY_PAYOR = 'N' "
                    + " WHERE " + tableName + ".PAY_BY_PAYOR = 'Y' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND(TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
        //System.out.println(sql1);
        sqlCommand.add( sql1 );
        sqlCommand.add( sql2 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
}
