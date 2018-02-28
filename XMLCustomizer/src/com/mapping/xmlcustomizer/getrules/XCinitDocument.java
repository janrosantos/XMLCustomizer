package com.mapping.xmlcustomizer.getrules;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;

public class XCinitDocument {

	public String[] executeXCinitDocument(String[] initDocKey, AbstractTrace trace)
			throws StreamTransformationException {

		String initTable = initDocKey[0];
		String direction = initDocKey[1];
		String standard = initDocKey[2];
		String message = initDocKey[3];
		String version = initDocKey[4];
		String vmKeyInput1 = initDocKey[5];
		String vmKeyInput2 = initDocKey[6];
		String vmKeyInput3 = initDocKey[7];
		String vmKeyInput4 = initDocKey[8];
		String vmKeyInput5 = initDocKey[9];
		String vmKeyInput6 = initDocKey[10];

		String delimiter = "~@#~";
		String senderAgency = "VMR_Key";
		String senderScheme = "VMR_Source";
		String receiverAgency = "VMR_Return";
		String receiverScheme = "VMR_Target";
		String context = "";

		trace.addInfo("Class XCinitDocument: Initialize document VM keys");

		try {

			IFIdentifier vmsetSource = XIVMFactory.newIdentifier("http://janro.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetDestination = XIVMFactory.newIdentifier("http://janro.com/vmrset", receiverAgency,
					receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetSource, vmsetDestination, "0.0.VMRSET");
			context = "http://janro.com/vmr/" + vmset;
			trace.addInfo("Class XCinitDocument: VMR Set: " + context);

		} catch (ValueMappingException e) {

			trace.addInfo("Class XCinitDocument: No ValueMapping found for " + "0.0.VMRSET");
			return new String[] { "", "", "", "" };

		}

		String vmKey = "";
		String vmReturn = "";

		IFIdentifier source = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier destination = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme);

		try {

			// Try unified init lookup
			vmKey = initTable + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter
					+ delimiter + vmKeyInput1 + delimiter + vmKeyInput2 + delimiter + vmKeyInput3 + delimiter
					+ vmKeyInput4 + delimiter + vmKeyInput5 + delimiter + vmKeyInput6;

			vmReturn = XIVMService.executeMapping(source, destination, vmKey);

			String L1[] = vmReturn.split("\\" + delimiter);

			String vmKeyA4 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "A4" + delimiter + delimiter;
			String vmKeyL1 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ L1[0] + delimiter + L1[1] + delimiter + L1[2];
			String vmKeyL2 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L2" + delimiter + L1[1] + delimiter;
			String vmKeyL3 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L3" + delimiter + delimiter + L1[2];
			String vmKeyL4 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L4" + delimiter + delimiter;
			String vmKeyZ4 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "Z4" + delimiter + delimiter;

			trace.addInfo("Class XCinitDocument: Initialize VM Key A4 - " + vmKeyA4);
			trace.addInfo("Class XCinitDocument: Initialize VM Key L1 - " + vmKeyL1);
			trace.addInfo("Class XCinitDocument: Initialize VM Key L2 - " + vmKeyL2);
			trace.addInfo("Class XCinitDocument: Initialize VM Key L3 - " + vmKeyL3);
			trace.addInfo("Class XCinitDocument: Initialize VM Key L4 - " + vmKeyL4);
			trace.addInfo("Class XCinitDocument: Initialize VM Key Z4 - " + vmKeyZ4);

			return new String[] { vmKeyA4, vmKeyL1, vmKeyL2, vmKeyL3, vmKeyL4, vmKeyZ4 };

		} catch (ValueMappingException epc) {

			try {

				trace.addInfo("Class XCinitDocument: No VM Key L1 found for " + vmKey + ". Trying VM Key L2.");

				// vmkey = initTable + delimiter + direction + delimiter +
				// standard + delimiter + message + delimiter
				// + version + delimiter + delimiter + delimiter + delimiter +
				// partnertype + delimiter + partner
				// + delimiter + delimiter + delimiter + delimiter;

				// Try unified init lookup
				vmKey = initTable + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter
						+ delimiter + vmKeyInput1 + delimiter + vmKeyInput2 + delimiter + delimiter + delimiter
						+ delimiter;

				vmReturn = XIVMService.executeMapping(source, destination, vmKey);

				String L2[] = vmReturn.split("\\" + delimiter);

				String vmKeyA4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "A4" + delimiter + delimiter;
				String vmKeyL1 = "";
				String vmKeyL2 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L2" + delimiter + L2[1] + delimiter;
				String vmKeyL3 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L3" + delimiter + delimiter + vmKeyInput3;
				String vmKeyL4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L4" + delimiter + delimiter;
				String vmKeyZ4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "Z4" + delimiter + delimiter;

				trace.addInfo("Class XCinitDocument: Initialize VM Key A4 - " + vmKeyA4);
				trace.addInfo("Class XCinitDocument: Initialize VM Key L1 not possible");
				trace.addInfo("Class XCinitDocument: Initialize VM Key L2 - " + vmKeyL2);
				trace.addInfo("Class XCinitDocument: Initialize VM Key L3 - " + vmKeyL3);
				trace.addInfo("Class XCinitDocument: Initialize VM Key L4 - " + vmKeyL4);
				trace.addInfo("Class XCinitDocument: Initialize VM Key Z4 - " + vmKeyZ4);

				return new String[] { vmKeyA4, vmKeyL1, vmKeyL2, vmKeyL3, vmKeyL4, vmKeyZ4 };

			} catch (ValueMappingException epg) {

				trace.addInfo("Class XCinitDocument: No VM Key L1 found for " + vmKey + ". Defaulting to VM Key L4.");

				String vmKeyA4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "A4" + delimiter + delimiter;
				String vmKeyL1 = "";
				String vmKeyL2 = "";
				String vmKeyL3 = "";
				String vmKeyL4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L4" + delimiter + delimiter;
				String vmKeyZ4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "Z4" + delimiter + delimiter;

				trace.addInfo("Class XCinitDocument: Initialize VM Key A4 - " + vmKeyA4);
				trace.addInfo("Class XCinitDocument: Initialize VM Key L1 not possible");
				trace.addInfo("Class XCinitDocument: Initialize VM Key L2 not possible");
				trace.addInfo("Class XCinitDocument: Initialize VM Key L3 - SKIP");
				trace.addInfo("Class XCinitDocument: Initialize VM Key L4 - " + vmKeyL4);
				trace.addInfo("Class XCinitDocument: Initialize VM Key Z4 - " + vmKeyZ4);

				return new String[] { vmKeyA4, vmKeyL1, vmKeyL2, vmKeyL3, vmKeyL4, vmKeyZ4 };

			}

		}

	}
}
