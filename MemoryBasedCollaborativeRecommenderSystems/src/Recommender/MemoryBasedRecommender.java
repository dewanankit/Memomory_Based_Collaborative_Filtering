package Recommender;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import DS.MovieData;
import DS.MovieRatings;
import DS.UserRatings;
import DS.UserRelations;

public class MemoryBasedRecommender {
	public static HashMap<Integer, HashMap<Integer,Double>> predictMissingEntries(HashMap<Integer, HashMap<Integer,Double>> userRatings,HashMap<Integer, List<UserRelations>> nClosestRelations,Set<Integer> listOfMovies, HashMap<Integer, Double> meansOfMovieRatings){
		HashMap<Integer, HashMap<Integer,Double>> allRatingsForAllUsers = new HashMap<>();
		for(int uid : userRatings.keySet()){
			HashMap<Integer, Double> allRatingsForThisUser = new HashMap<>();
			List<UserRelations> correlation = nClosestRelations.get(uid);
			for(int mid : listOfMovies){
				double averageratingThisUserThisMovie = 0.0;
				double count = 0;
				for(UserRelations relation:correlation){
					if(userRatings.get(relation.getUserID()).get(mid) !=null){
						count = count + relation.getCorrelation();
						averageratingThisUserThisMovie = averageratingThisUserThisMovie + (relation.getCorrelation())*userRatings.get(relation.getUserID()).get(mid);
					}
				}
				if(count!=0)
					allRatingsForThisUser.put(mid, averageratingThisUserThisMovie/count);
				else{
					allRatingsForThisUser.put(mid, meansOfMovieRatings.get(mid));
				}
			}
			allRatingsForAllUsers.put(uid, allRatingsForThisUser);
		}
		return allRatingsForAllUsers;
	}
	public static void main(String[] args) {
		String base = "data\\ml-100k\\ua.base";
		MovieData movieDataBase = new MovieData(base);
		movieDataBase.readData(new File(base));
		String test = "data\\ml-100k\\ua.test";
		MovieData movieDataTest = new MovieData(test);
		movieDataTest.readData(new File(test));
		HashMap<Integer, HashMap<Integer,Double>> ratingsOfAllUsers = movieDataBase.getUserToMoviesRelation();
		HashMap<Integer, HashMap<Integer,Double>> ratingsForAllMovies = movieDataBase.getMoviesToUserRelation();
		HashMap<Integer, HashMap<Integer,Double>> NormalizedRatingForUser;
		HashMap<Integer, Double> meansOfMovieRatings = movieDataBase.getMeans(ratingsForAllMovies);
		UserNormalizer userNormalizer = new UserNormalizer(movieDataBase.createClone(ratingsOfAllUsers));
		HashMap<Integer, Double> means = userNormalizer.getMeans();
		NormalizedRatingForUser = userNormalizer.normalizeByMean();
		CorrelationMetrics correlations = new CorrelationMetrics();
		HashMap<Integer, HashMap<Integer,Double>> pearsonCorrelation = correlations.pearsonCorrelation(ratingsOfAllUsers);
		HashMap<Integer, List<UserRelations>> sortedCorrelation = correlations.sortedCorrelation(pearsonCorrelation);
		HashMap<Integer, List<UserRelations>> nClosestRelations = new HashMap<>();
		for(int uid : ratingsOfAllUsers.keySet()){
			List<UserRelations> relations =  correlations.nNearestNeighbor(942,sortedCorrelation,uid);
			nClosestRelations.put(uid, relations);
		}
		HashMap<Integer, HashMap<Integer,Double>> allRatingsForAllUsers = predictMissingEntries(ratingsOfAllUsers,nClosestRelations,ratingsForAllMovies.keySet(),meansOfMovieRatings);
		HashMap<Integer, HashMap<Integer,Double>> testRatingsOfAllUsers = movieDataTest.getUserToMoviesRelation();
		HashMap<Integer, HashMap<Integer,Double>> testRatingsForAllMovies = movieDataTest.getMoviesToUserRelation();
		int count1 = 0;
		int count2 = 0;
		Double predictedrating;
		for(int uid:testRatingsOfAllUsers.keySet()){
			HashMap<Integer,Double> testratingsForThisUser = testRatingsOfAllUsers.get(uid);
			for(int mid:testratingsForThisUser.keySet()){
			if (count2 <4045) {
				//				System.out.println("UID: "+uid+" mid: "+mid+" rating: "+ testratingsForThisUser.get(mid)+" "+ allRatingsForAllUsers.get(uid).get(mid));
				predictedrating = allRatingsForAllUsers.get(uid).get(mid);
				if (predictedrating > 0.25) {
					predictedrating = 1.0;
				} else if (predictedrating < -0.25) {
					predictedrating = -1.0;
				} else {
					predictedrating = 0.0;
				}
				double testRating = testratingsForThisUser.get(mid);
				if (predictedrating.equals(testRating)) {
					count1++;
				} 
			}
			else{
				predictedrating = allRatingsForAllUsers.get(uid).get(mid);
				if(predictedrating == null){
					predictedrating = 0.0;
				}
				if (predictedrating > 0.33) {
					predictedrating = 1.0;
				} else if (predictedrating < -0.33) {
					predictedrating = -1.0;
				} else {
					predictedrating = 0.0;
				}
				double testRating = testratingsForThisUser.get(mid);
				if (predictedrating.equals(testRating)) {
					count1++;
			}
			}
				count2++;
				System.out.println(count2);
			}	
		}
		System.out.println(count1);
		System.out.println(count2);
	}
}
