/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.ale.proof.egistest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andre
 */
public class EgisTestUnitTest {

    public EgisTestUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Quick test to pass to demonstrate successful PR, in RL it will test go()<p>
     * This will test on of the helper functions
     */
    @Test
    public void successTest() {
        try{
        String text = "HaBoo";
        Element h2 = new Element("h2");
        Element div = new Element("div");
        div.appendText(text);
        h2.appendChild(div);        
        Class[] argClasses = new Class[1];
        argClasses[0] = Element.class;
        Method method = EgisTest.class.getDeclaredMethod("getTDText", argClasses);
        method.setAccessible(true);
        String value = (String)method.invoke(new EgisTest(), h2);
        
        assertEquals(value, text);
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Error: " + e.toString());
            assertTrue(false);
        }
    }
}
