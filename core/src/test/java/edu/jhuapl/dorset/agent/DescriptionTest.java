package edu.jhuapl.dorset.agent;

import static org.junit.Assert.*;

import org.junit.Test;

public class DescriptionTest {

    @Test
    public void testSetExample() {
        Description d = new Description();
        d.setExample("What is 1 + 1?");
        assertEquals(1, d.getExamples().length);
        assertEquals("What is 1 + 1?", d.getExamples()[0]);
    }

    @Test
    public void testSetExamples() {
        Description d = new Description();
        String[] examples = {"Where are you?", "What is this?"};
        d.setExamples(examples);
        assertEquals(2, d.getExamples().length);
        assertArrayEquals(examples, d.getExamples());
    }

}
