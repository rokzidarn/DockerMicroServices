package si.fri.rso.samples.images.api.v1.resources;

import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.samples.images.api.v1.dtos.HealthDto;
import si.fri.rso.samples.images.api.v1.dtos.LoadDto;
import si.fri.rso.samples.images.services.configuration.AppProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;
import si.fri.rso.samples.images.api.v1.resources.Demo;

import java.util.Arrays;
import java.util.List;

@Log
@ApplicationScoped
@Path("/demo")
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {

    private Logger log = Logger.getLogger(DemoResource.class.getName());

    @Inject
    private AppProperties appProperties;
    @GET
    @Path("instanceid")
    public Response getInstanceId() {
        String instanceId =
                "{\"instanceId\" : \"" + EeRuntime.getInstance().getInstanceId() + "\"}";
        return Response.ok(instanceId).build();
    }

    @POST
    @Path("healthy")
    public Response setHealth(HealthDto health) {
        appProperties.setHealthy(health.getHealthy());
        log.info("Setting health to " + health.getHealthy());
        return Response.ok().build();
    }

    @POST
    @Path("load")
    public Response loadImage(LoadDto loadDto) {
        for (int i = 1; i <= loadDto.getN(); i++) {
            fibonacci(i);
        }
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/test")
    public Response setHealthDown(DemoHealth dh) {
        appProperties.setHealthy(dh.getHealthy());
        log.info("Setting health to: " + dh.getHealthy());
        return Response.ok().build();
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/info")
    public Response showInfo() {

        List<String> members = Arrays.asList("rz3817", "mr3523");
        String describe = "Projekt implementira aplikacijo za deljenje slik";
        List<String> microservices =  Arrays.asList("http://159.122.186.58:30911/v1/images", "http://159.122.186.58:31010/v1/comments");
        List<String> github =  Arrays.asList("https://github.com/MRRSO/rso-images", "https://github.com/MRRSO/rso-comment");
        List<String> travis =  Arrays.asList("https://travis-ci.org/MRRSO/rso-images", "https://travis-ci.org/MRRSO/rso-comment");
        List<String> dockerhub =  Arrays.asList("https://hub.docker.com/r/rokzidarn/rso-images/", "https://hub.docker.com/r/rokzidarn/rso-comment/");

        Demo data = new Demo();
        data.setClani(members);
        data.setOpis_projekta(describe);
        data.setMikrostoritve(microservices);
        data.setGithub(github);
        data.setTravis(travis);
        data.setDockerhub(dockerhub);

        return Response.status(Response.Status.OK).entity(data).build();
    }
    private long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
