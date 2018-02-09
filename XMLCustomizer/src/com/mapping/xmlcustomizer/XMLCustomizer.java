package com.mapping.xmlcustomizer;

import java.io.InputStream;
import java.io.OutputStream;
import com.sap.aii.mapping.api.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLCustomizer extends AbstractTransformation {

	@Override
	public void transform(TransformationInput transformationInput, TransformationOutput transformationOutput)
			throws StreamTransformationException {

		String operation = "";
		String arg0 = "";
		String arg1 = "";
		String arg2 = "";
		String arg3 = "";

		try {
			this.execute(operation, arg0, arg1, arg2, arg3, transformationInput.getInputPayload().getInputStream(),
					transformationOutput.getOutputPayload().getOutputStream());
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	public void execute(String operation, String arg0, String arg1, String arg2, String arg3, InputStream in,
			OutputStream out) throws StreamTransformationException {

		if (operation.equals("playground")) {

			System.out.println("Playground");
			XMLCustomizerPlayground playgroundXML = new XMLCustomizerPlayground();
			playgroundXML.executePlayground(in, out);

		} else if (operation.equals("addNode")) {

			System.out.println("Add node to XML");
			XMLCustomizerAddNode addNodeXML = new XMLCustomizerAddNode();
			addNodeXML.executeAddNode(arg0, arg1, arg2, arg3, in, out);

		} else if (operation.equals("deleteNode")) {

			System.out.println("Delete node from XML");
			XMLCustomizerDeleteNode deleteNodeXML = new XMLCustomizerDeleteNode();
			deleteNodeXML.executeDeleteNode(arg0, arg1, arg2, arg3, in, out);

		} else if (operation.equals("replaceValue")) {

			System.out.println("Replacing value of XML segment");
			XMLCustomizerReplaceValue replaceValueXML = new XMLCustomizerReplaceValue();
			replaceValueXML.executeReplaceValue(arg0, arg1, arg2, arg3, in, out);

		} else {
			System.out.println("Nothing to do");

		}
	}
}