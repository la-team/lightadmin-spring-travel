package org.springframework.webflow.samples.booking;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * A Hotel Booking made by a User.
 */
@Entity
@BookingDateRange
public class Booking implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

	@ManyToOne
    private User user;

	@ManyToOne
    private Hotel hotel;

	@Basic
	@Temporal(TemporalType.DATE)
	@NotNull
	@Future
	@DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date checkinDate;

	@Basic
	@Temporal(TemporalType.DATE)
	@NotNull
	@Future
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date checkoutDate;

	@NotEmpty
    private String creditCard;

	@NotEmpty
    private String creditCardName;

    private int creditCardExpiryMonth;

    private int creditCardExpiryYear;

    private boolean smoking;

    private int beds;

	@Transient
    private Set<Amenity> amenities;

    public Booking() {
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DAY_OF_MONTH, 1);
	setCheckinDate(calendar.getTime());
	calendar.add(Calendar.DAY_OF_MONTH, 1);
	setCheckoutDate(calendar.getTime());
    }

    public Booking(Hotel hotel, User user) {
	this();
	this.hotel = hotel;
	this.user = user;
    }

    @Transient
    public BigDecimal getTotal() {
	return hotel.getPrice().multiply(new BigDecimal(getNights()));
    }

    @Transient
    public int getNights() {
	if (checkinDate == null || checkoutDate == null) {
	    return 0;
	} else {
	    return (int) ((checkoutDate.getTime() - checkinDate.getTime()) / 1000 / 60 / 60 / 24);
	}
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Date getCheckinDate() {
	return checkinDate;
    }

    public void setCheckinDate(Date datetime) {
	this.checkinDate = datetime;
    }

    public Hotel getHotel() {
	return hotel;
    }

    public void setHotel(Hotel hotel) {
	this.hotel = hotel;
    }


    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Date getCheckoutDate() {
	return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
	this.checkoutDate = checkoutDate;
    }

    public String getCreditCard() {
	return creditCard;
    }

    public void setCreditCard(String creditCard) {
	this.creditCard = creditCard;
    }

    @Transient
    public String getDescription() {
	DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
	return hotel == null ? null : hotel.getName() + ", " + df.format(getCheckinDate()) + " to "
		+ df.format(getCheckoutDate());
    }

    public boolean isSmoking() {
	return smoking;
    }

    public void setSmoking(boolean smoking) {
	this.smoking = smoking;
    }

    public int getBeds() {
	return beds;
    }

    public void setBeds(int beds) {
	this.beds = beds;
    }

    public String getCreditCardName() {
	return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
	this.creditCardName = creditCardName;
    }

    public int getCreditCardExpiryMonth() {
	return creditCardExpiryMonth;
    }

    public void setCreditCardExpiryMonth(int creditCardExpiryMonth) {
	this.creditCardExpiryMonth = creditCardExpiryMonth;
    }

    public int getCreditCardExpiryYear() {
	return creditCardExpiryYear;
    }

    public void setCreditCardExpiryYear(int creditCardExpiryYear) {
	this.creditCardExpiryYear = creditCardExpiryYear;
    }


    public Set<Amenity> getAmenities() {
	return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
	this.amenities = amenities;
    }

    private Date today() {
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DAY_OF_MONTH, -1);
	return calendar.getTime();
    }

    @Override
    public String toString() {
	return "Booking(" + user + "," + hotel + ")";
    }

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}

		if ( this.id == null || obj == null || !( this.getClass().equals( obj.getClass() ) ) ) {
			return false;
		}

		Booking that = ( Booking ) obj;

		return this.id.equals( that.getId() );
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}
}