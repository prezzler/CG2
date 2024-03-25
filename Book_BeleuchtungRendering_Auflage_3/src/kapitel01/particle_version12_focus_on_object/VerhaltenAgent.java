package kapitel01.particle_version12_focus_on_object;

import kapitel04.LineareAlgebra;
import kapitel04.Vektor2D;

public class VerhaltenAgent implements Verhalten {
	private Agent agent;
	private Steuerungsverhalten steering;

	public VerhaltenAgent(Agent agent) {
		this.agent = agent;
		this.steering = new Steuerungsverhalten();
	}

	@Override
	public void update(double time) {
		/*Vektor2D mausForce = steering.forceMousePosition(agent.position);
		mausForce.mult(100);
		agent.applyForce(mausForce);*/

		Vektor2D forceSeek = steering.forceSeek(agent.position, agent.velocity, steering.mousePosition());
		forceSeek.mult(agent.MAX_SPEED);
		agent.applyForce(forceSeek);
		
		// Rotationsrate einbeziehen!
		Vektor2D wunschVelocity = LineareAlgebra.add(agent.getVelocity(), LineareAlgebra.mult(agent.getAccelerationInRespectToMass(), time));
		if (agent.getVelocity().length()!=0 && wunschVelocity.length()!=0) {
			double kosinusFormel = LineareAlgebra.kosinusFormel(wunschVelocity, agent.getVelocity());
			double acos 		 = Math.acos(Math.min(1, kosinusFormel));
			double winkel 		 = LineareAlgebra.radToDegree(acos);
		
			if (winkel < agent.getMaxTurnRate()) {
				agent.setVelocity(LineareAlgebra.truncate(wunschVelocity, agent.getMaxSpeed()));
			} else {
				// mathematisch positiv
				if (LineareAlgebra.sinusFormel(wunschVelocity, agent.getVelocity()) > 0) {
					agent.setVelocity(LineareAlgebra.rotate(agent.getVelocity(),-agent.getMaxTurnRate()));
				} else { // mathematisch negativ
					agent.setVelocity(LineareAlgebra.rotate(agent.getVelocity(), agent.getMaxTurnRate()));
				}
				agent.setVelocity(LineareAlgebra.normalize(agent.getVelocity()));
				agent.setVelocity(LineareAlgebra.mult(agent.getVelocity(), wunschVelocity.length()));
				agent.setVelocity(LineareAlgebra.truncate(agent.getVelocity(), agent.getMaxSpeed()));
			}
		} else 
			agent.setVelocity(LineareAlgebra.truncate(wunschVelocity, agent.getMaxSpeed()));	
		
		agent.setPosition(LineareAlgebra.add(agent.getPosition(), LineareAlgebra.mult(agent.getVelocity(), time)));
		agent.getWegHistorie().addWaypoint(agent.getPosition());
		
		agent.resetAcceleration();
		steering.resetAcceleration();
	}
}
