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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;

public class XCdeleteNode {

	public StringBuilder executeXCdeleteNode(String inDelXPath, String dummyArg1, String dummyArg2, String dummyArg3, StringBuilder in,
			AbstractTrace trace) throws StreamTransformationException {

		/**
		 * This method will execute deletion of nodes from the XML document
		 * passed via the input stream
		 * 
		 * Node selection is based on the first argument
		 */

		/*-
		 * deleteXPath is the node/s to be deleted
		 * deleteXPath is an XPath expression
		 * dummyArg1 is not used
		 * dummyArg2 is not used
		 * dummyArg3 is not used
		 */

		trace.addInfo("Class XCdeleteNode: Starting delete node routine");

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

			// Create XPath expression from inDelXPath
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xPath = xPathfactory.newXPath();
			XPathExpression delXPath = xPath.compile(inDelXPath);

			// Parse XML document using XPath expression
			// List all matches in delNode
			NodeList delNodes = (NodeList) delXPath.evaluate(xmlDocument, XPathConstants.NODESET);

			// Execute deletion of nodes for all that matched the XPath
			// delNodes is an array/list of nodes that matched the XPath

			for (int i = 0; i < delNodes.getLength(); i++) {

				delNodes.item(i).getParentNode().removeChild(delNodes.item(i));

			}

			trace.addInfo("Class XCdeleteNode: " + delNodes.getLength() + " node(s) deleted");

			// Create new Transformer with the XSLT
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();

			// Create a temporary variable for transformed XML string storage
			StringWriter stringWriter = new StringWriter();

			// Assign transformed XML document to a temporary variable
			transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));

			// Write transformed XML string to output variable
			outputString.append(stringWriter.toString());

			trace.addInfo("Class XCdeleteNode: Deletion completed");

		} catch (Exception exception) {

			trace.addInfo("Class XCdeleteNode error: " + exception);

		}

		// Return output variable
		return outputString;

	}

}
