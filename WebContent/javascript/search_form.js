var searchForm = null;

function SEARCH_CODE_Click() {
}

function openSearchForm(url) {
    parent.OpenSearch(url);
    return false;
    
    if (searchForm && !searchForm.closed) {
        searchForm.focus();
    }
    else {
      var width = 820;
      var height = 600;
      var left = (screen.width - width) / 2;
      var top = (screen.height - height) / 2;
      searchForm = window.open(url, "Search", "menu=no, location=no, toolbar=no, scrollbars=yes, status, width=" + width + ", height=" + height + ", top=" + top + ", left=" + left);
    }
}
