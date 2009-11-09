package com.lyndir.lhunath.deblock.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.util.JSONBuilder;

import com.lyndir.lhunath.deblock.data.PlayerEntity;
import com.lyndir.lhunath.deblock.data.ScoreEntity;
import com.lyndir.lhunath.deblock.data.util.EMF;
import com.lyndir.lhunath.deblock.error.AuthenticationException;
import com.lyndir.lhunath.deblock.service.PlayerService;
import com.lyndir.lhunath.deblock.service.ScoreService;
import com.lyndir.lhunath.deblock.util.CheckUtil;
import com.lyndir.lhunath.deblock.util.DeblockConstants;
import com.lyndir.lhunath.lib.system.Utils;
import com.lyndir.lhunath.lib.system.logging.Logger;


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


    /**
     * GET requests are used to retrieve the current scores.
     * 
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType( "text/plain;charset=UTF-8" );

        try {
            // Look up and write out the current scores.
            writeScores( response );

            // Finished successfully.
            EMF.closeEm( true );
            logger.dbg( "Service completed successfully." );
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType( "text/plain;charset=UTF-8" );

        try {
            // Read request parameters.
            String name = request.getParameter( "name" );
            String pass = request.getParameter( "pass" );
            String date_ = request.getParameter( "date" );
            String score_ = request.getParameter( "score" );
            String check = request.getParameter( "check" );
            logger.dbg( "Servicing: name=%s, pass=%s, date=%s, score=%s, check=%s", //
                        name, pass, date_, score_, check );
            Integer score = Utils.parseInt( score_ );
            Long timeStamp = Utils.parseLong( date_ );

            Date date = null;
            if (timeStamp != null)
                date = new Date( timeStamp );

            // Save the score that is being submitted.
            recordScore( name, pass, check, score, date );

            // Finished successfully.
            EMF.closeEm( true );
            logger.dbg( "Service completed successfully." );
        }

        catch (AuthenticationException e) {
            response.addHeader( DeblockConstants.ERROR_HEADER, e.getErrorHeader() );
        }

        finally {
            // If the entity manager is still open the logic was interrupted early and must have failed.
            EMF.closeEm( false );
        }

        // Write out the current scores (result of a normal GET).
        doGet( request, response );
    }

    /**
     * Look up and write the current scores to the given response.
     */
    private void writeScores(HttpServletResponse response)
            throws IOException {

        // Output all known scores.
        JSONBuilder json = new JSONBuilder( response.getWriter() ).object();
        for (PlayerEntity playerEntity : PlayerService.get().getAllPlayers()) {
            ScoreEntity lastScoreEntity = playerEntity.getScores().last();
            json.key( playerEntity.getName() );

            json.object();
            String achievedDate = Float.toString( lastScoreEntity.getAchievedDate().getTime() / 1000.0f );
            json.key( achievedDate );
            json.value( lastScoreEntity.getScore() );
            json.endObject();
        }
        json.endObject();
    }

    /**
     * Record the given score for the given player at the given date after validating authenticity and data sanity.
     */
    private void recordScore(String name, String pass, String check, Integer score, Date date)
            throws AuthenticationException {

        // Validate name and password.
        PlayerEntity playerEntity = PlayerService.get().getPlayer( name, pass );

        // Validate checksum.
        CheckUtil.assertValidChecksum( check, name, score, date.getTime() );

        // Record the new score.
        ScoreEntity newScoreEntity = ScoreService.get().addScore( playerEntity, score, date );
        playerEntity.getScores().add( newScoreEntity );

        // Save player (and scores).
        PlayerService.get().save( playerEntity );
    }
}
