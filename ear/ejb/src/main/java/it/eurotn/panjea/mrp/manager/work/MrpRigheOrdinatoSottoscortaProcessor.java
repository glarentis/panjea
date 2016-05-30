/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;


/**
 * @author leonardo
 *
 */
public class MrpRigheOrdinatoSottoscortaProcessor {
}
//
// /**
// * Costruttore.
// *
// * @param workManager
// * workManager
// * @param righeOrdinatoCliente
// * righeOrdinatoCliente
// * @param boms
// * boms
// * @param articoliAnagrafica
// * articoliAnagrafica
// * @param giacenze
// * giacenze
// * @param ordinatoFornitore
// * ordinatoFornitore
// * @param calendarWork
// * calendarWork
// * @param startDate
// * startDate
// * @param numTime
// * numTime
// */
// public MrpRigheOrdinatoSottoscortaProcessor(final WorkManager workManager,
// final RigheOrdinatoCliente righeOrdinatoCliente, final Map<String, Bom> boms,
// final Map<Integer, ArticoloAnagrafica> articoliAnagrafica,
// final Map<ArticoloDepositoKey, Giacenza> giacenze,
// final Map<ArticoloDepositoKey, double[]> ordinatoFornitore, final boolean[] calendarWork,
// final Date startDate, final int numTime) {
// super(workManager, righeOrdinatoCliente, boms, articoliAnagrafica, giacenze, ordinatoFornitore, calendarWork,
// startDate, numTime);
// }
//
// @SuppressWarnings("unchecked")
// @Override
// public Map<ArticoloDepositoConfigurazioneKey, ArticoloCalcoloWork> calcola(
// // Map<ArticoloDepositoConfigurazioneKey, ArticoloCalcoloWork> risultati,
// // Map<ArticoloDepositoConfigurazioneKey, WorkItem> calcolati) {
// // try {
// // Collection<WorkItem> valuesCalcolati = new ArrayList<>(calcolati.values());
// // boolean workersDone = valuesCalcolati.isEmpty();
// // while (!workersDone) {
// // Collection<WorkItem> workerFinished = workManager.waitForAny(valuesCalcolati, 10000);
// // valuesCalcolati.removeAll(workerFinished);
// // for (WorkItem workItem : workerFinished) {
// // ArticoloCalcoloWork articoloCalcolo = (ArticoloCalcoloWork) workItem.getResult();
// //
// // logger.info("Calcolata richiesta sottoscorta id articolo " + articoloCalcolo.getKey());
// // ArticoloCalcoloWork articoloCalcolato = risultati.get(articoloCalcolo.getKey());
// // if (articoloCalcolato == null) {
// // risultati.put(articoloCalcolo.getKey(), articoloCalcolo);
// // } else {
// // for (int i = 0; i < articoloCalcolo.getRisultato().length; i++) {
// // if (articoloCalcolo.getRisultato()[i] != null) {
// // articoloCalcolato.getRisultato()[i] = articoloCalcolo.getRisultato()[i];
// // }
// // }
// // risultati.put(articoloCalcolo.getKey(), articoloCalcolato);
// // }
// // logger.trace("Elaborato articolo sottoscorta {}" + articoloCalcolo.getIdArticolo());
// // }
// // workersDone = valuesCalcolati.isEmpty();
// // }
// return risultati;
// } catch (ArrayIndexOutOfBoundsException e) {
// logger.error("Intervallo di calcolo insufficiente", e);
// throw e;
// } catch (Exception e) {
// logger.error("Errore nelcalcolo mrp", e);
// throw new RuntimeException("Errore nelcalcolo mrp", e);
// }
// }
//
// @Override
// protected ArticoloCalcoloWork createArticoloCalcoloWork(int idDeposito, int idArticolo, Integer idConfigurazione,
// Giacenza giacenza, RigheCalcolo[] righeCalcolo, double[] s, int numBucket,
// ArticoloAnagrafica articoloAnagrafica, boolean[] calendarWorkParam, int ordinamento,
// Map<Bom, RigheCalcolo[]> righeCalcolatePadre, Date startDateParam) {
// return new ArticoloCalcoloSottoScortaWork(idDeposito, idArticolo, idConfigurazione, giacenza, righeCalcolo, s,
// numBucket, articoloAnagrafica, calendarWork, ordinamento, null, startDate);
// }
//
// }
