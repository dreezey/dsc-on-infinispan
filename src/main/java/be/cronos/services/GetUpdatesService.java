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

package be.cronos.services;

import be.cronos.DsessConstants;
import be.cronos.model.DSCResultCode;
import be.cronos.model.GetUpdatesRequest;
import be.cronos.model.GetUpdatesResponse;
import be.cronos.model.GetUpdatesReturn;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class GetUpdatesService {
    private final String CN = GetUpdatesService.class.getName();
    private static final Logger LOG = Logger.getLogger(GetUpdatesService.class.getName());

    public GetUpdatesResponse getUpdates(GetUpdatesRequest getUpdatesRequest) {
        // At some point, this will look in a graveyard of sort to detect terminated sessions
        LOG.entering(CN, "getUpdates");
        return new GetUpdatesResponse(
                new GetUpdatesReturn(
                        DSCResultCode.OK.getResultCode(),
                        DsessConstants.NEW_KEY,
                        0
                )
        );
    }
}
