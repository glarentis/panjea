package it.eurotn.panjea.rich.statusBarItem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jidesoft.rss.FeedReader;
import com.jidesoft.rss.PreferencePanel;

import de.nava.informa.core.ChannelGroupIF;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.utils.ItemComparator;
import de.nava.informa.utils.manager.PersistenceManagerException;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author fattazzo
 *
 */
public class PanjeaFeedReader extends FeedReader {

    private static final Logger LOGGER = Logger.getLogger(PanjeaFeedReader.class);

    private static final long serialVersionUID = -2952347052169534714L;
    private static String feedFolderName = "panjeaRssFeeds";
    private static String rssArticoliPanjea = "http://www.eurotn.it/wiki/tiki-articles_rss.php?ver=2";

    /**
     * Costruttore.
     */
    public PanjeaFeedReader() {
        super(PanjeaSwingUtil.getHome().resolve(feedFolderName).toString(), new String[] { rssArticoliPanjea },
                rssArticoliPanjea);
    }

    @Override
    protected void displayHtmlBrowser(String content, ItemIF item) {

        String contentParsed = content;

        // siccome i link dei file del wiki aggiungono una immagine, la tolgo altrimenti nell'editor non verrebbe
        // caricata ma visualizzata come un link non corretto
        if (Objects.equals(rssArticoliPanjea, item.getChannel().getLocation().toString())) {
            int imgPos = StringUtils.indexOf(contentParsed, "<img");
            if (imgPos != -1) {
                int imgEnd = StringUtils.indexOf(contentParsed, "/>", imgPos);
                String imgTag = StringUtils.substring(contentParsed, imgPos, imgEnd + 2);
                contentParsed = StringUtils.replace(contentParsed, imgTag, "");
                contentParsed = StringUtils.replace(contentParsed, "class=\"wiki external\"", "");
            }
        }
        contentParsed += "<p><a href=\"" + item.getLink().toExternalForm() + "\">Leggi tutto l'articolo</a>";
        super.displayHtmlBrowser(contentParsed, item);
    }

    /**
     * @return tutti i canali configurati
     */
    private Collection<ChannelIF> getAllChannels() {
        // copiato di sana pianta della classe FeedReader per poter avere la lista di tutti i channel
        boolean bool = PreferencePanel.h;
        Set<ChannelIF> localHashSet = new HashSet<ChannelIF>();
        try {
            ChannelGroupIF[] arrayOfChannelGroupIF1 = getFeedPersistenceManager().getGroups();
            if ((bool) || (arrayOfChannelGroupIF1 != null)) {
                ChannelGroupIF[] arrayOfChannelGroupIF2 = arrayOfChannelGroupIF1;
                int i1 = arrayOfChannelGroupIF2.length;
                int i2 = 0;
                do {
                    if (i2 >= i1) {
                        break;
                    }
                    ChannelGroupIF localChannelGroupIF = arrayOfChannelGroupIF2[i2];
                    Collection<ChannelIF> localCollection = localChannelGroupIF.getAll();
                    if (!(bool)) {
                        if (localCollection != null) {
                            localHashSet.addAll(localCollection);
                        }
                        ++i2;
                    }
                } while (!(bool));
            }
        } catch (PersistenceManagerException localPersistenceManagerException) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Errore durante il caricamento dei canali rss");
            }
        }
        return localHashSet;
    }

    /**
     * @return elenco delle news presenti in formato html.
     */
    public String getNewsDescription() {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("<html>");
        Collection<ChannelIF> allChannels = getAllChannels();
        for (ChannelIF channelIF : allChannels) {
            sb.append("<b>");
            sb.append(channelIF.getTitle());
            sb.append("</b><br><ul>");

            for (ItemIF item : getUnreadNews(channelIF)) {
                sb.append("<li><b>");
                sb.append(item.getTitle());
                sb.append("</b></li>");
            }
            sb.append("</ul>");
        }
        sb.append("</html>");

        return sb.toString();
    }

    /**
     * @return descrizione del numero di news ancora se leggere per tutti i canali configurati
     */
    public String getUnReadItemsCountDescription() {
        int unreadFeeds = getUnReadItemCountForAllChannels();
        return String.format("%d nuovi ", unreadFeeds);
    }

    /**
     * Restituisce tutte le news non lette del channel ordinate in base alla data.
     *
     * @param channelIF
     *            canale
     * @return news
     */
    private ItemIF[] getUnreadNews(ChannelIF channelIF) {

        Set<ItemIF> items = channelIF.getItems();
        Iterator<ItemIF> iterator = items.iterator();
        while (iterator.hasNext()) {
            ItemIF itemIF = iterator.next();
            if (!itemIF.getUnRead()) {
                iterator.remove();
            }
        }

        // convert from List to Array
        ItemIF[] unreadItems = items.toArray(new ItemIF[items.size()]);

        // sort news items (latest entry first)
        java.util.Arrays.sort(unreadItems, new ItemComparator(true));

        return unreadItems;
    }
}
