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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.labs.rating.adapter.RatedAdapter;
import org.nuxeo.labs.rating.adapter.RatingAdapter;
import org.nuxeo.labs.rating.model.Rating;
import org.nuxeo.labs.rating.model.RatingImpl;
import org.nuxeo.labs.rating.service.RatingService;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import jakarta.inject.Inject;


@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({ "nuxeo-labs-rating-core" })
public class TestService {

    @Inject
    CoreSession session;

    @Inject
    RatingService ratingService;

    @Test
    public void testRate() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);

        Rating rating = new RatingImpl(5, doc.getId(), session.getPrincipal().getName(), "");
        ratingService.rate(session, rating);

        DocumentModelList list = session.query("Select * From Rating where rating:docId = '" + doc.getId() + "'");
        doc = session.getDocument(new IdRef(doc.getId()));

        // check rating
        Assert.assertTrue(list.size() > 0);
        Rating savedRating = list.get(0).getAdapter(RatingAdapter.class);
        Assert.assertEquals(5, savedRating.getRating());

        // check average
        Assert.assertEquals(1, (long) doc.getPropertyValue(RatedAdapter.PROPERTY_COUNT));
        Assert.assertEquals(5, (double) doc.getPropertyValue(RatedAdapter.PROPERTY_AVG), 0.1f);

        // update rating
        rating.setRating(1);
        ratingService.rate(session, rating);
        list = session.query("Select * From Rating where rating:docId = '" + doc.getId() + "'");
        Assert.assertTrue(list.size() > 0);
        savedRating = list.get(0).getAdapter(RatingAdapter.class);
        Assert.assertEquals(1, savedRating.getRating());
    }


    @Test
    public void testRateProxyLive() throws Exception {
        //create doc
        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);

        //create proxy
        DocumentModel proxy = session.createProxy(doc.getRef(),new PathRef("/"));

        //rate proxy
        Rating rating = new RatingImpl(2, proxy.getId(), session.getPrincipal().getName(), "Test Proxy Live");
        ratingService.rate(session,rating);
    }


    @Ignore
    @Test
    public void testRateVersion() throws Exception {
        //create doc
        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);

        //create version
        session.checkIn(doc.getRef(),VersioningOption.MAJOR,"Test Rate Proxy Version");

        //get version
        DocumentModel version = session.getVersions(doc.getRef()).get(0);

        //rate version
        Rating rating = new RatingImpl(2, version.getId(), session.getPrincipal().getName(), "Test Version");
        ratingService.rate(session,rating);
    }



    @Test
    public void testRateProxyVersion() throws Exception {
        //create doc
        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);

        //create version
        session.checkIn(doc.getRef(),VersioningOption.MAJOR,"Test Rate Proxy Version");

        //get version
        DocumentModel version = session.getVersions(doc.getRef()).get(0);

        //create proxy
        DocumentModel proxy = session.createProxy(version.getRef(),new PathRef("/"));

        //rate proxy
        Rating rating = new RatingImpl(2, proxy.getId(),
                session.getPrincipal().getName(), "Test Proxy Version");
        ratingService.rate(session,rating);
    }


    @Test
    public void testGetRate() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);
        Rating rating = new RatingImpl(2, doc.getId(), session.getPrincipal().getName(), "");

        ratingService.rate(session, rating);

        Rating savedRating = ratingService.getRating(session, doc.getId(), session.getPrincipal().getName());

        Assert.assertNotNull(savedRating);
        Assert.assertEquals(rating.getRating(), savedRating.getRating());
        Assert.assertEquals(rating.getComment(), savedRating.getComment());
    }

    @Test
    public void testGetRateFromProxy() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);
        Rating rating = new RatingImpl(2, doc.getId(), session.getPrincipal().getName(), "");

        ratingService.rate(session, rating);

        //create proxy
        DocumentModel proxy = session.createProxy(doc.getRef(),new PathRef("/"));

        Rating savedRating = ratingService.getRating(session, proxy.getId(), session.getPrincipal().getName());

        Assert.assertNotNull(savedRating);
        Assert.assertEquals(rating.getRating(), savedRating.getRating());
        Assert.assertEquals(rating.getComment(), savedRating.getComment());
    }

    @Test
    public void testGetRateFromVersionProxy() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);
        Rating rating = new RatingImpl(2, doc.getId(), session.getPrincipal().getName(), "");

        ratingService.rate(session, rating);

        //create version
        session.checkIn(doc.getRef(),VersioningOption.MAJOR,"Test Rate Proxy Version");
        DocumentModel version = session.getLastDocumentVersion(doc.getRef());

        //create proxy
        DocumentModel proxy = session.createProxy(version.getRef(),new PathRef("/"));

        Rating savedRating = ratingService.getRating(session, proxy.getId(), session.getPrincipal().getName());

        Assert.assertNotNull(savedRating);
        Assert.assertEquals(rating.getRating(), savedRating.getRating());
        Assert.assertEquals(rating.getComment(), savedRating.getComment());
    }
}
