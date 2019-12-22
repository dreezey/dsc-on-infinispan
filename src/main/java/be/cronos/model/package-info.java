@XmlSchema(
        xmlns = {
                @XmlNs(prefix = "ns1", namespaceURI = "http://sms.am.tivoli.com"),
                @XmlNs(prefix = "SOAP-ENV", namespaceURI = "http://schemas.xmlsoap.org/soap/envelope/"),
                @XmlNs(prefix = "xsi", namespaceURI = "http://www.w3.org/2001/XMLSchema-instance")
        },
        elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED
)
package be.cronos.model;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;