package pl.tk;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;

import java.io.IOException;
import java.nio.file.Paths;


public class App 
{



    public static void main( String[] args ) throws WriterException, IOException {
        final int requestedWidth = 400;
        final int requestedHeight  = 300;
        final String eanValue = "8592084405892";
        final String imageFormat = "jpg";
        final String outputLocation = "sample/qrcode_" + System.currentTimeMillis() + ".jpg";
        final BitMatrix bitMatrix = new EAN13Writer()
                .encode(eanValue, BarcodeFormat.EAN_13, requestedWidth, requestedHeight);
        MatrixToImageWriter.writeToPath(bitMatrix,imageFormat, Paths.get(outputLocation));
        System.out.println("Image written to: " + outputLocation);
    }
}
