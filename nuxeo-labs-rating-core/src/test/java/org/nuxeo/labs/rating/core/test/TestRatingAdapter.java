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
package org.nuxeo.labs.rating.core.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.labs.rating.adapter.RatingAdapter;
import org.nuxeo.labs.rating.model.Rating;
import org.nuxeo.labs.rating.model.RatingImpl;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;


@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({ "nuxeo-labs-rating-core" })
public class TestRatingAdapter {

    @Inject
    CoreSession session;

    @Test
    public void testGetterSetter() throws Exception {

        DocumentModel ratedDoc = session.createDocumentModel("/", "File", "File");
        ratedDoc = session.createDocument(ratedDoc);

        DocumentModel ratingDoc = session.createDocumentModel("/", "Rating", "Rating");
        ratingDoc = session.createDocument(ratingDoc);

        RatingAdapter adapter = ratingDoc.getAdapter(RatingAdapter.class);

        //test Id
        adapter.setDocId(ratedDoc.getId());
        Assert.assertEquals("doc Id is set",ratedDoc.getId(),adapter.getDocId());
        Assert.assertEquals("head Id is set",ratedDoc.getId(),adapter.getHeadId());

        //test username
        adapter.setUsername("mika");
        Assert.assertEquals("username is set","mika",adapter.getUsername());

        //test rating
        adapter.setRating(3);
        Assert.assertEquals("rating is set",3,adapter.getRating());

        //test comment
        adapter.setComment("this is a comment");
        Assert.assertEquals("comment is set","this is a comment",adapter.getComment());
    }

    @Test
    public void testCopy() throws Exception {

        DocumentModel ratedDoc = session.createDocumentModel("/", "File", "File");
        ratedDoc = session.createDocument(ratedDoc);

        DocumentModel ratingDoc = session.createDocumentModel("/", "Rating", "Rating");
        ratingDoc = session.createDocument(ratingDoc);

        RatingAdapter adapter = ratingDoc.getAdapter(RatingAdapter.class);

        Rating rating = new RatingImpl(2,ratedDoc.getId(),"mika","test copy");

        adapter.copyValue(rating);

        //test Id
        Assert.assertEquals("doc Id is set",ratedDoc.getId(),adapter.getDocId());

        //test username
        Assert.assertEquals("username is set","mika",adapter.getUsername());

        //test rating
        Assert.assertEquals("rating is set",2,adapter.getRating());

        //test comment
        Assert.assertEquals("comment is set","test copy",adapter.getComment());
    }


    @Test
    public void testWithProxyLive() throws Exception {
        //create doc
        DocumentModel ratedDoc = session.createDocumentModel("/", "File", "File");
        ratedDoc = session.createDocument(ratedDoc);

        //create proxy
        DocumentModel proxy = session.createProxy(ratedDoc.getRef(),new PathRef("/"));

        DocumentModel ratingDoc = session.createDocumentModel("/", "Rating", "Rating");
        ratingDoc = session.createDocument(ratingDoc);

        RatingAdapter adapter = ratingDoc.getAdapter(RatingAdapter.class);

        //test Id
        adapter.setDocId(proxy.getId());
        Assert.assertEquals("doc Id is set",proxy.getId(),adapter.getDocId());
        Assert.assertNull("version Id is not set",adapter.getVersionId());
        Assert.assertEquals("head Id is set",ratedDoc.getId(),adapter.getHeadId());
    }


    @Test
    public void testVersion() throws Exception {
        //create doc
        DocumentModel ratedDoc = session.createDocumentModel("/", "File", "File");
        ratedDoc = session.createDocument(ratedDoc);

        //create version
        session.checkIn(ratedDoc.getRef(),VersioningOption.MAJOR,"Test Rate Proxy Version");
        DocumentModel version = session.getLastDocumentVersion(ratedDoc.getRef());
        Assert.assertTrue(version.isVersion());

        DocumentModel ratingDoc = session.createDocumentModel("/", "Rating", "Rating");
        ratingDoc = session.createDocument(ratingDoc);

        RatingAdapter adapter = ratingDoc.getAdapter(RatingAdapter.class);

        //test Id
        adapter.setDocId(version.getId());
        Assert.assertEquals("doc Id is set",version.getId(),adapter.getDocId());
        Assert.assertEquals("version Id is set",version.getId(),adapter.getVersionId());
        Assert.assertEquals("head Id is set",ratedDoc.getId(),adapter.getHeadId());
    }


    @Test
    public void testProxyVersion() throws Exception {
        //create doc
        DocumentModel ratedDoc = session.createDocumentModel("/", "File", "File");
        ratedDoc = session.createDocument(ratedDoc);

        //create version
        session.checkIn(ratedDoc.getRef(),VersioningOption.MAJOR,"Test Rate Proxy Version");
        DocumentModel version = session.getLastDocumentVersion(ratedDoc.getRef());

        //create proxy
        DocumentModel proxy = session.createProxy(version.getRef(),new PathRef("/"));

        DocumentModel ratingDoc = session.createDocumentModel("/", "Rating", "Rating");
        ratingDoc = session.createDocument(ratingDoc);

        RatingAdapter adapter = ratingDoc.getAdapter(RatingAdapter.class);

        //test Id
        adapter.setDocId(proxy.getId());
        Assert.assertEquals("doc Id is set",proxy.getId(),adapter.getDocId());
        Assert.assertEquals("version Id is set",version.getId(),adapter.getVersionId());
        Assert.assertEquals("head Id is set",ratedDoc.getId(),adapter.getHeadId());
    }

}
