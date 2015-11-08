/**
 * Ein Zeichen, dass in einem ZeichenButton angezeigt wird
 * 
 * @author Robra1
 * @version 0.1
 */
public class Zeichen {
	/**
	 * der TeX-Code des Zeichens
	 */
	private String command;
	/**
	 * der Pfad zu dem Bild, das im ZeichenButton angezeigt wird
	 */
	private String imagePath;
	/**
	 * toolTip des ZeichenButtons
	 */
	private String toolTip;
	/**
	 * der Name des Zeichens
	 */
	private String name;

	/**
	 * @param name
	 *            der Name des Zeichens
	 * @param command
	 *            der TeX-Code des Zeichens
	 * @param toolTip
	 *            toolTip des ZeichenButtons
	 * @param imagePath
	 *            der Pfad zu dem Bild, das im ZeichenButton angezeigt wird
	 */
	public Zeichen(String name, String command, String toolTip, String imagePath) {
		this.name = name;
		this.command = command;
		this.toolTip = toolTip;
		this.imagePath = imagePath;
	}

	/**
	 * @return den Namen des Zeichens
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return den Pfad zum Bild
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @return den ToolTip
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * @return den TeX-Code
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return den TeX-Code ohne die Platzhalter
	 */
	public String getCommandText() {
		return command.replaceAll("%\\d", "") + " ";
	}

}
