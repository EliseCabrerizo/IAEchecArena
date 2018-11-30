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
	static long RANK_Masks[] = {

			0xFF00000000000000L, 0xFF000000000000L, 0xFF0000000000L,

			0xFF00000000L, 0xFF000000L, 0xFF0000L, 0xFF00L, 0xFFL

	};
	static long RANK_Masks_flip[] = {

			0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L,

			0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L

	};

	static long FILE_Masks[] = {

			0x0101010101010101L, 0x0202020202020202L, 0x0404040404040404L,

			0x0808080808080808L, 0x1010101010101010L, 0x2020202020202020L,

			0x4040404040404040L, 0x8080808080808080L

	};

	static long leftDiagonal_Mask[] = {

			0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,

			0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,

			0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L

	};

	static long rightDiagonal_Mask[] = {

			0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L,

			0x804020100804L, 0x80402010080402L, 0x8040201008040201L,

			0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,

			0x804020100000000L, 0x402010000000000L, 0x201000000000000L,

			0x100000000000000L

	};

	// fait les masques pour determiner les cases accessible pour un mvt de tour

	static long horNvertMoves(int pos) {

		long s = 1L << pos;

		long right_hor = Long.reverse(Long.reverse(OCCUPIED) - (Long.reverse(s) << 1)); // liste des position vide sur
																						// la ligne a droite

		long left_hor = (OCCUPIED - (s << 1)); // idem a gauche

		long hori_att = left_hor ^ right_hor; // position occupe sur la ligne

		long vert_OCC = OCCUPIED & FILE_Masks[pos % 8]; // cases occupe sur la verticale

		long left_ver = (vert_OCC - (s << 1)); //

		long right_ver = Long.reverse(Long.reverse(vert_OCC) - (Long.reverse(s) << 1));

		long vert_att = left_ver ^ right_ver;

		return ((hori_att & RANK_Masks_flip[pos / 8]) | vert_att & FILE_Masks[pos % 8]);

	}

	// faits les masques pour determiner les cases accessibles pour un mvt de fou

	static long diagonalMoves(int pos) {

		long s = 1L << pos;

		long leftdia_OCC = OCCUPIED & leftDiagonal_Mask[pos / 8 + pos % 8];

		long leftdia_up = (leftdia_OCC - (s << 1));

		long leftdia_down = Long.reverse(Long.reverse(leftdia_OCC) - (Long.reverse(s) << 1));

		long left_dia = leftdia_up ^ leftdia_down;

		long rightdia_OCC = OCCUPIED & rightDiagonal_Mask[pos / 8 + 7 - pos % 8];

		long rightdia_down = (rightdia_OCC - (s << 1));

		long rightdia_up = Long.reverse(Long.reverse(rightdia_OCC) - (Long.reverse(s) << 1));

		long rightdia = rightdia_up ^ rightdia_down;

		return ((left_dia & leftDiagonal_Mask[pos / 8 + pos % 8]) |

				(rightdia & rightDiagonal_Mask[pos / 8 + 7 - pos % 8]));

	}

	// caclul les mouvements possibles pour les blancs

	static String MovesAvailableW(long WK, long WQ, long WR, long WB,

			long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP) {

		String moves = "";

		VACANT = ~(WK | WQ | WR | WB | WN | WP | BK | BQ | BR | BB | BN | BP);

		OCCUPIED = ~VACANT;

		BLACK_PIECE = (BQ | BR | BB | BN | BP);

		NOT_ATTACK_OWN = ~(WK | WQ | WR | WB | WN | WP);

		Attack_bits = attackbitmapB(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP);

		moves = KingMoves(WK, Attack_bits) + KnightMoves(WN) + QueenMoves(WQ) + RookMoves(WR) +

				BishopMoves(WB) + PawnMovesW(WP);

		return moves;

	}

	// calcul les mouvement possibles pour les noirs

	static String MovesAvailableB(long WK, long WQ, long WR, long WB,

			long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP) {

		String moves = "";

		VACANT = ~(WK | WQ | WR | WB | WN | WP | BK | BQ | BR | BB | BN | BP);

		OCCUPIED = ~VACANT;

		WHITE_PIECE = (WQ | WR | WB | WN | WP);

		NOT_ATTACK_OWN = ~(BK | BQ | BR | BB | BN | BP);

		Attack_bits = attackbitmapW(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP);

		moves = PawnMovesB(BP) + KingMoves(BK, Attack_bits) + KnightMoves(BN) + QueenMoves(BQ) +

				RookMoves(BR) + BishopMoves(BB);

		return moves;

	}

	// calcul les casse attaquable par les blanc

	public static long attackbitmapW(long WK, long WQ, long WR, long WB,

			long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP) {

		long bitmap = 0;

		VACANT = ~(WK | WQ | WR | WB | WN | WP | BK | BQ | BR | BB | BN | BP);

		OCCUPIED = ~VACANT;

		bitmap = pawnattmapW(WP) | kingatt_map(WK) | kinghtatt_map(WN) |

				Queenatt_map(WQ) | Rookatt_map(WR) | Bishopatt_map(WB);

		return bitmap;

	}

	// calcul les casse attaquable par les noirs

	public static long attackbitmapB(long WK, long WQ, long WR, long WB,

			long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP) {

		long bitmap = 0;

		VACANT = ~(WK | WQ | WR | WB | WN | WP | BK | BQ | BR | BB | BN | BP); // calcul les cases vide

		OCCUPIED = ~VACANT; // calcul les cases occupe

		// calcul l'ensemble des cases attaquables par une équipe

		bitmap = pawnattmapB(BP) | kingatt_map(BK) | kinghtatt_map(BN) |

				Queenatt_map(BQ) | Rookatt_map(BR) | Bishopatt_map(BB);

		return bitmap;

	}

	// calcul le mvt d'une piece

	public static long makemove(long pieceboard, String move, char piecetype) {

		if (Character.isDigit(move.charAt(3)))

		{

			int begin = Character.getNumericValue(move.charAt(0)) * 8 + Character.getNumericValue(move.charAt(1));

			int end = Character.getNumericValue(move.charAt(2)) * 8 + Character.getNumericValue(move.charAt(3));

			if (((pieceboard >>> begin) & 1) == 1)

			{

				pieceboard &= ~(1L << begin);

				pieceboard |= (1L << end); // move the piece from begin to end

			} else

			{

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

	// calcul le mvt des pions blanc

	static String PawnMovesW(long WP) {

		String moves = "";

		long movebits;

		movebits = (WP >>> 7) & ~RANK_Masks[7] & ~FILE_A & BLACK_PIECE;// capture left black piece

		long possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index / 8 + 1) + (index % 8 - 1) + (index / 8) + (index % 8);

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (WP >> 9) & ~FILE_H & ~RANK_Masks[7] & BLACK_PIECE;// capture right black piece

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index / 8 + 1) + (index % 8 + 1) + (index / 8) + (index % 8);

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (WP >> 8) & VACANT & ~RANK_Masks[7]; // move one forward if empty

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index / 8 + 1) + (index % 8) + (index / 8) + (index % 8);

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (WP >> 16) & VACANT & (VACANT >> 8) & RANK_4; // move 2 forward

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index / 8 + 2) + (index % 8) + (index / 8) + (index % 8);

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (WP >> 7) & RANK_Masks[7] & ~FILE_A & BLACK_PIECE;

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index % 8 - 1) + (index % 8) + "QP" + (index % 8 - 1) + (index % 8) + "RP"

					+ (index % 8 - 1) + (index % 8) + "BP" + (index % 8 - 1) + (index % 8) + "NP";

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (WP >> 9) & RANK_Masks[7] & ~FILE_H & BLACK_PIECE;

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index % 8 + 1) + (index % 8) + "QP" + (index % 8 + 1) + (index % 8) + "RP"

					+ (index % 8 + 1) + (index % 8) + "BP" + (index % 8 + 1) + (index % 8) + "NP";

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (WP >> 8) & VACANT & RANK_Masks[7];

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index % 8) + (index % 8) + "QP" + (index % 8) + (index % 8) + "RP"

					+ (index % 8) + (index % 8) + "BP" + (index % 8) + (index % 8) + "NP";

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		return moves;

	}

	// calcul les mouvement des pions noir

	static String PawnMovesB(long BP) {

		String moves = "";

		long movebits;

		movebits = (BP << 7) & ~RANK_Masks[0] & ~FILE_H & WHITE_PIECE;// capture left black piece

		long possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index / 8 - 1) + (index % 8 + 1) + (index / 8) + (index % 8);

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (BP << 9) & ~FILE_A & ~RANK_Masks[0] & WHITE_PIECE;// capture right black piece

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index / 8 - 1) + (index % 8 - 1) + (index / 8) + (index % 8);

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (BP << 8) & VACANT & ~RANK_Masks[0]; // move one forward if empty

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index / 8 - 1) + (index % 8) + (index / 8) + (index % 8);

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (BP << 16) & VACANT & (VACANT << 8) & RANK_Masks[4]; // move 2 forward

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index / 8 - 2) + (index % 8) + (index / 8) + (index % 8);

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (BP << 7) & RANK_Masks[0] & ~FILE_H & WHITE_PIECE;

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index % 8 + 1) + (index % 8) + "qP" + (index % 8 + 1) + (index % 8) + "rP"

					+ (index % 8 + 1) + (index % 8) + "bP" + (index % 8 + 1) + (index % 8) + "nP";

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (BP << 9) & RANK_Masks[0] & ~FILE_A & WHITE_PIECE;

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index % 8 - 1) + (index % 8) + "qP" + (index % 8 - 1) + (index % 8) + "rP"

					+ (index % 8 - 1) + (index % 8) + "bP" + (index % 8 - 1) + (index % 8) + "nP";

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		movebits = (BP << 8) & VACANT & RANK_Masks[0];

		possible = movebits & ~(movebits - 1);

		while (possible != 0)

		{

			int index = Long.numberOfTrailingZeros(possible);

			moves += "" + (index % 8) + (index % 8) + "qP" + (index % 8) + (index % 8) + "rP"

					+ (index % 8) + (index % 8) + "bP" + (index % 8) + (index % 8) + "nP";

			movebits &= ~possible;

			possible = movebits & ~(movebits - 1);

		}

		return moves;

	}

	// calcul les mvt des fous

	static String BishopMoves(long B) {

		String moves = "";

		long bish_pos = B & ~(B - 1);

		long movebits;

		while (bish_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(bish_pos);

			movebits = diagonalMoves(position) & NOT_ATTACK_OWN;

			long possible = movebits & ~(movebits - 1);

			while (possible != 0)

			{

				int index = Long.numberOfTrailingZeros(possible);

				moves += "" + (position / 8) + (position % 8) + (index / 8) + (index % 8);

				movebits &= ~possible;

				possible = movebits & ~(movebits - 1);

			}

			B &= ~bish_pos;

			bish_pos = B & ~(B - 1);

		}

		return moves;

	}

	// calcul le mvt pour la tour

	static String RookMoves(long R) {

		String moves = "";

		long rook_pos = R & ~(R - 1);

		long movebits;

		while (rook_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(rook_pos);

			movebits = horNvertMoves(position) & NOT_ATTACK_OWN;

			long possible = movebits & ~(movebits - 1);

			while (possible != 0)

			{

				int index = Long.numberOfTrailingZeros(possible);

				moves += "" + (position / 8) + (position % 8) + (index / 8) + (index % 8);

				movebits &= ~possible;

				possible = movebits & ~(movebits - 1);

			}

			R &= ~rook_pos;

			rook_pos = R & ~(R - 1);

		}

		return moves;

	}

	// calcul le mvt pour la reine

	static String QueenMoves(long Q) {

		String moves = "";

		long queen_pos = Q & ~(Q - 1);

		long movebits;

		while (queen_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(queen_pos);

			movebits = (diagonalMoves(position) | horNvertMoves(position)) & NOT_ATTACK_OWN;

			long possible = movebits & ~(movebits - 1);

			while (possible != 0)

			{

				int index = Long.numberOfTrailingZeros(possible);

				moves += "" + (position / 8) + (position % 8) + (index / 8) + (index % 8);

				movebits &= ~possible;

				possible = movebits & ~(movebits - 1);

			}

			Q &= ~queen_pos;

			queen_pos = Q & ~(Q - 1);

		}

		return moves;

	}

	// calcul le mvt pour les cavaliers

	static String KnightMoves(long K) {

		String moves = "";

		long knight_pos = K & ~(K - 1);

		long movebits;

		while (knight_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(knight_pos); // on determine la position de la piece

			// pour cela on determine le nombre de zero qui se suivent dans l'entier. Cela
			// nous donne le numero de la case à 1

			// ce numero de case, apres remis dans un tableau correspond a la case ou se
			// situe le cavalier

			movebits = 0;

			// on fait les masques pour supprimer toutes les cases innacessibles

			movebits |= (knight_pos >>> 17) & ~FILE_Masks[7];

			movebits |= (knight_pos >>> 15) & ~FILE_Masks[0];

			movebits |= (knight_pos >>> 10) & ~(FILE_Masks[6] | FILE_Masks[7]);

			movebits |= (knight_pos >>> 6) & ~(FILE_Masks[0] | FILE_Masks[1]);

			movebits |= (knight_pos << 6) & ~(FILE_Masks[6] | FILE_Masks[7]);

			movebits |= (knight_pos << 10) & ~(FILE_Masks[0] | FILE_Masks[1]);

			movebits |= (knight_pos << 15) & ~(FILE_Masks[7]);

			movebits |= (knight_pos << 17) & ~(FILE_Masks[0]);

			movebits &= NOT_ATTACK_OWN; // masque pour be pas attaquer ses pions

			long possible = movebits & ~(movebits - 1);

			while (possible != 0)

			{

				int index = Long.numberOfTrailingZeros(possible); // determine l'index de la case

				moves += "" + (position / 8) + (position % 8) + (index / 8) + (index % 8);

				movebits &= ~possible;

				possible = movebits & ~(movebits - 1);

			}

			K &= ~knight_pos;

			knight_pos = K & ~(K - 1);

		}

		return moves;

	}

	// calcul les mvt des rois

	static String KingMoves(long K, long attack_bits) {

		String moves = "";

		long king_pos = K & ~(K - 1);

		long movebits;

		while (king_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(king_pos);

			movebits = 0;

			movebits |= (king_pos >>> 9) & ~(FILE_Masks[7]);

			movebits |= (king_pos >>> 8);

			movebits |= (king_pos >>> 7) & ~(FILE_Masks[0]);

			movebits |= (king_pos >>> 1) & ~(FILE_Masks[7]);

			movebits |= (king_pos << 1) & ~(FILE_Masks[0]);

			movebits |= (king_pos << 7) & ~(FILE_Masks[7]);

			movebits |= (king_pos << 8);

			movebits |= (king_pos << 9) & ~(FILE_Masks[0]);

			movebits &= NOT_ATTACK_OWN;

			movebits &= ~(attack_bits);

			long possible = movebits & ~(movebits - 1);

			while (possible != 0)

			{

				int index = Long.numberOfTrailingZeros(possible);

				moves += "" + (position / 8) + (position % 8) + (index / 8) + (index % 8);

				movebits &= ~possible;

				possible = movebits & ~(movebits - 1);

			}

			K &= ~king_pos;

			king_pos = K & ~(K - 1);

		}

		return moves;

	}

	public static void drawBitboard(long bitBoard) {

		String chessBoard[][] = new String[8][8];

		for (int i = 0; i < 64; i++) {

			chessBoard[i / 8][i % 8] = "";

		}

		for (int i = 0; i < 64; i++) {

			if (((bitBoard >>> i) & 1) == 1) {
				chessBoard[i / 8][i % 8] = "P";
			}

			if ("".equals(chessBoard[i / 8][i % 8])) {
				chessBoard[i / 8][i % 8] = " ";
			}

		}

		for (int i = 0; i < 8; i++) {

			System.out.println(Arrays.toString(chessBoard[i]));

		}

	}

	// masques position atteignable par un pion blanc

	public static long pawnattmapW(long WP) {

		long bitmap = 0;

		bitmap |= (WP >>> 7) & ~FILE_A;

		bitmap |= (WP >> 9) & ~FILE_H;

		return bitmap;

	}

	// masques position atteignable par un pion noir

	public static long pawnattmapB(long BP) {

		long bitmap = 0;

		bitmap |= (BP << 7) & ~FILE_H;

		bitmap |= (BP << 9) & ~FILE_A;

		return bitmap;

	}

	// masques position atteignable par un roi

	public static long kingatt_map(long K) {

		long bitmap = 0;

		long king_pos = K & ~(K - 1);

		bitmap |= (king_pos >>> 9) & ~(FILE_Masks[7]);

		bitmap |= (king_pos >>> 8);

		bitmap |= (king_pos >>> 7) & ~(FILE_Masks[0]);

		bitmap |= (king_pos >>> 1) & ~(FILE_Masks[7]);

		bitmap |= (king_pos << 1) & ~(FILE_Masks[0]);

		bitmap |= (king_pos << 7) & ~(FILE_Masks[7]);

		bitmap |= (king_pos << 8);

		bitmap |= (king_pos << 9) & ~(FILE_Masks[0]);

		return bitmap;

	}

	// masques position atteignable par un cavalier

	public static long kinghtatt_map(long K) {

		long knight_pos = K & ~(K - 1);

		long movebits = 0;

		while (knight_pos != 0)

		{

			movebits |= (knight_pos >>> 17) & ~FILE_Masks[7];

			movebits |= (knight_pos >>> 15) & ~FILE_Masks[0];

			movebits |= (knight_pos >>> 10) & ~(FILE_Masks[6] | FILE_Masks[7]);

			movebits |= (knight_pos >>> 6) & ~(FILE_Masks[0] | FILE_Masks[1]);

			movebits |= (knight_pos << 6) & ~(FILE_Masks[6] | FILE_Masks[7]);

			movebits |= (knight_pos << 10) & ~(FILE_Masks[0] | FILE_Masks[1]);

			movebits |= (knight_pos << 15) & ~(FILE_Masks[7]);

			movebits |= (knight_pos << 17) & ~(FILE_Masks[0]);

			K &= ~knight_pos;

			knight_pos = K & ~(K - 1);

		}

		return movebits;

	}

	// masques position atteignable par une reine

	public static long Queenatt_map(long Q) {

		long queen_pos = Q & ~(Q - 1);

		long movebits = 0;

		while (queen_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(queen_pos);

			movebits |= (diagonalMoves(position) | horNvertMoves(position));

			Q &= ~queen_pos;

			queen_pos = Q & ~(Q - 1);

		}

		return movebits;

	}

	// masques position atteignable par une tour

	public static long Rookatt_map(long R) {

		long rook_pos = R & ~(R - 1);

		long movebits = 0;

		while (rook_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(rook_pos);

			movebits |= horNvertMoves(position);

			R &= ~rook_pos;

			rook_pos = R & ~(R - 1);

		}

		return movebits;

	}

	// masques position atteignable par un fou

	public static long Bishopatt_map(long B) {

		long bish_pos = B & ~(B - 1);

		long movebits = 0;

		while (bish_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(bish_pos);

			movebits |= diagonalMoves(position) & NOT_ATTACK_OWN;

			B &= ~bish_pos;

			bish_pos = B & ~(B - 1);

		}

		return movebits;

	}

	public static String IntToString(int i, int j) {
		String aRetourner = "";
		aRetourner = Character.toString((char) (4 + 'a'));
		aRetourner += (j + 1);
		return aRetourner;
	}

	public static String Bishop(Case[][] echiquier, boolean W) {
		String move = "";
		char c = ' ';
		if (W) {
			c = 'f';
		} else {
			c = 'F';
		}
		int[] DepartureBox = new int[2];
		boolean trouver=false;
		for (int i = 0; i < echiquier.length&&!trouver; i++)
			for (int j = 0; j < echiquier.length&&!trouver; j++) {
				if (echiquier[i][j].isOccupe() == c) {
					DepartureBox[0] = i;
					DepartureBox[1] = j;
					trouver=true;
				}

			}

		boolean toReturn = true;

		if (W) {
			int i = DepartureBox[0]+1;
			int j = DepartureBox[1]+1;
			// Déplacement diagonale haut droite
			while (i < echiquier.length && j < echiquier.length && toReturn) {
				// Si la case est occupée par un noir, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un blanc, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i++;
				j++;
			}
			// Déplacement diagonale haut gauche
			toReturn = true;
			i=DepartureBox[0]+1;
			j=DepartureBox[1]-1;
			while (i < echiquier.length && j >= 0 && toReturn) {
				// Si la case est occupée par un noir, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un blanc, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i++;
				j--;
			}

			// Déplacement diagonale bas gauche
			i=DepartureBox[0]-1;
			j=DepartureBox[1]-1;
			toReturn = true;
			while (i >= 0 && j >= 0 && toReturn) {
				// Si la case est occupée par un noir, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un blanc, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i--;
				j--;
			}
			// Déplacement diagonale bas droite
			toReturn = true;
			i=DepartureBox[0]-1;
			j=DepartureBox[1]+1;
			while (i >= 0 && j < echiquier.length && toReturn) {
				// Si la case est occupée par un noir, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un blanc, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i--;
				j++;
			}
		} else {
			int i = DepartureBox[0]+1;
			int j = DepartureBox[1]+1;
			// Déplacement diagonale haut droite
			while (i < echiquier.length && j < echiquier.length && toReturn) {
				// Si la case est occupée par un blanc, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un noir, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i++;
				j++;
			}
			// Déplacement diagonale haut gauche
			toReturn = true;
			i=DepartureBox[0]+1;
			j=DepartureBox[1]-1;
			while (i < echiquier.length && j >= 0 && toReturn) {
				// Si la case est occupée par un blanc, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un noir, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i++;
				j--;
			}

			// Déplacement diagonale bas gauche
			toReturn = true;
			i=DepartureBox[0]-1;
			j=DepartureBox[1]-1;
			while (i >= 0 && j >= 0 && toReturn) {
				// Si la case est occupée par un blanc, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un noir, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i--;
				j--;
			}
			// Déplacement diagonale bas droite
			toReturn = true;
			i=DepartureBox[0]-1;
			j=DepartureBox[1]+1;
			while (i >= 0 && j < echiquier.length && toReturn) {
				// Si la case est occupée par un blanc, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un noir, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i--;
				j++;
			}
		}
		return move;
	}

	public static String Rook(Case[][] echiquier, boolean W) {
		String move = "";
		char c = ' ';
		if (W) {
			c = 't';
		} else {
			c = 'T';
		}
		int[] DepartureBox = new int[2];
		boolean trouver=false;
		for (int i = 0; i < echiquier.length&!trouver; i++)
			for (int j = 0; j < echiquier.length&&!trouver; j++) {
				if (echiquier[i][j].isOccupe() == c) {
					DepartureBox[0] = i;
					DepartureBox[1] = j;
					trouver=true;
				}

			}
		int i = DepartureBox[0];
		int j = DepartureBox[1];
		boolean toReturn = true;

		if (W) {
			// Déplacement vers le haut
			for (int k =i+1; k < echiquier.length && toReturn; k++) {
				// Si la case est celle d'arrivée, qu'elle est prise mais que la couleur est
				// celle de l'adversaire alors on peut déplacer
				if (echiquier[k][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[k][j].isOccupe())) {
					move = IntToString(k, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[k][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(k, j);
			}

			// Déplacement vers la bas
			toReturn = true;
			for (int k = i-1; k >= 0 && toReturn; k--) {
				// Si la case est celle d'arrivée, qu'elle est prise mais que la couleur est
				// celle de l'adversaire alors on peut déplacer
				if (echiquier[k][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[k][j].isOccupe())) {
					move = IntToString(k, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[k][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(k, j);
			}
			// Déplacement vers la droite
			toReturn = true;
			for (int l = j+1; l < echiquier.length && toReturn; l++) {
				if (echiquier[i][l].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][l].isOccupe())) {
					move = IntToString(i, l);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i][l].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i, l);
			}
			// Déplacement vers la gauche
			toReturn = true;
			for (int l =j-1; l >= 0 && toReturn; l--) {
				if (echiquier[i][l].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][l].isOccupe())) {
					move = IntToString(i, l);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i][l].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i, l);
			}
		} else {
			// Déplacement vers le haut
			toReturn = true;
			for (int k = i+1; k < echiquier.length && toReturn; k++) {
				// Si la case est celle d'arrivée, qu'elle est prise mais que la couleur est
				// celle de l'adversaire alors on peut déplacer
				if (echiquier[k][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[k][j].isOccupe())) {
					move = IntToString(k, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[k][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(k, j);
			}

			// Déplacement vers la bas
			toReturn = true;
			for (int k = i-1; k >= 0 && toReturn; k--) {
				// Si la case est celle d'arrivée, qu'elle est prise mais que la couleur est
				// celle de l'adversaire alors on peut déplacer
				if (echiquier[k][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[k][j].isOccupe())) {
					move = IntToString(k, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[k][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(k, j);
			}
			// Déplacement vers la droite
			toReturn = true;
			for (int l =j+1; l < echiquier.length && toReturn; l++) {
				if (echiquier[i][l].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][l].isOccupe())) {
					move = IntToString(i, l);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i][l].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i, l);
			}
			// Déplacement vers la gauche
			toReturn = true;
			for (int l = j-1; l >= 0 && toReturn; l--) {
				if (echiquier[i][l].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][l].isOccupe())) {
					move = IntToString(i, l);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i][l].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i, l);
			}
		}
		return move;
	}

	public static String Queen(Case[][] echiquier, boolean W) {
		String move = "";
		char c = ' ';
		if (W) {
			c = 'd';
		} else {
			c = 'D';
		}
		int[] DepartureBox = new int[2];
		boolean trouver=false;
		for (int i = 0; i < echiquier.length&&!trouver; i++)
			for (int j = 0; j < echiquier.length&&!trouver; j++) {
				if (echiquier[i][j].isOccupe() == c) {
					DepartureBox[0] = i;
					DepartureBox[1] = j;
					trouver=true;
				}

			}
		
		boolean toReturn = true;

		if (W) {
			int i = DepartureBox[0]+1;
			int j = DepartureBox[1]+1;
			// Déplacement diagonale haut droite
			while (i < echiquier.length && j < echiquier.length && toReturn) {
				// Si la case est occupée par un noir, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un blanc, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i++;
				j++;
			}
			toReturn = true;
			i = DepartureBox[0]+1;
			j = DepartureBox[1]-1;
			// Déplacement diagonale haut gauche
			while (i < echiquier.length && j >= 0 && toReturn) {
				// Si la case est occupée par un noir, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un blanc, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i++;
				j--;
			}
			toReturn = true;
			 i = DepartureBox[0]-1;
			 j = DepartureBox[1]-1;
			// Déplacement diagonale bas gauche
			while (i >= 0 && j >= 0 && toReturn) {
				// Si la case est occupée par un noir, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un blanc, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i--;
				j--;
			}
			// Déplacement diagonale bas droite
			i = DepartureBox[0]-1;
			j = DepartureBox[1]+1;
			toReturn = true;
			while (i >= 0 && j < echiquier.length && toReturn) {
				// Si la case est occupée par un noir, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un blanc, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i--;
				j++;
			}
		} else {
			// Déplacement diagonale haut droite
			toReturn = true;
			int i = DepartureBox[0]+1;
			int j = DepartureBox[1]+1;
			while (i < echiquier.length && j < echiquier.length && toReturn) {
				// Si la case est occupée par un blanc, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un noir, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i++;
				j++;
			}
			// Déplacement diagonale haut gauche
			toReturn = true;
			i = DepartureBox[0]+1;
			j = DepartureBox[1]+1;
			while (i < echiquier.length && j >= 0 && toReturn) {
				// Si la case est occupée par un blanc, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un noir, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i++;
				j--;
			}

			// Déplacement diagonale bas gauche
			toReturn = true;
			i = DepartureBox[0]-1;
			j = DepartureBox[1]-1;
			while (i >= 0 && j >= 0 && toReturn) {
				// Si la case est occupée par un blanc, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un noir, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i--;
				j--;
			}
			// Déplacement diagonale bas droite
			toReturn = true;
			i = DepartureBox[0]-1;
			j = DepartureBox[1]+1;
			while (i >= 0 && j < echiquier.length && toReturn) {
				// Si la case est occupée par un blanc, on ajoute le mouvement mais on s'arrête
				// ensuite
				if (echiquier[i][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][j].isOccupe())) {
					move.concat(IntToString(i, j));
					toReturn = false;
				}
				// Si la case est occupée par un noir, on s'arrête
				else if (echiquier[i][j].isOccupe() != 'v') {
					toReturn = false;
				}
				// Si la case est vide, on ajoute le mouvement
				else {
					move.concat(IntToString(i, j));
				}
				i--;
				j++;
			}
		}
		toReturn = true;
		if (W) {
			int i = DepartureBox[0];
			int j = DepartureBox[1];
			// Déplacement vers le haut
			for (int k = DepartureBox[0]+1; k < echiquier.length && toReturn; k++) {
				// Si la case est celle d'arrivée, qu'elle est prise mais que la couleur est
				// celle de l'adversaire alors on peut déplacer
				if (echiquier[k][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[k][j].isOccupe())) {
					move = IntToString(k, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[k][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(k, j);
			}

			// Déplacement vers la bas
			toReturn = true;
			for (int k = DepartureBox[0]-1; k >= 0 && toReturn; k--) {
				// Si la case est celle d'arrivée, qu'elle est prise mais que la couleur est
				// celle de l'adversaire alors on peut déplacer
				if (echiquier[k][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[k][j].isOccupe())) {
					move = IntToString(k, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[k][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(k, j);
			}
			// Déplacement vers la droite
			toReturn = true;
			for (int l = DepartureBox[1]+1; l < echiquier.length && toReturn; l++) {
				if (echiquier[i][l].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][l].isOccupe())) {
					move = IntToString(i, l);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i][l].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i, l);
			}
			// Déplacement vers la gauche
			toReturn = true;
			for (int l = DepartureBox[1]-1; l >= 0 && toReturn; l--) {
				if (echiquier[i][l].isOccupe() != 'v' && Character.isUpperCase(echiquier[i][l].isOccupe())) {
					move = IntToString(i, l);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i][l].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i, l);
			}
		} else {
			int i = DepartureBox[0];
			int j = DepartureBox[1];
			// Déplacement vers le haut
			toReturn = true;
			for (int k = DepartureBox[0]+1; k < echiquier.length && toReturn; k++) {
				// Si la case est celle d'arrivée, qu'elle est prise mais que la couleur est
				// celle de l'adversaire alors on peut déplacer
				if (echiquier[k][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[k][j].isOccupe())) {
					move = IntToString(k, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[k][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(k, j);
			}

			// Déplacement vers la bas
			toReturn = true;
			for (int k = DepartureBox[0]-1; k >= 0 && toReturn; k--) {
				// Si la case est celle d'arrivée, qu'elle est prise mais que la couleur est
				// celle de l'adversaire alors on peut déplacer
				if (echiquier[k][j].isOccupe() != 'v' && Character.isLowerCase(echiquier[k][j].isOccupe())) {
					move = IntToString(k, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[k][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(k, j);
			}
			// Déplacement vers la droite
			toReturn = true;
			for (int l = DepartureBox[1]+1; l < echiquier.length && toReturn; l++) {
				if (echiquier[i][l].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][l].isOccupe())) {
					move = IntToString(i, l);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i][l].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i, l);
			}
			// Déplacement vers la gauche
			toReturn = true;
			for (int l = DepartureBox[1]-1; l >= 0 && toReturn; l--) {
				if (echiquier[i][l].isOccupe() != 'v' && Character.isLowerCase(echiquier[i][l].isOccupe())) {
					move = IntToString(i, l);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i][l].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i, l);
			}
		}
		return move;
	}

	public static String Pawn(Case[][] echiquier, boolean W) {
		String move = "";
		char c = ' ';
		if (W) {
			c = 'p';
		} else {
			c = 'P';
		}
		int[] DepartureBox = new int[2];
		for (int i = 0; i < echiquier.length; i++)
			for (int j = 0; j < echiquier.length; j++) {
				if (echiquier[i][j].isOccupe() == c) {
					DepartureBox[0] = i;
					DepartureBox[1] = j;
				}

			}
		int i = DepartureBox[0];
		int j = DepartureBox[1];
		boolean toReturn = true;
		if (W) {
			// Règle 1er déplacement
			if (i == 1) {
				if (echiquier[i + 1][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i + 1][j].isOccupe())) {
					move = IntToString(i + 1, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i + 1][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i + 1, j);
				if (toReturn) {
					if (echiquier[i + 2][j].isOccupe() != 'v'
							&& Character.isUpperCase(echiquier[i + 2][j].isOccupe())) {
						move = IntToString(i + 2, j);
						toReturn = false;
					}
					// Si la case est prise on ne peut pas déplacer
					else if (echiquier[i + 2][j].isOccupe() != 'v')
						toReturn = false;
					else
						move = IntToString(i + 2, j);
				}
			}

			// Règle avancer de 1 case
			else {

				if (i + 1 < echiquier.length) {
					if (echiquier[i + 1][j].isOccupe() != 'v'
							&& Character.isUpperCase(echiquier[i + 1][j].isOccupe())) {
						move = IntToString(i + 1, j);
					}
					// Si la case est prise on ne peut pas déplacer
					else if (echiquier[i + 1][j].isOccupe() != 'v') {

					} else
						move = IntToString(i + 1, j);
				}

				if (i + 1 < echiquier.length && j + 1 < echiquier.length)
					if (echiquier[i + 1][j + 1].isOccupe() != 'v'
							&& Character.isUpperCase(echiquier[i + 1][j + 1].isOccupe())) {
						move = IntToString(i + 1, j + 1);
					}
				if (i + 1 < echiquier.length && j - 1 >= 0)
					if (echiquier[i + 1][j - 1].isOccupe() != 'v'
							&& Character.isUpperCase(echiquier[i + 1][j - 1].isOccupe())) {
						move = IntToString(i + 1, j - 1);
					}
			}

		}
		else 
		{
			//Règle 1er déplacement
			if(i==echiquier.length-2)
			{
				if (echiquier[i-1][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i-1][j].isOccupe())) {
					move = IntToString(i-1, j);
					toReturn = false;
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i-1][j].isOccupe() != 'v')
					toReturn = false;
				else
					move = IntToString(i-1, j);
				if(toReturn)
				{
					if (echiquier[i-2][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i-2][j].isOccupe())) {
						move = IntToString(i-2, j);
						toReturn = false;
					}
					// Si la case est prise on ne peut pas déplacer
					else if (echiquier[i-2][j].isOccupe() != 'v')
						toReturn = false;
					else
						move = IntToString(i-2, j);
				}
			}
			
			//Règle avancer de 1 case
			else
			{
				
				if(i-1>=0)
				{
					if (echiquier[i-1][j].isOccupe() != 'v' && Character.isUpperCase(echiquier[i-1][j].isOccupe())) {
					move = IntToString(i-1, j);
				}
				// Si la case est prise on ne peut pas déplacer
				else if (echiquier[i-1][j].isOccupe() != 'v')
				{
					
				}
				else
					move = IntToString(i-1, j);
				}
				
				if(i-1>=0&j-1>=0)
					if (echiquier[i-1][j-1].isOccupe() != 'v' && Character.isUpperCase(echiquier[i-1][j-1].isOccupe())) {
						move = IntToString(i-1, j-1);
					}
				if(i-1>=0&&j<echiquier.length)
					if (echiquier[i-1][j+1].isOccupe() != 'v' && Character.isUpperCase(echiquier[i-1][j+1].isOccupe())) {
						move = IntToString(i-1, j+1);
					}
			}
		}

		return move;
	}

	public static String calculW(Case[][] echiquier) {

		String moves = Bishop(echiquier, true);
		moves.concat(Rook(echiquier, true));
		moves.concat(Queen(echiquier,true));
		moves.concat(Pawn(echiquier,true));
		return moves;

	}

	public static String calculB(Case[][] echiquier)

	{
		String moves = Bishop(echiquier, false);
		moves.concat(Rook(echiquier, false));
		moves.concat(Queen(echiquier,false));
		moves.concat(Pawn(echiquier,false));
		return moves;

	}

}