package it.eurotn.panjea.vending.manager.evadts.importazioni.handler;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.vending.manager.evadts.importazioni.parser.LetturaEvaDtsParser;

public class IDEvaDtsHandler extends EvaDtsStandardHandler {

    /**
     * Costruttore.
     */
    public IDEvaDtsHandler() {
        super(null);
    }

    @Override
    public boolean canHandle(String riga) {
        return StringUtils.startsWith(riga, "ID1");
    }

    @Override
    protected LetturaEvaDtsParser creaParser(String[] righe, int rigaInizio) {
        LetturaEvaDtsParser parser = new LetturaEvaDtsParser();

        for (int i = rigaInizio; i < righe.length; i++) {

            if (i != rigaInizio && StringUtils.startsWith(righe[i], "ID1")) {
                break;
            }

            parser.addRiga(righe[i]);
            parser.setUltimaRiga(i);
        }

        return parser;
    }

}
