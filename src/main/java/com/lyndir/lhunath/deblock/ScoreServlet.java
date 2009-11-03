package com.lyndir.lhunath.deblock;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.util.JSONBuilder;

import com.lyndir.lhunath.deblock.entity.ScoreEntity;
import com.lyndir.lhunath.deblock.entity.UserEntity;
import com.lyndir.lhunath.deblock.entity.util.EMF;
import com.lyndir.lhunath.deblock.service.ScoreService;
import com.lyndir.lhunath.deblock.service.UserService;
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


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

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
                UserEntity userEntity = UserService.get().getUser( name, pass );
                userEntity.getScores().add( newScoreEntity );

                // Save user (and scores).
                EMF.getEm().getTransaction().begin();
                EMF.getEm().persist( userEntity );
                EMF.getEm().flush();
                EMF.getEm().getTransaction().commit();
            }

            // Output all known scores.
            JSONBuilder json = new JSONBuilder( response.getWriter() ).object();
            for (UserEntity userEntity : UserService.get().getAllUsers())
                json.key( userEntity.getUserName() ).value( userEntity.getScores().last().getScore() );
            json.endObject();

            // Finished successfully.
            EMF.closeEm( true );
            logger.dbg( "Service completed successfully." );
        }

        catch (Throwable e) {
            response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString() );
            throw logger.err( e, "Service failed: caught %s", e ).toError();
        }

        finally {
            // If the entity manager is still open the logic was interrupted early and must have failed.
            EMF.closeEm( false );
        }
    }
}
