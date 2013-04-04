package org.springframework.webflow.samples.booking.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.scope.DomainTypePredicate;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnitBuilder;
import org.springframework.webflow.samples.booking.Hotel;

import static org.lightadmin.core.config.domain.scope.ScopeMetadataUtils.all;
import static org.lightadmin.core.config.domain.scope.ScopeMetadataUtils.filter;

@Administration( Hotel.class )
public class HotelAdministration {

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
			.filter( "Name", "name" )
			.filter( "Country", "country" )
			.filter( "City", "city" ).build();
	}

	public static ScopesConfigurationUnit scopes( final ScopesConfigurationUnitBuilder scopeBuilder ) {
		return scopeBuilder
			.scope( "All", all() ).defaultScope()
			.scope( "Low-cost", filter( lowCostHotels() ) )
			.scope( "Regular", filter( regularHotels() ) )
			.scope( "Luxury", filter( luxuryHotels() ) ).build();
	}

	public static DomainTypePredicate<Hotel> lowCostHotels() {
		return new DomainTypePredicate<Hotel>() {
			@Override
			public boolean apply( final Hotel hotel ) {
				return hotel.getPrice().intValue() < 100 ;
			}
		};
	}

	public static DomainTypePredicate<Hotel> regularHotels() {
		return new DomainTypePredicate<Hotel>() {
			@Override
			public boolean apply( final Hotel hotel ) {
				return 100 <= hotel.getPrice().intValue() && hotel.getPrice().intValue() <= 250 ;
			}
		};
	}

	public static DomainTypePredicate<Hotel> luxuryHotels() {
		return new DomainTypePredicate<Hotel>() {
			@Override
			public boolean apply( final Hotel hotel ) {
				return hotel.getPrice().intValue() > 250;
			}
		};
	}
}