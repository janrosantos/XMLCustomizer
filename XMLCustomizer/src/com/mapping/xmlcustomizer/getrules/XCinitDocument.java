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
		String vmk1 = initDocKey[5];
		String vmk2 = initDocKey[6];
		String vmk3 = initDocKey[7];
		String vmk4 = initDocKey[8];
		String vmk5 = initDocKey[9];
		String vmk6 = initDocKey[10];

		String delimiter = "~@#~";
		String senderAgency = "VMR_Key";
		String senderScheme = "VMR_Source";
		String receiverAgency = "VMR_Return";
		String receiverScheme = "VMR_Target";
		String context = "";

		trace.addInfo("Class XCinitDocument: Initialize document VM keys");

		try {

			IFIdentifier vmsetsrc = XIVMFactory.newIdentifier("http://janro.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetdst = XIVMFactory
					.newIdentifier("http://janro.com/vmrset", receiverAgency, receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetsrc, vmsetdst, "0.0.VMRSET");
			context = "http://janro.com/vmr/" + vmset;
			trace.addInfo("Class XCinitDocument: VMR Set: " + context);

		} catch (ValueMappingException e) {

			trace.addInfo("Class XCinitDocument: No ValueMapping found for " + "0.0.VMRSET");
			return new String[] { "", "", "", "" };

		}

		// String vmkey =
		// "1.1.LOOKUP~@#1~@#E~@#ORDERS~@#96AZZ1~@#~@#~@#~@#LI~@#9999000000~@#996~@#~@#~@#";

		String vmkey = "";
		String vmreturn = "";

		IFIdentifier src = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier dst = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme);

		try {

			// vmkey = initTable + delimiter + direction + delimiter + standard
			// + delimiter + message + delimiter
			// + version + delimiter + delimiter + delimiter + delimiter +
			// partnertype + delimiter + partner
			// + delimiter + company + delimiter + delimiter + delimiter;

			// Try unified init lookup
			vmkey = initTable + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter
					+ delimiter + vmk1 + delimiter + vmk2 + delimiter + vmk3 + delimiter + vmk4 + delimiter + vmk5
					+ delimiter + vmk6;

			vmreturn = XIVMService.executeMapping(src, dst, vmkey);

			String L1[] = vmreturn.split("\\" + delimiter);

			String vmkeyA4 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "A4" + delimiter + delimiter;
			String vmkeyL1 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ L1[0] + delimiter + L1[1] + delimiter + L1[2];
			String vmkeyL2 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L2" + delimiter + L1[1] + delimiter;
			String vmkeyL3 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L3" + delimiter + delimiter + L1[2];
			String vmkeyL4 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "L4" + delimiter + delimiter;
			String vmkeyZ4 = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "Z4" + delimiter + delimiter;

			trace.addInfo("Class XCinitDocument: Initialize VM Key A4 - " + vmkeyA4);
			trace.addInfo("Class XCinitDocument: Initialize VM Key L1 - " + vmkeyL1);
			trace.addInfo("Class XCinitDocument: Initialize VM Key L2 - " + vmkeyL2);
			trace.addInfo("Class XCinitDocument: Initialize VM Key L3 - " + vmkeyL3);
			trace.addInfo("Class XCinitDocument: Initialize VM Key L4 - " + vmkeyL4);
			trace.addInfo("Class XCinitDocument: Initialize VM Key Z4 - " + vmkeyZ4);

			return new String[] { vmkeyA4, vmkeyL1, vmkeyL2, vmkeyL3, vmkeyL4, vmkeyZ4 };

		} catch (ValueMappingException epc) {

			try {

				trace.addInfo("No VM Key L1 found for " + vmkey + ". Trying VM Key L2.");

				// vmkey = initTable + delimiter + direction + delimiter +
				// standard + delimiter + message + delimiter
				// + version + delimiter + delimiter + delimiter + delimiter +
				// partnertype + delimiter + partner
				// + delimiter + delimiter + delimiter + delimiter;

				// Try unified init lookup
				vmkey = initTable + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter
						+ delimiter + vmk1 + delimiter + vmk2 + delimiter + delimiter + delimiter + delimiter;

				vmreturn = XIVMService.executeMapping(src, dst, vmkey);

				String L2[] = vmreturn.split("\\" + delimiter);

				String vmkeyA4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "A4" + delimiter + delimiter;
				String vmkeyL1 = "";
				String vmkeyL2 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L2" + delimiter + L2[1] + delimiter;
				String vmkeyL3 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L3" + delimiter + delimiter + vmk3;
				String vmkeyL4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "L4" + delimiter + delimiter;
				String vmkeyZ4 = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "Z4" + delimiter + delimiter;

				trace.addInfo("Class XCinitDocument: Initialize VM Key A4 - " + vmkeyA4);
				trace.addInfo("Class XCinitDocument: Initialize VM Key L1 not possible.");
				trace.addInfo("Class XCinitDocument: Initialize VM Key L2 - " + vmkeyL2);
				trace.addInfo("Class XCinitDocument: Initialize VM Key L3 - " + vmkeyL3);
				trace.addInfo("Class XCinitDocument: Initialize VM Key L4 - " + vmkeyL4);
				trace.addInfo("Class XCinitDocument: Initialize VM Key Z4 - " + vmkeyZ4);

				return new String[] { vmkeyA4, vmkeyL1, vmkeyL2, vmkeyL3, vmkeyL4, vmkeyZ4 };

			} catch (ValueMappingException epg) {

				trace.addInfo("Class XCinitDocument: Failed to initialize map with VM Key - " + vmkey);

				return new String[] { "", "", "", "", "", "" };

			}

		}

	}
}
