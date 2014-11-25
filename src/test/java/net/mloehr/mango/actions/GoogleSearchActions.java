package net.mloehr.mango.actions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import lombok.val;
import net.mloehr.mango.Mapper;
import net.mloehr.mango.action.Action;
import net.mloehr.mango.action.Result;
import net.mloehr.mango.model.Results;

public class GoogleSearchActions {

	public static final String HEADER_BANNER = ".//*[@id='gb']/div[1]/div[1]";
	private static final String SEARCH_INPUT = ".//*[@id='sb_ifc0']//input[1]";
	private static final String SEARCH_BUTTON = ".//*[@id='gbqfba']";
	private static final String SEARCHLOOP_BUTTON = "//button[@name='btnG']";
	private static final String SEARCH_BUTTON_DIV = ".//*[@id='gbqfbw']";

	private static final String RESULT_HEADLINES = ".//*[@id='res']//div/h3";
	private static final String BILDER_LINK = ".//*[@id='hdtb_s']/div/div[2]/a";

	public Action search(String text) {
		return Action.withTasks().type(SEARCH_INPUT, "to-be-cleared").clear(SEARCH_INPUT)
				.type(SEARCH_INPUT, text).click(SEARCHLOOP_BUTTON);
	}

	public int getBrowserHeight() {
		val result = new Result();
		evalScript("return window.outerHeight", result);
		return ((Long) result.getValue()).intValue();
	}

	public int getBrowserWidth() {
		val result = new Result();
		evalScript("return window.outerWidth", result);
		return ((Long) result.getValue()).intValue();
	}

	protected Action evalScript(String script, Result result) {
		return Action.withTasks().eval(script, result);
	}

	public Action getSearchResults(Object results) {
		return Action.withTasks().mapText(RESULT_HEADLINES, results,
				new Mapper(results, "items", "(.*)"));
	}

	public Action executeOnButtons(String script) {
		return Action.withTasks().executeOnElement(SEARCH_BUTTON_DIV, script);
	}

	public Action clickOn(String xpath) {
		return Action.withTasks().click(xpath);
	}

	public Action testButtonDisplayed(Object displayed) {
		return Action.withTasks().testVisibility(SEARCHLOOP_BUTTON, displayed);
	}

	public Action getSearchButtonClass(StringBuilder data) {
		return Action.withTasks().getAttribute(SEARCH_BUTTON, "class", data);
	}

	public Action getBilderText(StringBuilder text) {
		return Action.withTasks().getText(BILDER_LINK, text);
	}

	public Results searchAndReturnResults(String searchTerm) {
		val results = new Results();
		search(searchTerm);

		StringBuilder displayed = new StringBuilder();
		testButtonDisplayed(displayed);
		assertThat(displayed.toString(), is("true"));

		getSearchResults(results);
		return results;
	}

	public BufferedImage loadBannerImage(String path) {
		List<BufferedImage> imgs = new ArrayList<BufferedImage>();
		loadImage(path, imgs);
		return imgs.get(0);
	}

	public BufferedImage getBannerImage() {
		List<BufferedImage> imgs = new ArrayList<BufferedImage>();
		getImage(HEADER_BANNER, imgs);
		return imgs.get(0);
	}

	public Action getImage(String xPath, List<BufferedImage> images) {
		return Action.withTasks().getImage(xPath, images);
	}

	public Action loadImage(String path, List<BufferedImage> images) {
		return Action.withTasks().loadImage(path, images);
	}

}