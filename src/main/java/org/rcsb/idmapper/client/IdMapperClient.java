package org.rcsb.idmapper.client;

import org.rcsb.idmapper.input.AllInput;
import org.rcsb.idmapper.input.GroupInput;
import org.rcsb.idmapper.input.TranslateInput;
import org.rcsb.idmapper.output.AllOutput;
import org.rcsb.idmapper.output.TranslateOutput;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public interface IdMapperClient {
    Mono<TranslateOutput> doTranslate(@Nonnull TranslateInput input);

    Mono<TranslateOutput> doGroup(@Nonnull GroupInput input);

    Mono<AllOutput> doAll(@Nonnull AllInput input);
}
