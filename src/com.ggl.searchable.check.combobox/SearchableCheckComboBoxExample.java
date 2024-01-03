package com.ggl.searchable.check.combobox;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SearchableCheckComboBoxExample implements Runnable {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new SearchableCheckComboBoxExample());
	}

	private List<ExampleItem> exampleItemList;

	private JFrame frame;

	private JTextArea textArea;

	public SearchableCheckComboBoxExample() {
		this.exampleItemList = generateItemList();
	}

	@Override
	public void run() {
		frame = new JFrame("Searchable Check ComboBox Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(createMainPanel(), BorderLayout.CENTER);

		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		textArea = new JTextArea(5, 40);
		textArea.setEditable(false);
		textArea.setMargin(new Insets(0, 5, 0, 5));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		updateMainPanel();
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.add(scrollPane, BorderLayout.CENTER);

		JButton button = new JButton("Select Greek Letters");
		panel.add(button, BorderLayout.SOUTH);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Font font = textArea.getFont();
				SearchableCheckComboBoxDialog<ExampleItem> dialog = 
						new SearchableCheckComboBoxDialog<>(frame, font, exampleItemList);
				if (dialog.getReturnCode() == SearchableCheckComboBoxDialog.OK_BUTTON_PRESSED) {
					exampleItemList = dialog.getAllItems();
					updateMainPanel();
				}
			}
		});

		return panel;
	}

	private void updateMainPanel() {
		List<ExampleItem> selectedItemList = new ArrayList<>();
		for (ExampleItem exampleItem : exampleItemList) {
			if (exampleItem.isSelected()) {
				selectedItemList.add(exampleItem);
			}
		}

		String text = "No greek letters were selected.";
		if (selectedItemList.size() > 1) {
			text = "";
			for (int index = 0; index < selectedItemList.size(); index++) {
				text += selectedItemList.get(index).toDisplayString();
				if (index < (selectedItemList.size() - 2)) {
					text += ", ";
				} else if (index == (selectedItemList.size() - 2)) {
					if (selectedItemList.size() == 2) {
						text += " and ";
					} else {
						text += ", and ";
					}
				}
			}
			text += " were selected.";
		} else if (selectedItemList.size() == 1) {
			text = selectedItemList.get(0).toDisplayString();
			text += " was selected.";
		}

		textArea.setText(text);
	}

	private List<ExampleItem> generateItemList() {
		List<ExampleItem> list = new ArrayList<>();
		list.add(new ExampleItem("Alpha", false));
		list.add(new ExampleItem("Beta", false));
		list.add(new ExampleItem("Gamma", true));
		list.add(new ExampleItem("Delta", false));
		list.add(new ExampleItem("Epsilon", false));
		list.add(new ExampleItem("Zeta", false));
		list.add(new ExampleItem("Eta", false));
		list.add(new ExampleItem("Theta", false));
		list.add(new ExampleItem("Iota", false));
		list.add(new ExampleItem("Kappa", false));
		list.add(new ExampleItem("Lambda", false));
		list.add(new ExampleItem("Mu", false));
		list.add(new ExampleItem("Nu", false));
		list.add(new ExampleItem("Xi", false));
		list.add(new ExampleItem("Omicron", false));
		list.add(new ExampleItem("Pi", false));
		list.add(new ExampleItem("Rho", false));
		list.add(new ExampleItem("Sigma", false));
		list.add(new ExampleItem("Tau", false));
		list.add(new ExampleItem("Upsilon", false));
		list.add(new ExampleItem("Phi", false));
		list.add(new ExampleItem("Chi", false));
		list.add(new ExampleItem("Psi", false));
		list.add(new ExampleItem("Omega", false));

		return list;
	}

}
