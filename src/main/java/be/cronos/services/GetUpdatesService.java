package be.cronos.services;

import be.cronos.model.GetUpdatesRequest;
import be.cronos.model.GetUpdatesResponse;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class GetUpdatesService {
    private final String CN = GetUpdatesService.class.getName();
    private static final Logger LOG = Logger.getLogger(GetUpdatesService.class.getName());

    public Response getUpdates(GetUpdatesRequest getUpdatesRequest) {
        // At some point, this will look in a graveyard of sort to detect terminated sessions
        LOG.entering(CN, "getUpdates");
        return Response
                .status(200)
                .entity(new GetUpdatesResponse())
                .build();
    }
}
