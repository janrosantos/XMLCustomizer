package com.mapping.xmlcustomizer;

import com.sap.aii.mapping.api.StreamTransformationException;

public class XMLCustomizerGetRules {

	public String[][] executeGetRules() throws StreamTransformationException {

		// This method will acquire the rule parameters
		// from value mapping cache in PI

		String operation = "deleteNode";
		String arg0 = "/Message/Body/Section";
		String arg1 = "";
		String arg2 = "";
		String arg3 = "";

		return new String[][] { { operation, arg0, arg1, arg2, arg3 },
				{ "addNode", "/Message/Body", "Section", arg2, arg3 },
				{ "addNode", "/Message/Body/Section", "Level1", arg2, arg3 },
				{ "addNode", "/Message/Body/Section/Level1", "Level2", arg2, arg3 },
				{ "addNode", "/Message/Body/Section/Level1/Level2", "Level3", arg2, arg3 },
				{ "addNode", "/Message/Body/Section/Level1/Level2/Level3", "Item", "Constant Value 1", arg3 },
				{ "addNode", "/Message/Body/Section/Level1/Level2/Level3", "Item", "Constant Value 2", arg3 },
				{ "addNode", "/Message/Body/Section/Level1/Level2/Level3", "Item", "Constant Value 3", arg3 },
				{ "addNode", "/Message/Body/Section/Level1/Level2/Level3", "Item", "Constant Value 4", arg3 },
				{ "addNode", "/Message/Body/Section/Level1/Level2/Level3", "Item", "Constant Value 5", arg3 },
				{ "addNode", "/Message/Body/Section/Level1/Level2/Level3", "Item", "Constant Value 6", arg3 },
				{ "addNode", "/Message/Body/Section/Level1/Level2/Level3", "Item", "Constant Value 7", arg3 },
				{ "replaceValue", "/Message/Body/Section/Level1/Level2/Level3/Item[4]", "", "Constant Value Replace", arg3 }};
		
	}
}
