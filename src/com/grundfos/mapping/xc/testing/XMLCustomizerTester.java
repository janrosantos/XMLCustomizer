package com.grundfos.mapping.xc.testing;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.grundfos.mapping.xc.XMLCustomizer;
import com.sap.aii.mapping.api.InputParameters;
import com.sap.aii.mapping.lookup.Channel;

public class XMLCustomizerTester {

	public static void main(String[] args) {

		// Tester class only for offline execution of the Java map

		try {

			// Read/assign input and output XML files
			InputStream in = new FileInputStream(new File("xcPostEDIFACTVendorOutbound.xml"));
			OutputStream out = new FileOutputStream(new File("out.xml"));
			InputParameters inParam = new InputParameters() {
				
				@Override
				public Object getValue(String name) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public String getString(String name) {
					// TODO Auto-generated method stub
					return "XCI";
				}
				
				@Override
				public int getInt(String name) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public Channel getChannel(String name) {
					// TODO Auto-generated method stub
					return null;
				}
			};

			// Execute java map
			XMLCustomizer myMapping = new XMLCustomizer();
			myMapping.customizeXML(in, inParam, out);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}
