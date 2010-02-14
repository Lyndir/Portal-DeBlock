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

public enum Bot {

    JOHN("John", 4763, 0.2f),
    AERYN("Aeryn", 4961, 0.2f),
    DARGO("D'Argo", 4689, 0.15f),
    ZHAAN("Zhaan", 7386, 0.1f),
    RYGEL("Rygel", 1497, 0.2f),
    CHIANA("Chiana", 2892, 0.3f),
    PILOT("Pilot", 12744, 0.1f),
    CRAIS("Crais", 382, 0.3f),
    SCORPIUS("Scorpius", 3790, 0.1f), ;

    private String name;
    private int    baseScore;
    private float  luck;


    Bot(String name, int baseScore, float luck) {

        if (luck > 1 || luck < 0)
            throw new IllegalArgumentException( String.format(
                    "Bot %s was given a luck value of %d.  Luck must be in the range 0..1.", //
                    name, luck ) );

        this.name = name;
        this.baseScore = baseScore;
        this.luck = luck;
    }

    /**
     * @return The bot's player name.
     */
    public String getName() {

        return name;
    }

    /**
     * @return The bot's initial score.
     */
    public int getBaseScore() {

        return baseScore;
    }

    /**
     * @return The chance the bot has a lucky day.
     */
    public float getLuck() {

        return luck;
    }
}
