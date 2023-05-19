package org.rcsb.idmapper.client;

import com.google.gson.Gson;
import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.output.Output;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public abstract class HttpTransport<T extends Output<?>> implements Transport<T> {

    protected final Gson jsonMapper;
    protected final java.net.http.HttpClient httpClient;
    protected final URI uri;

    public HttpTransport(Gson jsonMapper, HttpClient httpClient, String uri) {
        this.jsonMapper = jsonMapper;
        this.httpClient = httpClient;
        this.uri = URI.create(uri);
    }

    @Override
    public T dispatch(Input input) throws IOException {
        jsonMapper.toJson(input);
        var request = HttpRequest.newBuilder(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonMapper.toJson(input)))//TODO this is possibly super un-optimal
                .setHeader("Content-Type", "application/json")
                .build();

        return handleResponse(request);
    }

    protected abstract T handleResponse(HttpRequest request) throws IOException;
}
