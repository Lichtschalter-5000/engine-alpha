package ea;

import ea.actor.Actor;
import ea.actor.ActorGroup;
import ea.internal.ano.API;
import ea.internal.ano.NoExternalUse;

import java.awt.*;

/**
 * Layer bieten die Möglichkeit, <code>Actors</code> vor und hinter der Zeichenebene mit zusätzlichen Eigenschaften
 * (wie Parallaxe) zu rendern.
 *
 * @author Michael Andonie
 */
public class Layer {

    /**
     * Der Inhalt des Layers.
     */
    private final ActorGroup root;

    /**
     * Parallaxen-X-Faktor
     */
    private float parallaxX = 1;

    /**
     * Parallaxen-Y-Faktor
     */
    private float parallaxY = 1;

    /**
     * Parallaxen-Rotations-Faktor
     */
    private float parallaxRotation = 1;

    /**
     * Der Parallaxen-Zoom-Faktor
     */
    private float parallaxZoom = 1;

    private int zIndex = -2;

    /**
     * Ob dieses Layer gerade sichtbar ist (also gerendert wird).
     */
    private boolean visible = true;

    /**
     * Erstellt ein neues Layer.
     *
     * @param scene Die Scene, zu der dieses Layer gehört.
     */
    @API
    public Layer(Scene scene) {
        root = new ActorGroup(scene);
    }

    /**
     * Setzt die Position dieses Layers relativ zu anderen Layers.
     *
     * @param sublayer Die neue Position dieses Layers. Je höher dieser Wert, desto weiter vorne ist das Layer.
     */
    @API
    public void setLayerPosition(int sublayer) {
        this.zIndex = sublayer;
    }

    /**
     * Gibt die Position des Layers aus.
     *
     * @return Der Wert, der die Position dieses Layers repräsentiert.
     *
     * @see #setLayerPosition(int)
     */
    @API
    public int getLayerPosition() {
        return zIndex;
    }

    /**
     * Setzt den Parallaxenwert der Bewegung für dieses Layer:
     * <ul>
     * <li><code>1</code> ist keine Parallaxe (Bewegung exakt mit der Kamera)</li>
     * <li>Werte zwischen <code>0</code> und <code>1</code> schaffen einen entfernten Effekt: Die Bewegung ist
     * weniger als die der Kamera</li>
     * <li><code>0</code> bedeutet, die Bewegung der Kamera hat gar keinen Einfluss auf das Layer.</li>
     * <li>Negative Werte sorgen für Bewegung entgegen der Kamera</li>
     * <li>Werte <code>>1</code> verstärken die Bewegung der Kamera (z.B. für Vordergrund). </li>
     * </ul>
     *
     * @param parallaxX Der X-Parallaxenwert.
     * @param parallaxY Der Y-Parallaxenwert.
     */
    @API
    public void setParallaxPosition(float parallaxX, float parallaxY) {
        this.parallaxX = parallaxX;
        this.parallaxY = parallaxY;
    }

    /**
     * Setzt den Parallaxenwert beim Zoom für dieses Layer:
     * <ul>
     * <li><code>1</code>: Normaler Zoom mit der Kamera</li>
     * <li><code>0</code>: Kamerazoom hat keinen Einfluss auf dieses Layer.</li>
     * <li><code>0 < parallaxZoom < 1</code>: Der Zoomeffekt tritt schwächer auf.</li>
     * <li><code>parallaxZoom>1</code>: Der Zoomeffekt tritt stärker auf. </li>
     * <li><code>parallaxZoom < 0</code>: Der Zoomeffekt tritt betragsmäßig ähnlich und umgekehrt auf.</li>
     * </ul>
     */
    @API
    public void setParallaxZoom(float parallaxZoom) {
        this.parallaxZoom = parallaxZoom;
    }

    /**
     * Setzt die Parallaxe der Rotation. Dieses Layer wird um <code>[kamerarotation] * parallaxRotation</code> rotiert.
     *
     * @param parallaxRotation Die Rotationsparallaxe.
     */
    @API
    public void setParallaxRotation(float parallaxRotation) {
        this.parallaxRotation = parallaxRotation;
    }

    @API
    public void add(Actor... actors) {
        for (Actor room : actors) {
            this.root.add(room);
        }
    }

    @API
    final public void remove(Actor... actors) {
        for (Actor room : actors) {
            this.root.remove(room);
            room.destroy();
        }
    }

    @NoExternalUse
    public void render(Graphics2D g, Camera camera, int width, int height) {

        Vector position = camera.getPosition();
        float rotation = -camera.getRotation();
        g.setClip(0, 0, width, height);
        g.translate(width / 2, height / 2);

        float layerZoom = 1 + (camera.getZoom() - 1) * parallaxZoom;
        if (layerZoom <= 0) {
            layerZoom = 0.05f;
        }
        g.scale(layerZoom, layerZoom);
        g.rotate(rotation * parallaxRotation, 0, 0);
        g.translate(-position.x * parallaxX, position.y * parallaxY);

        // TODO: Calculate optimal bounds
        int size = Math.max(width, height);

        root.renderBasic(g, new BoundingRechteck(position.x - size, position.y - size, size * 2, size * 2));
    }
}