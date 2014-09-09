package scopes;

public class FirstBookingFacade implements BookingFacade {

    private BookingService bookingService;

    public FirstBookingFacade(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public BookingService getBookingService() {
        return bookingService;
    }

}
