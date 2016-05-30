package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.rich.rules.AttributiRigaConstraint;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.form.support.AbstractFormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;

public class FormuleTrasformazioneRigaArticoloFormCalculator {

	private FormModel formModel;

	private static Logger logger = Logger.getLogger(FormuleTrasformazioneRigaArticoloFormCalculator.class);

	private AttributiRigaConstraint attributiRigaConstraint = new AttributiRigaConstraint();

	private FormuleRigaArticoloCalculator formuleRigaArticoloCalculator;

	/**
	 * Costruttore.
	 */
	public FormuleTrasformazioneRigaArticoloFormCalculator() {
		// Costruisco nel costruttore per evitare di ricorstruirlo ogni volta
		formuleRigaArticoloCalculator = new FormuleRigaArticoloCalculator();
	}

	/**
	 * Calcola il valore di tutti gli attributi e delle qta in base alle formule di trasformazione assegnate. e li
	 * riassegna ai valuemodel "attributi" "qta" e "qtaMagazzino"
	 *
	 * @throws FormuleTipoAttributoException
	 *             eccezione nel calcolo delle formule
	 *
	 */
	public void calcola() throws FormuleTipoAttributoException {
		if (formModel.isReadOnly()) {
			// non sono in modifica...ciao ciao
			return;
		}
		if ((Boolean) formModel.getValueModel("InCalcolo").getValue()) {
			return;
		}

		try {
			formModel.getValueModel("InCalcolo").setValue(true);
			@SuppressWarnings("unchecked")
			List<AttributoRigaArticolo> attributi = (List<AttributoRigaArticolo>) formModel.getValueModel("attributi")
			.getValue();

			// se gli attributi non sono validi non calcolo niente
			if (!attributi.isEmpty() && !attributiRigaConstraint.test(attributi)) {
				((ValidatingFormModel) formModel).validate();
				return;
			}

			IRigaArticoloDocumento riga = formuleRigaArticoloCalculator.calcola((IRigaArticoloDocumento) formModel
					.getFormObject());
			formModel.getValueModel("qta").setValue(riga.getQta());
			formModel.getValueModel("qtaMagazzino").setValue(riga.getQtaMagazzino());
			// Ricreo una nuova lista di attributi altrimenti il setValue (ed il changeDetector) non vede cambiamenti
			// alla proprietà attributi. Infatti FormuleRigaCalculator cambia i valori negli elementi della lista ma NON
			// cambia la lista quindi per il valueModel ho lo stesso oggetto. e non vedo gli attributi modificati.
			formModel.getValueModel("attributi").setValue(new ArrayList<AttributoRigaArticolo>(riga.getAttributi()));
			formModel.getValueModel("componenti").setValue(riga.getComponenti());

			// richiamo la validate per eseguire le validazioni sui componenti
			// della riga articolo
			((ValidatingFormModel) formModel).validate();

		} catch (FormuleTipoAttributoException fex) {
			throw fex;
		} catch (Exception e) {
			logger.error("--> errore sul cambio attributi ", e);
			throw new RuntimeException(e);
		} finally {
			formModel.getValueModel("InCalcolo").setValue(false);
		}
	}

	/**
	 * @param formModel
	 *            formModel
	 */
	public void setFormModel(FormModel formModel) {
		if (formModel == null) {
			return;
		}
		this.formModel = formModel;
		if (!this.formModel.hasValueModel("InCalcolo")) {
			ValueModel inCalcoloValueModel = new ValueHolder(Boolean.FALSE);
			DefaultFieldMetadata metaData = new DefaultFieldMetadata(formModel, new FormModelMediatingValueModel(
					inCalcoloValueModel), Boolean.class, true, null);
			((AbstractFormModel) formModel).add("InCalcolo", inCalcoloValueModel, metaData);
		}
	}

}
