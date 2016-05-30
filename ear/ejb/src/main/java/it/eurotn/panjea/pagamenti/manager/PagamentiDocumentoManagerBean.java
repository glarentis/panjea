package it.eurotn.panjea.pagamenti.manager;

@Deprecated
public class PagamentiDocumentoManagerBean {
	//
	//	static Logger logger = Logger.getLogger(PagamentiDocumentoManagerBean.class);
	//
	//	@EJB
	//	AreaPartiteManager areaPartiteManager;
	//
	//	@EJB
	//	PagamentiContabilitaManager pagamentiContabilitaManager;
	//
	//	@EJB
	//	DistintaBancariaManager distintaBancariaManager;
	//
	//	@EJB
	//	@IgnoreDependency
	//	PagamentiManager pagamentiManager;
	//
	//	@Resource
	//	SessionContext context;
	//
	//	@EJB
	//	DocumentiManager documentiManager;
	//
	//	@EJB
	//	PanjeaDAO panjeaDAO;
	//
	//	@EJB
	//	AreaContabileManager areaContabileManager;
	//
	//	@EJB
	//	AreaMagazzinoManager areaMagazzinoManager;
	//
	//	@Override
	//	public List<Object> caricaAreeLiteByAreaPartite(AreaPartite areaPartite) {
	//		logger.debug("--> Exit caricaAreeLiteByAreaPartita");
	//
	//		List<Object> listAree = new ArrayList<Object>();
	//
	//		// carico l'area contabile
	//		AreaContabileLite areaContabile = areaContabileManager.caricaAreaContabileLiteByDocumento(areaPartite
	//				.getDocumento());
	//		if (areaContabile != null) {
	//			listAree.add(areaContabile);
	//		}
	//
	//		// carico l'area magazzino
	//		AreaMagazzino areaMagazzino = areaMagazzinoManager.caricaAreaMagazzinoByDocumento(areaPartite.getDocumento());
	//		if (areaMagazzino != null) {
	//			listAree.add(areaMagazzino);
	//		}
	//
	//		logger.debug("--> Exit caricaAreeLiteByAreaPartita");
	//		return listAree;
	//	}
	//
	//	/**
	//	 * metodo di utilita' che registra i documenti per pagamenti passivi con distinta<br>
	//	 * (Pagamenti caso bonifico, rid, ecc.)<br>
	//	 * Un documento solo
	//	 * 
	//	 * @param pagamentiRaggruppati
	//	 * @param parametriCreazioneAreaPartita
	//	 * @return
	//	 * @throws DocumentoDuplicateException
	//	 */
	//
	//	private List<AreaPartite> creaAreaPartitaConDistinta(Map<EntitaLite, List<Pagamento>> pagamentiRaggruppati,
	//			ParametriCreazioneAreaPartita parametriCreazioneAreaPartita) throws DocumentoDuplicateException {
	//		logger.debug("--> Enter creaAreaPartitaConDistinta");
	//		if (parametriCreazioneAreaPartita.getRapportoBancarioAzienda() == null) {
	//			throw new IllegalArgumentException("Rapporto bancario NULLO!");
	//		}
	//		List<AreaPartite> areePartiteSalvate = new ArrayList<AreaPartite>();
	//		Importo totaleDocumento = new Importo();
	//
	//		for (List<Pagamento> arrays : pagamentiRaggruppati.values()) {
	//			for (Pagamento pagamento : arrays) {
	//				if (totaleDocumento.getCodiceValuta() == null) {
	//					// imposto il codice valuta al totaleDocumento prendendo il
	//					// codice
	//					// valuta del documento che ha generato la
	//					// rata
	//					String codValuta = pagamento.getRataPartita().getAreaPartite().getDocumento().getTotale()
	//							.getCodiceValuta();
	//					totaleDocumento.setCodiceValuta(codValuta);
	//				}
	//				totaleDocumento = totaleDocumento.add(pagamento.getImporto(), 2);
	//			}
	//		}
	//		Documento doc = new Documento();
	//		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
	//		doc.setDataDocumento(parametriCreazioneAreaPartita.getDataDocumento());
	//		doc.setTipoDocumento(parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoDocumento());
	//		doc.setEntita(null);
	//		doc.setRapportoBancarioAzienda(parametriCreazioneAreaPartita.getRapportoBancarioAzienda());
	//		doc.setTotale(totaleDocumento);
	//		Documento docSalvato;
	//		docSalvato = documentiManager.salvaDocumento(doc);
	//		AreaPartite areaPartite = new AreaPartite();
	//		areaPartite.setDocumento(docSalvato);
	//		areaPartite.setTipoAreaPartita(parametriCreazioneAreaPartita.getTipoAreaPartita());
	//		areaPartite.setCodicePagamento(null);
	//		areaPartite.setTipoOperazione(TipoOperazione.ALTRO);
	//		AreaPartite areaPartiteSalvata;
	//		areaPartiteSalvata = areaPartiteManager.salvaAreaPartite(areaPartite);
	//		areePartiteSalvate.add(areaPartiteSalvata);
	//		for (EntitaLite entita : pagamentiRaggruppati.keySet()) {
	//			// per ogni entita....
	//			List<Pagamento> pags = pagamentiRaggruppati.get(entita);
	//			// aggiorno rate e salvo pagamenti
	//			for (Pagamento pagamento : pags) {
	//				pagamento.setAreaPartite(areaPartiteSalvata);
	//				try {
	//					pagamento = panjeaDAO.save(pagamento);
	//				} catch (Exception e) {
	//					throw new RuntimeException(e);
	//				}
	//			}
	//		}
	//		logger.debug("--> Exit creaAreaPartitaConDistinta");
	//		return areePartiteSalvate;
	//	}
	//
	//	/**
	//	 * Questo metodo prepara una lista di aree partite con effetti a cui sono legati i vari pagamenti pnj.pa 10<br>
	//	 * caso delle RIBA<br>
	//	 * 
	//	 * @param parametriCreazioneAreaPartita
	//	 * 
	//	 * 
	//	 * @param pagamenti
	//	 * @return
	//	 * @throws DocumentoDuplicateException
	//	 */
	//	private List<AreaPartite> creaAreaPartitaIncassiConDistinta(
	//			ParametriCreazioneAreaPartita parametriCreazioneAreaPartita, List<Pagamento> pagamenti)
	//			throws DocumentoDuplicateException {
	//		logger.debug("--> Enter creaAreaPartitaIncassiConDistinta");
	//		Map<EntitaLite, List<Pagamento>> pagamentiRaggruppati = raggruppaPagamenti(parametriCreazioneAreaPartita
	//				.getTipoAreaPartita().getTipoDocumento().getTipoEntita(), pagamenti);
	//		List<AreaPartite> result = distintaBancariaManager.creaAreaPartitaIncassiConDistinta(
	//				parametriCreazioneAreaPartita, pagamentiRaggruppati);
	//		logger.debug("--> Exit creaAreaPartitaIncassiConDistinta");
	//		return result;
	//	}
	//
	//	/**
	//	 * Registra i pagamenti senza distinta attivi<br>
	//	 * Implementa lo UC pnj.07
	//	 * 
	//	 * @param parametriCreazioneAreaPartita
	//	 * @param pagamenti
	//	 * @return
	//	 * @throws DocumentoDuplicateException
	//	 */
	//	private List<AreaPartite> creaAreaPartitaIncassiSenzaDistinta(
	//			ParametriCreazioneAreaPartita parametriCreazioneAreaPartita, List<Pagamento> pagamenti)
	//			throws DocumentoDuplicateException {
	//		logger.debug("--> Enter creaAreaPartitaIncassiSenzaDistinta");
	//		Map<EntitaLite, List<Pagamento>> pagamentiRaggruppati = raggruppaPagamenti(parametriCreazioneAreaPartita
	//				.getTipoAreaPartita().getTipoDocumento().getTipoEntita(), pagamenti);
	//		List<AreaPartite> result = creaAreaPartitaSenzaDistinta(pagamentiRaggruppati, parametriCreazioneAreaPartita,
	//				pagamenti);
	//		logger.debug("--> Exit creaAreaPartitaIncassiSenzaDistinta");
	//		return result;
	//	}
	//
	//	/**
	//	 * Metodo di ingresso:<br>
	//	 * Seleziona il tipo partita (ATTIVA o PASSIVA)
	//	 * 
	//	 * @throws DocumentoDuplicateException
	//	 */
	//	@Override
	//	public List<AreaPartite> creaAreaPartitaPerPagamenti(ParametriCreazioneAreaPartita parametriCreazioneAreaPartita,
	//			List<Pagamento> pagamenti) throws DocumentoDuplicateException {
	//		logger.debug("--> Enter creaAreaPartitaPerPagamenti");
	//		List<AreaPartite> areePartite = new ArrayList<AreaPartite>();
	//		if (parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoPartita().equals(TipoPartita.ATTIVA)) {
	//			// E' una chiusura di una partita attiva
	//			areePartite = creaAreaPartitaPerPagamentiAttivi(parametriCreazioneAreaPartita, pagamenti);
	//		} else {
	//			// E' una chiusura di una partita passiva
	//			areePartite = creaAreaPartitaPerPagamentiPassivi(parametriCreazioneAreaPartita, pagamenti);
	//		}
	//		pagamentiContabilitaManager.creaAreeContabiliPagamento(areePartite);
	//		logger.debug("--> Exit creaAreaPartitaPerPagamenti");
	//		return areePartite;
	//	}
	//
	//	/**
	//	 * Metodo di utilita per selezionare nei pagamenti attivi quelli con gestione distinta o no
	//	 * 
	//	 * @param parametriCreazioneAreaPartita
	//	 * @param pagamenti
	//	 * @return
	//	 * @throws DocumentoDuplicateException
	//	 */
	//	private List<AreaPartite> creaAreaPartitaPerPagamentiAttivi(
	//			ParametriCreazioneAreaPartita parametriCreazioneAreaPartita, List<Pagamento> pagamenti)
	//			throws DocumentoDuplicateException {
	//		if (parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoOperazione().equals(
	//				TipoAreaPartita.TipoOperazione.GESTIONE_DISTINTA)) {
	//			// Incasso con distinta
	//			return creaAreaPartitaIncassiConDistinta(parametriCreazioneAreaPartita, pagamenti);
	//		} else {
	//			// Incasso senza distinta
	//			return creaAreaPartitaIncassiSenzaDistinta(parametriCreazioneAreaPartita, pagamenti);
	//		}
	//	}
	//
	//	/**
	//	 * Metodo di utilita per selezionare nei pagamenti passivi quelli con gestione distinta o no
	//	 * 
	//	 * @param parametriCreazioneAreaPartita
	//	 * @param pagamenti
	//	 * @return
	//	 * @throws DocumentoDuplicateException
	//	 */
	//	private List<AreaPartite> creaAreaPartitaPerPagamentiPassivi(
	//			ParametriCreazioneAreaPartita parametriCreazioneAreaPartita, List<Pagamento> pagamenti)
	//			throws DocumentoDuplicateException {
	//		if (parametriCreazioneAreaPartita.getTipoAreaPartita().equals(TipoAreaPartita.TipoOperazione.GESTIONE_DISTINTA)) {
	//			return creaAreaPartitaPerPagamentiPassiviConDistinta(parametriCreazioneAreaPartita, pagamenti);
	//		} else {
	//			return creaAreaPartitaPerPagamentiPassiviSenzaDistinta(parametriCreazioneAreaPartita, pagamenti);
	//		}
	//	}
	//
	//	/**
	//	 * Registra i pagamenti con distinta passivi<br>
	//	 * Implementa lo UC pnj.08
	//	 * 
	//	 * @param parametriCreazioneAreaPartita
	//	 * @param pagamenti
	//	 * @return
	//	 * @throws DocumentoDuplicateException
	//	 */
	//	private List<AreaPartite> creaAreaPartitaPerPagamentiPassiviConDistinta(
	//			ParametriCreazioneAreaPartita parametriCreazioneAreaPartita, List<Pagamento> pagamenti)
	//			throws DocumentoDuplicateException {
	//		logger.debug("--> Enter creaAreaPartitaPerPagamentiPassivi");
	//		Map<EntitaLite, List<Pagamento>> pagamentiRaggruppati = raggruppaPagamenti(parametriCreazioneAreaPartita
	//				.getTipoAreaPartita().getTipoDocumento().getTipoEntita(), pagamenti);
	//		List<AreaPartite> result = creaAreaPartitaConDistinta(pagamentiRaggruppati, parametriCreazioneAreaPartita);
	//		logger.debug("--> Exit creaAreaPartitaPerPagamentiPassivi");
	//		return result;
	//	}
	//
	//	/**
	//	 * Registra i pagamenti senza distinta passivi<br>
	//	 * Implementa lo UC pnj.07
	//	 * 
	//	 * @param parametriCreazioneAreaPartita
	//	 * @param pagamenti
	//	 * @return
	//	 * @throws DocumentoDuplicateException
	//	 */
	//	private List<AreaPartite> creaAreaPartitaPerPagamentiPassiviSenzaDistinta(
	//			ParametriCreazioneAreaPartita parametriCreazioneAreaPartita, List<Pagamento> pagamenti)
	//			throws DocumentoDuplicateException {
	//		logger.debug("--> Enter creaAreaPartitaPerPagamentiPassiviSenzaDistinta");
	//		Map<EntitaLite, List<Pagamento>> pagamentiRaggruppati = raggruppaPagamenti(parametriCreazioneAreaPartita
	//				.getTipoAreaPartita().getTipoDocumento().getTipoEntita(), pagamenti);
	//		List<AreaPartite> result = creaAreaPartitaSenzaDistinta(pagamentiRaggruppati, parametriCreazioneAreaPartita,
	//				pagamenti);
	//		logger.debug("--> Exit creaAreaPartitaPerPagamentiPassiviSenzaDistinta");
	//		return result;
	//	}
	//
	//	/**
	//	 * metodo di utilita' che registra i documenti per pagamenti passivi (senza distinta) e attivi<br>
	//	 * (Pagamenti e incassi diretti)<br>
	//	 * Un documento per ogni entita
	//	 * 
	//	 * @param parametriCreazioneAreaPartita
	//	 * @param pagamenti
	//	 * @return
	//	 * @throws DocumentoDuplicateException
	//	 */
	//	private List<AreaPartite> creaAreaPartitaSenzaDistinta(Map<EntitaLite, List<Pagamento>> pagamentiRaggruppati,
	//			ParametriCreazioneAreaPartita parametriCreazioneAreaPartita, List<Pagamento> pagamenti)
	//			throws DocumentoDuplicateException {
	//		logger.debug("--> Enter creaAreaPartitaSenzaDistinta");
	//
	//		List<AreaPartite> areePartiteSalvate = new ArrayList<AreaPartite>();
	//		for (EntitaLite entita : pagamentiRaggruppati.keySet()) {
	//			// per ogni entita....
	//			Importo totaleDocumento = new Importo();
	//
	//			List<Pagamento> pags = pagamentiRaggruppati.get(entita);
	//			// imposto il codice valuta al totaleDocumento prendendo il codice
	//			// valuta del documento che ha generato la
	//			// rata
	//			if (pags.size() > 0) {
	//				String codValuta = pags.get(0).getRataPartita().getAreaPartite().getDocumento().getTotale()
	//						.getCodiceValuta();
	//				totaleDocumento.setCodiceValuta(codValuta);
	//			}
	//			for (Pagamento pagamento : pags) {
	//				totaleDocumento = totaleDocumento.add(pagamento.getImporto(), 2);
	//			}
	//			Documento doc = new Documento();
	//			doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
	//			doc.setDataDocumento(parametriCreazioneAreaPartita.getDataDocumento());
	//			doc.setTipoDocumento(parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoDocumento());
	//			doc.setEntita(null);
	//			if (parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoDocumento().getTipoEntita().equals(
	//					TipoEntita.AZIENDA)) {
	//				// documento di pagamento relativo all'azienda
	//				doc.setRapportoBancarioAzienda(null);
	//			} else if (parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoDocumento().getTipoEntita().equals(
	//					TipoEntita.BANCA)) {
	//				// documento di pagamento relativo ad un rapporto bancario (non
	//				// ci dovrebbe essere ... ma non si sa mai)
	//				// TODO mettere controllo
	//				if (parametriCreazioneAreaPartita.getRapportoBancarioAzienda() == null) {
	//					throw new IllegalArgumentException("Rapporto bancario NULLO!");
	//				}
	//				doc.setRapportoBancarioAzienda(parametriCreazioneAreaPartita.getRapportoBancarioAzienda());
	//			} else if (parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoDocumento().getTipoEntita().equals(
	//					TipoEntita.FORNITORE)
	//					|| parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoDocumento().getTipoEntita().equals(
	//							TipoEntita.CLIENTE)
	//					|| parametriCreazioneAreaPartita.getTipoAreaPartita().getTipoDocumento().getTipoEntita().equals(
	//							TipoEntita.VETTORE)) {
	//				doc.setEntita(entita);
	//			}
	//
	//			doc.setTotale(totaleDocumento);
	//			doc.restoreNullValue();
	//			Documento docSalvato;
	//			docSalvato = documentiManager.salvaDocumento(doc);
	//			AreaPartite areaPartite = new AreaPartite();
	//			areaPartite.setDocumento(docSalvato);
	//			areaPartite.setTipoAreaPartita(parametriCreazioneAreaPartita.getTipoAreaPartita());
	//			areaPartite.setCodicePagamento(null);
	//			areaPartite.setTipoOperazione(TipoOperazione.ALTRO);
	//			AreaPartite areaPartiteSalvata;
	//			areaPartiteSalvata = areaPartiteManager.salvaAreaPartite(areaPartite);
	//
	//			for (Pagamento pagamento : pags) {
	//				RataPartita rata = pagamento.getRataPartita();
	//				if (pagamento.getChiusuraForzataRata()) {
	//					// ho forzato la chiusura
	//					pagamentiManager.cambiaStatoRataPagamento(StatoRata.CHIUSA, rata, pagamento,
	//							parametriCreazioneAreaPartita.getDataDocumento());
	//				} else {
	//					Importo giaPagato = pagamentiManager.calcolaPagamentiDellaRata(pagamento);
	//					if (rata.getImporto().equals(giaPagato)) {
	//						pagamentiManager.cambiaStatoRataPagamento(StatoRata.CHIUSA, rata, pagamento,
	//								parametriCreazioneAreaPartita.getDataDocumento());
	//					}
	//					// dato che sto creando una area partite senza distinta devo impostare cmq la data pagamento
	//					// e segnare la rata come parzialmente pagata perche' questo viene eseguito alla data documento
	//					// anche se non viene chiusa la rata
	//					else {
	//						pagamentiManager.cambiaStatoRataPagamento(StatoRata.PAGAMENTO_PARZIALE, rata, pagamento,
	//								parametriCreazioneAreaPartita.getDataDocumento());
	//					}
	//				}
	//				pagamento.setAreaPartite(areaPartiteSalvata);
	//				Pagamento pagamentoSalvato = pagamentiManager.salvaPagamento(pagamento);
	//				if (logger.isDebugEnabled()) {
	//					logger.debug("--> pagamento salvato " + pagamentoSalvato.getId());
	//				}
	//			}
	//			areePartiteSalvate.add(areaPartiteSalvata);
	//		}
	//		logger.debug("--> Exit creaAreaPartitaSenzaDistinta");
	//		return areePartiteSalvate;
	//	}
	//
	//	/**
	//	 * recupera {@link JecPrincipal} dal {@link SessionContext}
	//	 * 
	//	 * @return
	//	 */
	//	private JecPrincipal getJecPrincipal() {
	//		logger.debug("--> Enter getJecPrincipal");
	//		return (JecPrincipal) context.getCallerPrincipal();
	//	}
	//
	//	/**
	//	 * Metodo di utilita' che serve a raggruppare i pagamenti per ogni entita
	//	 * 
	//	 * @param pagamenti
	//	 * @return
	//	 */
	//	private Map<EntitaLite, List<Pagamento>> raggruppaPagamenti(TipoEntita tipoEntita, List<Pagamento> pagamenti) {
	//		logger.debug("--> Enter raggruppaPagamenti");
	//		Map<EntitaLite, List<Pagamento>> raggruppamenti = new HashMap<EntitaLite, List<Pagamento>>();
	//		for (Pagamento pagamento : pagamenti) {
	//			if (pagamento.getRataPartita().getAreaPartite().getDocumento().getTipoDocumento().getTipoEntita().equals(
	//					TipoEntita.CLIENTE)
	//					|| pagamento.getRataPartita().getAreaPartite().getDocumento().getTipoDocumento().getTipoEntita()
	//							.equals(TipoEntita.FORNITORE)) {
	//				// Cliente o Fornitore
	//				EntitaLite entita = pagamento.getRataPartita().getAreaPartite().getDocumento().getEntita();
	//				if (raggruppamenti.containsKey(entita)) {
	//					// gia' trovato
	//					List<Pagamento> pags = raggruppamenti.get(entita);
	//					pags.add(pagamento);
	//				} else {
	//					// nuovo
	//					List<Pagamento> pags = new ArrayList<Pagamento>();
	//					pags.add(pagamento);
	//					raggruppamenti.put(entita, pags);
	//				}
	//			}
	//		}
	//		logger.debug("--> Exit raggruppaPagamenti");
	//		return raggruppamenti;
	//	}

}
