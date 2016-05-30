set @idx=0;
set @cur_class=0;
update $TABLE_VALORIZZAZIONE$ join
(
   select
   r.areaMagazzino_id,
   r.articolo_id as idArticolo ,
   sum(r.qtaMagazzino) as qta ,
   r.importoInValutaAziendaNetto as costo,
   inventari.depositoOrigine_id,
   inventari.maxData
   from maga_righe_magazzino r
   inner join
   (
      SELECT
      areaMagazzinoId,depositoOrigine_id,maxData
      FROM
      (
         SELECT
         r.depositoOrigine_id,
         r.areaMagazzinoId,
         r.maxData,
         IF(@cur_class!=r.depositoOrigine_id,@idx:=1,@idx:=@idx+1) AS row_index,
         IF
         (
            @cur_class!=r.depositoOrigine_id,@cur_class:=r.depositoOrigine_id,0
         )
         AS discard
         FROM
         (
            SELECT
            am.depositoOrigine_id,
            am.id as areaMagazzinoId,
            max(am.dataRegistrazione) AS maxData
            FROM maga_area_magazzino am
            where am.tipoOperazione=0
            and am.dataRegistrazione<='$DATA_REGISTRAZIONE$'
            GROUP BY am.id, am.depositoOrigine_id
            ORDER BY am.depositoOrigine_id, maxData DESC
         )
         AS r HAVING row_index <= 1
      )
      AS r2
   )
   as inventari on inventari.areaMagazzinoId=r.areaMagazzino_id
   where r.TIPO_RIGA='A' group by r.articolo_id,r.areaMagazzino_id order by null
)
as tabQta on tabQta.depositoOrigine_id=$TABLE_VALORIZZAZIONE$.idDeposito and tabQta.idArticolo=$TABLE_VALORIZZAZIONE$.idArticolo  set 
$TABLE_VALORIZZAZIONE$.qtaInventario=tabQta.qta,
$TABLE_VALORIZZAZIONE$.costo=tabQta.costo,
$TABLE_VALORIZZAZIONE$.data_inventario=tabQta.maxData,
$TABLE_VALORIZZAZIONE$.area_inventario_id=areaMagazzino_id
;
set @idx=0;
set @cur_class=0;
update $TABLE_VALORIZZAZIONE$ join 
(
	 SELECT 
	 r.depositoOrigine_id,
	 r.areaMagazzinoId,
	 r.maxData,
	 IF(@cur_class!=r.depositoOrigine_id,@idx:=1,@idx:=@idx+1) AS row_index,
	 IF
	 (
	    @cur_class!=r.depositoOrigine_id,@cur_class:=r.depositoOrigine_id,0
	 )
	 AS discard 
	 FROM 
	 (
	    SELECT 
	    am.depositoOrigine_id,
	    am.id as areaMagazzinoId,
	    max(am.dataRegistrazione) AS maxData 
	    FROM maga_area_magazzino am 
	    where am.tipoOperazione=0 
	    and am.dataRegistrazione<='$DATA_REGISTRAZIONE$' 
	    GROUP BY am.id, am.depositoOrigine_id 
	    ORDER BY am.depositoOrigine_id, maxData DESC
	 )
	 AS r HAVING row_index <= 1
) as tabQta on tabQta.depositoOrigine_id=$TABLE_VALORIZZAZIONE$.idDeposito 
set $TABLE_VALORIZZAZIONE$.qtaInventario=0,
$TABLE_VALORIZZAZIONE$.costo=0,
$TABLE_VALORIZZAZIONE$.data_inventario=tabQta.maxData,
$TABLE_VALORIZZAZIONE$.area_inventario_id=areaMagazzinoId
 where $TABLE_VALORIZZAZIONE$.data_inventario is null 
;
update $TABLE_VALORIZZAZIONE$ join
(
   select 
   mov.articolo_id as idArticolo,
   mov.deposito_id,
   sum(mov.qtaMagazzinoCarico) as qtaCarico,
   sum(mov.qtaMagazzinoScarico) as qtaScarico,
   sum(mov.qtaMagazzinoCaricoAltro) as qtaCaricoAltro,
   sum(mov.qtaMagazzinoScaricoAltro) as qtaScaricoAltro,
   sum(mov.importoCaricoAltro + mov.importoCarico) as importoCarico,
   sum(mov.importoScaricoAltro + mov.importoScarico) as importoScarico,
   v.data_inventario
   from dw_movimentimagazzino mov 
   inner join $TABLE_VALORIZZAZIONE$ v on mov.articolo_id=v.idArticolo
   and mov.deposito_id=v.idDeposito
   where
   (
      (mov.dataRegistrazione>=v.data_inventario or  v.data_inventario is null) and
      mov.dataRegistrazione<='$DATA_REGISTRAZIONE$'
   )   
   group by articolo_id,deposito_id order by null
)
tabMovimenti on tabMovimenti.idArticolo=$TABLE_VALORIZZAZIONE$.idArticolo 
and tabMovimenti.deposito_id=$TABLE_VALORIZZAZIONE$.idDeposito set $TABLE_VALORIZZAZIONE$.qtaCarico=tabMovimenti.qtaCarico,
$TABLE_VALORIZZAZIONE$.qtaCaricoAltro=tabMovimenti.qtaCaricoAltro,
$TABLE_VALORIZZAZIONE$.qtaScarico=tabMovimenti.qtaScarico,
$TABLE_VALORIZZAZIONE$.qtaScaricoAltro=tabMovimenti.qtaScaricoAltro,
$TABLE_VALORIZZAZIONE$.importoCarico=tabMovimenti.importoCarico,
$TABLE_VALORIZZAZIONE$.importoScarico=tabMovimenti.importoScarico
;
update $TABLE_VALORIZZAZIONE$ set $TABLE_VALORIZZAZIONE$.giacenza=COALESCE
(
   $TABLE_VALORIZZAZIONE$.qtaInventario,0
)
+ COALESCE
(
   $TABLE_VALORIZZAZIONE$.qtaCarico,0
)
+COALESCE
(
   $TABLE_VALORIZZAZIONE$.qtaCaricoAltro,0
)
-COALESCE
(
   $TABLE_VALORIZZAZIONE$.qtaScarico,0
)
-COALESCE($TABLE_VALORIZZAZIONE$.qtaScaricoAltro,0)
;
