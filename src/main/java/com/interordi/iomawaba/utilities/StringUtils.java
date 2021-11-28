package com.interordi.iomawaba.utilities;

public class StringUtils {

	public static String strJoin(String[] aArr, String sSep) {
		return strJoin(aArr, sSep, 0);
	}
	
	
	public static String strJoin(String[] aArr, String sSep, int startPos) {
		if (aArr.length <= startPos)
			return "";
		
		StringBuilder sbStr = new StringBuilder();
		for (int i = startPos, il = aArr.length; i < il; i++) {
			if (i > startPos)
				sbStr.append(sSep);
			sbStr.append(aArr[i]);
		}
		return sbStr.toString();
	}
	
}
