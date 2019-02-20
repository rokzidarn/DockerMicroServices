package si.fri.rso.samples.images.api.v1.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import si.fri.rso.samples.images.entities.Image;
import si.fri.rso.samples.images.services.ImagesBean;

import javax.inject.Inject;

@GraphQLClass
public class ImageMutations {

    @Inject
    private ImagesBean imagesBean;

    @GraphQLMutation
    public Image addImage(@GraphQLArgument(name = "image") Image image) {
        imagesBean.createImage(image);

        return image;
    }

    @GraphQLMutation
    public DeleteResponse deleteImage(@GraphQLArgument(name = "id") Integer id) {
        return new DeleteResponse(imagesBean.deleteImage(id));
    }

}