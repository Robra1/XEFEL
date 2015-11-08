import java.util.ArrayList;
import java.util.List;

/**
 * Eine Gruppe von Zeichen, die von einem ZeichengruppenButton repräsentiert werden soll
 * @author Robra1
 * @version 0.1
 */
public class Zeichengruppe {

	/**
	 * die Zeichen in der Gruppe
	 */
	private List<Zeichen> zeichen;
	/**
	 * der Titel der Gruppe
	 */
	private String title;
	/**
	 * der ToolTip des Buttons
	 */
	private String toolTip;
	/**
	 * der Pfad des Bildes, das auf dem Button angezeigt werden soll
	 */
	private String imagePath;

	/**
	 * @param title
	 *            der Titel der Gruppe
	 * @param toolTip
	 *            der ToolTip des Buttons
	 * @param imagePath
	 *            der Pfad des Bildes, das auf dem Button angezeigt werden soll
	 */
	public Zeichengruppe(String title, String toolTip, String imagePath) {
		this.title = title;
		this.toolTip = toolTip;
		this.imagePath = imagePath;

		zeichen = new ArrayList<Zeichen>();
	}

	/**
	 * @param z
	 *            fügt das Zeichen der Gruppe hinzu
	 */
	public void addZeichen(Zeichen z) {
		zeichen.add(z);
	}

	/**
	 * @return die Liste an Zeichen
	 */
	public List<Zeichen> getZeichen() {
		return this.zeichen;
	}

	/**
	 * @return den Titel der Gruppe
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return den toolTip der Gruppe
	 */
	public String getToolTip() {
		return this.toolTip;
	}

	/**
	 * @return den Pfad zum Bild
	 */
	public String getImagePath() {
		return this.imagePath;
	}

}
