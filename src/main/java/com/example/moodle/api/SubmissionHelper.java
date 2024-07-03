package com.example.moodle.api;

import com.example.moodle.Entities.Submission;
import com.example.moodle.moodleclient.Moodleclient;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SubmissionHelper {
    private static final String GET_SUBMISSIONS = "mod_assign_get_submissions";
    private static final String SAVE_SUBMISSION = "mod_assign_save_submission";

    private static final String SERVER_ADDRESS = "http://localhost/";
    public SubmissionHelper() {
    }

    public ArrayList<Submission> getSubmissions(long assignmentid, long studentid) {
        ArrayList<Submission> submissions = new ArrayList<>();
        String urlStr = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + GET_SUBMISSIONS + "&moodlewsrestformat=json&assignmentids[0]=" + assignmentid;
        try {
            String res = RequestHelper.formRequest(urlStr);
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(res);
            JSONArray assignments = (JSONArray) data.get("assignments");
            for(int i = 0; i < assignments.size(); i++) {
                JSONObject assign = (JSONObject)assignments.get(i);
                if((Long)assign.get("assignmentid") == assignmentid) {
                    JSONArray jsonArray = (JSONArray) assign.get("submissions");
                    for(int j = 0; j < jsonArray.size(); j++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                        if((Long)jsonObject.get("userid") == studentid) {
                            Submission submission = new Submission();
                            submission.setSubmissionid((Long)jsonObject.get("id"));
                            submission.setAssignmentid(assignmentid);
                            submission.setStatus(jsonObject.get("status").toString());
                            submission.setStudentid(studentid);
                            submission.setCreated((Long)jsonObject.get("timecreated"));
                            submission.setUpdated((Long)jsonObject.get("timemodified"));

                            submissions.add(submission);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submissions;
    }

    // For the teacher
    public ArrayList<Submission> getAllSubmissions(long assignmentid) {
        ArrayList<Submission> submissions = new ArrayList<>();
        String urlStr = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + GET_SUBMISSIONS + "&moodlewsrestformat=json&assignmentids[0]=" + assignmentid;
        try {
            String res = RequestHelper.formRequest(urlStr);
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(res);
            JSONArray assignments = (JSONArray) data.get("assignments");
            for(int i = 0; i < assignments.size(); i++) {
                JSONObject assign = (JSONObject)assignments.get(i);
                if((Long)assign.get("assignmentid") == assignmentid) {
                    JSONArray jsonArray = (JSONArray) assign.get("submissions");
                    for(int j = 0; j < jsonArray.size(); j++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                        Submission submission = new Submission();
                        submission.setSubmissionid((Long)jsonObject.get("id"));
                        submission.setAssignmentid(assignmentid);
                        submission.setStatus(jsonObject.get("status").toString());
                        submission.setStudentid((Long)jsonObject.get("userid"));
                        submission.setCreated((Long)jsonObject.get("timecreated"));
                        submission.setUpdated((Long)jsonObject.get("timemodified"));

                            submissions.add(submission);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submissions;
    }

    public boolean saveSubmission(long assignmentid, long itemid) {
        String urlStr = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + SAVE_SUBMISSION + "&moodlewsrestformat=json&assignmentid=" + assignmentid +
                "&plugindata[files_filemanager]=" + itemid;
        try {
             String res = RequestHelper.formRequest(urlStr);
             JSONParser parser = new JSONParser();
             JSONArray data = (JSONArray) parser.parse(res);
             for(int i = 0; i < data.size(); i++) {
                 JSONObject object = (JSONObject) data.get(i);
                 if(object.keySet().contains("item")) return true;
             }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void saveSubmissionto_moddle(int assignmentId, String itemId) throws IOException {
        String requestUrl = SERVER_ADDRESS + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken +
                "&moodlewsrestformat=json&wsfunction=mod_assign_save_submission";

        // Prepare parameters
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("assignmentid", String.valueOf(assignmentId)));
        params.add(new BasicNameValuePair("plugindata[onlinetext_editor][text]", "Submission done."));
        params.add(new BasicNameValuePair("plugindata[onlinetext_editor][format]", "1"));
        params.add(new BasicNameValuePair("plugindata[onlinetext_editor][itemid]", itemId));
        params.add(new BasicNameValuePair("plugindata[files_filemanager]", itemId));

        // Execute the HTTP request
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = responseEntity != null ? EntityUtils.toString(responseEntity) : null;
                System.out.println("Response: " + responseString);
            }
        }
    }
}
