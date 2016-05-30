set @idx=0;
set @cur_articolo=0;

update $TABLE_VALORIZZAZIONE$ 
inner join
(
	         SELECT         	         
	         r.idArticolo,        
	         r.area_costoUltimo_id,
	         r.ultimoCosto,          
	         IF(@cur_articolo!=r.idArticolo,@idx:=1,@idx:=@idx+1) AS row_index,	         
	          IF
	         (
	            @cur_articolo!=r.idArticolo,@cur_articolo:=r.idArticolo,0
	         )
	         AS discard1
	         FROM
	         (
	         select
			   r.importoInValutaAziendaNetto as ultimoCosto,			   
		        am.id as area_costoUltimo_id,
		        r.articolo_id as idArticolo,
		        max(am.dataRegistrazione) AS maxData 	        	        
		   	from maga_righe_magazzino r
		     inner join maga_area_magazzino am on am.id=r.areaMagazzino_id
		     where r.importoInValutaAziendaNetto<>0
		     	and am.aggiornaCostoUltimo=true	   		
		   		and am.dataRegistrazione<='$DATA_REGISTRAZIONE$'
		   		GROUP BY am.id, r.articolo_id
		   		ORDER BY am.dataRegistrazione desc,am.tipoOperazione asc,am.timestamp            
	         )
	         AS r
	       HAVING row_index <= 1
	) as tabUltimoCosto 
on tabUltimoCosto.idArticolo=$TABLE_VALORIZZAZIONE$.idArticolo 
set $TABLE_VALORIZZAZIONE$.costo=tabUltimoCosto.ultimoCosto ,$TABLE_VALORIZZAZIONE$.area_costoUltimo_id=tabUltimoCosto.area_costoUltimo_id where tabUltimoCosto.ultimoCosto is not null;