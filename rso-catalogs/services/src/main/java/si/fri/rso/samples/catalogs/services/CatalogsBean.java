package si.fri.rso.samples.catalogs.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.samples.catalogs.dtos.Image;
import si.fri.rso.samples.catalogs.entities.Catalog;
import si.fri.rso.samples.catalogs.services.configuration.AppProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RequestScoped
public class CatalogsBean {

    private Logger log = Logger.getLogger(CatalogsBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private AppProperties appProperties;

    @Inject
    private CatalogsBean catalogsBean;

    private Client httpClient;

    @Inject
    @DiscoverService("rso-images")
    private Optional<String> baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<Catalog> getCatalogs(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Catalog.class, queryParameters);

    }

    public Catalog getCatalog(Integer catalogId) {
        Catalog catalog = em.find(Catalog.class, catalogId);

        if (catalog == null) {
            throw new NotFoundException();
        }

        List<Image> images = catalogsBean.getImages(catalogId);
        catalog.setImages(images);

        return catalog;
    }

    // outer service
    public List<Image> getImages(Integer catalogId) {
        if (appProperties.isExternalServicesEnabled() && baseUrl.isPresent()) {
            try {
                return httpClient
                        .target(baseUrl.get() + "/v1/images?where=catalogId:EQ:" + catalogId)
                        .request().get(new GenericType<List<Image>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }

        return null;

    }

    public Catalog createCatalog(Catalog catalog) {

        try {
            beginTx();
            em.persist(catalog);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return catalog;
    }

    public boolean deleteCatalog(Integer catalogId) {

        Catalog catalog = em.find(Catalog.class, catalogId);

        if (catalog != null) {
            try {
                beginTx();
                em.remove(catalog);
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
