/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 */
package it.eurotn.rich.report.jasperserver;

/**
 * 
 * @author gtoffoli
 */
public class JServer {

    private String name;
    private String url;
    private String username;
    private String password;

    private WSClient wSClient = null;

    private boolean loaded = false;
    private boolean loading = false;

    private String locale = null;

    /** Creates a new instance of JServer */
    public JServer() {
    }

    public String getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public WSClient getWSClient() throws Exception {
        if (wSClient == null) {
            setWSClient(new WSClient(this));
        }
        return wSClient;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setName(String name) {
        this.name = name;
        setWSClient(null);
    }

    public void setPassword(String password) {
        this.password = password;
        setWSClient(null);
    }

    public void setUrl(String url) {
        this.url = url;
        setWSClient(null);
    }

    public void setUsername(String username) {
        this.username = username;
        setWSClient(null);
    }

    public void setWSClient(WSClient wsClient) {
        this.wSClient = wsClient;
    }

    @Override
    public String toString() {
        return "" + getName();
    }

}
