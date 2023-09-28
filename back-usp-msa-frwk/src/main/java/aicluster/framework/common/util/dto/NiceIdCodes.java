package aicluster.framework.common.util.dto;

public class NiceIdCodes {
	public static class gender {
		public static final String 여성 = "0";
		public static final String 남성 = "1";
	}
	public static class nationInfo {
		public static final String 내국인 = "0";
		public static final String 외국인 = "1";
	}

	public static class mobileCo {
		public static String getMobileCoNm(String mobileCo) {
			switch (mobileCo) {
			case "SKT":
				return "SKT";
			case "KTF":
				return "KT";
			case "LGT":
				return "LGU+";
			case "SKM":
				return "SKT알뜰폰";
			case "KTM":
				return "KT알뜰폰";
			case "LGM":
				return "LGU+알뜰폰";
			default:
				return "알수없음";
			}
		}
	}
}
