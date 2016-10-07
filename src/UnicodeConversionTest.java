class UnicodeConversionTest 
{
    public static void main(String[] args) 
    {
   try {
     String str = new String("`test3`");
     String utfStr = new String(str.getBytes("UTF-8"), "UTF-8");
     System.out.println("Converted:" + str + " to:" + utfStr);
   } catch (Exception e) {
       e.printStackTrace(System.out);
     }
    }
}
