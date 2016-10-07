package df.bean.db.table;

public class StpTranferDfModel {

		// PK
		private String hospitalCode;
		
		// PK
		private String doctorFrom;
		
		private String doctorFromName;
		
		//PK
		private String doctorTo;
		
		private String doctorToName;
		
		//PK
		private String admissionType;
		
		public String getHospitalCode() {
			return hospitalCode;
		}
		
		public void setHospitalCode(String hospitalCode) {
			this.hospitalCode = hospitalCode;
		}
		
		public String getDoctorFrom() {
			return doctorFrom;
		}
		
		public void setDoctorFrom(String doctorFrom) {
			this.doctorFrom = doctorFrom;
		}
		public String getDoctorTo() {
			return doctorTo;
		}
		
		public void setDoctorTo(String doctorTo) {
			this.doctorTo = doctorTo;
		}
		public String getAdmissionType() {
			return admissionType;
		}
		
		public void setAdmissionType(String admissionType) {
			this.admissionType = admissionType;
		}

		public String getDoctorFromName() {
			return doctorFromName;
		}

		public void setDoctorFromName(String doctorFromName) {
			this.doctorFromName = doctorFromName;
		}

		public String getDoctorToName() {
			return doctorToName;
		}

		public void setDoctorToName(String doctorToName) {
			this.doctorToName = doctorToName;
		}
		
		
		
}
