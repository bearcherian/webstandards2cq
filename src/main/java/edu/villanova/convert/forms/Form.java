package edu.villanova.convert.forms;

import edu.villanova.convert.forms.input.Input;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by bear on 12/25/14.
 */
public class Form {

    public static final String ACTION_INPUT = "U-Action";
    public static final String CONFIRMATION_INPUT = "U-Confirmation-Url";
    public static final String EMAIL_INPUT = "S-Email-Address";
    public static final String SUBJECT_INPUT = "S-Email-Subject";

    public static final String SAVE_FORM = "save-results";
    public static final String ARCHIVE_FORM = "archive-results";
    public static final String RESULTS_FORM = "show-results";

    private String source, action, method, id, enctype, name, confirmationPage;
    private boolean isArchiveForm = false, isResultsForm = false, hasSubmitButton = true, hasResetButton = false;
    private Element element;

    private ArrayList<String> emails = new ArrayList<>();
    private String subject;
    private ArrayList<Input> inputs;
    private String pageTitle,pageName;
    private String submitValue = "Submit",resetValue = "Reset";

    public Form(String action, String method) {

        this.action = action;
        this.method = method;
        inputs = new ArrayList<>();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isResultsForm() {
        return isResultsForm;
    }

    public void setResultsForm(boolean isResultsForm) {
        this.isResultsForm = isResultsForm;
    }

    public boolean isArchiveForm() {
        return isArchiveForm;
    }

    public void setArchiveForm(boolean isArchiveForm) {
        this.isArchiveForm = isArchiveForm;
    }

    public String getConfirmationPage() {
        return confirmationPage;
    }

    public void setConfirmationPage(String confirmationPage) {
        this.confirmationPage = confirmationPage;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    public void addEmail(String email) {
        this.emails.add(email);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnctype() {
        return enctype;
    }

    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ArrayList<Input> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<Input> textInputs) {
        this.inputs = textInputs;
    }

    public void addInputs(ArrayList<Input> textInputs) {
        this.inputs.addAll(textInputs);
    }

    public void addInput(Input textInput) {
        this.inputs.add(textInput);
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String title) {
        this.pageTitle = title;
    }

    public String toString() {
        StringBuilder formString = new StringBuilder("FORM\n");
        formString
                .append("\tSource:\t").append(source).append("\n")
                .append("\tName:\t").append(name).append("\n")
                .append("\tId:\t").append(id).append("\n")
                .append("\tAction:\t").append(action).append("\n")
                .append("\tMethod:\t").append(method).append("\n")
                .append("\tArchive:\t").append(isArchiveForm).append("\n")
                .append("\tResults:\t").append(isResultsForm).append("\n");


        if (inputs.isEmpty()) {
            formString.append("\tNO INPUTS\n");
        } else {
            formString.append("\tINPUTS:\n");
        }

        for (Input textInput : inputs) {
            formString
                    .append("\t\ttype: ").append(textInput.getType()).append("\n")
                    .append("\t\t\tlabel: ").append(textInput.getLabel()).append("\n")
                    .append("\t\t\tid: ").append(textInput.getId()).append("\n")
                    .append("\t\t\tname: ").append(textInput.getName()).append("\n")
                    .append("\t\t\tvalue: ").append(textInput.getValue()).append("\n");
        }

        return formString.toString();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getResetValue() {
        return resetValue;
    }

    public void setResetValue(String resetValue) {
        this.resetValue = resetValue;
    }

    public String getSubmitValue() {
        return submitValue;
    }

    public void setSubmitValue(String submitValue) {
        this.submitValue = submitValue;
    }

    public boolean isHasResetButton() {
        return hasResetButton;
    }

    public void setHasResetButton(boolean hasResetButton) {
        this.hasResetButton = hasResetButton;
    }

    public boolean isHasSubmitButton() {
        return hasSubmitButton;
    }

    public void setHasSubmitButton(boolean hasSubmitButton) {
        this.hasSubmitButton = hasSubmitButton;
    }
}
