package it.eurotn.panjea.vending.rich.editors.lettureselezionatrice;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.GuiStandardUtils;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.vending.manager.lettureselezionatrice.RisultatiChiusuraLettureDTO;
import it.eurotn.rich.editors.PanjeaTitledApplicationDialog;

public class RisultatiLettureDialog extends PanjeaTitledApplicationDialog {

    private JLabel labelTotaleLetture = new JLabel("");

    private JLabel labelTotaleAssociati = new JLabel("");
    private JLabel labelTotaleNonAssociati = new JLabel("");

    private JLabel labelTotaleCreati = new JLabel("");
    private JLabel labelTotaleNonCreati = new JLabel("");

    private JLabel labelTotaleTrafCasse = new JLabel("");

    private JLabel labelTotaleNonValide = new JLabel("");

    /**
     * Costruttore.
     */
    public RisultatiLettureDialog() {
        super("Risultati chiusura letture.", null);
    }

    @Override
    protected JComponent createTitledDialogContentPane() {

        FormLayout layout = new FormLayout("right:pref,4dlu,left:150dlu",
                "2dlu,pref,10dlu,pref,2dlu,pref,2dlu,pref,10dlu,pref,2dlu,pref,2dlu,pref,10dlu,pref,2dlu,pref,10dlu,pref,2dlu,pref");
        PanelBuilder builder = new PanelBuilder(layout);

        CellConstraints cc = new CellConstraints();

        builder.add(labelTotaleLetture, cc.xyw(1, 2, 3));

        builder.addSeparator("Rifornimenti da associare", cc.xyw(1, 4, 3));
        builder.add(new JLabel("Associati:"), cc.xy(1, 6));
        builder.add(labelTotaleAssociati, cc.xy(3, 6));
        labelTotaleAssociati.setForeground(new Color(0, 153, 76));
        builder.add(new JLabel("Non associati:"), cc.xy(1, 8));
        builder.add(labelTotaleNonAssociati, cc.xy(3, 8));
        labelTotaleNonAssociati.setForeground(Color.RED);

        builder.addSeparator("Rifornimenti da creare", cc.xyw(1, 10, 3));
        builder.add(new JLabel("Creati:"), cc.xy(1, 12));
        builder.add(labelTotaleCreati, cc.xy(3, 12));
        labelTotaleCreati.setForeground(new Color(0, 153, 76));
        builder.add(new JLabel("Non creati:"), cc.xy(1, 14));
        builder.add(labelTotaleNonCreati, cc.xy(3, 14));
        labelTotaleNonCreati.setForeground(Color.RED);

        builder.addSeparator("Trasferimenti cassa", cc.xyw(1, 16, 3));
        builder.add(new JLabel("Creati:"), cc.xy(1, 18));
        builder.add(labelTotaleTrafCasse, cc.xy(3, 18));
        labelTotaleTrafCasse.setForeground(new Color(0, 153, 76));

        builder.addSeparator("Letture non valide", cc.xyw(1, 20, 3));
        builder.add(new JLabel("Non valide:"), cc.xy(1, 22));
        builder.add(labelTotaleNonValide, cc.xy(3, 22));
        labelTotaleNonValide.setForeground(Color.RED);

        JPanel rooPanel = getComponentFactory().createPanel(new BorderLayout());
        rooPanel.add(GuiStandardUtils.attachBorder(builder.getPanel()), BorderLayout.CENTER);
        return rooPanel;
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[] { getFinishCommand() };
    }

    @Override
    protected boolean isMessagePaneVisible() {
        return false;
    }

    @Override
    protected boolean onFinish() {
        return true;
    }

    /**
     * @param risultatiLetture
     *            the risultatiLetture to set
     */
    public void setRisultatiLetture(RisultatiChiusuraLettureDTO risultatiLetture) {
        labelTotaleLetture
                .setText("<html><b>Totale letture da chiudere: " + risultatiLetture.getTotaleLetture() + "</b></html>");
        labelTotaleAssociati
                .setText("<html><b>" + risultatiLetture.getLettureRifornimentoAssociato().size() + "</b></html>");
        labelTotaleNonAssociati
                .setText("<html><b>" + risultatiLetture.getLettureRifornimentoNonAssociato().size() + "</b></html>");
        labelTotaleCreati.setText("<html><b>" + risultatiLetture.getLettureRifornimentoCreato().size() + "</b></html>");
        labelTotaleNonCreati
                .setText("<html><b>" + risultatiLetture.getLettureRifornimentoNonCreato().size() + "</b></html>");
        labelTotaleTrafCasse.setText("<html><b>" + risultatiLetture.getLettureCassaGenerate().size() + "</b></html>");
        labelTotaleNonValide.setText("<html><b>" + risultatiLetture.getLettureNonValide().size() + "</b></html>");
    }

}