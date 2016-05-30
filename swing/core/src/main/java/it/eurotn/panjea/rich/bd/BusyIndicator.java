package it.eurotn.panjea.rich.bd;

import java.awt.Cursor;

import org.springframework.richclient.application.Application;

/**
 * Support for showing a Busy Cursor during a long running process.
 */
public final class BusyIndicator {

    /**
     * Costruttore.
     *
     */
    private BusyIndicator() {
        super();
    }

    /**
     * Setta il cursore di default.
     *
     */
    public static void clearAt() {
        Application.instance().getWindowManager().getActiveWindow().getPage().getView("workspaceView").getControl()
                .setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Setta il cursore {@link Cursor#WAIT_CURSOR}.
     *
     */
    public static void showAt() {
        Application.instance().getWindowManager().getActiveWindow().getPage().getView("workspaceView").getControl()
                .setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

}
