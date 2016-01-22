package org.nuxeo.labs.rating.adapter;

/**
 * Created by Michaël on 1/21/2016.
 */
public interface Rating {

    public void setRating(long rating);

    public long getRating();

    public String getUsername();

    public void setUsername(String username);

    public String getDocId();

    public void setDocId(String docId);

    public String getDocTitle();

    public void setDocTitle(String docTitle);

    public void copyValue(Rating rating);

}
