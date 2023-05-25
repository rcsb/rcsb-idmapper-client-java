package org.rcsb.idmapper.client;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.rcsb.common.constants.ContentType;
import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.input.TranslateInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@Ignore("requires server, currently launched manually")
class IdMapperHttpClientTest {

    @Test
    void doTranslate1000x() throws IOException {
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

    /**
     * 10K requests per 100 clients (Threads) for 10 s
     */
    @Test
    void doTranslate10000x100x10() throws IOException, InterruptedException {
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

        int numberOfClients = 100;
        var executor = Executors.newFixedThreadPool(numberOfClients);

        var latch = new CountDownLatch(1);


        Flux.range(1, numberOfClients)
                .publishOn(Schedulers.fromExecutor(executor))
                .flatMap(ignored -> {
                    return Flux.range(1,10000)
                            .flatMap(moreIgnored -> {
                                return client.doTranslate(input);
                            });
                })

                .take(Duration.ofSeconds(10))
                .count()
                .subscribe(p -> {
                            System.out.println("Total count: " + p);
                            latch.countDown();
                        }
                );

        latch.await();
    }

    @Test
    void doGroup() {
    }

    @Test
    void doAll() {
    }
}