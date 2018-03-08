package com.grundfos.mapping.xc.functions;

public class XCpadArray2D {

	public static String[][] executeXCpadArray2D(String[][] arr, String padWith, int arrWidth) {
		String[][] temp = new String[arr.length][arrWidth];
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
