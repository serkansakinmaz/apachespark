package bigdata.naivebayes;

import java.util.Arrays;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;

/**
 * Apache spark naive bayes text classification
 *
 */
public class App {

	public static final String DATA_PATH = "C:\\Users\\serkans\\Desktop\\data\\news\\";

	public static void main(String[] args) {
		System.setProperty("hadoop.home.dir", "C:\\programs\\hadoop-common-2.2.0-bin-master");
		JavaSparkContext sc = new JavaSparkContext("local", "NaiveBayes");

		JavaRDD<String> atheism = sc.textFile(DATA_PATH + "alt.atheism\\*");
		JavaRDD<String> graphics = sc.textFile(DATA_PATH + "comp.graphics\\*");
		JavaRDD<String> motorcycles = sc.textFile(DATA_PATH + "rec.motorcycles\\*");

		final HashingTF tf = new HashingTF(1000);

		JavaRDD<LabeledPoint> atheismExamples = atheism.map(new Function<String, LabeledPoint>() {
			public LabeledPoint call(String line) {
				return new LabeledPoint(5, tf.transform(Arrays.asList(line.split(" "))));
			}
		});
		JavaRDD<LabeledPoint> graphicsExamples = graphics.map(new Function<String, LabeledPoint>() {
			public LabeledPoint call(String line) {
				return new LabeledPoint(10, tf.transform(Arrays.asList(line.split(" "))));
			}
		});
		JavaRDD<LabeledPoint> motorcyclesExamples = motorcycles.map(new Function<String, LabeledPoint>() {
			public LabeledPoint call(String line) {
				return new LabeledPoint(15, tf.transform(Arrays.asList(line.split(" "))));
			}
		});

		JavaRDD<LabeledPoint> trainingData1 = atheismExamples.union(graphicsExamples);
		JavaRDD<LabeledPoint> trainingData2 = trainingData1.union(motorcyclesExamples);
		trainingData2.cache(); // Cache data since Logistic Regression is an iterative algorithm.
		final NaiveBayesModel model = NaiveBayes.train(trainingData2.rdd());

		Vector testAtheismWord = tf.transform(Arrays.asList(ContentTestData.atheismWord.split(" ")));
		Vector testCompGraphicsWord = tf.transform(Arrays.asList(ContentTestData.compGraphicsWord.split(" ")));
		Vector testMotorcyclesWord = tf.transform(Arrays.asList(ContentTestData.motorcyclesWord.split(" ")));
		
		System.out.println("Prediction for atheismWord : " + model.predict(testAtheismWord));
		System.out.println("Prediction for compGrapWord : " + model.predict(testCompGraphicsWord));
		System.out.println("Prediction for motorcyclesWord : " + model.predict(testMotorcyclesWord));

	}
}
