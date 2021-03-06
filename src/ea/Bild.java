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

import ea.internal.collision.Collider;
import ea.internal.io.ImageLoader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Ein Bild als Grafische Repraesentation einer Bilddatei, die gezeichnet werden kann.
 *
 * @author Michael Andonie
 */
public class Bild extends Raum {
	/**
	 * Die Breite der Quellbilddatei
	 */
	private final int urBreite;

	/**
	 * Die Höhe der Quellbilddatei
	 */
	private final int urHoehe;

	/**
	 * Die effektive Breite des Bildes auf der Zeichenebene
	 */
	private int breite;

	/**
	 * Die effektive Höhe des Bildes auf der Zeichenebene
	 */
	private int hoehe;

	/**
	 * Gibt an, ob sich das Bild wiederholen soll.<br /> In diesem Fall wird das Bild in der
	 * originalen Groesse ueber den Zugesprochenen Grund wiederholt.
	 */
	private boolean wiederholen;

	/**
	 * Das BufferedImage, das dieses Bild darstellt.
	 */
	private BufferedImage img;

	/**
	 * Minimaler Konstruktor. Erstellt ein neues Bild an der Position (0|0).
	 *
	 * @param verzeichnis
	 * 		Der Verzeichnispfad des Bildes, das geladen werden soll.
	 */
	public Bild (String verzeichnis) {
		this(0, 0, verzeichnis);
	}

	/**
	 * Der minimale Basiskonstruktor fuer Objekte der Klasse Bild.<br /> Der absolute
	 * Standartkonstruktor, der bei allen anderen ebenfalss aufgerufen wird. Dieser gleicht die
	 * Position an und laedt das Bild
	 *
	 * @param x
	 * 		Die X-Position
	 * @param y
	 * 		Die Y-Position
	 * @param verzeichnis
	 * 		Der Verzeichnispfad des Bildes, das geladen werden soll.
	 */
	public Bild (float x, float y, String verzeichnis) {
		this.position = new Punkt(x, y);
		this.wiederholen = false;

		img = ImageLoader.loadExternalImage(verzeichnis);

		urHoehe = img.getHeight();
		urBreite = img.getWidth();
	}

	/**
	 * Erweiterter Konstruktor.<br /> Hiebei wird ein Bild erstellt, wobei auch dessen Masse
	 * variabel angegeben werden koennen.
	 *
	 * @param x
	 * 		Die X-Position
	 * @param y
	 * 		Die Y-Position
	 * @param breite
	 * 		Die Breite, die das Bild haben soll
	 * @param hoehe
	 * 		Die Hoehe, die das Bild haben soll.
	 * @param verzeichnis
	 * 		Der Verzeichnispfad des Bildes, das geladen werden soll.
	 */
	public Bild (float x, float y, int breite, int hoehe, String verzeichnis) {
		this(x, y, breite, hoehe, verzeichnis, false);
	}

	/**
	 * Erweiterter Konstruktor.<br /> Hiebei wird ein Bild erstellt, wobei auch dessen Masse
	 * variabel angegeben werden koennen.
	 *
	 * @param x
	 * 		Die X-Position
	 * @param y
	 * 		Die Y-Position
	 * @param breite
	 * 		Die Breite, die das Bild haben soll
	 * @param hoehe
	 * 		Die Hoehe, die das Bild haben soll.
	 * @param verzeichnis
	 * 		Der Verzeichnispfad des Bildes, das geladen werden soll.
	 * @param wiederholen
	 * 		Ob das Bild skaliert oder wiederholt werden soll. <br /> In diesem Fall wird das Bild in
	 * 		der originalen Groesse ueber den Zugesprochenen Grund wiederholt: die Parameter
	 * 		<code>breite</code> und <code>hoehe</code> beschreiben diesen Flaeche.
	 */
	public Bild (float x, float y, int breite, int hoehe, String verzeichnis, boolean wiederholen) {
		this(x, y, verzeichnis);
		this.wiederholen = wiederholen;
		this.breite = breite;
		this.hoehe = hoehe;
		this.wiederholen = wiederholen;

		if (!wiederholen) {
			img = resize(img, breite, hoehe);
		}
	}

	/**
	 * Ändert ein BufferedImage von seinen Maßen her.<br /> Wird intern benutzt im Konstruktor.
	 *
	 * // TODO: [4.0] Change visibility
	 *
	 * @param img
	 * 		Das zu beschraenkende Bild
	 * @param width
	 * 		Die neue Breite des Bildes
	 * @param height
	 * 		Die neue Hoehe des Bildes
	 */
	public static BufferedImage resize (BufferedImage img, int width, int height) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = env.getDefaultScreenDevice().getDefaultConfiguration();

		BufferedImage resize = gc.createCompatibleImage(img.getWidth(), img.getHeight(), img.getTransparency());
		Graphics2D g = resize.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, width, height, 0, 0, img.getWidth(), img.getHeight(), null);
		g.dispose();

		return resize;
	}

	/**
	 * Erstellt ein Bild, unter Eingabe dessen effektiver Größe als prozentualer Anteil an der der
	 * Bilddatei.
	 *
	 * @param x
	 * 		Die X-Position
	 * @param y
	 * 		Die Y-Position
	 * @param prozent
	 * 		Der prozentuale Anteil, den das Bild im Vergleich zu der urspruenglichen Bilddatei haben
	 * 		soll
	 * @param verzeichnis
	 * 		Der Verzeichnispfad des Bildes, das geladen werden soll.
	 */
	public Bild (float x, float y, int prozent, String verzeichnis) {
		this(x, y, verzeichnis);

		img = resize(img, img.getWidth() * prozent / 100, (img.getHeight() * prozent) / 100);
	}

	/**
	 * Direkter Konstruktor.<br /> Dieser erwartet direkt die Bilddatei, die es anzuzeigen gilt.<br
	 * /> Dieser Konstruktor wird innerhalb der Engine verwendet fuer die Maus.
	 */
	public Bild (float x, float y, BufferedImage img) {
		this.img = img;

		this.positionSetzen(new Punkt(x, y));

		urHoehe = img.getHeight();
		urBreite = img.getWidth();

		hoehe = urHoehe;
		breite = urBreite;
	}

	/**
	 * Rotiert das Objekt um eine bereits definierte Rotation.
	 *
	 * TODO: Unterschied zu #drehen erklären
	 * FIXME: Es wird keine neue Breite gesetzt, aber die Breite verändert sich, falls nicht um 90 * n Grad gedreht wird
	 *
	 * @param rot
	 * 		Das Rotationsobjekt, das die Rotation beschreibt
	 *
	 * @see Rotation
	 */
	public void rotieren (Rotation rot) {
		img = rotieren(img, rot.winkelBogen());
	}

	/**
	 * Rotiert ein BufferedImage und gibt das neue, rotierte Bild aus.
	 * <p/>
	 * Es wird immer nur um die eigene Mitte gedreht!!
	 *
	 * @param img
	 * 		Das zu rotierende Bild
	 * @param angle
	 * 		Der Winkel im Bogenmass, um den gedreht werden soll.
	 */
	public static BufferedImage rotieren (BufferedImage img, double angle) {
		int w = img.getWidth();
		int h = img.getHeight();

		BufferedImage dimg = new BufferedImage((int) (w * Math.sin(angle) + w * Math.cos(angle)), (int) (h * Math.sin(angle) + h * Math.cos(angle)), img.getType());

		Graphics2D g = dimg.createGraphics();
		g.rotate(angle, w / 2, h / 2);
		g.drawImage(img, null, (int) (Math.cos(angle) * h / 2 * Math.sin(angle)), (int) (Math.cos(angle) * h / 2 * Math.sin(angle)));

		g.dispose();

		return dimg;
	}

	/**
	 * Zeichnet das Objekt.
	 *
	 * @param g
	 * 		Das zeichnende Graphics-Objekt
	 * @param r
	 * 		Das BoundingRechteck, dass die Kameraperspektive Repraesentiert.<br /> Hierbei soll
	 * 		zunaechst getestet werden, ob das Objekt innerhalb der Kamera liegt, und erst dann
	 * 		gezeichnet werden.
	 */
	public void zeichnen (Graphics2D g, BoundingRechteck r) {
		if (r.schneidetBasic(this.dimension())) {
			super.beforeRender(g, r);

			if (!wiederholen) {
				g.drawImage(img, (int) (position.realX() - r.x), (int) (position.realY() - r.y), null);
			} else {
				// Texturfarbe erstellen, Anchor-Rechteck hat genau die Bildmaße
				Paint tp = new TexturePaint(img, new Rectangle2D.Double(-r.x + position.realX(), -r.y + position.realY(), img.getWidth(), img.getHeight()));
				// Texturfarbe setzen
				g.setPaint(tp);
				// Rechteck füllen
				g.fill(new Rectangle2D.Double(position.realX() - r.x, position.realY() - r.y, breite, hoehe));
			}

			super.afterRender(g, r);
		}
	}

	/**
	 * @return Ein BoundingRechteck mit minimal nötigem Umfang, um das Objekt <b>voll
	 * einzuschließen</b>.
	 */
	public BoundingRechteck dimension () {
		if (!wiederholen) {
			return new BoundingRechteck(position.realX(), position.realY(), img.getWidth(), img.getHeight());
		} else {
			return new BoundingRechteck(position.realX(), position.realY(), breite, hoehe);
		}
	}

	/**
	 * {@inheritDoc} Collider wird direkt aus dem das <code>Raum</code>-Objekt umfassenden
	 * <code>BoundingRechteck</code> erzeugt, dass über die <code>dimension()</code>-Methode
	 * berechnet wird.
	 */
	@Override
	public Collider erzeugeCollider () {
		return erzeugeLazyCollider();
	}

	/**
	 * Gibt die Breite der Bilddatei, aus der dieses Bild besteht, in Pixeln zurueck.
	 *
	 * @return Die Breite der urspruenglichen Bilddatei in Pixeln
	 */
	public int normaleBreite () {
		return urBreite;
	}

	/**
	 * Gibt die Hoehe der Bilddatei, aus der dieses Bild besteht, in Pixeln zurueck.
	 *
	 * @return Die Hoehe der urspruenglichen Bilddatei in Pixeln
	 */
	public int normaleHoehe () {
		return urHoehe;
	}

	/**
	 * Gibt das Bild als <code>BufferedImage</code> zurueck.<br /> Dies ist eine
	 * JAVA-Standartklasse.
	 *
	 * @return Das Bild als <code>BufferedImage</code>.
	 */
	public BufferedImage bild () {
		return this.img;
	}

	public final Bild clone () {
		return new Bild(positionX(), positionY(), img);
	}
}
