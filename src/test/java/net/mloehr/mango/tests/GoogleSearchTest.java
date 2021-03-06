package net.mloehr.mango.tests;

import static net.mloehr.mango.Matchers.containsMatch;
import static net.mloehr.mango.Matchers.matchesImageFile;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import lombok.val;
import net.mloehr.mango.BaseTest;
import net.mloehr.mango.XPathNotFoundException;
import net.mloehr.mango.actions.GoogleSearchActions;
import net.mloehr.mango.selenium.WebUser;

import org.junit.Test;
import org.openqa.selenium.ElementNotVisibleException;
import static net.mloehr.mango.Matchers.matchesImage;

public class GoogleSearchTest extends BaseTest {

	private final static String GOOGLE = "https://www.google.de/";

	@Test
	public void shouldSearchResults() throws Exception {
		StringBuilder displayed;

		webUser = new WebUser(GOOGLE, "mango.execution-tracer=false;");
		assertThat(webUser.getCurrentUrl(), is(GOOGLE));

		displayed = new StringBuilder();
		on(googleSearch()).testButtonDisplayed(displayed);
		assertThat(displayed.toString(), is("false"));

		val results = on(googleSearch()).searchAndReturnResults("hello");
		webUser.refreshPage();
		assertThat(results.getItems(), everyItem(containsMatch("(?i)hello")));
	}

	@Test
	public void shouldExpectDifferentImages() throws Exception {
		webUser = new WebUser(GOOGLE, "mango.execution-tracer=true;");
		assertThat(webUser.getCurrentUrl(), is(GOOGLE));
		webUser.saveElementImage(GoogleSearchActions.HEADER_BANNER, "google_btn1.png");
		webUser.quit();

		webUser = new WebUser(GOOGLE, "mango.execution-tracer=false;");
		assertThat(webUser.getCurrentUrl(), is(GOOGLE));

		assertThat(on(googleSearch()).loadBannerImage("google_btn1.png"),
				not(matchesImage(on(googleSearch()).getBannerImage())));

		webUser.saveElementImage(GoogleSearchActions.HEADER_BANNER, "google_btn2.png");

		assertThat(on(googleSearch()).loadBannerImage("google_btn2.png"),
				matchesImage(on(googleSearch()).getBannerImage()));
		assertThat("google_btn1.png", not(matchesImageFile("google_btn2.png")));
		assertThat("google_btn1.png", matchesImageFile("google_btn1.png"));
		assertThat("google_btn2.png", matchesImageFile("google_btn2.png"));
	}

	@Test
	public void shouldGetAttribute() throws Exception {
		val classText = new StringBuilder();
		webUser = new WebUser(GOOGLE);
		on(googleSearch()).getSearchButtonClass(classText);
		assertThat(classText.toString(), is("gbqfba"));
	}

	@Test
	public void shouldGetText() throws Exception {
		val text = new StringBuilder();
		webUser = new WebUser(GOOGLE + "?hl=de");
		on(googleSearch()).search("hello");
		on(googleSearch()).getBilderText(text);
		assertThat(text.toString(), is("Bilder"));
	}

	@Test
	public void shouldHideGoogleLogoAndTestJavaScriptExecution() throws Exception {
		webUser = new WebUser(GOOGLE);
		on(googleSearch()).executeOnButtons("arguments[0].style.display = 'none';");
		try {
			on(googleSearch()).search("hello");
			fail("Missed Exception, should not happen!");
		} catch (Exception e) {
			assertThat(e, instanceOf(ElementNotVisibleException.class));
		}
	}

	@Test
	public void shouldNotFindNonExistingElement() throws Exception {
		webUser = new WebUser(GOOGLE, "mango.timeout=1;");
		try {
			on(googleSearch()).clickOn("./*[@id='thisIsNotExisting']");
			fail("Missed Exception, should not happen!");
		} catch (Exception e) {
			assertThat(e, instanceOf(XPathNotFoundException.class));
		}
	}

	public static Class<GoogleSearchActions> googleSearch() {
		return GoogleSearchActions.class;
	}

}
