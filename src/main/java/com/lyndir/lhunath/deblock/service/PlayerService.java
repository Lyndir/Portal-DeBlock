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
package com.lyndir.lhunath.deblock.service;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.lyndir.lhunath.deblock.entity.BotEntity;
import com.lyndir.lhunath.deblock.entity.PlayerEntity;
import com.lyndir.lhunath.deblock.entity.util.EMF;
import com.lyndir.lhunath.deblock.error.AuthenticationException;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link PlayerService}<br>
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
public class PlayerService {

    private static final Logger        logger   = Logger.get( PlayerService.class );
    private static final PlayerService instance = new PlayerService();


    public static PlayerService get() {

        return instance;
    }

    private PlayerService() {

    }

    /**
     * @return All registered players.
     */
    public List<PlayerEntity> getAllPlayers() {

        @SuppressWarnings("unchecked")
        List<PlayerEntity> players = EMF.getEm().createNamedQuery( PlayerEntity.findAll ).getResultList();

        return players;
    }

    /**
     * Look up the {@link PlayerEntity} with the given name. Checks whether the player's password is equal to the given
     * password.
     * 
     * <p>
     * If no {@link PlayerEntity} exists yet for the given name, a new {@link PlayerEntity} is registered with the given
     * password.
     * </p>
     * 
     * @param name
     *            The name or alias of the player.
     * @param password
     *            The password that guarantees only the player has access to his own profile.
     * 
     * @return The {@link PlayerEntity} with the given name.
     * 
     * @throws AuthenticationException
     *             When the given name or password is <code>null</code> or empty ({@link String#isEmpty()}). Also when
     *             the given password is not equal ({@link #equals(Object)}) to the existing {@link PlayerEntity}'s
     *             password ( {@link PlayerEntity#getPassword()}).
     */
    public PlayerEntity getPlayer(String name, String password)
            throws AuthenticationException {

        // Check whether the input is valid.
        if (name == null || name.isEmpty() || password == null || password.isEmpty())
            throw logger.err( "Invalid name (%s) or password (%s).", name, password ) //
            .toError( AuthenticationException.class, name, password );

        Query playerQuery = EMF.getEm().createNamedQuery( PlayerEntity.findByName );
        playerQuery.setParameter( "name", name );
        PlayerEntity playerEntity = null;
        try {
            playerEntity = (PlayerEntity) playerQuery.getSingleResult();
        } catch (NoResultException e) {}

        // Check if the player is already registered. If not, just register him now with the given name and password.
        if (playerEntity == null)
            playerEntity = new PlayerEntity( name, password );

        // Check whether the registered player's password matches the given password.
        if (!playerEntity.getPassword().equals( password ))
            throw logger.err( "Incorrect password (%s) for name (%s).", password, name ) //
            .toError( AuthenticationException.class, name, password );

        return playerEntity;
    }

    /**
     * Look up the {@link BotEntity} with the given name.
     * 
     * <p>
     * If no {@link BotEntity} exists yet for the given name, a new {@link BotEntity} is registered.
     * </p>
     * 
     * @param name
     *            The name or alias of the bot.
     * 
     * @return The {@link BotEntity} with the given name.
     */
    public BotEntity getBot(String name) {

        Query botQuery = EMF.getEm().createNamedQuery( PlayerEntity.findByName );
        botQuery.setParameter( "name", name );
        BotEntity botEntity = null;
        try {
            botEntity = (BotEntity) botQuery.getSingleResult();
        } catch (NoResultException e) {}

        // Check if the player is already registered. If not, just register him now with the given name and password.
        if (botEntity == null)
            botEntity = new BotEntity( name );

        return botEntity;
    }

    /**
     * Persist the given player and record his updated data (such as scores).
     * 
     * @param playerEntity
     *            The player whose updated data needs to be saved.
     * 
     * @return <code>true</code> if the player's data was successfully saved.<br>
     *         <code>false</code> if something happened made it impossible to save the player's data.
     */
    public boolean save(PlayerEntity playerEntity) {

        EntityTransaction transaction = EMF.getEm().getTransaction();
        try {
            transaction.begin();
            logger.inf( "Transaction started" );
            EMF.getEm().persist( playerEntity );
            EMF.getEm().flush();
            transaction.commit();
            logger.inf( "Transaction committed" );
        }

        catch (Throwable e) {
            logger.err( e, "unexpected" );
        }

        finally {
            if (transaction.isActive()) {
                transaction.rollback();
                logger.inf( "Transaction rolled back" );
                return false;
            }
        }

        logger.inf( "saved successfully" );
        return true;
    }
}
