package it.eurotn.panjea.anagrafica.rich.commands.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.LinkDocumento;
import it.eurotn.panjea.anagrafica.rich.converter.DocumentoConverter;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class LinkDocumentoForm extends PanjeaAbstractForm {

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public LinkDocumentoForm() {
		super(PanjeaFormModelHelper.createFormModel(new Documento(), false, "linkDocumentiPageForm"));
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:default:grow", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);
		StringBuilder sb = new StringBuilder("<HTML><B>");
		DocumentoConverter dc = new DocumentoConverter();
		sb.append(dc.toString((getFormObject()), null));
		sb.append("</B></HYML>");
		JLabel labelDocumento = new JLabel(sb.toString());
		builder.addComponent(labelDocumento);
		builder.nextRow();
		builder.setLabelAttributes("l,c");
		builder.addLabel("documentiOrigine");
		builder.nextRow();

		LinkDocumentoOrigineTableModel linkDocumentoOrigineTableModel = new LinkDocumentoOrigineTableModel(
				((Documento) getFormObject()));
		TableEditableBinding<LinkDocumento> righeDocumentiOriginebinding = new TableEditableBinding<LinkDocumento>(
				getFormModel(), "documentiOrigine", Set.class, linkDocumentoOrigineTableModel);
		righeDocumentiOriginebinding.getControl().setPreferredSize(new Dimension(100, 150));
		builder.addBinding(righeDocumentiOriginebinding);
		builder.nextRow();

		builder.addLabel("documentiDestinazione");
		builder.nextRow();
		LinkDocumentoDestinazioneTableModel linkDocumentoDestinazioneTableModel = new LinkDocumentoDestinazioneTableModel(
				((Documento) getFormObject()));
		TableEditableBinding<LinkDocumento> righeDocumentiDestinazionebinding = new TableEditableBinding<LinkDocumento>(
				getFormModel(), "documentiDestinazione", Set.class, linkDocumentoDestinazioneTableModel);
		righeDocumentiDestinazionebinding.getControl().setPreferredSize(new Dimension(100, 150));
		builder.addBinding(righeDocumentiDestinazionebinding);
		return builder.getPanel();
	}
}
