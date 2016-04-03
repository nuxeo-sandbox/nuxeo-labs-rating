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
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.labs.rating.adapter.Rating;
import org.nuxeo.labs.rating.adapter.RatingImpl;
import org.nuxeo.labs.rating.service.RatingService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

/**
 * Created by Michaël on 28/05/2015.
 */

@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({ "nuxeo-labs-rating-core" })
public class TestService {

    @Inject
    CoreSession session;

    @Test
    public void testRate() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);

        Rating rating = new RatingImpl(5, doc.getId(), doc.getTitle(), session.getPrincipal().getName(), "");
        RatingService service = Framework.getService(RatingService.class);
        service.rate(session, rating);

        DocumentModelList list = session.query("Select * From Rating where rating:docId = '" + doc.getId() + "'");
        doc = session.getDocument(new IdRef(doc.getId()));

        // check rating
        Assert.assertTrue(list.size() > 0);
        Rating savedRating = list.get(0).getAdapter(Rating.class);
        Assert.assertEquals(5, savedRating.getRating());

        // check average
        Assert.assertEquals(1, (long) doc.getPropertyValue("rated:count"));
        Assert.assertEquals(5, (double) doc.getPropertyValue("rated:avg"), 0.1f);

        // update rating
        rating.setRating(1);
        service.rate(session, rating);
        list = session.query("Select * From Rating where rating:docId = '" + doc.getId() + "'");
        Assert.assertTrue(list.size() > 0);
        savedRating = list.get(0).getAdapter(Rating.class);
        Assert.assertEquals(1, savedRating.getRating());

    }

    @Test
    public void testGetRate() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);
        Rating rating = new RatingImpl(2, doc.getId(), doc.getTitle(), session.getPrincipal().getName(), "");
        RatingService service = Framework.getService(RatingService.class);
        service.rate(session, rating);

        Rating savedRating = service.getRating(session, doc.getId(), session.getPrincipal().getName());

        Assert.assertNotNull(savedRating);
        Assert.assertEquals(rating.getRating(), savedRating.getRating());
        Assert.assertEquals(rating.getComment(), savedRating.getComment());
    }
}
