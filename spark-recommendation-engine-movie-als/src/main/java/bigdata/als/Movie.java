package bigdata.als;

import java.io.Serializable;

public class Movie implements Serializable {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;
	private Integer movieId;
	private String title;
	private String genres;

	public Movie(Integer movieId, String title, String genres) {
		super();
		this.movieId = movieId;
		this.title = title;
		this.genres = genres;
	}
}