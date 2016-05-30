/* creo la tablella */
CREATE TABLE $TBL_IMPORT_ARTICOLI_TMP$ (codiceArticolo varchar(255),barcode varchar(13),quantita decimal(19,2),prezzo decimal(19,6));

/* importo il file */
LOAD DATA LOCAL INFILE '$FILE_TO_IMPORT$' INTO TABLE $TBL_IMPORT_ARTICOLI_TMP$ FIELDS TERMINATED BY '|';

/* aggiungo la colonna id articolo */
ALTER TABLE $TBL_IMPORT_ARTICOLI_TMP$ ADD idArticolo int NULL;

/* aggiorno la colonna */
UPDATE $TBL_IMPORT_ARTICOLI_TMP$ import inner join maga_articoli art on import.codiceArticolo=art.codice set import.idArticolo=art.id where import.codiceArticolo is not null and import.codiceArticolo <> '';

/* aggiorno la colonna per i barcode */
UPDATE $TBL_IMPORT_ARTICOLI_TMP$ import inner join maga_articoli art on import.barcode=art.barCode set import.idArticolo=art.id where import.barcode is not null and import.barcode <> '';

/* aggiorno i prezzi */
UPDATE maga_inventari_articolo inv inner join $TBL_IMPORT_ARTICOLI_TMP$ import on inv.articolo_id=import.idArticolo set inv.importo=import.prezzo where inv.deposito_id = $IDDEPOSITO$;

/* inserisco le quantità rilevate */
INSERT INTO maga_righe_inventario_articolo (id,timeStamp,userInsert,version,data,quantita,inventarioArticolo_id) 
SELECT null,UNIX_TIMESTAMP(now()),'$user$',0,now(),import.quantita,inv.id from $TBL_IMPORT_ARTICOLI_TMP$ import inner join maga_inventari_articolo inv on inv.articolo_id=import.idArticolo where inv.deposito_id = $IDDEPOSITO$;