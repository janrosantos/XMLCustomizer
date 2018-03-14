package com.grundfos.mapping.xc.getrules;

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

public class XCprepIDOC {

	public String[] executeXCprepIDOC(StringBuilder in, AbstractTrace trace) throws StreamTransformationException {

		/**
		 * This method will prepare the necessary information from the IDOC that
		 * will be used to acquire rule from PI cache
		 */

		String initTable = "";
		String direction = "";
		String standard = "";
		String message = "";
		String version = "";
		String partnerType = "";
		String partnerFunction = "";
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

			// Create XPath expression
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xPath = xPathfactory.newXPath();

			// Check RCVLAD to identify if pre or post processing

			try {
				XPathExpression rcvladXPath = xPath.compile("//EDI_DC40/RCVLAD");
				Node rcvladNode = (Node) rcvladXPath.evaluate(doc, XPathConstants.NODE);
				rcvlad = rcvladNode.getTextContent();
			} catch (Exception e) {
				rcvlad = "";
			}

			// Check for XC validity
			if (rcvlad.contains("XC")) {

				// Table for generating lookup key
				initTable = "1.0.LOOKUP";

				try {
					XPathExpression directionXPath = xPath.compile("//EDI_DC40/DIRECT");
					Node directionNode = (Node) directionXPath.evaluate(doc, XPathConstants.NODE);
					direction = directionNode.getTextContent();
				} catch (Exception e) {
					direction = "2";
				}

				if (direction.equals("1")) {

					/*-
					 * For outbound IDOC transactions 
					 * Mainly for pre-processing rules
					 */

					try {
						XPathExpression partnerTypeXPath = xPath.compile("//EDI_DC40/RCVPRT");
						Node partnerTypeNode = (Node) partnerTypeXPath.evaluate(doc, XPathConstants.NODE);
						partnerType = partnerTypeNode.getTextContent();
					} catch (Exception e) {
						partnerType = "";
					}

					try {
						XPathExpression partnerTypeXPath = xPath.compile("//EDI_DC40/RCVPFC");
						Node partnertypeNode = (Node) partnerTypeXPath.evaluate(doc, XPathConstants.NODE);
						partnerFunction = partnertypeNode.getTextContent();
					} catch (Exception e) {
						partnerFunction = "";
					}

					try {
						XPathExpression partnerXPath = xPath.compile("//EDI_DC40/RCVPRN");
						Node partnerNode = (Node) partnerXPath.evaluate(doc, XPathConstants.NODE);
						partner = partnerNode.getTextContent();
						partner = String.format("%010d", Long.parseLong(partner));
					} catch (Exception e) {
						partner = "";
					}
					if (partnerType.equals("KU")) {

						// For Customer EDI only TODO
						// TODO
						// Source of VKORG/LIFNR varies per message type
						// Need to coordinate with SD to defined standard

						try {
							XPathExpression companyXPathKU = xPath.compile("//E1EDKA1[PARVW='AG']/LIFNR");
							Node companyKUNode = (Node) companyXPathKU.evaluate(doc, XPathConstants.NODE);
							company = companyKUNode.getTextContent().substring(
									companyKUNode.getTextContent().length() - 3,
									companyKUNode.getTextContent().length());
						} catch (Exception e) {
							company = "";

						}
					} else if ((partnerType.equals("LI")) && (partnerFunction.equals("LF"))) {

						// For Vendor EDI only
						// This includes Suppliers and DC

						try {

							if (message.equals("ORDERS")) {

								XPathExpression companyXPathLI = xPath.compile("//E1EDKA1[PARVW='AG']/PARTN");
								Node companyLINode = (Node) companyXPathLI.evaluate(doc, XPathConstants.NODE);
								company = companyLINode.getTextContent().substring(
										companyLINode.getTextContent().length() - 3,
										companyLINode.getTextContent().length());
							} else if (message.equals("DESADV") || message.equals("SHMNT")) {
								// TODO
								// Distribution Centers are also LI/LF
								company = "";
							}

						} catch (Exception e) {
							company = "";
						}

					} else if ((partnerType.equals("LI")) && (partnerFunction.equals("SP"))) {

						// For Carrier EDI only
						try {

							// TODO
							// Carrier EDI XPath
							XPathExpression companyXPathLI = xPath.compile("//E1EDL20/VKORG");
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
						xcTable = "9.1.CUSTOM.XML.PRE";

					} else if (rcvlad.contains("XCPOST")) {

						// Little possibility of usage
						xcTable = "9.1.CUSTOM.XML.POST";
					}

				} else if (direction.equals("2")) {

					/*-
					 * For inbound IDOC transactions 
					 * Mainly for post-processing
					 */

					try {
						XPathExpression partnertypeXPath = xPath.compile("//EDI_DC40/SNDPRT");
						Node partnertypeNode = (Node) partnertypeXPath.evaluate(doc, XPathConstants.NODE);
						partnerType = partnertypeNode.getTextContent();
					} catch (Exception e) {
						partnerType = "";
					}

					try {
						XPathExpression partnerXPath = xPath.compile("//EDI_DC40/SNDPRN");
						Node partnerNode = (Node) partnerXPath.evaluate(doc, XPathConstants.NODE);
						partner = partnerNode.getTextContent();
						partner = String.format("%010d", Long.parseLong(partner));
					} catch (Exception e) {
						partner = "";
					}

					if (partnerType.equals("KU")) {
						// For Customer EDI
						try {
							XPathExpression companyXPathKU = xPath.compile("//EDI_DC40/RCVPRN");
							Node companyKUNode = (Node) companyXPathKU.evaluate(doc, XPathConstants.NODE);

							company = companyKUNode.getTextContent().substring(
									companyKUNode.getTextContent().length() - 3,
									companyKUNode.getTextContent().length());
						} catch (Exception e) {
							company = "";

						}
					} else if (partnerType.equals("LI")) {

						// For Vendor EDI only
						// TODO
						// Carrier EDI

						try {
							XPathExpression companyXPathLI = xPath.compile("//EDI_DC40/RCVPRN");
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
						xcTable = "9.2.CUSTOM.XML.PRE";

					} else if (rcvlad.contains("XCPOST")) {
						// Second most possible usage
						xcTable = "9.2.CUSTOM.XML.POST";
					}
				}

				/*
				 * All details MUST be in the IDOC header to qualify for XC
				 * usage EDI Header data in EDIDC
				 */

				try {
					XPathExpression standardXPath = xPath.compile("//EDI_DC40/STD");
					Node standardNode = (Node) standardXPath.evaluate(doc, XPathConstants.NODE);
					standard = standardNode.getTextContent();
				} catch (Exception e) {
					standard = "";
				}

				try {
					XPathExpression messageXPath = xPath.compile("//EDI_DC40/STDMES");
					Node messageNode = (Node) messageXPath.evaluate(doc, XPathConstants.NODE);
					message = messageNode.getTextContent();
				} catch (Exception e) {
					message = "";
				}

				try {
					XPathExpression versionXPath = xPath.compile("//EDI_DC40/STDVRS");
					Node versionNode = (Node) versionXPath.evaluate(doc, XPathConstants.NODE);
					version = versionNode.getTextContent();
				} catch (Exception e) {
					version = "";
				}

			} else {

				/*-
				 * No configuration for XC
				 * Do nothing
				 */

			}

		} catch (Exception exception) {

			trace.addInfo("Class XCprepIDOC error: " + exception);
		}

		return new String[] { initTable, direction, standard, message, version, partnerType, partner, company, "", "",
				"", xcTable };

	}
}