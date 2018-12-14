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
package org.jorigin.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import org.jorigin.Common;
import org.jorigin.lang.LangResourceBundle;

/**
 * A Simple component used to handle date display and modification.
 * @author Julien Seinturier - (c) 2009 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
 */
public class JDatePanel extends JPanel {
  
  private static final long serialVersionUID = Common.BUILD;

  private LangResourceBundle langResource    = (LangResourceBundle) LangResourceBundle.getBundle(new Locale(System.getProperty("user.language"), 
                                                                                                            System.getProperty("user.country")));

  /**
   * The max year useable in the date panel
   */
  private static int MAX_YEAR = 9999;
  
  /**
   * JANUARY month. This variable is a convenience variable that 
   * match the java.util.Calendar.JANUARY variable.
   */
  public static final int JANUARY   = Calendar.JANUARY;
  
  /**
   * FEBRUARY month. This variable is a convenience variable that 
   * match the java.util.Calendar.FEBRUARY variable.
   */
  public static final int FEBRUARY  = Calendar.FEBRUARY;
  
  /**
   * MARCH month. This variable is a convenience variable that 
   * match the java.util.Calendar.MARCH variable.
   */
  public static final int MARCH     = Calendar.MARCH;
  
  /**
   * APRIL month. This variable is a convenience variable that 
   * match the java.util.Calendar.APRIL variable.
   */
  public static final int APRIL     = Calendar.APRIL;
  
  /**
   * MAY month. This variable is a convenience variable that 
   * match the java.util.Calendar.MAY variable.
   */
  public static final int MAY       = Calendar.MAY;
  
  /**
   * JUNE month. This variable is a convenience variable that 
   * match the java.util.Calendar.JUNE variable.
   */
  public static final int JUNE      = Calendar.JUNE;
  
  /**
   * JULY month. This variable is a convenience variable that 
   * match the java.util.Calendar.JULY variable.
   */
  public static final int JULY      = Calendar.JULY;
  
  /**
   * AUGUST month. This variable is a convenience variable that 
   * match the java.util.Calendar.AUGUST variable.
   */
  public static final int AUGUST    = Calendar.AUGUST;
  
  /**
   * SEPTEMBER month. This variable is a convenience variable that 
   * match the java.util.Calendar.SEPTEMBER variable.
   */
  public static final int SEPTEMBER = Calendar.SEPTEMBER;
  
  /**
   * OCTOBER month. This variable is a convenience variable that 
   * match the java.util.Calendar.OCTOBER variable.
   */
  public static final int OCTOBER   = Calendar.OCTOBER;
  
  /**
   * NOVEMBER month. This variable is a convenience variable that 
   * match the java.util.Calendar.NOVEMBER variable.
   */
  public static final int NOVEMBER  = Calendar.NOVEMBER;
  
  /**
   * DECEMBER month. This variable is a convenience variable that 
   * match the java.util.Calendar.DECEMBER variable.
   */
  public static final int DECEMBER  = Calendar.DECEMBER;
  
  private GregorianCalendar calendar = null;
  
  //Liste des écouteurs informés des evenements du panneau
  protected EventListenerList idListenerList = new EventListenerList();
  
  // Composants graphiques utilisée par l'IHM
  private JLabel dayLB              = null;
  private JSpinner dayTF            = null;
  private SpinnerModel dayTFModel   = null;
  private JLabel monthLB            = null;
  private JComboBox<String> monthCB = null;
  private JLabel yearLB             = null;
  private JSpinner yearTF           = null;
  private SpinnerModel yearTFModel  = null;
  
  private JSpinner hourMinuteTF     = null;
  
  private boolean showLabel         = false;
  
  private boolean listening         = true;
  
  /**
   * Construct a new default date panel. The date is set by default 
   * to the current date
   */
  public JDatePanel(){
    this(new Date());
  }
  
  /**
   * Construct a new date panel attached to the given date.
   * @param date the date to edit within the panel.
   */
  public JDatePanel(Date date){
    super();
    calendar = new GregorianCalendar();
    initGUI();
    setDate(date);
  }
  
  /**
   * Init the GUI
   */
  protected void initGUI(){
    
    // Initialisation des composants du jour
    dayLB      = new JLabel(langResource.getString("DATE_DAY"));
    dayTFModel = new SpinnerNumberModel(calendar.get(Calendar.DAY_OF_MONTH), 1, 
                                    getMaxDay(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)), 
                                    1);  
    dayTF      = new JSpinner(dayTFModel);
    dayTF.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
    
    if (listening){
      int day = Integer.parseInt(""+dayTF.getValue());
      setDay(day);
    
      fireChangeEvent();
    }
      }});
    
    monthLB    = new JLabel(langResource.getString("DATE_MONTH"));
    monthCB    = new JComboBox<String>(new String[]{langResource.getString("DATE_JANUARY"),
        langResource.getString("DATE_FEBRUARY"),
        langResource.getString("DATE_MARCH"),
        langResource.getString("DATE_APRIL"),
        langResource.getString("DATE_MAY"),
        langResource.getString("DATE_JUNE"),
        langResource.getString("DATE_JULY"),
        langResource.getString("DATE_AUGUST"),
        langResource.getString("DATE_SEPTEMBER"),
        langResource.getString("DATE_OCTOBER"),
        langResource.getString("DATE_NOVEMBER"),
        langResource.getString("DATE_DECEMBER")});
    monthCB.addItemListener(new ItemListener(){

      @Override
      public void itemStateChanged(ItemEvent e) {
    
    if (listening){
    
      int month = monthCB.getSelectedIndex();
    
      setMonth(month);
    
      fireChangeEvent();
    }
      }});
    
    
    yearLB      = new JLabel(langResource.getString("DATE_YEAR"));
    yearTFModel = new SpinnerNumberModel(calendar.get(Calendar.YEAR), 1, MAX_YEAR, 1);  
    yearTF      = new JSpinner(yearTFModel);
    yearTF.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
    
    if (listening){
      int year = Integer.parseInt(""+yearTF.getValue());
    
      setYear(year);
    
      fireChangeEvent();
    }
      }});
    

    Calendar firstDate = new GregorianCalendar();
    firstDate.setTime(new Date());
    firstDate.set(Calendar.HOUR, 0);
    firstDate.set(Calendar.MINUTE, 0);
    
    Calendar lastDate = new GregorianCalendar();
    lastDate.setTime(new Date());
    lastDate.set(Calendar.HOUR, 23);
    lastDate.set(Calendar.MINUTE, 59);

    final SpinnerDateModel model = new SpinnerDateModel(calendar.getTime(),
                             firstDate.getTime(),
                             lastDate.getTime(),
                                 Calendar.MINUTE);

    hourMinuteTF = new JSpinner(model);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(hourMinuteTF, "hh:mm");
    hourMinuteTF.setEditor(editor);
    hourMinuteTF.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
    
    if (listening){
      Calendar  cal = new GregorianCalendar();
      cal.setTime(model.getDate());
      
      int hour   = cal.get(Calendar.HOUR_OF_DAY);
      int minute = cal.get(Calendar.MINUTE);
 
      setHour(hour);
      setMinute(minute);
    
      cal = null;
      fireChangeEvent();
    }
      }});
    
    this.setLayout(new GridBagLayout());
    
    GridBagConstraints c = null;
    Insets labelInsets   = new Insets(0,8,0,0);
    Insets fieldInsets   = new Insets(0,5,0,0);
    Insets currentInsets = null;
    
    if (showLabel){
    
      c           = new GridBagConstraints ();
      c.gridx     = 0;
      c.gridy     = 0;
      c.gridheight= 1;
      c.gridwidth = 1;
      c.fill      = GridBagConstraints.NONE;
      c.insets    = labelInsets;
      c.weightx   = 0.0;
      c.weighty   = 0;
      this.add(dayLB, c);
      
      currentInsets = fieldInsets;
    } else {
      currentInsets = new Insets(0,0,0,0);
    }
    
    c           = new GridBagConstraints ();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = currentInsets;
    c.weightx   = 1.0;
    c.weighty   = 0;
    this.add(dayTF, c); 
    
    if (showLabel){
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = 1;
      c.fill      = GridBagConstraints.NONE;
      c.insets    = labelInsets;
      c.weightx   = 0.0;
      c.weighty   = 0;
      this.add(monthLB, c);
    }
    
    c           = new GridBagConstraints ();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0;
    this.add(monthCB, c);   
    
    if (showLabel){
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = 1;
      c.fill      = GridBagConstraints.NONE;
      c.insets    = labelInsets;
      c.weightx   = 0.0;
      c.weighty   = 0;
      this.add(yearLB, c);
    }
    
    c           = new GridBagConstraints ();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = 1;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0;
    this.add(yearTF, c);
    
    c           = new GridBagConstraints ();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0;
    this.add(hourMinuteTF, c);
    
    c             = null;
    labelInsets   = new Insets(0,8,0,0);
    fieldInsets   = new Insets(0,5,0,0);
    currentInsets = null;
  }
  
  /**
   * Update the panel graphical components. The components shows the current date.
   * @see #setDate(Date)
   * @see #getDate()
   */
  protected void updateGUI(){
    
    listening = false;
    
    dayTF.setValue(calendar.get(Calendar.DAY_OF_MONTH));
    
    monthCB.setSelectedIndex(calendar.get(Calendar.MONTH));
    monthCB.setSelectedItem(monthCB.getSelectedItem());
    
    yearTF.setValue(calendar.get(Calendar.YEAR));
    
    hourMinuteTF.setValue(calendar.getTime());
    
    listening = true;
  }
  
  /**
   * Get the date attached to this panel
   * @return the java object representing the date attached to this panel.
   * @see #setDate(Date)
   */
  public Date getDate(){
    return calendar.getTime();
  }
  
  /**
   * Set the date attached to this panel
   * @param newDate the java object representing the date to attach to this panel.
   * @see #getDate()
   */
  public void setDate(Date newDate){
    if (newDate != null){
      listening = false;
      calendar.setTime(newDate);
      updateGUI();
      listening = true;
    }
  }
  
  /**
   * Get the minute of the hour (0 - 59) specified by the current date
   * @return the minute in the hour.
   * @see #setMinute(int)
   */
  public int getMinute(){
    return calendar.get(Calendar.MINUTE);
  }
  
  /**
   * Set the minute of the hour (0 - 59) specified by the current date
   * @param minute the minute in the hour.
   * @see #getMinute()
   */
  public void setMinute(int minute){
    if ((minute < 0) || (minute > 59)){
      return;
    } else {
      calendar.set(Calendar.MINUTE, minute);
      updateGUI();
    }
  }
  
  /**
   * Get the hour of the date
   * @return the hour of the day (0 - 24)
   * @see #setHour(int)
   */
  public int getHour(){
    return calendar.get(Calendar.HOUR_OF_DAY);
  }
  
  /**
   * Set the hour of the date.
   * @param hour the hour of the day (0 - 24)
   * @see #getHour()
   */
  public void setHour(int hour){
    if ((hour < 0) || (hour > 23)){
      return;
    } else {
      calendar.set(Calendar.HOUR_OF_DAY, hour);
      updateGUI();
    }
  }
  
  /**
   * Get the month of the date.
   * @return the month of the date as stated in Calendar class
   * @see #setMonth(int)
   */
  public int getMonth(){
    return calendar.get(Calendar.MONTH);
  }
  
  /**
   * Set the month of the date. The month is given by the variable available in the class.
   * If the day is not compatible with the given month, day is changed to match the new month.
   * @param month the month.
   * @see #getMonth()
   */
  public void setMonth(int month){
    
    if ((month < 1) || (month > 12)){
      return;
    } else {
      calendar.set(Calendar.MONTH, month);
      
      int maxDay = getMaxDay(month, calendar.get(Calendar.YEAR));
      
      if (calendar.get(Calendar.DAY_OF_MONTH) > maxDay){
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
      }
      
      updateGUI();
    }
  }
  
  /**
   * Get the day of the date. The day number if the day in the month.
   * @return int the day of the date (month relative).
   * @see #setDay(int)
   */
  public int getDay(){
    return calendar.get(Calendar.DAY_OF_MONTH);
  }
  
  /**
   * Set the day of the date. If the day does not fit the month and the year, 
   * it is changed to fit.
   * @param day the day of the date
   * @see #getDay()
   */
  public void setDay(int day){

    int maxDay = getMaxDay(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

    if (!((day < 1) || (day > maxDay))){
      calendar.set(Calendar.DAY_OF_MONTH, day);
      updateGUI();
    }
  }
  
  /**
   * Return the year of the date.
   * @return the year of the date.
   * @see #setYear(int)
   */
  public int getYear(){
    return calendar.get(Calendar.YEAR);
  }
  
  /**
   * Set the year of the date. A year has to be positive.
   * @param year the year of the date.
   * @see #getYear()
   */
  public void setYear(int year){
    
    if (year > 0){
      
      calendar.set(Calendar.YEAR, year);
      
      int maxDay = getMaxDay(calendar.get(Calendar.MONTH), year);
      
      // Verification que si l'année est bisextile est le mois actuel est fevrier,
      // le jour ne doit pas être superieur à 29.
      if (calendar.get(Calendar.DAY_OF_MONTH) > maxDay){
    calendar.set(Calendar.DAY_OF_MONTH, maxDay);
      }
      updateGUI();
    }
  }
  
  /**
   * Get the maximal day number for the given month in the given year 
   * (year is needed for the computation of bisextile years).
   * @param month the month of the date
   * @param year the year of the date
   * @return the maximal day number in the month.
   */
  private int getMaxDay(int month, int year){
    switch(month){
      case JANUARY:
      case MARCH:
      case MAY:
      case JULY:
      case AUGUST:
      case OCTOBER:
      case DECEMBER:
    return 31;
        
      case APRIL:
      case JUNE:
      case SEPTEMBER:
      case NOVEMBER:
    return 30;
    
      case FEBRUARY:
    if (isBisextile(year)){
      return 29;
    } else {
      return 28;
    }
    
      default: return -1;
    }
  }
  
  /**
   * Compute if the given year if a bisextile year. A year is bisextile if it can be 
   * divided by 400 or if it can be divided by 4 but not by 100 at the same time. 
   * @param year the year to check
   * @return <code>true</code> if the year is bisextile, <code>false</code> otherwise.
   */
  public boolean isBisextile(int year){
    if (year % 400 == 0){
      return true;
    } else if ((year % 4 == 0) && (year % 100 != 100)){
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Add a {@link javax.swing.event.ChangeListener} to this panel.
   * @param l the listener to attach to this panel
   * @see #removeChangeListener(ChangeListener)
   */
  public void addChangeListener(ChangeListener l){
    idListenerList.add(ChangeListener.class, l);
  }
  
  /**
   * Remove the given {@link javax.swing.event.ChangeListener} from the panel.
   * @param l the listener to detach from the panel.
   * @see #addChangeListener(ChangeListener)
   */
  public void removeChangeListener(ChangeListener l){
    idListenerList.remove(ChangeListener.class, l);
  }
  
  /**
   * Fire a change event to all the registered {@link javax.swing.event.ChangeListener}
   * @see #addChangeListener(ChangeListener)
   * @see #removeChangeListener(ChangeListener)
   */
  protected void fireChangeEvent(){
    
    ChangeEvent e = new ChangeEvent(this);
    
    Object[] listeners = idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ChangeListener.class) {
        ( (ChangeListener) listeners[i + 1]).stateChanged(e);
      }
    }
  }
}

