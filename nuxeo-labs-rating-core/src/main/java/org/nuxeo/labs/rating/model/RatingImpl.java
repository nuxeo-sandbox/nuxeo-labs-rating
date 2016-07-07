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
package org.nuxeo.labs.rating.model;

/**
 * Created by Michaël on 1/21/2016.
 */
public class RatingImpl implements Rating {

    protected long rating;

    protected String docId;

    protected String username;

    protected String comment;

    public RatingImpl(long rating, String docId, String username, String comment) {
        this.rating = rating;
        this.docId = docId;
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
        this.username = rating.getUsername();
        this.comment = rating.getComment();
    }
}
