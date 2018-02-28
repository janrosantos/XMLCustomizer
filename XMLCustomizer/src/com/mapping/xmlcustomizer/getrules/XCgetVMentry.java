package com.mapping.xmlcustomizer.getrules;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;

public class XCgetVMentry {

	public String[] executeGetVMentry(String xcTable, String vmKeyRule, int ruleNumber, AbstractTrace trace)
			throws StreamTransformationException {

		trace.addInfo("Class XCgetVMentry: Executing rule acquisition from PI cache");

		String delimiter = "~@#~";
		String senderAgency = "VMR_Key";
		String senderScheme = "VMR_Source";
		String receiverAgency = "VMR_Return";
		String receiverScheme = "VMR_Target";
		String context = "";

		try {

			IFIdentifier vmsetSource = XIVMFactory.newIdentifier("http://janro.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetDestination = XIVMFactory
					.newIdentifier("http://janro.com/vmrset", receiverAgency, receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetSource, vmsetDestination, "0.0.VMRSET");
			context = "http://janro.com/vmr/" + vmset;

		} catch (ValueMappingException exception) {

			trace.addInfo("Class XCgetVMentry error: " + exception);

		}

		IFIdentifier source = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier destination = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme);

		String vmReturn = "";

		try {

			String vmKey = xcTable + delimiter + vmKeyRule + delimiter + ruleNumber + delimiter + delimiter + delimiter
					+ delimiter + delimiter;
			trace.addInfo("Class XCgetVMentry: Trying " + vmKey);
			vmReturn = XIVMService.executeMapping(source, destination, vmKey);
			String xcRuleParam[] = vmReturn.split("\\" + delimiter);

			return xcRuleParam;

		} catch (ValueMappingException exception) {

			if (ruleNumber > 10100) {
				trace.addInfo("Class XCgetVMentry : Rule number " + (ruleNumber - 100) + " is the last rule for "
						+ vmKeyRule);
			} else {
				trace.addInfo("Class XCgetVMentry : No rules for " + vmKeyRule);
			}
			return new String[] {};

		}

	}

}
