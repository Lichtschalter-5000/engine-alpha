package ea.example.showcase.jump;

import ea.Scene;
import ea.Vector;
import ea.actor.Actor;
import ea.actor.Animation;
import ea.actor.StatefulAnimation;
import ea.collision.CollisionEvent;
import ea.collision.CollisionListener;


public class PlayerCharacter
extends StatefulAnimation
implements CollisionListener<Actor> {

    private static final float MAX_SPEED = 5000;

    /**
     * Beschreibt die drei Zustände, die ein Character bezüglich seiner horizontalen Bewegung haben kann.
     */
    public enum MovementState {
        LEFT, RIGHT, IDLE;

        public float getTargetXVelocity() {
            switch (this) {
                case LEFT:
                    return -MAX_SPEED;
                case RIGHT:
                    return MAX_SPEED;
                case IDLE:
                    return 0;
            }
            return Float.NaN;
        }
    }

    private MovementState movementState=MovementState.IDLE;

    public PlayerCharacter(Scene parent) {
        //Load all Animations in

        //Alle einzuladenden Dateien teilen den Großteil des Paths (Ordner sowie gemeinsame Dateipräfixe)
        final String pathbase = "game-assets\\jump\\spr_m_traveler_";


        Animation idle = Animation.createFromAnimatedGif(pathbase+"idle_anim.gif");
        addState(idle, "idle");

        addState(Animation.createFromAnimatedGif(pathbase+"walk_anim.gif"), "walking");
        addState(Animation.createFromAnimatedGif(pathbase+"run_anim.gif"), "running");
        addState(Animation.createFromAnimatedGif(pathbase+"jump_1up_anim.gif"), "jumpingUp");
        addState(Animation.createFromAnimatedGif(pathbase+"jump_2midair_anim.gif"), "midair");
        addState(Animation.createFromAnimatedGif(pathbase+"jump_3down_anim.gif"), "falling");
        addState(Animation.createFromAnimatedGif(pathbase+"jump_4land_anim.gif"), "landing");

        setStateTransition("midair", "falling");
        setStateTransition("landing", "idle");

        physics.setFriction(0);
        physics.setElasticity(0);

        parent.add(this);
        physics.setMass(65);
        addCollisionListener(this);

    }

    /**
     * Wird ausgeführt, wenn ein Sprungbefehl (Leertaste) angekommen ist.
     */
    public void tryJumping() {
        if(physics.testStanding()) {
            //Figur steht -> Jump
            physics.applyImpulse(new Vector(0, -2000));
            setState("jumpingUp");
        }
    }

    public void setMovementState(MovementState state) {
        this.movementState = state;
    }

    public MovementState getMovementState() {
        return this.movementState;
    }

    /**
     * Wird frameweise aufgerufen: Checkt den aktuellen state des Characters und macht ggf. Änderungen
     */
    public void framewiseUpdate(int frameDuration) {
        Vector velocity = physics.getVelocity();
        //boolean standing = physics.testStanding();

        //kümmere dich um die Horizontale Bewegung
        float desiredVelocity = movementState.getTargetXVelocity();
        float velocityChange = desiredVelocity - velocity.x;
        float impulse = velocityChange;
        physics.applyForce(new Vector(impulse, 0));

        switch(getCurrentState()) {
            case "jumpingUp":
                if ( velocity.y > 0) setState("midair");
                break;
            case "idle":
            case "running":
            case "walking":
                //if(standing) {
                    if ( velocity.y > 0.1f) setState("midair");
                    else if(Math.abs(velocity.x) > 550f) changeState("running");
                    else if(Math.abs(velocity.x) > 10f) changeState("walking");
                    else changeState("idle");
                //}
                break;
        }


        if(velocity.x > 0) {
            setFlipHorizontal(false);
            //if(standing && !getCurrentState().equals("running")) setState("running");
        } else if (velocity.x < 0) {
            setFlipHorizontal(true);
            //if(standing && !getCurrentState().equals("running")) setState("running");
        }


    }

    @Override
    public void onCollision(CollisionEvent<Actor> collisionEvent) {
        if(collisionEvent.getColliding() instanceof Enemy) return;
        if(getCurrentState().equals("falling") && physics.testStanding()) {
            setState("landing");
        }
    }
}