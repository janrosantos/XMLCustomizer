package com.mapping.xmlcustomizer.nodeoperations;

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

	public StringBuilder executeXCreplaceValue(String valueSource, String arg0, String arg1, String arg2, String arg3,
			StringBuilder in, AbstractTrace trace) throws StreamTransformationException {

		// This method will execute value replacement of nodes on the XML
		// document passed via the input stream
		// Node selection and value to be assigned is based on the first, third
		// and fourth argument

		// arg0 is the parent node where the new element is to be inserted
		// arg0 is an XPath expression
		// arg1 is not used
		// arg2 is the value to be assigned to the new node
		// arg2 is a constant
		// arg2 is optional
		// arg3 is a node/element where a value should be copied
		// should arg2 is blank
		// arg3 is an XPath expression
		// arg3 is optional
		// Either arg1 or arg2 should at least exist else the created node will
		// be blank

		// Console output only for debugging
		// To be removed on actual deployment
		System.out.println("Entering node value replacement subroutine.");

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

			//
			// Identify new value
			//

			// Create a temporary variable to store value to be assigned on the
			// new node/element of the XML document
			String newValue = "";

			// Check where the value to assign will come from
			if (valueSource.equals("replaceValueConstant")) {

				if (arg2.isEmpty()) {

					// arg2 is empty
					// Assigning blank value
					newValue = "";

					// Console output only for debugging
					// To be removed on actual deployment
					System.out.println("Assigning blank value.");

				} else {

					// Using a constant value
					newValue = arg2;

					// Console output only for debugging
					// To be removed on actual deployment
					System.out.println("New constant value: " + newValue);

				}

			} else if (valueSource.equals("replaceValueXPath")) {

				if (arg2.isEmpty()) {

					// arg2 is empty
					// Assigning blank value
					newValue = "";

					// Console output only for debugging
					// To be removed on actual deployment
					System.out.println("Assigning blank value.");

				} else {

					// Copy value from an existing node

					// Create XPath expression from arg2
					XPathExpression copyXPath = xpath.compile(arg3);

					// Parse XML document using XPath expression
					// Assign matching node to copyNode
					Node copyNode = (Node) copyXPath.evaluate(xmlDocument, XPathConstants.NODE);

					// Get text content of copyNode
					newValue = copyNode.getTextContent();

					// Console output only for debugging
					// To be removed on actual deployment
					System.out.println("New value from existing node: " + newValue);

				}

			}

			//
			// Do replaceValue proper
			//

			// Create XPath expression from arg0 which is the node to be
			// replaced
			XPathExpression replaceXPath = xpath.compile(arg0);

			// Parse XML document using XPath expression
			// Assign matching node to replaceNode
			NodeList replaceNodes = (NodeList) replaceXPath.evaluate(xmlDocument, XPathConstants.NODESET);

			// Execute value replacement
			// replaceNodes is an array/list of parent nodes
			for (int i = 0; i < replaceNodes.getLength(); i++) {

				// Set new value for each node that matched
				replaceNodes.item(i).setTextContent(newValue);

			}

			// Create new Transformer with the XSLT
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();

			// Create a temporary variable for transformed XML string storage
			StringWriter stringWriter = new StringWriter();

			// Assign transformed XML document to a temporary variable
			transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println(stringWriter.toString());

			// Write transformed XML string to output variable
			outputString.append(stringWriter.toString());

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Replace value completed.");

		} catch (Exception exception) {

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Error encountered: " + exception);

		}

		// Return output variable
		return outputString;

	}

}
