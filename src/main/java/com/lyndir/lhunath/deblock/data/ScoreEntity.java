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

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.Objects;
import com.lyndir.lhunath.lib.system.logging.Logger;
import com.lyndir.lhunath.lib.system.util.SafeObjects;


/**
 * <h2>{@link ScoreEntity}<br>
 * <sub>[in short] (TODO).</sub></h2>
 *
 * <p>
 * [description / usage].
 * </p>
 *
 * <p>
 * <i>Oct 25, 2009</i>
 * </p>
 *
 * @author lhunath
 */
@Entity
public class ScoreEntity implements Comparable<ScoreEntity> {

    private static final Logger logger = Logger.get( ScoreEntity.class );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    private GameMode mode;
    private int level;
    private int score;
    private Date achievedDate;

    @ManyToOne(cascade = {CascadeType.ALL})
    private PlayerEntity player;


    public ScoreEntity() {

    }

    public ScoreEntity(final PlayerEntity player) {

        this();

        this.player = player;
    }

    public ScoreEntity(
            final PlayerEntity player, final GameMode mode, final int level, final int score,
            final Date achievedDate) {

        this( player );

        if (achievedDate == null)
            throw logger.bug( "Date when score was achieved can't be unset." )
                    .toError( IllegalArgumentException.class );

        setMode( mode );
        setLevel( level );
        setScore( score );
        setAchievedDate( achievedDate );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        return MessageFormat.format( "[Score: {0}]", score );
    }

    /**
     * @return The player of this {@link ScoreEntity}.
     */
    public PlayerEntity getPlayer() {

        return player;
    }

    /**
     * @return The score of this {@link ScoreEntity}.
     */
    public int getScore() {

        return score;
    }

    /**
     * @param score The score of this {@link ScoreEntity}.
     */
    public void setScore(final int score) {

        this.score = score;
    }

    /**
     * @return The achievedDate of this {@link ScoreEntity}.
     */
    public Date getAchievedDate() {

        return achievedDate;
    }

    /**
     * @param achievedDate The achievedDate of this {@link ScoreEntity}.
     */
    public void setAchievedDate(final Date achievedDate) {

        this.achievedDate = achievedDate;
    }

    /**
     * @return The game mode in which the score was achieved.
     */
    public GameMode getMode() {

        return mode;
    }

    /**
     * @param mode The game mode in which the score was achieved.
     */
    public void setMode(final GameMode mode) {

        this.mode = mode;
    }

    /**
     * @return The level in which the score was achieved.
     */
    public int getLevel() {

        return level;
    }

    /**
     * @param level The level in which the score was achieved.
     */
    public void setLevel(final int level) {

        this.level = level;
    }

    /**
     * @return The id of this {@link ScoreEntity}.
     */
    public Key getId() {

        return id;
    }

    /**
     * @param id The id of this {@link ScoreEntity}.
     */
    public void setId(final Key id) {

        this.id = id;
    }

    /**
     * {@link ScoreEntity}s are sorted according to the date they were achieved.
     *
     * @see #getAchievedDate()
     */
    @Override
    public int compareTo(final ScoreEntity o) {

        return score - o.score;
    }

    @Override
    public int hashCode() {

        return Objects.hashCode( level, score, player );
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == this)
            return true;
        if (obj == null || !getClass().isAssignableFrom( obj.getClass() ))
            return false;

        ScoreEntity o = (ScoreEntity) obj;
        return SafeObjects.equal( level, o.level ) && SafeObjects.equal( score, o.score ) && SafeObjects
                .equal( player, o.player );
    }
}
