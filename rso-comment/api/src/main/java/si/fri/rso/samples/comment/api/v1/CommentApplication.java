package si.fri.rso.samples.comment.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import com.kumuluz.ee.discovery.annotations.RegisterService;

@RegisterService
@ApplicationPath("/v1")
public class CommentApplication extends Application {
}
