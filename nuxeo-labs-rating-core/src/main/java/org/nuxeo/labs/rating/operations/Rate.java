/**
 * 
 */

package org.nuxeo.labs.rating.operations;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.labs.rating.adapter.Rating;
import org.nuxeo.labs.rating.adapter.RatingImpl;
import org.nuxeo.labs.rating.service.RatingService;
import org.nuxeo.runtime.api.Framework;

/**
 * @author Michaël Vachette
 */
@Operation(id= Rate.ID, category=Constants.CAT_DOCUMENT, label="Rate", description="")
public class Rate {

    public static final String ID = "Rate";

	@Context
	protected CoreSession session;

	@Param(name = "rating")
	protected int rating;

	@Param(name = "comment")
	protected String comment;

    @OperationMethod
    public DocumentModel run(DocumentModel doc) {
		String username = session.getPrincipal().getName();
		RatingService service = Framework.getService(RatingService.class);
		Rating ratingObj = new RatingImpl(rating,doc.getId(),doc.getTitle(),username,comment);
		CoreSession sysSession = CoreInstance.openCoreSessionSystem(session.getRepositoryName());
		service.rate(sysSession,ratingObj);
		sysSession.save();
		sysSession.close();
		return doc;
    }    

}
