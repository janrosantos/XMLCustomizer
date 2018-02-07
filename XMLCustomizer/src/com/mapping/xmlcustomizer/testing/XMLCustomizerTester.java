package com.mapping.xmlcustomizer.testing;

import java.io.*;


public class XMLCustomizerTester {

	public static void main(String[] args) {

	    try {
	      FileInputStream in = new FileInputStream(new File("in.xml"));
	      FileOutputStream out = new FileOutputStream(new File("out.xml"));
//	      XMLCustomizer myMapping = new XMLCustomizer();
//	      myMapping.transform(in, out);
	      
	      out.write(in.read());
	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	
}