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

/**
 * This interface represents a plugin manager listener.
 * @author Julien Seinturier
 * @version 1.1
 */
public interface PluginManagerListener {

  /**
   * This method is called when the initialization task for all registered plugins starts.
   * @param pluginCount the number of plugin to initialize
   */
  public void initStarted(int pluginCount);
  
  /**
   * This method is called when a progress is made during the initialization of all registered plugins. The default
   * behavior is to call this method after each plugin initialization during the initialization task.
   * @param plugin the initialized plugin
   * @param percentDone the percent done for the whole initialization task.
   */
  public void initProgress(IPlugin plugin, float percentDone);
  
  /**
   * This method is called if the initialization task is aborted and is not complete.
   * @param plugin the plugin processed when the abort has been done
   * @param percentDone the percent of the initialization task done when the abort occurred.
   */
  public void initAborted(IPlugin plugin, float percentDone); 
  
  /**
   * This method is called if an error has occurred during the initialization task. The error may or may not 
   * be critical and so the task can continue or can be aborted. 
   * @param plugin the plugin that produced an error.
   * @param percentDone the percent done for the whole initialization task.
   */
  public void initError(IPlugin plugin, float percentDone);
  
  /**
   * This method is called when the plugins initialization task is completed. The default behavior for this method
   * call is to provide the currently initialized plugin count. If plugins have not been initialized during the
   * task, they should not be take in account. 
   * @param pluginCount the number of plugin initialized.
   */
  public void initComplete(int pluginCount);
  
  /**
   * This method is called when the start task for all registered plugins starts.
   * @param pluginCount the number of plugin to start
   */
  public void startStarted(int pluginCount);
  
  /**
   * This method is called when a progress is made during the start of all registered plugins. The default
   * behavior is to call this method after each plugin start during the initialization task.
   * @param plugin the started plugin
   * @param percentDone the percent done for the whole start task.
   */
  public void startProgress(IPlugin plugin, float percentDone);
  
  /**
   * This method is called if the start task is aborted and is not complete.
   * @param plugin the plugin processed when the abort has been done
   * @param percentDone the percent of the start task done when the abort occurred.
   */
  public void startAborted(IPlugin plugin, float percentDone);
  
  /**
   * This method is called when the plugins start task is completed. The default behavior for this method
   * call is to provide the currently started plugin count. If plugins have not been started during the
   * task, they should not be take in account. 
   * @param pluginCount the number of plugin started.
   */
  public void startComplete(int pluginCount);
  
  /**
   * This method is called when the stop task for all registered plugins starts.
   * @param pluginCount the number of plugin to stop
   */
  public void stopStarted(int pluginCount);
  
  /**
   * This method is called when a progress is made during the stop of all registered plugins. The default
   * behavior is to call this method after each plugin stop during the initialization task.
   * @param plugin the stopped plugin
   * @param percentDone the percent done for the whole stop task.
   */
  public void stopProgress(IPlugin plugin, float percentDone);
  
  /**
   * This method is called if the stop task is aborted and is not complete.
   * @param plugin the plugin processed when the abort has been done
   * @param percentDone the percent of the stop task done when the abort occurred.
   */
  public void stopAborted(IPlugin plugin, float percentDone);
  
  /**
   * This method is called when the plugins stop task is completed. The default behavior for this method
   * call is to provide the currently stopped plugin count. If plugins have not been stopped during the
   * task, they should not be take in account. 
   * @param pluginCount the number of plugin stopped.
   */
  public void stopComplete(int pluginCount);
  
  /**
   * This method is called when a plugin has been successfully registered by the plugin manager.
   * @param plugin the registered plugin.
   */
  public void pluginRegistered(IPlugin plugin);
  
  /**
   * This method is called when an error has occurred during the registration of a plugin 
   * by the plugin manager.
   * @param plugin the plugin that have caused the error.
   */
  public void pluginRegistrationError(IPlugin plugin);
  
  /**
   * This method is called when a plugin has been successfully initialized by the plugin manager.
   * @param plugin the registered plugin.
   */
  public void pluginInitialized(IPlugin plugin);
  
  /**
   * This method is called when an error has occurred during the initialization of a plugin 
   * by the plugin manager.
   * @param plugin the plugin that have caused the error.
   */
  public void pluginInitializationError(IPlugin plugin);
  
  /**
   * This method is called when a plugin has been successfully started by the plugin manager.
   * @param plugin the registered plugin.
   */
  public void pluginStarted(IPlugin plugin);
  
  /**
   * This method is called when an error has occurred during the start of a plugin 
   * by the plugin manager.
   * @param plugin the plugin that have caused the error.
   */
  public void pluginStartError(IPlugin plugin);
  
  /**
   * This method is called when a plugin has been successfully stopped by the plugin manager.
   * @param plugin the registered plugin.
   */
  public void pluginStopped(IPlugin plugin);
  
  /**
   * This method is called when an error has occurred during the stop of a plugin 
   * by the plugin manager.
   * @param plugin the plugin that have caused the error.
   */
  public void pluginStopError(IPlugin plugin);
}
