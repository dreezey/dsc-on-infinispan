package be.cronos.services;

import be.cronos.model.PingRequest;
import be.cronos.model.PingResponse;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class PingService {
    private static final Logger LOG = Logger.getLogger(PingService.class.getName());

    public Response pong(PingRequest pingRequest) {
        return Response
                .status(200)
                .entity(new PingResponse())
                .build();
    }

}
