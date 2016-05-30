/**
 * 
 */
package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author leonardo
 */
public class CarrelloAssegniTablePage extends AbstractTablePageEditor<AssegnoDTO> implements TableModelListener {

	public class AssegnaRapportoBancarioCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 * 
		 */
		public AssegnaRapportoBancarioCommand() {
			super("assegnaRapportoBancarioCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			ParametriCreazioneAreaChiusure parametri = (ParametriCreazioneAreaChiusure) parametriCreazioneAccreditoAssegnoForm
					.getFormObject();

			List<AreaAssegno> aree = new ArrayList<AreaAssegno>();
			for (AssegnoDTO assegnoDTO : getTable().getRows()) {
				aree.add(assegnoDTO.getAreaAssegno());
			}

			tesoreriaBD.assegnaRapportoBancarioAssegni(parametri.getRapportoBancarioAzienda(), aree);

			getSvuotaCarrelloCommand().execute();
		}

	}

	public class GeneraAccreditoAssegnoCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 */
		public GeneraAccreditoAssegnoCommand() {
			super("generaAccreditoAssegnoCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure = (ParametriCreazioneAreaChiusure) parametriCreazioneAccreditoAssegnoForm
					.getFormObject();
			List<AssegnoDTO> assegniDTO = getTable().getRows();
			List<AreaAssegno> assegni = new ArrayList<AreaAssegno>();
			for (AssegnoDTO assegnoDTO : assegniDTO) {
				assegni.add(assegnoDTO.getAreaAssegno());
			}
			AreaAccreditoAssegno accredito = tesoreriaBD.creaAreaAccreditoAssegno(parametriCreazioneAreaChiusure,
					assegni);

			// Apri l'editor dei documento di pagamento con i pagamenti creati.
			ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria = new ParametriRicercaAreeTesoreria();
			List<AreaTesoreria> list = new ArrayList<AreaTesoreria>();
			list.add(accredito);
			parametriRicercaAreeTesoreria.setAreeTesoreria(list);
			parametriRicercaAreeTesoreria.setPeriodo(new Periodo());
			parametriRicercaAreeTesoreria.setEffettuaRicerca(true);
			parametriRicercaAreeTesoreria.setTipoAreaPartita(accredito.getTipoAreaPartita());
			LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaAreeTesoreria);
			Application.instance().getApplicationContext().publishEvent(event);

			// Svuoto i pagamenti
			getSvuotaCarrelloCommand().execute();
		}
	}

	public class RimuoviAssegniSelezionatiCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 */
		public RimuoviAssegniSelezionatiCommand() {
			super("deleteCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			List<AssegnoDTO> assegniDTO = getTable().getSelectedObjects();

			for (AssegnoDTO assegnoDTO : assegniDTO) {
				getTable().removeRowObject(assegnoDTO);
			}
		}
	}

	public class SvuotaCarrelloCommand extends ApplicationWindowAwareCommand {

		/**
		 * Costruttore.
		 */
		public SvuotaCarrelloCommand() {
			super("svuotaCarrelloAssegniCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			setRows(new ArrayList<AssegnoDTO>());
		}
	}

	private static Logger logger = Logger.getLogger(CarrelloAssegniTablePage.class);

	public static final String PAGE_ID = "carrelloAssegniTablePage";

	private ITesoreriaBD tesoreriaBD = null;

	private SvuotaCarrelloCommand svuotaCarrelloCommand = null;
	private RimuoviAssegniSelezionatiCommand rimuoviAssegniSelezionatiCommand = null;
	private ParametriCreazioneAccreditoAssegnoForm parametriCreazioneAccreditoAssegnoForm = null;
	private GeneraAccreditoAssegnoCommand generaAccreditoAssegnoCommand = null;
	private AssegnaRapportoBancarioCommand assegnaRapportoBancarioCommand = null;

	/**
	 * Costruttore.
	 */
	public CarrelloAssegniTablePage() {
		super(PAGE_ID, new RisultatiRicercaAssegniTableModel(PAGE_ID));
		getTableModel().addTableModelListener(this);
	}

	/**
	 * Aggiunge gli assegni al carrello.
	 * 
	 * @param assegni
	 *            gli assegni da aggiungere
	 */
	public void addAssegni(List<AssegnoDTO> assegni) {
		for (AssegnoDTO assegnoDTO : assegni) {
			getTable().replaceOrAddRowObject(assegnoDTO, assegnoDTO, null);
		}
	}

	@Override
	public void dispose() {
		getTableModel().removeTableModelListener(this);
		super.dispose();
	}

	/**
	 * @return the assegnaRapportoBancarioCommand
	 */
	public AssegnaRapportoBancarioCommand getAssegnaRapportoBancarioCommand() {
		if (assegnaRapportoBancarioCommand == null) {
			assegnaRapportoBancarioCommand = new AssegnaRapportoBancarioCommand();
		}

		return assegnaRapportoBancarioCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getAssegnaRapportoBancarioCommand(), getGeneraAccreditoAssegnoCommand(),
				getSvuotaCarrelloCommand(), getRimuoviAssegniSelezionatiCommand() };
	}

	/**
	 * @return GeneraAccreditoAssegnoCommand
	 */
	public GeneraAccreditoAssegnoCommand getGeneraAccreditoAssegnoCommand() {
		if (generaAccreditoAssegnoCommand == null) {
			generaAccreditoAssegnoCommand = new GeneraAccreditoAssegnoCommand();
			generaAccreditoAssegnoCommand.setEnabled(false);
		}
		return generaAccreditoAssegnoCommand;
	}

	@Override
	public JComponent getHeaderControl() {
		logger.debug("--> Enter getHeaderControl");
		parametriCreazioneAccreditoAssegnoForm = new ParametriCreazioneAccreditoAssegnoForm();
		new PanjeaFormGuard(parametriCreazioneAccreditoAssegnoForm.getFormModel(), getGeneraAccreditoAssegnoCommand());
		new PanjeaFormGuard(parametriCreazioneAccreditoAssegnoForm.getFormModel(), getAssegnaRapportoBancarioCommand());

		JComponent control = parametriCreazioneAccreditoAssegnoForm.getControl();

		parametriCreazioneAccreditoAssegnoForm.getNewFormObjectCommand().execute();
		return control;
	}

	/**
	 * @return the rimuoviAssegnoCommand
	 */
	public RimuoviAssegniSelezionatiCommand getRimuoviAssegniSelezionatiCommand() {
		if (rimuoviAssegniSelezionatiCommand == null) {
			rimuoviAssegniSelezionatiCommand = new RimuoviAssegniSelezionatiCommand();
			rimuoviAssegniSelezionatiCommand.setEnabled(false);
		}
		return rimuoviAssegniSelezionatiCommand;
	}

	/**
	 * @return the svuotaCarrelloCommand
	 */
	public SvuotaCarrelloCommand getSvuotaCarrelloCommand() {
		if (svuotaCarrelloCommand == null) {
			svuotaCarrelloCommand = new SvuotaCarrelloCommand();
		}
		return svuotaCarrelloCommand;
	}

	/**
	 * @return table model originario.
	 */
	private RisultatiRicercaAssegniTableModel getTableModel() {
		return (RisultatiRicercaAssegniTableModel) TableModelWrapperUtils.getActualTableModel(getTable().getTable()
				.getModel());
	}

	/**
	 * @return the tesoreriaBD
	 */
	public ITesoreriaBD getTesoreriaBD() {
		return tesoreriaBD;
	}

	@Override
	public Collection<AssegnoDTO> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<AssegnoDTO> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {

	}

	/**
	 * @param tesoreriaBD
	 *            The tesoreriaBD to set.
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		getRimuoviAssegniSelezionatiCommand().setEnabled(getTableModel().getRowCount() != 0);
		getGeneraAccreditoAssegnoCommand().setEnabled(getTableModel().getRowCount() != 0);
		getAssegnaRapportoBancarioCommand().setEnabled(getTableModel().getRowCount() != 0);
		ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure = (ParametriCreazioneAreaChiusure) parametriCreazioneAccreditoAssegnoForm
				.getFormObject();
		if (getTableModel().getRowCount() > 0) {
			parametriCreazioneAreaChiusure.setRapportoBancarioAzienda(new RapportoBancarioAzienda());
			parametriCreazioneAccreditoAssegnoForm.setFormObject(parametriCreazioneAreaChiusure);
		}
	}

}
