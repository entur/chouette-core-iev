package mobi.chouette.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;

@Stateless
@Log4j
public class SchemaDAO {

	public static final String SQL = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA";
	
	@PersistenceContext(unitName = "public")
	EntityManager em;

	public List<String> getSchemaListing() {
		Query query = em.createNativeQuery(SQL);
		return query.getResultList();
	}
}
