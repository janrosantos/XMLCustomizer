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
		String partnerType = "";
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
			XPath xPath = xPathfactory.newXPath();

			// Check RCVLAD to identify direction and
			// determine if pre or post processing
			// This will also determinE if the VM keys will come
			// from the IDOC or from RCVLAD

			try {
				XPathExpression rcvladXPath = xPath.compile("//RCVLAD");
				Node rcvladNode = (Node) rcvladXPath.evaluate(doc, XPathConstants.NODE);
				rcvlad = rcvladNode.getTextContent();
			} catch (Exception e) {
				rcvlad = "";
			}

			// Check for XC validity
			// if (rcvlad.contains("XCO")) {
			if (rcvlad.contains("XC")) {

				initTable = "1.0.LOOKUP";

				try {
					XPathExpression directionXPath = xPath.compile("//DIRECT");
					Node directionNode = (Node) directionXPath.evaluate(doc, XPathConstants.NODE);
					direction = directionNode.getTextContent();
				} catch (Exception e) {
					direction = "2";
				}

				if (direction.equals("1")) {

					try {
						XPathExpression partnertypeXPath = xPath.compile("//RCVPRT");
						Node partnertypeNode = (Node) partnertypeXPath.evaluate(doc, XPathConstants.NODE);
						partnerType = partnertypeNode.getTextContent();
					} catch (Exception e) {
						partnerType = "";
					}

					try {
						XPathExpression partnerXPath = xPath.compile("//RCVPRN");
						Node partnerNode = (Node) partnerXPath.evaluate(doc, XPathConstants.NODE);
						partner = partnerNode.getTextContent();
					} catch (Exception e) {
						partner = "";
					}
					if (partnerType.equals("KU")) {

						try {
							XPathExpression companyXPathKU = xPath.compile("//E1EDKA1[PARVW='AG']/LIFNR");
							Node companyKUNode = (Node) companyXPathKU.evaluate(doc, XPathConstants.NODE);
							company = companyKUNode.getTextContent().substring(
									companyKUNode.getTextContent().length() - 3,
									companyKUNode.getTextContent().length());
						} catch (Exception e) {
							company = "";

						}
					} else if (partnerType.equals("LI")) {

						try {
							XPathExpression companyXPathLI = xPath.compile("//E1EDKA1[PARVW='AG']/PARTN");
							Node companyLINode = (Node) companyXPathLI.evaluate(doc, XPathConstants.NODE);
							company = companyLINode.getTextContent().substring(
									companyLINode.getTextContent().length() - 3,
									companyLINode.getTextContent().length());
						} catch (Exception e) {
							company = "";
						}

					}

					if (rcvlad.contains("XCPRE")) {
						// Most common usage for IDOCs
						xcTable = "4.1.CUSTOM.XML.PRE";

					} else if (rcvlad.contains("XCPOST")) {

						// Little possibility of usage
						xcTable = "4.1.CUSTOM.XML.POST";
					}

				} else if (direction.equals("2")) {

					try {
						XPathExpression partnertypeXPath = xPath.compile("//SNDPRT");
						Node partnertypeNode = (Node) partnertypeXPath.evaluate(doc, XPathConstants.NODE);
						partnerType = partnertypeNode.getTextContent();
					} catch (Exception e) {
						partnerType = "";
					}

					try {
						XPathExpression partnerXPath = xPath.compile("//SNDPRN");
						Node partnerNode = (Node) partnerXPath.evaluate(doc, XPathConstants.NODE);
						partner = partnerNode.getTextContent();
					} catch (Exception e) {
						partner = "";
					}

					if (partnerType.equals("KU")) {
						// For Customer EDI
						try {
							XPathExpression companyXPathKU = xPath.compile("//RCVPRN");
							Node companyKUNode = (Node) companyXPathKU.evaluate(doc, XPathConstants.NODE);

							company = companyKUNode.getTextContent().substring(
									companyKUNode.getTextContent().length() - 3,
									companyKUNode.getTextContent().length());
						} catch (Exception e) {
							company = "";

						}
					} else if (partnerType.equals("LI")) {
						// For Vendor EDI only
						// TODO Carrier EDI
						try {
							XPathExpression companyXPathLI = xPath.compile("//RCVPRN");
							Node companyLINode = (Node) companyXPathLI.evaluate(doc, XPathConstants.NODE);
							company = companyLINode.getTextContent().substring(
									companyLINode.getTextContent().length() - 3,
									companyLINode.getTextContent().length());
						} catch (Exception e) {
							company = "";
						}
					}

					if (rcvlad.contains("XCPRE")) {
						// Very unlikely
						xcTable = "4.2.CUSTOM.XML.PRE";

					} else if (rcvlad.contains("XCPOST")) {
						// Second most possible usage
						xcTable = "4.2.CUSTOM.XML.POST";
					}
				}

				// All details MUST be in the IDOC header
				// to qualify for XC usage

				try {
					XPathExpression standardXPath = xPath.compile("//STD");
					Node standardNode = (Node) standardXPath.evaluate(doc, XPathConstants.NODE);
					standard = standardNode.getTextContent();
				} catch (Exception e) {
					standard = "";
				}

				try {
					XPathExpression messageXPath = xPath.compile("//STDMES");
					Node messageNode = (Node) messageXPath.evaluate(doc, XPathConstants.NODE);
					message = messageNode.getTextContent();
				} catch (Exception e) {
					message = "";
				}

				try {
					XPathExpression versionXPath = xPath.compile("//STDVRS");
					Node versionNode = (Node) versionXPath.evaluate(doc, XPathConstants.NODE);
					version = versionNode.getTextContent();
				} catch (Exception e) {
					version = "";
				}

			} else {

				// No config for XC
				// Do nothing

			}

		} catch (Exception exception) {

			trace.addInfo("Class XCprepIDOC error: " + exception);
		}

		return new String[] { initTable, direction, standard, message, version, partnerType, partner, company, "", "",
				"", xcTable };

	}
}