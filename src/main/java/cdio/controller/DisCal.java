package cdio.controller;

public class DisCal {

   public double disCal(int with){
        return Math.pow(5976.4*with, -0.923);
    }

    public double distanceFromHeight(double qrCodeHeight) {
        return Math.pow(48722* qrCodeHeight, -1.021);
    }

}
