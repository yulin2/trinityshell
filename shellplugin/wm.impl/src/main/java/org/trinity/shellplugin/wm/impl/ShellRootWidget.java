package org.trinity.shellplugin.wm.impl;

import java.util.Set;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shell.api.surface.ShellSurfaceParent;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shellplugin.wm.api.BottomBarItem;
import org.trinity.shellplugin.wm.api.TopBarItem;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;

public class ShellRootWidget extends BaseShellWidget {

	private final Object view;

	private final EventList<Object> topBar = new BasicEventList<Object>();
	private final EventList<Object> bottomBar = new BasicEventList<Object>();

	@Inject
	protected ShellRootWidget(	final ShellSurfaceFactory shellSurfaceFactory,
								@Named("Shell") final ListeningExecutorService shellExecutor,
								final PainterFactory painterFactory,
								@Named("RootView") final Object view,
								final Set<TopBarItem> topBarItems,
								final Set<BottomBarItem> bottomBarItems) {
		super(	shellSurfaceFactory,
				shellExecutor,
				painterFactory);
		this.view = view;
		addTopBarItems(topBarItems);
		addBottomBarItems(bottomBarItems);
		getPainter().bindView();

		// find correct size
		final ListenableFuture<Rectangle> rootGeometryFuture = transform(	shellSurfaceFactory.getRootShellSurface(),
																			new AsyncFunction<ShellSurfaceParent, Rectangle>() {
																				@Override
																				public ListenableFuture<Rectangle> apply(final ShellSurfaceParent input)
																						throws Exception {
																					return input.getGeometry();
																				}
																			});
		// set correct size
		addCallback(rootGeometryFuture,
					new FutureCallback<Rectangle>() {
						@Override
						public void onSuccess(final Rectangle result) {
							setSize(result.getSize());
							doResize();
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					},
					shellExecutor);
	}

	@Override
	public Object getViewImpl() {
		return this.view;
	}

	public EventList<Object> getTopBar() {
		return this.topBar;
	}

	private void addTopBarItems(final Set<TopBarItem> topBarItems) {
		try {
			this.topBar.getReadWriteLock().writeLock().lock();
			this.topBar.addAll(topBarItems);
		} finally {
			this.topBar.getReadWriteLock().writeLock().unlock();
		}
	}

	public EventList<Object> getBottomBar() {
		return this.bottomBar;
	}

	private void addBottomBarItems(final Set<BottomBarItem> bottomBarItems) {
		try {
			this.bottomBar.getReadWriteLock().writeLock().lock();
			this.bottomBar.addAll(bottomBarItems);
		} finally {
			this.bottomBar.getReadWriteLock().writeLock().unlock();
		}
	}
}