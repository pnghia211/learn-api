package actions;

import component.main.BaseComp;

public class BaseActions<T extends BaseComp> {
    private final T baseComp;

    public BaseActions(T baseComp) {
        this.baseComp = baseComp;
    }

    protected T getComp() {
        return baseComp;
    }
}
