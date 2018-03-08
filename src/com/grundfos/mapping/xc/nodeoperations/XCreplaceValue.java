package com.grundfos.mapping.xc.nodeoperations;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;

public class XCreplaceValue {

	public StringBuilder executeXCreplaceValue(String valueSource, String inReplaceXPath, String dummyArg2,
			String inNewValue, String dummyArg4, StringBuilder in, AbstractTrace trace)
			throws StreamTransformationException {

		/**
		 * This method will execute value replacement of nodes on the XML
		 * document passed via the input stream
		 * 
		 * Node selection and value to be assigned is based on the first, second
		 * and fourth argument
		 */

		/*-
		 * inReplaceXPath is the parent node where the new element is to be inserted
		 * inReplaceXPath is an XPath expression
		 * dummyArg2 is not used
		 * inNewValue is the value to be assigned to the new node or and xpath expression
		 * inNewValue is optional; blank value to be assigned
		 * dummyArg4 is not used

		 */

		trace.addInfo("Class XCreplaceValue: Starting replace value routine");

		// Assign variables for the XML string
		StringBuilder inputString = in;

		// Create a variable for output XML string
		// Initialize to ensure it is empty as start
		StringBuilder outputString = new StringBuilder();

		try {

			// Create XML document from input string
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlDocument = builder.parse(new InputSource(new StringReader(inputString.toString())));

			// Create XPath expression
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			/*
			 * Identify new value
			 */

			// Create a temporary variable to store value to be assigned on the
			// new node/element of the XML document
			String newValue = "";

			// Check where the value to assign will come from
			if (valueSource.equals("replaceValueConstant")) {

				try {
					newValue = inNewValue;
				} catch (Exception e) {
					newValue = "";
					trace.addInfo("Class XCreplaceValue: Assigning blank value " + newValue);
				}

				trace.addInfo("Class XCreplaceValue: New Value " + newValue);

			} else if (valueSource.equals("replaceValueXPath")) {

				try {
					// Copy value from an existing node

					// Create XPath expression from arg2
					XPathExpression copyXPath = xpath.compile(inNewValue);

					try {
						// Parse XML document using XPath expression
						// Assign matching node to copyNode
						Node copyNode = (Node) copyXPath.evaluate(xmlDocument, XPathConstants.NODE);
						// Get text content of copyNode
						newValue = copyNode.getTextContent();
					} catch (Exception e) {
						// Treat as string right away
						newValue = (String) copyXPath.evaluate(xmlDocument, XPathConstants.STRING);
					}

				} catch (Exception e) {
					newValue = "";
					trace.addInfo("Class XCreplaceValue Error: Assigning blank value " + e);
				}

				trace.addInfo("Class XCreplaceValue: New Value from XPath " + newValue);

			}

			/*
			 * Do replaceValue proper
			 */

			// Create XPath expression from arg0 which is the node to be
			// replaced
			XPathExpression replaceXPath = xpath.compile(inReplaceXPath);

			// Parse XML document using XPath expression
			// Assign matching node to replaceNode
			NodeList replaceNodes = (NodeList) replaceXPath.evaluate(xmlDocument, XPathConstants.NODESET);

			// Execute value replacement
			// replaceNodes is an array/list of parent nodes
			for (int i = 0; i < replaceNodes.getLength(); i++) {

				// Set new value for each node that matched
				replaceNodes.item(i).setTextContent(newValue);

			}

			trace.addInfo("Class XCreplaceValue: " + replaceNodes.getLength() + " node(s) replaced");

			// Create new Transformer with the XSLT
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();

			// Create a temporary variable for transformed XML string storage
			StringWriter stringWriter = new StringWriter();

			// Assign transformed XML document to a temporary variable
			transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));

			// Write transformed XML string to output variable
			outputString.append(stringWriter.toString());

			trace.addInfo("Class XCreplaceValue: Replace value completed");

		} catch (Exception exception) {

			trace.addInfo("Class XCreplaceValue error: " + exception);

		}

		// Return output variable
		return outputString;

	}

}
