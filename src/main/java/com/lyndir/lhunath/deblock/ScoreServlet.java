package com.lyndir.lhunath.deblock;

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
import com.lyndir.lhunath.lib.system.Utils;
import com.lyndir.lhunath.lib.system.logging.Logger;


/**
 * <h2>{@link ScoreServlet}<br>
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
public class ScoreServlet extends HttpServlet {

    private static final Logger logger = Logger.get( ScoreServlet.class );


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType( "text/plain;charset=UTF-8" );

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
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType( "text/plain;charset=UTF-8" );

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

            recordScore( name, pass, check, score, date );
            writeScores( response );

            // Finished successfully.
            EMF.closeEm( true );
            logger.dbg( "Service completed successfully." );
        }

        catch (Throwable e) {
            response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
            throw logger.err( e, "Service failed: caught %s", e ).toError();
        }

        finally {
            // If the entity manager is still open the logic was interrupted early and must have failed.
            EMF.closeEm( false );
        }
    }

    private void recordScore(String name, String pass, String check, Integer score, Date date)
            throws AuthenticationException {

        // Validate checksum.
        CheckUtil.assertValidChecksum( name, score, check );

        // Record the new score.
        PlayerEntity playerEntity = PlayerService.get().getPlayer( name, pass );
        ScoreEntity newScoreEntity = ScoreService.get().addScore( playerEntity, score, date );
        playerEntity.getScores().add( newScoreEntity );

        // Save player (and scores).
        PlayerService.get().save( playerEntity );
    }

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
}
