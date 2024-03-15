import org.json.JSONObject;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Actors actors = new Actors("0", true);
        Movie movie = new Movie(new ArrayList<>(), "", 0);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Searching about an actor ");
            System.out.println("2. Searching about a movie");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        lookUpActor(scanner, actors);
                        break;
                    case 2:
                        lookUpMovie(scanner, movie);
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                }
            }
            catch(Exception e){
                System.out.println("Invalid choice, please try again.");
                scanner.next(); // clears the scanner buffer and prevents the infinite loop
            }
        }
    }

    private static void lookUpActor(Scanner scanner, Actors actors) throws IOException {
        System.out.print("Enter the actor's name: ");
        scanner.nextLine();
        String actorName = scanner.nextLine();

        String actorData = actors.getActorData(actorName);

        if (actorData != null) {
            System.out.println("Actor data:");
            //System.out.println("Name: " + actors.name);
            System.out.println("Net worth: $" + actors.getNetWorthViaApi(actorData));
            System.out.println("Is alive: " + actors.isAlive(actorData));

            if (!actors.isAlive(actorData)) {
                System.out.println("Date of death: " + actors.getDateOfDeathViaApi(actorData));
            }
        } else {
            System.out.println("Failed to retrieve actor data.");
        }
    }

    private static void lookUpMovie(Scanner scanner, Movie movie) throws IOException {
        System.out.print("Enter a movie's title: ");
        scanner.nextLine(); // Consume newline left-over
        String movieTitle = scanner.nextLine();

        String movieData = movie.getMovieData(movieTitle);

        if (!movieData.contains("False")) {
            JSONObject movieJsonObject = new JSONObject(movieData);
            String title = movieJsonObject.getString("Title");

            System.out.println("Movie data:");
            System.out.println("Title: " + title);
            System.out.println("Rating: " + movie.getRatingViaApi(movieData));
            System.out.println("IMDb votes: " + movie.getImdbVotesViaApi(movieData));
            System.out.println("Actors: " + movie.getActorListViaApi(movieData));
        } else {
            System.out.println("Movie not found.");
        }
    }
}