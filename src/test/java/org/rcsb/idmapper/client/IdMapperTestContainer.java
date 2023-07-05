package org.rcsb.idmapper.client;

import org.junit.jupiter.api.extension.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class IdMapperTestContainer implements BeforeAllCallback, AfterAllCallback, ParameterResolver {
    private final GenericContainer<?> idMapper = new GenericContainer<>(
            DockerImageName.parse("harbor.devops.k8s.rcsb.org/rcsb/rcsb-idmapper:test-container"))
            //requires docker login
            .withEnv("MONGODB_URI", "mongodb://user:pwd@host:port")
            //actual host port may be different, use container.getPortMapping(port)
            .withExposedPorts(9000,8080);


    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        idMapper.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        idMapper.stop();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType()
                .equals(IdMapperTestContainer.class);
    }

    @Override
    public IdMapperTestContainer resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return this;
    }

    public GenericContainer<?> getIdMapper(){
        return idMapper;
    }
}
