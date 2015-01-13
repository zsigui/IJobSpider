package com.ijob.spider.model;

public class Organization {
	private int id;
	private String name;
	private String industry;
	private String natrue;
	private String scale;
	private String registeredCapital;
	private String realCapital;
	private String type;
	private String website;
	private String description;
	private String address;
	private String postalCode;
	private String contacts;
	private String email;
	private String pic = "default_cover.jpg";
	private int scanNum = 0;

	public Organization() {
	}

	public Organization(int id, String name, String industry, String natrue,
			String scale, String registeredCapital, String realCapital,
			String type, String website, String description, String address,
			String postalCode, String contacts, String email, String pic,
			int scanNum) {
		this.id = id;
		this.name = name;
		this.industry = industry;
		this.natrue = natrue;
		this.scale = scale;
		this.registeredCapital = registeredCapital;
		this.realCapital = realCapital;
		this.type = type;
		this.website = website;
		this.description = description;
		this.address = address;
		this.postalCode = postalCode;
		this.contacts = contacts;
		this.email = email;
		this.pic = pic;
		this.scanNum = scanNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getNatrue() {
		return natrue;
	}

	public void setNatrue(String natrue) {
		this.natrue = natrue;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public String getRealCapital() {
		return realCapital;
	}

	public void setRealCapital(String realCapital) {
		this.realCapital = realCapital;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getScanNum() {
		return scanNum;
	}

	public void setScanNum(int scanNum) {
		this.scanNum = scanNum;
	}

}
