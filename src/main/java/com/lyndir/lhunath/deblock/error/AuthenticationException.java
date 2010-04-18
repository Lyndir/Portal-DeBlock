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

import com.lyndir.lhunath.deblock.util.DeblockConstants;


/**
 * <h2>{@link AuthenticationException}<br>
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
public class AuthenticationException extends Exception {

    private final String name;
    private final String password;
    private final String errorHeader;


    /**
     * Create a new {@link AuthenticationException} instance.
     *
     * @param message     The problem description.
     * @param cause       The exception that caused this problem. (optional)
     * @param errorHeader The value of the {@link DeblockConstants#ERROR_HEADER} that should be set as a result of this
     *                    exception.
     * @param name        The name of the player that was trying to authenticate.
     * @param password    The password that the player was trying to authenticate with.
     */
    public AuthenticationException(
            final String message, final Throwable cause, final String errorHeader,
            final String name, final String password) {

        super( message, cause );

        this.name = name;
        this.password = password;
        this.errorHeader = errorHeader;
    }

    /**
     * @return The name of this {@link AuthenticationException}.
     */
    public String getName() {

        return name;
    }

    /**
     * @return The password of this {@link AuthenticationException}.
     */
    public String getPassword() {

        return password;
    }

    /**
     * @return The value of the {@link DeblockConstants#ERROR_HEADER} that should be set as a result of this exception.
     *         Describes its cause.
     */
    public String getErrorHeader() {

        return errorHeader;
    }

}
