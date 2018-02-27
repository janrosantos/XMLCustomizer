package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.mapping.xmlcustomizer.functions.XCpadArray2D;
import com.mapping.xmlcustomizer.getrules.XCgetRules;
import com.mapping.xmlcustomizer.nodeoperations.XCaddNode;
import com.mapping.xmlcustomizer.nodeoperations.XCdeleteNode;
import com.mapping.xmlcustomizer.nodeoperations.XCreplaceValue;
import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.InputParameters;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

public class XMLCustomizer extends AbstractTransformation {

	@Override
	public void transform(TransformationInput transformationInput, TransformationOutput transformationOutput)
			throws StreamTransformationException {

		try {
			this.customizeXML(transformationInput.getInputPayload().getInputStream(),transformationInput.getInputParameters(), transformationOutput
					.getOutputPayload().getOutputStream());
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	public void customizeXML(InputStream in, InputParameters inParam, OutputStream out) throws StreamTransformationException {

		AbstractTrace trace = getTrace();
		trace.addInfo("Class XMLCustomizer: Starting XML Customizer");

		try {

			StringBuilder inputstreamtemp = new StringBuilder();
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			char[] buffer = new char[4098];
			int charsRead;
			while ((charsRead = reader.read(buffer)) != -1) {
				inputstreamtemp.append(buffer, 0, charsRead);
			}
			in.close();

			StringBuilder outputstreamtemp = new StringBuilder();
			outputstreamtemp = inputstreamtemp;
			
			//Get OM parameter
			String omParam = inParam.getString("XC");
			getTrace().addInfo("Input Parameter: " + omParam);

			// Execute method to get rule parameters from PI cache
			String[][] XCrules = new String[][] { {} };
			XCgetRules getRules = new XCgetRules();
			XCrules = getRules.executeXCgetRules(inputstreamtemp, omParam, trace);

			XCrules = XCpadArray2D.executeXCpadArray2D(XCrules, "", 5);

			// Loop though all the rules found
			if (XCrules.length > 0) {

				for (int i = 0; i < XCrules.length; i++) {

					if (XCrules[0].length > 0) {

						// Assign output for getRules method to individual
						// variables
						String operation = XCrules[i][0];
						String arg0 = XCrules[i][1];
						String arg1 = XCrules[i][2];
						String arg2 = XCrules[i][3];
						String arg3 = XCrules[i][4];

						// Choose which operation/method to execute
						if ((operation.equals("addNodeConstant")) || (operation.equals("addNodeXPath"))) {

							// Add XML nodes/elements
							XCaddNode addNodeXML = new XCaddNode();
							outputstreamtemp = addNodeXML.executeXCaddNode(operation, arg0, arg1, arg2, arg3,
									inputstreamtemp, trace);

						} else if (operation.equals("deleteNode")) {

							// Delete XML nodes/elements
							XCdeleteNode deleteNodeXML = new XCdeleteNode();
							outputstreamtemp = deleteNodeXML.executeXCdeleteNode(arg0, arg1, arg2, arg3,
									inputstreamtemp, trace);

						} else if ((operation.equals("replaceValueConstant"))
								|| (operation.equals("replaceValueXPath"))) {

							// Replace XML nodes/elements values
							XCreplaceValue replaceValueXML = new XCreplaceValue();
							outputstreamtemp = replaceValueXML.executeXCreplaceValue(operation, arg0, arg1, arg2, arg3,
									inputstreamtemp, trace);

						} else {

							// No operation found, invalid operation parameter
							trace.addInfo("Class XMLCustomizer: No valid operation found.");

						}

						inputstreamtemp = outputstreamtemp;

					} else {

						trace.addInfo("Class XMLCustomizer: Rule # 1 has no valid entries");
						inputstreamtemp = outputstreamtemp;

					}

				}

			} else {

				// No valid rules found, invalid operation parameter
				trace.addInfo("Class XMLCustomizer: No rules found.");
				inputstreamtemp = outputstreamtemp;

			}

			out.write(outputstreamtemp.toString().getBytes(Charset.forName("UTF-8")));

		} catch (Exception exception) {

			trace.addInfo("Class XMLCustomizer error: " + exception);

		}
	}
}