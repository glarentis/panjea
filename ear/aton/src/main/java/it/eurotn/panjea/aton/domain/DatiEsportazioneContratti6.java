package it.eurotn.panjea.aton.domain;

public class DatiEsportazioneContratti6 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0008";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti6();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("categoria articolo-tutti");
		chiaveCondizRiga.setNumeroCampi(2);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("T_ARTICOL");
		chiaveCondizRiga.setNomeCampo1("Attributo2");

		chiaveCondizRiga.setNomeTabella2("T_CLIENTI");
		chiaveCondizRiga.setNomeCampo2("Campo1");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti4.TIPORECORD);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceCategoriaArticolo() + "*              ";
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
