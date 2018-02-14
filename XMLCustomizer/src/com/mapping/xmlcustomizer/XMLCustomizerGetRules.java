package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.OutputStream;

import com.sap.aii.mapping.api.StreamTransformationException;

public class XMLCustomizerGetRules {

	public String[] executeGetRules() throws StreamTransformationException {

		// This method will acquire the rule parameters
		// from value mapping cache in PI

		String operation = "addNode";
		String arg0 = "/Message/Body/Section[@id='2']/Item[Qualf='C']/Field3";
		String arg1 = "Constant";
		String arg2 = "/Message/Body/Section[@id='2']/Item[Qualf='C']/Field1";
		String arg3 = "";

		return new String[] { operation, arg0, arg1, arg2, arg3 };
	}
}
