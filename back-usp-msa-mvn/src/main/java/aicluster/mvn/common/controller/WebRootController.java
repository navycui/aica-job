package aicluster.mvn.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aicluster.framework.config.EnvConfig;
import aicluster.mvn.config.SwaggerConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class WebRootController {
    @Autowired
    private EnvConfig env;
    @Autowired
    private SwaggerConfig swagger;

    @RequestMapping("/")
    String get() {
        log.info(String.format("[%s] WebRoot context(/) access.", env.getSystemId()));

        StringBuilder message = new StringBuilder();
        message.append(swagger.apiInfo().getTitle());
        message.append(" ver ");
        message.append(swagger.apiInfo().getVersion());

        return message.toString();
    }
}
