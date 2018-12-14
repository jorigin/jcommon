package org.jorigin.task;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.jorigin.Common;

/**
 * A dialog dedicated to the reporting of tasks execution and activity monitoring.
 * @author Julien Seinturier - (c) 2010 - JOrigin project - <a href="http://www.jorigin.org">http:/www.jorigin.org</a>
 * @since 1.0.0
 *
 */
public class ActivityMonitor extends JDialog{


  private static final long serialVersionUID = Common.BUILD;
  
  /**
   * Show the progress in percent of the work.
   * @see #SHOW_PROGRESS_COUNT
   */
  public static final int SHOW_PROGRESS_PERCENT = 1;
  
  /**
   * Show the process in count of task units.
   * @see #SHOW_PROGRESS_PERCENT
   */
  public static final int SHOW_PROGRESS_COUNT   = 2;
  
  /**
   * Show all the progress indicators (percent and units)
   * @see #SHOW_PROGRESS_PERCENT
   * @see #SHOW_PROGRESS_COUNT
   */
  public static final int SHOW_PROGRESS_ALL     = SHOW_PROGRESS_PERCENT | SHOW_PROGRESS_COUNT;
  
  /**
   * A lock used to dispatch access to components in case of concurrent monitoring.
   */
  public Lock lock = null;
  
  Icon frameIcon;

  GridBagLayout layout;

  JProgressBar progressBar;
  javax.swing.text.DefaultStyledDocument activityTracerDocument;

  StyleContext sc = null;

  Style taskStartedStyle  = null;
  Style taskFinishedStyle = null;
  Style taskProgressStyle = null;
  Style taskInfoStyle = null;
  Style taskWarningStyle = null;
  Style taskErrorStyle = null;

  JTextPane activityTracer;
  JScrollPane activityTracerScrollPane;
  JCheckBox persistenceCheckBox;

  boolean showProgressionText = true;
  
  int showProgressType        = SHOW_PROGRESS_ALL;
  
  // La fenetre doit elle être persistante (ne pas se fermer seule)
  boolean isPersistent = true;

  boolean PROGRESS_BAR_VISIBLE;
  boolean ACTIVITY_TRACER_VISIBLE;

  // Compteur de taches imbriquees
  int boundedTask;

  Font taskStartFont      = new Font("Dialog", Font.BOLD, 11);
  Color taskStartColor    = Color.green;

  Font taskProgressFont   = new Font("Dialog", Font.ITALIC, 10);
  Color taskProgressColor = Color.black;

//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC CONSTRUCTEUR                                                             CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  
  /**
   * Create a new activity monitor as a modal dialog.
   * @param owner the owner of the activity monitor.
   */
  public ActivityMonitor(JFrame owner){
    super(owner);
    super.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    this.setSize(new Dimension(600, 500));
    this.setPreferredSize(new Dimension(600, 500));

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
    //StyleConstants.setComponent(taskErrorStyle, new JLabel("coucou"));
    //StyleConstants.setIcon(taskErrorStyle, IconServer.getIcon("plastik/error16.png"));

    
    lock = new ReentrantLock();
    
    initGUI();
  }
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
//CC FIN CONSTRUCTEUR                                                         CC
//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
 
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//AA ACCESSEURS                                                               AA
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
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
  

  
  protected void initGUI(){

    //frameIcon = IconServer.getIcon("plastik/information16.png");
    
    activityTracer = new JTextPane(){

      private static final long serialVersionUID = 1L;

      @Override
      public void repaint(){
        try {
          super.repaint();
        } catch (Exception e) {
          Common.logger.log(Level.WARNING, "Cannot repaint activity tracer");
        }
      }
      
      @Override
      public void repaint(long tm){
        try {
          super.repaint(tm);
        } catch (Exception e) {
          Common.logger.log(Level.WARNING, "Cannot repaint activity tracer");
        }
      }
      
      @Override
      public void repaint(int x, int y, int width, int height){
        try {
          super.repaint(x, y, width, height);
        } catch (Exception e) {
          Common.logger.log(Level.WARNING, "Cannot repaint activity tracer");
        }
      }
      
      @Override
      public void repaint(long tm, int x, int y, int width, int height){
        try {
          super.repaint(tm, x, y, width, height);
        } catch (Exception e) {
          Common.logger.log(Level.WARNING, "Cannot repaint activity tracer");
        }
      }
      
      @Override
      public void setCaretPosition(int position){
        try {
          super.setCaretPosition(position);
        } catch (Exception e) {
          Common.logger.log(Level.WARNING, "Cannot set caret position in activity tracer");
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


    progressBar    = new JProgressBar();
    progressBar.setSize(new Dimension(320, 20));
    progressBar.setPreferredSize(new Dimension(320, 20));
    progressBar.setMinimumSize(new Dimension(320, 20));
    progressBar.setStringPainted(true);
    
    persistenceCheckBox = new JCheckBox("Close");
    persistenceCheckBox.addItemListener(new ItemListener(){
                            public void itemStateChanged(ItemEvent e){
                              if (e.getStateChange() == ItemEvent.SELECTED){
                                isPersistent = false;
                              } else{
                                isPersistent = true;
                              }
                            }});

    layout = new GridBagLayout();
    this.setTitle("Monitor");
    //this.setIconImage(new ImageIcon(this.getClass().getResource("resource/icon/AmetistActivityMonitor16.gif")).getImage());
    this.setName("Monitor");
    this.getContentPane().setLayout(layout);
    
    
    GridBagConstraints c = new GridBagConstraints();
    
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
    add(activityTracerScrollPane, c);
    
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
    add(progressBar, c);
    
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
    
    
    this.pack();
  }

  /**
   * Initialize the activity monitor.
   * @param activityVisible <code>true</code> if the activity is visible and <code>false</code> otherwise.
   * @param progressBarVisible <code>true</code> if the progress bar is visible and <code>false</code> otherwise.
   */
  public void init(boolean activityVisible, boolean progressBarVisible){

    Point location      = null;
    Dimension ownerSize = null;

    if (getOwner() != null){
      location = getOwner().getLocation();
      ownerSize = getOwner().getSize();
      this.setLocation( ((int)location.getX() + ownerSize.width/2) - this.getWidth()/ 2,(((int)location.getY() + ownerSize.height) / 2) - this.getHeight()/2);

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

    progressBar.setVisible(progressBarVisible);
    progressBar.setEnabled(progressBarVisible);
    
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
      
      activityTracerDocument.insertString(activityTracerDocument.getLength(),
                                              "    " + str +
                                              "\n", style);
      activityTracer.setCaretPosition(activityTracerDocument.getLength());
      
    } catch (Exception ex) {
      
      Common.logger.log(Level.WARNING, "Cannot display message on activity monitor", ex);
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
   */
  public void processTaskEvent(TaskEvent event){

    try {
      //Common.logger.log(Level.INFO, "Event Processed: "+event.getTaskName()+" "+event.getDescription());

      switch (event.getID()){

      // Evènement de début de tâche
      case TaskEvent.TASK_STARTED:

        if ((event.getDescription() != null) && !(event.getDescription().trim().equals(""))){  
          writeWithIndent(event.getDescription(), boundedTask, taskStartedStyle);
        }

        boundedTask = boundedTask + 1;
        if (event.getSize() > 0){
          this.progressBar.setMinimum(0);
          this.progressBar.setMaximum(event.getSize());
          this.progressBar.setValue(0);
          this.progressBar.setIndeterminate(false);
        } else {
          this.progressBar.setMinimum(0);
          this.progressBar.setMaximum(0);
          this.progressBar.setValue(0);
          this.progressBar.setIndeterminate(true);
        }

        //this.pack();
        this.setVisible(true);
        break;

        // Progression dans une tache
      case TaskEvent.TASK_PROGRESS:

        String progress = "";
        String percent  = "";
        String text     = "";
        int   ratio     = 0;

        int size        = event.getSize();

        if ((event.getDescription() != null) && !(event.getDescription().trim().equals(""))){    
          writeWithIndent(event.getDescription(), boundedTask+3, taskProgressStyle);
        }

        this.progressBar.setValue(size);

        if (showProgressionText && (!this.progressBar.isIndeterminate())){
          if ((this.showProgressType & SHOW_PROGRESS_COUNT) != 0){
            progress += ""+size+" / "+this.progressBar.getMaximum();
          } 

          if ((this.showProgressType & SHOW_PROGRESS_PERCENT) != 0){
            ratio = (int)(((double)size / (double)this.progressBar.getMaximum())*100);
            percent  += ""+ratio+" %";
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
        }
        this.progressBar.setString(text);

        this.progressBar.repaint();
        this.activityTracer.repaint();
        this.repaint();  
        break;

        // Tâche suspendue
      case TaskEvent.TASK_SUSPENDED:

        break;

        // Tâche terminée
      case TaskEvent.TASK_FINISHED:

        boundedTask = boundedTask - 1;

        if ((event.getDescription() != null) && !(event.getDescription().trim().equals(""))){
          writeWithIndent(event.getDescription()+"\n", boundedTask, taskFinishedStyle);
        }

        if ((boundedTask < 1) && (!isPersistent)){
          this.setVisible(false);
        }

        this.progressBar.setIndeterminate(false);

        break;

        // Avertissement dans une tâche
      case TaskEvent.TASK_WARNING:
        if ((event.getDescription() != null) && !(event.getDescription().trim().equals(""))){  
          writeWithIndent(event.getDescription(), boundedTask+3, taskWarningStyle);
        }
        this.progressBar.repaint();
        this.activityTracer.repaint();
        this.repaint();
        break;

        // Erreur dans une tâche  
      case TaskEvent.TASK_ERROR:
        if ((event.getDescription() != null) && !(event.getDescription().trim().equals(""))){   
          writeWithIndent(event.getDescription(), boundedTask+3, taskErrorStyle);
        }
        this.progressBar.repaint();
        this.activityTracer.repaint();
        this.repaint();
        break;

        // Information sur une tâche  
      case TaskEvent.TASK_INFO:
        if ((event.getDescription() != null) && !(event.getDescription().trim().equals(""))){ 
          writeWithIndent(event.getDescription(), boundedTask+3, taskInfoStyle);
        }



        this.progressBar.repaint();
        this.activityTracer.repaint();
        this.repaint();

        break;
      }
    } catch (Exception e) {
      Common.logger.log(Level.WARNING, "Cannot repaint activity monitor", e);
    }
  }
  
}


