package com.mapping.xmlcustomizer.functions;

public class XCpadArray1D {

	public static String[] executeXCpadArray1D(String[] arr, String padWith, int arrWidth) {
		String[] temp = new String[arrWidth];
		for (int j = 0; j < temp.length; j++) {

			try {
				if (!arr[j].equals("")) {
					temp[j] = arr[j];
				}
			} catch (Exception e) {

				temp[j] = padWith;

			}

		}

		return temp;
	}

}
