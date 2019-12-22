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
//            JAXBContext context = JAXBContext.newInstance(type);
            Node soapEnvelope = doc.getFirstChild();
            LOG.finest(soapEnvelope.getLocalName());
            LOG.finest(soapEnvelope.toString());
            NodeList children = soapEnvelope.getChildNodes();
            Node soapBody = null;
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
            Object unmarshall = null;
            for (int i=0; i < children.getLength(); i++) {
                Node child = children.item(i);
                LOG.finest("child: " + child.getLocalName());
                if (child.getLocalName() != null) {
                    unmarshall = context.createUnmarshaller().unmarshal(child);
                    break;
                }
            }

            if (unmarshall == null) throw new JAXBException("Failed to unmarshall");

            LOG.finest("Unmarshalled to class = " + unmarshall.getClass());
//            if (unmarshall instanceof GetUpdatesRequest) {
//                GetUpdatesRequest getUpdatesRequest = (GetUpdatesRequest) unmarshall;
//                LOG.info("replica =" + getUpdatesRequest.getReplica());
//            } else if (unmarshall instanceof PingRequest) {
//                PingRequest pingRequest = (PingRequest) unmarshall;
//                LOG.info("Something = " + pingRequest.getSomething());
//            }

            return unmarshall;


//

//            Node soapBody = soapEnvelope.getFirstChild();
//            LOG.info(soapBody.getNodeName());

//            throw new JAXBException("denied");
//            Object unmarshall = context.createUnmarshaller().unmarshal(entityStream);
//            LOG.info("test = " + unmarshall.getClass());
//            LOG.info("test = " + unmarshall.toString());
//            return null;
        } catch (ParserConfigurationException | SAXException | JAXBException ex) {
            LOG.info(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
