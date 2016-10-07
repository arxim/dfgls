
function removeAllDataRow() {
    var table = document.getElementById("dataTable") 
    for (i = table.rows.length - 3; i > 0; i--) {
        table.deleteRow(2);
    }
}

function insertRow(datas, aligns) {
    var table = document.getElementById("dataTable") 
    var rowNew = table.insertRow(table.rows.length - 1);
    var cellNew;
    for (c = 0; c < datas.length; c++) {
        cellNew = rowNew.insertCell(c);
        cellNew.className = "row1";
        cellNew.style.textAlign = aligns[c];
        cellNew.innerHTML = datas[c];
    }
}
