package com.company.jmixpm.screen.task;

import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Task;

@UiController("Task_.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
@MultipleOpen
public class TaskBrowse extends StandardLookup<Task> {
}