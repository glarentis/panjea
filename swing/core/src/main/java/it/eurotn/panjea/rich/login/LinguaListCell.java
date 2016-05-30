package it.eurotn.panjea.rich.login;

import java.util.Locale;

import javafx.scene.control.ListCell;

/**
 * @author fattazzo
 *
 */
public class LinguaListCell extends ListCell<Locale> {
    @Override
    protected void updateItem(Locale paramT, boolean paramBoolean) {
        super.updateItem(paramT, paramBoolean);
        if (paramT != null) {
            setText(paramT.getDisplayName());
        }
    }
}