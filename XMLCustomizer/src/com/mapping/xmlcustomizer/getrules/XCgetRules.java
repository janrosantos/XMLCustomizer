package com.mapping.xmlcustomizer.getrules;

import java.util.Arrays;

import com.mapping.xmlcustomizer.XMLCustomizer;
import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;

public class XCgetRules extends XMLCustomizer {

	String delimiter = "~@#~";
	String senderAgency = "VMR_Key";
	String senderScheme = "VMR_Source";
	String receiverAgency = "VMR_Return";
	String receiverScheme = "VMR_Target";
	String context = "";

	public String[][] executeXCgetRules(StringBuilder in, AbstractTrace trace) throws StreamTransformationException {

		trace.addInfo("Acquiring rules for XML Customizer");

		String docType = "";
		String[] initVMkey = new String[] { "", "", "", "" };
		String[] blankVMkey = new String[] { "", "", "", "" };
		String[][] XCrules = new String[][] {};

		try {

			XCgetDocType getDocType = new XCgetDocType();
			String[] initVariables = getDocType.executeXCgetDocType(in);
			docType = initVariables[0];

			trace.addInfo(initVariables[0] + initVariables[1] + initVariables[2] + initVariables[3] + initVariables[4]);

			System.out.println(initVariables[0] + initVariables[1] + initVariables[2] + initVariables[3]
					+ initVariables[4]);

			if (docType.equals("IDOC")) {
				XCinitIDOC initIDOC = new XCinitIDOC();
				initVMkey = initIDOC
						.executeXCinitIDOC(initVariables[0], initVariables[1], initVariables[2], initVariables[3],
								initVariables[4], initVariables[5], initVariables[6], initVariables[7], trace);
			}

			if (!Arrays.equals(blankVMkey, initVMkey)) {

				XCgetVM getVM = new XCgetVM();
				XCrules = new String[][] { { getVM.executeGetVM(initVariables[1], 1),
						getVM.executeGetVM(initVariables[1], 2), getVM.executeGetVM(initVariables[1], 3),
						getVM.executeGetVM(initVariables[1], 4), getVM.executeGetVM(initVariables[1], 5) } };

			} else {

				// No rules acquired
				// getTrace().addInfo("No rules acquired from PI cache");
				XCrules = new String[][] {};

			}

		} catch (Exception exception) {

			// TODO: handle exception
			trace.addInfo("Class XCgetRules - error encountered: " + exception);
			System.out.println("Class XCgetRules - error encountered: " + exception);

		}

		return XCrules;

	}
}
