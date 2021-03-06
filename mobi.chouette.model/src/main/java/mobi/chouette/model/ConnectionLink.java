package mobi.chouette.model;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NaturalId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.converter.ConnectionLinkTypeToStringConverter;
import mobi.chouette.model.type.ConnectionLinkTypeEnum;
import mobi.chouette.model.type.UserNeedEnum;

/**
 * Chouette ConnectionLink : relation between 2 StopAreas
 * <p>
 * Neptune mapping : ConnectionLink <br>
 * Gtfs mapping : transfer
 * 
 */

@Entity
@Table(name = "connection_links",schema="public")
@NoArgsConstructor
@EqualsAndHashCode(of = { "objectId" }, callSuper = false)
@ToString
public class ConnectionLink extends ChouetteIdentifiedObject {
	private static final long serialVersionUID = 8490105295077539089L;

	@Getter
	@Setter
	@SequenceGenerator(name="connection_links_id_seq", sequenceName="public.connection_links_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="connection_links_id_seq")
	@Id
	@Column(name = "id", nullable = false)
	protected Long id;
	
	/**
	 * Neptune object id <br>
	 * composed of 3 items separated by a colon
	 * <ol>
	 * <li>prefix : an alphanumerical value (underscore accepted)</li>
	 * <li>type : a camelcase name describing object type</li>
	 * <li>technical id: an alphanumerical value (underscore and minus accepted)
	 * </li>
	 * </ol>
	 * This data must be unique in dataset
	 * 
	 * @return The actual value
	 */
	@Getter
	@NaturalId(mutable=true)
	@Column(name = "objectid", nullable = false, unique = true)
	protected String objectId;

	public void setObjectId(String value) {
		objectId = StringUtils.abbreviate(value, 255);
	}

	/**
	 * object version
	 * 
	 * @param objectVersion
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "object_version")
	protected Long objectVersion = 1L;

	/**
	 * name
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * set name <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setName(String value) {
		name = StringUtils.abbreviate(value, 255);
	}

	/**
	 * comment
	 * 
	 * @return The actual value
	 */
	@Getter
	@Column(name = "comment")
	private String comment;

	/**
	 * set comment <br>
	 * truncated to 255 characters if too long
	 * 
	 * @param value
	 *            New value
	 */
	public void setComment(String value) {
		comment = StringUtils.abbreviate(value, 255);
	}

	/**
	 * link length in meters
	 * 
	 * @param linkDistance
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "link_distance")
	private BigDecimal linkDistance;

	/**
	 * lift indicator <br>
	 * 
	 * <ul>
	 * <li>true if a lift is available on this link</li>
	 * <li>false if no lift is available on this link</li>
	 * </ul>
	 * 
	 * @param liftAvailable
	 *            New state for lift indicator
	 * @return The actual lift indicator
	 */
	@Getter
	@Setter
	@Column(name = "lift_availability")
	private Boolean liftAvailable = false;

	/**
	 * mobility restriction indicator (such as wheel chairs) <br>
	 * 
	 * <ul>
	 * <li>true if wheel chairs can follow this link</li>
	 * <li>false if wheel chairs can't follow this link</li>
	 * </ul>
	 * 
	 * @param mobilityRestrictedSuitable
	 *            New state for mobility restriction indicator
	 * @return The actual mobility restriction indicator
	 */
	@Getter
	@Setter
	@Column(name = "mobility_restricted_suitability")
	private Boolean mobilityRestrictedSuitable = false;

	/**
	 * stairs indicator <br>
	 * 
	 * <ul>
	 * <li>true if a stairs are presents on this link</li>
	 * <li>false if no stairs are presents on this link</li>
	 * </ul>
	 * 
	 * @param stairsAvailable
	 *            New state for stairs indicator
	 * @return The actual stairs indicator
	 */
	@Getter
	@Setter
	@Column(name = "stairs_availability")
	private Boolean stairsAvailable = false;

	/**
	 * medium time to follow the link <br>
	 * null if unknown
	 * 
	 * @param defaultDuration
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "default_duration")
	private Time defaultDuration;

	/**
	 * time to follow the link for a frequent traveller <br>
	 * null if unknown
	 * 
	 * @param frequentTravellerDuration
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "frequent_traveller_duration")
	private Time frequentTravellerDuration;

	/**
	 * time to follow the link for an occasional traveller <br>
	 * null if unknown
	 * 
	 * @param occasionalTravellerDuration
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "occasional_traveller_duration")
	private Time occasionalTravellerDuration;

	/**
	 * time to follow the link for a traveller with mobility restriction <br>
	 * null if unknown
	 * 
	 * @param mobilityRestrictedTravellerDuration
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	@Column(name = "mobility_restricted_traveller_duration")
	private Time mobilityRestrictedTravellerDuration;

	/**
	 * link type
	 * 
	 * @param linkType
	 *            New value
	 * @return The actual value
	 */
	@Getter
	@Setter
	// don't use Enumerated to manage blank database values
	@Convert(converter = ConnectionLinkTypeToStringConverter.class)
	// @Enumerated(EnumType.STRING)
	@Column(name = "link_type")
	private ConnectionLinkTypeEnum linkType;

	/**
	 * coded user needs as binary map<br>
	 * 
	 * use following methods for easier access :
	 * <ul>
	 * <li>getUserNeeds</li>
	 * <li>setUserNeeds</li>
	 * <li>addUserNeed</li>
	 * <li>addAllUserNeed</li>
	 * <li>removeUserNeed</li>
	 * </ul>
	 * 
	 * @param intUserNeeds
	 *            New value
	 * @return The actual value
	 */
	@Column(name = "int_user_needs")
	@Getter
	@Setter
	private Integer intUserNeeds = 0;

	/**
	 * return UserNeeds as Enum list
	 * 
	 * @return UserNeeds
	 */
	public List<UserNeedEnum> getUserNeeds() {
		List<UserNeedEnum> result = new ArrayList<>();
		if (intUserNeeds == null) return result;
		for (UserNeedEnum userNeed : UserNeedEnum.values()) {
			int mask = 1 << userNeed.ordinal();
			if ((this.intUserNeeds & mask) == mask) {
				result.add(userNeed);
			}
		}
		return result;
	}

	/**
	 * update UserNeeds as Enum list
	 * 
	 * @param userNeeds
	 */
	public void setUserNeeds(List<UserNeedEnum> userNeeds) {
		int value = 0;
		for (UserNeedEnum userNeed : userNeeds) {
			int mask = 1 << userNeed.ordinal();
			value |= mask;
		}
		this.intUserNeeds = value;
	}

	/**
	 * first stop area connected to link
	 * 
	 * @return The actual value
	 */
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "departure_id")
	private StopArea startOfLink;

	/**
	 * set startOfLink
	 * 
	 * @param stopArea
	 */
	public void setStartOfLink(StopArea stopArea) {
//		if (this.startOfLink != null) {
//			this.startOfLink.getConnectionStartLinks().remove(this);
//		}
		this.startOfLink = stopArea;
//		if (stopArea != null) {
//			stopArea.getConnectionStartLinks().add(this);
//		}
	}
	/**
	 * set stopArea without updating stopArea collection
	 * 
	 * @param stopArea
	 */
	public void forceStartOfLink(StopArea stopArea) {
		this.startOfLink = stopArea;
	}

	/**
	 * last stop area connected to link
	 * 
	 * @return The actual value
	 */
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "arrival_id")
	private StopArea endOfLink;

	/**
	 * set stopArea
	 * 
	 * @param stopArea
	 */
	public void setEndOfLink(StopArea stopArea) {
//		if (this.endOfLink != null) {
//			this.endOfLink.getConnectionEndLinks().remove(this);
//		}
		this.endOfLink = stopArea;
//		if (stopArea != null) {
//			stopArea.getConnectionEndLinks().add(this);
//		}
	}
	
	/**
	 * set stopArea without updating stopArea collection
	 * 
	 * @param stopArea
	 */
	public void forceEndOfLink(StopArea stopArea) {
		this.endOfLink = stopArea;
	}

}
