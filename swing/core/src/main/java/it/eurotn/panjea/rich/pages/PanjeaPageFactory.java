/**
 * 
 */
package it.eurotn.panjea.rich.pages;

import org.springframework.richclient.application.*;

/**
 * Estensione al singolo scopo di ritornare nella docking application window una application page di tipo
 * PanjeaDockingApplicationPage
 * 
 * @author Leonardo
 */
public class PanjeaPageFactory implements ApplicationPageFactory {

    @Override
    public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor pageDescriptor) {

        ApplicationPage page = new PanjeaDockingApplicationPage(window, pageDescriptor);
        return page;
    }
}
