package it.eurotn.panjea.magazzino.manager.documento.fatturazione;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import commonj.work.Work;
import commonj.work.WorkItem;
import de.myfoo.commonj.util.ThreadPool;
import de.myfoo.commonj.work.FooWorkManager;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.exception.SedeNonAppartieneAdEntitaException;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces.DocumentoFatturazioneGenerator;
import it.eurotn.panjea.magazzino.manager.documento.fatturazione.interfaces.PreparaFatturazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneDifferitaGenerator;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneManager;

/**
 * Genera i documenti temporanei necessari alla fatturazione differita.<br/>
 * Per confermarli usare {@link FatturazioneManager#confermaMovimentoInFatturazione()}
 *
 * @author giangi
 */
@Stateless(mappedName = "Panjea.FatturazioneDifferitaGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FatturazioneDifferitaGenerator")
public class FatturazioneDifferitaGeneratorBean extends AbstractFatturazioneDifferitaGenerator
        implements FatturazioneDifferitaGenerator {

    public class DocumentoGeneratorWork implements Work {

        private List<AreaMagazzinoFatturazione> listAree;
        private TipoAreaMagazzino tipoAreaMagazzinoDestinazione;
        private Date dataDocumentoDestinazione;
        private SedeEntita sedeEntitaDestinazione;
        private EntitaLite entitaDestinazione;
        private int numeroDocumento;
        private String noteFatturazione;
        private String uuidContabilizzazione;
        private String utente;

        private DocumentoFatturazioneGenerator documentoFatturazioneGenerator;

        private Exception exceptionGenerated = null;

        /**
         * Genera il documento di fatturazione.
         *
         * @param listAree
         *            aree da fatturare
         * @param tipoAreaMagazzinoDestinazione
         *            tipo area magazzino del documento di fatturazione
         * @param dataDocumentoDestinazione
         *            data del documento di fatturazione
         * @param sedeEntitaDestinazione
         *            sede entità del documento di fatturazione
         * @param entitaDestinazione
         *            entità del documento di fatturazione
         * @param numeroDocumento
         *            numero del documento di fatturazione
         * @param noteFatturazione
         *            note del documento di fatturazione
         * @param uuidContabilizzazione
         *            uuid di contabilizzazione
         * @param utente
         *            utente di generazione
         * @param documentoFatturazioneGenerator
         *            documentoFatturazioneGenerator
         */
        public DocumentoGeneratorWork(final List<AreaMagazzinoFatturazione> listAree,
                final TipoAreaMagazzino tipoAreaMagazzinoDestinazione, final Date dataDocumentoDestinazione,
                final SedeEntita sedeEntitaDestinazione, final EntitaLite entitaDestinazione, final int numeroDocumento,
                final String noteFatturazione, final String uuidContabilizzazione, final String utente,
                final DocumentoFatturazioneGenerator documentoFatturazioneGenerator) {
            this.listAree = listAree;
            this.tipoAreaMagazzinoDestinazione = tipoAreaMagazzinoDestinazione;
            this.dataDocumentoDestinazione = dataDocumentoDestinazione;
            this.sedeEntitaDestinazione = sedeEntitaDestinazione;
            this.entitaDestinazione = entitaDestinazione;
            this.numeroDocumento = numeroDocumento;
            this.noteFatturazione = noteFatturazione;
            this.uuidContabilizzazione = uuidContabilizzazione;
            this.utente = utente;
            this.documentoFatturazioneGenerator = documentoFatturazioneGenerator;
        }

        /**
         * @return the exceptionGenerated
         */
        public Exception getExceptionGenerated() {
            return exceptionGenerated;
        }

        /**
         * @return the numeroDocumento
         */
        public int getNumeroDocumento() {
            return numeroDocumento;
        }

        @Override
        public boolean isDaemon() {
            return false;
        }

        @Override
        public void release() {
            exceptionGenerated = null;
        }

        @Override
        public void run() {
            try {
                this.documentoFatturazioneGenerator.generaDocumentoFatturazione(listAree, tipoAreaMagazzinoDestinazione,
                        dataDocumentoDestinazione, sedeEntitaDestinazione, entitaDestinazione, numeroDocumento,
                        noteFatturazione, uuidContabilizzazione, utente);
            } catch (Exception e) {
                exceptionGenerated = e;
            }
        }

    }

    private static Logger logger = Logger.getLogger(FatturazioneDifferitaGeneratorBean.class);

    @EJB
    private DocumentoFatturazioneGenerator documentoFatturazioneGenerator;

    @EJB
    @IgnoreDependency
    private PreparaFatturazioneManager preparaFatturazioneManager;

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public void genera(List<AreaMagazzinoLite> areeDaFatturare, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione, String utente)
                    throws RigaArticoloNonValidaException, SedeNonAppartieneAdEntitaException {
        Calendar calendarInizio = Calendar.getInstance();
        logger.debug("--> Enter generaFatturazioneDifferitaTemporanea");

        List<DocumentoGeneratorWork> documentoGeneratorWorks = new ArrayList<FatturazioneDifferitaGeneratorBean.DocumentoGeneratorWork>();

        List<Documento> documentiConSediNonValide = getDocumentiConSediNonAppartenentiAdEntita(areeDaFatturare);
        if (!documentiConSediNonValide.isEmpty()) {
            throw new SedeNonAppartieneAdEntitaException(documentiConSediNonValide);
        }

        // genero l'UUID per la contabilizzazione
        Random random = new Random();
        long r1 = random.nextLong();
        long r2 = random.nextLong();
        String uuid = Long.toHexString(r1) + Long.toHexString(r2);

        int numeroDocFatturazione = -1;

        // chiamo il carica sul preparaFatturazioneManager per aprire la
        // transazione che mi permette di eseguire la query
        List<AreaMagazzinoFatturazione> areeFatturazione = preparaFatturazioneManager
                .caricaAreeMagazzinoFatturazione(areeDaFatturare);

        // ordino le righe all'interno delle aree per ordinamento
        for (AreaMagazzinoFatturazione area : areeFatturazione) {
            Collections.sort(area.getRigheMagazzino(), new Comparator<RigaMagazzino>() {

                @Override
                public int compare(RigaMagazzino o1, RigaMagazzino o2) {
                    return new Double(o1.getOrdinamento()).compareTo(new Double(o2.getOrdinamento()));
                }
            });
        }

        // cerco all'interno della lista se ci sono documenti che devono essere forzatamente
        // fatturati su un unico documento ( flag raggruppamento bolle sull'area magazzino).
        List<AreaMagazzinoFatturazione> areeDaFatturareConRaggruppamento = new ArrayList<AreaMagazzinoFatturazione>();

        for (AreaMagazzinoFatturazione areaMagazzinoFatturazione : areeFatturazione) {

            if (areaMagazzinoFatturazione.isRaggruppamentoBolle()) {
                // aggiungo l'area a quelle da fatturare raggruppate
                areeDaFatturareConRaggruppamento.add(areaMagazzinoFatturazione);
            } else {
                // fatturo subito il documento
                List<AreaMagazzinoFatturazione> listArea = new ArrayList<AreaMagazzinoFatturazione>();
                listArea.add(areaMagazzinoFatturazione);

                DocumentoGeneratorWork work = new DocumentoGeneratorWork(listArea,
                        areaMagazzinoFatturazione.getTipoAreaMagazzinoPerFatturazione(), dataDocumentoDestinazione,
                        areaMagazzinoFatturazione.getSedeEntitaPrincipale(), areaMagazzinoFatturazione.getEntita(),
                        numeroDocFatturazione, noteFatturazione, uuid, utente, documentoFatturazioneGenerator);
                documentoGeneratorWorks.add(work);
                numeroDocFatturazione--;
            }
        }

        // se sono rimaste dei documenti da fatturare procedo
        if (!areeDaFatturareConRaggruppamento.isEmpty()) {
            // Faccio il primo reggruppamento per entità del documento
            AreeMagazzinoEntitaGroup areeEntitaGroup = new AreeMagazzinoEntitaGroup(areeDaFatturareConRaggruppamento);

            // Raggruppo ora per condizione di pagamento per ogni entità
            for (List<AreaMagazzinoFatturazione> listAreeEntita : areeEntitaGroup) {
                AreeMagazzinoCodicePagamentoGroup areeMagazzinoCodicePagamentoGroup = new AreeMagazzinoCodicePagamentoGroup(
                        listAreeEntita);

                // Raggruppo per tipo documento di fatturazione
                for (List<AreaMagazzinoFatturazione> listAreeCodPag : areeMagazzinoCodicePagamentoGroup) {
                    AreeMagazzinoTipoDocumentoGroup areeTipoDocGroup = new AreeMagazzinoTipoDocumentoGroup(
                            listAreeCodPag);

                    // Raggruppo per sede entita
                    for (List<AreaMagazzinoFatturazione> listAreeTipoDoc : areeTipoDocGroup) {
                        AreeMagazzinoSedeGroup areeSedeGroup = new AreeMagazzinoSedeGroup(listAreeTipoDoc);

                        List<AreaMagazzinoFatturazione> listAreePerFatturazioneEntita = new ArrayList<AreaMagazzinoFatturazione>();

                        for (List<AreaMagazzinoFatturazione> listAreeSede : areeSedeGroup) {

                            DocumentoGeneratorWork work = null;
                            switch (listAreeSede.get(0).getSedeMagazzino()
                                    .getTipologiaGenerazioneDocumentoFatturazione()) {
                            case ENTITA:
                                listAreePerFatturazioneEntita.addAll(listAreeSede);
                                break;
                            case SEDE:
                                AreaMagazzinoFatturazione areaRif = listAreeSede.get(0);

                                work = new DocumentoGeneratorWork(listAreeSede,
                                        areaRif.getTipoAreaMagazzinoPerFatturazione(), dataDocumentoDestinazione,
                                        areaRif.getSedeEntita(), areaRif.getEntita(), numeroDocFatturazione,
                                        noteFatturazione, uuid, utente, documentoFatturazioneGenerator);
                                documentoGeneratorWorks.add(work);

                                numeroDocFatturazione--;
                                break;
                            case DOCUMENTO:
                                for (AreaMagazzinoFatturazione areaMagazzinoFatturazione : listAreeSede) {
                                    List<AreaMagazzinoFatturazione> listArea = new ArrayList<AreaMagazzinoFatturazione>();
                                    listArea.add(areaMagazzinoFatturazione);

                                    work = new DocumentoGeneratorWork(listArea,
                                            areaMagazzinoFatturazione.getTipoAreaMagazzinoPerFatturazione(),
                                            dataDocumentoDestinazione, areaMagazzinoFatturazione.getSedeEntita(),
                                            areaMagazzinoFatturazione.getEntita(), numeroDocFatturazione,
                                            noteFatturazione, uuid, utente, documentoFatturazioneGenerator);
                                    documentoGeneratorWorks.add(work);

                                    numeroDocFatturazione--;
                                }
                                break;
                            default:
                                throw new IllegalArgumentException();
                            }
                        }

                        if (listAreePerFatturazioneEntita.size() > 0) {
                            AreaMagazzinoFatturazione areaRif = areeSedeGroup.get(0).get(0);

                            DocumentoGeneratorWork work = new DocumentoGeneratorWork(listAreePerFatturazioneEntita,
                                    areaRif.getTipoAreaMagazzinoPerFatturazione(), dataDocumentoDestinazione,
                                    areaRif.getSedeEntitaPrincipale(), areaRif.getEntita(), numeroDocFatturazione,
                                    noteFatturazione, uuid, utente, documentoFatturazioneGenerator);
                            documentoGeneratorWorks.add(work);

                            numeroDocFatturazione--;
                        }
                    }
                }
            }
        }

        // create the thread pool for this work manager
        ThreadPool pool = new ThreadPool(10, 10, 1000);

        // create the work manager
        FooWorkManager workManager = new FooWorkManager(pool, 0);

        List<WorkItem> movimentiWorkItem = new ArrayList<WorkItem>();

        for (DocumentoGeneratorWork work : documentoGeneratorWorks) {
            try {
                movimentiWorkItem.add(workManager.schedule(work));
            } catch (Exception e) {
                logger.error("--> errore nel lanciare il task per la fatturazione differita ", e);
                throw new RuntimeException("errore nel lanciare il task per la fatturazione differita", e);
            }
        }

        try {
            workManager.waitForAll(movimentiWorkItem, 120000);
            for (WorkItem workItem : movimentiWorkItem) {
                DocumentoGeneratorWork documentoGeneratorWork = (DocumentoGeneratorWork) workItem.getResult();
                if (documentoGeneratorWork.getExceptionGenerated() != null) {
                    throw documentoGeneratorWork.getExceptionGenerated();
                }
            }
        } catch (RigaArticoloNonValidaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("-->errore nell'aspettare i processi per la valorizzazione dei depositi", e);
            throw new RuntimeException("-->errore nell'aspettare i processi per la valorizzazione dei depositi ", e);
        } finally {
            workManager.shutdown();
        }

        logger.debug("--> Tempo di fatturazione: "
                + (Calendar.getInstance().getTimeInMillis() - calendarInizio.getTimeInMillis()));

        logger.debug("--> Exit generaFatturazioneDifferitaTemporanea");
    }

    /**
     * Per ogni documento controlla che la sede dia dell'entità a cui il documento è intestato.
     *
     * @param areeDaFatturare
     *            areeDaFatturare
     * @return Lista documenti che hanno sedi non valide.
     */
    List<Documento> getDocumentiConSediNonAppartenentiAdEntita(List<AreaMagazzinoLite> areeDaFatturare) {
        List<Documento> documentiConSediNonValide = new ArrayList<Documento>();
        for (AreaMagazzinoLite areaMagazzino : areeDaFatturare) {
            SedeEntita sedeDocumento = areaMagazzino.getDocumento().getSedeEntita();
            Entita entitaDocumento = areaMagazzino.getDocumento().getEntita().creaProxyEntita();
            if (!entitaDocumento.equals(sedeDocumento.getEntita())) {
                documentiConSediNonValide.add(areaMagazzino.getDocumento());
            }
        }
        return documentiConSediNonValide;
    }
}
