package org.rcsb.idmapper.client;

import com.google.common.base.MoreObjects;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rcsb.common.constants.ContentType;
import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.input.TranslateInput;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@ExtendWith(IdMapperTestContainer.class)
public class IdMapperRSocketClientTest {

    private IdMapperClient client;
    private RSocket rsocket;


    @BeforeEach
    public void before(IdMapperTestContainer idMapperTestContainer){
        var idMapper = idMapperTestContainer.getIdMapper();
        rsocket = RSocketConnector.create()
                .payloadDecoder(PayloadDecoder.ZERO_COPY)
                .connectWith(TcpClientTransport.create(idMapper.getHost(),idMapper.getMappedPort(9000))).block();

        var jsonMapper = new JsonMapper().create();

        client = new IdMapperRSocketClient(rsocket, jsonMapper);
    }

    @AfterEach
    public void after(){
        rsocket.dispose();
    }

    @Test
    public void doTranslate() {
        var input = new TranslateInput();
        input.ids = List.of("BHH4");
        input.from = Input.Type.entry;
        input.to =  Input.Type.polymer_entity;
        input.content_type = List.of(ContentType.experimental);

        var actual = client.doTranslate(input).block();
        System.out.println(MoreObjects.toStringHelper(actual)
                .add("result", actual.results)
                .toString());
    }

    @Test
    @Disabled("requires server, currently launched manually")
    public void doTranslate1000x() {
        var input = new TranslateInput();
        input.ids = List.of("BHH4");
        input.from = Input.Type.entry;
        input.to =  Input.Type.polymer_entity;
        input.content_type = List.of(ContentType.experimental);

        Flux.range(1,1000)
                .elapsed()
                .subscribe(p -> {
                    System.out.println("Elapsed: " + p.getT1());
                    var actual = client.doTranslate(input).block();
                    System.out.println(MoreObjects.toStringHelper(actual)
                            .add("result", actual.results)
                            .toString());
        });


    }

    /**
     * 10K requests per 100 clients (Threads) for 10 s
     */
    @Test
    @Disabled("requires server, currently launched manually")
    public void doTranslate10000x100x10() throws InterruptedException {
        //RSocket
        var input = new TranslateInput();
        input.ids = List.of("BHH4");
        input.from = Input.Type.entry;
        input.to =  Input.Type.polymer_entity;
        input.content_type = List.of(ContentType.experimental);


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
//                .log()
                .count()
                .subscribe(p -> {
                    System.out.println("Total count:" + p);
                    latch.countDown();
                });

        latch.await();
    }

    @Test
    public void doGroup() {
    }
}