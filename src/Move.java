import java.util.*;

public class Move {
	static long FILE_A = 1L | 1L << 8 | 1L << 16 | 1L << 24 | 1L << 32 | 1L << 40 | 1L << 48 | 1L << 56;
	static long FILE_H = 1L << 7 | 1L << 15 | 1L << 23 | 1L << 31 | 1L << 39 | 1L << 47 | 1L << 55 | 1L << 63;
	static long RANK_4 = 1L << 32 | 1L << 33 | 1L << 34 | 1L << 35 | 1L << 36 | 1L << 37 | 1L << 38 | 1L << 39;
	static long VACANT;
	static long OCCUPIED;
	static long BLACK_PIECE;
	static long WHITE_PIECE;
	static long NOT_ATTACK_OWN;
	static long BLACK_NOT_ATTACK;
	static long Attack_bits;
	static long RANK_Masks[] = { 0xFF00000000000000L, 0xFF000000000000L, 0xFF0000000000L, 0xFF00000000L, 0xFF000000L,
			0xFF0000L, 0xFF00L, 0xFFL };
	static long RANK_Masks_flip[] = { 0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L,
			0xFF000000000000L, 0xFF00000000000000L };
	static long FILE_Masks[] = { 0x0101010101010101L, 0x0202020202020202L, 0x0404040404040404L, 0x0808080808080808L,
			0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L };
	static long leftDiagonal_Mask[] = { 0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L,
			0x1020408102040L, 0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
			0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L };
	static long rightDiagonal_Mask[] = { 0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L,
			0x80402010080402L, 0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
			0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L };

	// calcul le mvt d'une piece

	public static long makemove(long pieceboard, String move, char piecetype) {
		if (Character.isDigit(move.charAt(3))) {
			int begin = Character.getNumericValue(move.charAt(0)) * 8 + Character.getNumericValue(move.charAt(1));
			int end = Character.getNumericValue(move.charAt(2)) * 8 + Character.getNumericValue(move.charAt(3));
			if (((pieceboard >>> begin) & 1) == 1) {
				pieceboard &= ~(1L << begin);
				pieceboard |= (1L << end); // move the piece from begin to end
			} else {
				pieceboard &= ~(1L << end); // piece is captured by opposition
			}
		} else {
			int begin, end;
			if (Character.isUpperCase(move.charAt(2))) {
				begin = Long.numberOfTrailingZeros(Move.FILE_Masks[move.charAt(0) - '0'] & Move.RANK_Masks_flip[1]);
				end = Long.numberOfTrailingZeros(Move.FILE_Masks[move.charAt(1) - '0'] & Move.RANK_Masks_flip[0]);
			} else {
				begin = Long.numberOfTrailingZeros(Move.FILE_Masks[move.charAt(0) - '0'] & Move.RANK_Masks_flip[6]);
				end = Long.numberOfTrailingZeros(Move.FILE_Masks[move.charAt(1) - '0'] & Move.RANK_Masks_flip[7]);
			}
			if (piecetype == move.charAt(2)) {
				pieceboard |= (1L << end);
			} else {
				pieceboard &= ~(1L << begin);
				pieceboard &= ~(1L << end);
			}
		}
		return pieceboard;
	}

	/**
	 * @param echiquier
	 * @param white
	 * @return
	 */
	public static String Bishop(Case[][] echiquier, boolean white) {

		String move = "";
		char c = white ? 'F' : 'f';

		ArrayList<int[]> DepartureBox = calculDepartureBox(echiquier, c);
		// System.out.println("size = " + DepartureBox.size());
		if (!DepartureBox.isEmpty()) {
			// for (ArrayList<Integer> tab : DepartureBox) {
			for (int i = 0; i < DepartureBox.size(); i++) {
				// tab[0] = DepartureBox.get(i).get(0);
				// tab[1] = DepartureBox.get(i)[1];
				int[] tab = new int[2];
				tab = DepartureBox.get(i);
				// System.out.println("aa " + IntToString(tab[0], tab[1]));
				move += deplacementDiagonale(tab, white, echiquier);
			}
		}
		// System.out.print("Fou " + (white ? "white" : "black") + " ");
		// afficherMove(move);
		return move;
	}

	public static String Rook(Case[][] echiquier, boolean white) {
		String move = "";
		char c = white ? 'T' : 't';

		ArrayList<int[]> DepartureBox = calculDepartureBox(echiquier, c);

		if (!DepartureBox.isEmpty()) {
			for (int[] tab : DepartureBox) {
				move += deplacementCroix(tab, white, echiquier);
			}
		}
		// System.out.print("Tour " + (white ? "white" : "black") + " ");
		// afficherMove(move);
		return move;
	}

	public static String Queen(Case[][] echiquier, boolean white) {
		String move = "";
		char c = white ? 'D' : 'd';

		ArrayList<int[]> DepartureBox = calculDepartureBox(echiquier, c);
		if (!DepartureBox.isEmpty()) {
			for (int[] tab : DepartureBox) {
				move += deplacementDiagonale(tab, white, echiquier);
				move += deplacementCroix(tab, white, echiquier);
			}
		}
		// System.out.print("Dame " + (white ? "white" : "black") + " ");
		// afficherMove(move);
		return move;
	}

	public static String Pawn(Case[][] echiquier, boolean white) {
		String move = "";
		char c = white ? 'P' : 'p';
		ArrayList<int[]> DepartureBox = calculDepartureBox(echiquier, c);

		if (!DepartureBox.isEmpty()) {
			for (int[] tab : DepartureBox) {
				move += deplacementPion(tab, white, echiquier);
			}
		}
		// System.out.print("Pion " + (white ? "white" : "black") + " ");
		// afficherMove(move);
		return move;
	}

	public static String King(Case[][] echiquier, boolean white) {
		String move = "";
		char c = white ? 'R' : 'r';

		ArrayList<int[]> DepartureBox = calculDepartureBox(echiquier, c);

		if (!DepartureBox.isEmpty()) {
			for (int[] tab : DepartureBox) {

				// Deplacement de 1 a droite et 1 en-bas
				move += caseEstDiponible(echiquier, tab[0] + 1, tab[1] + 1, white, tab)[1];

				// Deplacement de 1 a droite
				move += caseEstDiponible(echiquier, tab[0], tab[1] + 1, white, tab)[1];

				// Deplacement de 1 a droite et 1 en-haut
				move += caseEstDiponible(echiquier, tab[0] - 1, tab[1] + 1, white, tab)[1];

				// Deplacement de 1 en-haut
				move += caseEstDiponible(echiquier, tab[0] - 1, tab[1], white, tab)[1];

				// Deplacement de 1 a gauche et 1 en-haut
				move += caseEstDiponible(echiquier, tab[0] - 1, tab[1] - 1, white, tab)[1];

				// Deplacement de 1 a gauche
				move += caseEstDiponible(echiquier, tab[0], tab[1] - 1, white, tab)[1];

				// Deplacement de 1 en-bas et 1 a gauche
				move += caseEstDiponible(echiquier, tab[0] + 1, tab[1] - 1, white, tab)[1];

				// Deplacement de 1 en-bas
				move += caseEstDiponible(echiquier, tab[0] + 1, tab[1], white, tab)[1];
			}
		}

		// System.out.print("Roi " + (white ? "white" : "black") + " ");
		// afficherMove(move);
		return move;
	}

	public static String Knight(Case[][] echiquier, boolean white) {
		String move = "";
		char c = white ? 'C' : 'c';

		ArrayList<int[]> DepartureBox = calculDepartureBox(echiquier, c);

		if (!DepartureBox.isEmpty()) {
			for (int[] tab : DepartureBox) {
				// Deplacement de 2 a droite et 1 en-bas
				move += caseEstDiponible(echiquier, tab[0] + 1, tab[1] + 2, white, tab)[1];

				// Deplacement de 2 a droite et 1 en-haut
				move += caseEstDiponible(echiquier, tab[0] - 1, tab[1] + 2, white, tab)[1];

				// Deplacement de 2 en-haut et 1 a droite
				move += caseEstDiponible(echiquier, tab[0] - 2, tab[1] + 1, white, tab)[1];

				// Deplacement de 2 en-haut et 1 a gauche
				move += caseEstDiponible(echiquier, tab[0] - 2, tab[1] - 1, white, tab)[1];

				// Deplacement de 2 a gauche et 1 en-haut
				move += caseEstDiponible(echiquier, tab[0] - 1, tab[1] - 2, white, tab)[1];

				// Deplacement de 2 a gauche et 1 en-bas
				move += caseEstDiponible(echiquier, tab[0] + 1, tab[1] - 2, white, tab)[1];

				// Deplacement de 2 en-bas et 1 a gauche
				move += caseEstDiponible(echiquier, tab[0] + 2, tab[1] - 1, white, tab)[1];

				// Deplacement de 2 en-bas et 1 a droite
				move += caseEstDiponible(echiquier, tab[0] + 2, tab[1] + 1, white, tab)[1];

			}
		}
		// System.out.print("Cavalier " + (white ? "white" : "black") + " ");
		// afficherMove(move);
		return move;
	}

	/**
	 * Parcours les trajectoires verticales et horizontales pour sauver la position
	 * de depart et les position d'arrivee, determinees par les cases vides, les
	 * pieces alliees bloquantes les pieces ennemies bloquantes mais qui euvent etre
	 * mangee
	 * 
	 * @param tab
	 *            Tableau contenant la position de depart i, j
	 * @param white
	 *            bouleen
	 * @param echiquier
	 * @return Un string contenant les multiples couples (la position de depart et
	 *         la position d'arrivee)
	 */
	private static String deplacementCroix(int[] tab, boolean white, Case[][] echiquier) {
		String move = "";
		int i;
		int j;
		Object[] isEmpty_Moves = new Object[2];// premiere valeur : la case i j est vide. Deuxieme valeur : les
												// mouvements possibles

		// Deplacement en-bas
		i = tab[0] + 1;
		j = tab[1];
		isEmpty_Moves[0] = true;
		while (i < echiquier.length && (boolean) isEmpty_Moves[0]) {
			isEmpty_Moves = caseEstDiponible(echiquier, i, j, white, tab);
			// aggregation de la position de depart et de la position d'arrivee
			// if ((boolean) isEmpty_Moves[0])
			// move += IntToString(tab[0], tab[1]) + isEmpty_Moves[1];
			move += isEmpty_Moves[1];
			i++;
		}

		// Deplacement haut
		i = tab[0] - 1;
		j = tab[1];
		isEmpty_Moves[0] = true;
		while (i >= 0 && (boolean) isEmpty_Moves[0]) {
			isEmpty_Moves = caseEstDiponible(echiquier, i, j, white, tab);
			// aggregation de la position de depart et de la position d'arrivee
			// if ((boolean) isEmpty_Moves[0])
			// move += IntToString(tab[0], tab[1]) + isEmpty_Moves[1];
			move += isEmpty_Moves[1];
			i--;
		}

		// Deplacement vers la droite
		i = tab[0];
		j = tab[1] + 1;
		isEmpty_Moves[0] = true;
		while (j < echiquier.length && (boolean) isEmpty_Moves[0]) {
			isEmpty_Moves = caseEstDiponible(echiquier, i, j, white, tab);
			// aggregation de la position de depart et de la position d'arrivee
			// if ((boolean) isEmpty_Moves[0])
			// move += IntToString(tab[0], tab[1]) + isEmpty_Moves[1];
			move += isEmpty_Moves[1];
			j++;
		}

		// Deplacement vers la gauche
		i = tab[0];
		j = tab[1] - 1;
		isEmpty_Moves[0] = true;
		while (j >= 0 && (boolean) isEmpty_Moves[0]) {
			isEmpty_Moves = caseEstDiponible(echiquier, i, j, white, tab);
			// aggregation de la position de depart et de la position d'arrivee
			// if ((boolean) isEmpty_Moves[0])
			// move += IntToString(tab[0], tab[1]) + isEmpty_Moves[1];
			move += isEmpty_Moves[1];
			j--;
		}
		return move;
	}

	/**
	 * Parcours les trajectoires verticales et horizontales pour sauver la position
	 * de depart et les position d'arrivee, determinees par les cases vides, les
	 * pieces alliees bloquantes les pieces ennemies bloquantes mais qui euvent etre
	 * mangee
	 * 
	 * @param tab
	 * @param white
	 *            bouleen
	 * @param echiquier
	 * @return Un string contenant les multiples couples (la position de depart et
	 *         la position d'arrivee)
	 */
	private static String deplacementDiagonale(int[] tab, boolean white, Case[][] echiquier) {
		String move = "";
		int i;
		int j;
		// premiere valeur : la case i j est vide. Deuxieme valeur : les
		// mouvements possibles
		Object[] isEmpty_Moves = new Object[2];

		// Deplacement diagonale bas droite
		isEmpty_Moves[0] = true;
		i = tab[0] + 1;
		j = tab[1] + 1;
		while (i < echiquier.length && j < echiquier.length && (boolean) isEmpty_Moves[0]) {
			isEmpty_Moves = caseEstDiponible(echiquier, i, j, white, tab);
			// if ((boolean) isEmpty_Moves[0])
			// move += IntToString(tab[0], tab[1]) + isEmpty_Moves[1];
			move += isEmpty_Moves[1];
			i++;
			j++;
		}

		// Deplacement diagonale bas gauche
		isEmpty_Moves[0] = true;
		i = tab[0] + 1;
		j = tab[1] - 1;
		while (i < echiquier.length && j >= 0 && (boolean) isEmpty_Moves[0]) {
			isEmpty_Moves = caseEstDiponible(echiquier, i, j, white, tab);
			// if ((boolean) isEmpty_Moves[0])
			// move += IntToString(tab[0], tab[1]) + isEmpty_Moves[1];
			move += isEmpty_Moves[1];
			i++;
			j--;
		}

		// Deplacement diagonale haut gauche
		isEmpty_Moves[0] = true;
		i = tab[0] - 1;
		j = tab[1] - 1;
		while (i >= 0 && j >= 0 && (boolean) isEmpty_Moves[0]) {
			isEmpty_Moves = caseEstDiponible(echiquier, i, j, white, tab);
			// if ((boolean) isEmpty_Moves[0])
			// move += IntToString(tab[0], tab[1]) + isEmpty_Moves[1];
			move += isEmpty_Moves[1];
			i--;
			j--;
		}

		// Deplacement diagonale haut droite
		i = tab[0] - 1;
		j = tab[1] + 1;
		isEmpty_Moves[0] = true;
		while (i >= 0 && j < echiquier.length && (boolean) isEmpty_Moves[0]) {
			isEmpty_Moves = caseEstDiponible(echiquier, i, j, white, tab);
			// if ((boolean) isEmpty_Moves[0])
			// move += IntToString(tab[0], tab[1]) + isEmpty_Moves[1];
			move += isEmpty_Moves[1];
			i--;
			j++;
		}

		return move;
	}

	private static String deplacementPion(int[] tab, boolean white, Case[][] echiquier) {
		String move = "";
		int i;
		int j;
		Object[] isEmpty_Moves = new Object[2];// premiere valeur : la case i j est vide. Deuxieme valeur : les
												// mouvements possibles

		// Les blancs montent
		// Les noirs descendent
		int monter = white ? -1 : 1;

		// Deplamcement diagonale (1 verticale + 1 a droite ou a gauche)
		// verticale de 1
		i = tab[0] + monter;

		// 1 a droite
		j = tab[1] + 1;
		if ((0 <= i && i <= 7) && (0 <= j && j <= 7)) {
			isEmpty_Moves[0] = echiquier[i][j].isOccupe() == 'v'; // La case est vide, utile pour les boucles while

			// La case n'est pas vide (ennemi ou allie)
			if (!(Boolean) isEmpty_Moves[0]) {
				// Si la case est occupee par un ennemi, on ajoute le deplacement sur l'ennemei
				// aux deplacements possibles
				if (isEnnemy(white, echiquier, i, j))
					move += IntToString(tab[0], tab[1]) + IntToString(i, j);
			}
		}

		// if (j >= 0) // evite de bugger si le pion est tout e gauche
		// {
		// isEmpty_Moves[0] = echiquier[i][j].isOccupe() == 'v'; // La case est vide,
		// utile pour les boucles while
		// } else {
		// j++;
		// isEmpty_Moves[0] = echiquier[i][j].isOccupe() == 'v';
		//
		// }

		// 1 a gauche
		j = tab[1] - 1;
		if ((0 <= i && i <= 7) && (0 <= j && j <= 7)) {
			isEmpty_Moves[0] = echiquier[i][j].isOccupe() == 'v'; // La case est vide, utile pour les boucles while
			// La case n'est pas vide (ennemi ou allie)
			if (!(Boolean) isEmpty_Moves[0]) {
				// Si la case est occupee par un ennemi, on ajoute le deplacement sur l'ennemei
				// aux deplacements possibles
				if (isEnnemy(white, echiquier, i, j))
					move += IntToString(tab[0], tab[1]) + IntToString(i, j);
			}
		}

		// Deplacement verticale
		i = tab[0] + monter;
		j = tab[1];
		if ((0 <= i && i <= 7) && (0 <= j && j <= 7)) {

			isEmpty_Moves[0] = true;

			// Les pions blancs commencent en i = 6
			// Les pions noirs commencent en i = 1
			int positionDepartPion = white ? 6 : 1;
			int deplacementMax = (tab[0] == positionDepartPion) ? 2 : 1;

			// tant que le pion ne depasse pas la distance maximale
			while (0 < i && i < echiquier.length && (boolean) isEmpty_Moves[0]
					&& Math.abs(tab[0] - i) <= deplacementMax) {
				isEmpty_Moves[0] = echiquier[i][j].isOccupe() == 'v'; // La case est vide, utile pour les boucles while
				if ((boolean) isEmpty_Moves[0]) {
					move += IntToString(tab[0], tab[1]) + IntToString(i, j);
				}
				i += monter;
			}
		}
		return move;
		// if (j < 8) // evite de bugger si le pion est tout e droite
		// {
		// isEmpty_Moves[0] = echiquier[i][j].isOccupe() == 'v'; // La case est vide,
		// utile pour les boucles while
		// } else {
		// j--;
		// isEmpty_Moves[0] = echiquier[i][j].isOccupe() == 'v';
		//
		// }
	}

	/**
	 * Sauve le deplacement uniquement si la case i j est vide ou si la case est
	 * occupee par un ennemi.
	 * 
	 * @param echiquier
	 *            L'echiquier
	 * @param i
	 * @param j
	 * @param tab
	 * @param White
	 * @return Un tableau contenant un booleen qui precise si la case est vide pour
	 *         que les boucles while peuvent continuer a chercher dans cette
	 *         direction et un String contenant "ij" si c'est une piece ennemi, qui
	 *         peut etre mange
	 */
	public static Object[] caseEstDiponible(Case[][] echiquier, int i, int j, boolean white, int[] tab) {
		Object[] toReturn = { false, "" };
		// i = (i >= 8) ? 7 : i;
		// j = (j >= 8) ? 7 : j;
		//
		// i = (i < 0) ? 0 : i;
		// j = (j < 0) ? 0 : j;
		if ((0 <= i && i <= 7) && (0 <= j && j <= 7)) {
			toReturn[0] = echiquier[i][j].isOccupe() == 'v'; // La case est vide, utile pour les boucles while

			// La case n'est pas vide (a cause d'un ennemi ou d'un allie)
			if (!(Boolean) toReturn[0]) {
				// Si la case est occupee par un ennemi, on ajoute le deplacement sur l'ennemei
				// aux deplacements possibles
				if (isEnnemy(white, echiquier, i, j))
					toReturn[1] = IntToString(tab[0], tab[1]) + IntToString(i, j);
			} else {
				// La case est vide, donc la piece peut s'y deplacer
				toReturn[1] = IntToString(tab[0], tab[1]) + IntToString(i, j);
			}
		}
		// if (toReturn[1].equals("")) {
		// char c = white ? 'R' : 'r';
		// ArrayList<int[]> DepartureBox = calculDepartureBox(echiquier, c);
		// if(DepartureBox.isEmpty()) {
		// System.err.println("Il n'y a plus de roi");
		// }else {
		// String moveEnnemy = "";
		// moveEnnemy += Rook(echiquier, !white);
		// moveEnnemy += Queen(echiquier, !white);
		// moveEnnemy += Pawn(echiquier, !white);
		// moveEnnemy += Knight(echiquier, !white);
		// moveEnnemy += King(echiquier, !white);
		//
		// }
		// }
		return toReturn;

		// Si la case est celle d'arrivee, qu'elle est prise mais que la couleur est
		// celle de l'adversaire alors on peut deplacer
		// if (echiquier[i][j].isOccupe() != 'v' &&
		// Character.isUpperCase(echiquier[i][j].isOccupe())) {
		// move = IntToString(i, j);
		// toReturn = false;
		// } // Si la case est prise on ne peut pasdeplacer
		// else if (echiquier[i][j].isOccupe() != 'v')
		// toReturn = false;
		// else
		// move = IntToString(i, j);
	}

	/**
	 * @param i
	 *            numero de la ligne
	 * @param j
	 *            numero de la colonne
	 * @return Un String contenant i change en lettre et j incremante de 1. Exemple
	 *         : "00" => "a8" ou "77" => "h1"
	 */
	public static String IntToString(int i, int j) {
		// if (!((0 <= i && i <= 7) && (0 <= j && j <= 7))) {
		// System.err.println("Depassement : i = " + i + ", j = " + j + "!!");
		// }
		String aRetourner = "";
		aRetourner = Character.toString((char) ('a' + j));
		aRetourner += (8 - i);
		// System.out.print(i+""+j+" => "+aRetourner+" ");
		return aRetourner;
	}

	/**
	 * Fonction parcourant tout l'echiquier
	 * 
	 * @param echiquier
	 * @param c
	 *            Caractere rersentant la piece (r, Q, t ou p etc). Minuscule pour
	 *            les noirs, majuscule pour les blancs
	 * @return Une liste contenant toutes les positions i, j des pieces de couleur
	 *         determinee par c
	 */
	public static ArrayList<int[]> calculDepartureBox(Case[][] echiquier, char c) {
		ArrayList<int[]> DepartureBox = new ArrayList<>();
		for (int i = 0; i < echiquier.length; i++) {
			for (int j = 0; j < echiquier.length; j++) {
				if (echiquier[i][j].isOccupe() == c) {
					// System.out.println("ok" + IntToString(i, j));
					int[] tab = new int[2];
					tab[0] = i;
					tab[1] = j;
					DepartureBox.add(tab);
				}
			}
		}
		return DepartureBox;
	}

	public static String calculW(Case[][] echiquier) {
		// System.out.println("Calcul White Debut");
		// afficherEchiquier(echiquier);

		String moves = Bishop(echiquier, true);
		moves += Rook(echiquier, true);
		moves += Queen(echiquier, true);
		moves += Pawn(echiquier, true);
		moves += Knight(echiquier, true);
		moves += King(echiquier, true);
		// System.out.print("Recapitulatif des mouvements");
		// afficherMove(moves);
		// System.out.println("Calcul White Fin");

		return moves;
	}

	public static String calculB(Case[][] echiquier) {
		// System.out.println("Calcul Black Debut");
		// afficherEchiquier(echiquier);
		String moves = Bishop(echiquier, false);
		moves += Rook(echiquier, false);
		moves += Queen(echiquier, false);
		moves += Pawn(echiquier, false);
		moves += Knight(echiquier, false);
		moves += King(echiquier, false);
		// System.out.print("Recapitulatif des mouvements");
		// afficherMove(moves);
		// System.out.println("Calcul Black Fin");

		return moves;
	}

	/**
	 * 
	 * @param white
	 * @param echiquier
	 * @param i
	 * @param j
	 * @return Bouleen qui indique si la case i j est occupee par un ennemi
	 */
	private static Boolean isEnnemy(boolean white, Case[][] echiquier, int i, int j) {
		// if (echiquier[i][j].isOccupe() == 'v')
		// System.out.println("Erreur a is ennemy");
		return white ? Character.isLowerCase(echiquier[i][j].isOccupe())
				: Character.isUpperCase(echiquier[i][j].isOccupe());
	}

	public static void afficherEchiquier(Case[][] echiquier) {
		for (int i = 0; i < echiquier.length; i++) {
			System.out.print("|");
			for (int j = 0; j < echiquier.length; j++) {
				// System.out.print("[i = " + i + ", j = " + j + ",
				// "+echiquier[i][j].getOccupe()+"]");
				System.out.print("(" + IntToString(i, j) + ") "
						+ ((echiquier[i][j].getOccupe() == 'v') ? " " : echiquier[i][j].getOccupe()) + " | ");
			}
			System.out.println("\n------------------------------------------------------------------------");
			// System.out.println("]");
		}
	}

	public static void afficherMove(String moves) {
		System.out.println(" : Mouvements = ");
		for (int i = 0; i <= moves.length() - 4; i += 4) {
			System.out.print(moves.substring(i, i + 4) + ", ");
		}
		System.out.println("\n");

	}
}
