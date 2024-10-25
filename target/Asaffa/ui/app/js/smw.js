var SMW = SMW ||{};

function getSMWBaseUrl(){
	var pathArray = window.location.pathname.split( '/' );
	var baseUrl= window.location.protocol + '//'+window.location.host+'/'+pathArray[1]+'/';
	return baseUrl;
}

function getSMWServerBaseUrl(){
	//return "http://192.168.1.209:8080/";
	var baseUrl= window.location.protocol + '//'+window.location.host+'/';
	return baseUrl;
}


SMW.fu = 'app/'; // Front end URL
SMW.du = 'app/data/'; // data URL
SMW.su = getSMWBaseUrl(); // Temporary Service URL
SMW.ip = getSMWServerBaseUrl()+'invoice/';
SMW.rp = getSMWServerBaseUrl()+'receipt/';
SMW.orp = getSMWServerBaseUrl()+'orders/';
SMW.vsr = getSMWServerBaseUrl()+'vansalesreport/';
SMW.init = function(){
 SMW.mobWebsite.init();
};