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
					// TODO: handle exception
					temp[i][j] = padWith;

				}

			}
		}
		// for (int i = 0; i < arr.length; i++) {
		// for (int j = 0; j < arr[i].length; j++) {
		// temp[i + numOfPads][j + numOfPads] = arr[i][j];
		// }
		// }
		return temp;
	}

}
