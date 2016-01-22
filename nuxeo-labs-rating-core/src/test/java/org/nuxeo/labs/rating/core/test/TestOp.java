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
import org.nuxeo.labs.rating.adapter.Rating;
import org.nuxeo.labs.rating.adapter.RatingImpl;
import org.nuxeo.labs.rating.operations.GetRating;
import org.nuxeo.labs.rating.operations.Rate;
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
@Features({AutomationFeature.class})
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({"nuxeo-labs-rating-core"})
public class TestOp {

    @Inject
    CoreSession session;

    @Test
    public void testRate() throws Exception {
        DocumentModel doc = session.createDocumentModel("/","File","File");
        doc = session.createDocument(doc);
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(doc);
        ctx.setCoreSession(session);
        OperationChain chain = new OperationChain("TestVote");
        chain.add(Rate.ID).
                set("rating", 3);
        as.run(ctx, chain);

        //check rating
        RatingService service = Framework.getService(RatingService.class);
        Rating rating = service.getRating(session,doc.getId(),session.getPrincipal().getName());

        Assert.assertNotNull(rating);
        Assert.assertEquals(3,rating.getRating());

    }



    @Test
    public void testGetRate() throws Exception {

        DocumentModel doc = session.createDocumentModel("/","File","File");
        doc = session.createDocument(doc);
        Rating rating = new RatingImpl(2,doc.getId(),doc.getTitle(),session.getPrincipal().getName());
        RatingService service = Framework.getService(RatingService.class);
        service.rate(session,rating);

        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext();
        ctx.setInput(doc);
        ctx.setCoreSession(session);
        OperationChain chain = new OperationChain("TestGetVote");
        chain.add(GetRating.ID);
        StringBlob blob = (StringBlob) as.run(ctx, chain);

        Assert.assertNotNull(blob);
        Assert.assertEquals("{\"rating\":2}",blob.getString());
    }
}
