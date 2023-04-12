package org.jorigin.swing.task;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.jorigin.Common;
import org.jorigin.swing.IconLoader;
import org.jorigin.task.TaskEvent;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JPanel;


/**
 * A class that enables to graphically monitor tasks.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @version {@value Common#version} - b{@value Common#BUILD}
 */
public class JActivityMonitor extends JDialog{


  private static final long serialVersionUID = 1;
  
  /**
   * Show the progress percent flag.
   */
  public static final int SHOW_PROGRESS_PERCENT = 1;
  
  /**
   * Show the progress count flag.
   */
  public static final int SHOW_PROGRESS_COUNT   = 2;
  
  /**
   * Show all progress flag.
   */
  public static final int SHOW_PROGRESS_ALL     = SHOW_PROGRESS_PERCENT | SHOW_PROGRESS_COUNT;
  
  private Lock lock = null;

  private GridBagLayout layout;


  private javax.swing.text.DefaultStyledDocument activityTracerDocument;

  private JPanel progressBarsPN   = null;
  
  private StyleContext sc         = null;

  private Style taskStartedStyle  = null;
  private Style taskFinishedStyle = null;
  private Style taskProgressStyle = null;
  private Style taskInfoStyle     = null;
  private Style taskWarningStyle  = null;
  private Style taskErrorStyle    = null;

  private JTextPane activityTracer;
  private JScrollPane activityTracerScrollPane;
  private JPanel activityTracerPN = null;
  
  private JCheckBox persistenceCheckBox;

  private boolean showProgressionText  = false;

  private boolean textAreaVisible            = false;
  
  private boolean persistenceCheckBoxVisible = false;
  
  private boolean progressLabelVisible       = false;
  
  private boolean progressBarVisible         = false;
  
  private HashMap<String, JTaskProgress> taskProgressMap = null;
  
  /** The way to show progress type. */
  private int showProgressType        = SHOW_PROGRESS_ALL;
  
  // La fenetre doit elle être persistante (ne pas se fermer seule)
  private boolean isPersistent = true;

  // Compteur de taches imbriquees
  private int boundedTask;

  private boolean useNewLine = true;
  
  private int activityTracerHeight = 440;

  private int progressBarHeight    = 24;
  
  private int progressLabelHeight  = 16;
  
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC CONSTRUCTEUR                                                             CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  
  /**
   * Is the monitor use new line for writing task informations.
   * @return <code>true</code> if the monitor has to use new line for writing task information of <code>false</code> otherwise.
   * @see #setUseNewLine(boolean)
   */
  public boolean isUseNewLine() {
    return useNewLine;
  }

  /**
   * Set if the monitor use new line for writing task informations.
   * @param useNewLine <code>true</code> if the monitor has to use new line for writing task information of <code>false</code> otherwise.
   * @see #isUseNewLine()
   */
  public void setUseNewLine(boolean useNewLine) {
    this.useNewLine = useNewLine;
  }

  /**
   * Create a new activity monitor.
   * @param owner the owner of the component.
   * @param activityTracerVisible is the activity tracer has to be visible.
   * @param progressLabelVisible is the progress labels have to be visible.
   * @param progressBarVisible is the progress bars have to be visible.
   */
  public JActivityMonitor(JFrame owner, boolean activityTracerVisible, boolean progressLabelVisible, boolean progressBarVisible){
    super(owner);
    super.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

    this.textAreaVisible      = activityTracerVisible;
    this.progressBarVisible   = progressBarVisible;
    this.progressLabelVisible = progressLabelVisible;
    
    taskProgressMap = new LinkedHashMap<String, JTaskProgress>();
    
    sc = new StyleContext();
    taskStartedStyle  = sc.addStyle("taskStarted", null);
    taskFinishedStyle = sc.addStyle("taskFinished", null);
    taskProgressStyle = sc.addStyle("taskProgress", null);
    taskInfoStyle     = sc.addStyle("taskInfo", null);
    taskWarningStyle  = sc.addStyle("taskWarning", null);
    taskErrorStyle    = sc.addStyle("taskError", null);

    StyleConstants.setBold(taskStartedStyle, true);
    StyleConstants.setFontFamily(taskStartedStyle, "Helvetica");
    StyleConstants.setFontSize(taskStartedStyle, 11);
    StyleConstants.setForeground(taskStartedStyle, Color.black);

    StyleConstants.setBold(taskFinishedStyle, true);
    StyleConstants.setFontFamily(taskFinishedStyle, "Helvetica");
    StyleConstants.setFontSize(taskFinishedStyle, 11);
    StyleConstants.setForeground(taskFinishedStyle, Color.black);

    StyleConstants.setBold(taskProgressStyle, false);
    StyleConstants.setFontFamily(taskProgressStyle, "Helvetica");
    StyleConstants.setFontSize(taskProgressStyle, 11);
    StyleConstants.setForeground(taskProgressStyle, Color.black);


    StyleConstants.setBold(taskInfoStyle, false);
    StyleConstants.setFontFamily(taskInfoStyle, "Helvetica");
    StyleConstants.setFontSize(taskInfoStyle, 11);
    StyleConstants.setForeground(taskInfoStyle, Color.black);
    //StyleConstants.setIcon(taskInfoStyle, IconServer.getIcon("plastik/information16.png"));

    StyleConstants.setBold(taskWarningStyle, false);
    StyleConstants.setFontFamily(taskWarningStyle, "Helvetica");
    StyleConstants.setFontSize(taskWarningStyle, 11);
    StyleConstants.setForeground(taskWarningStyle, Color.ORANGE);
    //StyleConstants.setIcon(taskWarningStyle, IconServer.getIcon("plastik/warning16.png"));

    StyleConstants.setBold(taskErrorStyle, false);
    StyleConstants.setFontFamily(taskErrorStyle, "Helvetica");
    StyleConstants.setFontSize(taskErrorStyle, 11);
    StyleConstants.setForeground(taskErrorStyle, Color.RED);
    //StyleConstants.setIcon(taskErrorStyle, IconServer.getIcon("plastik/error16.png"));

    
    lock = new ReentrantLock();
    
    initGUI();
  }
  
  /**
   * Creates a new default activity monitor attached to the given owner component.
   * @param owner the owner component.
   */
  public JActivityMonitor(JFrame owner){
    this(owner, false, true, true);
  }
  
  /**
   * Get the desired height of the activity tracer in pixels.
   * @return the desired height of the activity tracer in pixels.
   * @see #setActivityTracerHeight(int)
   * @see #isActivityTracerVisible()
   */
  public int getActivityTracerHeight() {
    return activityTracerHeight;
  }

  /**
   * Set the the desired height of the activity tracer in pixels.
   * @param height the the desired height of the activity tracer in pixels.
   * @see #getActivityTracerHeight()
   * @see #isActivityTracerVisible()
   */
  public void setActivityTracerHeight(int height) {
    this.activityTracerHeight = height;
  }

  /**
   * Get the desired height of the progress bars in pixels.
   * @return the desired height of the progress bars in pixels.
   * @see #setProgressBarHeight(int)
   * @see #isProgessBarVisible()
   * @see #getProgressLabelHeight()
   */
  public int getProgressBarHeight() {
    return progressBarHeight;
  }

  /**
   * Set the desired height of the progress bars in pixels.
   * @param height the desired height of the progress bars in pixels.
   * @see #getProgressBarHeight()
   * @see #isProgessBarVisible()
   * @see #getProgressLabelHeight()
   */
  public void setProgressBarHeight(int height) {
    this.progressBarHeight = height;
  }

  /**
   * Get the desired height of the progress labels in pixels.
   * @return the desired height of the progress labels in pixels.
   * @see #isProgessLabelVisible()
   * @see #setProgressLabelHeight(int)
   * @see #getProgressBarHeight()
   */
  public int getProgressLabelHeight() {
    return progressLabelHeight;
  }

  /**
   * Set the desired height of the progress labels in pixels.
   * @param height the desired height of the progress labels in pixels.
   * @see #getProgressLabelHeight()
   * @see #isProgessLabelVisible()
   * @see #getProgressBarHeight()
   */
  public void setProgressLabelHeight(int height) {
    this.progressLabelHeight = height;
  }
  
  /**
   * Get if the activity tracer that log progress details is visible.
   * @return <code>true</code> if the text area that log progress details is visible and <code>false</code> otherwise.
   * @see #setActivityTracerVisible(boolean)
   */
  public boolean isActivityTracerVisible(){
    return textAreaVisible;
  }
  
  /**
   * Set if the activity tracer that log progress details has to be visible.
   * @param visible <code>true</code> if the text area that log progress details has to be visible and <code>false</code> otherwise.
   * @see #isActivityTracerVisible()
   */
  public void  setActivityTracerVisible(boolean visible){
    
    if (isActivityTracerVisible() != visible){
      this.textAreaVisible = visible;
      
      if (visible){
        activityTracerPN.add(activityTracerScrollPane, BorderLayout.CENTER);
        
        Dimension dim    = getSize();

        Dimension newDim = new Dimension((int)dim.getWidth(), (int)(dim.getHeight() + activityTracerPN.getSize().getHeight()));
        
        setSize(newDim);
        setPreferredSize(newDim);
        
      } else {
        activityTracerPN.remove(activityTracerScrollPane);
        
        Dimension dim    = getSize();

        Dimension newDim = new Dimension((int)dim.getWidth(), (int)(dim.getHeight() - activityTracerPN.getSize().getHeight()));
        
        setSize(newDim);
        setPreferredSize(newDim);
        
      }
      validate();
      
      refreshGUI();
    }
    
    
  }
  
  /**
   * Get if the persistence check box is visible.
   * @return <code>true</code> if the persistence check box is visible and <code>false</code> otherwise.
   * @see #setPersistenceCheckBoxVisible(boolean)
   */
  public boolean isPersistenceCheckBoxVisible(){
    return persistenceCheckBoxVisible;
  }
  
  /**
   * Set if the persistence check box has to be  visible.
   * @param visible <code>true</code> if the persistence check box is visible and <code>false</code> otherwise.
   * @see #isPersistenceCheckBoxVisible()
   */
  public void setPersistenceCheckBoxVisible(boolean visible){
    this.persistenceCheckBoxVisible = visible;
    refreshGUI();
  }
  
  /**
   * Get if the progress labels (textual labels above progress bars) are visible.
   * @return <code>true</code> if the progress label are visible and <code>false</code> otherwise.
   * @see #setProgressLabelVisible(boolean)
   */
  public boolean isProgessLabelVisible(){
    return progressLabelVisible;  
  }
  
  /**
   * Set if the progress labels (textual labels above progress bars) are visible.
   * @param visible <code>true</code> if the progress label are visible and <code>false</code> otherwise.
   * @see #isProgessLabelVisible()
   */
  public void setProgressLabelVisible(boolean visible){
    progressLabelVisible = visible;
    refreshGUI();
  }
  
  /**
   * Get if the progress bars are visible.
   * @return <code>true</code> if the progress bar are visible and <code>false</code> otherwise.
   * @see #setProgressBarVisible(boolean)
   */
  public boolean isProgessBarVisible(){
    return progressBarVisible;  
  }
  
  /**
   * Set if the progress bars are visible.
   * @param visible <code>true</code> if the progress bar are visible and <code>false</code> otherwise.
   * @see #isProgessBarVisible()
   */
  public void setProgressBarVisible(boolean visible){
    progressBarVisible = visible;
    refreshGUI();
  }
  
  /**
   * Set if the progression labels has to be visible.
   * @return <code>true</code> if the progression labels are visible and <code>false</code> otherwise.
   * @see #isProgessLabelVisible()
   */
  public boolean isShowProgressionText() {
	return showProgressionText;
  }

  /**
   * Get if the progression labels are visible.
   * @param showProgressionText <code>true</code> if the progression labels are visible and <code>false</code> otherwise.
   * @see #setProgressLabelVisible(boolean)
   */
  public void setShowProgressionText(boolean showProgressionText) {
	this.showProgressionText = showProgressionText;
  }
  
  /**
   * Specify if the activity monitor is persistent. If it's the case, the monitor 
   * is always displayed. If the <code>isPersistent</code> value is equals to false,
   * the activity monitor is hiden when all monitored tasks are finished.
   * @param isPersistent the persistence value of the activity monitor
   */
  public void setPersistent(boolean isPersistent){
    this.isPersistent = isPersistent;
    if (isPersistent){
      persistenceCheckBox.setSelected(false);
    } else {
      persistenceCheckBox.setSelected(true);
    }
  }
  
  /**
   * Get the persistence value of the activity monitor. If the <code>isPersistent</code> 
   * value is equals to false, the activity monitor is hiden when all monitored 
   * tasks are finished. Otherwise, the monotor is always visible.
   * @return the persistence value of the monitor.
   */
  public boolean isPersistent(){
    return this.isPersistent();
  }
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA FIN ACCESSEURS                                                            AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
  

  /**
   * Initialize the Graphical User Interface (GUI) components.
   */
  protected void initGUI(){

    
    activityTracer = new JTextPane(){

      private static final long serialVersionUID = 1L;

      @Override
      public void repaint(){
        try {
          super.repaint();
        } catch (Exception e) {
          
        }
      }
      
      @Override
      public void repaint(long tm){
        try {
          super.repaint(tm);
        } catch (Exception e) {
          
        }
      }
      
      @Override
      public void repaint(int x, int y, int width, int height){
        try {
          super.repaint(x, y, width, height);
        } catch (Exception e) {
          
        }
      }
      
      @Override
      public void repaint(long tm, int x, int y, int width, int height){
        try {
          super.repaint(tm, x, y, width, height);
        } catch (Exception e) {
          
        }
      }
      
      @Override
      public void setCaretPosition(int position){
        try {
          super.setCaretPosition(position);
        } catch (Exception e) {
          
        }
      }
    };
    
    activityTracer.setCaret(new ActivityCaret());
    
    activityTracer.setMaximumSize(new Dimension(65535, 65535));
    activityTracerDocument = (DefaultStyledDocument)activityTracer.getDocument();

    activityTracerScrollPane = new JScrollPane();
    activityTracerScrollPane.setSize(320, 400);
    activityTracerScrollPane.setPreferredSize(new Dimension(320, 400));
    activityTracerScrollPane.setMinimumSize(new Dimension(180, 250));
    activityTracerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    activityTracerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    activityTracerScrollPane.setWheelScrollingEnabled(true);
    activityTracerScrollPane.getViewport().add(activityTracer);

    activityTracerPN = new JPanel();
    activityTracerPN.setLayout(new BorderLayout());
    
    if (isActivityTracerVisible()){
      activityTracerPN.add(activityTracerScrollPane, BorderLayout.CENTER);
    }
    
    
    progressBarsPN = new JPanel();
    progressBarsPN.setLayout(new GridBagLayout());
    
    persistenceCheckBox = new JCheckBox("Close");
    persistenceCheckBox.addItemListener(new ItemListener(){
                            @Override
                            public void itemStateChanged(ItemEvent e){
                              if (e.getStateChange() == ItemEvent.SELECTED){
                                isPersistent = false;
                              } else{
                                isPersistent = true;
                              }
                            }});

    layout = new GridBagLayout();
    this.setTitle("Monitor");
    
    List<Image> images = new ArrayList<Image>();
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-16.png"));
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-24.png"));
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-32.png"));
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-48.png"));
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-64.png"));
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-72.png"));
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-128.png"));
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-256.png"));
    images.add(IconLoader.getImage("icon/org/jorigin/swing/chart-raising-gray-512.png"));
    
    setIconImages(images);
    this.setName("Monitor");
    this.getContentPane().setLayout(layout);
    
    
    GridBagConstraints c = new GridBagConstraints();
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.NORTH;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.BOTH;
    //c.insets    = labelInsets;
    c.weightx   = 1.0;
    c.weighty   = 1.0;
    c.anchor    = GridBagConstraints.NORTH;
    add(activityTracerPN, c);
    
    c           = new GridBagConstraints();
    c.gridx     = GridBagConstraints.RELATIVE;
    c.gridy     = GridBagConstraints.RELATIVE;
    c.gridheight= 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill      = GridBagConstraints.BOTH;
    //c.insets    = labelInsets;
    c.weightx   = 1.0;
    c.weighty   = 1.0;
    c.anchor    = GridBagConstraints.NORTH;
    add(progressBarsPN, c);
    
    if (isPersistenceCheckBoxVisible()){
      c           = new GridBagConstraints();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.BOTH;
      //c.insets    = labelInsets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      c.anchor    = GridBagConstraints.NORTH;
      add(persistenceCheckBox, c);
    }
    
    if (getOwner() != null){
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = getSize();
      if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
      }

      setLocation( (screenSize.width - frameSize.width) / 2,(screenSize.height - frameSize.height) / 2);
    }

    int frameWidth  = 600;
    int frameHeight =   0;
    
    if (isActivityTracerVisible()){
      frameHeight += getActivityTracerHeight();
    }
    
    if (isProgessBarVisible()){
      frameHeight += getProgressBarHeight();
    }
    
    if (isProgessLabelVisible()){
      frameHeight += getProgressLabelHeight();
    }
    
    setSize(new Dimension(frameWidth, frameHeight));
    setPreferredSize(new Dimension(frameWidth, frameHeight));
    
    pack();
  }

  /**
   * Refresh the graphical user interface components.
   */
  protected void refreshGUI(){
    
  }
  
  /**
   * Init the activity monitor.
   * @param activityVisible is the activity tracer is visible.
   * @param progressBarVisible is the progress bar is visible.
   */
  public void init(boolean activityVisible, boolean progressBarVisible){

    Point location      = null;
    Dimension ownerSize = null;
    Dimension size      = null;

    if (getOwner() != null){
      location = getOwner().getLocation();
      ownerSize = getOwner().getSize();
      size      = getSize();
      
      location = new Point((int)(location.getX() + ownerSize.getWidth()/2), (int)(location.getY()+ownerSize.getHeight()/2));
      
      setLocation((int)(location.getX() - size.width/2), (int)(location.getY() + size.height/2));

    } else {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      
      if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
      }
      //this.setLocation( (screenSize.width - frameSize.width) / 2,(screenSize.height - frameSize.height) / 2);
      this.setLocation( 0,0);
    }
    
    activityTracerScrollPane.setVisible(activityVisible);
    
    this.boundedTask = 0;

    isPersistent = true;

    this.pack();
    //this.setVisible(true);
  }

  
  private String indent(String str, int indent){
    String tmp = new String();
    for(int i = 0; i<indent; i++){
        tmp = tmp+" ";
    }
    tmp = tmp+str;

    return tmp;
  }


  private void write(String str, Style style){

    try {
      
      lock.lock();
      
      if (isUseNewLine()){
        activityTracerDocument.insertString(activityTracerDocument.getLength(),
                                                "\n"
                                              + str
                                              , style);
      } else {
        activityTracerDocument.insertString(activityTracerDocument.getLength(),
          str
          , style);
      }
      activityTracer.setCaretPosition(activityTracerDocument.getLength());
      
    } catch (Exception ex) {
      
      
    } finally {
      lock.unlock();
    }
  }


  private void writeWithIndent(String str, int indent, Style style){
    write(indent(str, indent*2), style);
  }

  /**
   * Process a task event.
   * @param event The task Event to process.
   * @see #processTaskEvent(TaskEvent, boolean)
   */
  public void processTaskEvent(TaskEvent event){
    processTaskEvent(event, true);
  }
  
  /**
   * Process a task event. The boolean <code>progressBarVisible</code> can be used to override 
   * the {@link #isProgessBarVisible()} and {@link #isProgessLabelVisible()} values for the specified task.
   * @param event The task Event to process.
   * @param progressBarVisible <code>true</code> if a task progress (progress bar plus label) has to be created for this task.
   */
  public void processTaskEvent(TaskEvent event, boolean progressBarVisible){

    JTaskProgress taskProgress = null;
    
    try {
      //Common.logger.log.info( "Event Processed: "+event.getTaskName()+" "+event.getDescription());

      switch (event.getID()){

      // Evènement de début de tâche
      case TaskEvent.TASK_STARTED:

        if (isActivityTracerVisible() && (event.getDescription() != null) && !(event.getDescription().trim().equals(""))){  
          writeWithIndent(event.getDescription(), boundedTask, taskStartedStyle);
        }

        if (progressBarVisible){
          taskProgress = new JTaskProgress(isProgessLabelVisible(), isProgessBarVisible());
          taskProgress.getLabel().setText(event.getDescription());
          
          boundedTask = boundedTask + 1;
          if (event.getSize() > 0){
            taskProgress.getProgressBar().setMinimum(0);
            taskProgress.getProgressBar().setMaximum(event.getSize());
            taskProgress.getProgressBar().setValue(0);
            taskProgress.getProgressBar().setIndeterminate(false);
          } else {
            taskProgress.getProgressBar().setMinimum(0);
            taskProgress.getProgressBar().setMaximum(0);
            taskProgress.getProgressBar().setValue(0);
            taskProgress.getProgressBar().setIndeterminate(true);
          }

          taskProgress.getProgressBar().setStringPainted(true);
          
          taskProgressMap.put(event.getTaskName(), taskProgress);
          addJTaskProgress(taskProgress);
        }

        //this.pack();
        this.setVisible(true);
        break;

        // Progression dans une tache
      case TaskEvent.TASK_PROGRESS:

        String progress = null;
        String percent  = null;
        String text     = "";
        int   ratio     = 0;

        int size        = event.getSize();

        taskProgress = taskProgressMap.get(event.getTaskName());
        
        if (isActivityTracerVisible() && (event.getDescription() != null) && !(event.getDescription().trim().equals(""))){    
          if (isUseNewLine()){
            writeWithIndent(event.getDescription(), boundedTask+3, taskProgressStyle);
          } else {
            write(event.getDescription(), taskProgressStyle);
          }
        } 

        if (taskProgress != null){
          
          if (event.getDescription() != null){
            taskProgress.getLabel().setText(event.getDescription());
          }
          
          if (size >= 0){
            taskProgress.getProgressBar().setValue(size);
          }
          
          if (showProgressionText && (!taskProgress.getProgressBar().isIndeterminate()) && (size >= 0)){
            if ((this.showProgressType & SHOW_PROGRESS_COUNT) != 0){
              progress = ""+size+" / "+taskProgress.getProgressBar().getMaximum();
            } 

            if ((this.showProgressType & SHOW_PROGRESS_PERCENT) != 0){
              ratio = (int)(((float)size / (float)taskProgress.getProgressBar().getMaximum())*100);
              percent  = ""+ratio+"%%";
            }

            if (progress != null){
              text += progress;
            }

            if (percent != null){
              if (!text.equals("")){
                text +=" - "+percent;
              } else {
                text += percent;
              }
            }
            
            taskProgress.getProgressBar().setString(text);
          }
          
          taskProgress.getProgressBar().repaint();
        }

        this.activityTracer.repaint();
        this.repaint();  
        break;

        // Tâche suspendue
      case TaskEvent.TASK_SUSPENDED:

        break;

        // Tâche terminée
      case TaskEvent.TASK_FINISHED:

        boundedTask = boundedTask - 1;

        if (isActivityTracerVisible() && (event.getDescription() != null) && !(event.getDescription().trim().equals(""))){
          writeWithIndent(event.getDescription()+"\n", boundedTask, taskFinishedStyle);
        }

        if ((boundedTask < 1) && (!isPersistent)){
          this.setVisible(false);
        }

        removeJTaskProgress(taskProgressMap.get(event.getTaskName()));
        taskProgressMap.remove(event.getTaskName());
        
        break;

        // Avertissement dans une tâche
      case TaskEvent.TASK_WARNING:
        if (isActivityTracerVisible() && (event.getDescription() != null) && !(event.getDescription().trim().equals(""))){  
          if (isUseNewLine()){
            writeWithIndent(event.getDescription(), boundedTask+3, taskWarningStyle);
          } else {
            write(event.getDescription(), taskWarningStyle);
          }
        }

        this.activityTracer.repaint();
        this.repaint();
        break;

        // Erreur dans une tâche  
      case TaskEvent.TASK_ERROR:
        if (isActivityTracerVisible() && (event.getDescription() != null) && !(event.getDescription().trim().equals(""))){   
          if (isUseNewLine()){
            writeWithIndent(event.getDescription(), boundedTask+3, taskErrorStyle);
          } else {
            write(event.getDescription(), taskErrorStyle);
          }
        }

        this.activityTracer.repaint();
        this.repaint();
        break;

        // Information sur une tâche  
      case TaskEvent.TASK_INFO:
        if (isActivityTracerVisible() && (event.getDescription() != null) && !(event.getDescription().trim().equals(""))){ 
          if (isUseNewLine()){
            writeWithIndent(event.getDescription(), boundedTask+3, taskInfoStyle);
          } else {
            write(event.getDescription(), taskInfoStyle);
          }
        }

        this.activityTracer.repaint();
        this.repaint();

        break;
      }
    } catch (Exception e) {
     
    }
  }
  
  /**
   * Dispose all active tasks displayed within the monitor. You can use this method is the monitored process has failed.
   */
  public void disposeTasks(){
    boundedTask = 0;
    
    Iterator<JTaskProgress> iter = taskProgressMap.values().iterator();
    while(iter.hasNext()){
      removeJTaskProgress(iter.next());
    }
    
    if (!isPersistent){
      setVisible(false);
    }
  }
  
  private boolean addJTaskProgress(JTaskProgress taskProgress){
    
    if (taskProgress != null){
      
      GridBagConstraints c = null;
      
      c           = new GridBagConstraints();
      c.gridx     = GridBagConstraints.RELATIVE;
      c.gridy     = GridBagConstraints.RELATIVE;
      c.gridheight= 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.fill      = GridBagConstraints.BOTH;
      //c.insets    = labelInsets;
      c.weightx   = 1.0;
      c.weighty   = 1.0;
      c.anchor    = GridBagConstraints.NORTH;
      progressBarsPN.add(taskProgress, c);

      Dimension size             = getSize();
      Dimension preferredSize    = getPreferredSize();
      
      int height                 = (int)size.getHeight();
      int preferredHeight        = (int)preferredSize.getHeight();
      
      if (isProgessLabelVisible()){
        height          += getProgressLabelHeight();
        preferredHeight += getProgressLabelHeight();
      }
      
      if (isProgessBarVisible()){
        height          += getProgressBarHeight();
        preferredHeight += getProgressLabelHeight();
      }
      
      Dimension newSize          = new Dimension((int)size.getWidth(), height);
      Dimension newPreferredSize = new Dimension((int)preferredSize.getWidth(), preferredHeight);
      
      setSize(newSize);
      setPreferredSize(newPreferredSize);
      
      pack();
      validate();
      repaint();

      size             = null;
      preferredSize    = null;
      newSize          = null;
      newPreferredSize = null;
      
      return true;
    }
    
    return false;
  }
  
  private boolean removeJTaskProgress(JTaskProgress taskProgress){
    if (taskProgress != null){
      progressBarsPN.remove(taskProgress);
      
      Dimension size             = getSize();
      Dimension preferredSize    = getPreferredSize();
      
      int height                 = (int)size.getHeight();
      int preferredHeight        = (int)preferredSize.getHeight();
      
      if (isProgessLabelVisible()){
        height          -= getProgressLabelHeight();
        preferredHeight -= getProgressLabelHeight();
      }
      
      if (isProgessBarVisible()){
        height          -= getProgressBarHeight();
        preferredHeight -= getProgressLabelHeight();
      }
      
      Dimension newSize          = new Dimension((int)size.getWidth(), height);
      Dimension newPreferredSize = new Dimension((int)preferredSize.getWidth(), preferredHeight);
      
      setSize(newSize);
      setPreferredSize(newPreferredSize);
      
      pack();
      validate();
      repaint();
      
      size             = null;
      preferredSize    = null;
      newSize          = null;
      newPreferredSize = null;
      
      return true;
    }
    
    return false;
  }
}

