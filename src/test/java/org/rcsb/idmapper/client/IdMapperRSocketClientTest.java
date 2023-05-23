package org.rcsb.idmapper.client;

import com.google.common.base.MoreObjects;
import org.junit.Test;
import org.rcsb.common.constants.ContentType;
import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.input.TranslateInput;

import java.util.List;

import static org.junit.Assert.*;

public class IdMapperRSocketClientTest {

    @Test
    public void doTranslate() {
        var input = new TranslateInput();
        input.ids = List.of("BHH4");
        input.from = Input.Type.entry;
        input.to =  Input.Type.polymer_entity;
        input.content_type = List.of(ContentType.experimental);

        var client = new IdMapperRSocketClient();

        var actual = client.doTranslate(input).block();
        System.out.println(MoreObjects.toStringHelper(actual)
                .add("result", actual.results)
                .toString());
    }

    @Test
    public void doGroup() {
    }
}