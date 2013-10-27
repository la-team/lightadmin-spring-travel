package org.springframework.webflow.samples.booking.config;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.api.config.builder.FiltersConfigurationUnitBuilder;
import org.lightadmin.api.config.builder.ScopesConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.api.config.unit.ScopesConfigurationUnit;
import org.lightadmin.api.config.utils.DomainTypePredicate;
import org.springframework.webflow.samples.booking.Hotel;

import static org.lightadmin.api.config.utils.ScopeMetadataUtils.all;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.filter;

@SuppressWarnings("unused")
public class HotelAdministration extends AdministrationConfiguration<Hotel> {

    @Override
    public EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder.nameField("name").pluralName("Hotels").build();
    }

    @Override
    public FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder
                .filter("Name", "name")
                .filter("Country", "country")
                .filter("City", "city").build();
    }

    @Override
    public ScopesConfigurationUnit scopes(final ScopesConfigurationUnitBuilder scopeBuilder) {
        return scopeBuilder
                .scope("All", all()).defaultScope()
                .scope("Low-cost", filter(lowCostHotels()))
                .scope("Regular", filter(regularHotels()))
                .scope("Luxury", filter(luxuryHotels())).build();
    }

    private static DomainTypePredicate<Hotel> lowCostHotels() {
        return new DomainTypePredicate<Hotel>() {
            @Override
            public boolean apply(final Hotel hotel) {
                return hotel.getPrice().intValue() < 100;
            }
        };
    }

    private static DomainTypePredicate<Hotel> regularHotels() {
        return new DomainTypePredicate<Hotel>() {
            @Override
            public boolean apply(final Hotel hotel) {
                return 100 <= hotel.getPrice().intValue() && hotel.getPrice().intValue() <= 250;
            }
        };
    }

    private static DomainTypePredicate<Hotel> luxuryHotels() {
        return new DomainTypePredicate<Hotel>() {
            @Override
            public boolean apply(final Hotel hotel) {
                return hotel.getPrice().intValue() > 250;
            }
        };
    }
}