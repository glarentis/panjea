package it.eurotn.panjea.pagamenti.rich.rules;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.rich.tabelle.SimulazioneRateTablePage.ParametriSimulazioneRatePM;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.rich.tabelle.righestruttura.ParametriGeneraRigheStrutturaPartite;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rich.rules.ConfrontoDateConstraint;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.pm.ParametriCreazioneAreaEffetti;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAnticipi;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneInsoluti;
import it.eurotn.panjea.tesoreria.util.ParametriCreazionePagamento;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaSituazioneRata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.And;
import org.springframework.rules.constraint.GreaterThan;
import org.springframework.rules.constraint.LessThanEqualTo;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

public class PagamentiPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * Regole di validazione per {@link AreaAcconto}.
	 * 
	 * @return regole create
	 */
	private Rules createAreaAccontoRules() {
		return new Rules(AreaAcconto.class) {
			@Override
			protected void initRules() {
				add("documento.dataDocumento", getRequiredConstraint());
				add("documento.totale.codiceValuta", getRequiredConstraint());
				add(getAreaAccontoEntitaConstraint());
				add(getAreaAccontoSedeEntitaConstraint());
				add(getAreaAccontoRapportoBancarioConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link AreaPagamenti}.
	 * 
	 * @return regole create
	 */
	private Rules createAreaPagamentiRules() {
		return new Rules(AreaPagamenti.class) {
			@Override
			protected void initRules() {
				add("documento.dataDocumento", getRequiredConstraint());
				add("documento.codice.codice", getRequiredConstraint());
				add("speseIncasso", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link AreaTesoreria}.
	 * 
	 * @return regole create
	 */
	private Rules createAreaTesoreriaRules() {
		return new Rules(AreaTesoreria.class) {

			@Override
			protected void initRules() {
				add("tipoAreaPartita", getDomainAttributeRequiredConstraint());
			}

		};
	}

	/**
	 * Regole di validazione per {@link CalendarioRate}.
	 * 
	 * @return regole create
	 */
	private Rules createCalendarioRateRules() {
		return new Rules(CalendarioRate.class) {
			@Override
			protected void initRules() {
				add("descrizione", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link CategoriaRata}.
	 * 
	 * @return regole create
	 */
	private Rules createCategoriaRataRules() {
		return new Rules(CategoriaRata.class) {
			@Override
			protected void initRules() {
				add("descrizione", getRequiredConstraint());
				add("tipoCategoria", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link CodicePagamento}.
	 * 
	 * @return regole create
	 */
	private Rules createCodicePagamentoRules() {
		return new Rules(CodicePagamento.class) {
			@Override
			protected void initRules() {
				add("codicePagamento", getRequiredConstraint());
				add("codicePagamento", getMaxCharConstraint(10));
				add("descrizione", getRequiredConstraint());
				add("descrizione", getMaxCharConstraint(60));
				add("tipologiaPartita", getRequiredConstraint());
				add(getGiorniLimiteCodicePagamentoConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriCreazioneAreaEffetti}.
	 * 
	 * @return regole create
	 */
	private Rules createDistintaBancariaPMRules() {
		return new Rules(ParametriCreazioneAreaEffetti.class) {
			@Override
			protected void initRules() {
				add("dataDocumento", getRequiredConstraint());
				add("numeroDocumento", getRequiredConstraint());
				add("spese", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriCreazioneAnticipi}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriCreazioneAnticipiRules() {
		return new Rules(ParametriCreazioneAnticipi.class) {
			@Override
			protected void initRules() {
				add("dataDocumento", getRequiredConstraint());
				add(getNumeroDocumentoAnticipoConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriCreazioneAreaChiusure}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriCreazioneAreaChiusureRules() {
		return new Rules(ParametriCreazioneAreaChiusure.class) {
			@Override
			protected void initRules() {
				add("dataDocumento", getRequiredConstraint());
				add("tipoAreaPartita", getDomainAttributeRequiredConstraint());
				add(getRapportoBancarioConstraint());
				add("numeroAssegno", getMaxCharConstraint(10));
				add("abi", getMaxCharConstraint(5));
				add("cab", getMaxCharConstraint(5));
				add("abi", getNumericConstraint());
				add("cab", getNumericConstraint());
				add("numeroAssegno", getNumericConstraint());
				add(getAbiAssegnoRequired());
				add(getCabAssegnoRequired());
				add(getNumeroAssegnoRequired());
				add("noteContabili", getMaxCharConstraint(250));
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriCreazioneInsoluti}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriCreazioneInsolutiRules() {
		return new Rules(ParametriCreazioneInsoluti.class) {
			@Override
			protected void initRules() {
				add("dataDocumento", getRequiredConstraint());
				add(getNumeroDocumentoInsolutoConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriCreazionePagamento}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriCreazionePagamentoRules() {
		return new Rules(ParametriCreazionePagamento.class) {
			@Override
			protected void initRules() {
				add("dataDocumento", getRequiredConstraint());
				add(getRapportoBancarioConstraint());
				add(getTipoAreaPartitePagamentoConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriGeneraRigheStrutturaPartite}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriGeneraRigheStrutturaPartiteRules() {
		return new Rules(ParametriGeneraRigheStrutturaPartite.class) {
			@Override
			protected void initRules() {
				add("numeroRate", getRequiredConstraint());
				add("numeroRate", lt(100));
				add("numeroRate", gt(0));
				add("intervallo", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriRicercaAreeTesoreria}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriRicercaAreeTesoreriaRules() {
		return new Rules(ParametriRicercaAreeTesoreria.class) {
			@Override
			protected void initRules() {
				add("periodo", getPeriodoConstraint(false));
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriRicercaAssegni}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriRicercaAssegniRules() {
		return new Rules(ParametriRicercaAssegni.class) {
			@Override
			protected void initRules() {
				add("dataDocumento", getPeriodoConstraint(false));
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriRicercaEffetti}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriRicercaEffettiRules() {
		return new Rules(ParametriRicercaEffetti.class) {
			@Override
			protected void initRules() {
				add(new ConfrontoDateConstraint("daDataValuta", LessThanEqualTo.instance(), "aDataValuta"));
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriRicercaRate}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriRicercaRateRules() {
		return new Rules(ParametriRicercaRate.class) {
			@Override
			protected void initRules() {
				add(getAnagraficaParametriRicercaRateRequired());
				add("dataScadenza", getPeriodoConstraint(false));
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriRicercaSituazioneRata}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriRicercaSituazioneRataRules() {
		return new Rules(ParametriRicercaSituazioneRata.class) {

			@Override
			protected void initRules() {
				add("dataIniziale", getRequiredConstraint());
				add("dataFinale", getRequiredConstraint());
				add(new ConfrontoDateConstraint("dataIniziale", LessThanEqualTo.instance(), "dataFinale"));
			}

		};
	}

	/**
	 * Regole di validazione per {@link Rata}.
	 * 
	 * @return regole create
	 */
	private Rules createRataPartitaRules() {
		return new Rules(Rata.class) {
			@Override
			protected void initRules() {
				add("numeroRata", getRequiredConstraint());
				add("dataScadenza", getRequiredConstraint());
				add("tipoPagamento", getRequiredConstraint());
				add("importo", getRequiredConstraint());
				add("note", getMaxCharConstraint(100));
			}
		};
	}

	/**
	 * Regole di validazione per {@link RigaCalendarioRate}.
	 * 
	 * @return regole create
	 */
	private Rules createRigaCalendarioRateRules() {
		return new Rules(RigaCalendarioRate.class) {
			@Override
			protected void initRules() {
				add("dataIniziale", getRequiredConstraint());
				add("dataFinale", getRequiredConstraint());
				add(new ConfrontoDateConstraint("dataIniziale", LessThanEqualTo.instance(), "dataFinale"));
				add(new ConfrontoDateConstraint("dataAlternativa", GreaterThan.instance(), "dataFinale"));
			}
		};
	}

	/**
	 * Regole di validazione per {@link CodicePagamento}.
	 * 
	 * @return regole create
	 */
	private Rules createRigaStrutturaPartiteRules() {
		return new Rules(RigaStrutturaPartite.class) {
			@Override
			protected void initRules() {
				add("numeroRata", getRequiredConstraint());
				add("numeroRata", getMaxCharConstraint(2));
				add("intervallo", getRequiredConstraint());
				add("intervallo", getMaxCharConstraint(10));
				add("primaPercentuale", getRequiredConstraint());
				add("secondaPercentuale", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link ParametriSimulazioneRatePM}.
	 * 
	 * @return regole create
	 */
	private Rules createSimulazioneRateRules() {
		return new Rules(ParametriSimulazioneRatePM.class) {
			@Override
			protected void initRules() {
				add("data", getRequiredConstraint());
				add("imposta", getRequiredConstraint());
				add("imponibile", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link Sollecito}.
	 * 
	 * @return regole create
	 */
	private Rules createSolleciti() {
		return new Rules(Sollecito.class) {

			@Override
			protected void initRules() {
				add("template", getRequiredConstraint());
				add("nota", getMaxCharConstraint(120));
			}

		};
	}

	/**
	 * Regole di validazione per {@link StrutturaPartita}.
	 * 
	 * @return regole create
	 */
	private Rules createStrutturaPartiteRules() {
		return new Rules(StrutturaPartita.class) {

			@Override
			protected void initRules() {
				add("descrizione", getRequiredConstraint());
				add("categoriaRata", getDomainAttributeRequiredConstraint());
				add("tipoPagamento", getRequiredConstraint());
				add("tipoStrategiaDataScadenza", getRequiredConstraint());
			}

		};
	}

	/**
	 * Regole di validazione per {@link TemplateSolleciti}.
	 * 
	 * @return regole create
	 */
	private Rules createTemplateSolleciti() {
		return new Rules(TemplateSolleciti.class) {

			@Override
			protected void initRules() {
				add("descrizione", getRequiredConstraint());
			}

		};
	}

	/**
	 * Regole di validazione per {@link TipoAreaPartita}.
	 * 
	 * @return regole create
	 */
	private Rules createTipoAreaPartiteRules() {
		return new Rules(TipoAreaPartita.class) {
			@Override
			protected void initRules() {
				add("descrizionePerFlusso", getMaxCharConstraint(15));
				add("tipoPartita", getRequiredConstraint());
				add("tipoOperazione", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione per {@link TipoDocumentoBasePartite}.
	 * 
	 * @return regole create
	 */
	private Rules createTipoDocumentoBasePartiteRules() {
		return new Rules(TipoDocumentoBasePartite.class) {
			@Override
			protected void initRules() {
				add("tipoAreaPartita", getDomainAttributeRequiredConstraint());
				add("tipoOperazione", getRequiredConstraint());
			}
		};
	}

	/**
	 * Regola di validazione per abi.
	 * 
	 * @return PropertyConstraint
	 */
	private PropertyConstraint getAbiAssegnoRequired() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoAreaPartita.tipoOperazione",
				eq(TipoOperazione.GESTIONE_ASSEGNO));
		PropertyConstraint numeroAssegnoRequiredIfTrue = new RequiredIfTrue("abi", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
				numeroAssegnoRequiredIfTrue);
		propertyResolvableConstraint.setType("abiAssegnoRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per l'anagrafica nei parametri di ricerca rate.
	 * 
	 * @return PropertyConstraint
	 */
	private PropertyConstraint getAnagraficaParametriRicercaRateRequired() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("compensazione", eq(Boolean.TRUE));
		PropertyConstraint anagraficaRequiredIfTrue = new RequiredIfTrue("anagrafica", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
				anagraficaRequiredIfTrue);
		propertyResolvableConstraint.setType("anagraficaRicercaRateRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per l'entità dell'area acconto.
	 * 
	 * @return regola creata
	 */
	private PropertyConstraint getAreaAccontoEntitaConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoEntita", new Or(
				eq(TipoEntita.CLIENTE), eq(TipoEntita.FORNITORE)));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("documento.entita", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("areaAccontoEntitaRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per il rapporto bancario dell'area acconto.
	 * 
	 * @return regola creata
	 */
	private PropertyConstraint getAreaAccontoRapportoBancarioConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoEntita", eq(TipoEntita.AZIENDA));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("rapportoBancarioAzienda", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("areaAccontoRapportoBancarioRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per l'entità dell'area acconto.
	 * 
	 * @return regola creata
	 */
	private PropertyConstraint getAreaAccontoSedeEntitaConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoEntita", new Or(
				eq(TipoEntita.CLIENTE), eq(TipoEntita.FORNITORE)));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("documento.sedeEntita", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("areaAccontoEntitaRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per cab.
	 * 
	 * @return PropertyConstraint
	 */
	private PropertyConstraint getCabAssegnoRequired() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoAreaPartita.tipoOperazione",
				eq(TipoOperazione.GESTIONE_ASSEGNO));
		PropertyConstraint numeroAssegnoRequiredIfTrue = new RequiredIfTrue("cab", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
				numeroAssegnoRequiredIfTrue);
		propertyResolvableConstraint.setType("cabAssegnoRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per i giorni limite del codice di pagamento.
	 * 
	 * @return regola creata
	 */
	private PropertyConstraint getGiorniLimiteCodicePagamentoConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("percentualeSconto", new And(
				gt(BigDecimal.ZERO), not(eq(null))));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("giorniLimite", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("giorniLimiteRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per numero assegno.
	 * 
	 * @return PropertyConstraint
	 */
	private PropertyConstraint getNumeroAssegnoRequired() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoAreaPartita.tipoOperazione",
				eq(TipoOperazione.GESTIONE_ASSEGNO));
		PropertyConstraint numeroAssegnoRequiredIfTrue = new RequiredIfTrue("numeroAssegno", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
				numeroAssegnoRequiredIfTrue);
		propertyResolvableConstraint.setType("numeroAssegnoRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per il numero documento dell'anticipo.
	 * 
	 * @return regola creata
	 */
	private PropertyConstraint getNumeroDocumentoAnticipoConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("numeroDocumentoRichiesto", eq(true));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("numeroDocumento", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("numeroDocumentoAnticipoRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per il numero documento dell'insoluto.
	 * 
	 * @return regola creata
	 */
	private PropertyConstraint getNumeroDocumentoInsolutoConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("numeroDocumentoRichiesto", eq(true));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("numeroDocumento", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("numeroDocumentoInsolutoRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Regola di validazione per il rapporto bancario azienda.
	 * 
	 * @return regola creata
	 */
	private PropertyConstraint getRapportoBancarioConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoAreaPartita.tipoDocumento.tipoEntita",
				eq(TipoEntita.BANCA));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("rapportoBancarioAzienda", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("areaContabileEntitaRequired");
		return propertyResolvableConstraint;
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();
		listRules.add(createParametriCreazioneAreaChiusureRules());
		listRules.add(createRataPartitaRules());
		listRules.add(createTipoDocumentoBasePartiteRules());
		listRules.add(createDistintaBancariaPMRules());
		listRules.add(createCategoriaRataRules());
		listRules.add(createCodicePagamentoRules());
		listRules.add(createTipoAreaPartiteRules());
		listRules.add(createStrutturaPartiteRules());
		listRules.add(createAreaTesoreriaRules());
		listRules.add(createParametriRicercaSituazioneRataRules());
		listRules.add(createParametriCreazionePagamentoRules());
		listRules.add(createAreaAccontoRules());
		listRules.add(createAreaPagamentiRules());
		listRules.add(createParametriCreazioneAnticipiRules());
		listRules.add(createParametriCreazioneInsolutiRules());
		listRules.add(createTemplateSolleciti());
		listRules.add(createSolleciti());
		listRules.add(createParametriGeneraRigheStrutturaPartiteRules());
		listRules.add(createSimulazioneRateRules());
		listRules.add(createRigaCalendarioRateRules());
		listRules.add(createCalendarioRateRules());
		listRules.add(createRigaStrutturaPartiteRules());
		listRules.add(createParametriRicercaRateRules());
		listRules.add(createParametriRicercaAreeTesoreriaRules());
		listRules.add(createParametriRicercaEffettiRules());
		listRules.add(createParametriRicercaAssegniRules());
		return listRules;
	}

	/**
	 * Regola di validazione per il tipo area partite pagamento.
	 * 
	 * @return regola creata
	 */
	private PropertyConstraint getTipoAreaPartitePagamentoConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("pagamento.areaAcconto.id", not(present()));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("tipoAreaPartita", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("tipoAreaPartitaRequired");
		return propertyResolvableConstraint;
	}

}
