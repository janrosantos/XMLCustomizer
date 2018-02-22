package com.mapping.xmlcustomizer.functions;

public class XCpadArray {

	public static String[][] executeXCpadArray(String[][] arr, String padWith, int numOfPads) {
		String[][] temp = new String[arr.length][numOfPads];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {

				try {
					if (!arr[i][j].equals("")) {
						temp[i][j] = arr[i][j];
					}
				} catch (Exception e) {

					temp[i][j] = padWith;

				}

			}
		}
		return temp;
	}

}
