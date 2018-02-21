package com.mapping.xmlcustomizer;

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

import com.sap.aii.mapping.api.StreamTransformationException;

public class XMLCustomizerDeleteNode {

	public StringBuilder executeDeleteNode(String arg0, String arg1, String arg2, String arg3, StringBuilder in)
			throws StreamTransformationException {

		// This method will execute deletion of nodes from the XML document
		// passed via the input stream
		// Node selection is based on the first argument

		// arg0 is the node/s to be deleted
		// arg0 is an XPath expression
		// arg1 is not used
		// arg2 is not used
		// arg3 is not used

		// Console output only for debugging
		// To be removed on actual deployment
		System.out.println("Entering node delete subroutine.");

		// Assign variables for the XML string
		StringBuilder inputString = in;

		// Create a variable for output XML string
		// Initialize to ensure it is empty as start
		StringBuilder outputString = new StringBuilder();

		try {

			// Create XML document from input string
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(inputString.toString())));

			// Create XPath expression from arg0
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression delXPath = xpath.compile(arg0);

			// Parse XML document using XPath expression
			// List all matches in delNode
			NodeList delNodes = (NodeList) delXPath.evaluate(doc, XPathConstants.NODESET);

			// Execute deletion of nodes for all that matched the XPath
			// delNodes is an array/list of nodes that matched the XPath
			for (int i = 0; i < delNodes.getLength(); i++) {

				delNodes.item(i).getParentNode().removeChild(delNodes.item(i));

			}

			// Create new Transformer with the XSLT
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();

			// Create a temporary variable for transformed XML string storage
			StringWriter stringWriter = new StringWriter();

			// Assign transformed XML document to a temporary variable
			transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println(stringWriter.toString());

			// Write transformed XML string to output variable
			outputString.append(stringWriter.toString());

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Deletion completed.");

		} catch (Exception exception) {

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Error encountered: " + exception);

		}

		// Return output variable
		return outputString;

	}

}
