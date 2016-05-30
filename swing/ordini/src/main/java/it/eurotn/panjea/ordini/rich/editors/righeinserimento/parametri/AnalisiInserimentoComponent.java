package it.eurotn.panjea.ordini.rich.editors.righeinserimento.parametri;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento.TipoRicerca;
import it.eurotn.panjea.parametriricerca.domain.ParametriRicercaDTO;
import it.eurotn.panjea.rich.bd.IParametriRicercaBD;
import it.eurotn.panjea.rich.bd.ParametriRicercaBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import net.atlanticbb.tantlinger.ui.DefaultAction;

public class AnalisiInserimentoComponent extends AbstractInserimentoComponent {

    private class ParametriRicercaAnalisiComponent extends JPanel {

        private static final long serialVersionUID = -6525483593989923845L;

        private ParametriRicercaDTO parametriRicercaDTO;
        private ParametriRicercaMovimentazione parametroRicercaOrdini;

        /**
         * Costruttore.
         */
        public ParametriRicercaAnalisiComponent(final ParametriRicercaDTO parametriRicercaDTO) {
            super(new BorderLayout(5, 2));
            this.parametriRicercaDTO = parametriRicercaDTO;
            parametroRicercaOrdini = (ParametriRicercaMovimentazione) parametriBD
                    .caricaParametro(ParametriRicercaMovimentazione.class, this.parametriRicercaDTO.getNome());

            initControl();
        }

        private void initControl() {
            JButton analisiButton = new JButton();
            analisiButton.setAction(new DefaultAction() {
                private static final long serialVersionUID = -8370755527458765833L;

                @Override
                protected void execute(ActionEvent evt) throws Exception {
                    ParametriRigheOrdineInserimento parametri = new ParametriRigheOrdineInserimento();
                    parametri.setParametriRicercaMovimentazione(
                            (ParametriRicercaMovimentazione) PanjeaSwingUtil.cloneObject(parametroRicercaOrdini));
                    parametri.setTipoRicerca(TipoRicerca.ANALISI);
                    fireParametriCreated(parametri);
                }
            });
            analisiButton.setText(parametriRicercaDTO.getNome());
            analisiButton.setIcon(RcpSupport.getIcon("openMovimentazioneMagazzinoOrdiniCommand.icon"));
            add(analisiButton, BorderLayout.CENTER);

            JButton analisiEntitaButton = new JButton();
            analisiEntitaButton.setAction(new DefaultAction() {
                private static final long serialVersionUID = -1754857389973158315L;

                @Override
                protected void execute(ActionEvent evt) throws Exception {
                    ParametriRigheOrdineInserimento parametri = new ParametriRigheOrdineInserimento();
                    parametri.setParametriRicercaMovimentazione(
                            (ParametriRicercaMovimentazione) PanjeaSwingUtil.cloneObject(parametroRicercaOrdini));
                    parametri.getParametriRicercaMovimentazione()
                            .setEntitaLite(areaOrdineCorrente.getDocumento().getEntita());
                    parametri.setTipoRicerca(TipoRicerca.ANALISI);
                    fireParametriCreated(parametri);
                }
            });
            analisiEntitaButton.setIcon(RcpSupport.getIcon(EntitaLite.class.getName()));
            analisiEntitaButton.setPreferredSize(new Dimension(40, 40));
            add(analisiEntitaButton, BorderLayout.EAST);
        }

    }

    private static final long serialVersionUID = 6758231607866473353L;

    private AreaOrdine areaOrdineCorrente;

    private IParametriRicercaBD parametriBD;

    {
        parametriBD = RcpSupport.getBean(ParametriRicercaBD.BEAN_ID);
    }

    /**
     * Costruttore.
     */
    public AnalisiInserimentoComponent() {
        super();
        setLayout(new VerticalLayout());
    }

    @Override
    public String getTitle() {
        return "Analisi";
    }

    @Override
    public void updateControl(AreaOrdine areaOrdine) {
        this.areaOrdineCorrente = areaOrdine;

        removeAll();
        List<ParametriRicercaDTO> parametriOrdini = parametriBD.caricaParametri(ParametriRicercaMovimentazione.class);
        if (!parametriOrdini.isEmpty()) {
            for (ParametriRicercaDTO parametriRicercaDTO : parametriOrdini) {
                add(new ParametriRicercaAnalisiComponent(parametriRicercaDTO));
            }
            setBorder(BorderFactory.createTitledBorder(getTitle()));
        } else {
            setBorder(BorderFactory.createEmptyBorder());
        }

    }

}
