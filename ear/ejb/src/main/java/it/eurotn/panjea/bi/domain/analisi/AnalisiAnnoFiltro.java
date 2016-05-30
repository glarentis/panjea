package it.eurotn.panjea.bi.domain.analisi;

import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * Contiene i dati di un filtro impostato su un campo di analisi.
 *
 * @author giangi
 */
@Entity
@Audited
@DiscriminatorValue("Y")
public class AnalisiAnnoFiltro extends AnalisiValueSelected {
	private static final long serialVersionUID = -999505072345074676L;

	@Override
	public StringBuilder getSql(Colonna colonna, String aliasTabellaMisura) {
		Object[] valoriDaFiltrare = getParameter();
		StringBuilder whereSelected = new StringBuilder(100);
		String or = "";

		whereSelected.append(" AND ");
		for (Object anno : valoriDaFiltrare) {
			whereSelected.append(or);
			whereSelected.append(aliasTabellaMisura);
			whereSelected.append(" .dataRegistrazione between '");
			whereSelected.append(anno.toString());
			whereSelected.append("0101' ");
			whereSelected.append(" AND '");
			whereSelected.append(anno.toString());
			whereSelected.append("1231'");
			or = " OR ";
		}
		return whereSelected;
	}
}
