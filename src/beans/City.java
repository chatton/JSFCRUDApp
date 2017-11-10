package beans;


public class City {

	private String cityCode;
	private String countryCode;
	private String regCode;
	private String name;
	private long population;
	private boolean isCoastal;
	private double areaKm;
	
	public City() {}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getRegCode() {
		return regCode;
	}

	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	public boolean isCoastal() {
		return isCoastal;
	}

	public void setCoastal(boolean isCoastal) {
		this.isCoastal = isCoastal;
	}

	public double getAreaKm() {
		return areaKm;
	}

	public void setAreaKm(double areaKm) {
		this.areaKm = areaKm;
	}
	
	
	
}
