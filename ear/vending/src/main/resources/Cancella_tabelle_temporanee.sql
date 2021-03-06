/****** Object:  StoredProcedure [dbo].[X_Cancella_tabelle_temporanee]    Script Date: 31/03/2016 14:54:56 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[X_Cancella_tabelle_temporanee] AS
BEGIN
DECLARE @Nome varchar(50)
DECLARE Elenco CURSOR FOR
SELECT name FROM sysobjects WHERE type = 'U' AND status>=0 and (name like 'TRepor_%' or name like 'I00___%' or name like 'V00__%' or name like 'T00___%')
OPEN Elenco
FETCH Elenco INTO @Nome
WHILE @@FETCH_STATUS=0
BEGIN
EXECUTE ('drop table '+@Nome)
FETCH Elenco INTO @Nome
END
CLOSE Elenco
DEALLOCATE Elenco

drop table MovimentiCassaTestataStorico
drop table MovimentiCassaSaldiStorico
drop table MovimentiCassaRigheStorico
drop table MovimentiCassaIncassiStorico
drop table AuvendLog 

END
