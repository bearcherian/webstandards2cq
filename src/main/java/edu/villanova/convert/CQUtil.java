package edu.villanova.convert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by bcherian on 12/22/14.
 */
public class CQUtil {

    public static String formattedString(String contentString) {

        return contentString.replace("&","&amp;").replace("\"","&quot;").replace("<","&lt;");
    }

    public static String getHTMLContent(String filename) {
        try( BufferedReader br =  new BufferedReader( new FileReader(filename) ) ) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null) {
                sb.append(line);
                line = br.readLine();
            }

            return sb.toString();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
