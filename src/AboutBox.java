import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * AboutBox, in der Informationen über das Programm angezeigt werden
 * 
 * @author Robra
 * @version 0.1
 */
public class AboutBox extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8704046223823286998L;

	/**
	 * EditorPane, in der die readme.md.html angezeigt wurd
	 */
	JEditorPane box;
	/**
	 * Button, der das Fenster wieder schliesst
	 */
	JButton btOK;

	/**
	 * Erzeugt eine neue AboutBox
	 */
	public AboutBox() {
		try {
			box = new JEditorPane();
			// zeigt den Inhalt der readme.md.html in dem EditorPane
			box.setPage(new java.io.File(new java.io.File("").getAbsolutePath() + File.separator + "readme.md.html")
					.toURI().toURL());
			box.setEditable(false);
			// sorgt dafür, dass die Hyperlinks in der readme.md.html
			// funktionieren
			box.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
						if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
							try {
								desktop.browse(e.getURL().toURI());
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(null, "Browser konnte nicht geöffnet werden!", "XEFEL",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			});

			JScrollPane scrollPane = new JScrollPane(box);

			btOK = new JButton("OK");
			btOK.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					dispose();
				}
			});

			this.add(btOK, BorderLayout.SOUTH);
			this.add(scrollPane);

			this.setVisible(true);
			this.setSize(400, 300);
		} catch (IOException e) {
		}

	}

}
