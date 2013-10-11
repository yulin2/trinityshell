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

package org.trinity.foundation.api.shared;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;

/**
 * Extends guava's {@link EventBus} idea with asynchronous event delivery. This
 * interface does not (can not) extend from guava's Eventbus as it's developers
 * think it's a good idea to *not* separate implementation (a class) from api
 * (an interface)...
 */
public interface AsyncListenable {
	/**
	 * Immediately register a listener who's {@link Subscribe}d methods will be
	 * invoked by the {@link MoreExecutors#sameThreadExecutor()}, ie the thread
	 * that calls {@link #post(Object)} on this object.
	 * <p>
	 * This method should be used if the calling executor service is the same as
	 * than the one that owns this object.
	 *
	 * @see {@link EventBus#register(Object)}
	 * @param listener
	 *            An object with a public, single argument, {@link Subscribe}
	 *            method.
	 */
	void register(@Nonnull Object listener);

	/**
	 * Immediately register a listener who's {@link Subscribe}d methods will be
	 * invoked by the given {@link ExecutorService}.
	 * <p>
	 * This method should be used if the calling executor service is the same as
	 * than the one that owns this object.
	 *
	 * @see {@link EventBus#register(Object)}
	 * @param listener
	 *            An object with a public, single argument, {@link Subscribe}
	 *            method.
	 * @param executor
	 *            The executor that will call the listener when an event
	 *            arrives.
	 */
	void register(	@Nonnull Object listener,
					@Nonnull ExecutorService executor);

	/**
	 * @see {@link EventBus#unregister(Object)}
	 * @param listener
	 *            An previously registered listener.
	 */
	void unregister(@Nonnull Object listener);

	/**
	 * Asynchronously post an event.
	 *
	 * @see {@link EventBus#post(Object)}
	 * @param event
	 *            The even to post.
	 */
	void post(@Nonnull Object event);

	/**
	 * Same as {@link #register(Object)} but the registration task is delegated
	 * to the executor service that handle's the posting of events. Ie the
	 * executor service that 'owns' this object.
	 * <p>
	 * This method should be used if the calling executor service is different
	 * than the one that owns this object.
	 *
	 * @param listener
	 *            An object with a public, single argument, {@link Subscribe}
	 *            method.
	 */
	void scheduleRegister(@Nonnull Object listener);

	/**
	 * Same as {@link #register(Object, java.util.concurrent.ExecutorService)}
	 * but the registration task is delegated to the executor service that
	 * handle's the posting of events. Ie the executor service that 'owns' this
	 * object.
	 * <p>
	 * This method should be used if the calling executor service is different
	 * than the one that owns this object.
	 *
	 * @param listener
	 *            An object with a public, single argument, {@link Subscribe}
	 *            method.
	 * @param listenerActivationExecutor
	 *            The executor that will call the listener when an event
	 *            arrives.
	 */
	void scheduleRegister(	@Nonnull Object listener,
							@Nonnull ExecutorService listenerActivationExecutor);
}
