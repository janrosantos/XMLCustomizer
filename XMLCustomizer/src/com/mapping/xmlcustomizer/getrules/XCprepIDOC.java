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

	public String[] executeXCprepIDOC(StringBuilder in, String omParam, AbstractTrace trace)
			throws StreamTransformationException {

		String initTable = "";
		String direction = "";
		String standard = "";
		String message = "";
		String version = "";
		String partnertype = "";
		String partner = "";
		String company = "";

		String rcvlad = "";
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
				XPathExpression rcvladXPath = xpath.compile("//RCVLAD");
				Node rcvladNode = (Node) rcvladXPath.evaluate(doc, XPathConstants.NODE);
				rcvlad = rcvladNode.getTextContent();
			} catch (Exception e) {
				rcvlad = "";
			}

			// Check for XC validity
			if (rcvlad.contains("XCO")) {

				initTable = "1.0.LOOKUP";
				direction = "1";

				if (rcvlad.contains("XCOPRE")) {

					// Most common usage for IDOCs
					xcTable = "4.1.CUSTOM.XML.PRE";

				} else if (rcvlad.contains("XCOPOST")) {

					// Limited case following this scenario
					xcTable = "4.1.CUSTOM.XML.POST";

				}

				// Other information to be acquired from the IDOC

				try {
					XPathExpression standardXPath = xpath.compile("//STD");
					Node standardNode = (Node) standardXPath.evaluate(doc, XPathConstants.NODE);
					standard = standardNode.getTextContent();
				} catch (Exception e) {
					standard = "";
				}

				try {
					XPathExpression messageXPath = xpath.compile("//STDMES");
					Node messageNode = (Node) messageXPath.evaluate(doc, XPathConstants.NODE);
					message = messageNode.getTextContent();
				} catch (Exception e) {
					message = "";
				}

				try {
					XPathExpression versionXPath = xpath.compile("//STDVRS");
					Node versionNode = (Node) versionXPath.evaluate(doc, XPathConstants.NODE);
					version = versionNode.getTextContent();
				} catch (Exception e) {
					version = "";
				}

				try {
					XPathExpression partnertypeXPath = xpath.compile("//RCVPRT");
					Node partnertypeNode = (Node) partnertypeXPath.evaluate(doc, XPathConstants.NODE);
					partnertype = partnertypeNode.getTextContent();
				} catch (Exception e) {
					partnertype = "";
				}

				try {
					XPathExpression partnerXPath = xpath.compile("//RCVPRN");
					Node partnerNode = (Node) partnerXPath.evaluate(doc, XPathConstants.NODE);
					partner = partnerNode.getTextContent();
				} catch (Exception e) {
					partner = "";
				}

				// TODO
				// Define better logic for company code identification
				if (partnertype.equals("KU")) {

					try {
						XPathExpression companyXPathKU = xpath.compile("//E1EDKA1[PARVW='AG']/LIFNR");
						Node companyKUNode = (Node) companyXPathKU.evaluate(doc, XPathConstants.NODE);
						company = companyKUNode.getTextContent().substring(companyKUNode.getTextContent().length() - 4,
								companyKUNode.getTextContent().length() - 1);
					} catch (Exception e) {
						company = "";

					}
				} else if (partnertype.equals("LI")) {

					try {
						XPathExpression companyXPathLI = xpath.compile("//E1EDKA1[PARVW='AG']/PARTN");
						Node companyLINode = (Node) companyXPathLI.evaluate(doc, XPathConstants.NODE);
						company = companyLINode.getTextContent().substring(companyLINode.getTextContent().length() - 4,
								companyLINode.getTextContent().length() - 1);
					} catch (Exception e) {
						company = "";
					}

				}

			} else if (rcvlad.contains("XCIPOST")) {

				// Will be commonly used
				// Details will still be acquired from the
				// IDOC control record header
				initTable = "1.0.LOOKUP";
				direction = "2";
				xcTable = "4.2.CUSTOM.XML.POST";

				try {
					XPathExpression standardXPath = xpath.compile("//STD");
					Node standardNode = (Node) standardXPath.evaluate(doc, XPathConstants.NODE);
					standard = standardNode.getTextContent();
				} catch (Exception e) {
					standard = "";
				}

				try {
					XPathExpression messageXPath = xpath.compile("//STDMES");
					Node messageNode = (Node) messageXPath.evaluate(doc, XPathConstants.NODE);
					message = messageNode.getTextContent();
				} catch (Exception e) {
					message = "";
				}

				try {
					XPathExpression versionXPath = xpath.compile("//STDVRS");
					Node versionNode = (Node) versionXPath.evaluate(doc, XPathConstants.NODE);
					version = versionNode.getTextContent();
				} catch (Exception e) {
					version = "";
				}

				try {
					XPathExpression partnertypeXPath = xpath.compile("//SNDPRT");
					Node partnertypeNode = (Node) partnertypeXPath.evaluate(doc, XPathConstants.NODE);
					partnertype = partnertypeNode.getTextContent();
				} catch (Exception e) {
					partnertype = "";
				}

				try {
					XPathExpression partnerXPath = xpath.compile("//SNDPRN");
					Node partnerNode = (Node) partnerXPath.evaluate(doc, XPathConstants.NODE);
					partner = partnerNode.getTextContent();
				} catch (Exception e) {
					partner = "";
				}

				// TODO
				// Define better logic for company code identification

				if (partnertype.equals("KU")) {

					try {
						XPathExpression companyXPathKU = xpath.compile("//E1EDKA1[PARVW='LF']/PARTN");
						Node companyKUNode = (Node) companyXPathKU.evaluate(doc, XPathConstants.NODE);

						company = companyKUNode.getTextContent().substring(companyKUNode.getTextContent().length() - 4,
								companyKUNode.getTextContent().length() - 1);
					} catch (Exception e) {
						company = "";

					}
				} else if (partnertype.equals("LI")) {

					try {
						XPathExpression companyXPathLI = xpath.compile("//E1EDKA1[PARVW='RE']/PARTN");
						Node companyLINode = (Node) companyXPathLI.evaluate(doc, XPathConstants.NODE);
						company = companyLINode.getTextContent().substring(companyLINode.getTextContent().length() - 4,
								companyLINode.getTextContent().length() - 1);
					} catch (Exception e) {
						company = "";
					}

				}

			} else if ((rcvlad.isEmpty() && omParam.contains("XCIPRE")) || (rcvlad.contains("XCIPRE") && omParam.contains("XCIPRE"))) {

				initTable = "1.0.LOOKUP";
				direction = "2";

				// Very unlikely case
				xcTable = "4.2.CUSTOM.XML.PRE";

				// TODO
				// Acquire details from OM parameter
				// Format:XCIPRE_E96AZZ1

				standard = omParam.substring(7, 8);

				try {
					XPathExpression messageXPath = xpath.compile("//MESTYP");
					Node messageNode = (Node) messageXPath.evaluate(doc, XPathConstants.NODE);
					message = messageNode.getTextContent();
				} catch (Exception e) {
					message = "";
				}

				version = omParam.substring(8, 14);

				partnertype = "";

				partner = "";

				company = omParam.substring(14, 16);

			} else {

				// No config for XC
				// Do nothing

			}

		} catch (Exception exception) {

			trace.addInfo("Class XCprepIDOC error: " + exception);
		}

		return new String[] { initTable, direction, standard, message, version, partnertype, partner, company, "", "",
				"", xcTable };

	}
}