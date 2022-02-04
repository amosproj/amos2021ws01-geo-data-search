package com.example.backend.helpers;

import com.example.backend.data.kml.KML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This class manages the storage and retrieving of files containing KML-data. There is no limit of how many files will be stored.
 */
public class BackendFileManager {

    private final static String FILE_TYPE = "kml";
    private final static String SEP = File.separator;
    private final static String KML_FILES_DIRECTORY = "kml_files";
    private final static String DIRECTORY_FROM_ROOT = SEP + "app" + SEP + KML_FILES_DIRECTORY + SEP;
    private final Logger logger = LogManager.getLogger("BACKEND_FILE_MANAGER");
    private int fileCounter;

    public BackendFileManager() {
        if (!new File(KML_FILES_DIRECTORY).mkdir()) {
            logger.error("Could not create folder \"" + DIRECTORY_FROM_ROOT + "\"! No storage possible!");
        }
        fileCounter = 0;
    }

    /**
     * Saves given KML-data to a file in the directory {@value KML_FILES_DIRECTORY} in the format <strong>"kml_file_X.kml"</strong> and X replaced by the number of already saved files
     *
     * @param kml The data to be stored in the file
     * @return fileName of the saved file
     * @throws IOException In case we run into trouble while doing file-writing-operations
     */
    public String saveKmlFileAndReturnFileName(KML kml) throws IOException {
        logger.info("Trying to save KML...");
        XMLOutputter xmlOutputter = new XMLOutputter();
        String fileName = "kml_file_" + fileCounter + ".kml";
        FileWriter myWriter = new FileWriter(KML_FILES_DIRECTORY + SEP + fileName);
        xmlOutputter.setFormat(Format.getPrettyFormat());
        xmlOutputter.output(kml.getDoc(), myWriter);
        fileCounter++;
        myWriter.close();
        logger.info("Saved file \"" + fileName + "\" successfully!");
        return fileName;
    }

    /**
     * This method will try to find the requested {@value FILE_TYPE}-file in the internal file system.
     *
     * @param requestedFileName The name of the requested file, should be of type {@value FILE_TYPE}. If the requestedFileName does not contain any type, this method will attach the correct type automatically
     * @return The requested file when found
     * @throws WrongFileTypeException If the file is of another type as {@value FILE_TYPE}
     * @throws FileNotFoundException  If the requested file could not be found
     */
    public Path searchForFileAndGetPath(String requestedFileName) throws WrongFileTypeException, FileNotFoundException {
        String[] fileNameComponents = requestedFileName.split("\\.");
        if (fileNameComponents.length < 2) {
            requestedFileName += "." + FILE_TYPE;
        } else if (!fileNameComponents[1].equals(FILE_TYPE)) {
            throw new WrongFileTypeException("Unknown file type \"" + fileNameComponents[1] + "\"! We only support ." + FILE_TYPE + " files!");
        }
        Path requestedFile;
        if ((requestedFile = findPath(requestedFileName)) != null) {
            return requestedFile;
        } else {
            throw new FileNotFoundException("File \"" + requestedFileName + "\" not found!");
        }
    }

    private Path findPath(String requestedFileName) {
        File requestedFile = new File(DIRECTORY_FROM_ROOT + requestedFileName);
        if (requestedFile.exists() && requestedFile.isFile()) {
            return requestedFile.toPath();
        } else {
            return null;
        }
    }
}