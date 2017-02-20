package DS;

public class UserRatings {
	public UserRatings(int movieId, double rating) {
		super();
		this.movieId = movieId;
		this.rating = rating;
	}
	public int movieId;
	public double rating;
	
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}