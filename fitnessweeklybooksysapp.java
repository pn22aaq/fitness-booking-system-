import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
public class fitnessweeklybooksysapp 
{
    public fitnessweeklybooksysapp(Map<String, Integer> prices,
            Map<String, List<fitnessweeklybooksysapp.Lesson>> timetable, Map<String, Set<String>> bookings,
            Map<String, Map<String, int[]>> lessonStats, int numWeekends) {
        this.prices = prices;
        this.timetable = timetable;
        this.bookings = bookings;
        this.lessonStats = lessonStats;
        this.numWeekends = numWeekends;
    }

    private Map<String, Integer> prices = new HashMap<>(); // maps fitness type to price
    private Map<String, List<Lesson>> timetable = new HashMap<>(); // maps day/fitness type to lessons
    private Map<String, Set<String>> bookings = new HashMap<>(); // maps customer name to set of booked lessons
    private Map<String, Map<String, int[]>> lessonStats = new HashMap<>(); // maps lesson key to stats (bookings, ratings)
    private int numWeekends = 0;
    
    private static class Lesson {
        String day;
        String type;
        int maxCapacity;
        List<String> customers;
        List<Integer> ratings;
        
        public Lesson(String day, String type, int price, int maxCapacity) {
            this.day = day;
            this.type = type;
            this.maxCapacity = maxCapacity;
            this.customers = new ArrayList<>();
            this.ratings = new ArrayList<>();
        }
        
        public String getKey() {
            return day + "-" + type;
        }
        
        public boolean isFull() {
            return customers.size() >= maxCapacity;
        }
    }
    
    public fitnessweeklybooksysapp() {
        // initialize prices
        prices.put("Spin", 10);
        prices.put("Yoga", 12);
        prices.put("Bodysculpt", 15);
        prices.put("Zumba", 10);
        prices.put("Aquacise", 8);
        prices.put("Box Fit", 12);
        
        // initialize timetable
        for (String day : new String[] {"Saturday", "Sunday"}) {
            timetable.put(day, new ArrayList<>());
            for (String type : new String[] {"Spin", "Yoga", "Bodysculpt", "Zumba", "Aquacise", "Box Fit"}) {
                Lesson lesson1 = new Lesson(day, type, prices.get(type), 5);
                Lesson lesson2 = new Lesson(day, type, prices.get(type), 5);
                timetable.get(day).add(lesson1);
                timetable.get(day).add(lesson2);
            }
        }
    }
    
    private Lesson findLesson(String day, String type, int index) {
        List<Lesson> lessons = timetable.get(day);
        for (Lesson lesson : lessons) {
            if (lesson.type.equals(type) && index-- == 0) {
                return lesson;
            }
        }
        return null;
    }
    
    private void bookLesson(String customer, String day, String type, int index) {
        Set<String> customerBookings = bookings.getOrDefault(customer, new HashSet<>());
        Lesson lesson = findLesson(day, type, index);
        if (lesson == null) {
            System.out.println("Invalid lesson.");
        } else if (customerBookings.contains(lesson.getKey())) {
            System.out.println("You have already booked this lesson.");
        } else if (lesson.isFull()) {
            System.out.println("This lesson is already full.");
        } else {
            customerBookings.add(lesson.getKey());
            lesson.customers.add(customer);
            bookings.put(customer, customerBookings);
            lessonStats.get(lesson.getKey()).get("bookings")[numWeekends]++;
            System.out.println("Lesson booked successfully.");
        }
    }
    
    private void changeBooking(String customer, String oldKey, String newDay, String newType, int index)
    {
        Set<String> customerBookings = bookings.getOrDefault(customer, new HashSet<>());
        Lesson oldLesson = findLesson(oldKey.split("-")[0], oldKey.split("-")[1], Integer.parseInt(oldKey.split("-")[2]));
        Lesson newLesson = findLesson(newDay, newType, index);
        if (oldLesson == null || newLesson == null) {
        System.out.println("Invalid lesson.");
        } else if (!customerBookings.contains(oldKey)) {
        System.out.println("You have not booked this lesson.");
        } else if (customerBookings.contains(newLesson.getKey())) {
        System.out.println("You have already booked another lesson at this time.");
        } else if (newLesson.isFull()) {
        System.out.println("This lesson is already full.");
        } else {
        customerBookings.remove(oldKey);
        customerBookings.add(newLesson.getKey());
        oldLesson.customers.remove(customer);
        newLesson.customers.add(customer);
        bookings.put(customer, customerBookings);
        lessonStats.get(oldKey).get("bookings")[numWeekends]--;
        lessonStats.get(newLesson.getKey()).get("bookings")[numWeekends]++;
        System.out.println("Booking changed successfully.");
        }
        }
        private void rateLesson(String customer, String key, int rating) {
            Lesson lesson = findLesson(key.split("-")[0], key.split("-")[1], Integer.parseInt(key.split("-")[2]));
            if (lesson == null) {
                System.out.println("Invalid lesson.");
            } else if (!lesson.customers.contains(customer)) {
                System.out.println("You have not attended this lesson.");
            } else if (rating < 1 || rating > 5) {
                System.out.println("Invalid rating.");
            } else {
                lesson.ratings.add(rating);
                lessonStats.get(key).get("ratings")[numWeekends] += rating;
                System.out.println("Lesson rated successfully.");
            }
        }
        
        public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        fitnessweeklybooksysapp system = new fitnessweeklybooksysapp();

        while (true) {
            System.out.println("Enter command (book, change, rate, or quit):");
            String command = input.nextLine();

            if (command.equals("quit")) {
                System.out.println("Goodbye!");
                break;
            }

            if (command.equals("book")) {
                System.out.println("Enter customer name:");
                String customer = input.nextLine();
                System.out.println("Enter day (Saturday or Sunday):");
                String day = input.nextLine();
                System.out.println("Enter type (Spin, Yoga, Bodysculpt, Zumba, Aquacise, or Box Fit):");
                String type = input.nextLine();
                System.out.println("Enter lesson index (0 or 1):");
                int index = input.nextInt();
                input.nextLine(); // consume the newline character

                system.bookLesson(customer, day, type, index);
            }

            if (command.equals("change")) {
                System.out.println("Enter customer name:");
                String customer = input.nextLine();
                System.out.println("Enter old lesson key (format: day-type-index):");
                String oldKey = input.nextLine();
                System.out.println("Enter new day (Saturday or Sunday):");
                String newDay = input.nextLine();
                System.out.println("Enter new type (Spin, Yoga, Bodysculpt, Zumba, Aquacise, or Box Fit):");
                String newType = input.nextLine();
                System.out.println("Enter new lesson index (0 or 1):");
                int index = input.nextInt();
                input.nextLine(); // consume the newline character

                system.changeBooking(customer, oldKey, newDay, newType, index);
            }

            if (command.equals("rate")) {
                System.out.println("Enter customer name:");
                String customer = input.nextLine();
                System.out.println("Enter lesson key (format: day-type-index):");
                String key = input.nextLine();
                System.out.println("Enter rating (1-5):");
                int rating = input.nextInt();
                input.nextLine(); // consume the newline character

                system.rateLesson(customer, key, rating);
            }
        }

        input.close();
    }
}