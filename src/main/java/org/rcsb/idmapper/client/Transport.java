package org.rcsb.idmapper.client;

import org.rcsb.idmapper.input.Input;
import org.rcsb.idmapper.output.Output;

import java.io.IOException;

public interface Transport<T extends Output<?>> {
    T dispatch(Input input) throws IOException;
}
