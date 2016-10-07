package df.bean.obj.doctor;

import df.bean.db.conn.DBConnection;

public class DrConsult extends CareProvider {
    
    public DrConsult() {
        super();
    }
    public DrConsult(String doctorCode, String hospitalCode, DBConnection conn) {
        super(doctorCode, hospitalCode, conn);
    }
    
}
