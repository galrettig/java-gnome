/*
 * Block.java
 *
 * Copyright (c) 2007 Operational Dynamics Consulting Pty Ltd
 * 
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" See the LICENCE file for the terms governing usage and
 * redistribution.
 */
package com.operationaldynamics.codegen;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Base class representing a block s-expression .defs data. The system is
 * predicated around the notion that by the time you are done creating a Block
 * object, you know that Thing (type) the Block refers to, and can look it up
 * and set it accordingly.
 * 
 * <p>
 * All members are to be protected, and setters are to be used (via reflection
 * in the DefsParser) to map the characteristics in each block to fields.
 * 
 * @author Andrew Cowie
 */
abstract class Block
{
    /**
     * Strictly speaking, this isn't here, but down in MethodBlock and
     * VirtualBlock. But it needs to be figured out before FunctionGenerator
     * can be used in a (define-function ...), and it's present for all the
     * sub block types. As well, by having it here, we can enforce the '*'
     * business in setOfObject() so that lookups are consistent.
     */
    protected String ofObject;

    /**
     * The short "python" name for this Object/Function/Method/Constructor/etc
     */
    protected final String blockName;

    protected Block(final String blockName, final List characteristics) {
        this.blockName = blockName;

        processCharacteristics(characteristics);
    }

    /**
     * Add the '*' pointer character because our indexes are built on the
     * convention that the type that can be looked up is "GObject*", not
     * "GObject".
     */
    final void setOfObject(final String ofObject) {
        this.ofObject = ofObject + '*';
    }

    /**
     * Reflection engine to populate object members based on key/value pairs
     * in list
     */
    private void processCharacteristics(List list) {
        Iterator iter;

        if (list == null) {
            return;
        }

        iter = list.iterator();
        while (iter.hasNext()) {
            final String[] array;
            final String name, value;
            final Class[] signature;
            Method setter;
            Class target;

            array = (String[]) iter.next();

            name = nameToMethod(array[0]);
            value = array[1];

            /*
             * our setters are always setSomething(String)
             */
            signature = new Class[] {
                String.class
            };

            /*
             * Class's getMethod() searches super classes, but only for public
             * methods. So we have to use getDeclaredMethod() and walk up the
             * hierarchy. What a bore.
             */

            target = this.getClass();
            setter = null;

            while (target != Object.class) {
                try {
                    setter = target.getDeclaredMethod(name, signature);
                    break;
                } catch (NoSuchMethodException nsme) {
                    target = target.getSuperclass();
                }
            }
            if (setter == null) {
                throw new IllegalStateException("setter " + name + " not found");
            }

            /*
             * Call setter.
             */

            try {
                setter.invoke(this, new Object[] {
                    value
                });
            } catch (IllegalArgumentException e) {
                // If all screwed up
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // This shouldn't happen - setters are all protected here or
                // in
                // super classes above us.
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // the setter itself threw an exception! Crazy.
                e.printStackTrace();
            }
        }
    }

    /**
     * .defs files have characteristic names like "c-name" and "return-value".
     * Convert these to "setCName" and "setReturnValue" for calling setter.
     */
    static final String nameToMethod(String key) {
        StringBuffer buf;
        int i;
        char ch;

        buf = new StringBuffer(key);
        buf.insert(0, '-');

        i = 0;
        while ((i = buf.indexOf("-", i)) != -1) {
            buf.deleteCharAt(i);
            ch = buf.charAt(i);
            buf.setCharAt(i, Character.toUpperCase(ch));
        }

        buf.insert(0, "set");

        return buf.toString();
    }

    /**
     * Get the code factory appropriate to this Block type.
     */
    abstract Generator generator();

    public String toString() {
        final StringBuffer buf;
        Class target;

        buf = new StringBuffer();

        buf.append(this.getClass().getName());
        buf.append("\n");

        target = this.getClass();
        while (target != Object.class) {
            Field[] fields;
            int i;

            fields = target.getDeclaredFields();
            for (i = 0; i < fields.length; i++) {
                String name;
                Object value;

                // There's something weird about a few extra Class objects
                // showing up.
                if (fields[i].getType() == Class.class) {
                    continue;
                }

                buf.append("\t");
                name = fields[i].getName();
                buf.append(name);
                buf.append(": ");

                try {
                    value = fields[i].get(this);
                } catch (IllegalArgumentException e) {
                    // huh?
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e) {
                    // go figure.
                    e.printStackTrace();
                    continue;
                }

                if (name.equals("parameters")) {
                    buf.append("\n");
                    String[][] p = (String[][]) value;
                    for (int j = 0; j < p.length; j++) {
                        buf.append("\t\t");
                        buf.append(p[j][0]);
                        buf.append(" ");
                        buf.append(p[j][1]);
                        buf.append("\n");
                    }
                } else {
                    buf.append(value);
                    buf.append("\n");
                }
            }
            target = target.getSuperclass();
        }

        return buf.toString();
    }
}
