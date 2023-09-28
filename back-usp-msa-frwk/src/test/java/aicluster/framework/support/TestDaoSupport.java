package aicluster.framework.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import aicluster.sample.SampleApplication;

@Transactional
@SpringBootTest
@ContextConfiguration(classes = SampleApplication.class)
@ActiveProfiles("junit")
public class TestDaoSupport {

}
