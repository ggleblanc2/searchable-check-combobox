package com.ggl.searchable.check.combobox;

public abstract class BaseItem {
	
	private boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public abstract String toDisplayString();
	
}