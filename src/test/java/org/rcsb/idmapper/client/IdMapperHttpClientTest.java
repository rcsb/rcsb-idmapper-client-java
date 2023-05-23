package org.rcsb.idmapper.client;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.rcsb.common.constants.ContentType;
import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.input.TranslateInput;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

class IdMapperHttpClientTest {

    @Test
    void doTranslate() throws IOException {
        var input = new TranslateInput();
        input.ids = List.of("BHH4");
        input.from = Input.Type.entry;
        input.to =  Input.Type.polymer_entity;
        input.content_type = List.of(ContentType.experimental);

        var client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        var result = new IdMapperHttpClient(client, URI.create("http://localhost:8080"), new JsonMapper().create()).doTranslate(
                input
        );
        var actual = result.block();
        System.out.println(MoreObjects.toStringHelper(actual)
                .add("result", actual.results)
                .toString());
    }

    @Test
    void doGroup() {
    }

    @Test
    void doAll() {
    }
}