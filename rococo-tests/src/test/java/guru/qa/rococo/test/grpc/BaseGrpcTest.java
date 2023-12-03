package guru.qa.rococo.test.grpc;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.jupiter.annotation.GrpcTest;

@GrpcTest
public class BaseGrpcTest {

    protected static final Config CFG = Config.getConfig();
    protected static final String PHOTO_PATH = "files/testimg.jpg";
    protected static final String NEW_PHOTO_PATH = "files/newphoto.jpg";
    protected static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
}
