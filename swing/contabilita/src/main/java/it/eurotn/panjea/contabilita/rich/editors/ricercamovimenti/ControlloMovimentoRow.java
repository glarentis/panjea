package it.eurotn.panjea.contabilita.rich.editors.ricercamovimenti;

import java.util.ArrayList;
import java.util.List;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.AbstractExpandableRow;
import com.jidesoft.grid.Row;

import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;

public class ControlloMovimentoRow extends AbstractExpandableRow implements Comparable<AreaContabileDTO> {

    public enum ERowType {
        AREA, RIGA
    }

    private AreaContabileDTO areaContabileDTO;

    private List<ControlloMovimentoRow> children;

    private ERowType rowType;

    /**
     * Costruttore.
     *
     * @param areaContabileDTO
     *            areaContabileDTO
     */
    public ControlloMovimentoRow(final AreaContabileDTO areaContabileDTO) {
        this(areaContabileDTO, ERowType.AREA);
    }

    /**
     * Costruttore.
     *
     * @param areaContabileDTO
     *            areaContabileDTO
     * @param rowType
     *            tipo di riga ( area o riga contabile )
     */
    public ControlloMovimentoRow(final AreaContabileDTO areaContabileDTO, final ERowType rowType) {
        super();
        this.areaContabileDTO = areaContabileDTO;
        this.rowType = rowType;
    }

    @Override
    public int compareTo(AreaContabileDTO o) {
        if (areaContabileDTO.getClass().getName().compareTo(o.getClass().getName()) != 0) {
            return areaContabileDTO.getClass().getName().compareTo(o.getClass().getName());
        } else {
            return areaContabileDTO.getId().compareTo(o.getId());
        }
    }

    /**
     * @return Returns the areaContabileDTO.
     */
    public AreaContabileDTO getAreaContabileDTO() {
        return areaContabileDTO;
    }

    @Override
    public List<?> getChildren() {
        if (children != null) {
            return children;
        }

        List<ControlloMovimentoRow> rowChildren = new ArrayList<ControlloMovimentoRow>();

        for (RigaContabileDTO rigaContabileDTO : areaContabileDTO.getRigheContabili()) {
            rowChildren.add(new ControlloMovimentoRow(rigaContabileDTO, ERowType.RIGA));
        }

        setChildren(rowChildren);

        return children;
    }

    /**
     * @return Returns the rowType.
     */
    public ERowType getRowType() {
        return rowType;
    }

    @Override
    public Object getValueAt(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return areaContabileDTO.getStatoAreaContabile();
        case 1:
            return areaContabileDTO.getDataRegistrazione();
        case 2:
            return areaContabileDTO.getDataDocumento();
        case 3:
            return areaContabileDTO.getNumeroDocumento();
        case 4:
            return areaContabileDTO.getRegistroProtocollo();
        case 5:
            return areaContabileDTO.getNumeroProtocollo();
        case 6:
            if (this.rowType == ERowType.AREA) {
                return ObjectConverterManager.toString(areaContabileDTO.getTipoDocumento());
            }
            return ((RigaContabileDTO) areaContabileDTO).getCodiceSottoConto() + " - "
                    + ((RigaContabileDTO) areaContabileDTO).getDescrizioneSottoConto();
        case 7:
            return areaContabileDTO.getNote();
        case 8:
            return areaContabileDTO.getSbilancio();
        case 9:
            return areaContabileDTO.getImportoDare();
        case 10:
            return areaContabileDTO.getImportoAvere();
        default:
            return null;
        }
    }

    @Override
    public boolean hasChildren() {
        return !areaContabileDTO.getRigheContabili().isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setChildren(List<?> paramChildren) {
        children = (List<ControlloMovimentoRow>) paramChildren;
        if (children != null) {
            for (Object row : children) {
                if (row instanceof Row) {
                    ((Row) row).setParent(this);
                }
            }
        }
    }

}
