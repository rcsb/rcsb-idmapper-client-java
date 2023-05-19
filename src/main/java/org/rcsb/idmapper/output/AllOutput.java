package org.rcsb.idmapper.output;

import java.util.ArrayList;
import java.util.List;

public class AllOutput extends Output<List<String>> {

    public List<String> results = new ArrayList<>();

    @Override
    public List<String> getResults() {
        return results;
    }
}
