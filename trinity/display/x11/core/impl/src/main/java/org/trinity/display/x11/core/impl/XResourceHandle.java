package org.trinity.display.x11.core.impl;

import org.trinity.foundation.display.api.DisplayResourceHandle;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class XResourceHandle implements DisplayResourceHandle {

	private final int nativeHandle;

	@Inject
	XResourceHandle(@Assisted final int nativeHandle) {
		this.nativeHandle = nativeHandle;
	}

	public int getNativeHandle() {
		return this.nativeHandle;
	}

	@Override
	public int hashCode() {
		return getNativeHandle();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XResourceHandle) {
			final XResourceHandle otherXResourceHandle = (XResourceHandle) obj;
			return otherXResourceHandle.getNativeHandle() == getNativeHandle();
		}
		return false;
	}
}