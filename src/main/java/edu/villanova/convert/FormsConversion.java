package edu.villanova.convert;

import edu.villanova.convert.forms.Form;
import edu.villanova.convert.forms.FormXML;
import edu.villanova.convert.report.FormReportUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by bcherian on 12/24/14.
 */
public class FormsConversion {

    private static final String FORMS_DIRECTORY = "/Users/bear/Documents/Villanova/forms";
    private static final String TARGET_DIRECTORY = "/Users/bear/cq/vltWorkspace/villanova/jcr_root/misc/";
    private static final String CSV_FILE = "/Users/bear/Documents/Villanova/www_references.csv";

    private static Logger logger = LogManager.getLogger(FormsConversion.class);



    public static void main(String[] args) {
        logger.info("Forms conversion started");

        FormsConverter formsConverter = new FormsConverter(FORMS_DIRECTORY,CSV_FILE);

        HashMap<String, Form> forms = formsConverter.processForms();

        FormReportUtil.logDomStats();
        FormReportUtil.logInputStats();
        FormReportUtil.logLDAPStats();

        for (String formKey : forms.keySet()) {
            Form form = forms.get(formKey);
            String targetPath = TARGET_DIRECTORY + form.getSource().substring(0,form.getSource().lastIndexOf(".htm"));

            String formXml = FormXML.getFormXML(form);

            File formDir = new File(targetPath);
            logger.info(targetPath);
            logger.debug("Making directory  - " + formDir.mkdirs());
            String formContentPath = formDir.getAbsolutePath() + "/.content.xml";
            logger.debug("Writing " +formContentPath);
            try {
                PrintWriter pw = new PrintWriter(formContentPath,"UTF-8");
                pw.write(formXml);
                pw.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }

        logger.info("Forms conversion completed");


    }


}
