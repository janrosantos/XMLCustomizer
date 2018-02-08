package com.mapping.xmlcustomizer.testing;

import java.io.*;

import com.mapping.xmlcustomizer.XMLCustomizer;

//import com.sap.aii.mapping.api.TransformationInput;

public class XMLCustomizerTester {

	public static void main(String[] args) {

		try {
			InputStream in = new FileInputStream(new File("in.xml"));
			OutputStream out = new FileOutputStream(new File("out.xml"));

			String operation = "addNode";
			String arg0 = "";
			String arg1 = "";
			String arg2 = "";
			String arg3 = "";

			XMLCustomizer myMapping = new XMLCustomizer();
			myMapping.execute(operation, arg0, arg1, arg2, arg3, in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
