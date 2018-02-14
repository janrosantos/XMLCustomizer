package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

import com.sap.aii.mapping.api.StreamTransformationException;
import com.sun.org.apache.xpath.internal.NodeSet;

public class XMLCustomizerDeleteNode {

	public void executeDeleteNode(String arg0, String arg1, String arg2, String arg3, InputStream in, OutputStream out)
			throws StreamTransformationException {

		// This method will execute deletion of nodes from the XML document
		// passed via the input stream
		// Node selection is based on the first argument

		// arg0 is the node/s to be deleted
		// arg0 is an xpath expression
		// arg1 is not used
		// arg2 is not used
		// arg3 is not used

		// Console output only for debugging
		// To be removed on actual deployment
		System.out.println("Entering node delete subroutine.");

		// Assign variables for the XML stream
		InputStream inputstream = in;
		OutputStream outputstream = out;

		try {

			// Create XML document from input stream
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputstream);

			// Create XPath expression from arg0
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression delXPath = xpath.compile(arg0);

			// Parse XML document using XPath expression
			// List all matches in delNode
			NodeList delNode = (NodeList) delXPath.evaluate(doc, XPathConstants.NODESET);

			// Execute deletion of nodes for all that matched the XPath
			// delNode is an array/list of nodes that matched the XPath
			for (int i = 0; i < delNode.getLength(); i++) {
				delNode.item(i).getParentNode().removeChild(delNode.item(i));
			}

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
