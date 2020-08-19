package com.eki.aws.serverless.domain.user;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.eki.aws.serverless.DynamoDbKeyNames;
import com.eki.aws.serverless.dal.DynamoDBAdapter;

@DynamoDBTable(tableName = "aige-user")
public class UserEntity {
	private static final Logger LOG = LogManager.getLogger(UserEntity.class);

	private static final String USER_TABLE_NAME = System.getenv("CONFIG_USERS_TABLE");
	private DynamoDBAdapter db_adapter;
	private DynamoDBMapper mapper;
	private AmazonDynamoDB client;

	private String PK_PREFIX_VALUE = "user_";
	private String SK_VALUE = "profile";

	public UserEntity() {
		DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
				.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(USER_TABLE_NAME)).build();

		this.db_adapter = DynamoDBAdapter.getInstance();
		this.client = this.db_adapter.getDbClient();
		this.mapper = this.db_adapter.createDbMapper(mapperConfig);

	}

	private String pk;
	private String sk;
	private String username;
	private String firstname;
	private String lastname;
	private String address;
	private String city;
	private int zip;
	private String email;
	private String phone;
	private String mobile;
	private int admissionDate;
	private boolean isActive;
	private boolean isAdmin;

	@DynamoDBHashKey(attributeName = "PK")
	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	@DynamoDBRangeKey(attributeName = "SK")
	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	@DynamoDBAttribute(attributeName = "user_name")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@DynamoDBAttribute(attributeName = "first_name")
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@DynamoDBAttribute(attributeName = "last_name")
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@DynamoDBAttribute(attributeName = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@DynamoDBAttribute(attributeName = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@DynamoDBAttribute(attributeName = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DynamoDBAttribute(attributeName = "is_active")
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@DynamoDBAttribute(attributeName = "is_admin")
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@DynamoDBAttribute(attributeName = "zip")
	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	@DynamoDBAttribute(attributeName = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@DynamoDBAttribute(attributeName = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@DynamoDBAttribute(attributeName = "admission_date")
	public int getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(int admissionDate) {
		this.admissionDate = admissionDate;
	}

	@Override
	public String toString() {
		return "UserEntity [pk=" + pk + ", sk=" + sk + ", username=" + username + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", address=" + address + ", city=" + city + ", email=" + email + "]";
	}

	public List<UserEntity> list() throws IOException {
		LOG.info("UserEntity -list(): ");
		DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
		scanExp.addFilterCondition(DynamoDbKeyNames.SK.name(),
				new Condition().withComparisonOperator(ComparisonOperator.EQ)
						.withAttributeValueList(new AttributeValue().withS(SK_VALUE)));

		List<UserEntity> results = this.mapper.scan(UserEntity.class, scanExp);
		if (results.size() > 0) {
			LOG.info("Users - get():  result size: " + results.size());
			for (UserEntity p : results) {
				LOG.info("Users - list(): " + p.toString());
			}
		}
		return results;
	}

	public UserEntity get(String userName) throws IOException {
		LOG.info("UserEntity - get(): ");
		UserEntity user = null;

		UserEntity userKey = new UserEntity();
		userKey.setPk(PK_PREFIX_VALUE + userName);

		Condition rangeKeyCondition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
				.withAttributeValueList(new AttributeValue().withS(SK_VALUE));

		DynamoDBQueryExpression<UserEntity> queryExp = new DynamoDBQueryExpression<UserEntity>()
				.withHashKeyValues(userKey).withRangeKeyCondition(DynamoDbKeyNames.SK.name(), rangeKeyCondition);

		PaginatedQueryList<UserEntity> result = this.mapper.query(UserEntity.class, queryExp);

		if (result.size() > 0) {
			LOG.info("Users - get():  result size: " + result.size());
			user = result.get(0);
			LOG.info("Users - get(): user - " + user.toString());

		} else {
			LOG.info("Users - get(): user - Not Found.");
		}
		return user;
	}

	public void save(UserEntity user) throws IOException {
		user.setPk(PK_PREFIX_VALUE + user.getUsername());
		user.setSk(SK_VALUE);
		LOG.info("User - save(): " + user.toString());
		this.mapper.save(user);

	}

	public Boolean delete(String userName) throws IOException {

		UserEntity user = null;
		// get user if exists

		user = get(userName);
		if (user != null) {
			LOG.info("Users - delete(): " + user.toString());
			this.mapper.delete(user);
		} else {
			LOG.info("Users - delete(): user - does not exist.");
			return false;
		}
		return true;
	}
}