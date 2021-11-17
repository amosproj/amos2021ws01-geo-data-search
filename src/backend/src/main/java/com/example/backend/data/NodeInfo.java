package com.example.backend.data;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NodeInfo {
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
