package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino.EntitaTipoRicercaEnum;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino.ProvenienzaPrezzoManutenzioneListino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

public class ManutenzioneListinoQueryBuilder {

    private static final Logger LOGGER = Logger.getLogger(ManutenzioneListinoQueryBuilder.class);

    private ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino;

    /**
     *
     * @param parametriRicercaManutenzioneListino
     *            parametri della ricerca
     * @param panjeaDAO
     *            dao per eventuali query per preparare l'importazione
     */
    public ManutenzioneListinoQueryBuilder(
            final ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino, final PanjeaDAO panjeaDAO) {
        this.parametriRicercaManutenzioneListino = parametriRicercaManutenzioneListino;
        if (parametriRicercaManutenzioneListino
                .getProvenienzaPrezzoManutenzioneListino() == ProvenienzaPrezzoManutenzioneListino.FILE_ESTERNO) {
            importaFileEsterno(panjeaDAO);
        }
    }

    /**
     * @return sql da eseguire per creare il carrello della manutenzione listino
     */
    public String getSql() {
        SorgenteArticoli sorgenteArticoli = new SorgenteArticoli();

        if (parametriRicercaManutenzioneListino.getEntita() != null
                && parametriRicercaManutenzioneListino.getEntitaTipoRicerca() == EntitaTipoRicercaEnum.DOCUMENTO) {
            sorgenteArticoli.addFilter(new EntitaDocumentoFiltro());
        }

        if (parametriRicercaManutenzioneListino
                .getProvenienzaPrezzoManutenzioneListino() == ProvenienzaPrezzoManutenzioneListino.FILE_ESTERNO) {
            sorgenteArticoli.addFilter(new FileEsternoArticoloFiltro(parametriRicercaManutenzioneListino));
        }

        if (parametriRicercaManutenzioneListino.getEntita() != null && parametriRicercaManutenzioneListino
                .getEntitaTipoRicerca() == EntitaTipoRicercaEnum.ANAGRAFICAARTICOLO) {
            sorgenteArticoli.addFilter(new EntitaAnagraficaArticoloFiltro());
        }

        if (parametriRicercaManutenzioneListino.getCategorie() != null
                && !parametriRicercaManutenzioneListino.getCategorie().isEmpty()
                && !parametriRicercaManutenzioneListino.isTutteCategorie()) {
            sorgenteArticoli.addFilter(new CategoriaFiltro());
        }

        if (parametriRicercaManutenzioneListino.getArticoli() != null
                && !parametriRicercaManutenzioneListino.getArticoli().isEmpty()) {
            sorgenteArticoli.addFilter(new ArticoloFiltro(parametriRicercaManutenzioneListino));
        }

        if (parametriRicercaManutenzioneListino.getVersioneListino() != null) {
            sorgenteArticoli.addFilter(new ListinoFiltro());
        }

        StringBuilder sb = new StringBuilder(
                "insert into maga_riga_manutenzione_listino(articolo_id,userInsert,version,codiceAzienda,descrizione,numero,numeroDecimali,userManutenzione,provenienzaDecimali,quantita,listino_id)");
        sb.append(sorgenteArticoli.getSql(parametriRicercaManutenzioneListino));
        return sb.toString();
    }

    /**
     * Importa il file esterno in una tabella
     *
     * @param panjeaDAO
     *            dao
     */
    private void importaFileEsterno(PanjeaDAO panjeaDAO) {
        File fileImport = null;
        try {
            fileImport = File.createTempFile("listino" + parametriRicercaManutenzioneListino.getUserManutenzione()
                    + parametriRicercaManutenzioneListino.getNumeroInserimento(), ".csv");
            FileUtils.writeByteArrayToFile(fileImport, parametriRicercaManutenzioneListino.getDataFileEsterno());
        } catch (IOException e) {
            LOGGER.error("-->errore nel creare il file temporaneo per salvare il listino", e);
            throw new GenericException("-->errore nel creare il file temporaneo per salvare il listino", e);
        }
        String fileImportPath = fileImport.getAbsolutePath();
        String tableName = parametriRicercaManutenzioneListino.getTableNameFileEsterno();
        panjeaDAO.prepareNativeQuery("DROP TABLE IF EXISTS " + tableName).executeUpdate();
        panjeaDAO
                .prepareNativeQuery("CREATE TABLE `" + tableName
                        + "` ( `COD_ART` TEXT NULL,`PREZZO` DECIMAL(10,6) NULL) COLLATE='utf8_general_ci' ENGINE=InnoDB;")
                .executeUpdate();
        SQLQuery queryImport = panjeaDAO.prepareNativeQuery("LOAD DATA LOCAL INFILE '" + fileImportPath.replace("\\", "\\\\")
                + "' INTO TABLE " + tableName + " FIELDS TERMINATED BY '#';");
        queryImport.executeUpdate();
    }
}