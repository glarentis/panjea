package it.eurotn.panjea.cosaro.importazione.importer.unicomm;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.cosaro.CosaroSettings;
import it.eurotn.panjea.cosaro.importazione.importer.AbstractImporter;
import it.eurotn.panjea.cosaro.importazione.importer.ImporterCosaro;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdiniManager;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

@Stateless(name = "Panjea.CosaroImporter.UNICOMM")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CosaroImporter.UNICOMM")
public class UnicommImporter extends AbstractImporter implements ImporterCosaro {

    private static Logger logger = Logger.getLogger(UnicommImporter.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private ImportazioneOrdiniManager importazioneOrdiniManager;

    @EJB
    private CosaroSettings cosaroSettings;

    /**
     * Da un ordine unicomm genera una testata di backOrder.
     *
     * @param ordineUnicomm
     *            ordine unicomm sorgente
     * @param codiceCliente
     *            codice del cliente unicomm
     * @param codiceTipoDocumento
     *            codice del tipo documento da utilizzare per l'importazione
     * @return testata di un backorder
     */
    private OrdineImportato generaOrdineImportato(OrdineUnicomm ordineUnicomm, String codiceCliente,
            String codiceTipoDocumento) {
        OrdineImportato ordineImportato = new OrdineImportato();
        ordineImportato.setCodiceEntita(codiceCliente);
        ordineImportato.setCodiceTipoDocumento(codiceTipoDocumento);
        ordineImportato.setData(ordineUnicomm.getData());
        ordineImportato.setProvenienza(EProvenienza.ATON);
        ordineImportato.setNumero(ordineUnicomm.getNumero());
        ordineImportato.setIdSedeImportata(ordineUnicomm.getCodiceEnte());
        return ordineImportato;
    }

    /**
     * Da una riga ordine importata dal file unicomm genero una riga di ordine importato.
     *
     * @param ordineUnicomm
     *            riga odine unicomm
     * @param attributoQta
     *            nome dell'attributo contenente la qta
     * @return rigaordineImporatto
     */
    private RigaOrdineImportata generaRigaImportata(OrdineUnicomm ordineUnicomm) {
        RigaOrdineImportata rigaOrdineImportata = new RigaOrdineImportata();
        rigaOrdineImportata.setQta(1.0);
        rigaOrdineImportata.setPrezzoUnitarioDeterminato(BigDecimal.ZERO);
        rigaOrdineImportata.setCodiceArticolo(ordineUnicomm.getCodiceArticoloFornitore());
        rigaOrdineImportata.setNote("01");
        StringBuilder attributi = new StringBuilder();
        NumberFormat nb = NumberFormat.getInstance(Locale.ITALIAN);
        Number pezziNumber = 0;
        try {
            pezziNumber = nb.parse(ordineUnicomm.getPezzi());
        } catch (ParseException e) {
            logger.error("-->errore nel recuperare il numero pezzi de un ordine unicomm. Setto a zero", e);
            pezziNumber = 0;
        }
        double pezzi = pezziNumber.doubleValue();
        Integer colli = Integer.parseInt(ordineUnicomm.getColli());

        if (colli != 0) {
            attributi.append("colli=").append(colli).append("; ");
        } else {
            attributi.append("colli=0").append("; ");
        }

        if (pezzi != 0) {
            attributi.append("pezzi=").append(nb.format(pezzi)).append("; ");
        } else {
            attributi.append("pezzi=0").append("; ");
        }

        rigaOrdineImportata.setAttributi(attributi.toString());
        return rigaOrdineImportata;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 7200)
    @Override
    public boolean importa(File file) {

        String codiceCliente = cosaroSettings.caricaCodiceUnicomm();
        String codiceTipoDocumento = cosaroSettings.caricaCodiceTipoDocumentoUnicomm();
        if (codiceCliente == null || codiceTipoDocumento == null) {
            return false;
        }

        StreamFactory factory = cosaroSettings.getStreamTemplate("UNICOMM.xml");
        BeanReader in = factory.createReader("ordini", file);
        in.setErrorHandler(new FileReaderErrorHandler());
        OrdineUnicomm ordineUnicomm;
        OrdineImportato ordineImportato = new OrdineImportato();
        int numeroRiga = 0;
        List<OrdineImportato> ordiniImportati = new ArrayList<OrdineImportato>();
        try {
            while ((ordineUnicomm = (OrdineUnicomm) in.read()) != null) {
                // Verifico che l'ordine non esista già, in tal caso lancio un'errore
                if (!importazioneOrdiniManager.checkOrdineUnivoco(ordineUnicomm.getNumero(), codiceCliente)) {
                    Documento documento = new Documento();
                    documento.getCodice().setCodice(StringUtils.defaultString(ordineUnicomm.getNumero(), "0"));
                    throw new RuntimeException(new DocumentoDuplicateException("Documento duplicato ", documento));
                }

                if (!ordineUnicomm.getNumero().equals(ordineImportato.getNumero())) {
                    ordineImportato = generaOrdineImportato(ordineUnicomm, codiceCliente, codiceTipoDocumento);
                    ordiniImportati.add(ordineImportato);
                    numeroRiga = 0;
                }

                // Genero la riga importata in caso di colli
                RigaOrdineImportata riga = generaRigaImportata(ordineUnicomm);
                if (riga != null) {
                    riga.setNumeroRiga(++numeroRiga);
                    ordineImportato.addRiga(riga);
                }
            }

            List<OrdineImportato> ordiniDaCalcolare = new ArrayList<OrdineImportato>();
            for (OrdineImportato ordineImportatoDaSalvare : ordiniImportati) {
                try {
                    OrdineImportato ordineDaCalcolare = panjeaDAO.save(ordineImportatoDaSalvare);
                    ordiniDaCalcolare.add(ordineDaCalcolare);
                } catch (DuplicateKeyObjectException e) {
                    // Ordine duplicato, riutilizzo la DocumentoDuplicato exception creando un
                    // documento fake
                    Documento documento = new Documento();
                    documento.getCodice().setCodice(ordineImportato.getNumero());
                    throw new RuntimeException(new DocumentoDuplicateException("Documento duplicato ", documento));
                } catch (Exception e) {
                    logger.error("-->errore nell'importare l'ordine " + ordineImportato, e);
                    throw new RuntimeException(e);
                }
            }
            ParametriRicercaOrdiniImportati parametri = new ParametriRicercaOrdiniImportati();
            parametri.setProvenienza(EProvenienza.ATON);
            importazioneOrdiniManager.aggiornaRigheOrdineImportate(parametri);
            panjeaDAO.getEntityManager().flush();
            // Con questa importazione mancano parecchi dati recuperati dalle normali importazioni
            // ordine
            // (codicePagamento,prezzo etc)
            // Risalvo le righe con i dati che ho calcolato dalle condizioni preimpostate
            for (OrdineImportato ordineDaAggiornare : ordiniDaCalcolare) {
                panjeaDAO.getEntityManager().refresh(ordineDaAggiornare);
                ordineDaAggiornare.setPagamento(ordineDaAggiornare.getPagamentoDeterminato());
                try {
                    panjeaDAO.save(ordineDaAggiornare);
                } catch (DAOException e) {
                    logger.error("--> Errore nell'aggiornamento del pagamento sulla riga ordine", e);
                    throw new RuntimeException("Errore nell'aggiornamento del pagamento sulla riga ordine", e);
                }
            }
            panjeaMessage.send("importati " + ordiniImportati.size() + " ordini unicom", new String[] { "evasione" }, 5,
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return true;
    }

    @Override
    public boolean test(File file) {
        return leggiPrimaRiga(file).contains("UNICOMM");
    }
}
