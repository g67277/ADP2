/*
Nazir Shuqair
Java 1 - 1409
Final Project
 */
package com.android.nazirshuqair.lastpick.parser;

import android.net.Uri;

import com.android.nazirshuqair.lastpick.model.Resturant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazirshuqair on 9/21/14.
 */
public class JSONParser {

    public static List<Resturant> parseFeed(String content) {

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
                                JSONObject venueObject = items.getJSONObject(j).getJSONObject("venue");
                                JSONArray tipsObject = items.getJSONObject(j).getJSONArray("tips");
                                if (venueObject != null){
                                    venue.setName(venueObject.getString("name"));
                                    JSONObject contacts = venueObject.getJSONObject("contact");
                                    if (contacts.has("formattedPhone")){
                                        venue.setFormattedPhone(contacts.getString("formattedPhone"));
                                    }else {
                                        venue.setFormattedPhone("Not Available");
                                    }
                                    JSONObject location = venueObject.getJSONObject("location");
                                    if (location.has("formattedAddress")){
                                        venue.setFormattedAddress(location.getString("formattedAddress"));
                                    }else {
                                        venue.setFormattedAddress("Not Available");
                                    }
                                    venue.setLat(location.getDouble("lat"));
                                    venue.setLng(location.getDouble("lng"));
                                    if (venueObject.has("url")){
                                        String uri = venueObject.getString("url");
                                        venue.setUrl(Uri.parse(uri));
                                    }else {
                                        venue.setUrl(null);
                                    }
                                    venue.setCurrency(venueObject.getJSONObject("price").getString("currency"));
                                    venue.setRating(venueObject.getDouble("rating"));
                                    if (tipsObject != null){
                                        for (int o = 0; o < tipsObject.length(); o++){
                                            JSONObject tip = tipsObject.getJSONObject(o);
                                            if (tip != null){
                                                venue.setText(tip.getString("text"));
                                                venue.setFirstName(tip.getJSONObject("user").getString("firstName"));
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