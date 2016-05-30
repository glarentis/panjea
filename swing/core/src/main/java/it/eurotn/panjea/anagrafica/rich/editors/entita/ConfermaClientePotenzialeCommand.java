/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.ClientePotenziale;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.editors.entita.anagrafica.AnagraficaPage;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.util.Assert;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author Leonardo
 * 
 */
public class ConfermaClientePotenzialeCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "confermaClientePotenzialeCommand";

	private IAnagraficaBD anagraficaBD = null;
	private Integer idEntita = null;
	private Entita clienteConfermato = null;

	private final AnagraficaPage anagraficaPage;

	/**
	 * Costruttore.
	 * 
	 * @param anagraficaPage
	 *            pagina contenente il cliente da confermare.
	 */
	public ConfermaClientePotenzialeCommand(final AnagraficaPage anagraficaPage) {
		super(COMMAND_ID);
		setSecurityControllerId(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);
		this.setVisible(false);
		this.anagraficaPage = anagraficaPage;
	}

	@Override
	protected void doExecuteCommand() {
		Assert.notNull(idEntita, "idEntita cannot be null!");
		clienteConfermato = getAnagraficaBD().confermaClientePotenziale(idEntita);

		if (clienteConfermato != null) {
			OpenEditorEvent openEditorEvent = new OpenEditorEvent(clienteConfermato);

			// per far riconoscere gli elementi tolti e aggiunti alle
			// search result devo passare ClienteLite e ClientePotenzialeLite
			ClientePotenziale clientePotenziale = (ClientePotenziale) anagraficaPage.getBackingFormPage()
					.getFormObject();
			ClientePotenzialeLite clientePotenzialeLite = new ClientePotenzialeLite();
			clientePotenzialeLite.setId(clientePotenziale.getId());

			ClienteLite clienteLite = new ClienteLite();
			DatiGeografici datiGeografici = clienteLite.getAnagrafica().getSedeAnagrafica().getDatiGeografici();

			clienteLite.setId(clienteConfermato.getId());
			clienteLite.setCodice(clienteConfermato.getCodice());
			clienteLite.getAnagrafica().setDenominazione(clienteConfermato.getAnagrafica().getDenominazione());
			clienteLite.getAnagrafica().setSedeAnagrafica(clienteConfermato.getAnagrafica().getSedeAnagrafica());
			clienteLite.getAnagrafica().getSedeAnagrafica()
					.setIndirizzo(clienteConfermato.getAnagrafica().getSedeAnagrafica().getIndirizzo());
			clienteLite.getAnagrafica().setPartiteIVA(clienteConfermato.getAnagrafica().getPartiteIVA());

			String descCap = clienteConfermato.getAnagrafica().getSedeAnagrafica().getDatiGeografici()
					.getDescrizioneCap();
			if (descCap != null) {
				datiGeografici.setCap(new Cap());
				datiGeografici.getCap().setDescrizione(descCap);
			}

			String descLoc = clienteConfermato.getAnagrafica().getSedeAnagrafica().getDatiGeografici()
					.getDescrizioneLocalita();
			if (descLoc != null) {
				datiGeografici.setLocalita(new Localita());
				datiGeografici.getLocalita().setDescrizione(descLoc);
			}

			String descLvl1 = clienteConfermato.getAnagrafica().getSedeAnagrafica().getDatiGeografici()
					.getDescrizioneLivelloAmministrativo1();
			if (descLvl1 != null) {
				datiGeografici.setLivelloAmministrativo1(new LivelloAmministrativo1());
				datiGeografici.getLivelloAmministrativo1().setNome(descLvl1);
			}

			String descLvl2 = clienteConfermato.getAnagrafica().getSedeAnagrafica().getDatiGeografici()
					.getDescrizioneLivelloAmministrativo2();
			if (descLvl2 != null) {
				datiGeografici.setLivelloAmministrativo2(new LivelloAmministrativo2());
				datiGeografici.getLivelloAmministrativo2().setNome(descLvl2);
			}

			String descLvl3 = clienteConfermato.getAnagrafica().getSedeAnagrafica().getDatiGeografici()
					.getDescrizioneLivelloAmministrativo3();
			if (descLvl3 != null) {
				datiGeografici.setLivelloAmministrativo3(new LivelloAmministrativo3());
				datiGeografici.getLivelloAmministrativo3().setNome(descLvl3);
			}

			String descLvl4 = clienteConfermato.getAnagrafica().getSedeAnagrafica().getDatiGeografici()
					.getDescrizioneLivelloAmministrativo4();
			if (descLvl4 != null) {
				datiGeografici.setLivelloAmministrativo4(new LivelloAmministrativo4());
				datiGeografici.getLivelloAmministrativo4().setNome(descLvl4);
			}

			String descNaz = clienteConfermato.getAnagrafica().getSedeAnagrafica().getDatiGeografici()
					.getDescrizioneNazione();
			if (descNaz != null) {
				datiGeografici.setNazione(new Nazione());
				datiGeografici.getNazione().setDescrizione(descNaz);
			}

			PanjeaLifecycleApplicationEvent entitaProvvisoriaLiteEvent = new PanjeaLifecycleApplicationEvent(
					LifecycleApplicationEvent.DELETED, clientePotenzialeLite);
			PanjeaLifecycleApplicationEvent entitaProvvisoriaEvent = new PanjeaLifecycleApplicationEvent(
					LifecycleApplicationEvent.DELETED, clientePotenziale);
			PanjeaLifecycleApplicationEvent entitaConfermataLiteEvent = new PanjeaLifecycleApplicationEvent(
					LifecycleApplicationEvent.CREATED, clienteLite);

			Application.instance().getApplicationContext().publishEvent(entitaProvvisoriaEvent);
			Application.instance().getApplicationContext().publishEvent(entitaProvvisoriaLiteEvent);
			Application.instance().getApplicationContext().publishEvent(entitaConfermataLiteEvent);
			Application.instance().getApplicationContext().publishEvent(openEditorEvent);
		}
	}

	/**
	 * 
	 * @return bd dell'anagrafica
	 */
	private IAnagraficaBD getAnagraficaBD() {
		if (anagraficaBD == null) {
			anagraficaBD = (IAnagraficaBD) Application.instance().getApplicationContext()
					.getBean(IAnagraficaBD.BEAN_ID);
		}
		return anagraficaBD;
	}

	/**
	 * @return the clienteConfermato
	 */
	public Entita getClienteConfermato() {
		return clienteConfermato;
	}

	/**
	 * @return the idEntita
	 */
	public Integer getIdEntita() {
		return idEntita;
	}

	/**
	 * @param clienteConfermato
	 *            the clienteConfermato to set
	 */
	public void setClienteConfermato(Entita clienteConfermato) {
		this.clienteConfermato = clienteConfermato;
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		this.idEntita = idEntita;
		this.setVisible(idEntita != null);
	}
}
