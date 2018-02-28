package com.mapping.xmlcustomizer.getrules;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;

public class XCprepEDIFACT {

	public String[] executeXCprepEDIFACT(StringBuilder in, String omParam, AbstractTrace trace)
			throws StreamTransformationException {

		String initTable = "";
		String direction = "";
		String standard = "";
		String message = "";
		String version = "";
		String partnertype = "";
		String partner = "";
		String company = "";

		String xcIndicator = "";
		String xcTable = "";

		trace.addInfo("Class XCprepIDOC: Preparing IDOC document keys");

		// Assign variables for the XML string
		StringBuilder inputString = in;

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(inputString.toString())));

			// Create XPath expression from arg0
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			// Check RCVLAD to identify direction and
			// determine if pre or post processing
			// This will also determinE if the VM keys will come
			// from the IDOC or from RCVLAD

			try {
				XPathExpression rcvladXPath = xpath.compile("//D_0001");
				Node rcvladNode = (Node) rcvladXPath.evaluate(doc, XPathConstants.NODE);
				xcIndicator = rcvladNode.getTextContent();
			} catch (Exception e) {
				xcIndicator = "";
			}

			// Post processing for OUT only
			// Very unlikely to have INB post
			// Use segment D_0001 for checking
			// D_0001 should contain the indicator
			
			
			
			//TODO
			//Pre processing

		} catch (Exception exception) {

			trace.addInfo("Class XCprepIDOC error: " + exception);
		}

		return new String[] { initTable, direction, standard, message, version, partnertype, partner, company, "", "",
				"", xcTable };

	}

}
