package com.grundfos.mapping.xmlcustomizer.getrules;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sap.aii.mapping.api.AbstractTrace;
import com.sap.aii.mapping.api.StreamTransformationException;

public class XCgetDocType {

	public String executeXCgetDocType(StringBuilder in, AbstractTrace trace) throws StreamTransformationException {

		/**
		 * This method will identify the document type that is being processed.
		 * Different rules will be used to identify VM keys per document. This
		 * is a prerequisite for getVMentry proper.
		 */

		String docType = "";

		// Assign variables for the XML string
		StringBuilder inputString = in;

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(inputString.toString())));

			// Create XPath expression from arg0
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xPath = xPathfactory.newXPath();

			// Check if IDOC
			XPathExpression idocXPath = xPath.compile("//EDI_DC40");
			NodeList idocNodes = (NodeList) idocXPath.evaluate(doc, XPathConstants.NODESET);

			// Check if EDIFACT
			XPathExpression edifactXPath = xPath.compile("//S_UNA");
			NodeList edifactNodes = (NodeList) edifactXPath.evaluate(doc, XPathConstants.NODESET);

			// Check if X12
			XPathExpression x12XPath = xPath.compile("//S_ISA");
			NodeList x12Nodes = (NodeList) x12XPath.evaluate(doc, XPathConstants.NODESET);

			// Check if XML
			XPathExpression xmlXPath = xPath.compile("//MsgHeader");
			NodeList xmlNodes = (NodeList) xmlXPath.evaluate(doc, XPathConstants.NODESET);

			if (idocNodes.getLength() > 0) {
				docType = "IDOC";
			} else if (edifactNodes.getLength() > 0) {
				docType = "EDIFACT";
			} else if (x12Nodes.getLength() > 0) {
				docType = "X12";
			} else if (xmlNodes.getLength() > 0) {
				docType = "XML";
			} else {
				docType = "NotSupported";
			}

		} catch (Exception exception) {

			trace.addInfo("Class XCgetDocType error: " + exception);
		}

		return docType;

	}
}
