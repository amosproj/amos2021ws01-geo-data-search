package com.example.backend.controllers;

import com.example.backend.clients.NlpClient;
import com.example.backend.clients.OsmApiClient;
import com.example.backend.data.ApiResult;
import com.example.backend.data.HttpResponse;
import com.example.backend.data.osm.NodeInfo;
import com.example.backend.helpers.Error;
import com.example.backend.data.http.*;
import com.example.backend.data.kml.KML;
import com.example.backend.data.kml.KMLPlaceMark;
import com.example.backend.data.kml.KMLRoute;
import com.example.backend.helpers.*;
import com.example.backend.helpers.FileNotFoundException;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/backend")
public class FrontendController {

    public static final String BACKEND_VERSION = "0.12.0";
    public static final String MEDIA_TYPE_KML = "application/vnd.google-earth.kml+xml";
    private final NlpClient nlpClient;
    private final ApiController apiController;
    private final Logger logger = LogManager.getLogger("FRONTEND_CONTROLLER");
    private final BackendFileManager manager = new BackendFileManager();

    public FrontendController(NlpClient nlpClient, OsmApiClient osmApiClient, HereApiRestService hereApiRestService) {
        this.nlpClient = nlpClient;
        this.apiController = new ApiController(osmApiClient, hereApiRestService);
    }

    /**
     * Receives the query from Frontend and forwards it to NLP
     * Receives the response from NLP and forwards it to OSM or HERE API
     * Receives the response from OSM or HERE API and logs it
     *
     * @param query the input coming from the Frontend
     * @return result the answers from the OSM or HERE API for the Frontend
     */
    @PostMapping("/user_query")
    @ResponseBody
    public HttpResponse handleQueryRequest(@RequestBody String query) {
        logger.info("+ -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- +");

        logger.info("New query received! Query = \"" + query + "\"");
        query = prepareQuery(query);

        //  Sending query to NLP and receiving its response
        NlpQueryResponse nlpQueryResponse;
        try {
            nlpQueryResponse = getNlpQueryResponse(query);
        } catch (Throwable throwable) {
            return handleError(throwable);
        }

        // Decision which API to use, sending the query and receiving its response
        List<ApiResult> apiQueryResults;
        try {
            apiQueryResults = apiController.querySearch(nlpQueryResponse);
        } catch (MissingLocationException | UnknownQueryObjectException | NoPreferredApiFoundException | LocationNotFoundException | InvalidCalculationRequest | EmptyResultException e) {
            return handleError(e);
        }

        ResultResponse response = new ResultResponse(apiQueryResults);
        generateKmlAndSaveFile(response);
        logResponseToFrontend(response);
        logger.info("+ -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +");
        return response;
    }

    private void logResponseToFrontend(ResultResponse response) {
        if (!logger.isDebugEnabled()) {
            logger.info("Sending this respond to FRONTEND:\n" + response.toStringWithoutPolyline());
        } else {
            logger.debug("Sending this respond to FRONTEND:\n" + response.toStringWithPolyline());
        }
    }

    private void generateKmlAndSaveFile(ResultResponse response) {
        KML kml = generateKml(response);
        try {
            if (kml != null) {
                String fileName = manager.saveKmlFileAndReturnFileName(kml);
                response.setFileName(fileName);
            }
        } catch (IOException e) {
            handleError(e);
        }
    }

    private KML generateKml(ResultResponse response) {
        KML kml = null;
        if (response.getResult() != null && !response.getResult().isEmpty()) {
            if (!response.getResult().isEmpty() && response.getResult().get(0) instanceof NodeInfo) {
                //OSM node info results
                kml = new KMLPlaceMark.Builder().from(response.getResult());
            } else if (!response.getResult().isEmpty()) {
                //Here Api Routing result
                kml = new KMLRoute.Builder().from(response.getResult());
            }
            logger.debug("Here is the generated KML file: \n " + kml);
        } else {
            logger.error("No KML-File generation possible, because we have an empty response from API! Check log above for detailed information.");
        }
        return kml;
    }

    private String prepareQuery(String query) {
        query = URLDecoder.decode(query, StandardCharsets.UTF_8);
        query = query.replace("query=", "");
        logger.info("WORK AROUND! Query = \"" + query + "\"");
        return query;
    }

    private NlpQueryResponse getNlpQueryResponse(String query) {
        NlpQueryResponse nlpQueryResponse;
        logger.info("Sending data to NLP...");
        String nlpResponse = nlpClient.sendToNlp(query);
        logger.info("...SUCCESS!");
        logger.info("NLP RESPONSE:");
        logger.info(nlpResponse);
        logger.info("INTERPRETED NLP RESPONSE:");
        nlpQueryResponse = new Gson().fromJson(nlpResponse, NlpQueryResponse.class);
        logger.info(nlpQueryResponse.toString());
        return nlpQueryResponse;
    }

    private ErrorResponse handleError(Throwable throwable) {
        logger.error(throwable.toString());
        return new ErrorResponse(Error.createError(throwable.getMessage(), Arrays.toString(throwable.getStackTrace())));
    }

    /**
     * This method will be called when Frontend requests the current version numbers of NLP and backend
     * @return The current versions of NLP and backend
     */
    @GetMapping("/version")
    @ResponseBody
    public HttpResponse handleVersionRequest() {
        logger.info("Version query received!");

        String nlpVersion;
        try {
            String nlpVersionResponse = nlpClient.fetchNlpVersion();
            logger.info("NLP VERSION RESPONSE:");
            logger.info(nlpVersionResponse);
            Gson g = new Gson();
            nlpVersion = g.fromJson(nlpVersionResponse, NlpVersionResponse.class).getVersion();
        } catch (Throwable t) {
            nlpVersion = "unknown";
        }

        return new VersionResponse(Version.createVersion(BACKEND_VERSION, nlpVersion));
    }

    /**
     * This method will be called when Frontend requests a specific KML-file
     * @param fileName the name of the specific KML-file
     * @return Returns the file to be opened or downloaded by the user
     * @throws IOException All kind of file-reading exceptions
     */
    @GetMapping("/kml")
    public ResponseEntity<ByteArrayResource> downloadKmlFile(
            @RequestParam String fileName) throws IOException {
        logger.info("File request received for: " + fileName);
        Path path;
        try {
            path = manager.searchForFileAndGetPath(fileName);
        } catch (WrongFileTypeException | FileNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        byte[] data = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                // Content-Type
                .contentType(MediaType.parseMediaType(MEDIA_TYPE_KML)) //
                // Content-Length
                .contentLength(data.length) //
                .body(resource);
    }
}