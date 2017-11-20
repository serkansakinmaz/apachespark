package bigdata.als;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

import com.jasongoodwin.monads.Try;

public class RecommendationEngine {

	public static final String moviesPath = "C:\\Users\\serkans\\Desktop\\data\\ml-1m\\movies.dat";
	public static final String usersPath = "C:\\Users\\serkans\\Desktop\\data\\ml-1m\\users.dat";
	public static final String ratingsPath = "C:\\Users\\serkans\\Desktop\\data\\ml-1m\\ratings.dat";

	public static void main(String[] args) {

		JavaSparkContext jsc = new JavaSparkContext("local", "Recommendation Engine");

		/**
		 * Load Movie data
		 */
		JavaRDD<Movie> movieRDD = jsc.textFile(moviesPath).map(new Function<String, Movie>() {
			public Movie call(String line) throws Exception {
				String[] movieArr = line.split("::");
				Integer movieId = Integer.parseInt(Try.ofFailable(() -> movieArr[0]).orElse("-1"));
				return new Movie(movieId, movieArr[1], movieArr[2]);
			}
		}).cache();
		System.out.println(movieRDD.first());

		/**
		 * Load User data
		 */
		JavaRDD<User> userRDD = jsc.textFile(usersPath).map(new Function<String, User>() {
			@Override
			public User call(String line) throws Exception {
				String[] userArr = line.split("::");
				Integer userId = Integer.parseInt(Try.ofFailable(() -> userArr[0]).orElse("-1"));
				Integer age = Integer.parseInt(Try.ofFailable(() -> userArr[2]).orElse("-1"));
				Integer occupation = Integer.parseInt(Try.ofFailable(() -> userArr[3]).orElse("-1"));
				return new User(userId, userArr[1], age, occupation, userArr[4]);
			}
		}).cache();

		/**
		 * Load Rating data
		 */
		JavaRDD<Rating> ratingRDD = jsc.textFile(ratingsPath).map(new Function<String, Rating>() {
			@Override
			public Rating call(String line) throws Exception {
				String[] ratingArr = line.split("::");
				Integer userId = Integer.parseInt(Try.ofFailable(() -> ratingArr[0]).orElse("-1"));
				Integer movieId = Integer.parseInt(Try.ofFailable(() -> ratingArr[1]).orElse("-1"));
				Double rating = Double.parseDouble(Try.ofFailable(() -> ratingArr[2]).orElse("-1"));
				return new Rating(userId, movieId, rating);
			}
		}).cache();
		System.out.println("Total number of user : " + userRDD.count());
		System.out.println("Total number of rating : " + ratingRDD.count());

		/**
		 * Split rating into training and test
		 */
		JavaRDD<Rating>[] ratingSplits = ratingRDD.randomSplit(new double[] { 0.8, 0.2 });
		JavaRDD<Rating> trainingRatingRDD = ratingSplits[0].cache();
		JavaRDD<Rating> testRatingRDD = ratingSplits[1].cache();

		/**
		 * Create prediction model (Using ALS)
		 */
		ALS als = new ALS();
		MatrixFactorizationModel model = als.setRank(20).setIterations(10).run(trainingRatingRDD);

		/**
		 * Get the top 5 movie predictions for user 4169
		 */

		Rating[] recommendedsFor4169 = model.recommendProducts(4169, 5);

		System.out.println("Recommendations for 4169");

		for (Rating ratings : recommendedsFor4169) {
			System.out.println("Product id : " + ratings.product());
		}

	}
}