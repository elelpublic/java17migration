# java17migration
Tests and solutions for migration problems from java 8 to java 17.

This is not a general discussion of migration problems but
only deals with problems in our software.

## Problem areas

### Deprecated apis

For instance "new Integer(...)" and similar is migrated to Integer.valueOf(...).

### Java 9's module system

This causes reflection based code to fail when accessing classes in the 
jdk, for instance:

```
Unable to make field private final java.util.Comparator java.util.TreeMap.comparator accessible: module java.base does not "opens java.util" to unnamed module @2d58d9ed

java.lang.reflect.InaccessibleObjectException: Unable to make field private final java.util.Comparator java.util.TreeMap.comparator accessible: module java.base does not "opens java.util" to unnamed module @2d58d9ed
at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:354)
at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:297)
at java.base/java.lang.reflect.Field.checkCanSetAccessible(Field.java:178)
at java.base/java.lang.reflect.Field.setAccessible(Field.java:172)
at com.infodesire.v20.pojo.Pojos.getFields(Pojos.java:338)
```
