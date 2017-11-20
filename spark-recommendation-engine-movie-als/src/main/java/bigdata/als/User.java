package bigdata.als;

import java.io.Serializable;

public class User implements Serializable {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private String gender;
	private Integer age;
	private Integer occupation;
	private String zip;

	public User(Integer userId, String gender, Integer age, Integer occupation, String zip) {
		super();
		this.userId = userId;
		this.gender = gender;
		this.age = age;
		this.occupation = occupation;
		this.zip = zip;
	}
}