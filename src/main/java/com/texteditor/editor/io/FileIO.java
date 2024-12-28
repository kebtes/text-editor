package com.texteditor.editor.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import com.texteditor.editor.Rope;

public class FileIO {
    /**
     * Saves the content of a Rope object to a file at the specified path.
     *
     * @param path     The directory path where the file should be saved.
     * @param data     The Rope object containing the data to be saved.
     * @param type     The type of the file to be saved (e.g., TXT).
     * @param fileName The name of the file (without extension).
     */
    @SuppressWarnings("all")
    public static void saveFile(String path, Rope data, FileType type, String fileName) {
        File file = null;

        try {
            // Determine the file type and create the appropriate file
            switch (type) {
                case TXT -> {
                    file = new File(path, fileName + ".txt");

                    // Ensure the parent directory exists
                    if (file.getParentFile() != null && !file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    // Create the file if it doesn't exist
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                }
            }

            // Ensure the file object is not null and write data to the file
            assert file != null;
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(data.getRopeData());
            }

        } catch (IOException e) {
            throw new RuntimeException("Error saving file. ");
        }
    }
}
