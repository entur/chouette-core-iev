package mobi.chouette.util;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class DefaultTenantIdentifierResolver implements
		CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		return ContextHolder.getContext();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return false;
	}

}
