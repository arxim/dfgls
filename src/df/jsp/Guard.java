/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.jsp;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;

/**
 *
 * @author Pong
 */
public class Guard {
    
    public static String PERMISSION_ALL = new BigInteger("2").pow(300).subtract(new BigInteger("1")).toString(2);
    //
    // Permission for web page in root folder [0 - 9]
    //
    public static short PAGE_ROOT = 0;
    
    //
    // Permission for web page in input module [10 - 99]
    //
    public static short PAGE_INPUT_BANK = 1;
    public static short PAGE_INPUT_BANK_BRANCH = 2;
    public static short PAGE_INPUT_METHOD_ALLOC_MASTER_MAIN = 3;
    public static short PAGE_INPUT_METHOD_ALLOC_MASTER_DETAIL = 4;
    public static short PAGE_INPUT_METHOD_ALLOC_ITEM_MAIN =5;
    public static short PAGE_INPUT_METHOD_ALLOC_ITEM_DETAIL = 6;
    public static short PAGE_INPUT_METHOD_ALLOC_PERSONAL_MAIN = 7;
    public static short PAGE_INPUT_METHOD_ALLOC_PERSONAL_DETAIL = 8;
    public static short PAGE_INPUT_ACCOUNT = 9;
    public static short PAGE_INPUT_ADMISSION_TYPE = 10;
    public static short PAGE_INPUT_DEPARTMENT = 11;
    public static short PAGE_INPUT_DOCTOR_CATEGORY = 12;
    public static short PAGE_INPUT_ORDER_ITEM = 14;
    public static short PAGE_INPUT_TAX_TYPE = 15;
    public static short PAGE_INPUT_GUARANTEE_TYPE = 16;
    public static short PAGE_INPUT_RECEIPT_MODE = 17;
    public static short PAGE_INPUT_RECEIPT_TYPE = 18;
    public static short PAGE_INPUT_PAYOR_OFFICE = 19;
    public static short PAGE_INPUT_DOCTOR_PROFILE = 20;
    public static short PAGE_INPUT_DOCTOR = 21;
    public static short PAGE_INPUT_RECEIPT = 22;
    public static short PAGE_INPUT_GUARANTEE_MAIN = 23;
    public static short PAGE_INPUT_GUARANTEE_DETAIL = 24;
    public static short PAGE_INPUT_USER_GROUP = 25;    public static short PAGE_INPUT_EXPENSE = 13;

    public static short PAGE_INPUT_ORDER_ITEM_CATEGORY = 26;
    public static short PAGE_INPUT_DOCTOR_TYPE = 27;
    public static short PAGE_INPUT_EXPENSE_PERIOD = 28;
    public static short PAGE_INPUT_DISTRIBUTE_REVENUE = 29;
    public static short PAGE_INPUT_GROUP_DOCTOR_CATEGORY = 30;
    public static short PAGE_INPUT_GROUP_ORDER_ITEM_CATEGORY = 31;
    public static short PAGE_INPUT_GROUP = 32;
    public static short PAGE_INPUT_LOCATION = 33;
    public static short PAGE_INPUT_TAX_METHOD = 34;

    //
    // Permission for web page in process module [100 - 199]
    //
    public static short PAGE_PROCESS_DEMO = 41;
    public static short PAGE_PROCESS_PREPARE_GUARANTEE = 42;

    //
    // Permission for web page in report module [200 - 299]
    //
    public static short PAGE_REPORT_DEMO = 61;
    
    // Permission max = 299

    public static boolean checkPermission(HttpSession session, short page) {
        if (session.getAttribute("PERMISSION") == null) {
            return false;
        }
        
        if (session.getAttribute("PERMISSION").toString().charAt(page) == '0')
            return false;
        return true;
        //BigInteger userPermission = new BigInteger(session.getAttribute("PERMISSION").toString(), 2);
        //BigInteger pagePermission = new BigInteger("2").pow(page);
        //return userPermission.and(pagePermission).equals(pagePermission);
    }

}
