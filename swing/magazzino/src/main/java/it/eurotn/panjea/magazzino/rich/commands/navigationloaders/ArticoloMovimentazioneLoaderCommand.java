package it.eurotn.panjea.magazzino.rich.commands.navigationloaders;

import org.springframework.core.ReflectiveVisitorHelper;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.rich.factory.navigationloader.OpenEditorLoaderActionCommand;

/**
 * @author fattazzo
 *
 */
public class ArticoloMovimentazioneLoaderCommand
        extends OpenEditorLoaderActionCommand<ParametriRicercaMovimentazioneArticolo> {

    private ReflectiveVisitorHelper visitorHelper = new ReflectiveVisitorHelper();
    private ArticoloLoaderObjectVisitor articoloVisitor = new ArticoloLoaderObjectVisitor();

    private IAnagraficaBD anagraficaBD;

    /**
     * Costruttore.
     *
     * @param commandId
     */
    public ArticoloMovimentazioneLoaderCommand() {
        super("articoloMovimentazioneLoaderCommand");
    }

    /**
     * Costruttore.
     *
     * @param commandId
     *            id
     */
    public ArticoloMovimentazioneLoaderCommand(final String commandId) {
        super(commandId);
    }

    @Override
    protected ParametriRicercaMovimentazioneArticolo getObjectForOpenEditor(Object loaderObject) {
        // siccome il command è mappato sia su articolo sia su articolo lite perchè deve fare la stessa cosa,
        // controllo cosa mi arriva dai parametri
        Articolo articolo = (Articolo) visitorHelper.invokeVisit(articoloVisitor, loaderObject);

        ParametriRicercaMovimentazioneArticolo parametri = new ParametriRicercaMovimentazioneArticolo();
        parametri.setArticoloLite(articolo.getArticoloLite());
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.MESE_CORRENTE);
        parametri.setPeriodo(periodo);

        // carico di default la movimentazione sul deposito principale
        Deposito depositoPrincipale = anagraficaBD.caricaDepositoPrincipale();
        parametri.setDepositoLite(depositoPrincipale.creaLite());

        parametri.setEffettuaRicerca(true);

        return parametri;
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[] { Articolo.class, ArticoloLite.class };
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

}
