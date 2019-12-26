/*
 * Copyright 2019 IS4U NV. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.cronos.util;

import be.cronos.DsessConstants;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlTestUtils {
    public static String GetSoapMessage(Object o, Class<?> type) {
        try {
            // Construct a document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // Marshall the incoming class to the document
            JAXBContext jaxbContext = JAXBContext.newInstance(type);
            jaxbContext.createMarshaller().marshal(o, document);

            // Construct the SOAP message
            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            // Adopt the marshalled class in the body
            soapMessage.getSOAPBody().addDocument(document);

            // Add namespaces manually
            soapMessage.getSOAPPart().getEnvelope().addNamespaceDeclaration("xsd", DsessConstants.XSD_NS);
            soapMessage.getSOAPPart().getEnvelope().addNamespaceDeclaration("xsi", DsessConstants.XSI_NS);
            // Make sure the header is set: <?xml version='1.0' encoding='utf-8' ?>
            soapMessage.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
            // Remove the SOAP Header, we don't need that
            soapMessage.getSOAPHeader().detachNode();

            // And write to the output
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapMessage.writeTo(out);
            byte[] outputBytes = out.toByteArray();
            return new String(outputBytes);
        } catch (SOAPException | JAXBException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Headers getSoapRequestHeaders(ArrayList<Header> basicHeaders, String action) {
        List<Header> headerList = new ArrayList<Header>(basicHeaders) {{
            add(new Header("SOAPAction", String.format("\"%s\"", action)));
        }};
        return new Headers(headerList);
    }
}
