package net.mloehr.mango;

import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matchers that use regular expressions
 *
 * @author t.wood
 */
@Slf4j
public class Matchers {
    private static abstract class AbstractRegexpMatcher extends
            TypeSafeMatcher<String> {
        protected final String regex;
        protected final Pattern compiledRegex;

        private AbstractRegexpMatcher(final String regex) {
            this.regex = regex;
            compiledRegex = Pattern.compile(regex);
        }
    }

    private static class MatchesRegexpMatcher extends AbstractRegexpMatcher {
        private MatchesRegexpMatcher(final String regex) {
            super(regex);
        }

        @Override
        public boolean matchesSafely(final String item) {
            return compiledRegex.matcher(item).matches();
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("matches regex ").appendValue(regex);
        }
    }

    private static class ContainsMatchRegexpMatcher extends
            AbstractRegexpMatcher {
        private ContainsMatchRegexpMatcher(final String regex) {
            super(regex);
        }

        @Override
        public boolean matchesSafely(final String item) {
            return compiledRegex.matcher(item).find();
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("contains match for regex ").appendValue(
                    regex);
        }
    }

    private static class MatchesImageMatcher extends TypeSafeMatcher<BufferedImage> {
        protected final BufferedImage reference;

        private MatchesImageMatcher(final BufferedImage reference) {
        	this.reference = reference;
        }

        @Override
        public boolean matchesSafely(final BufferedImage source) {
        	log.debug("source {}", source);
            return ImageComparer.compareImages(source, reference) == 0; 
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("matches image ").appendValue(reference);
        }
    }

    public static Matcher<BufferedImage> matchesImage(final BufferedImage reference) {
        return new MatchesImageMatcher(reference);
    }

    private static class MatchesImageFileMatcher extends TypeSafeMatcher<String> {
        protected final String reference;

        private MatchesImageFileMatcher(final String reference) {
        	this.reference = reference;
        }

        @Override
        public boolean matchesSafely(final String source) {
        	log.debug("source {}", source);
            return ImageComparer.compareFiles(source, reference) == 0; 
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("matches image ").appendValue(reference);
        }
    }

    public static Matcher<String> matchesImageFile(final String reference) {
        return new MatchesImageFileMatcher(reference);
    }

    /**
     * Match the regexp against the whole input string
     *
     * @param regex
     *            the regular expression to match
     *
     * @return a matcher which matches the whole input string
     */
    public static Matcher<String> matches(final String regex) {
        return new MatchesRegexpMatcher(regex);
    }

    /**
     * Match the regexp against any substring of the input string
     *
     * @param regex
     *            the regular expression to match
     *
     * @return a matcher which matches anywhere in the input string
     */
    public static Matcher<String> containsMatch(final String regex) {
        return new ContainsMatchRegexpMatcher(regex);
    }
}