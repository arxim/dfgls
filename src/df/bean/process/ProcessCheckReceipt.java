package df.bean.process;

import df.bean.frame.*;
import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.receipt.Receipt;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;

public class ProcessCheckReceipt extends Process {
    String hospitalCode = "";
    String invoiceNo = "";
    public boolean success = false;
    
    public ProcessCheckReceipt(DBConnection conn) {
        super(conn);
        title = "Check data of Receipt";
        this.setDBConnection(conn);
    }
    
    protected void finalize() {
//        calculatorFrm = null;
    }
    
    public void run() {
        if (this.getDBConnection().IsClosed()) {  
            if (!this.getDBConnection().Connect()) { DialogBox.ShowError(" �������ö�Դ��͡Ѻ�ҹ�������� "); return ;}
        }
        
        // Add to list
        List values = new ArrayList();
        values.add(JDate.getDate());
        values.add(JDate.getTime());
        values.add("------------- Start --------------");
//        this.calculatorFrm.setTableValue(values);
        
        success = true;
        this.Calculate();
        if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
        /*
        if (this.calculatorFrm.lstDoctorGroupCode.getSelectedIndex() == this.calculatorFrm.lstDoctorGroupCode.getModel().getSize()-1) {
            if (success) {
//                this.getDBConnection().commitTrans();
//                if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
                DialogBox.ShowInfo("        ��÷ӧҹ�����z�ó� !!!!       ");
            } else {
//                this.getDBConnection().rollBackTrans();
//                if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
                DialogBox.ShowError("�Դ��ͼԴ��Ҵ ��سҵ�Ǩ�ͺ������ !!!!          " + "\n\nError in ProcessImportInvoice.Run() \n(Transaction are rollbacks)");
            }
        }
        else {
//            this.getDBConnection().rollBackTrans();
//            if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
            DialogBox.ShowWarning("��÷ӧҹ�ѧ���ú��hӹǹ !!!!          " + "\n\nWarning in ProcessImportInvoice.Run() \n(Transaction are rollbacks)");
        }
         */
//        this.calculatorFrm.stopProcess();

        values.clear();
        values.add(JDate.getDate());
        values.add(JDate.getTime());
        values.add("============== Stop ==============");
//        this.calculatorFrm.setTableValue(values);
        values = null;
    }
    
    public boolean Calculate() {   
        Receipt rr = new Receipt(this.getDBConnection());

//        rr.IsNotFoundDoctorCode (this.calculatorFrm, ((CheckReceiptFrm)this.calculatorFrm).getComputeDate());
//        rr.IsNotFoundOrderItemCode (this.calculatorFrm, ((CheckReceiptFrm)this.calculatorFrm).getComputeDate());
//        rr.IsNotFoundDepartmentCode(this.calculatorFrm, ((CheckReceiptFrm)this.calculatorFrm).getComputeDate());
//        rr.IsNotFoundReceiptTypeCode(this.calculatorFrm, ((CheckReceiptFrm)this.calculatorFrm).getComputeDate());
//        rr.IsNotFoundMedthodAllocation(this.calculatorFrm, ((CheckReceiptFrm)this.calculatorFrm).getComputeDate());
        return true;
    } 
    
    
}
