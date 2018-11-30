import java.util.*;
public class GenerateBoard {
	public static void beginboard(){
		long WK = 0l, WQ = 0l, WR = 0l, WB = 0l, WN = 0l, WP = 0l;
		long BK = 0l, BQ = 0l, BR = 0l, BB = 0l, BN = 0l, BP = 0l;
		String board[][] = {
				{"r","n","b","q","k","b","n","r"},
				{"p","p","p","p","p","p","p","p"},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{"P","P","P","P","P","P","P","P"},
				{"R","N","B","Q","K","B","N","R"}
		};
		stringtobitboard(board, WK,WQ,WR,WB,WN,WP,BK,BQ,BR,BB,BN,BP);
	}
	
	static void stringtobitboard(String[][] board, long WK,long WQ,long WR,long WB,
			long WN,long WP,long BK,long BQ,long BR,long BB,long BN,long BP){

        for (int i=0;i<64;i++) {
            switch (board[i/8][i%8]) {
                case "P": WP |= (1l << i);
                    break;
                case "N": WN |= (1l << i);
                    break;
                case "B": WB |= (1l << i);
                    break;
                case "R": WR |= (1l << i);
                    break;
                case "Q": WQ |= (1l << i);
                    break;
                case "K": WK |= (1l << i);
                    break;
                case "p": BP |= (1l << i);
                    break;
                case "n": BN |= (1l << i);
                    break;
                case "b": BB |= (1l << i);
                    break;
                case "r": BR |= (1l << i);
                    break;
                case "q": BQ |= (1l << i);
                    break;
                case "k": BK |= (1l << i);
                    break;
            }
        }
        GuiInterface.WK = WK;
		GuiInterface.WQ = WQ;
		GuiInterface.WR = WR;
		GuiInterface.WB = WB;
		GuiInterface.WN = WN;
		GuiInterface.WP = WP;
		GuiInterface.BK = BK;
		GuiInterface.BQ = BQ;
		GuiInterface.BR = BR;
		GuiInterface.BB = BB;
		GuiInterface.BN = BN;
		GuiInterface.BP = BP;
		
	}
	
    public static void drawboard(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]=" ";
        }
        for (int i=0;i<64;i++) {
            if (((WP>>i)&1)==1) {chessBoard[i/8][i%8]="P";}
            if (((WN>>i)&1)==1) {chessBoard[i/8][i%8]="N";}
            if (((WB>>i)&1)==1) {chessBoard[i/8][i%8]="B";}
            if (((WR>>i)&1)==1) {chessBoard[i/8][i%8]="R";}
            if (((WQ>>i)&1)==1) {chessBoard[i/8][i%8]="Q";}
            if (((WK>>i)&1)==1) {chessBoard[i/8][i%8]="K";}
            if (((BP>>i)&1)==1) {chessBoard[i/8][i%8]="p";}
            if (((BN>>i)&1)==1) {chessBoard[i/8][i%8]="n";}
            if (((BB>>i)&1)==1) {chessBoard[i/8][i%8]="b";}
            if (((BR>>i)&1)==1) {chessBoard[i/8][i%8]="r";}
            if (((BQ>>i)&1)==1) {chessBoard[i/8][i%8]="q";}
            if (((BK>>i)&1)==1) {chessBoard[i/8][i%8]="k";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
	

}
