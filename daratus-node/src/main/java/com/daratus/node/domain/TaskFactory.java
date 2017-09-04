package com.daratus.node.domain;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskFactory {

    
    
    public Task createTaskFromJson(String jsonTask) {
        Task task = null;
        try {
            JSONObject jsonEntryObject = new JSONObject(jsonTask);
            JSONObject jsonTaskObject = null;
            if(jsonEntryObject.has(Task.GET_DATA)){
                task = new GetData();
                jsonTaskObject = jsonEntryObject.getJSONObject(Task.GET_DATA);
            }else if(jsonEntryObject.has(Task.GET_URLS)){
                task = new GetUrls();
                jsonTaskObject = jsonEntryObject.getJSONObject(Task.GET_URLS);
            }
            if(jsonTaskObject != null){
                task.setTargetURL(jsonTaskObject.getString("targetURL"));
                JSONObject dataJson = jsonTaskObject.getJSONObject("data");
                Iterator<?> iterator = dataJson.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    task.addDataItem(key, dataJson.getString(key));
                }
                JSONArray nextUrls = jsonTaskObject.getJSONArray("nextUrls");
                for (int i = 0; i < nextUrls.length(); i++) {
                    task.addNextUrl(nextUrls.getString(i));
                }
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return task;
    }

    
}
