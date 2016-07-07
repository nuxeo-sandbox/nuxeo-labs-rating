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
package org.nuxeo.labs.rating.adapter;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.labs.rating.model.Rating;

public class RatingAdapter implements Rating {

    public static String PROPERTY_USERNAME = "rating:username";
    public static String PROPERTY_RATING = "rating:rating";
    public static String PROPERTY_COMMENT = "rating:comment";
    public static String PROPERTY_DOC_ID = "rating:docId";
    public static String PROPERTY_VERSION_ID = "rating:versionId";
    public static String PROPERTY_HEAD_ID = "rating:headId";


    private DocumentModel doc;

    public RatingAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public void setRating(long rating) {
        doc.setPropertyValue(PROPERTY_RATING, rating);
    }

    @Override
    public long getRating() {
        return (long) doc.getPropertyValue(PROPERTY_RATING);
    }

    @Override
    public String getUsername() {
        return (String) doc.getPropertyValue(PROPERTY_USERNAME);
    }

    @Override
    public void setUsername(String username) {
        doc.setPropertyValue(PROPERTY_USERNAME, username);
    }

    @Override
    public String getDocId() {
        return (String) doc.getPropertyValue(PROPERTY_DOC_ID);
    }

    @Override
    public void setDocId(String docId) {
        doc.setPropertyValue(PROPERTY_DOC_ID, docId);
        updateDocumentIds();
    }

    @Override
    public String getComment() {
        String comment = (String) doc.getPropertyValue(PROPERTY_COMMENT);
        return comment != null ? comment : "";
    }

    @Override
    public void setComment(String comment) {
        doc.setPropertyValue(PROPERTY_COMMENT, comment);
    }

    @Override
    public void copyValue(Rating rating) {
        setDocId(rating.getDocId());
        setUsername(rating.getUsername());
        setRating(rating.getRating());
        setComment(rating.getComment());
    }

    /**
     *
     * @return the id of the version document for which the rating applies
     */
    public String getVersionId() {
        return (String) doc.getPropertyValue(PROPERTY_VERSION_ID);
    }

    /**
     *
     * @return the id of the live document for which the rating applies
     */
    public String getHeadId() {
        return (String) doc.getPropertyValue(PROPERTY_HEAD_ID);
    }

    /**
     * Stores the ids of the version document and the head document in case the source document
     * is a version or a proxy
     */
    protected void updateDocumentIds() {
        CoreSession session = doc.getCoreSession();
        DocumentModel ratedDocument = session.getDocument(new IdRef(this.getDocId()));
        if (ratedDocument.isVersion()) {
            doc.setPropertyValue(PROPERTY_VERSION_ID,ratedDocument.getId());
            doc.setPropertyValue(PROPERTY_HEAD_ID,ratedDocument.getSourceId());
        } else if (ratedDocument.isProxy()) {
            DocumentModel targetDoc = session.getSourceDocument(ratedDocument.getRef());
            if (targetDoc.isVersion()) {
                doc.setPropertyValue(PROPERTY_VERSION_ID,ratedDocument.getSourceId());
                doc.setPropertyValue(PROPERTY_HEAD_ID,targetDoc.getSourceId());
            } else {
                doc.setPropertyValue(PROPERTY_HEAD_ID,ratedDocument.getSourceId());
            }
        } else {
            doc.setPropertyValue(PROPERTY_HEAD_ID,ratedDocument.getId());
        }
    }
}
