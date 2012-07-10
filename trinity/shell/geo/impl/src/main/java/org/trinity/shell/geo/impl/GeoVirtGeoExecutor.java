/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.geo.impl;

import javax.inject.Named;

import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>GeoVirtGeoExecutor</code> executes the actual geometry changes for a
 * {@link GeoVirtRectangle}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Singleton
@Bind
@Named("GeoVirt")
public class GeoVirtGeoExecutor implements GeoExecutor {

	/**
	 * @param virtSquare
	 *            The <code>GeoVirtRectangle</code> who's geometry will be
	 *            executed by this <code>GeoVirtGeoExecutor</code>.
	 */
	protected GeoVirtGeoExecutor() {
	}

	@Override
	public void lower(final GeoTransformableRectangle geoTransformableRectangle) {
		final GeoTransformableRectangle[] children = geoTransformableRectangle
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			child.getGeoExecutor().lower(child);
		}
	}

	@Override
	public void raise(final GeoTransformableRectangle geoTransformableRectangle) {
		final GeoTransformableRectangle[] children = geoTransformableRectangle
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			child.getGeoExecutor().raise(child);
		}
	}

	@Override
	public void updatePlace(final GeoTransformableRectangle geoTransformableRectangle,
							final int relativeX,
							final int relativeY) {

		final GeoTransformation geoTransformation = geoTransformableRectangle
				.toGeoTransformation();

		final int deltaX = geoTransformation.getDeltaX();
		final int deltaY = geoTransformation.getDeltaY();

		final GeoTransformableRectangle[] children = geoTransformableRectangle
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			final int oldRelX = child.getX();
			final int oldRelY = child.getY();

			final int newRelX = oldRelX + deltaX;
			final int newRelY = oldRelY + deltaY;

			// directly manipulated underlying platform specific geometry of the
			// child
			child.getGeoExecutor().updatePlace(child, newRelX, newRelY);
		}
	}

	@Override
	public void updateSize(	final GeoTransformableRectangle geoTransformableRectangle,
							final int width,
							final int height) {
		// do nothing
	}

	@Override
	public void updateSizePlace(final GeoTransformableRectangle geoTransformableRectangle,
								final int relativeX,
								final int relativeY,
								final int width,
								final int height) {
		updatePlace(geoTransformableRectangle, relativeX, relativeY);
	}

	@Override
	public void show(final GeoTransformableRectangle geoTransformableRectangle) {
		final GeoTransformableRectangle[] children = geoTransformableRectangle
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			updateChildVisibility(child, true);
		}
	}

	@Override
	public void hide(final GeoTransformableRectangle geoTransformableRectangle) {
		final GeoTransformableRectangle[] children = geoTransformableRectangle
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			updateChildVisibility(child, false);
		}
	}

	/**
	 * @param child
	 * @param visible
	 */
	private void updateChildVisibility(	final GeoTransformableRectangle child,
										final boolean parentVisible) {
		final boolean childVisible = child.isVisible();
		// directly update underlying platform specific visibility of
		// the child
		if (childVisible & parentVisible) {
			child.getGeoExecutor().show(child);
		} else {
			child.getGeoExecutor().hide(child);
		}
	}

	@Override
	public void updateParent(	final GeoTransformableRectangle geoTransformableRectangle,
								final GeoTransformableRectangle parent) {
		final GeoTransformableRectangle[] children = geoTransformableRectangle
				.getChildren();

		final boolean parentVisible = parent.isVisible();
		for (final GeoTransformableRectangle child : children) {
			// directly update underlying platform specific parent of
			// the child
			child.getGeoExecutor().updateParent(child,
												geoTransformableRectangle);
			updateChildVisibility(child, parentVisible);
		}
	}

	@Override
	public void destroy(final GeoTransformableRectangle geoTransformableRectangle) {
		// TODO destroy all children? (Will somebody *please* think of the
		// children! :'( )
	}
}
