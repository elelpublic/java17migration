package com.infodesire.java17migration;

import org.apache.velocity.runtime.parser.ParseException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MainTest {

  @Test
  public void velocityAndSpecialCharacters() throws ParseException {

    Main main = new Main();
    Map context = new HashMap();

    context.put( "who", "World" );
    assertEquals( "Hello World!", main.velocity( "Hello $who!", context ) );

    assertNotEquals( "Hello World", main.velocity( "Hello $who!", context ) );

    assertEquals( "Zurück", main.velocity( "Zurück", context ) );

  }

}

