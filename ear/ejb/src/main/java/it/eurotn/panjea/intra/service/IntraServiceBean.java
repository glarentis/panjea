package it.eurotn.panjea.intra.service;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.GruppoCondizioneConsegna;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.domain.NaturaTransazione;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.domain.TotaliDichiarazione;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;
import it.eurotn.panjea.intra.manager.esportazione.interfaces.FileDichiarazioneManager;
import it.eurotn.panjea.intra.manager.interfaces.AreaIntraManager;
import it.eurotn.panjea.intra.manager.interfaces.DichiarazioneIntraManager;
import it.eurotn.panjea.intra.manager.interfaces.IntraSettingsManager;
import it.eurotn.panjea.intra.manager.interfaces.MagazzinoIntraManager;
import it.eurotn.panjea.intra.manager.interfaces.ServizioManager;
import it.eurotn.panjea.intra.service.interfaces.IntraService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.IntraService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.IntraService")
public class IntraServiceBean implements IntraService {

	@EJB
	private FileDichiarazioneManager fileDichiarazioneManager;

	@EJB
	private ServizioManager servizioManager;

	@EJB
	private MagazzinoIntraManager magazzinoIntraManager;

	@EJB
	private AreaIntraManager areaIntraManager;

	@EJB
	private DichiarazioneIntraManager dichiarazioneIntraManager;

	@EJB
	private IntraSettingsManager intraSettingsManager;

	@Override
	public FileDichiarazione aggiornaNomeFileDichiarazione(int idFileDichiarazione, String nome) {
		return fileDichiarazioneManager.aggiornaNomeFileDichiarazione(idFileDichiarazione, nome);
	}

	@Override
	public String associaNomenclatura(byte[] file) {
		return servizioManager.associaNomenclatura(file);
	}

	@Override
	public TotaliDichiarazione calcolaTotaliDichiarazione(Integer id) {
		return dichiarazioneIntraManager.calcolaTotaliDichiarazione(id);
	}

	@Override
	public void cancellaAreaIntra(AreaIntra areaIntra) {
		areaIntraManager.cancellaAreaIntra(areaIntra);
	}

	@Override
	public void cancellaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		dichiarazioneIntraManager.cancellaDichiarazioneIntra(dichiarazioneIntra);
	}

	@Override
	public void cancellaFileDichiarazioni(int id) {
		FileDichiarazione fileDichiarazione = new FileDichiarazione();
		fileDichiarazione.setId(id);
		fileDichiarazioneManager.cancellaFileDichiarazioni(fileDichiarazione);
	}

	@Override
	public void cancellaRigaSezioneDichiarazione(RigaSezioneIntra rigaSezioneIntra) {
		dichiarazioneIntraManager.cancellaRigaSezioneDichiarazione(rigaSezioneIntra);
	}

	@Override
	public void cancellaServizio(Servizio servizio) {
		servizioManager.cancellaServizio(servizio);
	}

	@Override
	public AreaIntra caricaAreaIntraByDocumento(Documento documento) {
		return areaIntraManager.caricaAreaIntraByDocumento(documento);
	}

	@Override
	public DichiarazioneIntra caricaDichiarazioneIntra(int id) {
		return dichiarazioneIntraManager.caricaDichiarazioneIntra(id);
	}

	@Override
	public List<DichiarazioneIntra> caricaDichiarazioniIntra() {
		return dichiarazioneIntraManager.caricaDichiarazioniIntra();
	}

	@Override
	public List<DichiarazioneIntra> caricaDichiarazioniIntraDaPresentare() {
		return dichiarazioneIntraManager.caricaDichiarazioniIntraDaPresentare();
	}

	@Override
	public List<Documento> caricaDocumentiSenzaIntra(ParametriRicercaAreaIntra parametri) {
		return areaIntraManager.caricaDocumentiSenzaIntra(parametri);
	}

	@Override
	public List<FileDichiarazione> caricaFileDichiarazioni() {
		return fileDichiarazioneManager.caricaFileDichiarazioni();
	}

	@Override
	public List<GruppoCondizioneConsegna> caricaGruppiCondizioneConsegna() {
		return magazzinoIntraManager.caricaGruppiCondizioneConsegna();
	}

	@Override
	public IntraSettings caricaIntraSettings() {
		return intraSettingsManager.caricaIntraSettings();
	}

	@Override
	public List<NaturaTransazione> caricaNatureTransazione() {
		return magazzinoIntraManager.caricaNatureTransazione();
	}

	@Override
	public <T extends RigaSezioneIntra> List<T> caricaRigheSezioniDichiarazione(DichiarazioneIntra dichiarazioneIntra,
			Class<T> classeSezione) {
		return dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazioneIntra, classeSezione);
	}

	@Override
	public List<?> caricaServizi(Class<?> classServizio, String fieldSearch, String filtro) {
		return servizioManager.caricaServizi(classServizio, fieldSearch, filtro);
	}

	@Override
	public Servizio caricaServizio(Servizio servizio) {
		return servizioManager.caricaServizio(servizio);
	}

	@Override
	public DichiarazioneIntra compilaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		return dichiarazioneIntraManager.compilaDichiarazioneIntra(dichiarazioneIntra);
	}

	@Override
	public DichiarazioneIntra creaDichiarazioneIntra(TipoDichiarazione dichiarazione) {
		return dichiarazioneIntraManager.creaDichiarazioneIntra(dichiarazione);
	}

	@Override
	public void generaAreeIntra(List<Integer> documenti) {
		magazzinoIntraManager.generaAreeIntra(documenti);
	}

	@Override
	public FileDichiarazione generaFileEsportazione(List<Integer> dichiarazioni, boolean salvaRisultati)
			throws PreferenceNotFoundException {
		return fileDichiarazioneManager.generaFileEsportazione(dichiarazioni, salvaRisultati);
	}

	@Override
	@TransactionTimeout(1800)
	public void importaNomenclatura(byte[] file) {
		servizioManager.importaNomenclatura(file);
	}

	@Override
	@TransactionTimeout(1800)
	public void importaServizi(byte[] file) {
		servizioManager.importaServizi(file);
	}

	@Override
	public List<AreaContabile> ricercaAreeContabiliConIntra(ParametriRicercaAreaIntra parametri) {
		return areaIntraManager.ricercaAreeContabiliConIntra(parametri);
	}

	@Override
	public AreaIntra salvaAreaIntra(AreaIntra areaIntra) {
		return areaIntraManager.salvaAreaIntra(areaIntra);
	}

	@Override
	public DichiarazioneIntra salvaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		return dichiarazioneIntraManager.salvaDichiarazioneIntra(dichiarazioneIntra);
	}

	@Override
	public IntraSettings salvaIntraSettings(IntraSettings intraSettingsToSave) {
		return intraSettingsManager.salvaIntraSettings(intraSettingsToSave);
	}

	@Override
	public RigaSezioneIntra salvaRigaSezioneDichiarazione(RigaSezioneIntra riga) {
		return dichiarazioneIntraManager.salvaRigaSezioneDichiarazione(riga);
	}

	@Override
	public Servizio salvaServizio(Servizio servizio) {
		return servizioManager.salvaServizio(servizio);
	}

	@Override
	public void spedisciFileEsportazione(int id) {
		FileDichiarazione fileDichiarazione = new FileDichiarazione();
		fileDichiarazione.setId(id);
		fileDichiarazioneManager.spedisciFileEsportazione(fileDichiarazione);
	}

	@Override
	public void spedisciFileEsportazione(List<Integer> dichiarazioni) throws PreferenceNotFoundException {
		fileDichiarazioneManager.spedisciFileEsportazione(dichiarazioni);
	}

}
