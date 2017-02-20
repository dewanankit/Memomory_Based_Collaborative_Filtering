package CorrelationClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import DS.MovieData;

public class PearsonCorrelation {
	public double calculatePearsonCorrelation(HashMap<Integer,Double>listOfThisUserRatings, HashMap<Integer,Double>listOfOtherUserRatings, double mean1, double mean2){
		List<Integer> commonMovies = new ArrayList<>();
		for(int movieID : listOfThisUserRatings.keySet()){
			if(listOfOtherUserRatings.containsKey(movieID)){
				commonMovies.add(movieID);
			}
		}
		Double thisSumOfDifferenceSqr = 0.0;
		Double otherSumOfDifferenceSqr = 0.0;
		Double sumOfCommonDiffProduct = 0.0;
		for(int movieId : commonMovies){
			sumOfCommonDiffProduct += (listOfThisUserRatings.get(movieId) - mean1)*(listOfOtherUserRatings.get(movieId) - mean2);
			thisSumOfDifferenceSqr += Math.pow(listOfThisUserRatings.get(movieId) - mean1,2);
			otherSumOfDifferenceSqr += Math.pow(listOfOtherUserRatings.get(movieId) - mean2,2);
		}
		if(sumOfCommonDiffProduct !=0 && thisSumOfDifferenceSqr!=0 &&otherSumOfDifferenceSqr!=0)
			return sumOfCommonDiffProduct/(Math.sqrt(thisSumOfDifferenceSqr*otherSumOfDifferenceSqr));
		else return 0;
	}
	
	public HashMap<Integer, HashMap<Integer,Double>> pearsonCorrelation(HashMap<Integer, HashMap<Integer,Double>> userRatings){
		Set<Integer> listOfUsers = userRatings.keySet();
		ArrayList<Integer> arrayOfUser = new ArrayList<>(listOfUsers);
		HashMap<Integer, HashMap<Integer,Double>> correlationMatrix = new HashMap<>();
		MovieData movieData = new MovieData();
		HashMap<Integer, Double> means = movieData.getMeans(userRatings);
		for(int userID : arrayOfUser){
			//arrayOfUser.remove(arrayOfUser.indexOf(userID));
			double mean1 = means.get(userID);
			HashMap<Integer,Double> ListOfThisUserRatings = userRatings.get(userID);
			for(int otherUsersID : arrayOfUser){
				double mean2 = means.get(otherUsersID);
				HashMap<Integer,Double> listOfOtherUserRatings = userRatings.get(otherUsersID);
				double correlation = calculatePearsonCorrelation(ListOfThisUserRatings,listOfOtherUserRatings,mean1,mean2);
				//System.out.println(userID+" "+otherUsersID+" "+correlation);
				HashMap<Integer, Double> correlationForUser;
				HashMap<Integer, Double> correlationForOtherUser;
				if(correlationMatrix.get(userID)!=null){
					correlationForUser = correlationMatrix.get(userID);
				}
				else{
					correlationForUser = new HashMap<>();
				}
				if(correlationMatrix.get(otherUsersID)!=null){
					correlationForOtherUser = correlationMatrix.get(otherUsersID);
				}
				else{
					correlationForOtherUser = new HashMap<>();
				}
				correlationForUser.put(otherUsersID, correlation);
				correlationMatrix.put(userID, correlationForUser);
				correlationForOtherUser.put(userID, correlation);
				correlationMatrix.put(otherUsersID, correlationForUser);
			}
		}
		return correlationMatrix;
	}
	
}
