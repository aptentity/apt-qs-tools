package com.aptentity.aptqstools.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static void copyStreamToFile(InputStream inputStream, File destFile) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && !destFile.canWrite()) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyStreamToFile(inputStream, destFile);
    }

    private static void doCopyStreamToFile(InputStream inputStream, File destFile) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int n = inputStream.read(buffer);
            while (n >= 0) {
                fos.write(buffer, 0, n);
                n = inputStream.read(buffer);
            }
        } finally {
            closeQuietly(fos);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
        }
    }

}
