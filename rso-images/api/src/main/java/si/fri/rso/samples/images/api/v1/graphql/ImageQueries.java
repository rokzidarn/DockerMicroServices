package si.fri.rso.samples.images.api.v1.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import si.fri.rso.samples.images.entities.Image;
import si.fri.rso.samples.images.services.ImagesBean;

import javax.inject.Inject;

@GraphQLClass
public class ImageQueries {

    @Inject
    private ImagesBean imagesBean;

    @GraphQLQuery
    public PaginationWrapper<Image> allImages(@GraphQLArgument(name = "pagination") Pagination pagination,
                                              @GraphQLArgument(name = "sort") Sort sort,
                                              @GraphQLArgument(name = "filter") Filter filter) {
        return GraphQLUtils.process(imagesBean.getImagesGQL(), pagination, sort, filter);
    }

    @GraphQLQuery
    public Image getImage(@GraphQLArgument(name = "id") Integer id) {
        return imagesBean.getImage(id);
    }

}