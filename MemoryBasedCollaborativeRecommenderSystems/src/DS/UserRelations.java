package DS;

public class UserRelations {
	int userID;
	double correlation;
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public double getCorrelation() {
		return correlation;
	}
	public void setCorrelation(double correlation) {
		this.correlation = correlation;
	}
	public UserRelations(int userID, double correlation){
		this.userID = userID;
		this.correlation = correlation;
	}
}
