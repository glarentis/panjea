SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER OFF
GO


ALTER PROCEDURE [dbo].[XConvDb2](@Operazione varchar(1), @DBWrk varchar(255), @TblName varchar(30)=null)
AS
BEGIN
 DECLARE @Tabella varchar(255)
 DECLARE @Tabella1 varchar(255)
 DECLARE @Colonna varchar(255)
 DECLARE @Ele varchar(255)
 DECLARE @Ele1 varchar(255)
 DECLARE @Ele2 varchar(255)
 DECLARE @Ele3 varchar(255)
 DECLARE @Ele4 varchar(255)
 DECLARE @Ele5 varchar(255)
 DECLARE @Ele6 varchar(255)
 DECLARE @Ele7 varchar(255)
 DECLARE @Ele8 varchar(255)
 DECLARE @Ele9 varchar(255)
 DECLARE @Ele10 varchar(255)
 DECLARE @Originali int
 
 SET NOCOUNT ON
 SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
 
  CREATE TABLE #TabOri(Tabella varchar(255),
   Colonna varchar(255))
 CREATE TABLE #TabDup(Tabella varchar(255),
  Colonna varchar(255))
 CREATE TABLE #TabChk(Tabella varchar(255),
  Originali int,
  Copiati int)
 CREATE TABLE #TabCount(Numero INT)
 INSERT INTO #TabOri
  SELECT O.name,
    C.name
  FROM  sysobjects O,
   syscolumns C
  WHERE O.type="U"
        AND O.ID=C.ID
        AND O.name NOT LIKE "dt%" 
        AND O.name NOT LIKE "TP_%" 
        AND O.name NOT LIKE "[ICV][0-9][0-9][0-9][0-9]%" 
 EXECUTE ("INSERT INTO #TabDup SELECT O.name, C.name FROM "+@DBWrk+"..sysobjects O, "+@DBWrk+"..syscolumns C WHERE O.type='U' AND O.ID=C.ID AND O.name NOT LIKE 'dt%'")
 
 
 IF @TblName IS NOT NULL
   DELETE FROM #TabOri
   WHERE Tabella NOT LIKE @TblName
  DECLARE EleIN CURSOR FOR
   SELECT Tabella
   FROM #TabOri
   GROUP BY Tabella
  OPEN EleIn
  WHILE (0=0)
  BEGIN
   FETCH EleIn INTO @Tabella
   IF @@FETCH_STATUS<>0
    BREAK
   PRINT @Tabella
   EXECUTE ("INSERT INTO openquery(MYSQL, 'select * from "+@Tabella+"') SELECT * FROM "+@Tabella)
  END
  CLOSE EleIn
  DEALLOCATE EleIn
END

