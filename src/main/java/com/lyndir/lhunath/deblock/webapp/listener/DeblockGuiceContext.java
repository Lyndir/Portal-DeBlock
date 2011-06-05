/*
 *   Copyright 2010, Maarten Billemont
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.lyndir.lhunath.deblock.webapp.listener;

import com.google.common.collect.ImmutableList;
import com.lyndir.lhunath.deblock.webapp.DeblockWebApplication;
import com.lyndir.lhunath.portal.apps.model.*;
import com.lyndir.lhunath.portal.webapp.PortalWebApplication;
import com.lyndir.lhunath.portal.webapp.listener.PortalGuiceContext;
import java.util.Date;
import javax.servlet.ServletContextEvent;


/**
 * <h2>{@link DeblockGuiceContext}<br> <sub>[in short] (TODO).</sub></h2>
 *
 * <p> [description / usage]. </p>
 *
 * <p> <i>Feb 4, 2010</i> </p>
 *
 * @author lhunath
 */
public class DeblockGuiceContext extends PortalGuiceContext {

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {

        super.contextInitialized( servletContextEvent );

        // Register our application and its versions.
        App deblock = new App( "lhunath", "DeBlock", "deblock", new CodaSetSourceProvider(), new YouTrackIssueTracker(), "UA-90535-9" );
        deblock.setIssueTrackerName( "DBL" );
        String description = "<p class='short'>DeBlock is a block destruction game in the style of Bejuweled or Destruct-o-Block, " //
                             + "but unique altogether.</p>" //
                             + "<p>Each level is a field filled with blocks of different colors.<br />" //
                             + "The goal is to clear the field by destroying blocks that are sitting next to blocks of the same color.  " //
                             + "The more blocks you destroy at once, the higher your score gain.<br />" //
                             + "Remaining blocks collapse and you go again until the field is cleared or you're stuck with blocks that " //
                             + "have no links.</p>" //
                             + "<p>Then, there are special blocks, each with their own quirky effect.  Use them strategically and " //
                             + "beware of the dangerous ones!</p>";
        AppVersion.register(
                new AppVersion(
                        deblock, "100", "1.0", //
                        "“Mind-meltingly addictive.”", description, //
                        ImmutableList.of( //
                                          new Dependency(
                                                  "cocos2d-iphone (0.8.2-rc1)",
                                                  "http://code.google.com/p/cocos2d-iphone/source/browse/#svn/tags/release-0.8.2-rc1",
                                                  "http://cocos2d-iphone.googlecode.com/files/cocos2d-iphone-0.8.2-rc1.tar.gz" ), //
                                          new Dependency(
                                                  "iLibs", "http://github.com/lhunath/iLibs",
                                                  "http://github.com/lhunath/iLibs/downloads" ) ), //
                        new Date( 1265067646 * 1000L ), null/* TODO: YouTube video for 1.0 */, //
                        "Spellbinding levels", //
                        "Exciting for all ages", //
                        "Competitive online play", //
                        "Keeps track of multiple players", //
                        "Available in English, French, German, Russian, Spanish, Chinese and Japanese", //
                        "Open Source" //
                ) );
        AppVersion.register(
                new AppVersion(
                        deblock, "110", "1.1", //
                        "“Mind-meltingly addictive.”", description, //
                        ImmutableList.of( //
                                          new Dependency(
                                                  "cocos2d-iphone (0.8.2-rc1)",
                                                  "http://code.google.com/p/cocos2d-iphone/source/browse/#svn/tags/release-0.8.2-rc1",
                                                  "http://cocos2d-iphone.googlecode.com/files/cocos2d-iphone-0.8.2-rc1.tar.gz" ), //
                                          new Dependency(
                                                  "iLibs", "http://github.com/lhunath/iLibs",
                                                  "http://github.com/lhunath/iLibs/downloads" ) ), //
                        new Date( 1266963403 * 1000L ), null/* TODO: YouTube video for 1.1 */, //
                        "Scoreboard now shows and sorts by score ratio", //
                        "Improved scoreboard reloading" //
                ) );
        AppVersion.register(
                new AppVersion(
                        deblock, "120", "1.2", //
                        "“Mind-meltingly addictive.”", description, //
                        ImmutableList.of( //
                                          new Dependency(
                                                  "cocos2d-iphone (0.99.5-rc1)", "https://github.com/Lyndir/Cocos2D-iPhone/tree/c20c041",
                                                  "https://github.com/Lyndir/Cocos2D-iPhone/zipball/c20c041" ), //
                                          new Dependency(
                                                  "iLibs", "http://github.com/Lyndir/iLibs/tree/8431930",
                                                  "http://github.com/Lyndir/iLibs/downloads/zipball/8431930" ) ), //
                        new Date( 1304197596 * 1000L ), null/* TODO: YouTube video for 1.2 */, //
                        "Game Center integration: Scoring and user-specific level progress.", //
                        "Kids mode: No HUD to hit, no scoring or leveling, doesn't reset your own progress.", //
                        "PlayHaven integration" //
                ) );
    }

    @Override
    protected Class<? extends PortalWebApplication> getWebApplication() {

        return DeblockWebApplication.class;
    }
}
