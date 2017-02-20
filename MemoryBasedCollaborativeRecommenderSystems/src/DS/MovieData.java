package DS;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class MovieData implements Serializable{
	public HashMap<Integer, HashMap<Integer,Double>> userToMoviesRelation;
	public HashMap<Integer, HashMap<Integer,Double>> moviesToUserRelation;
	public HashMap<Integer, Long> sumByCust;
	public HashMap<Integer, Long> sumByMovie;
	File movieDataFile;


	public HashMap<Integer, HashMap<Integer,Double>> getUserToMoviesRelation() {
		return userToMoviesRelation;
	}
	
	public HashMap<Integer, HashMap<Integer,Double>> getMoviesToUserRelation() {
		return moviesToUserRelation;
	}

	public HashMap<Integer, Long> getSumByCust() {
		return sumByCust;
	}

	public HashMap<Integer, Long> getSumByMovie() {
		return sumByMovie;
	}

	public File getMovieDataFile() {
		return movieDataFile;
	}

	public void addCustomerToMovieRelation(int uid, int mid, int rating){
		HashMap<Integer,Double> listOfUserRatings;
		if(userToMoviesRelation.containsKey(uid)){
			listOfUserRatings = userToMoviesRelation.get(uid);
			sumByCust.put(uid, sumByCust.get(uid)+rating);
		}
		else{
			listOfUserRatings = new HashMap<Integer,Double>();
			sumByCust.put(uid, new Long(rating));
		}
		if(rating>3){
			rating = 1;
		}
		else if(rating<3){
			rating = -1;
		}
		else{
			rating = 0;
		}
		listOfUserRatings.put(mid, (double)rating);
		userToMoviesRelation.put(uid, listOfUserRatings);
		//System.out.println("added uid: "+uid+" mid: "+ mid +" rating: "+rating);
	}
	
	public void addMovieToCustomerRelation(int uid, int mid, int rating){
		HashMap<Integer,Double> listOfMovieRatings;
		if(moviesToUserRelation.containsKey(mid)){
			listOfMovieRatings = moviesToUserRelation.get(mid);
			sumByMovie.put(mid, sumByMovie.get(mid)+rating);
		}
		else{
			listOfMovieRatings = new HashMap<Integer,Double>();
			sumByMovie.put(mid, new Long(rating));
		}
		if(rating>3){
			rating = 1;
		}
		else if(rating<3){
			rating = -1;
		}
		else{
			rating = 0;
		}
		listOfMovieRatings.put(uid, (double)rating);
		moviesToUserRelation.put(mid, listOfMovieRatings);
	}
	
	public void readData(File movieDataFile){
		try {
			BufferedReader br = new BufferedReader(new FileReader(movieDataFile));
			String line;
			while((line=br.readLine())!=null){
				String[] fields = line.split("	");
				int uid, mid, rating;
				uid = Integer.parseInt(fields[0]);
				mid = Integer.parseInt(fields[1]);
				rating = Integer.parseInt(fields[2]);
				addCustomerToMovieRelation(uid, mid, rating);
				addMovieToCustomerRelation(uid, mid, rating);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MovieData(String fileName){
		sumByCust = new HashMap<>();
		sumByMovie = new HashMap<>();
		userToMoviesRelation = new HashMap<>();
		moviesToUserRelation = new HashMap<>();
		this.movieDataFile = new File(fileName);
	}
	
	public void writeToObject(String fileName, MovieData movieData){
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
			os.writeObject(movieData);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MovieData readFromObject(String fileName){
		MovieData movieData = null;
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(fileName)));
			movieData = (MovieData)ois.readObject();;
			ois.close();
		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		return movieData;
	}
	public HashMap<Integer, HashMap<Integer,Double>> createClone(HashMap<Integer, HashMap<Integer,Double>> data){
		HashMap<Integer, HashMap<Integer,Double>> clonedData = new HashMap<>();
		for(int uid : data.keySet()){
			HashMap<Integer, Double> innerClonedData= new HashMap<>();
			for(int mid : data.get(uid).keySet()){
				innerClonedData.put(mid, new Double(data.get(uid).get(mid)));
			}
			clonedData.put(uid, innerClonedData);
		}
		return clonedData;
	}
	
	public MovieData(){
		
	}

	public Set<Integer> UserList(){
		return this.userToMoviesRelation.keySet();
	}
	
	public Set<Integer> MovieIDList(){
		return this.moviesToUserRelation.keySet();
	}
	
	public HashMap<Integer, Double> getMeans(HashMap<Integer, HashMap<Integer,Double>> ratings){
		HashMap<Integer, Double> means = new HashMap<>();
		for(int id : ratings.keySet()){
			HashMap<Integer,Double> listOfRatings = ratings.get(id);
			int count = 0;
			double sum = 0;
			for(Integer movieId : listOfRatings.keySet()){
				sum = sum + listOfRatings.get(movieId);
				count++;
			}
			if(count>0){
				means.put(id, ((sum*1.0)/count));
			}
		}
		return means;
	}

	public static void main(String[] args) {
		String source = "data\\ml-100k\\u.data";
		MovieData data= new MovieData(source);
		File f = new File(source);
		data.readData(f);
	}
}