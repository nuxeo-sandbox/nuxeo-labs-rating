package org.nuxeo.labs.rating.service;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.labs.rating.adapter.Rating;

/**
 * Created by Michaël on 1/21/2016.
 */
public interface RatingService {

    public void rate(CoreSession session, Rating rating);

    public Rating getRating(CoreSession session, String docId, String username);

}
