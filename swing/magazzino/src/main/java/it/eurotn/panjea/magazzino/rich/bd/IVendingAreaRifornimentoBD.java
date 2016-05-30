package it.eurotn.panjea.magazzino.rich.bd;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.rate.domain.AreaRate;

public interface IVendingAreaRifornimentoBD {

    /**
     * salva area magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @param areaRate
     *            {@link AreaRate}
     * @param areaRifornimento
     *            area rifornimento
     * @param forzaSalvataggio
     *            forzar o meno il salvataggio
     * @return {@link AreaMagazzinoFullDTO}
     * @throws DocumentoAssenteBloccaException
     *             .
     * @throws DocumentoAssenteAvvisaException
     *             .
     * @throws DocumentiEsistentiPerAreaMagazzinoException
     *             .
     */
    AreaMagazzinoFullDTO salvaAreaMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate, Object areaRifornimento,
            boolean forzaSalvataggio) throws DocumentoAssenteBloccaException, DocumentoAssenteAvvisaException,
                    DocumentiEsistentiPerAreaMagazzinoException;

    /**
     * salva area ordine.
     *
     * @param areaOrdine
     *            {@link AreaOrdine}
     * @param areaRate
     *            {@link AreaRate}
     * @param areaRifornimento
     *            area rifornimento
     *
     * @return {@link AreaOrdineFullDTO}
     */
    AreaOrdineFullDTO salvaAreaOrdine(AreaOrdine areaOrdine, AreaRate areaRate, Object areaRifornimento);
}
