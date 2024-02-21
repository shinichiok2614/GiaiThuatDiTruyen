package geneticalgorithm;

import java.util.Arrays;
import java.util.Random;

public class GA {

    public int max = 10000;
    public int khoangcach[][] = {
            {max, 5, 6, 9, max},
            {5, max, 10, 2, 7},
            {6, 10, max, max, 15},
            {9, 2, max, max, 1},
            {max, 7, 15, 1, max}};
    public Random ran = new Random();
    public static int n = 1000;
    int[][] nghiem = new int[n][];
    int[] thichnghi = new int[n];


    public void khoitao() {

        for (int i = 0; i < n; i++) {
            nghiem[i] = new int[5];
            for (int j = 0; j < 5; j++) nghiem[i][j] = ran.nextInt(5);
        }
    }

    public void danhgia() {

        for (int i = 0; i < n; i++) {
            thichnghi[i] = 0;
            for (int j = 0; j < 4; j++) {  //tinh toan gia tri thichnghi theo khoangcach lay gia tri trong nghiem
                thichnghi[i] += khoangcach[nghiem[i][j]][nghiem[i][j + 1]];
            }
            for (int j = 0; j < 4; j++)   //tim gia tri trung nhau cua trong nghiem, trung thi +100.000
                for (int t = j + 1; t < 5; t++)
                    if (nghiem[i][j] == nghiem[i][t]) thichnghi[i] += 100000;
        }
    }

    public void print() {

        int[] temp = thichnghi.clone();
        Arrays.sort(temp);
        int best = temp[0];
        System.out.print(best + ": ");
        for (int i = 0; i < n; i++) {
            if (thichnghi[i] == best) {
                for (int j = 0; j < nghiem[i].length; j++)
                    System.out.print(nghiem[i][j] + " ,");
                System.out.println();
                break;
            }
        }
    }

    public void chonloc() {

        int[] temp = thichnghi.clone();
        Arrays.sort(temp);
        int nguong = temp[n * 80 / 100]; //lấy ngưỡng ở 80%
        for (int i = 0; i < n; i++) {
            if (thichnghi[i] > nguong) { //lớn hơn ngưỡng sẽ bị loại bỏ và tạo ra 1 đứa random khác
                nghiem[i] = nghiem[ran.nextInt(n)].clone();
            }
        }
    }

    public void laighep() {
        for (int i = 0; i < 20; i++) {
            int cha = ran.nextInt(n);
            int me = ran.nextInt(n);
            for (int j = 0; j < nghiem[cha].length; j++)
                if (ran.nextInt(2) == 1) {
                    int temp = nghiem[cha][j];
                    nghiem[cha][j] = nghiem[me][j];
                    nghiem[me][j] = temp;
                }
        }
    }

    public void dotbien() {
        int index = ran.nextInt(n);
        int bit = ran.nextInt(5);
        nghiem[index][bit] = ran.nextInt(5);
    }
}
