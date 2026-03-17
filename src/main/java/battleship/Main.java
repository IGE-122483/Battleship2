package battleship;

import java.util.ArrayList;
import java.util.List;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("***  Battleship  ***");

		List<String> jogadas = new ArrayList<>();
		jogadas.add("Jogada 1 - B4 - Agua");
		jogadas.add("Jogada 2 - C6 - Acertou");
		jogadas.add("Jogada 3 - D6 - Afundou");

		Pdf.exportarJogadas(jogadas, "jogadas.pdf");
		BoardView.mostrar(jogadas);

		Tasks.menu();
	}
}
