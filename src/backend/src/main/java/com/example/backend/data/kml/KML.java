package com.example.backend.data.kml;

import com.example.backend.data.ApiResult;
import com.example.backend.data.api.NodeInfo;
import lombok.SneakyThrows;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class KML {

    private Document doc;
    private List<PlaceMark> placeMarks;

    protected KML(KML.Builder builder) {
        setDoc(builder.getDoc());
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


    public static abstract class Builder {

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

        public void setRootDoc(Element rootDoc) {
            this.rootDoc = rootDoc;
        }

        public abstract KML build();

        public KML from(List<ApiResult> apiResultList) {
            if (apiResultList == null || apiResultList.isEmpty()) return null;
            if (apiResultList.get(0) instanceof NodeInfo) {
                forElevation(apiResultList);
            } else {
                forRoute(apiResultList);
            }
            return this.build();
        }

        protected abstract KML forRoute(List<ApiResult> apiResultList);

        protected abstract KML forElevation(List<ApiResult> apiResultList);

    }

    @SneakyThrows
    @Override
    public String toString() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        // pretty print
        FileWriter myWriter = new FileWriter("filename.txt");
        xmlOutputter.setFormat(Format.getPrettyFormat());
        try {
            xmlOutputter.output(getDoc(), myWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        myWriter.close();
        return xmlOutputter.outputString(getDoc());
    }
}
