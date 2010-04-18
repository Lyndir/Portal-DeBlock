package com.lyndir.lhunath.deblock.webapp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import com.google.inject.Inject;
import com.lyndir.lhunath.deblock.data.GameMode;
import com.lyndir.lhunath.deblock.data.PlayerEntity;
import com.lyndir.lhunath.deblock.data.ScoreEntity;
import com.lyndir.lhunath.deblock.data.util.EMF;
import com.lyndir.lhunath.deblock.error.AuthenticationException;
import com.lyndir.lhunath.deblock.service.PlayerService;
import com.lyndir.lhunath.deblock.service.ScoreService;
import com.lyndir.lhunath.deblock.util.CheckUtil;
import com.lyndir.lhunath.deblock.util.DeblockConstants;
import com.lyndir.lhunath.lib.system.logging.Logger;
import com.lyndir.lhunath.lib.system.util.Utils;
import net.sf.json.util.JSONBuilder;


/**
 * <h2>{@link ScoreServlet}<br>
 * <sub>A servlet for submitting and retrieving player scores.</sub></h2>
 *
 * <p>
 * <i>Oct 25, 2009</i>
 * </p>
 *
 * @author lhunath
 */
public class ScoreServlet extends HttpServlet {

    private static final Logger logger = Logger.get( ScoreServlet.class );

    private final PlayerService playerService;
    private final ScoreService scoreService;


    @Inject
    public ScoreServlet(final PlayerService playerService, final ScoreService scoreService) {

        this.playerService = playerService;
        this.scoreService = scoreService;
    }

    /**
     * GET requests are used to retrieve the current scores.
     *
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType( "text/plain;charset=UTF-8" );

        try {
            // Read request parameters.
            String name = request.getParameter( "name" );
            String pass = request.getParameter( "pass" );
            logger.dbg( "Servicing: name=%s, pass=%s", //
                        name, pass );

            // Look up and write out the current scores.
            writeScores( name, pass, response );

            // Finished successfully.
            EMF.closeEm( true );
        }

        catch (AuthenticationException e) {
            response.addHeader( DeblockConstants.ERROR_HEADER, e.getErrorHeader() );
        }
        catch (Throwable e) {
            logger.bug( e );
        }

        finally {
            // If the entity manager is still open the logic was interrupted early and must have failed.
            EMF.closeEm( false );
        }
    }

    /**
     * POST requests are used to submit new scores.
     *
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType( "text/plain;charset=UTF-8" );

        try {
            // Read request parameters.
            String name = request.getParameter( "name" );
            String pass = request.getParameter( "pass" );
            String mode__ = request.getParameter( "mode" );
            String level_ = request.getParameter( "level" );
            String score_ = request.getParameter( "score" );
            String date_ = request.getParameter( "date" );
            String check = request.getParameter( "check" );
            logger.dbg( "Servicing: name=%s, pass=%s, mode=%s, level=%s, score=%s, date=%s, check=%s", //
                        name, pass, mode__, level_, score_, date_, check );
            Integer mode_ = Utils.parseInt( mode__ );
            GameMode mode = null;
            if (mode_ != null && mode_ >= 0 && mode_ < GameMode.values().length)
                mode = GameMode.values()[mode_];
            Integer level = Utils.parseInt( level_ );
            Integer score = Utils.parseInt( score_ );
            Long timeStamp = Utils.parseLong( date_ );

            Date date = null;
            if (timeStamp != null)
                date = new Date( timeStamp );

            // Save the score that is being submitted.
            recordScore( name, pass, check, mode, level, score, date );

            // Finished successfully.
            EMF.closeEm( true );
        }

        catch (AuthenticationException e) {
            response.addHeader( DeblockConstants.ERROR_HEADER, e.getErrorHeader() );
        }
        catch (Throwable e) {
            logger.bug( e );
        }

        finally {
            // If the entity manager is still open the logic was interrupted early and must have failed.
            EMF.closeEm( false );

            // Write out the current scores (result of a normal GET).
            doGet( request, response );
        }
    }

    /**
     * Look up and write the current scores to the given response.
     *
     * @param name     The userName of the player who's requesting the scores.
     * @param pass     The password the player who's requesting the scores is authenticating himself with.
     * @param response Where to write the resulting scores to.
     *
     * @throws IOException             Writing to the response failed.
     * @throws AuthenticationException When the pass is not valid for the name.
     */
    private void writeScores(final String name, final String pass, final HttpServletResponse response)
            throws IOException, AuthenticationException {

        // Output all known scores.
        JSONBuilder json = new JSONBuilder( response.getWriter() ).object();
        if (name != null) {
            // Only put scores in response when we know the player in whose context to display the scores.
            PlayerEntity playerEntity = playerService.getPlayer( name, pass );

            for (final ScoreEntity scoreEntity : scoreService.getScoresForPlayer( playerEntity )) {

                json.key( scoreEntity.getPlayer().getName() );
                json.object();

                json.key( "m" );
                json.value( scoreEntity.getMode() );

                json.key( "l" );
                json.value( scoreEntity.getLevel() );

                json.key( "s" );
                json.value( scoreEntity.getScore() );

                json.key( "d" );
                String achievedDate = Float.toString( scoreEntity.getAchievedDate().getTime() / 1000.0f );
                json.value( achievedDate );

                json.endObject();
            }
        }
        json.endObject();
    }

    /**
     * Record the given score for the given player at the given date after validating authenticity and data sanity.
     *
     * @param name  The userName of the player who's requesting the scores.
     * @param pass  The password the player who's requesting the scores is authenticating himself with.
     * @param check The checksum of the request.
     * @param mode  The game mode in which the given score was achieved.
     * @param level The game level in which the given score was achieved.
     * @param score The score that was achieved.
     * @param date  The date at which the given score was achieved.
     *
     * @throws AuthenticationException When the pass is not valid for the name.
     */
    private void recordScore(
            final String name, final String pass, final String check, final GameMode mode,
            final Integer level, final Integer score, final Date date)
            throws AuthenticationException {

        // Validate name and password.
        PlayerEntity playerEntity = playerService.getPlayer( name, pass );

        // Validate checksum.
        CheckUtil.assertValidChecksum( check, name, mode, level, score, date.getTime() );

        // Record the new score.
        ScoreEntity newScoreEntity = scoreService.addScore( playerEntity, mode, level, score, date );
        playerEntity.getScores().add( newScoreEntity );

        // Save player (and scores).
        playerService.save( playerEntity );
    }
}
