package be.cronos.interceptors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

/**
 * The writer interceptor is required to rewrite "custom/xml" mimetype to "text/xml", this is performed
 * AFTER @{@link be.cronos.marshallers.DSCProtocolWriter}.
 */
@Provider
public class MimeTypeWriterInterceptor implements WriterInterceptor {
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        context.getHeaders().putSingle("Content-Type", "text/xml");
        context.proceed();
    }
}
