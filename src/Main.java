import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {

        System.out.println( "java 17 migration" );
        System.out.println( "java.runtime.version: " + System.getProperty( "java.runtime.version" ) );

        System.out.println( "Check reflection access to jdk classes:" );
        System.out.println( "This code will only run, when access to the classes is granted, for instance with these JDK-Options:" );
        System.out.println( "--add-opens java.base/java.util=ALL-UNNAMED" );

        Map<String, String> map = new TreeMap<>();
        Class pojoClass = map.getClass();
        for( Field field : pojoClass.getDeclaredFields() ) {
            field.setAccessible( true );
        }

        System.out.println( "OK" );

    }

}
