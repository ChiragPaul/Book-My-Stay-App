import java.util.*;



class RoomAllocationService {

    // Stores all allocated room IDs to prevent duplicates
    private Set<String> allocatedRoomIds;


    private Map<String, Set<String>> assignedRoomsByType;


    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }


    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        // Check inventory availability
        if (!inventory.isAvailable(roomType)) {
            System.out.println("No rooms available for type: " + roomType);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Store globally
        allocatedRoomIds.add(roomId);

        // Store by room type
        assignedRoomsByType.putIfAbsent(roomType, new HashSet<>());
        assignedRoomsByType.get(roomType).add(roomId);

        // Update inventory
        inventory.decrement(roomType);

        // Confirm reservation
        System.out.println("Booking confirmed for guest: "
                + reservation.getGuestName()
                + ", Room ID: " + roomId);
    }


    private String generateRoomId(String roomType) {

        int counter = 1;
        String roomId;

        do {
            roomId = roomType + "-" + counter;
            counter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
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


class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public boolean isAvailable(String type) {
        return inventory.containsKey(type) && inventory.get(type) > 0;
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}


public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Suite", 1);

        // Booking request queue (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Abhi", "Single"));
        bookingQueue.add(new Reservation("Subha", "Single"));
        bookingQueue.add(new Reservation("Vamathi", "Suite"));

        // Allocation service
        RoomAllocationService allocationService = new RoomAllocationService();

        // Process queue
        while (!bookingQueue.isEmpty()) {
            Reservation reservation = bookingQueue.poll();
            allocationService.allocateRoom(reservation, inventory);
        }
    }
}