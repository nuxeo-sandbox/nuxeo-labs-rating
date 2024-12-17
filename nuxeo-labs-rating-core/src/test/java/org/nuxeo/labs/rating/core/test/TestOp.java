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
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.labs.rating.model.Rating;
import org.nuxeo.labs.rating.model.RatingImpl;
import org.nuxeo.labs.rating.operations.GetRating;
import org.nuxeo.labs.rating.operations.Rate;
import org.nuxeo.labs.rating.service.RatingService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import jakarta.inject.Inject;

/**
 * Created by Michaël on 28/05/2015.
 */

@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({ "nuxeo-labs-rating-core" })
public class TestOp {

    @Inject
    CoreSession session;

    @Test
    public void testRate() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(doc);
        ctx.setCoreSession(session);
        OperationChain chain = new OperationChain("TestVote");
        chain.add(Rate.ID).set("rating", 3).set("comment", "My Comment");
        as.run(ctx, chain);

        // check rating
        RatingService service = Framework.getService(RatingService.class);
        Rating rating = service.getRating(session, doc.getId(), session.getPrincipal().getName());

        Assert.assertNotNull(rating);
        Assert.assertEquals(3, rating.getRating());
        Assert.assertEquals("My Comment", rating.getComment());

    }

    @Test
    public void testGetRate() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);
        Rating rating = new RatingImpl(2, doc.getId(), session.getPrincipal().getName(), "My comment");
        RatingService service = Framework.getService(RatingService.class);
        service.rate(session, rating);

        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(doc);
        ctx.setCoreSession(session);
        OperationChain chain = new OperationChain("TestGetVote");
        chain.add(GetRating.ID);
        StringBlob blob = (StringBlob) as.run(ctx, chain);

        Assert.assertNotNull(blob);
        Assert.assertEquals("{\"rating\":2,\"comment\":\"My comment\"}", blob.getString());
    }
}
