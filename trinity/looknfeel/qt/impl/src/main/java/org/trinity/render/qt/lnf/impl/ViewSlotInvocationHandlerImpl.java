package org.trinity.render.qt.lnf.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.render.qt.api.QJRenderEngine;
import org.trinity.shellplugin.widget.api.binding.ViewAttributeSlotInvocationHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ViewSlotInvocationHandlerImpl implements ViewAttributeSlotInvocationHandler {

	private final QJRenderEngine qjRenderEngine;

	@Inject
	ViewSlotInvocationHandlerImpl(final QJRenderEngine qjRenderEngine) {
		this.qjRenderEngine = qjRenderEngine;
	}

	@Override
	public void invoke(	final PaintableSurfaceNode paintableSurfaceNode,
						final Object view,
						final Method viewAttributeSlot,
						final Object viewAttribute) {
		this.qjRenderEngine.invoke(	paintableSurfaceNode,
									new PaintInstruction<Void, QJPaintContext>() {
										@Override
										public Void call(	final PaintableSurfaceNode paintableSurfaceNode,
															final QJPaintContext paintContext) {
											try {
												viewAttributeSlot.invoke(	view,
																			paintableSurfaceNode,
																			paintContext,
																			viewAttribute);
												return null;
											} catch (IllegalAccessException | IllegalArgumentException
													| InvocationTargetException e) {
												throw new Error(e);
											}
										};
									});

	}

}
