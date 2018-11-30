public class Search {

	public static int Max_depth = 1;

	public static int White = 1;

	public static int Black = 0;

	// methode pour trouver le meilleur coup
	public static String get_bestmove(long WK,
			long WQ, long WR, long WB, long WN, long WP, long BK, long BQ,
			long BR, long BB, long BN, long BP, boolean WhiteMove) {
		
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
		
		String bestmove = SuperMinMax_Alpha_Beta_Gamma_Omega(-100000, 100000,echiquier, WhiteMove, depth, ""); // min_maxAlphaBeta
		return bestmove;
	}

	public static String SuperMinMax_Alpha_Beta_Gamma_Omega(int alpha, int beta, Case[][] echiquier, boolean W,
			int prof, String mvt) {
		String Bestmove = " ";
		int bestScore = 0;
		String mvtDispo = "";
		String alphamove = "0000";
		String betamove = "0000";
		if (prof == Max_depth) { // si on a atteint la prof max on va evaluer le meilleur coup ??
			bestScore = Evaluation.Best(echiquier, W);
			Bestmove = mvt + bestScore;
			return Bestmove;
		}
		if (W) {
			mvtDispo = Move.calculW(echiquier);
		} else {
			mvtDispo = Move.calculB(echiquier);
		}
		for (int i = 0; i < mvtDispo.length(); i += 4) {
			String move = mvtDispo.substring(i, i + 4);
			Case[][] echiquierTemp = new Case[8][8];
			echiquierTemp = echiquier; // a voir si ça passe par pointeur (peut être bug)
			// Transforme le mouvement en position de matrice
			int[] tempDebut = StringToInt(move.substring(0, 1));
			int[] tempFin = StringToInt(move.substring(2, 3));
			echiquierTemp[tempFin[1]][tempFin[0]].setOccupe(echiquier[tempDebut[1]][tempDebut[0]].isOccupe());
			echiquierTemp[tempDebut[1]][tempDebut[0]].setOccupe('v');

			SuperMinMax_Alpha_Beta_Gamma_Omega(alpha, beta, echiquierTemp, !W, prof, move);

			// Renvoie le score du mouvement, il se situe après le mouvement donc après 4
			bestScore = Integer.valueOf(Bestmove.substring(4));

			if (!W) { // fait le changement alpha beta, en fct de quel joueur on calcul
				if (bestScore < beta) {
					beta = bestScore;
					betamove = move;
				}
			} else {
				if (bestScore > alpha) {
					alpha = bestScore;
					alphamove = move;
				}
			}

			if (alpha >= beta) {
				if (!W) {
					return betamove + beta;
				} else {
					return alphamove + alpha;
				}
			}
		}
		if (!W) {
			return betamove + beta;
		} else {
			return alphamove + alpha;
		}
	}

	public static int[] StringToInt(String mvt) {
		int[] aRetourner = new int[2];
		char number = mvt.charAt(0);
		aRetourner[0] = Character.getNumericValue(number) - 10;
		aRetourner[1] = Integer.parseInt(mvt.substring(1)) - 1;
		return aRetourner;
	}

	public static int ischeck(long WK,
			long WQ, long WR, long WB, long WN, long WP, long BK, long BQ,
			long BR, long BB, long BN, long BP, int player) {
		int bestscore = 0;
		// if(player == White){
		long Blackatt_map = Move.attackbitmapB(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP);
		if ((WK & Blackatt_map) != 0) {
			bestscore = 5000;
		}
		// }else{
		long Whiteatt_map = Move.attackbitmapW(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP);
		if ((BK & Whiteatt_map) != 0) {
			bestscore = -5000;
		}
		// }
		return bestscore;
	}

}