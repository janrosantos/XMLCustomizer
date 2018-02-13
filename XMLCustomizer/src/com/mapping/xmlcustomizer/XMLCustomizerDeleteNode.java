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

		// arg0 is the node to be deleted
		// arg1 is not used
		// arg2 is not used
		// arg3 is not used

		System.out.println("Entering delete sub routine.");

		String RESULT = new String();

		InputStream inputstream = in;
		OutputStream outputstream = out;

		arg0 = "/Message/Item[Qualf='B']/Field2[2]";

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputstream);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(arg0);

			Node delNode = (Node) expr.evaluate(doc, XPathConstants.NODE);
			
			delNode.getParentNode().removeChild(delNode);

			// Create new Transformer with the XSLT
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();

			// create variables for the output
			StringWriter stringWriter = new StringWriter();

			// transform
			transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));

			// output, so we see it is wrong
			System.out.println(stringWriter.toString());

			
			outputstream.write(stringWriter.toString().getBytes());

		} catch (Exception exception) {
			System.out.println("Result: ERROR " + exception);
		}

	}

}
