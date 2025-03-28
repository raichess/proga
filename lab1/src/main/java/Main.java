import java.util.List;
import static java.lang.Math.*;

public class Main {
    public static void main(String[] args) {
        int[] z = new int[13];
        for (int i = 4; i <= 16; i++) {
            z[i - 4] = i;
        }
        double[] x = new double[15];
        double min = -10.0, max = 6.0;
        for (int i = 0; i < x.length; i++) {
            x[i] = min + random() * (max - min);
        }
        double[][] r = new double[13][15];
        List<Integer> list = List.of(6, 7, 11, 12, 13, 15);
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[i].length; j++) {
                if (z[i] == 9) {
                    r[i][j] = Calculate1(x[j]);
                } else if (list.contains(z[i])) {
                    r[i][j] = Calculate2(x[j]);
                } else {
                    r[i][j] = Calculate3(x[j]);
                }
            }
        }
        Matrix(r);
    }

    public static double Calculate1(double a){
        return tan(pow(a, 1.0/9));
    }

    public static double Calculate2(double a){
        return pow(pow((pow(a, 1.0/3) * PI),1.0/3),1.0/2);
    }

    public static double Calculate3(double a){
        return tan(exp(cos(tan(a))));
    }
    public static void Matrix (double[][] array){
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.printf("%8.2f", array[i][j]);
            }
            System.out.println();
        }
    }
}
