package model;

import data.DropdownOption;

import java.util.List;

public record FormTestData(
        String textInput,
        String numberMax,
        String numberMin,
        String pin,
        String date,
        String time,
        String period,
        List<String> tags,
        DropdownOption menuOption,
        List<DropdownOption> menuOptions,
        String textArea,
        DropdownOption selectOption,
        List<DropdownOption> selectOptions,
        DropdownOption selectMenuOption,
        List<DropdownOption> selectMenuOptions,
        DropdownOption listBoxOption,
        List<DropdownOption> listBoxOptions,
        String uploadFilePath,
        int ratingValue,
        DropdownOption checkboxGroupOption,
        DropdownOption radioGroupOption,
        int sliderValue
) {}
