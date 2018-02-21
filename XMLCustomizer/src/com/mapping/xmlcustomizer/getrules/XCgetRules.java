package com.mapping.xmlcustomizer.getrules;

import java.io.InputStream;
import java.util.Arrays;

import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mappingtool.tf7.rt.Container;

public class XCgetRules {

	String delimiter = "~@#~";
	String senderAgency = "VMR_Key";
	String senderScheme = "VMR_Source";
	String receiverAgency = "VMR_Return";
	String receiverScheme = "VMR_Target";
	String context = "";

	public String[][] executeXCgetRules(InputStream in, Container container) throws StreamTransformationException {

		String docType = "";
		String[] initVMkey = new String[] { "", "", "", "" };
		String[] blankVMkey = new String[] { "", "", "", "" };
		String[][] XCrules = new String[][] {};

		try {

			XCgetDocType getDocType = new XCgetDocType();
			String[] initVariables = getDocType.executeXCgetDocType(in);

			if (docType.equals("IDOC")) {
				XCinitIDOC initIDOC = new XCinitIDOC();
				initVMkey = initIDOC.executeXCinitIDOC(initVariables[0], initVariables[1], initVariables[2],
						initVariables[3], initVariables[4], initVariables[5], initVariables[6], initVariables[7],
						container);
			}

			if (!Arrays.equals(blankVMkey, initVMkey)) {

				XCgetVM getVM = new XCgetVM();
				XCrules = new String[][] { { getVM.executeGetVM(initVariables[1], 1, container),
						getVM.executeGetVM(initVariables[1], 2, container),
						getVM.executeGetVM(initVariables[1], 3, container),
						getVM.executeGetVM(initVariables[1], 4, container),
						getVM.executeGetVM(initVariables[1], 5, container) } };

			} else {

				// No rules acquired
				XCrules = new String[][] {};

			}

		} catch (Exception e) {

			// TODO: handle exception

		}

		return XCrules;

	}
}
