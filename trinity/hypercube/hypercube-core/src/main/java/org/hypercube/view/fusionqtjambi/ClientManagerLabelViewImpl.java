/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube.view.fusionqtjambi;

import org.fusion.qt.painter.QFusionPaintCallBack;
import org.hydrogen.paintinterface.PaintCall;
import org.hydrogen.paintinterface.Paintable;
import org.hypercube.view.fusionqtjambi.visual.ClientManagerLabelVisual;
import org.hyperdrive.widget.ClientManager.ClientManagerLabel.ClientManagerLabelView;

import com.trolltech.qt.gui.QWidget;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ClientManagerLabelViewImpl extends ClientNameLabelViewImpl
		implements ClientManagerLabelView {

	@Override
	protected ClientManagerLabelVisual initVisual(final QWidget parentVisual,
			final Paintable paintable, final Object... args) {
		return new ClientManagerLabelVisual(parentVisual);
	}

	@Override
	public PaintCall<?, ?> onClientGainFocus() {
		return new QFusionPaintCallBack<ClientManagerLabelVisual, Void>() {
			@Override
			public Void call(final ClientManagerLabelVisual paintPeer,
					final Paintable paintable) {
				paintPeer.activate();
				return null;
			}
		};
	}

	@Override
	public PaintCall<?, ?> onClientLostFocus() {
		return new QFusionPaintCallBack<ClientManagerLabelVisual, Void>() {
			@Override
			public Void call(final ClientManagerLabelVisual paintPeer,
					final Paintable paintable) {
				if (paintPeer != null) {
					paintPeer.deactivate();
				}
				return null;
			}
		};
	}
}
