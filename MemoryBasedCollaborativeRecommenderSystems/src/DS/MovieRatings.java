package DS;

public class MovieRatings {
	public MovieRatings(int userId, double rating) {
		super();
		this.userId = userId;
		this.rating = rating;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public int userId;
	public double rating;
}
