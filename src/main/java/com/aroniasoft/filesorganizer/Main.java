package com.aroniasoft.filesorganizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;

import reactor.core.publisher.Flux;

public class Main {

	public static void main(String[] args) {
		try {
			File destinationDir = new File("/media/darko/ext/videos");
			destinationDir.mkdirs();
			final MessageDigest md = MessageDigest.getInstance("MD5");
			Res cnt = new Res();
			Flux.fromStream(Files.walk(Paths.get("/media/darko/SP_UHD_3/sve_slike"))).
//			filter(path -> FilenameUtils.getExtension(path.toString()).equalsIgnoreCase("jpg")).
//			collect(()-> new HashMap<String, String>(), (agg, f) -> {
//				try(InputStream is = Files.newInputStream(f)) {
//					agg.put(DigestUtils.md5Hex(is).toUpperCase(), f.toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}).subscribe(result -> System.out.println("Unique results: " + result.size()));
			collect(() -> new HashMap<String, String>(), (agg, path) -> {
				System.out.println(++cnt.cnt);
				String extension = FilenameUtils.getExtension(path.toString()).toLowerCase();
				if(extension.equalsIgnoreCase("mov") ||
						extension.equalsIgnoreCase("mp4") ||
						extension.equalsIgnoreCase("3gp")) {
					String creationDate = MediaProcessor.readVideoCreationDate(path.toAbsolutePath().toString());
					File dstDir = new File(destinationDir + File.separator + creationDate);
					dstDir.mkdir();
					File dst = new File(dstDir.getAbsolutePath() + File.separator + path.getFileName());
					if(!(dst.exists() && dst.isFile())) {
						System.out.println(dst.getAbsolutePath());
						Path copied = Paths.get(dstDir.getAbsolutePath() + File.separator + path.getFileName());
						try {
							Files.copy(path, copied, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							System.out.println("Failed to copy: " + path + " to: " + copied + " CAUSE: " + e.getMessage());
						}
						agg.put(path.toString(), copied.toString());
					}
				}
//				if(!extension.isBlank()) {
//					Integer current = agg.get(extension);
//					if(current == null) {
//						current = 0;
//					}
//					++current;
//					agg.put(extension, current);
//				}
			}).subscribe(result -> result.keySet().stream().forEach(key -> {
				System.out.println(key + " | " + result.get(key));
			}));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class Res {
		public int cnt = 0;
	}
}
