package df.bean.obj.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JDate {
    private static final int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    public JDate() {
    }
/*    static public Date getSystemDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        java.util.Date date = new java.util.Date();
//        System.out.println("Current Date Time : " + dateFormat.format(date));
        return date;
    }*/
    
    static public String getDay() {
        //Date now = new Date(System.currentTimeMillis());
        Calendar rightNow = Calendar.getInstance();
        //SystemDate
        //String strDay = Integer.toString(now.getDate());
        String strDay = Integer.toString(rightNow.get(Calendar.DAY_OF_MONTH));
        if (strDay.length() == 1) {
            strDay = "0" + strDay;
        }
        return strDay;
    }
    
    static public String getMonth() {
//        Date now = new Date(System.currentTimeMillis());
        Calendar rightNow = Calendar.getInstance();
//        String strMonth = Integer.toString(now.getMonth()+1);
        String strMonth = Integer.toString(rightNow.get(Calendar.MONTH)+1);
        if (strMonth.length() == 1) {
            strMonth = "0" + strMonth;
        }
        //System.out.println(strMonth);
        return strMonth;
    }
    
    static public String getYear() {
        //Date now = new Date(System.currentTimeMillis());
        Calendar rightNow = Calendar.getInstance();
        //String strYear = Integer.toString(now.getYear()+1900);
        String strYear = "";
        if (rightNow.get(Calendar.YEAR) >= 2550) { 
            strYear = Integer.toString(rightNow.get(Calendar.YEAR) - 543); 
        } else {
            strYear = Integer.toString(rightNow.get(Calendar.YEAR));
        }
        return strYear;
    }

    static public String getHour() {
        //Date now = new Date(System.currentTimeMillis());
        Calendar rightNow = Calendar.getInstance();
        //String strHour = Integer.toString(now.getHours());
        String strHour = Integer.toString(rightNow.get(Calendar.HOUR_OF_DAY));   
        if (strHour.length() == 1) { strHour = "0" + strHour; }
        return strHour;
    }

    static public String getMinutes() {
        //Date now = new Date(System.currentTimeMillis());
        Calendar rightNow = Calendar.getInstance();
        //String strMinutes = Integer.toString(now.getMinutes());
        String strMinutes = Integer.toString(rightNow.get(Calendar.MINUTE)); 
        if (strMinutes.length() == 1) { strMinutes = "0" + strMinutes; }
        return strMinutes;
    }

    static public String getSeconds() {
        //Date now = new Date(System.currentTimeMillis());
        Calendar rightNow = Calendar.getInstance();
        //String strSeconds = Integer.toString(now.getSeconds());
        String strSeconds = Integer.toString(rightNow.get(Calendar.SECOND));
        if (strSeconds.length() == 1) { strSeconds = "0" + strSeconds; }
        return strSeconds;
    }

    static public String getDate() {
        return getYear() + getMonth() + getDay();
    }
    
    static public String getDateDDMMYYY() {
        return getDay() + getMonth() + getYear();
    }

    static public String getTime() {
        return getHour() + "" + getMinutes() + "" + getSeconds();
    }

    static public long getTimeInMillis() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.getTimeInMillis();
    }

    static public String saveDate(String date) {
        String updateDate = "";
        String dd, mm, yyyy;
        try {
            if (date == null ||date.equalsIgnoreCase("")) return "";
            dd = date.substring(0, date.indexOf("/"));
            date = date.substring(date.indexOf("/") + 1);
            mm = date.substring(0, date.indexOf("/"));
            yyyy = date.substring(date.indexOf("/") + 1);
            //String a = String.format("%1$2d", mm);
            updateDate = yyyy + String.format("%1$02d", Integer.parseInt(mm)) + String.format("%1$02d", Integer.parseInt(dd));
            //return date.substring(6, 10) + date.substring(3, 5) + date.substring(0, 2);
        }
        catch (Exception e){
        	System.out.println("saveDate Method error : "+e+" : date = "+date);
            //e.printStackTrace();
        }
        return updateDate;
    }

    static public String showDate(String date) {
        String updateDate = "";
        try {
            if (date.equals("")) return "";
            updateDate += date.substring(6,8)+"/";
            updateDate += date.substring(4,6)+"/";
            updateDate += date.substring(0,4);
            //System.out.println(updateDate);
        }
        catch(Exception e){
            e.printStackTrace();
        }
            return updateDate;
    }

    static public String saveTime(String time) {
        String uptimeTime = "";
        String hh, mm, ss;
        try {
            if (time.equals("")) return "";
            hh = time.substring(0, time.indexOf(":"));
            time = time.substring(time.indexOf(":") + 1);
            mm = time.indexOf(":") != -1 ? time.substring(0, time.indexOf(":")) : time.substring(0);
            ss = time.indexOf(":") == -1 ? "00" : time.substring(time.indexOf(":") + 1);
            //String a = String.format("%1$2d", mm);
            uptimeTime = String.format("%1$02d", Integer.parseInt(hh)) + String.format("%1$02d", Integer.parseInt(mm)) + String.format("%1$02d", Integer.parseInt(ss));
            //return time.substring(6, 10) + time.substring(3, 5) + time.substring(0, 2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return uptimeTime;
    }
    
    static public String showTime(String time) {
        String updateTime = "";
        try{
            if (time.equals("")) return "";
            updateTime += time.substring(0,2) + ":";
            updateTime += time.substring(2,4) + ":";
            updateTime += time.substring(4,6);
        }catch(Exception e){
            e.printStackTrace();
        }
        return updateTime;
    }
    
    static public String saveTimeNOColon(String time) {
        String uptimeTime = "";
        String hh, mm, ss;
        try {
            if(time.length()==8){
                hh = time.substring(0,2);
                mm = time.substring(3,5);
                return hh + "" + mm +"00";
            }else if(time.length() == 4){
                hh = time.substring(0,2);
                mm = time.substring(2,4);
                return hh + "" + mm +"00";
            }else{
                return time;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return uptimeTime;
    }

    static public boolean IsWorkingDay(String YYYY, String MM, String DD) {
        boolean ret = true;
        int iYear = Integer.parseInt(YYYY);
        if (iYear < 2500) { iYear = iYear + 543;  }

        Calendar date = Calendar.getInstance();
        int dow = date.get(Calendar.DAY_OF_WEEK);
        date.clear();
        date.set(iYear, Integer.parseInt(MM)-1, Integer.parseInt(DD));
        
        dow = date.get(Calendar.DAY_OF_WEEK);
        String aaa = date.getTime().toString();
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { ret = false; }
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { ret = false; }
        return ret;
    }
    
    private boolean isLeapYear(int y) {
        if (y % 400 == 0) return true;
        if (y % 100 == 0) return false;
        return (y % 4 == 0);
    }

    static public String getEndMonthDate(String year, String month){
    	int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    	String t="";
    	Double d=0.00;
    	String[] tt = null;
    	d = Double.parseDouble(year)/4;
    	tt = d.toString().split("[.]");
    	if(tt[1].equals("0")){
    		if(month.equals("02")){
    			t = "29";
    			//System.out.println("JDate="+t);
    		}else{
        		t = ""+DAYS[Integer.parseInt(month)];
    		}
    	}else{
    		if(month.equals("02")){
    			t = "28";
    			//System.out.println("JDate="+t);
    		}else{
        		t = ""+DAYS[Integer.parseInt(month)];
    		}
    	}
    	return t;
    }

    static public String getDiffDay(String from_date, String to_date, String from_time, String to_time){
    	String day_num = "";
    	String year_from = from_date.substring(0, 4);
    	String year_to = to_date.substring(0, 4);;
    	String month_from = from_date.substring(4, 6);
    	String month_to = to_date.substring(4, 6);
    	String date_from = from_date.substring(6, 8);
    	String date_to = to_date.substring(6, 8);
    	String hour_from = from_time.substring(0, 2);
    	String hour_to = to_time.substring(0, 2);
    	String min_from = from_time.substring(2, 4);
    	String min_to = to_time.substring(2, 4);
    	
    	Date day_from = new GregorianCalendar(2009, 11, 01, 07, 00).getTime();
	    Date day_to = new GregorianCalendar(2009, 11, 02, 17, 00).getTime();
	    //Date today = new Date();

	    long diff = day_to.getTime() - day_from.getTime();
	    day_num = (diff / (1000 * 60 * 60 * 24))+":"+((diff / (1000 * 60 * 60))-((diff / (1000 * 60 * 60 * 24))*24) );
	    
	    //System.out.println(""+(diff / (1000 * 60 * 60 * 24))*24+" : "+(diff / (1000 * 60 * 60))+" : "
	    //    + (diff / (1000 * 60 * 60 * 24)) + " days old. and "+((diff / (1000 * 60 * 60))-((diff / (1000 * 60 * 60 * 24))*24) )+" Hours.");
    	return day_num;
    }

    static public double getDiffTimes(String from_date, String to_date, String from_time, String to_time){
    	if(from_date.length()>8){
    		from_date = saveDate(from_date);
    	}
    	if(to_date.length()>8){
    		to_date = saveDate(to_date);
    	}
    	if(from_time.length()>6){
    		from_time = saveTimeNOColon(from_time);
    	}
    	if(to_time.length()>6){
    		to_time = saveTimeNOColon(to_time);
    	}
    	int year_from = Integer.parseInt(from_date.substring(0, 4));
    	int year_to = Integer.parseInt(to_date.substring(0, 4));
    	int month_from = Integer.parseInt(from_date.substring(4, 6));
    	int month_to = Integer.parseInt(to_date.substring(4, 6));
    	int date_from = Integer.parseInt(from_date.substring(6, 8));
    	int date_to = Integer.parseInt(to_date.substring(6, 8));
    	int hour_from = Integer.parseInt(from_time.substring(0, 2));
    	int hour_to = Integer.parseInt(to_time.substring(0, 2));
    	int min_from = Integer.parseInt(from_time.substring(2, 4));
    	int min_to = Integer.parseInt(to_time.substring(2, 4));
    	long different_time = 0;
    	if(month_from != month_to){
        	Date day_from1 = new GregorianCalendar(year_from, month_from, date_from, hour_from, min_from).getTime();
    	    Date day_to1 = new GregorianCalendar(year_from, month_from, date_from, 24, 00).getTime();
        	Date day_from2 = new GregorianCalendar(year_to, month_to, date_to, 00, 00).getTime();
    	    Date day_to2 = new GregorianCalendar(year_to, month_to, date_to, hour_to, min_to).getTime();
    	    long dif_from1 = day_to1.getTime() - day_from1.getTime();
    	    long dif_from2 = day_to2.getTime() - day_from2.getTime();
    	    different_time = dif_from1+dif_from2;
    	}else{
        	Date day_from = new GregorianCalendar(year_from, month_from, date_from, hour_from, min_from).getTime();
    	    Date day_to = new GregorianCalendar(year_to, month_to, date_to, hour_to, min_to).getTime();
    	    different_time = day_to.getTime() - day_from.getTime();
    	}
    	
	    /** Today's date */
	    double num_hour = 0.0;
	    //String hour = ""+(different_time / (1000 * 60 * 60));
	    //String min = ""+((different_time / (1000 * 60))%60);
	    String text_hour = "";
	    
	    text_hour = (different_time / (1000 * 60 * 60))+"."+((different_time / (1000 * 60))%60 * 100 /60);
	    num_hour = Double.parseDouble(text_hour);
	    
	    System.out.println(text_hour+" : "+num_hour);
	    //System.out.println(day_from.getDay()+"<>"+day_to.getDay());
	    System.out.println(different_time / (1000 * 60 * 60)+".."+(different_time / (1000 * 60))%60 * 100 /60+" , "+different_time);
	    return num_hour;
    }
    
    static public String[] setPriviousBatch(String mm, String yy){
        String month = "";
        String year = "";
        if(mm.equals("01")){
        	month = "12";
        	year = ""+(Integer.parseInt(yy)-1); 
        }else{
        	month = ""+(Integer.parseInt(mm)-1);
                if(month.length()==1){
                	month = "0"+month;
                }
                year = yy;
        }
        String ret[] = new String[2];
        ret[0] = month;
        ret[1] = year;
        return ret;
    }

    static public String[] getNextBatch(String mm, String yy){
        String month = "";
        String year = "";
        if(mm.equals("12")){
        	month = "01";
        	year = ""+(Integer.parseInt(yy)+1); 
        }else{
        	month = ""+(Integer.parseInt(mm)+1);
                if(month.length()==1){
                	month = "0"+month;
                }
                year = yy;
        }
        String ret[] = new String[2];
        ret[0] = month;
        ret[1] = year;
        return ret;
    }
    static public String getPreviousBatch(String mm, String yy){
        String month = "";
        String year = "";
        if(mm.equals("01")){
        	month = "12";
        	year = ""+(Integer.parseInt(yy)-1); 
        }else{
        	month = ""+(Integer.parseInt(mm)-1);
                if(month.length()==1){
                	month = "0"+month;
                }
                year = yy;
        }
        String ret[] = new String[2];
        ret[0] = month;
        ret[1] = year;
        System.out.println(year+month);
        return year+month;
    }

    static public String getThaiYear(String year) {
        String strYear = "";
        if (year.length()!=4) {
            strYear = year; 
        } else {
            strYear = ""+(Integer.parseInt(year)+543);
        }
        return strYear;
    }
	static public double GetDiffMonth(double startMonth, double startYear, double endMonth, double endYear){
		double totalNum=0;
    	if(startYear==endYear){
        	totalNum=(endMonth-startMonth)+1;
        }else{
        	totalNum = (endYear-startYear)-1 > 1 ? ((endYear-startYear)-1)*12 : 0 ;
        	totalNum = totalNum+((12-startMonth)+1)+endMonth;
        }
    	return totalNum;
	}
    static public String getYearOfNextMonth(String mm, String yy){
        String month = "";
        String year = "";
        if(mm.equals("12")){
        	month = "01";
        	year = ""+(Integer.parseInt(yy)+1); 
        }else{
        	month = ""+(Integer.parseInt(mm)+1);
                if(month.length()==1){
                	month = "0"+month;
                }
                year = yy;
        }
        return year;
    }

    static public String getNextMonth(String mm, String yy){
        String month = "";
        String year = "";
        if(mm.equals("12")){
        	month = "01";
        	year = ""+(Integer.parseInt(yy)+1); 
        }else{
        	month = ""+(Integer.parseInt(mm)+1);
                if(month.length()==1){
                	month = "0"+month;
                }
                year = yy;
        }
        return month;
    }
    
    static public String setNotMatchDay(String yyyymmdd){
    	String ret = "";
    	if(yyyymmdd.length()!= 8){
    		ret = "";
    	}else{
        	int date=Integer.parseInt(yyyymmdd.substring(6,8));
        	if(date<15)date=14;
        	if(date>15)date=16;
        	ret = yyyymmdd.substring(0,6)+String.valueOf(date);
    	}
    	return ret;
    }
    
    static public String setTimeIncrement(String date,String hhmmss,int diff,String is_increment){
    DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String strdate = null;
    
    	try {
			Date parsed = df.parse(date+hhmmss);
			  Calendar calendar = GregorianCalendar.getInstance(); 
			  calendar.setTime(parsed); 
    		if(is_increment.equals("Y")){
    			calendar.add(Calendar.MINUTE,(diff));
	    	}else{
	    		calendar.add(Calendar.MINUTE,-(diff));
	    	}
    		 
			 strdate = sdf.format(calendar.getTime());
    		 } catch (ParseException e) {
					e.printStackTrace();
			}
    	return strdate;
    }
    static public String saveTaxDate(String date) {
        String updateDate = "";
        String dd, mm, yyyy;
        try {
            //updateDate = yyyy +"-"+ String.format("%1$02d", Integer.parseInt(mm)) +"-"+ String.format("%1$02d", Integer.parseInt(dd));
            updateDate = (Integer.parseInt(date.substring(4, 8))+543) +"-"+ date.substring(2, 4) +"-"+ date.substring(0, 2);
        }
        catch (Exception e){
        	System.out.println("saveDate Method error : "+e+" : date = "+date);
            //e.printStackTrace();
        }
        return updateDate;
    }

    public static void main(String arg[]){
    	//String date=setTimeIncrement("20110229", "000000", 30, "Y");
    	//System.out.println(""+getDiffMonth(2013,03,2014,12));
    }
}