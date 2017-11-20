package bigdata.kmeans;

import java.util.List;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import scala.Tuple2;

public class AnomalyApp {

	public static final String kddDataPath = "C:\\Users\\serkans\\Desktop\\data\\kddcup.data_10_percent\\*";

	public static void main(String[] args) {

		JavaSparkContext jsc = new JavaSparkContext("local", "Anomaly Detection");

		/**
		 * Load data from local
		 */
		JavaRDD<Vector> kddRDD = jsc.textFile(kddDataPath).map(new Function<String, Vector>() {
			public Vector call(String line) throws Exception {
				String[] kddArr = line.split(",");
				double[] values = new double[37];
				for (int i = 0; i < 37; i++) {
					values[i] = Double.parseDouble(kddArr[i + 4]);
				}
				return Vectors.dense(values);
			}
		}).cache();

		System.out.println("KDD data row size : " + kddRDD.count());
		System.out.println("Example data : " + kddRDD.first());

		/**
		 * Normalize data with mean and standart deviation
		 */
		JavaDoubleRDD firstColumn = kddRDD.mapToDouble(new DoubleFunction<Vector>() {
			public double call(Vector t) throws Exception {
				return t.apply(0);
			}
		});

		final double mean = firstColumn.mean();
		final double stdev = firstColumn.stdev();

		System.out.println("Meaning value : " + mean + " Standard deviation :" + stdev + " Max : " + firstColumn.max()
				+ " Min : " + firstColumn.min());

		JavaRDD filteredKddRDD = kddRDD.filter(new Function<Vector, Boolean>() {
			public Boolean call(Vector v1) throws Exception {
				double src_bytes = v1.apply(0);
				if (src_bytes > (mean - 2 * stdev) && src_bytes < (mean + 2 * stdev)) {
					return true;
				}
				return false;
			}
		}).cache();

		/**
		 * Create Kmeans model
		 */

		final int numClusters = 10;
		final int numIterations = 20;
		final KMeansModel clusters = KMeans.train(filteredKddRDD.rdd(), numClusters, numIterations);

		/**
		 * Take cluster centers
		 */
		final Vector[] clusterCenters = clusters.clusterCenters();

		/**
		 * Calculates the distance between points and cluster center (centroid)
		 */

		JavaPairRDD<Double, Vector> vectorDistanceRdd = kddRDD.mapToPair(new PairFunction<Vector, Double, Vector>() {
			public Tuple2<Double, Vector> call(Vector point) throws Exception {
				/**
				 * Find the data centroid
				 */
				int centroidIndex = clusters.predict(point);
				Vector centroid = clusterCenters[centroidIndex];
				double preDis = 0;
				for (int i = 0; i < centroid.size(); i++) {
					preDis += Math.pow((centroid.apply(i) - point.apply(i)), 2);
				}
				double distance = Math.sqrt(preDis);

				return new Tuple2<Double, Vector>(distance, point);
			}
		});
		
		/**
		 * List and take the top ten point (They can be flagged as errors or potential attacks. )
		 */
		List<Tuple2<Double, Vector>> result = vectorDistanceRdd.sortByKey(false).take(10);
		
		// Print top ten points
		for (Tuple2<Double, Vector> tuple : result) {
			System.out.println("Anomaly data" + tuple._2());
		}

	}

}
