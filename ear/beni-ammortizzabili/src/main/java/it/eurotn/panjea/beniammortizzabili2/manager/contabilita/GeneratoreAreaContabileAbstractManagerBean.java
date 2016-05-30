/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager.contabilita;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeniSettings;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.beniammortizzabili2.domain.SottocontiBeni;
import it.eurotn.panjea.beniammortizzabili2.manager.contabilita.interfaces.GeneratoreAreaContabileManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.BeniAmmortizzabiliSettingsManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.SimulazioneAmmortamentoManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.GeneratoreAreaContabileManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.GeneratoreAreaContabileManager")
public abstract class GeneratoreAreaContabileAbstractManagerBean implements GeneratoreAreaContabileManager {

	protected class ImportoSottoconti {

		private PoliticaCalcolo politicaCalcolo;

		private SottocontiBeni sottocontiBeni;

		/**
		 * Costruttore.
		 *
		 * @param politicaCalcolo
		 *            politica calcolo
		 * @param importoAnticipato
		 *            importo anticipato
		 * @param sottocontiBeni
		 *            sotto conti
		 */
		public ImportoSottoconti(final PoliticaCalcolo politicaCalcolo, final SottocontiBeni sottocontiBeni) {
			super();
			this.politicaCalcolo = politicaCalcolo;
			this.sottocontiBeni = sottocontiBeni;
		}

		/**
		 * @return the importoAnticipato
		 */
		public BigDecimal getImportoAnticipato() {
			return politicaCalcolo.getPoliticaCalcoloFiscale().getTotaleAnticipato();
		}

		/**
		 * @return the importoOrdinario
		 */
		public BigDecimal getImportoOrdinario() {
			return politicaCalcolo.getPoliticaCalcoloFiscale().getTotaleOrdinario();
		}

		/**
		 * @return the politicaCalcolo
		 */
		public PoliticaCalcolo getPoliticaCalcolo() {
			return politicaCalcolo;
		}

		/**
		 * @return the sottocontiBeni
		 */
		public SottocontiBeni getSottocontiBeni() {
			return sottocontiBeni;
		}
	}

	private static Logger logger = Logger.getLogger(GeneratoreAreaContabileAbstractManagerBean.ImportoSottoconti.class);

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private AziendeManager aziendeManager;

	@EJB
	private SimulazioneAmmortamentoManager simulazioneAmmortamentoManager;

	@EJB
	private BeniAmmortizzabiliSettingsManager beniAmmortizzabiliSettingsManager;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext sessionContext;

	protected abstract List<ImportoSottoconti> caricaImportoSottoconti(Simulazione simulazione)
			throws SottocontiBeniNonValidiException;

	private AreaContabile creaAreaContabile(Simulazione simulazione, TipoAreaContabile tipoAreaContabile,
			List<ImportoSottoconti> importoSottoconti, String codiceValuta) {

		// creo l'area
		AreaContabile areaContabile = new AreaContabile(simulazione.getAnno(), simulazione.getData(), tipoAreaContabile);
		areaContabile.setStatoAreaContabile(StatoAreaContabile.SIMULATO);
		areaContabile.getDatiGenerazione().setDataGenerazione(Calendar.getInstance().getTime());
		areaContabile.getDatiGenerazione().setTipoGenerazione(TipoGenerazione.SIMULAZIONE_BENI);
		areaContabile.getDatiGenerazione().setUtente(getPrincipal().getUserName());

		// calcolo il totale documento
		BigDecimal totaleImporti = BigDecimal.ZERO;
		for (ImportoSottoconti importoSC : importoSottoconti) {
			totaleImporti = totaleImporti.add(importoSC.getImportoOrdinario());
			totaleImporti = totaleImporti.add(importoSC.getImportoAnticipato());
		}

		Importo totale = new Importo(codiceValuta, BigDecimal.ONE);
		totale.setImportoInValuta(totaleImporti);
		totale.setImportoInValutaAzienda(totaleImporti);
		areaContabile.getDocumento().setTotale(totale);

		try {
			areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, true);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio dell'area contabile.", e);
			throw new RuntimeException("errore durante il salvataggio dell'area contabile.", e);
		}

		return areaContabile;
	}

	@Override
	public void creaAreeContabili(Integer idSimulazione, TipoAreaContabile tipoAreaContabile)
			throws SottocontiBeniNonValidiException {

		// Carico la simulazione così da avere tutti i totali sulle politiche di calcolo
		Simulazione simulazione = new Simulazione();
		simulazione.setId(idSimulazione);
		simulazione = simulazioneAmmortamentoManager.caricaSimulazione(simulazione);

		// carico subito gli importi e relativi sottoconti in modo da verificare che ci siano tutti altrimenti rilancio
		// un eccezione
		List<ImportoSottoconti> importoSottoconti = caricaImportoSottoconti(simulazione);

		AziendaLite azienda = aziendeManager.caricaAzienda();

		// creo le aree contabili
		AreaContabile areaContabile = null;
		BeniSettings beniSettings = beniAmmortizzabiliSettingsManager.caricaBeniSettings();
		switch (beniSettings.getRaggruppamentoACSimulazione()) {
		case UNICA:
			areaContabile = creaAreaContabile(simulazione, tipoAreaContabile, importoSottoconti,
					azienda.getCodiceValuta());

			for (ImportoSottoconti importi : importoSottoconti) {
				creaRigheContabili(areaContabile, importi);
			}

			simulazione.setAreaContabile(areaContabile.getAreaContabileLite());
			simulazioneAmmortamentoManager.salvaSimulazioneNoCheck(simulazione);
			break;
		case UNA_PER_TIPO_RAGGRUPPAMENTO:
			for (ImportoSottoconti importi : importoSottoconti) {
				List<ImportoSottoconti> importo = new ArrayList<ImportoSottoconti>();
				importo.add(importi);
				areaContabile = creaAreaContabile(simulazione, tipoAreaContabile, importo, azienda.getCodiceValuta());
				creaRigheContabili(areaContabile, importi);

				PoliticaCalcolo politicaCalcolo = importi.getPoliticaCalcolo();
				politicaCalcolo.setAreaContabile(areaContabile.getAreaContabileLite());
				simulazioneAmmortamentoManager.salvaPoliticaCalcolo(politicaCalcolo);
			}
			break;
		default:
			logger.error("--> errore. Raggruppamento area contabile simulazione beni non prevista.");
			throw new RuntimeException("errore. Raggruppamento area contabile simulazione beni non prevista.");
		}
	}

	private void creaRigheContabili(AreaContabile areaContabile, ImportoSottoconti importoSottoconti) {

		// righe contabili per l'ammortamento ordinario
		if (BigDecimal.ZERO.compareTo(importoSottoconti.getImportoOrdinario()) != 0) {
			// sottoconto ammortamento in dare
			RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, importoSottoconti
					.getSottocontiBeni().getSottoContoAmmortamento(), true, importoSottoconti.getImportoOrdinario(),
					null, false);
			areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);

			// sottoconto fondo ammortamento in avere
			rigaContabile = RigaContabile.creaRigaContabile(areaContabile, importoSottoconti.getSottocontiBeni()
					.getSottoContoFondoAmmortamento(), false, importoSottoconti.getImportoOrdinario(), null, false);
			areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);
		}

		// righe contabili per l'ammortamento anticipato
		if (BigDecimal.ZERO.compareTo(importoSottoconti.getImportoAnticipato()) != 0) {
			// sottoconto ammortamento anticipato in dare
			RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, importoSottoconti
					.getSottocontiBeni().getSottoContoAmmortamentoAnticipato(), true, importoSottoconti
					.getImportoAnticipato(), null, false);
			areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);

			// sottoconto fondo ammortamento anticipato in avere
			rigaContabile = RigaContabile.creaRigaContabile(areaContabile, importoSottoconti.getSottocontiBeni()
					.getSottoContoFondoAmmortamentoAnticipato(), false, importoSottoconti.getImportoAnticipato(), null,
					false);
			areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);
		}
	}

	/**
	 * Restituisce l'utente loggato.
	 *
	 * @return utente loggato
	 */
	private JecPrincipal getPrincipal() {
		JecPrincipal jecPrincipal = (JecPrincipal) sessionContext.getCallerPrincipal();
		return jecPrincipal;
	}

}