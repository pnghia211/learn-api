package data;

public enum FormFieldLabel {
    INPUT("Input"),
    INPUT_NUMBER("InputNumber"),
    PIN_INPUT("PinInput"),
    INPUT_DATE("InputDate"),
    INPUT_TIME("InputTime"),
    INPUT_TAGS("InputTags"),
    INPUT_MENU("InputMenu"),
    INPUT_MENU_MULTIPLE("InputMenu (Multiple)"),
    TEXTAREA("Textarea"),
    SELECT("Select"),
    SELECT_MULTIPLE("Select (Multiple)"),
    SELECT_MENU("SelectMenu"),
    SELECT_MENU_MULTIPLE("SelectMenu (Multiple)"),
    LISTBOX("Listbox"),
    LISTBOX_MULTIPLE("Listbox (Multiple)"),
    FILE_UPLOAD("FileUpload"),
    CHECKBOX("Checkbox"),
    SWITCH("Switch"),
    SLIDER("Slider"),
    INPUT_RATING("InputRating"),
    CHECKBOX_GROUP("CheckboxGroup"),
    RADIO_GROUP("RadioGroup");

    private final String label;

    FormFieldLabel(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
