/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.obj.util;


public class FindDate {
    private int day(int M, int D, int Y) {
        int y = Y - (14 - M) / 12;
        int x = y + y/4 - y/100 + y/400;
        int m = M + 12 * ((14 - M) / 12) - 2;
        int d = (D + x + (31*m)/12) % 7;
        return d;
    }


    // return true if the given year is a leap year
    private boolean isLeapYear(int year) {
        if  ((year % 4 == 0) && (year % 100 != 0)) return true;
        if  (year % 400 == 0) return true;
        return false;
    }

    public String FindDateAll(String dd,String mm ,String yyyy){
        int M = Integer.parseInt(mm);    // month (Jan = 1, Dec = 12)
        int Y = Integer.parseInt(yyyy);    // year

        // months[i] = name of month i
        String[] months = {
             "",                               // leave empty so that months[1] = "January"
             "January", "February", "March",
             "April", "May", "June",
             "July", "August", "September",
             "October", "November", "December"
        };

        // days[i] = number of days in month i
        int[] days = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        // check for leap year
        if (M == 2 && isLeapYear(Y)) days[M] = 29;
        /*
        if(M == 2){
        	days[M] = Integer.parseInt(JDate.getEndMonthDate(""+Y, ""+M)); 
        	System.out.println(Variables.IS_TEST ? "Check FEB Month is : "+days[M] : "Not Check FEB Month" );
        }
		*/
        // starting day
        int d = day(M, 1, Y);

        String FindDateOnMonth = "";
        String[] ArrayDay = {"sun","mon","tue","wed","thu","fri","sat"};
        int CountOfDay = 1;
        for(int m = 0 ; m < ArrayDay.length ; m++){
            if(dd.equalsIgnoreCase(ArrayDay[m])) break;
            CountOfDay++;
        }

        Boolean check = false;
        for (int i = 1; i <= days[M]; i++) {
            //System.out.println(i + " : " + days[M]);
            if(((i + d) % 7 == 0) || (i == days[M])){
                if((i-(7-CountOfDay)) > 0){
                    if(((i+d) % 7)==0){
                        FindDateOnMonth += (i-(7-CountOfDay)) + "|";
                        check = true;
                    }else{
                        if(i <= days[M]){
                            if((i-(7-CountOfDay) + (7-((i+d) % 7))) <= days[M]){
                                FindDateOnMonth += (i-(7-CountOfDay) + (7-((i+d) % 7)));
                                check = false;
                            }
                        }
                    }
                }
            }
        }
        if(check){
            FindDateOnMonth = FindDateOnMonth.substring(0, FindDateOnMonth.length()-1);
        }
        return FindDateOnMonth;
    }
//    public static void main(String[] args) {
//        FindDate fn = new FindDate();
//        System.out.println(fn.FindDateAll("thu","02","2007"));
//        System.out.println(fn.FindDateAll("fri","02","2008"));
//        System.out.println(fn.FindDateAll("thu","02","2008"));
//        System.out.println(fn.FindDateAll("mon","02","2008"));
//        System.out.println(fn.FindDateAll("sun","05","2008"));
//        System.out.println(fn.FindDateAll("thu","02","2009"));
//        System.out.println(fn.FindDateAll("tue","02","2009"));
//        System.out.println(fn.FindDateAll("mon","02","2009"));
//        System.out.println(fn.FindDateAll("fri","09","2010"));
//        System.out.println(fn.FindDateAll("sat","04","2010"));
//        System.out.println(fn.FindDateAll("wed","05","2010"));
//    }
}
