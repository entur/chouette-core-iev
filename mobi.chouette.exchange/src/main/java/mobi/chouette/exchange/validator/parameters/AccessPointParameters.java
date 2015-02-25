package mobi.chouette.exchange.validator.parameters;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;
import mobi.chouette.model.AccessPoint;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class AccessPointParameters {

	@XmlTransient
	public static String[] fields = { "Objectid", "Name", "CityName", "CountryCode", "ZipCode"} ;
	
	static {
		ValidationParametersUtil.addFieldList(AccessPoint.class.getSimpleName(), Arrays.asList(fields));
	}

	@XmlElement(name = "objectid")
	private FieldParameters objectid;

	@XmlElement(name = "name")
	private FieldParameters name;

	@XmlElement(name = "city_name")
	private FieldParameters cityName;

	@XmlElement(name = "country_code")
	private FieldParameters countryCode;

	@XmlElement(name = "zip_code")
	private FieldParameters zipCode;

}
