package app.bmaxtech.com.utility;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import app.bmaxtech.com.atm_finder.R;

public class NearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    private static HashMap<String, Marker> markers = new HashMap<>();

    @Override
    protected String doInBackground(Object... params) {
        try {
            // check map is null
            if (mMap == null) {
                mMap = (GoogleMap) params[0];
            }
            // request places data
            Log.d("URL", (String) params[1]);
            URL url = new URL((String) params[1]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                googlePlacesData = response.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (result != null) {
                JSONObject res = new JSONObject(result);
                ShowNearbyPlaces(res.getJSONArray("results"));
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

    public static void clearMarkers() {
        for (String key : markers.keySet()) {
            markers.get(key).remove();
        }
        markers.clear();
    }

    private void ShowNearbyPlaces(JSONArray nearbyPlacesList) {
        try {
            for (int i = 0; i < nearbyPlacesList.length(); i++) {
                JSONObject googlePlace = nearbyPlacesList.getJSONObject(i);
                double lat = googlePlace.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                double lng = googlePlace.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                String placeName = googlePlace.getString("name");
                String vicinity = googlePlace.getString("vicinity");
                // check marker exists
                if (!markers.containsKey(lat + "" + lng)) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                    Marker marker = mMap.addMarker(markerOptions);
                    // add to history markers list
                    markers.put(lat + "" + lng, marker);
                }
            }
        } catch (JSONException e) {
            Log.e("ShowNearbyPlaces JSONEx", e.getMessage());
        }
    }
}