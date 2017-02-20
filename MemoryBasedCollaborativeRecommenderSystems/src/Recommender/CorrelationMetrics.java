package Recommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import DS.MovieData;
import DS.UserRatings;
import DS.UserRelations;

public class CorrelationMetrics {
	
	public CorrelationMetrics(){
		
	}
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
	
	public HashMap<Integer, List<UserRelations>> sortedCorrelation(HashMap<Integer, HashMap<Integer,Double>> correlationMatrix)
	{
		HashMap<Integer, List<UserRelations>> sortedCorrelationMatrix = new HashMap<>();
		for(int userID : correlationMatrix.keySet()){
			List<UserRelations> sortedCorrelationVector = new ArrayList<>();
			HashMap<Integer, Double> userCorrelations = correlationMatrix.get(userID);
			for(int userID2: userCorrelations.keySet()){
				sortedCorrelationVector.add(new UserRelations(userID2,userCorrelations.get(userID2)));
			}
			Collections.sort(sortedCorrelationVector,new Comparator<UserRelations>() {
				public int compare(UserRelations lhs, UserRelations rhs){
					return lhs.getCorrelation() > rhs.getCorrelation() ? -1 : (lhs.getCorrelation() < rhs.getCorrelation() ) ? 1 : 0;
				}
			});
			sortedCorrelationMatrix.put(userID, sortedCorrelationVector);
		}
		return sortedCorrelationMatrix;
	}
	public ArrayList<UserRelations>nNearestNeighbor(int n, HashMap<Integer, List<UserRelations>> sortedCorrelationMatrix, int uid){
		return new ArrayList<>(sortedCorrelationMatrix.get(uid).subList(0, n));
	}
	public ArrayList<UserRelations>nNearestNeighborWithMovie(int n, HashMap<Integer, List<UserRelations>> sortedCorrelationMatrix, int uid, HashMap<Integer,HashMap<Integer, Double>> movieList){
		ArrayList<UserRelations> nearestNeighbors= new ArrayList<>();
		for(UserRelations correlation : sortedCorrelationMatrix.get(uid)){
			
		}
		return nearestNeighbors;
	}
}
