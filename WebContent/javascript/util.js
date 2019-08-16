function checkKeyTime(field){
    var value = field.value;

    if(field.value.length == 8 || field.value.length == 0){
        return true;
    }

    if(field.value.length < 4){
        alert("Invalid format time!");
        field.focus();
        return false;
    }

    for(var i=0;i < field.value.length; ++i)
    {
        var new_key = value.charAt(i); //cycle through characters
        if(((new_key < "0") || (new_key > "9")) && !(new_key == ""))
        {
            alert("Invalid format time!");
            field.focus();
            return false;
            break;
        }
    }
    var str = field.value;
    field.value = str.substr(0, 2) + ":" + str.substr(2, 2) + ":00";
    return true;
}
function getShowMoney(fieldMoney){
	return formatNumber(fieldMoney);
}
function formatCurrency(num, showDigit) {
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num)) {
        return "-";
    }
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    cents = num % 100;
    num = Math.floor(num / 100).toString();
    if(cents < 10) {
        cents = "0" + cents;
    }
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) {
        num = num.substring(0, num.length - (4 * i + 3)) + ',' + num.substring(num.length - (4 * i + 3));
    }
    
    if (showDigit) {
        return (((sign) ? '' : '-') +  num + '.' + cents);
    }
    return (((sign) ? '' : '-') +  num);
}
    
function formatHTMLString(str) {
    if (str == null || str.length == 0) {
        return "&nbsp;";
    }
    str = str.replace("&", "&amp;");
    str = str.replace("<", "&lt;");
    str = str.replace(">", "&gt;");
    return str;
}

function getXMLNodeValue(xmlDoc, nodeName, nodeID) {
    if (nodeID == null || nodeID < 0 || nodeID >= xmlDoc.getElementsByTagName(nodeName).length) {
        nodeID = 0;
    }
    if (xmlDoc == null || xmlDoc.getElementsByTagName(nodeName).length <= 0 || xmlDoc.getElementsByTagName(nodeName)[nodeID].firstChild == null) {
        return "";
    }
    return xmlDoc.getElementsByTagName(nodeName)[nodeID].firstChild.nodeValue;
}

function isXMLNodeExist(xmlDoc, nodeName) {
    return xmlDoc.getElementsByTagName(nodeName)[0] != null;
}

function trim(s) {
    var l = 0; 
    var r = s.length - 1;
    while(l < s.length && s[l] == ' ') {
        l++; 
    }
    while(r > l && s[r] == ' ') {
	r -= 1;	
    }
    return s.substring(l, r + 1);
}

function isObjectSelectEmptyString(obj , alertMsg){
	  if (obj == null) {
	        return true;
	    }
	  if (isEmptyString(obj.value)) {
	        obj.focus();
	        //obj.select();
	        alert(alertMsg);
	        return true;
	   }
	   return false;
}

function isObjectEmptyString(obj, alertMsg) {
    if (obj == null) {
        return true;
    }
    if (isEmptyString(obj.value)) {
        obj.focus();
        obj.select();
        alert(alertMsg);
        return true;
    }
    return false;
}

function isEmptyString(str) {
    return trim(str).length <= 0 ? true : false;
}

function isObjectValidNumber(obj, alertMsg) {
    if (obj == null) {
        return false;
    }
    if (isValidNumber(obj.value)) {
        return true;
    }
    obj.focus();
    obj.select();
    alert(alertMsg);
    return false;
}

function isValidNumber(str) {
    if (str == null) {
        return false;
    }
    //return /^(\-){0,1}\d{1,3}(,\d\d\d)*(\.){0,1}(\d){0,2}$/.test(str);
    return /^(\-){0,1}\d*(\.){0,1}(\d){0,2}$/.test(str);
}

function isValidInteger(str) {
    return /^(\-){0,1}\d{1,3}(,\d\d\d)*$/.test(str);
}

function isObjectValidDate(obj, alertMsg, nullAllowed) {

	if ((nullAllowed == null || nullAllowed == true) && obj.value == "") {
        return true;
    }
	
    if (isDate(obj.value)) {
        return true;
    }
    
    obj.focus();
    obj.select();
    alert(alertMsg);
    return false;
}

function toSaveDate(dateStr) {
    // 13/12/2008 => 20081213
    if (dateStr.length != 10) {
        return "";
    }
        
    return dateStr.substr(6, 4) + dateStr.substr(3, 2) + dateStr.substr(0, 2);
}

function toShowDate(dateStr) {
    // 20081213 => 13/12/2008
    if (dateStr.length != 8) {
        return "";
    }
        
    return dateStr.substr(6, 2) + '/' + dateStr.substr(4, 2) + '/' + dateStr.substr(0, 4);
}

function isObjectValidTime(obj, alertMsg, nullAllowed) {
    if ((nullAllowed == null || nullAllowed == true) && obj.value == "") {
        return true
    }
	
    if (obj.value.length <= 5) {
        obj.value += ":00";
    }
    
    if (isTime(obj.value)) {
        return true;
    }
    obj.focus();
    obj.select();
    alert(alertMsg);
    return false;
}

function toSaveTime(timeStr) {
    if (timeStr.length == 8) {
        return timeStr.substr(0, 2) + timeStr.substr(3, 2) + timeStr.substr(6, 2);
    }
    else if (timeStr.length == 6) {
        return timeStr.substr(0, 2) + timeStr.substr(3, 2);
    }
    return "";
        
}

function toSaveTimeNOColon(timeStr) {
    if (timeStr.length == 8) {
        return timeStr.substr(0, 2) + timeStr.substr(3, 2) + timeStr.substr(6, 2);
    }
    else if (timeStr.length == 6) {
        return timeStr.substr(0, 2) + timeStr.substr(3, 2);
    }else if (timeStr.length == 4) {
        return timeStr + "00";
    }
    return "";

}

function compareDate(startDateStr, endDateStr, alertMsg) {
    //alert(alertMsg);
    return -1;
}

//
//  <-- SmartWebby.com
//

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
    var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
    var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
    // February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
    for (var i = 1; i <= n; i++) {
        this[i] = 31
        if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
        if (i==2) {this[i] = 29}
    } 
    return this
}

function isDate(dtStr){
    var daysInMonth = DaysArray(12)
    var pos1=dtStr.indexOf(dtCh)
    var pos2=dtStr.indexOf(dtCh,pos1+1)
    var strDay=dtStr.substring(0,pos1)
    var strMonth=dtStr.substring(pos1+1,pos2)
    var strYear=dtStr.substring(pos2+1)
    strYr=strYear
    if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
    if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
    for (var i = 1; i <= 3; i++) {
        if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
    }
    month=parseInt(strMonth)
    day=parseInt(strDay)
    year=parseInt(strYr)
    if (pos1==-1 || pos2==-1){
        //alert("The date format should be : mm/dd/yyyy")
        return false
    }
    if (strMonth.length<1 || month<1 || month>12){
        //alert("Please enter a valid month")
        return false
    }
    if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
        //alert("Please enter a valid day")
        return false
    }
    if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
        //alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
        return false
    }
    if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
        //alert("Please enter a valid date")
        return false
    }
    return true
}

//
//  SmartWebby.com -->
//
//**************************************
//     
// Name: IsTime Function
// Description:This is a function that i
//     s similar to the IsDate function in VBSc
//     ript, but this function checks for 3 Val
//     id Time Formats and returns true or fals
//     e.
// By: Randy McCleary
//
// Inputs:Time in the following formats:
//     
// hh:mm:ss tt - 2:30:00 PM
// hh:mm tt - 2:30 PM
// hh:mm- 4:30
//
// Returns:true or false
//
//This code is copyrighted and has// limited warranties.Please see http://
//     www.1PerlStreet.com/vb/scripts/ShowCode.
//     asp?txtCodeId=3638&lngWId=2//for details.//**************************************
//     

/**************************************************************
IsTime: Returns a boolean (true) if the Time is validated as a 
Time Format, false is not
Parameters:
strTime: String Time in following formats:
		hh:mm:ss tt - 3:48:01 PM
		hh:mm tt- 3:48 PM
		hh:mm- 15:48
Returns: boolean
 ***************************************************************/


function isTime(strTime) {
    var strFormat = /^(\d{1,2})(\:)(\d{1,2})(\:)(\d{1,2})$/;
    var strTimeArr = strTime.match(strFormat);

    if (strTimeArr == null) {
        return false;
    }
    else {
        // Validate for this format: 15:48:59

        if (strTimeArr[1] > 24 || strTimeArr[1] < 00) {
            return false;
        }
        if (strTimeArr[3] > 59 || strTimeArr[3] < 00) {
            return false;	
        }
        if (strTimeArr[5] > 59 || strTimeArr[5] < 00) {
            return false;	
        }
    }
    return true;
    /*    
    var strTime1 = /^(\d{1,2})(\:)(\d{2})\2(\d{2})(\ )\w[AM|PM|am|pm]$/;
    var strTime2 = /^(\d{1,2})(\:)(\d{2})(\ )\w[A|P|a|p]\w[M|m]$/;
    var strTime3 = /^(\d{1,2})(\:)(\d{1,2})$/;

    var strFormat1 = strTime.match(strTime1);
    var strFormat2 = strTime.match(strTime2);
    var strFormat3 = strTime.match(strTime3);
// Check to see if it matches one of the
//     3 Format Strings.


    if (strFormat1 == null && strFormat2 == null && strFormat3 == null) {
          return false;
    }
    else if (strFormat1 != null) {
          // Validate for this format: 3:48:01 PM
          if (strFormat1[1] > 12 || strFormat1[1] < 00) {
                  return false;
          }
          if (strFormat1[3] > 59 || strFormat1[3] < 00) {
                  return false;	
          }
          if (strFormat1[4] > 59 || strFormat1[4] < 00) {
                  return false;	
          }
    }


    else if (strFormat2 != null) {
        // Validate for this format: 3:48 PM
        if (strFormat2[1] > 12 || strFormat2[1] < 00) {
        return false;
        }

        if (strFormat2[3] > 59 || strFormat2[3] < 00) {
        return false;	
        }
    }
    else if (strFormat3 != null) {
        // Validate for this format: 15:48

        if (strFormat3[1] > 23 || strFormat3[1] < 00) {
            return false;
        }
        if (strFormat3[3] > 59 || strFormat3[3] < 00) {
            return false;	
        }
    }
    return true;
     */
}
function FormatString(str,count){
    if(str.length == count){
        return str;
    }else{
        return "0" + str;
    }
}
function CountString(str,count){
    if(str.length <= count){
        return true;
    }else{
        return false;
    }
}
function OnlyNumber(inputdate){
    for(var i=0;i < inputdate.length; ++i)
    {
       var new_key = inputdate.charAt(i); //cycle through characters
       if(((new_key < "0") || (new_key > "9")) && !(new_key == ""))
       {
            return false;
            break;
       }
    }
    return true;
}
function CheckDate(dt,start_date, stop_date){
    var arrstr = dt.split('/');
    if(arrstr.length != 3){
        if(dt.length<1){
        }else{
          alert('Invalid format Date!');            
        }
    }else{
        if(!OnlyNumber(arrstr[0]) || !OnlyNumber(arrstr[1]) || !OnlyNumber(arrstr[2])){
            alert("Invalid Date Number!");
            document.getElementById(start_date).focus();
            return false;
        }else{
            if(arrstr[1]>12){
                alert("Invalid Month!");
                return false;
            }
            if(arrstr[0]>31){
                alert("Invalid Date!");
                return false;
            }
        }
        

        var str = '';
        if(CountString(arrstr[0],2)){
            str += FormatString(arrstr[0],2) + '/';
        }
        if(CountString(arrstr[1],2)){
            str += FormatString(arrstr[1],2) + '/';
        }
        if(CountString(arrstr[2],4)){
            str += FormatString(arrstr[2],4);
        }

        if(str.length == 10){
            document.getElementById(start_date).value = str;
            //document.mainForm.START_DATE.value = str;
            if(stop_date!=''){
                if(document.getElementById(stop_date).value == ''){
                    document.getElementById(stop_date).value = str;
                }
            }
        }else{
            alert('error');
        }
    }
    return true;
}
function CheckEndDate(dt,start_date, stop_date){
    var arrstr = dt.split('/');
    if(arrstr.length != 3){
        if(dt.length<1){
        }else{
          alert('Invalid format Date!');            
        }
    }else{
        if(!OnlyNumber(arrstr[0]) || !OnlyNumber(arrstr[1]) || !OnlyNumber(arrstr[2])){
            alert("Invalid Date Number!");
            document.getElementById(stop_date).focus();
            return false;
        }else{
            if(arrstr[1]>12){
                alert("Invalid Month!");
                return false;
            }
            if(arrstr[0]>31){
                alert("Invalid Date!");
                return false;
            }
        }
        

        var str = '';
        if(CountString(arrstr[0],2)){
            str += FormatString(arrstr[0],2) + '/';
        }
        if(CountString(arrstr[1],2)){
            str += FormatString(arrstr[1],2) + '/';
        }
        if(CountString(arrstr[2],4)){
            str += FormatString(arrstr[2],4);
        }

        if(str.length == 10){
            document.getElementById(stop_date).value = str;
            //document.mainForm.START_DATE.value = str;
            if(start_date!=''){
                if(document.getElementById(start_date).value == ''){
                    document.getElementById(start_date).value = str;
                }
            }
        }else{
            alert('error');
        }
    }
    return true;
}
parent.CloseSearch();

