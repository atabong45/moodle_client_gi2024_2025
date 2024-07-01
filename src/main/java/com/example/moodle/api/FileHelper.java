package com.example.moodle.api;

import com.example.moodle.Entities.File;
import com.example.moodle.moodleclient.Moodleclient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FileHelper {
    private static final String COURSE_CONTENT = "core_course_get_contents";

    public FileHelper() {
    }

    public File getFile(long courseid, long sectionid, long moduleid) {
        File file = null;
        String urlStr = Moodleclient.serverAddress + "webservice/rest/server.php?wstoken=" + Moodleclient.superToken + "&wsfunction=" + COURSE_CONTENT + "&moodlewsrestformat=json&courseid=" + courseid;

        try {
            String res = RequestHelper.formRequest(urlStr);
            JSONParser parser = new JSONParser();
            JSONArray data = (JSONArray) parser.parse(res);
            for(int j = 0; j < data.size(); j++) {
                JSONObject section = (JSONObject) data.get(j);
                if((Long) section.get("id") == sectionid) {
                    JSONArray jsonArray = (JSONArray) section.get("modules");
                    for(int i = 0; i < jsonArray.size(); i++) {
                        JSONObject module = (JSONObject) jsonArray.get(i);
                        if((Long) module.get("id") == moduleid) {
                            if((Long)module.get("downloadcontent") == 1) {
                                file = new File();
                                JSONArray contents = (JSONArray) module.get("contents");
                                if(contents != null) {
                                    JSONObject content = (JSONObject) contents.get(0);

                                    file.setFilename(content.get("filename").toString());
                                    file.setModuleid(moduleid);
                                    file.setFilepath(content.get("filepath").toString());
                                    file.setFilesize((Long)content.get("filesize"));
                                    file.setFileurl(content.get("fileurl").toString());
                                    file.setCreated((Long)content.get("timecreated"));
                                    file.setUpdated((Long)content.get("timemodified"));
                                    file.setRepositorytype("");
                                    file.setMimetype(content.get("mimetype").toString());
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
