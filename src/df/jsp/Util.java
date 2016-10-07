/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.jsp;

public class Util {
    
    public static String toOptionList(String [] value, String [] text, String selected) {
        String options = "";
        if (value != null && text != null && value.length == text.length) {
            for (int i = value.length - 1; i >= 0; i--) {
                if (value[i].equalsIgnoreCase(selected)) {
                    options = "<option value=\"" + value[i] + "\" selected=\"selected\">" + text[i] + "</option>" + options;
                }
                else {
                    options = "<option value=\"" + value[i] + "\">" + text[i] + "</option>" + options;
                }
            }
        }
        return options;
    }
    
    public static String formatHTMLString(String str, boolean beReplaceNullWithNBSP) {
        if (str == null || str.equalsIgnoreCase("null")) {
            if (beReplaceNullWithNBSP) {
                return "&nbsp;";
            }
            else {
                return "";
            }
        }
        str = str.replace("&", "&amp;");
        str = str.replace("<", "&lt;");
        str = str.replace(">", "&gt;");
        return str;
    }
}
