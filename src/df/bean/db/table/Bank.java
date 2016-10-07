package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class Bank  extends ABSTable{

    protected String code;
    protected String descriptionThai;
    protected String descriptionEng;
    protected String ACTIVE;

    public Bank() {
        super();
    }

    public Bank(String code, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from BANK where CODE='" + code + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.setDescriptionEng(this.getResultSet().getString("Description_Eng"));
                this.setDescriptionThai(this.getResultSet().getString("Description_Thai"));
                this.setActive(this.getResultSet().getString("ACTIVE"));
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescriptionThai() {
        return descriptionThai;
    }

    public void setDescriptionThai(String descriptionThai) {
        this.descriptionThai = descriptionThai;
    }

    public String getDescriptionEng() {
        return descriptionEng;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }
    
}
