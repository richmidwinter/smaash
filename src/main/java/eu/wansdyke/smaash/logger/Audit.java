package eu.wansdyke.smaash.logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import com.google.common.io.Files;

public class Audit {

	public static void log(final String auditLog, final String message) throws IOException {
		Files.append(
				String.format("%s %s", LocalDateTime.now().toString(), message),
				Paths.get(auditLog).toFile(),
				Charset.forName("UTF-8"));
	}
}
