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


public interface Rating {

    /**
     *
     * @param rating the user rating for the document
     */
    void setRating(long rating);

    /**
     *
     * @return The document rating
     */
    long getRating();

    /**
     *
     * @return The user who did the rating
     */
    String getUsername();

    /**
     *
     * @param username The user who did the rating
     */
    void setUsername(String username);

    /**
     *
     * @return The id of the document for which the rating applies
     */
    String getDocId();

    /**
     *
     * @param docId The id of the document for which the rating applies
     */
    void setDocId(String docId);

    /**
     *
     * @return The user comment associated with the rating
     */
    String getComment();

    /**
     *
     * @param comment The user comment associated with the rating
     */
    void setComment(String comment);

    /**
     * Copy the parameter object values in the current one
     * @param rating the input rating object
     */
    void copyValue(Rating rating);

}
