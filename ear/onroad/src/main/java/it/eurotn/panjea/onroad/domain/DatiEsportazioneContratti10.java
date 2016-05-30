package it.eurotn.panjea.onroad.domain;

public class DatiEsportazioneContratti10 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0005";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti10();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("articolo-nessuno");
		chiaveCondizRiga.setNumeroCampi(1);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("ARTICO");
		chiaveCondizRiga.setNomeCampo1("CodiceArticolo");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti9.TIPORECORD);
		return null;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceArticolo();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
