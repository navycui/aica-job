<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="ko">
<head>
<title> 인증서 선택 </title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no" />

<!-- Draw -->
<script type="text/javascript" src="UI/js/ML4Web_Draw.js"></script>
<script type="text/javascript" src="UI/js/ML4Web_Main.js"></script>
<script type="text/javascript" src="UI/js/ML4Web_Popup.js"></script>

<script type="text/javascript">
//IE Caps Lock 체크 해제
document.msCapsLockWarningOff = true;

var contextPath = '<%=request.getContextPath()%>';
// moved to ML_Draw.js all of func
$(document).ready(function(){
	// TODO : 초기화 함수 호출
	ML4WebDraw.loadCSS();
	ML4WebDraw.initDraw();
	
});
</script>


</head>
<body>
<div id="ML_window" style="display:none;">
	<div style="width:418px; float:left;"></div>
	<div id="ML_container" style="width:418px; float:left;"><!-- ML_container : S -->
		<div class="ML_content" style="width:418px;float:left;"><!-- ML_content : S -->
			<div class="ML_content-area" id="MagicLine" style="padding-top:5px;"><!-- ML_content-area : S -->
			<div id='ESignWindow'></div>
				<div class="ML_cp_AD" style="display:;"><img src="UI/images/ML5_banner.png" alt="MagicLine4Web 인증서 선택창 로고"></div>
				<div id="ML_dp_01" class="ML_sub_tit">인증서 저장 위치 선택
					<span class="ML_sub_tit_link">
						<span class="btn_cell_right">
							<p class="hp_btn"><button type="button" id='btn_browser_manual' style="width:160px;" title="새창열림"><span><img src="UI/images/icon_help.png" alt="">브라우저 인증서 사용방법</span></button></p>
						</span>
					</span>
				</div>
				<div class="ML_storage_box">
					<div class="ML_storage_box_sub">
						<!-- <form> -->
						<div class="MLstrSlide" id="MLstrSlide">
							<!-- // -->
						</div>
						<!-- </form> -->
					</div>
					<div class="customNavigation" id="0"><div class="str_add_li ne"><a class="next"><span class="btn_str_add"></span></a></div></div>
					<!-- Drive list : S -->
					<div id="driver_div" class="drive_position pos_05"  style="display:none;">
						<!-- // -->
					</div>
					<!-- Drive list : E -->
				</div>
				<!-- TRACE #1 -->
				<!-- Certificate list : S -->
				<div id="ML_dp_02"  class="ML_sub_tit">사용할 인증서 선택</div>
				<div id="ML_certlist_area" class="ML_cert_list_area">
					<div id="dataTable"></div>
				</div>
				<!-- BTN : S -->
				<span id="ML_dp_03" class="btn_row_s">
					<span class="btn_cell_left">
						<p id="flip" class="whtgrey_ws_btn"><button type="button" id=btn_deleteCert style="display:none; width:80px;"><span>인증서 삭제</span></button></p>
						<p id="flip" class="whtgrey_ws_btn"><button type="button" id="btn_CopyCert" style="display:none; width:80px;"><span>인증서 복사</span></button></p>
						<!-- <p id="flip" class="whtgrey_ws_btn2"><button type="button" id="btn_PFX"><span><img src="UI/images/icon_search.png" alt="pfx 찾기">PFX 인증서 가져오기</span></button></p> -->
					</span>
					<span class="btn_cell_right">
						<p id="btn_browser" class="whtgrey_ws_btn2"><button type="button" id="in_browser"><span id="get_cert"></span></button></p>
					</span>
				</span>
				<!-- BTN : E -->
				<!-- Certificate list : E -->

				<div id="ML_dp_04" class="ML_sub_tit">인증서 비밀번호 입력</div>
				<div id="ML_status_area" class="ML_status_area">
					<div class="ML_pw_area">
						<div class="ML_cert_pw_area">
							<span class="ML_cert_pw_field">
								<!-- <input type="password" id="input_cert_pw" class="passwd_input" style="ime-mode:active;" placeholder="비밀번호를 입력하시오." autocomplete="off" value="" onKeypress="signEnterKeyEvent(event)"/> -->
								<input type="password" id="input_cert_pw" class="passwd_input" data-tk-kbdType="qwerty" title="인증서 비밀번호 입력" placeholder="비밀번호를 입력하시오." autocomplete="off" value="" onKeypress="signEnterKeyEvent(event)" npkencrypt="on" data-keypad-type="alpha" data-keypad-theme="default" data-keypad-useyn-type="toggle" data-keypad-useyn-input="keyboardOn" />
								<span id="capslock" class="capslock_box">＜Caps Lock＞이 켜져 있습니다.</span>
								<!-- <span id="input_cert_pw_tk_btn">마우스입력</span> -->
								<span class="ML_cert_pw_key_box"><a href="#" id="keyboardOn" title="보안키보드"><div class="ML_cert_pw_keypad"></div></a></span>
							</span>
						</div>
						<!-- BTN -->
						<span class="btn_row">
							<p class="b_blue_btn"><button type="button" id="btn_confirm_iframe"><span>확인</span></button></p>
							<span class="w_space10"></span>
							<p class="b_grey_btn"><button type="button" id="btn_cancel"><span>취소</span></button></p>
							<span class="w_space10"></span>
							<p class="b_grey_btn"><button type="button" id="btn_viewCert"><span class="MSG_cls" id="MSG_TS017">인증서 보기</span></button></p>
						</span>
						<!-- BTN -->
					</div>
				</div>
			</div><!-- ML_content-area : E -->
		</div><!-- ML_content : E -->
	</div><!-- ML_container : E -->
</div>
<!-- 브라우저 메뉴얼에 타이틀바 없을 때 browser_menual3.png(250x558) 
            타이틀바 있을 때 browser_menual.png(250x523) 사용 -->
<!-- <div id="browser_manual" style="display:none;">
	<div class="ML_container">
		<div class="ML_content">
			<div class="ML_content-area">
				<img src="UI/images/browser_menual3.png" alt="브라우저 인증서 사용방법" class="MLjqui-window-close-button MLjqui-icon-close">
			</div>
		</div>
	</div>
</div> -->

<!-- 00. 인증서 Action Type 설정(MakeSignData, envelope, sign&envelope) -->
<input type="hidden" id="certaction" value="" />

<!-- 수정 : 디폴트 스토리지 hdd일때 제일 앞의 디스크 선택 판단하기 위한 변수 저장 -->
<input type="hidden" id="flagInit" value="0" />
<input type="hidden" id="temp" value="" />

<!-- 01. Alert dialog -->
<div id="popup_alert" class="ML_container_dialog" style="display:none;">
	<div class="ML_content">
		<div class="ML_content-area" style="padding-top:5px;">
			<div class="ML_pw_dialog_status_area">
				<div class="ML_pw_dialog_area">
					<div id="alert_msg" class="ML_pw_dialog_txt">비밀번호를 다시 입력하세요.</div>
					<!-- BTN -->
					<span class="btn_row">
						<p class="b_blue_btn"><button type="button" id="btn_alert_confirm" onClick="DSAlert.closeAlert();"><span>확인</span></button></p>
						<!--<span class="w_space40"></span>
						<p class="b_grey_btn"><button type="button" onClick="selfClose();"><span>취소</span></button></p>-->
					</span>
					<!-- BTN -->
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 02. Common -->
	<div id="ML_Dialog_common" style="display:none;">
		<div class="ML_content">
			<div id="ML_content_area" class="ML_content-area" style="padding-top: 5px; padding-bottom: 0px">
				<!-- Content : S -->

				<!-- Content : E -->
			</div>
			<div class="ML_content-area ML_pw_popup_area" style="padding-top: 5px;" id="btnArea">
				<!-- BTN -->
				<span class="btn_row"  id="btn_row_area">
					<p class="b_blue_btn">
						<button type="button" id="btn_common_confirm" onClick="DSDialog.releaseDialog();">
							<span>확인</span>
						</button>
					</p>
					<span class="w_space40"></span>
					<p class="b_grey_btn">
						<button type="button" id="btn_common_cancle" onClick="DSDialog.closeDialog(function(code,obj){});" >
							<span>취소</span>
						</button>
					</p>
				</span>
				<!-- BTN -->
			</div>
		</div>
	</div>
	
<!-- 03. Cs install dialog -->
	<div id="ML_dialog_cs_install" style="display:none;">
		<div class="ML_content">
			<div id="ML_content_area_cs" class="ML_content-area" style="padding-top: 5px; padding-bottom: 0px">
			</div>
			<div class="ML_content-area ML_pw_popup_area" style="padding-top: 5px;" id="btnArea">
				<span class="btn_row">
					<p class="b_blue_btn2">
						<button type="button" id="btn_common_confirm" onClick="DSDialog.releaseDialog();">
							<span>설치</span>
						</button>
					</p>
					<span class="w_space20"></span>
					<p class="b_blue_btn2">
						<button type="button" id="btn_common_run" onClick="DSDialog.runDialog();">
							<span>실행</span>
						</button>
					</p>
					<span class="w_space20"></span>
					<p class="b_grey_btn2">
						<button type="button" id="btn_common_cancle" onClick="DSDialog.closeDialog(function(code,obj){});" >
							<span>취소</span>
						</button>
					</p>
				</span>
			</div>
		</div>
	</div>
	
</body>
</html>
