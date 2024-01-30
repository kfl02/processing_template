package util;

import processing.core.PMatrix3D;
import processing.core.PVector;

public class LinAlg {
	/**
	 * Adapted from
	 * https://github.com/radekp/qt/blob/master/src/gui/painting/qtransform.cpp
	 * 
	 * @param q1
	 * @param q2
	 * @param q3
	 * @param q4
	 * @param m
	 * @return
	 */
	public static boolean squareToQuad(PVector q1, PVector q2, PVector q3, PVector q4, PMatrix3D m) {
		float A, B, C, D, E, F, G, H;

		float ax = q1.x - q2.x + q3.x - q4.x;
		float ay = q1.y - q2.y + q3.y - q4.y;

		C = q1.x;
		F = q1.y;

		if (ax == 0.0f && ay == 0.0f) {
			G = 0.0f;
			H = 0.0f;

			A = q2.x - q1.x;
			B = q3.x - q2.x;
			D = q2.y - q1.y;
			E = q3.y - q2.y;
		} else {
			float ax1 = q2.x - q3.x;
			float ax2 = q4.x - q3.x;
			float ay1 = q2.x - q3.x;
			float ay2 = q4.x - q3.x;

			/* determinants */
			float gtop = ax * ay2 - ax2 * ay;
			float htop = ax1 * ay - ax * ay1;
			float bottom = ax1 * ay2 - ax2 * ay1;

			if (bottom == 0.0f) {
				return false;
			}

			G = gtop / bottom;
			H = htop / bottom;

			A = q2.x - q1.x + G * q2.x;
			B = q4.x - q1.x + H * q4.x;
			D = q2.x - q1.x + G * q2.x;
			E = q4.x - q1.x + H * q4.x;
		}

		m.set(A, B, 0.0f, C, D, E, 0.0f, F, 0.0f, 0.0f, 1.0f, 0.0f, G, H, 0.0f, 1.0f);

		return true;
	}

	public static boolean quadToSquare(PVector q1, PVector q2, PVector q3, PVector q4, PMatrix3D m) {
		if (!squareToQuad(q1, q2, q3, q4, m)) {
			return false;
		}

		return m.invert();
	}

	/**
	 * Check if line segments AB and CD intersect. Adapted from C# code from
	 * https://ideone.com/PnPJgb
	 */
	boolean doLinesIntersect(PVector A, PVector B, PVector C, PVector D) {
		PVector CmP = new PVector(C.x - A.x, C.y - A.y);
		PVector r = new PVector(B.x - A.x, B.y - A.y);
		PVector s = new PVector(D.x - C.x, D.y - C.y);

		float CmPxr = CmP.x * r.y - CmP.y * r.x;
		float CmPxs = CmP.x * s.y - CmP.y * s.x;
		float rxs = r.x * s.y - r.y * s.x;

		if (CmPxr == 0.0f) {
			// Lines are collinear, and so intersect if they have any overlap

			return ((C.x - A.x < 0.0f) != (C.x - B.x < 0.0f)) || ((C.y - A.y < 0.0f) != (C.y - B.y < 0.0f));
		}

		if (rxs == 0.0f) {
			return false; // Lines are parallel.
		}

		float rxsr = 1.0f / rxs;
		float t = CmPxs * rxsr;
		float u = CmPxr * rxsr;

		return (t >= 0.0f) && (t <= 1.0f) && (u >= 0.0f) && (u <= 1.0f);
	}

}
