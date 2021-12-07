package com.github.iluvpy.badapple.registry;

import java.io.*;

public class BadAppleFolder {

    public static final String READING_ERROR = "ERR";
    public static final String NO_FILE_ERR = "NFERR";

    // returns true if the BadApple folder exists and false if not or if an error occurred
    public boolean exists() {
        try {
            File badAppleFolder = new File("BadApple");
            return badAppleFolder.exists();
        } catch (Exception e) {
            return false;
        }
    }

    // reads the first file inside the BadApple folder
    // returns the file content in case a file exists
    // returns BadAppleFolder.NO_FILE_ERR in case that no file was found
    // returns BadAppleFolder.READING_ERR in case an error occurs while reading
    public String read() {
        File badAppleFolder = new File("BadApple");
        File files[] = badAppleFolder.listFiles();
        StringBuilder content = new StringBuilder();
        if (files.length != 0) {
            try {
                BufferedReader buffer = new BufferedReader(new FileReader(files[0]));
                String temp;
                while ((temp = buffer.readLine()) != null) {
                    content.append(temp);
                }
                buffer.close();
                return content.toString();
            } catch (Exception e) {
                return READING_ERROR;
            }
        }

        return NO_FILE_ERR;
    }

}
