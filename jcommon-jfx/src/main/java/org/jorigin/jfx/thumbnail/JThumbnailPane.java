package org.jorigin.jfx.thumbnail;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jorigin.jfx.control.MouseClickFilter;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

/**
 * A pane that enable to display and manage {@link JThumbnail thumbnails}.
 * @author Julien SEINTURIER - <a href="http://www.univ-tln.fr">Universit&eacute; de Toulon</a> / <a href="http://www.lis-lab.fr">CNRS LIS umr 7020</a> - <a href="https://github.com/jorigin/jcommon">github.com/jorigin/jcommon</a> (<a href="mailto:contact@jorigin.org">contact@jorigin.org</a>)
 * @param <T> the type of the object that are represented by the {@link JThumbnail thumbnails}.
 */
// TODO JThumbnailPane - Implements underlying SelectionModel
public class JThumbnailPane<T> extends BorderPane{
	
	/** The {@link JThumbnail thumbnails} that are attached to this pane */
	private ObservableList<JThumbnail<T>> thumbnails = null;

	/** The filtered {@link JThumbnail thumbnails} that are attached to this pane */
	private FilteredList<JThumbnail<T>> filteredThumbnails = null;

	/** The {@link JThumbnail thumbnails} that are currentely selected */
	private ObservableList<JThumbnail<T>> selectedThumbnails = null;
	
	/** The pane that holds the thumbnails. */
	private TilePane tilePane = null;

	/** The pane scroll */
	private ScrollPane scrollPane = null;

	/** The thumbnail pane style */
	private ObjectProperty<JThumbnailStyle> style = null;

	/**
	 * The filter that will match the {@link JThumbnail thumbnails} that will be displayed in this JThumbnailPane according to their {@link JThumbnail#getThumbnailContent() content}.
	 * thumbnails not matching the predicate will be hidden.
	 * <code>Null</code> filter means "always true" predicate, all thumbnails will be matched.
	 */
	private ObjectProperty<Predicate<? super T>> filter = null;

	/**
	 * This runner enable to handle multiple click mouse event.
	 */
	private MouseClickFilter mouseClickFilter = null;

	/** The thumbnail that have been involved within the last selection event */
	private JThumbnail<T> selectionLastThumbnail = null;

	/** The thumbnails activation handlers */
	private ObservableList<JThumbnailActivationHandler<T>> activationHandlers;
	
	/** The thumbnails activation handlers */
	private ObservableList<JThumbnailSelectionHandler<T>> selectionHandlers;
	
	/**
	 * Create a new empty and default styled {@link JThumbnail thumbnails} pane.
	 */
	public JThumbnailPane() {
		this(true, null);
	}

	/**
	 * Create a new empty thumbnail pane.
	 * @param style the {@ink JThumbnailParameters style} of the pane.
	 */
	public JThumbnailPane(JThumbnailStyle style) {
		this(true, style);
	}

	/**
	 * Create a new pane that enable to display and manage {@link JThumbnail thumbnails}.
	 * @param scrollable <code>true</code> if the pane has to be scrollable or <code>false</code> otherwise
	 * @param style the style of the pane
	 */
	public JThumbnailPane(boolean scrollable, JThumbnailStyle style) {

		super();

		getStyleClass().setAll("list-view");
		
		mouseClickFilter = new MouseClickFilter((e) -> {	
			processMouseClickedEvent(e);
		});
		
		thumbnails = FXCollections.observableArrayList();
		filteredThumbnails = new FilteredList<JThumbnail<T>>(thumbnails);
		selectedThumbnails = FXCollections.observableArrayList();
		
		tilePane = new TilePane();
		tilePane.setCache(true);
		tilePane.setCacheHint(CacheHint.SPEED);

		this.setOnMouseClicked((e) -> processMouseClickedEvent(e));
		
		if (scrollable) {
			scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			scrollPane.setContent(tilePane);
			setCenter(scrollPane);
		} else {
			setCenter(tilePane);
		}

		setStyle(style);
	}

	/**
	 * Add the given {@link JThumbnailActivationHandler thumbnail activation handler} to this component.
	 * @param handler the handler to add
	 * @return <code>true</code> if the given handler is added and <code>false</code> otherwise
	 * @see #removeThumbnailActivationHandler(JThumbnailActivationHandler)
	 */
	public boolean addThumbnailActivationHandler(JThumbnailActivationHandler<T> handler) {
		boolean ok = false;
		
		if (handler != null) {
			
			if (activationHandlers == null) {
				activationHandlers = FXCollections.observableArrayList();
			}
			
			if (!activationHandlers.contains(handler)) {
				ok = activationHandlers.add(handler);
			}
		}
		
		return ok;
	}
	
	/**
	 * Remove the given {@link JThumbnailActivationHandler thumbnail activation handler} from this component.
	 * @param handler the handler to remove
	 * @return <code>true</code> if the given handler is removed and <code>false</code> otherwise
	 * @see #addThumbnailActivationHandler(JThumbnailActivationHandler)
	 */
	public boolean removeThumbnailActivationHandler(JThumbnailActivationHandler<T> handler) {
		boolean ok = false;
		if ((handler != null) && (activationHandlers != null)) {
			ok = activationHandlers.remove(handler);
		}
		return ok;
	}
	
	/**
	 * Add the given {@link JThumbnailSelectionHandler thumbnail selection handler} to this component.
	 * @param handler the handler to add
	 * @return <code>true</code> if the given handler is added and <code>false</code> otherwise
	 * @see #removeThumbnailActivationHandler(JThumbnailActivationHandler)
	 */
	public boolean addThumbnailSelectionHandler(JThumbnailSelectionHandler<T> handler) {
		boolean ok = false;
		
		if (handler != null) {
			
			if (selectionHandlers == null) {
				selectionHandlers = FXCollections.observableArrayList();
			}
			
			if (!selectionHandlers.contains(handler)) {
				ok = selectionHandlers.add(handler);
			}
		}
		
		return ok;
	}
	
	/**
	 * Remove the given {@link JThumbnailSelectionHandler thumbnail selection handler} from this component.
	 * @param handler the handler to remove
	 * @return <code>true</code> if the given handler is removed and <code>false</code> otherwise
	 * @see #addThumbnailSelectionHandler(JThumbnailSelectionHandler)
	 */
	public boolean removeThumbnailSelectionHandler(JThumbnailSelectionHandler<T> handler) {
		boolean ok = false;
		if ((handler != null) && (selectionHandlers != null)) {
			ok = selectionHandlers.remove(handler);
		}
		return ok;
	}
	
	/**
	 * Get the <code>filter</code> property. This property describe the filter that is applied to the thumbnail.
	 * The filter match the {@link JThumbnail thumbnails} that have to be displayed according to their {@link JThumbnail#getThumbnailContent() content}.
	 * Thumbnails that are not matching the predicate will be hidden.
	 * @return the <code>filter</code> property
	 */
	public final ObjectProperty<Predicate<? super T>> filterProperty() {
		if (filter == null) {
			filter = new ObjectPropertyBase<Predicate<? super T>>() {

				@Override
				protected void invalidated() {
					filter();
				}

				@Override
				public Object getBean() {
					return JThumbnailPane.this;
				}

				@Override
				public String getName() {
					return "filter";
				}

			};
		}
		return filter;
	}

	/**
	 * Get the <code>style</code> property. This property describe the style that is applied to the thumbnail.
	 * @return the <code>style</code> property
	 */
	public final ObjectProperty<JThumbnailStyle> thumbnailStyleProperty() {
		if (style == null) {
			style = new ObjectPropertyBase<JThumbnailStyle>() {

				@Override
				protected void invalidated() {
					filter();
				}

				@Override
				public Object getBean() {
					return JThumbnailPane.this;
				}

				@Override
				public String getName() {
					return "filter";
				}

			};
		}
		return style;
	}

	/**
	 * Add the given {@link JThumbnail thumbnail} to this pane.
	 * @param thumbnail the thumbnail to add
	 * @return <code>true</code> if the thumbnail is successfully added and <code>false</code> otherwise
	 */
	public boolean addThumbnail(JThumbnail<T> thumbnail) {
		boolean ok = thumbnails.add(thumbnail);		

		if (ok) {

			thumbnail.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickFilter);

			if (!Platform.isFxApplicationThread()) {
				Platform.runLater(() -> tilePane.getChildren().add(thumbnail));
			} else {
				tilePane.getChildren().add(thumbnail);
			}
		}

		return ok;
	}

	/**
	 * Remove the given {@link JThumbnail thumbnail} from this pane.
	 * @param thumbnail the thumbnail to remove
	 * @return <code>true</code> if the thumbnail is successfully removed and <code>false</code> otherwise
	 */
	public boolean removeThumbnail(JThumbnail<T> thumbnail) {

		int index = thumbnails.indexOf(thumbnail);

		boolean ok = (thumbnails.remove(index) != null);	

		if (ok) {

			Platform.runLater(() -> tilePane.getChildren().remove(thumbnail));
			thumbnail.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickFilter);
		}

		return ok;
	}

	/**
	 * Set the style that this thumbnail pane has to use for display and layout.
	 * @param style the style that this thumbnail pane has to use for display and layout
	 */
	public void setStyle(JThumbnailStyle style) {

		tilePane.hgapProperty().unbind();
		tilePane.vgapProperty().unbind();
		tilePane.backgroundProperty().unbind();

		thumbnailStyleProperty().set(style);

		if (thumbnailStyleProperty().get() != null) {
			tilePane.hgapProperty().bind(thumbnailStyleProperty().get().thumbnailHGapProperty());
			tilePane.vgapProperty().bind(thumbnailStyleProperty().get().thumbnailVGapProperty());

			tilePane.backgroundProperty().bind(thumbnailStyleProperty().get().thumbnailPaneBackgroundProperty());
		}

		for(JThumbnail<?> thumbnail : thumbnails) {
			thumbnail.setThumbnailStyle(thumbnailStyleProperty().get());
		}
	}

	/**
	 * Get the items that are displayed within the thumbnail pane.
	 * If a filter is applied on the pane, only items that are attached to a visible thumbnail are returned.
	 * @return the items that are displayed within the thumbnail pane
	 */
	public List<T> getItems(){
		return this.filteredThumbnails.stream().map(JThumbnail::getThumbnailContent).collect(Collectors.toList());
	}

	/**
	 * Apply the filter defined by the given <code>predicate</code> to the displayed {@link JThumbnail thumbnails}. 
	 * All thumbnail that are not accepted by the filter are masked and cannot be selected or activated.
	 * if <code>null</code> is given, no filter is applied.
	 * @param predicate the filter predicate
	 */
	public void setFilter(Predicate<? super T> predicate) {

		// Setting this property value will trigger a call to filter() method.
		filterProperty().set(predicate);
	}

	/**
	 * Update the thumbnails display after a change on underlying data (for example ). 
	 */
	protected void update() {

		for (JThumbnail<?> t : filteredThumbnails.getSource()) {
			t.setVisible(false);
			t.setManaged(false);
		}

		for (JThumbnail<?> t : filteredThumbnails) {
			t.setVisible(true);
			t.setManaged(true);
		}
	}

	/**
	 * Filter the thumbnail according the attached {@link #filter}.
	 */
	private void filter() {
		if (this.filter != null) {
			this.filteredThumbnails.setPredicate((t) -> {return this.filter.get().test(t.getThumbnailContent());});
		} else {
			this.filteredThumbnails.setPredicate(null);
		}

		update();
	}

	/**
	 * Fire an activation to all registered handlers.
	 * @param thumbnail the activated thumbnail
	 */
	private void fireActivation(JThumbnail<T> thumbnail) {
		if (activationHandlers != null) {
			for(JThumbnailActivationHandler<T> h : activationHandlers) {
				h.handle(this, thumbnail);
			}
		}
	}
	
	/**
	 * Fire a selection to all registered handlers.
	 * @param thumbnails the selected thumbnails
	 */
	private void fireSelection(List<JThumbnail<T>> thumbnails) {
		if (activationHandlers != null) {
			for(JThumbnailSelectionHandler<T> h : selectionHandlers) {
				h.handle(this, thumbnails);
			}
		}
	}
	
	/**
	 * Process the given {@link MouseEvent mouse event}.
	 * @param e the event to process
	 */
	private void processMouseClickedEvent(MouseEvent e) {
		
		if (e.getSource() != null) {
			if (e.getSource() instanceof JThumbnail ) {

				@SuppressWarnings("unchecked")
				JThumbnail<T> thumbnail = (JThumbnail<T>)e.getSource();

				if (e.getButton() == MouseButton.PRIMARY) {
					if (e.getClickCount() > 1) {
						fireActivation(thumbnail);
					} else {

						// Multiple range selection
						if (e.isControlDown() && e.isShiftDown()) {
							// TODO JThumbnailPane - implements JThumbnailPane CTRL + SHITF selection

						// Simple Range selection
						} else if(e.isShiftDown()) {
							if (selectionLastThumbnail == null) {
								
								if (!thumbnail.isStateSelected()) {
									
									if ((filter == null) || filter.get().test(thumbnail.getThumbnailContent())) {
										thumbnail.setStateSelected(true);
										selectedThumbnails.add(thumbnail);
										selectionLastThumbnail = thumbnail;
										
										fireSelection(selectedThumbnails);
									}
								}
							} else {
								int index1 = thumbnails.indexOf(selectionLastThumbnail);
								int index2 = thumbnails.indexOf(thumbnail);

								JThumbnail<T> tmp = null;
								for(int i = Math.min(index1, index2); i <= Math.max(index1, index2); i++) {
									tmp = thumbnails.get(i);
									if (!tmp.isStateSelected()) {
										
										if ((filter == null) || filter.get().test(tmp.getThumbnailContent())) {
											tmp.setStateSelected(true);
											selectedThumbnails.add(tmp);
											selectionLastThumbnail = thumbnail;
										}
										
									}
								}
								fireSelection(selectedThumbnails);
							}

						// Additive selection / unselection
						} else if (e.isControlDown()) {
							
							// Unselect the thumbnail
							if (thumbnail.isStateSelected()) {
								thumbnail.setStateSelected(false);
								selectedThumbnails.remove(thumbnail);
								selectionLastThumbnail = null;
								
								fireSelection(selectedThumbnails);
								
							// Select the thumbnail
							} else {
								
								if ((filter == null) || filter.get().test(thumbnail.getThumbnailContent())) {
									thumbnail.setStateSelected(true);
									selectedThumbnails.add(thumbnail);
									selectionLastThumbnail = thumbnail;
									
									fireSelection(selectedThumbnails);
								}
							}


						// Simple selection
						} else {
							
							for(JThumbnail<?> t : selectedThumbnails) {
								t.setStateSelected(false);
							}						
							selectedThumbnails.clear();
							
							if (!thumbnail.isStateSelected() && ((filter == null) || filter.get().test(thumbnail.getThumbnailContent()))) {
								thumbnail.setStateSelected(true);
								selectedThumbnails.add(thumbnail);
							}
							
							selectionLastThumbnail = thumbnail;
							
							fireSelection(selectedThumbnails);
						}
					}
				}
				
				e.consume();			
			}
		}
	}
}
