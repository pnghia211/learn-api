package actions;

import component.main.table.FooterComp;
import data.HeaderColumnOption;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FooterActions {
    private final FooterComp footerComp;

    public FooterActions(FooterComp footerComp) {
        this.footerComp = footerComp;
    }

    public String getFooterSummaryTxt() {
        return footerComp.getFooterSummary().getText();
    }

    public String getFooterCellByIndex(int index) {
        return footerComp.getFooterCellByIndex(index).getText();
    }
}
