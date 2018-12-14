# jorigin-common
Jaba based common tools and API

Jorigin Common

Changes:

1.0.6
  + Maven Central deploy integration
  + Java 9/10/11 compliance changes (removed calls to deprecated methods / classes)

1.0.5-SNAPSHTOT
  + Added public static void showImageFrame(String, Dimension, Point, BufferedImage, boolean) method to JImageFrame in order
    to quickly display images.

1.0.4:
  + Added List<Class<?>> listClasses(String) method within ClassUtil.
    This mlethod enable to list classes from a given package.
  + Added JImagePanel image visualization component that enable to display images and features
  + Added JImageFeature that enable to display feature within JImagePanel
  + Added JImageToolbar and JImageFrame helper classes

1.0.3:
  + Added ClassUtil that enable to manages classes