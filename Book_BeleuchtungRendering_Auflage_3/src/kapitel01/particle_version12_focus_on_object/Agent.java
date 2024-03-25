package kapitel01.particle_version12_focus_on_object;

import kapitel01.POGL;
import kapitel04.Vektor2D;

public class Agent extends BewegendesObjekt {
	private static int objCounter = 0;
	private int radius;
	private float r, g, b, a;
	public ObjektManager objektManager;

	public Agent(Vektor2D position, Vektor2D velocity, int radius, float r, float g, float b) {
		super(position, velocity);
		this.radius = radius;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1;
		this.id = ++objCounter;

		setMass(1);
		setMaxSpeed(100);
		setMaxTurnRate(5);

		setWegHistorie(new Weg2DDynamisch(20));
	}

	public void setObjektManager(ObjektManager objektManager) {
		this.objektManager = objektManager;
	}

	@Override
	public void render() {
		//POGL.renderObjectWithPath((float) position.x, (float) position.y, r, g, b, a, radius, getWegHistorie());
		POGL.renderObjectWithForces((float) position.x, (float) position.y, 10, velocity, getLastAcceleration());
	}
}
