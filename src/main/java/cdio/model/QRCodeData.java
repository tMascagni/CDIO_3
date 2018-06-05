package cdio.model;

public class QRCodeData {

    private int width;
    private int height;
    private int result;
    private int orientation;
    private float foundYaw;

    public QRCodeData(int codeWidth, int codeHeight, String codeResult, int codeAngle, float foundYaw) {
        this.width = codeWidth;
        this.height = codeHeight;
        this.result = codeResult.charAt(3) - '0';
        this.orientation = codeAngle;
        this.foundYaw = foundYaw;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
                "width = " + width +
                ", height = " + height +
                ", result = '" + result + '\'' +
                ", orientation = " + orientation +
                ", foundYaw = " + foundYaw + "]";
    }

}