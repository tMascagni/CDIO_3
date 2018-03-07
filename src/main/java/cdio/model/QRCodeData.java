package cdio.model;

public class QRCodeData {

    private int width;
    private int height;
    private String result;
    private int orientation;

    public QRCodeData(int codeWidth, int codeHeight, String codeResult, int codeAngle) {
        this.width = codeWidth;
        this.height = codeHeight;
        this.result = codeResult;
        this.orientation = codeAngle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getResult() {
        return result;
    }

    public int getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        return "QRCodeData [" +
                "width = " + width +
                ", height = " + height +
                ", result = '" + result + '\'' +
                ", orientation = " + orientation +
                ']';
    }

}