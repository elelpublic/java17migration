# java17migration
Tests and solutions for migration problems from java 8 to java 17.

This is not a general discussion of migration problems but
only deals with problems in our software.

## Problem areas

### Deprecated API

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

For unit tests in ant:

```
    <target name="java9settings">
    
        <condition property="addOpensPropertsPart1" value="--add-opens">
          <javaversion atleast="9"/>
        </condition>
        <property name="addOpensPropertsPart1" value="-Dummy1=1" />
        <condition property="addOpensPropertsPart2" value="java.base/java.util=ALL-UNNAMED">
          <javaversion atleast="9"/>
        </condition>
        <property name="addOpensPropertsPart2" value="-Dummy2=2" />
    
    </target>

    <target
        name="junit.run" 
        depends="java9settings, javac, junit.compile"
        >
    
        <junit fork="true">
    
          <jvmarg value="${addOpensPropertsPart1}" />
          <jvmarg value="${addOpensPropertsPart2}" />
          
          ...
```

### Error parsing java version numbers

commons-lang

```
Caused by: java.lang.NumberFormatException: multiple points
	at java.base/jdk.internal.math.FloatingDecimal.readJavaFormatString(FloatingDecimal.java:1914)
	at java.base/jdk.internal.math.FloatingDecimal.parseFloat(FloatingDecimal.java:122)
	at java.base/java.lang.Float.parseFloat(Float.java:476)
	at org.apache.commons.lang.SystemUtils.getJavaVersionAsFloat(SystemUtils.java:756)
	at org.apache.commons.lang.SystemUtils.<clinit>(SystemUtils.java:469)
```

Mitigation:

* throw out org.apache.commons.lang.* in favor of org.apache.commons.lang3.*

This causes the need to upgrade velocity.

### New version of velocity

#### Deprecated configuration:

```
# old way (velocity 1.7)
resource.loader=memory
memory.resource.loader.class=com.infodesire.v20.templating.velocity.MemoryResourceLoader

# the new way (velocity 2+)
resource.loaders=memory
resource.loader.memory.class=com.infodesire.v20.templating.velocity.MemoryResourceLoader
```

#### Problem with hyphens in variable names:

```
#set( $ext-debug="1" )
```

will cause:

```
66   [main] ERROR velocity.parser  - 1664649690654-0: Encountered "-" at line 1, column 11.
Was expecting one of:
    "[" ...
    <WHITESPACE> ...
    <NEWLINE> ...
    "=" ...
```

Mitigation:

Set variable in velocity properties:

```
parser.allow_hyphen_in_identifiers=true
```

#### Velocity 2 uses JVMs encoding instead of ISO-8859-15

Some characters will display incorrectly in rendered text.

Mitigation: in MemoryResourceLoader

```
  public Reader getResourceReader( String name, String encoding ) throws ResourceNotFoundException {
    try {
      return new InputStreamReader( getResourceStream( name ), "ISO-8859-15" );
    }
    catch( UnsupportedEncodingException ex ) {
      throw new RuntimeException( ex );
    }
  }
```
 