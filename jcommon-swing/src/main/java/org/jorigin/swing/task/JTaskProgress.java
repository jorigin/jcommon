package org.jorigin.swing.task;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.jorigin.Common;


/**
 * A panel dedicated to task progress monitoring.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.8
 */
public class JTaskProgress extends JPanel {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = Common.BUILD;

	/**
	 * The progress bar used to monitor the task progress.
	 */
	private JProgressBar progressBar   = null;

	/**
	 * The label used to display the task progress.
	 */
	private JLabel label               = null;

	/**
	 * Is the label visible (<code>true</code>) or not (<code>false</code>).
	 */
	private boolean labelVisible       = true;

	/**
	 * Is the progress bar visible (<code>true</code>) or not (<code>false</code>).
	 */
	private boolean progressBarVisible = true;

	/**
	 * Create a new task progress monitor.
	 * @param progressBar the progress bar to use.
	 * @param label the label of the monitor.
	 */
	public JTaskProgress(JProgressBar progressBar, JLabel label ){
		super();
		this.progressBar = progressBar;
		this.label       = label;
	}

	/**
	 * Create a new default task progress.
	 */
	public JTaskProgress(){
		this(new JProgressBar(), new JLabel());
		initGUI();
	}

	/**
	 * Create a new progress monitor.
	 * @param labelVisible is the label is visible.
	 * @param progressBarVisible is the progress bar is visible.
	 */
	public JTaskProgress(boolean labelVisible, boolean progressBarVisible){
		this(new JProgressBar(), new JLabel());
		initGUI();
	}

	/**
	 * Get if the label is visible.
	 * @return <code>true</code> if the label is visible and <code>false</code> otherwise.
	 * @see #setLabelVisible(boolean)
	 */
	public boolean isLabelVisible() {
		return this.labelVisible;
	}

	/**
	 * Set if the label is visible.
	 * @param labelVisible <code>true</code> if the label is visible and <code>false</code> otherwise.
	 * @see #isLabelVisible()
	 */
	public void setLabelVisible(boolean labelVisible) {

		if (this.labelVisible != labelVisible){
			this.labelVisible = labelVisible;

			if (isLabelVisible()){
				add(this.label, BorderLayout.NORTH);
			} else {
				remove(this.label);
			}
		}
	}

	/**
	 * Get if the progress bar is visible.
	 * @return <code>true</code> if the progress bar is visible and <code>false</code> otherwise.
	 * @see #setProgressBarVisible(boolean)
	 */
	public boolean isProgressBarVisible() {
		return this.progressBarVisible;
	}

	/**
	 * Set if the progress bar is visible.
	 * @param progressBarVisible <code>true</code> if the progress bar is visible and <code>false</code> otherwise.
	 * @see #isProgressBarVisible()
	 */
	public void setProgressBarVisible(boolean progressBarVisible) {

		if (this.progressBarVisible != progressBarVisible){
			this.progressBarVisible = progressBarVisible;

			if (isProgressBarVisible()){
				add(this.progressBar, BorderLayout.CENTER);
			} else {
				remove(this.progressBar);
			}
		}


	}

	/**
	 * Get the progress bar attached to this monitor.
	 * @return  the progress bar attached to this monitor.
	 */
	public JProgressBar getProgressBar() {
		return this.progressBar;
	}

	/**
	 * Get the label of this monitor.
	 * @return the label of this monitor.
	 */
	public JLabel getLabel() {
		return this.label;
	}

	/**
	 * Initialize the Graphical User Interface (GUI) components.
	 */
	protected void initGUI(){
		setLayout(new BorderLayout());

		if (isLabelVisible()){
			add(this.label, BorderLayout.NORTH);
		}

		if (isProgressBarVisible()){
			add(this.progressBar, BorderLayout.CENTER);
		}
	}
}