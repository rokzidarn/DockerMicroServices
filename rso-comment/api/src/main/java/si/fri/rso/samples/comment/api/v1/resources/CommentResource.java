package si.fri.rso.samples.comment.api.v1.resources;

import si.fri.rso.samples.comment.entities.Comment;
import si.fri.rso.samples.comment.services.CommentBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private CommentBean commentBean;

    @GET
    public Response getComments() {

        List<Comment> comment = commentBean.getComments(uriInfo);
        return Response.ok(comment).build();
    }

    @GET
    @Path("/{commentId}")
    public Response getComment(@PathParam("commentId") Integer commentId) {

        Comment comment = commentBean.getComment(commentId);

        if (comment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(comment).build();
    }

    @POST
    public Response createComment(Comment comment) {

        if (comment.getText().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            comment = commentBean.createComment(comment);
        }

        if (comment.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(comment).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(comment).build();
        }
    }

    @DELETE
    @Path("{commentId}")
    public Response deleteComment(@PathParam("commentId") Integer commentId) {

        boolean deleted = commentBean.deleteComment(commentId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
