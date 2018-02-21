package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.OutputStream;

import com.mapping.xmlcustomizer.getrules.XCgetRules;
import com.sap.aii.mapping.api.*;
import com.sap.aii.mappingtool.tf7.rt.Container;

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

		try {

			Container container = null;

			// Execute method to get rule parameters from PI cache
			// XMLCustomizerGetRules getRules = new XMLCustomizerGetRules();
			// String[][] rules = getRules.executeGetRules();

			// Execute method to get rule parameters from PI cache
			XCgetRules getRules = new XCgetRules();
			String[][] XCrules = getRules.executeXCgetRules(in, container);

			StringBuilder inputstreamtemp = new StringBuilder();

			byte[] buffer = new byte[2048];
			int length;
			while ((length = in.read(buffer)) != -1) {
				inputstreamtemp.append(new String(buffer, 0, length));
			}
			in.close();

			StringBuilder outputstreamtemp = new StringBuilder();
			String streamptemp = in.toString();

			// Loop though all the rules found
			for (int i = 0; i < XCrules.length; i++) {

				// Assign output for getRules method to individual variables
				String operation = XCrules[i][0];
				String arg0 = XCrules[i][1];
				String arg1 = XCrules[i][2];
				String arg2 = XCrules[i][3];
				String arg3 = XCrules[i][4];

				// Choose which operation/method to execute
				if ((operation.equals("addNodeConstant")) || (operation.equals("addNodeXPath"))) {

					// Add XML nodes/elements
					System.out.println("Add node to XML");
					XMLCustomizerAddNode addNodeXML = new XMLCustomizerAddNode();
					outputstreamtemp = addNodeXML.executeAddNode(operation, arg0, arg1, arg2, arg3, inputstreamtemp);

				} else if (operation.equals("deleteNode")) {

					// Delete XML nodes/elements
					System.out.println("Delete node from XML");
					XMLCustomizerDeleteNode deleteNodeXML = new XMLCustomizerDeleteNode();
					outputstreamtemp = deleteNodeXML.executeDeleteNode(arg0, arg1, arg2, arg3, inputstreamtemp);

				} else if ((operation.equals("replaceValueConstant")) || (operation.equals("replaceValueXPath"))) {

					// Replace XML nodes/elements values
					System.out.println("Replacing value of XML segment");
					XMLCustomizerReplaceValue replaceValueXML = new XMLCustomizerReplaceValue();
					outputstreamtemp = replaceValueXML.executeReplaceValue(operation, arg0, arg1, arg2, arg3,
							inputstreamtemp);

				} else {

					// No operation found, invalid operation parameter
					// Console output only for debugging
					// To be removed on actual deployment
					System.out.println("Nothing to do");

				}

				streamptemp = outputstreamtemp.toString();
				System.out.println("StreamTemp: " + i + " " + streamptemp);
				inputstreamtemp = outputstreamtemp;

			}

			out.write(outputstreamtemp.toString().getBytes());

		} catch (Exception exception) {

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Error encountered: " + exception);

		}
	}
}