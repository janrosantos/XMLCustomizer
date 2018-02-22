package com.mapping.xmlcustomizer.getrules;

import com.mapping.xmlcustomizer.XMLCustomizer;
import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;

public class XCgetVM extends XMLCustomizer {

	public String[] executeGetVM(String table, String[] initVMKey, String processor, int ruleNumber, AbstractTrace trace)
			throws StreamTransformationException {

		trace.addInfo("Class XCgetVM: Excuting rule acquisition from PI cache");

		String delimiter = "~@#~";
		String senderAgency = "VMR_Key";
		String senderScheme = "VMR_Source";
		String receiverAgency = "VMR_Return";
		String receiverScheme = "VMR_Target";
		String context = "";

		try {

			IFIdentifier vmsetsrc = XIVMFactory.newIdentifier("http://janro.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetdst = XIVMFactory
					.newIdentifier("http://janro.com/vmrset", receiverAgency, receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetsrc, vmsetdst, "0.0.VMRSET");
			context = "http://janro.com/vmr/" + vmset;

		} catch (ValueMappingException exception) {

			trace.addInfo("Class XCgetVM error: " + exception);

		}

		String gcvmkeypc = initVMKey[0];
		String gcvmkeypg = initVMKey[1];
		String gcvmkeygc = initVMKey[2];
		String gcvmkeygg = initVMKey[3];

		IFIdentifier src = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier dst = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme);

		String vmreturn = "";

		if (gcvmkeygg.length() > 0) {

			try {

				String vmkeypc = table + delimiter + gcvmkeypc + delimiter + processor + delimiter + ruleNumber
						+ delimiter + delimiter + delimiter + delimiter;
				vmreturn = XIVMService.executeMapping(src, dst, vmkeypc);
				String pc[] = vmreturn.split("\\" + delimiter);

				trace.addInfo("Class XCgetVM: " + vmkeypc);
				return pc;

			} catch (ValueMappingException epc) {

				trace.addInfo("Class XCgetVM: VM Key PC for Table " + table + " not found. Trying VM Key PG.");

				try {

					String vmkeypg = table + delimiter + gcvmkeypg + delimiter + processor + delimiter + ruleNumber
					+ delimiter + delimiter + delimiter + delimiter;
					vmreturn = XIVMService.executeMapping(src, dst, vmkeypg);
					String pg[] = vmreturn.split("\\" + delimiter);

					trace.addInfo("Class XCgetVM: " + vmkeypg);
					return pg;

				} catch (ValueMappingException epg) {

					trace.addInfo("Class XCgetVM: VM Key PG for Table " + table + " not found. Trying VM Key GC.");

					try {

						String vmkeygc = table + delimiter + gcvmkeygc + delimiter + processor + delimiter + ruleNumber
						+ delimiter + delimiter + delimiter + delimiter;
						vmreturn = XIVMService.executeMapping(src, dst, vmkeygc);
						String gc[] = vmreturn.split("\\" + delimiter);

						trace.addInfo("Class XCgetVM: " + vmkeygc);
						return gc;

					} catch (ValueMappingException egc) {

						trace.addInfo("Class XCgetVM: VM Key GC for Table " + table + " not found. Trying VM Key GG.");

						try {

							String vmkeygg = table + delimiter + gcvmkeygg + delimiter + processor + delimiter + ruleNumber
							+ delimiter + delimiter + delimiter + delimiter;

							vmreturn = XIVMService.executeMapping(src, dst, vmkeygg);
							String gg[] = vmreturn.split("\\" + delimiter);

							trace.addInfo("Class XCgetVM: " + vmkeygg);
							return gg;

						} catch (ValueMappingException egg) {

							trace.addInfo("Class XCgetVM: VM Key GG for Table " + table
									+ "  not found. Conversion failed. " + egg);
							return new String[] {};

						}

					}

				}

			}

		} else {

			trace.addInfo("Map not initialized. Aborting conversion.");
			// return "";
			return new String[] {};

		}

	}

}
