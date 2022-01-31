package com.example.backend.data.kml;

import com.example.backend.data.ApiResult;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KMLPlaceMark implements KML {
    private Document doc;
    private List<PlaceMark> placeMarks;

    private KMLPlaceMark(Builder builder) {
        setDoc(builder.getDoc());
        setPlaceMarks(builder.getPlaceMarks());
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public List<PlaceMark> getPlaceMarks() {
        return placeMarks;
    }

    public void setPlaceMarks(List<PlaceMark> placeMarks) {
        this.placeMarks = placeMarks;
    }

    public static class Builder {
        private final Namespace namespace = Namespace.getNamespace("http://www.opengis.net/kml/2.2");
        private Element rootDoc;
        private Document doc;
        private List<PlaceMark> placeMarks;

        public Builder() {
            doc = new Document();
            Element root = new Element("kml", namespace);
            rootDoc = new Element("Document", namespace);
            root.addContent(rootDoc);
            doc.setRootElement(root);
            placeMarks = new ArrayList<PlaceMark>();
        }

        public Document getDoc() {
            return doc;
        }

        public List<PlaceMark> getPlaceMarks() {
            return placeMarks;
        }

        public Builder addPlaceMark(PlaceMark placeMarkObj) {
            placeMarks.add(placeMarkObj);
            Element placeMarkElement = new Element("Placemark", namespace);
            Element nameElement = new Element("name", namespace).setText(placeMarkObj.getName());
            placeMarkElement.addContent(nameElement);

            Element descriptionElement = new Element("description", namespace).setText(placeMarkObj.getDescription());
            placeMarkElement.addContent(descriptionElement);

            Element point = new Element("Point", namespace);
            Element coordinates = new Element("coordinates", namespace);
            coordinates.setText(placeMarkObj.getCoordinates());
            point.addContent(coordinates);

            placeMarkElement.addContent(point);
            rootDoc.addContent(placeMarkElement);
            return this;
        }

        public KMLPlaceMark build() {
            return new KMLPlaceMark(this);
        }

        public KMLPlaceMark from(ApiResult apiResult) {
            PlaceMark placeMark = new PlaceMark(apiResult.getName(), apiResult.getType(), apiResult.getLat(), apiResult.getLon());
            return addPlaceMark(placeMark).build();
        }

        public KMLPlaceMark from(List<ApiResult> apiResultList) {
            if (apiResultList == null || apiResultList.isEmpty()) return null;
            for (ApiResult apiResult : apiResultList) {
                PlaceMark placeMark = new PlaceMark(apiResult.getName(), apiResult.getType(), apiResult.getLat(), apiResult.getLon());
                addPlaceMark(placeMark);
            }
            return this.build();
        }
    }

    @Override
    public String toString() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        // pretty print
        xmlOutputter.setFormat(Format.getPrettyFormat());
        try {
            xmlOutputter.output(getDoc(), System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlOutputter.outputString(getDoc());
    }
}
