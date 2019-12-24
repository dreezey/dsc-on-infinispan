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

import org.jboss.logmanager.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes(MediaType.TEXT_XML)
public class DSCProtocolReader implements MessageBodyReader {
    private static final Logger LOG = Logger.getLogger(DSCProtocolReader.class.getName());

    @Override
    public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.isAnnotationPresent(XmlRootElement.class);
    }

    @Override
    public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        try {
            LOG.finest("Manual document building");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(entityStream);

            Node soapEnvelope = doc.getFirstChild();
            LOG.finest(soapEnvelope.getLocalName());
            LOG.finest(soapEnvelope.toString());
            NodeList children = soapEnvelope.getChildNodes();
            Node soapBody = null;
            // Now attempt to find the body
            for (int i=0; i < children.getLength(); i++) {
                Node child = children.item(i);
                LOG.finest("child: " + child.getLocalName());
                if (child.getLocalName() != null && child.getLocalName().equalsIgnoreCase("body")) {
                    soapBody = child;
                    break;
                }
            }

            if (soapBody == null) throw new JAXBException("No SOAP Body found");

            children = soapBody.getChildNodes();
            JAXBContext context = JAXBContext.newInstance(type);
            Object unmarshal = null;
            // Automatically unmarshal to desired class
            for (int i=0; i < children.getLength(); i++) {
                Node child = children.item(i);
                LOG.finest("child: " + child.getLocalName());
                if (child.getLocalName() != null) {
                    unmarshal = context.createUnmarshaller().unmarshal(child);
                    break;
                }
            }

            if (unmarshal == null) throw new JAXBException("Failed to unmarshal");

            LOG.finest("Unmarshal to class = " + unmarshal.getClass());

            return unmarshal;
        } catch (ParserConfigurationException | SAXException | JAXBException ex) {
            LOG.severe("When unmarshaling, an exception occurred, unable to continue: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
