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

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link DeblockProperties}<br>
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
public class DeblockProperties {

    private static final DeblockProperties instance       = new DeblockProperties();
    private static final Logger            logger         = Logger.get( DeblockProperties.class );

    private static final String            SALT_PROPERTY  = "salt";
    private static final String            BOT_SCORE_GAIN = "botScoreGain";
    private static final String            BOT_LUCK_GAIN  = "botLuckGain";

    private Properties                     properties     = new Properties();


    public static DeblockProperties get() {

        return instance;
    }

    private DeblockProperties() {

        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            properties.loadFromXML( contextClassLoader.getResourceAsStream( "deblock.xml" ) );
        }

        catch (InvalidPropertiesFormatException e) {
            throw logger.bug( e ).toError();
        } catch (IOException e) {
            throw logger.bug( e ).toError();
        }
    }

    private String getProperty(String key) {

        String value = properties.getProperty( key );
        if (value == null)
            throw logger.bug( "Missing property: %s", key ).toError();

        return value;
    }

    /**
     * @return The secret salt for score submission checksums.
     */
    public String getSalt() {

        return getProperty( SALT_PROPERTY );
    }

    /**
     * The score gain is defined as follows:
     * 
     * <p>
     * <i>The fraction of the bot's experience (days since registration) that boost the bot's maximum score for the day
     * by his base score.</i>
     * </p>
     * 
     * <p>
     * Thus, a score gain of <code>0.5</code> means that a bot with a base score of <code>100</code> that has been
     * playing for <code>10</code> days can end his day with a score of
     * <code>100 + 10 * 0.5 * 100 = 100 + 500 = 600</code>, while a bot with a base score of <code>500</code> that has
     * been playing for <code>62</code> days (two months) can end his day with a score of
     * <code>500 + 62 * 0.5 * 500 = 500 + 15500 = 16000</code>.
     * </p>
     * 
     * @return The amount of score bots gain per day.
     */
    public float getBotScoreGain() {

        return Float.parseFloat( getProperty( BOT_SCORE_GAIN ) );
    }

    /**
     * The luck gain is defined as follows:
     * 
     * <p>
     * <i>The amount with which to multiply the score gain of a bot when he has a lucky day.</i>
     * </p>
     * 
     * @return The multiplier that is applied to a lucky bot's score gain.
     */
    public float getBotLuckGain() {

        return Float.parseFloat( getProperty( BOT_LUCK_GAIN ) );
    }
}
