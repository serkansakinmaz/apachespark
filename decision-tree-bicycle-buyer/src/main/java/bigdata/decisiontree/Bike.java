package bigdata.decisiontree;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

public class Bike {
	private Long id; // 1
	private String maritalStatus; // 2
	private String gender;// 3
	private Float yearlyIncome;// 4
	private Integer totalChildren;// 5
	private String education;// 6
	private String occupation;// 7
	private String houseOwner;// 8
	private Integer numberCarsOwned;// 9
	private String commuteDistance;// 10
	private String region;// 11
	private Integer age;// 12
	private String bikeBuyer;// 13

	public Bike(Long id, String maritalStatus, String gender, Float yearlyIncome, Integer totalChildren,
			String education, String occupation, String houseOwner, Integer numberCarsOwned, String commuteDistance,
			String region, Integer age, String bikeBuyer) {
		super();
		this.id = id;
		this.maritalStatus = maritalStatus;
		this.gender = gender;
		this.yearlyIncome = yearlyIncome;
		this.totalChildren = totalChildren;
		this.education = education;
		this.occupation = occupation;
		this.houseOwner = houseOwner;
		this.numberCarsOwned = numberCarsOwned;
		this.commuteDistance = commuteDistance;
		this.region = region;
		this.age = age;
		this.bikeBuyer = bikeBuyer;
	}

	public Bike(String... row) {
		this(Long.parseLong(row[0]), row[1], row[2], Float.parseFloat(row[3]), Integer.parseInt(row[4]), row[5], row[6],
				row[7], Integer.parseInt(row[8]), row[9], row[10], Integer.parseInt(row[11]), row[12]);
	}

	public Vector features() {
		double[] features = new double[getClass().getDeclaredFields().length];
		features[0] = id.doubleValue();

		switch (maritalStatus) {
		case "Single":
			features[1] = 0d;
			break;
		case "Married":
			features[1] = 1d;
			break;
		default:
			break;
		}

		switch (gender) {
		case "Male":
			features[2] = 0d;
			break;
		case "Female":
			features[2] = 1d;
			break;
		default:
		}
		features[3] = yearlyIncome;
		features[4] = totalChildren.doubleValue();

		switch (education) {
		case "High School":
			features[5] = 0d;
			break;
		case "Partial High School":
			features[5] = 1d;
			break;
		case "Partial College":
			features[5] = 2d;
			break;
		case "Graduate Degree":
			features[5] = 3d;
			break;
		case "Bachelors":
			features[5] = 4d;
			break;
		default:
		}

		switch (occupation) {
		case "Professional":
			features[6] = 0d;
			break;
		case "Clerical":
			features[6] = 1d;
			break;
		case "Manual":
			features[6] = 2d;
			break;
		case "Management":
			features[6] = 3d;
			break;
		case "Skilled Manual":
			features[6] = 4d;
			break;
		default:
		}

		switch (houseOwner) {
		case "Yes":
			features[7] = 0d;
			break;
		case "No":
			features[7] = 1d;
			break;
		default:
		}

		features[8] = numberCarsOwned.doubleValue();

		switch (commuteDistance) {
		case "0-1 Miles":
			features[9] = 0d;
			break;
		case "1-2 Miles":
			features[9] = 1d;
			break;
		case "2-5 Miles":
			features[9] = 2d;
			break;
		case "5-10 Miles":
			features[9] = 3d;
			break;
		case "10+ Miles":
			features[9] = 4d;
			break;
		default:
		}

		switch (region) {
		case "North America":
			features[10] = 0d;
			break;
		case "Pacific":
			features[10] = 1d;
			break;
		case "Europe":
			features[10] = 2d;
			break;
		default:
		}

		features[11] = age.doubleValue();

		switch (bikeBuyer) {
		case "Yes":
			features[12] = 0d;
			break;
		case "No":
			features[12] = 1d;
			break;
		default:
		}
		return Vectors.dense(features);
	}

	public static Map<Integer, Integer> categoricalFeaturesInfo() {
		return new HashMap<Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put(1, 2);
				put(2, 2);
				put(5, 5);
				put(6, 5);
				put(7, 2);
				put(9, 5);
				put(10, 3);
				put(12, 2);
			}
		};
	}

	public Long getId() {
		return id;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public String getGender() {
		return gender;
	}

	public Float getYearlyIncome() {
		return yearlyIncome;
	}

	public Integer getTotalChildren() {
		return totalChildren;
	}

	public String getEducation() {
		return education;
	}

	public String getOccupation() {
		return occupation;
	}

	public String getHouseOwner() {
		return houseOwner;
	}

	public Integer getNumberCarsOwned() {
		return numberCarsOwned;
	}

	public String getCommuteDistance() {
		return commuteDistance;
	}

	public String getRegion() {
		return region;
	}

	public Integer getAge() {
		return age;
	}

	public String getBikeBuyer() {
		return bikeBuyer;
	}
	
	

}