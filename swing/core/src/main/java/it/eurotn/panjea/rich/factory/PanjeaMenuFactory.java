/**
 * 
 */
package it.eurotn.panjea.rich.factory;

import javax.swing.JPopupMenu;

import org.springframework.richclient.factory.DefaultMenuFactory;

import com.jidesoft.swing.JidePopupMenu;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 25/set/07
 * 
 */
public class PanjeaMenuFactory extends DefaultMenuFactory {

    @Override
    public JPopupMenu createPopupMenu() {
        return new JidePopupMenu();
    }

}
