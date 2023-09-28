
//********* child.html *********//

//var lgUrl = "mlweb.dreamsecurity.com:18443";
//var lgUrl = "fido.dreamsecurity.com:9443";
var lgUrl = "";
var chUrl = "";
var mlcertUrl = ""
var is_ML_Sign_Init = false;
if( window.console == undefined ) { console = {log : function(){} }; }	//var console = window.console || {log:function(){}};
var devOpt = {
	signOpt : {},
	envelopOpt : {},
	signenvelopOpt : {},
	msg : "dreamsecuritymagicline4webdeveloping"
};

var defaultopt = {};

(function(){
	//allowed domains
	var whitelist = ["localhost",
					"127.0.0.1",
					lgUrl];

	function verifyOrigin(origin){
		// origin = https://self001.dreamsecuritytest.com:8443
		//origin = https://self001.dreamsecuritytest.com:8443
		//var domain = origin.replace(/^https?:\/\/|:\d{1,5}$/g, "").toLowerCase(),
		var domain = origin.replace('/^' + ML4WebApi.getProperty('protocoltype') + '?:\/\/|:\d{1,5}$/g', "").toLowerCase(),
		i = 0,
		len = whitelist.length;
		//console.log("[Child] domain === "+domain);
		while(i < len){
			if (whitelist[i] == domain){
				return true;
			}
			i++;
		}

		return false;
	}

})();


$(document).ready(function(){
	
	$("#outside_alert").hide();
	
	childInit.funcProcInit();
	childInit.setViewModeByOs();
	childInit.initEventHandler();
	
});

/**
 * 페이지 시작시 호출되는 객체
 * funcProcInit
 * setViewModeByOs
 * initEventHandler
 * completeMLInit
 * closeMainDialogToChild
 */
var childInit = {
	/**
	 * 바깥 alert 창 정의 (에러창, 안내창 등)
	 */
	funcProcInit : function(){
		var params = location.search.substring(1).split("&");
		$.each(params, function(){
			var arr01 = this.split("=");
			if(arr01[0] == "lgUrl"){
				lgUrl = arr01[1];
			}
		});
		
		// zIndex 값에 의해 div 노출 순서를 정함.
		$("#outside_alert").hide();		
		$('#outside_alert').MLjquiWindow({
			title:'알림',
			resizable: false,
			position: 'center',
			showCloseButton:false,
			showCollapseButton: false,
			maxHeight: 600,
			maxWidth:420,
			minHeight: 200,
			minWidth: 200,
			height: 148,
			width: 368,
			zIndex:99999,
			initContent: function () {
				$('#outside_alert').attr("status","ready");
				$('#outside_alert').MLjquiWindow('close');
			}
		});
	},
	/**
	 * 호스트 환경에 맞는 css 맵핑
	 */
	setViewModeByOs : function(){
		var currentOS = magiclineUtil.getOS(); // ML4WebApi.getProperty("os");
		if((currentOS=="Android" && navigator.userAgent.toUpperCase().indexOf("MOBILE") >-1)|| currentOS=="IPHONE" || currentOS=="BlackBerry"){
			$("#ML_common_css").attr("href", "UI/css/ML_common_mobile.css");
			$("#MLjquibase_css").attr("href", "UI/css/MLjquibase_mobile.css");
		}else if(currentOS=="IPAD" || currentOS=="Android" || currentOS == "IOS"){
			$("#ML_common_css").attr("href", "UI/css/ML_common_mobile.css");
			$("#MLjquibase_css").attr("href", "UI/css/MLjquibase_mobile.css");
			//$("#ML_common_css").attr("href", "UI/css/ML_common_tablet.css");
			//$("#MLjquibase_css").attr("href", "UI/css/MLjquibase_tablet.css");
		}
	},
	/**
	 * ML4WebApi 초기화
	 */
	initEventHandler : function(){
		// 필요없으면 지움
		$("#stg01").unbind().click(function(){
			
		});
		
		//system lang 설정 & message load
		var lang = navigator.language || navigator.userLanguage;
		MessageVO.loadMessage("ko");
		
		ML4WebApi.init(function(code, obj){
			//ML4WebLog.log("ML4WebApi.init() callback... code ="+code+" / obj="+JSON.stringify(obj));
			// CS 설치체크...
			var os_ver = ML4WebApi.getProperty("os");

			if(code==0){
				//초기화 성공
				childInit.completeMLInit();
			}else if(code==1){
				//초기화 성공
				childInit.completeMLInit();
			}else{
				//error
				childInit.closeMainDialogToChild();
			}
		});
		
	},
	/**
	 * 초기 작업, API 초기화 성공시 ML_Config 에 메시지 전송하는 함수
	 */
	completeMLInit : function(){
		
		is_ML_Sign_Init = true;
		
		chUrl = ML4WebApi.getProperty('baseUrl');
		mlcertUrl = ML4WebApi.getProperty('mlcertUrl');
	
		if(ML4WebApi.getProperty('isUseMLCert')){
			var mlcertFullUrl = "https://"+mlcertUrl+"/ML4-Web/ML4Web/mlcert.html?lgUrl="+lgUrl+"&chUrl="+chUrl+"&random=" + Math.random() * 99999 ;
			$('#dsmlcert').attr("src", mlcertFullUrl);
		}
		
		var requestObj = {
			funcName : "completeInit",
			code: 0,
			data : {}
		};
		
		var requestStr = JSON.stringify(requestObj);
		magiclineController.sendResultMessage(requestStr);
	},
	/**
	 * 인증서 선택창을 닫았을때 필요한 작업 수행
	 */
	closeMainDialogToChild : function(){
		console.log("Child.html closeMainDialogToChild() called...");
		
		$("#selectCertContainer").empty();
		$("#certDetailContainer").empty();
		$("#csContainer").empty();
	
		var requestObj = {
			key : "closeDialog",
			data : "",
			code : 1
		};
		var requestStr = JSON.stringify(requestObj); 
		
		magiclineController.sendResultMessage(requestStr);
	}
}

/**
 * ML_Config.js 의 요청, 결과 처리하는 객체
 * receiveMessage
 * sendResultMessage
 * MakeSignData
 * NTSCertAuth
 * MakeIrosMultiData
 * XMLSignature
 * keyBoardSecurityUse
 * tranx2PEM
 * getVIDRandom
 * getVIDRandomHash
 * getSignDN
 * setSessionID
 * signatureData
 */
var magiclineController = {
	
	/**
	 * 이벤트 리스너 함수
	 */
	receiveMessage : function(event){
		try{
			var requestStr = event.data;
			var request = JSON.parse(requestStr);
			
			var func = window["magiclineController"][request.funcName];			
			func(request.funcParam);
			
		}catch(e){
			//setError(요청 응답 실패)
			console.log(e.message);
		}
		
	},
	/**
	 * ML_Config 에 메시지 전달하는 함수
	 */
	sendResultMessage : function( result ){
		//기존 정보로 다시 설정
		if(Object.keys(defaultopt).length != 0){
			ML4WebApi.webConfig.storageList			= defaultopt.storageList;
			ML4WebApi.webConfig.defaultStorage		= defaultopt.defaultStorage;
			ML4WebApi.webConfig.useVirtualKeyboard 	= defaultopt.useVirtualKeyboard;
			ML4WebApi.webConfig.virtualKeyboardType = defaultopt.virtualKeyboardType;
			ML4WebApi.MobileOption 					= defaultopt.MobileOption;
			ML4WebApi.ClientVersion					= {};
			ML4WebApi.ClientVersion.Win 			= defaultopt.ClientVersion.Win;
			ML4WebApi.ClientVersion.Mac 			= defaultopt.ClientVersion.Mac;
			ML4WebApi.ClientVersion.Lin64 			= defaultopt.ClientVersion.Lin64;
			ML4WebApi.ClientVersion.Lin32 			= defaultopt.ClientVersion.Lin32;	
		}
		
		try{
			var browser = ML4WebApi.getProperty('browser');
			var ilgURL = "";
			
			if(typeof(lgURL)=='undefined'|| lgURL==null){
				ilgURL = ML4WebUI.funProcInit();
			}else{
				ilgURL = lgURL;
			}
			
			if(browser == "MSIE 8"){
				//window.parent.postMessage(requestStr, "https://"+ilgURL);
				window.parent.postMessage(result, ML4WebApi.getProperty('protocoltype')+"//"+ilgURL);
			}else{
				//top.window.postMessage(requestStr, "https://"+ilgURL);
				//top.window.postMessage(result, ML4WebApi.getProperty('protocoltype')+"//"+ilgURL); // 일반
				window.parent.postMessage(result, ML4WebApi.getProperty('protocoltype')+"//"+ilgURL); // 국세청 전용
			}
			$("#selectCertContainer").empty();
		}catch(e){
			console.log("88888888888888888="+e.message);
		}
	},
	
	PFXExport : function(param){
		ML4WebUI.init(function(status){
			if(status == 200){
				ML4WebCS.installCheck('main', function(code, obj){
					if(code == 0 || ML4WebLog.getErrCode("CS_Manager_API_checkInstall")){						
						var viewOptObj = {
							defaultStorage : ML4WebApi.getProperty("defaultStorage"), //"web"
							storageList : ML4WebApi.getProperty("storageList"), //["web(+a)","hdd","token","mobile","smartcert"]
							installcheck : obj.is_cs_install,
							updatecheck : obj.is_cs_update,
							browserInfo : ML4WebApi.get_browser_info(),
							certOidfilter : param.certOidfilter,
							certExpirefilter : param.certExpirefilter
						};
						if(obj.is_cs_install == false || obj.is_cs_update == true){
							if(viewOptObj.defaultStorage != "web_kftc"){
								if(viewOptObj.storageList.indexOf("web_kftc") >= 0){
									ML4WebApi.setProperty("defaultStorage", "web_kftc");
									viewOptObj.defaultStorage = "web_kftc";
								} else{
									ML4WebApi.setProperty("defaultStorage", "web");
									viewOptObj.defaultStorage = "web";
								}
							}
						}
						
						ML4WebUI.showCertDiv(viewOptObj, function(code, certObj){
							if(code === 0){																
								ML4WebCert.proceedCert(param, certObj);
							}else{
								ML4WebCert.certErrorHandler(code, certObj);								
							}
						});
					}else{ // end if(code == 0)
						//setError("installcheck 에러")
					}
				}); // end of ml4webCs.installCheck
			}else{
				//setError("UI 초기화 실패");
			}
		}); // end of ML4WebUI.init
	},
	
	/**
	 * 인증서 선택창 호출
	 */
	MakeSignData : function(param){
		magiclineController.setDefaultParam(param);
		
		ML4WebUI.init(function(status){
			
			if(status == 200){
				
				// 접근 호스트가 모바일일 경우 CS 설치 확인 없이 인증서 선택창 호출
				if(magiclineUtil.isMobile(magiclineUtil.getOS())){
					
					var viewOptObj = {
						defaultStorage : ML4WebApi.getProperty("defaultStorage"), //"web"
						storageList : ML4WebApi.getProperty("storageList_m"), //["web(+a)","hdd","token","mobile","smartcert"]
						browserInfo : ML4WebApi.get_browser_info(),
						certOidfilter : param.certOidfilter,
						certExpirefilter : param.certExpirefilter						
					};
					
					if(ML4WebApi.getProperty("storageList_m").indexOf("web_kftc") >= 0){
						ML4WebApi.setProperty("defaultStorage", "web_kftc");
						viewOptObj.defaultStorage = "web_kftc";
					}else{
						ML4WebApi.setProperty("defaultStorage", "web");
						viewOptObj.defaultStorage = "web";
					}
					
					ML4WebUI.showCertDiv(viewOptObj, function(code, certObj){						
						if(code == 0){					
							ML4WebCert.proceedCert(param, certObj);						
						}else{
							ML4WebCert.certErrorHandler(code, certObj);							
						}					
							
					});
					return;
				}
				
				ML4WebCS.installCheck('main', function(code, obj){
					
					if(code == 0 || ML4WebLog.getErrCode("CS_Manager_API_checkInstall")){
						
						var viewOptObj = {
							defaultStorage : ML4WebApi.getProperty("defaultStorage"), //"web"
							storageList : ML4WebApi.getProperty("storageList"), //["web(+a)","hdd","token","mobile","smartcert"]
							installcheck : obj.is_cs_install,
							updatecheck : obj.is_cs_update,
							browserInfo : ML4WebApi.get_browser_info(),
							certOidfilter : param.certOidfilter,
							certExpirefilter : param.certExpirefilter
						};
						
						if(obj.is_cs_install == false || obj.is_cs_update == true){
							if(viewOptObj.defaultStorage != "web_kftc"){
								if(viewOptObj.storageList.indexOf("web_kftc") >= 0){
									ML4WebApi.setProperty("defaultStorage", "web_kftc");
									viewOptObj.defaultStorage = "web_kftc";
								} else{
									ML4WebApi.setProperty("defaultStorage", "web");
									viewOptObj.defaultStorage = "web";
								}
							}
						}
						
						ML4WebUI.showCertDiv(viewOptObj, function(code, certObj){
							if(code === 0){
								// 기존 proceedCert
								// jsonObj keys : signcert, signpri, rowData, pw, selectedStg		
								ML4WebCert.proceedCert(param, certObj);
							}else{
								ML4WebCert.certErrorHandler(code, certObj);								

							}
						});
							
					}else{ // end if(code==0)
						
					}
					
				}); // end of ml4webCs.installCheck
				
			}else{
				
				//setError("UI 초기화 실패");
			}
		}); // end of ML4WebUI.init
		
	},
	/**
	 * 국세청 로그인 함수. 인증서 선택창 호출
	 */
	NTSCertAuth : function(param){
		magiclineController.setDefaultParam(param);
		
		ML4WebUI.init(function(status){
			
			if(status == 200){
			
				ML4WebCS.installCheck('main', function(code, obj){
					
					if(code == 0 || ML4WebLog.getErrCode("CS_Manager_API_checkInstall")){
						
						var viewOptObj = {
							defaultStorage : ML4WebApi.getProperty("defaultStorage"), //"web"
							storageList : ML4WebApi.getProperty("storageList"), //["web(+a)","hdd","token","mobile","smartcert"]
							installcheck : obj.is_cs_install,
							updatecheck : obj.is_cs_update,
							browserInfo : ML4WebApi.get_browser_info(),
							certOidfilter : param.certOidfilter,
							certExpirefilter : param.certExpirefilter
						};
						
						if(obj.is_cs_install == false || obj.is_cs_update == true){
							if(viewOptObj.defaultStorage != "web_kftc"){
								if(viewOptObj.storageList.indexOf("web_kftc") >= 0){
									ML4WebApi.setProperty("defaultStorage", "web_kftc");
									viewOptObj.defaultStorage = "web_kftc";
								} else{
									ML4WebApi.setProperty("defaultStorage", "web");
									viewOptObj.defaultStorage = "web";
								}
							}
						}
						
						ML4WebUI.showCertDiv(viewOptObj, function(code, certObj){
							
							if(code == 0){
								
								ML4WebCert.proceedCert(param, certObj);
							}else{
								ML4WebCert.certErrorHandler(code, certObj);								
							}							
						});
						
						
					}else{ // end if(code == 0)
						//setError("checkinstall 실패");
						
					}
					
				});
				
			}else{ // end if(status == 200)
				
				//setError("UI 초기화 실패");
			}
		});
	},
	MakeIrosMultiData : function(param){
		magiclineController.setDefaultParam(param);
		
		ML4WebUI.init(function(status){
			
			if(status == 200){
			
				ML4WebCS.installCheck('main', function(code, obj){
					
					if(code == 0 || ML4WebLog.getErrCode("CS_Manager_API_checkInstall")){
						
						var viewOptObj = {
							defaultStorage : ML4WebApi.getProperty("defaultStorage"), //"web"
							storageList : ML4WebApi.getProperty("storageList"), //["web(+a)","hdd","token","mobile","smartcert"]
							installcheck : obj.is_cs_install,
							updatecheck : obj.is_cs_update,
							browserInfo : ML4WebApi.get_browser_info(),
							certOidfilter : param.certOidfilter,
							certExpirefilter : param.certExpirefilter
						};
						
						if(obj.is_cs_install == false || obj.is_cs_update == true){
							if(viewOptObj.defaultStorage != "web_kftc"){
								if(viewOptObj.storageList.indexOf("web_kftc") >= 0){
									ML4WebApi.setProperty("defaultStorage", "web_kftc");
									viewOptObj.defaultStorage = "web_kftc";
								} else{
									ML4WebApi.setProperty("defaultStorage", "web");
									viewOptObj.defaultStorage = "web";
								}
							}
						}
						
						ML4WebUI.showCertDiv(viewOptObj, function(code, certObj){							
							if(code == 0){								
								ML4WebCert.proceedCert(param, certObj);
							}else{
								ML4WebCert.certErrorHandler(code, certObj);								
							}							
						});
						
						
					}else{ // end if(code == 0)
						//setError("checkinstall 실패");
						
					}
					
				});
				
			}else{ // end if(status == 200)
				
				//setError("UI 초기화 실패");
			}
		});
	},
	
	/**
	 * XMLSignature 국세청 XML 서명 함수
	 */
	XMLSignature : function(param){
		
		ML4WebUI.init(function(status){
			if(status == 200){
				//mobile
				if(magiclineUtil.isMobile(magiclineUtil.getOS())){
					var viewOptObj = {
						defaultStorage : ML4WebApi.getProperty("defaultStorage"), //"web"
						storageList : ML4WebApi.getProperty("storageList_m"), //["web(+a)","hdd","token","mobile","smartcert"]
						browserInfo : ML4WebApi.get_browser_info(),
						certOidfilter : param.certOidfilter,
						certExpirefilter : param.certExpirefilter
					};
					
					if(ML4WebApi.getProperty("storageList_m").indexOf("web_kftc") >= 0){
						ML4WebApi.setProperty("defaultStorage", "web_kftc");
						viewOptObj.defaultStorage = "web_kftc";
					}else{
						ML4WebApi.setProperty("defaultStorage", "web");
						viewOptObj.defaultStorage = "web";
					}
					
					ML4WebUI.showCertDiv(viewOptObj, function(code, certObj){
						if(code == 0){
							ML4WebCert.proceedCert(param, certObj);
						}else{
							ML4WebCert.certErrorHandler(code, certObj);
						}
					});
					return;
				}

				ML4WebCS.installCheck('main', function(code, obj){
					if(code == 0 || ML4WebLog.getErrCode("CS_Manager_API_checkInstall")){
						var viewOptObj = {
							defaultStorage : ML4WebApi.getProperty("defaultStorage"), //"web"
							storageList : ML4WebApi.getProperty("storageList"), //["web(+a)","hdd","token","mobile","smartcert"]
							installcheck : obj.is_cs_install,
							updatecheck : obj.is_cs_update,
							browserInfo : ML4WebApi.get_browser_info(),
							certOidfilter : param.certOidfilter,
							certExpirefilter : param.certExpirefilter
						};
						
						if(obj.is_cs_install == false || obj.is_cs_update == true){
							if(viewOptObj.defaultStorage != "web_kftc"){
								if(viewOptObj.storageList.indexOf("web_kftc") >= 0){
									ML4WebApi.setProperty("defaultStorage", "web_kftc");
									viewOptObj.defaultStorage = "web_kftc";
								} else{
									ML4WebApi.setProperty("defaultStorage", "web");
									viewOptObj.defaultStorage = "web";
								}
							}
						}
						
						ML4WebUI.showCertDiv(viewOptObj, function(code, certObj){							
							if(code == 0){								
								ML4WebCert.proceedCert(param, certObj);
							}else{
								ML4WebCert.certErrorHandler(code, certObj);
								//setError("인증서 선택창 호출 실패");
								
							}							
						});
					}else{ // end if(code == 0)
						//setError("checkinstall 실패");
						
					}
				});
			}else{ // end if(status == 200)				
				//setError("UI 초기화 실패");
				
			}
		});
	},
	
	MakeXMLEncryption : function(param){		
		var certAPI    = new JS_Crypto_API();		
		var randomKey  = certAPI.generateRandom(16);
		var randomIv   = certAPI.generateRandom(16);		
		var tbh		   = magiclineUtil.encodeUtf8Hex(param.msg);
		
		var xmlenc	   = certAPI.encrypt("SEED-CBC", randomKey.result, randomIv.result, tbh);
		var	xmlkey	   = certAPI.asymEncrypt(param.kmCert, magicjs.hex.decode(randomKey.resulthex), "rsa15");
		var xmlCipher  = randomIv.resulthex.concat(xmlenc.HexResult); 
		var xmlCipherB = magicjs.base64.encode(magicjs.hex.decode(xmlCipher));
			
		var requestObj = {
				code 			: 0,
				X509Certificate : param.kmCert,
				Symmetrickey 	: xmlkey,
				CipherValue 	: xmlCipherB,
		};
			
		var requestStr = JSON.stringify(requestObj);
		magiclineController.sendResultMessage(requestStr);
	},
	
	/**
	 * 키보드 보안
	 */
	keyBoardSecurityUse : function(param){
		
		var layer 		= param.layer;
		var strKeyBoard = param.strKeyboard;
		var requestObj  = {};
		
		try{
			
			if(layer=="UI"){
				
				requestObj.code = 1;
				requestObj.resultMsg = "";
				requestObj.isUtil = true;
				
			}else{
				
				var resource_api = ML4WebApi.getResourceApi();
			    var message = resource_api.makeCsJsonMessage("keyBoardSecurityUse", strKeyboard);
			    
			    resource_api.csAsyncCall(ML4WebApi.getProperty('CsUrl'), message, function(obj){
			    	requestObj.code = obj.ResultCode;
			    	requestObj.resultMsg = obj;
					requestObj.isUtil = true;
			    });
			}
			
		}catch(e){
			
			requestObj.code = ML4WebLog.getErrCode("ML4Web_API_keyBoardSecurityUse");
			requestObj.resultMsg = {
				"errCode":e.code,
				"errMsg":e.message
			};
			
		}
		
		var requestStr = JSON.stringify(requestObj);
		magiclineController.sendResultMessage(requestStr);
		
	},
	/**
	 * tranx2PEM 데이터 생성
	 */
	tranx2PEM : function(param){
		ML4WebLog.log("tranx2PEM() called... ");
		var certObj  = ML4WebCert.criteria.certObj;
		var usercert = "";
		
		if (typeof(certObj) == "object" && typeof(certObj.signcert) != "undefined") {			
			usercert = "-----BEGIN CERTIFICATE-----\n";
			for(var i=0; i<certObj.signcert.length; i+=64) {
				usercert += certObj.signcert.substr(i, 64)+"\n";
			}
			usercert += "-----END CERTIFICATE-----\n";
		}
		
		var requestObj = {
				code : 0,
				resultMsg : usercert,
				isUtil : true
		};
			
		var requestStr = JSON.stringify(requestObj);
		magiclineController.sendResultMessage(requestStr);
	},
	/**
	 * 선택한 인증서 정보를 기반으로 VID Random 값 생성
	 */
	getVIDRandom : function(callback, callbackObj){
		
		ML4WebLog.log("getVIDRandom() called... ");
		
		// Mapping #1 : 필요 정보 맵핑
		var certObj 		= ML4WebCert.criteria.certObj;
		var signOpt 		= ML4WebCert.criteria.signOpt;
		var selectedStorage = ML4WebCert.criteria.selectedStorage;		
		var passwd			= certObj.pw;
		
		// Mapping #2 : 함수 맵핑
		var isStorageAPI = (selectedStorage == 'token' || selectedStorage == 'smartcert' || selectedStorage == 'smartcertnx' || selectedStorage == 'mobisign' || selectedStorage == 'unisign');		
		
		var storageAPI = new Storage_API();
		var cryptoAPI = new JS_Crypto_API();
		var encVidData = '';
		var b64decData = '';
		
		if(isStorageAPI){			
			var storageRawCertIdx =  ML4WebCert.criteria.certObj.rowData.storageRawCertIdx;
			
			storageAPI.getVIDRandom(storageRawCertIdx, passwd, function(code, obj){
				if (code == 0){

					if(typeof(callback.kmCert) == "undefined" || callback.kmCert == "" || callback.kmCert == null){
						encVidData = obj.VIDRandom;
					}else{
						b64decData = magicjs.base64.decode(obj.VIDRandom);
						encVidData = cryptoAPI.asymEncrypt(callback.kmCert, b64decData, "");						
					}
					
					if(typeof callback === "function"){
						
						callbackObj.vidRandom = encVidData;
						callback(code, callbackObj);
						
					}else{
						var requestObj = {
							code : 0,
							resultMsg : encVidData,
							isUtil : true
						};
								
						var requestStr = JSON.stringify(requestObj);
						magiclineController.sendResultMessage(requestStr);
					}
					
				}else{
					ML4WebCert.certErrorHandler(code, obj);
				}
			});
		}else{ // end if(isStorageAPI)			
			
			cryptoAPI.getVIDRandom(certObj.signpri, passwd, function(code, obj){
				
				if (code == 0){
					
					if(typeof(callback.kmCert) == "undefined" || callback.kmCert == "" || callback.kmCert == null){
						encVidData = obj.result;
					}else{
						b64decData = magicjs.base64.decode(obj.result);
						encVidData = cryptoAPI.asymEncrypt(callback.kmCert, b64decData, "");
					}
					
					if(typeof callback === "function"){
						
						callbackObj.vidRandom = encVidData;
						callback(code, callbackObj);
						
					}else{
						var requestObj = {
							code : 0,
							resultMsg : encVidData,
							isUtil : true
						};
								
						var requestStr = JSON.stringify(requestObj);
						magiclineController.sendResultMessage(requestStr);
					}
					
					
				}else{
					ML4WebCert.certErrorHandler(code, obj);
				}
			});
		}
	},
	/**
	 * 선택된 인증서를 기준으로 VID RandomHash 값 생성
	 */
	getVIDRandomHash : function(callback, callbackObj){
		ML4WebLog.log("getVIDRandomHash() called... ");
		
		// Mapping #1 : 필요 정보 맵핑
		var certObj 		= ML4WebCert.criteria.certObj;
		var signOpt 		= ML4WebCert.criteria.signOpt;
		var selectedStorage = ML4WebCert.criteria.selectedStorage;		
		var idn				= ML4WebCert.criteria.idn;
		var passwd 			= certObj.pw;
		
		// Mapping #2 : 함수 맵핑
		var isStorageAPI = (selectedStorage == 'token' || selectedStorage == 'smartcert' || selectedStorage == 'smartcertnx' || selectedStorage == 'mobisign');		
		
		if(isStorageAPI){
			
			var storageAPI = new Storage_API();
			var stroageRawCertIdx = certObj.rowData.storageRawCertIdx;
			
			storageAPI.getVIDRandomHash(storageRawCertIdx, passwd, idn, function(code, obj){
				
				if(code == 0){
					
					if(typeof callback === "function"){						
						callbackObj.VIDRandomHash = obj.VIDRandomHash;
						callback(code, callbackObj);						
					}else{						
						var requestObj = {
							code : 0,
							resultMsg : obj.VIDRandomHash,
							isUtil : true
						};									
						var requestStr = JSON.stringify(requestObj);
						magiclineController.sendResultMessage(requestStr);
					}
					
				}else{ // end if (code == 0)
					
					ML4WebCert.certErrorHandler(code, obj);
				}
				
				
			});
			
		}else{
			
			var cryptoAPI = new JS_Crypto_API();
			cryptoAPI.getVIDRandomHash(certObj.signpri, passwd, idn, function(code, obj){
			
				if(code == 0){
					
					if(typeof callback === "function"){
						
						callbackObj.VIDRandomHash = obj.result;
						callback(code, callbackObj);
						
					}else{
						var requestObj = {
							code : 0,
							resultMsg : obj.result,
							isUtil : true
						};									
						var requestStr = JSON.stringify(requestObj);
						magiclineController.sendResultMessage(requestStr);
						
					}
					
					
				}else{ // end if (code == 0)
					
					ML4WebCert.certErrorHandler(code, obj);
					
				}
				
			});
			
		}
		
	},
	/**
	 * 선택된 인증서 정보를 기반으로 DN 값 생성
	 */
	getSignDN : function (param){
		var certObj  = ML4WebCert.criteria.certObj;
		var userdn = ML4WebApi.makeReverseDN( certObj.rowData.subjectname );

		var requestObj = {
				code : 0,
				resultMsg : userdn,
				isUtil : true
		};
			
		var requestStr = JSON.stringify(requestObj);
		magiclineController.sendResultMessage(requestStr);
	},
	/**
	 * 세션 아이디 설정
	 */
	setSessionID : function(param){		
		ML4WebApi.setSessionID(param.layer, param.strSessionID, function(code, jsonObj){					
			var requestObj = {
				code : code,
				resultMsg : jsonObj,
				isUtil : true
			};
			
			var requestStr = JSON.stringify(requestObj);
			magiclineController.sendResultMessage(requestStr);			
		});
	},
	/**
	 * signatureData 생성
	 */
	signatureData : function(param){
		
		// param 의 msg 가 서명 원문이 됨(dn 값)
		ML4WebCert.criteria.message = param.msg;
		
		ML4WebCert.MakeSignData(function(code, obj){
			if(code == 0){				
				var requestObj = {
					code : code,
					resultMsg : obj.encMsg,
					isUtil : true
				};
				
				var requestStr = JSON.stringify(requestObj);
				magiclineController.sendResultMessage(requestStr);
				
			}else{ // end if(code == 0)
				ML4WebCert.certErrorHandler(code, obj);
			}
		});		
	},
	/** 
	 * 인증서 저장을 위한 함수
	 */
	saveCertToStorage : function(param){
		
		// 앞단에서 입력받은 초기값 설정
		var certbag  	= param.certbag; // signcert, signpri, kmcert, kmpri
		var stgArr   	= param.stgArr; // ["web", "hdd", ...]
		var certInfo 	= null;
		var saveSelectedStg = '';
		
		// SaveCertMain.jsp
		ML4WebUI.saveCertInit(function(status){
			
			if(status == 200){
				
				// #1. 인증서 정보 가져오기 & UI 필요 정보 맵핑
				ML4WebApi.ml4web_crypto_api.getcertInfo(certbag.signcert, new Array(), function(code, obj){
					
					if(code == 0 && obj != null){
						
						certInfo = obj.result;
						
						ML4WebCS.installCheck('main', function(code1, obj1){
							
							var viewOptObj = {
								defaultStorage : '', //"web"
								storageList : stgArr,
								installcheck : obj1.is_cs_install,
								updatecheck : obj1.is_cs_update,
								browserInfo : ML4WebApi.get_browser_info(),
								certOidfilter : param.certOidfilter,
								certExpirefilter : param.certExpirefilter
							};
							
							if(magiclineUtil.isMobile(magiclineUtil.getOS())){
								viewOptObj.defaultStorage = [ "web" ];
							}else{
								if(obj1.is_cs_install == false || obj1.is_cs_update == true){
									if(viewOptObj.defaultStorage != "web_kftc"){
										if(viewOptObj.storageList.indexOf("web_kftc") >= 0){
											ML4WebApi.setProperty("defaultStorage", "web_kftc");
											viewOptObj.defaultStorage = "web_kftc";
										} else{
											ML4WebApi.setProperty("defaultStorage", "web");
											viewOptObj.defaultStorage = "web";
										}
									}
								}
							}
							
							// #2. 인증서 저장 UI 호출 
							ML4WebUI.showSaveCertDiv(viewOptObj, certbag, certInfo, function(code2, obj2){
								
								if(code2 == 0){

									var cryptoApi 		= ML4WebApi.getCryptoApi();
									var certPw 	  		= obj2.certPw;
									var selectedStorage = obj2.selectedStorage;
									var storageOpt 		= obj2.storageOpt;
									
									if(obj2.selectedStorage == "web"){
										saveSelectedStg = obj2.selectedStorage;
									}else{
										saveSelectedStg = obj2.storageOpt;
									}
									
									// #3. 비밀번호 검증
									cryptoApi.prikeyDecrypt(certbag.signpri, certPw, function(code3, obj3){
										
										if(code3 == 0){
											
											var targetStorageRawIdx = {};									
											// 옵션 key 이름을 아래와 같이 해줘야 CS 에러가 나지 않음
											targetStorageRawIdx.storageName    	  		   = selectedStorage;
											targetStorageRawIdx.storageOpt     	  		   = new Object();
											targetStorageRawIdx.storageOpt.hddOpt 		   = new Object();
											targetStorageRawIdx.storageOpt.hddOpt.diskname = storageOpt;
											targetStorageRawIdx.storageCertIdx 	  		   = "";
											
											// #4. 인증서 저장 API 호출
											var certAPI = new Storage_API();									
											certAPI.SaveCert(certbag, certPw, targetStorageRawIdx, function(code4, obj4){										
												
												if(code4 == 0){
																							
													ML4WebLog.log("SaveCertToStorage::"+targetStorageRawIdx.storageName+" SUCCESS");
													
													// 인증서 저장시 선택한 매체 callback resultMsg에 추가
													obj4.selectedStg = saveSelectedStg;
													
													var requestObj = {
														code : code4,
														resultMsg : obj4
													};
													
													var requestStr = JSON.stringify(requestObj);
													magiclineController.sendResultMessage(requestStr);
													
												}else{ // end if (code4 == 0)
													ML4WebLog.log("SaveCertToStorage::"+targetStorageRawIdx.storageName+" FAIL");
													ML4WebDraw.errorHandler("main", "인증서 저장에 실패했습니다.", $("#btn_confirm_saveCert"), null);
																								
												}
												
											});
										}else if(code3 == 41401){
											// 1. 비번검증을 위해 사용하는 prikeyDecrypt 함수는 비번이 틀리면 41401을 리턴함.. 그 외에 정상적인것은 0 을 리턴.
											// 2. 일반 서명 검증과 같이 비번이 틀리면 40701 로  떨궈서 횟수 제한 로직을 태우도록 한다.
											ML4WebCert.certErrorHandler(40701, obj3);
									
										}else{  // end if (code3 == 0)
											ML4WebCert.certErrorHandler(code3, obj3);
										}
										
									});
									
								}else{ // end if(code2 == 0)
									alert('잘못된 접근입니다.');
								}
								
							});
							
						});
						
					}else{ // end if(code == 0 && obj != null) signcert 에 대응하는 인증서 정보가 없음						
						
						alert("인증서 정보 조회에 실패했습니다.");						
						var requestObj = {
							code : 404,
							resultMsg : false
						};
						
						var requestStr = JSON.stringify(requestObj);
						magiclineController.sendResultMessage(requestStr);
						
					}
					
				});
				
			}else{ // end if (status == 200)
				alert("UI 초기화에 실패했습니다. 다시 시도하세요.");
				return;
			}
			
		});
		
	},
	/**
	 * 사용자가 옵션을 변경하고 싶을때 사용하는 함수
	 */
	setUserOption : function (param){
		var requestObj = {
			code : 0,
			resultMsg : "gggggg"
		};
			
		var requestStr = JSON.stringify(requestObj);
		magiclineController.sendResultMessage(requestStr);
			
		magiclineController.setDefaultParam(param);
	},
	
	setDefaultParam : function (param){
		//기본값 저장해 놓기
		defaultopt.storageList 					= ML4WebApi.webConfig.storageList;			
		defaultopt.defaultStorage 				= ML4WebApi.webConfig.defaultStorage;		
		defaultopt.useVirtualKeyboard 			= ML4WebApi.webConfig.useVirtualKeyboard;
		defaultopt.virtualKeyboardType 			= ML4WebApi.webConfig.virtualKeyboardType;
		defaultopt.MobileOption 				= ML4WebApi.MobileOption;
		defaultopt.ClientVersion				= {};
		defaultopt.ClientVersion.Win			= ML4WebApi.ClientVersion.Win;
		defaultopt.ClientVersion.Mac 			= ML4WebApi.ClientVersion.Mac;
		defaultopt.ClientVersion.Lin64 			= ML4WebApi.ClientVersion.Lin64;
		defaultopt.ClientVersion.Lin32 			= ML4WebApi.ClientVersion.Lin32;
		defaultopt.BROWSER_NOTICE_SHOW			= ML4WebApi.webConfig.browserNoticeShow;

		//변경된 option으로 셋팅
		if(typeof (param.STORAGELIST) != 'undefined'){
			ML4WebApi.webConfig.storageList			= param.STORAGELIST;
		}
		if(typeof (param.STORAGESELECT) != 'undefined'){
			ML4WebApi.webConfig.defaultStorage		= param.STORAGESELECT;
		}
		if(typeof (param.USEVIRTUALKEYBOARD) != 'undefined'){
			ML4WebApi.webConfig.useVirtualKeyboard 	= param.USEVIRTUALKEYBOARD;
		}
		if(typeof (param.VIRTUALKEYBOARDTYPE) != 'undefined'){
			ML4WebApi.webConfig.virtualKeyboardType = param.VIRTUALKEYBOARDTYPE;
		}
		if(typeof (param.MobileOption) != 'undefined'){
			ML4WebApi.MobileOption 					= param.MobileOption;
		}
		if(typeof (param.WinClientVersion) != 'undefined'){
			ML4WebApi.ClientVersion.Win 			= param.WinClientVersion;
		}
		if(typeof (param.MacClientVersion) != 'undefined'){
			ML4WebApi.ClientVersion.Mac 			= param.MacClientVersion
		}
		if(typeof (param.Lin64ClientVersion) != 'undefined'){
			ML4WebApi.ClientVersion.Lin64 			= param.Lin64ClientVersion;
		}
		if(typeof (param.Lin32ClientVersion) != 'undefined'){
			ML4WebApi.ClientVersion.Lin32 			= param.Lin32ClientVersion;
		}
		if(typeof (param.BROWSER_NOTICE_SHOW) != 'undefined'){
			ML4WebApi.webConfig.browserNoticeShow	= param.BROWSER_NOTICE_SHOW;
		}
	},
	
	checkInstall : function(callback){
		ML4WebUI.init(function(status){
			if(status == 200){
				ML4WebCS.installCheck('main', function(code, obj){
					if(code == 0 || ML4WebLog.getErrCode("CS_Manager_API_checkInstall")){
						installcheck = obj.is_cs_install;
						updatecheck = obj.is_cs_update;
						
						var requestObj = {
							code : code,
							resultMsg : obj
						};
						
						var requestStr = JSON.stringify(requestObj);
						magiclineController.sendResultMessage(requestStr);
					}
				}); // end of ml4webCs.installCheck
			}
		});
	},
	
	genHash : function(param){
		var algorithm = param.algorithm;
		var msg = param.msg;
		
		ML4WebApi.ml4web_crypto_api.genHash(algorithm, msg, function(code, obj){
			if(code == 0){
				var requestObj = {
					code : code,
					resultMsg : obj
				};
					
				var requestStr = JSON.stringify(requestObj);
				magiclineController.sendResultMessage(requestStr);
			}
		});
		
	}
}

/**
 * 인증서 서명 관련 객체
 * certCallback
 * proceedCert
 * MakeSignData
 * MakeSignatureData
 * NTSCertAuth
 * certErrorHandler
 */
var ML4WebCert = {
	criteria : {
		signType		: "",   // 서명 형식 (MakeSignData, MakeSignatureData, NTSCertAuth ...)		
		message 		: null, // 서명 원문
		signOpt 		: null, // 서명 옵션
		certObj 		: null, // 인증서 객체
		selectedStorage : "",	// 선택된 저장매체	
		vidType			: "",	// 본인인증 형식 (client, server ...)
		idn				: ""	// 주민번호
	},
	/**
	 * 서명 결과 callback 함수
	 */	
	certCallback : function(resultCode, resultObj){
		
		if(resultCode == 0) {
			if(resultObj == null || $.isEmptyObject(resultObj)){
				ML4WebLog.log("["+ ML4WebCert.criteria.signType + " FAIL!!!]");
			} else {				
				// 서명 성공
				
				ML4WebLog.log("["+ ML4WebCert.criteria.signType + " SUCCESS!!!]");
				
				resultObj.isPosted = true;
				resultObj.code = resultCode;
				var request = JSON.stringify(resultObj);
				magiclineController.sendResultMessage(request);
				
				//ML4WebUI.getDSOption().callback(0, resultObj);		//signed data callback
			}
		}else{
			ML4WebLog.log("[ERROR!!! - "+resultCode+" ] errCode === " + resultObj.errCode + ", errMsg === " +  resultObj.errMsg);
			resultObj.isPosted = true;
			resultObj.code = resultCode;
			var request = JSON.stringify(resultObj);
			
			if (resultCode == -121) {
				DSAlert.callbackAlert("main", resultObj, function(data){
					closeCertDialog('main');
					magiclineController.sendResultMessage(request);
				});
			} else {
				closeCertDialog('main');
				magiclineController.sendResultMessage(request);
			}						
		}
	},

	/**
	 * 서명 진행 함수. MakeSignData, MakeSignatureData 가 signType 으로서 분기
	 */
	proceedCert : function(param, certObj){
		
		ML4WebLog.log("ML4WebCert.proceedCert() called... ");
		
		// 서명에 필요한 값들 맵핑
		this.criteria.signType 			= param.signType;
		this.criteria.message 			= param.msg;
		this.criteria.signOpt 			= param.signOpt;
		this.criteria.vidType			= param.vidType;
		this.criteria.idn				= param.idn;
		this.criteria.certObj 			= certObj;
		this.criteria.selectedStorage 	= certObj.selectedStg;
		
		if(this.criteria.selectedStorage == "mobile" && this.criteria.certObj.pw == "mobisign"){
			this.criteria.selectedStorage = "mobisign";
		}
		if(this.criteria.selectedStorage == "smartcert" && ML4WebApi.getProperty("smartcert_type")!='C'){			
			DS_SmartcertRequest(this.criteria.message);
		}else{
			
			try{
				// signType 이 서명 형식이면서 함수명이 됨
				var func = window["ML4WebCert"][this.criteria.signType];				
				func();
				
			}catch(e){
				//setError(signType + "서명 실패")
				console.log(e.message);
			}
			
		}
		
	},
	/**
	 * MakeSignData, MakeSignature : signOpt 값에 분기해서 signed 데이터, signature 데이터 생성
	 * signOpt.ds_pki_sign_type == "sign" : SignatureData 
	 */
	MakeSignData : function(callback){
		ML4WebLog.log("ML4WebCert.MakeSignData() called... ");
		
		// Mapping #1 : 서명에 필요한 기본 정보 맵핑
		var signOpt 		= ML4WebCert.criteria.signOpt;
		var selectedStorage = ML4WebCert.criteria.selectedStorage;		
		var message 		= ML4WebCert.criteria.message;
		
		// Mapping #2 : 함수 맵핑
		var isStorageAPI = (selectedStorage == 'token' || selectedStorage == 'smartcert' || selectedStorage == 'smartcertnx' || selectedStorage == 'mobisign' || selectedStorage == 'unisign');				
		var certAPI = null;
		var certSignFunc = null;
		
		if(isStorageAPI){
			certAPI = new Storage_API();
			
			if(signOpt.ds_pki_sign_type == "sign"){
				certSignFunc = certAPI.Signature;
			}else{
				certSignFunc = certAPI.Sign; //window["Storage_API_"+selectedStorage]["Sign"];
			}
		}else{			
			certAPI = new JS_Crypto_API();
			
			if(signOpt.ds_pki_sign_type == "sign"){
				certSignFunc = certAPI.signature;
			}else{
				certSignFunc = certAPI.sign; //window["Storage_API_"+selectedStorage]["Sign"];
			}
		}
		
		
		// Mapping #3 : 각 서명에 필요한 파라미터 값 맵핑, 맵핑된 함수 호출
		if(isStorageAPI){
			
			var storageRawCertIdx =  ML4WebCert.criteria.certObj.rowData.storageRawCertIdx;			
			var option = ML4WebCert.criteria.signOpt;
			var passwd = ML4WebCert.criteria.certObj.pw;
			
			if(selectedStorage == 'unisign'){
				storageRawCertIdx.certObj = ML4WebCert.criteria.certObj.certObj;
			}
			
			function certStorageSignCallback(code, obj){
				
				if (code == 0){
					//선택된 storage 정보 보내기
					obj.selectStorage = selectedStorage;
					var encCertIdxStr = "";
					if (ML4WebApi.webConfig.libType == 0 || selectedStorage =='smartcert' || selectedStorage =='mobile' && selectedStorage == 'mobisign'){
						var rawCertIdxStr = JSON.stringify(obj);
						encCertIdxStr = rawCertIdxStr;
					} else{
						var rawCertIdxStr = JSON.stringify(obj.storageCertIdx);
						
						if (selectedStorage == "pfx"){
							encCertIdxStr = ML4WebApi.dsencrypt(rawCertIdxStr);
						}else {
							encCertIdxStr = rawCertIdxStr;
						}
					}
					
					for (var attr in obj.certInfo) {
						if (obj.certInfo.hasOwnProperty(attr)) {
							if (!(attr == "storageRawCertIdx")) {
								ML4WebCert.criteria.certObj.rowData[attr] = obj.certInfo[attr];
							}
						}
					}
					
					if (typeof(ML4WebCert.criteria.certObj.signcert) == "undefined"){
						ML4WebCert.criteria.certObj.signcert = obj.certbag.signcert;
					}
					
					obj.signcert = ML4WebCert.criteria.certObj.signcert;
					
					
					// NTSCertAuth, SignatureData 의 경우
					if (typeof callback ==="function"){
						
						callback(code, obj);
						
					}else if(ML4WebCert.criteria.vidType == "client"){
						
						/*
						certAPI.getVIDRandom(storageRawCertIdx, passwd, function(code2, data2){
							obj.vidRandom = data2.VIDRandom;
							ML4WebCert.certCallback(code2, obj);
						});
						*/
						magiclineController.getVIDRandom(ML4WebCert.certCallback, obj);
					
					}else if(ML4WebCert.criteria.vidType == "server" && ML4WebCert.criteria.idn != ""){
						/*
						certAPI.getVIDRandomHash(storageRawCertIdx, passwd, ML4WebCert.criteria.idn, function(code2, data2){
							obj.VIDRandomHash = data2.VIDRandomHash;
							ML4WebCert.certCallback(code2, obj);					
						});
						*/
						magiclineController.getVIDRandomHash(ML4WebCert.certCallback, obj);
						
					}else{
						ML4WebCert.certCallback(code, obj);
					}
					
				}else{ // end if (code == 0)
					ML4WebCert.certErrorHandler(code, obj);					
				}
			} // end function certSignCallback(code, obj)
			
			certSignFunc(storageRawCertIdx, option, passwd, message, certStorageSignCallback);
			
		}else{ // end if(isStorageAPI)
			
			// web, hdd
			var b64cert		   = ML4WebCert.criteria.certObj.signcert;
			var b64priKey	   = ML4WebCert.criteria.certObj.signpri;
			var sCertPassword  = ML4WebCert.criteria.certObj.pw;
			var msg			   = new Object();
			var callbackResult = new Object();
			
			msg.idx  = 0;
			
			if (typeof(message) == 'object' && typeof(message.length) != 'undefined') {	//JSON Array
				msg.msg = new Array();
				
				for (var i=0; i<message.length; i++){
					message[i] = magiclineUtil.encodeUtf8Hex(message[i]);
				}
				
				msg.type    = 3;
				msg.msg		= message;
				msg.length	= msg.msg.length;
			} else if (typeof(message) == 'object' && typeof(message.length) == 'undefined') {	//JSON Object				
				for (var i=0; i<Object.keys(message).length; i++){
					var keyname = Object.keys(message)[i];					
					message[keyname] = magiclineUtil.encodeUtf8Hex(message[keyname]);
				}
				
				msg.type    = 2;
				msg.msg     = message;
				msg.length	= Object.keys(message).length;
			} else {
				msg.msg = new Array();
				
				msg.type    = 1;
				msg.msg.push(magiclineUtil.encodeUtf8Hex(message));
			}

			if (msg.type == 3 || msg.type == 2) {
				var retobj = certAPI.prikeyDecrypt(b64priKey, sCertPassword);
				
				if (retobj.errCode == 0) {					
					//b64priKey = retobj.Base64String;
					b64priKey = retobj.priKeyInfo;
					sCertPassword = null;					
					b64cert = magicjs.x509Cert.create(b64cert);
				}
			}
			
			function certSignCallback(code, resultObj){
				
				if(code == 0) {
					certExpirePopup(resultObj, ML4WebCert.criteria.certObj.rowData.enddatetime, function(code, obj){
						if (msg.type == 1) {
							obj.encMsg		= obj.stringResult;					
							obj.certInfo	= ML4WebCert.criteria.certObj.rowData;
							obj.certbag		= ML4WebCert.criteria.certObj.certbag;
							obj.selectStorage = selectedStorage;
							
							delete obj.stringResult;
							
							if(typeof callback === "function"){
								callback(code, obj);
							}else if(ML4WebCert.criteria.vidType == "client"){
								magiclineController.getVIDRandom(ML4WebCert.certCallback, obj);
							}else if(ML4WebCert.criteria.vidType == "server" && ML4WebCert.criteria.idn != ""){
								magiclineController.getVIDRandomHash(ML4WebCert.certCallback, obj);
							}else{
								ML4WebCert.certCallback(code, obj);
							}						
						} else if (msg.type == 2) {						
							var keyname = Object.keys(msg.msg)[msg.idx];						
							
							++msg.idx;
							
							if (typeof(callbackResult.encMsg) == 'undefined') {
								callbackResult.encMsg = new Object();
							} 
							
							callbackResult.encMsg[keyname]	= obj.stringResult;
							callbackResult.certInfo			= ML4WebCert.criteria.certObj.rowData;
							callbackResult.certbag			= ML4WebCert.criteria.certObj.certbag;
							callbackResult.selectStorage = selectedStorage;
							
							delete obj.stringResult;
							
							if (msg.idx == msg.length) {
								if(typeof callback === "function"){
									callback(code, callbackResult);
								}else if(ML4WebCert.criteria.vidType == "client"){
									magiclineController.getVIDRandom(ML4WebCert.certCallback, callbackResult);
								}else if(ML4WebCert.criteria.vidType == "server" && ML4WebCert.criteria.idn != ""){
									magiclineController.getVIDRandomHash(ML4WebCert.certCallback, callbackResult);
								}else{
									ML4WebCert.certCallback(code, callbackResult);
								}	
							} else {
								keyname = Object.keys(msg.msg)[msg.idx];
								certSignFunc(b64cert, b64priKey, sCertPassword, msg.msg[keyname], signOpt, certSignCallback);
							}
						}
						else if (msg.type == 3) {					
							++msg.idx;
							
							if (typeof(callbackResult.encMsg) == 'undefined') {
								callbackResult.encMsg = new Array();
							} 
							
							callbackResult.encMsg.push(obj.stringResult);
							callbackResult.certInfo = ML4WebCert.criteria.certObj.rowData;
							callbackResult.certbag	= ML4WebCert.criteria.certObj.certbag;
							callbackResult.selectStorage = selectedStorage;
							
							delete obj.stringResult;
							
							if (msg.idx == msg.length) {							
								if(typeof callback === "function"){
									callback(code, callbackResult);
								}else if(ML4WebCert.criteria.vidType == "client"){
									magiclineController.getVIDRandom(ML4WebCert.certCallback, callbackResult);
								}else if(ML4WebCert.criteria.vidType == "server" && ML4WebCert.criteria.idn != ""){
									magiclineController.getVIDRandomHash(ML4WebCert.certCallback, callbackResult);
								}else{
									ML4WebCert.certCallback(code, callbackResult);
								}	
							} else {
								certSignFunc(b64cert, b64priKey, sCertPassword, msg.msg[msg.idx], signOpt, certSignCallback);
							}
						}
					});			
				} else {
					ML4WebCert.certErrorHandler(code, resultObj);
				}
			}
			
			if (msg.type == 1 || msg.type == 3) {
				certSignFunc(b64cert, b64priKey, sCertPassword, msg.msg[msg.idx], signOpt, certSignCallback);
			} else if (msg.type == 2) {
				var keyname = Object.keys(msg.msg)[0];
				certSignFunc(b64cert, b64priKey, sCertPassword, msg.msg[keyname], signOpt, certSignCallback);
			}		
		}
	},
	NTSCertAuth : function(){
		
		ML4WebLog.log("ML4WebCert.NTSCertAuth() called... ");
		
		// 필요 정보 맵핑
		var signType		= ML4WebCert.criteria.signType; // "NTSCertAuth"
		var certObj			= ML4WebCert.criteria.certObj;
		var message 		= ML4WebCert.criteria.message;
		var signOpt		 	= ML4WebCert.criteria.signOpt;
		var selectedStorage = ML4WebCert.criteria.selectedStorage;
		var passwd 			= ML4WebCert.criteria.pw;		
		var retObj			= new Object();
		
		ML4WebCert.MakeSignData(function(code, obj){
			
			if(code == 0){
				var reversDN  = ML4WebApi.makeReverseDN( obj.certInfo.subjectname );
				var serialnum = obj.certInfo.serialnum;
				var nowDate   = getFormatDate(new Date(), '2');
				
				if(message == undefined || message == "" || message == null){
					retObj.encMsg = reversDN + '$' + serialnum + '$' + nowDate + '$' + obj.encMsg;
				}else{
					retObj.encMsg = message + '$' + serialnum + '$' + nowDate + '$' + obj.encMsg;
				}
				
				retObj.encMsg = ML4WebApi.base64Encode(retObj.encMsg);
				retObj.selectStorage = selectedStorage;
				
				ML4WebCert.certCallback(0, retObj);
			} else {
				ML4WebCert.certErrorHandler(code, obj);
			}
		});
		
	},
	MakeIrosMultiData : function(){
		
		ML4WebLog.log("ML4WebCert.MakeIrosMultiData() called... ");
		
		// 필요 정보 맵핑
		var signType		= ML4WebCert.criteria.signType; // "NTSCertAuth"
		var certObj			= ML4WebCert.criteria.certObj;
		var message 		= ML4WebCert.criteria.message;
		var signOpt		 	= ML4WebCert.criteria.signOpt;
		var selectedStorage = ML4WebCert.criteria.selectedStorage;
		var passwd 			= ML4WebCert.criteria.pw;		
		var retObj			= new Object();
		
		ML4WebCert.MakeSignData(function(code, obj){
			
			if(code == 0){
				var reversDN  = ML4WebApi.makeReverseDN( obj.certInfo.subjectname );
				var serialnum = obj.certInfo.serialnum;
				var nowDate   = getFormatDate(new Date(), '2');
				
				if(message == undefined || message == "" || message == null){
					retObj.encMsg = reversDN + '$' + serialnum + '$' + nowDate + '$' + obj.encMsg;
				}else{
					retObj.encMsg = message + '$' + serialnum + '$' + nowDate + '$' + obj.encMsg;
				}
				
				retObj.encMsg = ML4WebApi.base64Encode(retObj.encMsg);
				retObj.selectStorage = selectedStorage;
				
				ML4WebCert.certCallback(0, retObj);
			} else {
				ML4WebCert.certErrorHandler(code, obj);
			}
		});
		
	},
	XMLSignature : function(){
		
		ML4WebLog.log("ML4WebCert.XMLSignature() called... ");
		var certObj  			 = ML4WebCert.criteria.certObj;
		var certAlgo 			 = certObj.rowData.pubkeyalgorithm; 		
		var SignatureMethodRSA	 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
		var DigestMethodRSA 	 = "http://www.w3.org/2001/04/xmlenc#sha256";
		var SignatureMethodKCDSA = "";
		var DigestMethodKCDSA 	 = "";
		var kcdsaflag			 = 0;
		
		if (typeof(certAlgo) != "undefined"){
			certAlgo = certAlgo.toLowerCase();
		}
		
		SignatureMethodKCDSASha1	= "http://www.tta.or.kr/2002/11/xmldsig#kcdsa-sha1";
		DigestMethodKCDSASha1		= "http://www.w3.org/2000/09/xmldsig#sha1";
		
		SignatureMethodKCDSASha256	= "http://www.tta.or.kr/2002/11/xmldsig#kcdsa-sha256";
		DigestMethodKCDSASha256		= "http://www.w3.org/2001/04/xmlenc#sha256";
		
		if (typeof(certAlgo) != "undefined" && certAlgo.indexOf("kcdsa") >=0) {
			kcdsaflag			 = 1;
			SignatureMethodKCDSA = SignatureMethodKCDSASha256;
			DigestMethodKCDSA	 = DigestMethodKCDSASha256;
		}
		
		if (kcdsaflag == 1 && typeof(ML4WebCert.criteria.message) == 'object' && typeof(ML4WebCert.criteria.message.length) != 'undefined') {	//JSON Array						
			for (var i=0; i<ML4WebCert.criteria.message.length; i++){				
				ML4WebCert.criteria.message[i] = ML4WebCert.criteria.message[i].replace(SignatureMethodRSA, SignatureMethodKCDSA);
				ML4WebCert.criteria.message[i] = ML4WebCert.criteria.message[i].replace(DigestMethodRSA, DigestMethodKCDSA);
			}						
		} else if (kcdsaflag == 1 && typeof(ML4WebCert.criteria.message) == 'object' && typeof(ML4WebCert.criteria.message.length) == 'undefined') {	//JSON Object				
			for (var i=0; i<Object.keys(ML4WebCert.criteria.message).length; i++){
				var keyname = Object.keys(ML4WebCert.criteria.message)[i];				
				ML4WebCert.criteria.message[keyname] = ML4WebCert.criteria.message[keyname].replace(SignatureMethodRSA, SignatureMethodKCDSA);
				ML4WebCert.criteria.message[keyname] = ML4WebCert.criteria.message[keyname].replace(DigestMethodRSA, DigestMethodKCDSA);
			}					
		} else if (kcdsaflag == 1) {
			ML4WebCert.criteria.message = ML4WebCert.criteria.message.replace(SignatureMethodRSA, SignatureMethodKCDSA);
			ML4WebCert.criteria.message = ML4WebCert.criteria.message.replace(DigestMethodRSA, DigestMethodKCDSA);
		}
		
		ML4WebCert.MakeSignData(function(code, obj){
			if(code == 0){
				ML4WebCert.certCallback(0, obj);
			} else {
				ML4WebCert.certErrorHandler(code, obj);
			}
		});
	},
	PFXExport : function(){
		ML4WebLog.log("ML4WebCert.PFXExport() called... ");
		
		// Mapping #1 : 서명에 필요한 기본 정보 맵핑
		var signOpt 		= ML4WebCert.criteria.signOpt;
		var selectedStorage = ML4WebCert.criteria.selectedStorage;		
		var message 		= ML4WebCert.criteria.message;
		
		// Mapping #2 : 함수 맵핑
		var isStorageAPI = (selectedStorage == 'token' || selectedStorage == 'smartcert' || selectedStorage == 'mobisign' || selectedStorage == 'unisign');
		var certAPI = null;
		
		// Mapping #3 : 각 서명에 필요한 파라미터 값 맵핑, 맵핑된 함수 호출
		if(isStorageAPI){
			var requestObj = {
					code : 1,
					resultMsg : $.i18n.prop("ES019"),
					isUtil : true
			};				
			var requestStr = JSON.stringify(requestObj);
			magiclineController.sendResultMessage(requestStr);
		}else{ // end if(isStorageAPI)
			certAPI = new JS_Crypto_API();
			
			certAPI.pfxChangePwExport(ML4WebCert.criteria.certObj.certbag, ML4WebCert.criteria.certObj.pw, message, function(code, obj){
				if(code == 0){				
					obj.result	= obj.result;					
					obj.certInfo	= ML4WebCert.criteria.certObj.rowData;
					obj.certbag	= ML4WebCert.criteria.certObj.certbag;
					
					ML4WebCert.certCallback(code, obj);
				}else{ // end if(code == 0)
					ML4WebCert.certErrorHandler(code, obj);
				}
			});
		}
	},
	/**
	 * 서명 오류를 처리하는 함수.
	 * 각 에러코드에 맵핑되는 alert 호출
	 */
	certErrorHandler : function(code, obj){
		
		ML4WebLog.log("[ERROR!!! - "+code+" ] errCode === " + obj.errCode + ", errMsg === " +  obj.errMsg);
		
		if(code==30011 || code==40701 || code==2004 || code==30016){
			if( ML4WebApi.webConfig.passwordCountLimit <= 0 ){
				$("#input_cert_pw").empty();
				ML4WebDraw.errorHandler("main", $.i18n.prop("ES002"), $("#input_cert_pw"), null);
				return;
			}else{
				if( ML4WebApi.webConfig.passwordFailCount >= ML4WebApi.webConfig.passwordCountLimit - 1 ){
					ML4WebApi.webConfig.passwordFailCount = 0;
					ML4WebCert.certCallback(-121, $.i18n.prop("ES124").replace( "count_limit" , ML4WebApi.webConfig.passwordCountLimit));
					return;
				}else{
					if(code==30011 || code==40701 || code==2004 || code==30016){
						$("#input_cert_pw").empty();
						ML4WebApi.webConfig.passwordFailCount += 1;
						ML4WebDraw.errorHandler("main", $.i18n.prop("ES121").replace( "count_limit" , ML4WebApi.webConfig.passwordCountLimit).replace( "count", ML4WebApi.webConfig.passwordCountLimit - ML4WebApi.webConfig.passwordFailCount), $("#input_cert_pw"), null);
						return;
					}
				}
			}
		}else if(obj.errCode == 30058){
			ML4WebDraw.errorHandler("main", $.i18n.prop("ES027"), $("#input_cert_pw"), null);
			return;
		}else if(obj.errCode == 30012){
			ML4WebDraw.errorHandler("main", $.i18n.prop("ER402"), $("#input_cert_pw"), null);
			return;
		}else if(obj.errCode == 30055){
			
			ML4WebDraw.confirm( ML4WebDraw.MSG_SMT_FAIL, function( code, obj ){
				$("#btn_common_confirm").unbind();
				$("#btn_common_cancle").unbind();
				if( code == 0 ){
					// 서명 재시도
					ML4WebCert.criteria.signOpt.errCode = 30055;
					var func = window["ML4WebCert"][ML4WebCert.criteria.signType];
					func();
				
				}else{
					ML4WebDraw.errorHandler("main", $.i18n.prop("ES027"), $("#input_cert_pw"), null);
					return;
				}
			});
		}else if(obj.errCode == 30057){
			//라온스마트인증 설치 안되어 있을때 라온스마트인증 설치페이지 띄우기
			var errMsg = obj.errMsg;
			
			var raonsitecode = ML4WebApi.getProperty('cs_smartcert_raonsitecode');
			var smartCertURL = '';
			
			if(errMsg != null && errMsg != "" && errMsg == "CLP_SetSmartCertInfo"){
				smartCertURL = ML4WebApi.getProperty('cs_smartcert_installurl');
			} else if (errMsg == "") {
				smartCertURL = ML4WebApi.getProperty('cs_smartcert_installurl');
			} else{
				smartCertURL = errMsg+'?sitecode='+raonsitecode;
			}
			ML4WebDraw.confirm( ML4WebDraw.MSG_SMT_INSTALL, function( code, obj ){
				$("#btn_common_confirm").unbind();
				$("#btn_common_cancle").unbind();
				if( code == 0 ){
					var width = 500;
					var height = 380;
					var top = (screen.availHeight/2) - (height/2);
					var left =(screen.availWidth/2) - (width/2);
					
					window.open(smartCertURL,'_blank','scrollbars=no,toolbar=no,resizable=no, width='+ width +', height='+ height +', top=' + top + ', left=' + left);
				}
			});
		}else if(obj.errCode == 30070){ // mobisign
			ML4WebDraw.confirm(ML4WebDraw.MSG_MOB_SIGN_INSTALL, function( code, obj ){
				$("#btn_common_confirm").unbind();
				$("#btn_common_cancle").unbind();
				if( code == 0 ){
					var mobisignURL = ML4WebApi.getProperty('cs_mobisign_popupurl');
					var width = 564;
					var height = 422;
					var top = (screen.availHeight/2) - (height/2);
					var left =(screen.availWidth/2) - (width/2);
					
					window.open(mobisignURL,'_blank','scrollbars=no,toolbar=no,resizable=no, width='+ width +', height='+ height +', top=' + top + ', left=' + left);	
				}
			});
		}else if(obj.errCode == 30071){ // mobisign
			ML4WebDraw.errorHandler('main',ML4WebDraw.MSG_MOB_SIGN_CANCEL);
		}else if(obj.errCode == 30072){ // mobisign
			ML4WebDraw.confirm(ML4WebDraw.MSG_MOB_SIGN_UPDATE,function(code,obj){
				$("#btn_common_confirm").unbind();
				$("#btn_common_cancle").unbind();
				if( code == 0 ){
					var mobisignURL = ML4WebApi.getProperty('cs_mobisign_popupurl');
					var width = 564;
					var height = 422;
					var top = (screen.availHeight/2) - (height/2);
					var left =(screen.availWidth/2) - (width/2);
					
					window.open(mobisignURL,'_blank','scrollbars=no,toolbar=no,resizable=no, width='+ width +', height='+ height +', top=' + top + ', left=' + left);
				}
			})
		}else if(code == 12100){
			if(obj.errCode == 31){
				ML4WebDraw.errorHandler("main", $.i18n.prop("ES031"));
			}else if(obj.errCode == 32){
				ML4WebDraw.errorHandler("main", $.i18n.prop("ER119"));
			}
		} else if(code == ML4WebLog.getErrCode("Storage_kftc")){					
			if (typeof(obj) == "object") {
				if (obj.code == 2211) {
					if (typeof(obj.origin) == "undefined"){
						obj.maxFailCount = 5;
					} else {
						obj.maxFailCount = 10;
					}
					
					if(obj.failCount>=obj.maxFailCount){
						ML4WebCert.certCallback( -121, $.i18n.prop("ES123").replace( "count" , obj.maxFailCount), $("#input_cert_pw"));
					}else{
						ML4WebDraw.errorHandler("main", $.i18n.prop("ES122").replace( "count_limit" , obj.maxFailCount).replace( "count", obj.failCount), $("#input_cert_pw"), null);
					}
				} else if (obj.code == 2212) {
					if (typeof(obj.origin) == "undefined"){
						obj.maxFailCount = 5;
					} else {
						obj.maxFailCount = 10;
					}
					
					ML4WebCert.certCallback( -121, $.i18n.prop("ES123").replace( "count" , obj.maxFailCount), $("#input_cert_pw"));
				}
			} else if (typeof(obj) == "string") {
				ML4WebDraw.errorHandler("main", obj);
			} else {
				ML4WebDraw.errorHandler("main", $.i18n.prop("ES030"));
			}
		}else if(code == 'E1000'){ //스마트인증nx 취소버튼 처리
			ML4WebDraw.errorHandler("main", $.i18n.prop("ES027"), $("#input_cert_pw"), null);
			return;
		}
		else{
			ML4WebDraw.errorHandler("main", $.i18n.prop("ES030"));
		}
	}
}


/**
 * 공통, 유틸성의 함수들을 모아둔 객체
 */
var magiclineUtil = {
	criteria : {	
		base64_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
		//whiteList : ["localhost", "127.0.0.1", lgUrl]
	},
		
	encodeUtf8Hex : function(str){
		return unescape(encodeURIComponent(str));
	},
	decodeUtf8Hex : function(str){
		return decodeURIComponent(escape(str));
	},
	base64encode : function(input){
		
		if(input.length < 1){
			// setError(input 데이터가 없음 (인코드) );
			return;
		}
			
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;

		while (i < input.length) {

		  chr1 = input.charCodeAt(i++);
		  chr2 = input.charCodeAt(i++);
		  chr3 = input.charCodeAt(i++);

		  enc1 = chr1 >> 2;
		  enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
		  enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
		  enc4 = chr3 & 63;

		  if (isNaN(chr2)) {
			  enc3 = enc4 = 64;
		  } else if (isNaN(chr3)) {
			  enc4 = 64;
		  }
		  output = output +
			  this.criteria.base64_keyStr.charAt(enc1) + this.criteria.base64_keyStr.charAt(enc2) +
			  this.criteria.base64_keyStr.charAt(enc3) + this.criteria.base64_keyStr.charAt(enc4);
		}
			
		return output;
	},
	base64decode : function(input){
		
		if(input.length < 1){
			// setError(input 데이터가 없음(디코드) );
			return;
		}
			
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
	    var i = 0;

	    input = input.replace(/[^A-Za-z0-9+/=]/g, "");
	    while (i < input.length)
	    {
	        enc1 = this.base64_keyStr.indexOf(input.charAt(i++));
	        enc2 = this.base64_keyStr.indexOf(input.charAt(i++));
	        enc3 = this.base64_keyStr.indexOf(input.charAt(i++));
	        enc4 = this.base64_keyStr.indexOf(input.charAt(i++));
	        
	        chr1 = (enc1 << 2) | (enc2 >> 4);
	        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
    	    chr3 = ((enc3 & 3) << 6) | enc4;

	        output = output + String.fromCharCode(chr1);

	        if (enc3 != 64) {
	            output = output + String.fromCharCode(chr2);
	        }
	        if (enc4 != 64) {
	            output = output + String.fromCharCode(chr3);
	        }
	    }

	    return output;
	},
	encodeUtf8andBase64 : function(input){
		// TODO: deprecate: "Deprecated. Use util.binary.base64.encode instead."
		var _base64 = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
		var line = '';
		var output = '';
		var chr1, chr2, chr3;
		var i = 0;

		input = unescape(encodeURIComponent(input));
		//R = (n + 2 - ((n + 2) % 3)) / 3 * 4
		maxline = (input.length + 2 - ((input.length + 2) % 3)) / 3 * 4;

		while(i < input.length) {
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
		    chr3 = input.charCodeAt(i++);
		    
		    // encode 4 character group
		    line += _base64.charAt(chr1 >> 2);
		    line += _base64.charAt(((chr1 & 3) << 4) | (chr2 >> 4));
		    
		    if(isNaN(chr2)) {
		    	line += '==';
		    }else {
		    	line += _base64.charAt(((chr2 & 15) << 2) | (chr3 >> 6));
		    	line += isNaN(chr3) ? '=' : _base64.charAt(chr3 & 63);
		    }

		    if(maxline && line.length > maxline) {
		    	output += line.substr(0, maxline) + '\r\n';
		    	line = line.substr(maxline);
		    }
		}
		
		output += line;
		
		return output;
	},
	/*setCookie : function(cname, cvalue, exdays){
				
		var d = new Date();
				
		d.setTime(d.getTime() + (exdays*300000));
		var expires = "expires=" + d.toUTCString();
		document.cookie = cname + "=" + cvalue + "; " + expires;
			
	},*/
	getTimeStamp : function(){
		var d = new Date();
		var s =
			this.leadingZeros(d.getFullYear(), 4) + '-' +
			this.leadingZeros(d.getMonth() + 1, 2) + '-' +
			this.leadingZeros(d.getDate(), 2) + ' ' +
			this.leadingZeros(d.getHours(), 2) + ':' +
			this.leadingZeros(d.getMinutes(), 2) + ':' +
			this.leadingZeros(d.getSeconds(), 2);

		return s;
	},
	leadingZeros : function(n, digits){
		var zero = '';
		n = n.toString();

		if (n.length < digits) {
			for (i = 0; i < digits - n.length; i++){
				zero += '0';
			}
		}
		return zero + n;
	},
	getOS : function(){
		try{
			var OsVersion = navigator.userAgent;
			OsVersion = OsVersion.toUpperCase();
			if( OsVersion.indexOf("NT 5.1") != -1 ) return "windows XP";
			else if( OsVersion.indexOf("NT 6.0") != -1 ) return "windows vista";
			else if( OsVersion.indexOf("NT 6.1") != -1 ) return "windows 7";
			else if( OsVersion.indexOf("NT 6.2") != -1 ) return "windows 8";
			else if( OsVersion.indexOf("NT 6.3") != -1 ) return "windows 8.1";
			else if( OsVersion.indexOf("NT 10.0") != -1 ) return "windows 10";
			else if( OsVersion.indexOf("IPAD") != -1 ) return "IOS";
			else if( OsVersion.indexOf("IPHONE") != -1 ) return "IPHONE";
			else if( OsVersion.indexOf("ANDROID") != -1 ) return "Android";
			else if( OsVersion.indexOf("BLACKBERRY") != -1 ) return "BlackBerry";
			else if( OsVersion.indexOf("MAC") != -1 ) return "MAC";
			else if( OsVersion.indexOf("SYMBIAN") != -1 ) return "Symbian";
			else if( OsVersion.indexOf("UBUNTU") != -1 ){
				if( OsVersion.indexOf("86_64") != -1 ){
					return "LINUX64_UBUNTU64";
				}else{
					return "LINUX32_UBUNTU32";
				}
			}else if( OsVersion.indexOf("FEDORA") != -1 ){
				if( OsVersion.indexOf("86_64") != -1 ){
					return "LINUX64_FEDORA64";
				}else{
					return "LINUX32_FEDORA32";
				}
			}
			else if( OsVersion.indexOf("LINUX") != -1 ){
				if( OsVersion.indexOf("86_64") != -1 ){
					return "LINUX64";
				}else{
					return "LINUX32";
				}
			}
			else return "Unknown";
		}catch( ex ) {
			return "Unknown";
		}	
	},
	readLength : function(b){
		var b2 = b.getByte();
		if(b2 === 0x80) {
			return undefined;
		}
		// see if the length is "short form" or "long form" (bit 8 set)
		var length;
		var longForm = b2 & 0x80;
		if(!longForm) {
		  // length is just the first byte
		  length = b2;
		} else {
		  // the number of bytes the length is specified in bits 7 through 1
		  // and each length byte is in big-endian base-256
		  length = b.getInt((b2 & 0x7F) << 3);
		}
		return length;
	},
	readValue : function(tag, bytes){
		if(bytes.length() < 2) {
			throw new Error('Too few bytes to parse DER.');
		}
		if (bytes.getByte() != tag) {
		           throw new Error('Invalid format.'); } 
		var length = readLength (bytes); 
		return bytes.getBytes(length); 
	},
	isMobile : function(){
			
		var currentOS = this.getOS();
		if(currentOS=="Android" || currentOS=="IPHONE" || currentOS=="IPAD" || currentOS=="BlackBerry" || currentOS == "IOS"){
			return true;
		}else{
			return false;
		}	
	}
		
}

if(window.addEventListener){
	window.addEventListener("message", magiclineController.receiveMessage, false);
}else if (window.attachEvent){
	window.attachEvent("onmessage", magiclineController.receiveMessage);
}


///////////////////////////////////////////////////////////////////////
//CS 설치 관련....
///////////////////////////////////////////////////////////////////////
	/* function StartCS(){
		var requestObj = {
			key : "StartCS",
			data : ""
		};
	} */
	/*function startCSInstall(){
		var requestObj = {
			key : "startCSInstall",
			data : {}
		}
	
		var requestStr = JSON.stringify(requestObj);
	
		//- cross domain, send data
		//top.window.postMessage(requestStr, "https://"+lgUrl+":8443");
		sendPostMessage(requestStr);
	}*/
	
	/*function startCSUpdate(){
		var requestObj = {
			key : "startCSUpdate",
			data : {}
		}
	
		var requestStr = JSON.stringify(requestObj);
	
		//- cross domain, send data
		//top.window.postMessage(requestStr, "https://"+lgUrl+":8443");
		sendPostMessage(requestStr);
	}*/
	
	// TODO : 쓰는지 확인 필요
	function installPageUrl(url){
		console.log("installPageUrl called....");
		var requestObj = {
			key : "installPageUrl",
			data : {url:url}
		}
	
		var requestStr = JSON.stringify(requestObj);
	
		//- cross domain, send data
		//top.window.postMessage(requestStr, "https://"+lgUrl+":8443");
		sendPostMessage(requestStr);
		//window.parent.postMessage(requestStr, url);
	}
//
//---------------------------------------------------------------------


	

//---------------------------------------------------------------------

///////////////////////////////////////////////////////////////////////
//To Mlcert
///////////////////////////////////////////////////////////////////////
	function getBackupCertListFromMlcert(){
	//	console.log("##############[Child] getBackupCertListFromMlcert() called...");
		sendPostMessageToMlcert("reqGetCertList", "");
	}
	
	function setBackupCertListToMlcert(str){
	//	console.log("##############[Child] setBackupCertListToMlcert() called...");
		sendPostMessageToMlcert("reqSetCertList", str);
	}
	
	function sendPostMessageToMlcert(arg1, arg2){
	//	console.log("##############[Child] sendPostMessageToMlcert() called...");
		var mlcertDomain = "https://"+mlcertUrl;  
	
		var iframeWindow = document.getElementById('dsmlcert').contentWindow;
	
		var requestObj = {
			key : arg1,
			data : arg2
	//		,browser : ML4WebApi.getProperty('browser')
		};
		var requestStr = JSON.stringify(requestObj);
	//	console.log("##############[Child] sendPostMessageToMlcert() called... requestStr = " + requestStr);
	
		iframeWindow.postMessage(requestStr, mlcertDomain);
	}
//
//---------------------------------------------------------------------

	