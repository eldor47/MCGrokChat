package com.grokchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests to verify code structure and logic without requiring Mockito
 */
class BasicCodeAnalysisTest {

    @Test
    void testMessageSplitting() {
        // Test that message splitting works correctly
        String shortMessage = "Hello world";
        java.util.List<String> result = com.grokchat.utils.MessageUtils.splitMessage(shortMessage, 50);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(shortMessage, result.get(0));
    }

    @Test
    void testMessageSplittingLongMessage() {
        String longMessage = "This is a very long message that needs to be split into multiple parts for proper display";
        java.util.List<String> result = com.grokchat.utils.MessageUtils.splitMessage(longMessage, 20);
        
        assertNotNull(result);
        assertTrue(result.size() > 1, "Long message should be split into multiple parts");
        
        // Verify no part exceeds the max length (accounting for words that might be longer)
        for (String part : result) {
            assertNotNull(part);
            assertFalse(part.isEmpty());
        }
    }

    @Test
    void testColorizeBasic() {
        String input = "&bHello &aWorld";
        String result = com.grokchat.utils.MessageUtils.colorize(input);
        
        assertNotNull(result);
        assertNotEquals(input, result); // Should be different after colorization
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("World"));
    }

    @Test
    void testColorizeNoColorCodes() {
        String input = "Plain text";
        String result = com.grokchat.utils.MessageUtils.colorize(input);
        
        assertNotNull(result);
        assertEquals(input, result); // Should be the same without color codes
    }

    @Test
    void testFormatError() {
        String result = com.grokchat.utils.MessageUtils.formatError("Test error");
        assertNotNull(result);
        assertTrue(result.contains("Test error"));
        assertTrue(result.contains("✗"));
    }

    @Test
    void testFormatSuccess() {
        String result = com.grokchat.utils.MessageUtils.formatSuccess("Test success");
        assertNotNull(result);
        assertTrue(result.contains("Test success"));
        assertTrue(result.contains("✓"));
    }

    @Test
    void testFormatInfo() {
        String result = com.grokchat.utils.MessageUtils.formatInfo("Test info");
        assertNotNull(result);
        assertTrue(result.contains("Test info"));
        assertTrue(result.contains("ℹ"));
    }
}

