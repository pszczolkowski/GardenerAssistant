package pl.weeia.gardenerassistant.model;

public class GardenLocation {

	private String cityName;
	private String countryCode;

	public String getCityName() {
		return cityName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public GardenLocation(String cityName, String countryCode) {
		this.cityName = cityName;
		this.countryCode = countryCode;
	}

}
