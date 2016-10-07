package df.bean.obj.doctor;

import df.bean.db.conn.DBConnection;

public class Technician extends CareProvider {
    public Technician() {
        super();
    }
    public Technician(String doctorCode, String hospitalCode, DBConnection conn) {
        super(doctorCode, hospitalCode, conn);
    }
}
