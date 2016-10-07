package df.bean.obj.util;

import java.util.ArrayList;

public class AjaxUtils {
	public String hospitalCode = "";
	public String code = "";
	boolean check =false;
	public ArrayList<String> arrData = new ArrayList<String>();
	public ArrayList<String> arrDoctorCode = new ArrayList<String>();
	public ArrayList<String> arrDoctorName = new ArrayList<String>();
	public int count = 0;
	public ArrayList<Boolean> arrBoolean = new ArrayList<Boolean>();
	public ArrayList<String> lstCode = new ArrayList<String>();
	public ArrayList<String> errorMessage = new ArrayList<String>();

	public ArrayList<String> getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(ArrayList<String> errorMessage) {
		this.errorMessage = errorMessage;
	}
	public ArrayList<Boolean> getArrBoolean() {
		return arrBoolean;
	}
	public ArrayList<String> getLstCode() {
		return lstCode;
	}
	public void setLstCode(ArrayList<String> lstCode) {
		this.lstCode = lstCode;
	}
	public void setArrBoolean(ArrayList<Boolean> arrBoolean) {
		this.arrBoolean = arrBoolean;
	}
	public ArrayList<String> getArrDoctorCode() {
		return arrDoctorCode;
	}
	public void setArrDoctorCode(ArrayList<String> arrDoctorCode) {
		this.arrDoctorCode = arrDoctorCode;
	}
	public ArrayList<String> getArrDoctorName() {
		return arrDoctorName;
	}
	public void setArrDoctorName(ArrayList<String> arrDoctorName) {
		this.arrDoctorName = arrDoctorName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ArrayList<String> getArrData() {
		return arrData;
	}
	public void setArrData(ArrayList<String> arrData) {
		this.arrData = arrData;
	}
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
}