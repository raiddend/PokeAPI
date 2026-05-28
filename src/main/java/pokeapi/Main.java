package pokeapi;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.URI;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    
    public static void main(String[] args) {
        try{

        HttpClient client = HttpClient.newHttpClient();
    
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://pokeapi.co/api/v2/pokemon?limit=150")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        JSONArray array = json.getJSONArray("results");
        for (int i = 0; i < array.length(); i++){
            JSONObject element = array.getJSONObject(i);
            String actualPokeUrl = element.getString("url");
            HttpRequest actualRequest = HttpRequest.newBuilder().uri(URI.create(actualPokeUrl)).GET().build();
            HttpResponse<String> actResponse = client.send(actualRequest, HttpResponse.BodyHandlers.ofString());
            JSONObject actualPokeJson = new JSONObject(actResponse.body());
            JSONArray types = actualPokeJson.getJSONArray("types");
            System.out.println((i+1) + ". " + element.getString("name") + " - " + IntStream.range(0, types.length()).mapToObj(s -> types.getJSONObject(s).getJSONObject("type").getString("name")).collect(Collectors.joining(", ")));
            System.out.println(response.statusCode());
        }
        }catch(Exception e){
            System.out.println("Ocurrio un error " + e.getMessage());
        }
    }
}
