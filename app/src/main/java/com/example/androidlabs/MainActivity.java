package com.example.androidlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private StarWarsAdapter adapter;
    private List<HashMap<String, String>> characterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        adapter = new StarWarsAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        // Fetch data from the Star Wars API
        new FetchStarWarsData().execute();

        listView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            HashMap<String, String> selectedCharacter = characterList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("name", selectedCharacter.get("name"));
            bundle.putString("height", selectedCharacter.get("height"));
            bundle.putString("mass", selectedCharacter.get("mass"));


            // Logging selected character data
            System.out.println("Selected Character: " + selectedCharacter);

            if (findViewById(R.id.frameLayout) == null) {
                // Phone layout: Start EmptyActivity
                System.out.println("Phone layout detected. Launching EmptyActivity.");
                Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                // Tablet layout: Replace the fragment
                System.out.println("Tablet layout detected. Replacing fragment.");
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
            }
        });
    }

    private class FetchStarWarsData extends AsyncTask<Void, Void, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(Void... voids) {
            List<HashMap<String, String>> characters = new ArrayList<>();
            try {
                // Fetch main list of characters
                URL url = new URL("https://swapi.tech/api/people/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                if (responseCode == 200) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    System.out.println("API Response: " + response.toString());

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray results = jsonResponse.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject character = results.getJSONObject(i);
                        String name = character.getString("name");
                        String detailUrl = character.getString("url");

                        // Fetch character details (e.g., height, mass) from detail URL
                        HashMap<String, String> characterData = fetchCharacterDetails(name, detailUrl);
                        characters.add(characterData);
                    }
                } else {
                    System.out.println("Error: HTTP response code " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return characters;
        }

        private HashMap<String, String> fetchCharacterDetails(String name, String url) {
            HashMap<String, String> characterData = new HashMap<>();
            characterData.put("name", name);

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    System.out.println("Detail Response for " + name + ": " + response.toString());

                    JSONObject detailResponse = new JSONObject(response.toString());
                    JSONObject properties = detailResponse.getJSONObject("result").getJSONObject("properties");

                    // Extract relevant fields
                    characterData.put("height", properties.getString("height"));
                    characterData.put("mass", properties.getString("mass"));

                }
            } catch (Exception e) {
                e.printStackTrace();
                characterData.put("height", "N/A");
                characterData.put("mass", "N/A");

            }

            return characterData;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> characters) {
            if (characters.isEmpty()) {
                System.out.println("No characters found.");
            } else {
                System.out.println("Fetched characters: " + characters.size());
                characterList = characters;
                List<String> characterNames = new ArrayList<>();
                for (HashMap<String, String> character : characters) {
                    characterNames.add(character.get("name"));
                }
                adapter.setData(characterNames);
            }
        }
    }
}
