package org.springframework.webflow.samples.booking.config;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.api.config.unit.ScopesConfigurationUnit;
import org.lightadmin.api.config.utils.DomainTypePredicate;
import org.lightadmin.api.config.utils.DomainTypeSpecification;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.springframework.webflow.samples.booking.Booking;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.lang.String.format;
import static org.lightadmin.api.config.utils.EnumElement.element;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.*;

@SuppressWarnings("unused")
public class BookingAdministration extends AdministrationConfiguration<Booking> {

    @Override
    public EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder.nameExtractor(bookingNameExtractor()).pluralName("Bookings").build();
    }

    @Override
    public ScopesConfigurationUnit scopes(final ScopesConfigurationUnitBuilder scopeBuilder) {
        return scopeBuilder
                .scope("All", all()).defaultScope()
                .scope("Smoking Apartments", specification(smokingApartmentsSpec(true)))
                .scope("Non Smoking Apartments", specification(smokingApartmentsSpec(false)))
                .scope("Long-term bookings", filter(longTermBookingPredicate())).build();
    }

    @Override
    public FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder
                .filter("Customer", "user")
                .filter("Booked Hotel", "hotel")
                .filter("Check-In Date", "checkinDate").build();
    }

    @Override
    public FieldSetConfigurationUnit listView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("user").caption("Customer")
                .field("hotel").caption("Hotel")
                .field("checkinDate").caption("Check-In Date")
                .dynamic("nights").caption("Nights")
                .field("smoking").caption("Smoking")
                .field("beds").caption("Beds")
                .renderable(totalValueRenderer()).caption("Total")
                .dynamic("description").caption("Description")
                .field("beds").caption("Beds")
                .build();
    }

    @Override
    public FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("user").caption("Customer")
                .field("hotel").caption("Hotel")
                .field("checkinDate").caption("Check-In Date")
                .field("checkoutDate").caption("Check-Out Date")
                .field("beds").caption("Beds").enumeration(
                        element(1, "One king-size bed"),
                        element(2, "Two double beds"),
                        element(3, "Three beds")
                )
                .field("smoking").caption("Smoking")
                .field("creditCard").caption("Card Number")
                .field("creditCardName").caption("Card Name")
                .field("creditCardExpiryMonth").caption("Card Expiry Month").enumeration(
                        element(1, "Jan"),
                        element(2, "Feb"),
                        element(3, "Mar"),
                        element(4, "Apr"),
                        element(5, "May"),
                        element(6, "Jun"),
                        element(7, "Jul"),
                        element(8, "Aug"),
                        element(9, "Sep"),
                        element(10, "Oct"),
                        element(11, "Nov"),
                        element(12, "Dec"))
                .field("creditCardExpiryYear").caption("Card Expiry Year").enumeration(
                        element(1, "2008"),
                        element(2, "2009"),
                        element(3, "2010"),
                        element(4, "2011"),
                        element(5, "2012"))
                .build();
    }

    private static DomainTypePredicate<Booking> longTermBookingPredicate() {
        return new DomainTypePredicate<Booking>() {
            @Override
            public boolean apply(final Booking booking) {
                return booking.getNights() > 20;
            }
        };
    }

    private static DomainTypeSpecification<Booking> smokingApartmentsSpec(final boolean isSmokingApartment) {
        return new DomainTypeSpecification<Booking>() {
            @Override
            public Predicate toPredicate(final Root<Booking> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
                return cb.equal(root.get("smoking"), isSmokingApartment);
            }
        };
    }

    private static EntityNameExtractor<Booking> bookingNameExtractor() {
        return new EntityNameExtractor<Booking>() {
            @Override
            public String apply(final Booking booking) {
                return format("Booking %s for $%d", booking.getHotel().getName(), booking.getTotal().intValue());
            }
        };
    }

    private static FieldValueRenderer<Booking> totalValueRenderer() {
        return new FieldValueRenderer<Booking>() {
            @Override
            public String apply(final Booking booking) {
                return format("\u20AC %s", booking.getTotal());
            }
        };
    }
}
