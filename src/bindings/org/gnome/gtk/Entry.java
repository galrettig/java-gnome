/*
 * Entry.java
 *
 * Copyright (c) 2007-2008 Operational Dynamics Consulting Pty Ltd, and Others
 *
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" plus the "Classpath Exception" (you may link to this code as a
 * library into other programs provided you don't make a derivation of it).
 * See the LICENCE file for the terms governing usage and redistribution.
 */
package org.gnome.gtk;

/**
 * A data entry field allowing the user to input a single line of text.
 * 
 * @author Sebastian Mancke
 * @author Andrew Cowie
 * @since 4.0.3
 */
public class Entry extends Widget implements Editable, CellEditable
{
    protected Entry(long pointer) {
        super(pointer);
    }

    /**
     * Construct a new Entry
     */
    public Entry() {
        this(GtkEntry.createEntry());
    }

    /**
     * Construct a new Entry, initialized with the specified text
     */
    public Entry(String text) {
        this(GtkEntry.createEntry());
        setText(text);
    }

    /**
     * Replace the current contents of the Entry with the supplied text.
     */
    public void setText(String text) {
        GtkEntry.setText(this, text);
    }

    /**
     * Get the text currently showing in the Entry. This is typically the most
     * significant method as it is the one you use to get the result of the
     * user's activity upon receiving a {@link Entry.ACTIVATE ACTIVATE}
     * signal.
     */
    public String getText() {
        return GtkEntry.getText(this);
    }

    /**
     * Specify the maximum number of characters the user is allowed to enter.
     * Note that if the current text in the Entry is longer than the specified
     * length, the contents will be truncated!
     * 
     * @param max
     *            A value of <code>0</code> indicates no maximum length.
     */
    public void setMaxLength(int max) {
        GtkEntry.setMaxLength(this, max);
    }

    /**
     * Returns the current maximum width, in characters, the text in the Entry
     * is allowed to be.
     */
    public int getMaxLength() {
        return GtkEntry.getMaxLength(this);
    }

    /**
     * Set whether the text in the entry is visible or obscured. This is
     * typically used for password fields.
     * 
     * When set to be not visible, characters entered are shown with a
     * <code>*</code> instead. This default can be changed with
     * {@link #setInvisibleChar(char) setInvisibleChar()}.
     * 
     * @param visible
     *            true for showing, false for hiding
     */
    public void setVisibleChars(boolean visible) {
        GtkEntry.setVisibility(this, visible);
    }

    /**
     * Returns the state of whether text in the Entry are visible or hidden by
     * an obscuring character.
     * 
     * @return <code>true</code> if characters entered are visible,
     *         <code>false</code> if obscured.
     */
    public boolean isVisibleChars() {
        return GtkEntry.getVisibility(this);
    }

    /**
     * Change the character used to obscure text when
     * {@link #setVisibleChars(boolean) setVisibleChars()} is false.
     * 
     * @param replacement
     *            The new character to be used to obscure text. A value of
     *            <code>0</code> will cause no feedback to displayed at all
     *            when the user is typing in the Entry.
     */
    public void setInvisibleChar(char replacement) {
        GtkEntry.setInvisibleChar(this, replacement);
    }

    /**
     * Set whether the text in the Entry can be change by the user.
     */
    public void setEditable(boolean editable) {
        GtkEntry.setEditable(this, editable);
    }

    /**
     * The <code>activate</code> signal occurs when the user presses
     * &lt;RETURN&gt; in an Entry.
     */
    public interface ACTIVATE extends GtkEntry.ACTIVATE
    {
        public void onActivate(Entry source);
    }

    /**
     * Connects an {@link ACTIVATE} handler to the Widget.
     * 
     * @since 4.0.3
     */
    public void connect(ACTIVATE handler) {
        GtkEntry.connect(this, handler);
    }

    public void setPosition(int position) {
        if (position < -1) {
            throw new IllegalArgumentException(
                    "Position must be -1 to indicate you want it after the last character.");
        }
        GtkEntry.setPosition(this, position);
    }

    /**
     * Request that the width of this Entry be wide enough for a given number
     * of characters.
     * 
     * <p>
     * As with all font related operations, there are a number of competing
     * approximations involved. In particular, this method operates by
     * influencing the size <i>requested</i> by this Widget; the box packing
     * model will still have the final say in the size allocation phase.
     * 
     * <p>
     * See also Label's {@link Label#setWidthChars(int) setWidthChars()}; the
     * challenges and constraints involved are similar.
     * 
     * @param width
     *            A setting of <code>-1</code> will return the Entry to
     *            normal sizing behaviour.
     * @since 4.0.6
     */
    public void setWidthChars(int width) {
        GtkEntry.setWidthChars(this, width);
    }

    /**
     * Connect a <code>CHANGED</code> handler.
     * 
     * @since 4.0.6
     */
    public void connect(CHANGED handler) {
        GtkEditable.connect(this, handler);
    }

    public void selectRegion(int start, int end) {
        GtkEditable.selectRegion(this, start, end);
    }

    /**
     * Set the alignment of the the text being displayed in the Entry.
     * 
     * @param xalign
     *            A value from <code>0.0f</code> for fully left-aligned
     *            through <code>1.0f</code> for fully right-aligned. You can
     *            use the constants {@link Alignment#LEFT LEFT},
     *            {@link Alignment#CENTER CENTER} and
     *            {@link Alignment#RIGHT RIGHT} in Alignment for convenience
     *            if you like. No, this has nothing to do with politics.
     * @since 4.0.6
     */
    /*
     * Supposedly this reverses for RTL text layout. It'd be nice of someone
     * could test that and document it to that effect if true.
     */
    public void setAlignment(float xalign) {
        if ((xalign < 0.0) || (xalign > 1.0)) {
            throw new IllegalArgumentException("xalign must be between 0.0 and 1.0");
        }
        GtkEntry.setAlignment(this, xalign);
    }
}
