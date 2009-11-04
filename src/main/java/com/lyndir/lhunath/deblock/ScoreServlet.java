package com.lyndir.lhunath.deblock;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.util.JSONBuilder;

import com.lyndir.lhunath.deblock.entity.PlayerEntity;
import com.lyndir.lhunath.deblock.entity.ScoreEntity;
import com.lyndir.lhunath.deblock.entity.util.EMF;
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
            String check = request.getParameter( "check" );
            Integer score = Utils.parseInt( request.getParameter( "score" ) );
            Long timeStamp = Utils.parseLong( request.getParameter( "date" ) );
            logger.dbg( "Servicing: name=%s, pass=%s, check=%s, score=%s, timeStamp=%s", name, pass, check, score,
                        timeStamp );

            Date date = null;
            if (timeStamp != null)
                date = new Date( timeStamp );

            recordScore( response, name, pass, check, score, date );
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

    private void recordScore(HttpServletResponse response, String name, String pass, String check, Integer score,
                             Date date)
            throws IOException, AuthenticationException {

        // See if this request is posting a new score.
        if (name != null && name.length() > 0 && score != null && score > 0) {

            // Validate checksum.
            if (check == null || check.length() == 0) {
                logger.inf( "Service failed: missing checksum." );
                response.sendError( HttpServletResponse.SC_BAD_REQUEST, "Checksum missing." );
                return;
            }
            if (!CheckUtil.check( name, score, check )) {
                logger.inf( "Service failed: invalid checksum: %s.", check );
                response.sendError( HttpServletResponse.SC_BAD_REQUEST, "Checksum invalid." );
                return;
            }

            // Record the new score.
            ScoreEntity newScoreEntity = ScoreService.get().addScore( score, date );
            PlayerEntity playerEntity = PlayerService.get().getPlayer( name, pass );
            playerEntity.getScores().add( newScoreEntity );

            // Save player (and scores).
            PlayerService.get().save( playerEntity );
        }
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
