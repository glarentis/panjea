package it.eurotn.panjea.aton.domain;

public class DatiEsportazioneContratti2 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0011";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti2();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("tutti-categoria cliente");
		chiaveCondizRiga.setNumeroCampi(1);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("T_CLIENTI");
		chiaveCondizRiga.setNomeCampo1("CodiceGruppo");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti1.TIPORECORD);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceCategoriaCliente();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
