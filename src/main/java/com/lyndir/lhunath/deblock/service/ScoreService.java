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
package com.lyndir.lhunath.deblock.service;

import java.util.Date;
import java.util.List;

import com.lyndir.lhunath.deblock.data.GameMode;
import com.lyndir.lhunath.deblock.data.PlayerEntity;
import com.lyndir.lhunath.deblock.data.ScoreEntity;


/**
 * <h2>{@link ScoreService}<br>
 * <sub>[in short] (TODO).</sub></h2>
 *
 * <p>
 * [description / usage].
 * </p>
 *
 * <p>
 * <i>Jan 16, 2010</i>
 * </p>
 *
 * @author lhunath
 */
public interface ScoreService {

    /**
     * Add a new score value to the given player's profile.
     *
     * @param player The player that achieved the given score.
     * @param mode   The game mode in which the score was achieved.
     * @param level  The level in which the score was achieved.
     * @param score  The score value that was achieved.
     * @param date   The date that the player achieved the given score.
     *
     * @return The {@link ScoreEntity} that records the given score for the given player.
     */
    public ScoreEntity addScore(PlayerEntity player, GameMode mode, Integer level, Integer score, Date date);

    /**
     * @param playerEntity The player in whose context scores should be returned.
     *
     * @return A list of scores that revolve around the top score of the given player.
     */
    public List<ScoreEntity> getScoresForPlayer(PlayerEntity playerEntity);
}
