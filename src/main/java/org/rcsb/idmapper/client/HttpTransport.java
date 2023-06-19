package org.rcsb.idmapper.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.output.Output;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public abstract class HttpTransport<T extends Output<?>> implements Transport<T> {

    protected final ObjectMapper jsonMapper;
    protected final java.net.http.HttpClient httpClient;
    protected final URI uri;

    public HttpTransport(ObjectMapper jsonMapper, HttpClient httpClient, URI uri) {
        this.jsonMapper = jsonMapper;
        this.httpClient = httpClient;
        this.uri = uri;
    }

    @Override
    public T dispatch(Input input) throws IOException {
        var request = HttpRequest.newBuilder(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonMapper.writeValueAsString(input)))//TODO this is possibly super un-optimal
                .setHeader("Content-Type", "application/json")
                .setHeader("User-Agent", "IdMapper Java Client")//TODO version
                .build();

        return handleResponse(request);
    }

    protected abstract T handleResponse(HttpRequest request) throws IOException;
}
