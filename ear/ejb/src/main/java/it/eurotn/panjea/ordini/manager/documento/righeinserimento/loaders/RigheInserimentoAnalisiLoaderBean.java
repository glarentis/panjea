package it.eurotn.panjea.ordini.manager.documento.righeinserimento.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.RigheInserimentoLoader;
import it.eurotn.panjea.ordini.manager.interfaces.OrdineMovimentazioneManager;
import it.eurotn.panjea.ordini.util.RigaMovimentazione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;

@Stateless(mappedName = "Panjea.RigheInserimentoAnalisiLoaderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheInserimentoAnalisiLoaderBean")
public class RigheInserimentoAnalisiLoaderBean implements RigheInserimentoLoader {

    @EJB
    private OrdineMovimentazioneManager ordineMovimentazioneManager;

    /**
     * Carica l'analisi delle righe ordine per un massimo di 15.000 righe.
     *
     * @param parametri
     *            parametri di ricerca movimentazione
     * @return righe movimentazione
     */
    private List<RigaMovimentazione> caricaAnalisi(ParametriRicercaMovimentazione parametri) {

        int sizeOfPage = 5000;
        List<RigaMovimentazione> righeMovimentazione = new ArrayList<RigaMovimentazione>();

        for (int page = 1; page <= 3; page++) {

            List<RigaMovimentazione> righePagina = ordineMovimentazioneManager.caricaMovimentazione(parametri, page,
                    sizeOfPage);
            righeMovimentazione.addAll(righePagina);

            if (sizeOfPage > righePagina.size()) {
                break;
            }
        }

        return righeMovimentazione;
    }

    @Override
    public List<RigaOrdineInserimento> caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri) {

        // carico l'analisi
        List<RigaMovimentazione> righeAnalisi = caricaAnalisi(parametri.getParametriRicercaMovimentazione());

        Map<Integer, RigaOrdineInserimento> articoliMap = new HashMap<Integer, RigaOrdineInserimento>();
        // Devo ciclare sull'analisi per creare le righe ordine inserimento raggruppate per articolo
        for (RigaMovimentazione rigaMovimentazione : righeAnalisi) {

            Integer idArticolo = rigaMovimentazione.getArticoloLite().getId();
            RigaOrdineInserimento rigaInserimento = ObjectUtils.defaultIfNull(articoliMap.get(idArticolo),
                    new RigaOrdineInserimento());
            rigaInserimento.setArticolo(rigaMovimentazione.getArticoloLite());
            rigaInserimento.setQta(rigaInserimento.getQta() + rigaMovimentazione.getQuantita());
            rigaInserimento.setNumeroDecimaliQuantita(rigaMovimentazione.getNumeroDecimaliQuantita());
            rigaInserimento.setIdRigheOrdine(StringUtils.isBlank(rigaInserimento.getIdRigheOrdine())
                    ? rigaMovimentazione.getRigaOrdineId().toString()
                    : rigaInserimento.getIdRigheOrdine() + "," + rigaMovimentazione.getRigaOrdineId());

            if (rigaInserimento.getDataIniziale() == null) {
                rigaInserimento.setDataIniziale(rigaMovimentazione.getDataRegistrazione());
            } else {
                rigaInserimento.setDataIniziale(
                        rigaMovimentazione.getDataRegistrazione().before(rigaInserimento.getDataIniziale())
                                ? rigaMovimentazione.getDataRegistrazione() : rigaInserimento.getDataIniziale());
            }

            if (rigaInserimento.getDataFinale() == null) {
                rigaInserimento.setDataFinale(rigaMovimentazione.getDataRegistrazione());
            } else {
                rigaInserimento
                        .setDataFinale(rigaInserimento.getDataFinale().before(rigaMovimentazione.getDataRegistrazione())
                                ? rigaMovimentazione.getDataRegistrazione() : rigaInserimento.getDataFinale());
            }

            articoliMap.put(idArticolo, rigaInserimento);
        }

        List<RigaOrdineInserimento> righe = new ArrayList<RigaOrdineInserimento>();
        righe.addAll(articoliMap.values());
        return righe;
    }

}
