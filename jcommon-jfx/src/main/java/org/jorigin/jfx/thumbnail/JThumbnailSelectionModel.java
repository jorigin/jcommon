package org.jorigin.jfx.thumbnail;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

/**
 * A {@link MultipleSelectionModel selection model} that is used by {@link JThumbnailPane}
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 *
 * @param <T> the type of the objects that are managed by the attached {@link JThumbnailPane}
 */
public class JThumbnailSelectionModel<T> extends MultipleSelectionModel<T>{

	/** The items that are managed by the attached {@link JThumbnailPane}. */
	private List<T> items = null;
	
	/** The selected indices */
	private ObservableList<Integer> selectedIndices;
	
	/** The selected items */
	private ObservableList<T> selectedItems;
	
	/**
	 * Create a new empty {@link JThumbnailPane} selection model.
	 */
	public JThumbnailSelectionModel() {
		this(null);
	}
	
	/**
	 * Create a new {@link JThumbnailPane} selection model based on the given <code>items</code>.
	 * @param items the items that have to be managed by this selection model
	 */
	public JThumbnailSelectionModel(List<T> items) {
		if (items != null) {
		  this.items = items;
		  
		  if (items.size() > 0) {
			  selectedIndices = FXCollections.observableList(new ArrayList<Integer>(items.size()));
			  selectedItems = FXCollections.observableList(new ArrayList<T>(items.size()));
		  } else {
			  selectedIndices = FXCollections.observableArrayList();
			  selectedItems = FXCollections.observableArrayList();
		  }
		} else {
			this.items = new ArrayList<T>();
			selectedIndices = FXCollections.observableArrayList();
			selectedItems = FXCollections.observableArrayList();
		}
	}
	
	@Override
	public ObservableList<Integer> getSelectedIndices() {
		return selectedIndices;
	}

	@Override
	public ObservableList<T> getSelectedItems() {
		return selectedItems;
	}

	@Override
	public void selectIndices(int index, int... indices) {
		selectedIndices.clear();
		selectedIndices.add(index);
		
		for(int i = 0; i < indices.length; i++) {
			selectedIndices.add(indices[i]);
		}
	}

	@Override
	public void selectAll() {
		selectedIndices.clear();
		selectedItems.clear();
		
		if (items != null) {
			for(int i = 0; i < items.size(); i++) {
				selectedIndices.add(i);
				selectedItems.add(items.get(i));
			}
		}
	}

	@Override
	public void selectFirst() {
		selectedIndices.clear();
		selectedItems.clear();
		
		if ((items != null) && (items.size() > 0)){
			selectedIndices.add(0);
			selectedItems.add(items.get(0));
		}
	}

	@Override
	public void selectLast() {
		selectedIndices.clear();
		selectedItems.clear();
		
		if ((items != null) && (items.size() > 0)){
			selectedIndices.add(items.size() - 1);
			selectedItems.add(items.get(items.size() - 1));
		}
	}

	@Override
	public void clearAndSelect(int index) {
		
	}

	@Override
	public void select(int index) {
		// TODO Implements select(int) method.
	}

	@Override
	public void select(T obj) {
		// TODO Implements select(Object) method.
	}

	@Override
	public void clearSelection(int index) {
		
	}

	@Override
	public void clearSelection() {
		selectedIndices.clear();
		selectedItems.clear();
	}

	@Override
	public boolean isSelected(int index) {
		return selectedIndices.contains(index);
	}

	@Override
	public boolean isEmpty() {
		return selectedIndices.size() <= 0;
	}

	@Override
	public void selectPrevious() {
		// TODO Implements selectPrevious() method
		
	}

	@Override
	public void selectNext() {
		// TODO Implements selectNext() method
		
	}

}
