package it.eurotn.rich.control.table;

import it.eurotn.rich.command.JideToggleCommand;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableUtils;

public class AutoResizePopupMenuCustomizer extends com.jidesoft.grid.AutoResizePopupMenuCustomizer {
    private class MenuCommand extends JideToggleCommand {
        public static final String COMMAND_ID = "moveHorrizontal";
        private JTableHeader header;
        private boolean selectedOnInit;

        /**
         * Command per abiltare l'auto resize delle colonne.
         * 
         * @param header
         *            header associato al menù
         */
        public MenuCommand(final JTableHeader header) {
            super(COMMAND_ID);
            this.header = header;
            RcpSupport.configure(this);
            this.selectedOnInit = true;
            this.setSelected(header.getTable().getAutoResizeMode() == JTable.AUTO_RESIZE_OFF);
            this.selectedOnInit = false;
        }

        @Override
        protected void onDeselection() {
            super.onDeselection();
            if (!selectedOnInit) {
                header.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                TableUtils.autoResizeAllColumns(header.getTable());
            }
        }

        @Override
        protected void onSelection() {
            super.onSelection();
            if (!selectedOnInit) {
                header.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                TableUtils.autoResizeAllColumns(header.getTable());
            }
        }

    }

    private MenuCommand command;

    @Override
    public void customizePopupMenu(JTableHeader header, JPopupMenu jpopupmenu, int clickingColumn) {
        super.customizePopupMenu(header, jpopupmenu, clickingColumn);
        if (command == null) {
            command = new MenuCommand(header);
        }
        jpopupmenu.add(command.createMenuItem());
    }
}
