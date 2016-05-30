package it.eurotn.panjea.rich.editors.webbrowser;

import java.net.MalformedURLException;
import java.net.URL;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebEngine;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

public abstract class PanjeaUrl implements ChangeListener<Number> {
    private String userName = "";
    private String password = "";
    private String userNameControl = "";
    private String passwordControl = "";
    private String formName = "";
    private URL url;
    protected WebEngine engine;

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
        if (newValue.intValue() == 100) {
            Document doc = engine.getDocument();
            if (doc != null) {
                System.err.println("value: " + newValue + " - " + observableValue);
                if (!getFormName().isEmpty()) {
                    NodeList tags = doc.getElementsByTagName("form");
                    if (tags.getLength() > 0) {
                        HTMLFormElement form = (HTMLFormElement) tags.item(0);
                        if (getFormName().equals(form.getName()) || getFormName().equals(form.getId())) {
                            tags = doc.getElementsByTagName("input");
                            for (int i = 0; i < tags.getLength() - 1; i++) {
                                HTMLInputElement input = (HTMLInputElement) tags.item(i);
                                String name = input.getName();
                                if (getPasswordControl().equals(name)) {
                                    input.setValue(getPassword());
                                }
                                if (getUserNameControl().equals(name)) {
                                    input.setValue(getUserName());
                                }
                            }
                            form.submit();
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @return nome da visualizzare nel'editor.
     */
    public abstract String getDisplayName();

    /**
     * @return Returns the formName.
     */
    public String getFormName() {
        return formName;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return Returns the passwordControl.
     */
    public String getPasswordControl() {
        return passwordControl;
    }

    /**
     * @return Returns the url.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        if (userName == null) {
            return "";
        }
        return userName;
    }

    /**
     * @return Returns the userNameControl.
     */
    public String getUserNameControl() {
        return userNameControl;
    }

    /**
     * @param engine
     *            The engine to set.
     */
    public void setEngine(WebEngine engine) {
        this.engine = engine;
    }

    /**
     * @param formName
     *            The formName to set.
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    /**
     *
     * @param indirizzo
     *            indirizzo
     */
    public void setIndirizzo(String indirizzo) {
        try {
            url = new URL(indirizzo);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param password
     *            The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param passwordControl
     *            The passwordControl to set.
     */
    public void setPasswordControl(String passwordControl) {
        this.passwordControl = passwordControl;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param userNameControl
     *            The userNameControl to set.
     */
    public void setUserNameControl(String userNameControl) {
        this.userNameControl = userNameControl;
    }
}
