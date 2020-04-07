import com.aroniasoft.filesorganizer.MetadataExample;
import org.apache.commons.imaging.ImageReadException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MetadataReaderTest {

    @Test
    public void readJPGMetadataTest() {
        File f = new File("/media/darko/ext/sve_slike/za_izradu/DSC_0005.JPG");
        try {
            MetadataExample.metadataExample(f);
        } catch (ImageReadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void formatDate() {
        String formattedDate = LocalDate.parse("2016-11-06T22:34:08Z",
                DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'")).
                format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        System.out.println(formattedDate);
    }
}
