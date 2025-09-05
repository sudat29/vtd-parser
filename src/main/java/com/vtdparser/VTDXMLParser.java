package com.vtdparser;

import com.vtdparser.config.ParserConfiguration;
import com.vtdparser.model.Element;
import com.vtdparser.model.ValidationResult;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import java.util.*;

public class VTDXMLParser implements AutoCloseable {
  // Core VTD-XML components
  private VTDGen vtdGen;
  private VTDNav vtdNav;
  private AutoPilot autoPilot;
  private ParserConfiguration configuration;

  public VTDXMLParser() {
    this(new ParserConfiguration());
  }

  public VTDXMLParser(ParserConfiguration configuration) {
    this.configuration = configuration;
    this.vtdGen = new VTDGen();
  }

  public void loadDocument(String xmlFilePath) throws Exception {
    if (!vtdGen.parseFile(xmlFilePath, false)) {
      throw new Exception("Failed to parse XML file: " + xmlFilePath);
    }
    vtdNav = vtdGen.getNav();
    autoPilot = new AutoPilot(vtdNav);
    this.vtdGen = null; // Clear VTDGen to free resources
  }

  public List<Element> searchChildrenByAttribute(Element parentElement, String attributeName, String attributeValue) {
    if (vtdNav == null || autoPilot == null || parentElement == null) {
      return Collections.emptyList();
    }

    String parentElementXpath = parentElement.getXpath();
    String xpathExpression = parentElementXpath + "/*[@" + attributeName + "='" + attributeValue + "']";
    return searchByXPath(xpathExpression);
  }

  public Element searchByIndex(String tagName, int index) {
    if (vtdNav == null || autoPilot == null) {
      return null;
    }
    String xpathExpression = "//" + tagName + "[" + (index + 1) + "]"; // XPath is 1-based
    List<Element> elements = searchByXPath(xpathExpression);
    return elements.isEmpty() ? null : elements.get(0);
  }

  public List<Element> searchByAttribute(String tagName, String attributeName, String attributeValue) {
    if (vtdNav == null || autoPilot == null) {
      return Collections.emptyList();
    }
    String xpathExpression = "//" + tagName + "[@" + attributeName + "='" + attributeValue + "']";
    return searchByXPath(xpathExpression);
  }

  public List<Element> searchByXPath(String xpathExpression) {
    List<Element> results = new ArrayList<>();
    try {
      // Reset AutoPilot and set XPath expression to find elements with specific attribute value
      autoPilot.resetXPath();
      autoPilot.selectXPath(xpathExpression);
      int xpathIndex;
      while ((xpathIndex = autoPilot.evalXPath()) != -1) {
        vtdNav.push(); // Push current context to stack
        String tagName = vtdNav.toString(xpathIndex);
        // Get the text content of the element
        String textContent = "";
        int textIndex = vtdNav.getText();
        if (textIndex != -1) {
          textContent = vtdNav.toString(textIndex);
        }

        // Get all attributes of the current element
        Map<String, String> attributes = getCurrentElementAttrs(xpathIndex);

        // Create ElementResult with all attributes and index
        Pair<Integer, String> elementInfo = getElementInfo(vtdNav);
        Element elementResult = new Element(tagName, textContent, attributes, xpathIndex, elementInfo.first, elementInfo.second);
        results.add(elementResult);
        vtdNav.pop(); // Pop back to the parent element after processing child
      }
    } catch (Exception e) {
      // Log error but return empty list instead of throwing
      System.err.println("Error in searchByAttribute: " + e.getMessage());
    }
    return results;
  }

  record Pair<F, S>(F first, S second) {
    public F getFirst() {
      return first;
    }

    public S getSecond() {
      return second;
    }
  }

  private static Pair<Integer, String> getElementInfo(VTDNav vn) throws NavException {
    StringBuilder xpath = new StringBuilder();
    int currentIndex = vn.getCurrentIndex();

    // Check if current index is an element node
    int tokenType = vn.getTokenType(currentIndex);
    if (tokenType != VTDNav.TOKEN_STARTING_TAG) {
      throw new NavException("Current index is not an element");
    }

    int currPos = -1;

    // Traverse up to root
    while (true) {
      int tagIndex = vn.getCurrentIndex();

      // Get element name
      String elementName = vn.toString(tagIndex);

      int position = getCurrentElementPosition(vn);

      if (currPos == -1) {
        currPos = position; // Store the first position found
      }

      // Prepend this step to xpath
      if (!elementName.isEmpty()) {
        xpath.insert(0, "/" + elementName + "[" + position + "]");
      }

      // Move to parent; break if no parent
      if (!vn.toElement(VTDNav.PARENT)) {
        break;
      }
    }
    return new Pair<>(currPos, xpath.toString());
  }

  private static int getCurrentElementPosition(VTDNav vn) throws NavException {
    int currentIndex = vn.getCurrentIndex();

//    // Check if current index is an element node
//    int tokenType = vn.getTokenType(currentIndex);
//    if (tokenType != VTDNav.TOKEN_STARTING_TAG) {
//      throw new NavException("Current index is not an element");
//    }


    // Get element name
    String elementName = vn.toString(currentIndex);

    // Get element position among siblings with same name
    int position = 1; // XPath is 1-based

    if (vn.toElement(VTDNav.PREV_SIBLING)) {
      do {
        if (vn.matchElement(elementName)) {
          position++;
        }
      } while (vn.toElement(VTDNav.PREV_SIBLING));

      // Move back to original element after sibling traversal
      vn.toElement(VTDNav.NEXT_SIBLING);
      while (vn.getCurrentIndex() != currentIndex) {
        vn.toElement(VTDNav.NEXT_SIBLING);
      }
    }

    return position;
  }

  public int getElementCountByXPath(String xpath) {
    if (vtdNav == null || autoPilot == null) {
      return 0;
    }

    int count = 0;

    try {
      // Reset AutoPilot and set the XPath expression
      autoPilot.resetXPath();
      autoPilot.selectXPath(xpath);

      // Count all matching nodes
      while (autoPilot.evalXPath() != -1) {
        count++;
      }

    } catch (Exception e) {
      // Log error but return 0 instead of throwing
      System.err.println("Error in getElementCountByXPath: " + e.getMessage());
      return 0;
    }

    return count;
  }

  public List<Element> getChildren(Element parentElement) {
    if (vtdNav == null || autoPilot == null || parentElement == null) {
      return Collections.emptyList();
    }
    String parentElementXpath = parentElement.getXpath();
    String xpathExpression = parentElementXpath + "/*";
    return searchByXPath(xpathExpression);
  }

  private Map<String, String> getCurrentElementAttrs(int elemIndex) throws NavException {
    // Get attributes of the child element
    Map<String, String> attributes = new HashMap<>();
    int attrCount = vtdNav.getAttrCount();

    for (int i = 0; i < attrCount * 2; ) {
      String attrName = vtdNav.toString(elemIndex + i + 1);
      String attrValue = vtdNav.toString(elemIndex + i + 2);
      attributes.put(attrName, attrValue);
      i += 2; // Attribute name and value are consecutive
    }
    return attributes;
  }

  public String getValueByXPath(String xpath) {
    // Implementation for retrieving text value by XPath expression
    if (vtdNav == null || autoPilot == null) {
      return null;
    }

    try {
      autoPilot.resetXPath();
      autoPilot.selectXPath(xpath);
      int xpathIndex = autoPilot.evalXPath();
      if (xpathIndex != -1) {
        int textIndex = vtdNav.getText();
        if (textIndex != -1) {
          return vtdNav.toString(textIndex);
        }
      }
    } catch (Exception e) {
      // Log error but return null instead of throwing
      System.err.println("Error in getValueByXPath: " + e.getMessage());
    }
    return null;
  }

  public boolean existsElement(String xpath) {
    // Implementation for checking if an element exists for the given XPath
    return getElementCountByXPath(xpath) > 0;
  }

  @Override
  public void close() {
    // Proper resource cleanup
    // Temporarily disabled until VTD-XML dependency is resolved
    // if (vtdGen != null) {
    //     vtdGen.close();
    // }
  }

  public void parse(String filePath) {
    try {
      loadDocument(filePath);
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse XML file: " + filePath, e);
    }
  }

  // Additional methods for caching, error handling, and performance metrics can be added here
}