package it.eurotn.panjea.intra.rich.editors.dichiarazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.TotaliDichiarazione;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.intra.rich.bd.IntraBD;

import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.form.binding.BindingFactory;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.StyledLabelBuilder;

public class RiassuntoComponent extends AbstractControlFactory {
	private class RicalcolaCommand extends ActionCommand {
		/**
		 * 
		 * Costruttore.
		 * 
		 */
		public RicalcolaCommand() {
			super("ricalcolaCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			calcolaTotali();
		}
	}

	private BindingFactory bf;
	private DichiarazioneIntra dichiarazioneIntra;
	private IIntraBD intraBD;
	private JLabel totaleRigheS1;
	private JLabel totaleRigheS2;
	private JLabel totaleRigheS3;
	private JLabel totaleRigheS4;
	private JLabel totaleImportoS1;
	private JLabel totaleImportoS2;
	private JLabel totaleImportoS3;
	private JLabel totaleImportoS4;
	private JLabel[][] totaliComponents;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param bf
	 *            .
	 */
	public RiassuntoComponent(final BindingFactory bf) {
		super();
		this.bf = bf;
		intraBD = RcpSupport.getBean(IntraBD.BEAN_ID);
	}

	/**
	 * calcola e visualizza i totali.
	 */
	private void calcolaTotali() {
		TotaliDichiarazione totali = intraBD.calcolaTotaliDichiarazione(dichiarazioneIntra);
		DecimalFormat rformat = new DecimalFormat("0");
		DecimalFormat iformat = new DecimalFormat("###,##0");
		totaliComponents[1][0].setText(rformat.format(totali.getNumRigheSezione1()));
		totaliComponents[1][1].setText(iformat.format(totali.getImportoSezione1()));
		totaliComponents[2][0].setText(rformat.format(totali.getNumRigheSezione2()));
		totaliComponents[2][1].setText(iformat.format(totali.getImportoSezione2()));
		totaliComponents[3][0].setText(rformat.format(totali.getNumRigheSezione3()));
		totaliComponents[3][1].setText(iformat.format(totali.getImportoSezione3()));
		totaliComponents[4][0].setText(rformat.format(totali.getNumRigheSezione4()));
		totaliComponents[4][1].setText(iformat.format(totali.getImportoSezione4()));

	}

	@Override
	protected JComponent createControl() {
		FormLayout layoutRiassunto = new FormLayout(
				"center:9dlu,center:35dlu,center:90dlu,center:150dlu,center:15dlu,center:9dlu,center:35dlu,center:90dlu,center:150dlu",
				"2dlu,default,6dlu,default,2dlu,75dlu,12dlu,default,2dlu,75dlu,2dlu");
		FormLayoutFormBuilder builderRiassunto = new FormLayoutFormBuilder(bf, layoutRiassunto, new FormLinePanel());
		builderRiassunto.setLabelAttributes("r, c");
		builderRiassunto.setRow(2);
		totaliComponents = new JLabel[5][2];
		totaleRigheS1 = createLabel("");
		totaliComponents[1][0] = totaleRigheS1;
		totaleRigheS2 = createLabel("");
		totaliComponents[2][0] = totaleRigheS2;
		totaleRigheS3 = createLabel("");
		totaliComponents[3][0] = totaleRigheS3;
		totaleRigheS4 = createLabel("");
		totaliComponents[4][0] = totaleRigheS4;
		totaleImportoS1 = createLabel("");
		totaliComponents[1][1] = totaleImportoS1;
		totaleImportoS2 = createLabel("");
		totaliComponents[2][1] = totaleImportoS2;
		totaleImportoS3 = createLabel("");
		totaliComponents[3][1] = totaleImportoS3;
		totaleImportoS4 = createLabel("");
		totaliComponents[4][1] = totaleImportoS4;

		JLabel titleRiassuntoComponent = createLabel("{DATI RIASSUNTIVI DELL’ELENCO:b}");
		builderRiassunto.addComponent(titleRiassuntoComponent, 2, 2, 3, 1);
		builderRiassunto.setLabelAttributes("l, c");
		builderRiassunto.addComponent(new RicalcolaCommand().createButton(), 1, 2);
		builderRiassunto.setLabelAttributes("r, c");
		builderRiassunto.nextRow();
		builderRiassunto.nextRow();
		builderRiassunto.addComponent(createLabel("{BENI:b}@rows:4:4:4"), 1, 4, 1, 3);
		builderRiassunto.addComponent(createLabel("SEZIONE"), 2, 4);
		builderRiassunto.addComponent(createLabel("TOTALE RIGHE DETTAGLIO"), 3, 4);
		builderRiassunto.addComponent(createLabel("AMMONTARE COMPL. IN EURO"), 4, 4);
		builderRiassunto.addComponent(createLabel("{BENI:b}@rows:4:4:4"), 6, 4, 1, 3);
		builderRiassunto.addComponent(createLabel("SEZIONE"), 7, 4);
		builderRiassunto.addComponent(createLabel("TOTALE RIGHE DETTAGLIO"), 8, 4);
		builderRiassunto.addComponent(createLabel("AMMONTARE COMPL. IN EURO"), 9, 4);

		builderRiassunto.nextRow();
		builderRiassunto.addComponent(createLabel("1"), 2, 6);
		builderRiassunto.addComponent(totaleRigheS1, 3, 6);
		builderRiassunto.addComponent(totaleImportoS1, 4, 6);
		builderRiassunto.addComponent(createLabel("2"), 7, 6);
		builderRiassunto.addComponent(totaleRigheS2, 8, 6);
		builderRiassunto.addComponent(totaleImportoS2, 9, 6);

		builderRiassunto.nextRow();
		// SERVIZI
		builderRiassunto.addComponent(createLabel("{SERVIZI:b}@rows:7:7:7"), 1, 10, 1, 1);
		builderRiassunto.addComponent(createLabel("SEZIONE"), 2, 8);
		builderRiassunto.addComponent(createLabel("TOTALE RIGHE DETTAGLIO"), 3, 8);
		builderRiassunto.addComponent(createLabel("AMMONTARE COMPL. IN EURO"), 4, 8);
		builderRiassunto.addComponent(createLabel("{SERVIZI:b}@rows:7:7:7"), 6, 10, 1, 1);
		builderRiassunto.addComponent(createLabel("SEZIONE"), 7, 8);
		builderRiassunto.addComponent(createLabel("TOTALE RIGHE DETTAGLIO"), 8, 8);
		builderRiassunto.addComponent(createLabel("AMMONTARE COMPL. IN EURO"), 9, 8);

		builderRiassunto.nextRow();
		builderRiassunto.addComponent(createLabel("3"), 2, 10);
		builderRiassunto.addComponent(totaleRigheS3, 3, 10);
		builderRiassunto.addComponent(totaleImportoS3, 4, 10);
		builderRiassunto.addComponent(createLabel("4"), 7, 10);
		builderRiassunto.addComponent(totaleRigheS4, 8, 10);
		builderRiassunto.addComponent(totaleImportoS4, 9, 10);
		return builderRiassunto.getPanel();
	}

	/**
	 * 
	 * @param labelString
	 *            stringa della label e con i parametri dello stile
	 * @return label Styled con border line
	 */
	private JLabel createLabel(String labelString) {
		JLabel result = StyledLabelBuilder.createStyledLabel(labelString);
		return result;
	}

	/**
	 * @param dichiarazioneIntra
	 *            The dichiarazioneIntra to set.
	 */
	public void setDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		this.dichiarazioneIntra = dichiarazioneIntra;
		calcolaTotali();
	}
}
