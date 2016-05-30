package it.eurotn.panjea.magazzino.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaTestataDTO;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 */
@Entity
@Audited
@DiscriminatorValue("T")
public class RigaTestata extends RigaMagazzino {

    private static final long serialVersionUID = -4872202558986542869L;

    private Date dataAreaMagazzinoCollegata;

    private String codiceTipoDocumentoCollegato;

    private String descrizione;

    @Override
    protected RigaMagazzinoDTO creaIstanzaRigaMagazzinoDTO() {
        return new RigaTestataDTO();
    }

    @Override
    public RigaMagazzino creaRigaCollegata(AreaMagazzino areaMagazzino, double ordinamentoRigaCollegata) {
        if (getRigaMagazzinoCollegata() != null) {
            throw new RuntimeException("Esiste già una riga collegata per la riga " + this.getId());
        }

        // Qui creo solamente le Righe testate che non sono legate al documento di origine.
        RigaTestata rigaTestataCollegata = new RigaTestata();
        rigaTestataCollegata.setAreaMagazzino(areaMagazzino);
        rigaTestataCollegata.setAreaCollegata(this.getAreaCollegata());
        rigaTestataCollegata.setLivello(this.getLivello() + 1);
        rigaTestataCollegata.setOrdinamento(ordinamentoRigaCollegata);
        rigaTestataCollegata.setDescrizione(this.getDescrizione());
        rigaTestataCollegata.setNoteRiga(this.getNoteRiga());
        rigaTestataCollegata.setRigaMagazzinoCollegata(this);
        rigaTestataCollegata.setRigaAutomatica(false);
        rigaTestataCollegata.setCodiceTipoDocumentoCollegato(this.getCodiceTipoDocumentoCollegato());
        return rigaTestataCollegata;
    }

    /**
     * Genera la descrizione per la riga testata in base all'area collegata.
     *
     * @return descrizione generata
     */
    public String generaDescrizioneTestata() {
        String maschera;
        IAreaDocumento areaDocumento = getAreaCollegata();
        if (areaDocumento instanceof AreaMagazzino) {
            maschera = areaMagazzinoCollegata.getTipoAreaMagazzino()
                    .getTipoDocumentoPerFatturazioneDescrizioneMaschera();

            // la descrizione della sede viene avvvalorata solo se la sede non è quella principale
            String descrizioneSede = "";
            if (!areaMagazzinoCollegata.getDocumento().getSedeEntita().getTipoSede().isSedePrincipale()) {
                descrizioneSede = areaMagazzinoCollegata.getDocumento().getSedeEntita().getSede().getDescrizione();
            }
            maschera = maschera.replace("$sede$", descrizioneSede);
        } else {
            maschera = areaOrdineCollegata.getTipoAreaOrdine().getTipoDocumentoEvasioneDescrizioneMaschera();

            // la descrizione della sede viene avvvalorata solo se la sede non è quella principale
            String descrizioneSede = "";
            if (areaOrdineCollegata.getDocumento().getSedeEntita() != null
                    && !areaOrdineCollegata.getDocumento().getSedeEntita().getTipoSede().isSedePrincipale()) {
                descrizioneSede = areaOrdineCollegata.getDocumento().getSedeEntita().getSede().getDescrizione();
            }
            maschera = maschera.replace("$sede$", descrizioneSede);
            if (((AreaOrdine) areaDocumento).getRiferimentiOrdine() != null) {
                String numeroRiferimento = "";
                if (((AreaOrdine) areaDocumento).getRiferimentiOrdine().getNumeroOrdine() != null) {
                    numeroRiferimento = ((AreaOrdine) areaDocumento).getRiferimentiOrdine().getNumeroOrdine();
                }
                String modalitaRiferimento = "";
                if (((AreaOrdine) areaDocumento).getRiferimentiOrdine().getModalitaRicezione() != null) {
                    modalitaRiferimento = ((AreaOrdine) areaDocumento).getRiferimentiOrdine().getModalitaRicezione()
                            .name();
                }
                String dataRiferimento = "";
                if (((AreaOrdine) areaDocumento).getRiferimentiOrdine().getDataOrdine() != null) {
                    dataRiferimento = new SimpleDateFormat("dd/MM/yyyy")
                            .format(((AreaOrdine) areaDocumento).getRiferimentiOrdine().getDataOrdine());
                }
                maschera = maschera.replace("$numeroOrdineRiferimento$", numeroRiferimento);
                maschera = maschera.replace("$modalitaRicezioneOrdineRiferimento$", modalitaRiferimento);
                maschera = maschera.replace("$dataDocumentoOrdineRiferimento$", dataRiferimento);
            }
        }

        maschera = maschera.replace("$numeroDocumento$", areaDocumento.getDocumento().getCodice().getCodice());
        maschera = maschera.replace("$dataDocumento$",
                new SimpleDateFormat("dd/MM/yyyy").format(areaDocumento.getDocumento().getDataDocumento()));
        maschera = maschera.replace("$codiceTipoDocumento$",
                areaDocumento.getDocumento().getTipoDocumento().getCodice());
        maschera = maschera.replace("$descrizioneTipoDocumento$",
                areaDocumento.getDocumento().getTipoDocumento().getDescrizione());
        if (areaDocumento.getDocumento().getEntita() != null) {
            maschera = maschera.replace("$codiceEntita$",
                    areaDocumento.getDocumento().getEntita().getCodice().toString());
            maschera = maschera.replace("$descrizioneEntita$",
                    areaDocumento.getDocumento().getEntita().getAnagrafica().getDenominazione());
        }
        return maschera;
    }

    /**
     * Genera la descrizione della riga testata dall'area ordine alla quale punta.
     *
     * @param areaSpedizione
     *            area Spedizione. Utilizzata per indicare il rullo di spedizione (solitamente in Gulliver).
     * @return descrizione della riga testata
     */
    public String generaDescrizioneTestata(String areaSpedizione) {
        String maschera = generaDescrizioneTestata();

        if (maschera.contains("$areaSpedizione$") && areaSpedizione != null) {
            maschera = maschera.replace("$areaSpedizione$", areaSpedizione);
        }

        return maschera;
    }

    /**
     * @return codiceTipoDocumentoCollegato
     */
    public String getCodiceTipoDocumentoCollegato() {
        return codiceTipoDocumentoCollegato;
    }

    /**
     * @return dataAreaMagazzinoCollegata
     */
    public Date getDataAreaMagazzinoCollegata() {
        return dataAreaMagazzinoCollegata;
    }

    /**
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("<html>");
        stringBuffer.append("<b>");
        stringBuffer.append(getDescrizione());
        stringBuffer.append("</b>");
        if (stampaNote && getNoteRiga() != null) {
            stringBuffer.append("<br>");
            stringBuffer.append("<i>");
            stringBuffer.append(getNoteRiga());
            stringBuffer.append("</i>");
        }
        stringBuffer.append("</html>");
        return stringBuffer.toString();
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua,
            boolean escludiTagHtml) {
        String descrizioneRiga = getDescrizioneRiga(stampaAttributi, stampaNote, lingua);
        if (escludiTagHtml) {
            descrizioneRiga = PanjeaEJBUtil.removeHtml(descrizioneRiga);
        }
        return descrizioneRiga;
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua, boolean escludiTagHtml,
            boolean stampaConai) {
        return getDescrizioneRiga(stampaAttributi, stampaNote, lingua, escludiTagHtml);
    }

    /**
     *
     * @return true se la riga testata è una riga Testata di un documento di origine
     */
    public boolean isRigaTestataDocumento() {
        return codiceTipoDocumentoCollegato != null;
    }

    /**
     * @param codiceTipoDocumentoCollegato
     *            the codiceTipoDocumentoCollegato to set
     */
    public void setCodiceTipoDocumentoCollegato(String codiceTipoDocumentoCollegato) {
        this.codiceTipoDocumentoCollegato = codiceTipoDocumentoCollegato;
    }

    /**
     * @param dataAreaMagazzinoCollegata
     *            the dataAreaMagazzinoCollegata to set
     */
    public void setDataAreaMagazzinoCollegata(Date dataAreaMagazzinoCollegata) {
        this.dataAreaMagazzinoCollegata = dataAreaMagazzinoCollegata;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("RigaTestata[");
        buffer.append(" codiceTipoDocumentoCollegato = ").append(codiceTipoDocumentoCollegato);
        buffer.append(" dataAreaMagazzinoCollegata = ").append(dataAreaMagazzinoCollegata);
        buffer.append("]");
        return buffer.toString();
    }

}
