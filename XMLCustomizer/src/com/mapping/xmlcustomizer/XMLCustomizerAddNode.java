package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.OutputStream;
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

import com.sap.aii.mapping.api.StreamTransformationException;

public class XMLCustomizerAddNode {

	public void executeAddNode(String arg0, String arg1, String arg2, String arg3, InputStream in, OutputStream out)
			throws StreamTransformationException {

		// This method will execute addition of nodes on the XML document
		// passed via the input stream
		// Node selection and value to be assigned is based on the first, second
		// and third argument

		// arg0 is the node/element to be created
		// arg0 is an XPath expression
		// arg1 is the value to be assigned
		// arg1 is a constant
		// arg1 is optional
		// arg2 is a node/element where a value should be copied should arg1 is
		// blank
		// arg2 is an XPath expression
		// arg2 is optional
		// Either arg1 or arg2 should at least exist else the created node will
		// be blank
		// arg3 is not used

		// Console output only for debugging
		// To be removed on actual deployment
		System.out.println("Entering node addition subroutine.");

		// Assign variables for the XML stream
		InputStream inputstream = in;
		OutputStream outputstream = out;

		try {

			// Create XML document from input stream
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputstream);

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
			if (!arg1.isEmpty()) {

				// Using a constant value
				newValue = arg1;

				// Console output only for debugging
				// To be removed on actual deployment
				System.out.println("New constant value: " + newValue);

			} else if (!arg2.isEmpty()) {

				// Copy value from an existing node

				// Create XPath expression from arg2
				XPathExpression copyXPath = xpath.compile(arg2);

				// Parse XML document using XPath expression
				// Assign matching node to copyNode
				Node copyNode = (Node) copyXPath.evaluate(doc, XPathConstants.NODE);

				// Get text content of copyNode
				newValue = copyNode.getTextContent();

				// Console output only for debugging
				// To be removed on actual deployment
				System.out.println("New value from existing node: " + newValue);

			} else {

				// Both arguments are empty
				// Assigning blank value
				newValue = "";

				// Console output only for debugging
				// To be removed on actual deployment
				System.out.println("Assigning blank value.");
			}

			//
			// Do addNode proper
			//

			// Create XPath expression from arg0
			XPathExpression addXPath = xpath.compile(arg0);

			// Parse XML document using XPath expression
			// Assign matching node to copyNode
			// Node addNode = (Node) addXPath.

			// addNode.appendChild(addNode);
			// addNode.setTextContent(newValue);

			// Create new Transformer with the XSLT
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();

			// Create a temporary variable for transformed XML storage
			StringWriter stringWriter = new StringWriter();

			// Assign transformed XML document to a temporary variable
			transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println(stringWriter.toString());

			// Export modified XML document to output stream
			outputstream.write(stringWriter.toString().getBytes());

			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Deletion completed.");

		} catch (Exception exception) {
			// Console output only for debugging
			// To be removed on actual deployment
			System.out.println("Result: ERROR " + exception);
		}

	}

}
