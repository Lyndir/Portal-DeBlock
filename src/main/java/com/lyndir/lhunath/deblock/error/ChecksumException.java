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

    private String  checksum;
    private Integer score;


    /**
     * Create a new {@link ChecksumException} instance.
     * 
     * @param message
     *            The problem description.
     * @param cause
     *            The exception that caused this problem. (optional)
     * @param name
     *            The name of the player that was trying to authenticate.
     * @param score
     *            The score that the player had achieved.
     * @param checksum
     *            The checksum that the player's client submitted for the data.
     */
    public ChecksumException(String message, Throwable cause, String name, Integer score, String checksum) {

        super( message, cause, name, null );

        this.score = score;
        this.checksum = checksum;
    }

    /**
     * @return The checksum of this {@link ChecksumException}.
     */
    public String getChecksum() {

        return checksum;
    }

    /**
     * @return The score of this {@link ChecksumException}.
     */
    public Integer getScore() {

        return score;
    }
}
