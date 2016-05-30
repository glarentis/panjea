package it.eurotn.panjea.rate.domain;

import it.eurotn.entity.EntityBase;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "part_righe_calendari_rate")
@NamedQueries({ @NamedQuery(name = "RigaCalendarioRate.caricaByCalendario", query = "select r from RigaCalendarioRate r where r.calendarioRate.id = :paramIdCalendario order by r.dataIniziale,r.dataFinale") })
public class RigaCalendarioRate extends EntityBase {

	private static final long serialVersionUID = -6122182349738680597L;

	@Temporal(TemporalType.DATE)
	private Date dataIniziale;

	@Temporal(TemporalType.DATE)
	private Date dataFinale;

	private boolean ripeti;

	@Temporal(TemporalType.DATE)
	private Date dataAlternativa;

	private String note;

	@ManyToOne
	private CalendarioRate calendarioRate;

	/**
	 * Restituisce la data alternativa a quella di riferimento. <code>null</code> se la data di riferimento non è nel
	 * range.
	 * 
	 * @param date
	 *            data di riferimento
	 * @return data calcolata
	 */
	public Date calcolaDataAlternativa(Date date) {

		Date dataAlternativaCalcolata = null;

		if (isInRange(date)) {

			Calendar calendarDataAlternativa = Calendar.getInstance();
			calendarDataAlternativa.setTime((dataAlternativa != null) ? dataAlternativa : dataFinale);

			if (!isRipeti()) {
				dataAlternativaCalcolata = calendarDataAlternativa.getTime();
			} else {
				Calendar calendarInizio = Calendar.getInstance();
				calendarInizio.setTime(dataIniziale);

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);

				if (calendarDataAlternativa.get(Calendar.YEAR) != calendarInizio.get(Calendar.YEAR)) {
					calendarDataAlternativa.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
				} else {
					calendarDataAlternativa.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
				}

				dataAlternativaCalcolata = calendarDataAlternativa.getTime();
			}
		}

		return dataAlternativaCalcolata;
	}

	/**
	 * @return the calendarioRate
	 */
	public CalendarioRate getCalendarioRate() {
		return calendarioRate;
	}

	/**
	 * @return the dataAlternativa
	 */
	public Date getDataAlternativa() {
		return dataAlternativa;
	}

	/**
	 * @return the dataFinale
	 */
	public Date getDataFinale() {
		return dataFinale;
	}

	/**
	 * @return the dataIniziale
	 */
	public Date getDataIniziale() {
		return dataIniziale;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Verifica se la data di riferimento è compresa nel range della data iniziale e finale della riga calendario.
	 * 
	 * @param date
	 *            data di riferimento
	 * @return <code>true</code> se la data è nel range
	 */
	public boolean isInRange(Date date) {

		// se è importato il flag ripeti assegno alla data di riferimento lo stesso anno della data iniziale
		if (isRipeti()) {
			Calendar calendarInizio = Calendar.getInstance();
			calendarInizio.setTime(dataIniziale);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.YEAR, calendarInizio.get(Calendar.YEAR));

			date = calendar.getTime();
		}

		return date.compareTo(dataIniziale) >= 0 && date.compareTo(dataFinale) <= 0;
	}

	/**
	 * @return the ripeti
	 */
	public boolean isRipeti() {
		return ripeti;
	}

	/**
	 * @param calendarioRate
	 *            the calendarioRate to set
	 */
	public void setCalendarioRate(CalendarioRate calendarioRate) {
		this.calendarioRate = calendarioRate;
	}

	/**
	 * @param dataAlternativa
	 *            the dataAlternativa to set
	 */
	public void setDataAlternativa(Date dataAlternativa) {
		this.dataAlternativa = dataAlternativa;
	}

	/**
	 * @param dataFinale
	 *            the dataFinale to set
	 */
	public void setDataFinale(Date dataFinale) {
		this.dataFinale = dataFinale;
	}

	/**
	 * @param dataIniziale
	 *            the dataIniziale to set
	 */
	public void setDataIniziale(Date dataIniziale) {
		this.dataIniziale = dataIniziale;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param ripeti
	 *            the ripeti to set
	 */
	public void setRipeti(boolean ripeti) {
		this.ripeti = ripeti;
	}
}
