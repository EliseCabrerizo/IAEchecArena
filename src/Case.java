import java.util.ArrayList;



public class Case {

	public static final int PION = 100;

	public static final int CAVALIER = 350;

	public static final int FOU = 500;

	public static final int TOUR = 500;

	public static final int DAME = 1750;

	public static final int ROI = 5000;

	private char occupe;

	private ArrayList<Character> atteignable; // liste des pieces pouvant atteindre la case au prochain tour

	public char isOccupe() {

		return occupe;

	}

	public void setOccupe(char occupe) {

		this.occupe = occupe;

	}

	public ArrayList<Character> getAtteignable() {

		return atteignable;

	}

	public void setAtteignable(ArrayList<Character> atteignable) {

		this.atteignable = atteignable;

	}



	public void addAtteignable(Character atteignable) {

		this.atteignable.add(atteignable);

	}

	

	public Case() {

		super();

		this.occupe = 'v';

		this.atteignable = new ArrayList<Character>();

	}

	public boolean defendable(boolean W)

	{

		if(W) {

			if(this.atteignable.contains('p') || this.atteignable.contains('t') || this.atteignable.contains('c') || this.atteignable.contains('f') || this.atteignable.contains('d')) // si defendable par autre chose que le roi

			{

				return true;

			}

			else

			{

				return false;

			}

		}

		else

		{

			if(this.atteignable.contains('P') || this.atteignable.contains('T') || this.atteignable.contains('C') || this.atteignable.contains('F') || this.atteignable.contains('D')) // si defendable par autre chose que le roi

			{

				return true;

			}

			else

			{

				return false;

			}

		}

	}



	public int valeurA(boolean W) // permet de connaitre la valeur de la piece adverse qui occupe la case (si adversaire il y a) 

	{

		if (W)

		{

			switch(this.occupe)

			{

			case 'p': return PION;

			case 'c': return CAVALIER;

			case 'f': return FOU;

			case 't': return TOUR;

			case 'd': return DAME;

			case 'r': return ROI;

			default : return 0;

			}

		}

		else

		{

			switch(this.occupe)

			{

			case 'P': return PION;

			case 'C': return CAVALIER;

			case 'F': return FOU;

			case 'T': return TOUR;

			case 'D': return DAME;

			case 'R': return ROI;

			default : return 0;

			}

		}

	}

	

	public int valeurCase()

	{

		switch(this.occupe)

		{

		case 'p': return PION;

		case 'c': return CAVALIER;

		case 'f': return FOU;

		case 't': return TOUR;

		case 'd': return DAME;

		case 'r': return ROI;

		case 'P': return PION;

		case 'C': return CAVALIER;

		case 'F': return FOU;

		case 'T': return TOUR;

		case 'D': return DAME;

		case 'R': return ROI;

		default : return 0;

		}

	}

	public char getOccupe() {
		return occupe;
	}

}