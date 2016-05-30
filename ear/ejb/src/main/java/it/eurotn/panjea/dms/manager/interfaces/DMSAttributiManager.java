package it.eurotn.panjea.dms.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoPanjea;
import it.eurotn.panjea.dms.mdb.AttributoAggiornamentoDTO.TipoAttributo;

@Local
public interface DMSAttributiManager {
    /**
     *
     * @param tipoAttributo
     *            tipo attributo
     * @param attributi
     *            attributo da aggiornare
     */
    void aggiornaAttributi(TipoAttributo tipoAttributo, List<AttributoAllegatoPanjea> attributi);

    /**
     * @param tipoAttributo
     *            tipo attributo
     * @param attributo
     *            attributo da aggiornare
     */
    void aggiornaAttributo(TipoAttributo tipoAttributo, AttributoAllegatoPanjea attributo);
}
