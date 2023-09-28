/**
****************************************************
TouchEnNx_config.js
****************************************************
| Version     작성자        수정일        변경사항 
 ---------  -------  -----------  ----------
 | v1.0.0.9    강남준    2021.08.14
 | v1.0.0.8    강남준    2021.01.29
 | v1.0.0.7    강남준    2020.02.17
 | v1.0.0.6    강남준    2019.07.05
 | v1.0.0.5    강남준    2018.12.14
 | v1.0.0.4    백서린    2018.11.12
 | v1.0.0.3    강남준    2018.05.14
 | v1.0.0.2    허혜림    2018.01.31
 | v1.0.0.1    허혜림    2017.12.20          

****************************************************
 Copyright ⒞ RaonSecure Co., Ltd. 
****************************************************
**/

var nxKeyConfig ={};
nxKeyConfig.version = {
	
	extension :   {
		exChromeExtVer		:	"1.0.0.0",
		exFirefoxExtVer		:	"1.0.2.5",
		exFirefoxJpmExtVer	:	"1.0.1.12",
		exOperaExtVer		:	"1.0.1.14"
	},
		
	/** 키보드보안 설정 */
		tkappiver			:	"1.0.0.69",
		tkappmver			:	"1.0.0.59",
		exWinVer			:	"1.0.0.75",
		exWin64Ver			:	"1.0.0.75",
		exWinProtocolVer	:	"1.0.1.1243",
		daemonVer			:   "1.0.2.8",
		macDaemonVer		:   "1.0.1.7",
		linuxDaemonVer		:   "1.0.0.1",
		exMacVer			:	"1.0.0.13",
		exMacProtocolVer	:	"1.0.1.1392"
};

nxKeyConfig.module = {
	
	extension	:{
		//exChromeExtDownURL	: "https://chrome.google.com/webstore/detail/dncepekefegjiljlfbihljgogephdhph",
		exChromeExtDownURL	: "https://download.raonsecure.com/extension/chrome/chrome.html",
		exFirefoxExtDownURL	: TouchEnNxConfig.path.base + "/extension/touchenex_firefox.xpi",
		exFirefoxJpmExtDownURL	: TouchEnNxConfig.path.base + "/extension/jpm_touchenex_firefox.xpi",
		exOperaExtDownURL	: TouchEnNxConfig.path.base + "/extension/touchenex_opera.nex"
	},
	
		exWinClient		            :	TouchEnNxConfig.path.base + "/nxKey/module/TouchEn_nxKey_32bit.exe",
		exWin64Client            	:	TouchEnNxConfig.path.base + "/nxKey/module/TouchEn_nxKey_64bit.exe",
		daemonDownURL				:	TouchEnNxConfig.path.base + "/nxKey/module/TouchEn_nxKey_32bit.exe",
		macDaemonDownURL			:	TouchEnNxConfig.path.base + "/nxKey/module/TouchEn_nxKey_Installer.pkg",
	//	ubuntu32DaemonDownURL		:	TouchEnNxConfig.path.base + "/nxKey/module/CrossEXService_32bit.deb",
	//	ubuntu64DaemonDownURL		:	TouchEnNxConfig.path.base + "/nxKey/module/CrossEXService_64bit.deb",
	//	fedora32DaemonDownURL		:	TouchEnNxConfig.path.base + "/nxKey/module/CrossEXService_32bit.rpm",
	//	fedora64DaemonDownURL		:	TouchEnNxConfig.path.base + "/nxKey/module/CrossEXService_64bit.rpm",
		exMacClient					:	TouchEnNxConfig.path.base + "/nxKey/module/TouchEn_nxKey_Installer.pkg",
		exMacProtocolDownURL		: 	TouchEnNxConfig.path.base + "/nxKey/module/TouchEn_nxKey_Installer.pkg"
};

/** 키보드보안 E2E 사용 : 주석처리 / E2E 사용X : 주석해제 */
var TNK_SR = "";

/**	클라이언트 솔루션별 동작 설정*/
TouchEnNxConfig.solution={
		nxkey : {
				tekOption : {
					"pki": "TouchEnkeyEx",
				    "keyboardonly": "false",
				    "defaultenc": "false",
				    "verify": "0",
				    "defaultpaste": "true",
				    "iframename": "",
				    "usegetenc": "false",
				    "clearbufferonempty": "true",
				    "refreshsession": "true",
				    "improve": "true",
					"bstart": 0,
				    "setcallback": "false",
				    "usebspress": "false",
				    "ignoreprogress": "true",
				    "ignoreprogress2": "true",
				    "exformname": "",
				    "idbase": "false",
				    "allcrypt": "false",
					"browserinfo" : "",
					"cert" : "-----BEGIN CERTIFICATE-----MIIDIDCCAgigAwIBAgIJAO4t+//wr+dfMA0GCSqGSIb3DQEBCwUAMGcxCzAJBgNVBAYTAktSMR0wGwYDVQQKExRSYW9uU2VjdXJlIENvLiwgTHRkLjEaMBgGA1UECxMRUXVhbGl0eSBBc3N1cmFuY2UxHTAbBgNVBAMTFFJhb25TZWN1cmUgQ28uLCBMdGQuMB4XDTIyMDMxNjAyNDAyM1oXDTQyMDMxMTAyNDAyM1owHDELMAkGA1UEBhMCS1IxDTALBgNVBAoMBHNpdGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC41UqoEM2gj7wltV+L35sO/TY8F9Fjv28E6uT3Ai0EJ5NV++K4kxoOyCErejNkAy1B08R+9atZT4B7vr+L8Ilwk0HuXdHBCgkt9ADvSLAMhFGnepdFzJSDKQE6X81D7su0B1aDXI4hhaWez33M/tj6UPTASJT4YJmcoAtlHbvBUCb2AOx5wifMqK9TCjcuJ+jatqAHccrWpKvScBU797sSlORSRMAFy7X2ydDCQilQmS8pqSuRIbExEKgKfcm/l8Gai+TtkhFtMHIPE8uU9tufYQo1xLNKYVKquAObXoFj7wWzvUlPk2NA2rNthP2ZbdyViahtO4aFj/jCp38VNxZPAgMBAAGjGjAYMAkGA1UdEwQCMAAwCwYDVR0PBAQDAgXgMA0GCSqGSIb3DQEBCwUAA4IBAQCeSpr68XWXIE4QTNfgzPoJo++UegCUynFINuMvuQa2ra3mzmj2SwYroVxkd93mt7vXRQkLUR5W/NlRgudekyuflvVA6Jgh+w2JLe1wJQCLlly05L+Fk4nNh5Y3GIPBS/ybPdfcXJNdWpyJPP0i8Y8u6OlHVnGVq1Vu1CehMU0HcoI7T7hUmyMLBCO0R2M2I9fBVPe8SlcsRc63kfvHz2wp+EC8r18/vXX6Jm8dI8DPYsZe09hUQS8cCBUSP5rot6+RHyl47xrhZYYlFtfoMtajj+Y6te579RIDJmnm9SBvBwCm4GdUroCn5hcHW9KW+Azq+TgZlWHHduzz32dNOeug-----END CERTIFICATE-----",
					"srdk": TNK_SR,
					"generate_event": "false",
					"driverexcept": "0",
					"delayedck": "false",
					"shiftbypass": "true",
					"allowdup": "false",
					"enc2": "false",
				    "searchformname":"",
					"runtype": TouchEnNxConfig.runtype,
					"tk_isRunningSecurity" : "false", 
					"isAllowIdOverlap" : "false", //히든필드 중복오류 수정시 false설정 및 서버버전 v2.0.3.3 적용필요
					"defaultsecurityid" : "true",
					"newModule" : "true"
				}
		}
};