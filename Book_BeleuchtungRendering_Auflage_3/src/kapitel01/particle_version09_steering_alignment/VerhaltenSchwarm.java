package kapitel01.particle_version09_steering_alignment;

import kapitel04.Vektor2D;

public class VerhaltenSchwarm implements Verhalten {
	private Flummi flummi;
	private Steuerungsverhalten steering;
	private final float MAX_VELOCITY = 10;

	public VerhaltenSchwarm(Flummi flummi) {
		this.flummi = flummi;
		this.steering = new Steuerungsverhalten();
	}

	@Override
	public void update() {
		Vektor2D mausForce = steering.followMousePosition(flummi.position);
		mausForce.mult(0.8);
		steering.applyForce(mausForce);
		Vektor2D separationForce = steering.separation(flummi, 5);
		steering.applyForce(separationForce);
		Vektor2D alignmentForce = steering.alignment(flummi, 10);
		alignmentForce.mult(0.12);
		steering.applyForce(alignmentForce);

		flummi.velocity.add(steering.acceleration);
		flummi.velocity.truncate(MAX_VELOCITY);
		flummi.position.add(flummi.velocity);

		steering.resetAcceleration();
	}
}
