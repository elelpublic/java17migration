package com.infodesire.java17migration;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws ParseException {

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

        Map<String, String> variables = new HashMap<>();
        variables.put( "who", "World" );
        String merged = new Main().velocity( "Hello ${who}!", variables );

        System.out.println( "Merged using velocity:\n" + merged );

    }

    public String velocity( String templateCode, Map variables ) throws ParseException {

        // source: https://stackoverflow.com/questions/1432468/how-to-use-string-as-velocity-template

        // Initialize the engine.
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty( "resource.loader", "string");
        engine.addProperty( "string.resource.loader.class", StringResourceLoader.class.getName() );
        engine.addProperty( "string.resource.loader.repository.static", "false" );
        engine.init();

        // Initialize my template repository. You can replace the "Hello $w" with your String.
        StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
        String templateName = "template_" + System.currentTimeMillis();
        repo.putStringResource( templateName, templateCode );

        // Set parameters for my template.
        VelocityContext context = new VelocityContext( variables );

        // Get and merge the template with my parameters.
        Template template = engine.getTemplate( templateName );
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return writer.toString();

    }

}
