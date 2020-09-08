package app.util.feature;

public class WordTokenTest {
	private String token;
	private String postag;
	private String lemma;
    public WordTokenTest(String token, String postag, String lemma) {
    	this.token = token;
    	this.lemma = lemma;
    	this.postag = postag;
    }
    
    public String toString() {
    	return "Token:"+token+" **Lemma:"+lemma+" ** Postag:"+postag;
    }
}
