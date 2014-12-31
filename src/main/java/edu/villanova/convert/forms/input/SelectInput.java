package edu.villanova.convert.forms.input;

import java.util.HashMap;

/**
 * Created by bear on 12/27/14.
 */
public class SelectInput extends Input {

    private HashMap<String,String> values = new HashMap<>();
    private boolean multiple = false;

    public SelectInput(){
        this.setType(Type.SELECT);
    }

    public HashMap<String, String> getValues() {
        return values;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
    }

    public void addValue(String key, String value) {

    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }
}
