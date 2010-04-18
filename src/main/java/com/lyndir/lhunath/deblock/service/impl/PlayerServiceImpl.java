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
package com.lyndir.lhunath.deblock.service.impl;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

import com.lyndir.lhunath.deblock.data.PlayerEntity;
import com.lyndir.lhunath.deblock.data.util.EMF;
import com.lyndir.lhunath.deblock.error.AuthenticationException;
import com.lyndir.lhunath.deblock.service.PlayerService;
import com.lyndir.lhunath.deblock.util.DeblockConstants;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link PlayerServiceImpl}<br>
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
public class PlayerServiceImpl implements PlayerService {

    private static final Logger logger = Logger.get( PlayerServiceImpl.class );


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerEntity> getAllPlayers() {

        @SuppressWarnings("unchecked")
        List<PlayerEntity> players = EMF.getEm().createNamedQuery( PlayerEntity.findAll ).getResultList();

        return players;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerEntity getPlayer(final String name, final String password)
            throws AuthenticationException {

        // Check whether the input is valid.
        if (name == null || name.isEmpty())
            throw logger.wrn( "Name not set." ) //
                    .toError( AuthenticationException.class, DeblockConstants.ERROR_MISSING_NAME, name, password );
        if (password == null || password.isEmpty())
            throw logger.wrn( "Password not set for player %s.", name ) //
                    .toError( AuthenticationException.class, DeblockConstants.ERROR_MISSING_PASS, name, password );

        Query playerQuery = EMF.getEm().createNamedQuery( PlayerEntity.findByName );
        playerQuery.setParameter( "name", name );
        PlayerEntity playerEntity = null;
        try {
            playerEntity = (PlayerEntity) playerQuery.getSingleResult();
        }
        catch (NoResultException ignored) {
        }

        // Check if the player is already registered. If not, just register him now with the given name and password.
        if (playerEntity == null)
            playerEntity = new PlayerEntity( name, password );

        // Check whether the registered player's password matches the given password.
        if (!playerEntity.getPassword().equals( password ))
            throw logger.wrn( "Incorrect password (%s) for name (%s).", password, name ) //
                    .toError( AuthenticationException.class, DeblockConstants.ERROR_INCORRECT_PASS, name, password );

        return playerEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final PlayerEntity playerEntity) {

        EntityTransaction transaction = EMF.getEm().getTransaction();
        try {
            transaction.begin();
            EMF.getEm().persist( playerEntity );
            EMF.getEm().flush();
            transaction.commit();
        }

        finally {
            if (transaction.isActive())
                transaction.rollback();
        }
    }
}
