import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Movie {
    public static final String API_KEY = "7c353a6f"; //api key from omdbapi
    int ImdbVotes;
    ArrayList<String> actorsList;
    String rating;


    public Movie(ArrayList<String> actorsList, String rating, int ImdbVotes) {      //initializing the object
        this.actorsList = actorsList;
        this.rating = rating;
        this.ImdbVotes = ImdbVotes;
    }


    /**
     * retrieves data for the specified movie.
     *
     * @param title the name of the title for which moviedata should be retrieved
     * @return a string representation of the moviedata, or null if an error occurred
     */

    public String getMovieData(String title) throws IOException {
        URL url = new URL("https://www.omdbapi.com/?t=" + title + "&apikey=" + API_KEY);
        URLConnection Url = url.openConnection();
        Url.setRequestProperty("apikey", API_KEY);
        BufferedReader reader = new BufferedReader(new InputStreamReader(Url.getInputStream()));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();

        String response = stringBuilder.toString();

        if (response.contains("False")) { //looking for an error
            return "Movie is not found.";
        }
        return response;
    }

    public int getImdbVotesViaApi(String moviesInfoJson) {
        JSONObject moviesJsonObject = new JSONObject(moviesInfoJson);
        if (moviesJsonObject.has("imdbVotes")) {
            String imdbVotesStr = moviesJsonObject.getString("imdbVotes");
            imdbVotesStr = imdbVotesStr.replaceAll("[^\\d]", "");    //removes characters which are not numbers
            try {                                                                     //parse the imdbvotes string to an integer
                int imdbVotesInt = Integer.parseInt(imdbVotesStr);
                return imdbVotesInt;
            } catch (NumberFormatException e) {
                System.err.println("Error parsing IMDB votes: " + e.getMessage());
            }
        }
        return 0;                                                                     //imdbvotes field is not found
    }


    public String getRatingViaApi(String moviesInfoJson) {
        JSONObject jsonObject = new JSONObject(moviesInfoJson);
        JSONArray ratings = jsonObject.getJSONArray("Ratings");
        JSONObject rating = ratings.getJSONObject(0);
        String result = rating.getString("Value");
        return result;
    }


    public String getActorListViaApi(String movieInfoJson) {
        JSONObject actorsJsonObject = new JSONObject(movieInfoJson);
        String[] actorsListArray = actorsList.toArray(new String[actorsList.size()]);
        if (actorsJsonObject.has("Actors")) {                        //checks if the actors field exists in the json data
            String actorsString = actorsJsonObject.getString("Actors");
            String[] actorsArray = actorsString.split(", ");
            for (String actor : actorsArray) {                           //adds each actor name to the actors list
                actorsList.add(actor);
            }
            actorsListArray = actorsList.toArray(new String[actorsList.size()]); //finalize the actors list string
            return Arrays.toString(actorsListArray);
        } else {
            System.out.println("actor information is not found.");
        }
        return Arrays.toString(actorsListArray);

    }
}