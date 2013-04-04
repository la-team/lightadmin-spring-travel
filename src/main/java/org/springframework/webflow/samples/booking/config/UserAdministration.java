package org.springframework.webflow.samples.booking.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.springframework.webflow.samples.booking.User;

@Administration( User.class )
public class UserAdministration {

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
			.filter( "Name", "name" )
			.filter( "Login", "username" ).build();
	}
}