package com.company.jmixpm.screen.maintopmenu;

import com.company.jmixpm.app.TaskService;
import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.Task;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.ScreenTools;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiControllerUtils;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@UiController("MainTop")
@UiDescriptor("main-screen-top-menu.xml")
@Route(path = "main", root = true)
public class MainScreenTopMenu extends Screen implements Window.HasWorkArea {

    @Autowired
    private ScreenTools screenTools;

    @Autowired
    private AppWorkArea workArea;
    @Autowired
    private EntityComboBox<Project> projectField;
    @Autowired
    private TextField<String> nameField;
    @Autowired
    private DateField<LocalDateTime> dateField;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CollectionLoader<Task> tasksDl;
    @Autowired
    private ScreenBuilders screenBuilders;

    @Override
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        screenTools.openDefaultScreen(
                UiControllerUtils.getScreenContext(this).getScreens());

        screenTools.handleRedirect();
    }

    @Subscribe("addTask")
    public void onAddTask(final Action.ActionPerformedEvent event) {
        Project project = projectField.getValue();
        String name = nameField.getValue();
        LocalDateTime dateTime = dateField.getValue();

        taskService.createTask(project, name, dateTime);

        projectField.clear();
        nameField.clear();
        dateField.clear();

        tasksDl.load();
    }

    @Subscribe("refresh")
    public void onRefresh(final Action.ActionPerformedEvent event) {
        getScreenData().loadAll();
    }

    @Subscribe("calendar")
    public void onCalendarCalendarEventClick(final Calendar.CalendarEventClickEvent<Task> event) {
        Screen screen = screenBuilders.editor(Task.class, this)
                .editEntity((Task) event.getEntity())
                .build();

        screen.addAfterCloseListener(e -> tasksDl.load());
        screen.show();
    }
}