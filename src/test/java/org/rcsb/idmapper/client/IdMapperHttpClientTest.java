package org.rcsb.idmapper.client;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.rcsb.common.constants.ContentType;
import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.input.TranslateInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

class IdMapperHttpClientTest {

    @Test
    void doTranslate() throws IOException {
        var input = new TranslateInput();
        input.ids = List.of("BHH4");
        input.from = Input.Type.entry;
        input.to =  Input.Type.polymer_entity;
        input.content_type = List.of(ContentType.experimental);

        var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        var client = new IdMapperHttpClient(httpClient, URI.create("http://localhost:8080"), new JsonMapper().create());


        Flux.range(1,1000)
                .elapsed()
                .subscribe(tuple -> {
                            System.out.println("Elapsed: " + tuple.getT1());
                    var result = client.doTranslate(
                            input
                    );
                    var actual = result.block();
                    System.out.println(MoreObjects.toStringHelper(actual)
                            .add("result", actual.results)
                            .toString());
                }

                );
    }

    @Test
    void doGroup() {
    }

    @Test
    void doAll() {
    }
}