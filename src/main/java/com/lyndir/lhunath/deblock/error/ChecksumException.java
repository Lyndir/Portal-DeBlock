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
package com.lyndir.lhunath.deblock.error;

import com.lyndir.lhunath.deblock.data.GameMode;
import com.lyndir.lhunath.deblock.util.DeblockConstants;


/**
 * <h2>{@link ChecksumException}<br>
 * <sub>[in short] (TODO).</sub></h2>
 *
 * <p>
 * [description / usage].
 * </p>
 *
 * <p>
 * <i>Nov 6, 2009</i>
 * </p>
 *
 * @author lhunath
 */
public class ChecksumException extends AuthenticationException {

    private final String checksum;
    private final GameMode mode;
    private final Integer level;
    private final Integer score;
    private final Long achievedTimeStamp;


    /**
     * Create a new {@link ChecksumException} instance.
     *
     * @param message           The problem description.
     * @param cause             The exception that caused this problem. (optional)
     * @param errorHeader       The value of the {@link DeblockConstants#ERROR_HEADER} that should be set as a result of this
     *                          exception.
     * @param checksum          The checksum that the player's client submitted for the data.
     * @param name              The name of the player that was trying to authenticate.
     * @param mode              The game mode that the score was achieved in.
     * @param level             The game level that the score was achieved in.
     * @param score             The score that the player had achieved.
     * @param achievedTimeStamp The time in milliseconds since the UNIX epoch.
     */
    public ChecksumException(
            final String message, final Throwable cause, final String errorHeader,
            final String checksum, final String name, final GameMode mode, final Integer level,
            final Integer score, final Long achievedTimeStamp) {

        super( message, cause, errorHeader, name, null );

        this.mode = mode;
        this.level = level;
        this.score = score;
        this.checksum = checksum;
        this.achievedTimeStamp = achievedTimeStamp;
    }

    /**
     * @return The checksum of this {@link ChecksumException}.
     */
    public String getChecksum() {

        return checksum;
    }

    /**
     * @return The mode of this {@link ChecksumException}.
     */
    public GameMode getMode() {

        return mode;
    }

    /**
     * @return The level of this {@link ChecksumException}.
     */
    public Integer getLevel() {

        return level;
    }

    /**
     * @return The score of this {@link ChecksumException}.
     */
    public Integer getScore() {

        return score;
    }

    /**
     * @return The achievedTimeStamp of this {@link ChecksumException}.
     */
    public Long getAchievedTimeStamp() {

        return achievedTimeStamp;
    }
}
