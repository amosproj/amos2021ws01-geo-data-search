package com.example.backend.kml;

import com.example.backend.api.ApiResult;
import com.example.backend.api.osm.NodeInfo;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

public abstract class KML {

    private Document doc;

    protected KML(KML.Builder builder) {
        setDoc(builder.getDoc());
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public static abstract class Builder {

        private final Namespace namespace = Namespace.getNamespace("http://www.opengis.net/kml/2.2");
        private final Element rootDoc;
        private final Document doc;
        private final List<PlaceMark> placeMarks;

        public Builder() {
            doc = new Document();
            Element root = new Element("kml", namespace);
            rootDoc = new Element("Document", namespace);
            root.addContent(rootDoc);
            doc.setRootElement(root);
            placeMarks = new ArrayList<>();
        }

        public Document getDoc() {
            return doc;
        }

        public List<PlaceMark> getPlaceMarks() {
            return placeMarks;
        }

        public Namespace getNamespace() {
            return namespace;
        }

        public Element getRootDoc() {
            return rootDoc;
        }

        public abstract KML build();

        /**
         * gets the result list and creates the KMLPlaceMark or
         * KMLRoute object depending on the results
         *
         * @param apiResultList result list
         * @return KML object
         */
        public KML from(List<ApiResult> apiResultList) {
            if (apiResultList == null || apiResultList.isEmpty()) return null;
            if (apiResultList.get(0) instanceof NodeInfo) {
                forElevation(apiResultList);
            } else {
                forRoute(apiResultList);
            }
            return this.build();
        }

        protected abstract void forRoute(List<ApiResult> apiResultList);

        protected abstract void forElevation(List<ApiResult> apiResultList);
    }
}