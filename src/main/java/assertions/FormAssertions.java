package assertions;

import actions.FormActions;
import component.main.form.FormComp;
import data.DropdownOption;
import data.FormFieldLabel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FormAssertions {
    private final FormComp formComp;
    private final FormActions formActions;
    private final Map<FormFieldLabel, InputAssertions> actionsMap = new HashMap<>();

    public FormAssertions(FormComp formComp, FormActions formActions) {
        this.formComp = formComp;
        this.formActions = formActions;
    }

    private InputAssertions assertionsFor(FormFieldLabel field) {
        return actionsMap.computeIfAbsent(field, f ->
                new InputAssertions(formComp.fieldInputComp(f), formActions.actionsFor(f)));
    }

    public FormAssertions textInputEquals(String expected) {
        assertionsFor(FormFieldLabel.INPUT).inputValueEquals(expected);
        return this;
    }

    public FormAssertions numberInputEquals(String expected) {
        assertionsFor(FormFieldLabel.NUMBER_INPUT).inputValueEquals(expected);
        return this;
    }

    public FormAssertions pinInput(String expected) {
        assertionsFor(FormFieldLabel.PIN_INPUT).pinInput(expected);
        return this;
    }

    public FormAssertions dateInput(String expected) {
        assertionsFor(FormFieldLabel.INPUT_DATE).dateSingleInput(expected);
        return this;
    }

    public FormAssertions timeInput(String expected, String period) {
        assertionsFor(FormFieldLabel.INPUT_TIME).timeValue(expected, period);
        return this;
    }

    public FormAssertions tagsInput(List<String> expected) {
        assertionsFor(FormFieldLabel.TAGS_INPUT).tagItems(expected);
        return this;
    }

    public FormAssertions textArea(String expected) {
        assertionsFor(FormFieldLabel.TEXTAREA).textAreaEquals(expected);
        return this;
    }

    public FormAssertions inputMenu(DropdownOption option) {
        assertionsFor(FormFieldLabel.INPUT_MENU).inputValueEquals(option.label());
        return this;
    }

    public FormAssertions inputMenus(List<DropdownOption> options) {
        List<String> expected = options.stream()
                .map(DropdownOption::label)
                .toList();

        assertionsFor(FormFieldLabel.INPUT_MENU_MULTIPLE).tagItems(expected);
        return this;
    }

    public FormAssertions selectValue(DropdownOption option) {
        assertionsFor(FormFieldLabel.SELECT).selectValueEquals(option.label());
        return this;
    }

    public FormAssertions selectMenuValue(DropdownOption option) {
        assertionsFor(FormFieldLabel.SELECT_MENU).selectValueEquals(option.label());
        return this;
    }

    public FormAssertions selectMultipleValue(List<DropdownOption> options) {
        String expectedLabels = options.stream()
                .map(DropdownOption::label)
                .collect(Collectors.joining(", "));

        assertionsFor(FormFieldLabel.SELECT_MULTIPLE).selectValueEquals(expectedLabels);
        return this;
    }

    public FormAssertions selectMenusValue(List<DropdownOption> options) {
        String expectedLabels = options.stream()
                .map(DropdownOption::label)
                .collect(Collectors.joining(", "));

        assertionsFor(FormFieldLabel.SELECT_MENU_MULTIPLE).selectValueEquals(expectedLabels);
        return this;
    }

    public FormActions and() {
        return formActions;
    }
}
