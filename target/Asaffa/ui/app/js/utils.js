// Utility functions goes here

var console = {};
console.log = function (msg) {};

function getBaseUrl() {
    var pathArray = window.location.pathname.split('/');
    var baseUrl = window.location.protocol + '//' + window.location.host + '/' + pathArray[1] + '/';
    return baseUrl;
}

var url = getBaseUrl();

if (document.URL.split('#').length > 1) {
    window.location.href = url + 'ui/index.html';
}



