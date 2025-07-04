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
package org.jorigin.plugin;

import java.awt.AWTEvent;

import org.jorigin.Common;

/**
 * An event launched by the plugin toolkit. This event enables to monitor the {@link org.jorigin.plugin.PluginToolkit PluginToolkit} work.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class PluginToolkitEvent extends AWTEvent{

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = Common.BUILD;

	/**
	 * Flag used when the plugin discovery task has sarted.
	 */
	public static final int PLUGIN_DISCOVERING_START     = AWTEvent.RESERVED_ID_MAX + 1;

	/**
	 * Flag used when the plugin discovery task has finished.
	 */
	public static final int PLUGIN_DISCOVERING_FINISHED  = AWTEvent.RESERVED_ID_MAX + 2;

	/**
	 * Flag used when the plugin discovery task is working on an archive.
	 */
	public static final int PLUGIN_DISCOVERING_ARCHIVE   = AWTEvent.RESERVED_ID_MAX + 3;

	/**
	 * Flag used when the plugin discovery task is working on a directory.
	 */
	public static final int PLUGIN_DISCOVERING_DIR       = AWTEvent.RESERVED_ID_MAX + 4;

	/**
	 * Flag used when the plugin discovery task has found no plugin.
	 */
	public static final int PLUGIN_NO_DISCOVERY          = AWTEvent.RESERVED_ID_MAX + 5;

	/**
	 * Flag used when the plugin loading task starts.
	 */
	public static final int PLUGIN_LOADING_START         = AWTEvent.RESERVED_ID_MAX + 6;

	/**
	 * Flag used when the plugin loading task finishes.
	 */
	public static final int PLUGIN_LOADING_FINISHED      = AWTEvent.RESERVED_ID_MAX + 7;

	/**
	 * Flag used when the plugin loading task has loaded a plugin.
	 */
	public static final int PLUGIN_LOADING_LOADED        = AWTEvent.RESERVED_ID_MAX + 8;

	/**
	 * Flag used when the plugin loading task has encountred an error.
	 */
	public static final int PLUGIN_LOADING_ERROR         = AWTEvent.RESERVED_ID_MAX + 9;

	/**
	 * The message attached to the event
	 */
	private String message                               = null;

	/**
	 * The progress attached to the event
	 */
	private double progress                              = 0.0d;

	/**
	 * The task size attached to the event
	 */
	private double taskSize                              = 0.0d;

	/**
	 * The plugin attached to the event
	 */
	private IPlugin plugin                               = null;

	/**
	 * Create a new event with the source and identifier given in parameter
	 * @param source the source of the event (should be an instance of {@link org.jorigin.plugin.PluginToolkit PluginToolkit})
	 * @param id the identifier of the event. See the static descriptors of this class
	 */
	public PluginToolkitEvent(Object source, int id) {
		this(source, id, null);
	}

	/**
	 * Create a new event with the source and identifier given in parameter. A message is attached to the event.
	 * @param source the source of the event (should be an instance of {@link org.jorigin.plugin.PluginToolkit PluginToolkit})
	 * @param id the identifier of the event. See the static descriptors of this class
	 * @param msg the message associated to the event
	 */
	public PluginToolkitEvent(Object source, int id, String msg) {
		this(source, id, msg, -1.0d);
	}

	/**
	 * Create a new event with the source and identifier given in parameter. A message is attached to the event. <br>
	 * A progress is attached to the event, so if the event represents a task progress, the overall progress can be monitored.
	 * @param source the source of the event (should be an instance of {@link org.jorigin.plugin.PluginToolkit PluginToolkit})
	 * @param id the identifier of the event. See the static descriptors of this class
	 * @param msg the message associated to the event
	 * @param progress the progress associated to this event.
	 */
	public PluginToolkitEvent(Object source, int id, String msg, double progress) {
		super(source, id);
		this.message  = ""+msg;
		this.progress = progress;
	}

	/**
	 * Create a new event with the source and identifier given in parameter. A message is attached to the event. <br>
	 * A progress is attached to the event, so if the event represents a task progress, the overall progress can be monitored.
	 * The overall task size is given by the parameter <code>taskSize</code>.
	 * @param source the source of the event (should be an instance of {@link org.jorigin.plugin.PluginToolkit PluginToolkit})
	 * @param id the identifier of the event. See the static descriptors of this class
	 * @param msg the message associated to the event
	 * @param progress the progress associated to this event.
	 * @param taskSize the total size of the task which this event is attached.
	 */
	public PluginToolkitEvent(Object source, int id, String msg, double progress, double taskSize) {
		super(source, id);
		this.message  = ""+msg;
		this.progress = progress;
		this.taskSize = taskSize;
	}

	/**
	 * Create a new event with the source and identifier given in parameter. The plugin thats lead to this event to be fired is given in parameter. <br>
	 * A progress is attached to the event, so if the event represents a task progress, the overall progress can be monitored.
	 * @param source the source of the event (should be an instance of {@link org.jorigin.plugin.PluginToolkit PluginToolkit})
	 * @param id the identifier of the event. See the static descriptors of this class
	 * @param plugin the plugin attached to this event
	 * @param progress the progress associated to this event.
	 */
	public PluginToolkitEvent(Object source, int id, IPlugin plugin, double progress) {
		super(source, id);
		this.plugin  = plugin;
		this.progress = progress;
	}

	/**
	 * Create a new event with the source and identifier given in parameter. 
	 * The plugin thats lead to this event to be fired is given in parameter. <br>
	 * A progress is attached to the event, so if the event represents a task progress, 
	 * the overall progress can be monitored. The overall task size is given by the parameter <code>taskSize</code>
	 * @param source the source of the event (should be an instance of {@link org.jorigin.plugin.PluginToolkit PluginToolkit})
	 * @param id the identifier of the event. See the static descriptors of this class
	 * @param plugin the plugin attached to this event
	 * @param progress the progress associated to this event.
	 * @param taskSize the total size of the task which this event is attached.
	 */
	public PluginToolkitEvent(Object source, int id, IPlugin plugin, double progress, double taskSize) {
		super(source, id);
		this.plugin  = plugin;
		this.progress = progress;
		this.taskSize = taskSize;
	}

	/**
	 * Get the message attached to this event.
	 * @return the message attached to this event.
	 */
	public String getMessage(){
		return this.message;
	}

	/**
	 * Get the progress attached to this event.
	 * @return the message attached to this event.
	 */
	public double getProgress(){
		return this.progress;
	}

	/**
	 * Get the plugin attached o this event.
	 * @return the plugin attached o this event.
	 */
	public IPlugin getPlugin(){
		return this.plugin;
	}

	/**
	 * Get the size of the task within this event has been fired.
	 * @return the size of the task within this event has been fired.
	 */
	public double getTaskSize(){
		return this.taskSize;
	}
}
