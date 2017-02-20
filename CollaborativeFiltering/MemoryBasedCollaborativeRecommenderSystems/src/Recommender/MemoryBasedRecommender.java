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
	public HashMap<Integer, HashMap<Integer,Double>> predictMissingEntries(HashMap<Integer, HashMap<Integer,Double>> userRatings,HashMap<Integer, List<UserRelations>> nClosestRelations,List<Integer> listOfMovies){
		HashMap<Integer,HashMap<Integer,Double>> allRatingsForAllUsers = new HashMap<>();
		for(int uid : userRatings.keySet()){
			HashMap<Integer, Double> allRatingsForThisUser = new HashMap<>();
			List<Double> correlation = null;
			for(int mid : listOfMovies){
				
			}
		}
		return allRatingsForAllUsers;
	}
	public static void main(String[] args) {
		String base = "data\\ml-100k\\ua.base";
		MovieData movieDataBase = new MovieData(base);
		String test = "data\\ml-100k\\ua.test";
		MovieData movieDataTest = new MovieData(test);
		movieDataTest.readData(new File(test));
		HashMap<Integer, HashMap<Integer,Double>> ratingsOfAllUsers = movieDataBase.getUserToMoviesRelation();
		HashMap<Integer, HashMap<Integer,Double>> ratingsForAllMovies = movieDataBase.getMoviesToUserRelation();
		HashMap<Integer, HashMap<Integer,Double>> NormalizedRatingForUser;
		UserNormalizer userNormalizer = new UserNormalizer(movieDataBase.createClone(ratingsOfAllUsers));
		HashMap<Integer, Double> means = userNormalizer.getMeans();
		NormalizedRatingForUser = userNormalizer.normalizeByMean();
		CorrelationMetrics correlations = new CorrelationMetrics();
		HashMap<Integer, HashMap<Integer,Double>> pearsonCorrelation = correlations.pearsonCorrelation(ratingsOfAllUsers);
		HashMap<Integer, List<UserRelations>> sortedCorrelation = correlations.sortedCorrelation(pearsonCorrelation);
		HashMap<Integer, List<UserRelations>> nClosestRelations = new HashMap<>();
	}
}
