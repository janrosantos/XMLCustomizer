package com.mapping.xmlcustomizer.getrules;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.value.api.IFIdentifier;
import com.sap.aii.mapping.value.api.ValueMappingException;
import com.sap.aii.mapping.value.api.XIVMFactory;
import com.sap.aii.mapping.value.api.XIVMService;
import com.sap.aii.mappingtool.tf7.rt.Container;
import com.sap.aii.mappingtool.tf7.rt.GlobalContainer;

public class XCinitIDOC {

	public String[] executeXCinitIDOC(String table, String direction, String standard, String message, String version,
			String partnertype, String partner, String company, Container container)
			throws StreamTransformationException {

		AbstractTrace trace = container.getTrace();

		String delimiter = "~@#~";
		String senderAgency = "VMR_Key";
		String senderScheme = "VMR_Source";
		String receiverAgency = "VMR_Return";
		String receiverScheme = "VMR_Target";
		String context = "";

		GlobalContainer globalContainer;
		globalContainer = container.getGlobalContainer();

		try {

			IFIdentifier vmsetsrc = XIVMFactory.newIdentifier("http://janro.com/vmrset", senderAgency, senderScheme);
			IFIdentifier vmsetdst = XIVMFactory
					.newIdentifier("http://janro.com/vmrset", receiverAgency, receiverScheme);
			String vmset = XIVMService.executeMapping(vmsetsrc, vmsetdst, "0.0.VMRSET");
			context = "http://janro.com/vmr/" + vmset;
			trace.addInfo("VMR Set: " + context);

		} catch (ValueMappingException e) {

			trace.addInfo("No ValueMapping found for " + "0.0.VMRSET");
			return new String[] { "", "", "", "" };

		}

		// String vmkey =
		// "1.1.LOOKUP~@#1~@#E~@#ORDERS~@#96AZZ1~@#~@#~@#~@#LI~@#9999000000~@#996~@#~@#~@#";

		String vmkey = "";
		String vmreturn = "";

		IFIdentifier src = XIVMFactory.newIdentifier(context, senderAgency, senderScheme);
		IFIdentifier dst = XIVMFactory.newIdentifier(context, receiverAgency, receiverScheme);

		try {

			vmkey = table + delimiter + direction + delimiter + standard + delimiter + message + delimiter + version
					+ delimiter + delimiter + delimiter + delimiter + partnertype + delimiter + partner + delimiter
					+ company + delimiter + delimiter + delimiter;

			vmreturn = XIVMService.executeMapping(src, dst, vmkey);

			String pc[] = vmreturn.split("\\" + delimiter);

			String vmkeypc = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ pc[0] + delimiter + pc[1] + delimiter + pc[2];
			String vmkeypg = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "PG" + delimiter + pc[1] + delimiter;
			String vmkeygc = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "GC" + delimiter + delimiter + pc[2];
			String vmkeygg = direction + delimiter + standard + delimiter + message + delimiter + version + delimiter
					+ "GG" + delimiter + delimiter;

//			globalContainer.setParameter("vmkeypc", vmkeypc);
//			globalContainer.setParameter("vmkeypg", vmkeypg);
//			globalContainer.setParameter("vmkeygc", vmkeygc);
//			globalContainer.setParameter("vmkeygg", vmkeygg);

			trace.addInfo("Initialize VM Key PC: " + vmkeypc);
			trace.addInfo("Initialize VM Key PG: " + vmkeypg);
			trace.addInfo("Initialize VM Key GC: " + vmkeygc);
			trace.addInfo("Initialize VM Key GG: " + vmkeygg);

			return new String[] { vmkeypc, vmkeypg, vmkeygc, vmkeygg };

		} catch (ValueMappingException epc) {

			try {

				trace.addInfo("No VM Key PC found for " + vmkey + ". Trying VM Key PG.");

				vmkey = table + delimiter + direction + delimiter + standard + delimiter + message + delimiter
						+ version + delimiter + delimiter + delimiter + delimiter + partnertype + delimiter + partner
						+ delimiter + delimiter + delimiter + delimiter;

				vmreturn = XIVMService.executeMapping(src, dst, vmkey);

				String pg[] = vmreturn.split("\\" + delimiter);

				String vmkeypc = ""; // direction + delimiter + standard +
				// delimiter + message + delimiter +
				// version + delimiter + pg[0] +
				// delimiter + pg[1] + delimiter;
				String vmkeypg = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "PG" + delimiter + pg[1] + delimiter;
				String vmkeygc = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "GC" + delimiter + delimiter + company;
				String vmkeygg = direction + delimiter + standard + delimiter + message + delimiter + version
						+ delimiter + "GG" + delimiter + delimiter;

//				globalContainer.setParameter("vmkeypc", vmkeypc);
//				globalContainer.setParameter("vmkeypg", vmkeypg);
//				globalContainer.setParameter("vmkeygc", vmkeygc);
//				globalContainer.setParameter("vmkeygg", vmkeygg);

				trace.addInfo("Initialize VM Key PC: Not possible.");
				trace.addInfo("Initialize VM Key PG: " + vmkeypg);
				trace.addInfo("Initialize VM Key GC: " + vmkeygc);
				trace.addInfo("Initialize VM Key GG: " + vmkeygg);

				return new String[] { vmkeypc, vmkeypg, vmkeygc, vmkeygg };

			} catch (ValueMappingException epg) {

//				globalContainer.setParameter("vmkeypc", "");
//				globalContainer.setParameter("vmkeypg", "");
//				globalContainer.setParameter("vmkeygc", "");
//				globalContainer.setParameter("vmkeygg", "");
				trace.addInfo("Failed to initialize map with VM Key: " + vmkey);

				return new String[] { "", "", "", "" };

			}

		}

	}
}
