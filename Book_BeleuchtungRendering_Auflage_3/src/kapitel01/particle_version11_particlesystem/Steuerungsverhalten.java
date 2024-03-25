package kapitel01.particle_version11_particlesystem;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import kapitel04.Vektor2D;
import kapitel04.LineareAlgebra;

public class Steuerungsverhalten {
	public Vektor2D acceleration;
	private Random zuf = ThreadLocalRandom.current();

	public Steuerungsverhalten() {
		acceleration = new Vektor2D(0, 0);
	}

	public void resetAcceleration() {
		acceleration.mult(0);
	}

	public void applyForce(Vektor2D force) {
		Vektor2D forceHelp = new Vektor2D(force);
		acceleration.add(forceHelp);
	}

	public Vektor2D randomForce() {
		return new Vektor2D(zuf.nextFloat() * 10 - 5, zuf.nextFloat() * 10 - 5);
	}

	public Vektor2D followMousePosition(Vektor2D currentPosition) {
		Vektor2D mousePosition = new Vektor2D(Mouse.getX(), Display.getDisplayMode().getHeight() - Mouse.getY());
		mousePosition.sub(currentPosition);
		mousePosition.normalize();
		return mousePosition;
	}

	public Vektor2D separation(Partikel me, float dist) {
		Vektor2D steeringForce = new Vektor2D(0, 0);
		for (int i = 0; i < me.objektManager.getPartikelSize(); i++) {
			if (me.id == i)
				continue;

			BasisObjekt bObj = me.objektManager.getPartikel(i);
			if (bObj instanceof Partikel) {
				Partikel bObjF = (Partikel)bObj;
				if (LineareAlgebra.euklDistanz(me.position, bObjF.position) < dist)
					steeringForce.add(LineareAlgebra.sub(me.position, bObjF.position));
			}
		}
		LineareAlgebra.normalize(steeringForce);
		return steeringForce;
	}

	public Vektor2D alignment(Partikel me, float dist) {
		Vektor2D steeringForce = new Vektor2D(0, 0);
		for (int i = 0; i < me.objektManager.getPartikelSize(); i++) {
			if (me.id == i)
				continue;

			BasisObjekt bObj = me.objektManager.getPartikel(i);
			if (bObj instanceof Partikel) {
				Partikel bObjF = (Partikel)bObj;
				if (LineareAlgebra.euklDistanz(me.position, bObjF.position) < dist)
					steeringForce.add(bObjF.velocity);
			}
		}

		LineareAlgebra.normalize(steeringForce);
		return steeringForce;
	}

	public Vektor2D cohesion(Partikel me, float dist) {
		Vektor2D steeringForce = new Vektor2D(0, 0);
		for (int i = 0; i < me.objektManager.getPartikelSize(); i++) {
			if (me.id == i)
				continue;

			BasisObjekt bObj = me.objektManager.getPartikel(i);
			if (bObj instanceof Partikel) {
				Partikel bObjF = (Partikel)bObj;
				if (LineareAlgebra.euklDistanz(me.position, bObjF.position) < dist)
					steeringForce.add(LineareAlgebra.sub(bObjF.position, me.position));
			}
		}

		LineareAlgebra.normalize(steeringForce);
		return steeringForce;
	}

}
