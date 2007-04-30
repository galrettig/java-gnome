/*
 * Generator.java
 *
 * Copyright (c) 2007 Operational Dynamics Consulting Pty Ltd
 * 
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" See the LICENCE file for the terms governing usage and
 * redistribution.
 */
package com.operationaldynamics.codegen;

import java.io.PrintWriter;

/**
 * Base class of the code generator classes. Also houses numerous utility
 * functions used to convert from the C names used in the .defs data to Java
 * style names according to our requirements.
 * 
 * @author Andrew Cowie
 */
public abstract class Generator
{

    /**
     * Generate the header for a Java file. The resason for the split between
     * header and body is so that we can insert include statements into the
     * output stream. To be called once per Block[] representing each .defs
     * file.
     */
    public abstract void writeJavaHeader(PrintWriter out);

    /**
     * Generate Java code!
     */
    public abstract void writeJavaBody(PrintWriter out);

    /**
     * Generate the header for a C file. The split between header and body
     * isn't used at present, but it is exposed here for symmetry.
     */
    public abstract void writeCHeader(PrintWriter out);

    /**
     * Generate C code!
     */
    public abstract void writeCBody(PrintWriter out);

    /**
     * Turn "org.gnome.glib", "GtkButton" into "org_gnome_glib_GtkButton"
     */
    protected static final String encodeJavaClassName(String javaPackageName, String javaClassName) {
        
        return javaPackageName.replace('.', '_') + "_" + javaClassName;
    }

    /**
     * Turn "gtk_button_set_label" into "gtk_1button_1set_1label"
     */
    protected static final String encodeJavaMethodName(String javaClassName) {
        StringBuffer buf;
        int i;

        buf = new StringBuffer(javaClassName);
        i = 0;

        while ((i = buf.indexOf("_", i)) != -1) {
            i++;
            buf.insert(i, '1');
        }
        return buf.toString();

        // TODO this method can be written like this from java 1.5
        // return javaClassName.replace("_", "_1");
        // From 1.4 exists a similar function, that uses regular expresions.
        // This leads to simpler code, but probably more inefficient, so I 
        // prefer current code
    }

    /**
     * Convert a short ["python"] name to the camel case used by convention in
     * the Java world.
     */
    protected static final String toCamel(String shortName) {
        StringBuffer buf;
        int i;
        char ch;

        buf = new StringBuffer(shortName);
        i = 0;

        while ((i = buf.indexOf("_", i)) != -1) {
            buf.deleteCharAt(i);
            ch = buf.charAt(i);
            buf.setCharAt(i, Character.toUpperCase(ch));
        }

        return buf.toString();
    }
}
