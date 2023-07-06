package com.company.jmixpm.screen.user;

import com.company.jmixpm.entity.User;
import io.jmix.ui.Dialogs;
import io.jmix.ui.Notifications;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.Action;
import io.jmix.ui.app.inputdialog.DialogActions;
import io.jmix.ui.app.inputdialog.DialogOutcome;
import io.jmix.ui.app.inputdialog.InputParameter;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.TextArea;
import io.jmix.ui.executor.BackgroundTask;
import io.jmix.ui.executor.BackgroundTaskHandler;
import io.jmix.ui.executor.BackgroundWorker;
import io.jmix.ui.executor.TaskLifeCycle;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@UiController("User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@Route("users")
public class UserBrowse extends StandardLookup<User> {

    @Autowired
    private Dialogs dialogs;
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private GroupTable<User> usersTable;
    @Autowired
    private CollectionContainer<User> usersDc;
    @Autowired
    private Notifications notifications;
    @Autowired
    private BackgroundWorker backgroundWorker;

    @Subscribe("usersTable.sendEmail")
    public void onUsersTableSendEmail(final Action.ActionPerformedEvent event) {
        dialogs.createInputDialog(this)
                .withCaption(messageBundle.getMessage("dialog.caption"))
                .withParameters(
                        InputParameter.stringParameter("title")
                                .withCaption(messageBundle.getMessage("dialog.title.caption"))
                                .withRequired(true),
                        InputParameter.stringParameter("body")
                                .withField(() -> {
                                            TextArea<String> textArea = uiComponents.create(TextArea.NAME);
                                            textArea.setCaption("Body");
                                            textArea.setRequired(true);
                                            textArea.setWidthFull();

                                            return textArea;
                                        }
                                )
                )
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        String title = closeEvent.getValue("title");
                        String body = closeEvent.getValue("body");

                        Set<User> selected = usersTable.getSelected();
                        Collection<User> users = selected.isEmpty()
                                ? usersDc.getItems()
                                : selected;

                        doSendEmail(title, body, users);
                    }
                })
                .show();
    }

    private void doSendEmail(String title, String body, Collection<User> users) {
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        EmailTask emailTask = new EmailTask(title, body, users);
        BackgroundTaskHandler<Void> handle = backgroundWorker.handle(emailTask);
        handle.execute();

        //Application stop
//        handle.getResult();


//        dialogs.createBackgroundWorkDialog(this, emailTask)
//                .withCaption("Sending")
//                .withMessage("Progress....")
//                .withTotal(users.size())
//                .withShowProgressInPercentage(true)
//                .withCancelAllowed(true)
//                .show();
    }

    private class EmailTask extends BackgroundTask<Integer, Void> {

        private String title;
        private String body;
        private Collection<User> users;

        public EmailTask(String title, String body, Collection<User> users) {
            super(10, TimeUnit.MINUTES, UserBrowse.this);

            this.title = title;
            this.body = body;
            this.users = users;
        }


        @Override
        public Void run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {
            int i = 0;
            for (User user : users) {
                if (taskLifeCycle.isCancelled()) {
                    break;
                }

                TimeUnit.SECONDS.sleep(2);
                i++;
                taskLifeCycle.publish(i);
            }

            return null;
        }

        @Override
        public void done(Void result) {
            notifications.create()
                    .withCaption("Email has been sent")
                    .withType(Notifications.NotificationType.TRAY)
                    .show();
        }
    }
}