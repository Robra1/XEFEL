import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Ein Button, der eine Zeichengruppe repräsentiert
 * 
 * @author Robra1
 * @version 0.1
 */
public class ZeichengruppeButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1533601583750211185L;
	/**
	 * Die Breite des Buttons
	 */
	private final int BUTTON_WIDTH = 50;
	/**
	 * Die Höhe des Buttons
	 */
	private final int BUTTON_HEIGHT = 50;
	/**
	 * Die Zeichengruppe, die repräsentiert werden soll
	 */
	private Zeichengruppe zg;

	/**
	 * @param icon
	 *            das Icon, das angezeigt werden soll
	 * @param zg
	 *            die Zeichengruppe, die repräsentiert werden soll
	 */
	public ZeichengruppeButton(Icon icon, Zeichengruppe zg) {
		this.zg = zg;

		this.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

		Image im = ((ImageIcon) icon).getImage();
		Image nIm = im.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_SMOOTH);
		icon = new ImageIcon(nIm);

		this.setIcon(icon);

		this.setToolTipText(zg.getToolTip());
	}

	/**
	 * @param panelZeichen
	 *            das JPanel, in den alle ZeichenButton angezeigt werden sollen
	 * @param a
	 *            der ActionListener, der auf die Buttons reagieren soll
	 */
	public void addZeichen(JPanel panelZeichen, ActionListener a) {
		panelZeichen.removeAll();
		for (Zeichen z : zg.getZeichen()) {
			ImageIcon icon = new ImageIcon("Images" + File.separator + z.getImagePath());
			ZeichenButton b = new ZeichenButton(icon, z);
			b.addActionListener(a);
			panelZeichen.add(b);
		}
		panelZeichen.revalidate();
		panelZeichen.repaint();
	}

}
