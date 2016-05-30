package it.eurotn.panjea.contabilita.domain.rateirisconti;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;
import org.joda.time.Days;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;

@Entity
@Audited
@Table(name = "cont_righe_rateo_risconto")
public class RigaRateoRisconto extends EntityBase {
    private static final long serialVersionUID = -1017909952448912624L;
    private BigDecimal importo;
    private Date inizio;
    private Date fine;
    @ManyToOne(fetch = FetchType.LAZY)
    private RigaContabile rigaContabile;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "rigaRateoRisconto", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RigaRiscontoAnno> rateiRiscontiCalcolati;

    @Column(length = 100)
    private String nota;

    private void calcolaRateiRisconti() {
        if (inizio != null && fine != null && importo != null) {
            List<RigaRiscontoAnno> rateiRiscontiCalcolatiTmp = new ArrayList<RigaRiscontoAnno>();
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            startDate.setTime(inizio);
            endDate.setTime(fine);
            // l'ultimo giorno sembra non essere mai preso in considerazione ( mistero contabilità italiana perchè
            // invece nel numero giorni devo tenerlo presente)
            // endDate.add(Calendar.DAY_OF_MONTH, -1);
            int startYear = startDate.get(Calendar.YEAR);
            int endYear = endDate.get(Calendar.YEAR);
            long numGiorniContratto = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / (1000 * 60 * 60 * 24)
                    + 1;
            if (numGiorniContratto == 0) {
                numGiorniContratto = 1;
            }
            BigDecimal importoRisconti = BigDecimal.ZERO;
            BigDecimal importoGiorno = importo.divide(new BigDecimal(numGiorniContratto), 15, RoundingMode.HALF_UP);
            int index = 0;
            boolean rateo = endDate.get(Calendar.YEAR) == rigaContabile.getAreaContabile().getAnnoMovimento();

            BigDecimal importoSucc = importo;

            while (startYear <= endYear) {
                Calendar fineAnno = Calendar.getInstance();
                fineAnno.set(Calendar.HOUR, 24);
                fineAnno.set(Calendar.HOUR_OF_DAY, 24);
                fineAnno.clear(Calendar.MINUTE);
                fineAnno.clear(Calendar.SECOND);
                fineAnno.clear(Calendar.MILLISECOND);
                // fineAnno.clear(Calendar.AM_PM);
                if (startYear != endYear) {
                    fineAnno.set(Calendar.YEAR, startYear);
                    fineAnno.set(Calendar.MONTH, 11);
                    fineAnno.set(Calendar.DAY_OF_MONTH, 31);
                } else {
                    fineAnno.set(Calendar.YEAR, endYear);
                    fineAnno.set(Calendar.MONTH, endDate.get(Calendar.MONTH));
                    fineAnno.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH));
                }
                int giorni = Days.daysBetween(new DateTime(startDate), new DateTime(fineAnno)).getDays();

                RigaRiscontoAnno calc = null;
                if (rateo) {
                    calc = new RigaRateoAnno();
                } else {
                    calc = new RigaRiscontoAnno();
                }
                if (rateiRiscontiCalcolati != null && rateiRiscontiCalcolati.size() > 0
                        && index < rateiRiscontiCalcolati.size()) {
                    calc = rateiRiscontiCalcolati.get(index);
                }
                BigDecimal importoAnno = importoGiorno.multiply(new BigDecimal(giorni)).setScale(2,
                        RoundingMode.HALF_UP);
                importoRisconti = importoRisconti.add(importoAnno);

                numGiorniContratto = numGiorniContratto - giorni;
                importoSucc = importoSucc.subtract(importoAnno);

                calc.setAnno(startYear);
                calc.setGiorni(giorni);
                calc.setRigaRateoRisconto(this);
                calc.setImportoSuccessivo(importoSucc);
                calc.setGiorniSuccessivo(new BigDecimal(numGiorniContratto).intValue());
                calc.setImporto(importoAnno);
                startYear++;
                index++;
                startDate.set(Calendar.YEAR, startYear);
                startDate.set(Calendar.MONTH, 0);
                startDate.set(Calendar.DAY_OF_MONTH, 1);
                rateiRiscontiCalcolatiTmp.add(calc);
            }

            // in caso di arrotondamenti li assegno all'importo dell'ultimo risconto
            BigDecimal arrotondamenti = importo.subtract(importoRisconti);
            if (!rateiRiscontiCalcolatiTmp.isEmpty() && arrotondamenti.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal ultimoImporto = rateiRiscontiCalcolatiTmp.get(rateiRiscontiCalcolatiTmp.size() - 1)
                        .getImporto();
                rateiRiscontiCalcolatiTmp.get(rateiRiscontiCalcolatiTmp.size() - 1)
                        .setImporto(ultimoImporto.add(arrotondamenti));
            }

            rateiRiscontiCalcolati = rateiRiscontiCalcolatiTmp;
        }
    }

    /**
     * @return Returns the fine.
     */
    public Date getFine() {
        return fine;
    }

    /**
     *
     * @return giorni totali tra la data di inizio e di fine
     */
    public int getGiorniTotali() {
        if (inizio == null || fine == null) {
            return 0;
        }
        Calendar calInizio = Calendar.getInstance();
        calInizio.setTime(inizio);

        Calendar calFine = Calendar.getInstance();
        calFine.setTime(fine);
        calFine.set(Calendar.HOUR, 24);
        calFine.set(Calendar.HOUR_OF_DAY, 24);
        calFine.clear(Calendar.MINUTE);
        calFine.clear(Calendar.SECOND);
        calFine.clear(Calendar.MILLISECOND);

        return Days.daysBetween(new DateTime(calInizio), new DateTime(calFine)).getDays();
    }

    /**
     * @return Returns the importo.
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * @return Returns the inizio.
     */
    public Date getInizio() {
        return inizio;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @return Returns the rateiRiscontiCalcolati.
     */
    public List<RigaRiscontoAnno> getRateiRiscontiAnno() {
        if (rateiRiscontiCalcolati == null) {
            calcolaRateiRisconti();
            if (rateiRiscontiCalcolati == null) {
                return new ArrayList<>();
            }
        }
        return rateiRiscontiCalcolati;
    }

    /**
     * @return Returns the rigaContabile.
     */
    public RigaContabile getRigaContabile() {
        return rigaContabile;
    }

    /**
     * @param fine
     *            The fine to set.
     */
    public void setFine(Date fine) {
        this.fine = fine;
        calcolaRateiRisconti();
    }

    /**
     * @param importo
     *            The importo to set.
     */
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
        calcolaRateiRisconti();
    }

    /**
     * @param inizio
     *            The inizio to set.
     */
    public void setInizio(Date inizio) {
        this.inizio = inizio;
        calcolaRateiRisconti();
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @param rigaContabile
     *            The rigaContabile to set.
     */
    public void setRigaContabile(RigaContabile rigaContabile) {
        this.rigaContabile = rigaContabile;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RigaRateoRisconto [importo=" + importo + ", inizio=" + inizio + ", fine=" + fine + ", rigaContabile="
                + rigaContabile + ", rateiRiscontiCalcolati=" + rateiRiscontiCalcolati + "]";
    }

}
