package edu.villanova.convert.forms.input;

import org.jsoup.nodes.Element;

/**
 * Created by bear on 12/27/14.
 */
public class InputFactory {

    public static Input getInput(Element element){
        String tagName = element.tagName();
        String type = element.attr("type").toLowerCase();
        Input input = null;
        if (tagName == null) {
            return null;
        }

        if (tagName.equalsIgnoreCase("select") || "checkbox".equals(type) || "radio".equals(type)) {
            input = new MultiValueInput();
        } else if (tagName.equalsIgnoreCase("textarea")) {
            input = new TextareaInput();
        } else if(tagName.equalsIgnoreCase("input")){
            input =  new Input();
        }

            return input;
    }
}
