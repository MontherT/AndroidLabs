package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        // Start the AsyncTask to download cat images
        CatImages catImages = new CatImages(this);
        catImages.execute();
    }

    private static class CatImages extends AsyncTask<Void, Integer, Void> {
        private final WeakReference<MainActivity> activityReference;
        private Bitmap bitmap;

        public CatImages(MainActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (!isCancelled()) {
                try {
                    // Fetch the random cat image data from the API
                    URL apiUrl = new URL("https://cataas.com/cat?json=true");
                    HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                    InputStream inputStream = connection.getInputStream();

                    String jsonResponse = new java.util.Scanner(inputStream).useDelimiter("\\A").next();
                    Log.d("CatImages", "JSON Response: " + jsonResponse);
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    // Check if URL field exists
                    String imageUrl = null;
                    if (jsonObject.has("url")) {
                        imageUrl = "https://cataas.com" + jsonObject.getString("url");
                    } else if (jsonObject.has("_id")) {
                        String imageId = jsonObject.getString("_id");
                        imageUrl = "https://cataas.com/cat/" + imageId;
                    }

                    if (imageUrl != null) {
                        // Download the image
                        InputStream imageStream = new URL(imageUrl).openStream();
                        bitmap = BitmapFactory.decodeStream(imageStream);

                        // Save the image locally
                        File file = new File(activityReference.get().getFilesDir(), jsonObject.getString("_id") + ".jpg");
                        try (FileOutputStream out = new FileOutputStream(file)) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        }
                    } else {
                        Log.e("CatImages", "No valid URL found in the response.");
                    }

                    // Simulate progress bar delay
                    for (int i = 0; i < 100; i++) {
                        publishProgress(i);
                        Thread.sleep(30);
                    }

                } catch (Exception e) {
                    Log.e("CatImages", "Error during image processing", e);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            MainActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                activity.progressBar.setProgress(values[0]);

                if (values[0] == 0) {
                    activity.imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
