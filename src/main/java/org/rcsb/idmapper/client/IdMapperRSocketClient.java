package org.rcsb.idmapper.client;

import com.google.gson.Gson;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.ByteBufPayload;
import org.rcsb.idmapper.input.AllInput;
import org.rcsb.idmapper.input.GroupInput;
import org.rcsb.idmapper.input.TranslateInput;
import org.rcsb.idmapper.output.AllOutput;
import org.rcsb.idmapper.output.TranslateOutput;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public class IdMapperRSocketClient implements IdMapperClient{

    private final RSocket client;

    private final Gson jsonMapper;

    public IdMapperRSocketClient(RSocket client, Gson jsonMapper) {
        this.client = client;
        this.jsonMapper = jsonMapper;
    }


    @Override
    public Mono<TranslateOutput> doTranslate(@Nonnull TranslateInput input) {
        return client.requestResponse(ByteBufPayload.create(jsonMapper.toJson(input), "/translate"))
                .map(Payload::getDataUtf8)
                .map(responsePayload -> {
                    return jsonMapper.fromJson(responsePayload, TranslateOutput.class);
                });
    }

    @Override
    public Mono<TranslateOutput> doGroup(@Nonnull GroupInput input) {
        return client.requestResponse(ByteBufPayload.create(jsonMapper.toJson(input), "/group"))
                .map(Payload::getDataUtf8)
                .map(responsePayload -> {
                    return jsonMapper.fromJson(responsePayload, TranslateOutput.class);
                });
    }

    @Override
    public Mono<AllOutput> doAll(@Nonnull AllInput input) {
        return client.requestResponse(ByteBufPayload.create(jsonMapper.toJson(input), "/all"))
                .map(Payload::getDataUtf8)
                .map(responsePayload -> {
                    return jsonMapper.fromJson(responsePayload, AllOutput.class);
                });
    }
}
