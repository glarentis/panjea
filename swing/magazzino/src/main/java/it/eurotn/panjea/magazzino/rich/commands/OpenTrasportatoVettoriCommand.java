/**
 *
 */
package it.eurotn.panjea.magazzino.rich.commands;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.forms.trasportatovettore.TrasportatoVettoreForm;
import it.eurotn.panjea.magazzino.rich.forms.trasportatovettore.TrasportatoVettorePM;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.rich.command.OpenEditorCommand;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.JecReport;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;

/**
 * @author fattazzo
 *
 */
public class OpenTrasportatoVettoriCommand extends OpenEditorCommand {

	private TrasportatoVettoreForm trasportatoVettoreForm = null;

	{
		trasportatoVettoreForm = new TrasportatoVettoreForm();
	}

	@Override
	protected void doExecuteCommand() {

		trasportatoVettoreForm.getNewFormObjectCommand().execute();
		PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(trasportatoVettoreForm, null) {

			@Override
			protected boolean onFinish() {
				TrasportatoVettorePM trasportatoVettorePM = (TrasportatoVettorePM) trasportatoVettoreForm
						.getFormObject();
				if (trasportatoVettorePM.getPeriodo().getDataFinale() == null
						|| trasportatoVettorePM.getPeriodo().getDataIniziale() == null) {
					Message message = new DefaultMessage("Impostare le date", Severity.WARNING);
					MessageDialog dialog = new MessageDialog("Attenzione", message);
					dialog.showDialog();
					return false;
				}
				Map<Object, Object> parameters = new HashMap<Object, Object>();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Periodo periodo = trasportatoVettorePM.getPeriodo();
				parameters.put("DATA_1", format.format(periodo.getDataIniziale()));
				parameters.put("DATA_2", format.format(periodo.getDataFinale()));
				parameters.put("tuttiVettori", true);
				if (trasportatoVettorePM.getVettore() != null) {
					parameters.put("tuttiVettori", false);
					parameters.put("idVettore", trasportatoVettorePM.getVettore().getId());
				}
				List<Integer> tipiDoc = new ArrayList<Integer>();
				if (trasportatoVettorePM.getTipiAree() != null && !trasportatoVettorePM.getTipiAree().isEmpty()) {
					for (TipoAreaMagazzino tam : trasportatoVettorePM.getTipiAree()) {
						tipiDoc.add(tam.getTipoDocumento().getId());
					}
				}
				parameters.put("tipiDoc", tipiDoc);
				JecReport report = new JecReport("Magazzino/TrasportatoVettori", parameters);
				report.setReportName("Trasportato vettori");
				report.execute();
				return true;
			}
		};
		dialog.setPreferredSize(new Dimension(900, 300));
		dialog.showDialog();
	}

}
