package com.mapping.xmlcustomizer.testing;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.mapping.xmlcustomizer.XMLCustomizer;

public class XMLCustomizerTester {

	public static void main(String[] args) {

		// Tester class only for offline execution of the Java map

		try {

			// Read/assign input and output XML files
			InputStream in = new FileInputStream(new File("inIDOC.xml"));
			OutputStream out = new FileOutputStream(new File("out.xml"));

			// Execute java map
			XMLCustomizer myMapping = new XMLCustomizer();
			myMapping.customizeXML(in, out);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}
