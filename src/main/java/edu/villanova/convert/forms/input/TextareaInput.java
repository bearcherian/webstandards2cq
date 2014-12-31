package edu.villanova.convert.forms.input;

/**
 * Created by bear on 12/27/14.
 */
public class TextareaInput extends Input {

    private int rows, cols;

    public TextareaInput(){
        this.setType(Type.TEXTAREA);
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
