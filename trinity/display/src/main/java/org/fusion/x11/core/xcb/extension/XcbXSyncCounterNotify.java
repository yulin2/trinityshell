/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core.xcb.extension;

import org.trinity.core.display.impl.event.BaseDisplayEvent;
import org.trinity.display.x11.api.extension.sync.XSyncCounterNotify;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbXSyncCounterNotify extends BaseDisplayEvent implements
                XSyncCounterNotify {

	private final long    waitValue;
	private final long    counterValue;
	private final boolean isCounterDestroyed;

	public XcbXSyncCounterNotify(final XcbXSyncCounter eventSource,
	                             final long waitValue,
	                             final long counterValue,
	                             final boolean isCounterDestroyed) {
		super(XSyncCounterNotify.TYPE,
		      eventSource);
		this.waitValue = waitValue;
		this.counterValue = counterValue;
		this.isCounterDestroyed = isCounterDestroyed;
	}

	@Override
	public XcbXSyncCounter getEventSource() {
		return (XcbXSyncCounter) super.getEventSource();
	}

	@Override
	public long getWaitValue() {
		return this.waitValue;
	}

	@Override
	public long getCounterValue() {
		return this.counterValue;
	}

	@Override
	public boolean isCounterDestroyed() {
		return this.isCounterDestroyed;
	}
}