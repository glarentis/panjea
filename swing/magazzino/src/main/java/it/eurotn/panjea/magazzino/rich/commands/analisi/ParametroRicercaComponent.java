package it.eurotn.panjea.magazzino.rich.commands.analisi;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.ParametriRicercaDTO;
import it.eurotn.panjea.rich.bd.IParametriRicercaBD;
import it.eurotn.panjea.rich.bd.ParametriRicercaBD;
import it.eurotn.panjea.rich.components.CompoundIcon;

public class ParametroRicercaComponent extends JPanel {

    public class OpenParametriRicercaCommand extends ActionCommand {

        public static final String PARAM_PARAMETRO_NOME = "paramParametroNome";
        public static final String PARAM_ID_ARTICOLO = "paramIdArticolo";
        public static final String PARAM_ID_ENTITA = "paramIdEntita";

        private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
        private IAnagraficaBD anagraficaBD;

        /**
         * Costruttore.
         */
        public OpenParametriRicercaCommand() {
            super();
            magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
            anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
        }

        @Override
        protected void doExecuteCommand() {

            String nomeParametro = (String) getParameter(PARAM_PARAMETRO_NOME);
            AbstractParametriRicerca parametro = parametriBD.caricaParametro(paramClass, nomeParametro);

            Integer idArt = (Integer) getParameter(PARAM_ID_ARTICOLO, null);
            ArticoloLite articoloLite = null;
            if (idArt != null) {
                articoloLite = magazzinoAnagraficaBD.caricaArticoloLite(idArt);
            }

            Integer idEnt = (Integer) getParameter(PARAM_ID_ENTITA, null);
            EntitaLite entitaLite = null;
            if (idEnt != null) {
                entitaLite = new EntitaLite();
                entitaLite.setId(idEnt);
                entitaLite = anagraficaBD.caricaEntitaLite(entitaLite);
            }

            if (paramClass.equals(ParametriRicercaMovimentazione.class)) {
                ((ParametriRicercaMovimentazione) parametro).setArticoloLite(articoloLite);
                ((ParametriRicercaMovimentazione) parametro).setEntitaLite(entitaLite);
            } else {
                ((it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione) parametro)
                        .setArticoloLite(articoloLite);
                ((it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione) parametro)
                        .setEntitaLite(entitaLite);
            }

            parametro.setEffettuaRicerca(true);

            LifecycleApplicationEvent event = new OpenEditorEvent(parametro);
            Application.instance().getApplicationContext().publishEvent(event);

            onAnalisiOpenClosure.call(null);
        }

    }

    private static final long serialVersionUID = -6013147024311872345L;

    private String titolo;

    private List<ParametriRicercaDTO> parametriRicercaDTO;
    private Class<? extends AbstractParametriRicerca> paramClass;

    private Integer idArticolo;
    private Integer idEntita;

    private IParametriRicercaBD parametriBD;

    private Closure onAnalisiOpenClosure;

    private Icon icon;

    {
        parametriBD = RcpSupport.getBean(ParametriRicercaBD.BEAN_ID);
    }

    /**
     * Costruttore.
     *
     * @param titolo
     *            titolo
     * @param icon
     *            icona da visualizzare
     * @param parametriRicerca
     *            DTO dei parametri di ricerca
     * @param paramClass
     *            classe del parametro
     * @param idArticolo
     *            id articolo
     * @param idEntita
     *            id entita
     * @param onAnalisiOpenClosure
     *            onAnalisiOpenClosure
     */
    public ParametroRicercaComponent(final String titolo, final Icon icon,
            final List<ParametriRicercaDTO> parametriRicerca,
            final Class<? extends AbstractParametriRicerca> paramClass, final Integer idArticolo,
            final Integer idEntita, final Closure onAnalisiOpenClosure) {
        super();
        this.titolo = titolo;
        this.icon = icon;
        this.parametriRicercaDTO = parametriRicerca;
        this.paramClass = paramClass;
        this.idArticolo = idArticolo;
        this.idEntita = idEntita;
        this.onAnalisiOpenClosure = onAnalisiOpenClosure;

        setBorder(null);

        bindParametri();
    }

    private void bindParametri() {
        // FormLayout layout = new
        // FormLayout("100dlu,1dlu,fill:30dlu,1dlu,fill:30dlu,1dlu,fill:30dlu", "12dlu");
        //
        // PanelBuilder builder = new PanelBuilder(layout);
        // CellConstraints cc = new CellConstraints();
        //
        // builder.add(createAnalisiButton(), cc.xy(1, 1));
        //
        // if (idArticolo != null) {
        // builder.add(createArticoloButton(), cc.xy(3, 1));
        // }
        // if (idEntita != null) {
        // builder.add(createEntitaButton(), cc.xy(5, 1));
        // }
        // if (idArticolo != null && idEntita != null) {
        // builder.add(createArticoloEntitaButton(), cc.xy(7, 1));
        // }
        //
        // JPanel panel = builder.getPanel();
        // panel.setBorder(null);
        // add(panel);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        JLabel titoloLabel = new JLabel(titolo.toUpperCase());
        Font font = titoloLabel.getFont();
        titoloLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD, new Float(font.getSize() - 3)));
        titoloLabel.setBackground(Color.LIGHT_GRAY);
        titoloLabel.setOpaque(true);
        add(titoloLabel, c);

        int row = 1;
        for (ParametriRicercaDTO parametro : parametriRicercaDTO) {
            c.gridy = row;
            c.ipadx = 20;
            c.gridwidth = 1;
            c.weightx = 1;

            c.gridx = 0;
            add(createAnalisiButton(parametro), c);

            c.weightx = 0;
            if (idArticolo != null) {
                c.gridx = 1;
                add(createArticoloButton(parametro), c);
            }

            if (idEntita != null) {
                c.gridx = 2;
                add(createEntitaButton(parametro), c);
            }

            if (idArticolo != null && idEntita != null) {
                c.gridx = 3;
                c.ipadx = 10;
                add(createArticoloEntitaButton(parametro), c);
            }
            row++;
        }
    }

    private JComponent createAnalisiButton(final ParametriRicercaDTO parametro) {
        OpenParametriRicercaCommand command = new OpenParametriRicercaCommand();
        command.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
            @Override
            public boolean preExecution(ActionCommand command) {
                command.addParameter(OpenParametriRicercaCommand.PARAM_PARAMETRO_NOME, parametro.getNome());
                return super.preExecution(command);
            }
        });
        AbstractButton button = command.createButton();
        button.setText(parametro.getNome());
        button.setIcon(icon);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        return button;
    }

    private JComponent createArticoloButton(final ParametriRicercaDTO parametro) {
        OpenParametriRicercaCommand command = new OpenParametriRicercaCommand();
        command.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
            @Override
            public boolean preExecution(ActionCommand command) {
                command.addParameter(OpenParametriRicercaCommand.PARAM_PARAMETRO_NOME, parametro.getNome());
                command.addParameter(OpenParametriRicercaCommand.PARAM_ID_ARTICOLO, idArticolo);
                return super.preExecution(command);
            }
        });
        AbstractButton button = command.createButton();
        button.setIcon(RcpSupport.getIcon(Articolo.class.getName()));

        return button;
    }

    private JComponent createArticoloEntitaButton(final ParametriRicercaDTO parametro) {
        OpenParametriRicercaCommand command = new OpenParametriRicercaCommand();
        command.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
            @Override
            public boolean preExecution(ActionCommand command) {
                command.addParameter(OpenParametriRicercaCommand.PARAM_PARAMETRO_NOME, parametro.getNome());
                command.addParameter(OpenParametriRicercaCommand.PARAM_ID_ARTICOLO, idArticolo);
                command.addParameter(OpenParametriRicercaCommand.PARAM_ID_ENTITA, idEntita);
                return super.preExecution(command);
            }
        });
        AbstractButton button = command.createButton();
        button.setIcon(new CompoundIcon(RcpSupport.getIcon(Articolo.class.getName()), RcpSupport.getIcon("entita")));

        return button;
    }

    private JComponent createEntitaButton(final ParametriRicercaDTO parametro) {
        OpenParametriRicercaCommand command = new OpenParametriRicercaCommand();
        command.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
            @Override
            public boolean preExecution(ActionCommand command) {
                command.addParameter(OpenParametriRicercaCommand.PARAM_PARAMETRO_NOME, parametro.getNome());
                command.addParameter(OpenParametriRicercaCommand.PARAM_ID_ENTITA, idEntita);
                return super.preExecution(command);
            }
        });
        AbstractButton button = command.createButton();
        button.setIcon(RcpSupport.getIcon("entita"));

        return button;
    }

}