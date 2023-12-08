package com.pa1.textdetectionapp.textdetectionapp.service;

import lombok.extern.slf4j.Slf4j;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class FileWriteService {

    // Method to write contents of a Map to a file
    public void writeMapToFile(Map<String, String> imageTextMap) {
        try {
            // Creating a FileWriter to write to ImageText.txt
            FileWriter fileWriter = new FileWriter("ImageText.txt");

            // Iterating over the map entries
            Iterator<Map.Entry<String, String>> mapIterator = imageTextMap.entrySet().iterator();
            while (mapIterator.hasNext()) {
                // Getting the key-value pair from the map
                Map.Entry<String, String> entry = mapIterator.next();

                // Writing the key-value pair to the file
                fileWriter.write(entry.getKey() + ":" + entry.getValue() + "\n");

                // Removing the entry from the map after writing
                mapIterator.remove();
            }

            // Closing the FileWriter
            fileWriter.close();

            // Logging success message
            log.info("Write operation complete, new file created: ImageText.txt");
        } catch (IOException e) {
            // Logging error message in case of an exception
            log.info("Error occurred while writing file");
            e.printStackTrace();
        }
    }
}
