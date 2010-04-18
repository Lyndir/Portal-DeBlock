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
package com.lyndir.lhunath.deblock.util;

import com.lyndir.lhunath.deblock.data.GameMode;
import com.lyndir.lhunath.deblock.error.ChecksumException;
import com.lyndir.lhunath.lib.system.logging.Logger;
import com.lyndir.lhunath.lib.system.util.Utils;


/**
 * <h2>{@link CheckUtil}<br>
 * <sub>Checksum validation utilities.</sub></h2>
 *
 * <p>
 * A checksum is a string that is used to verify that the checked data was sent by the client application.
 * </p>
 *
 * <p>
 * What we're technically checking is whether the submitter has access to the checksum secret salt, which is a part of
 * the checksum. The checksum is built up of three components:
 * </p>
 *
 * <code>[Salt (string)]</code>:<code>[Player Name (string)]</code>:<code>[Score (integer)]</code>:
 * <code>[Timestamp Score (ms since UNIX Epoch) (long)]</code>
 *
 * <p>
 * When then submitter knows these three components it can generate the checksum (the MD5 hash of the above string). It
 * submits the checksum along with the score submission request. The server rebuilds the checksum from the given player
 * name and score. The server also knows the secret salt. When the server's rebuilt checksum matches the checksum given
 * by the client, it can assume the client controls the secret salt. Under this assumption, it assumes the client is the
 * game client and not someone malicious.
 * </p>
 *
 * <p>
 * This method is not 100% infallible. An attacker needs to figure out the secret salt in order to obtain privileged
 * access to submit any score for any player for whom he has the password (likely just himself).
 * </p>
 *
 * <p>
 * <b> Anyone with enough time on their hands to waste on childish endeavours to figure out an iPhone game's secret salt
 * ought to go find something remotely useful to do with their life instead. Suspicious scores will obviously be
 * investigated and removed.</b>
 * </p>
 *
 * <p>
 * <i>Oct 29, 2009</i>
 * </p>
 *
 * @author lhunath
 */
public abstract class CheckUtil {

    private static final Logger logger = Logger.get( CheckUtil.class );


    public static void assertValidChecksum(
            final String checksum, final String name, final GameMode mode,
            final Integer level, final Integer score, final Long achievedTimeStamp)
            throws ChecksumException {

        if (checksum == null || name == null || mode == null || level == null || score == null
            || achievedTimeStamp == null)
            throw logger.wrn(
                    "Missing checksum or checksum data (check %s, name %s, mode %s, level %s, score %s, date %s).", //
                    checksum, name, mode, level, score, achievedTimeStamp )
                    //
                    .toError( ChecksumException.class, DeblockConstants.ERROR_MISSING_CHECK, name, mode, level,
                              score, checksum );

        String salt = DeblockProperties.get().getSalt();
        String correctChecksum = Utils.getMD5( String.format(
                "%s:%s:%d:%d:%d:%d", //
                salt, name, mode.ordinal(), level, score,
                achievedTimeStamp ) );

        if (!correctChecksum.equalsIgnoreCase( checksum ))
            throw logger.wrn( "Incorrect checksum." ) //
                    .toError( ChecksumException.class, DeblockConstants.ERROR_INCORRECT_CHECK, checksum, name,
                              score, achievedTimeStamp );
    }
}
