# 프로젝트 설명
- 본 프로젝트는 사용자지원포털 공동인증서(PKI) 솔루션(MagicLine4Web)을 연계하기 위하여 구동된 Monolithic was 웹서비스 프로젝트이다.

# local 개발환경에서의 tomcat 실행 방법
1. 프로젝트의 [Run As > Maven Build] 클릭한다.
2. Edit Configuration 창에서 아래 정보를 추가 입력한다.
 - Goals : clean package tomcat9:run-war
 - Profiles : local
3. [Apply] 버튼을 클릭하여 저장한다.
4. [Run] 버튼을 클릭하여 Maven Build 및 Tomcat 실행한다.


# local 개발환경에서의 tomcat 종료 방법
1. Console에서 앞서 생성한 Maven Build 명칭으로 실행되어진 Console 창을 선택한다.
2. Console 창의 우상단 메뉴들 중 Terminate 버튼([■])을 클릭하여 종료한다.