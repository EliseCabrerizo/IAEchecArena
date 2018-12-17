import java.util.ArrayList;

import com.sun.xml.internal.ws.dump.LoggingDumpTube.Position;

public class Search {

	public static int Max_depth = 4;

	public static int White = 1;

	public static int Black = 0;

	// methode pour trouver le meilleur coup
	public static String get_bestmove(long WK, long WQ, long WR, long WB, long WN, long WP, long BK, long BQ, long BR,
			long BB, long BN, long BP, boolean WhiteMove) {

		int depth = 0; // profondeur

		Case[][] echiquier = new Case[8][8];
		// construction de l'echiquier
		for (int i = 0; i < 64; i++) {
			echiquier[i / 8][i % 8] = new Case();
		}
		for (int i = 0; i < 64; i++) {
			if (((WP >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('P');
			}
			if (((WN >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('C');
			}
			if (((WB >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('F');
			}
			if (((WR >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('T');
			}
			if (((WQ >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('D');
			}
			if (((WK >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('R');
			}
			if (((BP >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('p');
			}
			if (((BN >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('c');
			}
			if (((BB >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('f');
			}
			if (((BR >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('t');
			}
			if (((BQ >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('d');
			}
			if (((BK >> i) & 1) == 1) {
				echiquier[i / 8][i % 8].setOccupe('r');
			}
		}

		String bestmove = AlphaBetaSearch(echiquier, depth, WhiteMove); // min_maxAlphaBeta
		return bestmove;
	}

	public static String AlphaBetaSearch(Case[][] echiquier, int prof, boolean W) {
		String mvt = MaxValue(echiquier, -150000, 150000, prof, "", W);
		return mvt.substring(0, 4);
	}

	private static String MaxValue(Case[][] echiquier, int a, int b, int prof, String bestMove, boolean W) {

		int bestScore = 0;
		if (prof == Max_depth) {
			bestScore = Evaluation.Best(echiquier, W);
			return bestMove + bestScore;
		}
		int v = -150000;
		String mvtDispo = "";
		if (W) {
			mvtDispo = Move.calculW(echiquier);
		} else {
			mvtDispo = Move.calculB(echiquier);
		}
		String move = "";
		for (int i1 = 0; i1 <= mvtDispo.length() - 4; i1 += 4) {
			move = mvtDispo.substring(i1, i1 + 4);
			Case[][] echiquierTemp = new Case[8][8];

			// Copie de l'echiqiuer dans un echiqiuer temporaire
			for (int m = 0; m < echiquierTemp.length; m++) {
				for (int n = 0; n < echiquierTemp.length; n++) {
					echiquierTemp[m][n] = echiquier[m][n];
				}
			}
			// Transforme le mouvement en position de matrice de la forme i j
			int[] tempDebut = StringToInt(move.substring(0, 2));
			int[] tempFin = StringToInt(move.substring(2, 4));

			// Execution du mouvement
			echiquierTemp[tempFin[0]][tempFin[1]].setOccupe(echiquierTemp[tempDebut[0]][tempDebut[1]].isOccupe());
			echiquierTemp[tempDebut[0]][tempDebut[1]].setOccupe('v');
			// if (!estEnEchec(echiquierTemp, W)) {
			v = Math.max(v, new Integer(MinValue(echiquierTemp, a, b, prof + 1, move, !W).substring(4)));
			// } else {
			// v = -150000;
			// }

			if (v >= b)
				return move + v;
			a = Math.max(a, v);

		}
		return move + v;
	}

	private static String MinValue(Case[][] echiquier, int a, int b, int prof, String bestMove, boolean W) {
		int bestScore = 0;
		if (prof == Max_depth) {
			bestScore = Evaluation.Best(echiquier, W);
			return bestMove + bestScore;
		}
		int v = 150000;
		String mvtDispo = "";
		if (W) {
			mvtDispo = Move.calculW(echiquier);
		} else {
			mvtDispo = Move.calculB(echiquier);
		}
		String move = "";
		for (int i1 = 0; i1 <= mvtDispo.length() - 4; i1 += 4) {
			move = mvtDispo.substring(i1, i1 + 4);
			Case[][] echiquierTemp = new Case[8][8];

			// Copie de l'echiqiuer dans un echiqiuer temporaire
			for (int m = 0; m < echiquierTemp.length; m++)
				for (int n = 0; n < echiquierTemp.length; n++) {
					echiquierTemp[m][n] = echiquier[m][n];
				}
			// Transforme le mouvement en position de matrice de la forme i j
			int[] tempDebut = StringToInt(move.substring(0, 2));
			int[] tempFin = StringToInt(move.substring(2, 4));

			// Execution du mouvement
			echiquierTemp[tempFin[0]][tempFin[1]].setOccupe(echiquierTemp[tempDebut[0]][tempDebut[1]].isOccupe());
			echiquierTemp[tempDebut[0]][tempDebut[1]].setOccupe('v');

			// if (!estEnEchec(echiquierTemp, W)) {
			v = Math.min(v, new Integer(MaxValue(echiquierTemp, a, b, prof + 1, move, !W).substring(4)));
			// } else {
			// v = 150000;
			// }
			if (v <= a)
				return move + v;
			b = Math.min(b, v);
		}
		return move + v;
	}

	/**
	 * 
	 * @param mvt
	 *            lettre de la colonne + numero de la ligne
	 * @return Un tableau de deux entiers representant le numero de la ligne et de
	 *         la colonne d'une matrice. Exemple : "a8" => "00" ou "h1" => "77"
	 */
	public static int[] StringToInt(String mvt) {
		int[] aRetourner = new int[2];
		char number = mvt.charAt(0);
		aRetourner[0] = 8 - Integer.parseInt(mvt.substring(1));
		aRetourner[1] = Character.getNumericValue(number) - 10;
		// System.out.println(mvt+" => "+aRetourner[0]+""+aRetourner[1]);
		return aRetourner;
	}

	/**
	 * 
	 * @param echiquierTemp
	 *            Echiqiuer modifie par le mouvement qu'on desire verifier sa
	 *            faisabilite
	 * @param white
	 *            Couleur du roi
	 * @return Vrai si le roi de la couleur white est en echec, Faux si le roi n'est
	 *         pas en echec
	 */
	private static boolean estEnEchec(Case[][] echiquierTemp, boolean white) {
		boolean resultat = false;

		// Recherche de la position du roi de la couleur white
		char c = white ? 'R' : 'r';
		ArrayList<int[]> DepartureBox = Move.calculDepartureBox(echiquierTemp, c);

		if (DepartureBox.size() != 1) {
			// System.err.println(white);
			// Move.afficherEchiquier(echiquierTemp);
			// System.err.println("Il n'y a plus de roi ou il y a plusieurs rois");
		} else {
			// Conversion de la position du roi en String
			String positionRoi = Move.IntToString(DepartureBox.get(0)[0], DepartureBox.get(0)[1]);

			// Calcul des movements possibles de l'adversaire

			// System.out.println("");
			// Recuperation des mouvements de l'ennemi
			String movesEnnemy = white ? Move.calculB(echiquierTemp) : Move.calculW(echiquierTemp);
			// System.out.println("\n");

			// le booleen resultat passe a vrai des qu'un ennemi peut capturer le roi
			int i = 0;
			while (i <= movesEnnemy.length() - 4 && !resultat) {
				// System.out.println(positionRoi + " " + movesEnnemy.substring(i + 2, i + 4));
				resultat = positionRoi.equals(movesEnnemy.substring(i + 2, i + 4));
				i += 4;
			}
		}
		return resultat;
	}
}