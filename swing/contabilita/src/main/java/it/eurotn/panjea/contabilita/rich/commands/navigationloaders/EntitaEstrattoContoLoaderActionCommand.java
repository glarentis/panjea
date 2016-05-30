package it.eurotn.panjea.contabilita.rich.commands.navigationloaders;

import java.util.Calendar;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.rich.commands.navigationloaders.EntitaLoaderActionCommand;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

/**
 * @author fattazzo
 *
 */
public class EntitaEstrattoContoLoaderActionCommand extends EntitaLoaderActionCommand {

    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    /**
     * Costruttore.
     */
    public EntitaEstrattoContoLoaderActionCommand() {
        super("entitaEstrattoContoLoaderActionCommand");
    }

    @Override
    protected OpenEditorEvent getOpenEditorEvent() {
        Entita entita = getEntita();
        if (entita == null || Conto.getSottoTipoConto(entita) == null) {
            return null;
        }

        SottotipoConto sottotipoConto = Conto.getSottoTipoConto(entita);
        SottoConto caricaSottoContoPerEntita = contabilitaAnagraficaBD.caricaSottoContoPerEntita(sottotipoConto,
                entita.getCodice());

        ParametriRicercaEstrattoConto parametriRicerca = new ParametriRicercaEstrattoConto();
        parametriRicerca.setAnnoCompetenza(Calendar.getInstance().get(Calendar.YEAR));
        parametriRicerca.getDataRegistrazione().setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
        parametriRicerca.setSottoConto(caricaSottoContoPerEntita);
        parametriRicerca.setEffettuaRicerca(true);

        return new OpenEditorEvent(parametriRicerca);
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[] { Cliente.class, ClienteLite.class, Fornitore.class, FornitoreLite.class,
                EntitaDocumento.class };
    }

    /**
     * @param contabilitaAnagraficaBD
     *            the contabilitaAnagraficaBD to set
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }
}
