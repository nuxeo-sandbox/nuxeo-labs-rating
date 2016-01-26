package org.nuxeo.labs.rating.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Created by Michaël on 1/21/2016.
 */
public class RatingAdapter implements Rating {

    private DocumentModel doc;

    public RatingAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public void setRating(long rating) {
        doc.setPropertyValue("rating:rating",rating);
    }

    @Override
    public long getRating() {
        return (long) doc.getPropertyValue("rating:rating");
    }

    @Override
    public String getUsername() {
        return (String) doc.getPropertyValue("rating:docId");
    }

    @Override
    public void setUsername(String username) {
        doc.setPropertyValue("rating:username",username);
    }

    @Override
    public String getDocId() {
        return doc.getId();
    }

    @Override
    public void setDocId(String docId) {
        doc.setPropertyValue("rating:docId",docId);
    }

    @Override
    public String getDocTitle() {
        return doc.getTitle();
    }

    @Override
    public void setDocTitle(String docTitle) {
        doc.setPropertyValue("rating:docTitle",docTitle);
    }

    @Override
    public String getComment() {
        String comment =  (String) doc.getPropertyValue("rating:comment");
        return comment != null ? comment : "";
    }

    @Override
    public void setComment(String comment) {
        doc.setPropertyValue("rating:comment",comment);
    }

    @Override
    public void copyValue(Rating rating) {
        setDocId(rating.getDocId());
        setDocTitle(rating.getDocTitle());
        setUsername(rating.getUsername());
        setRating(rating.getRating());
        setComment(rating.getComment());
    }
}
