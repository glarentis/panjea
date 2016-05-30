package it.eurotn.panjea.contabilita.manager.ivasospesa;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.manager.ivasospesa.interfaces.IvaSospesaManager;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;

@Stateless(name = "Panjea.IvaSospesaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.IvaSospesaManager")
public class IvaSospesaManagerBean implements IvaSospesaManager {

	private static Logger logger = Logger.getLogger(IvaSospesaManagerBean.class.getName());

	@Resource
	protected SessionContext context;

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	/**
	 * Raggruppa i risultati per codice iva sommando quindi imponibile e imposta e aggiorna la liquidazioneIvaDTO con i
	 * risultati aggiungendo i totali iva sospesa per registro e aggiungendo il totale incassato al totale generale del
	 * periodo preso in esame.
	 * 
	 * @param liquidazioneIvaDTO
	 *            liquidazioneIvaDTO
	 * @param resultMap
	 *            la mappa passata per riferimento da cui recuperare le righe da raggruppare
	 */
	private void aggiungiIvaSospesaLiquidazione(LiquidazioneIvaDTO liquidazioneIvaDTO,
			Map<RegistroIva, List<TotaliCodiceIvaDTO>> resultMap) {
		Set<RegistroIva> registri = resultMap.keySet();
		Comparator<TotaliCodiceIvaDTO> comparator = new Comparator<TotaliCodiceIvaDTO>() {

			@Override
			public int compare(TotaliCodiceIvaDTO o1, TotaliCodiceIvaDTO o2) {
				return o1.getIdCodiceIva().compareTo(o2.getIdCodiceIva());
			}
		};
		for (RegistroIva registroIva : registri) {
			List<TotaliCodiceIvaDTO> totaliCodiciIva = resultMap.get(registroIva);

			// ragguppo per codice iva id
			EventList<TotaliCodiceIvaDTO> eventList = new BasicEventList<TotaliCodiceIvaDTO>();
			eventList.addAll(totaliCodiciIva);

			// la grouping e' una lista di liste per codice iva
			GroupingList<TotaliCodiceIvaDTO> totaliSorted = new GroupingList<TotaliCodiceIvaDTO>(eventList, comparator);

			List<TotaliCodiceIvaDTO> listaRaggruppataConTotaliPerCodiceIva = new ArrayList<TotaliCodiceIvaDTO>();
			BigDecimal totaliRegistroIvaSospesa = BigDecimal.ZERO;
			for (List<TotaliCodiceIvaDTO> listPerCodiceIva : totaliSorted) {
				// variabili per calcolare il totale per codice iva
				BigDecimal totImponibile = BigDecimal.ZERO;
				BigDecimal totImposta = BigDecimal.ZERO;
				// ciclo ogni riga per codice iva e trovo il totale
				for (TotaliCodiceIvaDTO rigaCodiceIva : listPerCodiceIva) {
					totImponibile = totImponibile.add(rigaCodiceIva.getImponibile());
					totImposta = totImposta.add(rigaCodiceIva.getImposta());
				}

				// una volta trovato il totale uso il primo totaliCodiceIvaDTO per tenere il risultato
				// settandolo in una nuova lista che imposto alla mappa che contiene i risultati
				// aggiorno la mappa e la trovo aggiornata visto che l'oggetto e' per riferimento
				TotaliCodiceIvaDTO totaliCodiceIvaDTO = listPerCodiceIva.get(0);
				totaliCodiceIvaDTO.setCodiceIva(totaliCodiceIvaDTO.getCodiceIva() + "(I)");
				totaliCodiceIvaDTO.setImponibile(totImponibile);
				totaliCodiceIvaDTO.setImposta(totImposta);
				// se Tipo registro ACQUISTO calcolo i valori detraibili e indetraibili
				totaliCodiceIvaDTO.calcolaValoriDetraibili(registroIva);
				// imposto questo totaliCodiceIvaDTO considerato per liquidazione in modo da essere sommato alla
				// liquidazione
				totaliCodiceIvaDTO.setConsideraPerLiquidazione(true);

				listaRaggruppataConTotaliPerCodiceIva.add(totaliCodiceIvaDTO);
				if (registroIva.getTipoRegistro().equals(TipoRegistro.ACQUISTO)) {
					totaliRegistroIvaSospesa = totaliRegistroIvaSospesa.add(totaliCodiceIvaDTO.getImpostaDetraibile());
				} else {
					totaliRegistroIvaSospesa = totaliRegistroIvaSospesa.add(totaliCodiceIvaDTO.getImposta());
				}
			}
			liquidazioneIvaDTO.addToTotaliIvaSospesa(registroIva, listaRaggruppataConTotaliPerCodiceIva);
			liquidazioneIvaDTO.addToTotaliIncassato(registroIva, totaliRegistroIvaSospesa);

			// devo aggiungere l'incassato al totale generale
			BigDecimal totPerRegistro = BigDecimal.ZERO;
			if (liquidazioneIvaDTO.getTotali().get(registroIva) != null) {
				totPerRegistro = liquidazioneIvaDTO.getTotali().get(registroIva);
			}
			liquidazioneIvaDTO.addToTotali(registroIva, totPerRegistro.add(totaliRegistroIvaSospesa));
		}
	}

	@Override
	public LiquidazioneIvaDTO aggiungiTotaliCodiceIvaSospesa(LiquidazioneIvaDTO liquidazioneIvaDTO,
			Date dataInizioPeriodo, Date dataFinePeriodo) {
		logger.debug("Enter caricaTotaliCodiceIvaSospesa");

		StringBuffer stringBufferHqlTotaliIvaSospesa = new StringBuffer();
		stringBufferHqlTotaliIvaSospesa
				.append("select distinct areaIva as areaIva,areaRate as areaRate from Rata rata,AreaIva areaIva ");
		stringBufferHqlTotaliIvaSospesa.append("join rata.pagamenti p ");
		stringBufferHqlTotaliIvaSospesa.append("join rata.areaRate areaRate ");
		stringBufferHqlTotaliIvaSospesa.append("join areaRate.documento docrate ");
		stringBufferHqlTotaliIvaSospesa.append("join areaIva.documento dociva ");
		stringBufferHqlTotaliIvaSospesa.append("join areaIva.righeIva riva ");
		stringBufferHqlTotaliIvaSospesa.append("join riva.codiceIva c ");
		stringBufferHqlTotaliIvaSospesa.append("where p.dataPagamento>=:paramDaData ");
		stringBufferHqlTotaliIvaSospesa.append("and p.dataPagamento<=:paramAData ");
		stringBufferHqlTotaliIvaSospesa.append("and c.ivaSospesa=true ");
		stringBufferHqlTotaliIvaSospesa.append("and c.liquidazionePeriodica=true ");
		stringBufferHqlTotaliIvaSospesa.append("and areaIva.areaContabile.tipoAreaContabile.stampaRegistroIva=true ");
		stringBufferHqlTotaliIvaSospesa.append("and dociva.id=docrate.id ");

		String hqlTotaliIvaSospesa = stringBufferHqlTotaliIvaSospesa.toString();

		Query queryTotaliIvaSospesa = ((Session) panjeaDAO.getEntityManager().getDelegate())
				.createQuery(hqlTotaliIvaSospesa);
		queryTotaliIvaSospesa.setParameter("paramDaData", dataInizioPeriodo);
		queryTotaliIvaSospesa.setParameter("paramAData", dataFinePeriodo);
		queryTotaliIvaSospesa.setResultTransformer(Transformers.aliasToBean(IvaSospesaManagerCalculator.class));

		Map<RegistroIva, List<TotaliCodiceIvaDTO>> resultMap = new HashMap<RegistroIva, List<TotaliCodiceIvaDTO>>();

		@SuppressWarnings("unchecked")
		List<IvaSospesaManagerCalculator> resultList = queryTotaliIvaSospesa.list();

		for (IvaSospesaManagerCalculator managerCalculator : resultList) {
			List<TotaliCodiceIvaDTO> righe = managerCalculator.calcola(dataInizioPeriodo, dataFinePeriodo);
			RegistroIva registroIva = managerCalculator.getAreaIva().getRegistroIva();
			boolean containsKey = resultMap.containsKey(registroIva);
			if (!containsKey) {
				resultMap.put(registroIva, righe);
			} else {
				List<TotaliCodiceIvaDTO> totRegistro = resultMap.get(registroIva);
				totRegistro.addAll(righe);
			}
		}
		aggiungiIvaSospesaLiquidazione(liquidazioneIvaDTO, resultMap);

		logger.debug("Exit caricaTotaliCodiceIvaSospesa");
		return liquidazioneIvaDTO;
	}
}
