/**
 *
 */
package it.eurotn.panjea.contabilita.manager.corrispettivo;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipologiaCorrispettivo;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

/**
 * Classe astratta che rappresenta la tipologia corrispettivo per cui caricare i totali come list di TotaliCodiceIvaDTO
 * (lista che raggruppa codici iva e per i quali viene calcolato il totale).
 * 
 * @author Leonardo
 * @see TipologiaCorrispettivo
 * @see TotaliCodiceIvaDTO
 */
public abstract class AbstractTipologiaCorrispettivo {

	protected static Logger logger = Logger.getLogger(AbstractTipologiaCorrispettivo.class);

	private TipologiaCorrispettivo tipologiaCorrispettivo = null;
	private PanjeaDAO panjeaDAO = null;
	private String codiceAzienda = null;

	/**
	 * Costruttore base per impostare la TipologiaCorrispettivo delle classi derivate.
	 * 
	 * @param tipologiaCorrispettivo
	 *            la tipologiaCorrispettivo da impostare
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param codiceAzienda
	 *            codiceAzienda
	 */
	public AbstractTipologiaCorrispettivo(final TipologiaCorrispettivo tipologiaCorrispettivo,
			final PanjeaDAO panjeaDAO, final String codiceAzienda) {
		super();
		this.tipologiaCorrispettivo = tipologiaCorrispettivo;
		this.panjeaDAO = panjeaDAO;
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @return codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return panjeaDAO
	 */
	public PanjeaDAO getPanjeaDAO() {
		return panjeaDAO;
	}

	/**
	 * @return tipologiaCorrispettivo
	 */
	public TipologiaCorrispettivo getTipologiaCorrispettivo() {
		return tipologiaCorrispettivo;
	}

	/**
	 * Carica i totali della tipologia corrispettivo della classe derivata raggruppati per codice di CodiceIva.
	 * 
	 * @param registroIva
	 *            il registro iva su cui eseguire la ricerca
	 * @param dataInizioPeriodo
	 *            data di inizio periodo
	 * @param dataFinePeriodo
	 *            data di fine periodo
	 * @return una List<TotaliCodiceIvaDTO> ordinata e raggruppata per codice di CodiceIva
	 */
	@SuppressWarnings("unchecked")
	public List<TotaliCodiceIvaDTO> getTotali(RegistroIva registroIva, Date dataInizioPeriodo, Date dataFinePeriodo) {
		try {
			logger.debug("--> Enter getTotali dataInizioPeriodo: " + dataInizioPeriodo + ", dataFinePeriodo: "
					+ dataFinePeriodo + ", tipologiaCorrispettivo: " + getTipologiaCorrispettivo());
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(r.imponibile.importoInValutaAzienda) as imponibile,");
			buffer.append("sum(r.imposta.importoInValutaAzienda) as imposta,");
			buffer.append("r.codiceIva.id as idCodiceIva,");
			buffer.append("r.codiceIva.codice as codiceIva,");
			buffer.append("r.codiceIva.descrizioneRegistro as descrizioneRegistro,");
			buffer.append("r.codiceIva.percApplicazione as percApplicazione,");
			buffer.append("r.codiceIva.percIndetraibilita as percIndetraibilita,");
			buffer.append("(CASE r.codiceIva.ivaSospesa WHEN true THEN false else true END) as consideraPerLiquidazione ");
			buffer.append("from RigaIva r ");
			buffer.append("where r.areaIva.areaContabile.documento.codiceAzienda = :paramCodiceAzienda ");
			buffer.append("and r.areaIva.registroIva.id = :paramIdRegistroIva ");
			// stato verificato e confermato
			buffer.append("and r.areaIva.areaContabile.statoAreaContabile in (0,1) ");
			buffer.append("and (r.areaIva.areaContabile.dataRegistrazione >= :paramDaDataRegistrazione) ");
			buffer.append("and (r.areaIva.areaContabile.dataRegistrazione <= :paramADataRegistrazione) ");
			buffer.append("and r.areaIva.areaContabile.tipoAreaContabile.tipologiaCorrispettivo = :paramTipologiaCorrispettivo ");
			buffer.append("and r.areaIva.areaContabile.tipoAreaContabile.stampaRegistroIva=true ");
			buffer.append("group by r.codiceIva.codice");

			org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(buffer
					.toString());
			query.setResultTransformer(Transformers.aliasToBean(TotaliCodiceIvaDTO.class));

			query.setParameter("paramIdRegistroIva", registroIva.getId());
			query.setParameter("paramTipologiaCorrispettivo", tipologiaCorrispettivo);
			query.setParameter("paramDaDataRegistrazione", dataInizioPeriodo);
			query.setParameter("paramADataRegistrazione", dataFinePeriodo);
			query.setParameter("paramCodiceAzienda", getCodiceAzienda());

			List<TotaliCodiceIvaDTO> list = query.list();
			postProcessList(list);

			logger.debug("--> Exit getTotali trovati elementi # " + list.size());
			return list;
		} catch (Exception e) {
			logger.error("--> Errore nel trovare le righe totali per registro " + registroIva.getId()
					+ " di tipologiaCorrispettivo " + tipologiaCorrispettivo, e);
			throw new RuntimeException("--> Errore nel trovare le righe totali per registro " + registroIva.getId()
					+ " di tipologiaCorrispettivo " + tipologiaCorrispettivo, e);
		}
	}

	/**
	 * Processa la lista che include i valori di imponibile e imposta,sommandoli per associarli al totale dipendente
	 * dalla tipologiaCorrispettivo della classe derivata.
	 * 
	 * @param listTot
	 *            la lista di totaliCodiceIvaDTO
	 */
	protected void postProcessList(List<TotaliCodiceIvaDTO> listTot) {
		for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : listTot) {
			logger.debug("--> righe tipologia " + getTipologiaCorrispettivo().name() + ";codice:"
					+ totaliCodiceIvaDTO.getCodiceIva() + ",imponibile:" + totaliCodiceIvaDTO.getImponibile()
					+ ",imposta:" + totaliCodiceIvaDTO.getImposta());
			BigDecimal tot = totaliCodiceIvaDTO.getImponibile().add(totaliCodiceIvaDTO.getImposta());
			setTotTipoCorrispettivo(totaliCodiceIvaDTO, tot);
		}
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param panjeaDAO
	 *            the panjeaDAO to set
	 */
	public void setPanjeaDAO(PanjeaDAO panjeaDAO) {
		this.panjeaDAO = panjeaDAO;
	}

	/**
	 * @param tipologiaCorrispettivo
	 *            the tipologiaCorrispettivo to set
	 */
	public void setTipologiaCorrispettivo(TipologiaCorrispettivo tipologiaCorrispettivo) {
		this.tipologiaCorrispettivo = tipologiaCorrispettivo;
	}

	/**
	 * Imposta a totaliCodiceIvaDTO il totale a seconda del TipoCorrispettivo della classe derivata.
	 * 
	 * @param totaliCodiceIvaDTO
	 *            totaliCodiceIvaDTO
	 * @param tot
	 *            totale
	 */
	protected abstract void setTotTipoCorrispettivo(TotaliCodiceIvaDTO totaliCodiceIvaDTO, BigDecimal tot);

}
