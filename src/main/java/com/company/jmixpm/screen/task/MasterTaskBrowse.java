package com.company.jmixpm.screen.task;

import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Task;

@UiController("MasterTask_.browse")
@UiDescriptor("master-task-browse.xml")
@LookupComponent("table")
@Route(path = "mastertasks")
public class MasterTaskBrowse extends MasterDetailScreen<Task> {
}