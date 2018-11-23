package com.example.administrator.renhua.biz;

import android.content.Context;
import android.util.Log;


import com.example.administrator.renhua.entity.AffairsRecord;
import com.example.administrator.renhua.entity.Project;
import com.example.administrator.renhua.entity.RecordList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 2016/3/25.
 */
public class ProjectBiz {

    Context context;
    //

    public ProjectBiz(Context context) {
        this.context = context;
    }

    public List<Project> getProjects() {
        List<Project> projects = new ArrayList<Project>();
        InputStream is = ProjectBiz.this.getClass().getClassLoader().getResourceAsStream("assets/" + "text.json");
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Project project;
            JSONObject person = new JSONObject(stringBuilder.toString());
            JSONArray resArray = person.getJSONArray("result");
            Log.d("err1", "resArray:" + resArray);
            for (int i = 0; i < resArray.length(); i++) {
                project = new Project();
                JSONObject res_Array = resArray.getJSONObject(i);
                project.id = res_Array.getString("id");
                project.project = res_Array.getString("project");
                project.hall = res_Array.getString("hall");
                project.time = res_Array.getString("time");
                project.progress = res_Array.getString("progress");
                projects.add(project);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public List<RecordList> getRecords() {
        List<RecordList> records = new ArrayList<RecordList>();
        InputStream is = ProjectBiz.this.getClass().getClassLoader().getResourceAsStream("assets/" + "record.json");
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RecordList recordList;
            JSONObject person = new JSONObject(stringBuilder.toString());
            JSONArray resArray = person.getJSONArray("result");
            Log.d("err1", "resArray:" + resArray);
            for (int i = 0; i < resArray.length(); i++) {
                recordList = new RecordList();
                JSONObject res_Array = resArray.getJSONObject(i);
                recordList.order = res_Array.getString("order");
                recordList.amounts = res_Array.getString("amounts");
                recordList.number = res_Array.getString("number");
                recordList.name = res_Array.getString("name");
                recordList.date = res_Array.getString("date");
                recordList.amount = res_Array.getString("amount");
                records.add(recordList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<AffairsRecord> getAffairs() {
        List<AffairsRecord> affairs = new ArrayList<AffairsRecord>();
        InputStream is = ProjectBiz.this.getClass().getClassLoader().getResourceAsStream("assets/" + "affairsrecord.json");
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            AffairsRecord affairsRecord;
            JSONObject person = new JSONObject(stringBuilder.toString());
            JSONArray resArray = person.getJSONArray("result");
            Log.d("err1", "resArray:" + resArray);
            for (int i = 0; i < resArray.length(); i++) {
                affairsRecord = new AffairsRecord();
                JSONObject res_Array = resArray.getJSONObject(i);
                affairsRecord.id = res_Array.getString("id");
                affairsRecord.project = res_Array.getString("project");
                affairsRecord.finish_type = res_Array.getInt("finish_type");
                affairsRecord.progress = res_Array.getString("progress");
                affairs.add(affairsRecord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return affairs;
    }

}
