package it.eurotn.panjea.bi.domain.analisi;

import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import org.hibernate.envers.Audited;

/**
 * Contiene i dati di un filtro impostato su un campo di analisi.
 *
 * @author giangi
 */
@Entity
@Audited
@DiscriminatorValue("F")
public class AnalisiFiltro extends AnalisiValueSelected {
	private static final long serialVersionUID = -999505072345074676L;

	private String nome;
	private String parametro1;
	private String parametro2;
	@Enumerated
	private TipoPeriodo tipoPeriodo;

	@Column(length = 50)
	private String filterFactoryName;

	/**
	 * @return Returns the filterFactoryName.
	 */
	public String getFilterFactoryName() {
		return filterFactoryName;
	}

	/**
	 * @return nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Parametri impostati nel filtro (parametro1 + parametro2).
	 */
	@Override
	public Object[] getParameter() {
		Object[] objects;
		if (parametro2 == null) {
			objects = new Object[1];
			objects[0] = parametro1;
		} else {
			objects = new Object[2];
			objects[0] = parametro1;
			objects[1] = parametro2;
		}
		return objects;
	}

	/**
	 * @return parametro1
	 */
	public String getParametro1() {
		return parametro1;
	}

	/**
	 * @return parametro2
	 */
	public String getParametro2() {
		return parametro2;
	}

	@Override
	public StringBuilder getSql(Colonna colonna, String aliasTabellaMisura) {
		StringBuilder whereSelected = new StringBuilder(60);
		whereSelected.append(" AND ");
		Periodo periodo = new Periodo();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (getParametro1() != null) {
				periodo.setDataIniziale(format.parse(getParametro1()));
			}
			if (getParametro2() != null) {
				periodo.setDataFinale(format.parse(getParametro2()));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		periodo.setTipoPeriodo(getTipoPeriodo());
		Date dataInizio = periodo.getDataIniziale();
		Date dataFinale = periodo.getDataFinale();
		if (dataInizio != null) {
			whereSelected.append(" AND ");
			whereSelected.append(colonna.getTabella().getAlias());
			whereSelected.append(".");
			whereSelected.append(colonna.getNome());
			whereSelected.append(">=");
			whereSelected.append("'");
			whereSelected.append(format.format(dataInizio));
			whereSelected.append("'");
		}
		if (dataFinale != null) {
			whereSelected.append(" AND ");
			whereSelected.append(colonna.getTabella().getAlias());
			whereSelected.append(".");
			whereSelected.append(colonna.getNome());
			whereSelected.append("<=");
			whereSelected.append("'");
			whereSelected.append(format.format(dataFinale));
			whereSelected.append("'");
		}
		return whereSelected;
	}

	/**
	 * @return Returns the tipoPeriodo.
	 */
	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	/**
	 * @param filterFactoryName
	 *            The filterFactoryName to set.
	 */
	public void setFilterFactoryName(String filterFactoryName) {
		this.filterFactoryName = filterFactoryName;
	}

	/**
	 * @param nome
	 *            nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public void setParameter(Object[] parameter) {
		throw new UnsupportedOperationException(
				"Non posso settare  i parametri in questa classe. Useare setParameter1 e setParameter2");
	}

	/**
	 * @param parametro1
	 *            parametro1
	 */
	public void setParametro1(String parametro1) {
		this.parametro1 = parametro1;
	}

	/**
	 * @param parametro2
	 *            parametro2 se presente
	 */
	public void setParametro2(String parametro2) {
		this.parametro2 = parametro2;
	}

	/**
	 * @param tipoPeriodo
	 *            The tipoPeriodo to set.
	 */
	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}
}
