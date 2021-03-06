/*
 * Engine Alpha ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2015 Michael Andonie and contributors.
 *
 * @author: mike ganshorn
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

package ea.edu;

import ea.Figur;

public class FigurE extends Figur {

    /**
     * Konstruktor erstellt eine fertig sichtbare Figur. Ihre Position lässt sich leicht über die
     * geerbten Methoden ändern.
     *
     * @param figurDatei
     * 		Der Dateiname der Figur-Datei.
     */
    public FigurE (String figurDatei) {
        super(figurDatei);
        FensterE.getFenster().wurzel.add(this);
    }
}
