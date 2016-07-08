/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     
 */

package org.nuxeo.labs.rating.operations;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.labs.rating.model.Rating;
import org.nuxeo.labs.rating.service.RatingService;
import org.nuxeo.runtime.api.Framework;

@Operation(id = GetRating.ID, category = Constants.CAT_DOCUMENT, label = "Rate", description = "")
public class GetRating {

    final JsonNodeFactory factory = JsonNodeFactory.instance;

    public static final String ID = "GetRating";

    @Context
    protected CoreSession session;

    @OperationMethod
    public Blob run(DocumentModel doc) {

        String username = session.getPrincipal().getName();
        RatingService service = Framework.getService(RatingService.class);
        Rating rating = service.getRating(session, doc.getId(), username);

        ObjectNode object = factory.objectNode();

        if (rating != null) {
            object.put("rating", rating.getRating());
            object.put("comment", rating.getComment());
        } else {
            object.put("rating", 0);
            object.put("comment", "");
        }
        return Blobs.createBlob(object.toString(), "application/json");
    }

}
