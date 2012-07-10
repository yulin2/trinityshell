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

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.trinity.display.x11.core.api.XCall;
import org.trinity.display.x11.core.api.XCaller;
import org.trinity.display.x11.core.api.XConnection;
import org.trinity.display.x11.core.api.XDisplayResourceFactory;
import org.trinity.display.x11.core.api.XDisplayServer;
import org.trinity.display.x11.core.api.XResourceHandle;
import org.trinity.display.x11.core.api.XWindow;
import org.trinity.foundation.display.api.DisplayEventProducer;
import org.trinity.foundation.display.api.event.DisplayEvent;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * An <code>XDisplay</code> is the fusion-x11 representation of an X display
 * server.
 * <p>
 * <code>DisplayEvent</code>s that arrive on an <code>XDisplay</code> are
 * generated by underlying X11 native display and are automatically fetched. A
 * programmer should therefore not call <code>getNextEvent()</code> unless
 * strictly necessary. Instead automatically fetched <code>DisplayEvent</code>s
 * are placed on a queue and can be retrieved by calling
 * <code>getEventFromQueue()</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class XDisplayServerImpl implements XDisplayServer {

	private final XCaller xCaller;
	private final XCall<XResourceHandle, Long, Void> getInputFocus;

	private final XConnection<Long> xConnection;

	private int lastServerTime;
	private final int currentServerTime;

	private final Set<DisplayEventProducer> displayEventProducers;

	private final ArrayBlockingQueue<DisplayEvent> producedDisplayEvents = new ArrayBlockingQueue<DisplayEvent>(1024);
	private final XDisplayResourceFactory xResourceFactory;

	@Inject
	protected XDisplayServerImpl(	final Set<DisplayEventProducer> displayEventProducers,
									final XConnection<Long> xConnection,
									final XCaller xCaller,
									@Named("getInputFocus") final XCall<XResourceHandle, Long, Void> getInputFocus,
									final XDisplayResourceFactory xResourceFactory) {
		this.displayEventProducers = displayEventProducers;
		this.xResourceFactory = xResourceFactory;
		this.xConnection = xConnection;
		this.xCaller = xCaller;
		this.getInputFocus = getInputFocus;

		this.currentServerTime = 0;
		setLastServerTime(getCurrentServerTime());

		xConnection.open();
		startDisplayEventProducers();
	}

	private void startDisplayEventProducers() {
		for (final DisplayEventProducer displayEventProducer : this.displayEventProducers) {
			displayEventProducer.start();
		}
	}

	private void stopDisplayEventProducers() {
		for (final DisplayEventProducer displayEventProducer : this.displayEventProducers) {
			displayEventProducer.stop();
		}
	}

	@Override
	public DisplayEvent getNextDisplayEvent() {
		try {
			return this.producedDisplayEvents.take();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void shutDown() {
		stopDisplayEventProducers();
		this.xConnection.close();
	}

	@Override
	public XWindow getInputFocus() {
		final XResourceHandle resourceHandle = this.xCaller
				.doCall(this.getInputFocus,
						this.xConnection.getConnectionReference());

		final XWindow inputWindow = this.xResourceFactory
				.createDisplayRenderArea(resourceHandle);

		return inputWindow;
	}

	@Override
	public int getCurrentServerTime() {
		return this.currentServerTime;
	}

	@Override
	public int getLastServerTime() {
		return this.lastServerTime;
	}

	@Override
	public void setLastServerTime(final int lastServerTime) {
		this.lastServerTime = lastServerTime;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.display.api.Display#hasNextDisplayEvent()
	 */
	@Override
	public boolean hasNextDisplayEvent() {
		return !this.producedDisplayEvents.isEmpty();
	}
}