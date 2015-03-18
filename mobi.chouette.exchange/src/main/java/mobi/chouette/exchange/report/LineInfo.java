package mobi.chouette.exchange.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"name","status","stats"})
@Data
public class LineInfo {

	@XmlType(name="lineState")
	@XmlEnum
	public enum LINE_STATE 
	{
		OK,
		WARNING,
		ERROR
	};
	@XmlElement(name = "name",required=true)
	private String name;

	@XmlElement(name = "status",required=true)
	private LINE_STATE status;
	
	@XmlElement(name = "stats")
	private LineStats stats;
	

}