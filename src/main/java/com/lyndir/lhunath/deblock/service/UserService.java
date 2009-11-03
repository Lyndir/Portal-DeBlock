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

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.lyndir.lhunath.deblock.entity.UserEntity;
import com.lyndir.lhunath.deblock.entity.util.EMF;
import com.lyndir.lhunath.deblock.error.AuthenticationException;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link UserService}<br>
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
public class UserService {

    private static final Logger      logger   = Logger.get( UserService.class );
    private static final UserService instance = new UserService();


    public static UserService get() {

        return instance;
    }

    private UserService() {

    }

    /**
     * @return All registered users.
     */
    public List<UserEntity> getAllUsers() {

        @SuppressWarnings("unchecked")
        List<UserEntity> users = EMF.getEm().createNamedQuery( UserEntity.findAll ).getResultList();

        return users;
    }

    /**
     * Look up the {@link UserEntity} with the given userName. Checks whether the user's password is equal to the given
     * password.
     * 
     * <p>
     * If no {@link UserEntity} exists yet for the given userName, a new {@link UserEntity} is registered with the given
     * password.
     * </p>
     * 
     * @param userName
     *            The name or alias of the user.
     * @param password
     *            The password that guarantees only the user has access to his own profile.
     * 
     * @return The {@link UserEntity} with the given userName.
     * 
     * @throws AuthenticationException
     *             When the given userName or password is <code>null</code> or empty ({@link String#isEmpty()}). Also
     *             when the given password is not equal ({@link #equals(Object)}) to the existing {@link UserEntity}'s
     *             password ( {@link UserEntity#getPassword()}).
     */
    public UserEntity getUser(String userName, String password)
            throws AuthenticationException {

        // Check whether the input is valid.
        if (userName == null || userName.isEmpty() || password == null || password.isEmpty())
            throw logger.err( "Invalid username (%s) or password (%s).", userName, password ) //
            .toError( AuthenticationException.class, userName, password );

        Query userQuery = EMF.getEm().createNamedQuery( UserEntity.findByUserName );
        userQuery.setParameter( "userName", userName );
        UserEntity userEntity = null;
        try {
            userEntity = (UserEntity) userQuery.getSingleResult();
        } catch (NoResultException e) {}

        // Check if the user is already registered. If not, just register him now with the given userName and password.
        if (userEntity == null)
            userEntity = new UserEntity( userName, password );

        // Check whether the registered user's password matches the given password.
        if (!userEntity.getPassword().equals( password ))
            throw logger.err( "Incorrect password (%s) for username (%s).", password, userName ) //
            .toError( AuthenticationException.class, userName, password );

        return userEntity;
    }
}
