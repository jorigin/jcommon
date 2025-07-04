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
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 * @since 1.0.0
 */
public class JDatePanel extends JPanel {
  
  /**
   * Serial version UID for serialization.
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = Common.BUILD;

  /**
   * The resource bundle used to get the localized strings.
   * This resource bundle is used to get the localized strings for the date panel.
   * The resource bundle is loaded from the system locale.
   */
  private LangResourceBundle langResource    = (LangResourceBundle) LangResourceBundle.getBundle(Locale.getDefault());
  
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
  
  /**
   * The calendar used to store the date.
   * This calendar is used to store the date and to compute the day, month, year, hour and minute of the date.
   */
  private GregorianCalendar calendar = null;
  
  /**
   * The attached listeners.
   */
  protected EventListenerList idListenerList = new EventListenerList();
  
  /**
   * The label and text field used to edit the date.
   * The date is composed of a day, a month, a year and an hour/minute.
   * The day is edited with a JSpinner, the month with a JComboBox and the year with a JSpinner.
   * The hour and minute are edited with a JSpinner with a DateEditor.
   */
  private JLabel dayLB              = null;
  
  /**
   * The text field used to edit the day of the date.
   * The day is edited with a JSpinner.
   */
  private JSpinner dayTF            = null;
  
  /**
   * The model used to edit the day of the date.
   * The model is a SpinnerNumberModel that allows to edit the day of the date.
   */
  private SpinnerModel dayTFModel   = null;
  
  /**
   * The label and text field used to edit the month of the date.
   * The month is edited with a JComboBox.
   */
  private JLabel monthLB            = null;
  
  /**
   * The combo box used to edit the month of the date.
   * The month is edited with a JComboBox.
   */
  private JComboBox<String> monthCB = null;
  
  /**
   * The label and text field used to edit the year of the date.
   * The year is edited with a JSpinner.
   */
  private JLabel yearLB             = null;
  
  /**
   * The text field used to edit the year of the date.
   * The year is edited with a JSpinner.
   */
  private JSpinner yearTF           = null;
  
  /**
   * The model used to edit the year of the date.
   * The model is a SpinnerNumberModel that allows to edit the year of the date.
   */
  private SpinnerModel yearTFModel  = null;
  
  /**
   * The text field used to edit the hour and minute of the date.
   * The hour and minute are edited with a JSpinner with a DateEditor.
   */
  private JSpinner hourMinuteTF     = null;
  
  
  /**
   * Show the label in the panel or not.
   * If <code>true</code>, the label is shown, if <code>false</code>, the label is not shown.
   */
  private boolean showLabel         = false;
  
  /**
   * Listening to the changes of the components or not.
   * If <code>true</code>, the components are listening to the changes, if <code>false</code>, the components are not listening to the changes.
   * This is used to avoid infinite loops when setting the date.
   */
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
    this.calendar = new GregorianCalendar();
    initGUI();
    setDate(date);
  }
  
  /**
   * Init the GUI
   */
  protected void initGUI(){
    
    // Initialisation des composants du jour
    this.dayLB      = new JLabel(this.langResource.getString("DATE_DAY"));
    this.dayTFModel = new SpinnerNumberModel(this.calendar.get(Calendar.DAY_OF_MONTH), 1, 
                                    getMaxDay(this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.YEAR)), 
                                    1);  
    this.dayTF      = new JSpinner(this.dayTFModel);
    this.dayTF.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
    
    if (JDatePanel.this.listening){
      int day = Integer.parseInt(""+JDatePanel.this.dayTF.getValue());
      setDay(day);
    
      fireChangeEvent();
    }
      }});
    
    this.monthLB    = new JLabel(this.langResource.getString("DATE_MONTH"));
    this.monthCB    = new JComboBox<String>(new String[]{this.langResource.getString("DATE_JANUARY"),
        this.langResource.getString("DATE_FEBRUARY"),
        this.langResource.getString("DATE_MARCH"),
        this.langResource.getString("DATE_APRIL"),
        this.langResource.getString("DATE_MAY"),
        this.langResource.getString("DATE_JUNE"),
        this.langResource.getString("DATE_JULY"),
        this.langResource.getString("DATE_AUGUST"),
        this.langResource.getString("DATE_SEPTEMBER"),
        this.langResource.getString("DATE_OCTOBER"),
        this.langResource.getString("DATE_NOVEMBER"),
        this.langResource.getString("DATE_DECEMBER")});
    this.monthCB.addItemListener(new ItemListener(){

      @Override
      public void itemStateChanged(ItemEvent e) {
    
    if (JDatePanel.this.listening){
    
      int month = JDatePanel.this.monthCB.getSelectedIndex();
    
      setMonth(month);
    
      fireChangeEvent();
    }
      }});
    
    
    this.yearLB      = new JLabel(this.langResource.getString("DATE_YEAR"));
    this.yearTFModel = new SpinnerNumberModel(this.calendar.get(Calendar.YEAR), 1, MAX_YEAR, 1);  
    this.yearTF      = new JSpinner(this.yearTFModel);
    this.yearTF.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
    
    if (JDatePanel.this.listening){
      int year = Integer.parseInt(""+JDatePanel.this.yearTF.getValue());
    
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

    final SpinnerDateModel model = new SpinnerDateModel(this.calendar.getTime(),
                             firstDate.getTime(),
                             lastDate.getTime(),
                                 Calendar.MINUTE);

    this.hourMinuteTF = new JSpinner(model);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(this.hourMinuteTF, "hh:mm");
    this.hourMinuteTF.setEditor(editor);
    this.hourMinuteTF.addChangeListener(new ChangeListener(){

      @Override
      public void stateChanged(ChangeEvent e) {
    
    if (JDatePanel.this.listening){
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
    
    if (this.showLabel){
    
      c           = new GridBagConstraints ();
      c.gridx     = 0;
      c.gridy     = 0;
      c.gridheight= 1;
      c.gridwidth = 1;
      c.fill      = GridBagConstraints.NONE;
      c.insets    = labelInsets;
      c.weightx   = 0.0;
      c.weighty   = 0;
      this.add(this.dayLB, c);
      
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
    this.add(this.dayTF, c); 
    
    if (this.showLabel){
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = 1;
      c.fill      = GridBagConstraints.NONE;
      c.insets    = labelInsets;
      c.weightx   = 0.0;
      c.weighty   = 0;
      this.add(this.monthLB, c);
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
    this.add(this.monthCB, c);   
    
    if (this.showLabel){
      c           = new GridBagConstraints ();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = 1;
      c.fill      = GridBagConstraints.NONE;
      c.insets    = labelInsets;
      c.weightx   = 0.0;
      c.weighty   = 0;
      this.add(this.yearLB, c);
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
    this.add(this.yearTF, c);
    
    c           = new GridBagConstraints ();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.HORIZONTAL;
    c.insets    = fieldInsets;
    c.weightx   = 1.0;
    c.weighty   = 0;
    this.add(this.hourMinuteTF, c);
    
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
    
    this.listening = false;
    
    this.dayTF.setValue(this.calendar.get(Calendar.DAY_OF_MONTH));
    
    this.monthCB.setSelectedIndex(this.calendar.get(Calendar.MONTH));
    this.monthCB.setSelectedItem(this.monthCB.getSelectedItem());
    
    this.yearTF.setValue(this.calendar.get(Calendar.YEAR));
    
    this.hourMinuteTF.setValue(this.calendar.getTime());
    
    this.listening = true;
  }
  
  /**
   * Get the date attached to this panel
   * @return the java object representing the date attached to this panel.
   * @see #setDate(Date)
   */
  public Date getDate(){
    return this.calendar.getTime();
  }
  
  /**
   * Set the date attached to this panel
   * @param newDate the java object representing the date to attach to this panel.
   * @see #getDate()
   */
  public void setDate(Date newDate){
    if (newDate != null){
      this.listening = false;
      this.calendar.setTime(newDate);
      updateGUI();
      this.listening = true;
    }
  }
  
  /**
   * Get the minute of the hour (0 - 59) specified by the current date
   * @return the minute in the hour.
   * @see #setMinute(int)
   */
  public int getMinute(){
    return this.calendar.get(Calendar.MINUTE);
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
      this.calendar.set(Calendar.MINUTE, minute);
      updateGUI();
    }
  }
  
  /**
   * Get the hour of the date
   * @return the hour of the day (0 - 24)
   * @see #setHour(int)
   */
  public int getHour(){
    return this.calendar.get(Calendar.HOUR_OF_DAY);
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
      this.calendar.set(Calendar.HOUR_OF_DAY, hour);
      updateGUI();
    }
  }
  
  /**
   * Get the month of the date.
   * @return the month of the date as stated in Calendar class
   * @see #setMonth(int)
   */
  public int getMonth(){
    return this.calendar.get(Calendar.MONTH);
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
      this.calendar.set(Calendar.MONTH, month);
      
      int maxDay = getMaxDay(month, this.calendar.get(Calendar.YEAR));
      
      if (this.calendar.get(Calendar.DAY_OF_MONTH) > maxDay){
        this.calendar.set(Calendar.DAY_OF_MONTH, maxDay);
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
    return this.calendar.get(Calendar.DAY_OF_MONTH);
  }
  
  /**
   * Set the day of the date. If the day does not fit the month and the year, 
   * it is changed to fit.
   * @param day the day of the date
   * @see #getDay()
   */
  public void setDay(int day){

    int maxDay = getMaxDay(this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.YEAR));

    if (!((day < 1) || (day > maxDay))){
      this.calendar.set(Calendar.DAY_OF_MONTH, day);
      updateGUI();
    }
  }
  
  /**
   * Return the year of the date.
   * @return the year of the date.
   * @see #setYear(int)
   */
  public int getYear(){
    return this.calendar.get(Calendar.YEAR);
  }
  
  /**
   * Set the year of the date. A year has to be positive.
   * @param year the year of the date.
   * @see #getYear()
   */
  public void setYear(int year){
    
    if (year > 0){
      
      this.calendar.set(Calendar.YEAR, year);
      
      int maxDay = getMaxDay(this.calendar.get(Calendar.MONTH), year);
      
      // Verification que si l'année est bisextile est le mois actuel est fevrier,
      // le jour ne doit pas être superieur à 29.
      if (this.calendar.get(Calendar.DAY_OF_MONTH) > maxDay){
    this.calendar.set(Calendar.DAY_OF_MONTH, maxDay);
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
    this.idListenerList.add(ChangeListener.class, l);
  }
  
  /**
   * Remove the given {@link javax.swing.event.ChangeListener} from the panel.
   * @param l the listener to detach from the panel.
   * @see #addChangeListener(ChangeListener)
   */
  public void removeChangeListener(ChangeListener l){
    this.idListenerList.remove(ChangeListener.class, l);
  }
  
  /**
   * Fire a change event to all the registered {@link javax.swing.event.ChangeListener}
   * @see #addChangeListener(ChangeListener)
   * @see #removeChangeListener(ChangeListener)
   */
  protected void fireChangeEvent(){
    
    ChangeEvent e = new ChangeEvent(this);
    
    Object[] listeners = this.idListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ChangeListener.class) {
        ( (ChangeListener) listeners[i + 1]).stateChanged(e);
      }
    }
  }
}

