package com.company.jmixpm.screen.userinfo;

import com.company.jmixpm.app.PostService;
import com.company.jmixpm.entity.UserInfo;
import com.google.common.collect.ImmutableMap;
import io.jmix.core.LoadContext;
import io.jmix.ui.action.Action;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.navigation.UrlIdSerializer;
import io.jmix.ui.navigation.UrlParamsChangedEvent;
import io.jmix.ui.navigation.UrlRouting;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("UserInfoScreen")
@UiDescriptor("user-info-screen.xml")
@Route(path = "user-info")
public class UserInfoScreen extends Screen {

    @Autowired
    private PostService postService;
    @Autowired
    private UrlRouting urlRouting;

    private Long userId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Install(to = "userInfoDl", target = Target.DATA_LOADER)
    private UserInfo userInfoDlLoadDelegate(LoadContext<UserInfo> loadContext) {
        // Here you can load entity from an external store by ID passed in LoadContext
        return postService.fetchUserInfo(userId);
    }

    @Subscribe("windowClose")
    public void onWindowClose(final Action.ActionPerformedEvent event) {
        closeWithDefaultAction();
    }

    @Subscribe
    public void onAfterShow(final AfterShowEvent event) {
        String id = UrlIdSerializer.serializeId(userId);
        urlRouting.replaceState(this, ImmutableMap.of("id", id));
    }

    @Subscribe
    public void onUrlParamsChanged(final UrlParamsChangedEvent event) {
        String id = event.getParams().get("id");
        userId = (Long) UrlIdSerializer.deserializeId(Long.class, id);
    }


}