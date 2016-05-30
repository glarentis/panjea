/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.exception.FormulaException;
import it.eurotn.panjea.magazzino.exception.FormulaMrpCalcoloArticoloException;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.Moltiplicatore;
import it.eurotn.panjea.mrp.domain.OrdiniFornitoreCalcolo;
import it.eurotn.panjea.mrp.domain.RigheCalcolo;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.domain.RisultatoSottoScortaMRPArticoloBucket;
import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;
import it.eurotn.panjea.mrp.util.RisultatoPadre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import commonj.work.Work;

/**
 * @author leonardo
 */
public class ArticoloCalcoloWork implements Work {

	private static Logger logger = Logger.getLogger(ArticoloCalcoloWork.class);

	protected int numBucket;
	protected int ordinamento;

	protected boolean[] calendarWork;

	protected RigheCalcolo[] ordinatoCliente;
	protected Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultatiConfigurazione;

	protected ArticoloAnagrafica articoloAnagrafica;
	protected Giacenza giacenza;
	protected ArticoloDepositoKey key;

	protected List<RisultatoPadre> righeCalcolatePadre;

	protected Map<ArticoloConfigurazioneKey, Bom> boms;

	protected List<OrdiniFornitoreCalcolo> ordiniFornitoreCalcolo;

	/**
	 * Costruttore.
	 *
	 * @param idDeposito
	 *            idDeposito
	 * @param idArticolo
	 *            idArticolo
	 * @param giacenza
	 *            giacenza iniziale
	 * @param ordinatoCliente
	 *            righe iniziali di R.
	 * @param ordinatoAttivo
	 *            ordini fornitori
	 * @param numBucket
	 *            orizzonte calcolo
	 * @param articoloAnagrafica
	 *            articoloAnagrafica
	 * @param calendarWork
	 *            calendarWork
	 * @param ordinamento
	 *            ordinamento
	 * @param righeCalcolatePadre
	 *            righeCalcolatePadre
	 * @param boms
	 *            bom esplose
	 * @param ordiniFornitoreCalcolo
	 *
	 */
	public ArticoloCalcoloWork(final int idDeposito, final int idArticolo, final Giacenza giacenza,
			final RigheCalcolo[] ordinatoCliente, final int numBucket, final ArticoloAnagrafica articoloAnagrafica,
			final boolean[] calendarWork, final List<RisultatoPadre> righeCalcolatePadre,
			final Map<ArticoloConfigurazioneKey, Bom> boms, List<OrdiniFornitoreCalcolo> ordiniFornitoreCalcolo) {
		super();
		this.giacenza = giacenza;
		this.ordinatoCliente = ordinatoCliente;
		this.numBucket = numBucket;
		this.calendarWork = calendarWork;
		this.articoloAnagrafica = articoloAnagrafica;
		this.righeCalcolatePadre = righeCalcolatePadre;
		this.boms = boms;
		this.key = new ArticoloDepositoKey(idArticolo, idDeposito);
		this.ordiniFornitoreCalcolo = ordiniFornitoreCalcolo;
		this.ordinamento = 1;
	}

	/**
	 * calcolo por.
	 */
	protected void calcola() {
		Moltiplicatore moltiplicatoreCalculator = new Moltiplicatore();
		ordinatoCliente = ObjectUtils.defaultIfNull(ordinatoCliente, new RigheCalcolo[numBucket]);

		righeCalcolatePadre = ObjectUtils.defaultIfNull(righeCalcolatePadre, new ArrayList<RisultatoPadre>());
		double[] s = calcolaS();
		s = ObjectUtils.defaultIfNull(s, new double[numBucket]);
		giacenza = ObjectUtils.defaultIfNull(giacenza, new Giacenza(0.0, 0.0));

		for (RisultatoPadre padre : righeCalcolatePadre) {
			RigheCalcolo[] risultatoPadre = padre.getRigheCalcolo();
			for (int i = 0; i < risultatoPadre.length; i++) {
				RigheCalcolo righeCalcoloPadre = risultatoPadre[i];
				if (righeCalcoloPadre != null) {
					Integer idArticoloPadre = padre.getBom().getIdDistinta();
					Integer idConfigurazionePadre = padre.getBom().getKeyArticoloConfigurazione().getIdConfigurazione();

					ArticoloDepositoConfigurazioneKey keyArticoloDepositoConfigurazionePadre = new ArticoloDepositoConfigurazioneKey(
							idArticoloPadre, key.getIdDeposito(), idConfigurazionePadre);
					// Ciclo sui padri e calcolo le quantità che mi servono
					// moltiplicando il molt.*qta del padre
					for (RisultatoMRPArticoloBucket risultatoArticoloBucketPadre : righeCalcoloPadre
							.getRisultatiMRPArticoloBucket(keyArticoloDepositoConfigurazionePadre)) {
						if (risultatoArticoloBucketPadre.getTipoRiga() > 0) {
							continue;
						}
						String formula = padre.getBom().getFormulaMolt();
						String codiciAttributo = risultatoArticoloBucketPadre.getCodiciAttributo() != null ? risultatoArticoloBucketPadre
								.getCodiciAttributo() : "";
								String valoriAttributo = risultatoArticoloBucketPadre.getValoriAttributo() != null ? risultatoArticoloBucketPadre
										.getValoriAttributo() : "";

										String separator = "";
										if (padre.getBom().isTrasmettiAttributi()) {
											String codiciAttributoPadre = padre.getBom().getCodiciAttributi() != null ? padre.getBom()
													.getCodiciAttributi() : "";
													String valoriAttributoPadre = padre.getBom().getValoriAttributi() != null ? padre.getBom()
															.getValoriAttributi() : "";
															if (!codiciAttributoPadre.equals("") && !codiciAttributo.equals("")) {
																separator = ",";
															}
															codiciAttributo = codiciAttributo.concat(separator).concat(codiciAttributoPadre);
															valoriAttributo = valoriAttributo.concat(separator).concat(valoriAttributoPadre);
										}
										BigDecimal qtaPor = new BigDecimal(risultatoArticoloBucketPadre.getQtaPor());
										Double qtaAttrezzaggio = padre.getBom().getQtaAttrezzaggioArticolo();

										Double qtaComponente = 0.0;
										try {
											qtaComponente = moltiplicatoreCalculator.calcola(formula, qtaPor,
													articoloAnagrafica.getNumDecimali(), codiciAttributo, valoriAttributo)
													.doubleValue();
										} catch (FormulaException ex) {
											FormulaMrpCalcoloArticoloException exf = new FormulaMrpCalcoloArticoloException(formula,
													ex.getMessage(), padre.getBom().getIdDistinta(), articoloAnagrafica.getIdArticolo());
											logger.error(
													"-->errore nella formula per la distinta  " + articoloAnagrafica.getIdArticolo()
													+ " ed il padre con chiave \n " + padre.getBom(), ex);
											throw exf;
										}
										if (qtaComponente != 0.0) {
											qtaComponente = qtaComponente + qtaAttrezzaggio;
										}

										RisultatoMRPArticoloBucket rigaComponente = new RisultatoMRPArticoloBucket();
										rigaComponente.setDataConsegna(DateUtils.addDays(
												risultatoArticoloBucketPadre.getDataDocumento(),
												ObjectUtils.defaultIfNull(articoloAnagrafica.getGgSicurezza() * -1, 0)));
										rigaComponente.setArticoloAnagrafica(articoloAnagrafica);
										rigaComponente.setIdDeposito(key.getIdDeposito());
										rigaComponente.setIdConfigurazioneDistinta(risultatoArticoloBucketPadre
												.getIdConfigurazioneDistinta());
										rigaComponente.setIdComponente(padre.getBom().getIdComponente());
										rigaComponente.setFormula(padre.getBom().getFormulaMolt());
										rigaComponente.setQtaR(qtaComponente);
										rigaComponente.setIdRigaOrdine(risultatoArticoloBucketPadre.getIdRigaOrdine());
										rigaComponente.setValoriAttributo(risultatoArticoloBucketPadre.getValoriAttributo());
										rigaComponente.setCodiciAttributo(risultatoArticoloBucketPadre.getCodiciAttributo());
										rigaComponente.setRisultatoArticoloDistinta(risultatoArticoloBucketPadre);
										// rigaComponente.setOrdinamento(ordinamento++);
										rigaComponente.setIdOrdine(risultatoArticoloBucketPadre.getIdOrdine());

						// DateTime dataConsegnaPadre = new
										// DateTime(risultatoArticoloBucketPadre.getDataConsegna());
										// DateTime dataDocumentoPadre = new
										// DateTime(risultatoArticoloBucketPadre.getDataDocumento());
										// int bucketIndex =
										// Days.daysBetween(dataDocumentoPadre.withTimeAtStartOfDay(),
										// dataConsegnaPadre.withTimeAtStartOfDay()).getDays();
										// RigheCalcolo righeCalcoloRisultatoArticolo =
										// ObjectUtils.defaultIfNull(ordinatoCliente[i
										// - bucketIndex], new RigheCalcolo());
										// righeCalcoloRisultatoArticolo.addRisultatoMRPArticoloBucket(padre.getKey().getKey(),
										// rigaComponente);
										// ordinatoCliente[i - bucketIndex] =
										// righeCalcoloRisultatoArticolo;
										RigheCalcolo righeCalcoloRisultatoArticolo = ObjectUtils.defaultIfNull(ordinatoCliente[i],
												new RigheCalcolo());
										righeCalcoloRisultatoArticolo.addRisultatoMRPArticoloBucket(
												keyArticoloDepositoConfigurazionePadre, rigaComponente);
										ordinatoCliente[i] = righeCalcoloRisultatoArticolo;
					}
				}
			}
		}

		Double disponibilita = giacenza.getGiacenza();
		Double disponibilitaPrimaDiUtilizzo = disponibilita;

		risultatiConfigurazione = new HashMap<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]>();

		for (int i = 0; i < numBucket; i++) {
			disponibilita += s[i];
			disponibilitaPrimaDiUtilizzo = disponibilita;
			RigheCalcolo risultatoGiorno = ordinatoCliente[i];
			if (risultatoGiorno != null) {
				// Se ho righe in risultatoGiorno calcolo i tempi con leadTime e
				// calendari.
				int leadTimeProgressivo = articoloAnagrafica.getLeadTime();
				Date dataConsegna = risultatoGiorno.getRisultatiMRPArticoloBucket().get(0).getDataConsegna();
				Date dataProduzione = risultatoGiorno.getRisultatiMRPArticoloBucket().get(0).getDataProduzione();

				int bucketDataDocumento = 0;
				while (leadTimeProgressivo > 0 && (i - bucketDataDocumento) > 0) {
					bucketDataDocumento++;
					try {
						if (calendarWork[i - bucketDataDocumento]) {
							leadTimeProgressivo--;
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}

				// Per ogni riga setto la dataDocumento appena calcolata e setto
				// i parametri dell'anagrafica
				// articolo

				Date dataDocumento = new DateTime(dataConsegna).minusDays(bucketDataDocumento).toDate();
				Double minOrdinabile = articoloAnagrafica.getMinOrdinabile();
				// se ho impostata una data di produzione sull'ordine cliente,
				// allora uso quello invece di considerare
				// la data calcolata
				if (dataProduzione != null && articoloAnagrafica.isDistinta()) {
					dataDocumento = dataProduzione;
				}

				for (RisultatoMRPArticoloBucket risultatoMRPArticoloBucket : risultatoGiorno
						.getRisultatiMRPArticoloBucket()) {
					// Se nel bucket ho righe create da l sottoscorta non le
					// considero
					if (!(risultatoMRPArticoloBucket instanceof RisultatoSottoScortaMRPArticoloBucket)) {

						// Calcolo la qta richiesta data dalla qta dell'ordine o
						// dei padri + attrezzaggio
						ArticoloConfigurazioneKey keyBom = new ArticoloConfigurazioneKey(getIdArticolo(),
								risultatoMRPArticoloBucket.getIdConfigurazioneDistinta());
						Bom bom = boms.get(keyBom);
						if (bom != null) {
							double qtaR = risultatoMRPArticoloBucket.getQtaR();
							// Aggiungo la qta di attrezzaggio solamente se devo
							// produrre qualcosa.
							// E se ho un ordine cliente. All'ordine produzione
							// ho già aggiunto l'attrezzaggio in fase
							// di creazione.
							if (qtaR > 0.0 && risultatoMRPArticoloBucket.getTipoRiga() == 0) {
								risultatoMRPArticoloBucket.setQtaR(qtaR
										+ ObjectUtils.defaultIfNull(bom.getQtaAttrezzaggioDistinta(), 0.0));
								// +
								// ObjectUtils.defaultIfNull(bom.getQtaAttrezzaggioFasi(),
								// 0.0)
							}
						}
						// Double qtaRiordino = 0.0;
						// Double lottoRiordino =
						// risultatoMRPArticoloBucket.getLottoRiordino();
						if (risultatoMRPArticoloBucket.getTipoRiga() != 2) {
							disponibilita = disponibilita - risultatoMRPArticoloBucket.getQtaR();
						}

						if (disponibilita >= 0) {
							risultatoMRPArticoloBucket.setQtaPor(0);
						} else {
							double qtaDaOrdinare = Math.abs(disponibilita);
							// if (minOrdinabile > 0 && qtaDaOrdinare <
							// minOrdinabile) {
							// qtaDaOrdinare = minOrdinabile;
							// }
							risultatoMRPArticoloBucket.setQtaPor(qtaDaOrdinare);
							disponibilita = qtaDaOrdinare + disponibilita;
						}
						risultatoMRPArticoloBucket.setQtaInArrivo(s[i]);
						if (risultatoMRPArticoloBucket.getTipoRiga() != 2) {
							risultatoMRPArticoloBucket.setDataDocumento(dataDocumento);
						}
						risultatoMRPArticoloBucket.setGiacenza(giacenza.getGiacenza());
						risultatoMRPArticoloBucket.setDisponibilita(disponibilitaPrimaDiUtilizzo);
						risultatoMRPArticoloBucket.setDisponibilitaDopoUtilizzo(disponibilita);
						disponibilitaPrimaDiUtilizzo = disponibilita;

						risultatoMRPArticoloBucket.setScorta(giacenza.getScorta());
						risultatoMRPArticoloBucket.setArticoloAnagrafica(articoloAnagrafica);
						risultatoMRPArticoloBucket.setOrdinamento(ordinamento++);

						ArticoloDepositoConfigurazioneKey keyConfigurazione = new ArticoloDepositoConfigurazioneKey(
								articoloAnagrafica.getIdArticolo(), key.getIdDeposito(),
								risultatoMRPArticoloBucket.getIdConfigurazioneDistinta());
						RigheCalcolo[] risultatoConfigurazione = risultatiConfigurazione.get(keyConfigurazione);
						risultatoConfigurazione = ObjectUtils.defaultIfNull(risultatoConfigurazione,
								new RigheCalcolo[numBucket]);
						RigheCalcolo risultatoGiornoCalcolato = ObjectUtils.defaultIfNull(risultatoConfigurazione[i
						                                                                                          - bucketDataDocumento], new RigheCalcolo());
						risultatoGiornoCalcolato.addRisultatiMRPArticoloBucket(
								risultatoMRPArticoloBucket.getIdConfigurazioneDistinta(),
								risultatoMRPArticoloBucket.getIdArticolo(), risultatoMRPArticoloBucket);
						risultatoConfigurazione[i - bucketDataDocumento] = risultatoGiornoCalcolato;
						risultatiConfigurazione.put(keyConfigurazione, risultatoConfigurazione);
					}
				}
				// Controllo le scorte. Se sono sotto scorta creo una riga per
				// la scorta.
				if (disponibilita < giacenza.getScorta()) {
					double qtaDaOrdinare = giacenza.getScorta() - disponibilita;
					if (minOrdinabile > 0 && qtaDaOrdinare < minOrdinabile) {
						qtaDaOrdinare = minOrdinabile;
					}
					disponibilita = qtaDaOrdinare + disponibilita;
					RisultatoMRPArticoloBucket rigaScorta = new RisultatoMRPArticoloBucket();
					rigaScorta.setDataDocumento(dataDocumento);
					rigaScorta.setDataConsegna(dataConsegna);
					rigaScorta.setIdDeposito(key.getIdDeposito());
					rigaScorta.setGiacenza(giacenza.getGiacenza());
					rigaScorta.setDisponibilita(disponibilitaPrimaDiUtilizzo);
					rigaScorta.setDisponibilitaDopoUtilizzo(disponibilita);
					rigaScorta.setScorta(giacenza.getScorta());
					rigaScorta.setQtaR(qtaDaOrdinare);
					rigaScorta.setQtaPor(qtaDaOrdinare);
					rigaScorta.setArticoloAnagrafica(articoloAnagrafica);
					rigaScorta.setOrdinamento(ordinamento++);

					ArticoloDepositoConfigurazioneKey keyConfigurazione = new ArticoloDepositoConfigurazioneKey(
							articoloAnagrafica.getIdArticolo(), key.getIdDeposito(), null);
					RigheCalcolo[] risultatoConfigurazione = ObjectUtils.defaultIfNull(
							risultatiConfigurazione.get(keyConfigurazione), new RigheCalcolo[numBucket]);
					RigheCalcolo risultatoGiornoCalcolato = ObjectUtils.defaultIfNull(risultatoConfigurazione[i
					                                                                                          - bucketDataDocumento], new RigheCalcolo());
					risultatoGiornoCalcolato.addRisultatiMRPArticoloBucket(rigaScorta.getIdConfigurazioneDistinta(),
							rigaScorta.getIdArticolo(), rigaScorta);
					risultatoConfigurazione[i - bucketDataDocumento] = risultatoGiornoCalcolato;
					risultatiConfigurazione.put(keyConfigurazione, risultatoConfigurazione);

				}
			}
		}
	}

	protected double[] calcolaS() {
		// for (Object[] rigaInevaso : ordinatoInevasoFornitore) {
		// int idDeposito = (int) rigaInevaso[3];
		// int idArticolo = (int) rigaInevaso[0];
		// ArticoloDepositoKey chiave = new ArticoloDepositoKey(idArticolo,
		// idDeposito);
		// if (!chiave.equals(chiaveCorrente)) {
		// arrayRequirement = new double[numTime];
		// ordinatiFornitoreProduzione.put(chiave, arrayRequirement);
		// chiaveCorrente = chiave;
		// }
		// if (arrayRequirement != null) {
		// int pos = ((BigInteger) rigaInevaso[2]).intValue() +
		// MrpCalcoloManager.BUCKET_ZERO;
		// if (pos > -1 && pos < arrayRequirement.length) {
		// arrayRequirement[pos] = (double) rigaInevaso[1];
		// }
		// }
		// }
		double[] s = new double[numBucket];
		if (ordiniFornitoreCalcolo != null) {
			for (OrdiniFornitoreCalcolo ordineFornitore : ordiniFornitoreCalcolo) {
				int pos = ordineFornitore.getDiffData() + MrpCalcoloManager.BUCKET_ZERO;
				s[pos] = s[pos] + ordineFornitore.getQta();
			}
		}
		return s;
	}

	/**
	 * @return Returns the calendarWork.
	 */
	public boolean[] getCalendarWork() {
		return calendarWork;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public int getIdArticolo() {
		return key.getIdArticolo();
	}

	/**
	 *
	 * @return id deposito
	 */
	public int getIdDeposito() {
		return key.getIdDeposito();
	}

	/**
	 *
	 * @return chiave con articoloedeposito.
	 */
	public ArticoloDepositoKey getKey() {
		return key;
	}

	/**
	 * @return the ordinamento
	 */
	public int getOrdinamento() {
		return ordinamento;
	}

	/**
	 * @return Returns the risultato.
	 */
	public Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> getRisultato() {
		return risultatiConfigurazione;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	/**
	 * @return per calcolare le righe ordine cliente o produzione.
	 */
	protected boolean isRigaOrdineCliente() {
		return true;
	}

	@Override
	public void release() {
	}

	@Override
	public void run() {
		calcola();
	}

	/**
	 * @param calendarWork
	 *            The calendarWork to set.
	 */
	public void setCalendarWork(boolean[] calendarWork) {
		this.calendarWork = calendarWork;
	}

}
