package com.vtdparser;

import com.vtdparser.model.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VTDXMLParserTest {

  private VTDXMLParser parser;

  @BeforeEach
  public void setUp() {
    // Initialize the parser before each test
    parser = new VTDXMLParser();
    // Load a sample XML document for testing
    try {
      parser.loadDocument("src/test/resources/test-data/small-test.xml");
    } catch (Exception e) {
      throw new RuntimeException("Failed to load test document", e);
    }
  }

  @AfterEach
  public void tearDown() {
    // Clean up resources after each test
    parser.close();
  }

  @Test
  public void testSearchByIndex() {
    // Test searching for the first element with tag name "element"
    Element element = parser.searchByIndex("element", 3);
    assertEquals("element", element.getTagName());
    assertEquals("", element.getValue());
    assertEquals(4, element.getElementIndex()); // First matching element should have index 0
    assertEquals(27, element.getXpathIndex()); // XPath index for the first element
    assertEquals("/root[1]/element[4]", element.getXpath()); // XPath for the first element
  }

  @Test
  public void testSearchByXPath() {
    // Test searching for elements with XPath
    List<Element> results = parser.searchByXPath("//element");
    assertNotNull(results);
    assertEquals(4, results.size()); // There are 4 elements with tag name "element"

    // Verify the first result (index 0)
    Element firstResult = results.get(0);
    assertEquals("element", firstResult.getTagName());
    assertEquals("Content 1", firstResult.getValue());
    assertEquals(1, firstResult.getElementIndex()); // First matching element should have index 0
    assertEquals(6, firstResult.getXpathIndex()); // XPath index for the first element
    assertEquals("/root[1]/element[1]", firstResult.getXpath()); // XPath for the first element

    // Verify the second result (index 1)
    Element secondResult = results.get(1);
    assertEquals("element", secondResult.getTagName());
    assertEquals("Content 2", secondResult.getValue());
    assertEquals(2, secondResult.getElementIndex()); // Second matching element should have index 1
    assertEquals(12, secondResult.getXpathIndex()); // XPath index for the second element
    assertEquals("/root[1]/element[2]", secondResult.getXpath()); // XPath for the second element

    // Verify the third result (index 2)
    Element thirdResult = results.get(2);
    assertEquals("element", thirdResult.getTagName());
    assertEquals("", thirdResult.getValue());
    assertEquals(3, thirdResult.getElementIndex()); // Third matching element should have index 2
    assertEquals(16, thirdResult.getXpathIndex()); // XPath index for the third element
    assertEquals("/root[1]/element[3]", thirdResult.getXpath()); // XPath for the third element

    // Verify the fourth result (index 3)
    Element fourthResult = results.get(3);
    assertEquals("element", fourthResult.getTagName());
    assertEquals("", fourthResult.getValue()); // Self-closing element has no content
    assertEquals(4, fourthResult.getElementIndex()); // Fourth matching element should have index 3
    assertEquals(27, fourthResult.getXpathIndex()); // XPath index for the self-closing element
    assertEquals("/root[1]/element[4]", fourthResult.getXpath()); // XPath for
  }

  @Test
  public void testSearchByAttribute() {
    // Test searching for elements with attribute1="value1"
    List<Element> results = parser.searchByAttribute("element", "attribute1", "value1");
    assertNotNull(results);
    assertEquals(2, results.size()); // There are 2 elements with attribute1="value1"

    // Verify the first result (index 0)
    Element firstResult = results.get(0);
    assertEquals("element", firstResult.getTagName());
    assertEquals("Content 1", firstResult.getValue());
    assertEquals(1, firstResult.getElementIndex()); // First matching element should have index 0
    assertEquals(6, firstResult.getXpathIndex()); // XPath index for the first element
    assertEquals("/root[1]/element[1]", firstResult.getXpath()); // XPath for the first element
    // Check all attributes for first element
    Map<String, String> firstAttributes = firstResult.getAttributes();
    assertEquals("value1", firstAttributes.get("attribute1"));
    assertEquals("value2", firstAttributes.get("attribute2"));
    assertEquals(2, firstAttributes.size()); // Should have 2 attributes

    // Verify the second result (index 1)
    Element secondResult = results.get(1);
    assertEquals("element", secondResult.getTagName());
    assertEquals("Content 2", secondResult.getValue());
    assertEquals(2, secondResult.getElementIndex()); // Second matching element should have index 1
    assertEquals(12, secondResult.getXpathIndex()); // XPath index for the first element
    assertEquals("/root[1]/element[2]", secondResult.getXpath()); // XPath for the first element

    // Check attributes for second element
    Map<String, String> secondAttributes = secondResult.getAttributes();
    assertEquals("value1", secondAttributes.get("attribute1"));
    assertEquals(1, secondAttributes.size()); // Should have only 1 attribute

    // Test searching for element with attribute1="value3" (has nested content)
    List<Element> nestedResult = parser.searchByAttribute("element", "attribute1", "value3");
    assertNotNull(nestedResult);
    assertEquals(1, nestedResult.size());
    Element thirdResult = nestedResult.get(0);
    assertEquals(3, thirdResult.getElementIndex()); // Only one match, so index 0
    assertEquals(16, thirdResult.getXpathIndex()); // XPath index for the first element
    assertEquals("/root[1]/element[3]", thirdResult.getXpath()); // XPath for the first element

    // Test searching for non-existent attribute value
    List<Element> noResults = parser.searchByAttribute("element", "attribute1", "nonexistent");
    assertNotNull(noResults);
    assertEquals(0, noResults.size());

    // Test searching for element with "attribute" (different attribute name)
    List<Element> differentAttrResults = parser.searchByAttribute("element", "attribute", "value4");
    assertNotNull(differentAttrResults);
    assertEquals(1, differentAttrResults.size());
    assertEquals("", differentAttrResults.get(0).getValue()); // Self-closing element
    assertEquals(4, differentAttrResults.get(0).getElementIndex());
    assertEquals(27, differentAttrResults.get(0).getXpathIndex()); // XPath index for the self-closing element
    assertEquals("/root[1]/element[4]", differentAttrResults.get(0).getXpath()); // XPath for the self-closing element
  }

  @Test
  public void testGetChildren() {
    // Test getting children of the first element with attribute1="value1"
    List<Element> result = parser.searchByAttribute("element", "attribute1", "value3");
    List<Element> children = parser.getChildren(result.get(0));
    assertNotNull(children);
    assertEquals(2, children.size()); // There are 2 child elements

    // Verify the first child
    Element firstChild = children.get(0);
    assertEquals("childElement", firstChild.getTagName());
    assertEquals("Child Content1", firstChild.getValue());
    assertEquals(1, firstChild.getElementIndex()); // First child should have index 0
    assertEquals(19, firstChild.getXpathIndex()); // XPath index for the first child
    assertEquals("/root[1]/element[3]/childElement[1]", firstChild.getXpath()); // XPath for the first child

    // Verify the second child
    Element secondChild = children.get(1);
    assertEquals("childElement", secondChild.getTagName());
    assertEquals("Child Content2", secondChild.getValue());
    assertEquals(2, secondChild.getElementIndex()); // Second child should have index 1
    assertEquals(23, secondChild.getXpathIndex()); // XPath index for the second child
    assertEquals("/root[1]/element[3]/childElement[2]", secondChild.getXpath()); // XPath for the second child
  }

  @Test
  public void testSearchChildrenByAttribute() {
    // Test searching children by attribute
    List<Element> result = parser.searchByAttribute("element", "attribute1", "value3");

    List<Element> results = parser.searchChildrenByAttribute(result.get(0), "name", "child2");
    assertNotNull(results);
    assertEquals(1, results.size()); // There should be 1 matching child element

    Element child = results.get(0);
    assertEquals("childElement", child.getTagName());
    assertEquals("Child Content2", child.getValue());
    assertEquals(2, child.getElementIndex()); // Should be the second child
    assertEquals(23, child.getXpathIndex()); // XPath index for the second child
    assertEquals("/root[1]/element[3]/childElement[2]", child.getXpath()); // XPath for the second child
  }

  @Test
  public void testGetElementCountByXPath() {
    int count = parser.getElementCountByXPath("/root[1]/element");
    assertEquals(4, count); // There are 4 elements with tag name "element"
  }

  @Test
  public void testGetValueByXPath() {
    String value = parser.getValueByXPath("//element[@attribute1='value1']");
    assertEquals("Content 1", value);
  }

  @Test
  public void testExistsElement() {
    boolean exists = parser.existsElement("//element[@attribute1='value1']");
    assertTrue(exists);
  }

}