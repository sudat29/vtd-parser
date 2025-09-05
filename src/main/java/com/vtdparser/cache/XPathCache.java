package com.vtdparser.cache;

import com.vtdparser.exception.XPathParsingException;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XPathCache {
    private final Map<String, XPathExpression> cache;

    public XPathCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    public XPathExpression getXPath(String expression) {
        return cache.computeIfAbsent(expression, this::compileXPath);
    }

    private XPathExpression compileXPath(String expression) {
        try {
            return XPathFactory.newInstance().newXPath().compile(expression);
        } catch (XPathExpressionException e) {
            throw new XPathParsingException("Failed to compile XPath expression: " + expression, e);
        }
    }

    public void clearCache() {
        cache.clear();
    }

    public int getCacheSize() {
        return cache.size();
    }
}