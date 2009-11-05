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
package com.lyndir.lhunath.deblock.service;

import java.util.Date;

import com.lyndir.lhunath.deblock.data.PlayerEntity;
import com.lyndir.lhunath.deblock.data.ScoreEntity;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link ScoreService}<br>
 * <sub>[in short] (TODO).</sub></h2>
 * 
 * <p>
 * [description / usage].
 * </p>
 * 
 * <p>
 * <i>Oct 29, 2009</i>
 * </p>
 * 
 * @author lhunath
 */
public class ScoreService {

    private static final Logger       logger   = Logger.get( ScoreService.class );
    private static final ScoreService instance = new ScoreService();


    public static ScoreService get() {

        return instance;
    }

    private ScoreService() {

    }

    /**
     * Add a new score value to the given player's profile.
     * 
     * @param player
     *            The player that anchieved the given score.
     * @param score
     *            The score value that was achieved.
     * @param date
     *            The date that the player achieved the given score.
     * 
     * @return The {@link ScoreEntity} that records the given score for the given player.
     */
    public ScoreEntity addScore(PlayerEntity player, Integer score, Date date) {

        if (score == null)
            throw logger.err( "No score specified to add to player." ).toError( IllegalArgumentException.class );
        if (date == null)
            throw logger.err( "No date specified or score to add to player." ).toError( IllegalArgumentException.class );
        if (date.after( new Date() ))
            throw logger.err( "Can't add scores achieved in the future." ).toError( IllegalArgumentException.class );

        return new ScoreEntity( player, score, date );
    }
}
