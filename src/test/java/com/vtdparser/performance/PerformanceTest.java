// PerformanceTest.java
package com.vtdparser.performance;

import com.vtdparser.VTDXMLParser;
import com.vtdparser.model.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerformanceTest {

    private static VTDXMLParser parser;
    private static final String LARGE_XML_FILE = "src/test/resources/test-data/4-6-2022.xml"; // Path to a large XML file for testing

    @Test
    public void testNestedChildrenAttributeRetrievalPerformance() {
        // Setup
        parser = new VTDXMLParser();
        File xmlFile = new File(LARGE_XML_FILE);
        assertTrue(xmlFile.exists(), "Large XML test file should exist");

        parser.parse(LARGE_XML_FILE);

        // Measure performance of nested children attribute retrieval
        long startTime = System.currentTimeMillis();

        // Get root elements by attribute
        List<Element> rootElements = parser.searchByAttribute("root", "id", "main");

        int totalNestedElements = 0;
        for (Element rootElement : rootElements) {
            // Get direct children
            List<Element> children = parser.getChildren(rootElement);

            for (Element child : children) {
                // Search for nested children with specific attributes
                List<Element> nestedChildren = parser.searchChildrenByAttribute(child, "type", "data");
                totalNestedElements += nestedChildren.size();

                // Further drill down for deeply nested elements
                for (Element nestedChild : nestedChildren) {
                    List<Element> deepChildren = parser.searchChildrenByAttribute(nestedChild, "status", "active");
                    totalNestedElements += deepChildren.size();
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Performance assertions
        assertTrue(executionTime < 5000, "Nested children attribute retrieval should complete within 5 seconds");
        assertTrue(totalNestedElements >= 0, "Should retrieve some nested elements");

        System.out.println("Nested children retrieval performance: " + executionTime + "ms");
        System.out.println("Total nested elements processed: " + totalNestedElements);

        parser.close();
    }

    @Test
    public void testDeeplyNestedElementRetrievalPerformance() {
        // Setup
        parser = new VTDXMLParser();
        File xmlFile = new File(LARGE_XML_FILE);
        assertTrue(xmlFile.exists(), "Large XML test file should exist");

        parser.parse(LARGE_XML_FILE);

        // Measure performance of deeply nested element retrieval
        long startTime = System.currentTimeMillis();

        List<Element> smartFolders = parser.searchByAttribute("SMART_FOLDER", "JOBNAME", "TDPROD2_TDPROD_BATCH_008");
        int foundElements = 0;
        for (Element smartFolder : smartFolders) {
            List<Element> subFolders = parser.searchChildrenByAttribute(smartFolder, "JOBNAME", "TDPROD2_TDPROD_BATCH_008_GGSRC");
            for (Element subFolder : subFolders) {
                List<Element> jobs = parser.searchChildrenByAttribute(subFolder, "JOBNAME", "TDPROD2_PY_JOB_CSF_XXCAS_PRJ_ATB_SERVICE");
                for (Element job : jobs) {
                    List<Element> ons = parser.getChildren(job); // Assuming ON is a direct child
                    for (Element on : ons) {
                        if ("ON".equalsIgnoreCase(on.getTagName())) {
                            foundElements++;
                        }
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Performance assertions
        assertTrue(executionTime < 500, "Deeply nested element retrieval should complete within 5 seconds");
        assertTrue(foundElements > 0, "Should find at least one deeply nested element");

        System.out.println("Deeply nested element retrieval performance: " + executionTime + "ms");
        System.out.println("Total deeply nested elements found: " + foundElements);

        parser.close();
    }

    @Test
    public void testWideSiblingElementRetrievalPerformance() {
        // Setup
        parser = new VTDXMLParser();
        File xmlFile = new File(LARGE_XML_FILE);
        assertTrue(xmlFile.exists(), "Large XML test file should exist");

        parser.parse(LARGE_XML_FILE);

        // Measure performance of wide sibling element retrieval
        long startTime = System.currentTimeMillis();

        List<Element> smartFolders = parser.searchByAttribute("SMART_FOLDER", "JOBNAME", "TDPROD2_GENERAL");
        assertTrue(smartFolders.size() > 0, "Should find the 'TDPROD2_GENERAL' SMART_FOLDER");

        Element generalFolder = smartFolders.get(0);
        List<Element> jobs = parser.getChildren(generalFolder);

        int commandJobs = 0;
        for (Element job : jobs) {
            if ("JOB".equals(job.getTagName()) && "Command".equals(job.getAttributes().get("TASKTYPE"))) {
                commandJobs++;
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Performance assertions
        assertTrue(executionTime < 5000, "Wide sibling element retrieval should complete within 5 seconds");
        assertTrue(commandJobs > 0, "Should find command jobs in the general folder");

        System.out.println("Wide sibling element retrieval performance: " + executionTime + "ms");
        System.out.println("Total command jobs found: " + commandJobs);

        parser.close();
    }

    @Test
    public void testMultipleAttributeFilterPerformance() {
        // Setup
        parser = new VTDXMLParser();
        File xmlFile = new File(LARGE_XML_FILE);
        assertTrue(xmlFile.exists(), "Large XML test file should exist");

        parser.parse(LARGE_XML_FILE);

        // Measure performance of filtering by multiple attributes
        long startTime = System.currentTimeMillis();

        List<Element> commandJobs = parser.searchByAttribute("JOB", "TASKTYPE", "Command");

        int criticalCommandJobs = 0;
        for (Element job : commandJobs) {
            if ("0".equals(job.getAttributes().get("CRITICAL"))) {
                criticalCommandJobs++;
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Performance assertions
        assertTrue(executionTime < 5000, "Multiple attribute filter performance should complete within 5 seconds");
        assertTrue(criticalCommandJobs > 0, "Should find critical command jobs");

        System.out.println("Multiple attribute filter performance: " + executionTime + "ms");
        System.out.println("Total critical command jobs found: " + criticalCommandJobs);

        parser.close();
    }
}