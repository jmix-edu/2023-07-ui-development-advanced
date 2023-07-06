package com.company.jmixpm.screen.task;

import com.company.jmixpm.app.TaskService;
import com.company.jmixpm.entity.User;
import com.company.jmixpm.screen.blank.BlankScreen;
import io.jmix.core.LoadContext;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Task_.edit")
@UiDescriptor("task-edit.xml")
@EditedEntityContainer("taskDc")
@DialogMode(width = "AUTO", height = "AUTO", forceDialog = true)
public class TaskEdit extends StandardEditor<Task> {
}