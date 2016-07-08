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
import org.nuxeo.labs.rating.adapter.RatedAdapter;
import org.nuxeo.labs.rating.service.RatingService;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;


@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({ "nuxeo-labs-rating-core" })
public class TestRatedAdapter {

    @Inject
    CoreSession session;

    @Inject
    RatingService ratingService;

    @Test
    public void testGetterSetter() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);

        RatedAdapter adapter = doc.getAdapter(RatedAdapter.class);
        adapter.setAverage(3);
        Assert.assertEquals("average is set",3.0,adapter.getAverage(),0.01);

        adapter.setCount(1000);
        Assert.assertEquals("count is set",1000,adapter.getCount());
    }

    @Test
    public void testProxyToVersion() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);

        //create version
        session.checkIn(doc.getRef(),VersioningOption.MAJOR,"Test Rate Proxy Version");
        DocumentModel version = session.getLastDocumentVersion(doc.getRef());

        //create proxy
        DocumentModel proxy = session.createProxy(version.getRef(),new PathRef("/"));

        RatedAdapter adapter = proxy.getAdapter(RatedAdapter.class);
        adapter.setAverage(3);
        Assert.assertEquals("average is set on proxy ",3.0f,adapter.getAverage(),0.01);
        adapter.setCount(1000);
        Assert.assertEquals("count is set on proxy",1000,adapter.getCount());

        doc = session.getDocument(doc.getRef());
        RatedAdapter liveDocAdapter = doc.getAdapter(RatedAdapter.class);
        Assert.assertEquals("average is set on live doc",3.0,liveDocAdapter.getAverage(),0.01);
        Assert.assertEquals("count is set on live doc",1000,liveDocAdapter.getCount());

    }


}
