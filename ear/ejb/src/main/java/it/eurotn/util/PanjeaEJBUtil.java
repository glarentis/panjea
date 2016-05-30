package it.eurotn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.joda.time.DateTime;

import it.eurotn.entity.EntityBase;

public final class PanjeaEJBUtil {

    /**
     * Costruttore.
     *
     */
    private PanjeaEJBUtil() {
        super();
    }

    /**
     *
     * @param value
     *            valore .
     * @return vlaue con gli apici
     */
    public static String addQuote(String value) {
        return new StringBuilder("'").append(value).append("'").toString();
    }

    /**
     * Trova la differenza in giorni tra le 2 date.
     *
     * @param data1
     *            prima data
     * @param date2
     *            sedonda data
     * @return differenza
     */
    public static int calculateDifference(Date data1, Date date2) {
        int tempDifference = 0;
        int difference = 0;
        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();

        if (data1.compareTo(date2) < 0) {
            earlier.setTime(data1);
            later.setTime(date2);
        } else {
            earlier.setTime(date2);
            later.setTime(data1);
        }

        while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
            tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
            difference += tempDifference;

            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
            tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
            difference += tempDifference;

            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }
        if (data1.compareTo(date2) > 0) {
            difference = -difference;
        }
        return difference;
    }

    /**
     * @param sourceObject
     *            l'oggetto da clonare
     * @param <T>
     *            tipo della classe
     * @return l'oggetto clonato
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneObject(T sourceObject) {
        T clonedObject = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            // serialize and pass the object
            oos.writeObject(sourceObject);
            oos.flush();
            ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);
            // return the new object
            clonedObject = (T) ois.readObject();
            return clonedObject;
        } catch (Exception e) {
            clonedObject = null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }

        return clonedObject;
    }

    /**
     * Trasforma un set in una mappa tramite {@link KeyFromValueProvider}.
     *
     * @param <T>
     *            generics
     * @param <S>
     *            generics
     * @param coll
     *            collection da trasformare
     * @param provider
     *            recupera dall'elemento la chiave della mappa
     * @return mappa con chiave il valore generato da provider e il valore nella collection
     */
    public static <T, S> Map<S, T> collectionToMap(Collection<T> coll, KeyFromValueProvider<T, S> provider) {
        Map<S, T> retval = new HashMap<>();
        for (T elem : coll) {
            retval.put(provider.keyFromValue(elem), elem);
        }
        return retval;
    }

    /**
     *
     * @param destinazione
     *            bean di destinazione
     * @param origine
     *            bean di origine
     */
    public static void copyProperties(Object destinazione, Object origine) {
        BeanUtilsBean.getInstance().getConvertUtils().register(new BigDecimalConverter(null), BigDecimal.class);
        BeanUtilsBean.getInstance().getConvertUtils().register(new DateConverter(null), Date.class);
        try {
            BeanUtils.copyProperties(destinazione, origine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce un array di 7 elementi contenenti le date della settimana relativa alla data
     * passata come parametro.
     *
     * @param data
     *            data di riferimento
     * @return date della settimana
     */
    public static Date[] getDateOfWeek(Date data) {

        Date[] result = new Date[7];

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        for (int i = 0; i < 7; i++) {
            result[i] = cal.getTime();
            cal.add(Calendar.DATE, 1);
        }

        return result;
    }

    /**
     * Setta alla data passata come parametro ore, minuti, secondi e millisecondi a zero e la
     * restituisce.
     *
     * @param date
     *            data di riferimento
     * @return data generata
     */
    public static Date getDateTimeToZero(Date date) {
        if (date != null) {
            Calendar calendarDataDataOrigine = Calendar.getInstance();
            calendarDataDataOrigine.setTime(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
            calendar.set(Calendar.DAY_OF_MONTH, calendarDataDataOrigine.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.MONTH, calendarDataDataOrigine.get(Calendar.MONTH));
            calendar.set(Calendar.YEAR, calendarDataDataOrigine.get(Calendar.YEAR));
            calendar.set(Calendar.HOUR_OF_DAY, 0);

            return calendar.getTime();
        } else {
            return null;
        }
    }

    /**
     *
     * @param proxy
     *            proxy hibernate
     * @param <T>
     *            classe base
     * @return classe che implementa T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getImplementationClass(HibernateProxy proxy) {
        if (proxy == null) {
            return null;
        }
        return (T) proxy.getHibernateLazyInitializer().getImplementation();
    }

    /**
     * Se l'oggetto è un oggetto di persistenza lazy restituisce l'id senza inizializzarlo.
     *
     * @param lazyObject
     *            oggetto proxy
     * @return id
     */
    public static Integer getLazyId(EntityBase lazyObject) {
        if (lazyObject instanceof HibernateProxy) {
            LazyInitializer lazyInitializer = ((HibernateProxy) lazyObject).getHibernateLazyInitializer();
            if (lazyInitializer.isUninitialized()) {
                return (Integer) lazyInitializer.getIdentifier();
            }
        }
        if (lazyObject == null) {
            return null;
        }
        return lazyObject.getId();
    }

    /**
     *
     * @return restituisce il max address della scheda di rete
     */
    public static String getMacAddress() {

        StringBuilder sb = new StringBuilder();
        try {
            // recupero tutte le interfacce di rete
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();

            // le aggiungo in una tree map in questo modo saranno sempre ordinate in base al loro
            // nome
            Map<String, NetworkInterface> networksMap = new TreeMap<String, NetworkInterface>();
            while (networks.hasMoreElements()) {
                NetworkInterface networInterface = networks.nextElement();
                networksMap.put(networInterface.getName(), networInterface);
            }

            // recupero il mac address della prima scheda di rete che lo ha
            for (Entry<String, NetworkInterface> entry : networksMap.entrySet()) {
                byte[] mac = entry.getValue().getHardwareAddress();
                if (mac != null) {
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("errore nel recuperare il mac address della scheda");
        }
        return sb.toString();
    }

    /**
     * Restituisce la data finale del trimestre della data passata come parametro.
     *
     * @param curDate
     *            data corrente
     * @return data finale del trimestre
     */
    public static DateTime getQuarterEnddate(DateTime curDate) {
        int ttr = (curDate.monthOfYear().get() - 1) / 3 + 1;
        DateTime date = new DateTime(curDate.year().get(), (ttr * 3), 1, 0, 0);
        return date.dayOfMonth().withMaximumValue();
    }

    /**
     * Restituisce la data iniziale del trimestre della data passata come parametro.
     *
     * @param curDate
     *            data corrente
     * @return data iniziale del trimestre
     */
    public static DateTime getQuarterStartDate(DateTime curDate) {
        return getQuarterEnddate(curDate).plusDays(1).plusMonths(-3);
    }

    /**
     *
     * @return data di oggi all'ora 00:00
     */
    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Trasforma un set in una mappa tramite {@link KeyFromValueProvider}.
     *
     * @param <T>
     *            generics
     * @param <S>
     *            generics
     * @param coll
     *            collection da trasformare
     * @param provider
     *            recupera dall'elemento la chiave della mappa
     * @return mappa con chiave il valore generato da provider e il valore nella collection
     */
    public static <S, T> Map<S, List<T>> listToMap(Collection<T> coll, KeyFromValueProvider<T, S> provider) {
        Map<S, List<T>> retval = new HashMap<S, List<T>>();
        for (T elem : coll) {
            S key = provider.keyFromValue(elem);
            if (!retval.containsKey(key)) {
                retval.put(key, new ArrayList<T>());
            }
            retval.get(key).add(elem);
        }
        return retval;
    }

    /**
     * Rimuove dal testo tutti i tag html di formattazione (i,b,u,font) e sostituisce i div con dei
     * br ed elimina i tag /div.
     *
     * @param htmlText
     *            il testo con i tag html da eliminare
     * @return testo senza tag html
     */
    public static String removeHtml(String htmlText) {
        String noHtml = htmlText.replace("<div>", "<br>");
        noHtml = noHtml.replaceAll("(?i)</?(font*|b|u|i|/div)\\b[^>]*>", "");
        return noHtml;
    }

    /**
     * Arrotonda il parametro a 2 cifre decimali.
     *
     * @param num
     *            numero da arrotondare
     * @return risultato
     */
    public static Double roundPercentuale(Double num) {
        return roundToDecimals(num, 2);
    }

    /**
     * arrrotonda un double.
     *
     * @param date
     *            cifra da arrotondare
     * @param numberOfDecimal
     *            decimali
     * @return double arrotondato.
     */
    public static Double roundToDecimals(Double date, int numberOfDecimal) {
        Double result = null;
        if (date != null) {
            result = Math.round(date * Math.pow(10, numberOfDecimal)) / Math.pow(10, numberOfDecimal);
        }
        return result;
    }
}
