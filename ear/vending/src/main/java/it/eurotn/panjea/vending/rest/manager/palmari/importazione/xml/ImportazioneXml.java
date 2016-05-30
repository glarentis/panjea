package it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;

import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.Fatture;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.FattureRighe;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.Rifornimenti;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RifornimentiProdotti;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RilevazioneEva;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RilevazioneFascieEva;

public class ImportazioneXml {
    String context;
    JAXBContext jaxbContext;
    private byte[] xmlContent;
    private NewDataSet dataSet;
    private Map<Integer, NewDataSet.Fatture> fatture;
    private MultiValuedMap<Integer, NewDataSet.FattureRighe> fattureRighe;
    private Map<Integer, NewDataSet.Rifornimenti> rifornimenti;
    private MultiValuedMap<Integer, NewDataSet.RifornimentiProdotti> rifornimentiRighe;
    private Map<Integer, NewDataSet.ResoMerce> resiMerce;
    private Map<Integer, RilevazioneEva> rilevazioniEva;
    private MultiValuedMap<Integer, NewDataSet.RilevazioneFascieEva> rilevazioniFasceEva;
    private MultiValuedMap<Integer, NewDataSet.RilevazioneErroriEva> rilevazioniErroriEva;

    /**
     * Costruttore
     *
     * @param xmlContent
     *            contenuto XML
     */
    public ImportazioneXml(final byte[] xmlContent) {
        rifornimenti = new HashMap<>();
        rifornimentiRighe = MultiMapUtils.newListValuedHashMap();
        fatture = new HashMap<>();
        fattureRighe = MultiMapUtils.newListValuedHashMap();
        resiMerce = new HashMap<>();
        rilevazioniEva = new HashMap<>();
        rilevazioniFasceEva = MultiMapUtils.newListValuedHashMap();
        rilevazioniErroriEva = MultiMapUtils.newListValuedHashMap();

        this.xmlContent = xmlContent;
        this.context = "it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml";
        try {
            jaxbContext = JAXBContext.newInstance(context);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        creaMovimenti();
    }

    /**
     * esegue il parse dell'xml
     */
    private void creaMovimenti() {
        Unmarshaller unmarshaller = null;
        dataSet = null;
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
            dataSet = (NewDataSet) unmarshaller.unmarshal(new ByteArrayInputStream(xmlContent));
            for (Object record : dataSet.bollaTestataOrRifornimentiOrRifornimentiProdotti) {
                if (record instanceof Rifornimenti) {
                    Rifornimenti rif = (Rifornimenti) record;
                    rifornimenti.put(rif.getProgressivo(), rif);
                }
                if (record instanceof RifornimentiProdotti) {
                    RifornimentiProdotti rif = (RifornimentiProdotti) record;
                    rifornimentiRighe.put(rif.getProgressivo(), rif);
                }
                if (record instanceof Fatture) {
                    Fatture fatt = (Fatture) record;
                    fatture.put(fatt.getNumeroFattura(), fatt);
                }
                if (record instanceof FattureRighe) {
                    FattureRighe riga = (FattureRighe) record;
                    fattureRighe.put(riga.getNumeroFattura(), riga);
                }
                if (record instanceof RilevazioneEva) {
                    RilevazioneEva rilevazione = (RilevazioneEva) record;
                    rilevazioniEva.put(rilevazione.getProgressivo(), rilevazione);
                }
                if (record instanceof RilevazioneFascieEva) {
                    RilevazioneFascieEva rilevazione = (RilevazioneFascieEva) record;
                    rilevazioniFasceEva.put(rilevazione.getProgressivo(), rilevazione);
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Returns the fatture.
     */
    public Map<Integer, NewDataSet.Fatture> getFatture() {
        return fatture;
    }

    /**
     * @return Returns the fattureRighe.
     */
    public Map<Integer, Collection<FattureRighe>> getFattureRighe() {
        return fattureRighe.asMap();
    }

    /**
     * @return Returns the resiMerce.
     */
    public Map<Integer, NewDataSet.ResoMerce> getResiMerce() {
        return resiMerce;
    }

    /**
     * @return Returns the rifornimenti.
     */
    public Map<Integer, NewDataSet.Rifornimenti> getRifornimenti() {
        return rifornimenti;
    }

    /**
     * @return Returns the rifornimentiRighe.
     */
    public Map<Integer, Collection<RifornimentiProdotti>> getRifornimentiRighe() {
        return rifornimentiRighe.asMap();
    }

    /**
     * @return Returns the rilevazioniErroriEva.
     */
    public MultiValuedMap<Integer, NewDataSet.RilevazioneErroriEva> getRilevazioniErroriEva() {
        return rilevazioniErroriEva;
    }

    /**
     *
     * @return lista rilevazione evadts
     */
    public Map<Integer, NewDataSet.RilevazioneEva> getRilevazioniEva() {
        return rilevazioniEva;
    }

    /**
     * @return Returns the rilevazioniFascieEva.
     */
    public Map<Integer, Collection<RilevazioneFascieEva>> getRilevazioniFasceEva() {
        return rilevazioniFasceEva.asMap();
    }

}