package si.fri.rso.samples.catalogs.api.v1.resources;

import si.fri.rso.samples.catalogs.entities.Catalog;
import si.fri.rso.samples.catalogs.services.CatalogsBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/catalogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatalogsResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private CatalogsBean catalogsBean;

    @GET
    public Response getCatalogs() {

        List<Catalog> catalog = catalogsBean.getCatalogs(uriInfo);
        return Response.ok(catalog).build();
    }

    @GET
    @Path("/{catalogId}")
    public Response getCatalog(@PathParam("catalogId") Integer catalogId) {

        Catalog catalog = catalogsBean.getCatalog(catalogId);

        if (catalog == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(catalog).build();
    }

    @POST
    public Response createCatalog(Catalog catalog) {

        if (catalog.getCname().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            catalog = catalogsBean.createCatalog(catalog);
        }

        if (catalog.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(catalog).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(catalog).build();
        }
    }

    @DELETE
    @Path("{catalogId}")
    public Response deleteCatalog(@PathParam("catalogId") Integer catalogId) {

        boolean deleted = catalogsBean.deleteCatalog(catalogId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
