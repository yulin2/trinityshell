/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.xcb.eventconverters;

import org.trinity.core.display.api.event.DisplayEvent;
import org.trinity.core.display.api.event.DisplayEventFactory;
import org.trinity.display.x11.api.XEventConverter;
import org.trinity.display.x11.api.XProtocolConstants;
import org.trinity.display.x11.api.XResourceFactory;
import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.api.event.XMapEvent;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.event.XMapEventImpl;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

public class XMapEventConverter implements XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.MAP_NOTIFY);
	private final DisplayEventFactory displayEventFactory;
	private final XResourceFactory xResourceFactory;

	public XMapEventConverter(	final XResourceFactory xResourceFactory,
							final DisplayEventFactory displayEventFactory) {

		this.displayEventFactory = displayEventFactory;
		this.xResourceFactory = xResourceFactory;
	}

	/*****************************************
	 * @return the eventCode
	 ****************************************/
	@Override
	public Integer getXEventCode() {
		return this.eventCode;
	}

	@Override
	public XEvent constructEvent(final NativeBufferHelper eventStruct) {
		// Contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t window; /**< */
		// uint8_t override_redirect; /**< */
		// uint8_t pad1[3]; /**< */

		eventStruct.readUnsignedByte();
		final int sequence = eventStruct.readUnsignedShort();
		final int eventId = (int) eventStruct.readUnsignedInt();
		final int windowId = (int) eventStruct.readUnsignedInt();
		final boolean overrideRedirect = eventStruct.readBoolean();
		eventStruct.doneReading();

		return new XMapEventImpl(	this.eventCode.intValue(),
									sequence,
									this.xResourceFactory
											.createXWindow(XResourceHandleImpl
													.valueOf(eventId)),
									this.xResourceFactory
											.createXWindow(XResourceHandleImpl
													.valueOf(windowId)),
									overrideRedirect);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#convertEvent(org.trinity.
	 * display.x11.api.event.XEvent)
	 */
	@Override
	public DisplayEvent convertEvent(final XEvent xEvent) {
		final XMapEvent event = (XMapEvent) xEvent;
		final XWindow window = event.getWindow();
		return this.displayEventFactory.createMapNotify(window);
	}
}