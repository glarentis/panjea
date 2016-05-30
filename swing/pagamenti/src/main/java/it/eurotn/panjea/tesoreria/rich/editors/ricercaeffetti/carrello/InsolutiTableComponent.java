package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.carrello;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.rich.commands.RiemettiRataCommand;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.forms.ricercaeffetti.ParametriCreazioneInsolutiForm;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneInsoluti;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.util.PanjeaEJBUtil;

public class InsolutiTableComponent extends AccreditiTableComponent {

    private ParametriCreazioneInsolutiForm parametriCreazioneInsolutiForm;
    private AreaInsoluti areaInsoluti = null;

    @Override
    public void addSituazioneEffetti(List<SituazioneEffetto> situazioneEffetti, ITesoreriaBD tesoreriaBD) {

        // controllo che sia presente 1 solo rapporto bancario altrimenti non
        // inserisco la situazione effetto
        RapportoBancarioAzienda rapportoBancarioCarrello = null;

        if (!tableWidget.getRows().isEmpty()) {
            rapportoBancarioCarrello = tableWidget.getRows().get(0).getRapportoBancario();
        }

        for (SituazioneEffetto situazioneEffetto : situazioneEffetti) {
            if (rapportoBancarioCarrello == null
                    || rapportoBancarioCarrello.equals(situazioneEffetto.getRapportoBancario())) {
                tableWidget.replaceOrAddRowObject(situazioneEffetto, situazioneEffetto, null);

                if (rapportoBancarioCarrello == null) {
                    rapportoBancarioCarrello = situazioneEffetto.getRapportoBancario();
                }
            }
        }
        aggiornaSpeseInsoluto();
    }

    /**
     * Aggiorna le spese di insoluto utilizzando il rapporto bancario del primo effetto nel carrello.
     */
    private void aggiornaSpeseInsoluto() {

        if (tableWidget.getRows().isEmpty()) {
            parametriCreazioneInsolutiForm.getValueModel("speseInsoluto").setValue(BigDecimal.ZERO);
            return;
        }

        RapportoBancarioAzienda rapportoBancarioCarrello = tableWidget.getRows().get(0).getRapportoBancario();
        if (rapportoBancarioCarrello.getSpeseInsoluto() != null) {
            BigDecimal speseInsoluto = rapportoBancarioCarrello.getSpeseInsoluto();
            parametriCreazioneInsolutiForm.getValueModel("speseInsoluto")
                    .setValue(speseInsoluto.multiply(new BigDecimal(tableWidget.getRows().size())));
        }
    }

    @Override
    protected JComponent createControl() {

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        parametriCreazioneInsolutiForm = new ParametriCreazioneInsolutiForm();

        rootPanel.add(parametriCreazioneInsolutiForm.getControl(), BorderLayout.NORTH);
        rootPanel.add(super.createControl(), BorderLayout.CENTER);

        return rootPanel;
    }

    @Override
    boolean generaAreeTesoreria(ITesoreriaBD tesoreriaBD) {

        if (parametriCreazioneInsolutiForm.getFormModel().getHasErrors() || tableWidget.getRows().isEmpty()) {
            return false;
        }

        Set<Effetto> effetti = new TreeSet<Effetto>();

        for (SituazioneEffetto situazioneEffetto : tableWidget.getRows()) {
            effetti.add(situazioneEffetto.getEffetto());
        }

        ParametriCreazioneInsoluti parametri = (ParametriCreazioneInsoluti) parametriCreazioneInsolutiForm
                .getFormObject();

        areaInsoluti = tesoreriaBD.creaAreaInsoluti(parametri.getDataDocumento(), parametri.getNumeroDocumento(),
                parametri.getSpeseInsoluto(), effetti);

        if (areaInsoluti != null) {
            riemettiRate(areaInsoluti);

            LifecycleApplicationEvent event = new OpenEditorEvent(
                    ParametriRicercaAreeTesoreria.creaParametriRicercaAreeTesoreria(areaInsoluti));
            Application.instance().getApplicationContext().publishEvent(event);
        }
        return true;
    }

    @Override
    void removeAll() {
        super.removeAll();

        ITesoreriaBD tesoreriaBD = RcpSupport.getBean("tesoreriaBD");

        boolean numeroDocumentoRichiesto = true;
        try {
            TipoDocumentoBasePartite tipoDocumentoBase = tesoreriaBD
                    .caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione.INSOLUTO);

            String registro = tipoDocumentoBase.getTipoAreaPartita().getTipoDocumento().getRegistroProtocollo();
            if (registro != null && !registro.isEmpty()) {
                numeroDocumentoRichiesto = false;
            }
        } catch (TipoDocumentoBaseException e) {
            // non esiste il tipo documento base quindi lascio il numero documento come non richiesto
            numeroDocumentoRichiesto = true;
        }
        parametriCreazioneInsolutiForm.getFormModel().getValueModel("numeroDocumentoRichiesto")
                .setValue(numeroDocumentoRichiesto);

        aggiornaSpeseInsoluto();
    }

    @Override
    void removeSelected() {
        super.removeSelected();
        aggiornaSpeseInsoluto();
    }

    /**
     * @param areaInsolutiParam
     *            areaInsoluti
     */
    private void riemettiRate(AreaInsoluti areaInsolutiParam) {
        List<RataRiemessa> rateRiemesse = new ArrayList<>();

        for (Effetto effetto : areaInsolutiParam.getEffetti()) {
            for (Rata rata : effetto.getRate()) {
                RataRiemessa rataRiemessa = new RataRiemessa(rata);
                EntitaLite el = new EntitaLite();
                PanjeaEJBUtil.copyProperties(el, rata.getAreaRate().getDocumento().getEntita());
                rataRiemessa.getAreaRate().getDocumento().setEntita(el);
                rateRiemesse.add(rataRiemessa);
            }
        }
        RiemettiRataCommand riemettiRataCommand = new RiemettiRataCommand();
        riemettiRataCommand.addParameter(RiemettiRataCommand.PARAM_RATE, rateRiemesse);
        riemettiRataCommand.execute();
    }
}
