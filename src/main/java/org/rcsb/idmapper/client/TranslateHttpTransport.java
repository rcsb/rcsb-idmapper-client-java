package org.rcsb.idmapper.client;

import com.google.gson.Gson;
import org.rcsb.idmapper.output.TranslateOutput;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TranslateHttpTransport extends HttpTransport<TranslateOutput>{
    public TranslateHttpTransport(Gson jsonMapper, HttpClient httpClient, URI uri) {
        super(jsonMapper, httpClient, uri);
    }

    @Override
    protected TranslateOutput handleResponse(HttpRequest request) throws IOException {
        try {
            return jsonMapper.fromJson(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body(), TranslateOutput.class);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
}
