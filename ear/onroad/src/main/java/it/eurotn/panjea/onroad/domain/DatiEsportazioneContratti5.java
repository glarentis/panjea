package it.eurotn.panjea.onroad.domain;

public class DatiEsportazioneContratti5 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0010";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti5();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("categoria articolo-nessuno");
		chiaveCondizRiga.setNumeroCampi(1);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("ARTICO");
		chiaveCondizRiga.setNomeCampo1("Campo1");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti4.TIPORECORD);
		return null;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceCategoriaArticolo();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
