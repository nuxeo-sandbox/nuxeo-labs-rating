package org.nuxeo.labs.rating.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.impl.ACLImpl;
import org.nuxeo.ecm.core.api.security.impl.ACPImpl;
import org.nuxeo.labs.rating.adapter.Rating;
import org.nuxeo.labs.rating.utils.Average;

/**
 * Created by Michaël on 1/21/2016.
 */
public class RatingServiceImpl implements RatingService {


    protected static final Log log = LogFactory.getLog(RatingServiceImpl.class);


    @Override
    public void rate(CoreSession session, Rating rating) {

        DocumentModel targetDoc = session.getDocument(new IdRef(rating.getDocId()));

        //get Previous vote
        DocumentModel ratingDoc = getRatingDoc(session,rating.getDocId(),rating.getUsername());
        if (ratingDoc==null) {
            DocumentModel container = getContainer(session);
            ratingDoc = session.createDocumentModel(container.getPathAsString(),"Rating","Rating");
            ratingDoc = session.createDocument(ratingDoc);
        } else {
            //remove old value from average
            removeValueFromAverage(rating.getRating(),targetDoc);
        }
        Rating adapter = ratingDoc.getAdapter(Rating.class);
        adapter.copyValue(rating);

        //add new value to average
        addValueToAverage(rating.getRating(),targetDoc);

        session.saveDocument(targetDoc);
        session.saveDocument(ratingDoc);
        session.save();
    }

    @Override
    public Rating getRating(CoreSession session,String docId, String username) {
        DocumentModel ratingDoc = getRatingDoc(session,docId,username);
        if (ratingDoc!=null) {
            return ratingDoc.getAdapter(Rating.class);
        } else {
            return null;
        }
    }


    protected DocumentModel getContainer(CoreSession session) {
        DocumentModelList list = session.query("Select * From RatingContainer");
        if (list.size()>0) {
            return list.get(0);
        } else {
            DocumentModel container = session.createDocumentModel("/","RatingContainer","RatingContainer");
            container.setPropertyValue("dc:title","RatingContainer");
            container = session.createDocument(container);

            //set ACL
            ACPImpl acp = new ACPImpl();
            ACLImpl acl = new ACLImpl("local");
            acp.addACL(acl);
            ACE ace = new ACE("members","Write",true);
            acl.add(ace);
            session.setACP(container.getRef(), acp,true);
            return container;
        }
    }

    public DocumentModel getRatingDoc(CoreSession session, String docId, String username) {
        String query = String.format(
                "Select * From Rating Where rating:docId = '%s' AND " +
                        "rating:username = '%s'",docId,username);
        DocumentModelList ratings = session.query(query);
        if (ratings.size()>0) {
            return ratings.get(0);
        } else {
            return null;
        }
    }

    protected void addValueToAverage(long value, DocumentModel doc) {
        if (!doc.hasFacet("Rated")) doc.addFacet("Rated");
        double avg = (double) doc.getPropertyValue("rated:avg");
        long count = (long) doc.getPropertyValue("rated:count");
        double newAvg = Average.addValueToAverage(value,avg,count);
        doc.setPropertyValue("rated:avg",newAvg);
        doc.setPropertyValue("rated:count",count+1);
    }

    protected void removeValueFromAverage(long value, DocumentModel doc) {
        if (!doc.hasFacet("Rated")) doc.addFacet("Rated");
        double avg = (double) doc.getPropertyValue("rated:avg");
        long count = (long) doc.getPropertyValue("rated:count");
        double newAvg = Average.removeValueFromAverage(value,avg,count);
        doc.setPropertyValue("rated:avg",newAvg);
        doc.setPropertyValue("rated:count",count-1);
    }
}
