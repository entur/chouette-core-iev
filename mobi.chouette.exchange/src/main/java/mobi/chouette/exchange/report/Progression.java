
package mobi.chouette.exchange.report;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Progression {
	
	public enum STEP {
		INITIALISATION,
		PROCESSING,
		FINALISATION
	};
    @XmlElement( name = "step")
    private STEP step;
	
	@XmlAttribute(name = "total")
	private int total = 1;

	@XmlAttribute(name = "realized")
	private int realized = 0;


}
