package bigdata.decisiontree;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;

/**
 * Apache Spark Decision Tree Example
 *
 */
public class App {

	final static Integer numClasses = 2;
	final static String impurity = "entropy";
	final static Integer maxDepth = 20;
	final static Integer maxBins = 34;

	public static void main(String[] args) {
		System.setProperty("hadoop.home.dir", "C:\\programs\\hadoop-common-2.2.0-bin-master");
		JavaSparkContext javaSparkContext = new JavaSparkContext("local", "BikeBuyer");
		JavaRDD<String> bikeRdd = javaSparkContext.textFile("C:\\Users\\serkans\\Desktop\\data\\bikebuyers\\*");

		JavaRDD<LabeledPoint> data = bikeRdd.map(new Function<String, LabeledPoint>() {
			@Override
			public LabeledPoint call(String line) throws Exception {
				Bike bike = new Bike(line.split(","));
				double val = "Yes".equals(bike.getBikeBuyer()) ? 1.0 : 0.0;
				LabeledPoint LP = new LabeledPoint(val, bike.features());
				return LP;
			}
		});

		JavaRDD<LabeledPoint>[] split = data.randomSplit(new double[] { .9, .1 });
		JavaRDD<LabeledPoint> train = split[0].cache();
		JavaRDD<LabeledPoint> test = split[1].cache();

		final DecisionTreeModel dtree = DecisionTree.trainClassifier(train, numClasses, Bike.categoricalFeaturesInfo(),
				impurity, maxDepth, maxBins);

		test.take(10).forEach(x -> {
			System.out.println(String.format("Predicted: %.1f, Label: %.1f", dtree.predict(x.features()), x.label()));
		});

	}
}
