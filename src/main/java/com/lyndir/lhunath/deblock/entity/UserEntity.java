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
package com.lyndir.lhunath.deblock.entity;

import static com.lyndir.lhunath.deblock.entity.UserEntity.findAll;
import static com.lyndir.lhunath.deblock.entity.UserEntity.findByUserName;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.appengine.api.datastore.Key;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link UserEntity}<br>
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
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "userName" }) })
@NamedQueries( { @NamedQuery(name = findAll, query = "SELECT u FROM UserEntity u"),
                @NamedQuery(name = findByUserName, query = "SELECT u FROM UserEntity u WHERE u.userName = :userName") })
public class UserEntity implements Comparable<UserEntity> {

    private static final Logger    logger         = Logger.get( UserEntity.class );

    public static final String     findAll        = "UserEntity.findAll";
    public static final String     findByUserName = "UserEntity.findByUserName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key                    id;

    private String                 userName;
    private String                 password;

    private Date                   registered;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE })
    private SortedSet<ScoreEntity> scores;


    public UserEntity() {

    }

    public UserEntity(String userName, String password) {

        if (userName == null || userName.isEmpty())
            throw logger.bug( "Username can't be empty." ).toError( IllegalArgumentException.class );
        if (password == null || password.isEmpty())
            throw logger.bug( "Password can't be empty." ).toError( IllegalArgumentException.class );

        this.userName = userName;
        this.password = password;

        registered = new Date();
        scores = new TreeSet<ScoreEntity>();
    }

    /**
     * @return The id of this {@link UserEntity}.
     */
    public Key getId() {

        return id;
    }

    /**
     * @param id
     *            The id of this {@link UserEntity}.
     */
    public void setId(Key id) {

        this.id = id;
    }

    /**
     * @return The userName of this {@link UserEntity}.
     */
    public String getUserName() {

        return userName;
    }

    /**
     * @param userName
     *            The userName of this {@link UserEntity}.
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * @return The password of this {@link UserEntity}.
     */
    public String getPassword() {

        return password;
    }

    /**
     * @param password
     *            The password of this {@link UserEntity}.
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * @return The registered of this {@link UserEntity}.
     */
    public Date getRegistered() {

        return registered;
    }

    /**
     * @param registered
     *            The registered of this {@link UserEntity}.
     */
    public void setRegistered(Date registered) {

        this.registered = registered;
    }

    /**
     * @return The scores of this {@link UserEntity}.
     */
    public SortedSet<ScoreEntity> getScores() {

        return scores;
    }

    /**
     * @param scores
     *            The scores of this {@link UserEntity}.
     */
    public void setScores(SortedSet<ScoreEntity> scores) {

        this.scores = scores;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(UserEntity o) {

        return registered.compareTo( o.registered );
    }
}
