package actions;

import component.main.table.FooterComp;
import data.HeaderColumnOption;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FooterActions {
    private final String tableLabel;
    private final FooterComp footerComp;
    private final TableActions tableActions;

    public FooterActions(FooterComp footerComp, String tableLabel, TableActions tableActions) {
        this.tableLabel = tableLabel;
        this.footerComp = footerComp;
        this.tableActions = tableActions;
    }

    public String getFooterSummaryTxt() {
        return footerComp.getFooterSummary().getText();
    }

    public String getFooterTotalAmountTxt() {
        int index = tableActions.headerActions().getHeadersMap().get(HeaderColumnOption.AMOUNT.headerLabel());
        return footerComp.getFooterCellByIndex(index + 1).getText();
    }

    public Integer getFooterTotalAmount() {
        String result = getFooterTotalAmountTxt().replace("Total: €", "").replace(",", "");
        return new BigDecimal(result).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public TableActions and() {
        return tableActions;
    }
}
