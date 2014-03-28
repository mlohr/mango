package net.mloehr.mango;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebTasks {

    @NonNull
    private DriveSupport support;

    public String getText(String xpath) {
        return support.forThis(xpath)
            .getText();
    }

    public void type(String xpath, String text) {
        support.forThis(xpath)
            .sendKeys(text);
    }

}
