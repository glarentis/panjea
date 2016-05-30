/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.commands;

import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.IDescrizioneFactory;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaArticolo;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;
import it.eurotn.rich.command.JideToggleCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.support.AbstractPropertyChangePublisher;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.image.IconSource;

/**
 * Controller dei command per il cambio di lingua della maschera di Articolo.
 * 
 * @author adriano
 * @version 1.0, 12/mag/08
 * 
 */
public class CambioLinguaArticoloController extends AbstractPropertyChangePublisher {

	/**
	 * Estensione di ActionCommand per il cambio lingua di Articolo.
	 * 
	 * @author adriano
	 * @version 1.0, 12/mag/08
	 * 
	 */
	private final class CambioLinguaArticoloCommand extends JideToggleCommand {
		private static final String COMMAND_ID = "cambioLinguaArticoloCommand";
		private final FormModel formModel;
		private final String lingua;

		/**
		 * 
		 * @param lingua
		 *            lingua associata al command.
		 * @param formModel
		 *            formModel del form associato al controller della lingua
		 */
		private CambioLinguaArticoloCommand(final String lingua, final FormModel formModel) {
			super(COMMAND_ID + "." + lingua);
			setSecurityControllerId(COMMAND_ID);
			this.formModel = formModel;
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
			IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
			setIcon(iconSource.getIcon(lingua));
			this.lingua = lingua;
			if (this.lingua.equals(Locale.getDefault().getLanguage())) {
				setSelected(true);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onSelection() {
			super.onSelection();
			formModel.getValueModel("codiceLingua").setValue(lingua);
			codiceLinguaSelected = lingua;
			if (!aziendaCorrente.getLingua().equals(lingua)) {
				Map<String, IDescrizioneLingua> descrizioni = (Map<String, IDescrizioneLingua>) formModel
						.getValueModel("descrizioniLingua").getValue();
				if (!descrizioni.containsKey(lingua)) {
					IDescrizioneLingua descrizioneLingua = ((IDescrizioneFactory) this.formModel.getFormObject())
							.createDescrizioneLingua();
					descrizioneLingua.setCodiceLingua(lingua);
					((Articolo) this.formModel.getFormObject())
							.addDescrizione((DescrizioneLinguaArticolo) descrizioneLingua);
				}
			}
			formModel.getValueModel("descrizione").setValue(formModel.getValueModel("descrizione").getValue());
			if (formModel.isCommittable()) {
				formModel.commit();
			}
		}
	}

	private static Logger logger = Logger.getLogger(CambioLinguaArticoloController.class);
	public static final String PROPERTY_CAMBIO_LINGUA = "propertyCambioLingua";
	private IAnagraficaTabelleBD anagraficaTabelleBD;
	private final FormModel formModel;
	private List<AbstractCommand> commands = null;
	private AziendaCorrente aziendaCorrente;
	private String codiceLinguaSelected;

	/**
	 * 
	 * @param formModel
	 *            formModel associato al controller.
	 */
	public CambioLinguaArticoloController(final FormModel formModel) {
		super();
		this.formModel = formModel;
	}

	/**
	 * restituisce una {@link List} di command per il cambio della lingua.
	 * 
	 * @return List di command associati ad una lingua aziendale
	 */
	public AbstractCommand[] getCambioLinguaCommands() {
		logger.debug("--> Enter getCommands");
		// crea e aggiunge come primo command la Lingua dell'azienda corrente
		if (commands == null) {
			commands = new ArrayList<AbstractCommand>();
			CambioLinguaArticoloCommand cambioLinguaArticoloCommand;
			cambioLinguaArticoloCommand = new CambioLinguaArticoloCommand(aziendaCorrente.getLingua(), formModel);
			List<Lingua> list = anagraficaTabelleBD.caricaLingue();
			commands.add(cambioLinguaArticoloCommand);
			for (Lingua lingua : list) {
				cambioLinguaArticoloCommand = new CambioLinguaArticoloCommand(lingua.getCodice(), formModel);
				commands.add(cambioLinguaArticoloCommand);
			}
		}
		logger.debug("--> Exit getCommands");
		return commands.toArray(new AbstractCommand[commands.size()]);
	}

	/**
	 * @return Returns the codiceLinguaSelected.
	 */
	public String getCodiceLinguaSelected() {
		if (codiceLinguaSelected == null) {
			return Locale.getDefault().getLanguage();
		}
		return codiceLinguaSelected;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            The anagraficaTabelleBD to set.
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	/**
	 * 
	 * @param aziendaCorrente
	 *            azienda loggata.
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}
}
