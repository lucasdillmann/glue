package glue.config.api.extension.artifacts;

/**
 * Test artifacts for {@link glue.config.api.extension.DefaultConfigurationTranslatorTests}
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2018-12-02
 */
public class DefaultConfigurationTranslatorArtifacts {

    public enum Enum {
        A, B, C
    }

    public static abstract class Base {
        Integer value;

        Base() {}

        public Integer getValue() {
            return value;
        }
    }

    public static class ValueOf extends Base {
        public static ValueOf valueOf(String value) {
            final ValueOf instance = new ValueOf();
            instance.value = Integer.valueOf(value);
            return instance;
        }
    }

    public static class FromString extends Base {
        public static FromString fromString(String value) {
            final FromString fromString = new FromString();
            fromString.value = Integer.valueOf(value);
            return fromString;
        }
    }

    public static class Parse extends Base {
        public static Parse parse(String value) {
            final Parse instance = new Parse();
            instance.value = Integer.valueOf(value);
            return instance;
        }
    }

    public static class ParseString extends Base {
        public static ParseString parseString(String value) {
            final ParseString instance = new ParseString();
            instance.value = Integer.valueOf(value);
            return instance;
        }
    }

    public static class Constructor extends Base {
        public Constructor(String value) {
            this.value = Integer.valueOf(value);
        }
    }

    public static class Uncompatible {}

}
