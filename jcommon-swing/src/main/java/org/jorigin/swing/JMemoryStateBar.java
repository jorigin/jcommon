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


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;

import org.jorigin.Common;


/**
 * This class represents a simple widget dedicated to the monitoring of the available Java Runtime memory. 
 * The memory state bar is a {@link javax.swing.JPanel} standalone component that can be embedded into other components.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class JMemoryStateBar extends JPanel{

    /**
     * The Serial version UID.
     */
	private static final long serialVersionUID         = Common.BUILD;

	/**
	 * The command used to trigger garbage collecting.
	 */
	public static final String COMMAND_GARBAGE         = "commandGarbage";

	/**
	 * The command used to activate the memory state bar.
	 */
	public static final String COMMAND_ACTIVE          = "commandActive";

	/**
	 * The progress bar.
	 */
	private JProgressBar memoryStateBar                = null;

	/**
	 * The garbage collector activation button.
	 */
	private JButton memoryGarbageCollectorButton       = null;  

	/**
	 * The memory monotirong activation button.
	 */
	private JToggleButton memoryMonitorActivateButton  = null;

	/**
	 * The memory monitor worker.
	 */
	private SwingWorker<Object, Object> memoryMonitor  = null;

	/**
	 * The refresh delay in milliseconds (ms)
	 */
	private long refreshDelay                          = 500;

	/**
	 * The activation flag
	 */
	private boolean isActive = true;

	/**
	 * Construct a new default memory monitor
	 */
	public JMemoryStateBar(){
		super();  
		initGUI();
	}

	/**
	 * Init the GUI component of the memory monitor
	 */
	protected void initGUI(){  
		memoryStateBar = new JProgressBar();   
		memoryStateBar.setSize(new Dimension(100, 20));
		memoryStateBar.setPreferredSize(new Dimension(100, 20));
		memoryStateBar.setMinimum(0);
		memoryStateBar.setMaximum((int) (Runtime.getRuntime().totalMemory()/1024));
		memoryStateBar.setIndeterminate(false);
		memoryStateBar.setStringPainted(true);

		memoryGarbageCollectorButton = new JButton();
		memoryGarbageCollectorButton.setIcon(IconLoader.getIcon("icon/org/jorigin/swing/refresh-turquoise-24.png"));
		memoryGarbageCollectorButton.setPreferredSize(new Dimension(20,20));
		memoryGarbageCollectorButton.setEnabled(true);
		memoryGarbageCollectorButton.setToolTipText("Force unused memory free");
		memoryGarbageCollectorButton.setActionCommand(COMMAND_GARBAGE);
		memoryGarbageCollectorButton.setContentAreaFilled(true);
		memoryGarbageCollectorButton.setFocusPainted(true);
		memoryGarbageCollectorButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.gc();
			}
		});


		memoryMonitorActivateButton = new JToggleButton();
		memoryMonitorActivateButton.setSelectedIcon(IconLoader.getIcon("icon/org/jorigin/swing/lamp-green-24.png"));
		memoryMonitorActivateButton.setIcon(IconLoader.getIcon("icon/org/jorigin/swing/lamp-gray-24.png"));
		memoryMonitorActivateButton.setPreferredSize(new Dimension(20,20));
		memoryMonitorActivateButton.setEnabled(true);
		memoryMonitorActivateButton.setToolTipText("Desactivate the memory monitor");
		memoryMonitorActivateButton.setActionCommand(COMMAND_ACTIVE);
		memoryMonitorActivateButton.setContentAreaFilled(true);
		memoryMonitorActivateButton.setFocusPainted(true);
		memoryMonitorActivateButton.setSelected(true);

		memoryMonitorActivateButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {

				if (memoryMonitorActivateButton.isSelected()){ 
					startMonitor();
				} else { 
					stopMonitor();
				}

			}
		});


		this.setLayout(new FlowLayout());
		((FlowLayout)this.getLayout()).setAlignment(FlowLayout.LEFT);
		((FlowLayout)this.getLayout()).setHgap(1);
		((FlowLayout)this.getLayout()).setVgap(1);

		this.add(memoryStateBar);
		this.add(memoryGarbageCollectorButton);
		this.add(memoryMonitorActivateButton);


		startMonitor();
	}

	/**
	 * Start the monitoring of the Java Runtime memory. The components of the state bar are updated by a thread at 
	 * the frequency given by the refresh delay.
	 * @see #stopMonitor()
	 * @see #setRefreshDelay(long)
	 * @see #getRefreshDelay()
	 */
	public void startMonitor(){
		memoryStateBar.setIndeterminate(false);
		memoryStateBar.setEnabled(true);
		isActive = true;

		memoryMonitorActivateButton.setToolTipText("Desactivate the memory monitor");

		final JProgressBar stateBar = memoryStateBar;

		if ((memoryMonitor == null) || (memoryMonitor.isDone())) {
			memoryMonitor = new SwingWorker<Object, Object>(){

				int occupedMemory  = 0;
				int totalMemory = 0;


				@Override
				public Object doInBackground() {  
					isActive = true;
					while(isActive){
						totalMemory    = (int) (Runtime.getRuntime().totalMemory()/1048576);
						occupedMemory  = (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1048576);
						stateBar.setMaximum(totalMemory*1024);
						stateBar.setValue(occupedMemory*1024);
						stateBar.setString(""+occupedMemory+" Mo / "+totalMemory+" Mo");

						try {
							Thread.sleep(refreshDelay);
						} catch (InterruptedException ex) {
							isActive = true;
						}
					}  

					return null;
				}
			};

			memoryMonitor.execute();
		}
	}

	/**
	 * Stop the monitoring of the Java Runtime memory. The monitoring thread is sopped and the components are no more updated.
	 * @see #startMonitor()
	 */
	public void stopMonitor(){
		isActive = false;

		memoryStateBar.setIndeterminate(true);
		memoryStateBar.setEnabled(false);
		memoryStateBar.setString("Inactive");
		memoryMonitorActivateButton.setToolTipText("Activate the memory monitor");

	}

	/**
	 * Get the delay of time in milliseconds between two refreshes of the memory state bar.
	 * @param delay the delay of time.
	 * @see #getRefreshDelay()
	 */
	public void setRefreshDelay(long delay){
		this.refreshDelay = delay;   
	}

	/**
	 * Set the delay of time in milliseconds between two refreshes of the memory state bar.
	 * @return the delay of time.
	 * @see #getRefreshDelay()
	 */
	public long getRefreshDelay(){
		return refreshDelay;
	}

	/**
	 * Get the icon displayed on the garbage button.
	 * @return the icon used on the garbage button.
	 */
	public Icon getGarbageIcon(){
		return memoryGarbageCollectorButton.getIcon();
	}

	/**
	 * Set the icon displayed on the garbage button.
	 * @param icon the icon used on the garbage button.
	 */
	public void setGarbageIcon(Icon icon){
		memoryGarbageCollectorButton.setIcon(icon);
	}

	/**
	 * Get the active icon.
	 * @return the active icon.
	 */
	public Icon getActiveIcon(){
		return memoryMonitorActivateButton.getIcon();
	}

	/**
	 * Set the active icon.
	 * @param icon the active icon
	 */
	public void setActiveIcon(Icon icon){
		memoryMonitorActivateButton.setIcon(icon);
	}

	/**
	 * Get the inactive icon
	 * @return the inactive icon
	 */
	public Icon getInactiveIcon(){
		return memoryMonitorActivateButton.getSelectedIcon();
	}

	/**
	 * Set the inactive icon
	 * @param icon the inactive icon
	 */
	public void setInactiveIcon(Icon icon){
		memoryMonitorActivateButton.setSelectedIcon(icon);
	}
}