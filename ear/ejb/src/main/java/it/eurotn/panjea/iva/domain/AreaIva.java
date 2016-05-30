package it.eurotn.panjea.iva.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.DatiValidazione;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

/**
 * Area iva di Documento; il link a Documento segue lo standard dove ogni area � associata al documento, senza avere
 * riferimenti tra aree diverse con l'eccezione di questa area che per motivi di esecuzione queries viene collegata a
 * AreaContabile oltre che a Documento. TODO se possibile rimuovere link a AreaContabile
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
@Entity
@Audited
@Table(name = "cont_aree_iva")
@NamedQueries({
		@NamedQuery(name = "AreaIva.caricaByAreaContabile", query = "select ai from AreaIva ai where ai.areaContabile.id=:paramIdAreaContabile"),
		@NamedQuery(name = "AreaIva.ricercaAreaByDocumento", query = "select a from AreaIva a left join fetch a.areaContabile inner join fetch a.documento d where d.id = :paramIdDocumento") })
public class AreaIva extends EntityBase implements IAreaDocumento {

	/**
	 * Compara le righeIva in base al loro CodiceIva.
	 *
	 * @author giangi
	 */
	private class RigaIvaCodiceIvaComparator implements Comparator<RigaIva> {

		@Override
		public int compare(RigaIva rigaIva1, RigaIva rigaIva2) {
			return rigaIva1.getCodiceIva().compareTo(rigaIva2.getCodiceIva());
		}

	}

	private static final long serialVersionUID = -98288340204451109L;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "valid", column = @Column(name = "validRigheIva")),
			@AttributeOverride(name = "validData", column = @Column(name = "validDataRigheIva")),
			@AttributeOverride(name = "validUtente", column = @Column(name = "validUtenteRigheIva")) })
	private DatiValidazione datiValidazioneRighe;

	@Deprecated
	@OneToOne(fetch = FetchType.LAZY)
	private AreaContabile areaContabile;

	@OneToOne(fetch = FetchType.LAZY)
	private Documento documento;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "areaIva")
	@OrderBy("ordinamento")
	private List<RigaIva> righeIva;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	private RegistroIva registroIva;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	private RegistroIva registroIvaCollegato;

	{
		this.areaContabile = new AreaContabile();
		this.documento = new Documento();
		this.datiValidazioneRighe = new DatiValidazione();
		this.righeIva = new ArrayList<RigaIva>();
	}

	/**
	 * Default constructor.
	 */
	public AreaIva() {
		super();
	}

	@Override
	public Map<String, Object> fillVariables() {
		return new HashMap<String, Object>();
	}

	/**
	 * @return the areaContabile
	 */
	public AreaContabile getAreaContabile() {
		return areaContabile;
	}

	/**
	 * @return the datiValidazioneRighe
	 */
	public DatiValidazione getDatiValidazioneRighe() {
		if (this.datiValidazioneRighe == null) {
			this.datiValidazioneRighe = new DatiValidazione();
		}
		return datiValidazioneRighe;
	}

	@Override
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return Returns the registroIva.
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @return Returns the registroIvaCollegato.
	 */
	public RegistroIva getRegistroIvaCollegato() {
		return registroIvaCollegato;
	}

	/**
	 * @return Returns the righeIva.
	 */
	public List<RigaIva> getRigheIva() {
		return righeIva;
	}

	/**
	 *
	 * @return imponibile splitPayment
	 */
	public BigDecimal getSplitPaymentImposta() {
		BigDecimal splitPayment = BigDecimal.ZERO;
		if (righeIva != null) {
			for (RigaIva rigaIva : righeIva) {
				if (rigaIva.getCodiceIva().getSplitPayment()) {
					splitPayment = splitPayment.add(rigaIva.getImposta().getImportoInValuta());
				}
			}
		}
		return splitPayment;
	}

	@Override
	public IStatoDocumento getStato() {
		return null;
	}

	@Override
	public StatoSpedizione getStatoSpedizione() {
		return null;
	}

	@Override
	public ITipoAreaDocumento getTipoAreaDocumento() {
		return null;
	}

	/**
	 * @return calcola il totale dell'areIva come somma di imponibile e imposta di tutte le righe iva presenti; 0 se non
	 *         ci sono righe.
	 */
	public BigDecimal getTotale() {
		BigDecimal importoTotale = BigDecimal.ZERO;
		if (righeIva != null) {
			for (RigaIva rigaIva : righeIva) {
				importoTotale = importoTotale.add(rigaIva.getImponibile().getImportoInValuta()
						.add(rigaIva.getImposta().getImportoInValuta()));
			}
		}
		return importoTotale;
	}

	/**
	 * @param tipoOperazioneValuta
	 *            indica se voglio il totale valuta o valutazienda
	 * @return totale delle righe o in valuta o in valutaAzienda
	 */
	public BigDecimal getTotaleImponibile(Importo.TIPO_OPERAZIONE_VALUTA tipoOperazioneValuta) {
		BigDecimal importoTotale = BigDecimal.ZERO;
		if (righeIva != null) {
			for (RigaIva rigaIva : righeIva) {
				importoTotale = rigaIva.getImponibile().addValore(importoTotale, tipoOperazioneValuta);
			}
		}

		return importoTotale;
	}

	/**
	 * @param tipoOperazioneValuta
	 *            indica se volgio il totale valuta o valutazienda
	 * @return totale delle righe o in valuta o in valutaAzienda
	 */
	public BigDecimal getTotaleImposta(Importo.TIPO_OPERAZIONE_VALUTA tipoOperazioneValuta) {
		BigDecimal importoTotale = BigDecimal.ZERO;
		if (righeIva != null) {
			for (RigaIva rigaIva : righeIva) {
				importoTotale = rigaIva.getImposta().addValore(importoTotale, tipoOperazioneValuta);
			}
		}

		return importoTotale;
	}

	/**
	 * Verifica se il documento collegato e' di tipo documento con caratteristica nota di credito.
	 *
	 * @return true se e' nota di credito, false altrimenti
	 */
	public boolean isNotaCredito() {
		return getDocumento() != null && getDocumento().getTipoDocumento() != null
				&& getDocumento().getTipoDocumento().isNotaCreditoEnable();
	}

	/**
	 * @param areaContabile
	 *            the areaContabile to set
	 */
	public void setAreaContabile(AreaContabile areaContabile) {
		this.areaContabile = areaContabile;
	}

	/**
	 * @param datiValidazioneRighe
	 *            the datiValidazioneRighe to set
	 */
	public void setDatiValidazioneRighe(DatiValidazione datiValidazioneRighe) {
		this.datiValidazioneRighe = datiValidazioneRighe;
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	@Override
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param registroIva
	 *            The registroIva to set.
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}

	/**
	 * @param registroIvaCollegato
	 *            The registroIvaCollegato to set.
	 */
	public void setRegistroIvaCollegato(RegistroIva registroIvaCollegato) {
		this.registroIvaCollegato = registroIvaCollegato;
	}

	/**
	 * @param righeIva
	 *            The righeIva to set.
	 */
	public void setRigheIva(List<RigaIva> righeIva) {
		this.righeIva = righeIva;
	}

	@Override
	public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
	}

	@Override
	public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {

	}

	/**
	 * Controlla se le righeIva passate come argomento corrispondono alle righeIva dell'area contabile.
	 *
	 * @param righeIvaDaControllare
	 *            righeIvaDaControllare
	 * @return true se le righe corrispondono
	 */
	public boolean verificaRigheIva(List<RigaIva> righeIvaDaControllare) {
		if (righeIvaDaControllare.size() != getRigheIva().size()) {
			return false;
		}

		Comparator<RigaIva> codiceIvaComparator = new RigaIvaCodiceIvaComparator();
		// ordino le due liste
		Collections.sort(righeIvaDaControllare, codiceIvaComparator);
		Collections.sort(getRigheIva(), codiceIvaComparator);
		// Creo una stringa che descriva il contenuto delle righe
		String checkSumRigheIva;
		String checkSumRigheIvaDaControllare;

		StringBuilder sb = new StringBuilder();
		for (RigaIva rigaIvaDaControllare : righeIvaDaControllare) {
			sb.append(rigaIvaDaControllare.getCodiceIva().getId());
			sb.append(",");
			sb.append(rigaIvaDaControllare.getImponibile().getImportoInValutaAzienda());
			sb.append(",");
			sb.append(rigaIvaDaControllare.getImposta().getImportoInValutaAzienda());
		}
		checkSumRigheIvaDaControllare = sb.toString();

		sb = new StringBuilder();
		for (RigaIva rigaIva : getRigheIva()) {
			sb.append(rigaIva.getCodiceIva().getId());
			sb.append(",");
			sb.append(rigaIva.getImponibile().getImportoInValutaAzienda().setScale(2));
			sb.append(",");
			sb.append(rigaIva.getImposta().getImportoInValutaAzienda().setScale(2));
		}
		checkSumRigheIva = sb.toString();
		return checkSumRigheIva.equals(checkSumRigheIvaDaControllare);
	}

}
