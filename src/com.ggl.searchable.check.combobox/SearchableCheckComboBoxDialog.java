package com.ggl.searchable.check.combobox;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * <p>
 * This class creates a <code>JDialog</code> that displays a searchable display
 * of items with checkboxes. Use this class to create a <code>JDialog</code> to
 * allow the user to select multiple items from a long list of possible items.
 * This <code>JDialog</code> is blocking. The user has to click either the
 * <code>OK</code> or <code>Cancel</code> buttons, or left-click the X in the
 * upper right, to close the <code>JDialog</code>.
 * </p>
 * 
 * <p>
 * One possible application that comes to mind is a list of building supplies. A
 * 2x4, as one example, comes in pine or oak, and can come in 8, 10, or 12 foot
 * lengths. The user can shorten the list of all building supplies to just
 * display 2x4s.
 * </p>
 * 
 * <p>
 * The default font is the system dialog font, 12 point. The labels will be bold
 * and the entry field and checkboxes will be plain.
 * </p>
 * 
 * @author Gilbert G. Le Blanc, written 3 Jan 2004
 * @since 1.8
 * @version 1
 *
 * @param <T> - A class that extends the abstract class <code>BaseItem</code>.
 *            This is usually a decorator class around some other class that
 *            contains information about the displayed text. The
 *            <code>BaseItem</code> class adds a boolean <code>isSelected</code>
 *            indicator and a <code>toDisplayString</code> method. The
 *            <code>toDisplayString</code> allows the decorator class to
 *            determine how to display the object item and leaves the
 *            <code>toString</code> method free for debugging use.
 * 
 * @see JComboBox
 * @see JDialog
 * @see JPanel
 * @see JScrollPane
 */

public class SearchableCheckComboBoxDialog<T extends BaseItem> extends JDialog {

	private static final long serialVersionUID = 1L;

	public static int OK_BUTTON_PRESSED = 0;
	public static int CANCEL_NUTTON_PRESSED = 4;

	private int returnCode;

	private SearchableCheckComboBox<T> comboBox;

	/**
	 * This constructor creates the searchable, checkbox, combobox
	 * <code>JDialog</code>.
	 * 
	 * @param frame    - The <code>JFrame</code> of the application. This is
	 *                 used to center the <code>JDialog</code> on the
	 *                 <code>JFrame</code>.
	 * @param font     - The font for the searchable, checkbox, combobox
	 *                 <code>JDialog</code>.
	 * @param itemList - The <code>List</code> of item objects that extend the
	 *                 abstract class <code>BaseItem</code>.
	 */
	public SearchableCheckComboBoxDialog(JFrame frame, Font font,
			List<T> itemList) {
		super(frame, "Select Greek Letters", true);
		comboBox = new SearchableCheckComboBox<T>(itemList);
		comboBox.addDisposeListener(new DialogDisposeListener());
		comboBox.setFont(font);
		JPanel panel = comboBox.createSearchableCheckComboBox();
		add(panel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	/**
	 * <p>
	 * This method returns the return code.  Two <code>static</code>
	 * <code>int</code> values are provided for self-documenting
	 * calling code.
	 * </p>
	 * 
	 * <p>
	 * The possible return codes are:
	 * <ul>
	 * <li>0 - User pressed the <code>OK</code> button.</li>
	 * <li>4 - User cancelled the <code>JDialog</code<.</li>
	 * </ul>
	 * </p>
	 * 
	 * @return - The <code>int</code> return code.
	 */
	public int getReturnCode() {
		return returnCode;
	}

	/**
	 * <p>
	 * This method returns the <code>List</code> of items extending the abstract
	 * <code>BaseItem</code> class with the <code>isSelected</code> indicators
	 * set.
	 * </p>
	 * 
	 * <p>
	 * This method should only be called if the user pressed the <code>OK</code>
	 * button.
	 * </p>
	 * 
	 * @return - A <code>List</code> of items extending the abstract
	 *         <code>BaseItem</code> class.
	 */
	public List<T> getAllItems() {
		return comboBox.getAllItems();
	}

	private class DialogDisposeListener implements DisposeListener {

		@Override
		public void disposePerformed() {
			if (comboBox.isOkButtonPressed()) {
				returnCode = OK_BUTTON_PRESSED;
			} else {
				returnCode = CANCEL_NUTTON_PRESSED;
			}
			dispose();
		}

	}

}
