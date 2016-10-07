package df.bean.obj.doctor;

import df.bean.db.conn.DBConnection;

public class DrInturn extends CareProvider {
    public DrInturn() {
        super();
    }
    public DrInturn(String doctorCode, String hospitalCode, DBConnection conn) {
        super(doctorCode, hospitalCode, conn);
    }
}
