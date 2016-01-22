package org.nuxeo.labs.rating.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

/**
 * Created by Michaël on 1/21/2016.
 */
public class RatingAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if (doc.getType().equals("Rating")) {
            return new RatingAdapter(doc);
        } else {
            throw new NuxeoException();
        }
    }

}
