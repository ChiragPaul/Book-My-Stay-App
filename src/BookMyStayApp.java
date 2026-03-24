import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

 class ReservationValidator {


    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (roomType == null) {
            throw new InvalidBookingException("Room type cannot be null.");
        }

        // Case-sensitive validation as per requirement
        if (!roomType.equals("Single") &&
                !roomType.equals("Double") &&
                !roomType.equals("Suite")) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Validate inventory availability
        int availableRooms = inventory.getAvailableRooms(roomType);

        if (availableRooms <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for selected type: " + roomType
            );
        }
    }
}




public class BookMyStayApp {

    public static void main(String[] args) {

        // Display application header
        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Collect input
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validate input (fail-fast)
            validator.validate(guestName, roomType, inventory);

            // If validation passes
            System.out.println("Booking request is valid.");

            // (Optional) Add to queue
            bookingQueue.addRequest(guestName, roomType);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}



 class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room counts
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }


    public int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }


    public void bookRoom(String roomType) {
        int count = getAvailableRooms(roomType);
        if (count > 0) {
            inventory.put(roomType, count - 1);
        }
    }
}


class BookingRequestQueue {

    private Queue<String> queue = new LinkedList<>();

    public void addRequest(String guestName, String roomType) {
        queue.offer(guestName + " - " + roomType);
    }
}

