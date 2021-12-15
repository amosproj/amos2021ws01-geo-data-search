package com.example.backend.data.api;

import com.example.backend.data.ApiResult;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.annotation.XmlRootElement;

@ResponseBody
@XmlRootElement
public class NodeInfo implements ApiResult {
    private final String unknown = "Unknown";
    String id;
    String visible;
    String version;
    String changeSet;
    String timeStamp;
    String user;
    String uid;
    String lat;
    String lon;
    NodeTag tags;

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

    @Override
    public String getType() {
        String type;
        if (tags != null && tags.amenity != null) {
            type = tags.getAmenity();
        } else {
            type = unknown;
        }
        return type;
    }

    @Override
    public void setType(String type) {
        if (tags != null) {
            tags.amenity = type;
        }
    }

    @Override
    public int getId() {
        return Integer.parseInt(id);
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

    @Override
    public String getLat() {
        return lat;
    }

    @Override
    public String getLon() {
        return lon;
    }

    @Override
    public String getName() {
        String name;
        if (tags != null && tags.name != null) {
            name = tags.getName();
        } else {
            name = unknown;
        }
        return name;
    }

    public NodeTag getTags() {
        return tags;
    }

    public void setTags(NodeTag tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "id='" + id + '\'' +
                ", visible='" + visible + '\'' +
                ", version='" + version + '\'' +
                ", changeSet='" + changeSet + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", user='" + user + '\'' +
                ", uid='" + uid + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", tags=" + tags +
                '}';
    }
}
