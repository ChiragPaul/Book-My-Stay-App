import java.util.ArrayList;
import java.util.List;

 class BookingHistory {


    private List<Reservation> confirmedReservations;


    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }


    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }


    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

class BookingReportService {


    public void generateReport(BookingHistory history) {
        System.out.println("Booking History Report:");

        for (Reservation reservation : history.getConfirmedReservations()) {
            System.out.println(
                    "Guest: " + reservation.getGuestName() +
                            ", Room Type: " + reservation.getRoomType()
            );
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // Create booking history
        BookingHistory history = new BookingHistory();

        // Add confirmed reservations
        history.addReservation(new Reservation("Abhi", "Single"));
        history.addReservation(new Reservation("Subha", "Double"));
        history.addReservation(new Reservation("Vamrathi", "Suite"));

        // Generate report
        BookingReportService reportService = new BookingReportService();

        System.out.println("Booking History and Reporting\n");
        reportService.generateReport(history);
    }
}

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}