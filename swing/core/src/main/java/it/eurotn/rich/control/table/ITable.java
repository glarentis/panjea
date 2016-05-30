package it.eurotn.rich.control.table;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.springframework.rules.constraint.Constraint;

import com.jidesoft.grid.TableHeaderPopupMenuInstaller;

public interface ITable<T> {

    /**
     * Verifica se ho cliccato sulla colonna voluta. Se clicco sul simbolo di espansione di un raggruppamento ritorno
     * false.
     * 
     * @param mouseevent
     *            .
     * @param actualColumn
     *            colonna da testare
     * @return true se ho cliccato in una cella della actualColumn.
     */
    boolean checkColumn(MouseEvent mouseevent, int actualColumn);

    /**
     * 
     * @param visualColumnIndex
     *            indeice della colonna visuale
     * @return indice della colonna nel modello sottostante
     */
    int getActualColumn(int visualColumnIndex);

    /**
     * Restituisce tutti gli indici delle righe dell'inner table model dagli indici visuali di riga e colonna.
     * 
     * @param visualRowIndex
     *            indice visuale della riga
     * @param visualColumnIndex
     *            indice visuale della colonna
     * @return indici relativi all'inner table model
     */
    int[] getActualRowsAt(int visualRowIndex, int visualColumnIndex);

    /**
     * Restituisce il {@link Rectangle} che rappresenta le coordinate dell'icona per la cella.
     * 
     * @param visualRowIndex
     *            indice della riga
     * @param visualColumnIndex
     *            indice della colonna
     * @return {@link Rectangle}
     */
    Rectangle getIconCellRect(int visualRowIndex, int visualColumnIndex);

    /**
     * 
     * @return tag first object that are currently selected
     */
    T getSelectedObject();

    /**
     * @return List of objects that are currently selected
     */
    List<T> getSelectedObjects();

    /**
     * 
     * @param table
     *            tabella di riferimento
     * @return table header da installare nella tabella. Il table header viene installato nel
     *         {@link JideTableCustomizer}
     */
    JTableHeader getTableHeader(JTable table);

    /**
     * Implementato se la tabella che implementa l'interfaccia deve avere dei menù personalizzati.
     * 
     * @param installer
     *            installer per installare i menù
     */
    void installMenu(TableHeaderPopupMenuInstaller installer);

    /**
     * carico il layout corrente da uno stream.
     * 
     * @param stream
     *            steam da dove caricare il layout
     */
    void loadLayout(InputStream stream);

    /**
     * salvo il layout corrente su uno stream.
     * 
     * @param stream
     *            steam dove salvare il layout
     */
    void saveLayout(OutputStream stream);

    /**
     * Seleziona l'oggetto nella tabella in base all'indice.
     * 
     * @param innerIndexModel
     *            indice dell'oggetto da selezionare riferito al modello più interno
     */
    void selectRowObject(int innerIndexModel);

    /**
     * 
     * @param columns
     *            colonne da aggregare nella tabella
     */
    void setAggregatedColumns(String[] columns);

    /**
     * @param changeSelectionConstraint
     *            the changeSelectionConstraint to set
     */
    void setChangeSelectionConstraint(Constraint changeSelectionConstraint);

    /**
     * 
     * @param editable
     *            rende le celle editable non editable
     */
    void setEditable(boolean editable);
}