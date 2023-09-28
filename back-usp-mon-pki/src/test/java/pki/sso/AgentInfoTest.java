package pki.sso;

import org.junit.Test;

public class AgentInfoTest {

    @Test
    public void test() {
    	AgentInfo agentInfo = new AgentInfo("", "");
        System.out.println(agentInfo.toString());

        // 사업자등록번호 검증 테스트
        String compNumber = "3334448887";
        int hap = 0;
        int temp = 0;
        int check[] = {1,3,7,1,3,7,1,3,5};  //사업자번호 유효성 체크 필요한 수

        for(int i=0; i < 9; i++){
            if(compNumber.charAt(i) < '0' || compNumber.charAt(i) > '9') {
                continue;
            }

            hap = hap + (Character.getNumericValue(compNumber.charAt(i)) * check[temp]); //검증식 적용
            temp++;
        }

        hap += (Character.getNumericValue(compNumber.charAt(8))*5)/10;

        System.out.println("" + (10 - (hap%10))%10 + "==" + Character.getNumericValue(compNumber.charAt(9)));
    }

}
