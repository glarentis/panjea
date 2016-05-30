package it.eurotn.panjea.aton.domain;

public class DatiEsportazioneContratti1 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0012";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti1();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("tutti-tutti");
		chiaveCondizRiga.setNumeroCampi(1);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("T_CLIENTI");
		chiaveCondizRiga.setNomeCampo1("Campo1");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return "*              ";
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
