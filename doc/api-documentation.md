# API Documentation for VTD-XML Parser

## Overview
The VTD-XML Parser is a high-performance, memory-efficient XML parsing library built on the VTD-XML technology. This document provides comprehensive API documentation for the public methods of the `VTDXMLParser` class and other components within the library.

## Package Structure
The VTD-XML Parser is organized into several packages, each serving a specific purpose:

- **com.vtdparser**: Contains the main parser class and core functionalities.
- **com.vtdparser.builder**: Implements the builder pattern for parser configuration.
- **com.vtdparser.config**: Defines configuration settings for the parser.
- **com.vtdparser.exception**: Contains custom exception classes for error handling.
- **com.vtdparser.model**: Defines model classes for representing results.
- **com.vtdparser.cache**: Implements caching strategies for XPath expressions.
- **com.vtdparser.util**: Contains utility classes for resource management and performance metrics.

## VTDXMLParser Class
### Methods

#### `searchByAttribute(String tagName, String attributeName, String attributeValue)`
- **Description**: Searches for elements by tag name and attribute value.
- **Parameters**:
  - `tagName`: The name of the XML tag to search for.
  - `attributeName`: The name of the attribute to match.
  - `attributeValue`: The value of the attribute to match.
- **Returns**: A list of matching elements or custom result objects.

#### `getElementCountByXPath(String xpath)`
- **Description**: Counts all elements matching the given XPath expression.
- **Parameters**:
  - `xpath`: The XPath expression to evaluate.
- **Returns**: The count of matching elements.

#### `getElementByXPathAndIndex(String xpath, int index)`
- **Description**: Retrieves a specific element by XPath and zero-based index.
- **Parameters**:
  - `xpath`: The XPath expression to evaluate.
  - `index`: The zero-based index of the element to retrieve.
- **Returns**: An `Element` object or null if not found.

#### `getValueByXPath(String xpath)`
- **Description**: Extracts the text content/value for the given XPath.
- **Parameters**:
  - `xpath`: The XPath expression to evaluate.
- **Returns**: The text content or null if not found.

#### `getAllValuesByXPath(String xpath)`
- **Description**: Retrieves all text values matching the XPath expression.
- **Parameters**:
  - `xpath`: The XPath expression to evaluate.
- **Returns**: A list of text values.

#### `getAttributeValue(String xpath, String attributeName)`
- **Description**: Gets the attribute value for the element at the specified XPath.
- **Parameters**:
  - `xpath`: The XPath expression to evaluate.
  - `attributeName`: The name of the attribute to retrieve.
- **Returns**: The attribute value or null if not found.

#### `existsElement(String xpath)`
- **Description**: Checks if an element exists at the given XPath.
- **Parameters**:
  - `xpath`: The XPath expression to evaluate.
- **Returns**: `true` if the element exists, `false` otherwise.

#### `getElementsWithNamespace(String namespaceURI, String localName)`
- **Description**: Retrieves elements with the specified namespace.
- **Parameters**:
  - `namespaceURI`: The namespace URI to match.
  - `localName`: The local name of the elements to retrieve.
- **Returns**: A list of matching elements.

#### `validateXML(String schemaPath)`
- **Description**: Validates the XML against the specified XSD schema.
- **Parameters**:
  - `schemaPath`: The path to the XSD schema file.
- **Returns**: A `ValidationResult` object indicating the validation outcome.

## Exception Handling
The parser includes custom exceptions to handle various error scenarios:

- **VTDParserException**: General parser errors.
- **XPathParsingException**: Errors related to XPath parsing.
- **ResourceManagementException**: Errors related to resource management.

## Usage Examples
Refer to the `usage-examples.md` document for practical examples of how to use the VTD-XML Parser effectively.

## Performance Considerations
The VTD-XML Parser is designed to handle large XML documents efficiently. For performance benchmarks and memory usage analysis, see the `performance-benchmarks.md` document.

## Conclusion
This API documentation provides a comprehensive overview of the VTD-XML Parser's capabilities. For further information, please refer to the additional documentation files in the `doc` directory.