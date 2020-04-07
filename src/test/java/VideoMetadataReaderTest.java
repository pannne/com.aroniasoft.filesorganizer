import com.aroniasoft.filesorganizer.MediaProcessor;
import com.xuggle.xuggler.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class VideoMetadataReaderTest {

    @Test
    public void readVideoMetadata() {
        try {
            BasicFileAttributes attr = Files.readAttributes(Path.of("/media/darko/ext/sve_slike/Iphone/09-11-16/IMG_3885.MOV"), BasicFileAttributes.class);
            FileTime fileTime = attr.creationTime();
            System.out.println(fileTime);
        } catch (IOException ex) {
            // handle exception
        }
        // first we create a Xuggler container object

        IContainer container = IContainer.make();

        // we attempt to open up the container

//        int result = container.open("/media/darko/ext/sve_slike/Iphone/iphone_slike_09.01.16/IMG_1473.MOV", IContainer.Type.READ, null);
//        int result = container.open("/media/darko/ext/sve_slike/natasa-tel/DCIM/Camera/VID_20170421_151730.3gp", IContainer.Type.READ, null);
        int result = container.open("/media/darko/ext/videos/27-08-2017/IMG_0974.MOV", IContainer.Type.READ, null);

        // check if the operation was successful

        if (result<0)

            throw new RuntimeException("Failed to open media file");

        // query how many streams the call to open found

        int numStreams = container.getNumStreams();

        // query for the total duration

        long duration = container.getDuration();

        // query for the file size

        long fileSize = container.getFileSize();

        // query for the bit rate

        long bitRate = container.getBitRate();

        System.out.println("Number of streams: " + numStreams);

        System.out.println("Duration (ms): " + duration);

        System.out.println("File Size (bytes): " + fileSize);

        System.out.println("Bit Rate: " + bitRate);

        // iterate through the streams to print their meta data

        for (int i=0; i<numStreams; i++) {

// find the stream object

            IStream stream = container.getStream(i);

// get the pre-configured decoder that can decode this stream;

            IStreamCoder coder = stream.getStreamCoder();

            System.out.println("*** Start of Stream Info ***");

            System.out.printf("stream %d: ", i);

            System.out.printf("type: %s; ", coder.getCodecType());

            System.out.printf("codec: %s; ", coder.getCodecID());

            System.out.printf("duration: %s; ", stream.getDuration());

            System.out.printf("start time: %s; ", container.getStartTime());

            System.out.printf("timebase: %d/%d; ",

                    stream.getTimeBase().getNumerator(),

                    stream.getTimeBase().getDenominator());

            System.out.printf("coder tb: %d/%d; ",

                    coder.getTimeBase().getNumerator(),

                    coder.getTimeBase().getDenominator());

            System.out.println();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {

                System.out.printf("sample rate: %d; ", coder.getSampleRate());

                System.out.printf("channels: %d; ", coder.getChannels());

                System.out.printf("format: %s", coder.getSampleFormat());

            }

            else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {

                System.out.printf("width: %d; ", coder.getWidth());

                System.out.printf("height: %d; ", coder.getHeight());

                System.out.printf("format: %s; ", coder.getPixelType());

                System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());

            }

            IMetaData meta = container.getMetaData();
            for(String key : meta.getKeys()) {
                if(key.equalsIgnoreCase("creation_time")) {
                    System.out.println(key + " | " + meta.getValue(key));
                    System.out.println(MediaProcessor.formatDate(meta.getValue(key), "uuuu-MM-dd HH:mm:ss", "dd-MM-uuuu"));
                }
            }

            System.out.println();

            System.out.println("*** End of Stream Info ***");

        }
    }
}
