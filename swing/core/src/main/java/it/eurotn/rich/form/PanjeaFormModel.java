package it.eurotn.rich.form;

import org.springframework.binding.MutablePropertyAccessStrategy;
import org.springframework.binding.form.support.DefaultFormModel;

public class PanjeaFormModel extends DefaultFormModel {

    private boolean adjustingMode = false;

    /**
     * Costruttore.
     *
     * @param domainObjectAccessStrategy
     *            strategy
     * @param bufferChanges
     *            bufferChanges
     */
    public PanjeaFormModel(final MutablePropertyAccessStrategy domainObjectAccessStrategy,
            final boolean bufferChanges) {
        super(domainObjectAccessStrategy, bufferChanges);
    }

    /**
     * @return the adjustingMode
     */
    public final boolean isAdjustingMode() {
        return adjustingMode;
    }

    /**
     * @param adjustingMode
     *            the adjustingMode to set
     */
    public final void setAdjustingMode(boolean adjustingMode) {
        this.adjustingMode = adjustingMode;
    }

}
