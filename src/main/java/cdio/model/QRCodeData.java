package cdio.model;

public class QRCodeData {

    private int result;
    private int orientation;
    private float foundYaw;

    public QRCodeData(String codeResult, int codeAngle, float foundYaw) {
        this.result = codeResult.charAt(3) - '0';
        this.orientation = codeAngle;
        this.foundYaw = foundYaw;
    }

    public int getResult() {
        return result;
    }

    public int getOrientation() {
        return orientation;
    }

    public float getFoundYaw() {
        return foundYaw;
    }

    @Override
    public String toString() {
        return "QRCodeData [" +
                ", result = '" + result + '\'' +
                ", orientation = " + orientation +
                ", foundYaw = " + foundYaw + "]";
    }

}