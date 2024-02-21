import geneticalgorithm.GA;

public class Main {
    public static void main(String[] args) {
        GA ga=new GA();
        ga.khoitao();
        for (int i = 0; i < 100; i++) {
            ga.danhgia();
            ga.print();
            ga.chonloc();
            ga.laighep();
            ga.dotbien();
        }
    }
}