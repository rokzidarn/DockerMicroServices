package si.fri.rso.samples.images.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.samples.images.entities.Image;
import si.fri.rso.samples.images.services.ImagesBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Log
@ApplicationScoped
@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImagesResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private ImagesBean imagesBean;

    @GET
    public Response getImages() {

        List<Image> images = imagesBean.getImages(uriInfo);
        return Response.ok(images).build();
    }

    @GET
    @Path("/{imageId}")
    public Response getImage(@PathParam("imageId") Integer imageId) {

        Image image = imagesBean.getImage(imageId);

        if (image == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(image).build();
    }

    @GET
    @Path("/{imageId}/search")
    public Response getImageKeyword(@PathParam("imageId") Integer imageId, @QueryParam("keyword") String keyword) {

        Image image = imagesBean.getImageKeyword(imageId, keyword);

        if (image == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(image).build();
    }

    @POST
    public Response createImage(Image image) {

        if (image.getCaption().isEmpty() || image.getURL().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            image = imagesBean.createImage(image);
        }

        if (image.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(image).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(image).build();
        }
    }

    @DELETE
    @Path("{imageId}")
    public Response deleteImage(@PathParam("imageId") Integer imageId) {

        boolean deleted = imagesBean.deleteImage(imageId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
