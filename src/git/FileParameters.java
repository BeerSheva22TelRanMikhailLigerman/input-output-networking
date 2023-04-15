package git;

import java.time.Instant;

public record FileParameters(String[] data, Instant timeOfFileModified) {

}
