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
	public void transform(TransformationInput transformationInput,
			TransformationOutput transformationOutput)
			throws StreamTransformationException {

		try {
			this.execute(
					transformationInput.getInputPayload().getInputStream(),
					transformationOutput.getOutputPayload().getOutputStream());
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	public void execute(InputStream in, OutputStream out)
			throws StreamTransformationException {

		String RESULT = new String();

		String inicio = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<ns0:MT_test xmlns:ns0=\"urn:elrosado.com:switchTransaccional:listaNegra\">";
		String fin = "</ns0:MT_test>";
		String nombre = "";
		String apellido = "";
		String edad = "";

		try {

			InputStream inputstream = in;

			OutputStream outputstream = out;
			
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(inputstream);

			NodeList children = doc.getElementsByTagName("row");

			for (int i = 0; i < children.getLength(); i++) {

				Node node = children.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) node;

					nombre = eElement.getElementsByTagName("nombre").item(0)
							.getTextContent();

					apellido = eElement.getElementsByTagName("apellido")
							.item(0).getTextContent();

					edad = eElement.getElementsByTagName("edad").item(0)
							.getTextContent();

					RESULT = RESULT + "<row>" + "<nombre>" + nombre + " "
							+ apellido + "</nombre>" +

							"<apellido>" + nombre + " " + apellido
							+ "</apellido>" + "<edad>" + edad + "</edad>"
							+ "</row>";

				}

			}

			RESULT = inicio + RESULT + fin;

			outputstream.write(RESULT.getBytes());

		} catch (Exception exception) {

//			getTrace().addDebugMessage(exception.getMessage() + RESULT);

//			throw new StreamTransformationException(exception.toString()
//					+ RESULT);
			RESULT = "";

		}

	}

}
