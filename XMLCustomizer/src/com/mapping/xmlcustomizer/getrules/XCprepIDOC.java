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

import com.mapping.xmlcustomizer.XMLCustomizer;
import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;

public class XCprepIDOC extends XMLCustomizer {

	public String[] executeXCprepIDOC(StringBuilder in, AbstractTrace trace) throws StreamTransformationException {

		String table = "";
		String direction = "";
		String standard = "";
		String message = "";
		String version = "";
		String partnertype = "";
		String partner = "";
		String company = "";
		String processor = "";

		trace.addInfo("Class XCprepIDOC: Preparing IDOC VM Keys");

		// Assign variables for the XML string
		StringBuilder inputString = in;

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(inputString.toString())));

			// Create XPath expression from arg0
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			XPathExpression directionXPath = xpath.compile("//DIRECT");
			Node directionNode = (Node) directionXPath.evaluate(doc, XPathConstants.NODE);
			if (directionNode.getTextContent().equals("1")) {
				direction = "1";
				table = "1.1.LOOKUP";
			} else {
				direction = "2";
				table = "1.2.LOOKUP";
			}

			XPathExpression standardXPath = xpath.compile("//STD");
			Node standardNode = (Node) standardXPath.evaluate(doc, XPathConstants.NODE);
			standard = standardNode.getTextContent();

			XPathExpression messageXPath = xpath.compile("//STDMES");
			Node messageNode = (Node) messageXPath.evaluate(doc, XPathConstants.NODE);
			message = messageNode.getTextContent();

			XPathExpression versionXPath = xpath.compile("//STDVRS");
			Node versionNode = (Node) versionXPath.evaluate(doc, XPathConstants.NODE);
			version = versionNode.getTextContent();

			XPathExpression partnertypeXPath = xpath.compile("//RCVPRT");
			Node partnertypeNode = (Node) partnertypeXPath.evaluate(doc, XPathConstants.NODE);
			partnertype = partnertypeNode.getTextContent();

			XPathExpression partnerXPath = xpath.compile("//RCVPRN");
			Node partnerNode = (Node) partnerXPath.evaluate(doc, XPathConstants.NODE);
			partner = partnerNode.getTextContent();

			XPathExpression companyXPathKU = xpath.compile("//E1EDKA1[PARVW='AG']/LIFNR");
			Node companyKUNode = (Node) companyXPathKU.evaluate(doc, XPathConstants.NODE);

			XPathExpression companyXPathLI = xpath.compile("//E1EDKA1[PARVW='AG']/PARTN");
			Node companyLINode = (Node) companyXPathLI.evaluate(doc, XPathConstants.NODE);

			if (partnertype.equals("KU")) {
				company = companyKUNode.getTextContent();
			} else if ((partnertype.equals("LI")) && !companyLINode.getTextContent().isEmpty()) {
				company = companyLINode.getTextContent();
			}
			
			if (direction.equals("1")) {
				processor = "Pre";
			} else  {
				processor = "Post";
			}

		} catch (Exception exception) {
			
			trace.addInfo("Class XCprepIDOC error: " + exception);
		}

		return new String[] { table, direction, standard, message, version, partnertype, partner, company, processor };

	}
}