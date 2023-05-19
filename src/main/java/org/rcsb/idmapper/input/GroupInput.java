package org.rcsb.idmapper.input;

import java.util.List;

public class GroupInput extends Input {
    public List<String> ids;
    public Input.AggregationMethod aggregation_method;
    public Integer similarity_cutoff;
}
