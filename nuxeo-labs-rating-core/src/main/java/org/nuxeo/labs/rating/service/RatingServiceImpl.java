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
package org.nuxeo.labs.rating.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.impl.ACLImpl;
import org.nuxeo.ecm.core.api.security.impl.ACPImpl;
import org.nuxeo.labs.rating.adapter.RatedAdapter;
import org.nuxeo.labs.rating.adapter.RatingAdapter;
import org.nuxeo.labs.rating.model.Rated;
import org.nuxeo.labs.rating.model.Rating;
import org.nuxeo.labs.rating.utils.Average;
import org.nuxeo.labs.rating.utils.DocumentHelper;

public class RatingServiceImpl implements RatingService {

    protected static final Logger log = LogManager.getLogger(RatingServiceImpl.class);

    @Override
    public void rate(CoreSession session, Rating rating) {

        DocumentModel targetDoc = session.getDocument(new IdRef(rating.getDocId()));

        // get Previous vote
        DocumentModel ratingDoc = getRatingDoc(session, rating.getDocId(), rating.getUsername());
        if (ratingDoc == null) {
            DocumentModel container = getContainer(session);
            ratingDoc = session.createDocumentModel(container.getPathAsString(), "Rating", "Rating");
            ratingDoc = session.createDocument(ratingDoc);
        } else {
            // remove old value from average
            removeValueFromAverage(rating.getRating(), targetDoc);
        }
        Rating adapter = ratingDoc.getAdapter(RatingAdapter.class);
        adapter.copyValue(rating);

        // add new value to average
        addValueToAverage(rating.getRating(), targetDoc);

        session.saveDocument(targetDoc);
        session.saveDocument(ratingDoc);
        session.save();
    }

    @Override
    public Rating getRating(CoreSession session, String docId, String username) {
        DocumentModel ratingDoc = getRatingDoc(session, docId, username);
        if (ratingDoc != null) {
            return ratingDoc.getAdapter(RatingAdapter.class);
        } else {
            return null;
        }
    }

    protected DocumentModel getContainer(CoreSession session) {
        DocumentModelList list = session.query("Select * From RatingContainer");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            DocumentModel container = session.createDocumentModel("/", "RatingContainer", "RatingContainer");
            container.setPropertyValue("dc:title", "RatingContainer");
            container = session.createDocument(container);

            // set ACL
            ACPImpl acp = new ACPImpl();
            ACLImpl acl = new ACLImpl("local");
            acp.addACL(acl);
            ACE ace = new ACE("members", "Write", true);
            acl.add(ace);
            session.setACP(container.getRef(), acp, true);
            return container;
        }
    }

    protected DocumentModel getRatingDoc(CoreSession session, String docId, String username) {
        DocumentModel doc = session.getDocument(new IdRef(docId));
        String headDocumentId = DocumentHelper.getHeadDocumentId(doc);

        String query = String.format(
                "Select * From Rating Where (rating:docId = '%s' OR rating:headId = '%s') AND " +
                        "rating:username = '%s'", docId, headDocumentId, username);

        DocumentModelList ratings = session.query(query);
        if (ratings.size() > 0) {
            return ratings.get(0);
        } else {
            return null;
        }
    }

    protected void addValueToAverage(long value, DocumentModel doc) {
        Rated ratedAdapter = doc.getAdapter(RatedAdapter.class);
        double newAvg = Average.addValueToAverage(value, ratedAdapter.getAverage(),ratedAdapter.getCount());
        ratedAdapter.setAverage(newAvg);
        ratedAdapter.setCount(ratedAdapter.getCount()+1);
    }

    protected void removeValueFromAverage(long value, DocumentModel doc) {
        Rated ratedAdapter = doc.getAdapter(RatedAdapter.class);
        double newAvg = Average.removeValueFromAverage(value, ratedAdapter.getAverage(),ratedAdapter.getCount());
        ratedAdapter.setAverage(newAvg);
        ratedAdapter.setCount(ratedAdapter.getCount()-1);
    }
}
