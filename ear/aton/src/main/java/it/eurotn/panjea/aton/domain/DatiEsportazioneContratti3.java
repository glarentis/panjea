package it.eurotn.panjea.aton.domain;

public class DatiEsportazioneContratti3 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0010";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti3();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("tutti-cliente");
		chiaveCondizRiga.setNumeroCampi(1);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("T_CLIENTI");
		chiaveCondizRiga.setNomeCampo1("CodiceCliente");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti2.TIPORECORD);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceCliente();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
