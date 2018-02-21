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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.sap.aii.mapping.api.StreamTransformationException;

public class XMLCustomizerAddNode {

	public StringBuilder executeAddNode(String addSource, String arg0, String arg1, String arg2, String arg3,
			StringBuilder in) throws StreamTransformationException {

		// This method will execute addition of nodes on the XML document
		// passed via the input stream
		// Node selection and value to be assigned is based on the first, second
		// and third argument

		// arg0 is the parent node where the new element is to be inserted
		// arg0 is an XPath expression
		// arg1 is the name of the new element/node
		// arg1 is a string
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
		System.out.println("Entering node addition subroutine.");

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
			if (addSource.equals("addNodeConstant")) {

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

			} else if (addSource.equals("addNodeXPath")) {

				// Copy value from an existing node

				if (arg2.isEmpty()) {

					// arg2 is empty
					// Assigning blank value
					newValue = "";

					// Console output only for debugging
					// To be removed on actual deployment
					System.out.println("Assigning blank value.");

				} else {

					// Create XPath expression from arg2
					XPathExpression copyXPath = xpath.compile(arg3);

					// Parse XML document using XPath expression
					// Assign matching node to copyNode
					Node copyNode = (Node) copyXPath.evaluate(doc, XPathConstants.NODE);

					// Get text content of copyNode
					newValue = copyNode.getTextContent();

					// Console output only for debugging
					// To be removed on actual deployment
					System.out.println("New value from existing node: " + newValue);

				}
			}

			//
			// Do addNode proper
			//

			// Create XPath expression from arg0 which is the parent node
			XPathExpression parentXPath = xpath.compile(arg0);

			// Parse XML document using XPath expression
			// Assign matching node to parentNode
			NodeList parentNodes = (NodeList) parentXPath.evaluate(doc, XPathConstants.NODESET);

			// Create XML document for the new node to be inserted
			Document addNodeDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			addNodeDoc.appendChild(addNodeDoc.createElement(arg1));

			// Create XPath expression from arg1 which is the child/new node
			XPathExpression addXPath = xpath.compile("/" + arg1);

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
				parentNodes.item(i).appendChild(doc.importNode(addNode, true));

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
			System.out.println("Addition completed.");

		} catch (Exception exception) {

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Error encountered: " + exception);

		}

		// Return output variable
		return outputString;

	}

}
