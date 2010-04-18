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
package com.lyndir.lhunath.deblock.data;

import static com.lyndir.lhunath.deblock.data.PlayerEntity.findAll;
import static com.lyndir.lhunath.deblock.data.PlayerEntity.findByName;

import javax.persistence.*;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Objects;
import com.lyndir.lhunath.lib.system.logging.Logger;
import com.lyndir.lhunath.lib.system.util.SafeObjects;


/**
 * <h2>{@link PlayerEntity}<br>
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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@NamedQueries({
        @NamedQuery(name = findAll, query = "SELECT p FROM PlayerEntity p"),
        @NamedQuery(name = findByName, query = "SELECT p FROM PlayerEntity p WHERE p.name = :name")})
public class PlayerEntity implements Comparable<PlayerEntity> {

    private static final Logger logger = Logger.get( PlayerEntity.class );

    public static final String findAll = "PlayerEntity.findAll";
    public static final String findByName = "PlayerEntity.findByName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    private String name;
    private String password;

    private Date registered;

    @OneToMany(mappedBy = "player", cascade = {CascadeType.ALL})
    private SortedSet<ScoreEntity> scores;


    public PlayerEntity() {

        setRegistered( new Date() );
        setScores( new TreeSet<ScoreEntity>() );
    }

    public PlayerEntity(final String name) {

        this();

        setName( name );
    }

    public PlayerEntity(final String name, final String password) {

        this();

        setName( name );
        setPassword( password );
    }

    /**
     * @return The id of this {@link PlayerEntity}.
     */
    public Key getId() {

        return id;
    }

    /**
     * @param id The id of this {@link PlayerEntity}.
     */
    public void setId(final Key id) {

        this.id = id;
    }

    /**
     * @return The name of this {@link PlayerEntity}.
     */
    public String getName() {

        return name;
    }

    /**
     * @param name The name of this {@link PlayerEntity}.
     */
    public void setName(final String name) {

        if (name == null || name.isEmpty())
            throw logger.bug( "Player name can't be empty." ).toError( IllegalArgumentException.class );

        this.name = name;
    }

    /**
     * @return The password of this {@link PlayerEntity}.
     */
    public String getPassword() {

        return password;
    }

    /**
     * @param password The password of this {@link PlayerEntity}.
     */
    public void setPassword(final String password) {

        if (password == null || password.isEmpty())
            throw logger.bug( "Password can't be empty." ).toError( IllegalArgumentException.class );

        this.password = password;
    }

    /**
     * @return The registered of this {@link PlayerEntity}.
     */
    public Date getRegistered() {

        return registered;
    }

    /**
     * @param registered The registered of this {@link PlayerEntity}.
     */
    public void setRegistered(final Date registered) {

        this.registered = registered;
    }

    /**
     * {@link ScoreEntity}s are sorted in ascending score value.
     *
     * @return The scores of this {@link PlayerEntity}.
     */
    public SortedSet<ScoreEntity> getScores() {

        return scores;
    }

    /**
     * @param scores The scores of this {@link PlayerEntity}.
     */
    public void setScores(final SortedSet<ScoreEntity> scores) {

        this.scores = scores;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final PlayerEntity o) {

        return registered.compareTo( o.registered );
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == this)
            return true;
        if (obj == null || !getClass().isAssignableFrom( obj.getClass() ))
            return false;

        PlayerEntity o = (PlayerEntity) obj;
        return SafeObjects.equal( name, o.name ) && SafeObjects.equal( password, o.password );
    }

    @Override
    public int hashCode() {

        return Objects.hashCode( name, password );
    }
}
