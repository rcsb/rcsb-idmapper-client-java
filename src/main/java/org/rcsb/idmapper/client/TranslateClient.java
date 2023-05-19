package org.rcsb.idmapper.client;

import org.rcsb.idmapper.input.TranslateInput;
import org.rcsb.idmapper.output.TranslateOutput;

import java.util.concurrent.Callable;

public class TranslateClient implements Callable<TranslateOutput> {

    private final Transport<TranslateOutput> transport;
    private final TranslateInput input;

    public TranslateClient(Transport<TranslateOutput> transport, TranslateInput input) {
        this.transport = transport;
        this.input = input;
    }


    @Override
    public TranslateOutput call() throws Exception {
        return transport.dispatch(input);
    }
}
