import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Hauptfenster des Programms
 * 
 * @author Robra1
 * @version 0.1
 */
public class XEFEL extends JFrame implements ActionListener, MouseListener, DocumentListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -294284514504271087L;

	Container content;

	JMenuBar menuBar;
	JMenu menuDatei, menuEdit, menuFormel, menuHelp;
	JMenuItem dateiNew, dateiOpen, dateiSave, dateiSaveAs, dateiExit;
	JMenuItem editUndo, editCut, editCopy, editPaste, editDelete;
	JMenuItem formelSavePNG, formelSaveSVG, formelWolfram;
	JMenuItem helpAbout;

	private JPanel pnlZeichen;
	private JPanel pnlZeichengruppen;
	private JPanel pnlVorschau;
	private JPanel pnlTeX;

	private JScrollPane scrZeichen;
	private JScrollPane scrZeichengruppen;
	private JScrollPane scrVorschau;
	private JScrollPane scrTeX;

	private JTextPane tpTeX;
	private JLabel lbVorschau;

	private List<Key> hotkeys;
	private List<Zeichengruppe> zeichengruppen;

	private String math;
	private TeXFormula formel;
	private TeXIcon icon;
	private BufferedImage b;

	private boolean isNew = true;
	private boolean isSaved = true;
	private String filename = "";

	private UndoManager undoMan;

	private final String title = "XEFEL";

	public XEFEL() throws HeadlessException {
		this.setSize(new Dimension(800, 600));
		this.setTitle(title);

		content = new Container();

		menuBar = new JMenuBar();

		menuDatei = new JMenu("Datei");
		menuEdit = new JMenu("Bearbeiten");
		menuFormel = new JMenu("Formel");
		menuHelp = new JMenu("Hilfe");

		dateiNew = new JMenuItem("Neu");
		dateiOpen = new JMenuItem("Öffnen...");
		dateiSave = new JMenuItem("Speichern");
		dateiSaveAs = new JMenuItem("Speichern unter...");
		dateiExit = new JMenuItem("Beenden");

		dateiNew.addActionListener(this);
		dateiOpen.addActionListener(this);
		dateiSave.addActionListener(this);
		dateiSaveAs.addActionListener(this);
		dateiExit.addActionListener(this);

		editUndo = new JMenuItem("Rückgängig");
		editCut = new JMenuItem("Ausschneiden");
		editCopy = new JMenuItem("Kopieren");
		editPaste = new JMenuItem("Einfügen");
		editDelete = new JMenuItem("Löschen");

		editUndo.addActionListener(this);
		editCut.addActionListener(this);
		editCopy.addActionListener(this);
		editPaste.addActionListener(this);
		editDelete.addActionListener(this);

		formelSavePNG = new JMenuItem("als PNG speichern...");
		formelSaveSVG = new JMenuItem("als SVG speichern...");
		formelWolfram = new JMenuItem("Wolfram Alpha...");

		formelSavePNG.addActionListener(this);
		formelSaveSVG.addActionListener(this);
		formelWolfram.addActionListener(this);

		helpAbout = new JMenuItem("Über...");

		helpAbout.addActionListener(this);

		menuBar.add(menuDatei);
		menuBar.add(menuEdit);
		menuBar.add(menuFormel);
		menuBar.add(menuHelp);

		menuDatei.add(dateiNew);
		menuDatei.add(dateiOpen);
		menuDatei.add(dateiSave);
		menuDatei.add(dateiSaveAs);
		menuDatei.add(dateiExit);

		menuEdit.add(editUndo);
		menuEdit.add(editCut);
		menuEdit.add(editCopy);
		menuEdit.add(editPaste);
		menuEdit.add(editDelete);

		menuFormel.add(formelSavePNG);
		menuFormel.add(formelSaveSVG);
		menuFormel.add(formelWolfram);

		menuHelp.add(helpAbout);

		this.add(menuBar, BorderLayout.NORTH);

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		content.setLayout(gbl);

		gbc.weightx = gbc.weighty = .15;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		pnlZeichengruppen = new JPanel();
		scrZeichengruppen = new JScrollPane(pnlZeichengruppen);
		gbl.setConstraints(scrZeichengruppen, gbc);
		content.add(scrZeichengruppen);

		gbc.weightx = gbc.weighty = .15;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		pnlZeichen = new JPanel();
		scrZeichen = new JScrollPane(pnlZeichen);
		gbl.setConstraints(scrZeichen, gbc);
		content.add(scrZeichen);

		gbc.weightx = gbc.weighty = .35;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 4;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		pnlVorschau = new JPanel(new FlowLayout());
		scrVorschau = new JScrollPane(pnlVorschau);
		gbl.setConstraints(scrVorschau, gbc);
		content.add(scrVorschau);

		gbc.weightx = gbc.weighty = .35;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 3;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 5;
		pnlTeX = new JPanel(new GridLayout(1, 1));
		scrTeX = new JScrollPane(pnlTeX);
		gbl.setConstraints(scrTeX, gbc);
		content.add(scrTeX);

		pnlZeichengruppen.setPreferredSize(new Dimension(200, 300));
		pnlZeichen.setPreferredSize(new Dimension(100, 10000));

		lbVorschau = new JLabel();
		pnlVorschau.add(lbVorschau);

		hotkeys = new ArrayList<Key>();

		scrVorschau.addMouseListener(this);

		tpTeX = new JTextPane();
		tpTeX.getDocument().addDocumentListener(this);
		pnlTeX.add(tpTeX);

		loadGruppen();

		ZeichengruppeButton[] buttons = new ZeichengruppeButton[zeichengruppen.size()];
		int zealer = 0;

		for (Zeichengruppe z : zeichengruppen) {
			ImageIcon icon = new ImageIcon("Images/" + z.getImagePath());
			ZeichengruppeButton b = new ZeichengruppeButton(icon, z);
			pnlZeichengruppen.add(b);
			b.addActionListener(this);
			buttons[zealer++] = b;
		}
		tpTeX.addKeyListener(this);
		undoMan = new UndoManager();
		tpTeX.getDocument().addUndoableEditListener(undoMan);
		this.add(content, BorderLayout.CENTER);
		this.setVisible(true);
	}

	/**
	 * lädt alle Zeichengruppen aus den XML-Dateien
	 */
	public void loadGruppen() {
		try {
			String pfad = new java.io.File("").getAbsolutePath();
			File dir = new File(pfad + File.separator + "Gruppen");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();

			zeichengruppen = new ArrayList<Zeichengruppe>();
			for (File f : dir.listFiles()) {
				Document doc;
				doc = builder.parse(f);
				String name = doc.getElementsByTagName("name").item(0).getTextContent();
				String toolTip = doc.getElementsByTagName("toolTip").item(0).getTextContent();
				String imagePath = doc.getElementsByTagName("image").item(0).getTextContent();
				Zeichengruppe zg = new Zeichengruppe(name, toolTip, imagePath);

				Node items = doc.getElementsByTagName("items").item(0);
				NodeList nl = items.getChildNodes();

				for (int i = 0; i < nl.getLength(); i++) {
					Node n = nl.item(i);
					if (n.getNodeType() == Node.ELEMENT_NODE) {
						Element e = (Element) n;
						String itemName = e.getAttribute("name");
						String itemCommand = e.getElementsByTagName("command").item(0).getTextContent();
						String itemToolTip = e.getElementsByTagName("toolTip").item(0).getTextContent();
						String itemImage = e.getElementsByTagName("image").item(0).getTextContent();
						String itemKeys = e.getElementsByTagName("keys").item(0).getTextContent();

						Zeichen z = new Zeichen(itemName, itemCommand, itemToolTip, itemImage);
						if (itemKeys.length() > 0) {
							Key ke = new Key(itemKeys, z.getCommandText());
							hotkeys.add(ke);
						}
						zg.addZeichen(z);
					}
				}
				zeichengruppen.add(zg);
			}
		} catch (ParserConfigurationException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
		} catch (SAXException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
		}

	}

	/**
	 * fügt den Text ab der Stelle des Cartes ein
	 * 
	 * @param s
	 *            der Text, der eingefügt werden soll
	 */
	private void insertCommand(String s) {
		javax.swing.text.Document d = tpTeX.getDocument();
		try {
			d.insertString(tpTeX.getCaretPosition(), s, null);
		} catch (BadLocationException e1) {

		}
	}

	/**
	 * rendert den Text im Textfeld und zeigt das Ergebnis an
	 */
	public void render() {
		try {
			math = tpTeX.getText();
			formel = new TeXFormula(math);
			icon = formel.createTeXIcon(TeXConstants.STYLE_DISPLAY, 40);
			b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			icon.paintIcon(new JLabel(), b.getGraphics(), 0, 0);
			lbVorschau.setIcon(icon);
		} catch (Exception e) {

		}
	}

	/**
	 * löscht den Inhalt des Textfeldes
	 */
	private void newFile() {
		if (!isSaved) {
			if (JOptionPane.showConfirmDialog(null, "Ungespeicherte Änderungen gehen verloren.\n Fortfahren?",
					title + " Achtung!", JOptionPane.OK_CANCEL_OPTION) != 0) {
				return;
			}
		}
		tpTeX.setText("");
		isNew = true;
		isSaved = true;
		this.setTitle(title);
	}

	/**
	 * öffnet einen FileChoose Dialog und schreibt den Inhalt der ausgewählten
	 * Datei in das Textfeld
	 */
	private void openFile() {
		JFileChooser jfc = new JFileChooser();
		if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				tpTeX.setText(FileUtils.readFileToString(jfc.getSelectedFile()));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
			}
		}
	}

	/**
	 * speichert die Formel ab
	 */
	private void saveFile() {
		try {
			FileUtils.writeStringToFile(new File(filename), tpTeX.getText());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
		}
		isSaved = true;
		this.setTitle(title);
	}

	/**
	 * öffnet einen FileChoose Dialog und speichert die Formel unter dem
	 * angegebenen Pfad
	 */
	private void saveFileAs() {
		JFileChooser jfc = new JFileChooser();
		if (jfc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		filename = jfc.getSelectedFile().getAbsolutePath();
		filename += (FilenameUtils.getExtension(filename) == "") ? ".xfd" : "";
		File f = new File(filename);
		try {
			FileUtils.writeStringToFile(f, tpTeX.getText());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
		}
		isSaved = true;
		isNew = false;
		this.setTitle(title);
	}

	/**
	 * Beendet das Programm
	 */
	private void exit() {
		if (!isSaved) {
			if (JOptionPane.showConfirmDialog(null, "Ungespeicherte Änderungen gehen verloren.\n Fortfahren?",
					title + " Achtung!", JOptionPane.OK_CANCEL_OPTION) != 0) {
				return;
			}
		}
		System.exit(0);
	}

	/**
	 * öffnet einen FileChoose Dialog und speichert die Formel unter dem
	 * angegebenen Pfad als PNG-Datei
	 */
	private void save() {
		JFileChooser fc = new JFileChooser();
		fc.showSaveDialog(null);
		try {
			BufferedImage img = new BufferedImage(lbVorschau.getWidth(), lbVorschau.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, lbVorschau.getWidth(), lbVorschau.getHeight());
			g2.setColor(Color.BLACK);
			lbVorschau.printAll(g2);
			g2.dispose();

			String filename = fc.getSelectedFile().getAbsolutePath();
			filename += (filename.endsWith(".png")) ? "" : ".png";
			File newFile = new File(filename);

			ImageIO.write(img, "png", newFile);
			JOptionPane.showMessageDialog(null, "Datei gespeichert!", "XEFEL", JOptionPane.PLAIN_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler!", JOptionPane.OK_OPTION);
		}
	}

	/**
	 * öffnet einen FileChoose Dialog und speichert die Formel unter dem
	 * angegebenen Pfad als Vektorgrafik
	 */
	private void saveSVG() {
		JFileChooser fc = new JFileChooser();
		if (fc.showSaveDialog(null) == JFileChooser.CANCEL_OPTION)
			return;

		try {

			DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
			String svgNS = "http://www.w3.org/2000/svg";
			Document document = domImpl.createDocument(svgNS, "svg", null);
			SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);

			SVGGraphics2D g2 = new SVGGraphics2D(ctx, true);

			TeXFormula formula = new TeXFormula(math);
			TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
			icon.setInsets(new Insets(5, 5, 5, 5));
			g2.setSVGCanvasSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
			g2.setColor(Color.white);
			g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());

			JLabel jl = new JLabel();
			jl.setForeground(new Color(0, 0, 0));
			icon.paintIcon(jl, g2, 0, 0);

			String filename = fc.getSelectedFile().getAbsolutePath();
			filename += (filename.endsWith(".svg")) ? "" : ".svg";
			File newFile = new File(filename);

			boolean useCSS = true;
			FileOutputStream svgs = new FileOutputStream(newFile);
			Writer out = new OutputStreamWriter(svgs, "UTF-8");
			g2.stream(out, useCSS);
			svgs.flush();
			svgs.close();
			JOptionPane.showMessageDialog(null, "Datei gespeichert!", "XEFEL", JOptionPane.PLAIN_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * gibt die Formel in WolframAlpha ein und ruft die Seite im Browser auf
	 */
	private void toWolframAlpha() {
		String out = "http://www.wolframalpha.com/input/?i=";
		for (char c : math.toCharArray()) {
			out += "%" + Integer.toHexString((int) c);
		}
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI(out));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Browser konnte nicht geöffnet werden!", "XEFEL",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * wird aufgerufen, wenn der TeX Code verändert wurde.<br>
	 * Sorgt dafür, dass der Code neu gezeichnet werden soll und markiert die
	 * Datei als ungespeichert
	 * 
	 */
	private void changedTeX() {
		render();
		isSaved = false;
		this.setTitle(title + "*");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o instanceof ZeichengruppeButton) {
			ZeichengruppeButton b = (ZeichengruppeButton) o;
			b.addZeichen(pnlZeichen, this);
		} else if (o instanceof ZeichenButton) {
			ZeichenButton b = (ZeichenButton) o;
			insertCommand(b.getZeichen().getCommandText());
		} else if (o instanceof JMenuItem) {
			if (o == dateiNew) {
				newFile();
			} else if (o == dateiOpen) {
				newFile();
				openFile();
			} else if (o == dateiSave) {
				if (isNew) {
					saveFileAs();
				} else {
					saveFile();
				}
			} else if (o == dateiSaveAs) {
				saveFileAs();
			} else if (o == dateiExit) {
				exit();
			} else if (o == editUndo) {
				try {
					undoMan.undo();
				} catch (Exception e2) {

				}
			} else if (o == editCut) {
				tpTeX.cut();
			} else if (o == editCopy) {
				tpTeX.copy();
			} else if (o == editPaste) {
				tpTeX.paste();
			} else if (o == editDelete) {
				try {
					tpTeX.getDocument().remove(tpTeX.getSelectionStart(), tpTeX.getSelectedText().length());
				} catch (BadLocationException e1) {

				}
			} else if (o == formelSavePNG) {
				save();
			} else if (o == formelSaveSVG) {
				saveSVG();
			} else if (o == formelWolfram) {
				toWolframAlpha();
			} else if (o == helpAbout) {
				JFrame ab = new AboutBox();
				ab.setVisible(true);
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_TAB:
			e.consume();
			if (tpTeX.getText().contains("}")) {
				int posNow = tpTeX.getCaretPosition() + 1;
				int posNext = tpTeX.getText().indexOf("}", posNow);
				tpTeX.setCaretPosition((posNext < 1) ? tpTeX.getText().indexOf("}") : posNext);
			}
			break;
		case KeyEvent.VK_N:
			if (e.isControlDown())
				newFile();
			break;
		case KeyEvent.VK_O:
			if (e.isControlDown()) {
				newFile();
				openFile();
			}
			break;
		case KeyEvent.VK_S:
			if (e.isControlDown()) {
				if (e.isAltDown()) {
					saveFileAs();
				} else {
					if (isNew) {
						saveFileAs();
					} else {
						saveFile();
					}
				}
			}
			break;
		default:
			hotkeys.parallelStream().filter(k -> k.isKey(e)).forEach(k -> insertCommand(k.getCommand()));
			break;
		}

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON3) {
			toWolframAlpha();
		} else if (arg0.getButton() == MouseEvent.BUTTON1) {
			saveSVG();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		changedTeX();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		changedTeX();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		changedTeX();
	}

	public static void main(String[] args) {
		new XEFEL();
	}

}
