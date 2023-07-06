package com.company.jmixpm.screen.post;

import com.company.jmixpm.app.PostService;
import com.company.jmixpm.entity.Post;
import com.company.jmixpm.screen.userinfo.UserInfoScreen;
import io.jmix.core.LoadContext;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Table;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("PostScreen")
@UiDescriptor("post-screen.xml")
@Route(path = "posts")
public class PostScreen extends Screen {


    @Autowired
    private PostService postService;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private Table<Post> postsTable;
    @Autowired
    private Notifications notifications;

    @Install(to = "postsDl", target = Target.DATA_LOADER)
    private List<Post> postsDlLoadDelegate(final LoadContext<Post> loadContext) {
        return postService.fetchPosts(loadContext.getQuery().getFirstResult(),
                loadContext.getQuery().getMaxResults());
    }

    @Subscribe("postsTable.userInfo")
    public void onPostsTableUserInfo(final Action.ActionPerformedEvent event) {
        Post singleSelected = postsTable.getSingleSelected();
        if (singleSelected == null) {
            return;
        }

        UserInfoScreen infoScreen = screenBuilders.screen(this)
                .withScreenClass(UserInfoScreen.class)
                .withOpenMode(OpenMode.DIALOG)
                .build();

        infoScreen.setUserId(singleSelected.getUserId());
        infoScreen.show();

        notifications.create()
                .withCaption("Caption")
                .withDescription("Description")
                .withPosition(Notifications.Position.BOTTOM_RIGHT)
                .show();
    }

    @Install(to = "pagination", subject = "totalCountDelegate")
    private Integer paginationTotalCountDelegate() {
        return postService.postsTotalCount();
    }


}