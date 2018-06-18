package cdio.main.test;

public class testafstand {


    public static void main(String[] args) {

        for (int i = 130; i < 400; i = i + 20) {
            System.out.println(0.8 * Math.pow(i, -1.021) * 48722);
        }

    }
}
