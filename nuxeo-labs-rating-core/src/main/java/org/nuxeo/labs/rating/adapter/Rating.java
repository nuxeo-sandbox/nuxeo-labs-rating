package org.nuxeo.labs.rating.adapter;

/**
 * Created by Michaël on 1/21/2016.
 */
public interface Rating {

    void setRating(long rating);

    long getRating();

    String getUsername();

    void setUsername(String username);

    String getDocId();

    void setDocId(String docId);

    String getDocTitle();

    void setDocTitle(String docTitle);

    String getComment();

    void setComment(String comment);

    void copyValue(Rating rating);

}
