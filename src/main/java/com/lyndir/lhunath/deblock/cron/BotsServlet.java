/*
 *   Copyright 2009, Maarten Billemont
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
package com.lyndir.lhunath.deblock.cron;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lyndir.lhunath.deblock.data.Bot;
import com.lyndir.lhunath.deblock.data.PlayerEntity;
import com.lyndir.lhunath.deblock.data.ScoreEntity;
import com.lyndir.lhunath.deblock.data.util.EMF;
import com.lyndir.lhunath.deblock.service.PlayerService;
import com.lyndir.lhunath.deblock.service.ScoreService;
import com.lyndir.lhunath.deblock.util.DeblockProperties;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link BotsServlet}<br>
 * <sub>[in short] (TODO).</sub></h2>
 * 
 * <p>
 * [description / usage].
 * </p>
 * 
 * <p>
 * <i>Nov 4, 2009</i>
 * </p>
 * 
 * @author lhunath
 */
public class BotsServlet extends HttpServlet {

    static final Logger logger = Logger.get( BotsServlet.class );

    private Random      random = new Random();


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType( "text/plain;charset=UTF-8" );

            for (Bot bot : Bot.values())
                updateBot( response, bot );

            EMF.closeEm( true );
        }

        finally {
            // If the entity manager is still open the logic was interrupted early and must have failed.
            EMF.closeEm( false );
        }
    }

    private void updateBot(HttpServletResponse response, Bot bot)
            throws IOException {

        long currentTimeMillis = System.currentTimeMillis();
        long dayTimeMillis = 1000 /* ms */* 3600 /* s */* 24 /* h */;
        PlayerEntity botEntity = PlayerService.get().getBot( bot.getName() );

        // Determine the bot's newly achieved score based on his base score, luck and experience.
        float registeredDays = (currentTimeMillis - botEntity.getRegistered().getTime()) / dayTimeMillis;
        float botScoreGain = DeblockProperties.get().getBotScoreGain();
        if (random.nextFloat() < bot.getLuck())
            registeredDays *= DeblockProperties.get().getBotLuckGain();
        int newScore = (int) (random.nextFloat() * bot.getBaseScore() * (1 + registeredDays * botScoreGain));

        // Record a new score for the bot achieved at the random time span since now.
        Date achievedDate = new Date( (long) (currentTimeMillis - random.nextFloat() * dayTimeMillis) );
        ScoreEntity newScoreEntity = ScoreService.get().addScore( botEntity, newScore, achievedDate );
        botEntity.getScores().add( newScoreEntity );

        // Save player (and scores).
        if (PlayerService.get().save( botEntity ))
            response.getWriter().write(
                                        String.format( "[*] Successfully updated score for bot %s to %d.\n",
                                                       botEntity.getName(), newScoreEntity.getScore() ) );
        else
            response.getWriter().write(
                                        String.format( "[!] Failed updating score for bot %s to %d.\n",
                                                       botEntity.getName(), newScoreEntity.getScore() ) );
    }
}
