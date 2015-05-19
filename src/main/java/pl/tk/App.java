package pl.tk;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.IntStream;


public class App 
{


    /*
    Cyfry kontrolne oblicza się według stałego algorytmu modulo 10.
    Dla przykładu cyfrę kontrolną (trzynastą) w EAN 13 oblicza się mnożąc pierwszą cyfrę przez 1,
    drugą przez 3, trzecią przez 1, czwartą przez 3, piątą przez 1, szóstą przez 3, siódmą przez 1, ósmą przez 3,
    dziewiątą przez 1, dziesiątą przez 3, jedenastą przez 1, dwunastą przez 3.
    Następnie sumuje się wszystkie iloczyny.
    Sumę dzieli się przez 10 i zostawia się tylko resztę.
    Na koniec od 10 odejmuje się resztę.
    Jeśli suma iloczynów jest liczbą podzielną przez 10, to w takim wypadku cyfrą kontrolną jest 0.
     */

    public static int genChecksumNumberForEan13And8(String numbers) {
        assert (numbers.length() == 7 || numbers.length() == 12);
        Objects.requireNonNull(numbers);

        int oddMultiplier;
        int evenMultipler;
        switch (numbers.length()) {
            case 7:
                oddMultiplier = 1;
                evenMultipler = 3;
                break;
            default:
            case 12:
                oddMultiplier = 3;
                evenMultipler = 1;
        }

        char [] asChars = numbers.toCharArray();
        int rest = IntStream.range(0, numbers.length()).map((int i) -> {
            int number = asChars[i] - 48;
            if(i % 2 == 0) number *= evenMultipler;
            else number *= oddMultiplier;
            return number;
        }).sum() % 10;
        if(rest == 0) return 0;
        else return 10 - rest;
    }


    public static void main( String[] args ) throws WriterException, IOException {
        final int requestedWidth = 400;
        final int requestedHeight  = 300;

        final String dataEan8 = "4857262";
        final String dataEan13 = "973518470290";

        final String eanValue13 = dataEan13 + genChecksumNumberForEan13And8(dataEan13);
        final String eanValue8 = dataEan8 + genChecksumNumberForEan13And8(dataEan8);
        final String imageFormat = "jpg";
        final String outputLocationEan13 = "sample/ean13_" + System.currentTimeMillis() + ".jpg";
        final String outputLocationEan8 = "sample/ean8_" + System.currentTimeMillis() + ".jpg";
        final BitMatrix bitMatrix8 = new EAN8Writer()
                .encode(eanValue8, BarcodeFormat.EAN_8, requestedWidth, requestedHeight);

        final BitMatrix bitMatrix13 = new EAN13Writer()
                .encode(eanValue13, BarcodeFormat.EAN_13, requestedWidth, requestedHeight);
        MatrixToImageWriter.writeToPath(bitMatrix13,imageFormat, Paths.get(outputLocationEan13));
        MatrixToImageWriter.writeToPath(bitMatrix8,imageFormat, Paths.get(outputLocationEan8));
        System.out.println("Image ean8 written to: " + outputLocationEan8);
        System.out.println("Image ean13 written to: " + outputLocationEan13);
    }
}
