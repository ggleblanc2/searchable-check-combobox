package com.ggl.searchable.check.combobox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * <p>
 * This class creates a <code>JPanel</code> that displays a searchable display
 * of items with checkboxes. Use this class to create a <code>JDialog</code> to
 * allow the user to select multiple items from a long list of possible items.
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
 * @see JPanel
 * @see JScrollPane
 */
public class SearchableCheckComboBox<T extends BaseItem> {

	private boolean okButtonPressed;

	private int visibleRowCount;

	private Dimension itemDimension;

	private DisposeListener listener;

	private Font font, labelFont;

	private JCheckBox[] itemCheckBoxes;

	private final List<T> allItems, selectedItems;

	private JScrollPane scrollPane;

	private JTextField searchField;

	/**
	 * <p>
	 * This constructor reads the list of items to be displayed in the
	 * searchable, checkbox, combobox.
	 * </p>
	 * 
	 * @param allItems - A <code>List</code> of objects that extend the abstract
	 *                 <code>BaseItem</code> class. This is the list that will
	 *                 be displayed in the searchable, checkbox, combobox.
	 */
	public SearchableCheckComboBox(List<T> allItems) {
		this.allItems = List.copyOf(allItems);
		this.selectedItems = new ArrayList<>(allItems);
		this.font = new Font(Font.DIALOG, Font.PLAIN, 12);
		this.labelFont = font.deriveFont(Font.BOLD);
		this.visibleRowCount = 5;
		this.okButtonPressed = false;
	}

	/**
	 * <p>
	 * This method lets you set the font of the searchable, checkbox, combobox.
	 * The labels will be bold and the entry field and checkboxes will be plain.
	 * </p>
	 * 
	 * @param font - The desired <code>Font</code>
	 */
	public void setFont(Font font) {
		this.font = font.deriveFont(Font.PLAIN);
		this.labelFont = font.deriveFont(Font.BOLD);
	}

	/**
	 * <p>
	 * This method adds the <code>DisposeListener</code> to the class. The
	 * <code>DisposeListener</code> allows the <code>OK</code> and
	 * <code>Cancel</code> buttons to tell the calling <code>JDialog</code> to
	 * dispose.
	 * </p>
	 * 
	 * <p>
	 * This method <strong>must</strong> be called and set by the calling
	 * <code>JDialog</code>.
	 * </p>
	 * 
	 * <p>
	 * The <code>DisposeListener</code> is a simple example of the many Swing
	 * listeners.
	 * </p>
	 * 
	 * @param listener - The <code>DisposeListener</code>.
	 */
	public void addDisposeListener(DisposeListener listener) {
		this.listener = listener;
	}

	/**
	 * This method lets the calling class know whether or not the
	 * <code>OK</code> button was pressed.
	 * 
	 * @return - A boolean indicating whether or not the <code>OK</code> button
	 *         was pressed.
	 */
	public boolean isOkButtonPressed() {
		return okButtonPressed;
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
		return allItems;
	}

	/**
	 * This method is called after all initialization parameters are set.
	 * 
	 * @return - A <code>JPanel</code> with the searchable, checkbox, combobox
	 *         display,
	 */
	public JPanel createSearchableCheckComboBox() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

		JPanel searchPanel = new JPanel(new FlowLayout());

		JLabel label = new JLabel("Search:");
		label.setFont(labelFont);
		searchPanel.add(label);

		searchField = new JTextField(15);
		searchField.setFont(font);
		searchPanel.add(searchField);

		Document doc = searchField.getDocument();
		doc.addDocumentListener(new SearchFieldListener());

		panel.add(searchPanel, BorderLayout.NORTH);

		JPanel checkPanel = createCheckPanel();
		scrollPane = new JScrollPane(checkPanel);
		Dimension d = scrollPane.getPreferredSize();
		int height = visibleRowCount * itemDimension.height;
		scrollPane.setPreferredSize(new Dimension(d.width, height));

		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.addAdjustmentListener(new ScrollBarListener());
		int max = selectedItems.size() * itemDimension.height;
		scrollBar.setBlockIncrement(itemDimension.height);
		scrollBar.setUnitIncrement(itemDimension.height);
		scrollBar.setMinimum(0);
		scrollBar.setMaximum(max);
		scrollBar.setValue(0);

		panel.add(scrollPane, BorderLayout.CENTER);

		panel.add(createButtonPanel(), BorderLayout.SOUTH);

		return panel;
	}

	private void getDocumentText(Document doc) {
		String text;
		try {
			text = doc.getText(0, doc.getLength());
			updateScrollBarView(text.toLowerCase());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void updateScrollBarView(String text) {
		if (text.length() < 1) {
			resetCheckPanel();
		} else {
			updateCheckPanel(text);
		}
	}

	private void resetCheckPanel() {
		selectedItems.clear();
		for (T t : allItems) {
			selectedItems.add(t);
		}
		updateScrollPaneViewPort();
	}

	private void updateCheckPanel(String text) {
		selectedItems.clear();
		for (T t : allItems) {
			String displayString = t.toDisplayString().toLowerCase();
			if (displayString.contains(text)) {
				selectedItems.add(t);
			}
		}
		updateScrollPaneViewPort();
	}

	private void updateScrollPaneViewPort() {
		JPanel panel = createCheckPanel();
		scrollPane.getViewport().removeAll();
		scrollPane.getViewport().add(panel);
	}

	private JPanel createCheckPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		itemCheckBoxes = new JCheckBox[selectedItems.size()];
		for (int index = 0; index < selectedItems.size(); index++) {
			T t = selectedItems.get(index);
			itemCheckBoxes[index] = new JCheckBox(t.toDisplayString());
			itemCheckBoxes[index].setFont(font);
			itemCheckBoxes[index].setSelected(t.isSelected());

			if (index == 0) {
				itemDimension = itemCheckBoxes[index].getPreferredSize();
			}
			panel.add(itemCheckBoxes[index]);
		}

		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JButton selectAllButton = new JButton("Select All");
		selectAllButton.addActionListener(new SelectAllListener(true));
		selectAllButton.setFont(labelFont);
		panel.add(selectAllButton);

		JButton deselectAllButton = new JButton("Deselect All");
		deselectAllButton.addActionListener(new SelectAllListener(false));
		deselectAllButton.setFont(labelFont);
		panel.add(deselectAllButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(event -> {
			getCheckedState();
			okButtonPressed = true;
			listener.disposePerformed();
		});
		okButton.setFont(labelFont);
		panel.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(event -> {
			listener.disposePerformed();
		});
		cancelButton.setFont(labelFont);
		panel.add(cancelButton);

		return panel;
	}

	private void getCheckedState() {
		for (int index = 0; index < selectedItems.size(); index++) {
			T t = selectedItems.get(index);
			t.setSelected(itemCheckBoxes[index].isSelected());
		}
	}

	private class SelectAllListener implements ActionListener {

		private final boolean b;

		public SelectAllListener(boolean b) {
			this.b = b;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			setSelection(b);
			getDocumentText(searchField.getDocument());
		}

		private void setSelection(boolean b) {
			for (T t : selectedItems) {
				t.setSelected(b);
			}
		}

	}

	/**
	 * This class fixes the scroll bar pointer so it will stop on the boundaries
	 * of an item <code>JCheckBox</code>.
	 *
	 */
	private class ScrollBarListener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent event) {
			JScrollBar scrollBar = (JScrollBar) event.getSource();
			int value = scrollBar.getValue();
			int index = value / itemDimension.height;
			int offset = value % itemDimension.height;
			if (offset > itemDimension.height / 2) {
				scrollBar.setValue((index + 1) * itemDimension.height);
			} else {
				scrollBar.setValue(index * itemDimension.height);
			}
		}

	}

	private class SearchFieldListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent event) {
			getDocumentText(event.getDocument());
		}

		@Override
		public void removeUpdate(DocumentEvent event) {
			getDocumentText(event.getDocument());
		}

		@Override
		public void changedUpdate(DocumentEvent event) {
			getDocumentText(event.getDocument());
		}

	}

}
