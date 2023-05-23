package org.rcsb.idmapper.client;

import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.output.Output;

import java.io.IOException;

public class RSocketTransport<T extends Output<?>> implements Transport<T> {


    @Override
    public T dispatch(Input input) throws IOException {
        return null;
    }
}
