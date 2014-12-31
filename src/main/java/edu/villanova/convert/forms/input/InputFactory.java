package edu.villanova.convert.forms.input;

import org.jsoup.nodes.Element;

/**
 * Created by bear on 12/27/14.
 */
public class InputFactory {

    public static Input getInput(Element element){
        String tagName = element.tagName();
        Input input = null;
        if (tagName == null) {
            return null;
        }

        if(tagName.equalsIgnoreCase("input")){
            input =  new Input();
        } else if (tagName.equalsIgnoreCase("select")) {
            input = new SelectInput();
        } else if (tagName.equalsIgnoreCase("textarea")) {
            input = new TextareaInput();
        }

        return input;
    }
}
