package edu.villanova.convert;

import edu.villanova.convert.forms.Form;
import edu.villanova.convert.forms.input.Input;
import edu.villanova.convert.forms.input.InputFactory;
import edu.villanova.convert.forms.input.MultiValueInput;
import edu.villanova.convert.forms.input.TextareaInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by bear on 12/26/14.
 */
public class FormUtil {

    private static Logger logger = LogManager.getLogger(FormUtil.class);

    public static Form getFormFromElement(Element formElement) {
        Form form;

        form = new Form(formElement.attr("action"),formElement.attr("method"));
        form.setElement(formElement);
        form.setId(formElement.id());
        form.setName(formElement.attr("name"));
        form.setEnctype(formElement.attr("enctype"));
        form.setMethod(formElement.attr("method"));
        form.setAction(formElement.attr("action"));

        Elements actionInputs = formElement.select("input[name=" + Form.ACTION_INPUT + "]");
        Elements confirmationPageInputs = formElement.select("input[name=" + Form.CONFIRMATION_INPUT + "]");
        Elements emailInputs = formElement.select("input[name=" + Form.EMAIL_INPUT + "]");
        Elements subjectInputs = formElement.select("input[name=" + Form.SUBJECT_INPUT + "]");

        Elements submitInputs = formElement.select("input[name="+Input.Type.SUBMIT.getTypeValue()+"]");
        form.setHasSubmitButton(!submitInputs.isEmpty());
        if (form.isHasSubmitButton()) {
            form.setSubmitValue(submitInputs.get(0).val());
        }
        Elements resetInputs = formElement.select("input[name="+Input.Type.RESET.getTypeValue()+"]");
        form.setHasResetButton(!resetInputs.isEmpty());
        if (form.isHasResetButton()) {
            form.setResetValue(resetInputs.get(0).val());
        }

        try {
            switch (actionInputs.get(0).val()) {
                case Form.ARCHIVE_FORM:
                    form.setArchiveForm(true);
                    break;
                case Form.RESULTS_FORM:
                    form.setResultsForm(true);
                    break;
            }

        } catch (IndexOutOfBoundsException ignored) { }

        if (!confirmationPageInputs.isEmpty()){
            form.setConfirmationPage(confirmationPageInputs.get(0).val());
        }

        for(Element emailInput : emailInputs) {
            form.addEmail(emailInput.val());
        }

        if (!subjectInputs.isEmpty()) {
            form.setSubject(subjectInputs.get(0).val());
        }

        return form;
    }

    public static ArrayList<Input> getInputsFromElement(Element formElement) {
        ArrayList<Input> textInputs = new ArrayList<>();
        Elements inputElements = formElement.select("input, textarea, select");
        ArrayList<String> checkboxNames = new ArrayList<>();
        ArrayList<String> radioNames = new ArrayList<>();

        for (Element inputElement : inputElements) {
//            logger.info("Parsing " + inputElement.tagName() + " : " + inputElement.attr("type") + " : " + inputElement.attr("name"));
            Input input = getInputFromElement(inputElement);

            if (input != null) {
                //Get Label
                if (!"".equals(input.getId())) {
                    Elements labelElements = formElement.select("label[for=" + input.getId() + "]");

                    if (!labelElements.isEmpty()) {
                        input.setLabel(labelElements.get(0).text());
                    }
                }

                if ("".equals(input.getLabel())) {

                        //if in a td, get the text from the previous td
                        if (inputElement.parent().tagName().equals("td")) {
                            logger.debug("Found td parent");
                            Element labelTd = inputElement.parent().previousElementSibling();
                            if (labelTd != null && labelTd.tagName().equals("td")) {
                                logger.debug("Getting text from prevElementSibling()");
                                input.setLabel(labelTd.text());
                            }
                        //if in a div.form_value, look for the div.form_label
                        } else if (inputElement.parent().tagName().equals("div") &&
                                inputElement.parent().hasClass("form_value")) {
                            Element formLabelEle = inputElement.parent().previousElementSibling();
                            if (formLabelEle != null && formLabelEle.hasClass("form_label") && formLabelEle.hasText()) {
                                input.setLabel(formLabelEle.text());
                            }

                        }
                }

                if (!input.getType().equals(Input.Type.SUBMIT) &&
                        !input.getType().equals(Input.Type.RESET) &&
                        !input.getType().equals(Input.Type.HIDDEN) &&
                        "".equals(input.getLabel())) {
                    logger.warn("Empty Label");
                }

                //Get options for Select elements
                if(input instanceof MultiValueInput) {

                    //Select inputs
                    if (input.getType().equals(Input.Type.SELECT)) {
                        ((MultiValueInput) input).setMultiple(inputElement.hasAttr("multiple"));
                        for (Element optionElement : inputElement.select("option")) {
                            ((MultiValueInput) input).addValue(optionElement.text(), optionElement.val());
                        }

                    //checkbox and radio inputs
                    } else {
                        if (!checkboxNames.contains(input.getName()) || !radioNames.contains(input.getName())) {
                            //get inputs in the DOM that contain the same name
                            for (Element namedElement : inputElement.ownerDocument().select("input[name=" + input.getName() + "]")) {
                                String elementText = "";
                                if (namedElement.nextElementSibling() != null && !namedElement.nextElementSibling().isBlock()) {
                                    elementText = namedElement.nextElementSibling().text();
                                }

                                //Add the value
                                ((MultiValueInput) input).addValue(elementText, namedElement.val());

                                //add the name to the list of completed items
                                if (input.getType().equals(Input.Type.CHECKBOX)) {
                                    checkboxNames.add(input.getName());
                                }
                                if (input.getType().equals(Input.Type.RADIO)) {
                                    radioNames.add(input.getName());
                                }
                            }
                        }

                    }
                }

                //Get attributes of textarea elements
                if (input instanceof TextareaInput) {
                    if (inputElement.hasAttr("rows")){
                        ((TextareaInput) input).setRows(Integer.parseInt(inputElement.attr("rows")));
                    }
                    if (inputElement.hasAttr("cols")) {
                        ((TextareaInput) input).setCols(Integer.parseInt(inputElement.attr("cols")));
                    }
                    input.setValue(inputElement.ownText());
                }

                textInputs.add(input);
            }

        }

        return textInputs;
    }

    private static Input getInputFromElement(Element element) {
        String inputName = element.attr("name");
        String inputType = element.attr("type");
        if (inputName.startsWith("U-") || inputName.startsWith("S-") ||
                inputName.equals("rname") || inputName.equals("dname")) {
            return null;
        }
        Input input = InputFactory.getInput(element);

        if (input != null) {
            input.setType(element.attr("type"));
            input.setId(element.id());
            input.setName(element.attr("name"));
            input.setRequired(element.hasAttr("required"));
            input.setDisabled(element.hasAttr("disabled"));
            input.setReadOnly(element.hasAttr("readonly"));
            input.setChecked(element.hasAttr("checked"));
            input.setPlaceholder(element.attr("placeholder"));
            input.setAlt(element.attr("alt"));
            input.setAutoComplete("on".equals(element.attr("autocomplete")));
            input.setMaxLength("".equals(element.attr("maxlength")) ? 0 : Integer.parseInt(element.attr("maxlength")));

            String inputValue = element.val();

            if (inputValue.contains("<vu:")) {
                boolean vuValueMatched = false;
                for (Input.VuValue vuValue : Input.VuValue.values()) {
                    if (inputValue.startsWith(vuValue.getVuTagValue())){
                        input.setVuValue(vuValue.getProfileAttr());
                        vuValueMatched = true;
                        break;
                    }
                }

                if (!vuValueMatched){
                    logger.error("VuValue match error on " + inputValue);
                }
            } else {
                input.setValue(element.val());
            }


            //Get text after the current input
            if (element.nextElementSibling() != null && !element.nextElementSibling().isBlock()) {
                input.setText(element.nextElementSibling().toString());
            }
        }

        return input;
    }

    public static boolean containsForm(String filePath) {

        String pageHTMLContent = CQUtil.getHTMLContent(filePath);

        return pageHTMLContent != null && pageHTMLContent.contains("<form");

    }
}
