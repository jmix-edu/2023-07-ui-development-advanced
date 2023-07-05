package com.company.jmixpm.screen.blank;

import io.jmix.ui.component.Button;
import io.jmix.ui.screen.*;

@UiController("BlankScreen")
@UiDescriptor("blank-screen.xml")
public class BlankScreen extends Screen {

    @Subscribe("close")
    public void onCloseClick(final Button.ClickEvent event) {
        close(StandardOutcome.COMMIT);
    }
}