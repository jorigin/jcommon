# Changelog

## [1.0.14](https://github.com/jorigin/jcommon/releases/tag/release-1.0.14)
### Added
 - Added `org.jorigin.device.DeficeInfo` interface for handling manufactured devices metadata
 - Added `org.jorigin.device.SimpleDeficeInfo` class for handling manufactured devices metadata
 - Added new icon set for GUI components
 - Added `org.jorigin.state.HandleFocus` interface for handling focus related states

### Changed
 - Refactored package `org.jorigin.gui` into `org.jorigin.swing`
 - Moved class GraphicsEnvironmentPanel from `org.jorigin.system.gui` to `org.jorigin.swing`
 - Moved class `org.jorigin.task.ActivityMonitor` to `org.jorigin.swing.task.JActivityMonitor`
 - Moved class `org.jorigin.task.gui.JTaskProgress` to `org.jorigin.swing.task.JTaskProgress`
 - Moved class `org.jorigin.task.ActivityCaret` to `org.jorigin.swing.task.ActivityCaret`
 - IconLoader can now load icons from Jar files or specified directory
 - JMemoryStateBar can be stopped / restarted
 - Corrected inconsistents Javadoc @author
 - Modified `org.jorigin.state.HandleState` interface for handling focus related states

## [1.0.13](https://github.com/jorigin/jcommon/releases/tag/release-1.0.13)
### Changed
 - Corrected icon resource bug for `JMemoryStateBar`

## [1.0.12](https://github.com/jorigin/jcommon/releases/tag/release-1.0.12)
### Added
  + Access to display layer within JImagePanel
  + Added module-info.java file
  + Added Junit 5 compliance
  
### Changed
  + PathUtil `getDirectory(String)` has been updated

## 1.0.11
### Added
  + Added state package that provide interfaces that can handle various states (Activation, Display, Selection, ...)
  + Interface Displayable from org.jorigin.gui package has been refactored into HandleDisplay interface from org.jorigin.state package.
  + [BUG] Corrected an encoding bug for '%' display within Activity Monitor 

## 1.0.10
### Added
  + Added layer handling within Image display (see JImagePanel)

## 1.0.8
### Added
  + Updated FileUtil
  + Added JThumbnail handling

### Changed
  + Updated ActivityMonitor

## 1.0.7
### Added
  + Maven Central deploy integration
  + Java 9/10/11 compliance changes (removed calls to deprecated methods / classes)

## 1.0.5-SNAPSHTOT
### Added
  + Added public static void showImageFrame(String, Dimension, Point, BufferedImage, boolean) method to JImageFrame in order
    to quickly display images.

## 1.0.4:
### Added
  + Added List<Class<?>> listClasses(String) method within ClassUtil.
    This mlethod enable to list classes from a given package.
  + Added JImagePanel image visualization component that enable to display images and features
  + Added JImageFeature that enable to display feature within JImagePanel
  + Added JImageToolbar and JImageFrame helper classes

## 1.0.3:
### Added
  + Added ClassUtil that enable to manages classes