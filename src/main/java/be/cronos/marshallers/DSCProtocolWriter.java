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

package be.cronos.marshallers;

import be.cronos.DsessConstants;
import org.jboss.logmanager.Logger;
import org.w3c.dom.Document;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(DsessConstants.CUSTOM_XML_MIMETYPE)
public class DSCProtocolWriter implements MessageBodyWriter {
    private static final Logger LOG = Logger.getLogger(DSCProtocolWriter.class.getName());

    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        LOG.finest("isWriteable");
        return type.isAnnotationPresent(XmlRootElement.class);
    }

    @Override
    public void writeTo(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream outputStream) throws IOException, WebApplicationException {
        LOG.finest("> writeTo()");
        LOG.finest("Output object class: " + o.getClass().getName());
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

            // And write to the outputstream
            soapMessage.writeTo(outputStream);
            LOG.finest("< writeTo()");
        } catch (ParserConfigurationException | JAXBException | SOAPException e) {
            LOG.severe("Unable to marshal to a SOAP message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getSize(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        LOG.finest("getSize");
        return 0;
    }
}
