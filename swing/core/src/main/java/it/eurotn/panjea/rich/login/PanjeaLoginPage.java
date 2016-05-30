package it.eurotn.panjea.rich.login;

import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.security.ApplicationSecurityManager;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.security.Authentication;
import org.springframework.security.SpringSecurityException;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.JecPrincipalSpring;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

public class PanjeaLoginPage {

    private static final Logger LOGGER = Logger.getLogger(PanjeaLoginPage.class);

    private ISicurezzaBD sicurezzaBD;
    private SettingsManager settingsManager;

    private ObservableList<String> aziendeDisponibili = FXCollections.observableArrayList();
    private ObservableList<Locale> lingue = FXCollections.observableArrayList();

    private TextField userNameTextField;
    private PasswordField passwordField;

    private ComboBox<Locale> linguaComboBox;

    private ComboBox<String> aziendaComboBox;

    private Button loginButton;
    private Button cancelButton;

    private Label labelLoginMessage;

    private Reflection reflectionEffect;

    {
        sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        settingsManager = (SettingsManager) ApplicationServicesLocator.services().getService(SettingsManager.class);

        reflectionEffect = new Reflection();
        reflectionEffect.setTopOffset(7);
    }

    /**
     * Costruttore.
     */
    public PanjeaLoginPage() {
        super();
        init();
    }

    /**
     * Aggiunge tutti i controlli nel {@link GridPane}.
     *
     * @param gridPane
     *            pane al quale aggiungere i controllo
     */
    private void addControls(GridPane gridPane) {
        Label lblUserName = new Label("Username");
        gridPane.add(lblUserName, 0, 1);
        gridPane.add(getUserNameTextField(), 1, 1);

        Label lblPassword = new Label("Password");
        gridPane.add(lblPassword, 0, 2);
        gridPane.add(getPasswordField(), 1, 2);

        Label lblAzienda = new Label("Azienda");
        gridPane.add(lblAzienda, 0, 3);
        gridPane.add(getAziendaComboBox(), 1, 3);

        Label lblLingua = new Label("Lingua");
        lblLingua.setEffect(reflectionEffect);
        gridPane.add(lblLingua, 0, 4);
        gridPane.add(getLinguaComboBox(), 1, 4);

        gridPane.add(getLoginButton(), 2, 4);
        gridPane.add(getCancelButton(), 3, 4);
        gridPane.add(getLabelLoginMessage(), 0, 0, 2, 1);

        Image image = null;
        try {
            image = new Image(PanjeaLoginPage.this.getClass()
                    .getResource("/it/eurotn/panjea/resources/images/logo-small.png").openStream());
        } catch (IOException e) {
            LOGGER.error("-->errore nel caricare l'immagine logo-small.png", e);
        }
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(70);
        imageView.setSmooth(true);
        imageView.setCache(true);

        RotateTransition transition = new RotateTransition(Duration.seconds(6), imageView);
        transition.setAxis(Rotate.Z_AXIS);
        transition.setFromAngle(0);
        transition.setToAngle(360);
        transition.setCycleCount(Timeline.INDEFINITE);

        gridPane.add(imageView, 3, 0, 1, 4);

        transition.play();
    }

    /**
     * Crea il {@link GridPane} nel quale verranno visualizzati tutti i controlli.
     *
     * @return gridPane
     */
    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setId("root");
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(column1, column1);

        return gridPane;
    }

    /**
     * Crea la scena.
     *
     * @return {@link Scene}
     */
    public Scene createScene() {

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(0, 0, 0, 0));

        // Adding HBox
        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 20, 20, 30));
        hb.setAlignment(Pos.CENTER);
        bp.setId("bp");

        // Adding GridPane
        GridPane gridPane = createGridPane();
        addControls(gridPane);

        // DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        bp.setCenter(gridPane);

        // Adding BorderPane to the scene and loading CSS
        Scene scene = new Scene(bp);
        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());

        EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    event.consume();
                    getLoginButton().getOnAction().handle(null);
                }
            }
        };
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
        return scene;
    }

    /**
     * Crea il principal per l'autenticazione con i parametri impostati.
     *
     * @return principal
     */
    private Authentication getAuthentication() {
        String descLingua = linguaComboBox.getValue().getLanguage();
        Authentication jecPrincipal = new JecPrincipalSpring(
                getUserNameTextField().getText() + "#" + aziendaComboBox.getValue() + "#" + descLingua);
        ((JecPrincipalSpring) jecPrincipal).setCredentials(getPasswordField().getText());
        return jecPrincipal;
    }

    /**
     * @return the aziendaComboBox
     */
    private ComboBox<String> getAziendaComboBox() {
        if (aziendaComboBox == null) {
            aziendaComboBox = new ComboBox<String>(aziendeDisponibili);
            aziendaComboBox.setMaxWidth(Double.MAX_VALUE);
            try {
                String ultimaAziendaLoggata = settingsManager.getUserSettings().getString("ultimaAziendaLoggata");
                aziendaComboBox.setValue(ultimaAziendaLoggata);
            } catch (SettingsException e1) {
                LOGGER.error("errore ne l recupero dell'ultima azienda loggata", e1);
            }
        }

        return aziendaComboBox;
    }

    /**
     * @return the cancelButton
     */
    private Button getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new Button("Annulla");
            cancelButton.setEffect(reflectionEffect);
            cancelButton.setId("btnCancel");
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent paramT) {
                    org.springframework.richclient.application.Application.instance().close(true, 0);
                }
            });
        }

        return cancelButton;
    }

    /**
     * @return the labelLoginMessage
     */
    private Label getLabelLoginMessage() {
        if (labelLoginMessage == null) {
            labelLoginMessage = new Label();
            labelLoginMessage.setTextFill(Color.RED);
        }

        return labelLoginMessage;
    }

    /**
     * @return the linguaComboBox
     */
    private ComboBox<Locale> getLinguaComboBox() {
        if (linguaComboBox == null) {
            linguaComboBox = new ComboBox<Locale>(lingue);
            linguaComboBox.setEffect(reflectionEffect);
            linguaComboBox.setMaxWidth(Double.MAX_VALUE);
            linguaComboBox.setButtonCell(new LinguaListCell());
            linguaComboBox.setCellFactory(new Callback<ListView<Locale>, ListCell<Locale>>() {
                @Override
                public ListCell<Locale> call(ListView<Locale> element) {
                    return new LinguaListCell();
                }
            });
            linguaComboBox.setValue(Locale.getDefault());
        }

        return linguaComboBox;
    }

    /**
     * @return the loginButton
     */
    private Button getLoginButton() {
        if (loginButton == null) {
            loginButton = new Button("Conferma");
            loginButton.setEffect(reflectionEffect);
            loginButton.setId("btnLogin");
            loginButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    org.springframework.security.Authentication authentication;
                    ApplicationSecurityManager sm;
                    authentication = getAuthentication();
                    sm = (ApplicationSecurityManager) org.springframework.richclient.application.Application.services()
                            .getService(ApplicationSecurityManager.class);
                    try {
                        sm.doLogin(authentication);
                        saveSettings();
                        loginButton.getScene().getWindow().fireEvent(
                                new WindowEvent(loginButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));

                        getLabelLoginMessage().setText("");
                        getUserNameTextField().requestFocus();
                    } catch (SpringSecurityException e) {
                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace("Utente o password errati", e);
                        }
                        getLabelLoginMessage().setText("Utente o password errati");
                        getUserNameTextField().requestFocus();
                    }
                    getPasswordField().setText("");
                }
            });
        }

        return loginButton;
    }

    /**
     * @return the passwordField
     */
    private PasswordField getPasswordField() {
        if (passwordField == null) {
            passwordField = new PasswordField();
            passwordField.setId("textField");
        }

        return passwordField;
    }

    /**
     * @return the userNameTextField
     */
    private TextField getUserNameTextField() {
        if (userNameTextField == null) {
            userNameTextField = new TextField();
            userNameTextField.setId("textField");
            try {
                String ultimoUtenteLoggato = settingsManager.getUserSettings().getString("ultimoUtenteLoggato");
                userNameTextField.setText(ultimoUtenteLoggato);
            } catch (SettingsException e1) {
                LOGGER.error("-->errore nel recupero dell'ultimo utente loggato", e1);
            }
        }

        return userNameTextField;
    }

    /**
     * Inizializza i valori.
     */
    private void init() {

        // inizializza la lista delle aziende deployate
        aziendeDisponibili.clear();
        aziendeDisponibili.addAll(sicurezzaBD.caricaAziendeDeployate());

        // inizializza la lista delle lingue disponibili
        lingue.add(Locale.ITALY);
        lingue.add(Locale.GERMANY);
    }

    /**
     * Salva nei settings locali gli ultimi dati inseriti del login;<br/>
     * il metodo deve essere chiamato una volta autenticato l'utente correttamente.
     *
     */
    private void saveSettings() {
        SettingsManager manager = (SettingsManager) ApplicationServicesLocator.services()
                .getService(SettingsManager.class);
        try {
            manager.getUserSettings().setString("ultimaAziendaLoggata", aziendaComboBox.getValue());
            manager.getUserSettings().setString("ultimoUtenteLoggato", userNameTextField.getText());
            manager.getUserSettings().save();
        } catch (Exception e) {
            LOGGER.error(
                    "-->errore nel settare la property ultimaAziendaLoggata con value " + aziendaComboBox.getValue(),
                    e);
        }
    }

}