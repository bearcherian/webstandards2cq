package edu.villanova.convert.forms;

import edu.villanova.convert.CQUtil;
import edu.villanova.convert.forms.input.Input;
import edu.villanova.convert.forms.input.MultiValueInput;
import edu.villanova.convert.forms.input.TextareaInput;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Created by bear on 1/1/15.
 */
public class FormXML {

    private static Logger logger = LogManager.getLogger(FormXML.class);

    public static String getFormXML(Form form) {
        HashMap<String, String> actions = new HashMap<>();
        String actionType = "foundation/components/form/actions/store";
        if (form.getAction().startsWith("/servlet/FormServlet") && !form.getEmails().isEmpty()) {
            actionType = "foundation/components/form/actions/storeandmail";
        }
        String toEmails = StringUtils.join(form.getEmails(), ",");

        int textFieldCount = 0, hiddenFieldCount = 0, selectFieldCount = 0, checkboxFieldCount = 0, radioFieldCount = 0;
        StringBuilder content = new StringBuilder();
        String contentStart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jcr:root xmlns:sling=\"http://sling.apache.org/jcr/sling/1.0\" xmlns:cq=\"http://www.day.com/jcr/cq/1.0\" xmlns:jcr=\"http://www.jcp.org/jcr/1.0\" xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\"\n" +
                "    jcr:primaryType=\"cq:Page\">\n" +
                "    <jcr:content\n" +
                "        cq:template=\"/apps/villanova/templates/formpage\"\n" +
                "        jcr:primaryType=\"cq:PageContent\"\n" +
                "        jcr:title=\"" + CQUtil.formattedString(form.getPageTitle()) + "\"\n" +
                "        sling:resourceType=\"villanova/components/page/contentpage\">\n" +
                "        <pagecontent\n" +
                "            jcr:primaryType=\"nt:unstructured\"\n" +
                "            sling:resourceType=\"foundation/components/parsys\">\n" +
                "            <start\n" +
                "                jcr:primaryType=\"nt:unstructured\"\n" +
                "                jcr:title=\"Form\"\n" +
                "                sling:resourceType=\"foundation/components/form/start\"\n" +
                "                action=\"/content/usergenerated/villanova/misc/forms/cq-gen136/\"\n" +
                "                actionType=\"" + CQUtil.formattedString(actionType) + "\"\n" +
                "                formid=\"_content_villanova_misc_forms_jcr_content_pagecontent_start\"\n" +
                "                mailto=\"[" + CQUtil.formattedString(toEmails) + "]\"\n" +
                "                subject=\"" + CQUtil.formattedString(form.getSubject()) + "\"\n" +
                "                from=\"noreply@villanova.edu\"/>\n";

        String contentEnd =
                "            <form_end\n" +
                        "                jcr:primaryType=\"nt:unstructured\"\n" +
                        "                sling:resourceType=\"foundation/components/form/end\"\n" +
                        "                reset=\"true\"\n" +
                        "                resetTitle=\"Reset\"\n" +
                        "                submit=\"true\"/>\n" +
                        "        </pagecontent>\n" +
                        "    </jcr:content>\n" +
                        "</jcr:root>\n";

        content.append(contentStart);

        for (Input input : form.getInputs()) {
            switch (input.getType()) {
                case TEL:
                case DATE:
                case TEXT:
                    content.append(getTextFieldXML(input, textFieldCount));
                    textFieldCount++;
                    break;
                case HIDDEN:
                    content.append(getHiddenFieldXML(input, hiddenFieldCount));
                    hiddenFieldCount++;
                    break;
                case SELECT:
                    content.append(getDropdownXML(input, selectFieldCount));
                    selectFieldCount++;
                    break;
                case TEXTAREA:
                    content.append(getTextFieldXML(input, textFieldCount));
                    textFieldCount++;
                    break;
                case CHECKBOX:
                    content.append(getCheckboxXml(input, checkboxFieldCount));
                    checkboxFieldCount++;
                    break;
                case RADIO:
                    content.append(getRadioXML(input, radioFieldCount));
                    radioFieldCount++;
                    break;
                case SUBMIT:
                    break;
                case RESET:
                    break;
                default:
                    logger.error("Unable to process input with type " + input.getType());
                    logger.error(input);
                    break;
            }
        }

        content.append(contentEnd);

        return content.toString();
    }

    private static String getHiddenFieldXML(Input input, int fieldCount) {
        String nodeName = "hiddenfield";
        if (fieldCount > 0) {
            nodeName = nodeName + "_" + fieldCount;
        }

        return "            <" + CQUtil.formattedString(nodeName) + "\n" +
                "                jcr:primaryType=\"nt:unstructured\"\n" +
                "                sling:resourceSuperType=\"foundation/components/form/defaults/field\"\n" +
                "                sling:resourceType=\"foundation/components/form/hiddenField\"\n" +
                "                defaultValue=\"" + CQUtil.formattedString(input.getValue()) + "\"\n" +
                "                name=\"" + CQUtil.formattedString(input.getName()) + "\"/>\n";
    }

    private static String getTextFieldXML(Input input, int fieldCount) {
        String nodeName = "text";
        if (fieldCount > 0) {
            nodeName = nodeName + "_" + fieldCount;
        }

        String textXML =
                "            <" + CQUtil.formattedString(nodeName) + "\n" +
                        "                jcr:primaryType=\"nt:unstructured\"\n" +
                        "                jcr:title=\"" + CQUtil.formattedString(input.getLabel()) + "\"\n" +
                        "                sling:resourceSuperType=\"foundation/components/form/defaults/field\"\n" +
                        "                sling:resourceType=\"foundation/components/form/text\"\n" +
                        "                name=\"" + CQUtil.formattedString(input.getName()) + "\"\n" +
                        "                defaultValue=\"" + CQUtil.formattedString(input.getValue()) + "\"\n" +
                        "                vuDefautlValue=\"" + CQUtil.formattedString(input.getVuValue()) + "\"\n" +
                        "                readOnly=\"" + input.isReadOnly() + "\"\n" +
                        "                required=\"" + input.isRequired() + "\"\n";
        if (input instanceof TextareaInput) {
            int rows = ((TextareaInput) input).getRows();
            int cols = ((TextareaInput) input).getCols();
            textXML += "                rows=\"" + (rows > 0 ? rows : "") + "\"\n";
            textXML += "                cols=\"" + (cols > 0 ? cols : "") + "\"\n";
        }

        textXML += "/>\n";

        if ("".equals(input.getLabel())){
            logger.warn("Empty label");
        }

        return textXML;
    }

    private static String getDropdownXML(Input input, int fieldCount) {
        String nodeName = "dropdown";
        if (fieldCount > 0) {
            nodeName = nodeName + "_" + fieldCount;
        }
        String optionsString = "";
        String comma = "";
        for (String key : ((MultiValueInput) input).getValues().keySet()) {
            optionsString += key + "=" + ((MultiValueInput) input).getValues().get(key) + comma;
            comma = ",";
        }

        return "            <" + CQUtil.formattedString(nodeName) + "\n" +
                "                jcr:primaryType=\"nt:unstructured\"\n" +
                "                jcr:title=\"" + CQUtil.formattedString(input.getLabel()) + "\"\n" +
                "                sling:resourceSuperType=\"foundation/components/form/defaults/field\"\n" +
                "                sling:resourceType=\"foundation/components/form/dropdown\"\n" +
                "                name=\"" + CQUtil.formattedString(input.getName()) + "\"\n" +
                "                options=\"[" + CQUtil.formattedString(optionsString) + "]\"\n" +
                "                unselectedText=\"\"/>\n";
    }

    private static String getCheckboxXml(Input input, int fieldCount) {
        String nodeName = "checkbox";
        if (fieldCount > 0) {
            nodeName = nodeName + "_" + fieldCount;
        }
        String optionsString = "";
        String comma = "";
        for (String key : ((MultiValueInput) input).getValues().keySet()) {
            optionsString += key + "=" + ((MultiValueInput) input).getValues().get(key) + comma;
            comma = ",";
        }

        return "            <" + CQUtil.formattedString(nodeName) + "\n" +
                "                jcr:primaryType=\"nt:unstructured\"\n" +
                "                jcr:title=\"" + CQUtil.formattedString(input.getLabel()) + "\"\n" +
                "                sling:resourceSuperType=\"foundation/components/form/defaults/field\"\n" +
                "                sling:resourceType=\"foundation/components/form/dropdown\"\n" +
                "                name=\"" + CQUtil.formattedString(input.getName()) + "\"\n" +
                "                options=\"[" + CQUtil.formattedString(optionsString) + "]\"/>\n";
    }

    private static String getRadioXML(Input input, int fieldCount) {
        String nodeName = "radio";
        if (fieldCount > 0) {
            nodeName = nodeName + "_" + fieldCount;
        }
        String optionsString = "";
        String comma = "";
        for (String key : ((MultiValueInput) input).getValues().keySet()) {
            optionsString += key + "=" + ((MultiValueInput) input).getValues().get(key) + comma;
            comma = ",";
        }

        return "            <" + CQUtil.formattedString(nodeName) + "\n" +
                "                jcr:primaryType=\"nt:unstructured\"\n" +
                "                jcr:title=\"" + CQUtil.formattedString(input.getLabel()) + "\"\n" +
                "                sling:resourceSuperType=\"foundation/components/form/defaults/field\"\n" +
                "                sling:resourceType=\"foundation/components/form/dropdown\"\n" +
                "                name=\"" + CQUtil.formattedString(input.getName()) + "\"\n" +
                "                options=\"[" + CQUtil.formattedString(optionsString) + "]\"/>\n";
    }

}
