package edu.villanova.convert.forms.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bear on 12/26/14.
 */
public class Input {

    private String text;
    private int maxLength;
    private boolean checked;
    private String value, name, id, label, placeholder, alt;
    private boolean disabled = false, readOnly = false, required = false, autoComplete = true;
    private Type type;

    private Logger logger = LogManager.getLogger(Input.class);
    public Input() {

    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public Type getType() {
        return type;
    }

    public void setType(String typeValue) {
        for (Type type : Type.values()){
            if (typeValue.toLowerCase().equals(type.getTypeValue())){
                this.type = type;
            }
        }

        if (this.type == null) {
            this.type = Type.TEXT;
            logger.info("Set type value of '" + typeValue + "' to Type.TEXT");
        }
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public boolean isAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public enum Type{
        TEXT("text"),
        PASSWORD("password"),
        RADIO("radio"),
        CHECKBOX("checkbox"),
        SUBMIT("submit"),
        BUTTON("button"),
        COLOR("color"),
        DATE("date"),
        DATETIME("datetime"),
        DATETIME_LOCAL("datetime-local"),
        EMAIL("email"),
        MONTH("month"),
        NUMBER("number"),
        RANGE("range"),
        SEARCH("search"),
        TEL("tel"),
        TIME("time"),
        URL("url"),
        WEEK("week"),
        HIDDEN("hidden"),
        RESET("reset"),
        TEXTAREA("textarea"),
        SELECT("select");

        private String typeValue;
        Type(String type) {
            this.typeValue = type;
        }

        public String getTypeValue() {
            return typeValue;
        }
    }

}
