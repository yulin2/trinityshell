package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import java.util.concurrent.Callable;

import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;
import static java.lang.String.format;

import static com.google.common.base.Preconditions.checkArgument;

@Bind
@Singleton
public class ChildViewDelegateImpl implements ChildViewDelegate {

	private final Injector injector;

	@Inject
	ChildViewDelegateImpl(final Injector injector) {
		this.injector = injector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ListenableFuture<T> newView(	final Object parentView,
											final Class<T> childViewType,
											final int position) {
		checkArgument(	parentView instanceof QWidget,
						format(	"Expected parent view should be of type %s",
								QWidget.class.getName()));
		checkArgument(	QWidget.class.isAssignableFrom(childViewType),
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		final ListenableFutureTask<T> newViewTask = ListenableFutureTask.create(new Callable<T>() {
			@Override
			public T call() throws Exception {
				final QWidget parentViewInstance = (QWidget) parentView;

				final T childView = ChildViewDelegateImpl.this.injector.getInstance(childViewType);
				final QWidget childViewInstance = (QWidget) childView;
				childViewInstance.setParent(parentViewInstance);
				return (T) childViewInstance;
			}
		});

		QApplication.invokeLater(newViewTask);

		return newViewTask;
	}

	@Override
	public ListenableFuture<Void> destroyView(	final Object parentView,
												final Object deletedChildView,
												final int deletedPosition) {

		final ListenableFutureTask<Void> destroyTask = ListenableFutureTask.create(new Callable<Void>() {
			@Override
			public Void call() {
				if (deletedChildView instanceof QWidget) {
					final QWidget deletedChildViewInstance = (QWidget) deletedChildView;
					deletedChildViewInstance.close();
				}
				return null;
			}
		});

		QApplication.invokeLater(destroyTask);
		return destroyTask;
	}

	@Override
	public ListenableFuture<Void> updateChildViewPosition(	final Object parentView,
															final Object childView,
															final int oldPosition,
															final int newPosition) {
		checkArgument(	parentView instanceof QWidget,
						format(	"Expected parent view should be of type %s",
								QWidget.class.getName()));

		checkArgument(	childView instanceof QWidget,
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		final ListenableFutureTask<Void> updateChildViewPosTask = ListenableFutureTask.create(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				// FIXME only layouts have a notion of order in qt. How to
				// notify the layout the order of a child?
				return null;
			}
		});

		QApplication.invokeLater(updateChildViewPosTask);
		return updateChildViewPosTask;

	}
}