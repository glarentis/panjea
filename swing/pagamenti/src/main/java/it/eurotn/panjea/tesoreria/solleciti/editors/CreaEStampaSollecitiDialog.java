package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.partite.rich.editors.ricercarate.PagamentoPM;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;

public class CreaEStampaSollecitiDialog extends StampaSollecitiDialog {

	private class GeneraSollecitiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "generaSollecitiCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public GeneraSollecitiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			List<Sollecito> solleciti = creaSolleciti();
			setSolleciti(solleciti);
		}

	}

	public static final String PAGE_ID = "creaEStampaSollecitiDialog";

	private List<PagamentoPM> pagamenti;

	private GeneraSollecitiCommand generaSollecitiCommand;

	private SollecitiForm sollecitiForm;

	/**
	 * Costruttore.
	 * 
	 */
	public CreaEStampaSollecitiDialog() {
		super();

		sollecitiForm = new SollecitiForm();

		new PanjeaFormGuard(sollecitiForm.getFormModel(), getGeneraSollecitiCommand(), FormGuard.ON_ISDIRTY
				+ FormGuard.ON_NOERRORS + FormGuard.ON_ENABLED);
	}

	/**
	 * @return crea i sollecitin perndendo come base i pagamenti.
	 */
	public List<Sollecito> creaSolleciti() {

		List<Sollecito> solleciti = new ArrayList<Sollecito>();
		List<RigaSollecito> righeSollecito = new ArrayList<RigaSollecito>();
		Importo importoSollecito = null;
		// per ogni cliente nella lista dei pagamenti genero un sollecito
		GroupingList<PagamentoPM> pagamentiRaggruppati = createSollecitiGrouplist();
		for (List<PagamentoPM> rate : pagamentiRaggruppati) {
			importoSollecito = new Importo();
			Sollecito sollecito = new Sollecito();
			sollecito.setDataCreazione(Calendar.getInstance().getTime());

			// recupero le rate del pagameto
			for (PagamentoPM pagamentoPM : rate) {
				// if (solleciti.size() <= pagamentiRaggruppati.indexOfGroup(pagamentoPM)) {
				// solleciti.add(pagamentiRaggruppati.indexOfGroup(pagamentoPM), new Sollecito());
				// groupNumber = pagamentiRaggruppati.indexOfGroup(pagamentoPM);
				// }
				Integer idRata = pagamentoPM.getRata().getRata().getId();
				Rata rata = getTesoreriaBD().caricaRata(idRata);
				RigaSollecito rigaSollecito = new RigaSollecito();
				rigaSollecito.setSollecito(sollecito);
				rigaSollecito.setRata(rata);
				rigaSollecito.setDataScadenza(rata.getDataScadenza());
				rigaSollecito.setImporto(rata.getImporto());
				rigaSollecito.setResiduo(rata.getResiduoConSegno());
				righeSollecito.add(rigaSollecito);

				importoSollecito.setCodiceValuta(pagamentoPM.getImporto().getCodiceValuta());
				// calcolo il monto del sollecito
				importoSollecito = importoSollecito.add(pagamentoPM.getImporto(), 2);
			}
			// setto le rate nel sollecito
			sollecito.setRigheSollecito(righeSollecito);
			sollecito.setImporto(importoSollecito);

			TemplateSolleciti templateSolleciti = ((Sollecito) sollecitiForm.getFormObject()).getTemplate();
			String nota = ((Sollecito) sollecitiForm.getFormObject()).getNota();
			sollecito.setTemplate(templateSolleciti);
			sollecito.setNota(nota);
			// genero il testo del sollecito in base al template
			try {
				sollecito.setTesto(getTesoreriaBD().creaTesto(templateSolleciti.getTesto(), sollecito));
				sollecito.setTestoFooter(getTesoreriaBD().creaTesto(templateSolleciti.getTestoFooter(), sollecito));
				sollecito.setOggettoMail(getTesoreriaBD().creaTesto(templateSolleciti.getOggettoMail(), sollecito));
				sollecito.setTestoMail(getTesoreriaBD().creaTesto(templateSolleciti.getTestoMail(), sollecito));
			} catch (Exception e) {
				throw new RuntimeException(
						"Errore durante la trasformacione del testo per genneracion report solleciti", e);
			}
			// save
			Sollecito sollecitoSalvato = getTesoreriaBD().salvaSollecito(sollecito);
			solleciti.add(sollecitoSalvato);
			righeSollecito.clear();
		}

		return solleciti;
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		JPanel buttonPanel = getComponentFactory().createPanel(new BorderLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
		buttonPanel.add(getGeneraSollecitiCommand().createButton(), BorderLayout.SOUTH);

		JPanel formPanel = getComponentFactory().createPanel(new BorderLayout());
		formPanel.add(sollecitiForm.getControl(), BorderLayout.CENTER);
		formPanel.add(buttonPanel, BorderLayout.EAST);

		rootPanel.add(formPanel, BorderLayout.NORTH);
		rootPanel.add(super.createDialogContentPane(), BorderLayout.CENTER);

		return rootPanel;
	}

	/**
	 * gruppa i pagamenti per cliente.
	 * 
	 * @return groupList.
	 */
	private GroupingList<PagamentoPM> createSollecitiGrouplist() {
		Comparator<PagamentoPM> entitacomparator = new Comparator<PagamentoPM>() {
			@Override
			public int compare(PagamentoPM o1, PagamentoPM o2) {
				return o1.getEntita().getId() - o2.getEntita().getId();
			}
		};
		// genero la grupinglist
		EventList<PagamentoPM> eventList = new BasicEventList<PagamentoPM>();
		eventList.addAll(pagamenti);
		GroupingList<PagamentoPM> group = new GroupingList<PagamentoPM>(eventList, entitacomparator);
		return group;
	}

	/**
	 * @return the generaSollecitiCommand
	 */
	public GeneraSollecitiCommand getGeneraSollecitiCommand() {
		if (generaSollecitiCommand == null) {
			generaSollecitiCommand = new GeneraSollecitiCommand();
		}

		return generaSollecitiCommand;
	}

	/**
	 * @param pagamenti
	 *            the pagamenti to set
	 */
	public void setPagamenti(List<PagamentoPM> pagamenti) {
		this.pagamenti = pagamenti;
	}

}
