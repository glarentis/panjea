//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.11 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.02.15 alle 10:33:19 AM CET 
//


package it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="BollaTestata"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Chiave"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="1"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Numero_XE" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="10"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Data_XE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Rifornimenti"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Numero_fattura" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Data_intervento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="Ora_intervento"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Ora_fine_intervento"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Barcode" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Matricola"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Tipo_gestione" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
 *                   &lt;element name="Periferico" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="Lettura_precedente" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Lettura" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Prove" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Calcolato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Incassato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Reso" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Sospeso" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Recuperato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Cassetta" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Cassetta_nuova" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="ProgressivoRichiesta" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="Moneta002ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Moneta005ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Moneta010ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Moneta020ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Moneta050ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Moneta100ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Moneta200ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="ValoreResoMerce" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Profilo"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RifornimentiProdotti"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Prodotto"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                   &lt;element name="Omaggio" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                   &lt;element name="Coefficiente_resa" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                   &lt;element name="Prezzo" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Inventari"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Prodotto"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Quantita_inventario" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="InterventiManutenzione"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Data_intervento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Matricola"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Tempo_trasferimento" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
 *                   &lt;element name="Tempo_intervento" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
 *                   &lt;element name="Lettura_iniziale" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Lettura_finale" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Scatti_prova" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Descrizione_aggiuntiva" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="255"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Ora_inizio" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Ora_fine" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="BarCode" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="Numero_chiamata" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="InterventiManutenzioneDettaglio"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Manutenzione"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Rilevazioni"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Matricola"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Data_rilevazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="Data_prelievo" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="Ora_prelievo"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Data_prelievo_precedente" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="Ora_prelievo_precedente" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Venduto_chiave_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Venduto_chiave_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Venduto_moneta_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Venduto_moneta_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Incassato_ricarica_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Incassato_ricarica_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Ricaricato_da_chiave_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Ricaricato_da_chiave_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Incassato_moneta" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Incassato_banconote" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Overpay_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Overpay_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Monete_nei_tubi" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RilevazioniSelezioni"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Matricola"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Data_prelievo" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="Ora_prelievo"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Riga" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
 *                   &lt;element name="Valore_chiave" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Valore_moneta" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Vendite_chiave" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Vendite_moneta" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Vendite_service" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Fatture"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Cliente"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Numero_fattura" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Data_fattura" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="Totale_fattura" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Totale_imponibile" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Totale_imposta" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Aliquota_1" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                   &lt;element name="Imponibile_1" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Imposta_1" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Aliquota_2" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                   &lt;element name="Imponibile_2" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Imposta_2" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Abbuono" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FattureRighe"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Data_fattura" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="Numero_fattura" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Prodotto"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Omaggio" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Prezzo" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Iva" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *                   &lt;element name="Somministrazione" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="InstallazioniRitiriDistributori"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="DataMovimento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="MatricolaInstallata"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="BattuteInstallata" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="ModelloInstallato"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Flag_nuova_installazione" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="MatricolaRitirata"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="BattuteRitirata" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="ModelloRitirato"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Flag_ritiro_definitivo" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="Armadietti" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Cestini" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Note"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="255"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NuoveInstallazioniDettaglio"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Ragione_sociale" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="50"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Indirizzo" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="50"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Cap" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Citta" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="50"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Provincia" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="2"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Listino_prodotti_1"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Listino_prodotti_2"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Listino_prodotti_3"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TabellaGenerica"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Chiave"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="20"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Data_modifica" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="ValoreIntero" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="ValoreReale" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *                   &lt;element name="ValoreBooleano" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                   &lt;element name="ValoreStringa" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="255"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="ValoreData" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NuoveRichiesteIntervento"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Matricola"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="TipoChiamata"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Note" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="255"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ChiaviMovimenti"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="DataMovimento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="Cliente"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Tipo"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="5"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Chiave" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Cauzione" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Incassato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Reso" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Omaggio" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Note" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="255"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ResoMerce"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Prodotto"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="MerceNonConforme" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="RiportoInMagazzino" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                   &lt;element name="Note"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="50"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Prezzo" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Resa" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RilevazioneEva"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Divisore" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA203" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA204" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA301" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA302" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA303" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA304" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA309" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA311" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA401" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA402" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA406" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA407" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA701" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA703" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA705" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA707" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA801" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA1001" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA1003" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="CA1501" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA203" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA204" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA302" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA402" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA501" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA502" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA505" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA506" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA602" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="DA901" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA103" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA104" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA107" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA108" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA111" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA112" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA203" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA204" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA206" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA303" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="VA304" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RilevazioneFascieEva"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="LA101" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="LA102" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="LA103" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="LA104" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RilevazioneErroriEva"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Tipo"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="3"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Codice"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="10"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Elemento"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="20"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Occorrenze" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RaccoltaPuntiPremiClienti"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Raccolta"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Prodotto"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Punti" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Cliente"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Data_ordine" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="Data_consegna" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="Valore" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Sospeso" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Pagato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="Ordine" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *                   &lt;element name="Rifornimento" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *                   &lt;element name="Nuovo" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Log"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Log" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="Sql" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bollaTestataOrRifornimentiOrRifornimentiProdotti"
})
@XmlRootElement(name = "NewDataSet")
public class NewDataSet {

    @XmlElements({
        @XmlElement(name = "BollaTestata", type = NewDataSet.BollaTestata.class),
        @XmlElement(name = "Rifornimenti", type = NewDataSet.Rifornimenti.class),
        @XmlElement(name = "RifornimentiProdotti", type = NewDataSet.RifornimentiProdotti.class),
        @XmlElement(name = "Inventari", type = NewDataSet.Inventari.class),
        @XmlElement(name = "InterventiManutenzione", type = NewDataSet.InterventiManutenzione.class),
        @XmlElement(name = "InterventiManutenzioneDettaglio", type = NewDataSet.InterventiManutenzioneDettaglio.class),
        @XmlElement(name = "Rilevazioni", type = NewDataSet.Rilevazioni.class),
        @XmlElement(name = "RilevazioniSelezioni", type = NewDataSet.RilevazioniSelezioni.class),
        @XmlElement(name = "Fatture", type = NewDataSet.Fatture.class),
        @XmlElement(name = "FattureRighe", type = NewDataSet.FattureRighe.class),
        @XmlElement(name = "InstallazioniRitiriDistributori", type = NewDataSet.InstallazioniRitiriDistributori.class),
        @XmlElement(name = "NuoveInstallazioniDettaglio", type = NewDataSet.NuoveInstallazioniDettaglio.class),
        @XmlElement(name = "TabellaGenerica", type = NewDataSet.TabellaGenerica.class),
        @XmlElement(name = "NuoveRichiesteIntervento", type = NewDataSet.NuoveRichiesteIntervento.class),
        @XmlElement(name = "ChiaviMovimenti", type = NewDataSet.ChiaviMovimenti.class),
        @XmlElement(name = "ResoMerce", type = NewDataSet.ResoMerce.class),
        @XmlElement(name = "RilevazioneEva", type = NewDataSet.RilevazioneEva.class),
        @XmlElement(name = "RilevazioneFascieEva", type = NewDataSet.RilevazioneFascieEva.class),
        @XmlElement(name = "RilevazioneErroriEva", type = NewDataSet.RilevazioneErroriEva.class),
        @XmlElement(name = "RaccoltaPuntiPremiClienti", type = NewDataSet.RaccoltaPuntiPremiClienti.class),
        @XmlElement(name = "Log", type = NewDataSet.Log.class)
    })
    protected List<Object> bollaTestataOrRifornimentiOrRifornimentiProdotti;

    /**
     * Gets the value of the bollaTestataOrRifornimentiOrRifornimentiProdotti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bollaTestataOrRifornimentiOrRifornimentiProdotti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBollaTestataOrRifornimentiOrRifornimentiProdotti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NewDataSet.BollaTestata }
     * {@link NewDataSet.Rifornimenti }
     * {@link NewDataSet.RifornimentiProdotti }
     * {@link NewDataSet.Inventari }
     * {@link NewDataSet.InterventiManutenzione }
     * {@link NewDataSet.InterventiManutenzioneDettaglio }
     * {@link NewDataSet.Rilevazioni }
     * {@link NewDataSet.RilevazioniSelezioni }
     * {@link NewDataSet.Fatture }
     * {@link NewDataSet.FattureRighe }
     * {@link NewDataSet.InstallazioniRitiriDistributori }
     * {@link NewDataSet.NuoveInstallazioniDettaglio }
     * {@link NewDataSet.TabellaGenerica }
     * {@link NewDataSet.NuoveRichiesteIntervento }
     * {@link NewDataSet.ChiaviMovimenti }
     * {@link NewDataSet.ResoMerce }
     * {@link NewDataSet.RilevazioneEva }
     * {@link NewDataSet.RilevazioneFascieEva }
     * {@link NewDataSet.RilevazioneErroriEva }
     * {@link NewDataSet.RaccoltaPuntiPremiClienti }
     * {@link NewDataSet.Log }
     * 
     * 
     */
    public List<Object> getBollaTestataOrRifornimentiOrRifornimentiProdotti() {
        if (bollaTestataOrRifornimentiOrRifornimentiProdotti == null) {
            bollaTestataOrRifornimentiOrRifornimentiProdotti = new ArrayList<Object>();
        }
        return this.bollaTestataOrRifornimentiOrRifornimentiProdotti;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Chiave"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="1"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Numero_XE" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="10"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Data_XE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "chiave",
        "numeroXE",
        "dataXE"
    })
    public static class BollaTestata {

        @XmlElement(name = "Chiave", required = true)
        protected String chiave;
        @XmlElement(name = "Numero_XE")
        protected String numeroXE;
        @XmlElement(name = "Data_XE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataXE;

        /**
         * Recupera il valore della proprietà chiave.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getChiave() {
            return chiave;
        }

        /**
         * Imposta il valore della proprietà chiave.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setChiave(String value) {
            this.chiave = value;
        }

        /**
         * Recupera il valore della proprietà numeroXE.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroXE() {
            return numeroXE;
        }

        /**
         * Imposta il valore della proprietà numeroXE.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroXE(String value) {
            this.numeroXE = value;
        }

        /**
         * Recupera il valore della proprietà dataXE.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataXE() {
            return dataXE;
        }

        /**
         * Imposta il valore della proprietà dataXE.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataXE(XMLGregorianCalendar value) {
            this.dataXE = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="DataMovimento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="Cliente"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Tipo"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Chiave" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Cauzione" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Incassato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Reso" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Omaggio" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Note" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="255"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "dataMovimento",
        "cliente",
        "tipo",
        "chiave",
        "quantita",
        "cauzione",
        "incassato",
        "reso",
        "omaggio",
        "note"
    })
    public static class ChiaviMovimenti {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "DataMovimento", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataMovimento;
        @XmlElement(name = "Cliente", required = true)
        protected String cliente;
        @XmlElement(name = "Tipo", required = true)
        protected String tipo;
        @XmlElement(name = "Chiave")
        protected int chiave;
        @XmlElement(name = "Quantita")
        protected int quantita;
        @XmlElement(name = "Cauzione", required = true)
        protected BigDecimal cauzione;
        @XmlElement(name = "Incassato", required = true)
        protected BigDecimal incassato;
        @XmlElement(name = "Reso", required = true)
        protected BigDecimal reso;
        @XmlElement(name = "Omaggio", required = true)
        protected BigDecimal omaggio;
        @XmlElement(name = "Note")
        protected String note;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà dataMovimento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataMovimento() {
            return dataMovimento;
        }

        /**
         * Imposta il valore della proprietà dataMovimento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataMovimento(XMLGregorianCalendar value) {
            this.dataMovimento = value;
        }

        /**
         * Recupera il valore della proprietà cliente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCliente() {
            return cliente;
        }

        /**
         * Imposta il valore della proprietà cliente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCliente(String value) {
            this.cliente = value;
        }

        /**
         * Recupera il valore della proprietà tipo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipo() {
            return tipo;
        }

        /**
         * Imposta il valore della proprietà tipo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipo(String value) {
            this.tipo = value;
        }

        /**
         * Recupera il valore della proprietà chiave.
         * 
         */
        public int getChiave() {
            return chiave;
        }

        /**
         * Imposta il valore della proprietà chiave.
         * 
         */
        public void setChiave(int value) {
            this.chiave = value;
        }

        /**
         * Recupera il valore della proprietà quantita.
         * 
         */
        public int getQuantita() {
            return quantita;
        }

        /**
         * Imposta il valore della proprietà quantita.
         * 
         */
        public void setQuantita(int value) {
            this.quantita = value;
        }

        /**
         * Recupera il valore della proprietà cauzione.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCauzione() {
            return cauzione;
        }

        /**
         * Imposta il valore della proprietà cauzione.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCauzione(BigDecimal value) {
            this.cauzione = value;
        }

        /**
         * Recupera il valore della proprietà incassato.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIncassato() {
            return incassato;
        }

        /**
         * Imposta il valore della proprietà incassato.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIncassato(BigDecimal value) {
            this.incassato = value;
        }

        /**
         * Recupera il valore della proprietà reso.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getReso() {
            return reso;
        }

        /**
         * Imposta il valore della proprietà reso.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setReso(BigDecimal value) {
            this.reso = value;
        }

        /**
         * Recupera il valore della proprietà omaggio.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOmaggio() {
            return omaggio;
        }

        /**
         * Imposta il valore della proprietà omaggio.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOmaggio(BigDecimal value) {
            this.omaggio = value;
        }

        /**
         * Recupera il valore della proprietà note.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNote() {
            return note;
        }

        /**
         * Imposta il valore della proprietà note.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNote(String value) {
            this.note = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Cliente"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Numero_fattura" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Data_fattura" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="Totale_fattura" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Totale_imponibile" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Totale_imposta" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Aliquota_1" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *         &lt;element name="Imponibile_1" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Imposta_1" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Aliquota_2" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *         &lt;element name="Imponibile_2" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Imposta_2" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Abbuono" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cliente",
        "numeroFattura",
        "dataFattura",
        "totaleFattura",
        "totaleImponibile",
        "totaleImposta",
        "aliquota1",
        "imponibile1",
        "imposta1",
        "aliquota2",
        "imponibile2",
        "imposta2",
        "abbuono"
    })
    public static class Fatture {

        @XmlElement(name = "Cliente", required = true)
        protected String cliente;
        @XmlElement(name = "Numero_fattura")
        protected int numeroFattura;
        @XmlElement(name = "Data_fattura", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataFattura;
        @XmlElement(name = "Totale_fattura", required = true)
        protected BigDecimal totaleFattura;
        @XmlElement(name = "Totale_imponibile", required = true)
        protected BigDecimal totaleImponibile;
        @XmlElement(name = "Totale_imposta", required = true)
        protected BigDecimal totaleImposta;
        @XmlElement(name = "Aliquota_1")
        protected double aliquota1;
        @XmlElement(name = "Imponibile_1", required = true)
        protected BigDecimal imponibile1;
        @XmlElement(name = "Imposta_1", required = true)
        protected BigDecimal imposta1;
        @XmlElement(name = "Aliquota_2")
        protected double aliquota2;
        @XmlElement(name = "Imponibile_2", required = true)
        protected BigDecimal imponibile2;
        @XmlElement(name = "Imposta_2", required = true)
        protected BigDecimal imposta2;
        @XmlElement(name = "Abbuono", required = true)
        protected BigDecimal abbuono;

        /**
         * Recupera il valore della proprietà cliente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCliente() {
            return cliente;
        }

        /**
         * Imposta il valore della proprietà cliente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCliente(String value) {
            this.cliente = value;
        }

        /**
         * Recupera il valore della proprietà numeroFattura.
         * 
         */
        public int getNumeroFattura() {
            return numeroFattura;
        }

        /**
         * Imposta il valore della proprietà numeroFattura.
         * 
         */
        public void setNumeroFattura(int value) {
            this.numeroFattura = value;
        }

        /**
         * Recupera il valore della proprietà dataFattura.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataFattura() {
            return dataFattura;
        }

        /**
         * Imposta il valore della proprietà dataFattura.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataFattura(XMLGregorianCalendar value) {
            this.dataFattura = value;
        }

        /**
         * Recupera il valore della proprietà totaleFattura.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotaleFattura() {
            return totaleFattura;
        }

        /**
         * Imposta il valore della proprietà totaleFattura.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotaleFattura(BigDecimal value) {
            this.totaleFattura = value;
        }

        /**
         * Recupera il valore della proprietà totaleImponibile.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotaleImponibile() {
            return totaleImponibile;
        }

        /**
         * Imposta il valore della proprietà totaleImponibile.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotaleImponibile(BigDecimal value) {
            this.totaleImponibile = value;
        }

        /**
         * Recupera il valore della proprietà totaleImposta.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotaleImposta() {
            return totaleImposta;
        }

        /**
         * Imposta il valore della proprietà totaleImposta.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotaleImposta(BigDecimal value) {
            this.totaleImposta = value;
        }

        /**
         * Recupera il valore della proprietà aliquota1.
         * 
         */
        public double getAliquota1() {
            return aliquota1;
        }

        /**
         * Imposta il valore della proprietà aliquota1.
         * 
         */
        public void setAliquota1(double value) {
            this.aliquota1 = value;
        }

        /**
         * Recupera il valore della proprietà imponibile1.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImponibile1() {
            return imponibile1;
        }

        /**
         * Imposta il valore della proprietà imponibile1.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImponibile1(BigDecimal value) {
            this.imponibile1 = value;
        }

        /**
         * Recupera il valore della proprietà imposta1.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImposta1() {
            return imposta1;
        }

        /**
         * Imposta il valore della proprietà imposta1.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImposta1(BigDecimal value) {
            this.imposta1 = value;
        }

        /**
         * Recupera il valore della proprietà aliquota2.
         * 
         */
        public double getAliquota2() {
            return aliquota2;
        }

        /**
         * Imposta il valore della proprietà aliquota2.
         * 
         */
        public void setAliquota2(double value) {
            this.aliquota2 = value;
        }

        /**
         * Recupera il valore della proprietà imponibile2.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImponibile2() {
            return imponibile2;
        }

        /**
         * Imposta il valore della proprietà imponibile2.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImponibile2(BigDecimal value) {
            this.imponibile2 = value;
        }

        /**
         * Recupera il valore della proprietà imposta2.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImposta2() {
            return imposta2;
        }

        /**
         * Imposta il valore della proprietà imposta2.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImposta2(BigDecimal value) {
            this.imposta2 = value;
        }

        /**
         * Recupera il valore della proprietà abbuono.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getAbbuono() {
            return abbuono;
        }

        /**
         * Imposta il valore della proprietà abbuono.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setAbbuono(BigDecimal value) {
            this.abbuono = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Data_fattura" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="Numero_fattura" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Prodotto"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Omaggio" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Prezzo" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Iva" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *         &lt;element name="Somministrazione" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dataFattura",
        "numeroFattura",
        "prodotto",
        "quantita",
        "omaggio",
        "prezzo",
        "iva",
        "somministrazione"
    })
    public static class FattureRighe {

        @XmlElement(name = "Data_fattura", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataFattura;
        @XmlElement(name = "Numero_fattura")
        protected int numeroFattura;
        @XmlElement(name = "Prodotto", required = true)
        protected String prodotto;
        @XmlElement(name = "Quantita")
        protected int quantita;
        @XmlElement(name = "Omaggio")
        protected int omaggio;
        @XmlElement(name = "Prezzo", required = true)
        protected BigDecimal prezzo;
        @XmlElement(name = "Iva")
        protected double iva;
        @XmlElement(name = "Somministrazione")
        protected boolean somministrazione;

        /**
         * Recupera il valore della proprietà dataFattura.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataFattura() {
            return dataFattura;
        }

        /**
         * Imposta il valore della proprietà dataFattura.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataFattura(XMLGregorianCalendar value) {
            this.dataFattura = value;
        }

        /**
         * Recupera il valore della proprietà numeroFattura.
         * 
         */
        public int getNumeroFattura() {
            return numeroFattura;
        }

        /**
         * Imposta il valore della proprietà numeroFattura.
         * 
         */
        public void setNumeroFattura(int value) {
            this.numeroFattura = value;
        }

        /**
         * Recupera il valore della proprietà prodotto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProdotto() {
            return prodotto;
        }

        /**
         * Imposta il valore della proprietà prodotto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProdotto(String value) {
            this.prodotto = value;
        }

        /**
         * Recupera il valore della proprietà quantita.
         * 
         */
        public int getQuantita() {
            return quantita;
        }

        /**
         * Imposta il valore della proprietà quantita.
         * 
         */
        public void setQuantita(int value) {
            this.quantita = value;
        }

        /**
         * Recupera il valore della proprietà omaggio.
         * 
         */
        public int getOmaggio() {
            return omaggio;
        }

        /**
         * Imposta il valore della proprietà omaggio.
         * 
         */
        public void setOmaggio(int value) {
            this.omaggio = value;
        }

        /**
         * Recupera il valore della proprietà prezzo.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPrezzo() {
            return prezzo;
        }

        /**
         * Imposta il valore della proprietà prezzo.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPrezzo(BigDecimal value) {
            this.prezzo = value;
        }

        /**
         * Recupera il valore della proprietà iva.
         * 
         */
        public double getIva() {
            return iva;
        }

        /**
         * Imposta il valore della proprietà iva.
         * 
         */
        public void setIva(double value) {
            this.iva = value;
        }

        /**
         * Recupera il valore della proprietà somministrazione.
         * 
         */
        public boolean isSomministrazione() {
            return somministrazione;
        }

        /**
         * Imposta il valore della proprietà somministrazione.
         * 
         */
        public void setSomministrazione(boolean value) {
            this.somministrazione = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="DataMovimento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="MatricolaInstallata"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="BattuteInstallata" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="ModelloInstallato"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Flag_nuova_installazione" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="MatricolaRitirata"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="BattuteRitirata" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="ModelloRitirato"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Flag_ritiro_definitivo" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="Armadietti" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Cestini" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Note"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="255"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "installazione",
        "dataMovimento",
        "matricolaInstallata",
        "battuteInstallata",
        "modelloInstallato",
        "flagNuovaInstallazione",
        "matricolaRitirata",
        "battuteRitirata",
        "modelloRitirato",
        "flagRitiroDefinitivo",
        "armadietti",
        "cestini",
        "note"
    })
    public static class InstallazioniRitiriDistributori {

        @XmlElement(name = "Installazione")
        protected int installazione;
        @XmlElement(name = "DataMovimento", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataMovimento;
        @XmlElement(name = "MatricolaInstallata", required = true)
        protected String matricolaInstallata;
        @XmlElement(name = "BattuteInstallata")
        protected int battuteInstallata;
        @XmlElement(name = "ModelloInstallato", required = true)
        protected String modelloInstallato;
        @XmlElement(name = "Flag_nuova_installazione")
        protected boolean flagNuovaInstallazione;
        @XmlElement(name = "MatricolaRitirata", required = true)
        protected String matricolaRitirata;
        @XmlElement(name = "BattuteRitirata")
        protected int battuteRitirata;
        @XmlElement(name = "ModelloRitirato", required = true)
        protected String modelloRitirato;
        @XmlElement(name = "Flag_ritiro_definitivo")
        protected boolean flagRitiroDefinitivo;
        @XmlElement(name = "Armadietti")
        protected int armadietti;
        @XmlElement(name = "Cestini")
        protected int cestini;
        @XmlElement(name = "Note", required = true)
        protected String note;

        /**
         * Recupera il valore della proprietà installazione.
         * 
         */
        public int getInstallazione() {
            return installazione;
        }

        /**
         * Imposta il valore della proprietà installazione.
         * 
         */
        public void setInstallazione(int value) {
            this.installazione = value;
        }

        /**
         * Recupera il valore della proprietà dataMovimento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataMovimento() {
            return dataMovimento;
        }

        /**
         * Imposta il valore della proprietà dataMovimento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataMovimento(XMLGregorianCalendar value) {
            this.dataMovimento = value;
        }

        /**
         * Recupera il valore della proprietà matricolaInstallata.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMatricolaInstallata() {
            return matricolaInstallata;
        }

        /**
         * Imposta il valore della proprietà matricolaInstallata.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMatricolaInstallata(String value) {
            this.matricolaInstallata = value;
        }

        /**
         * Recupera il valore della proprietà battuteInstallata.
         * 
         */
        public int getBattuteInstallata() {
            return battuteInstallata;
        }

        /**
         * Imposta il valore della proprietà battuteInstallata.
         * 
         */
        public void setBattuteInstallata(int value) {
            this.battuteInstallata = value;
        }

        /**
         * Recupera il valore della proprietà modelloInstallato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getModelloInstallato() {
            return modelloInstallato;
        }

        /**
         * Imposta il valore della proprietà modelloInstallato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setModelloInstallato(String value) {
            this.modelloInstallato = value;
        }

        /**
         * Recupera il valore della proprietà flagNuovaInstallazione.
         * 
         */
        public boolean isFlagNuovaInstallazione() {
            return flagNuovaInstallazione;
        }

        /**
         * Imposta il valore della proprietà flagNuovaInstallazione.
         * 
         */
        public void setFlagNuovaInstallazione(boolean value) {
            this.flagNuovaInstallazione = value;
        }

        /**
         * Recupera il valore della proprietà matricolaRitirata.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMatricolaRitirata() {
            return matricolaRitirata;
        }

        /**
         * Imposta il valore della proprietà matricolaRitirata.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMatricolaRitirata(String value) {
            this.matricolaRitirata = value;
        }

        /**
         * Recupera il valore della proprietà battuteRitirata.
         * 
         */
        public int getBattuteRitirata() {
            return battuteRitirata;
        }

        /**
         * Imposta il valore della proprietà battuteRitirata.
         * 
         */
        public void setBattuteRitirata(int value) {
            this.battuteRitirata = value;
        }

        /**
         * Recupera il valore della proprietà modelloRitirato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getModelloRitirato() {
            return modelloRitirato;
        }

        /**
         * Imposta il valore della proprietà modelloRitirato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setModelloRitirato(String value) {
            this.modelloRitirato = value;
        }

        /**
         * Recupera il valore della proprietà flagRitiroDefinitivo.
         * 
         */
        public boolean isFlagRitiroDefinitivo() {
            return flagRitiroDefinitivo;
        }

        /**
         * Imposta il valore della proprietà flagRitiroDefinitivo.
         * 
         */
        public void setFlagRitiroDefinitivo(boolean value) {
            this.flagRitiroDefinitivo = value;
        }

        /**
         * Recupera il valore della proprietà armadietti.
         * 
         */
        public int getArmadietti() {
            return armadietti;
        }

        /**
         * Imposta il valore della proprietà armadietti.
         * 
         */
        public void setArmadietti(int value) {
            this.armadietti = value;
        }

        /**
         * Recupera il valore della proprietà cestini.
         * 
         */
        public int getCestini() {
            return cestini;
        }

        /**
         * Imposta il valore della proprietà cestini.
         * 
         */
        public void setCestini(int value) {
            this.cestini = value;
        }

        /**
         * Recupera il valore della proprietà note.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNote() {
            return note;
        }

        /**
         * Imposta il valore della proprietà note.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNote(String value) {
            this.note = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Data_intervento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Matricola"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Tempo_trasferimento" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
     *         &lt;element name="Tempo_intervento" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
     *         &lt;element name="Lettura_iniziale" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Lettura_finale" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Scatti_prova" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Descrizione_aggiuntiva" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="255"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Ora_inizio" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Ora_fine" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="BarCode" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="Numero_chiamata" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "dataIntervento",
        "installazione",
        "matricola",
        "tempoTrasferimento",
        "tempoIntervento",
        "letturaIniziale",
        "letturaFinale",
        "scattiProva",
        "descrizioneAggiuntiva",
        "oraInizio",
        "oraFine",
        "barCode",
        "numeroChiamata"
    })
    public static class InterventiManutenzione {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Data_intervento", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataIntervento;
        @XmlElement(name = "Installazione")
        protected int installazione;
        @XmlElement(name = "Matricola", required = true)
        protected String matricola;
        @XmlElement(name = "Tempo_trasferimento")
        protected short tempoTrasferimento;
        @XmlElement(name = "Tempo_intervento")
        protected short tempoIntervento;
        @XmlElement(name = "Lettura_iniziale")
        protected int letturaIniziale;
        @XmlElement(name = "Lettura_finale")
        protected int letturaFinale;
        @XmlElement(name = "Scatti_prova")
        protected int scattiProva;
        @XmlElement(name = "Descrizione_aggiuntiva")
        protected String descrizioneAggiuntiva;
        @XmlElement(name = "Ora_inizio")
        protected String oraInizio;
        @XmlElement(name = "Ora_fine")
        protected String oraFine;
        @XmlElement(name = "BarCode")
        protected boolean barCode;
        @XmlElement(name = "Numero_chiamata")
        protected Integer numeroChiamata;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà dataIntervento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataIntervento() {
            return dataIntervento;
        }

        /**
         * Imposta il valore della proprietà dataIntervento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataIntervento(XMLGregorianCalendar value) {
            this.dataIntervento = value;
        }

        /**
         * Recupera il valore della proprietà installazione.
         * 
         */
        public int getInstallazione() {
            return installazione;
        }

        /**
         * Imposta il valore della proprietà installazione.
         * 
         */
        public void setInstallazione(int value) {
            this.installazione = value;
        }

        /**
         * Recupera il valore della proprietà matricola.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMatricola() {
            return matricola;
        }

        /**
         * Imposta il valore della proprietà matricola.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMatricola(String value) {
            this.matricola = value;
        }

        /**
         * Recupera il valore della proprietà tempoTrasferimento.
         * 
         */
        public short getTempoTrasferimento() {
            return tempoTrasferimento;
        }

        /**
         * Imposta il valore della proprietà tempoTrasferimento.
         * 
         */
        public void setTempoTrasferimento(short value) {
            this.tempoTrasferimento = value;
        }

        /**
         * Recupera il valore della proprietà tempoIntervento.
         * 
         */
        public short getTempoIntervento() {
            return tempoIntervento;
        }

        /**
         * Imposta il valore della proprietà tempoIntervento.
         * 
         */
        public void setTempoIntervento(short value) {
            this.tempoIntervento = value;
        }

        /**
         * Recupera il valore della proprietà letturaIniziale.
         * 
         */
        public int getLetturaIniziale() {
            return letturaIniziale;
        }

        /**
         * Imposta il valore della proprietà letturaIniziale.
         * 
         */
        public void setLetturaIniziale(int value) {
            this.letturaIniziale = value;
        }

        /**
         * Recupera il valore della proprietà letturaFinale.
         * 
         */
        public int getLetturaFinale() {
            return letturaFinale;
        }

        /**
         * Imposta il valore della proprietà letturaFinale.
         * 
         */
        public void setLetturaFinale(int value) {
            this.letturaFinale = value;
        }

        /**
         * Recupera il valore della proprietà scattiProva.
         * 
         */
        public int getScattiProva() {
            return scattiProva;
        }

        /**
         * Imposta il valore della proprietà scattiProva.
         * 
         */
        public void setScattiProva(int value) {
            this.scattiProva = value;
        }

        /**
         * Recupera il valore della proprietà descrizioneAggiuntiva.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrizioneAggiuntiva() {
            return descrizioneAggiuntiva;
        }

        /**
         * Imposta il valore della proprietà descrizioneAggiuntiva.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrizioneAggiuntiva(String value) {
            this.descrizioneAggiuntiva = value;
        }

        /**
         * Recupera il valore della proprietà oraInizio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraInizio() {
            return oraInizio;
        }

        /**
         * Imposta il valore della proprietà oraInizio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraInizio(String value) {
            this.oraInizio = value;
        }

        /**
         * Recupera il valore della proprietà oraFine.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraFine() {
            return oraFine;
        }

        /**
         * Imposta il valore della proprietà oraFine.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraFine(String value) {
            this.oraFine = value;
        }

        /**
         * Recupera il valore della proprietà barCode.
         * 
         */
        public boolean isBarCode() {
            return barCode;
        }

        /**
         * Imposta il valore della proprietà barCode.
         * 
         */
        public void setBarCode(boolean value) {
            this.barCode = value;
        }

        /**
         * Recupera il valore della proprietà numeroChiamata.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getNumeroChiamata() {
            return numeroChiamata;
        }

        /**
         * Imposta il valore della proprietà numeroChiamata.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setNumeroChiamata(Integer value) {
            this.numeroChiamata = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Manutenzione"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "manutenzione"
    })
    public static class InterventiManutenzioneDettaglio {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Manutenzione", required = true)
        protected String manutenzione;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà manutenzione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getManutenzione() {
            return manutenzione;
        }

        /**
         * Imposta il valore della proprietà manutenzione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setManutenzione(String value) {
            this.manutenzione = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Prodotto"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Quantita_inventario" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "prodotto",
        "quantitaInventario"
    })
    public static class Inventari {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Prodotto", required = true)
        protected String prodotto;
        @XmlElement(name = "Quantita_inventario")
        protected double quantitaInventario;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà prodotto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProdotto() {
            return prodotto;
        }

        /**
         * Imposta il valore della proprietà prodotto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProdotto(String value) {
            this.prodotto = value;
        }

        /**
         * Recupera il valore della proprietà quantitaInventario.
         * 
         */
        public double getQuantitaInventario() {
            return quantitaInventario;
        }

        /**
         * Imposta il valore della proprietà quantitaInventario.
         * 
         */
        public void setQuantitaInventario(double value) {
            this.quantitaInventario = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Log" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="Sql" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "log",
        "sql"
    })
    public static class Log {

        @XmlElement(name = "Log")
        protected String log;
        @XmlElement(name = "Sql")
        protected String sql;

        /**
         * Recupera il valore della proprietà log.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLog() {
            return log;
        }

        /**
         * Imposta il valore della proprietà log.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLog(String value) {
            this.log = value;
        }

        /**
         * Recupera il valore della proprietà sql.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSql() {
            return sql;
        }

        /**
         * Imposta il valore della proprietà sql.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSql(String value) {
            this.sql = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Ragione_sociale" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="50"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Indirizzo" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="50"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Cap" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Citta" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="50"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Provincia" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="2"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Listino_prodotti_1"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Listino_prodotti_2"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Listino_prodotti_3"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "installazione",
        "ragioneSociale",
        "indirizzo",
        "cap",
        "citta",
        "provincia",
        "listinoProdotti1",
        "listinoProdotti2",
        "listinoProdotti3"
    })
    public static class NuoveInstallazioniDettaglio {

        @XmlElement(name = "Installazione")
        protected int installazione;
        @XmlElement(name = "Ragione_sociale")
        protected String ragioneSociale;
        @XmlElement(name = "Indirizzo")
        protected String indirizzo;
        @XmlElement(name = "Cap")
        protected String cap;
        @XmlElement(name = "Citta")
        protected String citta;
        @XmlElement(name = "Provincia")
        protected String provincia;
        @XmlElement(name = "Listino_prodotti_1", required = true)
        protected String listinoProdotti1;
        @XmlElement(name = "Listino_prodotti_2", required = true)
        protected String listinoProdotti2;
        @XmlElement(name = "Listino_prodotti_3", required = true)
        protected String listinoProdotti3;

        /**
         * Recupera il valore della proprietà installazione.
         * 
         */
        public int getInstallazione() {
            return installazione;
        }

        /**
         * Imposta il valore della proprietà installazione.
         * 
         */
        public void setInstallazione(int value) {
            this.installazione = value;
        }

        /**
         * Recupera il valore della proprietà ragioneSociale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRagioneSociale() {
            return ragioneSociale;
        }

        /**
         * Imposta il valore della proprietà ragioneSociale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRagioneSociale(String value) {
            this.ragioneSociale = value;
        }

        /**
         * Recupera il valore della proprietà indirizzo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIndirizzo() {
            return indirizzo;
        }

        /**
         * Imposta il valore della proprietà indirizzo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIndirizzo(String value) {
            this.indirizzo = value;
        }

        /**
         * Recupera il valore della proprietà cap.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCap() {
            return cap;
        }

        /**
         * Imposta il valore della proprietà cap.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCap(String value) {
            this.cap = value;
        }

        /**
         * Recupera il valore della proprietà citta.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCitta() {
            return citta;
        }

        /**
         * Imposta il valore della proprietà citta.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCitta(String value) {
            this.citta = value;
        }

        /**
         * Recupera il valore della proprietà provincia.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProvincia() {
            return provincia;
        }

        /**
         * Imposta il valore della proprietà provincia.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProvincia(String value) {
            this.provincia = value;
        }

        /**
         * Recupera il valore della proprietà listinoProdotti1.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getListinoProdotti1() {
            return listinoProdotti1;
        }

        /**
         * Imposta il valore della proprietà listinoProdotti1.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setListinoProdotti1(String value) {
            this.listinoProdotti1 = value;
        }

        /**
         * Recupera il valore della proprietà listinoProdotti2.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getListinoProdotti2() {
            return listinoProdotti2;
        }

        /**
         * Imposta il valore della proprietà listinoProdotti2.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setListinoProdotti2(String value) {
            this.listinoProdotti2 = value;
        }

        /**
         * Recupera il valore della proprietà listinoProdotti3.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getListinoProdotti3() {
            return listinoProdotti3;
        }

        /**
         * Imposta il valore della proprietà listinoProdotti3.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setListinoProdotti3(String value) {
            this.listinoProdotti3 = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Matricola"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="TipoChiamata"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Note" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="255"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "installazione",
        "matricola",
        "data",
        "tipoChiamata",
        "note"
    })
    public static class NuoveRichiesteIntervento {

        @XmlElement(name = "Installazione")
        protected int installazione;
        @XmlElement(name = "Matricola", required = true)
        protected String matricola;
        @XmlElement(name = "Data", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar data;
        @XmlElement(name = "TipoChiamata", required = true)
        protected String tipoChiamata;
        @XmlElement(name = "Note")
        protected String note;

        /**
         * Recupera il valore della proprietà installazione.
         * 
         */
        public int getInstallazione() {
            return installazione;
        }

        /**
         * Imposta il valore della proprietà installazione.
         * 
         */
        public void setInstallazione(int value) {
            this.installazione = value;
        }

        /**
         * Recupera il valore della proprietà matricola.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMatricola() {
            return matricola;
        }

        /**
         * Imposta il valore della proprietà matricola.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMatricola(String value) {
            this.matricola = value;
        }

        /**
         * Recupera il valore della proprietà data.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getData() {
            return data;
        }

        /**
         * Imposta il valore della proprietà data.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setData(XMLGregorianCalendar value) {
            this.data = value;
        }

        /**
         * Recupera il valore della proprietà tipoChiamata.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoChiamata() {
            return tipoChiamata;
        }

        /**
         * Imposta il valore della proprietà tipoChiamata.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoChiamata(String value) {
            this.tipoChiamata = value;
        }

        /**
         * Recupera il valore della proprietà note.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNote() {
            return note;
        }

        /**
         * Imposta il valore della proprietà note.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNote(String value) {
            this.note = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Raccolta"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Prodotto"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Punti" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Cliente"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Data_ordine" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="Data_consegna" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="Valore" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Sospeso" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Pagato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Ordine" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="Rifornimento" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Nuovo" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "raccolta",
        "prodotto",
        "punti",
        "cliente",
        "quantita",
        "dataOrdine",
        "dataConsegna",
        "valore",
        "sospeso",
        "pagato",
        "ordine",
        "rifornimento",
        "nuovo"
    })
    public static class RaccoltaPuntiPremiClienti {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Raccolta", required = true)
        protected String raccolta;
        @XmlElement(name = "Prodotto", required = true)
        protected String prodotto;
        @XmlElement(name = "Punti")
        protected int punti;
        @XmlElement(name = "Cliente", required = true)
        protected String cliente;
        @XmlElement(name = "Quantita")
        protected int quantita;
        @XmlElement(name = "Data_ordine")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataOrdine;
        @XmlElement(name = "Data_consegna")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataConsegna;
        @XmlElement(name = "Valore", required = true)
        protected BigDecimal valore;
        @XmlElement(name = "Sospeso", required = true)
        protected BigDecimal sospeso;
        @XmlElement(name = "Pagato", required = true)
        protected BigDecimal pagato;
        @XmlElement(name = "Ordine")
        protected Integer ordine;
        @XmlElement(name = "Rifornimento")
        protected int rifornimento;
        @XmlElement(name = "Nuovo")
        protected boolean nuovo;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà raccolta.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRaccolta() {
            return raccolta;
        }

        /**
         * Imposta il valore della proprietà raccolta.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRaccolta(String value) {
            this.raccolta = value;
        }

        /**
         * Recupera il valore della proprietà prodotto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProdotto() {
            return prodotto;
        }

        /**
         * Imposta il valore della proprietà prodotto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProdotto(String value) {
            this.prodotto = value;
        }

        /**
         * Recupera il valore della proprietà punti.
         * 
         */
        public int getPunti() {
            return punti;
        }

        /**
         * Imposta il valore della proprietà punti.
         * 
         */
        public void setPunti(int value) {
            this.punti = value;
        }

        /**
         * Recupera il valore della proprietà cliente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCliente() {
            return cliente;
        }

        /**
         * Imposta il valore della proprietà cliente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCliente(String value) {
            this.cliente = value;
        }

        /**
         * Recupera il valore della proprietà quantita.
         * 
         */
        public int getQuantita() {
            return quantita;
        }

        /**
         * Imposta il valore della proprietà quantita.
         * 
         */
        public void setQuantita(int value) {
            this.quantita = value;
        }

        /**
         * Recupera il valore della proprietà dataOrdine.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataOrdine() {
            return dataOrdine;
        }

        /**
         * Imposta il valore della proprietà dataOrdine.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataOrdine(XMLGregorianCalendar value) {
            this.dataOrdine = value;
        }

        /**
         * Recupera il valore della proprietà dataConsegna.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataConsegna() {
            return dataConsegna;
        }

        /**
         * Imposta il valore della proprietà dataConsegna.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataConsegna(XMLGregorianCalendar value) {
            this.dataConsegna = value;
        }

        /**
         * Recupera il valore della proprietà valore.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getValore() {
            return valore;
        }

        /**
         * Imposta il valore della proprietà valore.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setValore(BigDecimal value) {
            this.valore = value;
        }

        /**
         * Recupera il valore della proprietà sospeso.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getSospeso() {
            return sospeso;
        }

        /**
         * Imposta il valore della proprietà sospeso.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setSospeso(BigDecimal value) {
            this.sospeso = value;
        }

        /**
         * Recupera il valore della proprietà pagato.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPagato() {
            return pagato;
        }

        /**
         * Imposta il valore della proprietà pagato.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPagato(BigDecimal value) {
            this.pagato = value;
        }

        /**
         * Recupera il valore della proprietà ordine.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getOrdine() {
            return ordine;
        }

        /**
         * Imposta il valore della proprietà ordine.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setOrdine(Integer value) {
            this.ordine = value;
        }

        /**
         * Recupera il valore della proprietà rifornimento.
         * 
         */
        public int getRifornimento() {
            return rifornimento;
        }

        /**
         * Imposta il valore della proprietà rifornimento.
         * 
         */
        public void setRifornimento(int value) {
            this.rifornimento = value;
        }

        /**
         * Recupera il valore della proprietà nuovo.
         * 
         */
        public boolean isNuovo() {
            return nuovo;
        }

        /**
         * Imposta il valore della proprietà nuovo.
         * 
         */
        public void setNuovo(boolean value) {
            this.nuovo = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Prodotto"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="MerceNonConforme" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="RiportoInMagazzino" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="Note"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="50"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Prezzo" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Resa" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "prodotto",
        "quantita",
        "merceNonConforme",
        "riportoInMagazzino",
        "note",
        "prezzo",
        "resa"
    })
    public static class ResoMerce {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Prodotto", required = true)
        protected String prodotto;
        @XmlElement(name = "Quantita")
        protected int quantita;
        @XmlElement(name = "MerceNonConforme")
        protected boolean merceNonConforme;
        @XmlElement(name = "RiportoInMagazzino")
        protected boolean riportoInMagazzino;
        @XmlElement(name = "Note", required = true)
        protected String note;
        @XmlElement(name = "Prezzo", required = true)
        protected BigDecimal prezzo;
        @XmlElement(name = "Resa")
        protected int resa;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà prodotto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProdotto() {
            return prodotto;
        }

        /**
         * Imposta il valore della proprietà prodotto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProdotto(String value) {
            this.prodotto = value;
        }

        /**
         * Recupera il valore della proprietà quantita.
         * 
         */
        public int getQuantita() {
            return quantita;
        }

        /**
         * Imposta il valore della proprietà quantita.
         * 
         */
        public void setQuantita(int value) {
            this.quantita = value;
        }

        /**
         * Recupera il valore della proprietà merceNonConforme.
         * 
         */
        public boolean isMerceNonConforme() {
            return merceNonConforme;
        }

        /**
         * Imposta il valore della proprietà merceNonConforme.
         * 
         */
        public void setMerceNonConforme(boolean value) {
            this.merceNonConforme = value;
        }

        /**
         * Recupera il valore della proprietà riportoInMagazzino.
         * 
         */
        public boolean isRiportoInMagazzino() {
            return riportoInMagazzino;
        }

        /**
         * Imposta il valore della proprietà riportoInMagazzino.
         * 
         */
        public void setRiportoInMagazzino(boolean value) {
            this.riportoInMagazzino = value;
        }

        /**
         * Recupera il valore della proprietà note.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNote() {
            return note;
        }

        /**
         * Imposta il valore della proprietà note.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNote(String value) {
            this.note = value;
        }

        /**
         * Recupera il valore della proprietà prezzo.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPrezzo() {
            return prezzo;
        }

        /**
         * Imposta il valore della proprietà prezzo.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPrezzo(BigDecimal value) {
            this.prezzo = value;
        }

        /**
         * Recupera il valore della proprietà resa.
         * 
         */
        public int getResa() {
            return resa;
        }

        /**
         * Imposta il valore della proprietà resa.
         * 
         */
        public void setResa(int value) {
            this.resa = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Numero_fattura" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Data_intervento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="Ora_intervento"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Ora_fine_intervento"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Barcode" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Matricola"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Tipo_gestione" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
     *         &lt;element name="Periferico" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *         &lt;element name="Lettura_precedente" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Lettura" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Prove" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Calcolato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Incassato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Reso" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Sospeso" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Recuperato" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Cassetta" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Cassetta_nuova" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="ProgressivoRichiesta" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="Moneta002ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Moneta005ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Moneta010ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Moneta020ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Moneta050ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Moneta100ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Moneta200ec" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="ValoreResoMerce" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Profilo"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "numeroFattura",
        "dataIntervento",
        "oraIntervento",
        "oraFineIntervento",
        "barcode",
        "installazione",
        "matricola",
        "tipoGestione",
        "periferico",
        "letturaPrecedente",
        "lettura",
        "prove",
        "calcolato",
        "incassato",
        "reso",
        "sospeso",
        "recuperato",
        "cassetta",
        "cassettaNuova",
        "progressivoRichiesta",
        "moneta002Ec",
        "moneta005Ec",
        "moneta010Ec",
        "moneta020Ec",
        "moneta050Ec",
        "moneta100Ec",
        "moneta200Ec",
        "valoreResoMerce",
        "profilo"
    })
    public static class Rifornimenti {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Numero_fattura")
        protected int numeroFattura;
        @XmlElement(name = "Data_intervento", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataIntervento;
        @XmlElement(name = "Ora_intervento", required = true)
        protected String oraIntervento;
        @XmlElement(name = "Ora_fine_intervento", required = true)
        protected String oraFineIntervento;
        @XmlElement(name = "Barcode")
        protected boolean barcode;
        @XmlElement(name = "Installazione")
        protected int installazione;
        @XmlElement(name = "Matricola", required = true)
        protected String matricola;
        @XmlElement(name = "Tipo_gestione")
        protected short tipoGestione;
        @XmlElement(name = "Periferico")
        protected boolean periferico;
        @XmlElement(name = "Lettura_precedente")
        protected int letturaPrecedente;
        @XmlElement(name = "Lettura")
        protected int lettura;
        @XmlElement(name = "Prove")
        protected int prove;
        @XmlElement(name = "Calcolato", required = true)
        protected BigDecimal calcolato;
        @XmlElement(name = "Incassato", required = true)
        protected BigDecimal incassato;
        @XmlElement(name = "Reso", required = true)
        protected BigDecimal reso;
        @XmlElement(name = "Sospeso", required = true)
        protected BigDecimal sospeso;
        @XmlElement(name = "Recuperato", required = true)
        protected BigDecimal recuperato;
        @XmlElement(name = "Cassetta")
        protected int cassetta;
        @XmlElement(name = "Cassetta_nuova")
        protected int cassettaNuova;
        @XmlElement(name = "ProgressivoRichiesta")
        protected Integer progressivoRichiesta;
        @XmlElement(name = "Moneta002ec")
        protected int moneta002Ec;
        @XmlElement(name = "Moneta005ec")
        protected int moneta005Ec;
        @XmlElement(name = "Moneta010ec")
        protected int moneta010Ec;
        @XmlElement(name = "Moneta020ec")
        protected int moneta020Ec;
        @XmlElement(name = "Moneta050ec")
        protected int moneta050Ec;
        @XmlElement(name = "Moneta100ec")
        protected int moneta100Ec;
        @XmlElement(name = "Moneta200ec")
        protected int moneta200Ec;
        @XmlElement(name = "ValoreResoMerce", required = true)
        protected BigDecimal valoreResoMerce;
        @XmlElement(name = "Profilo", required = true)
        protected String profilo;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà numeroFattura.
         * 
         */
        public int getNumeroFattura() {
            return numeroFattura;
        }

        /**
         * Imposta il valore della proprietà numeroFattura.
         * 
         */
        public void setNumeroFattura(int value) {
            this.numeroFattura = value;
        }

        /**
         * Recupera il valore della proprietà dataIntervento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataIntervento() {
            return dataIntervento;
        }

        /**
         * Imposta il valore della proprietà dataIntervento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataIntervento(XMLGregorianCalendar value) {
            this.dataIntervento = value;
        }

        /**
         * Recupera il valore della proprietà oraIntervento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraIntervento() {
            return oraIntervento;
        }

        /**
         * Imposta il valore della proprietà oraIntervento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraIntervento(String value) {
            this.oraIntervento = value;
        }

        /**
         * Recupera il valore della proprietà oraFineIntervento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraFineIntervento() {
            return oraFineIntervento;
        }

        /**
         * Imposta il valore della proprietà oraFineIntervento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraFineIntervento(String value) {
            this.oraFineIntervento = value;
        }

        /**
         * Recupera il valore della proprietà barcode.
         * 
         */
        public boolean isBarcode() {
            return barcode;
        }

        /**
         * Imposta il valore della proprietà barcode.
         * 
         */
        public void setBarcode(boolean value) {
            this.barcode = value;
        }

        /**
         * Recupera il valore della proprietà installazione.
         * 
         */
        public int getInstallazione() {
            return installazione;
        }

        /**
         * Imposta il valore della proprietà installazione.
         * 
         */
        public void setInstallazione(int value) {
            this.installazione = value;
        }

        /**
         * Recupera il valore della proprietà matricola.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMatricola() {
            return matricola;
        }

        /**
         * Imposta il valore della proprietà matricola.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMatricola(String value) {
            this.matricola = value;
        }

        /**
         * Recupera il valore della proprietà tipoGestione.
         * 
         */
        public short getTipoGestione() {
            return tipoGestione;
        }

        /**
         * Imposta il valore della proprietà tipoGestione.
         * 
         */
        public void setTipoGestione(short value) {
            this.tipoGestione = value;
        }

        /**
         * Recupera il valore della proprietà periferico.
         * 
         */
        public boolean isPeriferico() {
            return periferico;
        }

        /**
         * Imposta il valore della proprietà periferico.
         * 
         */
        public void setPeriferico(boolean value) {
            this.periferico = value;
        }

        /**
         * Recupera il valore della proprietà letturaPrecedente.
         * 
         */
        public int getLetturaPrecedente() {
            return letturaPrecedente;
        }

        /**
         * Imposta il valore della proprietà letturaPrecedente.
         * 
         */
        public void setLetturaPrecedente(int value) {
            this.letturaPrecedente = value;
        }

        /**
         * Recupera il valore della proprietà lettura.
         * 
         */
        public int getLettura() {
            return lettura;
        }

        /**
         * Imposta il valore della proprietà lettura.
         * 
         */
        public void setLettura(int value) {
            this.lettura = value;
        }

        /**
         * Recupera il valore della proprietà prove.
         * 
         */
        public int getProve() {
            return prove;
        }

        /**
         * Imposta il valore della proprietà prove.
         * 
         */
        public void setProve(int value) {
            this.prove = value;
        }

        /**
         * Recupera il valore della proprietà calcolato.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCalcolato() {
            return calcolato;
        }

        /**
         * Imposta il valore della proprietà calcolato.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCalcolato(BigDecimal value) {
            this.calcolato = value;
        }

        /**
         * Recupera il valore della proprietà incassato.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIncassato() {
            return incassato;
        }

        /**
         * Imposta il valore della proprietà incassato.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIncassato(BigDecimal value) {
            this.incassato = value;
        }

        /**
         * Recupera il valore della proprietà reso.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getReso() {
            return reso;
        }

        /**
         * Imposta il valore della proprietà reso.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setReso(BigDecimal value) {
            this.reso = value;
        }

        /**
         * Recupera il valore della proprietà sospeso.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getSospeso() {
            return sospeso;
        }

        /**
         * Imposta il valore della proprietà sospeso.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setSospeso(BigDecimal value) {
            this.sospeso = value;
        }

        /**
         * Recupera il valore della proprietà recuperato.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getRecuperato() {
            return recuperato;
        }

        /**
         * Imposta il valore della proprietà recuperato.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setRecuperato(BigDecimal value) {
            this.recuperato = value;
        }

        /**
         * Recupera il valore della proprietà cassetta.
         * 
         */
        public int getCassetta() {
            return cassetta;
        }

        /**
         * Imposta il valore della proprietà cassetta.
         * 
         */
        public void setCassetta(int value) {
            this.cassetta = value;
        }

        /**
         * Recupera il valore della proprietà cassettaNuova.
         * 
         */
        public int getCassettaNuova() {
            return cassettaNuova;
        }

        /**
         * Imposta il valore della proprietà cassettaNuova.
         * 
         */
        public void setCassettaNuova(int value) {
            this.cassettaNuova = value;
        }

        /**
         * Recupera il valore della proprietà progressivoRichiesta.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getProgressivoRichiesta() {
            return progressivoRichiesta;
        }

        /**
         * Imposta il valore della proprietà progressivoRichiesta.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setProgressivoRichiesta(Integer value) {
            this.progressivoRichiesta = value;
        }

        /**
         * Recupera il valore della proprietà moneta002Ec.
         * 
         */
        public int getMoneta002Ec() {
            return moneta002Ec;
        }

        /**
         * Imposta il valore della proprietà moneta002Ec.
         * 
         */
        public void setMoneta002Ec(int value) {
            this.moneta002Ec = value;
        }

        /**
         * Recupera il valore della proprietà moneta005Ec.
         * 
         */
        public int getMoneta005Ec() {
            return moneta005Ec;
        }

        /**
         * Imposta il valore della proprietà moneta005Ec.
         * 
         */
        public void setMoneta005Ec(int value) {
            this.moneta005Ec = value;
        }

        /**
         * Recupera il valore della proprietà moneta010Ec.
         * 
         */
        public int getMoneta010Ec() {
            return moneta010Ec;
        }

        /**
         * Imposta il valore della proprietà moneta010Ec.
         * 
         */
        public void setMoneta010Ec(int value) {
            this.moneta010Ec = value;
        }

        /**
         * Recupera il valore della proprietà moneta020Ec.
         * 
         */
        public int getMoneta020Ec() {
            return moneta020Ec;
        }

        /**
         * Imposta il valore della proprietà moneta020Ec.
         * 
         */
        public void setMoneta020Ec(int value) {
            this.moneta020Ec = value;
        }

        /**
         * Recupera il valore della proprietà moneta050Ec.
         * 
         */
        public int getMoneta050Ec() {
            return moneta050Ec;
        }

        /**
         * Imposta il valore della proprietà moneta050Ec.
         * 
         */
        public void setMoneta050Ec(int value) {
            this.moneta050Ec = value;
        }

        /**
         * Recupera il valore della proprietà moneta100Ec.
         * 
         */
        public int getMoneta100Ec() {
            return moneta100Ec;
        }

        /**
         * Imposta il valore della proprietà moneta100Ec.
         * 
         */
        public void setMoneta100Ec(int value) {
            this.moneta100Ec = value;
        }

        /**
         * Recupera il valore della proprietà moneta200Ec.
         * 
         */
        public int getMoneta200Ec() {
            return moneta200Ec;
        }

        /**
         * Imposta il valore della proprietà moneta200Ec.
         * 
         */
        public void setMoneta200Ec(int value) {
            this.moneta200Ec = value;
        }

        /**
         * Recupera il valore della proprietà valoreResoMerce.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getValoreResoMerce() {
            return valoreResoMerce;
        }

        /**
         * Imposta il valore della proprietà valoreResoMerce.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setValoreResoMerce(BigDecimal value) {
            this.valoreResoMerce = value;
        }

        /**
         * Recupera il valore della proprietà profilo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProfilo() {
            return profilo;
        }

        /**
         * Imposta il valore della proprietà profilo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProfilo(String value) {
            this.profilo = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Prodotto"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *         &lt;element name="Omaggio" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *         &lt;element name="Coefficiente_resa" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
     *         &lt;element name="Prezzo" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "prodotto",
        "quantita",
        "omaggio",
        "coefficienteResa",
        "prezzo"
    })
    public static class RifornimentiProdotti {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Prodotto", required = true)
        protected String prodotto;
        @XmlElement(name = "Quantita")
        protected double quantita;
        @XmlElement(name = "Omaggio")
        protected double omaggio;
        @XmlElement(name = "Coefficiente_resa")
        protected double coefficienteResa;
        @XmlElement(name = "Prezzo", required = true)
        protected BigDecimal prezzo;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà prodotto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProdotto() {
            return prodotto;
        }

        /**
         * Imposta il valore della proprietà prodotto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProdotto(String value) {
            this.prodotto = value;
        }

        /**
         * Recupera il valore della proprietà quantita.
         * 
         */
        public double getQuantita() {
            return quantita;
        }

        /**
         * Imposta il valore della proprietà quantita.
         * 
         */
        public void setQuantita(double value) {
            this.quantita = value;
        }

        /**
         * Recupera il valore della proprietà omaggio.
         * 
         */
        public double getOmaggio() {
            return omaggio;
        }

        /**
         * Imposta il valore della proprietà omaggio.
         * 
         */
        public void setOmaggio(double value) {
            this.omaggio = value;
        }

        /**
         * Recupera il valore della proprietà coefficienteResa.
         * 
         */
        public double getCoefficienteResa() {
            return coefficienteResa;
        }

        /**
         * Imposta il valore della proprietà coefficienteResa.
         * 
         */
        public void setCoefficienteResa(double value) {
            this.coefficienteResa = value;
        }

        /**
         * Recupera il valore della proprietà prezzo.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPrezzo() {
            return prezzo;
        }

        /**
         * Imposta il valore della proprietà prezzo.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPrezzo(BigDecimal value) {
            this.prezzo = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Tipo"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="3"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Codice"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="10"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Elemento"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="20"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Occorrenze" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "tipo",
        "codice",
        "elemento",
        "occorrenze"
    })
    public static class RilevazioneErroriEva {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Tipo", required = true)
        protected String tipo;
        @XmlElement(name = "Codice", required = true)
        protected String codice;
        @XmlElement(name = "Elemento", required = true)
        protected String elemento;
        @XmlElement(name = "Occorrenze")
        protected int occorrenze;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà tipo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipo() {
            return tipo;
        }

        /**
         * Imposta il valore della proprietà tipo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipo(String value) {
            this.tipo = value;
        }

        /**
         * Recupera il valore della proprietà codice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodice() {
            return codice;
        }

        /**
         * Imposta il valore della proprietà codice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodice(String value) {
            this.codice = value;
        }

        /**
         * Recupera il valore della proprietà elemento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getElemento() {
            return elemento;
        }

        /**
         * Imposta il valore della proprietà elemento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setElemento(String value) {
            this.elemento = value;
        }

        /**
         * Recupera il valore della proprietà occorrenze.
         * 
         */
        public int getOccorrenze() {
            return occorrenze;
        }

        /**
         * Imposta il valore della proprietà occorrenze.
         * 
         */
        public void setOccorrenze(int value) {
            this.occorrenze = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Divisore" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA203" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA204" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA301" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA302" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA303" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA304" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA309" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA311" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA401" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA402" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA406" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA407" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA701" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA703" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA705" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA707" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA801" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA1001" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA1003" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="CA1501" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA203" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA204" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA302" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA402" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA501" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA502" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA505" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA506" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA602" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="DA901" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA103" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA104" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA107" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA108" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA111" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA112" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA203" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA204" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA206" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA303" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="VA304" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "divisore",
        "ca203",
        "ca204",
        "ca301",
        "ca302",
        "ca303",
        "ca304",
        "ca309",
        "ca311",
        "ca401",
        "ca402",
        "ca406",
        "ca407",
        "ca701",
        "ca703",
        "ca705",
        "ca707",
        "ca801",
        "ca1001",
        "ca1003",
        "ca1501",
        "da203",
        "da204",
        "da302",
        "da402",
        "da501",
        "da502",
        "da505",
        "da506",
        "da602",
        "da901",
        "va103",
        "va104",
        "va107",
        "va108",
        "va111",
        "va112",
        "va203",
        "va204",
        "va206",
        "va303",
        "va304"
    })
    public static class RilevazioneEva {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Divisore")
        protected Integer divisore;
        @XmlElement(name = "CA203")
        protected Integer ca203;
        @XmlElement(name = "CA204")
        protected Integer ca204;
        @XmlElement(name = "CA301")
        protected Integer ca301;
        @XmlElement(name = "CA302")
        protected Integer ca302;
        @XmlElement(name = "CA303")
        protected Integer ca303;
        @XmlElement(name = "CA304")
        protected Integer ca304;
        @XmlElement(name = "CA309")
        protected Integer ca309;
        @XmlElement(name = "CA311")
        protected Integer ca311;
        @XmlElement(name = "CA401")
        protected Integer ca401;
        @XmlElement(name = "CA402")
        protected Integer ca402;
        @XmlElement(name = "CA406")
        protected Integer ca406;
        @XmlElement(name = "CA407")
        protected Integer ca407;
        @XmlElement(name = "CA701")
        protected Integer ca701;
        @XmlElement(name = "CA703")
        protected Integer ca703;
        @XmlElement(name = "CA705")
        protected Integer ca705;
        @XmlElement(name = "CA707")
        protected Integer ca707;
        @XmlElement(name = "CA801")
        protected Integer ca801;
        @XmlElement(name = "CA1001")
        protected Integer ca1001;
        @XmlElement(name = "CA1003")
        protected Integer ca1003;
        @XmlElement(name = "CA1501")
        protected Integer ca1501;
        @XmlElement(name = "DA203")
        protected Integer da203;
        @XmlElement(name = "DA204")
        protected Integer da204;
        @XmlElement(name = "DA302")
        protected Integer da302;
        @XmlElement(name = "DA402")
        protected Integer da402;
        @XmlElement(name = "DA501")
        protected Integer da501;
        @XmlElement(name = "DA502")
        protected Integer da502;
        @XmlElement(name = "DA505")
        protected Integer da505;
        @XmlElement(name = "DA506")
        protected Integer da506;
        @XmlElement(name = "DA602")
        protected Integer da602;
        @XmlElement(name = "DA901")
        protected Integer da901;
        @XmlElement(name = "VA103")
        protected Integer va103;
        @XmlElement(name = "VA104")
        protected Integer va104;
        @XmlElement(name = "VA107")
        protected Integer va107;
        @XmlElement(name = "VA108")
        protected Integer va108;
        @XmlElement(name = "VA111")
        protected Integer va111;
        @XmlElement(name = "VA112")
        protected Integer va112;
        @XmlElement(name = "VA203")
        protected Integer va203;
        @XmlElement(name = "VA204")
        protected Integer va204;
        @XmlElement(name = "VA206")
        protected Integer va206;
        @XmlElement(name = "VA303")
        protected Integer va303;
        @XmlElement(name = "VA304")
        protected Integer va304;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà divisore.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDivisore() {
            return divisore;
        }

        /**
         * Imposta il valore della proprietà divisore.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDivisore(Integer value) {
            this.divisore = value;
        }

        /**
         * Recupera il valore della proprietà ca203.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA203() {
            return ca203;
        }

        /**
         * Imposta il valore della proprietà ca203.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA203(Integer value) {
            this.ca203 = value;
        }

        /**
         * Recupera il valore della proprietà ca204.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA204() {
            return ca204;
        }

        /**
         * Imposta il valore della proprietà ca204.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA204(Integer value) {
            this.ca204 = value;
        }

        /**
         * Recupera il valore della proprietà ca301.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA301() {
            return ca301;
        }

        /**
         * Imposta il valore della proprietà ca301.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA301(Integer value) {
            this.ca301 = value;
        }

        /**
         * Recupera il valore della proprietà ca302.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA302() {
            return ca302;
        }

        /**
         * Imposta il valore della proprietà ca302.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA302(Integer value) {
            this.ca302 = value;
        }

        /**
         * Recupera il valore della proprietà ca303.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA303() {
            return ca303;
        }

        /**
         * Imposta il valore della proprietà ca303.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA303(Integer value) {
            this.ca303 = value;
        }

        /**
         * Recupera il valore della proprietà ca304.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA304() {
            return ca304;
        }

        /**
         * Imposta il valore della proprietà ca304.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA304(Integer value) {
            this.ca304 = value;
        }

        /**
         * Recupera il valore della proprietà ca309.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA309() {
            return ca309;
        }

        /**
         * Imposta il valore della proprietà ca309.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA309(Integer value) {
            this.ca309 = value;
        }

        /**
         * Recupera il valore della proprietà ca311.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA311() {
            return ca311;
        }

        /**
         * Imposta il valore della proprietà ca311.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA311(Integer value) {
            this.ca311 = value;
        }

        /**
         * Recupera il valore della proprietà ca401.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA401() {
            return ca401;
        }

        /**
         * Imposta il valore della proprietà ca401.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA401(Integer value) {
            this.ca401 = value;
        }

        /**
         * Recupera il valore della proprietà ca402.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA402() {
            return ca402;
        }

        /**
         * Imposta il valore della proprietà ca402.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA402(Integer value) {
            this.ca402 = value;
        }

        /**
         * Recupera il valore della proprietà ca406.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA406() {
            return ca406;
        }

        /**
         * Imposta il valore della proprietà ca406.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA406(Integer value) {
            this.ca406 = value;
        }

        /**
         * Recupera il valore della proprietà ca407.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA407() {
            return ca407;
        }

        /**
         * Imposta il valore della proprietà ca407.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA407(Integer value) {
            this.ca407 = value;
        }

        /**
         * Recupera il valore della proprietà ca701.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA701() {
            return ca701;
        }

        /**
         * Imposta il valore della proprietà ca701.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA701(Integer value) {
            this.ca701 = value;
        }

        /**
         * Recupera il valore della proprietà ca703.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA703() {
            return ca703;
        }

        /**
         * Imposta il valore della proprietà ca703.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA703(Integer value) {
            this.ca703 = value;
        }

        /**
         * Recupera il valore della proprietà ca705.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA705() {
            return ca705;
        }

        /**
         * Imposta il valore della proprietà ca705.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA705(Integer value) {
            this.ca705 = value;
        }

        /**
         * Recupera il valore della proprietà ca707.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA707() {
            return ca707;
        }

        /**
         * Imposta il valore della proprietà ca707.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA707(Integer value) {
            this.ca707 = value;
        }

        /**
         * Recupera il valore della proprietà ca801.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA801() {
            return ca801;
        }

        /**
         * Imposta il valore della proprietà ca801.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA801(Integer value) {
            this.ca801 = value;
        }

        /**
         * Recupera il valore della proprietà ca1001.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA1001() {
            return ca1001;
        }

        /**
         * Imposta il valore della proprietà ca1001.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA1001(Integer value) {
            this.ca1001 = value;
        }

        /**
         * Recupera il valore della proprietà ca1003.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA1003() {
            return ca1003;
        }

        /**
         * Imposta il valore della proprietà ca1003.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA1003(Integer value) {
            this.ca1003 = value;
        }

        /**
         * Recupera il valore della proprietà ca1501.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCA1501() {
            return ca1501;
        }

        /**
         * Imposta il valore della proprietà ca1501.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCA1501(Integer value) {
            this.ca1501 = value;
        }

        /**
         * Recupera il valore della proprietà da203.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA203() {
            return da203;
        }

        /**
         * Imposta il valore della proprietà da203.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA203(Integer value) {
            this.da203 = value;
        }

        /**
         * Recupera il valore della proprietà da204.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA204() {
            return da204;
        }

        /**
         * Imposta il valore della proprietà da204.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA204(Integer value) {
            this.da204 = value;
        }

        /**
         * Recupera il valore della proprietà da302.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA302() {
            return da302;
        }

        /**
         * Imposta il valore della proprietà da302.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA302(Integer value) {
            this.da302 = value;
        }

        /**
         * Recupera il valore della proprietà da402.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA402() {
            return da402;
        }

        /**
         * Imposta il valore della proprietà da402.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA402(Integer value) {
            this.da402 = value;
        }

        /**
         * Recupera il valore della proprietà da501.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA501() {
            return da501;
        }

        /**
         * Imposta il valore della proprietà da501.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA501(Integer value) {
            this.da501 = value;
        }

        /**
         * Recupera il valore della proprietà da502.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA502() {
            return da502;
        }

        /**
         * Imposta il valore della proprietà da502.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA502(Integer value) {
            this.da502 = value;
        }

        /**
         * Recupera il valore della proprietà da505.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA505() {
            return da505;
        }

        /**
         * Imposta il valore della proprietà da505.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA505(Integer value) {
            this.da505 = value;
        }

        /**
         * Recupera il valore della proprietà da506.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA506() {
            return da506;
        }

        /**
         * Imposta il valore della proprietà da506.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA506(Integer value) {
            this.da506 = value;
        }

        /**
         * Recupera il valore della proprietà da602.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA602() {
            return da602;
        }

        /**
         * Imposta il valore della proprietà da602.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA602(Integer value) {
            this.da602 = value;
        }

        /**
         * Recupera il valore della proprietà da901.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getDA901() {
            return da901;
        }

        /**
         * Imposta il valore della proprietà da901.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setDA901(Integer value) {
            this.da901 = value;
        }

        /**
         * Recupera il valore della proprietà va103.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA103() {
            return va103;
        }

        /**
         * Imposta il valore della proprietà va103.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA103(Integer value) {
            this.va103 = value;
        }

        /**
         * Recupera il valore della proprietà va104.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA104() {
            return va104;
        }

        /**
         * Imposta il valore della proprietà va104.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA104(Integer value) {
            this.va104 = value;
        }

        /**
         * Recupera il valore della proprietà va107.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA107() {
            return va107;
        }

        /**
         * Imposta il valore della proprietà va107.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA107(Integer value) {
            this.va107 = value;
        }

        /**
         * Recupera il valore della proprietà va108.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA108() {
            return va108;
        }

        /**
         * Imposta il valore della proprietà va108.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA108(Integer value) {
            this.va108 = value;
        }

        /**
         * Recupera il valore della proprietà va111.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA111() {
            return va111;
        }

        /**
         * Imposta il valore della proprietà va111.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA111(Integer value) {
            this.va111 = value;
        }

        /**
         * Recupera il valore della proprietà va112.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA112() {
            return va112;
        }

        /**
         * Imposta il valore della proprietà va112.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA112(Integer value) {
            this.va112 = value;
        }

        /**
         * Recupera il valore della proprietà va203.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA203() {
            return va203;
        }

        /**
         * Imposta il valore della proprietà va203.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA203(Integer value) {
            this.va203 = value;
        }

        /**
         * Recupera il valore della proprietà va204.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA204() {
            return va204;
        }

        /**
         * Imposta il valore della proprietà va204.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA204(Integer value) {
            this.va204 = value;
        }

        /**
         * Recupera il valore della proprietà va206.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA206() {
            return va206;
        }

        /**
         * Imposta il valore della proprietà va206.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA206(Integer value) {
            this.va206 = value;
        }

        /**
         * Recupera il valore della proprietà va303.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA303() {
            return va303;
        }

        /**
         * Imposta il valore della proprietà va303.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA303(Integer value) {
            this.va303 = value;
        }

        /**
         * Recupera il valore della proprietà va304.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getVA304() {
            return va304;
        }

        /**
         * Imposta il valore della proprietà va304.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setVA304(Integer value) {
            this.va304 = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="LA101" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="LA102" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="LA103" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="LA104" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "progressivo",
        "la101",
        "la102",
        "la103",
        "la104"
    })
    public static class RilevazioneFascieEva {

        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "LA101")
        protected int la101;
        @XmlElement(name = "LA102")
        protected int la102;
        @XmlElement(name = "LA103")
        protected int la103;
        @XmlElement(name = "LA104")
        protected int la104;

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà la101.
         * 
         */
        public int getLA101() {
            return la101;
        }

        /**
         * Imposta il valore della proprietà la101.
         * 
         */
        public void setLA101(int value) {
            this.la101 = value;
        }

        /**
         * Recupera il valore della proprietà la102.
         * 
         */
        public int getLA102() {
            return la102;
        }

        /**
         * Imposta il valore della proprietà la102.
         * 
         */
        public void setLA102(int value) {
            this.la102 = value;
        }

        /**
         * Recupera il valore della proprietà la103.
         * 
         */
        public int getLA103() {
            return la103;
        }

        /**
         * Imposta il valore della proprietà la103.
         * 
         */
        public void setLA103(int value) {
            this.la103 = value;
        }

        /**
         * Recupera il valore della proprietà la104.
         * 
         */
        public int getLA104() {
            return la104;
        }

        /**
         * Imposta il valore della proprietà la104.
         * 
         */
        public void setLA104(int value) {
            this.la104 = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Matricola"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Data_rilevazione" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="Data_prelievo" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="Ora_prelievo"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Data_prelievo_precedente" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="Ora_prelievo_precedente" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Venduto_chiave_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Venduto_chiave_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Venduto_moneta_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Venduto_moneta_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Incassato_ricarica_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Incassato_ricarica_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Ricaricato_da_chiave_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Ricaricato_da_chiave_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Incassato_moneta" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Incassato_banconote" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Overpay_parziale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Overpay_totale" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Monete_nei_tubi" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "matricola",
        "installazione",
        "dataRilevazione",
        "dataPrelievo",
        "oraPrelievo",
        "dataPrelievoPrecedente",
        "oraPrelievoPrecedente",
        "vendutoChiaveParziale",
        "vendutoChiaveTotale",
        "vendutoMonetaParziale",
        "vendutoMonetaTotale",
        "incassatoRicaricaParziale",
        "incassatoRicaricaTotale",
        "ricaricatoDaChiaveParziale",
        "ricaricatoDaChiaveTotale",
        "incassatoMoneta",
        "incassatoBanconote",
        "overpayParziale",
        "overpayTotale",
        "moneteNeiTubi"
    })
    public static class Rilevazioni {

        @XmlElement(name = "Matricola", required = true)
        protected String matricola;
        @XmlElement(name = "Installazione")
        protected int installazione;
        @XmlElement(name = "Data_rilevazione", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataRilevazione;
        @XmlElement(name = "Data_prelievo", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataPrelievo;
        @XmlElement(name = "Ora_prelievo", required = true)
        protected String oraPrelievo;
        @XmlElement(name = "Data_prelievo_precedente")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataPrelievoPrecedente;
        @XmlElement(name = "Ora_prelievo_precedente")
        protected String oraPrelievoPrecedente;
        @XmlElement(name = "Venduto_chiave_parziale", required = true)
        protected BigDecimal vendutoChiaveParziale;
        @XmlElement(name = "Venduto_chiave_totale", required = true)
        protected BigDecimal vendutoChiaveTotale;
        @XmlElement(name = "Venduto_moneta_parziale", required = true)
        protected BigDecimal vendutoMonetaParziale;
        @XmlElement(name = "Venduto_moneta_totale", required = true)
        protected BigDecimal vendutoMonetaTotale;
        @XmlElement(name = "Incassato_ricarica_parziale", required = true)
        protected BigDecimal incassatoRicaricaParziale;
        @XmlElement(name = "Incassato_ricarica_totale", required = true)
        protected BigDecimal incassatoRicaricaTotale;
        @XmlElement(name = "Ricaricato_da_chiave_parziale", required = true)
        protected BigDecimal ricaricatoDaChiaveParziale;
        @XmlElement(name = "Ricaricato_da_chiave_totale", required = true)
        protected BigDecimal ricaricatoDaChiaveTotale;
        @XmlElement(name = "Incassato_moneta", required = true)
        protected BigDecimal incassatoMoneta;
        @XmlElement(name = "Incassato_banconote", required = true)
        protected BigDecimal incassatoBanconote;
        @XmlElement(name = "Overpay_parziale", required = true)
        protected BigDecimal overpayParziale;
        @XmlElement(name = "Overpay_totale", required = true)
        protected BigDecimal overpayTotale;
        @XmlElement(name = "Monete_nei_tubi", required = true)
        protected BigDecimal moneteNeiTubi;

        /**
         * Recupera il valore della proprietà matricola.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMatricola() {
            return matricola;
        }

        /**
         * Imposta il valore della proprietà matricola.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMatricola(String value) {
            this.matricola = value;
        }

        /**
         * Recupera il valore della proprietà installazione.
         * 
         */
        public int getInstallazione() {
            return installazione;
        }

        /**
         * Imposta il valore della proprietà installazione.
         * 
         */
        public void setInstallazione(int value) {
            this.installazione = value;
        }

        /**
         * Recupera il valore della proprietà dataRilevazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataRilevazione() {
            return dataRilevazione;
        }

        /**
         * Imposta il valore della proprietà dataRilevazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataRilevazione(XMLGregorianCalendar value) {
            this.dataRilevazione = value;
        }

        /**
         * Recupera il valore della proprietà dataPrelievo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataPrelievo() {
            return dataPrelievo;
        }

        /**
         * Imposta il valore della proprietà dataPrelievo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataPrelievo(XMLGregorianCalendar value) {
            this.dataPrelievo = value;
        }

        /**
         * Recupera il valore della proprietà oraPrelievo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraPrelievo() {
            return oraPrelievo;
        }

        /**
         * Imposta il valore della proprietà oraPrelievo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraPrelievo(String value) {
            this.oraPrelievo = value;
        }

        /**
         * Recupera il valore della proprietà dataPrelievoPrecedente.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataPrelievoPrecedente() {
            return dataPrelievoPrecedente;
        }

        /**
         * Imposta il valore della proprietà dataPrelievoPrecedente.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataPrelievoPrecedente(XMLGregorianCalendar value) {
            this.dataPrelievoPrecedente = value;
        }

        /**
         * Recupera il valore della proprietà oraPrelievoPrecedente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraPrelievoPrecedente() {
            return oraPrelievoPrecedente;
        }

        /**
         * Imposta il valore della proprietà oraPrelievoPrecedente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraPrelievoPrecedente(String value) {
            this.oraPrelievoPrecedente = value;
        }

        /**
         * Recupera il valore della proprietà vendutoChiaveParziale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVendutoChiaveParziale() {
            return vendutoChiaveParziale;
        }

        /**
         * Imposta il valore della proprietà vendutoChiaveParziale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVendutoChiaveParziale(BigDecimal value) {
            this.vendutoChiaveParziale = value;
        }

        /**
         * Recupera il valore della proprietà vendutoChiaveTotale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVendutoChiaveTotale() {
            return vendutoChiaveTotale;
        }

        /**
         * Imposta il valore della proprietà vendutoChiaveTotale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVendutoChiaveTotale(BigDecimal value) {
            this.vendutoChiaveTotale = value;
        }

        /**
         * Recupera il valore della proprietà vendutoMonetaParziale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVendutoMonetaParziale() {
            return vendutoMonetaParziale;
        }

        /**
         * Imposta il valore della proprietà vendutoMonetaParziale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVendutoMonetaParziale(BigDecimal value) {
            this.vendutoMonetaParziale = value;
        }

        /**
         * Recupera il valore della proprietà vendutoMonetaTotale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVendutoMonetaTotale() {
            return vendutoMonetaTotale;
        }

        /**
         * Imposta il valore della proprietà vendutoMonetaTotale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVendutoMonetaTotale(BigDecimal value) {
            this.vendutoMonetaTotale = value;
        }

        /**
         * Recupera il valore della proprietà incassatoRicaricaParziale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIncassatoRicaricaParziale() {
            return incassatoRicaricaParziale;
        }

        /**
         * Imposta il valore della proprietà incassatoRicaricaParziale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIncassatoRicaricaParziale(BigDecimal value) {
            this.incassatoRicaricaParziale = value;
        }

        /**
         * Recupera il valore della proprietà incassatoRicaricaTotale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIncassatoRicaricaTotale() {
            return incassatoRicaricaTotale;
        }

        /**
         * Imposta il valore della proprietà incassatoRicaricaTotale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIncassatoRicaricaTotale(BigDecimal value) {
            this.incassatoRicaricaTotale = value;
        }

        /**
         * Recupera il valore della proprietà ricaricatoDaChiaveParziale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getRicaricatoDaChiaveParziale() {
            return ricaricatoDaChiaveParziale;
        }

        /**
         * Imposta il valore della proprietà ricaricatoDaChiaveParziale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setRicaricatoDaChiaveParziale(BigDecimal value) {
            this.ricaricatoDaChiaveParziale = value;
        }

        /**
         * Recupera il valore della proprietà ricaricatoDaChiaveTotale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getRicaricatoDaChiaveTotale() {
            return ricaricatoDaChiaveTotale;
        }

        /**
         * Imposta il valore della proprietà ricaricatoDaChiaveTotale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setRicaricatoDaChiaveTotale(BigDecimal value) {
            this.ricaricatoDaChiaveTotale = value;
        }

        /**
         * Recupera il valore della proprietà incassatoMoneta.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIncassatoMoneta() {
            return incassatoMoneta;
        }

        /**
         * Imposta il valore della proprietà incassatoMoneta.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIncassatoMoneta(BigDecimal value) {
            this.incassatoMoneta = value;
        }

        /**
         * Recupera il valore della proprietà incassatoBanconote.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIncassatoBanconote() {
            return incassatoBanconote;
        }

        /**
         * Imposta il valore della proprietà incassatoBanconote.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIncassatoBanconote(BigDecimal value) {
            this.incassatoBanconote = value;
        }

        /**
         * Recupera il valore della proprietà overpayParziale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOverpayParziale() {
            return overpayParziale;
        }

        /**
         * Imposta il valore della proprietà overpayParziale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOverpayParziale(BigDecimal value) {
            this.overpayParziale = value;
        }

        /**
         * Recupera il valore della proprietà overpayTotale.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getOverpayTotale() {
            return overpayTotale;
        }

        /**
         * Imposta il valore della proprietà overpayTotale.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setOverpayTotale(BigDecimal value) {
            this.overpayTotale = value;
        }

        /**
         * Recupera il valore della proprietà moneteNeiTubi.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMoneteNeiTubi() {
            return moneteNeiTubi;
        }

        /**
         * Imposta il valore della proprietà moneteNeiTubi.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMoneteNeiTubi(BigDecimal value) {
            this.moneteNeiTubi = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Matricola"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Installazione" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Data_prelievo" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="Ora_prelievo"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Riga" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
     *         &lt;element name="Valore_chiave" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Valore_moneta" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Vendite_chiave" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Vendite_moneta" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="Vendite_service" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "matricola",
        "installazione",
        "dataPrelievo",
        "oraPrelievo",
        "riga",
        "valoreChiave",
        "valoreMoneta",
        "venditeChiave",
        "venditeMoneta",
        "venditeService"
    })
    public static class RilevazioniSelezioni {

        @XmlElement(name = "Matricola", required = true)
        protected String matricola;
        @XmlElement(name = "Installazione")
        protected int installazione;
        @XmlElement(name = "Data_prelievo", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataPrelievo;
        @XmlElement(name = "Ora_prelievo", required = true)
        protected String oraPrelievo;
        @XmlElement(name = "Riga")
        protected short riga;
        @XmlElement(name = "Valore_chiave", required = true)
        protected BigDecimal valoreChiave;
        @XmlElement(name = "Valore_moneta", required = true)
        protected BigDecimal valoreMoneta;
        @XmlElement(name = "Vendite_chiave", required = true)
        protected BigDecimal venditeChiave;
        @XmlElement(name = "Vendite_moneta", required = true)
        protected BigDecimal venditeMoneta;
        @XmlElement(name = "Vendite_service", required = true)
        protected BigDecimal venditeService;

        /**
         * Recupera il valore della proprietà matricola.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMatricola() {
            return matricola;
        }

        /**
         * Imposta il valore della proprietà matricola.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMatricola(String value) {
            this.matricola = value;
        }

        /**
         * Recupera il valore della proprietà installazione.
         * 
         */
        public int getInstallazione() {
            return installazione;
        }

        /**
         * Imposta il valore della proprietà installazione.
         * 
         */
        public void setInstallazione(int value) {
            this.installazione = value;
        }

        /**
         * Recupera il valore della proprietà dataPrelievo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataPrelievo() {
            return dataPrelievo;
        }

        /**
         * Imposta il valore della proprietà dataPrelievo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataPrelievo(XMLGregorianCalendar value) {
            this.dataPrelievo = value;
        }

        /**
         * Recupera il valore della proprietà oraPrelievo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraPrelievo() {
            return oraPrelievo;
        }

        /**
         * Imposta il valore della proprietà oraPrelievo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraPrelievo(String value) {
            this.oraPrelievo = value;
        }

        /**
         * Recupera il valore della proprietà riga.
         * 
         */
        public short getRiga() {
            return riga;
        }

        /**
         * Imposta il valore della proprietà riga.
         * 
         */
        public void setRiga(short value) {
            this.riga = value;
        }

        /**
         * Recupera il valore della proprietà valoreChiave.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getValoreChiave() {
            return valoreChiave;
        }

        /**
         * Imposta il valore della proprietà valoreChiave.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setValoreChiave(BigDecimal value) {
            this.valoreChiave = value;
        }

        /**
         * Recupera il valore della proprietà valoreMoneta.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getValoreMoneta() {
            return valoreMoneta;
        }

        /**
         * Imposta il valore della proprietà valoreMoneta.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setValoreMoneta(BigDecimal value) {
            this.valoreMoneta = value;
        }

        /**
         * Recupera il valore della proprietà venditeChiave.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVenditeChiave() {
            return venditeChiave;
        }

        /**
         * Imposta il valore della proprietà venditeChiave.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVenditeChiave(BigDecimal value) {
            this.venditeChiave = value;
        }

        /**
         * Recupera il valore della proprietà venditeMoneta.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVenditeMoneta() {
            return venditeMoneta;
        }

        /**
         * Imposta il valore della proprietà venditeMoneta.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVenditeMoneta(BigDecimal value) {
            this.venditeMoneta = value;
        }

        /**
         * Recupera il valore della proprietà venditeService.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getVenditeService() {
            return venditeService;
        }

        /**
         * Imposta il valore della proprietà venditeService.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setVenditeService(BigDecimal value) {
            this.venditeService = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Chiave"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="20"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Progressivo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="Data_modifica" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="ValoreIntero" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="ValoreReale" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
     *         &lt;element name="ValoreBooleano" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *         &lt;element name="ValoreStringa" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="255"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ValoreData" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "chiave",
        "progressivo",
        "dataModifica",
        "valoreIntero",
        "valoreReale",
        "valoreBooleano",
        "valoreStringa",
        "valoreData"
    })
    public static class TabellaGenerica {

        @XmlElement(name = "Chiave", required = true)
        protected String chiave;
        @XmlElement(name = "Progressivo")
        protected int progressivo;
        @XmlElement(name = "Data_modifica", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataModifica;
        @XmlElement(name = "ValoreIntero")
        protected Integer valoreIntero;
        @XmlElement(name = "ValoreReale")
        protected Float valoreReale;
        @XmlElement(name = "ValoreBooleano")
        protected Boolean valoreBooleano;
        @XmlElement(name = "ValoreStringa")
        protected String valoreStringa;
        @XmlElement(name = "ValoreData")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar valoreData;

        /**
         * Recupera il valore della proprietà chiave.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getChiave() {
            return chiave;
        }

        /**
         * Imposta il valore della proprietà chiave.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setChiave(String value) {
            this.chiave = value;
        }

        /**
         * Recupera il valore della proprietà progressivo.
         * 
         */
        public int getProgressivo() {
            return progressivo;
        }

        /**
         * Imposta il valore della proprietà progressivo.
         * 
         */
        public void setProgressivo(int value) {
            this.progressivo = value;
        }

        /**
         * Recupera il valore della proprietà dataModifica.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDataModifica() {
            return dataModifica;
        }

        /**
         * Imposta il valore della proprietà dataModifica.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDataModifica(XMLGregorianCalendar value) {
            this.dataModifica = value;
        }

        /**
         * Recupera il valore della proprietà valoreIntero.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getValoreIntero() {
            return valoreIntero;
        }

        /**
         * Imposta il valore della proprietà valoreIntero.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setValoreIntero(Integer value) {
            this.valoreIntero = value;
        }

        /**
         * Recupera il valore della proprietà valoreReale.
         * 
         * @return
         *     possible object is
         *     {@link Float }
         *     
         */
        public Float getValoreReale() {
            return valoreReale;
        }

        /**
         * Imposta il valore della proprietà valoreReale.
         * 
         * @param value
         *     allowed object is
         *     {@link Float }
         *     
         */
        public void setValoreReale(Float value) {
            this.valoreReale = value;
        }

        /**
         * Recupera il valore della proprietà valoreBooleano.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isValoreBooleano() {
            return valoreBooleano;
        }

        /**
         * Imposta il valore della proprietà valoreBooleano.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setValoreBooleano(Boolean value) {
            this.valoreBooleano = value;
        }

        /**
         * Recupera il valore della proprietà valoreStringa.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValoreStringa() {
            return valoreStringa;
        }

        /**
         * Imposta il valore della proprietà valoreStringa.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValoreStringa(String value) {
            this.valoreStringa = value;
        }

        /**
         * Recupera il valore della proprietà valoreData.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getValoreData() {
            return valoreData;
        }

        /**
         * Imposta il valore della proprietà valoreData.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setValoreData(XMLGregorianCalendar value) {
            this.valoreData = value;
        }

    }

}
