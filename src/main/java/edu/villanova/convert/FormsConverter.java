package edu.villanova.convert;

import edu.villanova.convert.forms.Form;
import edu.villanova.convert.forms.input.Input;
import edu.villanova.convert.forms.input.MultiValueInput;
import edu.villanova.convert.forms.input.TextareaInput;
import edu.villanova.convert.report.FormReportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bear on 12/24/14.
 */
public class FormsConverter {

    Logger logger = LogManager.getLogger(FormsConverter.class);
    Logger reportLogger = LogManager.getLogger("FormReport");
    private File formsDirectory;
    private File csvFile;
    private ArrayList<Form> archiveFormsList = new ArrayList<>();
    private ArrayList<Form> resultsFormList = new ArrayList<>();
    private HashMap<String, Form> validForms = new HashMap<>();

    FormsConverter(String formsDirectoryPath, String csvFilePath) {
        this.formsDirectory = new File(formsDirectoryPath);
        this.csvFile = new File(csvFilePath);
    }

    /**
     * Originally 1341 HTML files, 723 pages with forms, 708 valid forms, 485 with archive option, 526 with download option
     */
    public HashMap<String, Form> processForms() {

        ArrayList<String> fileList = readCSVFile();
        logger.debug(fileList.size() + " forms to process");
        HashMap<String, Integer> vuTags = new HashMap<>();

        for (String filePath : fileList) {

            //process pages that have a form element
            if (FormUtil.containsForm(filePath)) {
                logger.debug("Parsing form in " + filePath);

                String formHTMLContent = CQUtil.getHTMLContent(filePath);
                Document formDOM = Jsoup.parseBodyFragment(formHTMLContent);
                //Parse Document stats
                FormReportUtil.addDomStats(formDOM);

                //Parse form
                Elements formElements = formDOM.select("form");
                for (Element formElement : formElements) {
                    Form form = FormUtil.getFormFromElement(formElement);
                    form.setSource(filePath.substring(filePath.indexOf("/forms")+1));
                    form.setPageTitle(formDOM.title());
                    form.setPageName(filePath.substring(filePath.lastIndexOf("/")+1,filePath.indexOf(".htm")));



                    if (form.isArchiveForm()) {
                        archiveFormsList.add(form);
                    } else if (form.isResultsForm()) {
                        resultsFormList.add(form);
                    } else {
                        logger.debug("Getting inputs");
                        form.addInputs(FormUtil.getInputsFromElement(formElement));
                        for (Input input : form.getInputs()) {
                            FormReportUtil.addInputStats(input);
                        }

                        validForms.put(filePath, form);
                    }

                }

            } else {
                logger.info("No <form> found in " + filePath);
            }

        }
        reportLogger.info("Valid Forms," + validForms.size());
        reportLogger.info("Archive Forms," + archiveFormsList.size());
        reportLogger.info("Result Forms," + resultsFormList.size());
        reportLogger.info("VUTags," + vuTags.size());

        return validForms;
    }

    private ArrayList<String> readCSVFile() {
        ArrayList<String> formFiles = new ArrayList<>();

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            String line = br.readLine();

            while (line != null) {
                String formPath = formsDirectory + line.split(",")[1].substring(6);
                if (!formFiles.contains(formPath.trim())) {
                    formFiles.add(formPath.trim());
                    logger.debug(formFiles.get(formFiles.size() - 1));
                }

                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            logger.error("Cannot find CSV file");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return formFiles;
    }

}
