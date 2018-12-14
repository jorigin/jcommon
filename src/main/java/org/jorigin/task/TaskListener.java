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

import java.util.EventListener;

/**
 * <p>Title: Task Event</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Julien Seinturier
 * @version 1.0
 */
public interface TaskListener extends EventListener{

  /**
   * Dispatch a {@link org.jorigin.task.TaskEvent TaskEvent} in the listener
   * @param event the task event dispatched
   */
  public void eventDispatched(TaskEvent event);

}
