/**
 *
 */
package it.eurotn.panjea.intra.manager;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.DatiArticoloIntra;
import it.eurotn.panjea.intra.domain.ModalitaErogazione;
import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.panjea.intra.domain.RigaBeneIntra;
import it.eurotn.panjea.intra.domain.RigaIntra;
import it.eurotn.panjea.intra.domain.RigaServizioIntra;
import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.domain.TipoPeriodo;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author leonardo
 */
public final class RigaIntraBuilder {

	/**
	 *
	 * @param areaIntra
	 *            l'area da cui verificare particolari condizioni per la creazione della riga
	 * @param datiIntra
	 *            dati dell'intra dell'articolo
	 * @param rigaArticolo
	 *            rigaArticolo da fillare
	 * @return rigaArticolo con i dati dell'intra
	 */
	public static RigaIntra creaRigaIntra(AreaIntra areaIntra, DatiArticoloIntra datiIntra, RigaArticolo rigaArticolo) {

		ETipoArticolo tipoArticolo = rigaArticolo.getArticolo().getTipoArticolo();
		double qta = rigaArticolo.getQta();
		ModalitaErogazione modalitaErogazione = datiIntra.getModalitaErogazione();
		Nazione nazione = datiIntra.getNazione();
		String codiceNazione = nazione != null ? nazione.getCodice() : "";
		Servizio servizio = datiIntra.getServizio();
		RigaIntra rigaIntra = null;
		Importo importo = rigaArticolo.getPrezzoTotale();

		switch (tipoArticolo) {
		case FISICO:
		case ACCESSORI:
			RigaBeneIntra rigaBeneIntra = new RigaBeneIntra();
			Nomenclatura nomenclatura = (Nomenclatura) servizio;
			BigDecimal massaNetta = BigDecimal.ZERO;

			// se l'unità di misura supplementare della nomenclatura articolo è presente, devo prendere il valore
			// u.m.suppl. dell'articolo
			if (nomenclatura.getUmsupplementare() != null) {
				massaNetta = datiIntra.getValoreUnitaMisuraSupplementare() != null ? datiIntra
						.getValoreUnitaMisuraSupplementare() : BigDecimal.ZERO;
			} else if (datiIntra.getMassaNetta() != null) {
				massaNetta = datiIntra.getMassaNetta();
			}

			// se la massa netta non è configurata la imposto di default a 0
			if(massaNetta == null) {
				massaNetta = BigDecimal.ZERO;
			}

			// se la massa è tra 0 e 1 forzo 1.
			BigDecimal tot = BigDecimal.valueOf(qta).multiply(massaNetta);
			if (tot.compareTo(BigDecimal.ONE) < 0 && tot.compareTo(BigDecimal.ZERO) > 0) {
				tot = BigDecimal.ONE;
			}
			tot = tot.round(new MathContext(3, RoundingMode.HALF_UP));

			rigaBeneIntra.setNomenclatura(nomenclatura);
			rigaBeneIntra.setImporto(importo);
			if (areaIntra.getTipoPeriodo().equals(TipoPeriodo.M)) {
				rigaBeneIntra.setPaeseOrigineArticolo(codiceNazione);
			}

			rigaBeneIntra.setMassa(tot);
			rigaBeneIntra.setUm(rigaArticolo.getUnitaMisura());
			rigaIntra = rigaBeneIntra;
			break;
		case SERVIZI:
			RigaServizioIntra rigaServizioIntra = new RigaServizioIntra();
			rigaServizioIntra.setServizio(servizio);
			rigaServizioIntra.setImporto(importo);
			rigaServizioIntra.setModalitaErogazione(modalitaErogazione);
			rigaIntra = rigaServizioIntra;
		default:
			break;
		}

		return rigaIntra;
	}

	/**
	 * Costruttore.
	 */
	private RigaIntraBuilder() {
	}

}
