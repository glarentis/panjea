package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.updateultimocosto.DialogAggiornamento;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;

public class AggiornaListinoCommand extends ApplicationWindowAwareCommand {

    private static final String COMMAND_ID = "aggiornaListinoCommand";
    public static final String PARAMETER_RIGHE_MAGAZZINO = "parameter_righe_magazzino";
    public static final String PARAMETER_RIGHE_MAGAZZINO_DTO = "parameter_righe_magazzino_dto";

    public static final String PARAMETER_AREA_MAGAZZINO = "parameter_area_magazzino";

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    /**
     * Costruttore di default.
     */
    public AggiornaListinoCommand() {
        super(COMMAND_ID);
        this.setSecurityControllerId(COMMAND_ID);
        this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
        RcpSupport.configure(this);
    }

    @Override
    public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
            CommandButtonConfigurer buttonConfigurer) {
        AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
        button.setFocusable(false);
        return button;
    }

    @Override
    protected void doExecuteCommand() {
        @SuppressWarnings("unchecked")
        List<RigaMagazzino> righeMagazzino = (List<RigaMagazzino>) getParameter(PARAMETER_RIGHE_MAGAZZINO,
                new ArrayList<RigaMagazzino>());
        @SuppressWarnings("unchecked")
        List<RigaMagazzinoDTO> righeMagazzinoDto = (List<RigaMagazzinoDTO>) getParameter(PARAMETER_RIGHE_MAGAZZINO_DTO,
                new ArrayList<RigaMagazzinoDTO>());

        if (righeMagazzino.isEmpty() && righeMagazzinoDto.isEmpty()) {
            return;
        }

        Map<ArticoloLite, Importo> importiArticoli = new HashMap<ArticoloLite, Importo>();
        for (RigaMagazzino rigaMagazzino : righeMagazzino) {
            // NPE Mail: se ho una riga nuova, o dove ho pulito l'articolo nella searchtext,
            // l'articolo è null, non devo
            // aggiungerlo
            if (rigaMagazzino instanceof RigaArticolo && ((RigaArticolo) rigaMagazzino).getArticolo() != null) {
                importiArticoli.put(((RigaArticolo) rigaMagazzino).getArticolo(),
                        ((RigaArticolo) rigaMagazzino).getPrezzoNetto());
            }
        }
        for (RigaMagazzinoDTO rigaMagazzinoDTO : righeMagazzinoDto) {
            if (rigaMagazzinoDTO instanceof RigaArticoloDTO
                    && ((RigaArticoloDTO) rigaMagazzinoDTO).getArticolo() != null) {
                importiArticoli.put(((RigaArticoloDTO) rigaMagazzinoDTO).getArticolo(),
                        ((RigaArticoloDTO) rigaMagazzinoDTO).getPrezzoNetto());
            }
        }

        AreaMagazzino areaMagazzino = (AreaMagazzino) getParameter(PARAMETER_AREA_MAGAZZINO);
        Date dataRegistrazione = areaMagazzino.getDataRegistrazione();
        List<ArticoloLite> articoli = new ArrayList<ArticoloLite>(importiArticoli.keySet());
        Listino listino = areaMagazzino.getListino();

        // mi interessa presentare il dialogo se ho degli articoli altrimenti è inutile
        if (articoli.size() > 0) {
            List<RigaListino> righe = magazzinoAnagraficaBD.caricaRigheListinoDaAggiornare(dataRegistrazione, articoli);
            DialogAggiornamento dialogAggiornamento = new DialogAggiornamento(righe, listino, magazzinoAnagraficaBD,
                    importiArticoli);
            dialogAggiornamento.setModal(true);
            dialogAggiornamento.setCloseAction(CloseAction.DISPOSE);
            dialogAggiornamento.showDialog();
        }
    }
}
