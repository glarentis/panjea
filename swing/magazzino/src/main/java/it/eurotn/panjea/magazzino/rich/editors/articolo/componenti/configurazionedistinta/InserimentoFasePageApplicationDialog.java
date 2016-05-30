package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistintaBase;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

import org.springframework.richclient.dialog.FormBackedDialogPage;

public class InserimentoFasePageApplicationDialog extends PanjeaTitledPageApplicationDialog {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private ConfigurazioneDistinta configurazioneDistinta;
	private Componente componente;
	private Set<FaseLavorazioneArticolo> fasiAggiunte = new HashSet<>();

	/**
	 * Costruttore.
	 * 
	 * @param componente
	 *            componente
	 * @param configurazioneDistinta
	 *            configurazioneDistinta
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 */
	public InserimentoFasePageApplicationDialog(final Componente componente,
			final ConfigurazioneDistinta configurazioneDistinta, final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super(new FasiLavorazioneForm(), null);
		this.componente = componente;
		this.configurazioneDistinta = configurazioneDistinta;
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		setPreferredSize(new Dimension(500, 300));
		Integer numeroDecimaliQta = configurazioneDistinta.getDistinta().getNumeroDecimaliQta();
		if (componente != null) {
			numeroDecimaliQta = componente.getArticolo().getNumeroDecimaliQta();
		}
		((FasiLavorazioneForm) ((FormBackedDialogPage) getDialogPage()).getBackingFormPage())
				.setNumeroDecimaliQta(numeroDecimaliQta);
	}

	/**
	 * @return Returns the fasiAggiunte.
	 */
	public Set<FaseLavorazioneArticolo> getFasiAggiunte() {
		return fasiAggiunte;
	}

	@Override
	protected String getTitle() {
		// lo chiamo esplicitamente ,non so perche' non me lo chiama nella super.getTitle()
		return getCallingCommand() != null ? getCallingCommand().getText() : super.getTitle();
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();
		FasiLavorazioneForm fasiLavorazioneForm = (FasiLavorazioneForm) ((FormBackedDialogPage) getDialogPage())
				.getBackingFormPage();
		fasiLavorazioneForm.startTableEditing();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean onFinish() {
		Set<FaseLavorazioneArticolo> fasiLavorazione = (Set<FaseLavorazioneArticolo>) ((FormBackedDialogPage) getDialogPage())
				.getBackingFormPage().getValueModel("fasiLavorazioneArticolo").getValue();
		if (configurazioneDistinta instanceof ConfigurazioneDistintaBase) {
			ArticoloLite articoloBase = configurazioneDistinta.getDistinta().getArticoloLite();
			if (componente != null) {
				articoloBase = componente.getArticolo();
			}
			fasiAggiunte = magazzinoAnagraficaBD.aggiungiFasiLavorazione(configurazioneDistinta, articoloBase,
					fasiLavorazione);
		} else {
			fasiAggiunte = magazzinoAnagraficaBD.aggiungiFasiLavorazione(configurazioneDistinta, componente,
					fasiLavorazione);
		}
		return true;
	}
}
