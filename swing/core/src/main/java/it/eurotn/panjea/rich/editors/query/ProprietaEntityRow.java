package it.eurotn.panjea.rich.editors.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.DefaultExpandableRow;

import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.filter.OperatoreQuery;

public class ProprietaEntityRow extends DefaultExpandableRow {

    private ProprietaEntity proprieta;
    private String propertyLabel;

    /**
     * @param proprieta
     *            prop
     * @param defaultFilterProperties
     *            proprietà di cui settare il filtro di default ( = )
     * @param defaultSelectProperties
     *            proprietà di cui settare il valore di select
     */
    public ProprietaEntityRow(final ProprietaEntity proprieta, final String[] defaultFilterProperties,
            final String[] defaultSelectProperties) {
        super();
        this.proprieta = proprieta;
        propertyLabel = RcpSupport.getMessage(proprieta.getNome());
        if (StringUtils.isEmpty(propertyLabel)) {
            propertyLabel = proprieta.getNome().replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])",
                    "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
            propertyLabel = propertyLabel.substring(0, 1).toUpperCase() + propertyLabel.substring(1).toLowerCase();
        }

        if (ArrayUtils.contains(defaultFilterProperties, proprieta.getPropertyFullPath())) {
            proprieta.setOperatore(OperatoreQuery.UGUALE);
        }
        proprieta.setInSelect(ArrayUtils.contains(defaultSelectProperties, proprieta.getPropertyFullPath()));

        if (proprieta.getProprieta() != null && !proprieta.getProprieta().isEmpty()) {
            List<ProprietaEntityRow> tmp = new ArrayList<>();
            for (ProprietaEntity proprietaEntity : proprieta.getProprieta()) {
                ProprietaEntityRow chidRow = new ProprietaEntityRow(proprietaEntity, defaultFilterProperties,
                        defaultSelectProperties);
                chidRow.setParent(this);
                tmp.add(chidRow);
            }
            setChildren(tmp);
        }
    }

    /**
     * @return the propertyLabel
     */
    public String getPropertyLabel() {
        return propertyLabel;
    }

    /**
     * @return Returns the proprieta.
     */
    public ProprietaEntity getProprieta() {
        return proprieta;
    }

    @Override
    public Object getValueAt(int col) {
        switch (col) {
        case 0:
            return propertyLabel;
        case 1:
            return proprieta.isInSelect();
        case 2:
            return proprieta.getOperatore();
        case 3:
            return proprieta.getFiltro();
        default:
            return "n.p.";
        }
    }

    @Override
    public void setValueAt(Object value, int col) {
        if (col == 1) {
            proprieta.setInSelect((boolean) value);
        }
        if (col == 2) {
            proprieta.setOperatore((OperatoreQuery) value);
        }
        if (col == 3) {
            proprieta.setFiltro(value);
        }
        super.setValueAt(value, col);
    }

}
