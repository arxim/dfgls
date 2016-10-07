var AJAX = AJAX_Create();
function AJAX_Create() {
    var ajax = null;

    if (window.XMLHttpRequest) {
        ajax = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        ajax = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return ajax;
}

function AJAX_Request(url, handle) {
    if (AJAX) {
        AJAX.open("POST", url);
        AJAX.onreadystatechange = handle;
        AJAX.send(null);
    }
}

function AJAX_IsComplete() {
    if (AJAX.readyState == 4 && AJAX.status == 200) {
        return 1;
    }    
    return 0;
}