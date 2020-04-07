package com.aroniasoft.filesorganizer;

import com.xuggle.xuggler.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MediaProcessor {

    public static String readVideoCreationDate(String filePath) {

        IContainer container = IContainer.make();
//        int result = container.open("/media/darko/ext/sve_slike/Iphone/iphone_slike_09.01.16/IMG_1473.MOV", IContainer.Type.READ, null);
//        int result = container.open("/media/darko/ext/sve_slike/natasa-tel/DCIM/Camera/VID_20170421_151730.3gp", IContainer.Type.READ, null);
//        int result = container.open("/media/darko/ext/sve_slike/bec-2017/SAM_0932.MP4", IContainer.Type.READ, null);
        int result = container.open(filePath, IContainer.Type.READ, null);
        String creationDate = "undetermined";
        if (result<0) {
            System.out.println("Failed to open media file");
            try {
                BasicFileAttributes attr = Files.readAttributes(Path.of(filePath), BasicFileAttributes.class);
                FileTime fileTime = attr.creationTime();
                creationDate = formatDate(fileTime.toString(), "uuuu-MM-dd'T'HH:mm:ss'Z'", "dd-MM-uuuu");
            } catch (IOException ex) {
                System.out.println("Failed to read file: " + ex);
                ex.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
//            int numStreams = container.getNumStreams();
//            long duration = container.getDuration();
//            long fileSize = container.getFileSize();
//            long bitRate = container.getBitRate();
//
//            System.out.println("Number of streams: " + numStreams);
//            System.out.println("Duration (ms): " + duration);
//            System.out.println("File Size (bytes): " + fileSize);
//            System.out.println("Bit Rate: " + bitRate);
//
//            // iterate through the streams to print their meta data
//
//            for (int i = 0; i < numStreams; i++) {
//
//                IStream stream = container.getStream(i);
//                IStreamCoder coder = stream.getStreamCoder();
//                System.out.println("*** Start of Stream Info ***");
//                System.out.printf("stream %d: ", i);
//                System.out.printf("type: %s; ", coder.getCodecType());
//                System.out.printf("codec: %s; ", coder.getCodecID());
//                System.out.printf("duration: %s; ", stream.getDuration());
//                System.out.printf("start time: %s; ", container.getStartTime());
//                System.out.printf("timebase: %d/%d; ",
//                        stream.getTimeBase().getNumerator(),
//                        stream.getTimeBase().getDenominator());
//
//                System.out.printf("coder tb: %d/%d; ",
//                        coder.getTimeBase().getNumerator(),
//                        coder.getTimeBase().getDenominator());
//
//                System.out.println();
//
//                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
//                    System.out.printf("sample rate: %d; ", coder.getSampleRate());
//                    System.out.printf("channels: %d; ", coder.getChannels());
//                    System.out.printf("format: %s", coder.getSampleFormat());
//                } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
//                    System.out.printf("width: %d; ", coder.getWidth());
//                    System.out.printf("height: %d; ", coder.getHeight());
//                    System.out.printf("format: %s; ", coder.getPixelType());
//                    System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());
//                }

                IMetaData meta = container.getMetaData();
                for (String key : meta.getKeys()) {
//                    System.out.println(key + " | " + meta.getValue(key));
                    if(key.equalsIgnoreCase("creation_time")) {
                        creationDate = MediaProcessor.formatDate(meta.getValue(key), "uuuu-MM-dd HH:mm:ss", "dd-MM-uuuu");
                    }
                }
                container.close();
//                System.out.println();
//
//                System.out.println("*** End of Stream Info ***");

            }
        return creationDate;
    }

    public static String formatDate(String date, String inputPattern, String outputPattern) {
        return LocalDate.parse(date,
                DateTimeFormatter.ofPattern(inputPattern)).
                format(DateTimeFormatter.ofPattern(outputPattern));
    }
}
