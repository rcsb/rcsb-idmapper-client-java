package org.rcsb.idmapper.client;

import org.junit.jupiter.api.extension.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class IdMapperTestContainer implements BeforeAllCallback, AfterAllCallback, ParameterResolver {
    private final GenericContainer<?> idMapper = new GenericContainer<>(DockerImageName.parse("harbor.devops.k8s.rcsb.org/ingvord/rcsb-idmapper:test-container"))
            //requires docker login
            .withEnv("MONGODB_URI", "mongodb://updater:w31teQuerie5@132.249.210.169:27017/dw?authSource=admin&connectTimeoutMS=3000000&socketTimeoutMS=3000000")
            //actual host port may be different, use container.getPortMapping(port)
            .withExposedPorts(7000,8080);


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
