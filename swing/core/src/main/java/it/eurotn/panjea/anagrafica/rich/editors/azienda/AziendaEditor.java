package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.rich.editors.DefaultEditor;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.security.JecPrincipal;

import org.springframework.richclient.util.RcpSupport;

public class AziendaEditor extends DefaultEditor {

	@Override
	public void initialize(Object editorObject) {

		if (editorObject instanceof String) {
			JecPrincipal principal = PanjeaSwingUtil.getUtenteCorrente();
			editorObject = ((IAnagraficaBD) RcpSupport.getBean(AnagraficaBD.BEAN_ID)).caricaAziendaAnagrafica(principal
					.getCodiceAzienda());
		}

		super.initialize(editorObject);
	}
}
