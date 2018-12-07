public class Evaluation {

	private static final int White = 2;

	private static final int Black = 0;

	private static final int[][][] Pawn = {

			{

					{ 0, 0, 0, 0, 0, 0, 0, 0 },

					{ 5, 10, 10, -20, -20, 10, 10, 5 },

					{ 5, -5, -10, 0, 0, -10, -5, 5 },

					{ 0, 0, 0, 20, 20, 0, 0, 0 },

					{ 5, 5, 10, 25, 25, 10, 5, 5 },

					{ 10, 10, 20, 30, 30, 20, 10, 10 },

					{ 50, 50, 50, 50, 50, 50, 50, 50 },

					{ 0, 0, 0, 0, 0, 0, 0, 0 }

			}, {

					{ 0, 0, 0, 0, 0, 0, 0, 0 },

					{ 50, 50, 50, 50, 50, 50, 50, 50 },

					{ 10, 10, 20, 30, 30, 20, 10, 10 },

					{ 5, 5, 10, 25, 25, 10, 5, 5 },

					{ 0, 0, 0, 20, 20, 0, 0, 0 },

					{ 5, -5, -10, 0, 0, -10, -5, 5 },

					{ 5, 10, 10, -20, -20, 10, 10, 5 },

					{ 0, 0, 0, 0, 0, 0, 0, 0 }

			}

	};

	private static final int[][][] Knight = {

			{

					{ -50, -40, -30, -30, -30, -30, -40, -50 },

					{ -40, -20, 0, 5, 5, 0, -20, -40 },

					{ -30, 5, 10, 15, 15, 10, 5, -30 },

					{ -30, 0, 15, 20, 20, 15, 0, -30 },

					{ -30, 5, 15, 20, 20, 15, 5, -30 },

					{ -30, 0, 10, 15, 15, 10, 0, -30 },

					{ -40, -20, 0, 0, 0, 0, -20, -40 },

					{ -50, -40, -30, -30, -30, -30, -40, -50 }

			}, {

					{ -50, -40, -30, -30, -30, -30, -40, -50 },

					{ -40, -20, 0, 0, 0, 0, -20, -40 },

					{ -30, 0, 10, 15, 15, 10, 0, -30 },

					{ -30, 5, 15, 20, 20, 15, 5, -30 },

					{ -30, 0, 15, 20, 20, 15, 0, -30 },

					{ -30, 5, 10, 15, 15, 10, 5, -30 },

					{ -40, -20, 0, 5, 5, 0, -20, -40 },

					{ -50, -40, -30, -30, -30, -30, -40, -50 }

			}

	};

	private static final int[][][] Bishop = {

			{

					{ -20, -10, -10, -10, -10, -10, -10, -20 },

					{ -10, 5, 0, 0, 0, 0, 5, -10 },

					{ -10, 10, 10, 10, 10, 10, 10, -10 },

					{ -10, 0, 10, 10, 10, 10, 0, -10 },

					{ -10, 5, 5, 10, 10, 5, 5, -10 },

					{ -10, 0, 5, 10, 10, 5, 0, -10 },

					{ -10, 0, 0, 0, 0, 0, 0, -10 },

					{ -20, -10, -10, -10, -10, -10, -10, -20 }

			}, {

					{ -20, -10, -10, -10, -10, -10, -10, -20 },

					{ -10, 0, 0, 0, 0, 0, 0, -10 },

					{ -10, 0, 5, 10, 10, 5, 0, -10 },

					{ -10, 5, 5, 10, 10, 5, 5, -10 },

					{ -10, 0, 10, 10, 10, 10, 0, -10 },

					{ -10, 10, 10, 10, 10, 10, 10, -10 },

					{ -10, 5, 0, 0, 0, 0, 5, -10 },

					{ -20, -10, -10, -10, -10, -10, -10, -20 }

			}

	};

	private static final int[][][] Rook = {

			{

					{ 0, 0, 0, 5, 5, 0, 0, 0 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ 5, 10, 10, 10, 10, 10, 10, 5 },

					{ 0, 0, 0, 5, 5, 0, 0, 0 }

			}, {

					{ 0, 0, 0, 0, 0, 0, 0, 0 },

					{ 5, 10, 10, 10, 10, 10, 10, 5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ -5, 0, 0, 0, 0, 0, 0, -5 },

					{ 0, 0, 0, 5, 5, 0, 0, 0 }

			}

	};

	private static final int[][][] Queen = {

			{

					{ -20, -10, -10, -5, -5, -10, -10, -20 },

					{ -10, 0, 5, 0, 0, 0, 0, -10 },

					{ -10, 5, 5, 5, 5, 5, 0, -10 },

					{ 0, 0, 5, 5, 5, 5, 0, -5 },

					{ -5, 0, 5, 5, 5, 5, 0, -5 },

					{ -10, 0, 5, 5, 5, 5, 0, -10 },

					{ -10, 0, 0, 0, 0, 0, 0, -10 },

					{ -20, -10, -10, -5, -5, -10, -10, -20 }

			}, {

					{ -20, -10, -10, -5, -5, -10, -10, -20 },

					{ -10, 0, 0, 0, 0, 0, 0, -10 },

					{ -10, 0, 5, 5, 5, 5, 0, -10 },

					{ -5, 0, 5, 5, 5, 5, 0, -5 },

					{ 0, 0, 5, 5, 5, 5, 0, -5 },

					{ -10, 5, 5, 5, 5, 5, 0, -10 },

					{ -10, 0, 5, 0, 0, 0, 0, -10 },

					{ -20, -10, -10, -5, -5, -10, -10, -20 }

			}

	};

	private static final int[][][] King = {

			{

					{ 20, 30, 10, 0, 0, 10, 30, 20 },

					{ 20, 20, 0, 0, 0, 0, 20, 20 },

					{ -10, -20, -20, -20, -20, -20, -20, -10 },

					{ -20, -30, -30, -40, -40, -30, -30, -20 },

					{ -30, -40, -40, -50, -50, -40, -40, -30 },

					{ -30, -40, -40, -50, -50, -40, -40, -30 },

					{ -30, -40, -40, -50, -50, -40, -40, -30 },

					{ -30, -40, -40, -50, -50, -40, -40, -30 }

			}, {

					{ -30, -40, -40, -50, -50, -40, -40, -30 },

					{ -30, -40, -40, -50, -50, -40, -40, -30 },

					{ -30, -40, -40, -50, -50, -40, -40, -30 },

					{ -30, -40, -40, -50, -50, -40, -40, -30 },

					{ -20, -30, -30, -40, -40, -30, -30, -20 },

					{ -10, -20, -20, -20, -20, -20, -20, -10 },

					{ 20, 20, 0, 0, 0, 0, 20, 20 },

					{ 20, 30, 10, 0, 0, 10, 30, 20 }

			}

	};

	public static int rating(long WK, long WQ, long WR, long WB, long WN, long WP,

			long BK, long BQ, long BR, long BB, long BN, long BP) {

		int bestscore = 0;

		bestscore += mobilityrating(WK, WQ, WR, WB, WN, WP, BK,

				BQ, BR, BB, BN, BP);

		bestscore += materialrating(WK, WQ, WR, WB, WN, WP, BK,

				BQ, BR, BB, BN, BP);

		bestscore += positionrating(WK, WQ, WR, WB, WN, WP, BK,

				BQ, BR, BB, BN, BP);

		return bestscore;

	}

	public static int Best(Case[][] echiquier, boolean W)

	{

		int WhiteScore = 0;

		int BlackScore = 0;

		for (int i = 0; i < 8; i++)

		{

			for (int j = 0; j < 8; j++)

			{

				if (echiquier[i][j].isOccupe() == 'p' || echiquier[i][j].isOccupe() == 'c'
						|| echiquier[i][j].isOccupe() == 't' || echiquier[i][j].isOccupe() == 'f'
						|| echiquier[i][j].isOccupe() == 'd' || echiquier[i][j].isOccupe() == 'r')

				{

					switch (echiquier[i][j].isOccupe())

					{

					case 'p':
						BlackScore += (Pawn[0][i][j] ) + echiquier[i][j].valeurCase();

					case 'c':
						BlackScore += (Knight[0][i][j] ) + echiquier[i][j].valeurCase();

					case 'f':
						BlackScore += (Bishop[0][i][j] ) + echiquier[i][j].valeurCase();

					case 't':
						BlackScore += (Rook[0][i][j] ) + echiquier[i][j].valeurCase();

					case 'd':
						BlackScore += (Queen[0][i][j] ) + echiquier[i][j].valeurCase();

					case 'r':
						BlackScore += (King[0][i][j] ) + echiquier[i][j].valeurCase();
					default:
						break;

					}

				}

				else {

					switch (echiquier[i][j].isOccupe())

					{
					case 'P':
						WhiteScore += (Pawn[1][i][j] ) + echiquier[i][j].valeurCase();

					case 'C':
						WhiteScore += (Knight[1][i][j] ) + echiquier[i][j].valeurCase();

					case 'F':
						WhiteScore += (Bishop[1][i][j] ) + echiquier[i][j].valeurCase();

					case 'T':
						WhiteScore += (Rook[1][i][j] ) + echiquier[i][j].valeurCase();

					case 'D':
						WhiteScore += (Queen[1][i][j] ) + echiquier[i][j].valeurCase();

					case 'R':
						WhiteScore += (King[1][i][j] ) + echiquier[i][j].valeurCase();
					default:
						break;

					}

				}

			}

		}

		if (W)

		{

			return WhiteScore - BlackScore;

		}

		else

		{

			return BlackScore - WhiteScore;

		}

	}

	public static int materialrating(long WK, long WQ, long WR, long WB, long WN, long WP,

			long BK, long BQ, long BR, long BB, long BN, long BP) {

		int bestscore = 0;

		long WKt = WK, WQt = WQ, WRt = WR, WBt = WB, WNt = WN, WPt = WP;

		long BKt = BK, BQt = BQ, BRt = BR, BBt = BB, BNt = BN, BPt = BP;

		while (WPt != 0) {

			WPt = WPt & (WPt - 1);

			bestscore += getmaterialvalue('P');

		}

		while (WNt != 0) {

			WNt = WNt & (WNt - 1);

			bestscore += getmaterialvalue('N');

		}

		while (WBt != 0) {

			WBt = WBt & (WBt - 1);

			bestscore += getmaterialvalue('B');

		}

		while (WRt != 0) {

			WRt = WRt & (WRt - 1);

			bestscore += getmaterialvalue('R');

		}

		while (WQt != 0) {

			WQt = WQt & (WQt - 1);

			bestscore += getmaterialvalue('Q');

		}

		while (WKt != 0) {

			WKt = WKt & (WKt - 1);

			bestscore += getmaterialvalue('K');

		}

		while (BPt != 0) {

			BPt = BPt & (BPt - 1);

			bestscore -= getmaterialvalue('P');

		}

		while (BNt != 0) {

			BNt = BNt & (BNt - 1);

			bestscore -= getmaterialvalue('N');

		}

		while (BBt != 0) {

			BBt = BBt & (BBt - 1);

			bestscore -= getmaterialvalue('B');

		}

		while (BRt != 0) {

			BRt = BRt & (BRt - 1);

			bestscore -= getmaterialvalue('R');

		}

		while (BQt != 0) {

			BQt = BQt & (BQt - 1);

			bestscore -= getmaterialvalue('Q');

		}

		while (BKt != 0) {

			BKt = BKt & (BKt - 1);

			bestscore -= getmaterialvalue('K');

		}

		return bestscore;

	}

	public static int getmaterialvalue(char piece) {

		int bestscore = 0;

		switch (piece) {

		case 'P':

			bestscore += 100;

			break;

		case 'N':

			bestscore += 320;

			break;

		case 'B':

			bestscore += 333;

			break;

		case 'R':

			bestscore += 510;

			break;

		case 'Q':

			bestscore += 880;

			break;

		case 'K':

			bestscore += 5000;

			break;

		}

		return bestscore;

	}

	public static int positionrating(long WK, long WQ, long WR, long WB, long WN, long WP,

			long BK, long BQ, long BR, long BB, long BN, long BP) {

		int score = 0;

		score += get_posRating(WK, 'K', White);

		score += get_posRating(WQ, 'Q', White);

		score += get_posRating(WR, 'R', White);

		score += get_posRating(WB, 'B', White);

		score += get_posRating(WN, 'N', White);

		score += get_posRating(WP, 'P', White);

		score -= get_posRating(BK, 'K', Black);

		score -= get_posRating(BQ, 'Q', Black);

		score -= get_posRating(BR, 'R', Black);

		score -= get_posRating(BB, 'B', Black);

		score -= get_posRating(BN, 'N', Black);

		score -= get_posRating(BP, 'P', Black);

		return score;

	}

	public static int get_posRating(long bitboard, char piece, int color) {

		int score = 0;

		long piece_pos = bitboard & ~(bitboard - 1);

		while (piece_pos != 0)

		{

			int position = Long.numberOfTrailingZeros(piece_pos);

			switch (piece) {

			case 'P':

				score += Pawn[color][position / 8][position % 8];

				break;

			case 'N':

				score += Knight[color][position / 8][position % 8];

				break;

			case 'B':

				score += Bishop[color][position / 8][position % 8];

				break;

			case 'R':

				score += Rook[color][position / 8][position % 8];

				break;

			case 'Q':

				score += Queen[color][position / 8][position % 8];

				break;

			case 'K':

				score += King[color][position / 8][position % 8];

				break;

			}

			bitboard &= ~piece_pos;

			piece_pos = bitboard & ~(bitboard - 1);

		}

		return score;

	}

	public static int mobilityrating(long WK,

			long WQ, long WR, long WB, long WN, long WP, long BK, long BQ,

			long BR, long BB, long BN, long BP) {

		int bestscore = 0;

		// String white_moves =
		// Move.MovesAvailableW(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);

		// String Black_moves =
		// Move.MovesAvailableB(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);

		// bestscore += (white_moves.length()/4) * 5;

		// bestscore -= (Black_moves.length()/4) * 5;

		return bestscore;

	}

}