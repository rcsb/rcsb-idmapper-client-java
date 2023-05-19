package org.rcsb.idmapper.client;

import org.rcsb.idmapper.input.AllInput;
import org.rcsb.idmapper.input.GroupInput;
import org.rcsb.idmapper.input.TranslateInput;
import org.rcsb.idmapper.output.AllOutput;
import org.rcsb.idmapper.output.TranslateOutput;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;

public class IdMapperHttpClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();


    public Mono<TranslateOutput> doTranslate(@Nonnull TranslateInput input){
        try {
            var transport = new TranslateHttpTransport(new JsonMapper().create(),httpClient, "http://localhost:8080/translate");
            return Mono.just(transport.dispatch(input));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    public Mono<TranslateOutput> doGroup(@Nonnull GroupInput input){
        return null;
    }

    public Mono<AllOutput> doAll(@Nonnull AllInput input){
        return null;
    }
}
