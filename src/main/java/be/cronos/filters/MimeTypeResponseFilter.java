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

package be.cronos.filters;

import be.cronos.DsessConstants;
import org.jboss.logmanager.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is executed before marshalling, and must be done because JAX has selected probably selected "text/xml" as
 * out outgoing MIME Type, which will never pass the @{@link be.cronos.marshallers.DSCProtocolWriter} and thus will not
 * be a valid SOAP Message.
 */
@Provider
public class MimeTypeResponseFilter implements ContainerResponseFilter {
    private static final Logger LOG = Logger.getLogger(MimeTypeResponseFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        LOG.finest("content types = " + responseContext.getHeaders().get("Content-Type"));
        List<Object> contentTypes = responseContext.getHeaders().get("Content-Type");
        if (contentTypes == null) {
            LOG.fine("Could not find 'Content-Type' in response, probably something else failed.");
            return;
        }
        AtomicBoolean overrideMimeType = new AtomicBoolean(false);
        contentTypes.forEach(contentType -> {
            if (contentType instanceof MediaType) {
                MediaType mediaType = (MediaType) contentType;
                LOG.finest("MediaType = " + mediaType.isCompatible(MediaType.valueOf("text/xml")));
                if (mediaType.isCompatible(MediaType.valueOf("text/xml"))) {
                    overrideMimeType.set(true);
                }
            }
        });
        if (overrideMimeType.get()) {
            responseContext.getHeaders().putSingle("Content-Type", DsessConstants.CUSTOM_XML_MIMETYPE);
        }
        LOG.finest("outgoing content types = " + responseContext.getHeaders().get("Content-Type"));
    }
}
