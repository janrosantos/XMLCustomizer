package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.OutputStream;

import com.mapping.xmlcustomizer.getrules.XCgetRules;
import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

public class XMLCustomizer extends AbstractTransformation {

	@Override
	public void transform(TransformationInput transformationInput, TransformationOutput transformationOutput)
			throws StreamTransformationException {

		try {
			this.customizeXML(transformationInput.getInputPayload().getInputStream(), transformationOutput
					.getOutputPayload().getOutputStream());
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	public void customizeXML(InputStream in, OutputStream out) throws StreamTransformationException {

		AbstractTrace trace = getTrace();
		trace.addInfo("Class XMLCustomizer: Starting XML Customizer");

		try {

			StringBuilder inputstreamtemp = new StringBuilder();

			byte[] buffer = new byte[2048];
			int length;
			while ((length = in.read(buffer)) != -1) {
				inputstreamtemp.append(new String(buffer, 0, length));
			}

			in.close();

			StringBuilder outputstreamtemp = new StringBuilder();
			outputstreamtemp = inputstreamtemp;
			trace.addInfo(inputstreamtemp.toString());

			// Execute method to get rule parameters from PI cache
			// XMLCustomizerGetRules getRules = new XMLCustomizerGetRules();
			// String[][] XCrules = getRules.executeGetRules();

			// Execute method to get rule parameters from PI cache
			String[][] XCrules = new String[][] { {} };
			XCgetRules getRules = new XCgetRules();
			XCrules = getRules.executeXCgetRules(inputstreamtemp, trace);

			// Loop though all the rules found
			if (XCrules.length > 0) {

				for (int i = 0; i < XCrules.length; i++) {

					if (XCrules[0].length > 0) {

						// Assign output for getRules method to individual
						// variables
						String operation = XCrules[i][1];
						String arg0 = XCrules[i][2];
						String arg1 = XCrules[i][3];
						String arg2 = XCrules[i][4];
						String arg3 = XCrules[i][5];

						trace.addInfo("Class XMLCustomizer: " + operation + " " + arg0 + " " + arg1 + " " + arg2 + " "
								+ arg3);

						// Choose which operation/method to execute
						if ((operation.equals("addNodeConstant")) || (operation.equals("addNodeXPath"))) {

							// Add XML nodes/elements
							System.out.println("Add node to XML");
							XMLCustomizerAddNode addNodeXML = new XMLCustomizerAddNode();
							outputstreamtemp = addNodeXML.executeAddNode(operation, arg0, arg1, arg2, arg3,
									inputstreamtemp);

						} else if (operation.equals("deleteNode")) {

							// Delete XML nodes/elements
							System.out.println("Delete node from XML");
							XMLCustomizerDeleteNode deleteNodeXML = new XMLCustomizerDeleteNode();
							outputstreamtemp = deleteNodeXML.executeDeleteNode(arg0, arg1, arg2, arg3, inputstreamtemp, trace);

						} else if ((operation.equals("replaceValueConstant"))
								|| (operation.equals("replaceValueXPath"))) {

							// Replace XML nodes/elements values
							System.out.println("Replacing value of XML segment");
							XMLCustomizerReplaceValue replaceValueXML = new XMLCustomizerReplaceValue();
							outputstreamtemp = replaceValueXML.executeReplaceValue(operation, arg0, arg1, arg2, arg3,
									inputstreamtemp);

						} else {

							// No operation found, invalid operation parameter
							trace.addInfo("Class XMLCustomizer: No valid operation found.");

						}

//						streamptemp = outputstreamtemp.toString();
						inputstreamtemp = outputstreamtemp;
						
					} else {

						trace.addInfo("Class XMLCustomizer: Rule # 1 has no valid entries");
//						streamptemp = outputstreamtemp.toString();
						inputstreamtemp = outputstreamtemp;
						

					}

				}

			} else {

				// No valid rules found, invalid operation parameter
				trace.addInfo("Class XMLCustomizer: No rules found.");
//				streamptemp = outputstreamtemp.toString();
				inputstreamtemp = outputstreamtemp;

			}

			out.write(outputstreamtemp.toString().getBytes());

		} catch (Exception exception) {

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Class XMLCustomizer error: " + exception);
			trace.addInfo("Class XMLCustomizer error: " + exception);

		}
	}
}