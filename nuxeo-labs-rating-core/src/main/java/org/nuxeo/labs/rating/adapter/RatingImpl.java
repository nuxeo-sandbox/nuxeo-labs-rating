package org.nuxeo.labs.rating.adapter;

/**
 * Created by Michaël on 1/21/2016.
 */
public class RatingImpl implements Rating {

    protected long rating;
    protected String docId;
    protected String docTitle;
    protected String username;
    protected String comment;


    public RatingImpl(long rating, String docId, String docTitle, String username, String comment) {
        this.rating = rating;
        this.docId = docId;
        this.docTitle = docTitle;
        this.username = username;
        this.comment = comment;
    }

    @Override
    public void setRating(long rating) {
        this.rating = rating;
    }

    @Override
    public long getRating() {
        return rating;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getDocId() {
        return docId;
    }

    @Override
    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public String getDocTitle() {
        return docTitle;
    }

    @Override
    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void copyValue(Rating rating) {
        this.rating = rating.getRating();
        this.docId = rating.getDocId();
        this.docTitle = rating.getDocTitle();
        this.username = rating.getUsername();
        this.comment = rating.getComment();
    }
}
