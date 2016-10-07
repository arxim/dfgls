package df.bean.obj.invoice;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.AdmissionType;
import df.bean.db.table.Hospital;
import df.bean.db.table.InvoiceHeader;
import df.bean.db.table.PayorOffice;

import df.bean.obj.location.Location;

public class Invoice {
    InvoiceHeader invoiceHeader = null;
    Hospital hospital = null;
    AdmissionType admissionType = null;
    Location location = null;
    PayorOffice payorOffice = null;

    private String invoiceNo;
    private String hospitalCode;
    public DBConnection conn = null;

    public Invoice(String invoiceNo, String hospitalCode, DBConnection conn) {
        this.setInvoiceNo(invoiceNo);
        this.setHospitalCode(hospitalCode);
        
        this.conn = conn;
        this.newInvoiceHeader();
//        this.newHospital();
//        this.newAdmissionType();
//        this.newLocation();
//        this.newPayorOffice();
    }
    protected void finalize() {
        invoiceHeader = null;
        hospital = null;
        admissionType = null;
        location = null;
        payorOffice = null;
    }
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }
    // new invoice header
    public void newInvoiceHeader() {
        invoiceHeader = new InvoiceHeader(this.invoiceNo, this.hospitalCode, this.getDBConnection());
    }
    // new hospital
    public void newHospital() {
       setHospital(new Hospital(this.getHospitalCode(), this.getDBConnection()));    
    }
    // new admission type
    public void newAdmissionType() {
        String Code = getDBConnection().executeQueryString("select ADMISSION_TYPE_CODE from Invoice_Header where INVOICE_NO='" + 
                        this.invoiceNo + "' and HOSPITAL_CODE = '" + this.hospitalCode + "'");
        admissionType = new AdmissionType(Code, this.getDBConnection());       
    }
    // new location
    public void newLocation() {
        String Code = getDBConnection().executeQueryString("select LOCATION_CODE from Invoice_Header where INVOICE_NO='" + 
                        this.getInvoiceNo() + "' and HOSPITAL_CODE = '" + this.getHospitalCode() + "'");
        location = new Location(Code, this.hospitalCode, this.getDBConnection());    
    }
    // new payor office
    public void newPayorOffice() {
        String Code = getDBConnection().executeQueryString("select Payor_Office_Code from Invoice_Header where INVOICE_NO='" + 
                        this.getInvoiceNo() + "' and HOSPITAL_CODE = '" + this.getHospitalCode() + "'");
        payorOffice = new PayorOffice(Code, this.getHospitalCode(), this.getDBConnection());    
    }

    // get and set
    public InvoiceHeader getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(InvoiceHeader invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public AdmissionType getAdmissionType() {
        return admissionType;
    }

    public void setAdmissionType(AdmissionType admissionType) {
        this.admissionType = admissionType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PayorOffice getPayorOffice() {
        return payorOffice;
    }

    public void setPayorOffice(PayorOffice payorOffice) {
        this.payorOffice = payorOffice;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }


}
