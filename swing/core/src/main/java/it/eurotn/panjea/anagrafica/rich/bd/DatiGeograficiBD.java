package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa.NumeroLivelloAmministrativo;
import it.eurotn.panjea.anagrafica.service.interfaces.DatiGeograficiService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DatiGeograficiBD extends AbstractBaseBD implements IDatiGeograficiBD {

	public static final String BEAN_ID = "datiGeograficiBD";

	private static Logger logger = Logger.getLogger(DatiGeograficiBD.class);

	private DatiGeograficiService datiGeograficiService;

	@Override
	public void cancellaCap(Cap cap) {
		logger.debug("--> Enter cancellaCap");
		start("cancellaCap");
		try {
			datiGeograficiService.cancellaCap(cap);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCap");
		}
		logger.debug("--> Exit cancellaCap ");
	}

	// @Override
	// public void cancellaLivelloAmministrativo1(LivelloAmministrativo1 livelloAmministrativo1) {
	// logger.debug("--> Enter cancellaLivelloAmministrativo1");
	// start("cancellaLivelloAmministrativo1");
	// try {
	// datiGeograficiService.cancellaLivelloAmministrativo1(livelloAmministrativo1);
	// } catch (Exception e) {
	// PanjeaSwingUtil.checkAndThrowException(e);
	// } finally {
	// end("cancellaLivelloAmministrativo1");
	// }
	// logger.debug("--> Exit cancellaLivelloAmministrativo1 ");
	// }
	//
	// @Override
	// public void cancellaLivelloAmministrativo2(LivelloAmministrativo2 livelloAmministrativo2) {
	// logger.debug("--> Enter cancellaLivelloAmministrativo2");
	// start("cancellaLivelloAmministrativo2");
	// try {
	// datiGeograficiService.cancellaLivelloAmministrativo2(livelloAmministrativo2);
	// } catch (Exception e) {
	// PanjeaSwingUtil.checkAndThrowException(e);
	// } finally {
	// end("cancellaLivelloAmministrativo2");
	// }
	// logger.debug("--> Exit cancellaLivelloAmministrativo2 ");
	// }
	//
	// @Override
	// public void cancellaLivelloAmministrativo3(LivelloAmministrativo3 livelloAmministrativo3) {
	// logger.debug("--> Enter cancellaLivelloAmministrativo3");
	// start("cancellaLivelloAmministrativo3");
	// try {
	// datiGeograficiService.cancellaLivelloAmministrativo3(livelloAmministrativo3);
	// } catch (Exception e) {
	// PanjeaSwingUtil.checkAndThrowException(e);
	// } finally {
	// end("cancellaLivelloAmministrativo3");
	// }
	// logger.debug("--> Exit cancellaLivelloAmministrativo3 ");
	// }
	//
	// @Override
	// public void cancellaLivelloAmministrativo4(LivelloAmministrativo4 livelloAmministrativo4) {
	// logger.debug("--> Enter cancellaLivelloAmministrativo4");
	// start("cancellaLivelloAmministrativo4");
	// try {
	// datiGeograficiService.cancellaLivelloAmministrativo4(livelloAmministrativo4);
	// } catch (Exception e) {
	// PanjeaSwingUtil.checkAndThrowException(e);
	// } finally {
	// end("cancellaLivelloAmministrativo4");
	// }
	// logger.debug("--> Exit cancellaLivelloAmministrativo4 ");
	// }

	@Override
	public void cancellaLocalita(Localita localita) {
		logger.debug("--> Enter cancellaLocalita");
		start("cancellaLocalita");
		try {
			datiGeograficiService.cancellaLocalita(localita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaLocalita");
		}
		logger.debug("--> Exit cancellaLocalita");
	}

	@Override
	public void cancellaNazione(Nazione nazione) {
		logger.debug("--> Enter cancellaNazione");
		start("cancellaNazione");
		try {
			datiGeograficiService.cancellaNazione(nazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaNazione");
		}
		logger.debug("--> Exit cancellaNazione");
	}

	@Override
	public void cancellaSuddivisioneAmministrativa(SuddivisioneAmministrativa suddivisioneAmministrativa) {
		logger.debug("--> Enter cancellaSuddivisioneAmministrativa");
		start("cancellaSuddivisioneAmministrativa");
		try {
			datiGeograficiService.cancellaSuddivisioneAmministrativa(suddivisioneAmministrativa);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaSuddivisioneAmministrativa");
		}
		logger.debug("--> Exit cancellaSuddivisioneAmministrativa");
	}

	@Override
	public List<Cap> caricaCap(DatiGeografici datiGeografici) {
		logger.debug("--> Enter caricaCap");
		start("caricaCap");
		List<Cap> caps = new ArrayList<Cap>();
		try {
			caps = datiGeograficiService.caricaCap(datiGeografici);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCap");
		}
		logger.debug("--> Exit caricaCap");
		return caps;
	}

	@Override
	public Cap caricaCap(Integer idCap) {
		logger.debug("--> Enter caricaCap");
		start("caricaCap");
		Cap cap = null;
		try {
			cap = datiGeograficiService.caricaCap(idCap);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCap");
		}
		logger.debug("--> Exit caricaCap");
		return cap;
	}

	@Override
	public List<LivelloAmministrativo1> caricaLivelloAmministrativo1(DatiGeografici datiGeografici) {
		logger.debug("--> Enter caricaLivelloAmministrativo1");
		start("caricaLivelloAmministrativo1");
		List<LivelloAmministrativo1> livelloAmministrativo = new ArrayList<LivelloAmministrativo1>();
		try {
			livelloAmministrativo = datiGeograficiService.caricaLivelloAmministrativo1(datiGeografici);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLivelloAmministrativo1");
		}
		logger.debug("--> Exit caricaLivelloAmministrativo1");
		return livelloAmministrativo;
	}

	@Override
	public List<LivelloAmministrativo2> caricaLivelloAmministrativo2(DatiGeografici datiGeografici) {
		logger.debug("--> Enter caricaLivelloAmministrativo2");
		start("caricaLivelloAmministrativo1");
		List<LivelloAmministrativo2> livelloAmministrativo = new ArrayList<LivelloAmministrativo2>();
		try {
			livelloAmministrativo = datiGeograficiService.caricaLivelloAmministrativo2(datiGeografici);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLivelloAmministrativo2");
		}
		logger.debug("--> Exit caricaLivelloAmministrativo2");
		return livelloAmministrativo;
	}

	@Override
	public List<LivelloAmministrativo3> caricaLivelloAmministrativo3(DatiGeografici datiGeografici) {
		logger.debug("--> Enter caricaLivelloAmministrativo3");
		start("caricaLivelloAmministrativo3");
		List<LivelloAmministrativo3> livelloAmministrativo = new ArrayList<LivelloAmministrativo3>();
		try {
			livelloAmministrativo = datiGeograficiService.caricaLivelloAmministrativo3(datiGeografici);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLivelloAmministrativo3");
		}
		logger.debug("--> Exit caricaLivelloAmministrativo3");
		return livelloAmministrativo;
	}

	@Override
	public List<LivelloAmministrativo4> caricaLivelloAmministrativo4(DatiGeografici datiGeografici) {
		logger.debug("--> Enter caricaLivelloAmministrativo4");
		start("caricaLivelloAmministrativo4");
		List<LivelloAmministrativo4> livelloAmministrativo = new ArrayList<LivelloAmministrativo4>();
		try {
			livelloAmministrativo = datiGeograficiService.caricaLivelloAmministrativo4(datiGeografici);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLivelloAmministrativo4");
		}
		logger.debug("--> Exit caricaLivelloAmministrativo4");
		return livelloAmministrativo;
	}

	@Override
	public List<Localita> caricaLocalita(DatiGeografici datiGeografici) {
		logger.debug("--> Enter caricaLocalita");
		start("caricaLocalita");
		List<Localita> localitas = new ArrayList<Localita>();
		try {
			localitas = datiGeograficiService.caricaLocalita(datiGeografici);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLocalita");
		}
		logger.debug("--> Exit caricaLocalita");
		return localitas;
	}

	@Override
	public Localita caricaLocalita(Integer idLocalita) {
		logger.debug("--> Enter caricaLocalita");
		start("caricaLocalita");
		Localita localita = null;
		try {
			localita = datiGeograficiService.caricaLocalita(idLocalita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLocalita");
		}
		logger.debug("--> Exit caricaLocalita");
		return localita;
	}

	@Override
	public List<Nazione> caricaNazioni(String codice) {
		logger.debug("--> Enter caricaNazioni");
		start("caricaNazioni");
		List<Nazione> nazioni = new ArrayList<Nazione>();
		try {
			nazioni = datiGeograficiService.caricaNazioni(codice);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaNazioni");
		}
		logger.debug("--> Exit caricaNazioni");
		return nazioni;
	}

	@Override
	public List<SuddivisioneAmministrativa> caricaSuddivisioniAmministrative(DatiGeografici datiGeografici,
			NumeroLivelloAmministrativo lvl, String filtro) {
		logger.debug("--> Enter caricaSuddivisioniAmministrative");
		start("caricaSuddivisioniAmministrative");
		List<SuddivisioneAmministrativa> livelloAmministrativo = new ArrayList<SuddivisioneAmministrativa>();
		try {
			livelloAmministrativo = datiGeograficiService.caricaSuddivisioniAmministrative(datiGeografici, lvl, filtro);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSuddivisioniAmministrative");
		}
		logger.debug("--> Exit caricaSuddivisioniAmministrative");
		return livelloAmministrativo;
	}

	/**
	 * @return the datiGeograficiService
	 */
	public DatiGeograficiService getDatiGeograficiService() {
		return datiGeograficiService;
	}

	@Override
	public Cap salvaCap(Cap cap) {
		logger.debug("--> Enter salvaCap");
		start("salvaCap");
		Cap capSalvato = null;
		try {
			capSalvato = datiGeograficiService.salvaCap(cap);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCap");
		}
		logger.debug("--> Exit salvaCap");
		return capSalvato;
	}

	@Override
	public Cap salvaCap(Cap cap, List<Localita> listLocalita) {
		logger.debug("--> Enter salvaCap");
		start("salvaCap");
		Cap capSalvato = null;
		try {
			capSalvato = datiGeograficiService.salvaCap(cap, listLocalita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCap");
		}
		logger.debug("--> Exit salvaCap");
		return capSalvato;
	}

	@Override
	public Cap salvaCap(Cap cap, List<Localita> localitaAggiunte, List<Localita> localitaRimosse) {
		logger.debug("--> Enter salvaCap");
		start("salvaCap");
		Cap capSalvato = null;
		try {
			capSalvato = datiGeograficiService.salvaCap(cap, localitaAggiunte, localitaRimosse);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCap");
		}
		logger.debug("--> Exit salvaCap");
		return capSalvato;
	}

	// @Override
	// public LivelloAmministrativo1 salvaLivelloAmministrativo1(LivelloAmministrativo1 livelloAmministrativo1) {
	// logger.debug("--> Enter salvaLivelloAmministrativo1");
	// start("salvaLivelloAmministrativo1");
	// LivelloAmministrativo1 livelloAmministrativoSalvato = null;
	// try {
	// livelloAmministrativoSalvato = datiGeograficiService.salvaLivelloAmministrativo1(livelloAmministrativo1);
	// } catch (Exception e) {
	// PanjeaSwingUtil.checkAndThrowException(e);
	// } finally {
	// end("salvaLivelloAmministrativo1");
	// }
	// logger.debug("--> Exit salvaLivelloAmministrativo1");
	// return livelloAmministrativoSalvato;
	// }
	//
	// @Override
	// public LivelloAmministrativo2 salvaLivelloAmministrativo2(LivelloAmministrativo2 livelloAmministrativo2) {
	// logger.debug("--> Enter salvaLivelloAmministrativo2");
	// start("salvaLivelloAmministrativo2");
	// LivelloAmministrativo2 livelloAmministrativoSalvato = null;
	// try {
	// livelloAmministrativoSalvato = datiGeograficiService.salvaLivelloAmministrativo2(livelloAmministrativo2);
	// } catch (Exception e) {
	// PanjeaSwingUtil.checkAndThrowException(e);
	// } finally {
	// end("salvaLivelloAmministrativo2");
	// }
	// logger.debug("--> Exit salvaLivelloAmministrativo2");
	// return livelloAmministrativoSalvato;
	// }
	//
	// @Override
	// public LivelloAmministrativo3 salvaLivelloAmministrativo3(LivelloAmministrativo3 livelloAmministrativo3) {
	// logger.debug("--> Enter salvaLivelloAmministrativo3");
	// start("salvaLivelloAmministrativo3");
	// LivelloAmministrativo3 livelloAmministrativoSalvato = null;
	// try {
	// livelloAmministrativoSalvato = datiGeograficiService.salvaLivelloAmministrativo3(livelloAmministrativo3);
	// } catch (Exception e) {
	// PanjeaSwingUtil.checkAndThrowException(e);
	// } finally {
	// end("salvaLivelloAmministrativo3");
	// }
	// logger.debug("--> Exit salvaLivelloAmministrativo3");
	// return livelloAmministrativoSalvato;
	// }
	//
	// @Override
	// public LivelloAmministrativo4 salvaLivelloAmministrativo4(LivelloAmministrativo4 livelloAmministrativo4) {
	// logger.debug("--> Enter salvaLivelloAmministrativo4");
	// start("salvaLivelloAmministrativo4");
	// LivelloAmministrativo4 livelloAmministrativoSalvato = null;
	// try {
	// livelloAmministrativoSalvato = datiGeograficiService.salvaLivelloAmministrativo4(livelloAmministrativo4);
	// } catch (Exception e) {
	// PanjeaSwingUtil.checkAndThrowException(e);
	// } finally {
	// end("salvaLivelloAmministrativo4");
	// }
	// logger.debug("--> Exit salvaLivelloAmministrativo4");
	// return livelloAmministrativoSalvato;
	// }

	@Override
	public Localita salvaLocalita(Localita localita) {
		logger.debug("--> Enter salvaLocalita");
		start("salvaLocalita");
		Localita localitaSalvata = null;
		try {
			localitaSalvata = datiGeograficiService.salvaLocalita(localita);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaLocalita");
		}
		logger.debug("--> Exit salvaLocalita");
		return localitaSalvata;
	}

	@Override
	public Localita salvaLocalita(Localita localita, List<Cap> caps) {
		logger.debug("--> Enter salvaLocalita");
		start("salvaLocalita");
		Localita localitaSalvata = null;
		try {
			localitaSalvata = datiGeograficiService.salvaLocalita(localita, caps);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaLocalita");
		}
		logger.debug("--> Exit salvaLocalita");
		return localitaSalvata;
	}

	@Override
	public Localita salvaLocalita(Localita localita, List<Cap> capsAggiunti, List<Cap> capsRimossi) {
		logger.debug("--> Enter salvaLocalita");
		start("salvaLocalita");
		Localita localitaSalvata = null;
		try {
			localitaSalvata = datiGeograficiService.salvaLocalita(localita, capsAggiunti, capsRimossi);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaLocalita");
		}
		logger.debug("--> Exit salvaLocalita");
		return localitaSalvata;
	}

	@Override
	public Nazione salvaNazione(Nazione nazione) {
		logger.debug("--> Enter salvaNazione");
		start("salvaNazione");
		Nazione nazioneSalvata = null;
		try {
			nazioneSalvata = datiGeograficiService.salvaNazione(nazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaNazione");
		}
		logger.debug("--> Exit salvaNazione");
		return nazioneSalvata;
	}

	@Override
	public SuddivisioneAmministrativa salvaSuddivisioneAmministrativa(
			SuddivisioneAmministrativa suddivisioneAmministrativa) {
		logger.debug("--> Enter salvaSuddivisioneAmministrativa");
		start("salvaSuddivisioneAmministrativa");
		SuddivisioneAmministrativa suddivisioneAmministrativaSalvata = null;
		try {
			suddivisioneAmministrativaSalvata = datiGeograficiService
					.salvaSuddivisioneAmministrativa(suddivisioneAmministrativa);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaSuddivisioneAmministrativa");
		}
		logger.debug("--> Exit salvaSuddivisioneAmministrativa");
		return suddivisioneAmministrativaSalvata;
	}

	/**
	 * @param datiGeograficiService
	 *            the datiGeograficiService to set
	 */
	public void setDatiGeograficiService(DatiGeograficiService datiGeograficiService) {
		this.datiGeograficiService = datiGeograficiService;
	}

}
