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

import java.util.List;

import com.lyndir.lhunath.deblock.data.PlayerEntity;
import com.lyndir.lhunath.deblock.error.AuthenticationException;


/**
 * <h2>{@link PlayerService}<br>
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
public interface PlayerService {

    /**
     * @return All registered players.
     */
    List<PlayerEntity> getAllPlayers();

    /**
     * Look up the {@link PlayerEntity} with the given name. Checks whether the player's password is equal to the given
     * password.
     *
     * <p>
     * If no {@link PlayerEntity} exists yet for the given name, a new {@link PlayerEntity} is registered with the given
     * password.
     * </p>
     *
     * @param name     The name or alias of the player.
     * @param password The password that guarantees only the player has access to his own profile.
     *
     * @return The {@link PlayerEntity} with the given name.
     *
     * @throws AuthenticationException When the given name or password is <code>null</code> or empty ({@link String#isEmpty()}). Also when
     *                                 the given password is not equal ({@link #equals(Object)}) to the existing {@link PlayerEntity}'s
     *                                 password ( {@link PlayerEntity#getPassword()}).
     */
    PlayerEntity getPlayer(String name, String password)
            throws AuthenticationException;

    /**
     * Persist the given player and record his updated data (such as scores).
     *
     * @param playerEntity The player whose updated data needs to be saved.
     */
    void save(PlayerEntity playerEntity);
}
