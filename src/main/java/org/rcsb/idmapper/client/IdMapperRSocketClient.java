package org.rcsb.idmapper.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper jsonMapper;

    public IdMapperRSocketClient(RSocket client, ObjectMapper jsonMapper) {
        this.client = client;
        this.jsonMapper = jsonMapper;
    }

    public IdMapperRSocketClient(RSocket client) {
        this(client, new JsonMapper().create());
    }


    @Override
    public Mono<TranslateOutput> doTranslate(@Nonnull TranslateInput input) {
        try {
            return client.requestResponse(ByteBufPayload.create(jsonMapper.writeValueAsString(input), "/translate"))
                    .map(Payload::getDataUtf8)
                    .flatMap(responsePayload -> {
                        try {
                            return Mono.just(jsonMapper.readValue(responsePayload, TranslateOutput.class));
                        } catch (JsonProcessingException e) {
                            return Mono.error(e);
                        }
                    });
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<TranslateOutput> doGroup(@Nonnull GroupInput input) {
        try {
            return client.requestResponse(ByteBufPayload.create(jsonMapper.writeValueAsString(input), "/group"))
                    .map(Payload::getDataUtf8)
                    .flatMap(responsePayload -> {
                        try {
                            return Mono.just(jsonMapper.readValue(responsePayload, TranslateOutput.class));
                        } catch (JsonProcessingException e) {
                            return Mono.error(e);
                        }
                    });
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<AllOutput> doAll(@Nonnull AllInput input) {
        try {
            return client.requestResponse(ByteBufPayload.create(jsonMapper.writeValueAsString(input), "/all"))
                    .map(Payload::getDataUtf8)
                    .flatMap(responsePayload -> {
                        try {
                            return Mono.just(jsonMapper.readValue(responsePayload, AllOutput.class));
                        } catch (JsonProcessingException e) {
                            return Mono.error(e);
                        }
                    });
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
