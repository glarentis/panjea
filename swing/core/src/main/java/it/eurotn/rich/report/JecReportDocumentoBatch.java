package it.eurotn.rich.report;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;

import java.util.List;

import org.springframework.rules.closure.Closure;

public class JecReportDocumentoBatch extends JecReportDocumento {

	/**
	 * Costruttore.
	 * 
	 * @param areaDocumento
	 *            areaDocumento da stampare
	 */
	public JecReportDocumentoBatch(final IAreaDocumento areaDocumento) {
		super(areaDocumento);
	}

	/**
	 * Crea una nuova istanza di JecReportDocumento.
	 * 
	 * @param areaDocumento
	 *            areaDocumento di cui creare il jecReport
	 * @return JecReportDocumento
	 */
	protected JecReportDocumento creaJecReportDocumento(IAreaDocumento areaDocumento) {
		return new JecReportDocumento(areaDocumento);
	}

	@Override
	public void execute(Closure postAction) {
		List<LayoutStampaDocumento> layoutsBatch = layoutStampeManager.caricaLayoutStampaBatch(areaDocumento
				.getTipoAreaDocumento(), areaDocumento.getDocumento().getEntita(), areaDocumento.getDocumento()
				.getSedeEntita());

		String linguaEntita = (areaDocumento.getDocumento().getSedeEntita() != null) ? areaDocumento.getDocumento()
				.getSedeEntita().getLingua() : null;

		if (linguaEntita != null) {
			this.setTipoLinguaReport(TipoLingua.ENTITA);
		}

		if (layoutsBatch != null) {
			for (LayoutStampa layoutStampa : layoutsBatch) {
				if (getLayoutStampa() == null) {
					// imposto come layout di default il primo layout batch
					setLayoutStampa(layoutStampa);
				} else {
					// aggiungo il report con altri layout batch
					JecReportDocumento jecReportDocumentoBatch = new JecReportDocumento(areaDocumento);
					jecReportDocumentoBatch.setLayoutStampa(layoutStampa);
					if (linguaEntita != null) {
						jecReportDocumentoBatch.setTipoLinguaReport(TipoLingua.ENTITA);
					}
					addReport(jecReportDocumentoBatch);
				}
			}
		}

		LayoutStampa layout = getLayoutStampa();
		// devo controllare se diverso da null nel caso in cui non ci siano layout batch, compreso quello di default
		if (layout != null) {
			// devo settare il layout con il preview forzato a false
			layout.setPreview(false);
			setLayoutStampa(layout);
			super.execute(postAction);
		}
	}

}
