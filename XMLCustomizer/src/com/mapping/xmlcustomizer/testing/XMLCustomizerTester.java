package com.mapping.xmlcustomizer.testing;

import java.io.*;

import com.mapping.xmlcustomizer.XMLCustomizer;

//import com.sap.aii.mapping.api.TransformationInput;

public class XMLCustomizerTester {

	public static void main(String[] args) {

		try {
			InputStream in = new FileInputStream(new File("in.xml"));
			OutputStream out = new FileOutputStream(new File("out.xml"));

//			byte[] buffer = new byte[1024];
//			int len;
//			while ((len = in.read(buffer)) != -1) {
//				out.write(buffer, 0, len);
//			}

			XMLCustomizer myMapping = new XMLCustomizer();
			myMapping.execute(in, out);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
