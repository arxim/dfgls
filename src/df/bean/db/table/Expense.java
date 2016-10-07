package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class Expense extends ABSTable {

    private String code;
    private String description;
    private String hospitalCode;
    private Integer sign;
    private String accountCode;

    public Expense() {
        super();
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public Integer getSign() {
        return this.sign;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    public Expense(String code, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from EXPENSE where CODE='" + code + "'" + 
                                            " and HOSPITAL_CODE='" + hospitalCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
                this.hospitalCode = this.getResultSet().getString("HOSPITAL_CODE");
                this.sign = this.getResultSet().getInt("Sign");
                this.accountCode = this.getResultSet().getString("Account_Code");
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

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }
}
