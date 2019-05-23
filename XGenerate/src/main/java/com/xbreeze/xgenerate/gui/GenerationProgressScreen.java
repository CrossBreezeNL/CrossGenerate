package com.xbreeze.xgenerate.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.xbreeze.xgenerate.observer.GenerationObserver;

public class GenerationProgressScreen extends JFrame implements GenerationObserver {
	// The serial version uid.
	private static final long serialVersionUID = -5313577704793371238L;
	
	// The initial width of the window.
	private static final int PROGRESS_WINDOW_WIDTH = 750;
	// The initial height of the window.
	private static final int PROGRESS_WINDOW_HEIGHT = 300;
	// The title of the window.
	private static final String PROGRESS_WINDOW_TITLE = "CrossGenerate - Generation progress";
	
	// The progress bar.
	private JProgressBar _generationProgressBar;
	// The title for the progress bar.
	private TitledBorder _generationProgressBorder;
	// The generation log.
	private JTextArea _generationLog;

	/**
	 * Constructor for the GenerationProgressScreen.
	 * @throws Exception It might throw an exception of there was a problem setting the look and feel, or invoking the creation of the window.
	 */
	public GenerationProgressScreen() throws Exception {
		// Set the look and feel of the UI manager to the OS default.
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				GenerationProgressScreen.this.CreateProgressFrame();
			}
		});
	}
	
	/**
	 * Procedure to create the progress window.
	 */
	public void CreateProgressFrame() {
		// Set the title of the frame.
		this.setTitle(PROGRESS_WINDOW_TITLE);
		// Set the icon of the frame.
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("x-generate-icon.png")));
		// Set the size of the frame.
		this.setSize(PROGRESS_WINDOW_WIDTH, PROGRESS_WINDOW_HEIGHT);
		
		// Create a content panel to put all UI elements in.
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout(10, 10));
		// Create an empty border with a width of 10px.
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10,  10,  10,  10));
		
		// Add the content pane to the frame's content pane.
		this.getContentPane().add(contentPanel);
		
		// Create the progress bar.
		JPanel progressBarPanel = new JPanel(new BorderLayout());
		_generationProgressBorder = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10), "Starting...");
		progressBarPanel.setBorder(_generationProgressBorder);
		_generationProgressBar = new JProgressBar();
		_generationProgressBar.setStringPainted(true);
		progressBarPanel.add(_generationProgressBar, BorderLayout.CENTER);
		contentPanel.add(progressBarPanel, BorderLayout.NORTH);
		
		// Create the JTextArea for the logging of the generation.
		_generationLog = new JTextArea();
		_generationLog.setAutoscrolls(true);
		_generationLog.setBackground(_generationProgressBar.getBackground());
		// Add the log area to the frame.
		_generationLog.setEditable(false);
		_generationLog.setRows(10);
		JScrollPane generationLogScrollPane = new JScrollPane(_generationLog);
		// Create a titled border for the scroll pane.
		TitledBorder generationLogBorder = BorderFactory.createTitledBorder("Generation log");
		generationLogScrollPane.setBorder(generationLogBorder);
		contentPanel.add(generationLogScrollPane, BorderLayout.CENTER);
		
		// Add a window listener for closing the frame.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Align the frame to the middle of the screen.
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenDimension.width/2-this.getSize().width/2, screenDimension.height/2-this.getSize().height/2);
		
		// Make the frame visible.
		this.setVisible(true);
	}
	
	@Override
	public void generationStarting(int totalGenerationSteps, LocalDateTime eventDateTime) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Set the maximum value for the progress bar.
				GenerationProgressScreen.this._generationProgressBar.setMinimum(0);
				GenerationProgressScreen.this._generationProgressBar.setMaximum(totalGenerationSteps);
				// Set the progress bar to its minimum.
				GenerationProgressScreen.this._generationProgressBar.setValue(0);
				// Update the generation progress label.
				GenerationProgressScreen.this._generationProgressBorder.setTitle("Generation starting");
				GenerationProgressScreen.this.addToLog("Generation starting.", eventDateTime);
				
				GenerationProgressScreen.this.refreshUI();
			}
		});
	}
	
	/**
	 * Function to refresh the UI.
	 */
	private void refreshUI() {
		this.getContentPane().repaint();
	}
	
	@Override
	public void generationStepStarting(int generationStepIndex, String generationStepName, LocalDateTime eventDateTime) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Update the current value of the progress bar to the step minus 1 (since it is not done with this step yet).
				_generationProgressBar.setValue(generationStepIndex - 1);
				// Update the generation progress label.
				_generationProgressBorder.setTitle(String.format("Generation step %d/%d started", generationStepIndex, _generationProgressBar.getMaximum()));
				addToLog(String.format("Generation started for '%s'.", generationStepName), eventDateTime);
				// Repaint the progress bar, the progress bar only repaints if the value has changed, but here we updated to border title.
				refreshUI();
			}
		});
	}
	
	@Override
	public void generationStepFinished(int generationStepIndex, String generationStepName, LocalDateTime eventDateTime) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Update the current value of the progress bar.
				_generationProgressBar.setValue(generationStepIndex);
				// Update the generation progress label.
				_generationProgressBorder.setTitle(String.format("Generation step %d/%d finished", generationStepIndex, _generationProgressBar.getMaximum()));
				addToLog(String.format("Generation finished for '%s'.", generationStepName), eventDateTime);
				
				refreshUI();				
			}
		});
	}
	
	@Override
	public void generationStepFailed(int generationStepIndex, String generationStepName, String errorMessage, LocalDateTime eventDateTime) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Update the current value of the progress bar.
				_generationProgressBar.setValue(generationStepIndex);
				// Set the bar color to red.
				_generationProgressBar.setForeground(Color.RED);
				// Update the generation progress label.
				_generationProgressBorder.setTitle(String.format("Generation step %d/%d failed", generationStepIndex, _generationProgressBar.getMaximum()));
				addToLog(String.format("Error while generating '%s': %s.", generationStepName, errorMessage), eventDateTime);
				
				// Repaint the progress bar, the progress bar only repaints if the value has changed, but here we updated to border title.
				refreshUI();				
			}
		});
	}

	@Override
	public void generationFinished(LocalDateTime eventDateTime) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Set the progress bar to its maximum.
				_generationProgressBar.setValue(_generationProgressBar.getMaximum());
				// Update the generation progress label.
				_generationProgressBorder.setTitle("Generation done.");
				addToLog("Generation done.", eventDateTime);
				refreshUI();
				
				// Close the UI.
				GenerationProgressScreen.this.setVisible(false);
				GenerationProgressScreen.this.dispose();
			}
		});
	}
	
	/**
	 * Add a line to the log text area.
	 * @param logMessage The log message to add.
	 * @param eventDateTime The date and time of the log event.
	 */
	public void addToLog(String logMessage, LocalDateTime eventDateTime) {
		_generationLog.append(String.format("%s%s - %s", (_generationLog.getText().length() > 0) ? "\n" : "", eventDateTime.toString(), logMessage));
	}
}
