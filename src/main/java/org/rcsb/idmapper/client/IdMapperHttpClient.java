package org.rcsb.idmapper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rcsb.idmapper.input.AllInput;
import org.rcsb.idmapper.input.GroupInput;
import org.rcsb.idmapper.input.TranslateInput;
import org.rcsb.idmapper.output.AllOutput;
import org.rcsb.idmapper.output.TranslateOutput;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

public class IdMapperHttpClient implements IdMapperClient {

    private final HttpClient httpClient;
    private final URI uri;
    private final ObjectMapper jsonMapper;

    public IdMapperHttpClient(HttpClient httpClient, URI uri, ObjectMapper jsonMapper) {
        this.httpClient = httpClient;
        this.uri = uri;
        this.jsonMapper = jsonMapper;
    }

    public IdMapperHttpClient(HttpClient httpClient, URI uri) {
        this(httpClient, uri, new JsonMapper().create());
    }

    @Override
    public Mono<TranslateOutput> doTranslate(@Nonnull TranslateInput input){
        try {
            var transport = new TranslateHttpTransport(jsonMapper, httpClient, uri.resolve("/translate"));
            return Mono.just(transport.dispatch(input));//TODO we can use defer to postpone actual request to the subscribe moment
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<TranslateOutput> doGroup(@Nonnull GroupInput input){
        try {
            var transport = new TranslateHttpTransport(jsonMapper,httpClient, uri.resolve("/group"));
            return Mono.just(transport.dispatch(input));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<AllOutput> doAll(@Nonnull AllInput input){
        try {
            var transport = new AllHttpTransport(jsonMapper,httpClient, uri.resolve("/all"));
            return Mono.just(transport.dispatch(input));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }
}
