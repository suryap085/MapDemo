package com.map.mapview.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.map.mapview.modal.Company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    private static final String defaultCollegeImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2Uzd92OqsPDOWV22tAUZYSkLAqlMqxh7yloM9x1IHF9yazxCEr0bh35-3YSbSKrWENRvJ2ErOBTnAd_H4bpYW9e51g3GDAp02Aw&usqp=CAU&ec=45750089";

    //Private Constructor so that this class cannot be sub-classed
    private NetworkUtils() {
    }

    //Create URL object from the string
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Problem", "Problem building the URL ", e);
        }
        return url;
    }

    //Perform a network call and obtain the response
    private static String makeHttpRequest(URL url) throws IOException {
        String response = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e("Problem", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Problem", "Problem retrieving the college JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return response;
    }

    //Convert receicved stream from API to String
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Extract required information of each college from JSON and store in List
    private static List<Company> extractFeatureFromJson(String companyJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(companyJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding colleges to
        List<Company> company = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONArray companyArray = new JSONArray(companyJSON);
            for (int i = 0; i < companyArray.length(); i++) {
                String company_image_url;
                JSONObject responseObj = companyArray.getJSONObject(i);
                String company_id = responseObj.getString("company_id");
                String company_name = responseObj.getString("company_name");
                String company_description = responseObj.getString("company_description");
                String latitude = responseObj.getString("latitude");
                String longitude = responseObj.getString("longitude");
                if (company_id.equals("1121")) {
                    company_image_url = defaultCollegeImageUrl;
                } else {
                    company_image_url = responseObj.getString("company_image_url").equals("") ? "" : responseObj.getString("company_image_url");
                }

                String avg_rating = responseObj.getString("avg_rating");
                company.add(new Company(company_id, company_name, company_description, latitude, longitude, company_image_url, avg_rating, i));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the company JSON results", e);
        }
        return company;
    }


    //Driver Method for all the other methods
    public static List<Company> fetchData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Problem", "Problem making the HTTP request.", e);
        }
        List<Company> colleges = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Colleges}s
        return colleges;
    }

    public static BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
