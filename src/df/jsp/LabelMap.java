/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.jsp;

import java.util.HashMap;

/**
 *
 * @author Pong
 */
public class LabelMap {
    public static final int SINCE_YEAR = 2000;
    
    private HashMap mapEN = new HashMap();
    private HashMap mapTH = new HashMap();
    private HashMap mapDefault;
    
    //public static final String NULL_STRING = "-";
    
    public static final String LANG_TH = "T";
    public static final String LANG_EN = "E";
    
    public static final String LANG_TH_DESCRIPTION = "Thai";
    public static final String LANG_EN_DESCRIPTION = "English";

    protected static final String UNTITLED = "???";

    public static final String PAGE_TITLE = "PAGE_TITLE";
    protected static final String PAGE_TITLE_EN = "- Doctor Fee -";
    protected static final String PAGE_TITLE_TH = "- Doctor Fee -";

    public static final String SAVE = "SAVE";
    protected static final String SAVE_EN = "Save";
    protected static final String SAVE_TH = "บันทึก";

    public static final String RESET = "RESET";
    protected static final String RESET_EN = "Reset";
    protected static final String RESET_TH = "ล้าง";

    public static final String CLOSE = "CLOSE";
    protected static final String CLOSE_EN = "Close";
    protected static final String CLOSE_TH = "ปิด";

    public static final String EDIT = "EDIT";
    protected static final String EDIT_EN = "Edit";
    protected static final String EDIT_TH = "แก้ไข";

    public static final String UPDATE = "UPDATE";
    protected static final String UPDATE_EN = "Update";
    protected static final String UPDATE_TH = "อัพเดต";

    public static final String NEW = "NEW";
    protected static final String NEW_EN = "New";
    protected static final String NEW_TH = "เพิ่ม";

    public static final String RUN = "RUN";
    protected static final String RUN_EN = "Run";
    protected static final String RUN_TH = "ทำงาน";

    public static final String SEARCH = "SEARCH";
    protected static final String SEARCH_EN = "Search";
    protected static final String SEARCH_TH = "ค้นหา";

    public static final String SELECT = "SELECT";
    protected static final String SELECT_EN = "Select";
    protected static final String SELECT_TH = "เลือก";

    public static final String ACTIVE = "ACTIVE";
    protected static final String ACTIVE_EN = "Status";
    protected static final String ACTIVE_TH = "สถานะ";

    public static final String ACTIVE_0 = "ACTIVE_0";
    protected static final String ACTIVE_0_EN = "Inactive";
    protected static final String ACTIVE_0_TH = "ไม่ใช้งาน";

    public static final String ACTIVE_1 = "ACTIVE_1";
    protected static final String ACTIVE_1_EN = "Active";
    protected static final String ACTIVE_1_TH = "ใช้งาน";

    public static final String SEARCH_NOT_FOUND = "SEARCH_NOT_FOUND";
    protected static final String SEARCH_NOT_FOUND_EN = "Search item not found";
    protected static final String SEARCH_NOT_FOUND_TH = "ไม่พบผลลัพธ์ที่คุณค้นหา";

    public static final String MSG_NOT_HAVE_PERMISSION = "MSG_NOT_HAVE_PERMISSION";
    protected static final String MSG_NOT_HAVE_PERMISSION_EN = "You do not have permission to view this page";
    protected static final String MSG_NOT_HAVE_PERMISSION_TH = "คุณไม่สามารถเข้าใช้งานหน้านี้ได้";

    public static final String MSG_DUPLICATE_DATA = "MSG_DUPLICATE_DATA";
    protected static final String MSG_DUPLICATE_DATA_EN = "Your data was duplicated with another. <a href=\"javascript:history.back()\" title=\"Go back to edit\">Click here to go back to edit.</a>";
    protected static final String MSG_DUPLICATE_DATA_TH = "ข้อมูลของคุณซ้ำกับข้อมูลที่มีอยู่แล้ว <a href=\"javascript:history.back()\" title=\"ย้อนกลับไปแก้ไข\">คลิกที่นี่เพื่อย้อนกลับไปแก้ไข</a>";

    public static final String MSG_SAVE_SUCCESS = "MSG_SAVE_SUCCESS";
    protected static final String MSG_SAVE_SUCCESS_EN = "Your data was saved.<br /><br /><a href=\"[HREF]\" title=\"Back\">Click here to go back.</a>";
    protected static final String MSG_SAVE_SUCCESS_TH = "ข้อมูลของคุณได้รับการบันทึกแล้ว<br /><br /><a href=\"[HREF]\" title=\"ย้อนกลับ\">คลิกที่นี่เพื่อย้อนกลับไปหน้ารายการข้อมูล</a>";

    public static final String MSG_SAVE_FAIL = "MSG_SAVE_FAIL";
    protected static final String MSG_SAVE_FAIL_EN = "Error occured while saving data.<br /><br /><a href=\"javascript:history.back()\" title=\"Back\">Click here to go back.</a>";
    protected static final String MSG_SAVE_FAIL_TH = "เกิดความผิดพลาดขณะทำการบันทึกข้อมูล<br /><br /><a href=\"javascript:history.back()\" title=\"ย้อนกลับ\">คลิกที่นี่เพื่อย้อนกลับไปแก้ไข</a>";

    public static final String MSG_DATA_NOT_FOUND = "MSG_DATA_NOT_FOUND";
    protected static final String MSG_DATA_NOT_FOUND_EN = "Data not found.";
    protected static final String MSG_DATA_NOT_FOUND_TH = "ไม่พบข้อมูลที่ต้องการ";

    public static final String ALERT_DATA_NOT_FOUND = "ALERT_DATA_NOT_FOUND";
    protected static final String ALERT_DATA_NOT_FOUND_EN = "Data not found";
    protected static final String ALERT_DATA_NOT_FOUND_TH = "ไม่พบข้อมูลที่ต้องการ";

    public static final String ALERT_REQUIRED_FIELD = "ALERT_REQUIRED_FIELD";
    protected static final String ALERT_REQUIRED_FIELD_EN = "Please fill all required fields.";
    protected static final String ALERT_REQUIRED_FIELD_TH = "กรุณาป้อนข้อมูลสำคัญให้ครบถ้วน";

    public static final String ALERT_INVALID_NUMBER = "ALERT_INVALID_NUMBER";
    protected static final String ALERT_INVALID_NUMBER_EN = "Invalid number.";
    protected static final String ALERT_INVALID_NUMBER_TH = "ตัวเลขที่ป้อนมาไม่ถูกต้อง";

    public static final String ALERT_INVALID_DATE = "ALERT_INVALID_DATE";
    protected static final String ALERT_INVALID_DATE_EN = "Invalid date.";
    protected static final String ALERT_INVALID_DATE_TH = "วันที่ที่ป้อนมาไม่ถูกต้อง";

    public static final String ALERT_INVALID_TIME = "ALERT_INVALID_TIME";
    protected static final String ALERT_INVALID_TIME_EN = "Invalid time.";
    protected static final String ALERT_INVALID_TIME_TH = "เวลาที่ป้อนมาไม่ถูกต้อง";

    public static final String ALERT_INVALID_DATE_INTERVAL = "ALERT_INVALID_DATE_INTERVAL";
    protected static final String ALERT_INVALID_DATE_INTERVAL_EN = "Invalid date interval.";
    protected static final String ALERT_INVALID_DATE_INTERVAL_TH = "กำหนดช่วงวันไม่ถูกต้อง";

    public static final String ALERT_DUPLICATE_DATA = "ALERT_DUPLICATE_DATA";
    protected static final String ALERT_DUPLICATE_DATA_EN = "Duplicate data.";
    protected static final String ALERT_DUPLICATE_DATA_TH = "ข้อมูลซ้ำกับที่มีอยู่แล้ว";

    public static final String CONFIRM_REPLACE_DATA = "CONFIRM_REPLACE_DATA";
    protected static final String CONFIRM_REPLACE_DATA_EN = "Data was existed. Do you want to replace?";
    protected static final String CONFIRM_REPLACE_DATA_TH = "ข้อมูลนี้มีอยู่แล้ว ต้องการเขียนทับหรือไม่";

    public static final String CONFIRM_ADD_NEW_DATA = "CONFIRM_ADD_NEW_DATA";
    protected static final String CONFIRM_ADD_NEW_DATA_EN = "Data was not existed. Do you want to insert new?";
    protected static final String CONFIRM_ADD_NEW_DATA_TH = "ข้อมูลนี้ไม่มีอยู่ในระบบ ต้องการเพิ่มรายการใหม่หรือไม่";
    
    public LabelMap() {
        this(LabelMap.LANG_EN);
    }
    
    public LabelMap(String lang) {
        if (lang.equalsIgnoreCase(LabelMap.LANG_TH))
            this.mapDefault = this.mapTH;
        else
            this.mapDefault = this.mapEN;
        
        this.mapEN.put(LabelMap.PAGE_TITLE, LabelMap.PAGE_TITLE_EN);
        this.mapTH.put(LabelMap.PAGE_TITLE, LabelMap.PAGE_TITLE_TH);
        
        this.mapEN.put(LabelMap.SAVE, LabelMap.SAVE_EN);
        this.mapTH.put(LabelMap.SAVE, LabelMap.SAVE_TH);
        
        this.mapEN.put(LabelMap.RESET, LabelMap.RESET_EN);
        this.mapTH.put(LabelMap.RESET, LabelMap.RESET_TH);
        
        this.mapEN.put(LabelMap.CLOSE, LabelMap.CLOSE_EN);
        this.mapTH.put(LabelMap.CLOSE, LabelMap.CLOSE_TH);
        
        this.mapEN.put(LabelMap.NEW, LabelMap.NEW_EN);
        this.mapTH.put(LabelMap.NEW, LabelMap.NEW_TH);
        
        this.mapEN.put(LabelMap.EDIT, LabelMap.EDIT_EN);
        this.mapTH.put(LabelMap.EDIT, LabelMap.EDIT_TH);
        
        this.mapEN.put(LabelMap.UPDATE, LabelMap.UPDATE_EN);
        this.mapTH.put(LabelMap.UPDATE, LabelMap.UPDATE_TH);
        
        this.mapEN.put(LabelMap.RUN, LabelMap.RUN_EN);
        this.mapTH.put(LabelMap.RUN, LabelMap.RUN_TH);
        
        this.mapEN.put(LabelMap.SEARCH, LabelMap.SEARCH_EN);
        this.mapTH.put(LabelMap.SEARCH, LabelMap.SEARCH_TH);
        
        this.mapEN.put(LabelMap.SELECT, LabelMap.SELECT_EN);
        this.mapTH.put(LabelMap.SELECT, LabelMap.SELECT_TH);
        
        this.mapEN.put(LabelMap.ACTIVE, LabelMap.ACTIVE_EN);
        this.mapTH.put(LabelMap.ACTIVE, LabelMap.ACTIVE_TH);
        
        this.mapEN.put(LabelMap.ACTIVE_0, LabelMap.ACTIVE_0_EN);
        this.mapTH.put(LabelMap.ACTIVE_0, LabelMap.ACTIVE_0_TH);
        
        this.mapEN.put(LabelMap.ACTIVE_1, LabelMap.ACTIVE_1_EN);
        this.mapTH.put(LabelMap.ACTIVE_1, LabelMap.ACTIVE_1_TH);
        
        this.mapEN.put(LabelMap.SEARCH_NOT_FOUND, LabelMap.SEARCH_NOT_FOUND_EN);
        this.mapTH.put(LabelMap.SEARCH_NOT_FOUND, LabelMap.SEARCH_NOT_FOUND_TH);
        
        this.mapEN.put(LabelMap.MSG_NOT_HAVE_PERMISSION, LabelMap.MSG_NOT_HAVE_PERMISSION_EN);
        this.mapTH.put(LabelMap.MSG_NOT_HAVE_PERMISSION, LabelMap.MSG_NOT_HAVE_PERMISSION_TH);
        
        this.mapEN.put(LabelMap.MSG_DUPLICATE_DATA, LabelMap.MSG_DUPLICATE_DATA_EN);
        this.mapTH.put(LabelMap.MSG_DUPLICATE_DATA, LabelMap.MSG_DUPLICATE_DATA_TH);
        
        this.mapEN.put(LabelMap.MSG_SAVE_SUCCESS, LabelMap.MSG_SAVE_SUCCESS_EN);
        this.mapTH.put(LabelMap.MSG_SAVE_SUCCESS, LabelMap.MSG_SAVE_SUCCESS_TH);
        
        this.mapEN.put(LabelMap.MSG_SAVE_FAIL, LabelMap.MSG_SAVE_FAIL_EN);
        this.mapTH.put(LabelMap.MSG_SAVE_FAIL, LabelMap.MSG_SAVE_FAIL_TH);
        
        this.mapEN.put(LabelMap.MSG_DATA_NOT_FOUND, LabelMap.MSG_DATA_NOT_FOUND_EN);
        this.mapTH.put(LabelMap.MSG_DATA_NOT_FOUND, LabelMap.MSG_DATA_NOT_FOUND_TH);
        
        this.mapEN.put(LabelMap.ALERT_DATA_NOT_FOUND, LabelMap.ALERT_DATA_NOT_FOUND_EN);
        this.mapTH.put(LabelMap.ALERT_DATA_NOT_FOUND, LabelMap.ALERT_DATA_NOT_FOUND_TH);
        
        this.mapEN.put(LabelMap.CONFIRM_REPLACE_DATA, LabelMap.CONFIRM_REPLACE_DATA_EN);
        this.mapTH.put(LabelMap.CONFIRM_REPLACE_DATA, LabelMap.CONFIRM_REPLACE_DATA_TH);
        
        this.mapEN.put(LabelMap.CONFIRM_ADD_NEW_DATA, LabelMap.CONFIRM_ADD_NEW_DATA_EN);
        this.mapTH.put(LabelMap.CONFIRM_ADD_NEW_DATA, LabelMap.CONFIRM_ADD_NEW_DATA_TH);
        
        this.mapEN.put(LabelMap.ALERT_INVALID_NUMBER, LabelMap.ALERT_INVALID_NUMBER_EN);
        this.mapTH.put(LabelMap.ALERT_INVALID_NUMBER, LabelMap.ALERT_INVALID_NUMBER_TH);
        
        this.mapEN.put(LabelMap.ALERT_INVALID_DATE, LabelMap.ALERT_INVALID_DATE_EN);
        this.mapTH.put(LabelMap.ALERT_INVALID_DATE, LabelMap.ALERT_INVALID_DATE_TH);
        
        this.mapEN.put(LabelMap.ALERT_INVALID_TIME, LabelMap.ALERT_INVALID_TIME_EN);
        this.mapTH.put(LabelMap.ALERT_INVALID_TIME, LabelMap.ALERT_INVALID_TIME_TH);
        
        this.mapEN.put(LabelMap.ALERT_INVALID_DATE_INTERVAL, LabelMap.ALERT_INVALID_DATE_INTERVAL_EN);
        this.mapTH.put(LabelMap.ALERT_INVALID_DATE_INTERVAL, LabelMap.ALERT_INVALID_DATE_INTERVAL_TH);
        
        this.mapEN.put(LabelMap.ALERT_REQUIRED_FIELD, LabelMap.ALERT_REQUIRED_FIELD_EN);
        this.mapTH.put(LabelMap.ALERT_REQUIRED_FIELD, LabelMap.ALERT_REQUIRED_FIELD_TH);
        
        this.mapEN.put(LabelMap.ALERT_DUPLICATE_DATA, LabelMap.ALERT_DUPLICATE_DATA_EN);
        this.mapTH.put(LabelMap.ALERT_DUPLICATE_DATA, LabelMap.ALERT_DUPLICATE_DATA_TH);
        
        setDefaultLabel();
    }
    
    public void add(String field, String labelEN, String labelTH) {
        this.mapEN.put(field, labelEN);
        this.mapTH.put(field, labelTH);
    }
    
    public String get(String field) {
        if (this.mapDefault.containsKey(field)) 
            return this.mapDefault.get(field).toString();
        return LabelMap.UNTITLED;
    }
    
    public HashMap getHashMap() {
        return this.mapDefault;
    }
    
    public String getFieldLangSuffix() {
        return this.mapDefault == this.mapEN ? "ENG" : "THAI";
    }
    
    private void setDefaultLabel(){
        this.add("YEAR", "Year", "ปี");
        this.add("MONTH", "Month", "เดือน");
        this.add("BAHT", "Baht", "บาท");
        this.add("DOCTOR_PROFILE_CODE", "Doctor Profile", "รหัสแพทย์บุคคล");
        this.add("ADDRESS1", "Address No.", "เลขที่/ที่อยู่");
        this.add("ADDRESS2", "Sub District/District", "แขวง/เขต");
        this.add("ADDRESS3", "Province", "จังหวัด");
        this.add("ADMISSION_TYPE_CODE","Admission Type","แผนกรับผู้ป่วย");
        this.add("HN_NO","HN No.","เลขประจำตัวผู้ป่วย");
        this.add("PATIENT_NAME", "Patient Name", "ชื่อผู้ป่วย");
        this.add("DOCTOR_CODE","Doctor Code","รหัสแพทย์");
    }
}
