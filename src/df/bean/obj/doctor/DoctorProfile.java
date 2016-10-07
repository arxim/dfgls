package df.bean.obj.doctor;

import java.util.List;
import df.bean.db.table.Hospital;

public class DoctorProfile {

    df.bean.db.table.DoctorProfile doctorGroup;
    Hospital hospital;


    /**
     * @associates <{df.bean.obj.DOCTOR.CareProvider}>
     */
    List careProvider;
}
