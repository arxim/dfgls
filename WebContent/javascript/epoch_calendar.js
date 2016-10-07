function loadCalendar() {
	var dp_cal;
	dp_cal  = new Epoch('epoch_popup','popup',document.getElementById('START_DATE'),document.getElementById('showDate1'));
	dp_cal  = new Epoch('epoch_popup','popup',document.getElementById('END_DATE'),document.getElementById('showDate2'));
}
function chooseStartDate(){
	var dp_cal;
	dp_cal  = new Epoch('epoch_popup','popup',document.getElementById('START_DATE'),document.getElementById('showDate1'));
}
function chooseEndDate(){
	var dp_cal;
	dp_cal  = new Epoch('epoch_popup','popup',document.getElementById('END_DATE'),document.getElementById('showDate2'));
}