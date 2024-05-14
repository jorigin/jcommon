package org.jorigin.jfx.control;

import java.util.concurrent.CompletableFuture;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
/**
 * A mouse event filter that enable to filter intermediate mouse click events when multiple clicks occur. 
 * For example, This filter enables to produce only one MouseEvent when clicking consecutively on a button (instead of firing multiple events).
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 */
public class MouseClickFilter implements EventHandler<MouseEvent> {

	/** The delay in milliseconds (ms) between two clicks in order to consider them as consecutive. */
	private long clickDelay = 200;

	/** The handler that process the filtered event */
    private EventHandler<? super MouseEvent> handler = null;
	
	/** The last click time */
    private long lastClickTime = 0;
    
    /** The number of consecutive click count */
    private int consecutiveClickCount = 0;
    
    /** The mouse current event that will be returned */
    private MouseEvent currentEvent;
    
    /** The timer running flag. while this flag is <code>true</code>, the timer is running */
    private boolean timerRunning = true;
    
    /** The timer that trigger the attached event handler when delay is out.*/
    private Runnable timer;
    
    /**
     * Create a new mouse click filter that trigger the given handler when a click event is accepted.
     * @param handler the handler to trigger.
     */
    public MouseClickFilter(EventHandler<? super MouseEvent> handler) {
    	setMouseClickEventHandler(handler);
    }

    /**
     * Create a new mouse click filter that trigger the given handler when a click event is accepted.
     * @param delay the maximal delay in milliseconds (ms) between two clicks to consider them as consecutive. 
     * @param handler the handler to trigger.
     */
    public MouseClickFilter(long delay, EventHandler<? super MouseEvent> handler) {
    	setMouseClickEventHandler(handler);
    }
    
    /**
     * Get the handler that is attached to this filter. Only accepted event are sent to the handler.    
     * @return the handler that is attached to this filter
     * @see #setMouseClickEventHandler(EventHandler)
     */
    public EventHandler<? super MouseEvent> getMouseClickEventHandler() {
    	return this.handler;
    }
    
    /**
     * Set the handler to attach to this filter. Only accepted event are sent to the handler.    
     * @param handler the handler to attach
     */
    public void setMouseClickEventHandler(EventHandler<? super MouseEvent> handler) {
    	this.handler = handler;
    }
    
    /**
     * Get the maximal time between two mouse clicks in order to consider them as consecutive.
     * @return the maximal time between two mouse clicks in order to consider them as consecutive
     * @see #setClickDelay(long)
     */
    public long getClickDelay() {
    	return this.clickDelay;
    }
    
    /**
     * Set the maximal time between two mouse clicks in order to consider them as consecutive.
     * @param delay the maximal time between two mouse clicks in order to consider them as consecutive
     */
    public void setClickDelay(long delay) {
    	this.clickDelay = delay;
    }
    
    /**
     * Process a mouse event. When multiple events are processed in a flow, this filter will only keep single clicks or maximum consecutive click events.
     * @param e the mouse event to process.
     */
    @Override
    public void handle(MouseEvent e) {

				long currentClickTime = System.currentTimeMillis();
				
				if (this.consecutiveClickCount == 0) {

					this.timer = new Runnable() {

						@Override
						public void run() {
							
							while(MouseClickFilter.this.timerRunning) {

								MouseClickFilter.this.timerRunning = false;
								
								try {
									Thread.sleep(MouseClickFilter.this.clickDelay);
								} catch (InterruptedException e) {
								}
							}

							MouseClickFilter.this.handler.handle(MouseClickFilter.this.currentEvent);
							MouseClickFilter.this.consecutiveClickCount = 0;
						}			    	
				    };

				    this.consecutiveClickCount = 1;
				    this.timerRunning = true;
				    CompletableFuture.runAsync(this.timer);
				} else {
					// Multiple clicks suspected
					if (currentClickTime - this.lastClickTime <= this.clickDelay) {
						this.consecutiveClickCount += 1;
						this.timerRunning = true;
					}

				}

				this.currentEvent = e;
				
				this.lastClickTime = currentClickTime;
    }
    
}