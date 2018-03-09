package com.grundfos.mapping.xc.dynamicconfiguration;

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
import com.sap.aii.mapping.api.DynamicConfiguration;
import com.sap.aii.mapping.api.DynamicConfigurationKey;
import com.sap.aii.mapping.api.StreamTransformationException;

public class XCsetDyamicConfig {

	public void executeXCsetDyamicConfig(String dcSource, DynamicConfiguration dynConfig, String namespaceDC,
			String nameDC, String inValue, StringBuilder in, AbstractTrace trace) throws StreamTransformationException {

		trace.addInfo("Class XCreplaceValue: Starting replace value routine");

		DynamicConfigurationKey keyDC = DynamicConfigurationKey.create(namespaceDC, nameDC);

		if (dcSource.equals("setDynamicConfigConstant")) {
			try {
				dynConfig.put(keyDC, inValue);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (dcSource.equals("setDynamicConfigXPath")) {

			// Assign variables for the XML string
			StringBuilder inputString = in;

			// Create a variable for output XML string
			// Initialize to ensure it is empty as start

			String dcValue = "";

			try {

				// Create XML document from input string
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document xmlDocument = builder.parse(new InputSource(new StringReader(inputString.toString())));

				// Create XPath expression
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xPath = xPathfactory.newXPath();

				try {
					// Copy value from an existing node

					// Create XPath expression from arg2
					XPathExpression dcXPath = xPath.compile(inValue);

					try {
						// Parse XML document using XPath expression
						// Assign matching node to copyNode
						Node dcCopyNode = (Node) dcXPath.evaluate(xmlDocument, XPathConstants.NODE);
						// Get text content of copyNode
						dcValue = dcCopyNode.getTextContent();
					} catch (Exception e) {
						// Treat as string right away
						dcValue = (String) dcXPath.evaluate(xmlDocument, XPathConstants.STRING);
					}

				} catch (Exception e) {
					dcValue = "";
					trace.addInfo("Class XCaddNode Error: Assigning blank value " + e);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			dynConfig.put(keyDC, dcValue);
		}

	}

}
