package app.util.feature;

public class WordToken {
	private String postag;
	private String token;
	private String lemma;
	private Integer index;
	private Integer sentence;

	public WordToken(String token, String lemma, String postag, Integer index, Integer sentence) {
		this.token = token;
		this.lemma = lemma;
		this.postag = postag;
		this.setIndex(index);
		this.setSentence(sentence);
	}

	public String getPostag() {
		return postag;
	}

	public void setPostag(String postag) {
		this.postag = postag;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getSentence() {
		return sentence;
	}

	public void setSentence(Integer sentence) {
		this.sentence = sentence;
	}
	
	public String toString() {
		return ("token:"+token+" lemma:"+lemma+" tag:"+postag+" index:"+index);
	}

}
