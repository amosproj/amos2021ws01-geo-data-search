package com.example.backend.data;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.annotation.XmlRootElement;

@ResponseBody
@XmlRootElement
public class NodeInfo implements ApiResult {
    String id;
    String visible;
    String version;
    String changeSet;
    String timeStamp;
    String user;
    String uid;
    String lat;
    String lon;

    public NodeInfo(String id, String visible, String version, String changeSet, String timeStamp, String user, String uid, String lat, String lon) {
        this.id = id;
        this.visible = visible;
        this.version = version;
        this.changeSet = changeSet;
        this.timeStamp = timeStamp;
        this.user = user;
        this.uid = uid;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public String getVisible() {
        return visible;
    }

    public String getVersion() {
        return version;
    }

    public String getChangeSet() {
        return changeSet;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUser() {
        return user;
    }

    public String getUid() {
        return uid;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "OSMXml{" +
                "id='" + id + '\'' +
                ", visible='" + visible + '\'' +
                ", version='" + version + '\'' +
                ", changeSet='" + changeSet + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", user='" + user + '\'' +
                ", uid='" + uid + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
