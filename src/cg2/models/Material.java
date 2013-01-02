package cg2.models;

import cg2.lights.AmbientLight;
import cg2.lights.LightSource;
import cg2.raytracer.Hit;
import cg2.vecmath.Color;
import cg2.vecmath.Vector;

public class Material {

	private Color ambientCo = new Color(0,0,0);
	private Color diffuseCo = new Color(0,0,0);
	private Color specularCo = new Color(0,0,0);
	private float phongExponent = 0;
	private float reflectionCo = 0;
	private float refractionMediumOutside = 0;
	private float refractionMediumInside = 0;
	

	public Material() {
		
	}
	
	public Material(Color ambientCo, Color diffuseCo, Color specularCo, float phongExponent) {
		this.ambientCo = ambientCo;
		this.diffuseCo = diffuseCo;
		this.specularCo = specularCo;
		this.phongExponent = phongExponent;
	}
	
	public Material(Color ambientCo, Color diffuseCo, Color specularCo, float phongExponent, float reflectionCo) {
		this.ambientCo = ambientCo;
		this.diffuseCo = diffuseCo;
		this.specularCo = specularCo;
		this.phongExponent = phongExponent;
		this.reflectionCo = reflectionCo;
	}
	
	
	public Material(Color ambientCo, Color diffuseCo, Color specularCo, float phongExponent, float reflectionCo, float refractionMediumOutside , float refractionMediumInside) {
		this.ambientCo = ambientCo;
		this.diffuseCo = diffuseCo;
		this.specularCo = specularCo;
		this.phongExponent = phongExponent;
		this.reflectionCo = reflectionCo;
		this.refractionMediumOutside = refractionMediumOutside;
		this.refractionMediumInside = refractionMediumInside;
	}

	public Color getAmbientCo() {
		return ambientCo;
	}

	public void setAmbientCo(Color ambientCo) {
		this.ambientCo = ambientCo;
	}

	public Color getDiffuseCo() {
		return diffuseCo;
	}

	public void setDiffuseCo(Color diffuseCo) {
		this.diffuseCo = diffuseCo;
	}

	public Color getSpecularCo() {
		return specularCo;
	}

	public void setSpecularCo(Color specularCo) {
		this.specularCo = specularCo;
	}

	public float getPhongExponent() {
		return phongExponent;
	}

	public void setPhongExponent(float phongExponent) {
		this.phongExponent = phongExponent;
	}
	
	public void setReflectionCo(float reflectionCoeff) {
		this.reflectionCo = reflectionCoeff;
	}

	public float getReflectionCo() {
		return reflectionCo;
	}
	
	public void setRefractionMediumOutside(float refractionMediumOutside) {
		this.refractionMediumOutside = refractionMediumOutside;
	}
	
	public float getRefractionMediumOutside() {
		return refractionMediumOutside;
	}
	
	public void setRefractionMediumInside(float refractionMediumInside) {
		this.refractionMediumInside = refractionMediumInside;
	}
	
	public float getRefractionMediumInside() {
		return refractionMediumInside;
	}
	
	
	public Color computeAmbient(AmbientLight light) {
		Color ambientLight = new Color(0,0,0);
		if (light.isEnabled()) {
			// Von Quelle j in Punkt p eintreffende Lichtmenge	
			ambientLight = light.getLightQuantity();
			// berechne Licht mit dem Ambienten Koeffizienten
			return ambientLight.modulate(ambientCo);
		}
		return ambientLight;
	}

	public Color computeDiffuseLight(Hit hit, LightSource light) {
		Color diffuseLight = new Color(0, 0, 0);
		if (light.isEnabled()) {
			// Von Quelle j in Punkt p eintreffende Lichtmenge	
			Color lj = light.getLightQuantity();
			// Schnittpunkt von Strahl und Objekt
			Vector hitPoint = hit.getIntersectionPoint();
			// Richtungsvektor Light
			Vector s = light.getPosition().sub(hitPoint).normalize();
			// Normale des Schnittounkts
			Vector n = hit.getNormal();
			// Winkel‐Abschwächung	des Lichts aus Richtung sj	
			float cosa = n.dot(s);
			// Prüfung ob Licht Vorederseite
			if (cosa > 0) {
				// berechne diffusen Anteil
				return diffuseLight.add(lj.modulate(cosa).modulate(diffuseCo));
			}
		}
		return diffuseLight;
	}

	public Color computeSpecularLight(Hit hit, LightSource light) {
		Color specularLight = new Color(0, 0, 0);
		if (light.isEnabled()) {
			// Von Quelle j in Punkt p eintreffende Lichtmenge	
			Color lj = light.getLightQuantity();
			// Betrachter
			Vector v = hit.getRay().getDirection().normalize().mult(-1);
			// Richtungsvektor Light
			Vector s = light.getPosition().sub(hit.getIntersectionPoint()).normalize();
			// Normale des Hits
			Vector n = hit.getNormal();
			// ideale Reflexionsrichtung
			Vector r = n.mult(n.dot(s)*2).sub(s);
			// Winkel zwischen Beterachter und idealer Reflexionsrichtung 
			float vDotr = v.dot(r);
			// Prüfung ob Licht auf Vorderseite
			if (vDotr > 0 && n.dot(s) > 0) {
				// Berechnung des spekularen Anteils
				specularLight = lj.modulate((float)Math.pow(vDotr, phongExponent)).modulate(specularCo);
			}
		}
		return specularLight;
	}
}
