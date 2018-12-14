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
package org.jorigin.task;

import java.awt.AWTEvent;

/**
 * An event that describes the life cycle of a task.
 * @author Julien Seinturier - (c) 2010 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
 *
 */
public class TaskEvent extends AWTEvent{

  private static final long serialVersionUID = 1L;
  
  /**
   * The task has started.
   */
  public static final int TASK_STARTED     = AWTEvent.RESERVED_ID_MAX*2;
  
  /**
   * The task has progressed.
   */
  public static final int TASK_PROGRESS    = TASK_STARTED*2;
  
  /**
   * The task is suspended.
   */
  public static final int TASK_SUSPENDED   = TASK_PROGRESS*2;
  
  /**
   * The task has finished.
   */
  public static final int TASK_FINISHED    = TASK_SUSPENDED*2;
  
  /**
   * The task has produced a warning.
   */
  public static final int TASK_WARNING     = TASK_FINISHED*2;
  
  /**
   * The task has produced an error.
   */
  public static final int TASK_ERROR       = TASK_WARNING*2;
  
  /**
   * The task has produces an information.
   */
  public static final int TASK_INFO        = TASK_ERROR*2;

  
  /**
   * The name of the task which this event is attached
   */
  private String taskName = null;

  /**
   * The description of the current task
   */
  private String taskDescription = null;

  /**
   * The size of the task (number of step of progress needed
   * to accomplish the task
   */
  private int size = -1;
  
  /**
   * Create a new task event.
   * @param source the source (task) of the event.
   * @param id the identifier of the task.
   */
  public TaskEvent(Object source, int id){
    super(source, id);
  }

  /**
   * Create a new task event.
   * @param event the original event to copy.
   */
  public TaskEvent(AWTEvent event){
    super(event.getSource(), event.getID());
  }

  /**
   * Create a new task event.
   * @param source the source (task) of the event.
   * @param id the identifier of the task.
   * @param name the name of the task.
   */
  public TaskEvent(Object source, int id, String name){
    this(source, id, name, null, -1);
  }

  /**
   * Create a new task event.
   * @param source the source (task) of the event.
   * @param id the identifier of the task.
   * @param name the name of the task.
   * @param description the description of the event.
   */
  public TaskEvent(Object source, int id, String name, String description){
    this(source, id, name, description, -1);
  }
     
  /**
   * Create a new task event.
   * @param source the source (task) of the event.
   * @param id the identifier of the task.
   * @param size the size of the event in case of progress monitoring. If the event is {@link #TASK_STARTED}, the size is the size of the task. If the event is {@link #TASK_PROGRESS}, the size is the actually accomplished part of the task.
   */
  public TaskEvent(Object source, int id, int size){
    this(source, id, null, null, size);    
  }
  
  /**
   * Create a new task event.
   * @param source the source (task) of the event.
   * @param id the identifier of the task.
   * @param name the name of the task.
   * @param size the size of the event in case of progress monitoring. If the event is {@link #TASK_STARTED}, the size is the size of the task. If the event is {@link #TASK_PROGRESS}, the size is the actually accomplished part of the task.
   */
  public TaskEvent(Object source, int id, String name, int size){
    this(source, id, name, null, size);    
  }
  
  /**
   * Create a new task event.
   * @param source the source (task) of the event.
   * @param id the identifier of the task.
   * @param name the name of the task.
   * @param description the description of the event.
   * @param size the size of the event in case of progress monitoring. If the event is {@link #TASK_STARTED}, the size is the size of the task. If the event is {@link #TASK_PROGRESS}, the size is the actually accomplished part of the task.
   */
  public TaskEvent(Object source, int id, String name, String description, int size){
    this(source, id);
    this.taskName = name;
    this.taskDescription = description;
    this.size = size;
  }
  
   
  /**
   * Get the name of the task attached to this event
   * @return String the name of the task attached to this event
   */
  public String getTaskName(){
    return this.taskName;
  }

  /**
   * Get the description of the task
   * @return String the description of the task
   */
  public String getDescription(){
    return this.taskDescription;
  }
  
  /**
   * Get the size of the task.
   * @return int the size of the task
   */
  public int getSize(){
    return this.size;  
  }
}