package it.eurotn.panjea.ordini.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticoloComponente;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.rate.domain.AreaRate;

public class AreaProduzioneFullDTOStampa extends AreaOrdineFullDTO {

    private static final long serialVersionUID = 667356456661079301L;

    private Date dataConsegna = null;

    private List<RigaOrdineProduzioneDTOStampa> righeProduzione = null;

    private List<AreaOrdine> areaOrdineCliente = null;
    private List<RigaOrdineProduzioneDTOStampa> righeOrdineCliente = null;
    private List<RigaOrdineProduzioneDTOStampa> carichiProduzione = null;

    {
        righeOrdineCliente = new ArrayList<RigaOrdineProduzioneDTOStampa>();
        righeProduzione = new ArrayList<RigaOrdineProduzioneDTOStampa>();
        carichiProduzione = new ArrayList<RigaOrdineProduzioneDTOStampa>();
    }

    /**
     * Costruttore.
     *
     * @param areaOrdine
     *            area ordine
     * @param areeOrdineCliente
     *            area ordine cliente
     * @param areaRate
     *            area rate
     * @param righeOrdine
     *            righe ordine
     * @param fasiLavorazionePerRiga
     *            fasiLavorazionePerRiga
     * @param componentiDistintaPerRiga
     *            componentiDistintaPerRiga
     * @param impegnatoClientePerRiga
     *            impegnatoClientePerRiga
     * @param ordinatoFornitorePerArticoloOrdine
     *            ordinatoFornitorePerArticolo
     * @param ordinatoFornitoreDataConsegna
     *            ordinatoFornitoreDataConsegna
     * @param ordinatoFornitorePerArticolo
     *            ordinatoFornitorePerArticolo
     */
    public AreaProduzioneFullDTOStampa(final AreaOrdine areaOrdine, final List<AreaOrdine> areeOrdineCliente,
            final AreaRate areaRate, final List<RigaOrdine> righeOrdine,
            final Map<RigaOrdine, List<FaseLavorazioneArticolo>> fasiLavorazionePerRiga,
            final Map<RigaOrdine, List<Componente>> componentiDistintaPerRiga,
            final Map<Integer, Double> impegnatoClientePerRiga,
            final Map<Integer, Double> ordinatoFornitorePerArticoloOrdine,
            final Map<Integer, Date> ordinatoFornitoreDataConsegna,
            final Map<Integer, Double> ordinatoFornitorePerArticolo) {
        super();
        setAreaOrdine(areaOrdine);
        setAreaOrdineCliente(areeOrdineCliente);
        setAreaRateEnabled(areaRate.getId() != null);
        setAreaRate(areaRate);
        setRigheOrdine(righeOrdine);
        initDatiStampa();
        initRigheProduzione(righeOrdine, fasiLavorazionePerRiga, componentiDistintaPerRiga, impegnatoClientePerRiga,
                ordinatoFornitorePerArticoloOrdine, ordinatoFornitoreDataConsegna, ordinatoFornitorePerArticolo);
    }

    /**
     * @return the areaOrdineCliente
     */
    public List<AreaOrdine> getAreaOrdineCliente() {
        return areaOrdineCliente;
    }

    /**
     * @return carichi produzione
     */
    public List<RigaOrdineProduzioneDTOStampa> getCarichiProduzione() {
        return carichiProduzione;
    }

    /**
     * @return Returns the dataConsegna.
     */
    public Date getDataConsegna() {
        return dataConsegna;
    }

    /**
     * @return the righeOrdineCliente
     */
    public List<RigaOrdineProduzioneDTOStampa> getRigheOrdineCliente() {
        return righeOrdineCliente;
    }

    /**
     * @return the righeProduzione
     */
    public List<RigaOrdineProduzioneDTOStampa> getRigheProduzione() {
        return righeProduzione;
    }

    /**
     * Assegno a dataConsegna la prima dataConsegna che trovo nelle righe.
     */
    private void initDatiStampa() {
        Iterator<RigaOrdine> iterator = getRigheOrdine().iterator();
        while (dataConsegna == null && iterator.hasNext()) {
            RigaOrdine riga = iterator.next();
            if (riga instanceof RigaArticolo) {
                dataConsegna = ((RigaArticolo) riga).getDataConsegna();
            }
        }
    }

    /**
     * Dalle righe ordine, prepara la lista di RigaOrdineProduzioneDTOStampa.
     *
     * @param righeOrdine
     *            righeOrdine
     * @param fasiLavorazionePerRiga
     *            fasiLavorazionePerRiga
     * @param componentiDistintaPerRiga
     *            componentiDistintaPerRiga
     * @param impegnatoClientePerRiga
     *            impegnatoClientePerRiga
     * @param ordinatoFornitorePerArticoloOrdine
     *            ordinatoFornitorePerArticolo
     * @param ordinatoFornitoreDataConsegna
     *            ordinatoFornitoreDataConsegna
     * @param ordinatoFornitorePerArticolo2
     */
    private void initRigheProduzione(List<RigaOrdine> righeOrdine,
            Map<RigaOrdine, List<FaseLavorazioneArticolo>> fasiLavorazionePerRiga,
            Map<RigaOrdine, List<Componente>> componentiDistintaPerRiga, Map<Integer, Double> impegnatoClientePerRiga,
            Map<Integer, Double> ordinatoFornitorePerArticoloOrdine, Map<Integer, Date> ordinatoFornitoreDataConsegna,
            Map<Integer, Double> ordinatoFornitorePerArticolo) {
        int idDistinta = 0;
        for (RigaOrdine rigaOrdine : righeOrdine) {
            RigaOrdineProduzioneDTOStampa rigaOrdineProduzioneDTOStampa = new RigaOrdineProduzioneDTOStampa(rigaOrdine);

            List<FaseLavorazioneArticolo> righeFasi = new ArrayList<>();
            Integer id = rigaOrdineProduzioneDTOStampa.getId();
            Integer idArticolo = rigaOrdineProduzioneDTOStampa.getIdArticolo();
            rigaOrdineProduzioneDTOStampa.setQtaOrdinatoFornitore(ordinatoFornitorePerArticoloOrdine.get(idArticolo));
            rigaOrdineProduzioneDTOStampa.setQtaOrdinatoFornitoreArticolo(ordinatoFornitorePerArticolo.get(idArticolo));
            rigaOrdineProduzioneDTOStampa.setQtaImpegnatoCliente(impegnatoClientePerRiga.get(idArticolo));

            if (fasiLavorazionePerRiga != null && fasiLavorazionePerRiga.get(rigaOrdine) != null) {
                if (rigaOrdine instanceof RigaArticoloComponente
                        && !((RigaArticoloComponente) rigaOrdine).getArticolo().isDistinta()) {
                    List<FaseLavorazioneArticolo> fasi = fasiLavorazionePerRiga.get(rigaOrdine);
                    for (FaseLavorazioneArticolo faseLavorazioneArticolo : fasi) {
                        righeFasi.add(faseLavorazioneArticolo);
                    }
                } else {
                    rigaOrdineProduzioneDTOStampa.setFasiLavorazioneArticolo(fasiLavorazionePerRiga.get(rigaOrdine));
                }
            }
            if (componentiDistintaPerRiga != null && componentiDistintaPerRiga.get(rigaOrdine) != null) {
                List<Componente> list = componentiDistintaPerRiga.get(rigaOrdine);
                rigaOrdineProduzioneDTOStampa.setComponentiDistintaBase(list);
            }
            if (rigaOrdineProduzioneDTOStampa.isDistinta()) {
                idDistinta++;
            }
            if (ordinatoFornitoreDataConsegna != null
                    && ordinatoFornitoreDataConsegna.get(rigaOrdine.getId()) != null) {
                rigaOrdineProduzioneDTOStampa.setDataProduzione(ordinatoFornitoreDataConsegna.get(rigaOrdine.getId()));
            }
            rigaOrdineProduzioneDTOStampa.setIdDistinta(idDistinta);
            getRigheProduzione().add(rigaOrdineProduzioneDTOStampa);
            for (FaseLavorazioneArticolo faseLavorazioneArticolo : righeFasi) {
                getRigheProduzione().add(new RigaOrdineProduzioneDTOStampa(faseLavorazioneArticolo, idDistinta));
            }
        }
    }

    /**
     * @param areaOrdineCliente
     *            the areaOrdineCliente to set
     */
    public void setAreaOrdineCliente(List<AreaOrdine> areaOrdineCliente) {
        this.areaOrdineCliente = areaOrdineCliente;
    }

    /**
     * @param carichiProduzione
     *            the carichiProduzione to set
     */
    public void setCarichiProduzione(List<RigaOrdineProduzioneDTOStampa> carichiProduzione) {
        this.carichiProduzione = carichiProduzione;
    }

    @Override
    public void setRigheOrdine(List<RigaOrdine> righeOrdine) {
        super.setRigheOrdine(righeOrdine);
    }

    /**
     * @param righeOrdineCliente
     *            the righeOrdineCliente to set
     */
    public void setRigheOrdineCliente(List<RigaOrdineProduzioneDTOStampa> righeOrdineCliente) {
        this.righeOrdineCliente = righeOrdineCliente;

        if (this.righeOrdineCliente != null && this.areaOrdineCliente != null) {
            for (RigaOrdineProduzioneDTOStampa rigaCliente : this.righeOrdineCliente) {
                for (AreaOrdine areaCliente : areaOrdineCliente) {
                    if (Objects.equals(areaCliente.getId(), rigaCliente.getIdAreaOrdineCliente())) {
                        rigaCliente.setAreaOrdine(areaCliente);
                        break;
                    }
                }
            }
        }
    }

}
