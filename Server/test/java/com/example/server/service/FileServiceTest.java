import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * @author 言曌
 * @date 2020/3/1 8:24 下午
 */

@RunWith(Arquillian.class)
public class FileServiceTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(com.example.server.service.FileService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
