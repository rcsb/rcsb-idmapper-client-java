package org.rcsb.idmapper.output;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class TranslateOutput extends Output<Multimap<String, String>> {

    public Multimap<String, String> results = ArrayListMultimap.create();

    @Override
    public Multimap<String, String> getResults() {
        return results;
    }
}
