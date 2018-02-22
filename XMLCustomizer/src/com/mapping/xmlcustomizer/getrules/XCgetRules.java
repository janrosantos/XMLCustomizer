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

		trace.addInfo("Class XCgetRules: Acquiring rules for XML Customizer");

		String docType = "";
		String[] initDocKey = new String[] {};
		String[] initVMKey = new String[] { "", "", "", "" };
		String[] blankVMKey = new String[] { "", "", "", "" };
		String[][] XCrules = new String[][] { {} };

		try {

			XCgetDocType getDocType = new XCgetDocType();
			docType = getDocType.executeXCgetDocType(in, trace);

			if (docType.equals("IDOC")) {
				XCprepIDOC prepIDOC = new XCprepIDOC();
				initDocKey = prepIDOC.executeXCprepIDOC(in, trace);
			}

			XCinitDocument initDocument = new XCinitDocument();
			initVMKey = initDocument.executeXCinitDocuement(initDocKey, trace);

			if (!Arrays.equals(blankVMKey, initVMKey)) {

				XCgetVM getVM = new XCgetVM();
				XCrules = new String[][] { { getVM.executeGetVM("4.1.CUSTOM.XML", initVMKey, 1, trace),
						getVM.executeGetVM("4.1.CUSTOM.XML", initVMKey, 2, trace),
						getVM.executeGetVM("4.1.CUSTOM.XML", initVMKey, 3, trace),
						getVM.executeGetVM("4.1.CUSTOM.XML", initVMKey, 4, trace),
						getVM.executeGetVM("4.1.CUSTOM.XML", initVMKey, 5, trace),
						getVM.executeGetVM("4.1.CUSTOM.XML", initVMKey, 6, trace) } };

			} else {

				// No rules acquired
				trace.addInfo("Class XCgetRules: No rules acquired from PI cache");
				XCrules = new String[][] {};
				
			}

		} catch (Exception exception) {

			trace.addInfo("Class XCgetRules error: " + exception);
			trace.addInfo("Class XCgetRules error: Returning blank rules");
			return XCrules;
			
		}

		trace.addInfo("Class XCgetRules: " + XCrules.length + " rule(s) acquired");
		return XCrules;

	}
}
