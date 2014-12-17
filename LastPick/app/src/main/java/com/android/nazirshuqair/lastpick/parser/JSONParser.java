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

        JSONObject apiData;
        try{

            List<Resturant> venuList = new ArrayList();

            if (venuList.size() > 0){
                venuList.clear();
            }

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
                                if (items.getJSONObject(j).has("venue")) {
                                    venueObject = items.getJSONObject(j).getJSONObject("venue");

                                    if (items.getJSONObject(j).has("tips")) {
                                        tipsObject = items.getJSONObject(j).getJSONArray("tips");
                                    }
                                    if (venueObject != null) {
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
                                            if (venueObject.has("location")) {
                                                JSONObject location = venueObject.getJSONObject("location");
                                                if (location.has("distance")) {
                                                    venue.setDistance(location.getDouble("distance") / 5280);
                                                } else {
                                                    venue.setDistance(0.0);
                                                }
                                                if (location.has("formattedAddress")) {
                                                    JSONArray formattedAddressArray = location.getJSONArray("formattedAddress");
                                                    if (formattedAddressArray.length() > 2) {
                                                        String formattedAddress = formattedAddressArray.getString(0) + "\n" +
                                                                formattedAddressArray.getString(1) + "\n" +
                                                                formattedAddressArray.getString(2);
                                                        venue.setFormattedAddress(formattedAddress);
                                                    }else {
                                                        venue.setFormattedAddress("Not Available");
                                                    }
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
                                            if (venueObject.has("photos")) {
                                                JSONObject photos = venueObject.getJSONObject("photos");
                                                if (photos.has("groups")) {
                                                    JSONArray photoGroups = photos.getJSONArray("groups");
                                                    if (photoGroups.getJSONObject(0).has("items")) {
                                                        JSONArray photoItems = photoGroups.getJSONObject(0).getJSONArray("items");
                                                        String imgprefix = "";
                                                        String imgsuffix = "";
                                                        String imgFull = "";
                                                        if (photoItems.getJSONObject(0).has("prefix")) {
                                                            imgprefix = photoItems.getJSONObject(0).getString("prefix");
                                                            imgsuffix = photoItems.getJSONObject(0).getString("suffix");
                                                            imgFull = imgprefix + "width900" + imgsuffix;
                                                            venue.setImgUrl(imgFull);
                                                        }
                                                    }
                                                }
                                            }
                                            if (tipsObject != null) {
                                                for (int o = 0; o < tipsObject.length(); o++) {
                                                    JSONObject tip = tipsObject.getJSONObject(o);
                                                    if (tip != null) {
                                                        if (tip.has("text")) {
                                                            venue.setText(tip.getString("text"));
                                                        } else {
                                                            venue.setText("Not Available");
                                                        }
                                                        if (tip.has("user")) {
                                                            JSONObject user = tip.getJSONObject("user");
                                                            if (user.has("firstName")) {
                                                                venue.setFirstName(tip.getJSONObject("user").getString("firstName"));
                                                            } else {
                                                                venue.setFirstName("Not Available");
                                                            }
                                                        } else {
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
                }
                venuList.size();
                return venuList;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}