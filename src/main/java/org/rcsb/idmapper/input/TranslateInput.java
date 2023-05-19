package org.rcsb.idmapper.input;

import org.rcsb.common.constants.ContentType;

import java.util.List;

public class TranslateInput extends Input {
    public Input.Type from;
    public Input.Type to;
    public List<String> ids;
    public List<ContentType> content_type;
}
