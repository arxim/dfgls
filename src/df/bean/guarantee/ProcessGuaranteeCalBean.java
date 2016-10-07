package df.bean.guarantee;

import df.bean.db.conn.DBConn;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
import df.bean.obj.util.JNumber;

public class ProcessGuaranteeCalBean {
    DBConn cdb;
    boolean status_guarantee = true;
	String hospitalCode, year, month, term, result, is_paid_allocate_guarantee, hospital_code, user_id, guarantee_note;
    String[][] g_setup = null;
    double dr_amt = 0.00;
    double hp_amt = 0.00;
    double tax_amt = 0.00;
    double sum_trn_guarantee_amt = 0.00;
    double sum_trn_guarantee_balance = 0.00;
    double sum_amount_aft_discount = 0.00;
    double trn_guarantee_amt = 0.00;
    double trn_guarantee_paid_amt = 0.00;
    double percent_in_allocate = 0.00;
    double percent_over_allocate = 0.00;
    double amount_allocate = 0.00;
    double guarantee_balance = 0.00;
    double guarantee_paid = 0.00; //ยอดรวมเงินที่ทำจ่ายสำหรับกรณีค่าแพทย์การันตี
    double guarantee_amt = 0.00;
    
	public ProcessGuaranteeCalBean(String hospitalCode, String year, String month, String term){
		this.hospitalCode = hospitalCode;
		this.year = year;
		this.month = month;
		this.term = term;
		this.cdb = new DBConn();
	}
	public boolean doProcess(String process){
		return true;
	}
    private void inGuaranteeAllocate(int g_index, int t_index, String[][] g, String[][] t){
    	double amount = Double.parseDouble(t[t_index][22]);
    	double temp = 0.00;
    	
    	if(this.is_paid_allocate_guarantee.equals("Y")){//เทียบการันตีแล้วค่อยคิดส่วนแบ่ง (BMC)
            this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100);
    		this.guarantee_paid = this.guarantee_paid + this.dr_amt;
    		this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.trn_guarantee_amt;
    		this.guarantee_balance = this.guarantee_balance - (this.trn_guarantee_amt+temp) < 0 ? 0 : this.guarantee_balance - this.trn_guarantee_amt;
            this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
            this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee
        }else{//เทียบการันตีโดยหลังหักส่วนแบ่งต้องไม่ต่ำกว่าการันตี
        	if(this.sum_amount_aft_discount <= this.guarantee_amt){
        		//ถ้าจำนวนเงินก่อนแบ่งรวม น้อยกว่าจำนวนเงินการันตีจะทำการจ่ายเต็ม 100% ไม่ต้องหักส่วนแบ่ง
        		this.dr_amt = amount;
        	}else if(this.sum_trn_guarantee_amt > this.guarantee_amt){
        		//ถ้าจำนวนเงินสำหรับเทียบการันตีรวม มากกว่า จำนวนเงินการันตี
        		this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100);
        		double balance_diff = 0;
        		double balance_temp = this.sum_trn_guarantee_balance - this.trn_guarantee_amt+(this.trn_guarantee_amt * (this.percent_in_allocate/100))+this.guarantee_paid;
        		if(balance_temp < this.guarantee_amt){
        			balance_diff = this.guarantee_amt - balance_temp;
        			this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100)+balance_diff;
        		}
        	}else if(this.sum_trn_guarantee_amt == this.guarantee_amt){
        		//ถ้าจำนวนเงินสำหรับเทียบการันตีรวม เท่ากับ จำนวนเงินการันตี
        		this.dr_amt = this.trn_guarantee_amt;
        	}else if(this.sum_trn_guarantee_amt < this.guarantee_amt){
        		//ถ้าจำนวนเงินสำหรับเทียบการันตีรวม น้อยกว่า จำนวนเงินการันตี
        		temp = amount - this.trn_guarantee_amt;
        		if(this.sum_trn_guarantee_amt+temp > this.guarantee_amt){
        			temp = this.guarantee_amt-this.sum_trn_guarantee_amt;
        		}
        		this.sum_trn_guarantee_amt = this.sum_trn_guarantee_amt+temp;
        		this.dr_amt = this.trn_guarantee_amt+temp;
        	}else{ /*not implement*/ }
        	
    		this.guarantee_paid = this.guarantee_paid + this.dr_amt;
    		this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.trn_guarantee_amt;
    		//หากจำนวนเงินในการันตีไม่ถึงการันตีจะไม่หักส่วนแบ่งโรงพยาบาลดังนั้น จำนวนเงินการันตีคงเหลือจะต้องหักจากจำนวนเงินแพทย์ แทนจำนวนเงินที่นำมาเทียบการันตี 
    		this.guarantee_balance = this.guarantee_balance - this.dr_amt < 0 ? 0 : this.guarantee_balance - this.dr_amt;
            this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
            this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee
        }
        if(t[t_index][21].toString().equals("Y")){
            this.tax_amt = this.dr_amt;
        }else{
        	this.tax_amt = amount;
        }
    }
    private void inAndOverGuaranteeAllocate(int g_index, int t_index, String[][] g, String[][] t){
    	double amount = Double.parseDouble(t[t_index][22]); //AMOUNT_AFT_DISCOUNT
    	double amountAfterDiscount = Double.parseDouble(t[t_index][22]); //AMOUNT_AFT_DISCOUNT
        double trn_in_guarantee_amount = 0;
        double over_guarantee_amount = 0;
        double temp_amount = 0;
        this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee

    	if(this.is_paid_allocate_guarantee.equals("Y")){
    		//เทียบการันตีแล้วค่อยคิดส่วนแบ่ง (BGH)
            if(this.guarantee_balance == 0){
                this.dr_amt = this.trn_guarantee_amt * (this.percent_over_allocate/100);
            	if(!t[t_index][5].equals("")){
            		this.guarantee_note = "OVER GUARANTEE "+t[t_index][16]+" to "+this.percent_over_allocate;
            	}else{
            		this.guarantee_note = "";
            	}
            }
            if(this.guarantee_balance > 0 && this.guarantee_balance < this.trn_guarantee_amt){
            	trn_in_guarantee_amount = this.guarantee_balance * (percent_in_allocate /100);
                over_guarantee_amount = (this.trn_guarantee_amt - this.guarantee_balance) * (percent_over_allocate/100);
            	if(!t[t_index][5].equals("")){
            		//if Receipt transaction
                    this.dr_amt = trn_in_guarantee_amount+over_guarantee_amount;
            		this.guarantee_note = "IN/OVER GUARANTEE="+JNumber.getSaveMoney(trn_in_guarantee_amount)+"/"+JNumber.getSaveMoney(over_guarantee_amount);
            	}else{
            		//if Invoice transaction
                	if(this.guarantee_balance <= 0){
                		this.guarantee_note = "";
                	}else{
                		this.guarantee_note = "ABSORB SOME GUARANTEE";
                		this.dr_amt = over_guarantee_amount;
                		this.trn_guarantee_paid_amt = trn_in_guarantee_amount;
                		//this.dr_amt = (this.dr_amt - this.guarantee_balance);
                		//this.trn_guarantee_paid_amt = this.guarantee_balance;
                	}
            	}
            }
        	this.guarantee_balance = 0;
    	}else{
        	//เทียบการันตีโดยหลังหักส่วนแบ่งต้องไม่ต่ำกว่าการันตี (BNH)
            trn_in_guarantee_amount = this.guarantee_balance * (percent_in_allocate /100);
            over_guarantee_amount = (this.trn_guarantee_amt - this.guarantee_balance) * (percent_over_allocate/100);
            this.dr_amt = trn_in_guarantee_amount+over_guarantee_amount;
            temp_amount = this.dr_amt;
        	if(!t[t_index][5].equals("")){
        	//if Receipt transaction
        		this.guarantee_note = "IN/OVER GUARANTEE="+JNumber.getSaveMoney(trn_in_guarantee_amount)+"/"+JNumber.getSaveMoney(over_guarantee_amount);
        	}else{
        	//if Invoice transaction
            	if(this.guarantee_paid+this.dr_amt <= this.guarantee_amt){
            		double balance_diff = 0;
            		double balance_temp = this.sum_trn_guarantee_balance - this.trn_guarantee_amt+(this.trn_guarantee_amt * (this.percent_in_allocate/100))+this.guarantee_paid;
            		//System.out.println(this.guarantee_paid+"<>"+this.dr_amt+"<>"+this.guarantee_balance+"<>"+this.trn_guarantee_amt);
            		if(balance_temp < this.guarantee_amt){
            			balance_diff = this.guarantee_amt - balance_temp;
            			this.dr_amt = this.trn_guarantee_amt * (this.percent_in_allocate/100)+balance_diff;
            			temp_amount = this.dr_amt;
            		}
            		this.guarantee_note = "ABSORB GUARANTEE IN/OVER";
            	}else{
            		this.guarantee_note = "ABSORB SOME GUARANTEE";
            		this.dr_amt = (this.dr_amt - this.guarantee_balance);
            		this.trn_guarantee_paid_amt = this.guarantee_balance;
            		temp_amount = this.guarantee_balance;
            		amount = amount - this.guarantee_balance;
            	}
        	}
    	}
        if(t[t_index][21].toString().equals("Y")){//if tax from after allocate
            this.tax_amt = this.dr_amt;        		
    	}else{//else tax from before allocate
    		//this.tax_amt = amount;
        	this.tax_amt = this.guarantee_note.equals("ABSORB SOME GUARANTEE")? amountAfterDiscount - this.trn_guarantee_paid_amt : amountAfterDiscount;
        }
		this.guarantee_paid = this.guarantee_paid + temp_amount;
        this.guarantee_balance = 0;
        //this.tax_amt = this.guarantee_note.equals("ABSORB SOME GUARANTEE")? this.tax_amt - this.trn_guarantee_paid_amt : this.tax_amt;
        this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
        this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.trn_guarantee_amt;
    }
    private void overGuaranteeAllocate(int g_index, int t_index, String[][] g, String[][] t){
    	double amount = Double.parseDouble(t[t_index][22]); //AMOUNT_AFT_DISCOUNT
    	double amountAfterDiscount = Double.parseDouble(t[t_index][22]); //AMOUNT_AFT_DISCOUNT
        this.trn_guarantee_paid_amt = 0; //guarantee_paid_amt for Absorb some Guarantee
        this.dr_amt = this.trn_guarantee_amt * (this.percent_over_allocate/100);

        //Method นี้ใช้สำหรับการคิดแบบ (BNH) เท่านั้น
		double balance_diff = 0;
		double balance_temp = this.sum_trn_guarantee_balance - this.trn_guarantee_amt+(this.trn_guarantee_amt * (this.percent_over_allocate/100))+this.guarantee_paid;
		if(balance_temp < this.guarantee_amt){
			balance_diff = this.guarantee_amt - balance_temp;
			this.dr_amt = this.trn_guarantee_amt * (this.percent_over_allocate/100)+balance_diff;
			this.guarantee_note = t[t_index][5].equals("") ? "ABSORB GUARANTEE OVER" : "OVER GUARANTEE";
		}else if(this.guarantee_paid+this.dr_amt > this.guarantee_amt && this.guarantee_paid < this.guarantee_amt){
			if(!t[t_index][5].equals("")){//if Receipt transaction
            		this.guarantee_note = "OVER GUARANTEE";
            }else{
	    		this.dr_amt = this.dr_amt - (this.guarantee_amt - this.guarantee_paid);
	    		this.trn_guarantee_paid_amt = this.guarantee_amt - this.guarantee_paid;
	    		this.guarantee_note = "ABSORB SOME GUARANTEE";
            }
    	}else if(this.guarantee_paid+this.dr_amt < this.guarantee_amt && this.guarantee_paid < this.guarantee_amt){
    		this.guarantee_note = t[t_index][5].equals("") ? "ABSORB GUARANTEE OVER" : "OVER GUARANTEE";
    	}else{
    		if(!t[t_index][5].equals("")){//if Receipt transaction
        		this.guarantee_note = "OVER GUARANTEE";
	        }else{
	    		this.guarantee_note = "";
	        }
    	}
        if(t[t_index][21].toString().equals("Y")){//if tax from after allocate
            this.tax_amt = this.dr_amt;        		
    	}else{//else tax from before allocate
    		//this.tax_amt = amount;
        	this.tax_amt = this.guarantee_note.equals("ABSORB SOME GUARANTEE")? amountAfterDiscount - this.trn_guarantee_paid_amt : amountAfterDiscount;
        }
		this.guarantee_paid = this.guarantee_note.equals("ABSORB SOME GUARANTEE")? this.guarantee_paid + this.trn_guarantee_paid_amt : this.guarantee_paid + this.dr_amt;
    	this.guarantee_balance = 0;
        this.hp_amt = amount - this.dr_amt < 0 ? 0 : amount - this.dr_amt;
        this.sum_trn_guarantee_balance = this.sum_trn_guarantee_balance - this.trn_guarantee_amt;
    }
}
