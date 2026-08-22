package actions;

import component.main.form.InputComp;

import java.sql.Driver;

public class InputActions {
    private final InputComp inputComp;
    private final String inputLabel;

    public InputActions(InputComp inputComp, String inputLabel) {
        this.inputComp = inputComp;
        this.inputLabel = inputLabel;
    }

    public InputActions uploadFile(String filePath) {
        inputComp.fileInput(inputLabel).sendKeys(filePath);
        return this;
    }
}
