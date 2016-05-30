/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Contiene i controlli dei dati accessori dell'area magazzino.
 *
 * @author fattazzo
 *
 */
public class DatiAccessoriForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "datiAccessoriForm";

	private final IAnagraficaBD anagraficaBD;

	/**
	 *
	 * Costruttore.
	 *
	 * @param pageFormModel
	 *            form model della pagina
	 * @param anagraficaBD
	 *            bd dell'anagrafica
	 */
	public DatiAccessoriForm(final FormModel pageFormModel, final IAnagraficaBD anagraficaBD) {
		super(pageFormModel, FORM_ID);
		this.anagraficaBD = anagraficaBD;

		// aggiungo la finta proprietà tipiEntita per far si che la search text dei vettori mi selezioni solo vettori e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaVettori = new ArrayList<TipoEntita>();
		tipiEntitaVettori.add(TipoEntita.VETTORE);

		ValueModel tipiEntitaVettoriValueModel = new ValueHolder(tipiEntitaVettori);
		DefaultFieldMetadata tipiEntitaVettoriData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaVettoriValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaVettori", tipiEntitaVettoriValueModel, tipiEntitaVettoriData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		// RIGA 1:vettore e sede
		Binding entitaBinding = getVettoreBinding(bf);
		builder.add(entitaBinding);
		Binding sedeEntitaBinding = bf.createBoundSearchText("areaMagazzino.sedeVettore",
				new String[] { "sede.descrizione" }, new String[] { "areaMagazzino.vettore" },
				new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		builder.add(sedeEntitaBinding, "align=left");

		builder.add(bf.createBoundCalendar("areaMagazzino.dataInizioTrasporto", "dd/MM/yy HH:mm", "##/##/## ##:##"),
				"align=left");

		builder.row();

		// RIGA 2
		builder.add(bf.createBoundSearchText("areaMagazzino.causaleTrasporto", null, CausaleTrasporto.class));
		builder.add(bf.createBoundSearchText("areaMagazzino.trasportoCura", null, TrasportoCura.class), "align=left");
		builder.add(
				bf.createBoundCalendar("areaMagazzino.datiSpedizioniDocumento.dataConsegna", "dd/MM/yy", "##/##/##"),
				"align=left");
		builder.row();

		// RIGA 3
		builder.add(bf.createBoundSearchText("areaMagazzino.aspettoEsteriore", null, AspettoEsteriore.class));
		builder.add(bf.createBoundSearchText("areaMagazzino.tipoPorto", null, TipoPorto.class), "align=left");
		((JTextField) builder.add("areaMagazzino.pallet", "align=left")[1]).setColumns(10);
		builder.row();

		// RIGA 4
		((JTextField) builder.add("areaMagazzino.pesoNetto", "align=left")[1]).setColumns(10);
		((JTextField) builder.add("areaMagazzino.pesoLordo", "align=left")[1]).setColumns(10);
		JFormattedTextField volumeField = ((JFormattedTextField) builder.add(
				bf.createBoundFormattedTextField("areaMagazzino.volume",
						new DefaultNumberFormatterFactory("###,###,###,##0", 3, BigDecimal.class, true)),
				"align=left")[1]);
		volumeField.setColumns(10);
		volumeField.setHorizontalAlignment(SwingConstants.RIGHT);
		builder.row();

		// RIGA 5
		// ((JTextField)
		builder.add("areaMagazzino.responsabileRitiroMerce", "align=left");// [1]).setColumns(10);
		((JTextField) builder.add("areaMagazzino.numeroColli", "align=left")[1]).setColumns(10);

		// builder.add(bf.createBoundCalendar("areaMagazzino.dataInizioTrasporto", "dd/MM/yy HH:mm", "##/##/## ##:##"),
		// "align=left");
		builder.row();

		VettorePropertyChange vettorePropertyChange = new VettorePropertyChange(
				getValueModel("areaMagazzino.sedeVettore"));
		vettorePropertyChange.setFormModel(getFormModel());
		vettorePropertyChange.setAnagraficaBD(this.anagraficaBD);
		getFormModel().getValueModel("areaMagazzino.vettore").addValueChangeListener(vettorePropertyChange);

		return builder.getForm();
	}

	/**
	 *
	 * @param bf
	 *            binding Factory
	 * @return binding del vettore.
	 */
	private Binding getVettoreBinding(PanjeaSwingBindingFactory bf) {
		Binding bindingEntita = bf.createBoundSearchText("areaMagazzino.vettore",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaVettori" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("codice").setColumns(5);
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("anagrafica.denominazione").setColumns(15);
		return bindingEntita;
	}
}
