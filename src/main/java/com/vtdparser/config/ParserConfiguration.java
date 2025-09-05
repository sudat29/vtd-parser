package com.vtdparser.config;

public class ParserConfiguration {
    private int bufferSize;
    private String encoding;
    private boolean namespaceAware;

    public ParserConfiguration() {
        // Default values
        this.bufferSize = 8192; // Default buffer size
        this.encoding = "UTF-8"; // Default encoding
        this.namespaceAware = false; // Default namespace awareness
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isNamespaceAware() {
        return namespaceAware;
    }

    public void setNamespaceAware(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }
}