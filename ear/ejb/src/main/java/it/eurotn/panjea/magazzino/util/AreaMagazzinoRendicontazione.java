package it.eurotn.panjea.magazzino.util;

import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.lotti.util.RigaLottoRendicontazione;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.util.PanjeaEJBUtil;

public class AreaMagazzinoRendicontazione extends AreaMagazzinoFullDTOStampa {

    private static final long serialVersionUID = 5730893879002280190L;

    private List<RigaMagazzinoRendicontazione> righeRendicontazione;

    private List<RigaLottoRendicontazione> righeLottoRendicontazione;

    private String codiceCliente;

    /**
     * Costruttore di default.
     *
     * @param areaMagazzinoFullDTO
     *            l'areaMagazzinoFullDTO da cui recuperare i dati
     * @param sedeAzienda
     *            la sede del deposito di origine se esiste
     * @param deposito
     *            il deposito
     * @param depositoOrigine
     *            il deposito di origine
     * @param trasportoCura
     *            trasportoCura
     * @param sedeCollegata
     *            sedeCollegata. Può essere la sedeCollegata oppuer la sede di rifatturazione
     * @param azienda
     *            la sede dell'azienda
     */
    public AreaMagazzinoRendicontazione(final AreaMagazzinoFullDTO areaMagazzinoFullDTO, final SedeAzienda sedeAzienda,
            final Deposito deposito, final Deposito depositoOrigine, final TrasportoCura trasportoCura,
            final SedeEntita sedeCollegata, final SedeAzienda azienda) {
        super(areaMagazzinoFullDTO, sedeAzienda, deposito, depositoOrigine, trasportoCura, sedeCollegata, azienda);
    }

    /**
     * Costruttore.
     *
     * @param fullDTOStampa
     *            area full DTO stampa
     */
    public AreaMagazzinoRendicontazione(final AreaMagazzinoFullDTOStampa fullDTOStampa) {
        super();
        PanjeaEJBUtil.copyProperties(this, fullDTOStampa);
        getRigheRendicontazione();
    }

    /**
     * @return the codiceCliente
     */
    public String getCodiceCliente() {
        return codiceCliente;
    }

    /**
     * @return the righeLottoRendicontazione
     */
    public List<RigaLottoRendicontazione> getRigheLottoRendicontazione() {
        if (righeLottoRendicontazione == null) {
            righeLottoRendicontazione = new ArrayList<RigaLottoRendicontazione>();

            // riassegno i progressivi riga perchè usando i lotti non tutte le righe vengono usate ( righe testata,
            // note, ecc...)
            int progressivoRiga = 0;
            for (RigaMagazzinoRendicontazione riga : getRigheRendicontazione()) {
                if (!riga.getRigheLottoRendicontazione().isEmpty()) {
                    riga.setProgressivoRiga(++progressivoRiga);
                }
                righeLottoRendicontazione.addAll(riga.getRigheLottoRendicontazione());
            }
        }

        return righeLottoRendicontazione;
    }

    /**
     * @return the righeRendicontazione
     */
    public List<RigaMagazzinoRendicontazione> getRigheRendicontazione() {
        if (righeRendicontazione == null) {
            righeRendicontazione = new ArrayList<RigaMagazzinoRendicontazione>();

            int progressivoRiga = 1;
            for (RigaMagazzino rigaMagazzino : getRigheMagazzino()) {
                RigaMagazzinoRendicontazione rigaRendicontazione = null;
                if (rigaMagazzino instanceof RigaArticolo) {
                    rigaRendicontazione = new RigaMagazzinoRendicontazione((RigaArticolo) rigaMagazzino,
                            progressivoRiga);
                } else if (rigaMagazzino instanceof RigaNota) {
                    rigaRendicontazione = new RigaMagazzinoRendicontazione((RigaNota) rigaMagazzino, progressivoRiga);
                } else if (rigaMagazzino instanceof RigaTestata) {
                    rigaRendicontazione = new RigaMagazzinoRendicontazione((RigaTestata) rigaMagazzino,
                            progressivoRiga);
                }

                if (rigaRendicontazione != null) {
                    righeRendicontazione.add(rigaRendicontazione);
                }

                progressivoRiga++;
            }
        }

        return righeRendicontazione;
    }

    /**
     * @param codiceCliente
     *            the codiceCliente to set
     */
    public void setCodiceCliente(String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }

    /**
     * @param righeLottoRendicontazione
     *            the righeLottoRendicontazione to set
     */
    public void setRigheLottoRendicontazione(List<RigaLottoRendicontazione> righeLottoRendicontazione) {
        this.righeLottoRendicontazione = righeLottoRendicontazione;
    }

    /**
     * @param righeRendicontazione
     *            the righeRendicontazione to set
     */
    public void setRigheRendicontazione(List<RigaMagazzinoRendicontazione> righeRendicontazione) {
        this.righeRendicontazione = righeRendicontazione;
    }
}
