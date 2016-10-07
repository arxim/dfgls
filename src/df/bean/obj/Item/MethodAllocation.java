package df.bean.obj.Item;

import df.bean.db.conn.DBConnection;

public class MethodAllocation {

    private String hospitalCode;
    private String admissionTypeCode;
    private String orderItemCategoryCode;
    private String orderItemCode;
    private String doctorCategoryCode;
    private String doctorCode;
    private String payerCategoryCode;
    private String doctorTreatmentCode;
    private String privateDoctor;
    private String itemProcedure;
    private double price;
    private double normalAllocPct;
    private double normalAllocAmt;
    private String guaranteeSource;
    private String excluedTreatment;
    private double taxRate;
    private String taxType;
    private String taxRevenueFrom;
    private String active;
    private String step;
    private DBConnection conn = null;

    public MethodAllocation(DBConnection conn, String hospitalCode) {
        this.setHospitalCode(hospitalCode);
        this.conn = conn;
    }

    public boolean isFound(String admissionType, String orderItemCat, String orderItem, String doctorCat, String doctor, String payer, String doctorTreatment, String privateDoctor, String itemProcedure, double price){
    	return true;
    }
    public String getHospitalCode() {
        return hospitalCode;
    }
    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
         
    public String getAdmissionTypeCode() {
        return admissionTypeCode;
    }
    public void setAdmissionTypeCode(String admissionTypeCode) {
        this.admissionTypeCode = admissionTypeCode;
    }

    public String getOrderItemCategoryCode() {
        return orderItemCategoryCode;
    }
    public void setOrderItemCategoryCode(String orderItemCategoryCode) {
        this.orderItemCategoryCode = orderItemCategoryCode;
    }

    public String getOrderItemCode() {
        return orderItemCode;
    }
    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }
    
    public String getDoctorCategoryCode() {
        return doctorCategoryCode;
    }
    public void setDoctorCategoryCode(String doctorCategoryCode) {
        this.doctorCategoryCode = doctorCategoryCode;
    }

    public String getDoctorCode() {
        return doctorCode;
    }
    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

	public String getPayerCategoryCode() {
		return payerCategoryCode;
	}
	public void setPayerCategoryCode(String payerCategoryCode) {
		this.payerCategoryCode = payerCategoryCode;
	}

	public String getDoctorTreatmentCode() {
		return doctorTreatmentCode;
	}
	public void setDoctorTreatmentCode(String doctorTreatmentCode) {
		this.doctorTreatmentCode = doctorTreatmentCode;
	}    

	public String getPrivateDoctor() {
		return privateDoctor;
	}
	public void setPrivateDoctor(String privateDoctor) {
		this.privateDoctor = privateDoctor;
	}

	public String getItemProcedure() {
		return itemProcedure;
	}
	public void setItemProcedure(String itemProcedure) {
		this.itemProcedure = itemProcedure;
	}
	
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }


    public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getNormalAllocPct() {
		return normalAllocPct;
	}
	public void setNormalAllocPct(double normalAllocPct) {
		this.normalAllocPct = normalAllocPct;
	}

	public double getNormalAllocAmt() {
		return normalAllocAmt;
	}
	public void setNormalAllocAmt(double normalAllocAmt) {
		this.normalAllocAmt = normalAllocAmt;
	}


	public String getGuaranteeSource() {
		return guaranteeSource;
	}

	public void setGuaranteeSource(String guaranteeSource) {
		this.guaranteeSource = guaranteeSource;
	}

	public String getExcluedTreatment() {
		return excluedTreatment;
	}
	public void setExcluedTreatment(String excluedTreatment) {
		this.excluedTreatment = excluedTreatment;
	}

	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTaxRevenueFrom() {
		return taxRevenueFrom;
	}
	public void setTaxRevenueFrom(String taxRevenueFrom) {
		this.taxRevenueFrom = taxRevenueFrom;
	}

	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}

	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
}