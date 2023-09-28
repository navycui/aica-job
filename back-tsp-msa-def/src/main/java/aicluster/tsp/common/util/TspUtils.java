package aicluster.tsp.common.util;


import aicluster.framework.common.entity.BnMember;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;

import java.util.Date;

public class TspUtils {

	public static class stringUtils {

		/**
		 * String 을 받아서 yyyy-MM-dd 형식의 String 으로 반환
		 *
		 * ex) (String) 20220301 -> (String) 2022-03-01
		 * */
		public static String toStringDateFormat(String _date) {
			Date date = CoreUtils.string.toDate(_date);
			return CoreUtils.date.format(date, "yyyy-MM-dd");
		}
		/**
		 * yyyy-MM-dd 형식으로 받기로 되어 있기에 yyyy-MM-dd 형식으로 받은 후 '-' 문자를 지워서 yyyyMMdd 형식으로 만듬
		 *
		 * ex) (String) 2022-03-01 -> (String) 20220301
		 * */
		public static String toDateFormat(String _date) {
			return CoreUtils.string.removePattern(_date, "-");
		}
	}

	public static class DateUtils {
	}

	// 테스트용
	public static BnMember getMember(){
		BnMember worker = SecurityUtils.getCurrentMember();
		if(worker == null ){
			throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "Login Token 만료"));
//			worker = new BnMember();
//			worker.setMemberId("member-20186b1226b444b88dee46e5e356de6e");
//			worker.setMemberNm("이충혁");
//			worker.setLoginId("chlee");
//			worker.setMemberType("INDIVIDUAL");
		}
		return worker;
	}

}
