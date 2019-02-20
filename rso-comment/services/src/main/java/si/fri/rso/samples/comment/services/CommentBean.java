package si.fri.rso.samples.comment.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.samples.comment.entities.Comment;
import si.fri.rso.samples.comment.services.configuration.AppProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CommentBean {

    private Logger log = Logger.getLogger(CommentBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private AppProperties appProperties;

    public List<Comment> getComments(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Comment.class, queryParameters);

    }

    public Comment getComment(Integer commentId) {
        Comment comment = em.find(Comment.class, commentId);

        if (comment == null) {
            throw new NotFoundException();
        }
        return comment;
    }

    public Comment createComment(Comment comment) {

        try {
            beginTx();
            em.persist(comment);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return comment;
    }

    public boolean deleteComment(Integer commentId) {

        Comment comment = em.find(Comment.class, commentId);

        if (comment != null) {
            try {
                beginTx();
                em.remove(comment);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
