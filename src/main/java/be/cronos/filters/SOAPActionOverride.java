package be.cronos.filters;

import org.jboss.logmanager.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;

@Provider
@PreMatching
public class SOAPActionOverride implements ContainerRequestFilter {
    private static final Logger LOG = Logger.getLogger(SOAPActionOverride.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) {
        LOG.finest("> filter()");
        // Get the current Request URI
        URI requestUri = requestContext.getUriInfo().getRequestUri();
        if (requestUri.getPath().endsWith("health")) return;

        // Make sure "SOAPAction" HTTP Header is sent
        if (requestContext.getHeaderString("SOAPAction") == null) {
            throw new BadRequestException(
                    Response
                            .status(400)
                            .build()
            );
        }
        // Get the SOAP Action HTTP Header
        String SOAPAction = requestContext.getHeaderString("SOAPAction").replaceAll("\"", "");

        LOG.finest("Request URI = " + requestUri);
        LOG.finest("SOAP Action = " + SOAPAction);
        // Construct new URI based on SOAP Action
        String routedSOAPActionUri = String.format("%s/%s", requestUri.toString(), SOAPAction);
        URI uriOverride = UriBuilder.fromUri(routedSOAPActionUri).build();
        LOG.finest("Routed Request URI = " + uriOverride);
        // Set the new route
        requestContext.setRequestUri(uriOverride);
        LOG.finest("< filter()");
    }
}
