package com.mapping.xmlcustomizer.getrules;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sap.aii.mapping.api.StreamTransformationException;

public class XCgetDocType {

	public String[] executeXCgetDocType(InputStream in) throws StreamTransformationException {

		String doctype = "";
		String table = "";
		String direction = "";
		String standard = "";
		String message = "";
		String version = "";
		String partnertype = "";
		String partner = "";
		String company = "";

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);

			// Create XPath expression from arg0
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			// Check if IDOC
			XPathExpression idocXPath = xpath.compile("//EDI_DC40");
			NodeList idocNodes = (NodeList) idocXPath.evaluate(doc, XPathConstants.NODESET);

			// Check if EDIFACT
			XPathExpression edifactXPath = xpath.compile("//S_UNA");
			NodeList edifactNodes = (NodeList) edifactXPath.evaluate(doc, XPathConstants.NODESET);

			// Check if X12
			XPathExpression x12XPath = xpath.compile("//S_ISA");
			NodeList x12Nodes = (NodeList) x12XPath.evaluate(doc, XPathConstants.NODESET);

			// Check if XML
			XPathExpression xmlXPath = xpath.compile("//MsgHeader");
			NodeList xmlNodes = (NodeList) xmlXPath.evaluate(doc, XPathConstants.NODESET);

			XPathExpression directionXPath = xpath.compile("");
			XPathExpression standardXPath = xpath.compile("");
			XPathExpression messageXPath = xpath.compile("");
			XPathExpression versionXPath = xpath.compile("");
			XPathExpression partnertypeXPath = xpath.compile("");
			XPathExpression partnerXPath = xpath.compile("");
			XPathExpression companyXPathKU = xpath.compile("");
			XPathExpression companyXPathLI = xpath.compile("");

			if (idocNodes.getLength() > 0) {

				doctype = "IDOC";
				directionXPath = xpath.compile("//DIRECT");
				standardXPath = xpath.compile("//STD");
				messageXPath = xpath.compile("//STDMES");
				versionXPath = xpath.compile("//STDVRS");
				partnertypeXPath = xpath.compile("//RCVPRT");
				partnerXPath = xpath.compile("//RCVPRN");
				companyXPathKU = xpath.compile("//E1EDKA1[PARVW='AG']/LIFNR");
				companyXPathLI = xpath.compile("//E1EDKA1[PARVW='AG']/PARTN");

			} else if (edifactNodes.getLength() > 0) {
				doctype = "EDFICAT";
			} else if (x12Nodes.getLength() > 0) {
				doctype = "X12";
			} else if (xmlNodes.getLength() > 0) {
				doctype = "XML";
			} else {
				doctype = "NotSupported";
			}

			Node directionNode = (Node) directionXPath.evaluate(doc, XPathConstants.NODE);
			if (directionNode.getTextContent().equals("1")) {
				direction = "1";
				table = "1.1.LOOKUP";
			} else {
				direction = "2";
				table = "1.2.LOOKUP";
			}

			Node standardNode = (Node) standardXPath.evaluate(doc, XPathConstants.NODE);
			standard = standardNode.getTextContent();

			Node messageNode = (Node) messageXPath.evaluate(doc, XPathConstants.NODE);
			message = messageNode.getTextContent();

			Node versionNode = (Node) versionXPath.evaluate(doc, XPathConstants.NODE);
			version = versionNode.getTextContent();

			Node partnertypeNode = (Node) partnertypeXPath.evaluate(doc, XPathConstants.NODE);
			partnertype = partnertypeNode.getTextContent();

			Node partnerNode = (Node) partnerXPath.evaluate(doc, XPathConstants.NODE);
			partner = partnerNode.getTextContent();

			Node companyKUNode = (Node) companyXPathKU.evaluate(doc, XPathConstants.NODE);
			Node companyLINode = (Node) companyXPathLI.evaluate(doc, XPathConstants.NODE);

			if (partnertype.equals("KU")) {
				company = companyKUNode.getTextContent();
			} else if ((partnertype.equals("LI")) && !companyLINode.getTextContent().isEmpty()) {
				company = companyLINode.getTextContent();
			}

		} catch (Exception exception) {
			// TODO: handle exception
		}

		return new String[] { doctype, table, direction, standard, message, version, partnertype, partner, company };

	}
}
