package com.grundfos.mapping.xmlcustomizer.nodeoperations;

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

public class XCaddNode {

	public StringBuilder executeXCaddNode(String addSource, String inParentXPath, String inNewField, String inNewValue,
			String dummyArg4, StringBuilder in, AbstractTrace trace) throws StreamTransformationException {

		/**
		 * This method will execute addition of nodes on the XML document passed
		 * via the input stream
		 * 
		 * Node selection and value to be assigned is based on the first,
		 * second, third and fourth argument
		 */

		/*-
		 * inParentXPath is the parent node where the new element is to be inserted
		 * inParentXPath is an XPath expression
		 * inNewField is the name of the new element/node
		 * inNewValue is either the value to be assigned to the new node or an XPath expression
		 * inNewValue is optional; blank value is assigned
		 * dummyArg4 is not used
		 * */

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
			XPath xPath = xPathfactory.newXPath();

			//
			// Identify new value
			//

			// Create a temporary variable to store value to be assigned on the
			// new node/element of the XML document
			String newValue = "";

			// Check where the value to assign will come from
			if (addSource.equals("addNodeConstant")) {

				try {
					newValue = inNewValue;
				} catch (Exception e) {
					newValue = "";
					trace.addInfo("Class XCaddNode: Assigning blank value " + newValue);
				}

			} else if (addSource.equals("addNodeXPath")) {

				// Copy value from an existing node

				try {
					// Copy value from an existing node

					// Create XPath expression from arg2
					XPathExpression copyXPath = xPath.compile(inNewValue);

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
					trace.addInfo("Class XCaddNode Error: Assigning blank value " + e);
				}

				trace.addInfo("Class XCaddNode: New Value from XPath " + newValue);

			}

			//
			// Do addNode proper
			//

			// Create XPath expression from arg0 which is the parent node
			XPathExpression parentXPath = xPath.compile(inParentXPath);

			// Parse XML document using XPath expression
			// Assign matching node to parentNode
			NodeList parentNodes = (NodeList) parentXPath.evaluate(xmlDocument, XPathConstants.NODESET);

			// Create XML document for the new node to be inserted
			Document addNodeDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			addNodeDoc.appendChild(addNodeDoc.createElement(inNewField));

			// Create XPath expression from arg1 which is the child/new node
			XPathExpression addXPath = xPath.compile("/" + inNewField);

			// Parse XML document using XPath expression
			// Assign matching node to addNode
			Node addNode = (Node) addXPath.evaluate(addNodeDoc, XPathConstants.NODE);

			// Assign value to new node
			addNode.setTextContent(newValue);

			// Execute insertion of new node for all parent nodes
			// parentNodes is an array/list of parent nodes
			// addNode is the node object of the child node to be inserted
			for (int i = 0; i < parentNodes.getLength(); i++) {

				// Insert child node to parent node
				parentNodes.item(i).appendChild(xmlDocument.importNode(addNode, true));

			}

			trace.addInfo("Class XCdeleteNode: " + parentNodes.getLength() + " " + inNewField + " node(s) deleted");

			// Create new Transformer with the XSLT
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();

			// Create a temporary variable for transformed XML string storage
			StringWriter stringWriter = new StringWriter();

			// Assign transformed XML document to a temporary variable
			transformer.transform(new DOMSource(xmlDocument), new StreamResult(stringWriter));

			// Write transformed XML string to output variable
			outputString.append(stringWriter.toString());

			trace.addInfo("Class XCaddNode: Replace value completed");

		} catch (Exception exception) {

			trace.addInfo("Class XCaddNode error: " + exception);

		}

		// Return output variable
		return outputString;

	}

}
