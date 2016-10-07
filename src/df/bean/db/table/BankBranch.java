package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class BankBranch  extends ABSTable{

    private String code;
    private String descriptionThai;
    private String descriptionEng;
    protected String ACTIVE;

    public BankBranch() {
        super();
    }

    public String getCode() {
        return this.code;
    }

    public String getDescriptionEng() {
        return this.descriptionEng;
    }

    public String getDescriptionThai() {
        return this.descriptionThai;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public void setDescriptionThai(String descriptionThai) {
        this.descriptionThai = descriptionThai;
    }
    public BankBranch(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from BANK_BRANCH where CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.setCode(this.getResultSet().getString("CODE"));
                this.setDescriptionThai(this.getResultSet().getString("Description_Thai"));
                this.setDescriptionEng(this.getResultSet().getString("Description_Eng"));
                this.setActive(this.getResultSet().getString("ACTIVE"));
            }
            this.getResultSet().close();
        } catch (SQLException e) {
            // TODO
            System.out.print(e.getMessage());
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

    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

}
