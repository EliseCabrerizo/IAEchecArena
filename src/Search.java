public class Search {

	

	public static int Max_depth = 1;

	public static int White = 1;

	public static int Black = 0;

	// methode pour trouver le meilleur coup

	public static String get_bestmove(long WK,

			long WQ, long WR,long WB,long WN,long WP,long BK,long BQ,

			long BR,long BB,long BN,long BP, boolean WhiteMove){

		int depth = 0; // profondeur

		int player; // pour savoir si on est noir ou blanc

		if(WhiteMove){

			player = White;

		}else{

			player = Black;

		}

		Case[][] echiquier=new Case[8][8];

		// construction de l'echiquier

		for (int i=0;i<64;i++) {

            echiquier[i/8][i%8]=new Case();

        }

		for (int i=0;i<64;i++) {

            if (((WP>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('P');}

            if (((WN>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('C');}

            if (((WB>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('F');}

            if (((WR>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('T');}

            if (((WQ>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('D');}

            if (((WK>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('R');}

            if (((BP>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('p');}

            if (((BN>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('c');}

            if (((BB>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('f');}

            if (((BR>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('t');}

            if (((BQ>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('d');}

            if (((BK>>i)&1)==1) {echiquier[i/8][i%8].setOccupe('r');}

        }

		String bestmove = minmax_alphabeta(-10000,10000, WK,WQ,WR,WB,WN,WP,BK,

				BQ,BR,BB,BN,BP,player,depth,""); // min_maxAlphaBeta

		return bestmove;

	}

	

	

	public static String SuperMinMax_Alpha_Beta_Gamma_Omega(int alpha, int beta, Case[][] echiquier, boolean W, int prof, String mvt)

	{

		String Bestmove=" ";

		int bestScore=0;

		String mvtDispo="";

		String alphamove = "0000";

		String betamove = "0000";

		if(prof == Max_depth){	// si on a atteint la prof max on va evaluer le meilleur coup ??

			bestScore = Evaluation.Best(echiquier,W);

			Bestmove=mvt+bestScore;

			return Bestmove;

		}

		if(W)

		{

			mvtDispo=Move.calculW(echiquier);

		}

		else

		{

			mvtDispo=Move.calculB(echiquier);

		}

		for(int i=0;i<mvtDispo.length();i+=4)

		{

			String move=mvtDispo.substring(i, i+4);

			Case[][] echiquierTemp=new Case[8][8];

			echiquierTemp=echiquier; // a voir si ça passe par pointeur (peut être bug)

			// faire fonction conversion string to int de position

			

		}



		return Bestmove;

	}

	

	

	

	

	

	public static String minmax_alphabeta(int alpha, int beta, long WK,

			long WQ, long WR,long WB,long WN,long WP,long BK,long BQ,

			long BR,long BB,long BN,long BP, int player, int depth, String moves){

		long WKt = 0l, WQt = 0l, WRt = 0l, WBt = 0l, WNt = 0l, WPt = 0l;

		long BKt = 0l, BQt = 0l, BRt = 0l, BBt = 0l, BNt = 0l, BPt = 0l; // permet de garder en mémoire la position du mvt à faire

		int bestscore; //  score du meilleur mvt

		String bestmove = "0000"; 

		String move = "0000";

		String alphamove = "0000";

		String betamove = "0000";

		if(depth == Max_depth){	// si on a atteint la prof max on va evaluer le meilleur coup ??

			bestscore = Evaluation.rating(WK,WQ,WR,WB,WN,WP,BK,

					BQ,BR,BB,BN,BP);

			bestmove = moves+bestscore;

			return bestmove;

		}

		

		if(player == White){

			moves = Move.MovesAvailableW(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);	// calcul des coups possibles ?

		}else{

			moves = Move.MovesAvailableB(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);

		}

		

		for(int i = 0; i < moves.length(); i+=4){	// calcul les coups possible de chaque piece ?

			move = moves.substring(i, i+4);

			WPt = Move.makemove(WP, move, 'P');

			WNt = Move.makemove(WN, move, 'N');

			WBt = Move.makemove(WB, move, 'B');

			WRt = Move.makemove(WR, move, 'R');

			WQt = Move.makemove(WQ, move, 'Q');

			WKt = Move.makemove(WK, move, 'K');

			BPt = Move.makemove(BP, move, 'p');

			BNt = Move.makemove(BN, move, 'n');

			BBt = Move.makemove(BB, move, 'b');

			BRt = Move.makemove(BR, move, 'r');

			BQt = Move.makemove(BQ, move, 'q');

			BKt = Move.makemove(BK, move, 'k');

			

			bestmove = minmax_alphabeta(alpha, beta, WKt,WQt,WRt,WBt,WNt,WPt,BKt,

					BQt,BRt,BBt,BNt,BPt,player^1,depth+1,move); // rappelle le minmax_alphabeta en changeant le joueur qui joue

			bestscore = Integer.valueOf(bestmove.substring(4)); // pourquoi substring ?

			if(player == Black){	// fait le changement alpha beta, en fct de quel joueur on calcul

				if(bestscore < beta){

					beta = bestscore;

					betamove = move;

				}

			}else{

				if(bestscore > alpha){

					alpha = bestscore;

					alphamove = move;

				}

			}

			

			if(alpha >= beta){

				if(player == Black){

					return betamove+beta;

				}else{

					return alphamove+alpha;

				}

			}

		}

		if(player == Black){

			return betamove+beta;

		}else{

			return alphamove+alpha;

		}

	}

	

	public static int ischeck(long WK,

			long WQ, long WR,long WB,long WN,long WP,long BK,long BQ,

			long BR,long BB,long BN,long BP, int player){

		int bestscore = 0;

		//if(player == White){

			long Blackatt_map = Move.attackbitmapB(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);

			if((WK & Blackatt_map) != 0){

				bestscore = 5000;

			}

		//}else{

			long Whiteatt_map = Move.attackbitmapW(WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);

			if((BK & Whiteatt_map) != 0){

				bestscore = -5000;

			}

		//}

		

		return bestscore;

		

	}



}