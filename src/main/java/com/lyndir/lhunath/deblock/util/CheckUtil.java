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

import java.text.MessageFormat;

import com.lyndir.lhunath.deblock.error.ChecksumException;
import com.lyndir.lhunath.lib.system.Utils;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link CheckUtil}<br>
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
public abstract class CheckUtil {

    private static final Logger logger = Logger.get( CheckUtil.class );


    public static void assertValidChecksum(String name, Integer score, String checksum)
            throws ChecksumException {

        if (name == null || score == null || checksum == null)
            throw logger.wrn( "Missing checksum or checksum components." ) //
            .toError( ChecksumException.class, name, score, checksum );

        String salt = DeblockProperties.get().getSalt();
        String correctChecksum = Utils.getMD5( MessageFormat.format( "{0}:{1}:{2}", name, score, salt ) );

        if (!correctChecksum.equalsIgnoreCase( checksum ))
            throw logger.wrn( "Incorrect checksum." ) //
            .toError( ChecksumException.class, name, score, checksum );
    }
}
