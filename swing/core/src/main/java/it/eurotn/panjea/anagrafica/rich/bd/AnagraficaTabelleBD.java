/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaTabelleService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author adriano
 * @version 1.0, 18/dic/07
 */
public class AnagraficaTabelleBD extends AbstractBaseBD implements IAnagraficaTabelleBD {

	public static final String BEAN_ID = "anagraficaTabelleBD";
	private final Logger logger = Logger.getLogger(AnagraficaTabelleBD.class);
	private AnagraficaTabelleService anagraficaTabelleService;

	/**
	 * Costruttore.
	 */
	public AnagraficaTabelleBD() {
		super();
	}

	@Override
	public Documento aggiungiContrattoADocumento(ContrattoSpesometro contratto, Documento documento) {
		logger.debug("--> Enter aggiungiContrattoADocumento");
		start("aggiungiContrattoADocumento");
		Documento documentoSalvato = null;
		try {
			documentoSalvato = anagraficaTabelleService.aggiungiContrattoADocumento(contratto, documento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("aggiungiContrattoADocumento");
		}
		logger.debug("--> Exit aggiungiContrattoADocumento con contratto " + contratto);
		return documentoSalvato;
	}

	@Override
	public void cancellaCarica(Carica carica) {
		logger.debug("--> Enter cancellaCarica");
		start("cancellaCarica");
		try {
			anagraficaTabelleService.cancellaCarica(carica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCarica");
		}
		logger.debug("--> Exit cancellaCarica ");
	}

	@Override
	public void cancellaCodiceIva(CodiceIva codiceIva) {
		logger.debug("--> Enter cancellaCodiceIva");
		start("cancellaCodiceIva");
		try {
			anagraficaTabelleService.cancellaCodiceIva(codiceIva);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCodiceIva");
		}
		logger.debug("--> Exit cancellaCodiceIva");
	}

	@Override
	public void cancellaContratto(ContrattoSpesometro contratto) {
		logger.debug("--> Enter cancellaContratto");
		start("cancellaContratto");
		try {
			anagraficaTabelleService.cancellaContratto(contratto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaContratto");
		}
		logger.debug("--> Exit cancellaContratto ");
	}

	@Override
	public void cancellaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura) {
		logger.debug("--> Enter cancellaConversioneUnitaMisura");
		start("cancellaConversioneUnitaMisura");
		try {
			anagraficaTabelleService.cancellaConversioneUnitaMisura(conversioneUnitaMisura);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaConversioneUnitaMisura");
		}
		logger.debug("--> Exit cancellaConversioneUnitaMisura");
	}

	@Override
	public void cancellaFormaGiuridica(FormaGiuridica formaGiuridica) {
		logger.debug("--> Enter cancellaFormaGiuridica");
		start("cancellaFormaGiuridica");
		try {
			anagraficaTabelleService.cancellaFormaGiuridica(formaGiuridica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaFormaGiuridica");
		}
		logger.debug("--> Exit cancellaFormaGiuridica ");
	}

	@Override
	public void cancellaLingua(Lingua lingua) {
		logger.debug("--> Enter cancellaLingua");
		start("cancellaLingua");
		try {
			anagraficaTabelleService.cancellaLingua(lingua);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaLingua");
		}
		logger.debug("--> Exit cancellaLingua ");
	}

	@Override
	public void cancellaMansione(Mansione mansione) {
		logger.debug("--> Enter cancellaMansione");
		start("cancellaMansione");
		try {
			anagraficaTabelleService.cancellaMansione(mansione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaMansione");
		}
		logger.debug("--> Exit cancellaMansione ");
	}

	@Override
	public void cancellaNotaAnagrafica(NotaAnagrafica notaAnagrafica) {
		logger.debug("--> Enter cancellaNotaAnagrafica");
		start("cancellaNotaAnagrafica");
		try {
			anagraficaTabelleService.cancellaNotaAnagrafica(notaAnagrafica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaNotaAnagrafica");
		}
		logger.debug("--> Exit cancellaNotaAnagrafica ");
	}

	@Override
	public void cancellaTipoDeposito(TipoDeposito tipoDeposito) {
		logger.debug("--> Enter cancellaTipoDeposito");
		start("cancellaTipoDeposito");
		try {
			anagraficaTabelleService.cancellaTipoDeposito(tipoDeposito);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaTipoDeposito");
		}
		logger.debug("--> Exit cancellaTipoDeposito");
	}

	@Override
	public void cancellaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) {
		logger.debug("--> Enter cancellaTipoSedeEntita");
		start("cancellaTipoSedeEntita");
		try {
			anagraficaTabelleService.cancellaTipoSedeEntita(tipoSedeEntita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaTipoSedeEntita");
		}
		logger.debug("--> Exit cancellaTipoSedeEntita ");
	}

	@Override
	public void cancellaUnitaMisura(UnitaMisura unitaMisura) {
		logger.debug("--> Enter cancellaUnitaMisura");
		start("cancellaUnitaMisura");
		try {
			anagraficaTabelleService.cancellaUnitaMisura(unitaMisura);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaUnitaMisura");
		}
		logger.debug("--> Exit cancellaUnitaMisura ");
	}

	@Override
	public void cancellaZonaGeografica(ZonaGeografica zonaGeografica) {
		logger.debug("--> Enter cancellaZonaGeografica");
		start("cancellaZonaGeografica");
		try {
			anagraficaTabelleService.cancellaZonaGeografica(zonaGeografica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaZonaGeografica");
		}
		logger.debug("--> Exit cancellaZonaGeografica");
	}

	@Override
	public List<Carica> caricaCariche(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaCariche");
		start("caricaCariche");
		List<Carica> cariche = null;
		try {
			cariche = anagraficaTabelleService.caricaCariche(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCariche");
		}
		logger.debug("--> Exit caricaCariche ");
		return cariche;
	}

	@Override
	public CodiceIva caricaCodiceIva(Integer id) {
		logger.debug("--> Enter caricaCodiceIva");
		start("caricaCodiceIva");
		CodiceIva codiceIva = null;
		try {
			codiceIva = anagraficaTabelleService.caricaCodiceIva(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCodiceIva");
		}
		logger.debug("--> Exit caricaCodiceIva con codiceIva " + codiceIva);
		return codiceIva;
	}

	@Override
	public List<CodiceIva> caricaCodiciIva(String codice) {
		logger.debug("--> Enter caricaCodiciIva");
		start("caricaCodiciIva");
		List<CodiceIva> codiciIva = null;
		try {
			codiciIva = anagraficaTabelleService.caricaCodiciIva(codice);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCodiciIva");
		}
		logger.debug("--> Exit caricaCodiciIva.");
		return codiciIva;
	}

	@Override
	public List<ContrattoSpesometro> caricaContratti(EntitaLite entita) {
		logger.debug("--> Enter caricaContratti");
		start("caricaContratti");
		List<ContrattoSpesometro> contratti = null;
		try {
			contratti = anagraficaTabelleService.caricaContratti(entita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContratti");
		}
		logger.debug("--> Exit caricaContratti ");
		return contratti;
	}

	@Override
	public ContrattoSpesometro caricaContratto(Integer idContratto) {
		logger.debug("--> Enter caricaContratto");
		start("caricaContratto");
		ContrattoSpesometro codiceIva = null;
		try {
			codiceIva = anagraficaTabelleService.caricaContratto(idContratto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContratto");
		}
		logger.debug("--> Exit caricaContratto con contratto " + codiceIva);
		return codiceIva;
	}

	@Override
	public ContrattoSpesometro caricaContratto(Integer idContratto, boolean loadCollection) {
		logger.debug("--> Enter caricaContratto");
		start("caricaContratto");
		ContrattoSpesometro codiceIva = null;
		try {
			codiceIva = anagraficaTabelleService.caricaContratto(idContratto, loadCollection);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaContratto");
		}
		logger.debug("--> Exit caricaContratto con contratto " + codiceIva);
		return codiceIva;
	}

	@Override
	public ConversioneUnitaMisura caricaConversioneUnitaMisura(String unitaMisuraOrigine, String unitaMisuraDestinazione) {
		logger.debug("--> Enter caricaConversioneUnitaMisura");
		start("caricaConversioneUnitaMisura");
		ConversioneUnitaMisura result = null;
		try {
			result = anagraficaTabelleService.caricaConversioneUnitaMisura(unitaMisuraOrigine, unitaMisuraDestinazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaConversioneUnitaMisura");
		}
		logger.debug("--> Exit caricaConversioneUnitaMisura");
		return result;
	}

	@Override
	public List<ConversioneUnitaMisura> caricaConversioniUnitaMisura() {
		logger.debug("--> Enter caricaConversioniUnitaMisura");
		start("caricaConversioniUnitaMisura");
		List<ConversioneUnitaMisura> listReturn = new ArrayList<ConversioneUnitaMisura>();
		try {
			listReturn = anagraficaTabelleService.caricaConversioniUnitaMisura();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaConversioniUnitaMisura");
		}
		logger.debug("--> Exit caricaConversioniUnitaMisura");
		return listReturn;
	}

	@Override
	public List<Documento> caricaDocumentiContratto(Integer idContratto) {
		logger.debug("--> Enter caricaDocumentiContratto");
		start("caricaDocumentiContratto");
		List<Documento> documenti = null;
		try {
			documenti = anagraficaTabelleService.caricaDocumentiContratto(idContratto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDocumentiContratto");
		}
		logger.debug("--> Exit caricaDocumentiContratto ");
		return documenti;
	}

	@Override
	public FormaGiuridica caricaFormaGiuridica(Integer idFormaGiuridica) {
		logger.debug("--> Enter caricaFormaGiuridica");
		start("caricaFormaGiuridica");
		FormaGiuridica formaGiuridica = null;
		try {
			formaGiuridica = anagraficaTabelleService.caricaFormaGiuridica(idFormaGiuridica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaFormaGiuridica");
		}
		logger.debug("--> Exit caricaFormaGiuridica ");
		return formaGiuridica;
	}

	@Override
	public List<FormaGiuridica> caricaFormeGiuridiche(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaFormeGiuridiche");
		start("caricaFormeGiuridiche");
		List<FormaGiuridica> formeGiuridiche = null;
		try {
			formeGiuridiche = anagraficaTabelleService.caricaFormeGiuridiche(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaFormeGiuridiche");
		}
		logger.debug("--> Exit caricaFormeGiuridiche ");
		return formeGiuridiche;
	}

	@Override
	public Lingua caricaLingua(Lingua lingua) {
		logger.debug("--> Enter caricaLingua");
		start("caricaLingua");
		try {
			lingua = anagraficaTabelleService.caricaLingua(lingua);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLingua");
		}
		logger.debug("--> Exit caricaLingua ");
		return lingua;
	}

	@Override
	public List<Lingua> caricaLingue() {
		logger.debug("--> Enter caricaLingue");
		start("caricaLingue");
		List<Lingua> lingue = null;
		try {
			lingue = anagraficaTabelleService.caricaLingue();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLingue");
		}
		logger.debug("--> Exit caricaLingue ");
		return lingue;
	}

	@Override
	public List<Mansione> caricaMansioni(String descrizione) {
		logger.debug("--> Enter caricaMansioni");
		start("caricaMansioni");
		List<Mansione> mansioni = null;
		try {
			mansioni = anagraficaTabelleService.caricaMansioni(descrizione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaMansioni");
		}
		logger.debug("--> Exit caricaMansioni ");
		return mansioni;
	}

	@Override
	public List<NotaAnagrafica> caricaNoteAnagrafica() {
		logger.debug("--> Enter caricaNoteAnagrafica");
		start("caricaNoteAnagrafica");
		List<NotaAnagrafica> note = new ArrayList<NotaAnagrafica>();
		try {
			note = anagraficaTabelleService.caricaNoteAnagrafica();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaNoteAnagrafica");
		}
		logger.debug("--> Exit caricaNoteAnagrafica ");
		return note;
	}

	@Override
	public List<TipoDeposito> caricaTipiDepositi() {
		logger.debug("--> Enter caricaTipiDepositi");
		start("caricaTipiDepositi");
		List<TipoDeposito> tipiDepositi = new ArrayList<TipoDeposito>();
		try {
			tipiDepositi = anagraficaTabelleService.caricaTipiDepositi();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiDepositi");
		}
		logger.debug("--> Exit caricaTipiDepositi ");
		return tipiDepositi;
	}

	@Override
	public List<TipoSedeEntita> caricaTipiSede(String codice) {
		logger.debug("--> Enter caricaTipiSede");
		start("caricaTipiSede");
		List<TipoSedeEntita> tipiSede = null;
		try {
			tipiSede = anagraficaTabelleService.caricaTipiSede(codice);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiSede");
		}
		logger.debug("--> Exit caricaTipiSede ");
		return tipiSede;
	}

	@Override
	public List<TipoSedeEntita> caricaTipiSedeSecondari() {
		logger.debug("--> Enter caricaTipiSedeSecondari");
		start("caricaTipiSedeSecondari");
		List<TipoSedeEntita> tipiSede = null;
		try {
			tipiSede = anagraficaTabelleService.caricaTipiSedeSecondari();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiSedeSecondari");
		}
		logger.debug("--> Exit caricaTipiSedeSecondari ");
		return tipiSede;
	}

	@Override
	public List<UnitaMisura> caricaUnitaMisura() {
		logger.debug("--> Enter caricaUnitaMisura");
		start("caricaUnitaMisura");
		List<UnitaMisura> listReturn = new ArrayList<UnitaMisura>();
		try {
			listReturn = anagraficaTabelleService.caricaUnitaMisura();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaUnitaMisura");
		}
		logger.debug("--> Exit caricaUnitaMisura ");
		return listReturn;
	}

	@Override
	public List<UnitaMisura> caricaUnitaMisura(String codice) {
		logger.debug("--> Enter caricaUnitaMisura");
		start("caricaUnitaMisura");
		List<UnitaMisura> listReturn = new ArrayList<UnitaMisura>();
		try {
			listReturn = anagraficaTabelleService.caricaUnitaMisura(codice);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaUnitaMisura");
		}
		logger.debug("--> Exit caricaUnitaMisura ");
		return listReturn;
	}

	@Override
	public UnitaMisura caricaUnitaMisura(UnitaMisura unitaMisura) {
		logger.debug("--> Enter caricaUnitaMisura");
		start("caricaUnitaMisura");
		UnitaMisura unitaMisuraCaricata = null;
		try {
			unitaMisuraCaricata = anagraficaTabelleService.caricaUnitaMisura(unitaMisura);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaUnitaMisura");
		}
		logger.debug("--> Exit caricaUnitaMisura ");
		return unitaMisuraCaricata;
	}

	@Override
	public List<ZonaGeografica> caricaZoneGeografiche(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaZoneGeografiche");
		start("caricaZoneGeografiche");
		List<ZonaGeografica> list = new ArrayList<ZonaGeografica>();
		try {
			list = anagraficaTabelleService.caricaZoneGeografiche(fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaZoneGeografiche");
		}
		logger.debug("--> Exit caricaZoneGeografiche ");
		return list;
	}

	@Override
	public void rimuovContrattoDaDocumento(Documento documento) {
		logger.debug("--> Enter rimuovContrattoDaDocumento");
		start("rimuovContrattoDaDocumento");
		try {
			anagraficaTabelleService.rimuovContrattoDaDocumento(documento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("rimuovContrattoDaDocumento");
		}
		logger.debug("--> Exit rimuovContrattoDaDocumento");
	}

	@Override
	public Carica salvaCarica(Carica carica) {
		logger.debug("--> Enter salvaCarica");
		start("salvaCarica");
		Carica caricaSave = null;
		try {
			caricaSave = anagraficaTabelleService.salvaCarica(carica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCarica");
		}
		logger.debug("--> Exit salvaCarica ");
		return caricaSave;
	}

	@Override
	public CodiceIva salvaCodiceIva(CodiceIva codiceIva) {
		logger.debug("--> Enter salvaCodiceIva");
		start("salvaCodiceIva");
		try {
			codiceIva = anagraficaTabelleService.salvaCodiceIva(codiceIva);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCodiceIva");
		}
		logger.debug("--> Exit salvaCodiceIva. Entità salvata " + codiceIva);
		return codiceIva;
	}

	@Override
	public ContrattoSpesometro salvaContratto(ContrattoSpesometro contratto) {
		logger.debug("--> Enter salvaContratto");
		start("salvaContratto");
		ContrattoSpesometro contrattoSpesometro = null;
		try {
			contrattoSpesometro = anagraficaTabelleService.salvaContratto(contratto);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaContratto");
		}
		logger.debug("--> Exit salvaContratto");
		return contrattoSpesometro;
	}

	@Override
	public ConversioneUnitaMisura salvaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura) {
		logger.debug("--> Enter salvaConversioneUnitaMisura");
		start("salvaConversioneUnitaMisura");
		ConversioneUnitaMisura conversioneUnitaMisuraSalvata = null;
		try {
			conversioneUnitaMisuraSalvata = anagraficaTabelleService
					.salvaConversioneUnitaMisura(conversioneUnitaMisura);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaConversioneUnitaMisura");
		}
		logger.debug("--> Exit salvaConversioneUnitaMisura ");
		return conversioneUnitaMisuraSalvata;
	}

	@Override
	public FormaGiuridica salvaFormaGiuridica(FormaGiuridica formaGiuridica) {
		logger.debug("--> Enter salvaFormaGiuridica");
		start("salvaFormaGiuridica");
		FormaGiuridica formaGiuridicaSave = null;
		try {
			formaGiuridicaSave = anagraficaTabelleService.salvaFormaGiuridica(formaGiuridica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaFormaGiuridica");
		}
		logger.debug("--> Exit salvaFormaGiuridica ");
		return formaGiuridicaSave;
	}

	@Override
	public Lingua salvaLingua(Lingua lingua) {
		logger.debug("--> Enter salvaLingua");
		start("salvaLingua");
		try {
			return anagraficaTabelleService.salvaLingua(lingua);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaLingua");
		}
		logger.debug("--> Exit salvaLingua ");
		return null;
	}

	@Override
	public Mansione salvaMansione(Mansione mansione) {
		logger.debug("--> Enter salvaMansione");
		start("salvaMansione");
		Mansione mansioneSave = null;
		try {
			mansioneSave = anagraficaTabelleService.salvaMansione(mansione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaMansione");
		}
		logger.debug("--> Exit salvaMansione ");
		return mansioneSave;
	}

	@Override
	public NotaAnagrafica salvaNotaAnagrafica(NotaAnagrafica notaAnagrafica) {
		logger.debug("--> Enter salvaNotaAnagrafica");
		start("salvaNotaAnagrafica");
		NotaAnagrafica nota = null;
		try {
			nota = anagraficaTabelleService.salvaNotaAnagrafica(notaAnagrafica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaNotaAnagrafica");
		}
		logger.debug("--> Exit salvaNotaAnagrafica ");
		return nota;
	}

	@Override
	public TipoDeposito salvaTipoDeposito(TipoDeposito tipoDeposito) {
		logger.debug("--> Enter salvaTipoDeposito");
		start("salvaTipoDeposito");
		TipoDeposito tipoDepositoSalvata = null;
		try {
			tipoDepositoSalvata = anagraficaTabelleService.salvaTipoDeposito(tipoDeposito);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaTipoDeposito");
		}
		logger.debug("--> Exit salvaZonaGeografica");
		return tipoDepositoSalvata;
	}

	@Override
	public TipoSedeEntita salvaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) {
		logger.debug("--> Enter salvaTipoSedeEntita");
		start("salvaTipoSedeEntita");
		TipoSedeEntita tipoSedeEntitaSave = null;
		try {
			tipoSedeEntitaSave = anagraficaTabelleService.salvaTipoSedeEntita(tipoSedeEntita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaTipoSedeEntita");
		}
		logger.debug("--> Exit salvaTipoSedeEntita ");
		return tipoSedeEntitaSave;
	}

	@Override
	public UnitaMisura salvaUnitaMisura(UnitaMisura unitaMisura) {
		logger.debug("--> Enter salvaUnitaMisura");
		start("salvaUnitaMisura");
		UnitaMisura unitaMisuraSalvata = null;
		try {
			unitaMisuraSalvata = anagraficaTabelleService.salvaUnitaMisura(unitaMisura);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaUnitaMisura");
		}
		logger.debug("--> Exit salvaUnitaMisura ");
		return unitaMisuraSalvata;
	}

	@Override
	public ZonaGeografica salvaZonaGeografica(ZonaGeografica zonaGeografica) {
		logger.debug("--> Enter salvaZonaGeografica");
		start("salvaZonaGeografica");
		ZonaGeografica zonaGeograficaSalvata = null;
		try {
			zonaGeograficaSalvata = anagraficaTabelleService.salvaZonaGeografica(zonaGeografica);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaZonaGeografica");
		}
		logger.debug("--> Exit salvaZonaGeografica");
		return zonaGeograficaSalvata;
	}

	/**
	 *
	 * @param anagraficaTabelleService
	 *            the anagraficaTabelleService to set
	 */
	public void setAnagraficaTabelleService(AnagraficaTabelleService anagraficaTabelleService) {
		this.anagraficaTabelleService = anagraficaTabelleService;
	}

}
