package com.jszhan.commons.kern.apiext.validate;


/**
 * 
 * @description: 证件类型判断
 * @author: wujinsong
 * @time: 2014年9月22日 下午4:08:38
 */
public class CertificateValidate {

	/**
	 * 
	 * @description: 判断是否是组织机构代码
	 * @author: wujinsong
	 * @param code
	 * @return
	 */
	public static final boolean isValidEntpCode(String code) {
		int[] ws = { 3, 7, 9, 10, 5, 8, 4, 2 };
		String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String regex = "^([0-9A-Z]){8}-[0-9|X]$";
		if (!code.matches(regex)) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < 8; i++) {
			sum += str.indexOf(String.valueOf(code.charAt(i))) * ws[i];
		}

		int c9 = 11 - (sum % 11);
		String sc9 = String.valueOf(c9);

		if (11 == c9) {
			sc9 = "0";
		} else if (10 == c9) {
			sc9 = "X";
		}

		return sc9.equals(String.valueOf(code.charAt(9)));
	}
	
	/**
	 * 
	 *@description: 判断证件类型与证件长度是否一致
	 *@author: wujinsong
	 * @param certiType
	 * @param certiNum
	 * @return
	 */
	public static boolean justfyCertTypeAccordToNum(String certiType, String certiNum) {
		// 身份证号码验证，验证18位身份证号
		String CertiNum = "(\\d{18}|(\\d{17}[\\d|X]))";
		// 港澳通行证
		String Suffix = "(H|M)\\d{10}";
		// 组织机构代码
		String companyCode = "^[A-Z0-9]{8}-[A-Z0-9]{1}";
		// 护照
		String passport = "\\w{5,20}";
		// 军官证
		String officercertificate = "\\w{6,14}";
		// 台湾居住证
		String tresidencepermit = "[A-Za-z]\\w{7,13}";

		// 验证身份证
		if (certiType != null && "身份证".equals(certiType)) {
			if (!certiNum.matches(CertiNum)) {
				return false;
			}
		} else if (certiType != null && "港澳通行证".equals(certiType)) {
			// 香港身份证格式是:H1234567800
			// 澳门身份证格式是:M1234567800
			if (!certiNum.matches(Suffix)) {
				return false;
			}
		} else if (certiType != null && "组织机构代码".equals(certiType)) {
			// 组织机构代码
			if (!certiNum.matches(companyCode)) {
				return false;
			}
		} else if (certiType != null && "护照".equals(certiType)) {
			// 护照
			if (!certiNum.matches(passport)) {
				return false;
			}

		} else if (certiType != null && "军官证".equals(certiType)) {
			// 军官证
			if (!certiNum.matches(officercertificate)) {
				return false;
			}

			// 台湾居住证
		} else if (certiType != null && "台湾居住证".equals(certiType)) {
			// /8-14个字符组成，并以字母开头

			if (!certiNum.matches(tresidencepermit)) {
				return false;
			}

		} else {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @description: 获取客户类型
	 * @author: wujinsong
	 * @param code
	 * @return
	 */
//	public static final String getCustomerType(String code) {
//		boolean isOrg = isValidEntpCode(code);
//
////		if (isOrg) {
////			return PoseidonConstants.CUSTOMER_TYPE_ENTERPRISE;
////		} else {
////			return PoseidonConstants.CUSTOMER_TYPE_PERSON;
////		}
//	}

	/**
	 * 
	 * @description: 返回数字表示的客户类型1：个人，2：企业
	 * @author: wujinsong
	 * @param code
	 * @return
	 */
	public static final String getCustomerTypeRetDig(String code) {
		boolean isOrg = isValidEntpCode(code);

		if (isOrg) {
			return "2";
		} else {
			return "1";
		}
	}

	public enum CertificateType {
		/**
		 * 身份证
		 */
		D00230001("00230001"),
		/**
		 * 港澳通行证
		 */
		D00230006("00230006"),
		/**
		 * 组织机构代码
		 */
		D00230007("00230007"),
		/**
		 * 护照
		 */
		D00230002("00230002"),
		/**
		 * 军官证
		 */
		D00230003("00230003"),
		/**
		 * 台湾居住证
		 */
		D00230008("00230008"),
		/**
		 * 户口簿
		 */
		D00230004("00230004"),
		/**
		 * 学生证
		 */
		D00230005("00230005");

		String certificateType;

		public String getCertificateType() {
			return certificateType;
		}

		public void setCertificateType(String certificateType) {
			this.certificateType = certificateType;
		}

		private CertificateType(String certificateType) {
			this.certificateType = certificateType;
		}
	}
}
