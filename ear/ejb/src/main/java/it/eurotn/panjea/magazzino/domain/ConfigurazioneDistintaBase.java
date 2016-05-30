package it.eurotn.panjea.magazzino.domain;

public class ConfigurazioneDistintaBase extends ConfigurazioneDistinta {
    private static final long serialVersionUID = -2283842573086089214L;

    /**
     * Costruttore.
     *
     * @param distinta
     *            distinta con la configurazione base
     */
    public ConfigurazioneDistintaBase(final Articolo distinta) {
        setNome("BASE");
        setDistinta(distinta);
    }

    @Override
    public boolean isConfigurazioneBase() {
        return true;
    }

    @Override
    public boolean isNew() {
        return false;
    }

}
