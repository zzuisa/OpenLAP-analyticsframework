package com.openlap;

import static org.junit.Assert.assertTrue;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import org.attoparser.util.TextUtil;
import org.junit.Test;
import org.thymeleaf.util.TextUtils;

import javax.swing.plaf.TextUI;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 Unit test for simple App.
 */
public class AppTest {
    /**
     Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        String a = "234,455,66";
        LinkedHashMap linkedHashMap = new LinkedHashMap() {{
            put("a", 1);
            put("B", 2);
            put("C", 3);
        }};
        Object[] objects = Arrays.stream(a.split(",")).mapToDouble(Double::parseDouble).boxed().collect(Collectors.toList()).toArray();
        String listString = Arrays.stream(a.split(",")).mapToDouble(Double::parseDouble).boxed().collect(Collectors.toList())
                .stream().map(Object::toString)
                .collect(Collectors.joining(", "));

        System.out.println(listString);
        assertTrue(true);
        System.out.println("linkedHashMap.get(0) = " + linkedHashMap.get(0));
        linkedHashMap.keySet().forEach(System.out::println);
    }
}
