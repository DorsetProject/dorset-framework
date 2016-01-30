package edu.jhuapl.dorset.nlp;

import static org.junit.Assert.*;

import org.junit.Test;

public class BasicTokenizerTest {

    @Test
    public void testTokenize() {
        String testString = "Today is the first day of the rest of your life.";
        Tokenizer tokenizer = new BasicTokenizer();
        
        String[] tokens = tokenizer.tokenize(testString);
        
        assertEquals("Today", tokens[0]);
        assertEquals("rest", tokens[7]);
    }

}
