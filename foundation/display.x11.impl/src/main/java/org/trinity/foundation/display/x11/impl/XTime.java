/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.display.x11.impl;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_button_press_event_t;
import org.freedesktop.xcb.xcb_enter_notify_event_t;
import org.freedesktop.xcb.xcb_key_press_event_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(to = @To(IMPLEMENTATION))
@Singleton
@NotThreadSafe
@ExecutionContext(DisplayExecutor.class)
public class XTime {

	private final AtomicInteger time = new AtomicInteger();

	@Inject
	XTime(@XEventBus final EventBus xEventBus) {
		xEventBus.register(this);
	}

	public int getTime() {
		return this.time.get();
	}

	@Subscribe
	public void handleButtonPressed(final xcb_button_press_event_t button_press_event) {
		// press&release have the same type
		this.time.set(button_press_event.getTime());
	}

	@Subscribe
	public void handleKeyPressed(final xcb_key_press_event_t key_press_event) {
		// press&release have the same type
		this.time.set(key_press_event.getTime());
	}

	@Subscribe
	public void handlePropertyNotify(final xcb_property_notify_event_t property_notify_event) {
		this.time.set(property_notify_event.getTime());
	}

	@Subscribe
	public void handleEnterNotify(final xcb_enter_notify_event_t enter_notify_event) {
		// enter & leave have the same type
		this.time.set(enter_notify_event.getTime());
	}
}
