package com.grundfos.mapping.xmlcustomizer.getrules;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;

public class XCgetVMentry {

	public String[] executeGetVMentry(String xcTable, String vmKeyRuleLevel, int ruleNumber, AbstractTrace trace)
			throws StreamTransformationException {

		/**
		 * This is a generic method for the extraction of rules from PI cache
		 * 
		 * Input arguments are the VM table, the rule level and the rule number
		 */

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

		} catch (ValueMappingException exception) {

			trace.addInfo("Class XCgetVMentry error: " + exception);

		}

		// Build VM prerequisites
		IFIdentifier source = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier destination1 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme1);
		IFIdentifier destination2 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme2);
		IFIdentifier destination3 = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme3);

		String vmReturn = "";
		String vmReturn1 = "";
		String vmReturn2 = "";
		String vmReturn3 = "";

		try {

			String vmKey = xcTable + delimiter + vmKeyRuleLevel + delimiter + ruleNumber + delimiter + delimiter
					+ delimiter + delimiter + delimiter;

			trace.addInfo("Class XCgetVMentry: Extracting " + vmKey);

			vmReturn1 = XIVMService.executeMapping(source, destination1, vmKey);

			try {
				// Check if VM Target has part 2
				vmReturn2 = XIVMService.executeMapping(source, destination2, vmKey);
			} catch (Exception e) {
				vmReturn2 = "";
			}

			try {
				// Check if VM Target has part 3
				vmReturn3 = XIVMService.executeMapping(source, destination3, vmKey);
			} catch (Exception e) {
				vmReturn3 = "";
			}

			vmReturn = vmReturn1 + vmReturn2 + vmReturn3;
			String xcRuleParam[] = vmReturn.split("\\" + delimiter);

			return xcRuleParam;

		} catch (ValueMappingException exception) {

			if (ruleNumber > 10100) {
				// trace.addInfo("Class XCgetVMentry : Rule number " +
				// (ruleNumber - 100) + " is the last rule for "
				// + vmKeyRuleLevel);
			} else {
				trace.addInfo("Class XCgetVMentry : No rules for " + vmKeyRuleLevel);
			}
			return new String[] {};

		}

	}

}
