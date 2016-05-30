package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.GroupingList;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.exception.CodicePagamentoAssenteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoEvasioneAssenteException;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.evasione.RigaComparator;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception.CodicePagamentoAssenteDialog;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception.CodicePagamentoEvasioneAssenteDialog;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception.EntitaSenzaTipoDocumentoEvasioneDialog;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class EvadiCarrelloCommand extends ApplicationWindowAwareCommand {

    private static final String COMMAND_ID = "evadiCarrelloEvasioneCommand";

    public static final String PARAM_RIGHE = "paramRighe";
    public static final String PARAM_DATA_EVASIONE = "dataEvasione";
    public static final String PARAM_DISTINTE_CARICO_PAGE = "distinteCaricoPage";

    private static Logger logger = Logger.getLogger(EvadiCarrelloCommand.class);
    private IOrdiniDocumentoBD ordiniDocumentoBD = null;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private boolean evasioneEffettuata;

    /**
     * Costruttore.
     */
    EvadiCarrelloCommand() {
        super(COMMAND_ID);
        setSecurityControllerId("evasioneController");
        RcpSupport.configure(this);

        this.ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    /**
     *
     * @param righeDaEvadere
     *            righe selezionate per l'evasione
     * @return lista di areeOrdine raggruppate sulle righe
     */
    private List<AreaOrdinePM> creaOrdiniDaEvadere(List<RigaDistintaCarico> righeDaEvadere) {
        GroupingList<RigaDistintaCarico> ordiniDaEvadere = new GroupingList<RigaDistintaCarico>(
                GlazedLists.eventList(righeDaEvadere), new RigaComparator());
        List<AreaOrdinePM> ordini = new ArrayList<AreaOrdinePM>();
        for (List<RigaDistintaCarico> righeOrdine : ordiniDaEvadere) {
            AreaOrdinePM areaOrdinePM = new AreaOrdinePM(righeOrdine);
            ordini.add(areaOrdinePM);
        }
        return ordini;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doExecuteCommand() {

        Date dataEvasione = (Date) getParameter(PARAM_DATA_EVASIONE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataEvasione);
        calendar.set(Calendar.MILLISECOND, 0);
        dataEvasione = calendar.getTime();

        evasioneEffettuata = false;
        try {
            List<RigaDistintaCarico> righeDaEvadere = (List<RigaDistintaCarico>) getParameter(PARAM_RIGHE);
            righeDaEvadere = ordiniDocumentoBD.aggiornaRigheCaricoConDatiEvasione(righeDaEvadere);

            List<AreaOrdinePM> ordiniDaEvadere = creaOrdiniDaEvadere(righeDaEvadere);

            AreaOrdineEvasioneDialog dialog = new AreaOrdineEvasioneDialog(ordiniDaEvadere);
            dialog.showDialog();
            ordiniDaEvadere = dialog.getRigheOrdinePM();
            if (ordiniDaEvadere != null) {

                righeDaEvadere = new ArrayList<RigaDistintaCarico>();
                for (AreaOrdinePM areaOrdinePM : ordiniDaEvadere) {
                    righeDaEvadere.addAll(areaOrdinePM.getRighe());
                }

                dialog = null;

                ordiniDocumentoBD.evadiOrdini(righeDaEvadere, dataEvasione);

                // se ci sono documenti che richiedono i dati accompagnatori apro il dialog per
                // compilarli
                openRichiestaDatiAccompagnatoriDialog(dataEvasione);

                // apro la ricerca aree magazzino con i documenti appena creati
                openAreeMagazzinoEditor(dataEvasione);
                evasioneEffettuata = true;
            }
        } catch (EntitaSenzaTipoDocumentoEvasioneException e) {
            PanjeaTitledPageApplicationDialog dialog = new EntitaSenzaTipoDocumentoEvasioneDialog(e);
            dialog.showDialog();
        } catch (CodicePagamentoEvasioneAssenteException e) {
            PanjeaTitledPageApplicationDialog dialog = new CodicePagamentoEvasioneAssenteDialog(e.getAreeOrdine());
            dialog.showDialog();
        } catch (CodicePagamentoAssenteException e) {
            PanjeaTitledPageApplicationDialog dialog = new CodicePagamentoAssenteDialog(e.getAreeOrdine());
            dialog.showDialog();
        } catch (ContabilizzazioneException e) {
            openAreeMagazzinoEditor(dataEvasione);
            LifecycleApplicationEvent event = new OpenEditorEvent(e);
            Application.instance().getApplicationContext().publishEvent(event);
        } catch (Exception e) {
            logger.error("-->errore nell'evasione documenti", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            DistinteCaricoTablePage distinteCaricoTablePage = (DistinteCaricoTablePage) getParameter(
                    PARAM_DISTINTE_CARICO_PAGE, null);
            if (distinteCaricoTablePage != null) {
                if (evasioneEffettuata) {
                    distinteCaricoTablePage.loadData();
                }
                distinteCaricoTablePage.showCarrello();
            }
        }
    }

    private ParametriRicercaAreaMagazzino getParametriRicercaPerEvasione(Date dataEvasione) {
        ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = new ParametriRicercaAreaMagazzino();

        parametriRicercaAreaMagazzino.setAnnoCompetenza(null);
        parametriRicercaAreaMagazzino.setDataGenerazione(dataEvasione);
        parametriRicercaAreaMagazzino.getTipiGenerazione().add(TipoGenerazione.EVASIONE);
        parametriRicercaAreaMagazzino.setEffettuaRicerca(true);

        return parametriRicercaAreaMagazzino;
    }

    /**
     *
     * @return true se l'evasione ha avuto esito positivo, false altrimenti
     */
    public boolean isEvasioneEfettuata() {
        return evasioneEffettuata;
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
        super.onButtonAttached(button);
        button.setName("EvadiCarrelloCommand");
    }

    /**
     * Visualizza l'editor della ricerca aree magazzino per visualizzare le aree create dalla
     * evasione.
     *
     * @param dataEvasione
     *            data dell'evasione
     */
    private void openAreeMagazzinoEditor(Date dataEvasione) {
        ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = getParametriRicercaPerEvasione(dataEvasione);

        LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaAreaMagazzino);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    private void openRichiestaDatiAccompagnatoriDialog(Date dataEvasione) {

        List<AreaMagazzinoRicerca> aree = magazzinoDocumentoBD
                .caricaAreeMagazzinoConRichiestaDatiAccompagnatori(dataEvasione);

        // se ho aree che richiedono i dati accompagnatori visualizzo il dialog
        if (!aree.isEmpty()) {
            AreeMagazzinoDatiAccompagnatoriDialog dialog = new AreeMagazzinoDatiAccompagnatoriDialog(aree);
            dialog.showDialog();
        }
    }
}