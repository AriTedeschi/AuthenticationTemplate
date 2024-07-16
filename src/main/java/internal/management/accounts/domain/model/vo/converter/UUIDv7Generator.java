package internal.management.accounts.domain.model.vo.converter;

import java.security.SecureRandom;
import java.util.UUID;

public class UUIDv7Generator {
    private static final SecureRandom random = new SecureRandom();

    private UUIDv7Generator() {
        throw new IllegalStateException("Utility class");
    }
    public static UUID generateUUID() {
        long timestamp = System.currentTimeMillis();
        long timestampHigh = timestamp >>> 28;
        long timestampLow = timestamp & 0x0FFFFFFF;
        long randomHigh = random.nextLong() & 0x0FFFFF;
        long randomLow = random.nextLong();

        long mostSigBits = (timestampHigh << 44) | (timestampLow << 16) | randomHigh;
        long leastSigBits = randomLow;

        return new UUID(mostSigBits, leastSigBits);
    }
}

