package it.eurotn.panjea.vending.domain.evadts;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "vend_rilevazioni_eva_dts")
public class RilevazioneEvaDts extends EntityBase {

    private static final Logger LOGGER = Logger.getLogger(RilevazioneEvaDts.class);

    @ManyToOne
    private AreaRifornimento areaRifornimento;

    private BigDecimal progressivo;
    private Integer divisore;
    private BigDecimal ca201;
    private BigDecimal ca202;
    private BigDecimal ca203;
    private BigDecimal ca204;
    private BigDecimal ca301;
    private BigDecimal ca302;
    private BigDecimal ca303;
    private BigDecimal ca304;
    private BigDecimal ca305;
    private BigDecimal ca306;
    private BigDecimal ca307;
    private BigDecimal ca308;
    private BigDecimal ca309;
    private BigDecimal ca311;
    private BigDecimal ca401;
    private BigDecimal ca402;
    private BigDecimal ca406;
    private BigDecimal ca407;
    private BigDecimal ca701;
    private BigDecimal ca703;
    private BigDecimal ca705;
    private BigDecimal ca707;
    private BigDecimal ca801;
    private BigDecimal ca802;
    private BigDecimal ca1001;
    private BigDecimal ca1003;
    private BigDecimal ca1501;
    private BigDecimal da201;
    private BigDecimal da202;
    private BigDecimal da203;
    private BigDecimal da204;
    private BigDecimal da301;
    private BigDecimal da302;
    private BigDecimal da303;
    private BigDecimal da304;
    private BigDecimal da401;
    private BigDecimal da402;
    private BigDecimal da403;
    private BigDecimal da404;
    private BigDecimal da501;
    private BigDecimal da502;
    private BigDecimal da505;
    private BigDecimal da506;
    private BigDecimal da602;
    private BigDecimal da901;
    private Date ea302;
    private Date ea305;
    private BigDecimal va101;
    private BigDecimal va102;
    private BigDecimal va103;
    private BigDecimal va104;
    private BigDecimal va107;
    private BigDecimal va108;
    private BigDecimal va111;
    private BigDecimal va112;
    private BigDecimal va203;
    private BigDecimal va204;
    private BigDecimal va206;
    private BigDecimal va301;
    private BigDecimal va302;
    private BigDecimal va303;
    private BigDecimal va304;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "rilevazioneEvaDts", cascade = { CascadeType.ALL })
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RilevazioniFasceEva> fasce;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rilevazioneEvaDts", cascade = { CascadeType.ALL })
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RilevazioniEvaDtsErrori> errori;

    // Utilizzati solo sull'importazione dei file delle rilevazioni
    private String id101;
    private String id102;
    private String id103;
    private String id104;
    private String id105;
    private String id106;

    @Transient
    private List<String> righeFileImport;

    /**
     * Divide tutti gli importi in con il divisore presente.
     */
    public void applicaDivisore() {
        if (divisore == null || divisore == 0) {
            return;
        }

        try {
            // Tutti i campi BigDecimal vanno divisi
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!"progressivo".equals(field.getName()) && field.getType().equals(BigDecimal.class)) {
                    field.setAccessible(true);
                    Object value = ObjectUtils.defaultIfNull((BigDecimal) field.get(this), BigDecimal.ZERO)
                            .divide(new BigDecimal(divisore));
                    field.set(this, value);
                }
            }

            if (getFasce() != null) {
                for (RilevazioniFasceEva fascia : getFasce()) {
                    BigDecimal la103 = ObjectUtils.defaultIfNull(fascia.getLa103(), BigDecimal.ZERO);
                    if ((la103.compareTo(BigDecimal.ZERO) != 0 && la103.compareTo(new BigDecimal(9999)) != 0
                            || fascia.getLa104() != 0)) {
                        BigDecimal value = ObjectUtils.defaultIfNull(fascia.getLa103(), BigDecimal.ZERO)
                                .divide(new BigDecimal(divisore));
                        fascia.setLa103(value);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante l'applicazione del divisori sugli importi della rilevazione", e);
        }
    }

    /**
     * @return Returns the areaRifornimento.
     */
    public AreaRifornimento getAreaRifornimento() {
        return areaRifornimento;
    }

    /**
     * @return Returns the ca1001.
     */
    public BigDecimal getCa1001() {
        return ca1001;
    }

    /**
     * @return Returns the ca1003.
     */
    public BigDecimal getCa1003() {
        return ca1003;
    }

    /**
     * @return Returns the ca1501.
     */
    public BigDecimal getCa1501() {
        return ca1501;
    }

    /**
     * @return the ca201
     */
    public BigDecimal getCa201() {
        return ca201;
    }

    /**
     * @return the ca202
     */
    public BigDecimal getCa202() {
        return ca202;
    }

    /**
     * @return Returns the ca203.
     */
    public BigDecimal getCa203() {
        return ca203;
    }

    /**
     * @return Returns the ca204.
     */
    public BigDecimal getCa204() {
        return ca204;
    }

    /**
     * @return Returns the ca301.
     */
    public BigDecimal getCa301() {
        return ca301;
    }

    /**
     * @return Returns the ca302.
     */
    public BigDecimal getCa302() {
        return ca302;
    }

    /**
     * @return Returns the ca303.
     */
    public BigDecimal getCa303() {
        return ca303;
    }

    /**
     * @return Returns the ca304.
     */
    public BigDecimal getCa304() {
        return ca304;
    }

    /**
     * @return the ca305
     */
    public BigDecimal getCa305() {
        return ca305;
    }

    /**
     * @return the ca306
     */
    public BigDecimal getCa306() {
        return ca306;
    }

    /**
     * @return the ca307
     */
    public BigDecimal getCa307() {
        return ca307;
    }

    /**
     * @return the ca308
     */
    public BigDecimal getCa308() {
        return ca308;
    }

    /**
     * @return Returns the ca309.
     */
    public BigDecimal getCa309() {
        return ca309;
    }

    /**
     * @return Returns the ca311.
     */
    public BigDecimal getCa311() {
        return ca311;
    }

    /**
     * @return Returns the ca401.
     */
    public BigDecimal getCa401() {
        return ca401;
    }

    /**
     * @return Returns the ca402.
     */
    public BigDecimal getCa402() {
        return ca402;
    }

    /**
     * @return Returns the ca406.
     */
    public BigDecimal getCa406() {
        return ca406;
    }

    /**
     * @return Returns the ca407.
     */
    public BigDecimal getCa407() {
        return ca407;
    }

    /**
     * @return Returns the ca701.
     */
    public BigDecimal getCa701() {
        return ca701;
    }

    /**
     * @return Returns the ca703.
     */
    public BigDecimal getCa703() {
        return ca703;
    }

    /**
     * @return Returns the ca705.
     */
    public BigDecimal getCa705() {
        return ca705;
    }

    /**
     * @return Returns the ca707.
     */
    public BigDecimal getCa707() {
        return ca707;
    }

    /**
     * @return Returns the ca801.
     */
    public BigDecimal getCa801() {
        return ca801;
    }

    /**
     * @return the ca802
     */
    public BigDecimal getCa802() {
        return ca802;
    }

    /**
     * @return the da201
     */
    public BigDecimal getDa201() {
        return da201;
    }

    /**
     * @return the da202
     */
    public BigDecimal getDa202() {
        return da202;
    }

    /**
     * @return Returns the da203.
     */
    public BigDecimal getDa203() {
        return da203;
    }

    /**
     * @return Returns the da204.
     */
    public BigDecimal getDa204() {
        return da204;
    }

    /**
     * @return the da301
     */
    public BigDecimal getDa301() {
        return da301;
    }

    /**
     * @return Returns the da302.
     */
    public BigDecimal getDa302() {
        return da302;
    }

    /**
     * @return the da303
     */
    public BigDecimal getDa303() {
        return da303;
    }

    /**
     * @return the da304
     */
    public BigDecimal getDa304() {
        return da304;
    }

    /**
     * @return the da401
     */
    public BigDecimal getDa401() {
        return da401;
    }

    /**
     * @return Returns the da402.
     */
    public BigDecimal getDa402() {
        return da402;
    }

    /**
     * @return the da403
     */
    public BigDecimal getDa403() {
        return da403;
    }

    /**
     * @return the da404
     */
    public BigDecimal getDa404() {
        return da404;
    }

    /**
     * @return Returns the da501.
     */
    public BigDecimal getDa501() {
        return da501;
    }

    /**
     * @return Returns the da502.
     */
    public BigDecimal getDa502() {
        return da502;
    }

    /**
     * @return Returns the da505.
     */
    public BigDecimal getDa505() {
        return da505;
    }

    /**
     * @return Returns the da506.
     */
    public BigDecimal getDa506() {
        return da506;
    }

    /**
     * @return Returns the da602.
     */
    public BigDecimal getDa602() {
        return da602;
    }

    /**
     * @return Returns the da901.
     */
    public BigDecimal getDa901() {
        return da901;
    }

    /**
     * @return Returns the divisore.
     */
    public Integer getDivisore() {
        return divisore;
    }

    /**
     * @return the ea302
     */
    public Date getEa302() {
        return ea302;
    }

    /**
     * @return the ea305
     */
    public Date getEa305() {
        return ea305;
    }

    /**
     * @return Returns the errori.
     */
    public List<RilevazioniEvaDtsErrori> getErrori() {
        return errori;
    }

    /**
     * @return Returns the fasce.
     */
    public List<RilevazioniFasceEva> getFasce() {
        return fasce;
    }

    /**
     * @return the id101
     */
    public String getId101() {
        return id101;
    }

    /**
     * @return the id102
     */
    public String getId102() {
        return id102;
    }

    /**
     * @return the id103
     */
    public String getId103() {
        return id103;
    }

    /**
     * @return the id104
     */
    public String getId104() {
        return id104;
    }

    /**
     * @return the id105
     */
    public String getId105() {
        return id105;
    }

    /**
     * @return the id106
     */
    public String getId106() {
        return id106;
    }

    /**
     * @return the identificativo
     */
    public String getIdentificativo() {
        return StringUtils.isBlank(id106) ? id101 : id106;
    }

    /**
     * @return Returns the progressivo.
     */
    public BigDecimal getProgressivo() {
        return progressivo;
    }

    /**
     * @return the righeFileImport
     */
    public List<String> getRigheFileImport() {
        return righeFileImport;
    }

    /**
     * @return the va101
     */
    public BigDecimal getVa101() {
        return va101;
    }

    /**
     * @return the va102
     */
    public BigDecimal getVa102() {
        return va102;
    }

    /**
     * @return Returns the va103.
     */
    public BigDecimal getVa103() {
        return va103;
    }

    /**
     * @return Returns the va104.
     */
    public BigDecimal getVa104() {
        return va104;
    }

    /**
     * @return Returns the va107.
     */
    public BigDecimal getVa107() {
        return va107;
    }

    /**
     * @return Returns the va108.
     */
    public BigDecimal getVa108() {
        return va108;
    }

    /**
     * @return Returns the va111.
     */
    public BigDecimal getVa111() {
        return va111;
    }

    /**
     * @return Returns the va112.
     */
    public BigDecimal getVa112() {
        return va112;
    }

    /**
     * @return Returns the va203.
     */
    public BigDecimal getVa203() {
        return va203;
    }

    /**
     * @return Returns the va204.
     */
    public BigDecimal getVa204() {
        return va204;
    }

    /**
     * @return Returns the va206.
     */
    public BigDecimal getVa206() {
        return va206;
    }

    /**
     * @return the va301
     */
    public BigDecimal getVa301() {
        return va301;
    }

    /**
     * @return the va302
     */
    public BigDecimal getVa302() {
        return va302;
    }

    /**
     * @return Returns the va303.
     */
    public BigDecimal getVa303() {
        return va303;
    }

    /**
     * @return Returns the va304.
     */
    public BigDecimal getVa304() {
        return va304;
    }

    /**
     * @return <code>true</code> se la rilevazione è vuota
     */
    public boolean isEmpty() {
        return isEmpty(ca203) && isEmpty(ca204) && isEmpty(ca301) && isEmpty(ca302) && isEmpty(ca304) && isEmpty(ca303)
                && isEmpty(ca309) && isEmpty(ca311) && isEmpty(ca401) && isEmpty(ca402) && isEmpty(ca406)
                && isEmpty(ca407) && isEmpty(ca701) && isEmpty(ca703) && isEmpty(ca705) && isEmpty(ca707)
                && isEmpty(ca801) && isEmpty(ca1001) && isEmpty(ca1003) && isEmpty(da203) && isEmpty(da204)
                && isEmpty(da302) && isEmpty(ca402) && isEmpty(da501) && isEmpty(da502) && isEmpty(da505)
                && isEmpty(da506) && isEmpty(da602) && isEmpty(da901) && isEmpty(va103) && isEmpty(va104)
                && isEmpty(va107) && isEmpty(va108) && isEmpty(va111) && isEmpty(va112) && isEmpty(va203)
                && isEmpty(va204) && isEmpty(va206) && isEmpty(va303) && isEmpty(va304) && isEmpty(ca1501);
    }

    private boolean isEmpty(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * @param areaRifornimento
     *            The areaRifornimento to set.
     */
    public void setAreaRifornimento(AreaRifornimento areaRifornimento) {
        this.areaRifornimento = areaRifornimento;
    }

    /**
     * @param ca1001
     *            The ca1001 to set.
     */
    public void setCa1001(BigDecimal ca1001) {
        this.ca1001 = ca1001;
    }

    /**
     * @param ca1003
     *            The ca1003 to set.
     */
    public void setCa1003(BigDecimal ca1003) {
        this.ca1003 = ca1003;
    }

    /**
     * @param ca1501
     *            The ca1501 to set.
     */
    public void setCa1501(BigDecimal ca1501) {
        this.ca1501 = ca1501;
    }

    /**
     * @param ca201
     *            the ca201 to set
     */
    public void setCa201(BigDecimal ca201) {
        this.ca201 = ca201;
    }

    /**
     * @param ca202
     *            the ca202 to set
     */
    public void setCa202(BigDecimal ca202) {
        this.ca202 = ca202;
    }

    /**
     * @param ca203
     *            The ca203 to set.
     */
    public void setCa203(BigDecimal ca203) {
        this.ca203 = ca203;
    }

    /**
     * @param ca204
     *            The ca204 to set.
     */
    public void setCa204(BigDecimal ca204) {
        this.ca204 = ca204;
    }

    /**
     * @param ca301
     *            The ca301 to set.
     */
    public void setCa301(BigDecimal ca301) {
        this.ca301 = ca301;
    }

    /**
     * @param ca302
     *            The ca302 to set.
     */
    public void setCa302(BigDecimal ca302) {
        this.ca302 = ca302;
    }

    /**
     * @param ca303
     *            The ca303 to set.
     */
    public void setCa303(BigDecimal ca303) {
        this.ca303 = ca303;
    }

    /**
     * @param ca304
     *            The ca304 to set.
     */
    public void setCa304(BigDecimal ca304) {
        this.ca304 = ca304;
    }

    /**
     * @param ca305
     *            the ca305 to set
     */
    public void setCa305(BigDecimal ca305) {
        this.ca305 = ca305;
    }

    /**
     * @param ca306
     *            the ca306 to set
     */
    public void setCa306(BigDecimal ca306) {
        this.ca306 = ca306;
    }

    /**
     * @param ca307
     *            the ca307 to set
     */
    public void setCa307(BigDecimal ca307) {
        this.ca307 = ca307;
    }

    /**
     * @param ca308
     *            the ca308 to set
     */
    public void setCa308(BigDecimal ca308) {
        this.ca308 = ca308;
    }

    /**
     * @param ca309
     *            The ca309 to set.
     */
    public void setCa309(BigDecimal ca309) {
        this.ca309 = ca309;
    }

    /**
     * @param ca311
     *            The ca311 to set.
     */
    public void setCa311(BigDecimal ca311) {
        this.ca311 = ca311;
    }

    /**
     * @param ca401
     *            The ca401 to set.
     */
    public void setCa401(BigDecimal ca401) {
        this.ca401 = ca401;
    }

    /**
     * @param ca402
     *            The ca402 to set.
     */
    public void setCa402(BigDecimal ca402) {
        this.ca402 = ca402;
    }

    /**
     * @param ca406
     *            The ca406 to set.
     */
    public void setCa406(BigDecimal ca406) {
        this.ca406 = ca406;
    }

    /**
     * @param ca407
     *            The ca407 to set.
     */
    public void setCa407(BigDecimal ca407) {
        this.ca407 = ca407;
    }

    /**
     * @param ca701
     *            The ca701 to set.
     */
    public void setCa701(BigDecimal ca701) {
        this.ca701 = ca701;
    }

    /**
     * @param ca703
     *            The ca703 to set.
     */
    public void setCa703(BigDecimal ca703) {
        this.ca703 = ca703;
    }

    /**
     * @param ca705
     *            The ca705 to set.
     */
    public void setCa705(BigDecimal ca705) {
        this.ca705 = ca705;
    }

    /**
     * @param ca707
     *            The ca707 to set.
     */
    public void setCa707(BigDecimal ca707) {
        this.ca707 = ca707;
    }

    /**
     * @param ca801
     *            The ca801 to set.
     */
    public void setCa801(BigDecimal ca801) {
        this.ca801 = ca801;
    }

    /**
     * @param ca802
     *            the ca802 to set
     */
    public void setCa802(BigDecimal ca802) {
        this.ca802 = ca802;
    }

    /**
     * @param da201
     *            the da201 to set
     */
    public void setDa201(BigDecimal da201) {
        this.da201 = da201;
    }

    /**
     * @param da202
     *            the da202 to set
     */
    public void setDa202(BigDecimal da202) {
        this.da202 = da202;
    }

    /**
     * @param da203
     *            The da203 to set.
     */
    public void setDa203(BigDecimal da203) {
        this.da203 = da203;
    }

    /**
     * @param da204
     *            The da204 to set.
     */
    public void setDa204(BigDecimal da204) {
        this.da204 = da204;
    }

    /**
     * @param da301
     *            the da301 to set
     */
    public void setDa301(BigDecimal da301) {
        this.da301 = da301;
    }

    /**
     * @param da302
     *            The da302 to set.
     */
    public void setDa302(BigDecimal da302) {
        this.da302 = da302;
    }

    /**
     * @param da303
     *            the da303 to set
     */
    public void setDa303(BigDecimal da303) {
        this.da303 = da303;
    }

    /**
     * @param da304
     *            the da304 to set
     */
    public void setDa304(BigDecimal da304) {
        this.da304 = da304;
    }

    /**
     * @param da401
     *            the da401 to set
     */
    public void setDa401(BigDecimal da401) {
        this.da401 = da401;
    }

    /**
     * @param da402
     *            The da402 to set.
     */
    public void setDa402(BigDecimal da402) {
        this.da402 = da402;
    }

    /**
     * @param da403
     *            the da403 to set
     */
    public void setDa403(BigDecimal da403) {
        this.da403 = da403;
    }

    /**
     * @param da404
     *            the da404 to set
     */
    public void setDa404(BigDecimal da404) {
        this.da404 = da404;
    }

    /**
     * @param da501
     *            The da501 to set.
     */
    public void setDa501(BigDecimal da501) {
        this.da501 = da501;
    }

    /**
     * @param da502
     *            The da502 to set.
     */
    public void setDa502(BigDecimal da502) {
        this.da502 = da502;
    }

    /**
     * @param da505
     *            The da505 to set.
     */
    public void setDa505(BigDecimal da505) {
        this.da505 = da505;
    }

    /**
     * @param da506
     *            The da506 to set.
     */
    public void setDa506(BigDecimal da506) {
        this.da506 = da506;
    }

    /**
     * @param da602
     *            The da602 to set.
     */
    public void setDa602(BigDecimal da602) {
        this.da602 = da602;
    }

    /**
     * @param da901
     *            The da901 to set.
     */
    public void setDa901(BigDecimal da901) {
        this.da901 = da901;
    }

    /**
     * @param divisore
     *            The divisore to set.
     */
    public void setDivisore(Integer divisore) {
        this.divisore = divisore;
    }

    /**
     * @param ea302
     *            the ea302 to set
     */
    public void setEa302(Date ea302) {
        this.ea302 = ea302;
    }

    /**
     * @param ea305
     *            the ea305 to set
     */
    public void setEa305(Date ea305) {
        this.ea305 = ea305;
    }

    /**
     * @param errori
     *            The errori to set.
     */
    public void setErrori(List<RilevazioniEvaDtsErrori> errori) {
        this.errori = errori;
    }

    /**
     * @param fasce
     *            The fasce to set.
     */
    public void setFasce(List<RilevazioniFasceEva> fasce) {
        this.fasce = fasce;
    }

    /**
     * @param id101
     *            the id101 to set
     */
    public void setId101(String id101) {
        this.id101 = id101;
    }

    /**
     * @param id102
     *            the id102 to set
     */
    public void setId102(String id102) {
        this.id102 = id102;
    }

    /**
     * @param id103
     *            the id103 to set
     */
    public void setId103(String id103) {
        this.id103 = id103;
    }

    /**
     * @param id104
     *            the id104 to set
     */
    public void setId104(String id104) {
        this.id104 = id104;
    }

    /**
     * @param id105
     *            the id105 to set
     */
    public void setId105(String id105) {
        this.id105 = id105;
    }

    /**
     * @param id106
     *            the id106 to set
     */
    public void setId106(String id106) {
        this.id106 = id106;
    }

    /**
     * @param progressivo
     *            The progressivo to set.
     */
    public void setProgressivo(BigDecimal progressivo) {
        this.progressivo = progressivo;
    }

    /**
     * @param righeFileImport
     *            the righeFileImport to set
     */
    public void setRigheFileImport(List<String> righeFileImport) {
        this.righeFileImport = righeFileImport;
    }

    /**
     * @param va101
     *            the va101 to set
     */
    public void setVa101(BigDecimal va101) {
        this.va101 = va101;
    }

    /**
     * @param va102
     *            the va102 to set
     */
    public void setVa102(BigDecimal va102) {
        this.va102 = va102;
    }

    /**
     * @param va103
     *            The va103 to set.
     */
    public void setVa103(BigDecimal va103) {
        this.va103 = va103;
    }

    /**
     * @param va104
     *            The va104 to set.
     */
    public void setVa104(BigDecimal va104) {
        this.va104 = va104;
    }

    /**
     * @param va107
     *            The va107 to set.
     */
    public void setVa107(BigDecimal va107) {
        this.va107 = va107;
    }

    /**
     * @param va108
     *            The va108 to set.
     */
    public void setVa108(BigDecimal va108) {
        this.va108 = va108;
    }

    /**
     * @param va111
     *            The va111 to set.
     */
    public void setVa111(BigDecimal va111) {
        this.va111 = va111;
    }

    /**
     * @param va112
     *            The va112 to set.
     */
    public void setVa112(BigDecimal va112) {
        this.va112 = va112;
    }

    /**
     * @param va203
     *            The va203 to set.
     */
    public void setVa203(BigDecimal va203) {
        this.va203 = va203;
    }

    /**
     * @param va204
     *            The va204 to set.
     */
    public void setVa204(BigDecimal va204) {
        this.va204 = va204;
    }

    /**
     * @param va206
     *            The va206 to set.
     */
    public void setVa206(BigDecimal va206) {
        this.va206 = va206;
    }

    /**
     * @param va301
     *            the va301 to set
     */
    public void setVa301(BigDecimal va301) {
        this.va301 = va301;
    }

    /**
     * @param va302
     *            the va302 to set
     */
    public void setVa302(BigDecimal va302) {
        this.va302 = va302;
    }

    /**
     * @param va303
     *            The va303 to set.
     */
    public void setVa303(BigDecimal va303) {
        this.va303 = va303;
    }

    /**
     * @param va304
     *            The va304 to set.
     */
    public void setVa304(BigDecimal va304) {
        this.va304 = va304;
    }
}