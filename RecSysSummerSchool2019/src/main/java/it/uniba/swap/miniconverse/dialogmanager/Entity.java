package it.uniba.swap.miniconverse.dialogmanager;

public class Entity {
	private String uri;
	private String label;
	private int rating;
	public Entity(String uri, String label, int rating) {
		super();
		this.uri = uri;
		this.label = label;
		this.rating = rating;
	}
	public String getUri() {
		return uri;
	}
	public String getLabel() {
		return label;
	}
	public int getRating() {
		return rating;
	}
	
	
}
