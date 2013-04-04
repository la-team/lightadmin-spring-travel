package org.springframework.webflow.samples.booking.config;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.scope.DomainTypePredicate;
import org.lightadmin.core.config.domain.scope.DomainTypeSpecification;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnitBuilder;
import org.springframework.webflow.samples.booking.Booking;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.lightadmin.core.config.domain.scope.ScopeMetadataUtils.*;

@Administration( Booking.class )
public class BookingAdministration {

	public static ScopesConfigurationUnit scopes( final ScopesConfigurationUnitBuilder scopeBuilder ) {
		return scopeBuilder
			.scope( "All", all() ).defaultScope()
			.scope( "Smoking Apartments", specification( smokingApartmentsSpec( true ) ) )
			.scope( "Non Smoking Apartments", specification( smokingApartmentsSpec( false ) ) )
			.scope( "Long-term bookings", filter( longTermBookingPredicate() ) ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
			.filter( "Customer", "user" )
			.filter( "Booked Hotel", "hotel" )
			.filter( "Check In Date", "checkinDate" ).build();
	}

	public static DomainTypePredicate<Booking> longTermBookingPredicate() {
		return new DomainTypePredicate<Booking>() {
			@Override
			public boolean apply( final Booking booking ) {
				return Days.daysBetween( new DateTime( booking.getCheckinDate().getTime() ), new DateTime( booking.getCheckoutDate().getTime() ) ).getDays() > 20;
			}
		};
	}

	public static DomainTypeSpecification<Booking> smokingApartmentsSpec( final boolean isSmokingApartment ) {
		return new DomainTypeSpecification<Booking>() {
			@Override
			public Predicate toPredicate( final Root<Booking> root, final CriteriaQuery<?> query, final CriteriaBuilder cb ) {
				return cb.equal( root.get( "smoking" ), isSmokingApartment );
			}
		};
	}
}