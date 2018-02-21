package com.mapping.xmlcustomizer.getrules;

import java.util.Map;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationConstants;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.GlobalContainer;
import com.sap.aii.mappingtool.tf7.rt.ResultList;

public class XCgetVM {

	public String executeGetVM(String table, int returnval, Container container) throws StreamTransformationException {

		AbstractTrace trace = container.getTrace();

		String delimiter = "~@#~";
		String senderAgency = "VMR_Key";
		String senderScheme = "VMR_Source";
		String receiverAgency = "VMR_Return";
		String receiverScheme = "VMR_Target";
		String context = "";
		ResultList result = null;

		try {

			IFIdentifier vmsetsrc = XIVMFactory.newIdentifier("http://janro.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetdst = XIVMFactory
					.newIdentifier("http://janro.com/vmrset", receiverAgency, receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetsrc, vmsetdst, "0.0.VMRSET");
			context = "http://janro.com/vmr/" + vmset;

		} catch (ValueMappingException e) {

			result.addValue("");

		}

		String gcvmkeypc = "";
		String gcvmkeypg = "";
		String gcvmkeygc = "";
		String gcvmkeygg = "";

		GlobalContainer globalContainer;
		globalContainer = container.getGlobalContainer();
		gcvmkeypc = "" + globalContainer.getParameter("vmkeypc");
		gcvmkeypg = "" + globalContainer.getParameter("vmkeypg");
		gcvmkeygc = "" + globalContainer.getParameter("vmkeygc");
		gcvmkeygg = "" + globalContainer.getParameter("vmkeygg");

		try {
			if (gcvmkeypc.equals("null") || gcvmkeypc.length() == 0) {

				java.util.Map param = (Map) container.getInputParameters();
				gcvmkeypc = (String) param.get(StreamTransformationConstants.CONVERSATION_ID);
			}
		} catch (Exception e) {

			java.util.Map param = (Map) container.getInputParameters();
			gcvmkeypc = (String) param.get(StreamTransformationConstants.CONVERSATION_ID);
		}

		IFIdentifier src = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier dst = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme);

		String vmreturn = "";
		String res = "";

		if (gcvmkeygg.length() > 0) {

			try {

				String vmkeypc = table + delimiter + gcvmkeypc + delimiter + delimiter + delimiter + delimiter
						+ delimiter + delimiter;
				vmreturn = XIVMService.executeMapping(src, dst, vmkeypc);
				String pc[] = vmreturn.split("\\" + delimiter);

				if ((returnval > pc.length) || (returnval < 1)) {

					trace.addInfo("VM Key PC: " + vmkeypc);
					res = "";

				} else {

					trace.addInfo("VM Key PC: " + vmkeypc);
					res = pc[returnval - 1];

				}

				trace.addInfo("Get Value 0: " + res);
				result.addValue(res);

			} catch (ValueMappingException epc) {

				trace.addInfo("VM Key PC for Table " + table + " not found. Trying VM Key PG.");

				try {

					String vmkeypg = table + delimiter + gcvmkeypg + delimiter + delimiter + delimiter + delimiter
							+ delimiter + delimiter;
					vmreturn = XIVMService.executeMapping(src, dst, vmkeypg);
					String pg[] = vmreturn.split("\\" + delimiter);

					if ((returnval > pg.length) || (returnval < 1)) {

						trace.addInfo("VM Key PG: " + vmkeypg);
						res = "";

					} else {

						trace.addInfo("VM Key PG: " + vmkeypg);
						res = pg[returnval - 1];

					}

					trace.addInfo("Get Value 0: " + res);
					result.addValue(res);

				} catch (ValueMappingException epg) {

					trace.addInfo("VM Key PG for Table " + table + " not found. Trying VM Key GC.");

					try {

						String vmkeygc = table + delimiter + gcvmkeygc + delimiter + delimiter + delimiter
								+ delimiter + delimiter + delimiter;
						vmreturn = XIVMService.executeMapping(src, dst, vmkeygc);
						String gc[] = vmreturn.split("\\" + delimiter);

						if ((returnval > gc.length) || (returnval < 1)) {

							trace.addInfo("VM Key GC: " + vmkeygc);
							res = "";

						} else {

							trace.addInfo("VM Key GC: " + vmkeygc);
							res = gc[returnval - 1];

						}

						trace.addInfo("Get Value 0: " + res);
						result.addValue(res);

					} catch (ValueMappingException egc) {

						trace.addInfo("VM Key GC for Table " + table + " not found. Trying VM Key GG.");

						try {

							String vmkeygg = table + delimiter + gcvmkeygg + delimiter + delimiter + delimiter
									+ delimiter + delimiter + delimiter;

							vmreturn = XIVMService.executeMapping(src, dst, vmkeygg);
							String gg[] = vmreturn.split("\\" + delimiter);

							if ((returnval > gg.length) || (returnval < 1)) {

								trace.addInfo("VM Key GG: " + vmkeygg);
								res = "";

							} else {

								trace.addInfo("VM Key GG: " + vmkeygg);
								res = gg[returnval - 1];
							}

							trace.addInfo("Get Value 0: " + res);
							result.addValue(res);

						} catch (ValueMappingException egg) {

							trace.addInfo("VM Key GG for Table " + table + "  not found. Conversion failed. " + egg);
							result.addValue("");

						}

					}

				}

			}

		} else {

			trace.addInfo("Map not initialized. Aborting conversion.");
			result.addValue("");

		}

		return result.toString();

	}

}
