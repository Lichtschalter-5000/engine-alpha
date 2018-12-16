/*
 * Engine Alpha ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2014 Michael Andonie and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ea;

import ea.internal.net.DiscoveryClient;

public class ServerSuche {
	private static DiscoveryClient client;

	private ServerSuche () {

	}

	public static void start (ServerGefundenReagierbar listener) {
		if (client != null) {
			stop();
		}

		client = new DiscoveryClient(listener);
		client.start();
	}

	public static void stop () {
		if (client == null) {
			return;
		}

		client.interrupt();

		try {
			client.join();
		} catch (InterruptedException e) {
			// don't care
		}
	}
}