package app.bmaxtech.com.utility;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

public class GetPlaces {
    private static final String API_KEY = "AIzaSyDjPgD2I7RazSpnvzqWYqGBAZsbbYOe2m0";
    private static final String TYPE = "atm";
    private static GoogleMap googleMap;
    private static Location location;
    private static int radius = 1000;

    public GetPlaces(GoogleMap c_googleMap) {
        googleMap = c_googleMap;
    }

    public void updateLocation(Location c_location) {
        if (this.location != null) {
            float[] res = new float[3];
            Location.distanceBetween(c_location.getLatitude(), c_location.getLongitude(), location.getLatitude(), location.getLongitude(), res);
            if (res[0] > 500) {
                // update
                request();
            }
            location = c_location;
        } else {
            // only for first time
            location = c_location;
            request();
        }
    }

    public static void updateRadius(int val) {
        if ((val + 1) * 1000 != radius) {
            if (location != null) {
                radius = (val + 1) * 1000;
                NearbyPlacesData.clearMarkers();
                request();
            }
        }
    }

    private static void request() {
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = googleMap;
        DataTransfer[1] = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&radius=" + radius + "&type=" + TYPE + "&key=" + API_KEY;
        new NearbyPlacesData().execute(DataTransfer);
    }
}
