import java.awt.event.KeyEvent;

/**
 * Objekt, in dem ein Tastendruck mit einem TeX-Code verbunden wird
 * 
 * @author Robra1
 * @version 0.1
 */

public class Key {

	/***
	 * Die Tastenkombination als Text
	 ***/
	private String key;

	/**
	 * der Code, der bei Tastendruck eingefügt wird
	 */
	private String command;

	/**
	 * @param k
	 *            die Tastenkombination als Text
	 * @param c
	 *            der Code, der bei Tastendruck eingefügt wird
	 */
	public Key(String k, String c) {
		this.key = k.toUpperCase();
		this.command = c;
	}

	/**
	 * @return den Text, der bei Tastendruck eingefügt werden soll
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param e
	 *            das ausgelöste KeyEvent, das untersucht werden soll
	 * @return gibt zurück, ob bei dem KeyEvent die Taste gedrückt wurde
	 */
	public boolean isKey(KeyEvent e) {

		// prüft, ob die STRG-Taste gedrückt sein muss
		boolean strg = key.contains("CTRL");
		// prüft, ob die ALT-Taste gedrückt sein muss
		boolean alt = key.contains("ALT");
		// prüft, ob die SHIFT-Taste gedrückt sein muss
		boolean shift = key.contains("SHIFT");
		// prüft, ob die ALTGR-Taste gedrückt sein muss
		boolean altgr = key.contains("ALTGR");

		// entfernt alle Key-Modifier, sodass nur die Taste übrig bleibt
		String k = key.replace("CTRL", "").replace("ALT", "").replace("SHIFT", "").replace("ALTGR", "").trim();

		if (e.isControlDown() == strg && e.isAltDown() == alt && e.isAltGraphDown() == altgr && e.isShiftDown() == shift
				&& (int) k.charAt(0) == e.getKeyCode()) {
			return true;
		}
		return false;

	}

}
