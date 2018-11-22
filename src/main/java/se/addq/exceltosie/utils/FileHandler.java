package se.addq.exceltosie.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;


public class FileHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void addLineToFile(String path, String data, Charset charset) {
        Path filePath = Paths.get(path);
        try (OutputStreamWriter out = new OutputStreamWriter(
                Files.newOutputStream(filePath, CREATE, APPEND), charset)) {
            out.write(data);
        } catch (IOException e) {
            LOG.error("Could add line to file with path {}", path, e);
        }
    }


}
