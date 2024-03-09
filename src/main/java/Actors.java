import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class Actors {
    public static final String API_KEY = "xBCdo/WzbzYrfkOUmGk6Nw==7NtV8oJzJA1PJMbB";

    String name;
    Double netWorth;
    Boolean isAlive;
    String dateOfDeath;

    public Actors(String netWorth, boolean isAlive) { //in constructor, I  added some input validation for the netWorth parameter.
        // The replaceAll method is used to remove any non-numeric characters
        //(except for the decimal point) from the netWorth string before parsing it as a Double.
        // This ensures that only numeric values are assigned to the netWorth attribute.
        if (netWorth != null && !netWorth.isEmpty()) {
            this.netWorth = Double.parseDouble(netWorth.replaceAll("[^0-9.]", ""));
        }
        this.isAlive = isAlive;
    }

    @SuppressWarnings({"deprecation"})
    /**
     * Retrieves data for the specified actor.
     * @param name for which Actor should be retrieved
     * @return a string representation of the Actors info or null if an error occurred
     */
    public String getActorData(String name) {
        try {
            URL url = new URL("https://api.api-ninjas.com/v1/celebrity?name=" +
                    name.replace(" ", "+") + "&apikey=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Api-Key", API_KEY);
            System.out.println(connection);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return response.substring(1, response.length()-1);
            } else {
                return "Error: " + connection.getResponseCode() + " " + connection.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getNetWorthViaApi(String actorsInfoJson) {

        JSONObject jsonObject = new JSONObject(actorsInfoJson);
        Double result = jsonObject.getDouble("net_worth");
        return result;
    }

    public boolean isAlive(String actorsInfoJson) {
        JSONObject jsonObject = new JSONObject(actorsInfoJson);
        boolean result = jsonObject.getBoolean("is_alive");
        return result;
    }

    public String getDateOfDeathViaApi(String actorsInfoJson) {
        JSONObject jsonObject = new JSONObject(actorsInfoJson);
        String result = jsonObject.getString("death");
        return result;
    }


}