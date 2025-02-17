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

import be.cronos.view.DSCResultCode;
import be.cronos.view.PingRequest;
import be.cronos.view.PingResponse;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PingService {
    private static final Logger LOG = Logger.getLogger(PingService.class.getName());

    public PingResponse pong(PingRequest pingRequest) {
        return new PingResponse(DSCResultCode.OK.getResultCode());
    }

}
