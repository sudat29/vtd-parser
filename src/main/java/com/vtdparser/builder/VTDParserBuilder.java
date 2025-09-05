package com.vtdparser.builder;

import com.vtdparser.VTDXMLParser;
import com.vtdparser.config.ParserConfiguration;

public class VTDParserBuilder {
    private ParserConfiguration configuration;

    public VTDParserBuilder() {
        this.configuration = new ParserConfiguration();
    }

    public VTDParserBuilder setBufferSize(int bufferSize) {
        this.configuration.setBufferSize(bufferSize);
        return this;
    }

    public VTDParserBuilder setEncoding(String encoding) {
        this.configuration.setEncoding(encoding);
        return this;
    }

    public VTDParserBuilder setNamespaceAware(boolean namespaceAware) {
        this.configuration.setNamespaceAware(namespaceAware);
        return this;
    }

    public VTDXMLParser build() {
        return new VTDXMLParser(configuration);
    }
}