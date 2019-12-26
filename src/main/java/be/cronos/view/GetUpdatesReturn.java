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

package be.cronos.view;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {
                "result",
                "newKey",
                "newKeyId",
                "terminations"
        }
)
@XmlRootElement(name = "getUpdatesReturn", namespace = DsessConstants.SMS_NS)
public class GetUpdatesReturn {

    @XmlElement(name = "result", namespace = DsessConstants.SMS_NS)
    private int result;
    @XmlElement(name = "newKey", namespace = DsessConstants.SMS_NS)
    private String newKey;
    @XmlElement(name = "newKeyID", namespace = DsessConstants.SMS_NS)
    private int newKeyId;
    @XmlElement(name = "terminations", namespace = DsessConstants.SMS_NS, nillable = true)
    private ArrayList<TerminationsDataReturn> terminations;

    public GetUpdatesReturn() {
    }

    public GetUpdatesReturn(int result, String newKey, int newKeyId) {
        this.result = result;
        this.newKey = newKey;
        this.newKeyId = newKeyId;
    }

    public GetUpdatesReturn(int result, String newKey, int newKeyId, ArrayList<TerminationsDataReturn> terminations) {
        this(result, newKey, newKeyId);
        this.terminations = terminations;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getNewKey() {
        return newKey;
    }

    public void setNewKey(String newKey) {
        this.newKey = newKey;
    }

    public int getNewKeyId() {
        return newKeyId;
    }

    public void setNewKeyId(int newKeyId) {
        this.newKeyId = newKeyId;
    }

    public ArrayList<TerminationsDataReturn> getTerminations() {
        return terminations;
    }

    public void setTerminations(ArrayList<TerminationsDataReturn> terminations) {
        this.terminations = terminations;
    }
}
