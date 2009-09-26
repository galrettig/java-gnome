/*
 * ValidateEnchantInternals.java
 *
 * Copyright (c) 2009 Operational Dynamics Consulting Pty Ltd
 * 
 * The code in this file, and the suite it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" See the LICENCE file for the terms governing usage and
 * redistribution.
 */
package org.freedesktop.enchant;

import org.gnome.gtk.GraphicalTestCase;

/**
 * @author Andrew Cowie
 */
public class ValidateEnchantInternals extends GraphicalTestCase
{
    public final void testLibraryInitialization() {
        Enchant.init();

        assertNotNull(Enchant.getDefault());
    }

    public final void testDictionaryCreation() {
        final Dictionary dict;

        assertNotNull(Enchant.getDefault());

        dict = Enchant.requestDictionary("en");
        assertNotNull(dict);
    }

    public final void testCheckWord() {
        final Dictionary dict;
        int result;

        dict = Enchant.requestDictionary("en");

        result = dict.check("hello");
        assertTrue(result == 0);

        result = dict.check("insain");
        assertTrue(result > 0);
    }

    public final void testSuggestionsWordCorrect() {
        final Dictionary dict;
        String[] list;
        int i, count;

        dict = Enchant.requestDictionary("en");

        /*
         * Dramatically complicating matters, if the word is correct, you can
         * still get suggestions.
         */

        list = dict.suggest("catamaran");
        assertTrue(list.length > 0);

        count = 0;
        for (i = 0; i < list.length; i++) {
            if (list[i].equals("catamaran")) {
                count++;
            }
        }
        assertEquals("Our correctly spelt word should be a suggestion!", 1, count);
    }

    public final void testSuggestionsWordIncorrect() {
        final Dictionary dict;
        String[] list;
        int i, count;

        dict = Enchant.requestDictionary("en");

        list = dict.suggest("katamaran");
        assertTrue(list.length > 0);

        count = 0;
        for (i = 0; i < list.length; i++) {
            if (list[i].equals("catamaran")) {
                count++;
            }
        }
        assertEquals("Should only be one suggestion correctly spelling our word!", 1, count);
    }

    public final void testSuggestionsWordUnknown() {
        final String bogus;
        final Dictionary dict;
        String[] list;
        int result;

        dict = Enchant.requestDictionary("en");

        /*
         * Finding a word that really has no suggestions is tough. Good old
         * supercalifragilisticexpialidocious would have been good (it offered
         * no suggestions in gedit when writing this test) but it's still a
         * real word. Try this one.
         */

        bogus = "metakathamrthamorosishmockinggutgoberlize";

        result = dict.check(bogus);

        if (result == 0) {
            fail("Need a better word to use, one known not to result in any suggestions");
        }

        list = dict.suggest(bogus);
        assertNull(list);
    }
}
