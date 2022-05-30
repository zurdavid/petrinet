package org.zurdavid.petrinets.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import org.zurdavid.petrinets.*;

/**
 * The class represents the applications main frame. It contains two GraphStream
 * panels in which the Petri net and the reachability graph are displayed as
 * well as a TextArea for text output, a menu bar and a tool bar.
 * 
 * @author David Zurschmitten
 */
public class PetrinetFrame extends JFrame implements IPetrinetAppView {
	private GraphStreamPetrinet petrinetGraph;
	private GraphStreamReachabilityGraph reachabilityGraph;
	private ViewPanel petrinetViewPanel;
	private ViewPanel reachabilityGraphPanel;
	private final IPetrinetController controller;
	private final ITextView textView;
	private final Toolbar toolbar;

	/**
	 * In the constructor the frame and all its components are created and then
	 * displayed.
	 * 
	 * @param controller The applications controller
	 */
	public PetrinetFrame(IPetrinetController controller) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		try {
			URL url = PetrinetFrame.class.getResource("/icon.png");
			setIconImage(ImageIO.read(new File(url.getPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setTitle("PetriNetApp");
		setSize(1000, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JLabel statusbar = new JLabel("Status Bar");
		statusbar.setFont(new Font(statusbar.getFont().getName(), Font.PLAIN, 14));
		statusbar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusbar, BorderLayout.SOUTH);

		textView = new TextView(this);
		JScrollPane txtAreaScrollPane = new JScrollPane((JTextArea) textView);

		setJMenuBar(new Menu(controller, statusbar));
		toolbar = new Toolbar(controller, textView);
		add(toolbar, BorderLayout.NORTH);

		// Initialise view panels for the Petrinet graph and reachability graph
		this.controller = controller;
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		JPanel petrinetViewContainer = initPetrinetView();
		JPanel reachabilityViewContainer = initReachabilityGraphView();

		JSplitPane hsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, petrinetViewContainer, reachabilityViewContainer);
		JSplitPane vsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, hsplit, txtAreaScrollPane);
		hsplit.setResizeWeight(0.5);
		vsplit.setResizeWeight(0.5);
		vsplit.setDividerLocation(getHeight() - 200);
		add(vsplit);

		setVisible(true);
	}

	// initialising the GraphStream panel to display the Petri net
	private JPanel initPetrinetView() {
		petrinetGraph = new GraphStreamPetrinet();

		Viewer viewer = new Viewer(petrinetGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.disableAutoLayout();
		petrinetViewPanel = viewer.addDefaultView(false);
		// The viewer is placed in an additional JPanel, because otherwise there seems
		// to be a bug in GraphStream with click-events if the View is in the second
		// position in a JSplitPane and some other containers. The bug seems to occure
		// only on systems with Windows 7.
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());
		containerPanel.add(petrinetViewPanel);

		ViewerPipe viewerPipe = viewer.newViewerPipe();
		viewerPipe.addViewerListener(new GSPetrinetViewerListener(petrinetGraph, controller, toolbar));
		petrinetViewPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				viewerPipe.pump();
			}

			@Override
			public void mouseReleased(MouseEvent me) {
				viewerPipe.pump();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				petrinetViewPanel.repaint();
			}
		});
		return containerPanel;
	}

	// Initialising the GraphStream panel to display the reachability graph
	private JPanel initReachabilityGraphView() {
		reachabilityGraph = new GraphStreamReachabilityGraph();

		Viewer viewer = new Viewer(reachabilityGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		reachabilityGraphPanel = viewer.addDefaultView(false);
		// The viewer is placed in an additional JPanel, because otherwise there seems
		// to be a bug in GraphStream with click-events if the View is in the second
		// position in a JSplitPane and some other containers. The bug seems to occure
		// only on systems with Windows 7.
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());
		containerPanel.add(reachabilityGraphPanel);
		ViewerPipe viewerPipe = viewer.newViewerPipe();
		viewerPipe.addViewerListener(new GSReachabilityViewerListener(controller));
		reachabilityGraphPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				viewerPipe.pump();
			}

			@Override
			public void mouseReleased(MouseEvent me) {
				viewerPipe.pump();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				reachabilityGraphPanel.repaint();
			}
		});
		return containerPanel;
	}

	@Override
	public void loadPetrinet(IPetrinet petrinet) {
		petrinetGraph.loadPetrinet(petrinet);
		toolbar.onPetrinetLoaded();
	}

	@Override
	public IPetrinetView getPetrinetGraph() {
		return petrinetGraph;
	}

	@Override
	public void loadReachabilityGraph(IReachabilityGraph graph) {
		reachabilityGraph.load(graph);
	}

	@Override
	public IReachabilityGraphView getpRGView() {
		return reachabilityGraph;
	}

	@Override
	public ITextView getTextView() {
		return textView;
	}
}
