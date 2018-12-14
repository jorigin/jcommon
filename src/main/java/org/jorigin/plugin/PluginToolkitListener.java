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

import java.awt.event.AWTEventListener;

/**
 * The <code>PluginToolkitListener</code> is a convenience interface that wrap an <code>AWTEventListener</code>. This listener can be 
 * used for the capture of {@link org.jorigin.plugin.PluginToolkitEvent PluginToolkitEvent} 
 * fired by the {@link org.jorigin.plugin.PluginToolkit PluginToolkit}.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
 */
public interface PluginToolkitListener extends AWTEventListener{

}
