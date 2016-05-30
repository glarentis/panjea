package it.eurotn.panjea.cosaro.sync.bilance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.Matcher;
import it.eurotn.panjea.cosaro.CosaroSettings;
import it.eurotn.panjea.cosaro.sync.EsportaDistinta;
import it.eurotn.panjea.cosaro.sync.RigaOrdineBilance;
import it.eurotn.panjea.exception.FileSemaforoPresente;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

@Stateless(name = "Panjea.EsportaDistintaBilance")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.EsportaDistintaBilance")
public class EsportaDistintaBilanceBean implements EsportaDistinta {

    public class RigheFilter implements Matcher<RigaDistintaCarico> {
        private String clienteUnicomm;
        private String clienteCoop;

        /**
         *
         * Costruttore.
         *
         * @param clienteUnicomm
         *            .
         * @param clienteCoop
         *            .
         */
        public RigheFilter(final String clienteUnicomm, final String clienteCoop) {
            this.clienteUnicomm = clienteUnicomm;
            this.clienteCoop = clienteCoop;
        }

        @Override
        public boolean matches(RigaDistintaCarico riga) {
            String codiceCliente = riga.getRigaArticolo().getAreaOrdine().getDocumento().getEntita().getCodice()
                    .toString();
            return codiceCliente.equals(clienteUnicomm) || codiceCliente.equals(clienteCoop);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(EsportaDistintaBilanceBean.class);

    @Resource
    protected SessionContext sessionContext;

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private ArticoloManager articoloManager;

    @EJB
    private CosaroSettings cosaroSettings;

    /**
     * Genera il file semaforo. Rilancia un errore se il file semaforo esiste.
     *
     * @param pathFile
     *            path del file semaforo
     * @throws FileSemaforoPresente
     *             rilanciato se il file semaforo esiste già
     */
    private void creaFileSemaforo(String pathFile) throws FileSemaforoPresente {
        File file = new File(pathFile);
        if (file.exists()) {
            sessionContext.setRollbackOnly();
            throw new FileSemaforoPresente(pathFile);
        }
        FileWriter outFile = null;
        try {
            outFile = new FileWriter(file);
            PrintWriter out = new PrintWriter(outFile);
            out.print("SEMAFORO");
            out.flush();
            out.close();
        } catch (IOException e) {
            LOGGER.error("-->errore nello scrivere il file semaforo " + pathFile, e);
        }
    }

    @Override
    public void esporta(List<DistintaCarico> distinteSelezionate) throws FileSemaforoPresente {
        LOGGER.debug("--> Enter esporta per bilance");
        // Filtro le distinte con ordini coop o unicomm
        String clienteCoop = cosaroSettings.caricaCodiceCoop();

        // Filtro le distinte con ordini coop o unicomm
        String clienteUnicomm = cosaroSettings.caricaCodiceUnicomm();

        BasicEventList<RigaDistintaCarico> righeDistinte = new BasicEventList<RigaDistintaCarico>();
        for (DistintaCarico distinta : distinteSelezionate) {
            righeDistinte.addAll(distinta.getRigheEvasione());
        }

        FilterList<RigaDistintaCarico> righeDaEsportare = new FilterList<RigaDistintaCarico>(righeDistinte,
                new RigheFilter(clienteCoop, clienteUnicomm));

        if (righeDaEsportare.isEmpty()) {
            return;
        }

        // Valido gli attributi (se non validi viene spedito un messaggio).
        if (!validaAttributi(righeDaEsportare)) {
            sessionContext.setRollbackOnly();
            return;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Inizio ad esportare le distinte. Righe da esportare: " + righeDaEsportare.size());
        }
        StreamFactory factory = cosaroSettings.getStreamTemplate("DISTINTETEMPLATE.xml");
        if (factory == null) {
            throw new IllegalArgumentException(
                    "Errore nel cercare il template DISTINTA+EXPORT.XML di cosaro per l'esportazione");
        }
        String filePath = cosaroSettings.caricaFilePathBilanceExport();
        if (filePath == null) {
            // throw new RuntimeException("Errore nel cercare il file degli ordini di cosaro per
            // l'esportazione");
            LOGGER.debug("--> chiave nelle preference cosaroFileExport non trovato.");
            sessionContext.setRollbackOnly();
            return;
        }

        LOGGER.debug("--> esporto i dati delle bilance nel file " + filePath);

        creaFileSemaforo(filePath.replace("txt", "sem"));

        BeanWriter out = null;
        try {
            out = factory.createWriter("distinta", new File(filePath));
            Map<String, Articolo> articoliCaricati = new HashMap<String, Articolo>();
            for (RigaDistintaCarico rigaDistintaCarico : righeDaEsportare) {
                if (!articoliCaricati.containsKey(rigaDistintaCarico.getArticolo().getCodice())) {
                    Articolo articolo = articoloManager
                            .caricaArticolo(rigaDistintaCarico.getArticolo().creaProxyArticolo(), false);
                    articolo.getAttributiArticolo().size();
                    articoliCaricati.put(articolo.getCodice(), articolo);
                }
                // Durante l'importazione avvaloro solamente l'attributo o pezzi o colli.
                // Questo va sulla qta della riga da passare alla bilancia
                Double qta = trasformaQtaDaAttributo(rigaDistintaCarico.getRigaArticolo(), "pezzi");
                if (qta != null && qta > 0) {
                    RigaOrdineBilance rigaOrdineBilance = new RigaOrdineBilance(rigaDistintaCarico,
                            articoliCaricati.get(rigaDistintaCarico.getArticolo().getCodice()), "PZ");
                    rigaOrdineBilance.setQtaDaEvadere(qta);
                    out.write(rigaOrdineBilance);
                }

                qta = trasformaQtaDaAttributo(rigaDistintaCarico.getRigaArticolo(), "colli");
                if (qta != null && qta > 0) {
                    RigaOrdineBilance rigaOrdineBilance = new RigaOrdineBilance(rigaDistintaCarico,
                            articoliCaricati.get(rigaDistintaCarico.getArticolo().getCodice()), "CT");
                    rigaOrdineBilance.setQtaDaEvadere(qta);
                    out.write(rigaOrdineBilance);
                }

            }
        } catch (Exception ex) {
            panjeaMessage.send("Errore nel generare il file per le bilance" + ex.getMessage(),
                    new String[] { "evasione" }, 8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            sessionContext.setRollbackOnly();
            return;
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        panjeaMessage.send("Spedite " + righeDaEsportare.size() + " righe alla bilancia", new String[] { "evasione" },
                8, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
        LOGGER.debug("--> Exit esporta per bilance");
    }

    /**
     *
     * @param rigaArticolo
     *            riga dell'ordine associata
     * @param codiceAttributo
     *            codiceAttributo contenente la qta
     * @return qta presente nell'attributo della riga
     */
    private Double trasformaQtaDaAttributo(RigaArticolo rigaArticolo, String codiceAttributo) {
        LOGGER.debug("--> Enter trasformaQtaDaAttributo");
        Double qta = 0.0;
        NumberFormat nb = NumberFormat.getInstance(Locale.ITALIAN);
        for (AttributoRiga attributoRiga : rigaArticolo.getAttributi()) {
            if (attributoRiga.getTipoAttributo().getCodice().equals(codiceAttributo)) {
                try {
                    qta = nb.parse(attributoRiga.getValore() == null ? "0,0" : attributoRiga.getValore()).doubleValue();
                } catch (ParseException nfe) {
                    LOGGER.error("-->errore nel recuperare la qta dall'attributo " + codiceAttributo, nfe);
                } catch (NullPointerException np) {
                    LOGGER.error("-->errore nel recuperare la qta dall'attributo " + codiceAttributo, np);
                }
                break;
            }
        }
        LOGGER.debug("--> Exit trasformaQtaDaAttributo con qta " + qta + " per l'attributo " + codiceAttributo);
        return qta;
    }

    /**
     *
     * @param righeDaEsportare
     *            distite da validare
     * @return true se tutte le righe hanno gli attributi pezzi e
     */
    private boolean validaAttributi(FilterList<RigaDistintaCarico> righeDaEsportare) {
        boolean valid = true;
        for (RigaDistintaCarico rigaDistintaCarico : righeDaEsportare) {
            valid = valid && validaAttributo(rigaDistintaCarico.getRigaArticolo(), "pezzi")
                    && validaAttributo(rigaDistintaCarico.getRigaArticolo(), "colli");
        }
        return valid;
    }

    /**
     *
     * @param rigaArticolo
     *            rigaArticolo da validare
     * @param codiceAttributo
     *            attributo da validare
     * @return true se trovo l'attributo
     */
    private boolean validaAttributo(RigaArticolo rigaArticolo, String codiceAttributo) {
        LOGGER.debug("--> Enter validaAttributo");
        for (AttributoRiga attributoRiga : rigaArticolo.getAttributi()) {
            if (attributoRiga.getTipoAttributo().getCodice().equals(codiceAttributo)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(
                            "--> trovato attributo " + codiceAttributo + " per la riga rigaArticolo " + rigaArticolo);
                }
                return true;
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Non ho trovato l'attributo attributo " + codiceAttributo + " per la riga rigaArticolo "
                    + rigaArticolo);
        }
        panjeaMessage.send("Non ho trovato l'attributo " + codiceAttributo + " per l'articolo "
                + rigaArticolo.getArticolo().getCodice(), PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
        return false;
    }

}
