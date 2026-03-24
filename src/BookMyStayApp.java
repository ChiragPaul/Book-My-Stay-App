import java.util.*;

class CancellationService {

    // Stack that stores recently released room IDs (LIFO rollback)
    private Stack<String> releasedRoomIds;

    // Maps reservation ID to room type
    private Map<String, String> reservationRoomTypeMap;

    // Constructor
    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }


    public void registerBooking(String reservationId, String roomType) {
        if (reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Reservation already exists.");
            return;
        }
        reservationRoomTypeMap.put(reservationId, roomType);
    }


    public void cancelBooking(String reservationId, RoomInventory inventory) {

        // Validate reservation
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid cancellation. Reservation not found.");
            return;
        }

        // Get room type
        String roomType = reservationRoomTypeMap.get(reservationId);

        // Push to rollback stack
        releasedRoomIds.push(reservationId + ":" + roomType);

        // Restore inventory
        inventory.incrementRoom(roomType);

        // Remove booking
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");

        if (releasedRoomIds.isEmpty()) {
            System.out.println("No cancellations yet.");
            return;
        }

        ListIterator<String> iterator = releasedRoomIds.listIterator(releasedRoomIds.size());
        while (iterator.hasPrevious()) {
            String record = iterator.previous();
            String[] parts = record.split(":");
            System.out.println("Released Reservation ID: " + parts[0] + " -> " + parts[1]);
        }
    }
}



 class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    public void incrementRoom(String roomType) {
        roomAvailability.put(roomType, roomAvailability.getOrDefault(roomType, 0) + 1);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 5);

        // Initialize service
        CancellationService service = new CancellationService();

        // Register a booking
        service.registerBooking("101", "Single");

        // Cancel booking
        service.cancelBooking("101", inventory);

        // Show rollback history
        service.showRollbackHistory();

        // Show updated inventory
        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getAvailability("Single"));
    }
}

