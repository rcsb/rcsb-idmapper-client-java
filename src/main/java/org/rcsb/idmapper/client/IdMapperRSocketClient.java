package org.rcsb.idmapper.client;

import com.google.gson.Gson;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.ByteBufPayload;
import org.rcsb.idmapper.input.AllInput;
import org.rcsb.idmapper.input.GroupInput;
import org.rcsb.idmapper.input.TranslateInput;
import org.rcsb.idmapper.output.AllOutput;
import org.rcsb.idmapper.output.TranslateOutput;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public class IdMapperRSocketClient implements IdMapperClient{

    private final RSocket client =
            RSocketConnector.create()
                .payloadDecoder(PayloadDecoder.ZERO_COPY)
                .connectWith(TcpClientTransport.create(7000)).block();

    private final Gson jsonMapper = new JsonMapper().create();


    @Override
    public Mono<TranslateOutput> doTranslate(@Nonnull TranslateInput input) {
        return client.requestResponse(ByteBufPayload.create(jsonMapper.toJson(input), "/translate"))
                .map(Payload::getDataUtf8)
                .map(responsePayload -> {
                    return jsonMapper.fromJson(responsePayload, TranslateOutput.class);
                })
                .doOnNext(System.out::println);
    }

    @Override
    public Mono<TranslateOutput> doGroup(@Nonnull GroupInput input) {
        return null;
    }

    @Override
    public Mono<AllOutput> doAll(@Nonnull AllInput input) {
        return null;
    }
}
