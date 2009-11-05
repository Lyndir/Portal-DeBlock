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

import java.text.MessageFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;
import com.lyndir.lhunath.lib.system.logging.Logger;


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
    private Key                 id;

    private int                 score;
    private Date                achievedDate;

    @ManyToOne(cascade = { CascadeType.ALL })
    private PlayerEntity        player;


    public ScoreEntity() {

    }

    public ScoreEntity(PlayerEntity player) {

        this();

        this.player = player;
    }

    public ScoreEntity(PlayerEntity player, int score, Date achievedDate) {

        this( player );

        if (achievedDate == null)
            throw logger.bug( "Date when score was achieved can't be unset." ).toError( IllegalArgumentException.class );

        this.score = score;
        this.achievedDate = achievedDate;
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
     * @param score
     *            The score of this {@link ScoreEntity}.
     */
    public void setScore(int score) {

        this.score = score;
    }

    /**
     * @return The achievedDate of this {@link ScoreEntity}.
     */
    public Date getAchievedDate() {

        return achievedDate;
    }

    /**
     * @param achievedDate
     *            The achievedDate of this {@link ScoreEntity}.
     */
    public void setAchievedDate(Date achievedDate) {

        this.achievedDate = achievedDate;
    }

    /**
     * @return The id of this {@link ScoreEntity}.
     */
    public Key getId() {

        return id;
    }

    /**
     * @param id
     *            The id of this {@link ScoreEntity}.
     */
    public void setId(Key id) {

        this.id = id;
    }

    /**
     * {@link ScoreEntity}s are sorted according to the date they were achieved.
     * 
     * @see #getAchievedDate()
     */
    public int compareTo(ScoreEntity o) {

        return score - o.score;
    }
}
