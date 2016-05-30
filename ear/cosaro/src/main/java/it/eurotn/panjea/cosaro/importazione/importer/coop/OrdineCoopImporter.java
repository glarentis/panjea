package it.eurotn.panjea.cosaro.importazione.importer.coop;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.cosaro.CosaroSettings;
import it.eurotn.panjea.cosaro.CosaroSettingsBean;
import it.eurotn.panjea.cosaro.RigaFileCoop;
import it.eurotn.panjea.cosaro.importazione.importer.AbstractImporter;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.OrdineImportato.EProvenienza;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.manager.interfaces.ImportazioneOrdiniManager;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

@Stateless(name = "Panjea.CosaroImporter.COOP")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CosaroImporter.COOP")
public class OrdineCoopImporter extends AbstractImporter {
    private static Logger logger = Logger.getLogger(OrdineCoopImporter.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private CosaroSettings cosaroSettings;

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private ImportazioneOrdiniManager importazioneOrdiniManager;

    /**
     * Da un ordine unicomm genera una testata di backOrder.
     *
     * @param ordineCoop
     *            ordine unicomm sorgente
     * @param codiceCliente
     *            codice del cliente unicomm
     * @param codiceTipoDocumento
     *            codice del tipo documento da utilizzare per l'importazione
     * @param righeFile
     *            lista con le righe contenute nel file
     * @param numRigaFile
     *            numDiriga nel file
     * @param uuid
     *            uuid indicante l'uid della generazione
     * @return testata di un backorder
     */
    private OrdineImportato generaOrdineImportato(OrdineCoop ordineCoop, String codiceCliente,
            String codiceTipoDocumento, int numRigaFile, List<String> righeFile, UUID uuid) {
        OrdineImportato ordineImportato = new OrdineImportato();
        ordineImportato.setCodiceEntita(codiceCliente);
        ordineImportato.setCodiceTipoDocumento(codiceTipoDocumento);
        ordineImportato.setData(ordineCoop.getDataOrdine());
        ordineImportato.setProvenienza(EProvenienza.ATON);
        ordineImportato.setNumero(ordineCoop.getNumOrdine());
        ordineImportato.setIdSedeImportata(Integer.parseInt(ordineCoop.getCodicePiattaforma()));
        // Scrivo le righe del file nel database.
        RigaFileCoop rigaFileCoop = new RigaFileCoop();
        rigaFileCoop.setText(righeFile.get(numRigaFile));
        rigaFileCoop.setNumeroRiga(numRigaFile);
        rigaFileCoop.setNumeroOrdine(ordineImportato.getNumero());
        rigaFileCoop.setDataOrdine(ordineImportato.getData());
        rigaFileCoop.setTipoRiga("T");
        rigaFileCoop.setUuid(uuid.toString());
        numRigaFile++;
        Query query = panjeaDAO.prepareQuery("delete from RigaFileCoop r where r.text=:testo");
        try {
            query.setParameter("testo", rigaFileCoop.getText());
            query.executeUpdate();
            panjeaDAO.save(rigaFileCoop);
        } catch (DAOException e1) {
            e1.printStackTrace();
        }
        // Genero le righe
        int numRiga = 0;
        for (RigaOrdineCoop rigaCoop : ordineCoop.getRighe()) {
            rigaFileCoop = new RigaFileCoop();
            rigaFileCoop.setText(righeFile.get(numRigaFile));
            rigaFileCoop.setNumeroRiga(numRigaFile);
            rigaFileCoop.setNumeroOrdine(ordineImportato.getNumero());
            rigaFileCoop.setDataOrdine(ordineImportato.getData());
            rigaFileCoop.setUuid(uuid.toString());
            RigaOrdineImportata rigaOrdineImportata = generaRiga(rigaCoop, CosaroSettingsBean.COLLI_ATTRIBUTO);
            if (rigaOrdineImportata != null) {
                rigaOrdineImportata.setOrdine(ordineImportato);
                rigaOrdineImportata.setNumeroRiga(++numRiga);
                ordineImportato.addRiga(rigaOrdineImportata);
                rigaFileCoop.setNumeroRigaColli(numRiga);
            }
            rigaOrdineImportata = generaRiga(rigaCoop, CosaroSettingsBean.PEZZI_ATTRIBUTO);
            if (rigaOrdineImportata != null) {
                rigaOrdineImportata.setOrdine(ordineImportato);
                rigaOrdineImportata.setNumeroRiga(++numRiga);
                ordineImportato.addRiga(rigaOrdineImportata);
                rigaFileCoop.setNumeroRigaPezzi(numRiga);
            }
            try {
                query.setParameter("testo", rigaFileCoop.getText());
                query.executeUpdate();
                panjeaDAO.save(rigaFileCoop);
            } catch (DAOException e1) {
                e1.printStackTrace();
            }
            numRigaFile++;
        }
        return ordineImportato;
    }

    /**
     *
     * @param rigaCoop
     *            rigaDiOrdigine
     * @param attributoQta
     *            attributo contenente la qta
     * @return rigaOrdine con l'attributo desiderato avvalorato
     */
    private RigaOrdineImportata generaRiga(RigaOrdineCoop rigaCoop, String attributoQta) {
        RigaOrdineImportata rigaOrdineImportata = new RigaOrdineImportata();
        rigaOrdineImportata.setQta(1.0);
        rigaOrdineImportata.setPrezzoUnitarioDeterminato(BigDecimal.ZERO);
        rigaOrdineImportata.setCodiceArticolo(rigaCoop.getCodiceArticolo());
        rigaOrdineImportata.setNote(rigaCoop.getLinea());
        StringBuilder attributi = new StringBuilder();
        Integer pezzi = rigaCoop.getPezzi();
        Integer colli = rigaCoop.getColli();
        if ("colli".equals(attributoQta) && colli != 0) {
            attributi.append("colli=").append(colli).append("; ");
            attributi.append("pezzi=0").append("; ");
        } else if ("pezzi".equals(attributoQta) && pezzi != 0 && colli == 0) {
            attributi.append("pezzi=").append(pezzi).append("; ");
            attributi.append("colli=0").append("; ");
        } else {
            return null;
        }
        rigaOrdineImportata.setAttributi(attributi.toString());
        return rigaOrdineImportata;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean importa(File file) {
        String codiceCliente = cosaroSettings.caricaCodiceCoop();
        if (codiceCliente == null) {
            return false;
        }

        String codiceTipoDocumento = cosaroSettings.caricaCodiceTipoDocumentoCoop();
        if (codiceTipoDocumento == null) {
            return false;
        }

        List<String> righeFile = null;
        try {
            righeFile = FileUtils.readLines(file);
        } catch (IOException e1) {
            logger.error("-->errore nel leggere il file " + file, e1);
            throw new RuntimeException(e1);
        }

        StreamFactory factory = cosaroSettings.getStreamTemplate("COOP.xml");
        BeanReader in = factory.createReader("ordinistream", file);
        in.setErrorHandler(new FileReaderErrorHandler());
        System.out.println("Importo gli ordini del cliente coop " + codiceCliente);
        OrdineCoop ordineCoop;
        OrdineImportato ordineImportato = new OrdineImportato();
        List<OrdineImportato> ordiniImportati = new ArrayList<OrdineImportato>();

        int numRigaFile = 0;
        try {
            while ((ordineCoop = (OrdineCoop) in.read()) != null) {
                // Verifico che l'ordine non esista già, in tal caso lancio un'errore
                if (!importazioneOrdiniManager.checkOrdineUnivoco(ordineCoop.getNumOrdine(), codiceCliente)) {
                    Documento documento = new Documento();
                    documento.getCodice().setCodice(StringUtils.defaultString(ordineCoop.getNumOrdine(), "0"));
                    throw new RuntimeException(new DocumentoDuplicateException("Documento duplicato ", documento));
                }

                ordineImportato = generaOrdineImportato(ordineCoop, codiceCliente, codiceTipoDocumento, numRigaFile,
                        righeFile, UUID.randomUUID());

                try {
                    ordineImportato = panjeaDAO.save(ordineImportato);
                } catch (Exception e) {
                    logger.error("-->errore nel salvare l'ordine importato " + ordineImportato, e);
                    throw new RuntimeException(e);
                }
                ordiniImportati.add(ordineImportato);
            }

            ParametriRicercaOrdiniImportati parametri = new ParametriRicercaOrdiniImportati();
            parametri.setProvenienza(EProvenienza.ATON);
            importazioneOrdiniManager.aggiornaRigheOrdineImportate(parametri);
            panjeaDAO.getEntityManager().flush();
            // Con questa importazione mancano parecchi dati recuperati dalle normali importazioni
            // ordine
            // (codicePagamento,prezzo etc)
            // Risalvo le righe con i dati che ho calcolato dalle condizioni preimpostate
            for (OrdineImportato ordineDaAggiornare : ordiniImportati) {
                // Ricarico l'ordine con i dati associati (isCodicePagamento etc..)
                panjeaDAO.getEntityManager().refresh(ordineDaAggiornare);
                ordineDaAggiornare.setPagamento(ordineDaAggiornare.getPagamentoDeterminato());
                try {
                    panjeaDAO.save(ordineDaAggiornare);
                } catch (DAOException e) {
                    logger.error("-->errore nel salvare la riga ordine per l'aggiornamento del pagamento", e);
                    throw new RuntimeException(e);
                }
            }
            panjeaMessage.send("importati " + ordiniImportati.size() + " ordini coop", new String[] { "evasione" }, 5,
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
        return true;
    }
}
