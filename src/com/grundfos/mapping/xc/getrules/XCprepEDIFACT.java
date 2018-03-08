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

public class XCprepEDIFACT {

	public String[] executeXCprepEDIFACT(StringBuilder in, String omParam, AbstractTrace trace)
			throws StreamTransformationException {

		/**
		 * This method will prepare the necessary information from the EDIFACT
		 * EDI that will be used to acquire rule from PI cache
		 */

		String initTable = "";
		String direction = "";
		String standard = "";
		String message = "";
		String version = "";

		String xcIndicator = "";
		String xcTable = "";

		String[] edifactKey = new String[] {};

		trace.addInfo("Class XCprepEDIFACT: Preparing EDIFACT document keys");

		// Assign variables for the XML string
		StringBuilder inputString = in;

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(inputString.toString())));

			// Create XPath expression
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			// Check D_0001 content

			try {
				XPathExpression xcIndicatorXPath = xpath.compile("//D_0001");
				Node xcIndicatorNode = (Node) xcIndicatorXPath.evaluate(doc, XPathConstants.NODE);
				xcIndicator = xcIndicatorNode.getTextContent();
			} catch (Exception e) {
				xcIndicator = "";
			}

			/*-			 
			 * Post processing for OUT only
			 * Very unlikely to have INB post
			 * Use segment D_0001 for checking
			 * D_0001 should contain the indicator
			 * Format: UNOC-XCPOST-1E96AZZ1-LI:9999000004:996
			 * Retain syntax identifier after i.e. UNOC
			 */

			if (xcIndicator.contains("XCPOST-1")) {

				initTable = "1.0.LOOKUP";
				direction = "1";

				standard = "E";

				try {
					XPathExpression xcIndicatorXPath = xpath.compile("//D_0065");
					Node xcIndicatorNode = (Node) xcIndicatorXPath.evaluate(doc, XPathConstants.NODE);
					message = xcIndicatorNode.getTextContent();
				} catch (Exception e) {
					message = "";
				}

				String xcVar[] = xcIndicator.split("\\" + "-");
				String xcPartner[] = xcVar[3].split("\\" + ":");

				version = xcVar[2].substring(2, 8);

				String partnertype = xcPartner[0];
				String partner = xcPartner[1];
				String company = "";
				try {
					company = xcPartner[2];
				} catch (Exception e) {
					company = "";
				}
				xcTable = "4.1.CUSTOM.XML.POST";

				edifactKey = new String[] { initTable, direction, standard, message, version, partnertype, partner,
						company, "", "", "", xcTable };

			}

			/*-
			 * Pre processing is for IN only
			 * Very unlikely to have OUT pre
			 * Check the value of the parameter to determine XC qualification
			 * Format: XCPRE-2E96AZZ1
			 * Inbound pre processing is for A4, L4 and Z4 rules only
			 */

			else if (omParam.contains("XCPRE-2")) {

				initTable = "1.0.LOOKUP";
				direction = "2";

				standard = "E";

				try {
					XPathExpression messageXPath = xpath.compile("//D_0065");
					Node messageNode = (Node) messageXPath.evaluate(doc, XPathConstants.NODE);
					message = messageNode.getTextContent();
				} catch (Exception e) {
					message = "";
				}

				String xcVar[] = omParam.split("\\" + "-");
				version = xcVar[1].substring(2, 8);

				String senderGLN = "";
				String senderQualf = "";
				String receiverGLN = "";
				String receiverQualf = "";

				try {
					XPathExpression senderGLNXPath = xpath.compile("//C_S002/D_0004");
					Node senderGLNNode = (Node) senderGLNXPath.evaluate(doc, XPathConstants.NODE);
					senderGLN = senderGLNNode.getTextContent();
				} catch (Exception e) {
					senderGLN = "";
				}

				try {
					XPathExpression senderQualfXPath = xpath.compile("//C_S002/D_0007");
					Node senderQualfNode = (Node) senderQualfXPath.evaluate(doc, XPathConstants.NODE);
					senderQualf = senderQualfNode.getTextContent();
				} catch (Exception e) {
					senderQualf = "";
				}

				try {
					XPathExpression receiverGLNxPath = xpath.compile("//C_S003/D_0010");
					Node receiverGLNNode = (Node) receiverGLNxPath.evaluate(doc, XPathConstants.NODE);
					receiverGLN = receiverGLNNode.getTextContent();
				} catch (Exception e) {
					receiverGLN = "";
				}

				try {
					XPathExpression receiverQualfXPath = xpath.compile("//C_S003/D_0007");
					Node receiverQualfNode = (Node) receiverQualfXPath.evaluate(doc, XPathConstants.NODE);
					receiverQualf = receiverQualfNode.getTextContent();
				} catch (Exception e) {
					receiverQualf = "";
				}

				xcTable = "4.2.CUSTOM.XML.PRE";

				return new String[] { initTable, direction, standard, message, version, senderGLN, senderQualf,
						receiverGLN, receiverQualf, "", "", xcTable };

			} else {
				edifactKey = new String[] { initTable, direction, standard, message, version, "", "", "", "", "", "",
						xcTable };

			}

		} catch (Exception exception) {

			trace.addInfo("Class XCprepEDIFACT error: " + exception);
		}

		return edifactKey;

	}
}
