package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.Task;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.TabSheet;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Project.edit")
@UiDescriptor("project-edit.xml")
@EditedEntityContainer("projectDc")
public class ProjectEdit extends StandardEditor<Project> {


    @Autowired
    private DataManager dataManager;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private Notifications notifications;

    private Table<Task> tasksTable;
    @Autowired
    private CollectionLoader<Task> tasksDl;

    @Subscribe("tabSheet")
    public void onTabSheetSelectedTabChange(final TabSheet.SelectedTabChangeEvent event) {
        if ("tasksTab".equals(event.getSelectedTab().getName())) {
            initTable();
        }
    }

    private void initTable() {
        if (tasksTable != null) {
            return;
        }

        tasksTable = (Table<Task>) getWindow().getComponentNN("tasksTable");
        ((BaseAction) tasksTable.getActionNN("create"))
                .addActionPerformedListener(this::onTasksTableCreate);
        ((BaseAction) tasksTable.getActionNN("edit"))
                .addActionPerformedListener(this::onTasksTableEdit);

        tasksDl.setParameter("project", getEditedEntity());
        tasksDl.load();
    }

    //    @Subscribe("tasksTable.create")
    public void onTasksTableCreate(final Action.ActionPerformedEvent event) {
        Task newTask = dataManager.create(Task.class);
        newTask.setProject(getEditedEntity());

        screenBuilders.editor(tasksTable)
                .newEntity(newTask)
                .withParentDataContext(getScreenData().getDataContext())
                .show();
    }

    //    @Subscribe("tasksTable.edit")
    public void onTasksTableEdit(final Action.ActionPerformedEvent event) {
        Task task = tasksTable.getSingleSelected();
        if (task == null) {
            return;
        }

        screenBuilders.editor(tasksTable)
                .editEntity(task)
                .withParentDataContext(getScreenData().getDataContext())
                .show();
    }

    @Subscribe(id = "tasksDc", target = Target.DATA_CONTAINER)
    public void onTasksDcCollectionChange(final CollectionContainer.CollectionChangeEvent<Task> event) {
        notifications.create()
                .withCaption("[tasksDc] CollectionChangeEvent")
                .withDescription(String.valueOf(event.getChangeType()))
                .withType(Notifications.NotificationType.TRAY)
                .show();
    }


//    @Subscribe
//    public void onInitEntity(final InitEntityEvent<Project> event) {
//        tasksTable.setEnabled(false);
//    }

//    @Install(to = "tasksTable.create", subject = "initializer")
//    private void tasksTableCreateInitializer(final Task task) {
//        task.setProject(getEditedEntity());
//    }

//    @Subscribe(target = Target.DATA_CONTEXT)
//    public void onPostCommit(final DataContext.PostCommitEvent event) {
//        tasksTable.setEnabled(true);
//    }


}