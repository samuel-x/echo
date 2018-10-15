package com.unimelb.droptable.echo;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static ImmutableTask.Builder currentTaskBuilder = ImmutableTask.builder()
            .title("Placeholder Title")
            .address("Placeholder Address")
            .category("Placeholder Category")
            .subCategory("Placeholder Subcategory")
            .notes("Placeholder Notes");

    /**
     * Given two user names, creates a unique chat ID for them, which simply involves sorting them
     * alphabetically and adding a hyphen between them. Hence, it doesn't matter in what order the
     * two arguments are given.
     * @param user1 a username
     * @param user2 another username
     * @return a unique chat id for the two given users.
     */
    public static String generateUserChatId(String user1, String user2) {
        String childName;

        if (user1.compareTo(user2) < 0) {
            childName = String.format("%s-%s", user1, user2);
        } else {
            childName = String.format("%s-%s", user2, user1);
        }

        return childName;
    }

    /**
     * Returns a path between two coordinates.
     * @return
     */
    public static List<LatLng> getPath(LatLng startLL, LatLng destination) {

        List<LatLng> path = new ArrayList<LatLng>();

        double startLatitude = startLL.latitude;
        double destinationLatitude = destination.latitude;
        double startLongitude = startLL.longitude;
        double destinationLongitude = destination.longitude;

        //Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBrPt88vvoPDDn_imh-RzCXl5Ha2F2LYig")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context,
                ("\"" + startLatitude + "," + startLongitude + "\""),
                ("\"" + destinationLatitude + "," + destinationLongitude + "\""));
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of
                                            // route coordinates
                                            List<com.google.maps.model.LatLng> coords1 =
                                                    points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route
                                        // coordinates
                                        List<com.google.maps.model.LatLng> coords =
                                                points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Request", "help");
        }

        return path;
    }

    public static LatLng getMidPoint(LatLng startLL, LatLng destination) {
        return new LatLng ((startLL.latitude + destination.latitude)/2,
                (startLL.longitude + destination.longitude)/2);
    }

    public static double distance(LatLng startLL, LatLng destLL) {
        double theta = startLL.longitude - destLL.longitude;
        double dist = Math.sin(deg2rad(startLL.latitude)) *
                        Math.sin(deg2rad(destLL.latitude)) +
                        Math.cos(deg2rad(startLL.latitude)) *
                        Math.cos(deg2rad(destLL.latitude)) *
                        Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
