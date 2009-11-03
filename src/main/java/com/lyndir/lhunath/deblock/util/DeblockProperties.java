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

    private static final DeblockProperties instance      = new DeblockProperties();
    private static final Logger            logger        = Logger.get( DeblockProperties.class );

    private static final String            SALT_PROPERTY = "salt";

    private Properties                     properties    = new Properties();


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

    public String getSalt() {

        return getProperty( SALT_PROPERTY );
    }
}
