CREATE TABLE $TABLE_VALORIZZAZIONE$ 
(
   idArticolo int(11) DEFAULT NULL,
   codiceArticolo varchar(30) DEFAULT NULL,
   descrizioneArticolo varchar(255) DEFAULT NULL,
   campoLibero varchar(10) DEFAULT NULL,
   numeroPezzi varchar(30) DEFAULT NULL,
   idCategoria int(11) DEFAULT NULL,
   codiceCategoria varchar(255) DEFAULT NULL,
   descrizioneCategoria varchar(255) DEFAULT NULL,
   codiceFornitoreAbituale int(11) DEFAULT NULL,
   idDeposito int(11) DEFAULT NULL,
   codiceDeposito varchar(10) DEFAULT NULL,
   descrizioneDeposito varchar(30) DEFAULT NULL,
   data_inventario DATE DEFAULT NULL,
   area_inventario_id int(11) DEFAULT NULL,
   qtaInventario DOUBLE(19,6) DEFAULT NULL,
   giacenza DOUBLE(19,6) DEFAULT NULL,
   qtaCarico DOUBLE(19,6) DEFAULT NULL,
   qtaScarico DOUBLE(19,6) DEFAULT NULL,
   qtaCaricoAltro DOUBLE(19,6) DEFAULT NULL,
   qtaScaricoAltro DOUBLE(19,6) DEFAULT NULL,
   importoCarico DECIMAL(19,6) DEFAULT 0,
   importoScarico DECIMAL(19,6) DEFAULT 0,
   costo DECIMAL(19,6) DEFAULT 0,
   area_costoUltimo_id int(11) DEFAULT 0,
   data_costoUltimo DATE DEFAULT NULL,
   scorta DOUBLE(19,6) DEFAULT NULL,
   PRIMARY KEY (idArticolo,idDeposito),
   KEY `idart` (`idArticolo`)
) 
ENGINE=MyIsam DEFAULT CHARSET=latin1
;
insert into $TABLE_VALORIZZAZIONE$ 
select 
art.id as articolo,
art.codice as codArt,
art.descrizioneLinguaAziendale as descArt,
art.campoLibero,
null as numeroPezzi,
cat.id as categoria,
cat.codice as codCat,
cat.descrizioneLinguaAziendale as descCat,
(select ent.codice from maga_codici_articolo_entita cent inner join anag_entita ent on ent.id=cent.entita_id where cent.articolo_id=art.id and cent.entitaPrincipale=1) as codiceFornitoreAbituale,
dep.id as deposito,
dep.codice as codDep,
dep.descrizione as descDep,
null as data_inventario,
null as area_magazzino_id,
null as qta,
null as giacenza,
null as qtaCarico,
null as qtaScarico,
null as qtaCaricoAltro,
null as sqtaScaricoAltro,
0 as importoCarico,
0 as importoScarico,
0 as costo,
null as area_costoUltimo_id,
null as data_costoUltimo,
artdep.scorta as scorta 
 from maga_articoli art inner join maga_categorie cat on cat.id=art.categoria_id cross join anag_depositi dep 
left join maga_articolo_deposito artdep on artdep.deposito_id=dep.id and art.id=artdep.articolo_id 
$WHERE_GIACENZA_ARTICOLO_DEPOSITO$ 
;
