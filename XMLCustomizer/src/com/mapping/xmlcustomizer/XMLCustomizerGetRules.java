package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.OutputStream;

import com.sap.aii.mapping.api.StreamTransformationException;

public class XMLCustomizerGetRules {

	public String[][] executeGetRules() throws StreamTransformationException {

		// This method will acquire the rule parameters
		// from value mapping cache in PI

		String operation = "deleteNode";
		String arg0 = "/Message/Body/Section[@id='2']/Item[Qualf='C']";
		String arg1 = "Field3";
		String arg2 = "TEST NEW VALUE";
		String arg3 = "/Message/Body/Section[@id='2']/Item[Qualf='C']/Field1";

		return new String[][] { { operation, arg0, arg1, arg2, arg3 }, { operation, arg0, arg1, arg2, arg3 } };
	}
}
