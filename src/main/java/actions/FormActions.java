package actions;

import assertions.FormAssertions;
import component.main.form.FormComp;
import data.DropdownOption;
import data.FormFieldLabel;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FormActions {
    FormComp formComp;
    private final Map<FormFieldLabel, InputActions> actionsCache = new EnumMap<>(FormFieldLabel.class);

    public FormActions(FormComp formComp) {
        this.formComp = formComp;
    }

    public InputActions actionsFor(FormFieldLabel field) {
        return actionsCache.computeIfAbsent(field, f -> new InputActions(formComp.fieldInputComp(f)));
    }

    public FormActions fillTextInput(String text) {
        actionsFor(FormFieldLabel.INPUT).type(text);
        return this;
    }

    public FormActions increaseInputNumberTo(String number) {
        actionsFor(FormFieldLabel.NUMBER_INPUT).increaseInputTo(number);
        return this;
    }

    public FormActions decreaseInputNumberTo(String number) {
        actionsFor(FormFieldLabel.NUMBER_INPUT).decreaseInputTo(number);
        return this;
    }

    public FormActions fillPinInput(String value) {
        actionsFor(FormFieldLabel.PIN_INPUT).typeInputs(value);
        return this;
    }

    public FormActions fillInputDate(String date) {
        actionsFor(FormFieldLabel.INPUT_DATE).fillDate(date);
        return this;
    }
    public FormActions fillInputTime(String time, String period) {
        actionsFor(FormFieldLabel.INPUT_TIME).fillTime(time, period);
        return this;
    }

    public FormActions inputTags(List<String> inputs) {
        actionsFor(FormFieldLabel.TAGS_INPUT).inputTags(inputs);
        return this;
    }

    public FormActions selectDropdownOption(DropdownOption option) {
        actionsFor(FormFieldLabel.SELECT).selectDropdownOpt(option);
        return this;
    }

    public FormActions selectDropdownOptions(List<DropdownOption> options) {
        actionsFor(FormFieldLabel.SELECT_MULTIPLE).selectMultiDropdownOpt(options);
        return this;
    }

    public FormActions selectMenuDropdownOption(DropdownOption option) {
        actionsFor(FormFieldLabel.SELECT_MENU).selectDropdownOpt(option);
        return this;
    }

    public FormActions selectMenuDropdownOptions(List<DropdownOption> options) {
        actionsFor(FormFieldLabel.SELECT_MENU_MULTIPLE).selectMultiDropdownOpt(options);
        return this;
    }

    public FormActions selectListBoxOption(DropdownOption option) {
        actionsFor(FormFieldLabel.LISTBOX).selectListBoxOption(option);
        return this;
    }

    public FormActions selectListBoxOptions(List<DropdownOption> options) {
        actionsFor(FormFieldLabel.LISTBOX_MULTIPLE).selectListBoxOptions(options);
        return this;
    }

    public FormActions uploadFile(String path) {
        actionsFor(FormFieldLabel.FILE_UPLOAD).uploadFile(path);
        return this;
    }

    public FormActions selectCheckbox() {
        actionsFor(FormFieldLabel.CHECKBOX).selectCheckbox();
        return this;
    }

    public FormActions selectRating(int value) {
        actionsFor(FormFieldLabel.INPUT_RATING).selectInputRating(value);
        return this;
    }

    public FormActions clickSwitchToggle() {
        actionsFor(FormFieldLabel.SWITCH).clickSwitchToogle();
        return this;
    }

    public FormActions selectCheckboxGroup(DropdownOption option) {
        actionsFor(FormFieldLabel.CHECKBOX_GROUP).clickCheckboxGroup(option);
        return this;
    }

    public FormActions selectRadioGroup(DropdownOption option) {
        actionsFor(FormFieldLabel.RADIO_GROUP).clickCheckboxGroup(option);
        return this;
    }

    public FormActions setSliderTo(int target) {
        actionsFor(FormFieldLabel.SLIDER).setSliderTo(target);
        return this;
    }

    public FormActions fillTextArea(String text) {
        actionsFor(FormFieldLabel.TEXTAREA).typeInTextArea(text);
        return this;
    }

    public FormActions inputMenu(DropdownOption option) {
        actionsFor(FormFieldLabel.INPUT_MENU).inputMenu(option);
        return this;
    }

    public FormActions inputMenus(List<DropdownOption> options) {
        actionsFor(FormFieldLabel.INPUT_MENU_MULTIPLE).inputMenus(options);
        return this;
    }

    public FormActions clickSubmitBtn() {
        formComp.submit().click();
        return this;
    }

    public String getToastText() {
        return formComp.toastTitle().getText();
    }

    public FormAssertions verify() {
        return new FormAssertions(formComp, this);
    }
}
