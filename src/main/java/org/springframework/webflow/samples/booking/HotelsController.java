package org.springframework.webflow.samples.booking;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static org.apache.commons.io.IOUtils.write;

@Controller
public class HotelsController {

    private BookingService bookingService;

    @Autowired
    public HotelsController(BookingService bookingService) {
	this.bookingService = bookingService;
    }

    @RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
    public void search(SearchCriteria searchCriteria, Principal currentUser, Model model) {
	if (currentUser != null) {
	    List<Booking> booking = bookingService.findBookings(currentUser.getName());
	    model.addAttribute(booking);
	}
    }

    @RequestMapping(value = "/hotels", method = RequestMethod.GET)
    public String list(SearchCriteria criteria, Model model) {
	List<Hotel> hotels = bookingService.findHotels(criteria);
	model.addAttribute(hotels);
	return "hotels/list";
    }

    @RequestMapping(value = "/hotels/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id, Model model) {
	model.addAttribute(bookingService.findHotelById(id));
	return "hotels/show";
    }

    @RequestMapping(value = "/bookings/{id}", method = RequestMethod.DELETE)
    public String deleteBooking(@PathVariable Long id) {
	bookingService.cancelBooking(id);
	return "redirect:../hotels/search";
    }

	@RequestMapping(value = "/hotels/{id}/picture", method = RequestMethod.GET)
	public void hotelPicture( HttpServletResponse response, @PathVariable Long id ) throws IOException {
		final Hotel hotel = bookingService.findHotelById( id );
		final byte[] picture = hotel.getPicture();
		if ( picture != null ) {
			final MediaType mediaType = getMediaType( picture );
			response.setContentLength( picture.length );
			response.setContentType( mediaType.toString() );
			write( picture, response.getOutputStream() );
			response.flushBuffer();
		}
	}

	private MediaType getMediaType( final byte[] bytes ) throws IOException {
		ContentHandlerDecorator contentHandler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		Parser parser = new AutoDetectParser();
		try {
			final ParseContext parseContext = new ParseContext();
			parser.parse( new ByteArrayInputStream( bytes ), contentHandler, metadata, parseContext );
			return MediaType.parseMediaType( metadata.get( "Content-Type" ) );
		} catch ( Exception e ) {
			return MediaType.IMAGE_JPEG;
		}
	}
}