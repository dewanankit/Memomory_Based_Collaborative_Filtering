package Recommender;

import java.util.HashMap;
import java.util.List;


import DS.UserRatings;

public class UserNormalizer {
	HashMap<Integer,HashMap<Integer,Double>> userRatings = null;
	public UserNormalizer(HashMap<Integer,HashMap<Integer,Double>>userRatings)
	{
		this.userRatings = userRatings;
	}
	public HashMap<Integer, Double> getMeans(){
		HashMap<Integer, Double> means = new HashMap<>();
		HashMap<Integer, HashMap<Integer,Double>> userRatings = this.userRatings;
		for(int userID : userRatings.keySet()){
			HashMap<Integer,Double> listOfUserRatings = userRatings.get(userID);
			int count = 0;
			double sum = 0;
			for(Integer movieId : listOfUserRatings.keySet()){
				sum = sum + listOfUserRatings.get(movieId);
				count++;
			}
			if(count>0){
				means.put(userID, ((sum*1.0)/count));
			}
		}
		return means;
	}
	public HashMap<Integer,HashMap<Integer,Double>> normalizeByMean() {
		HashMap<Integer,Double> means= null;
		HashMap<Integer, HashMap<Integer,Double>> userRatings = this.userRatings;
		HashMap<Integer,HashMap<Integer,Double>> meanUpdatedUserRating = (HashMap<Integer,HashMap<Integer,Double>>)this.userRatings.clone();
		
		means = this.getMeans();
		for(int userID : userRatings.keySet()){
//			System.out.println(userRatings.get(userID).get(15));
			HashMap<Integer, Double> ratings = userRatings.get(userID);
			double mean =means.get(userID);
			for(int movieID : userRatings.get(userID).keySet()){
				ratings.put(movieID, ratings.get(movieID)- mean);
			}
			meanUpdatedUserRating.put(userID, ratings);
//			System.out.println(meanUpdatedUserRating.get(userID).get(15));
			//System.out.println(userRatings.get(userID).get(15));
		}
		return meanUpdatedUserRating;
	}

	
	public void normalizeByZScore() {
		
	}
	
}
