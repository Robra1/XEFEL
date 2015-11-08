import java.awt.Dimension;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Ein ZeichenButton der ein Zeichen repräsentiert
 * 
 * @author Robra1
 * @version 0.1
 */
public class ZeichenButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8054355992389390525L;
	/**
	 * Die Breite des Buttons
	 */
	private final int BUTTON_WIDTH = 50;
	/**
	 * Die Höhe des Buttons
	 */
	private final int BUTTON_HEIGHT = 50;
	/**
	 * Das Zeichen, das repräsentiert werden soll
	 * 
	 * @see Zeichen
	 */
	private Zeichen z;

	/**
	 * @param icon
	 *            das Icon des Buttons
	 * @param z
	 *            das Zeichen, das repräsentiert werden soll
	 */
	public ZeichenButton(Icon icon, Zeichen z) {

		this.z = z;
		this.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

		Image im = ((ImageIcon) icon).getImage();
		Image nIm = im.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_SMOOTH);
		icon = new ImageIcon(nIm);

		this.setIcon(icon);

		this.setToolTipText(z.getToolTip());
	}

	/**
	 * @return das repräsentierte Zeichen
	 */
	public Zeichen getZeichen() {
		return this.z;
	}

}
