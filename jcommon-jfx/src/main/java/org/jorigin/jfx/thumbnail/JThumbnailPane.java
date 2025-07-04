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

	// The style that is applied to the thumbnails.
	private ObjectProperty<JThumbnailStyle> thumbnailStyle = null;

	// The filter that is applied to the thumbnails.
	// This filter is used to select the {@link JThumbnail thumbnails} that have to be displayed.
	// If <code>null</code> is given, no filter is applied.
	private ObjectProperty<Predicate<? super T>> thumbnailFilter = null;

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
	 * @param style the {@link JThumbnailStyle style} of the pane.
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
		
		this.mouseClickFilter = new MouseClickFilter((e) -> {	
			processMouseClickedEvent(e);
		});
		
		this.thumbnails = FXCollections.observableArrayList();
		this.filteredThumbnails = new FilteredList<JThumbnail<T>>(this.thumbnails);
		this.selectedThumbnails = FXCollections.observableArrayList();
		
		this.tilePane = new TilePane();
		this.tilePane.setCache(true);
		this.tilePane.setCacheHint(CacheHint.SPEED);

		this.setOnMouseClicked((e) -> processMouseClickedEvent(e));
		
		if (scrollable) {
			this.scrollPane = new ScrollPane();
			this.scrollPane.setFitToWidth(true);
			this.scrollPane.setContent(this.tilePane);
			setCenter(this.scrollPane);
		} else {
			setCenter(this.tilePane);
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
			
			if (this.activationHandlers == null) {
				this.activationHandlers = FXCollections.observableArrayList();
			}
			
			if (!this.activationHandlers.contains(handler)) {
				ok = this.activationHandlers.add(handler);
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
		if ((handler != null) && (this.activationHandlers != null)) {
			ok = this.activationHandlers.remove(handler);
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
			
			if (this.selectionHandlers == null) {
				this.selectionHandlers = FXCollections.observableArrayList();
			}
			
			if (!this.selectionHandlers.contains(handler)) {
				ok = this.selectionHandlers.add(handler);
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
		if ((handler != null) && (this.selectionHandlers != null)) {
			ok = this.selectionHandlers.remove(handler);
		}
		return ok;
	}
	
	/**
	 * Get the <code>filter</code> property. This property describe the filter that is applied to the thumbnail.
	 * The filter match the {@link JThumbnail thumbnails} that have to be displayed according to their {@link JThumbnail#getThumbnailContent() content}.
	 * Thumbnails that are not matching the predicate will be hidden.
	 * @return the <code>filter</code> property
	 */
	public final ObjectProperty<Predicate<? super T>> thumbnailFilterProperty() {
		if (this.thumbnailFilter == null) {
			this.thumbnailFilter = new ObjectPropertyBase<Predicate<? super T>>() {

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
		return this.thumbnailFilter;
	}

	/**
	 * Get the <code>style</code> property. This property describe the style that is applied to the thumbnail.
	 * @return the <code>style</code> property
	 */
	public final ObjectProperty<JThumbnailStyle> thumbnailStyleProperty() {
		if (this.thumbnailStyle == null) {
			this.thumbnailStyle = new ObjectPropertyBase<JThumbnailStyle>() {

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
		return this.thumbnailStyle;
	}

	/**
	 * Add the given {@link JThumbnail thumbnail} to this pane.
	 * @param thumbnail the thumbnail to add
	 * @return <code>true</code> if the thumbnail is successfully added and <code>false</code> otherwise
	 */
	public boolean addThumbnail(JThumbnail<T> thumbnail) {
		boolean ok = this.thumbnails.add(thumbnail);		

		if (ok) {

			thumbnail.addEventFilter(MouseEvent.MOUSE_CLICKED, this.mouseClickFilter);

			if (!Platform.isFxApplicationThread()) {
				Platform.runLater(() -> this.tilePane.getChildren().add(thumbnail));
			} else {
				this.tilePane.getChildren().add(thumbnail);
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

		int index = this.thumbnails.indexOf(thumbnail);

		boolean ok = (this.thumbnails.remove(index) != null);	

		if (ok) {

			Platform.runLater(() -> this.tilePane.getChildren().remove(thumbnail));
			thumbnail.removeEventHandler(MouseEvent.MOUSE_CLICKED, this.mouseClickFilter);
		}

		return ok;
	}

	/**
	 * Set the style that this thumbnail pane has to use for display and layout.
	 * @param style the style that this thumbnail pane has to use for display and layout
	 */
	public void setStyle(JThumbnailStyle style) {

		this.tilePane.hgapProperty().unbind();
		this.tilePane.vgapProperty().unbind();
		this.tilePane.backgroundProperty().unbind();

		thumbnailStyleProperty().set(style);

		if (thumbnailStyleProperty().get() != null) {
			this.tilePane.hgapProperty().bind(thumbnailStyleProperty().get().thumbnailHGapProperty());
			this.tilePane.vgapProperty().bind(thumbnailStyleProperty().get().thumbnailVGapProperty());

			this.tilePane.backgroundProperty().bind(thumbnailStyleProperty().get().thumbnailPaneBackgroundProperty());
		}

		for(JThumbnail<?> thumbnail : this.thumbnails) {
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
		thumbnailFilterProperty().set(predicate);
	}

	/**
	 * Update the thumbnails display after a change on underlying data (for example ). 
	 */
	protected void update() {

		for (JThumbnail<?> t : this.filteredThumbnails.getSource()) {
			t.setVisible(false);
			t.setManaged(false);
		}

		for (JThumbnail<?> t : this.filteredThumbnails) {
			t.setVisible(true);
			t.setManaged(true);
		}
	}

	/**
	 * Filter the thumbnail according the attached {@link #thumbnailFilter}.
	 */
	private void filter() {
		if (this.thumbnailFilter != null) {
			this.filteredThumbnails.setPredicate((t) -> {return this.thumbnailFilter.get().test(t.getThumbnailContent());});
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
		if (this.activationHandlers != null) {
			for(JThumbnailActivationHandler<T> h : this.activationHandlers) {
				h.handle(this, thumbnail);
			}
		}
	}
	
	/**
	 * Fire a selection to all registered handlers.
	 * @param thumbnails the selected thumbnails
	 */
	private void fireSelection(List<JThumbnail<T>> thumbnails) {
		if (this.activationHandlers != null) {
			for(JThumbnailSelectionHandler<T> h : this.selectionHandlers) {
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
							if (this.selectionLastThumbnail == null) {
								
								if (!thumbnail.isStateSelected()) {
									
									if ((this.thumbnailFilter == null) || this.thumbnailFilter.get().test(thumbnail.getThumbnailContent())) {
										thumbnail.setStateSelected(true);
										this.selectedThumbnails.add(thumbnail);
										this.selectionLastThumbnail = thumbnail;
										
										fireSelection(this.selectedThumbnails);
									}
								}
							} else {
								int index1 = this.thumbnails.indexOf(this.selectionLastThumbnail);
								int index2 = this.thumbnails.indexOf(thumbnail);

								JThumbnail<T> tmp = null;
								for(int i = Math.min(index1, index2); i <= Math.max(index1, index2); i++) {
									tmp = this.thumbnails.get(i);
									if (!tmp.isStateSelected()) {
										
										if ((this.thumbnailFilter == null) || this.thumbnailFilter.get().test(tmp.getThumbnailContent())) {
											tmp.setStateSelected(true);
											this.selectedThumbnails.add(tmp);
											this.selectionLastThumbnail = thumbnail;
										}
										
									}
								}
								fireSelection(this.selectedThumbnails);
							}

						// Additive selection / unselection
						} else if (e.isControlDown()) {
							
							// Unselect the thumbnail
							if (thumbnail.isStateSelected()) {
								thumbnail.setStateSelected(false);
								this.selectedThumbnails.remove(thumbnail);
								this.selectionLastThumbnail = null;
								
								fireSelection(this.selectedThumbnails);
								
							// Select the thumbnail
							} else {
								
								if ((this.thumbnailFilter == null) || this.thumbnailFilter.get().test(thumbnail.getThumbnailContent())) {
									thumbnail.setStateSelected(true);
									this.selectedThumbnails.add(thumbnail);
									this.selectionLastThumbnail = thumbnail;
									
									fireSelection(this.selectedThumbnails);
								}
							}


						// Simple selection
						} else {
							
							for(JThumbnail<?> t : this.selectedThumbnails) {
								t.setStateSelected(false);
							}						
							this.selectedThumbnails.clear();
							
							if (!thumbnail.isStateSelected() && ((this.thumbnailFilter == null) || this.thumbnailFilter.get().test(thumbnail.getThumbnailContent()))) {
								thumbnail.setStateSelected(true);
								this.selectedThumbnails.add(thumbnail);
							}
							
							this.selectionLastThumbnail = thumbnail;
							
							fireSelection(this.selectedThumbnails);
						}
					}
				}
				
				e.consume();			
			}
		}
	}
}
