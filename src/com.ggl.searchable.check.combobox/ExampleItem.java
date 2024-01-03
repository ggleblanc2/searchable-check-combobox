package com.ggl.searchable.check.combobox;

 	public class ExampleItem extends BaseItem {
  		
  		private String item;
  		
  		public ExampleItem(String item, boolean isSelected) {
  			this.item = item;
  			setSelected(isSelected);
  		}

		@Override
		public String toDisplayString() {
			return item;
		}
  	}
 	