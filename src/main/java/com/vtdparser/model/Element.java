package com.vtdparser.model;

import java.util.Map;
import java.util.HashMap;

public class Element {
    private String tagName;
    private String textContent;
    private Map<String, String> attributes;
    private int xpathIndex;
    private int elementIndex;
    private String xpath;

    public Element(String tagName, String textContent, Map<String, String> attributes, int xpathIndex, int elementIndex, String xpath) {
        this.tagName = tagName;
        this.textContent = textContent;
        this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
        this.xpathIndex = xpathIndex;
        this.elementIndex = elementIndex;
        this.xpath = xpath;
    }

    // Legacy constructor for backward compatibility
    public Element(String tagName, String attributeName, String attributeValue, String textContent) {
        this.tagName = tagName;
        this.textContent = textContent;
        this.attributes = new HashMap<>();
        if (attributeName != null && attributeValue != null) {
            this.attributes.put(attributeName, attributeValue);
        }
        this.elementIndex = 0;
    }

    public String getTagName() {
        return tagName;
    }

    public int getXpathIndex() {
        return xpathIndex;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getXpath() {
        return xpath;
    }

    public String getValue() {
        return textContent;
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes);
    }

    public String getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    public int getElementIndex() {
        return elementIndex;
    }

    // Legacy methods for backward compatibility
    public String getAttributeName() {
        return attributes.isEmpty() ? null : attributes.keySet().iterator().next();
    }

    public String getAttributeValue() {
        return attributes.isEmpty() ? null : attributes.values().iterator().next();
    }

    @Override
    public String toString() {
        return "ElementResult{" +
                "tagName='" + tagName + '\'' +
                ", attributes=" + attributes +
                ", textContent='" + textContent + '\'' +
                ", elementIndex=" + elementIndex +
                '}';
    }
}