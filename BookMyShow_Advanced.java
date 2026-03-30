package bookmyshow;

import java.util.*;

// ENUMS
enum SeatType { REGULAR, PREMIUM }
enum BookingStatus { CREATED, CONFIRMED, CANCELLED }

// MODELS
class User {
    int id;
    String name;
}

class Movie {
    int id;
    String title;
}

class Seat {
    int id;
    SeatType type;
    boolean isBooked;
}

class Screen {
    int id;
    List<Seat> seats;
}

class Show {
    int id;
    Movie movie;
    Screen screen;
    Map<Integer, Seat> seatMap;

    public Show(Screen screen, Movie movie) {
        this.screen = screen;
        this.movie = movie;
        this.seatMap = new HashMap<>();
        for (Seat s : screen.seats) seatMap.put(s.id, s);
    }
}

class Booking {
    int id;
    User user;
    Show show;
    List<Seat> seats;
    BookingStatus status;
}

// STRATEGY PATTERN
interface PaymentStrategy {
    void pay(double amount);
}

class UpiPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid via UPI: " + amount);
    }
}

// SERVICE LAYER
class BookingService {
    public synchronized Booking bookSeats(User user, Show show, List<Integer> seatIds, PaymentStrategy payment) {
        List<Seat> booked = new ArrayList<>();

        for (int id : seatIds) {
            Seat s = show.seatMap.get(id);
            if (s.isBooked) throw new RuntimeException("Seat already booked");
            s.isBooked = true;
            booked.add(s);
        }

        payment.pay(500);

        Booking booking = new Booking();
        booking.user = user;
        booking.show = show;
        booking.seats = booked;
        booking.status = BookingStatus.CONFIRMED;

        return booking;
    }
}
