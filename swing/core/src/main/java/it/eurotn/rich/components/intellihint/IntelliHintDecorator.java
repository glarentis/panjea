package it.eurotn.rich.components.intellihint;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.springframework.rules.closure.Closure;

/**
 * Decorator che installa un popup per l'autocompletamento dei valori durante la digitazione in un componente
 * {@link JTextField}.
 * 
 * @author fattazzo
 */
public class IntelliHintDecorator {

	private JTextComponent textFieldComponent;
	private Closure closure;
	private String specialKey;

	private IntelliHintPopup popup;

	public static final String INTELLIHINT_VALUE_SEPARATOR = " - ";

	/**
	 * Costruttore di default.
	 */
	public IntelliHintDecorator() {
		super();
	}

	/**
	 * Aggancia l'intellihint al componente passato come parametro.
	 * 
	 * @param paramTextField
	 *            Componente di riferimento
	 * @param paramClosure
	 *            Deve restituire una lista di valori che dovrà essere formattata nel seguente modo:<br>
	 *            Valore_selezionabile {@link IntelliHintDecorator#INTELLIHINT_VALUE_SEPARATOR}
	 *            Descrizione_valore_selezionabile
	 * @param paramSpecialKey
	 *            Rappresenta il carattere di delimitazione dei parametri e quindi il carattere che farà apparire il
	 *            popup di scelta
	 */
	public void attachHintelliHint(JTextComponent paramTextField, Closure paramClosure, String paramSpecialKey) {
		this.textFieldComponent = paramTextField;
		this.closure = paramClosure;
		this.popup = new IntelliHintPopup(this);

		this.specialKey = paramSpecialKey;

		// qui gestisco il filtro sulla lista del popup
		this.textFieldComponent.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String stringContent = textFieldComponent.getText();

				String text = stringContent.substring(textFieldComponent.getCaretPosition(),
						textFieldComponent.getCaretPosition() + 1);
				if (text.equals(specialKey)) {
					popup.setFilterKey("");
				} else {
					if (popup.isShowing()) {
						popup.setFilterKey(getCurrentTextConstraint());
					}
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (popup.isShowing()) {
					popup.setFilterKey(getCurrentTextConstraint());
				}
			}
		});

		// qui gestisco i caratteri premuti e la visualizzazione del popup
		this.textFieldComponent.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent paramKeyEvent) {
				char keyChar = paramKeyEvent.getKeyChar();

				// visto che non conosco quale e' il carattere special scelto, lo
				if (keyChar == specialKey.charAt(0)) {
					// recupero la posizione (x,y) del caret perchè nel caso in cui il textcomponent sia una text area o
					// un editor pane, il metodo caret position, non mi torna quello che mi serve
					Point point = textFieldComponent.getCaret().getMagicCaretPosition();
					if (point != null) {
						popup.showPopup((int) point.getX(), (int) point.getY(), textFieldComponent);
					}
				}

				switch (paramKeyEvent.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					popup.selectNextValue();
					if (popup.isPopupVisible()) {
						paramKeyEvent.consume();
					}
					break;
				case KeyEvent.VK_UP:
					popup.selecPriorValue();
					if (popup.isPopupVisible()) {
						paramKeyEvent.consume();
					}
					break;
				case KeyEvent.VK_ENTER:
					String selectecValue = popup.getSelectedValue();

					if (selectecValue != null && popup.isPopupVisible()) {
						try {
							// rimuovo il test che finora era stato inserito per la costante
							int caratteriinseriti = getCurrentTextConstraint().length() + 1;
							textFieldComponent.getDocument().remove(
									textFieldComponent.getCaretPosition() - caratteriinseriti, caratteriinseriti);

							// inserisco la stringa della costante preceduta e seguita del carattere specialkey
							textFieldComponent.getDocument().insertString(textFieldComponent.getCaretPosition(),
									specialKey + selectecValue + specialKey, null);
							popup.hidePopup();

							paramKeyEvent.consume();
						} catch (BadLocationException e) {
							// non rilancio l'eccezione perchè non dovrebbe ma esserci.
						}
					}

					break;
				case KeyEvent.VK_ESCAPE:
					popup.hidePopup();
					break;
				default:
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});
	}

	/**
	 * Restituisce la closure del decorator.
	 * 
	 * @return closure
	 */
	public Closure getClosure() {
		return this.closure;
	}

	/**
	 * Restituisce in base alla posizione del caret tutti i caratteri inseriti per la costante.
	 * 
	 * @return caratteri inseriti
	 */
	private String getCurrentTextConstraint() {
		int caretPosition = textFieldComponent.getCaretPosition();

		String textConstraint = "";
		String currentChar = "";

		while (!currentChar.equals(specialKey) && caretPosition >= 0) {

			if (!currentChar.equals("\n")) {
				textConstraint = currentChar + textConstraint;
			}

			try {
				currentChar = textFieldComponent.getDocument().getText(caretPosition, 1);
			} catch (BadLocationException e) {
			}
			caretPosition--;

		}

		return textConstraint;
	}

	/**
	 * @return Restituisce lo specialkey utilizzato per il riconoscimento delle variabili.
	 */
	public String getSpecialKey() {
		return specialKey;
	}

	/**
	 * Restituisce il textfield gestito dal decorator.
	 * 
	 * @return textfield
	 */
	public JTextComponent getTextFieldComponent() {
		return textFieldComponent;
	}
}
