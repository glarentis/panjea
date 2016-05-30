package it.eurotn.panjea.intra.rich.commands;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.intra.rich.bd.IntraBD;
import it.eurotn.panjea.intra.rich.pages.AreaIntraPage;

import java.awt.Dimension;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.util.Assert;
import org.springframework.richclient.util.RcpSupport;

/**
 * Apre l'area Intra del documento preso come parametro.<br/>
 * Se l'area intra non esiste ne viene creata una.
 * 
 * @author giangi
 * @version 1.0, 28/set/2012
 */
public class AreaIntraCommand extends ActionCommand {

	/**
	 * Costruttore.
	 */
	public AreaIntraCommand() {
		super("areaIntraCommand");
	}

	@Override
	protected void doExecuteCommand() {
		Documento documento = (Documento) getParameter("documento");
		Assert.notNull(documento);
		final AreaIntraPage areaIntraPage = new AreaIntraPage(true);
		ParametriRicercaAreaIntra parametri = new ParametriRicercaAreaIntra();
		parametri.setDocumentoCorrente(documento);
		areaIntraPage.setIntraBD((IIntraBD) RcpSupport.getBean(IntraBD.BEAN_ID));
		areaIntraPage.setFormObject(parametri);
		areaIntraPage.onPostPageOpen();
		TitledPageApplicationDialog dialog = new TitledPageApplicationDialog(areaIntraPage) {

			@Override
			protected Dimension getPreferredSize() {
				return new Dimension(1000, 600);
			}

			@Override
			protected String getTitle() {
				return RcpSupport.getMessage(AreaIntra.class.getName());
			}

			@Override
			protected boolean onFinish() {
				if (areaIntraPage.getForm().isDirty()) {
					areaIntraPage.onSave();
				}
				return true;
			}
		};
		dialog.showDialog();
	}

}
