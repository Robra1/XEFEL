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
	 * der Code, der bei Tastendruck eingef�gt wird
	 */
	private String command;

	/**
	 * @param k
	 *            die Tastenkombination als Text
	 * @param c
	 *            der Code, der bei Tastendruck eingef�gt wird
	 */
	public Key(String k, String c) {
		this.key = k.toUpperCase();
		this.command = c;
	}

	/**
	 * @return den Text, der bei Tastendruck eingef�gt werden soll
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param e
	 *            das ausgel�ste KeyEvent, das untersucht werden soll
	 * @return gibt zur�ck, ob bei dem KeyEvent die Taste gedr�ckt wurde
	 */
	public boolean isKey(KeyEvent e) {

		// pr�ft, ob die STRG-Taste gedr�ckt sein muss
		boolean strg = key.contains("CTRL");
		// pr�ft, ob die ALT-Taste gedr�ckt sein muss
		boolean alt = key.contains("ALT");
		// pr�ft, ob die SHIFT-Taste gedr�ckt sein muss
		boolean shift = key.contains("SHIFT");
		// pr�ft, ob die ALTGR-Taste gedr�ckt sein muss
		boolean altgr = key.contains("ALTGR");

		// entfernt alle Key-Modifier, sodass nur die Taste �brig bleibt
		String k = key.replace("CTRL", "").replace("ALT", "").replace("SHIFT", "").replace("ALTGR", "").trim();

		if (e.isControlDown() == strg && e.isAltDown() == alt && e.isAltGraphDown() == altgr && e.isShiftDown() == shift
				&& (int) k.charAt(0) == e.getKeyCode()) {
			return true;
		}
		return false;

	}

}
