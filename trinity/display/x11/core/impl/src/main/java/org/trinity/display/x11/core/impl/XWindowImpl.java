/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.core.impl;

import org.trinity.display.x11.core.api.XCall;
import org.trinity.display.x11.core.api.XCaller;
import org.trinity.display.x11.core.api.XConnection;
import org.trinity.display.x11.core.api.XDisplayServer;
import org.trinity.display.x11.core.api.XProtocolConstants;
import org.trinity.display.x11.core.api.XResourceHandle;
import org.trinity.display.x11.core.api.XWindow;
import org.trinity.display.x11.core.api.XWindowAttributes;
import org.trinity.display.x11.core.api.XWindowGeometry;
import org.trinity.foundation.display.api.DisplayEventSelector;
import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.shared.geometry.api.Coordinates;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

/**
 * An <code>XWindow</code> represents a native X window. This implies that a
 * <code>XWindow</code> is actually a proxy to a native X window. To address the
 * correct native X window, a <code>XWindow</code> has a <code>XID</code>. This
 * <code>XID</code> holds both the window id and the display of the target
 * native X window.
 * <p>
 * A <code>XWindow</code> can be used to manipulate the native X window that it
 * represents. Manipulation of a <code>XWindow</code> is limited to non painting
 * operations.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XWindowImpl extends XResourceImpl implements XWindow {

	// TODO clean up resources (ie certain xwindow relations, xresource
	// registry, ...) if the render area on the display server that is
	// represented by this XWindow is destroyed.

	// private final Map<XAtom, Boolean> propertyValidityCache = new
	// HashMap<XAtom, Boolean>();
	// private final HashMap<Property<? extends PropertyInstance>,
	// PropertyInstance> cachedPropertyReplies = new HashMap<Property<? extends
	// PropertyInstance>, PropertyInstance>();

	private final XCaller xCaller;

	private final XCall<Void, Long, Integer> addToSaveSet;
	private final XCall<Void, Long, Integer> destroyWindow;
	private final XCall<Void, Long, Number> enableEvents;
	private final XCall<Void, Long, Integer> focusWindow;
	private final XCall<XWindowAttributes, Long, Integer> getWindowAttributesCopy;
	private final XCall<XWindowGeometry, Long, Integer> getWindowGeometry;
	private final XCall<Void, Long, Integer> grabButton;
	private final XCall<Void, Long, Number> grabKey;
	private final XCall<Void, Long, Integer> grabKeyboard;
	private final XCall<Void, Long, Integer> grabMouse;
	private final XCall<Void, Long, Integer> lowerWindow;
	private final XCall<Void, Long, Integer> mapWindow;
	private final XCall<Void, Long, Integer> moveResizeWindow;
	private final XCall<Void, Long, Integer> moveWindow;
	private final XCall<Void, Long, Object> overrideRedirectWindow;
	private final XCall<Void, Long, Integer> raiseWindow;
	private final XCall<Void, Long, Integer> removeFromSaveSet;
	private final XCall<Void, Long, Integer> reparentWindow;
	private final XCall<Void, Long, Integer> resizeWindow;
	// private final XCall<Void, Long, Object> sendClientMessage;
	private final XCall<Coordinates, Long, Integer> translateCoordinates;
	private final XCall<Void, Long, Integer> ungrabButton;
	private final XCall<Void, Long, Integer> ungrabKey;
	private final XCall<Void, Long, Integer> ungrabKeyboard;
	private final XCall<Void, Long, Integer> ungrabMouse;
	private final XCall<Void, Long, Integer> unmapWindow;

	private final XConnection<Long> xConnection;
	private final XDisplayServer display;

	// private final XDisplayResourceFactory xResourceFactory;

	@Inject
	protected XWindowImpl(	final XDisplayServer display,
							final XConnection<Long> xConnection,
							final XCaller xCaller,
							@Named("addToSaveSet") final XCall<Void, Long, Integer> addToSaveSet,
							@Named("destroyWindow") final XCall<Void, Long, Integer> destroyWindow,
							@Named("enableEvents") final XCall<Void, Long, Number> enableEvents,
							@Named("focusWindow") final XCall<Void, Long, Integer> focusWindow,
							@Named("getWindowAttributes") final XCall<XWindowAttributes, Long, Integer> getWindowAttributes,
							@Named("getWindowGeometry") final XCall<XWindowGeometry, Long, Integer> getWindowGeometry,
							@Named("grabButton") final XCall<Void, Long, Integer> grabButton,
							@Named("grabKey") final XCall<Void, Long, Number> grabKey,
							@Named("grabKeyboard") final XCall<Void, Long, Integer> grabKeyboard,
							@Named("grabMouse") final XCall<Void, Long, Integer> grabMouse,
							@Named("lowerWindow") final XCall<Void, Long, Integer> lowerWindow,
							@Named("mapWindow") final XCall<Void, Long, Integer> mapWindow,
							@Named("moveResizeWindow") final XCall<Void, Long, Integer> moveResizeWindow,
							@Named("moveWindow") final XCall<Void, Long, Integer> moveWindow,
							@Named("overrideRedirectWindow") final XCall<Void, Long, Object> overrideRedirectWindow,
							@Named("raiseWindow") final XCall<Void, Long, Integer> raiseWindow,
							@Named("removeFromSaveSet") final XCall<Void, Long, Integer> removeFromSaveSet,
							@Named("reparentWindow") final XCall<Void, Long, Integer> reparentWindow,
							@Named("resizeWindow") final XCall<Void, Long, Integer> resizeWindow,
							// @Named("sendClientMessage") final XCall<Void,
							// Long, Object> sendClientMessage,
							@Named("translateCoordinates") final XCall<Coordinates, Long, Integer> translateCoordinates,
							@Named("ungrabButton") final XCall<Void, Long, Integer> ungrabButton,
							@Named("ungrabKey") final XCall<Void, Long, Integer> ungrabKey,
							@Named("ungrabKeyboard") final XCall<Void, Long, Integer> ungrabKeyboard,
							@Named("ungrabMouse") final XCall<Void, Long, Integer> ungrabMouse,
							@Named("unmapWindow") final XCall<Void, Long, Integer> unmapWindow,
							@Assisted final XResourceHandle xResourceHandle) {
		super(xResourceHandle);
		this.display = display;
		this.xConnection = xConnection;
		this.xCaller = xCaller;
		this.addToSaveSet = addToSaveSet;
		this.destroyWindow = destroyWindow;
		this.enableEvents = enableEvents;
		this.focusWindow = focusWindow;
		this.getWindowAttributesCopy = getWindowAttributes;
		this.getWindowGeometry = getWindowGeometry;
		this.grabButton = grabButton;
		this.grabKey = grabKey;
		this.grabKeyboard = grabKeyboard;
		this.grabMouse = grabMouse;
		this.lowerWindow = lowerWindow;
		this.mapWindow = mapWindow;
		this.moveResizeWindow = moveResizeWindow;
		this.moveWindow = moveWindow;
		this.overrideRedirectWindow = overrideRedirectWindow;
		this.raiseWindow = raiseWindow;
		this.removeFromSaveSet = removeFromSaveSet;
		this.reparentWindow = reparentWindow;
		this.resizeWindow = resizeWindow;
		// this.sendClientMessage = sendClientMessage;
		this.translateCoordinates = translateCoordinates;
		this.ungrabButton = ungrabButton;
		this.ungrabKey = ungrabKey;
		this.ungrabKeyboard = ungrabKeyboard;
		this.ungrabMouse = ungrabMouse;
		this.unmapWindow = unmapWindow;
	}

	@Override
	public void destroy() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.destroyWindow, displayAddress, winId);
	}

	@Override
	public void setInputFocus() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();
		final int time = this.display.getLastServerTime();

		this.xCaller.doCall(this.focusWindow, displayAddress, winId, time);
	}

	@Override
	public void lower() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.lowerWindow, displayAddress, winId);
	}

	@Override
	public void show() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.mapWindow, displayAddress, winId);
	}

	@Override
	public void move(final int x, final int y) {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.moveWindow,
							displayAddress,
							winId,
							Integer.valueOf(x),
							Integer.valueOf(y));
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.moveResizeWindow,
							displayAddress,
							winId,
							Integer.valueOf(x),
							Integer.valueOf(y),
							Integer.valueOf(width),
							Integer.valueOf(height));
	}

	// @Override
	public void overrideRedirect(final boolean override) {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.overrideRedirectWindow,
							displayAddress,
							winId,
							Boolean.valueOf(override));
	}

	@Override
	public void selectEvent(final DisplayEventSelector... eventMasks) {
		long eventMask0 = 0L;
		for (final DisplayEventSelector eventPropagator : eventMasks) {
			if (eventPropagator == DisplayEventSelector.NOTIFY_CHANGED_WINDOW_PROPERTY) {
				eventMask0 |= XProtocolConstants.PROPERTY_CHANGE_MASK;
				continue;
			}
			if (eventPropagator == DisplayEventSelector.NOTIFY_MOUSE_ENTER) {
				eventMask0 |= XProtocolConstants.ENTER_WINDOW_MASK;
				continue;
			}
			if (eventPropagator == DisplayEventSelector.NOTIFY_MOUSE_LEAVE) {
				eventMask0 |= XProtocolConstants.LEAVE_WINDOW_MASK;
				continue;
			}
			if (eventPropagator == DisplayEventSelector.NOTIFY_CHANGED_WINDOW_GEOMETRY) {
				eventMask0 |= XProtocolConstants.STRUCTURE_NOTIFY_MASK;
				continue;
			}
			if (eventPropagator == DisplayEventSelector.REDIRECT_CHILD_WINDOW_GEOMETRY_CHANGES) {
				eventMask0 |= XProtocolConstants.SUBSTRUCTURE_REDIRECT_MASK;
				continue;
			}
			if (eventPropagator == DisplayEventSelector.NOTIFY_CHANGED_WINDOW_FOCUS) {
				eventMask0 |= XProtocolConstants.FOCUS_CHANGE_MASK;
				continue;
			}
		}
		final Long eventMask = Long.valueOf(eventMask0);
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();
		this.xCaller
				.doCall(this.enableEvents, displayAddress, winId, eventMask);
	}

	@Override
	public XWindowGeometry getGeometry() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		final XWindowGeometry wg = this.xCaller.doCall(	this.getWindowGeometry,
														displayAddress,
														winId);

		return wg;
	}

	@Override
	public void raise() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.raiseWindow, displayAddress, winId);
	}

	@Override
	public XWindowAttributes getWindowAttributes() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		return this.xCaller.doCall(	this.getWindowAttributesCopy,
									displayAddress,
									winId);

	}

	@Override
	public void setParent(	final DisplayRenderArea parent,
							final int x,
							final int y) {

		final XWindowImpl xParent = (XWindowImpl) parent;

		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer childWinId = getResourceHandle().getNativeHandle();
		final Integer parentWinId = xParent.getResourceHandle()
				.getNativeHandle();

		this.xCaller.doCall(this.reparentWindow,
							displayAddress,
							childWinId,
							parentWinId,
							Integer.valueOf(x),
							Integer.valueOf(y));
	}

	@Override
	public void resize(final int width, final int height) {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();
		this.xCaller.doCall(this.resizeWindow,
							displayAddress,
							winId,
							Integer.valueOf(width),
							Integer.valueOf(height));
	}

	@Override
	public void hide() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer winId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.unmapWindow, displayAddress, winId);
	}

	// @Override
	// public void sendMessage(final ClientMessageEvent clientMessageEvent) {
	// final Long displayAddress = this.xConnection.getConnectionReference();
	// final Integer windowId = getResourceHandle().getNativeHandle();
	// final Integer atomId = Integer.valueOf(clientMessageEvent
	// .getMessageType().getAtomId());
	// final Integer format = Integer.valueOf(clientMessageEvent
	// .getDataFormat());
	// final byte[] data = clientMessageEvent.getData();
	//
	// this.xCaller.doCall(this.sendClientMessage,
	// displayAddress,
	// windowId,
	// atomId,
	// format,
	// data);
	// }

	@Override
	public void catchKeyboardInput(	final Key catchKey,
									final InputModifiers withModifiers) {
		final Long displayPeer = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();
		final Short keyCode = Short.valueOf((short) catchKey.getKeyCode());
		final Integer inputModifiersMask = Integer.valueOf(withModifiers
				.getInputModifiersMask());

		this.xCaller.doCall(this.grabKey,
							displayPeer,
							windowId,
							keyCode,
							inputModifiersMask);
	}

	@Override
	public void catchMouseInput(final Button catchButton,
								final InputModifiers withModifiers) {
		final Long displayPeer = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();
		final Integer buttonCode = Integer.valueOf(catchButton.getButtonCode());
		final Integer inputModifiersMask = Integer.valueOf(withModifiers
				.getInputModifiersMask());

		this.xCaller.doCall(this.grabButton,
							displayPeer,
							windowId,
							buttonCode,
							inputModifiersMask);
	}

	@Override
	public void disableKeyboardInputCatching(	final Key catchKey,
												final InputModifiers withModifiers) {
		final Long displayPeer = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();
		final Integer keyCode = Integer.valueOf(catchKey.getKeyCode());
		final Integer inputModifiersMask = Integer.valueOf(withModifiers
				.getInputModifiersMask());

		this.xCaller.doCall(this.ungrabKey,
							displayPeer,
							windowId,
							keyCode,
							inputModifiersMask);
	}

	@Override
	public void disableMouseInputCatching(	final Button catchButton,
											final InputModifiers withModifiers) {
		final Long displayPeer = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();
		final Integer buttonCode = catchButton.getButtonCode();
		final Integer inputModifiersMask = Integer.valueOf(withModifiers
				.getInputModifiersMask());

		this.xCaller.doCall(this.ungrabButton,
							displayPeer,
							windowId,
							buttonCode,
							inputModifiersMask);
	}

	@Override
	public void catchAllKeyboardInput() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();
		final Integer time = Integer.valueOf(this.display.getLastServerTime());

		this.xCaller.doCall(this.grabKeyboard, displayAddress, windowId, time);
	}

	@Override
	public void catchAllMouseInput() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();
		final Integer time = Integer.valueOf(this.display.getLastServerTime());

		this.xCaller.doCall(this.grabMouse, displayAddress, windowId, time);
	}

	@Override
	public void stopKeyboardInputCatching() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer time = Integer.valueOf(this.display.getLastServerTime());

		this.xCaller.doCall(this.ungrabKeyboard, displayAddress, time);
	}

	@Override
	public void stopMouseInputCatching() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer time = Integer.valueOf(this.display.getLastServerTime());

		this.xCaller.doCall(this.ungrabMouse, displayAddress, time);
	}

	public void addToSaveSet() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.addToSaveSet, displayAddress, windowId);
	}

	public void removeFromSaveSet() {
		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();

		this.xCaller.doCall(this.removeFromSaveSet, displayAddress, windowId);
	}

	@Override
	public Coordinates translateCoordinates(final DisplayRenderArea source,
											final int sourceX,
											final int sourceY) {
		final XWindowImpl xSource = (XWindowImpl) source;

		final Long displayAddress = this.xConnection.getConnectionReference();
		final Integer windowId = getResourceHandle().getNativeHandle();
		final Integer sourceWindowId = xSource.getResourceHandle()
				.getNativeHandle();
		final Integer sourceXCoordinate = Integer.valueOf(sourceX);
		final Integer sourceYCoordinate = Integer.valueOf(sourceY);

		return this.xCaller.doCall(	this.translateCoordinates,
									displayAddress,
									windowId,
									sourceWindowId,
									sourceXCoordinate,
									sourceYCoordinate);
	}
}