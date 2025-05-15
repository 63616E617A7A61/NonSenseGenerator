package com.g63616e617a7a61.nonsensegenerator;

import static org.junit.Assert.*;
import org.junit.Test;

import com.g63616e617a7a61.nonsensegenerator.model.Template;
/**
 * Unit test for simple App.
 */
public class TemplateTest
{
    @Test
    public void loadShouldLoad()
    {
        Template t = new Template();
        assertFalse(t.getTemplate().equals("null"));
    }

    @Test
    public void getterShouldGet()
    {
        Template t = new Template();
        assertNotNull(t.getTemplate());
    }
}
