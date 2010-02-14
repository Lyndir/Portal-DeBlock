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
package com.lyndir.lhunath.deblock.service.impl;

import java.util.Date;

import com.lyndir.lhunath.deblock.data.GameMode;
import com.lyndir.lhunath.deblock.data.PlayerEntity;
import com.lyndir.lhunath.deblock.data.ScoreEntity;
import com.lyndir.lhunath.deblock.service.ScoreService;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link ScoreServiceImpl}<br>
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
public class ScoreServiceImpl implements ScoreService {

    private static final Logger logger                     = Logger.get( ScoreServiceImpl.class );
    private static final long   FUTURE_SCORE_TIME_FRAME_MS = 5 /* min */* 60 /* s */* 1000 /* ms */;


    public ScoreServiceImpl() {

    }

    /**
     * {@inheritDoc}
     */
    public ScoreEntity addScore(PlayerEntity player, GameMode mode, Integer level, Integer score, Date date) {

        Date now = new Date();
        if (score == null)
            throw logger.wrn( "No score specified to add to player." ) //
                        .toError( IllegalArgumentException.class );
        if (date == null)
            throw logger.wrn( "No date specified or score to add to player." ) //
                        .toError( IllegalArgumentException.class );
        if (date.getTime() - now.getTime() > FUTURE_SCORE_TIME_FRAME_MS)
            throw logger.wrn( "Can't add scores achieved in the future (given > allowed: %+d ms > +%+d ms).", //
                    date.getTime() - now.getTime(), FUTURE_SCORE_TIME_FRAME_MS ) //
                        .toError( IllegalArgumentException.class );

        return new ScoreEntity( player, mode, level, score, date );
    }
}
