package com.grundfos.mapping.xc.getrules;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;

public class XCinitDocument {

	public String[] executeXCinitDocument(String[] initDocKey, AbstractTrace trace)
			throws StreamTransformationException {

		/**
		 * This is a generic method for the initiation of document and rules
		 * from PI cache.
		 * 
		 * Input arguments are the VM input keys that are prepared by XCprep*
		 * classes
		 */

		String[] vmOut = new String[] {};
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
		String receiverScheme1 = "VMR_Target_1";
		String receiverScheme2 = "VMR_Target_2";
		String receiverScheme3 = "VMR_Target_3";
		String context = "";

		// Get VM set
		try {

			IFIdentifier vmsetSource = XIVMFactory.newIdentifier("http://janro.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetDestination = XIVMFactory.newIdentifier("http://janro.com/vmrset", receiverAgency,
					receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetSource, vmsetDestination, "0.0.VMRSET");
			context = "http://janro.com/vmr/" + vmset;
			trace.addInfo("Class XCinitDocument: VMR Set: " + context);

		} catch (ValueMappingException e) {

			trace.addInfo("Class XCinitDocument: No ValueMapping found for " + "0.0.VMRSET");
			vmOut = new String[] { "", "", "", "", "", "" };

		}

		String vmKey = "";
		String vmReturn = "";
		String vmReturn1 = "";
		String vmReturn2 = "";
		String vmReturn3 = "";

		// Build VM prerequisites
		IFIdentifier source = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier destination1 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme1);
		IFIdentifier destination2 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme2);
		IFIdentifier destination3 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme3);

		try {

			/*
			 * Try first L1 initialization
			 */

			vmKey = initTable + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter
					+ delimiter + vmKeyInput1 + delimiter + vmKeyInput2 + delimiter + vmKeyInput3 + delimiter
					+ vmKeyInput4 + delimiter + vmKeyInput5 + delimiter + vmKeyInput6;

			vmReturn1 = XIVMService.executeMapping(source, destination1, vmKey);
			try {
				// Check if VM Target has part 2
				vmReturn2 = XIVMService.executeMapping(source, destination2, vmKey);
			} catch (Exception evmReturn2) {
				vmReturn2 = "";
			}
			try {
				// Check if VM Target has part 3
				vmReturn3 = XIVMService.executeMapping(source, destination3, vmKey);
			} catch (Exception evmReturn3) {
				vmReturn3 = "";
			}

			vmReturn = vmReturn1 + vmReturn2 + vmReturn3;
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

			vmOut = new String[] { vmKeyA4, vmKeyL1, vmKeyL2, vmKeyL3, vmKeyL4, vmKeyZ4 };

		} catch (Exception eL1) {

			try {

				/*
				 * If L1 initialization failed, try L2
				 */

				trace.addInfo("Class XCinitDocument: No VM Key L1 found for " + vmKey + ". Trying VM Key L2.");

				vmKey = initTable + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter + delimiter
						+ delimiter + vmKeyInput1 + delimiter + vmKeyInput2 + delimiter + delimiter + delimiter
						+ delimiter;

				vmReturn1 = XIVMService.executeMapping(source, destination1, vmKey);
				try {
					// Check if VM Target has part 2
					vmReturn2 = XIVMService.executeMapping(source, destination2, vmKey);
				} catch (Exception evmReturn2) {
					vmReturn2 = "";
				}
				try {
					// Check if VM Target has part 3
					vmReturn3 = XIVMService.executeMapping(source, destination3, vmKey);
				} catch (Exception evmReturn3) {
					vmReturn3 = "";
				}

				vmReturn = vmReturn1 + vmReturn2 + vmReturn3;
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

				vmOut = new String[] { vmKeyA4, vmKeyL1, vmKeyL2, vmKeyL3, vmKeyL4, vmKeyZ4 };

			} catch (Exception eL2) {

				/*
				 * If L2 initialization failed, default to L4
				 */

				trace.addInfo("Class XCinitDocument: No VM Key L2 found for " + vmKey + ". Defaulting to VM Key L4.");

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
				trace.addInfo("Class XCinitDocument: Initialize VM Key L3 - SKIPPED");
				trace.addInfo("Class XCinitDocument: Initialize VM Key L4 - " + vmKeyL4);
				trace.addInfo("Class XCinitDocument: Initialize VM Key Z4 - " + vmKeyZ4);

				vmOut = new String[] { vmKeyA4, vmKeyL1, vmKeyL2, vmKeyL3, vmKeyL4, vmKeyZ4 };

			}

		}

		return vmOut;

	}
}
