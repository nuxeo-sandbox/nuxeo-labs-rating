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

package org.nuxeo.labs.rating.utils;


import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public abstract class DocumentHelper {

    public static String getHeadDocumentId(DocumentModel doc) {
        CoreSession session = doc.getCoreSession();
        String headDocumentId;
        if (doc.isProxy()) {
            DocumentModel targetDoc = session.getSourceDocument(doc.getRef());
            if (targetDoc.isVersion()) {
                headDocumentId = targetDoc.getSourceId();
            } else {
                headDocumentId = targetDoc.getId();
            }
        } else if (doc.isVersion()) {
            headDocumentId = doc.getSourceId();
        } else {
            headDocumentId = doc.getId();
        }
        return headDocumentId;
    }
}
