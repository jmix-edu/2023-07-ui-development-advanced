package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.action.Action;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@UiController("Project.browse")
@UiDescriptor("project-browse.xml")
@LookupComponent("projectsTable")
public class ProjectBrowse extends StandardLookup<Project> {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private CollectionLoader<Project> projectsDl;

    @Subscribe("projectsTable.generateData")
    public void onProjectsTableGenerateData(final Action.ActionPerformedEvent event) {
        List<Project> projects = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            Project project = dataManager.create(Project.class);
            project.setName("Genetared " + i);
            project.setManager((User) currentAuthentication.getUser());

            projects.add(project);
        }

        dataManager.save(projects.toArray());
        projectsDl.load();
    }
}