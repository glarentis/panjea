package it.eurotn.panjea.vending.manager.evadts.importazioni.handler;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.vending.manager.evadts.importazioni.parser.LetturaEvaDtsParser;

public class DXSEvaDtsHandler extends EvaDtsStandardHandler {

    /**
     * Costruttore.
     */
    public DXSEvaDtsHandler() {
        super(new IDEvaDtsHandler());
    }

    @Override
    public boolean canHandle(String riga) {
        return StringUtils.startsWith(riga, "DXS");
    }

    @Override
    protected LetturaEvaDtsParser creaParser(String[] righe, int rigaInizio) {
        LetturaEvaDtsParser parser = new LetturaEvaDtsParser();

        for (int i = rigaInizio; i < righe.length; i++) {
            parser.addRiga(righe[i]);
            parser.setUltimaRiga(i);

            if (StringUtils.startsWith(righe[i], "DXE")) {
                break;
            }
        }

        return parser;
    }

}
