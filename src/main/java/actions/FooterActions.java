package actions;

import component.main.table.FooterComp;

public class FooterActions extends BaseActions<FooterComp> {
    public FooterActions(FooterComp footerComp) {
        super(footerComp);
    }

    public String getFooterSummaryTxt() {
        return getComp().getFooterSummary().getText();
    }

    public String getFooterCellByIndex(int index) {
        return getComp().getFooterCellByIndex(index).getText();
    }
}
