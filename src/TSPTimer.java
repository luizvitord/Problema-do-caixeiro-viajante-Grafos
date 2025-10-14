/* *****************************************************************************
 * VOCÊ NÃO PRECISA MODIFICAR ESTE ARQUIVO
 *
 * Compilação:  javac TSPTimer.java
 * Execução:    java -Xint TSPTimer n
 * Dependências: Tour.java Point.java Stopwatch.java StdOut.java
 *
 * Mede o tempo da heurística do vizinho mais próximo gerando instâncias aleatórias de tamanho n.
 *
 * Exemplo de execução:
 * % java -Xint TSPTimer 1000
 *
 * Observação: os arquivos de entrada (caso utilizados) devem estar na pasta data/.
 *
 **************************************************************************** */

import algs4.StdRandom;
import algs4.Stopwatch;
import algs4.StdOut;

public class TSPTimer {

    public static void main(String[] args) {
        double lo = 0.0;
        double hi = 600.0;
        int n = Integer.parseInt(args[0]);
        long seed = 123456789L;

        // --- Teste da Versão Ingênua (sem Kd-Tree) ---
        // comentar esse bloco de teste caso queira testar somente a KdTree (para valores mt altos)
        StdRandom.setSeed(seed);
        Stopwatch timer1 = new Stopwatch();
        Tour tour1 = new Tour(false);
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniformDouble(lo, hi);
            double y = StdRandom.uniformDouble(lo, hi);
            Point p = new Point(x, y);
            tour1.insertNearest(p);
        }
        double elapsed1 = timer1.elapsedTime();
        StdOut.println("Comprimento do ciclo (ingênuo) = " + tour1.length());
        StdOut.println("Inserção pelo vizinho mais próximo: " + elapsed1 + " segundos");
        StdOut.println();

        // --- Teste da Versão Otimizada (com Kd-Tree) ---
        StdRandom.setSeed(seed);
        Stopwatch timer2 = new Stopwatch();
        Tour tour2 = new Tour(true);
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniformDouble(lo, hi);
            double y = StdRandom.uniformDouble(lo, hi);
            Point p = new Point(x, y);
            tour2.insertNearest(p);
        }
        double elapsed2 = timer2.elapsedTime();
        StdOut.println("Comprimento do ciclo (Kd-Tree) = " + tour2.length());
        StdOut.println("Inserção pelo vizinho mais próximo (Kd-Tree): " + elapsed2 + " segundos");
    }
}