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

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * A test case for Activity monitor.
 * @author Julien Seinturier - (c) 2010 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
 *
 */
public class ActivityMonitorTest {


  /**
   * The test main method.
   * @param args the command line arguments.
   */
  public static void main(String[] args){
    
    int taskSize = 60;
    
    JFrame frame = new JFrame();
    
    frame.setSize(new Dimension(320, 200));
    frame.setPreferredSize(new Dimension(320, 200));
    frame.setTitle("JOrigin common: Activity monitor test");
    
    ActivityMonitor am = new ActivityMonitor(frame);
    
    frame.setVisible(true);
    
    am.processTaskEvent(new TaskEvent(frame, TaskEvent.TASK_STARTED, "TASK1", "TASK1: Start", taskSize));
    
    for(int i = 0; i < taskSize; i++){
      am.processTaskEvent(new TaskEvent(frame, TaskEvent.TASK_PROGRESS, "TASK1", "TASK1: Progress "+i, i));
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
    
    
    am.processTaskEvent(new TaskEvent(frame, TaskEvent.TASK_STARTED, "TASK1", "TASK1: Finished"));
    
    System.exit(0);
  }
}