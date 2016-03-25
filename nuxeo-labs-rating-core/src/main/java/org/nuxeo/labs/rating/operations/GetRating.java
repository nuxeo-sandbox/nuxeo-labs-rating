/**
 * 
 */

package org.nuxeo.labs.rating.operations;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.labs.rating.adapter.Rating;
import org.nuxeo.labs.rating.service.RatingService;
import org.nuxeo.runtime.api.Framework;

/**
 * @author Michaël Vachette
 */
@Operation(id= GetRating.ID, category=Constants.CAT_DOCUMENT, label="Rate", description="")
public class GetRating {

	final JsonNodeFactory factory = JsonNodeFactory.instance;

    public static final String ID = "GetRating";

	@Context
	protected CoreSession session;

    @OperationMethod
    public Blob run(DocumentModel doc) {

		String username = session.getPrincipal().getName();
		RatingService service = Framework.getService(RatingService.class);
		Rating rating = service.getRating(session,doc.getId(),username);

		ObjectNode object = factory.objectNode();

		if (rating!=null) {
			object.put("rating",rating.getRating());
			object.put("comment",rating.getComment());
		} else {
			object.put("rating",0);
			object.put("comment","");
		}
		return Blobs.createBlob(object.toString(), "application/json");
    }    

}
