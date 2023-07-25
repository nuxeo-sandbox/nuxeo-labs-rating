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
import org.nuxeo.labs.rating.model.Rated;

import java.io.Serializable;

public class RatedAdapter implements Rated {

    public static String RATED_FACET = "Rated";
    public static String PROPERTY_AVG = "rated:avg";
    public static String PROPERTY_COUNT = "rated:count";

    protected DocumentModel doc;

    public RatedAdapter(DocumentModel doc) {
        this.doc = doc;
        if (!doc.hasFacet(RATED_FACET)) doc.addFacet(RATED_FACET);
    }

    @Override
    public double getAverage() {
        Double average = (Double) this.getPropertyValue(PROPERTY_AVG);
        return average!=null ? average : 0.0;
    }

    @Override
    public void setAverage(double average) {
       this.setPropertyValue(PROPERTY_AVG, average);
    }

    @Override
    public long getCount() {
        Long count = (Long) this.getPropertyValue(PROPERTY_COUNT);
        return count!=null ? count : 0;
    }

    @Override
    public void setCount(long count) {
        this.setPropertyValue(PROPERTY_COUNT,count);
    }

    @Override
    public void copyValue(Rated rated) {
        setAverage(rated.getAverage());
        setCount(rated.getCount());
    }

    protected void setPropertyValue(String propertyName, Serializable value) {
        CoreSession session = doc.getCoreSession();

        doc.putContextData(org.nuxeo.ecm.platform.dublincore.listener.DublinCoreListener.DISABLE_DUBLINCORE_LISTENER, Boolean.TRUE);
        doc.setPropertyValue(propertyName,value);
        doc = session.saveDocument(doc);

        //Set value on original document if current doc is a published one
        if (doc.isProxy()) {
            DocumentModel targetDoc = session.getSourceDocument(doc.getRef());
            if (targetDoc.isVersion()) {
                DocumentModel liveDocument =
                        session.getSourceDocument(new IdRef(targetDoc.getSourceId()));
                RatedAdapter liveAdapter = liveDocument.getAdapter(RatedAdapter.class);
                liveAdapter.setPropertyValue(propertyName,value);
            }
        }
    }

    protected Serializable getPropertyValue(String propertyName) {
        CoreSession session = doc.getCoreSession();

        //Set value on original document if current doc is a published one
        if (doc.isProxy()) {
            DocumentModel targetDoc = session.getSourceDocument(doc.getRef());
            if (targetDoc.isVersion()) {
                DocumentModel liveDocument =
                        session.getSourceDocument(new IdRef(targetDoc.getSourceId()));
                RatedAdapter liveAdapter = liveDocument.getAdapter(RatedAdapter.class);
                return liveAdapter.getPropertyValue(propertyName);
            } else {
                return null;
            }
        } else {
            return doc.getPropertyValue(propertyName);
        }
    }

}
