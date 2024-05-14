/*
  This file is part of JOrigin Common Library.

    JOrigin Common is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JOrigin Common is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JOrigin Common.  If not, see <http://www.gnu.org/licenses/>.

 */
package org.jorigin.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import org.jorigin.Common;

/**
 * Create a splash screen with a progress bar. This code is inspired by the code given
 * at http://www.labo-sun.com/resource-fr-codesamples-1126-0-java-gui-splash-screen-avec-progress-bar.htm.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class SplashWindow extends JWindow { 

	private static final long serialVersionUID = Common.BUILD;

	/**
	 * The progress bar integrated to the splash window.
	 */
	private JProgressBar progressBar           = null; 

	/**
	 * The max value of the progress bar.
	 */
	private int maxValue                       = 0;

	/**
	 * The label displaying the state of the splash window.
	 */
	private JLabel state                       = null;

	/**
	 * The panel composing the south of the splash window.
	 */
	private JPanel southPanel                  = null;

	/**
	 * Create a splash window displaying the image given in parameter. 
	 * The maximum value of the progress bar attached to the splash windows is set to <code>0</code>
	 * @param image the image to display in the splash window.
	 */
	public SplashWindow(ImageIcon image){
		this(image, 0);
	}

	/**
	 * Create a splash window displaying the image given in parameter and 
	 * set the attached progress bar maximum value to <code>intProgressMaxValue</code>
	 * @param image the image to display within the splash window.
	 * @param intProgressMaxValue the maximum value of the attached progress bar.
	 */
	public SplashWindow(ImageIcon image, int intProgressMaxValue){
		super();
		//initialise la valeur a laquelle le splash screen doit etre fermé

		// Ajoute le panneau d'etat
		this.state = new JLabel("");
		this.state.setForeground(Color.white);

		//ajoute la progress bar
		if (intProgressMaxValue > 0){
			this.progressBar = new JProgressBar(0, intProgressMaxValue);
			this.maxValue = intProgressMaxValue;
		} else {
			this.progressBar = new JProgressBar();
			this.progressBar.setIndeterminate(true);
			this.maxValue = 0;
		}


		this.southPanel = new JPanel();
		this.southPanel.setBackground(Color.black);
		this.southPanel.setLayout(new BorderLayout());
		this.southPanel.add(this.state,       BorderLayout.NORTH);
		this.southPanel.add(this.progressBar, BorderLayout.CENTER);


		getContentPane().add(this.southPanel, BorderLayout.SOUTH);

		// cree un label avec notre image
		JLabel label = new JLabel(image);
		// ajoute le label au panel
		getContentPane().add(label, BorderLayout.CENTER);
		pack(); 


		// centre le splash screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension labelSize = label.getPreferredSize();
		setLocation(screenSize.width / 2 - (labelSize.width / 2),
				screenSize.height / 2 - (labelSize.height / 2)); 

		// rend le splash screen invisible lorsque l'on clique dessus
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		}); 

		// affiche le splash screen
		setVisible(true); 
	}

	/**
	 * Set the maximum value of the progress bar attached to the splash windows.
	 * If the parameter <code>maxValue</code> is lesser than <code>0</code>, the progress bar is
	 * set to indeterminate.
	 * @param maxValue the maximum value.
	 */
	public void setProgressMaxValue(int maxValue){;

	if (maxValue > 0){
		this.progressBar.setMaximum(maxValue);
		this.progressBar.setIndeterminate(false);
	} else {
		this.progressBar.setMaximum(0);
		this.progressBar.setIndeterminate(true);
	}
	}

	/**
	 * Set the current value of the progress bar attached to the splash windows.
	 * @param value the current value of the progress bar.
	 */
	public void setProgressValue(int value) {
		this.progressBar.setValue(value);
		//si est arrivé a la valeur max : ferme le splash screen en lancant le thread
		if ((value >= this.maxValue) && (this.maxValue > 0)){
			try {
				SwingUtilities.invokeAndWait(this.closerRunner);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set the state of the splash windows, the state is the text displayed under the progress bar.
	 * @param text te text to display in the splash window.
	 */
	public void setState(String text){
		this.state.setText(text);
		this.state.repaint();
	}

	/**
	 * the thread that close the splash window.
	 */
	final Runnable closerRunner = new Runnable() {
		public void run() {
			setVisible(false);
			dispose();
		}
	};

}
