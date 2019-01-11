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
package org.jorigin.event;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;

import javax.swing.event.EventListenerList;

import org.jorigin.Common;

/**
 * Event pool class enable to share events between many components. The event pool register listeners
 * and dispatch event. Components which have to fire event to other use the event pool to propagate it.<br>
 * This class use {@link java.awt.AWTEvent} as a global event. So All event that can be fired on the event pool must be
 * an {@link java.awt.AWTEvent} or a subclass.
 * @author Julien Seinturier - COMEX S.A. - <a href="mailto:contact@jorigin.org">contact@jorigin.org</a> - <a href="https://github.com/jorigin/jeometry">https://github.com/jorigin/jeometry</a>
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class EventPool {

  
  //Liste des écouteurs informés des evenements du panneau
  /**
   * The list of listenners attached to the pool
   */
  protected EventListenerList idListenerList = new EventListenerList();
  
  /**
   * Construct a new event pool. This method creates the listener list.
   */
  public EventPool(){
    idListenerList = new EventListenerList(); 
  }
  
  /**
   * Add a listener to the event pool. The listener must implements the interface
   * {@link java.awt.event.AWTEventListener} because the interface {@link java.util.EventListener}
   * is empty and does not determine a dispatch method.
   * @param listener the listener to add.
   */
  public void addListener(AWTEventListener listener){
    idListenerList.add(AWTEventListener.class, listener);    
  }

  /**
   * Remove a listener from the event pool. The listener must implements interface
   * {@link java.awt.event.AWTEventListener} because the interface {@link java.util.EventListener}
   * is empty and does not determine a dispatch method.
   * @param listener the listener to remove.
   */
  public void removeListener(AWTEventListener listener){
    idListenerList.remove(AWTEventListener.class, listener);   
  }
  
  /**
   * Dispatch a new event to all registered listeners. The event must be a subclass of 
   * {@link java.awt.AWTEvent} because the listener are AWTEventListener.
   * @param event the event to dispatch.
   */
  public void dispatchEvent(AWTEvent event){
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == AWTEventListener.class) {
        ( (AWTEventListener) listeners[i + 1]).eventDispatched(event);
      }
    }  
  }
}