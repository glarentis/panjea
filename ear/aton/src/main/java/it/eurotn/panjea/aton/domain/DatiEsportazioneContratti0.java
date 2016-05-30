package it.eurotn.panjea.aton.domain;

public class DatiEsportazioneContratti0 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0015";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti0();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("tutti-nessuno");
		chiaveCondizRiga.setNumeroCampi(1);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("T_ARTICOL");
		chiaveCondizRiga.setNomeCampo1("Attributo1");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		return null;
	}

	@Override
	public String getCodiceEsportazione() {
		return "*         ";
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
