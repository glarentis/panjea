ALTER TABLE `installazioni`
	ADD INDEX `Matricola` (`Matricola`);
	
ALTER TABLE `distributori`
	ADD INDEX `Matricola` (`Matricola`);



INSERT INTO maga_categorie (version, userInsert, codiceAzienda, codice, descrizioneLinguaAziendale)
select 0, 'auvend', 'gda', categoria, descrizione from gdaAuvend.categorieprodotti where categoria <> '.';

INSERT INTO anag_unita_misura (codice, descrizione, version, userInsert, codiceAzienda, timeStamp)
select distinct unita_misura, unita_misura, 1, 'auvend', 'gda', UNIX_TIMESTAMP()
from gdaAuvend.prodotti 
left join anag_unita_misura on anag_unita_misura.codice = gdaAuvend.prodotti.Unita_misura
where unita_misura <> '.' and anag_unita_misura.codice is null;

INSERT INTO vend_tipi_comunicazione (timeStamp, userInsert, version, codice, comunicazioneAsl, comunicazioneComuni, descrizione)
select UNIX_TIMESTAMP(), 'auvend', 0, tipo, comunicazioni_asl, comunicazioni_comune, descrizione from gdaAuvend.tipidistributoripercomunicazioni;

INSERT INTO vend_tipi_modello (timeStamp, userInsert, version, acqua, caldo, codice, descrizione, freddo, kit, snack, tipoComunicazione_id, gelato, snackRefrigerati)
select UNIX_TIMESTAMP(), 'auvend', 0, Acqua, Caldo, Riclassificazione, gdaAuvend.riclassificazionimodelli.Descrizione, Freddo, Kit, Snack,vend_tipi_comunicazione.id, 0, 0
from gdaAuvend.riclassificazionimodelli 
left join vend_tipi_comunicazione on vend_tipi_comunicazione.codice = gdaAuvend.riclassificazionimodelli.Tipo_per_comunicazioni
where riclassificazione <> '.';

INSERT INTO vend_modelli (timeStamp, userInsert, version, cassettaPresente, codice, descrizione, obbligoLetturaCassetta, ritiroCassetta, tipoModello_id)
select UNIX_TIMESTAMP(), 'auvend', 0, 0, modello, gdaAuvend.modellidistributori.descrizione, obbligaRitiroCassetta, obbligaLetturaContatore, vend_tipi_modello.id
from gdaAuvend.modellidistributori 
join vend_tipi_modello on vend_tipi_modello.codice = gdaAuvend.modellidistributori.riclassificazione
where gdaAuvend.modellidistributori.Modello <> '.'; 

INSERT INTO vend_sistemi_elettronici (timeStamp, userInsert, version, caricaChiave, cassetta, chiave, codice, descrizione,  resto, tipo, baud, rx, start9600, tipoComunicazione, tx)
select UNIX_TIMESTAMP(), 'auvend', 0, Carica_chiavi, Cassetta, Chiave, Gettoniera, gdaAuvend.gettoniere.Descrizione,  Resto, 1, 
case gdaAuvend.gettoniere.Baud when 2400 then 0 when 4800 then 1 when 9600 then 3 when 19200 then 4 when 38400 then 5 when 57600 then 6 when 115200 then 7 end, 
gdaAuvend.gettoniere.Rx, gdaAuvend.gettoniere.Start9600, gdaAuvend.gettoniere.Protocollo_IRda, gdaAuvend.gettoniere.Tx 
from gdaAuvend.gettoniere 
join (select distinct Sistema_pagamento from gdaAuvend.installazioni) Sis on Sis.Sistema_pagamento = gdaAuvend.gettoniere.Gettoniera 
where gettoniera <> '.';

ALTER TABLE vend_dati_vending_distributore
ADD COLUMN `matricola` varchar(13) NOT NULL;

ALTER TABLE vend_dati_vending_distributore ADD INDEX `matricola` (`matricola`)

INSERT INTO vend_dati_vending_distributore (timeStamp, userInsert, version, cassettaAbitata, modello_id, matricolaFornitore, matricola)
select UNIX_TIMESTAMP(), 'auvend', 0, 1, vend_modelli.id, matricola_fornitore, gdaAuvend.distributori.matricola
from gdaAuvend.distributori
join vend_modelli on vend_modelli.codice = gdaAuvend.distributori.Modello
where gdaAuvend.distributori.matricola <> '.'

INSERT INTO maga_articoli (version, userInsert, codiceAzienda, tipoArticolo, codice,  abilitato, ivaAlternativa, descrizioneLinguaAziendale, unitaMisura_id, articoloLibero, numeroDecimaliQta, unitaMisuraQtaMagazzino_id, provenienzaPrezzoArticolo, numeroDecimaliPrezzo,
timeStamp, tipoLotto, distinta, categoriaCommercialeArticolo_id, lottoFacoltativo, gestioneSchedaArticolo, mrp, gestioneQuantitaZero, stampaLotti, 
produzione, TIPO, somministrazione, proprietaCliente, datiVending_id, codiceLinguaAzienda) 
select 0, 'auvend', 'gda', 0, gdaAuvend.distributori.matricola, 1, 0, gdaAuvend.modellidistributori.descrizione, 1, 0, 0, 1, 0, 0, UNIX_TIMESTAMP(), 0, 0, 1, 0, 0, 0, 0, 0, 0, 'DI', 0, IF( gdaAuvend.distributori.matricola like '$%', 1, 0), vend_dati_vending_distributore.id, 'IT'
from gdaAuvend.distributori
join gdaAuvend.modellidistributori on gdaAuvend.modellidistributori.modello = gdaAuvend.distributori.Modello 
join vend_dati_vending_distributore on vend_dati_vending_distributore.matricola = gdaAuvend.distributori.matricola
where gdaAuvend.distributori.matricola <> '.';

INSERT INTO geog_localita (timeStamp, userInsert, version, descrizione, livello1_id, livello2_id, livello3_id, livello4_id, nazione_id) 
select UNIX_TIMESTAMP(), 'auvend', 0, gdaAuvend.comuni.descrizione, geog_livello_1.id, coalesce(geog_livello_2.id, proGenerica.id), coalesce(geog_livello_3.id, comGenerico.id), null, geog_nazioni.id
from gdaAuvend.comuni
left join geog_livello_2 on geog_livello_2.sigla = gdaAuvend.comuni.provincia
left join geog_livello_1 on geog_livello_1.id = geog_livello_2.livello1_id
left join geog_livello_3 on geog_livello_3.nome = gdaAuvend.comuni.descrizione
join geog_livello_3 as comGenerico on comGenerico.nome = 'Generico'
join geog_livello_2 as proGenerica on proGenerica.sigla = 'ZZ'
join geog_nazioni on geog_nazioni.codice = 'IT'
left join (
	select geog_localita.id, geog_localita.descrizione, geog_livello_2.sigla 
	from geog_localita
	join geog_livello_2 on geog_localita.livello2_id = geog_livello_2.id
	) as locGiaPresenti on locGiaPresenti.descrizione = gdaAuvend.comuni.descrizione and locGiaPresenti.sigla = gdaAuvend.comuni.Provincia
where locGiaPresenti.id is null

ALTER TABLE anag_sedi_anagrafica
ADD COLUMN `clienteAuVend` varchar(6) NOT NULL;

ALTER TABLE anag_sedi_anagrafica ADD INDEX `clienteAuVend` (`clienteAuVend`);


--UPDATE clienti SET Codice_contabile=Cliente

-- Sedi principali presenti in AuVend
INSERT INTO anag_sedi_anagrafica (clienteAuvend,sedeAuVend,version, userInsert, descrizione, indirizzo, abilitato, telefono, fax, indirizzo_mail, timeStamp,
cap_localita_id, localita_id, lvl1_id, lvl2_id, lvl3_id, lvl4_id, nazione_id, spedizioneDocumentiViaPEC, tipoSpedizioneDocumenti)
select cl.codice_contabile, cls.Sede, 0, 'auvend', 'Sede principale', cl.Indirizzo,1, cl.Telefono_1, cl.Fax, cl.Indirizzo_EMAIL, UNIX_TIMESTAMP(),
geog_cap.id, loc.id, loc.livello1_id, loc.livello2_id, loc.livello3_id, null, loc.nazione_id,0,1
from gdaAuvend.clienti cl
join gdaAuvend.clientisedi cls on cl.Cliente = cls.Cliente
join gdaAuvend.comuni com on cl.Comune = com.Comune
left join geog_livello_2 l2 on l2.sigla = cl.Provincia
left join geog_localita loc on loc.descrizione = com.Descrizione and loc.livello2_id = l2.id
left join geog_cap on geog_cap.descrizione = cl.cap
where cl.cliente <> '.' and 
(	coalesce(cl.Indirizzo,'') = coalesce(cls.Indirizzo,'') and 
 	coalesce(cl.Comune,'') = coalesce(cls.Comune,'') and
	coalesce(cl.Cap, '') = coalesce(cls.Cap,'')
)


ALTER TABLE anag_anagrafica
ADD COLUMN `clienteAuVend` varchar(6) NOT NULL;

ALTER TABLE anag_anagrafica ADD INDEX `clienteAuVend` (`clienteAuVend`);

ALTER TABLE anag_anagrafica
ADD COLUMN `sedeAuVend` varchar(6) NOT NULL;

ALTER TABLE anag_anagrafica ADD INDEX `sedeAuVend` (`sedeAuVend`);

ALTER TABLE anag_sedi_anagrafica
ADD COLUMN `sedeAuVend` varchar(6) NOT NULL;

ALTER TABLE anag_sedi_anagrafica
ADD COLUMN `sedeAuVend` varchar(6) NOT NULL;

ALTER TABLE anag_sedi_anagrafica ADD INDEX `sedeAuVend` (`sedeAuVend`);

ALTER TABLE gdaAuvend.`clienti`
	ADD INDEX `Cliente` (`Cliente`);

ALTER TABLE gdaAuvend.`clientisedi`
	ADD INDEX `Cliente` (`Cliente`);
	
ALTER TABLE gdaAuvend.`comuni`
	ADD INDEX `Comune` (`Comune`);
	
ALTER TABLE gdaAuvend.`clientisedi`
	ADD INDEX `Comune` (`Comune`);
	
ALTER TABLE gdaAuvend.`clientisedi`
	ADD INDEX `Provincia` (`Provincia`);
	
ALTER TABLE gdaAuvend.`clientisedi`
	ADD INDEX `cap` (`cap`);

-- Sedi principali non presenti in AuVend

INSERT INTO anag_sedi_anagrafica (clienteAuvend,sedeAuVend,version, userInsert, descrizione, indirizzo, abilitato, telefono, fax, indirizzo_mail, timeStamp,
cap_localita_id, localita_id, lvl1_id, lvl2_id, lvl3_id, lvl4_id, nazione_id, spedizioneDocumentiViaPEC, tipoSpedizioneDocumenti)
select cl.codice_contabile, '-1', 0, 'auvend', 'Sede principale', cl.Indirizzo,1, cl.Telefono_1, cl.Fax, cl.Indirizzo_EMAIL, UNIX_TIMESTAMP(),
geog_cap.id, loc.id, loc.livello1_id, loc.livello2_id, loc.livello3_id, null, loc.nazione_id,0,1
from gdaAuvend.clienti cl
join gdaAuvend.comuni com on cl.Comune = com.Comune
left join geog_livello_2 l2 on l2.sigla = cl.Provincia
left join geog_localita loc on loc.descrizione = com.Descrizione and loc.livello2_id = l2.id
left join geog_cap on geog_cap.descrizione = cl.cap
left join anag_sedi_anagrafica sa on sa.clienteAuVend = cl.Codice_contabile
where cl.cliente <> '.' and sa.id is null

-- Sedi secondarie
INSERT INTO anag_sedi_anagrafica (clienteAuvend,sedeAuVend,version, userInsert, descrizione, indirizzo, abilitato, telefono, fax, indirizzo_mail, timeStamp,
cap_localita_id, localita_id, lvl1_id, lvl2_id, lvl3_id, lvl4_id, nazione_id, spedizioneDocumentiViaPEC, tipoSpedizioneDocumenti)
select cl.codice_contabile, cls.Sede, 0, 'auvend', 'Sede secondaria', cls.Indirizzo,1, null, null, null, UNIX_TIMESTAMP(),
geog_cap.id, loc.id, loc.livello1_id, loc.livello2_id, loc.livello3_id, null, loc.nazione_id,0,1
from gdaAuvend.clienti cl
join gdaAuvend.clientisedi cls on cl.Cliente = cls.Cliente
join gdaAuvend.comuni com on cls.Comune = com.Comune
left join geog_livello_2 l2 on l2.sigla = cls.Provincia
left join geog_localita loc on loc.descrizione = com.Descrizione and loc.livello2_id = l2.id
left join geog_cap on geog_cap.descrizione = cls.cap
where cl.cliente <> '.' and not
(	coalesce(cl.Indirizzo,'') = coalesce(cls.Indirizzo,'') and 
 	coalesce(cl.Comune,'') = coalesce(cls.Comune,'') and
	coalesce(cl.Cap, '') = coalesce(cls.Cap,'')
)

/* DEVO CANCELLARE LE SEDI DOPPIE ENTRATE A CAUSA DELLE LOCALITA RIPETUTE IN PANJEA
 * QUESTA E' LA QUERY PER TROVARLE, CANCELLARLE A MANO
Select  id, anag_sedi_anagrafica.clienteAuvend, anag_sedi_anagrafica.sedeAuvend  from anag_sedi_anagrafica join 
( select count(*), clienteAuVend, sedeAuVend from anag_sedi_anagrafica group by clienteAuVend, sedeAuvend having count(*) > 1) as T 
	on T.clienteAuvend = anag_sedi_anagrafica.clienteAuVend 
	and T.sedeAuvend = anag_sedi_anagrafica.sedeAuvend
order by anag_sedi_anagrafica.clienteAuvend, anag_sedi_anagrafica.sedeAuVend
 */



INSERT INTO anag_anagrafica (clienteAuVend,sedeAuVend,version, userInsert, codice_azienda, denominazione, partita_iva, codice_fiscale, sede_anagrafica_id, timeStamp)
select cli.codice_contabile, anag_sedi_anagrafica.sedeAuVend, 0, 'auvend', 'gda', cli.Ragione_sociale, cli.Partita_IVA, cli.Codice_fiscale, anag_sedi_anagrafica.id, UNIX_TIMESTAMP()
from gdaAuvend.clienti cli
join anag_sedi_anagrafica on anag_sedi_anagrafica.clienteAuVend = cli.Codice_contabile
join (select min(anag_sedi_anagrafica.id) as id from anag_sedi_anagrafica join gdaAuvend.clienti on gdaAuvend.clienti.Codice_contabile = anag_sedi_anagrafica.clienteAuvend group by anag_sedi_anagrafica.clienteAuVend) as T on T.id = anag_sedi_anagrafica.id

--Per verificare se sono state importate anagrafiche doppie
--select clienteAuvend,count(clienteAuvend) from anag_anagrafica group by  clienteAuVend
INSERT INTO anag_entita (TIPO_ANAGRAFICA, version, userInsert, codice, abilitato, anagrafica_id, fido,  noteContabilita, noteMagazzino, raggruppaEffetti, timeStamp, assortimentoArticoli, capoArea, codiceClienteMittenteEstero, codiceClienteMittenteItalia, codiceTariffaEstero, codiceTariffaItalia, codiceTrattamentoMerce, etichettePath, indirizzoInvio, nameFileEtichetteToExport, nameFileEtichetteToImport, naturaMerceMittente, noteStampa, numeratore, numeroSerie, passwordInvio, pathFileRendiconto, pathFileTemplateEtichette, pathFileTemplateRendiconto, puntoOperativoArrivo, puntoOperativoPartenzaMerce, tipoInvio, tipoServizioBolle, tipoStampante, userInvio, escludiSpesometro, fatturazioneAgente, causaleRitenutaAcconto_id, contributoPrevidenziale_id, percFondoProfessionisti, tipoMandato, riepilogativo, capoarea_id, prestazione_id, blocco, noteBlocco, codiceEori, codiceIdentificativoFiscale, fatturazionePA, splitPayment, noteFatturaPA, codiceEsterno, fatturaPalmare) 
select 'C', 0, 'auvend', gdaAuvend.clienti.Codice_contabile, 1, anag_anagrafica.id, 0, NULL, NULL, 1, UNIX_TIMESTAMP(), 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0,  NULL, NULL, 0, 0, 0, NULL, NULL, 0, NULL, NULL, NULL, 0, 0, NULL, NULL, 0
from gdaAuvend.clienti
join anag_anagrafica on anag_anagrafica.clienteAuVend = gdaAuvend.clienti.codice_contabile;

INSERT INTO cont_sottoconti (version, userInsert, codiceAzienda, codice, descrizione, classificazioneConto, flagIRAP, conto_id, abilitato, timeStamp, centroCosto_id, soggettoCentroCosto, backGroundColor, condizione, importo, stile_abilitato) 
select 0, 'auvend', 'gda', LPAD(anag_entita.codice,6,'0'), coalesce(anag_anagrafica.denominazione,'') , 0, 0, cont_conti.id, 1, UNIX_TIMESTAMP(), NULL, 0, NULL, NULL, NULL, NULL
from anag_anagrafica
join anag_entita on anag_entita.anagrafica_id = anag_anagrafica.id
join cont_conti on cont_conti.sottotipoConto = 0
where anag_entita.TIPO_ANAGRAFICA = 'C'

-- inserisco le sedi entita
INSERT INTO anag_sedi_entita (version, userInsert, abilitato, entita_id, tipoSede_id, sede_anagrafica_id, ereditaRapportiBancari, codiceValuta, ereditaDatiCommerciali, timeStamp, codice, blocco, lingua, predefinita ) 
select 0, 'auvend', 1, anag_entita.id, 1, anag_sedi_anagrafica.id, 0, 'EUR', 0, UNIX_TIMESTAMP(), anag_sedi_anagrafica.sedeAuVend, 0, 'it', case when anag_sedi_anagrafica.descrizione = 'Sede principale' then 1 else 0 end  
from anag_sedi_anagrafica
join anag_anagrafica on anag_anagrafica.clienteAuVend = anag_sedi_anagrafica.clienteAuVend
join anag_entita on anag_entita.anagrafica_id = anag_anagrafica.id
where CHAR_LENGTH(anag_sedi_anagrafica.clienteAuVend) > 0
	
--select sedeAuVend,count(sedeAuVend) from anag_sedi_anagrafica group by clienteAuVend,sedeAuVend

INSERT INTO anag_tipo_deposito (timeStamp, userInsert, version, codice) 
select UNIX_TIMESTAMP(), 'auvend', 0, 'DI'

-- Inserisco i Depositi Installazione
INSERT INTO anag_depositi (version, userInsert, descrizione, attivo, sedeDeposito_id, codice, principale, codiceAzienda,  timeStamp, tipoDeposito_id, entita_id, sedeEntita_id, TIPO) 
select 0, 'auvend', 'Deposito installazione', 0, 1, 'DINST', 0, 'gda', UNIX_TIMESTAMP(), 3, anag_entita.id, anag_sedi_entita.id, 'IN'
from anag_sedi_entita
join anag_entita on anag_entita.id = anag_sedi_entita.entita_id
where anag_entita.TIPO_ANAGRAFICA = 'C'

INSERT INTO maga_tipi_mezzo_trasporto (userInsert, version, codice, codiceAzienda, descrizione, articolo_id, timeStamp)
VALUES ('auvend', 0, 'F', 'gda', 'FURGONE', null, UNIX_TIMESTAMP());

INSERT INTO anag_tipo_deposito (timeStamp, userInsert, version, codice) 
select UNIX_TIMESTAMP(), 'auvend', 0, 'F'

INSERT INTO anag_depositi (version, userInsert, descrizione, attivo, sedeDeposito_id, codice, principale, codiceAzienda, timeStamp, tipoDeposito_id, TIPO) 
select 0, 'auvend', gdaAuvend.caricatori.Nome , IF(T.Caricatore is null, 0, 1), 1, gdaAuvend.caricatori.Caricatore, 0, 'gda', UNIX_TIMESTAMP(), anag_tipo_deposito.id , 'DE'
from gdaAuvend.caricatori
left join (select distinct gdaAuvend.installazioni.Caricatore from gdaAuvend.installazioni) as T on T.Caricatore = gdaAuvend.caricatori.Caricatore
join anag_tipo_deposito on anag_tipo_deposito.codice = 'F'
where gdaAuvend.caricatori.caricatore <> '.'

INSERT INTO maga_mezzi_trasporto (userInsert, version, codiceAzienda, descrizione, targa, tipoMezzoTrasporto_id, timeStamp, abilitato, entita_id, deposito_id) 
select 'auvend', 0, 'gda', anag_depositi.descrizione, anag_depositi.codice, 1, UNIX_TIMESTAMP(), 1, NULL, anag_depositi.id
from anag_depositi
join anag_tipo_deposito on anag_depositi.tipoDeposito_id = anag_tipo_deposito.id
join maga_tipi_mezzo_trasporto on maga_tipi_mezzo_trasporto.codice = 'F'
where anag_depositi.attivo = 1 and anag_tipo_deposito.codice = 'F'

--CARICATORI
insert into manu_operatori (version, userInsert,codiceAzienda, caricatore,tecnico,codice,nome,mezzoTrasporto_id,patenteScadenza,documentoIdentitaNumero) 
select 0,'auvend', 'gda',1,0,c.Caricatore,c.nome,mt.id,c.Data_scadenza_patente,c.Numero_documento from gdaAuvend.caricatori c inner join maga_mezzi_trasporto mt on mt.descrizione=c.Nome
where c.Caricatore<>'.'

-- Creo tabella di appoggio dove salvare gli id del codice iva da utilizzare per ogni aliquota usata dai prodotti
-- Va riempita a mano prima della prossima query
CREATE TABLE gdaAuvend.panjea_codici_iva (
	codiceIva_id INT(11) NOT NULL,
	aliquota_iva DECIMAL(19,2) NOT NULL
)

-- ARTICOLI
-- Categoria commerciale id fissa ad 1
INSERT INTO maga_articoli (version, userInsert, codiceAzienda, tipoArticolo, codice, abilitato, ivaAlternativa, descrizioneLinguaAziendale, codiceIva_id, categoria_id, unitaMisura_id, 
articoloLibero, numeroDecimaliQta, categoriaContabileArticolo_id, unitaMisuraQtaMagazzino_id,  provenienzaPrezzoArticolo, numeroDecimaliPrezzo, codiceLinguaAzienda, timeStamp, tipoLotto,  distinta, 
 categoriaCommercialeArticolo_id, lottoFacoltativo, gestioneSchedaArticolo, leadTime, mrp, gestioneQuantitaZero, stampaLotti, codiceInterno, produzione, TIPO, resa, somministrazione, proprietaCliente, datiVending_id) 
select 0, 'auvend', 'gda', 0, prodotto, obsoleto, 0, gdaAuvend.prodotti.descrizione, gdaAuvend.panjea_codici_iva.codiceIva_id, maga_categorie.id, anag_unita_misura.id,
 0, 2, NULL, anag_unita_misura.id, 0, 6, 'IT', UNIX_TIMESTAMP(), 0, 0, 
 1, 0, 0, 0, 0, 0, 0, NULL, 0, 'A', 1, 0, 0, NULL
from gdaAuvend.prodotti
join gdaAuvend.panjea_codici_iva on gdaAuvend.panjea_codici_iva.aliquota_iva = gdaAuvend.prodotti.Aliquota_IVA
join maga_categorie on maga_categorie.codice = gdaAuvend.prodotti.Categoria
join anag_unita_misura on anag_unita_misura.codice = gdaAuvend.prodotti.Unita_misura
where gdaAuvend.prodotti.prodotto != '.'

-- ARTICOLI - TIPI MODELLO
INSERT INTO manu_prodotti_collegati (TIPO_PRODOTTO, timeStamp, userInsert, version, tipo, articolo_id, installazione_id, modello_id, tipoModello_id, datiVendingDistributore_id) 
select 'PT', UNIX_TIMESTAMP(), 'auvend', 0, 0, maga_articoli.id, NULL, NULL, vend_tipi_modello.id, NULL
from gdaAuvend.riclassificazionimodelliprodotti ric
join vend_tipi_modello on vend_tipi_modello.codice = ric.Riclassificazione
join maga_articoli on maga_articoli.codice = ric.Prodotto


ALTER TABLE auvend.`listinoprodottidettaglio`
	ADD INDEX `Prodotto` (`Prodotto`);
	
ALTER TABLE gdaAuvend.`listinoprodotti`
	ADD INDEX `listino` (`listino`);

ALTER TABLE gdaAuvend.`installazioni`
	ADD INDEX `listino_prodotti_1` (`listino_prodotti_1`);

ALTER TABLE gdaAuvend.`installazioni`
	ADD INDEX `listino_prodotti_2` (`listino_prodotti_2`);
	
ALTER TABLE gdaAuvend.`installazioni`
	ADD INDEX `listino_prodotti_3` (`listino_prodotti_3`);
	
--LISTINI (sono tutti con iva)
insert into maga_listini(version,userInsert,codiceAzienda,codice,descrizione,codiceValuta,iva,tipoListino,aggiornaDaUltimoCosto)
select 0,'auvend','gda',l.Listino,ifnull(l.Descrizione,''),'EUR',false,0,0 
from gdaAuvend.listinoprodotti l
left join (
	select lis.Listino 
	from gdaAuvend.listinoprodotti lis
	left join gdaAuvend.installazioni in1 on in1.Listino_prodotti_1 = lis.Listino
	left join gdaAuvend.installazioni in2 on in2.Listino_prodotti_2 = lis.Listino
	left join gdaAuvend.installazioni in3 on in3.Listino_prodotti_3 = lis.Listino
	where in1.installazione is null and in2.installazione is null and in3.installazione is null
) as ListiniNonUsati on ListiniNonUsati.listino = l.listino
where ListiniNonUsati.listino is null

--VERSIONI LISTINI
insert into maga_versioni_listino (version,userInsert,codice,dataVigore,listino_id)
select 0,'auvend',1,'20150101',id from maga_listini

--RIGHE LISTINO
insert into maga_righe_listini (version,userInsert,versioneListino_id,articolo_id,numeroDecimaliPrezzo,timeStamp,percentualeRicarico,prezzoChiave)
SELECT
0,
'auvend',
vl.id,
art.id,
6,
UNIX_TIMESTAMP(),
0,
gdaAuvend.listinoprodottidettaglio.Prezzo_Chiave
FROM gdaAuvend.listinoprodottidettaglio, maga_articoli art, maga_listini l
inner join maga_versioni_listino vl on vl.listino_id=l.id
WHERE l.codice=auvend.listinoprodottidettaglio.Listino
and art.codice=auvend.listinoprodottidettaglio.Prodotto

--SCAGLIONI
insert into maga_scaglioni_listini
(
   timeStamp,userInsert,version,prezzo,quantita,rigaListino_id
)
SELECT
UNIX_TIMESTAMP(),
'auvend',
0,
dett.Prezzo_Moneta,
999999999,
rl.id
FROM gdaAuvend.listinoprodottidettaglio dett
join maga_articoli art on art.codice=dett.Prodotto and art.codiceAzienda='gda'
join maga_listini l on l.codice=dett.Listino
inner join maga_versioni_listino vl on vl.listino_id=l.id
inner join maga_righe_listini rl on rl.versioneListino_id=vl.id and rl.articolo_id = art.id

-- Installazioni
INSERT INTO manu_installazioni (id, timeStamp, userInsert, version, codice, autoAlimentato, pubblico, tipoContrattoInstallazione, descrizione, deposito_id)
select 0, UNIX_TIMESTAMP(), 'auvend', 0, installazione, autoAlimentato, posto_pubblico, 
case tipo_contratto when 'R' then 0 when 'C' then 1 else 2 end, inst.Ragione_sociale, dep.id 
from gdaAuvend.installazioni inst
join gdaAuvend.clienti cli on cli.cliente = inst.cliente 
join anag_sedi_anagrafica sa on sa.sedeAuVend = inst.sede and sa.clienteAuVend = cli.codice_contabile
join anag_sedi_entita se on se.sede_anagrafica_id = sa.id
join anag_depositi dep on dep.sedeEntita_id = se.id and dep.TIPO = 'IN'

ALTER TABLE gdaAuvend.`caricatori`
	ADD INDEX `caricatore` (`caricatore`);
	
ALTER TABLE gdaAuvend.`installazioni`
	ADD INDEX `instcaricatore` (`caricatore`);
	
ALTER TABLE gdaAuvend.`pagamenti`
	ADD INDEX `pagamento` (`pagamento`);
	
-- Imposto caricatore su installazioni
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join gdaAuvend.caricatori car on insa.caricatore = car.Caricatore
join manu_operatori op on op.caricatore = 1 and op.codice = car.caricatore
set ins.caricatore_id = op.id

-- Imposto distributore su installazioni
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join vend_dati_vending_distributore vd on vd.matricola = insa.matricola and insa.Matricola <> '.'
join maga_articoli art on art.datiVending_id = vd.id
set ins.articolo_id = art.id

-- Imposto documento RDF (Rifornimento da fatturare) su installazioni
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join gdaAuvend.distributori di on di.matricola = insa.Matricola
join gdaAuvend.clienti cli on cli.cliente = insa.cliente
join gdaAuvend.pagamenti pag on pag.pagamento = cli.pagamento
join docu_tipi_documento td on td.codice = 'RDF'
join maga_tipi_area_magazzino tam on tam.tipoDocumento_id
set ins.tipoAreaMagazzino_id = tam.id
where di.Tipo_gestione = 1 and pag.Da_fatturare <> 0

-- Imposto documento RDA (Rifornimento D.A.) su installazioni
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join gdaAuvend.distributori di on di.matricola = insa.Matricola
join docu_tipi_documento td on td.codice = 'RDA'
join maga_tipi_area_magazzino tam on tam.tipoDocumento_id = td.id
set ins.tipoAreaMagazzino_id = tam.id
where di.Tipo_gestione in (2,3,4)

-- Imposto documento RFC (Rifornimento fap a corrispettivo) su installazioni
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join gdaAuvend.distributori di on di.matricola = insa.Matricola
join gdaAuvend.clienti cli on cli.cliente = insa.cliente
join gdaAuvend.pagamenti pag on pag.pagamento = cli.pagamento
join docu_tipi_documento td on td.codice = 'RFC'
join maga_tipi_area_magazzino tam on tam.tipoDocumento_id = td.id
set ins.tipoAreaMagazzino_id = tam.id
where di.Tipo_gestione = 1 and pag.Da_fatturare = 0

-- Imposto documento RND (Rifornimento non D.A.) su installazioni
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join gdaAuvend.distributori di on di.matricola = insa.Matricola
join gdaAuvend.clienti cli on cli.cliente = insa.cliente
join gdaAuvend.pagamenti pag on pag.pagamento = cli.pagamento
join docu_tipi_documento td on td.codice = 'RND'
join maga_tipi_area_magazzino tam on tam.tipoDocumento_id = td.id
set ins.tipoAreaMagazzino_id = tam.id
where di.Tipo_gestione not in (1,2,3,4)

-- Inserisco le causali di installazione e ritiro
INSERT INTO `manu_causali_installazioni` (`timeStamp`, `userInsert`, `version`, `codice`, `descrizione`, `ordinamento`, `tipoInstallazione`) 
VALUES (UNIX_TIMESTAMP(), 'auvend', 0, 'PI', 'PRIMA INSTALLAZIONE', 1, 0);

INSERT INTO `manu_causali_installazioni` (`timeStamp`, `userInsert`, `version`, `codice`, `descrizione`, `ordinamento`, `tipoInstallazione`) 
VALUES (UNIX_TIMESTAMP(), 'auvend', 0, 'I', 'INSTALLAZIONE', 2, 1);

INSERT INTO `manu_causali_installazioni` (`timeStamp`, `userInsert`, `version`, `codice`, `descrizione`, `ordinamento`, `tipoInstallazione`) 
VALUES (UNIX_TIMESTAMP(), 'auvend', 0, 'R', 'RITIRO', 3, 2);

INSERT INTO `manu_causali_installazioni` (`timeStamp`, `userInsert`, `version`, `codice`, `descrizione`, `ordinamento`, `tipoInstallazione`) 
VALUES (UNIX_TIMESTAMP(), 'auvend', 0, 'RD', 'RITIRO DEFINITIVO', 4, 3);

-- Inserisco il documento di installazione
INSERT INTO `docu_documenti` (`version`, `userInsert`, `codice`, `codiceAzienda`, `dataDocumento`, `entita_id`, `tipo_documento_id`, `sedeEntita_id`, `codiceOrder`)
select 0, 'auvend', insa.Installazione,'gda', str_to_date('01/01/2010', '%d/%m/%Y'), ent.id, tdoc.id, dep.sedeEntita_id, lpad(insa.Installazione, 8, '0')
from gdaauvend.installazioni insa
join manu_installazioni ins on ins.codice = insa.Installazione
join anag_depositi dep on ins.deposito_id = dep.id
join anag_entita ent on ent.id = dep.entita_id
join docu_tipi_documento tdoc on tdoc.codice = 'MD'
where insa.Matricola <> '.'

-- Inserisco l'area installazione
INSERT INTO `manu_area_installazioni` (`timeStamp`, `userInsert`, `version`, `note`, `stato`, `depositoOrigine_id`, `documento_id`, `tipoAreaInstallazione_id`) 
select UNIX_TIMESTAMP(), 'auvend', 0, null, 0, 1, doc.id, tai.id
from docu_documenti doc
join manu_tipi_area_installazioni tai on tai.tipoDocumento_id = doc.tipo_documento_id

-- Inserisco le righe d'installazione
INSERT INTO `manu_righe_installazione` (`timeStamp`, `userInsert`, `version`, `ordinamento`, `tipoMovimento`, `areaInstallazione_id`, `articoloInstallazione_id`, `causaleInstallazione_id`, `installazione_id`)
select UNIX_TIMESTAMP(), 'auvend', 0, 1, 0, mai.id, dist.id, ca.id, ins.id
from manu_area_installazioni mai
join docu_documenti doc on doc.id = mai.documento_id
join gdaAuvend.installazioni insa on insa.installazione = doc.codice
join manu_installazioni ins on ins.codice = insa.Installazione
join maga_articoli dist on dist.codice = insa.Matricola
join manu_causali_installazioni ca on ca.codice = 'PI'

-- Imposto sul distributore l'id evadts
update vend_dati_vending_distributore dd
join gdaauvend.installazioni ins on ins.Matricola = dd.matricola
set dd.idEvaDts = ins.Id_sistema_pagamento

-- Imposto la gettoniera
update vend_dati_vending_distributore dd
join gdaauvend.installazioni ins on ins.Matricola = dd.matricola
join vend_sistemi_elettronici el on el.codice = ins.Gettoniera
set dd.gettoniera_id = el.id

-- Imposto il sistema di pagamento
update vend_dati_vending_distributore dd
join gdaauvend.installazioni ins on ins.Matricola = dd.matricola
join vend_sistemi_elettronici el on el.codice = ins.Sistema_pagamento
set dd.sistemaPagamento_id = el.id

-- Imposto lettore di banconote
update vend_dati_vending_distributore dd
join gdaauvend.installazioni ins on ins.Matricola = dd.matricola
join vend_sistemi_elettronici el on el.codice = ins.Lettore_banconote
set dd.lettoreBanconote_id = el.id

-- ASL
insert into vend_asl (timestamp, userinsert, version, codice, descrizione, indirizzo)
select UNIX_TIMESTAMP(), 'auvend', 0, asl.asl, asl.Descrizione, asl.Indirizzo
from gdaauvend.asl asl
where asl.ASL <> '.'

-- Listini su installazioni
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join maga_listini li on li.codice = insa.listino_prodotti_1
set ins.listino_id = li.id
where insa.listino_prodotti_1 <> '.'

-- Listino alternativo
-- Imposto il secondo livello dove non c'è impostato il listino di terzo livello
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join maga_listini li on li.codice = insa.listino_prodotti_2
set ins.listino_alternativo_id = li.id
where insa.listino_prodotti_2 <> '.' and insa.listino_prodotti_3 = '.'

-- Imposto il terzo livello dove non c'è impostato il listino di secondo livello
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join maga_listini li on li.codice = insa.listino_prodotti_3
set ins.listino_alternativo_id = li.id
where insa.listino_prodotti_3 <> '.' and insa.listino_prodotti_2 = '.'

-- Creo la tabella per il merge del secondo e terzo listino
create table panjea_merge_listini (
	listino2 varchar(6),
	listino3 varchar(6),
	listino_nuovo varchar(10)
)

-- Inserisco le coppie di listini
SET @lis = 0;
insert into panjea_merge_listini
select ins.Listino_prodotti_2, ins.Listino_prodotti_3, concat('NEW', @lis:=@lis+1)
from installazioni ins 
where ins.Listino_prodotti_2 <> '.' and ins.Listino_prodotti_3 <> '.'
group by ins.Listino_prodotti_2, ins.Listino_prodotti_3;

-- creo i nuovi listini
insert into maga_listini(version,userInsert,codiceAzienda,codice,descrizione,codiceValuta,iva,tipoListino,aggiornaDaUltimoCosto)
select 0,'auvend','gda',l.listino_nuovo,concat('merge ', l.listino2, ' ', l.listino3),'EUR',false,0,0 
from gdaAuvend.panjea_merge_listini l

--VERSIONI LISTINI
insert into maga_versioni_listino (version,userInsert,codice,dataVigore,listino_id)
select 0,'auvend',1,'20150101',id from maga_listini where maga_listini.codice like 'NEW%'

--RIGHE LISTINO
insert into maga_righe_listini (version,userInsert,versioneListino_id,articolo_id,numeroDecimaliPrezzo,timeStamp,percentualeRicarico,prezzoChiave)
SELECT
0,
'auvend',
vl.id,
art.id,
6,
UNIX_TIMESTAMP(),
0,
0
FROM gdaauvend.panjea_merge_listini ml
join (
	select det2.Prodotto as Prodotto, ml.listino_nuovo as Listino
	from gdaauvend.panjea_merge_listini ml
	join gdaAuvend.listinoprodottidettaglio det2 on det2.Listino = ml.listino2
	union
	select det3.Prodotto, ml.listino_nuovo
	from gdaauvend.panjea_merge_listini ml
	join gdaAuvend.listinoprodottidettaglio det3 on det3.Listino = ml.listino3
) as prodotti on prodotti.listino = ml.listino_nuovo
join maga_articoli art on art.codice=prodotti.Prodotto 
join maga_listini l on l.codice=prodotti.Listino
inner join maga_versioni_listino vl on vl.listino_id=l.id

-- Aggiorno le righe listino con il prezzo del listino2

update maga_righe_listini rlis
join maga_articoli art on art.id = rlis.articolo_id
join maga_versioni_listino vlis on vlis.id = rlis.versioneListino_id
join maga_listini lis on lis.id = vlis.listino_id
join gdaauvend.panjea_merge_listini ml on ml.listino_nuovo = lis.codice
join gdaauvend.listinoprodottidettaglio dett on dett.Listino = ml.listino2 and dett.Prodotto = art.codice
set rlis.prezzoChiave = dett.Prezzo_chiave

-- Aggiorno le righe listino con il prezzo del listino3

update maga_righe_listini rlis
join maga_articoli art on art.id = rlis.articolo_id
join maga_versioni_listino vlis on vlis.id = rlis.versioneListino_id
join maga_listini lis on lis.id = vlis.listino_id
join gdaauvend.panjea_merge_listini ml on ml.listino_nuovo = lis.codice
join gdaauvend.listinoprodottidettaglio dett on dett.Listino = ml.listino3 and dett.Prodotto = art.codice
set rlis.prezzoChiave = dett.Prezzo_chiave

--SCAGLIONI
insert into maga_scaglioni_listini
(
   timeStamp,userInsert,version,prezzo,quantita,rigaListino_id
)
SELECT
UNIX_TIMESTAMP(),
'auvend',
0,
0,
999999999,
rl.id
from gdaauvend.panjea_merge_listini ml
join (
	select det2.Prodotto as Prodotto, ml.listino_nuovo as Listino
	from gdaauvend.panjea_merge_listini ml
	join gdaAuvend.listinoprodottidettaglio det2 on det2.Listino = ml.listino2
	union
	select det3.Prodotto, ml.listino_nuovo
	from gdaauvend.panjea_merge_listini ml
	join gdaAuvend.listinoprodottidettaglio det3 on det3.Listino = ml.listino3
) as prodotti on prodotti.listino = ml.listino_nuovo
join maga_articoli art on art.codice=prodotti.Prodotto 
join maga_listini l on l.codice=prodotti.Listino
join maga_versioni_listino vl on vl.listino_id=l.id
join maga_righe_listini rl on rl.versioneListino_id=vl.id and rl.articolo_id = art.id

-- Aggiorno gli scaglioni con il prezzo dal listino2
update maga_scaglioni_listini sl
join maga_righe_listini rlis on rlis.id = sl.rigaListino_id
join maga_articoli art on art.id = rlis.articolo_id
join maga_versioni_listino vlis on vlis.id = rlis.versioneListino_id
join maga_listini lis on lis.id = vlis.listino_id
join gdaauvend.panjea_merge_listini ml on ml.listino_nuovo = lis.codice
join gdaauvend.listinoprodottidettaglio dett on dett.Listino = ml.listino2 and dett.Prodotto = art.codice
set sl.prezzo = dett.Prezzo_moneta

-- Aggiorno gli scaglioni con il prezzo dal listino3
update maga_scaglioni_listini sl
join maga_righe_listini rlis on rlis.id = sl.rigaListino_id
join maga_articoli art on art.id = rlis.articolo_id
join maga_versioni_listino vlis on vlis.id = rlis.versioneListino_id
join maga_listini lis on lis.id = vlis.listino_id
join gdaauvend.panjea_merge_listini ml on ml.listino_nuovo = lis.codice
join gdaauvend.listinoprodottidettaglio dett on dett.Listino = ml.listino3 and dett.Prodotto = art.codice
set sl.prezzo = dett.Prezzo_moneta

-- Imposto i listini nuovi sulle installazioni
update manu_installazioni ins
join gdaauvend.installazioni insau on insau.Installazione = ins.codice
join gdaauvend.panjea_merge_listini ml on insau.Listino_prodotti_2 = ml.listino2 and insau.Listino_prodotti_3 = ml.listino3
join maga_listini lis on lis.codice = ml.listino_nuovo
set ins.listino_alternativo_id = lis.id

-- Importo ubicazioni
ALTER TABLE gdaAuvend.`installazioni`
	ADD INDEX `ubicazione` (`ubicazione`);

INSERT INTO `manu_ubicazioni_installazione` (`timeStamp`, `userInsert`, `version`, `descrizione`)
select unix_timestamp(), 'auvend', 0, ubicazione
from gdaauvend.installazioni
where ubicazione is not null && length(rtrim(ubicazione))>0
group by ubicazione

-- Assegno le ubicazioni alle installazioni
update manu_installazioni ins 
join gdaAuvend.installazioni insa on ins.codice = insa.Installazione 
join manu_ubicazioni_installazione ub on ub.descrizione = insa.ubicazione
set ins.ubicazione_id = ub.id

-- Sede magazzino
INSERT INTO `maga_sedi_magazzino` (`version`, `userInsert`, `sedeEntita_id`, `calcoloSpese`, `raggruppamentoBolle`, `sedeDiRifatturazione`, `stampaPrezzo`, `addebito`)
select 0, 'auvend', se.id, 0, 0, 0, 0, 0
from anag_sedi_entita se
