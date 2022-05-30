package org.zurdavid.petrinets.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.zurdavid.petrinets.IPetrinetController;

/**
 * The class implements a menu bar for a PetrinetFrame.
 * 
 * @author David Zurschmitten
 *
 */
public class Menu extends JMenuBar {
	private String path = System.getProperty("user.dir");
	private File lastFile;

	/**
	 * In the constructor the menu items are created and added to the menu.
	 * 
	 * @param controller The Petri net applications main controller.
	 * @param statusbar  The status bar of a PetrinetFrmae.
	 */
	public Menu(IPetrinetController controller, JLabel statusbar) {
		Locale currentLocale = Locale.getDefault();
		ResourceBundle re = ResourceBundle.getBundle("Strings", currentLocale);

		JMenu menu = new JMenu(re.getString("file"));
		add(menu);

		JMenuItem openFile = new JMenuItem(re.getString("open"));
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(path);
				chooser.setPreferredSize( new Dimension(800, 600) );
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNML", "pnml");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(getTopLevelAncestor());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File petrinetfile = chooser.getSelectedFile();
					path = petrinetfile.getPath();
					lastFile = petrinetfile;
					controller.loadPetrinet(petrinetfile);
					statusbar.setText(petrinetfile.getName());
				}
			}
		});
		menu.add(openFile);

		JMenuItem reloadCurrentFile = new JMenuItem(re.getString("reload"));
		reloadCurrentFile.addActionListener(e -> {
			if (lastFile != null)
				controller.loadPetrinet(lastFile);
		});
		menu.add(reloadCurrentFile);

		JMenuItem analyseMultipleFiles = new JMenuItem(re.getString("analyzeMult"));
		analyseMultipleFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(path);
				chooser.setMultiSelectionEnabled(true);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNML", "pnml");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(getTopLevelAncestor());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File[] files = chooser.getSelectedFiles();
					path = files[0].getPath();
					controller.boundednessAnalysisOnMultipleFiles(files);
				}
			}
		});
		menu.add(analyseMultipleFiles);

		JMenuItem propertyAnalysis = new JMenuItem(re.getString("propertyAnalysis"));
		propertyAnalysis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(path);
				chooser.setMultiSelectionEnabled(true);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNML", "pnml");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(getTopLevelAncestor());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File[] files = chooser.getSelectedFiles();
					path = files[0].getPath();
					controller.propertyAnalysis(files);
				}

			}
		});
		menu.add(propertyAnalysis);

		JMenuItem exit = new JMenuItem(re.getString("exit"));
		exit.addActionListener(e -> System.exit(0));
		menu.add(exit);
	}
}
