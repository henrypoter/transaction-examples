package scopes;


public class SecondBookingFacade implements BookingFacade {

    private BookingService bookingService;

    public SecondBookingFacade(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public BookingService getBookingService() {
        return bookingService;
    }

}
