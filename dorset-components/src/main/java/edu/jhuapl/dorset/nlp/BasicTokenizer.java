package edu.jhuapl.dorset.nlp;

public class BasicTokenizer implements Tokenizer {

    @Override
    public String[] tokenize(String text) {
        return text.split("\\s+");
    }

}
