<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.dreamsecurity.magicline.JCaosCheckCert"%>
<%@ page import="com.dreamsecurity.jcaos.x509.X509Certificate"%>
<%@ page import="com.dreamsecurity.jcaos.x509.X509GeneralName"%>
<%@ page import="com.dreamsecurity.jcaos.x509.X509OtherName"%>
<%@ page import="com.dreamsecurity.jcaos.util.encoders.Base64"%>
<%@ page import="com.dreamsecurity.jcaos.util.encoders.Hex"%>
<%@ page import="com.dreamsecurity.jcaos.vid.VID"%>
<%@ page import="com.dreamsecurity.jcaos.cms.SignedData"%>
<%@ page import="com.dreamsecurity.jcaos.cms.SignerInfo"%>
<%@ page import="com.dreamsecurity.magice2e.MagicE2E" %>
<%@ page import="com.dreamsecurity.magice2e.jcaos.JSONUtil"%>
<%@ page import="com.dreamsecurity.magice2e.util.Log"%>
<%@ page import="java.math.BigInteger"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.ResourceBundle"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="javax.xml.bind.JAXBException"%>
<%@ page import="bnet.library.exception.LoggableException"%>
<%@ page import="bnet.library.util.CoreUtils.aes256"%>
<%@ page import="bnet.library.util.CoreUtils.exception"%>
<%@ page import="bnet.library.util.CoreUtils.json"%>
<%@ page import="bnet.library.util.CoreUtils.string"%>
<%@ page import="kong.unirest.Unirest"%>
<%@ page import="kong.unirest.HttpResponse"%>
<%@ page import="org.apache.commons.lang3.BooleanUtils"%>
<%@ include file="/include/response.jsp" %>
<%
	ResourceBundle resource = ResourceBundle.getBundle("setting");

	// setting.properties 파일에 정의한 인증API HOST를 상수로 세팅한다.
	final String AUTH_API_HOST = resource.getString("api.auth.host");
	Log.info(this.getClass().getName(), String.format("- 인증 성공시 호출될 인증API URI: [%s]", AUTH_API_HOST));

	// 18.07.10 : 결과 확인을 위해 서명 원문 데이터르 가져옴
	String sSignOrigin = request.getParameter("signOrigin");
	String sSignData  = request.getParameter("sign");
	String sVIDRandom = request.getParameter("vidRandom");
	String sEncData   = request.getParameter("encData");

	if(string.isNotBlank(sSignOrigin)){
		sSignOrigin = new String(sSignOrigin.getBytes("8859_1"), "utf-8");
	}

	if (string.isNotBlank(sSignData)) {
		sSignData = URLDecoder.decode(sSignData, "utf-8");
	}
	if (string.isNotBlank(sVIDRandom)) {
		sVIDRandom = URLDecoder.decode(sVIDRandom, "utf-8");
	}
	if (string.isNotBlank(sEncData)) {
		sEncData = URLDecoder.decode(sEncData, "utf-8");
		sEncData = sEncData.replaceAll("&amp;quot;", "\"").replaceAll("&quot;", "\""); // 암호화 데이터
	}

	Log.info(this.getClass().getName(), "- Request Parameter[signOrigin] : ["+sSignOrigin+"]");
	Log.info(this.getClass().getName(), "- Request Parameter[sign] : ["+sSignData+"]");
	Log.info(this.getClass().getName(), "- Request Parameter[vidRandom] : ["+sVIDRandom+"]");
	Log.info(this.getClass().getName(), "- Request Parameter[encData] : ["+sEncData+"]");

	// 공동인증서 인증결과 정보 저장 변수
	String sSignResult = "";
	String sSignerDn = "";
	String sIssuerDn = "";
	String sSerialNumber = "";
	String sCertificatePolicies = "";
	String sPolicy = "";
	String sIdentifyData = "";
	String sJurirno = "";

	// 처리 결과 출력 변수
	String smessage = "";  // 처리결과 메시지
	Map<String, String> mResult = new HashMap<>();

	// 서명 데이타가 있을때 서명 검증
	if ( string.isNotBlank(sSignData) ){
		try	{
			// 개인정보 복호화 시작
			MagicE2E ml = (MagicE2E)session.getAttribute("Magie2e");
			Log.info(this.getClass().getName(), "- MagicE2E Session ["+ ml +"]");

			HashMap<String, String> mDecIdn = new HashMap<String, String>();  // 개인정보 복호화 결과
			StringBuffer sbPlain = new StringBuffer(); // 출력 버퍼

			// 암호 스트링이 있을때만 복호화
			if ( string.isNotBlank(sEncData) && ml != null ){
				int sDecryptResult = ml.decrypt(sEncData, sbPlain); // 복호화 상태값(0일 경우 정상)

				// 복호화 데이터
				String[] parts = java.net.URLDecoder.decode((String)sbPlain.toString(),"utf-8").split("&");

				if( parts.length > 1){
					for (String pair : parts) {
						String[] kv = pair.split("=");
						mDecIdn.put(kv[0], kv[1]);
					}
				}else{
					String temp = java.net.URLDecoder.decode((String)sbPlain.toString(),"utf-8");
					String[] kv = temp.split("=");
					mDecIdn.put( kv[0], kv[1] );
				}

				sJurirno = mDecIdn.get("encIdn");
			}
			// 개인정보 복호화 끝

			JCaosCheckCert jcaosCheck = new JCaosCheckCert();

			// 서버가 알고 잇는 사업자등록번호를 등록한다
			jcaosCheck.setVIDRandom(mDecIdn.get("encIdn"), sVIDRandom);

			// 서명 검증
			// 검증후 원문이 리턴됨
			int iResult = jcaosCheck.checkCert(sSignData);

			Log.debug(this.getClass().getName(), "- 서명검증 처리결과값: ["+ iResult +"]");

			/*
			- JCaosCheckCert.checkCert 의 에러코드는 하기와 같습니다.
			JCaosCheckCert.STAT_OK										// 성공
			JCaosCheckCert.STAT_ERR_WRONGCERT							// 정상적인 인증서가 아님
			JCaosCheckCert.STAT_ERR_ETC									// 기타 오류
			JCaosCheckCert.STAT_ERR_VerifyException 					// 서명 검증 실패
			JCaosCheckCert.STAT_ERR_CertificateNotYetValidException 	// 인증서 유효기간 검증 오류
			JCaosCheckCert.STAT_ERR_CertificateExpiredException 		// 인증서 만료
	 		JCaosCheckCert.STAT_ERR_ObtainCertPathException				// 인증서 경로 구축 실패
			JCaosCheckCert.STAT_ERR_BuildCertPathException 				// 인증서 경로 구축 실패
			JCaosCheckCert.STAT_ERR_TrustRootException 					// 신뢰할수 없는 최상위 인증서
			JCaosCheckCert.STAT_ERR_ValidateCertPathException			// 인증서 경로 검증 실패
			JCaosCheckCert.STAT_ERR_RevokedCertException				// 폐지된 인증서
			JCaosCheckCert.STAT_ERR_RevocationCheckException			// CRL 검증 실패
			JCaosCheckCert.STAT_ERR_NotExistSignerCertException			// 서명자 인증서 누락
			JCaosCheckCert.STAT_ERR_IOException							// IOException
			JCaosCheckCert.STAT_ERR_FileNotFoundException				// FileNotFoundException
			JCaosCheckCert.STAT_ERR_NoSuchAlgorithmException			// NoSuchAlgorithmException
			JCaosCheckCert.STAT_ERR_NoSuchProviderException 			// NoSuchProviderException
			JCaosCheckCert.STAT_ERR_ParsingException        			// ParsingException
			JCaosCheckCert.STAT_ERR_IdentifyException       			// 본인확인 실패
			*/
			if( iResult == 0 ){
				smessage = "인증서 검증 성공";
			}else if( iResult == 3000 ){
				smessage = "인증서 검증 하지않음";
			}else if (  iResult != 0 ){
				// 오류 발생시 오류를 구분
				String sCertResult = null;
				switch(iResult){
					case JCaosCheckCert.STAT_ERR_WRONGCERT							:	// 정상적인 인증서가 아님
						sCertResult = "서명에 사용된 인증서가 정상적인 인증서가 아닙니다.";
						break;
					case JCaosCheckCert.STAT_ERR_RevocationCheckException			:	// CRL 검증 실패
					case JCaosCheckCert.STAT_ERR_NotExistSignerCertException		:	// 서명자 인증서 누락
					case JCaosCheckCert.STAT_ERR_IOException						:	// IOException
					case JCaosCheckCert.STAT_ERR_FileNotFoundException				:	// FileNotFoundException
					case JCaosCheckCert.STAT_ERR_ETC								:	// 기타 오류
					case JCaosCheckCert.STAT_ERR_BuildCertPathException 			:	// 인증서 경로 구축 실패
					case JCaosCheckCert.STAT_ERR_ObtainCertPathException			:	// 인증서 경로 구축 실패
					case JCaosCheckCert.STAT_ERR_ValidateCertPathException			:	// 인증서 경로 검증 실패
					case JCaosCheckCert.STAT_ERR_TrustRootException 				:	// 신뢰할수 없는 최상위 인증서
						sCertResult = "서명 인증서 검증 오류 ["+iResult+"].";
						break;
					case JCaosCheckCert.STAT_ERR_VerifyException 					:	// 서명 검증 실패
						sCertResult = "서명 검증 실패";
						break;
					case JCaosCheckCert.STAT_ERR_CertificateNotYetValidException	: 	// 인증서 유효기간 검증 오류
						sCertResult = "서명 인증서 유효기간 검증 오류";
						break;
					case JCaosCheckCert.STAT_ERR_CertificateExpiredException 		:	// 인증서 만료
						sCertResult = "만료된 인증서 ";
						break;
					case JCaosCheckCert.STAT_ERR_RevokedCertException				:	// 폐지된 인증서
						sCertResult = "폐지된 인증서";
						break;
					default:
						sCertResult = "기타오류 ["+iResult+"]";
						break;
				}
				smessage = ""+sCertResult+" \n[" + jcaosCheck.getLastErr() +"]";

			}

			// 정상 인증서 인 경우 본인확인 검증 수행
			Boolean isAuthSucess = null;  // 본인확인성공여부
			if( iResult == 0 ){

				// 서명에 사용된 인증서를 가져온다
				X509Certificate cert = jcaosCheck.getUserCert();
				sSignerDn = cert.getSubjectDN().getName();	// 인증서 DN
				BigInteger serialNumber = cert.getSerialNumber();	// 인증서 시리얼

				// 본인확인 결과 세팅
				switch (jcaosCheck.getVIDCheck()){
					case JCaosCheckCert.STAT_VID_NOTCHECK:
						isAuthSucess = false;
						smessage = "본인 확인을 하지 않았습니다.";
						break;
					case JCaosCheckCert.STAT_VID_CHECK_FAIL:
						isAuthSucess = false;
						smessage = "본인 확인에 실패하였습니다.";
						break;
					case JCaosCheckCert.STAT_VID_CHECK_OK:
						isAuthSucess = true;
						smessage += " \n[본인 확인 성공]";
						break;
		 		}

				// 정상 인증서이고 본인확인도 성공하면 본인 식별 정보 생성
				if( BooleanUtils.isTrue(isAuthSucess) ){

					// 서명 값 (Base64)
					String base64SignData = sSignData;

					// 서명 원문을 가져온다
					sSignResult = new String( jcaosCheck.getSrcByte(), "utf-8" );

					// URL 형식 문자열을 잘라서 서명원문을 가져온다
					// ex) sign=&signOrigin=1234&vidRandom=&vidType=client&encData=&signData=1234&idn=
					//String signResultVal = signResult.substring(signResult.indexOf("signData="));
					//sSignResult = signResultVal.substring(signResultVal.indexOf("=")+1, signResultVal.indexOf("&idn="));

					//byte[] textByte = Base64.decode(textCheck);
					//sSignResult = new String(textByte, "utf-8");

					Properties props = new Properties();
					props.load(getServletContext().getResourceAsStream("WEB-INF/magicline/message/Messages.properties"));

					sPolicy = props.getProperty("OID_" + cert.getCertificatePolicies().getPolicyIdentifier(0).replace(".", "_"));

					// 본인확인 식별값 생성
					ArrayList generalNames = cert.getSubjectAlternativeName();
					if (generalNames != null && generalNames.size() > 0)
					{
						X509GeneralName genName;
						for (int i=0; i<generalNames.size(); i++) {
							genName = (X509GeneralName)generalNames.get(i);
							if (genName.getType() == X509GeneralName.TYPE_OTHER_NAME) {
								String identifyData = genName.getStringName();

								X509OtherName otherName = X509OtherName.getInstance(((X509GeneralName)generalNames.get(i)).getOtherName());
								VID vid = VID.getInstance(otherName.getIdentifyData().getVid());
								sIdentifyData = new String(Hex.encode(vid.getVirtualID()));
							}
						}
					}

					sIssuerDn = cert.getIssuerDN().getName();
					sSerialNumber = cert.getSerialNumber().toString(16);
					sCertificatePolicies = cert.getCertificatePolicies().getPolicyIdentifier(0);
				}

			}

			if ( BooleanUtils.isNotTrue(isAuthSucess) ) {
				mResult.put("code", "-8000");  // 본인확인 실패한 경우 에러코드 정의
			}
			else {
				mResult.put("code", Integer.toString(iResult));	// 인증서 검증(서명 검증) 결과값 코드 정의
			}
			mResult.put("message", smessage);
		}catch(Exception e){
			// 인증서 검증중 오류가 난 경우
			// 처리를 편하게 하기 위해
			// 상용중에는 사용자의 인증서의 유효성의 문제가 잇는 경우가 대부분 입니다.
			//
			Log.error(this.getClass().getName(), exception.getStackTraceString(e));
			mResult.put("code", "-9998");
			mResult.put("message", "서명 검증에 실패 하였습니다.\n [" + e.getMessage()+"]");
		}

		if ( string.equals(mResult.get("code"), "0") ) {

		 	// ai-member API의 공동인증서 결과 세션 저장 API 호출
			try {
				// HTTP 통신 Parameter
				Map<String, String> jsonParams = new HashMap<>();
				jsonParams.put("signData", sSignData);  // SignData
				jsonParams.put("vidData", sVIDRandom);  // VIDData
				jsonParams.put("signerDn", sSignerDn);  // 사용자 DN
				jsonParams.put("issuerDn", sIssuerDn);  // 발급자 DN
				jsonParams.put("serialNumber", sSerialNumber);  // 인증서 SN
				jsonParams.put("policyIdentifier", sCertificatePolicies);  // 인증서 정책
				jsonParams.put("policy", sPolicy);  // 인증서 구분
				jsonParams.put("identifyData", sIdentifyData);  // 본인확인 식별값

				if (string.isNotBlank(sJurirno)) {
					jsonParams.put("encBizrno", aes256.encrypt(sJurirno, sIdentifyData));  // 암호화된 개인정보로부터 추출한 '사업자등록번호'를 aes256으로 암호화한다.
				}
				else {
					jsonParams.put("encBizrno", "");
				}

				// 전자서명 원문 일치여부
				Log.info(this.getClass().getName(), "원문비교(1) : ["+sSignOrigin+"]");
				Log.info(this.getClass().getName(), "원문비교(2) : ["+sSignResult+"]");
				if (string.equals(sSignOrigin, sSignResult)) {
					jsonParams.put("isSigned", BooleanUtils.toStringTrueFalse(true));
				}
				else {
					jsonParams.put("isSigned", BooleanUtils.toStringTrueFalse(false));
				}

				Log.info(this.getClass().getName(), "HTTP Parameter : ["+json.toString(jsonParams)+"]");

			 	// HTTP 호출
		    	String url = AUTH_API_HOST + "/api/contact-module/pki-cert/save-signed";
		    	HttpResponse<String> res = Unirest.post(url)
		    			.contentType("application/json")
		    			.body(jsonParams)
		    			.asString();

		    	if (res.getStatus() != 200) {
		    		String errs = res.getBody();
		    		Map<String, Object> jsonErrs = json.toObject(errs, HashMap.class);

		    		Log.error("Response Error : \n" + json.toString(jsonErrs));

		    		mResult.put("code", "-9996");
		    		mResult.put("message", (String) jsonErrs.get("message"));
		    	}
		    	else {
			    	String body = res.getBody();
			    	Map<String, Object> jsonBody = json.toObject(body, HashMap.class);

			    	// 정상 처리 시 인증API에서 생성된 CMMT_SESSION 테이블 PK 값
			    	mResult.put("key", (String) jsonBody.get("key"));
		    	}
		 	} catch (Exception e) {
		 		Log.error(this.getClass().getName(), exception.getStackTraceString(e));
				mResult.put("code", "-9997");
				mResult.put("message", "인증API로부터 받은 데이터 분석 중 오류가 발생하여 작업을 중단하였습니다.");
		 	}
		}

	}else{
		mResult.put("code", "-9999");
		mResult.put("message", "서명 데이타가 존재하지 않습니다.");
	}

	out.print(json.toString(mResult));
%>