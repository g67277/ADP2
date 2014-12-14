/*
Nazir Shuqair
Java 1 - 1409
Final Project
 */
package com.android.nazirshuqair.lastpick.parser;

import com.android.nazirshuqair.lastpick.model.Resturant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazirshuqair on 12/04/14.
 */
public class JSONParser {

    public static List<Resturant> parseFeed(String content) {

        boolean tipPresent;

        JSONObject apiData;
        try{

            List<Resturant> venuList = new ArrayList();

            apiData = new JSONObject(content);

            JSONArray groups = apiData.getJSONObject("response").getJSONArray("groups");

            if (groups.length() < 1){
                return null;
            }else {

                for (int i = 0; i < groups.length(); i++){
                    JSONObject item = groups.getJSONObject(i);
                    if (item != null){
                        JSONArray items = item.getJSONArray("items");
                        if (items != null){
                            for (int j = 0; j < items.length(); j++){
                                Resturant venue = new Resturant();
                                JSONObject venueObject = null;
                                JSONArray tipsObject = null;
                                if (items.getJSONObject(j).has("venue")){
                                    venueObject = items.getJSONObject(j).getJSONObject("venue");
                                }
                                if (items.getJSONObject(j).has("tips")){
                                    tipsObject = items.getJSONObject(j).getJSONArray("tips");
                                }
                                if (venueObject != null){
                                    if (venueObject.has("name")) {
                                        venue.setName(venueObject.getString("name"));
                                        if (venueObject.has("contact")) {
                                            JSONObject contacts = venueObject.getJSONObject("contact");
                                            if (contacts.has("formattedPhone")) {
                                                venue.setFormattedPhone(contacts.getString("formattedPhone"));
                                            } else {
                                                venue.setFormattedPhone("Not Available");
                                            }
                                        } else {
                                            venue.setFormattedPhone("Not Available");
                                        }
                                        if (venueObject.has("location")){
                                            JSONObject location = venueObject.getJSONObject("location");
                                            if (location.has("formattedAddress")) {
                                                venue.setFormattedAddress(location.getString("formattedAddress"));
                                            } else {
                                                venue.setFormattedAddress("Not Available");
                                            }
                                            venue.setLat(location.getDouble("lat"));
                                            venue.setLng(location.getDouble("lng"));
                                        }
                                        if (venueObject.has("url")) {
                                            venue.setUrl(venueObject.getString("url"));
                                        } else {
                                            venue.setUrl(null);
                                        }
                                        if (venueObject.has("price")) {
                                            JSONObject price = venueObject.getJSONObject("price");
                                            if (price.has("currency")) {
                                                venue.setCurrency(venueObject.getJSONObject("price").getString("currency"));
                                            } else {
                                                venue.setCurrency("N/A");
                                            }
                                        } else {
                                            venue.setCurrency("N/A");
                                        }
                                        if (venueObject.has("rating")) {
                                            venue.setRating(venueObject.getDouble("rating"));
                                        } else {
                                            venue.setRating(0.0);
                                        }
                                        if (tipsObject != null) {
                                            for (int o = 0; o < tipsObject.length(); o++) {
                                                JSONObject tip = tipsObject.getJSONObject(o);
                                                if (tip != null) {
                                                    if (tip.has("text")){
                                                        venue.setText(tip.getString("text"));
                                                    }else {
                                                        venue.setText("Not Available");
                                                    }
                                                    if (tip.has("user")){
                                                        JSONObject user = tip.getJSONObject("user");
                                                        if (user.has("firstName")){
                                                            venue.setFirstName(tip.getJSONObject("user").getString("firstName"));
                                                        }else {
                                                            venue.setFirstName("Not Available");
                                                        }
                                                    }else {
                                                        venue.setFirstName("Not Available");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                venuList.add(venue);
                            }
                        }
                    }
                }

                /*weather.setCity(channel.getJSONObject("location").getString("city"));
                weather.setRegion(channel.getJSONObject("location").getString("region"));
                weather.setTemperature(channel.getJSONObject("item").getJSONObject("condition").getInt("temp"));
                weather.setTempText(channel.getJSONObject("item").getJSONObject("condition").getString("text"));
                weather.setForecastJSON(channel.getJSONObject("item").getJSONArray("forecast"));*/

                //venuList.add(venue);
                //Change
                return venuList;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}