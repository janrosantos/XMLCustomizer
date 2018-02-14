package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.OutputStream;
import com.sap.aii.mapping.api.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

		// Execute method to get rule parameters from PI cache
		XMLCustomizerGetRules getRules = new XMLCustomizerGetRules();
		String[] rules = getRules.executeGetRules();

		// Assign output ofr getRules method to individual variables
		String operation = rules[0];
		String arg0 = rules[1];
		String arg1 = rules[2];
		String arg2 = rules[3];
		String arg3 = rules[4];

		// Choose which operation/method to execute
		if (operation.equals("playground")) {

			// Playground only
			// Can be removed on actual deployment
			System.out.println("Playground");
			XMLCustomizerPlayground playgroundXML = new XMLCustomizerPlayground();
			playgroundXML.executePlayground(in, out);

		} else if (operation.equals("addNode")) {

			// Add XML nodes/elements
			System.out.println("Add node to XML");
			XMLCustomizerAddNode addNodeXML = new XMLCustomizerAddNode();
			addNodeXML.executeAddNode(arg0, arg1, arg2, arg3, in, out);

		} else if (operation.equals("deleteNode")) {

			// Delete XML nodes/elements
			System.out.println("Delete node from XML");
			XMLCustomizerDeleteNode deleteNodeXML = new XMLCustomizerDeleteNode();
			deleteNodeXML.executeDeleteNode(arg0, arg1, arg2, arg3, in, out);

		} else if (operation.equals("replaceValue")) {

			// Replace XML nodes/elements values
			System.out.println("Replacing value of XML segment");
			XMLCustomizerReplaceValue replaceValueXML = new XMLCustomizerReplaceValue();
			replaceValueXML.executeReplaceValue(arg0, arg1, arg2, arg3, in, out);

		} else {

			// No operation found, invalid operation parameter
			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Nothing to do");

		}
	}
}