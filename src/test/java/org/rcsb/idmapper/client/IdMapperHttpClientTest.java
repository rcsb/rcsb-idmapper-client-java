package org.rcsb.idmapper.client;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.junit.jupiter.api.Test;
import org.rcsb.common.constants.ContentType;
import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.input.TranslateInput;

import java.io.IOException;
import java.util.List;

class IdMapperHttpClientTest {

    @Test
    void doTranslate() throws IOException {
        var input = new TranslateInput();
        input.ids = List.of("HBB4");
        input.from = Input.Type.entry;
        input.to =  Input.Type.polymer_entity;
        input.content_type = List.of(ContentType.experimental);

        var result = new IdMapperHttpClient().doTranslate(
                input
        );
        var actual = result.block();
        System.out.println(MoreObjects.toStringHelper(actual)
                .add("result", actual.results)
                .toString());
    }

    @Test
    void doGroup() {
    }

    @Test
    void doAll() {
    }
}